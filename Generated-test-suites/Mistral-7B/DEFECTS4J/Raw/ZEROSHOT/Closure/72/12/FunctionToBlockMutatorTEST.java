import static org.junit.Assert.*;
import static com.google.javascript.jscomp.FunctionToBlockMutator.*;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.rhino.Node;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

public class FunctionToBlockMutatorTest {

  private AbstractCompiler compiler;
  private Supplier<String> safeNameIdSupplier;

  @Before
  public void setUp() {
    compiler = new AbstractCompiler();
    safeNameIdSupplier = () -> "test_id";
  }

  @Test
  public void testMutateFunctionWithNoArguments() {
    Node functionNode = Node.newFunctionNode(Token.NONE, null, null, null);
    Node callNode = Node.newCallNode(functionNode, null);
    String resultName = "result";
    Node resultBlock = mutate(null, functionNode, callNode, resultName, false, false);

    assertNotNull(resultBlock);
    assertEquals(NodeUtil.getFunctionBody(functionNode), resultBlock);
  }

  @Test
  public void testMutateFunctionWithOneArgument() {
    String functionName = "testFunction";
    Node functionNode = Node.newFunctionNode(Token.NONE, null, null, null);
    Node argumentNode = Node.newIdentNode("arg");
    Node callNode = Node.newCallNode(functionNode, argumentNode);
    String resultName = "result";
    Node resultBlock = mutate(functionName, functionNode, callNode, resultName, false, false);

    assertNotNull(resultBlock);
    assertNotSame(functionNode, resultBlock);
    assertEquals(NodeUtil.getFunctionBody(functionNode), resultBlock);

    Node argumentMap = NodeUtil.getFunctionCallParameterMap(functionNode, callNode, safeNameIdSupplier);
    assertEquals(1, argumentMap.size());
    assertEquals("arg_test_id", argumentMap.get("arg").getText());
  }

  @Test
  public void testMutateFunctionWithMultipleArguments() {
    String functionName = "testFunction";
    Node functionNode = Node.newFunctionNode(Token.NONE, null, null, null);
    Node argument1Node = Node.newIdentNode("arg1");
    Node argument2Node = Node.newIdentNode("arg2");
    Node callNode = Node.newCallNode(functionNode, argument1Node, argument2Node);
    String resultName = "result";
    Node resultBlock = mutate(functionName, functionNode, callNode, resultName, false, false);

    assertNotNull(resultBlock);
    assertNotSame(functionNode, resultBlock);
    assertEquals(NodeUtil.getFunctionBody(functionNode), resultBlock);

    Map<String, Node> argumentMap = Maps.newLinkedHashMap(
        Maps.newHashMap(NodeUtil.getFunctionCallParameterMap(functionNode, callNode, safeNameIdSupplier).entrySet()));
    assertEquals(2, argumentMap.size());
    assertEquals("arg1_test_id", argumentMap.get("arg1").getText());
    assertEquals("arg2_test_id", argumentMap.get("arg2").getText());
  }

  @Test
  public void testMutateFunctionWithArgumentsAndResult() {
    String functionName = "testFunction";
    Node functionNode = Node.newFunctionNode(Token.NONE, null, null, null);
    Node argument1Node = Node.newIdentNode("arg1");
    Node argument2Node = Node.newIdentNode("arg2");
    Node resultNode = NodeUtil.newIdentNode("result");
    Node callNode = Node.newCallNode(functionNode, argument1Node, argument2Node);
    String resultName = "result";
    Node resultBlock = mutate(functionName, functionNode, callNode, resultName, true, false);

    assertNotNull(resultBlock);
    assertNotSame(functionNode, resultBlock);
    assertEquals(NodeUtil.getFunctionBody(functionNode), resultBlock);

    Map<String, Node> argumentMap = Maps.newLinkedHashMap(
        Maps.newHashMap(NodeUtil.getFunctionCallParameterMap(functionNode, callNode, safeNameIdSupplier).entrySet()));
    assertEquals(2, argumentMap.size());
    assertEquals("arg1_test_id", argumentMap.get("arg1").getText());
    assertEquals("arg2_test_id", argumentMap.get("arg2").getText());

    Node resultNodeInFunction = resultBlock.getFirstChild();
    assertNotNull(resultNodeInFunction);
    assertEquals(resultNode.getText(), resultNodeInFunction.getText());
  }

  @Test
  public void testMutateFunctionWithArgumentsAndResultInLoop() {
    String functionName = "testFunction";
    Node functionNode = Node.newFunctionNode(Token.NONE, null, null, null);
    Node argument1Node = Node.newIdentNode("arg1");
    Node argument2Node = Node.newIdentNode("arg2");
    Node resultNode = NodeUtil.newIdentNode("result");
    Node callNode = Node.newCallNode(functionNode, argument1Node, argument2Node);
    String resultName = "result";
    Node resultBlock = mutate(functionName, functionNode, callNode, resultName, false, true);

    assertNotNull(resultBlock);
    assertNotSame(functionNode, resultBlock);
    assertEquals(NodeUtil.getFunctionBody(functionNode), resultBlock);

    Map<String, Node> argumentMap = Maps.newLinkedHashMap(
        Maps.newHashMap(NodeUtil.getFunctionCallParameterMap(functionNode, callNode, safeNameIdSupplier).entrySet()));
    assertEquals(2, argumentMap.size());
    assertEquals("arg1_test_id", argumentMap.get("arg1").getText());
    assertEquals("arg2_test_id", argumentMap.get("arg2").getText());

    Set<String> modifiedParameters = FunctionArgumentInjector.findModifiedParameters(functionNode);
    assertTrue(modifiedParameters.isEmpty());
  }

  @Test
  public void testAliasingUnsafeNames() {
    String functionName = "testFunction";
    Node functionNode = Node.newFunctionNode(Token.NONE, null, null, null);
    Node argumentNode = Node.newIdentNode("unsafeName");
    Node callNode = Node.newCallNode(functionNode, argumentNode);
    String resultName = "result";
    Node resultBlock = mutate(functionName, functionNode, callNode, resultName, false, false);

    assertNotNull(resultBlock);
    assertNotSame(functionNode, resultBlock);
    assertEquals(NodeUtil.getFunctionBody(functionNode), resultBlock);

    Map<String, Node> argumentMap = Maps.newLinkedHashMap(
        Maps.newHashMap(NodeUtil.getFunctionCallParameterMap(functionNode, callNode, safeNameIdSupplier).entrySet()));
    assertEquals(1, argumentMap.size());
    assertEquals("alias_unsafeName_test_id", argumentMap.get("unsafeName").getText());

    assertEquals(functionName + "_alias", resultBlock.getFirstChild().getText());
  }

  @Test
  public void testReplaceReturnWithBreak() {
    String labelName = "testLabel";
    String resultName = "result";
    Node returnNode = Node.newReturnNode(Node.newIdentNode("returnValue"), null);
    Node functionNode = Node.newFunctionNode(Token.NONE, null, null, returnNode);
    Node blockNode = NodeUtil.newBlockNode(functionNode);

    Node resultBlock = replaceReturns(blockNode, resultName, labelName, false);
    assertEquals(NodeUtil.getFunctionBody(functionNode), resultBlock);

    resultBlock = replaceReturns(blockNode, resultName, labelName, true);
    assertEquals(NodeUtil.getFunctionBody(functionNode), resultBlock);

    resultBlock = replaceReturns(blockNode, null, labelName, false);
    assertEquals(NodeUtil.getFunctionBody(functionNode), resultBlock);

    resultBlock = replaceReturns(blockNode, null, labelName, true);
    assertEquals(NodeUtil.getFunctionBody(functionNode), resultBlock);

    resultBlock = replaceReturns(blockNode, resultName, null, false);
    assertEquals(NodeUtil.getFunctionBody(functionNode), resultBlock);

    resultBlock = replaceReturns(blockNode, resultName, null, true);
    assertEquals(NodeUtil.getFunctionBody(functionNode), resultBlock);
  }

  @Test
  public void testReplaceReturnWithBreakInLoop() {
    String labelName = "testLabel";
    String resultName = "result";
    Node returnNode = Node.newReturnNode(Node.newIdentNode("returnValue"), null);
    Node functionNode = Node.newFunctionNode(Token.NONE, null, null, returnNode);
    Node blockNode = NodeUtil.newBlockNode(functionNode);

    Node resultBlock = replaceReturns(blockNode, resultName, labelName, false);
    assertEquals(NodeUtil.getFunctionBody(functionNode), resultBlock);

    resultBlock = replaceReturns(blockNode, resultName, labelName, true);

    // This test checks that the blockNode is cloned and the resultBlock
    // doesn't contain the original functionNode.
    assertEquals(functionNode.hashCode(), NodeUtil.getFunctionBody(functionNode).hashCode());
    assertNotSame(functionNode, resultBlock.getFirstChild());
  }

  @Test
  public void testFixUnitializedVarDeclarations() {
    Node varNode = NodeUtil.newVarNode("varName", null);
    Node blockNode = NodeUtil.newBlockNode(varNode);

    Node fixedBlockNode = fixUnitializedVarDeclarations(blockNode);
    assertEquals(NodeUtil.getFunctionBody(blockNode), fixedBlockNode);

    Node varNodeWithoutInitialization = Node.newVarNode(Token.NONE, "varName");
    Node blockNodeWithUninitializedVar = NodeUtil.newBlockNode(varNodeWithoutInitialization);

    Node fixedBlockNodeWithUninitializedVar = fixUnitializedVarDeclarations(blockNodeWithUninitializedVar);
    Node fixedVarNodeWithInitialization = fixedBlockNodeWithUninitializedVar.getFirstChild();
    Node initializationNode = fixedVarNodeWithInitialization.getFirstChild();

    assertEquals("undefined", initializationNode.getText());
    assertEquals(NodeUtil.getFunctionBody(blockNodeWithUninitializedVar), fixedBlockNodeWithUninitializedVar);
  }

  @Test
  public void testMakeLocalNamesUnique() {
    String functionName = "testFunction";
    Node functionNode = Node.newFunctionNode(Token.NONE, null, null, null);
    Node functionBodyNode = NodeUtil.getFunctionBody(functionNode);

    Node mutatedFunctionBodyNode = makeLocalNamesUnique(functionBodyNode, false);

    Node clonedFunctionBodyNode = functionBodyNode.cloneTree();
    makeLocalNamesUnique(clonedFunctionBodyNode, false);

    Node identicalNodes = NodeUtil.compareNodes(mutatedFunctionBodyNode, clonedFunctionBodyNode);
    assertTrue(identicalNodes == null);

    functionBodyNode = NodeUtil.newBlockNode(Node.newReturnNode(NodeUtil.newIdentNode("returnValue"), null));

    mutatedFunctionBodyNode = makeLocalNamesUnique(functionBodyNode, true);

    Node clonedFunctionBodyNode2 = functionBodyNode.cloneTree();
    makeLocalNamesUnique(clonedFunctionBodyNode2, true);

    identicalNodes = NodeUtil.compareNodes(mutatedFunctionBodyNode, clonedFunctionBodyNode2);
    assertTrue(identicalNodes != null);
    assertEquals(mutatedFunctionBodyNode.getText(), clonedFunctionBodyNode2.getText());
  }

}