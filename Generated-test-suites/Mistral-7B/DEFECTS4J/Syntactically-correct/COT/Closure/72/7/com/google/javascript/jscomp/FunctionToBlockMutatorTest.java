import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static com.google.javascript.rhino.NodeUtil.isFunction;
import static com.google.javascript.rhino.NodeUtil.isVar;
import static com.google.javascript.rhino.NodeUtil.isStatementBlock;
import static com.google.javascript.rhino.NodeUtil.getNodeTypeReferenceCount;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

import org.junit.Test;

import com.google.javascript.jscomp.FunctionToBlockMutator;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.rhino.Token;

public class FunctionToBlockMutatorTest {

  @Test
  public void testMutateMethod() {
    FunctionToBlockMutator functionToBlockMutator = new FunctionToBlockMutator(
        new NoopCompiler(), new SimpleNameIdSupplier());

    String fnName = "testFunction";
    Node fnNode = NodeUtil.newFunctionNode(Token.FUNCTION, null, null, null,
        null, null, Token.VOID, null, null);
    Node callNode = NodeUtil.newCallNode(fnNode, null, null);
    String resultName = "result";
    boolean needsDefaultResult = false;
    boolean isCallInLoop = false;

    Node newFnNode = functionToBlockMutator.mutate(fnName, fnNode, callNode,
        resultName, needsDefaultResult, isCallInLoop);

    assertNotNull(newFnNode);
    assertNotNull(NodeUtil.getFunctionBody(newFnNode));
  }

  @Test
  public void testMutateMethodWithArguments() {
    FunctionToBlockMutator functionToBlockMutator = new FunctionToBlockMutator(
        new NoopCompiler(), new SimpleNameIdSupplier());

    String fnName = "testFunction";
    Node fnNode = NodeUtil.newFunctionNode(Token.FUNCTION, null, null, null,
        null, null, Token.VOID, null, null);
    Node callNode = NodeUtil.newCallNode(fnNode, null, null);
    String resultName = "result";
    boolean needsDefaultResult = false;
    boolean isCallInLoop = false;

    // Prepare arguments.
    Map<String, Node> argMap = Maps.newLinkedHashMap();
    argMap.put("arg1", NodeUtil.newNumberNode(1));
    argMap.put("arg2", NodeUtil.newStringNode("str"));

    LinkedHashMap<String, Node> argMappedNodes =
        FunctionArgumentInjector.getFunctionCallParameterMap(
            fnNode, callNode, new SimpleNameIdSupplier());
    Set<String> namesToAlias = Sets.newHashSet("arg1");

    Node newFnNode = functionToBlockMutator.mutate(fnName, fnNode, callNode,
        resultName, needsDefaultResult, isCallInLoop);

    // Check that arguments have been inlined.
    Node newBlock = NodeUtil.getFunctionBody(newFnNode);
    assertNotNull(newBlock);
    FunctionArgumentInjector.assertArgumentsInlined(newBlock, argMap);
  }

  @Test
  public void testMutateMethodWithLabelName() {
    FunctionToBlockMutator functionToBlockMutator = new FunctionToBlockMutator(
        new NoopCompiler(), new SimpleNameIdSupplier());

    String fnName = "testFunction";
    Node fnNode = NodeUtil.newFunctionNode(Token.FUNCTION, null, null, null,
        null, null, Token.VOID, null, null);
    Node callNode = NodeUtil.newCallNode(fnNode, null, null);
    String resultName = "result";
    boolean needsDefaultResult = false;
    boolean isCallInLoop = false;
    String labelName = "testLabel";

    Node newFnNode = functionToBlockMutator.mutate(fnName, fnNode, callNode,
        resultName, needsDefaultResult, isCallInLoop);

    assertNotNull(newFnNode);
    assertNull(getLabelNameForFunction(fnName, newFnNode));
  }

  @Test
  public void testReplaceReturnsMethod() {
    FunctionToBlockMutator functionToBlockMutator = new FunctionToBlockMutator(
        new NoopCompiler(), new SimpleNameIdSupplier());

    Node block = NodeUtil.newBlockNode();
    String resultName = "result";

    // Test without result name.
    Node resultNode1 = replaceReturns(block, null, resultName, null);
    assertNull(resultNode1);

    // Test with result name.
    Node resultNode2 = replaceReturns(block, null, resultName, resultName);
    assertNotNull(resultNode2);

    // Test with return statement.
    Node returnNode = NodeUtil.newReturnNode(
        NodeUtil.newNumberNode(1), null);
    Node resultNode3 = replaceReturns(block, returnNode, resultName, resultName);
    assertNotNull(resultNode3);
  }

  /**
   * Verify that a return statement is replaced with a break statement
   * and, if specified, a assignment to a result variable.
   */
  private Node replaceReturns(Node block, Node returnNode, String resultName,
      String labelName) {
    FunctionToBlockMutator functionToBlockMutator = new FunctionToBlockMutator(
        new NoopCompiler(), new SimpleNameIdSupplier());
    return functionToBlockMutator.replaceReturns(block, resultName, labelName,
        returnNode != null);
  }

  /**
   * Verify that label name is added to the block.
   */
  private String getLabelNameForFunction(String fnName, Node node) {
    FunctionToBlockMutator functionToBlockMutator = new FunctionToBlockMutator(
        new NoopCompiler(), new SimpleNameIdSupplier());
    return functionToBlockMutator.getLabelNameForFunction(fnName);
  }

  private static class NoopCompiler implements AbstractCompiler {}

  private static class SimpleNameIdSupplier implements Supplier<String> {
    private int counter = 0;

    @Override
    public String get() {
      return String.valueOf(counter++);
    }
  }
}