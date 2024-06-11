import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import com.google.common.collect.LinkedHashMap;
import com.google.common.collect.Maps;
import com.google.javascript.jscomp.FunctionToBlockMutator;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.jscomp.Supplier;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

public class FunctionToBlockMutatorTest {

  private AbstractCompiler compiler;
  private Supplier<String> safeNameIdSupplier;

  @org.junit.Before
  public void setUp() {
    this.compiler = new AbstractCompiler() {};
    this.safeNameIdSupplier = () -> "testId";
  }

  @org.junit.Test
  public void testMutateFunctionWithNoArguments() {
    String fnName = "testFunction";
    Node fnNode = Node.newFunction(Token.FUNCTION, null, null, null, null);
    Node callNode = Node.newCall(Token.CALL, fnNode, null);
    String resultName = "result";
    boolean needsDefaultResult = false;
    boolean isCallInLoop = false;

    FunctionToBlockMutator mutator = new FunctionToBlockMutator(compiler, safeNameIdSupplier);
    Node result = mutator.mutate(fnName, fnNode, callNode, resultName, needsDefaultResult, isCallInLoop);

    assertSame(fnNode, result);
  }

  @org.junit.Test
  public void testMutateFunctionWithArguments() {
    String fnName = "testFunction";
    Node fnNode = Node.newFunction(Token.FUNCTION, null, null, null, null);
    Node callNode = Node.newCall(Token.CALL, fnNode, null);
    String resultName = "result";
    boolean needsDefaultResult = false;
    boolean isCallInLoop = false;

    LinkedHashMap<String, Node> argMap = new LinkedHashMap<>();
    argMap.put("arg1", Node.newString(Token.IDENTIFIER, "arg1"));
    argMap.put("arg2", Node.newString(Token.IDENTIFIER, "arg2"));

    FunctionToBlockMutator mutator = new FunctionToBlockMutator(compiler, safeNameIdSupplier);
    Node result = mutator.mutate(fnName, fnNode, callNode, resultName, needsDefaultResult, isCallInLoop);

    assertNull(result);
  }

  @org.junit.Test
  public void testMutateFunctionWithArgumentsAndInlineArguments() {
    String fnName = "testFunction";
    Node fnNode = Node.newFunction(Token.FUNCTION, null, null, null, null);
    Node callNode = Node.newCall(Token.CALL, fnNode, null);
    String resultName = "result";
    boolean needsDefaultResult = false;
    boolean isCallInLoop = false;

    LinkedHashMap<String, Node> argMap = new LinkedHashMap<>();
    argMap.put("arg1", Node.newNumber(Token.NUMBER, 1));
    argMap.put("arg2", Node.newNumber(Token.NUMBER, 2));

    FunctionToBlockMutator mutator = new FunctionToBlockMutator(compiler, safeNameIdSupplier);
    Node result = mutator.mutate(fnName, fnNode, callNode, resultName, needsDefaultResult, isCallInLoop);

    // The result should be a cloned and mutated version of the function node.
    assertSame(fnNode.getFirstChild(), result.getFirstChild());
    assertSame(fnNode.getLastChild(), result.getLastChild());
  }

  @org.junit.Test
  public void testReplaceReturns() {
    Node block = Node.newBlock(Token.BLOCK, null);
    Node returnNode = Node.newReturn(Token.RETURN, Node.newNumber(Token.NUMBER, 42));
    block.addChildToBack(returnNode);

    String resultName = "result";
    String labelName = "testLabel";
    boolean needsDefaultResult = false;

    Node result = FunctionToBlockMutator.replaceReturns(block, resultName, labelName, needsDefaultResult);

    // The block should be modified to contain a break statement followed by
    // the return value if resultName is null, or an assignment followed by a break statement.
    if (resultName == null) {
      Node breakNode = Node.newBreak(Token.BREAK, Node.newString(Token.LABEL_NAME, labelName));
      Node resultNode = NodeUtil.newExpr(returnNode.getFirstChild());
      Node expected = new Node(Token.BLOCK, null);
      expected.addChildrenToBack(breakNode);
      expected.addChildrenToBack(resultNode);
      assertSame(expected, result);
    } else {
      Node assignNode = createAssignStatementNode(resultName, returnNode.getFirstChild());
      Node breakNode = Node.newBreak(Token.BREAK, Node.newString(Token.LABEL_NAME, labelName));
      Node expected = new Node(Token.BLOCK, null);
      expected.addChildrenToBack(assignNode);
      expected.addChildrenToBack(breakNode);
      assertSame(expected, result);
    }
  }

  private Node createAssignStatementNode(String name, Node expression) {
    Node nameNode = Node.newString(Token.NAME, name);
    Node assign = new Node(Token.ASSIGN, nameNode, expression);
    return NodeUtil.newExpr(assign);
  }
}