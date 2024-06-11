import static org.junit.Assert.assertTrue;
import static com.google.javascript.jscomp.CheckLevel.WARNING;
import static com.google.javascript.jscomp.Token.*;

import com.google.common.collect.Lists;
import com.google.javascript.jscomp.CheckLevel;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerInput;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.jscomp.JSDocInfoBuilder;
import com.google.javascript.rhino.IR;
import com.google.javascript.rhino.JSDocInfo;
import com.google.javascript.rhino.NodeFactory;

import org.junit.Test;

public class CheckSideEffectsTest {

  @Test
  public void testCheckSideEffects() {
    CompilerOptions options = new CompilerOptions();
    options.setWarningLevel(TokenName.WARNING, CheckLevel.WARNING);

    Compiler compiler = new Compiler();
    CompilerInput input = new CompilerInput(null, "test.js", null);
    String testCode = "function test() {" +
        "var s = \"this string is \";" +
        "var a = 1;" +
        "var b = 2;" +
        "var c = s + \"continued on the next line but you forgot the +\";" +
        "x = foo();" +
        "foo();;" +
        "for (i = 0; i < 10; i++) {" +
        "  if (i % 2 == 0) {" +
        "    break;" +
        "  }" +
        "}" +
        "}";
    Node root = NodeFactory.parse(input, testCode, null);

    CheckSideEffects checkSideEffects = new CheckSideEffects(compiler, WARNING, false);
    NodeTraversal nodeTraversal = new NodeTraversal(compiler, root, checkSideEffects);
    nodeTraversal.traverse();

    assertTrue(checkSideEffects.problemNodes.isEmpty());

    input.setOutputFile("test-out.js");
    compiler.compile(input, null);
  }

  @Test
  public void testCheckSideEffectsWithProblemNodes() {
    CompilerOptions options = new CompilerOptions();
    options.setWarningLevel(TokenName.WARNING, CheckLevel.WARNING);

    Compiler compiler = new Compiler();
    CompilerInput input = new CompilerInput(null, "test.js", null);
    String testCode = "function test() {" +
        "var s = \"this string is \";" +
        "var a = 1;" +
        "var b = 2;" +
        "var c = s + \"continued on the next line but you forgot the +\";" +
        "x = foo();" +
        "foo();;" +
        "for (i = 0; i < 10; i++) {" +
        "  if (i % 2 == 0) {" +
        "    break;" +
        "  }" +
        "}" +
        "var d = Math.random();" +
        "var e = [];" +
        "for (var i = 0; i < 10; i++) {" +
        "  e[i] = i;" +
        "}" +
        "}" +
        "function foo() {" +
        "  return 42;" +
        "}";
    Node root = NodeFactory.parse(input, testCode, null);

    CheckSideEffects checkSideEffects = new CheckSideEffects(compiler, WARNING, false);
    NodeTraversal nodeTraversal = new NodeTraversal(compiler, root, checkSideEffects);
    nodeTraversal.traverse();

    List<Node> problemNodesExpected = Lists.newArrayList(
        NodeUtil.findSingleChildOfType(root, COMMA),
        NodeUtil.findSingleChildOfType(root.getFirstChild(), COMMA),
        NodeUtil.findSingleChildOfType(root.getFirstChild().getNext(), COMMA),
        NodeUtil.findSingleChildOfType(root.getLastChild().getFirstChild(), ASSIGN),
        NodeUtil.findSingleChildOfType(root.getLastChild().getLastChild(), ASSIGN)
    );

    assertTrue(checkSideEffects.problemNodes.size() == problemNodesExpected.size());
    for (int i = 0; i < checkSideEffects.problemNodes.size(); i++) {
      Node problemNode = checkSideEffects.problemNodes.get(i);
      Node expectedProblemNode = problemNodesExpected.get(i);
      assertTrue(NodeUtil.areSame(problemNode, expectedProblemNode));
    }

    input.setOutputFile("test-out.js");
    compiler.compile(input, null);
  }

  @Test(expected = AssertionError.class)
  public void testCheckSideEffectsWithProtectedCode() {
    CompilerOptions options = new CompilerOptions();
    options.setWarningLevel(TokenName.WARNING, CheckLevel.WARNING);

    Compiler compiler = new Compiler();
    CompilerInput input = new CompilerInput(null, "test.js", null);
    String testCode = "function test() {" +
        "function JSCOMPILER_PRESERVE protectCode() {" +
        "  return 42;" +
        "}" +
        "JSCOMPILER_PRESERVE protectCode();" +
        "}" +
        "";
    Node root = NodeFactory.parse(input, testCode, null);

    CheckSideEffects checkSideEffects = new CheckSideEffects(compiler, WARNING, true);
    NodeTraversal nodeTraversal = new NodeTraversal(compiler, root, checkSideEffects);
    nodeTraversal.traverse();

    assertTrue(checkSideEffects.problemNodes.isEmpty());
  }

  @Test
  public void testStripProtection() {
    CompilerOptions options = new CompilerOptions();
    options.setWarningLevel(TokenName.WARNING, CheckLevel.WARNING);

    Compiler compiler = new Compiler();
    CompilerInput input = new CompilerInput(null, "test.js", null);
    String testCode = "function test() {" +
        "function JSCOMPILER_PRESERVE protectCode() {" +
        "  return 42;" +
        "}" +
        "protectCode();" +
        "}" +
        "";
    Node root = NodeFactory.parse(input, testCode, null);

    CheckSideEffects checkSideEffects = new CheckSideEffects(compiler, WARNING, false);
    NodeTraversal nodeTraversal = new NodeTraversal(compiler, root, checkSideEffects);
    nodeTraversal.traverse();

    Node protectionFunction = NodeUtil.findSingleChildOfType(root, NAME);
    Node protectionFunctionCall = NodeUtil.findSingleChildOfType(protectionFunction.getLastChild(), CALL);

    StripProtection stripProtection = new StripProtection(compiler);
    NodeTraversal stripNodeTraversal = new NodeTraversal(compiler, root, stripProtection);
    stripNodeTraversal.traverse();

    Node removedFunctionCall = protectionFunctionCall;
    protectionFunctionCall = NodeUtil.findSingleChildOfType(protectionFunction.getLastChild(), CALL);

    assertTrue(protectionFunctionCall != null);
    assertTrue(NodeUtil.areSame(protectionFunctionCall.getFirstChild(), protectionFunction));

    assertTrue(removedFunctionCall != null);
    assertTrue(removedFunctionCall.isDetached());
  }
}