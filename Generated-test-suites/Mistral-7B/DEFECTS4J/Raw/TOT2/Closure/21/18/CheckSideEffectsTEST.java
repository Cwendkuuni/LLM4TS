import static org.junit.Assert.*;
import static com.google.javascript.jscomp.CheckLevel.*;
import static com.google.javascript.rhino.Token.*;

import com.google.common.collect.Lists;
import com.google.javascript.jscomp.CheckSideEffects;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.rhino.IR;
import com.google.javascript.rhino.JSDocInfo;
import com.google.javascript.rhino.NodeFactory;

import java.util.List;

public class CheckSideEffectsTest {

  @org.junit.Test
  public void testCheckSideEffects() {
    CheckSideEffects checkSideEffects = new CheckSideEffects(null, WARNING, false);
    Compiler compiler = new Compiler();

    String sourceCode = "function main() {\n" +
        "  var s = \"this string is \"; // JSDoc comment\n" +
        "  var x = 1;\n" +
        "  x = foo(); // check for assignment instead of comparison\n" +
        "  foo();; // check for stray semicolon\n" +
        "  for (var i = 0; i < 10; i++) {\n" +
        "    x = i;\n" +
        "  }\n" +
        "  var el = document.getElementById(\"myId\");\n" +
        "  el.offsetWidth;\n" +
        "}\n" +
        "function foo() {\n" +
        "  return 2;\n" +
        "}";

    Node node = NodeFactory.parse(sourceCode);
    Node root = node.getFirstChild();

    NodeTraversal.traverse(compiler, root, checkSideEffects);

    List<Node> problemNodes = checkSideEffects.getProblemNodes();

    assertEquals(4, problemNodes.size());
    assertTrue(problemNodes.get(0).getJSDocInfo() != null);
    assertEquals(EXPR_RESULT, problemNodes.get(1).getType());
    assertEquals(COMMA, problemNodes.get(2).getType());
    assertEquals(BLOCK, problemNodes.get(3).getType());

    checkSideEffects.protectSideEffects();

    Node protectedRoot = node.getFirstChild();
    Node protectedProblemNode = problemNodes.get(1);
    Node expectedProtectorCall = IR.call(IR.name("JSCOMPILER_PRESERVE"));
    Node expectedProtectorCallChild = expectedProtectorCall.getFirstChild();
    Node expectedProtectorCallChildName = IR.name("JSCOMPILER_PRESERVE");

    Node expectedProtectedExpression = IR.call(expectedProtectorCall);
    Node actualProtectedExpression = protectedProblemNode.getLastChild();

    assertEquals(expectedProtectorCallChildName.getJSDocInfo(), actualProtectedExpression.getFirstChild().getJSDocInfo());
    assertEquals(expectedProtectorCall, actualProtectedExpression.getParent());
    assertEquals(expectedProtectedExpression, actualProtectedExpression.getNext().getNext());
  }

  @org.junit.Test
  public void testCheckSideEffectsWithProtection() {
    CheckSideEffects checkSideEffects = new CheckSideEffects(null, WARNING, true);
    Compiler compiler = new Compiler();

    String sourceCode = "function main() {\n" +
        "  var s = \"this string is \"; // JSDoc comment\n" +
        "  var x = 1;\n" +
        "  x = foo(); // check for assignment instead of comparison\n" +
        "  foo();; // check for stray semicolon\n" +
        "  for (var i = 0; i < 10; i++) {\n" +
        "    x = i;\n" +
        "  }\n" +
        "  var el = document.getElementById(\"myId\");\n" +
        "  el.offsetWidth;\n" +
        "}\n" +
        "function foo() {\n" +
        "  return 2;\n" +
        "}";

    Node node = NodeFactory.parse(sourceCode);
    Node root = node.getFirstChild();

    NodeTraversal.traverse(compiler, root, checkSideEffects);

    List<Node> problemNodes = checkSideEffects.getProblemNodes();

    assertEquals(0, problemNodes.size());
  }

  @org.junit.Test
  public void testStripProtection() {
    CheckSideEffects checkSideEffects = new CheckSideEffects(null, WARNING, false);
    Compiler compiler = new Compiler();

    String sourceCode = "function main() {\n" +
        "  JSCOMPILER_PRESERVE(JSCOMPILER_PRESERVE(1 + 1));\n" +
        "}";

    Node node = NodeFactory.parse(sourceCode);
    Node root = node.getFirstChild();

    NodeTraversal.traverse(compiler, root, checkSideEffects);

    CheckSideEffects.StripProtection stripProtection = new CheckSideEffects.StripProtection(compiler);
    NodeTraversal.traverse(compiler, root, stripProtection);

    assertEquals(EXPR_RESULT, root.getType());
    assertEquals(NODE_TYPE, root.getFirstChild().getType());
    assertEquals(NUMBER, root.getFirstChild().getFirstChild().getType());
    assertEquals(NUMBER, root.getFirstChild().getLastChild().getType());
  }
}