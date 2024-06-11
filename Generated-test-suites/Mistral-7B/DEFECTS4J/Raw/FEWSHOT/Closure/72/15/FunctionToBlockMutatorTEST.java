package org.apache.commons.cli.jscomp; // Assuming the test class is in the same package as FunctionToBlockMutator

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkState;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import com.google.common.collect.LinkedHashMap;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.jscomp.Supplier;

import java.util.List;
import java.util.Map;
import java.util.Set;

public class FunctionToBlockMutatorTest {

  private FunctionToBlockMutator mutator;

  @SuppressWarnings("unchecked")
  @Before
  public void setUp() {
    AbstractCompiler compiler = new NoOpCompiler();
    Supplier<String> safeNameIdSupplier = () -> "test";
    mutator = new FunctionToBlockMutator(compiler, safeNameIdSupplier);
  }

  @Test
  public void testMutate_SimpleFunction() {
    String fnName = "myFunction";
    Node fnNode = NodeUtil.newFunctionNode(Token.FUNCTION, "myFunction",
        Lists.newArrayList("a", "b"), null, null, NodeUtil.newBlockNode(
            Token.BLOCK, NodeUtil.newVarNode("a", NodeUtil.newIntNode(1)),
            NodeUtil.newVarNode("b", NodeUtil.newIntNode(2)),
            NodeUtil.newReturnNode(NodeUtil.newIntNode(3))));
    Node callNode = NodeUtil.newCallNode(Token.CALL, NodeUtil.newMemberNode(
        NodeUtil.newVarNode("obj"), "myFunction"), Lists.newArrayList(
            NodeUtil.newIntNode(4), NodeUtil.newIntNode(5)));
    String resultName = "result";
    boolean needsDefaultResult = false;
    boolean isCallInLoop = false;

    Node expectedResult = NodeUtil.newBlockNode(Token.BLOCK,
        NodeUtil.newVarNode("a", NodeUtil.newIntNode(4)),
        NodeUtil.newVarNode("b", NodeUtil.newIntNode(5)),
        NodeUtil.newAssignStatementNode("result", NodeUtil.newIntNode(3)),
        NodeUtil.newReturnNode(NodeUtil.newVarNode("result")));

    Node actualResult = mutator.mutate(fnName, fnNode, callNode, resultName,
        needsDefaultResult, isCallInLoop);

    checkState(actualResult != null, "Expected non-null result");
    assertEquals(expectedResult, actualResult);
  }

  @Test
  public void testMutate_FunctionWithArgsAliased() {
    String fnName = "myFunction";
    Node fnNode = NodeUtil.newFunctionNode(Token.FUNCTION, "myFunction",
        Lists.newArrayList("a", "b"), null, null, NodeUtil.newBlockNode(
            Token.BLOCK, NodeUtil.newVarNode("a", NodeUtil.newIntNode(1)),
            NodeUtil.newVarNode("b", NodeUtil.newIntNode(2)),
            NodeUtil.newReturnNode(NodeUtil.newIntNode(3))));
    Node callNode = NodeUtil.newCallNode(Token.CALL, NodeUtil.newMemberNode(
        NodeUtil.newVarNode("obj"), "myFunction"), Lists.newArrayList(
            NodeUtil.newIntNode(4), NodeUtil.newIntNode(5)));
    String resultName = "result";
    boolean needsDefaultResult = false;
    boolean isCallInLoop = false;

    Set<String> namesToAlias = Sets.newHashSet("a");
    LinkedHashMap<String, Node> argMap = Maps.newLinkedHashMap();
    argMap.put("a", NodeUtil.newVarNode("aAlias", NodeUtil.newIntNode(4)));
    argMap.put("b", NodeUtil.newVarNode("b", NodeUtil.newIntNode(5)));

    Node expectedResult = NodeUtil.newBlockNode(Token.BLOCK,
        NodeUtil.newVarNode("aAlias", NodeUtil.newIntNode(4)),
        NodeUtil.newVarNode("b", NodeUtil.newIntNode(5)),
        NodeUtil.newAssignStatementNode("result", NodeUtil.newIntNode(3)),
        NodeUtil.newReturnNode(NodeUtil.newVarNode("result")));

    Node actualResult = mutator.mutate(fnName, fnNode, callNode, resultName,
        needsDefaultResult, isCallInLoop);

    checkState(actualResult != null, "Expected non-null result");
    assertEquals(expectedResult, actualResult);
  }

  @Test
  public void testMutate_FunctionWithArgsAndDefaultResult() {
    String fnName = "myFunction";
    Node fnNode = NodeUtil.newFunctionNode(Token.FUNCTION, "myFunction",
        Lists.newArrayList("a", "b"), null, null, NodeUtil.newBlockNode(
            Token.BLOCK, NodeUtil.newVarNode("a", NodeUtil.newIntNode(1)),
            NodeUtil.newVarNode("b", NodeUtil.newIntNode(2)),
            NodeUtil.newAssignStatementNode("result", NodeUtil.newIntNode(3)),
            NodeUtil.newReturnNode(NodeUtil.newVarNode("result"))));
    Node callNode = NodeUtil.newCallNode(Token.CALL, NodeUtil.newMemberNode(
        NodeUtil.newVarNode("obj"), "myFunction"), Lists.newArrayList());
    String resultName = "result";
    boolean needsDefaultResult = true;
    boolean isCallInLoop = false;

    Node expectedResult = NodeUtil.newBlockNode(Token.BLOCK,
        NodeUtil.newVarNode("a", NodeUtil.newIntNode(0)), // default value
        NodeUtil.newVarNode("b", NodeUtil.newIntNode(0)), // default value
        NodeUtil.newAssignStatementNode("result", NodeUtil.newIntNode(3)),
        NodeUtil.newReturnNode(NodeUtil.newVarNode("result")));

    Node actualResult = mutator.mutate(fnName, fnNode, callNode, resultName,
        needsDefaultResult, isCallInLoop);

    checkState(actualResult != null, "Expected non-null result");
    assertEquals(expectedResult, actualResult);
  }

  @Test
  public void testMutate_FunctionInLoop() {
    String fnName = "myFunction";
    Node fnNode = NodeUtil.newFunctionNode(Token.FUNCTION, "myFunction",
        Lists.newArrayList("a"), null, null, NodeUtil.newBlockNode(
            Token.BLOCK, NodeUtil.newVarNode("a", NodeUtil.newIntNode(1)),
            NodeUtil.newAssignStatementNode("a", NodeUtil.newIntNode(2)),
            NodeUtil.newReturnNode(NodeUtil.newIntNode(3))));
    Node callNode = NodeUtil.newCallNode(Token.CALL, NodeUtil.newMemberNode(
        NodeUtil.newVarNode("obj"), "myFunction"), Lists.newArrayList());
    String resultName = "result";
    boolean needsDefaultResult = false;
    boolean isCallInLoop = true;

    Node expectedResult = NodeUtil.newBlockNode(Token.BLOCK,
        NodeUtil.newVarNode("a", NodeUtil.newIntNode(1)),
        NodeUtil.newAssignStatementNode("a", NodeUtil.newIntNode(2)),
        NodeUtil.newReturnNode(NodeUtil.newIntNode(3)));

    Node actualResult = mutator.mutate(fnName, fnNode, callNode, resultName,
        needsDefaultResult, isCallInLoop);

    checkState(actualResult != null, "Expected non-null result");
    assertEquals(expectedResult, actualResult);
  }

  @Test
  public void testReplaceReturns() {
    Node block = NodeUtil.newBlockNode(Token.BLOCK,
        NodeUtil.newVarNode("a", NodeUtil.newIntNode(1)),
        NodeUtil.newReturnNode(NodeUtil.newIntNode(3)));
    String resultName = "result";
    String labelName = "myLabel";
    boolean needsDefaultResult = false;

    Node expectedResult = NodeUtil.newBlockNode(Token.BLOCK,
        NodeUtil.newVarNode("a", NodeUtil.newIntNode(1)),
        NodeUtil.newLabelNode(labelName),
        NodeUtil.newAssignStatementNode("result", NodeUtil.newVarNode("a")),
        NodeUtil.newBreakNode(labelName));

    Node actualResult = mutator.replaceReturns(block, resultName, labelName,
        needsDefaultResult);

    checkState(actualResult != null, "Expected non-null result");
    assertEquals(expectedResult, actualResult);
  }

  @Test
  public void testReplaceReturns_WithDefaultResult() {
    Node block = NodeUtil.newBlockNode(Token.BLOCK,
        NodeUtil.newVarNode("a", NodeUtil.newIntNode(1)),
        NodeUtil.newReturnNode(NodeUtil.newIntNode(3)));
    String resultName = "result";
    String labelName = "myLabel";
    boolean needsDefaultResult = true;

    Node expectedResult = NodeUtil.newBlockNode(Token.BLOCK,
        NodeUtil.newVarNode("a", NodeUtil.newIntNode(1)),
        NodeUtil.newLabelNode(labelName),
        NodeUtil.newAssignStatementNode("result", NodeUtil.newIntNode(3)),
        NodeUtil.newAssignStatementNode("result", NodeUtil.newIntNode(0)),
        NodeUtil.newBreakNode(labelName));

    Node actualResult = mutator.replaceReturns(block, resultName, labelName,
        needsDefaultResult);

    checkState(actualResult != null, "Expected non-null result");
    assertEquals(expectedResult, actualResult);
  }

  @Test
  public void testReplaceReturns_NoReturnStatement() {
    Node block = NodeUtil.newBlockNode(Token.BLOCK,
        NodeUtil.newVarNode("a", NodeUtil.newIntNode(1)));
    String resultName = "result";
    String labelName = "myLabel";
    boolean needsDefaultResult = false;

    Node actualResult = mutator.replaceReturns(block, resultName, labelName,
        needsDefaultResult);

    assertNull(actualResult);
  }

  @Test
  public void testReplaceReturns_FunctionWithoutBody() {
    Node block = NodeUtil.newFunctionNode(Token.FUNCTION, "myFunction",
        Lists.newArrayList(), null, null, null);
    String resultName = "result";
    String labelName = "myLabel";
    boolean needsDefaultResult = false;

    Node actualResult = mutator.replaceReturns(block, resultName, labelName,
        needsDefaultResult);

    assertNull(actualResult);
  }

  @Test
  public void testReplaceReturns_EmptyFunctionBody() {
    Node block = NodeUtil.newFunctionNode(Token.FUNCTION, "myFunction",
        Lists.newArrayList(), null, null, NodeUtil.newBlockNode(
            Token.BLOCK, NodeUtil.newEmptyStatementNode()));
    String resultName = "result";
    String labelName = "myLabel";
    boolean needsDefaultResult = false;

    Node expectedResult = NodeUtil.newBlockNode(Token.BLOCK,
        NodeUtil.newLabelNode(labelName),
        NodeUtil.newAssignStatementNode("result", NodeUtil.newIntNode(0)),
        NodeUtil.newBreakNode(labelName));

    Node actualResult = mutator.replaceReturns(block, resultName, labelName,
        needsDefaultResult);

    checkState(actualResult != null, "Expected non-null result");
    assertEquals(expectedResult, actualResult);
  }

  private static class NoOpCompiler implements AbstractCompiler {}
}