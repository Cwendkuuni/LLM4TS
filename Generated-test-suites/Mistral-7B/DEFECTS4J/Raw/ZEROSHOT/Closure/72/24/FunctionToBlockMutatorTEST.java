import static org.junit.Assert.*;
import static com.google.javascript.jscomp.FunctionToBlockMutator.*;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.rhino.Node;

import org.junit.Before;
import org.junit.Test;

import java.util.LinkedHashMap;
import java.util.Map;

public class FunctionToBlockMutatorTest {

  private FunctionToBlockMutator functionToBlockMutator;
  private AbstractCompiler compiler;

  @Before
  public void setUp() {
    CompilerOptions options = new CompilerOptions(true);
    compiler = new AbstractCompiler() {
      @Override
      public CompilerPass getProcessor(JavaScriptContext context, CompilerOptions options) {
        return null;
      }
    };
    functionToBlockMutator = new FunctionToBlockMutator(compiler, () -> "test_id");
  }

  @Test
  public void testMutateFunctionWithNoArguments() {
    String functionName = "testFunction";
    Node functionNode = NodeUtil.newFunctionNode(functionName, null, null, null, null, null, null);
    Node callNode = NodeUtil.newCallNode(NodeUtil.newIdentifier("testFunction"), null);

    Node result = functionToBlockMutator.mutate(
        functionName, functionNode, callNode, null, false, false);

    // The mutated function should have the same structure as the original one.
    assertEquals(functionNode.toSource(), result.toSource());
  }

  @Test
  public void testMutateFunctionWithArguments() {
    String functionName = "testFunction";
    String argName1 = "arg1";
    String argName2 = "arg2";
    Node functionNode = NodeUtil.newFunctionNode(functionName, null, null, null, null, null, null);
    Node argNode1 = NodeUtil.newVarNode(argName1, null);
    Node argNode2 = NodeUtil.newVarNode(argName2, null);
    Node callNode = NodeUtil.newCallNode(NodeUtil.newIdentifier(functionName), List.of(argNode1, argNode2));

    LinkedHashMap<String, Node> argMap = new LinkedHashMap<>();
    argMap.put(argName1, argNode1);
    argMap.put(argName2, argNode2);

    Node result = functionToBlockMutator.mutate(
        functionName, functionNode, callNode, null, false, false);

    // The mutated function should have the same structure as the original one,
    // but with inlined arguments and unique local names.
    assertEquals(functionNode.toSource(), result.toSource());
  }

  @Test
  public void testReplaceReturns() {
    String functionName = "testFunction";
    Node functionNode = NodeUtil.newFunctionNode(functionName, null, null, null, null, null, null);
    Node callNode = NodeUtil.newCallNode(NodeUtil.newIdentifier(functionName), null);

    // Test a function with a single return statement.
    Node singleReturnNode = NodeUtil.newReturnNode(NodeUtil.newNumber(1));
    Node singleReturnFunctionNode = NodeUtil.newFunctionNode(functionName, null, null, singleReturnNode, null, null, null);
    Node singleReturnResult = replaceReturns(singleReturnFunctionNode, null, null, false);

    // The result of replaceReturns should be a simple statement block.
    assertEquals(NodeUtil.newBlockNode(singleReturnNode), singleReturnResult);

    // Test a function with multiple return statements.
    Node multipleReturnNode = NodeUtil.newBlockNode(
        NodeUtil.newReturnNode(NodeUtil.newNumber(1)),
        NodeUtil.newReturnNode(NodeUtil.newNumber(2)));
    Node multipleReturnFunctionNode = NodeUtil.newFunctionNode(functionName, null, null, multipleReturnNode, null, null, null);
    Node multipleReturnResult = replaceReturns(multipleReturnFunctionNode, null, null, false);

    // The result of replaceReturns should be a block with breaks.
    Node expectedMultipleReturnBlock = NodeUtil.newBlockNode(
        NodeUtil.newLabelNode("testFunction"),
        NodeUtil.newAssignStatementNode("result", NodeUtil.newUndefinedNode()),
        NodeUtil.newReturnNode(NodeUtil.newIdentifier("result")),
        NodeUtil.newBreakNode("testFunction"));
    assertEquals(expectedMultipleReturnBlock, multipleReturnResult);

    // Test a function with a return statement and a statement following it.
    Node statementNode = NodeUtil.newStatementNode(NodeUtil.newPrintStatementNode(NodeUtil.newString("Hello, world!")));
    Node statementFunctionNode = NodeUtil.newFunctionNode(functionName, null, null, null, multipleReturnNode, statementNode, null);
    Node statementResult = replaceReturns(statementFunctionNode, null, null, false);

    // The result of replaceReturns should be a block with breaks.
    Node expectedStatementBlock = NodeUtil.newBlockNode(
        NodeUtil.newLabelNode("testFunction"),
        NodeUtil.newAssignStatementNode("result", NodeUtil.newUndefinedNode()),
        NodeUtil.newReturnNode(NodeUtil.newIdentifier("result")),
        NodeUtil.newBreakNode("testFunction"),
        statementNode);
    assertEquals(expectedStatementBlock, statementResult);

    // Test a function with a result that needs to be set explicitly.
    Node resultNode = NodeUtil.newAssignStatementNode("result", NodeUtil.newNumber(1));
    Node resultFunctionNode = NodeUtil.newFunctionNode(functionName, null, null, null, null, resultNode, null);
    Node resultResult = replaceReturns(resultFunctionNode, "result", null, true);

    // The result of replaceReturns should be a block with breaks.
    Node expectedResultBlock = NodeUtil.newBlockNode(
        NodeUtil.newLabelNode("testFunction"),
        NodeUtil.newAssignStatementNode("result", NodeUtil.newNumber(1)),
        NodeUtil.newBreakNode("testFunction"));
    assertEquals(expectedResultBlock, resultResult);
  }
}