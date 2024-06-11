package com.google.javascript.jscomp;

import com.google.common.base.FunctionToBlockMutator;
import com.google.common.base.Supplier;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.rhino.Node;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class FunctionToBlockMutatorTest {

 private FunctionToBlockMutator functionToBlockMutator;
 private Supplier<String> safeNameIdSupplier;

 @Before
 public void setUp() {
 safeNameIdSupplier = () -> "test";
 functionToBlockMutator = new FunctionToBlockMutator(
 new TestCompiler(), safeNameIdSupplier);
 }

 @Test
 public void testMutateFunctionWithArguments() {
 // Given a function with arguments
 Node fnNode = NodeUtil.newFunctionNode("testFunc", new Node[0], null);
 Node callNode = NodeUtil.newCallNode("testFunc", ImmutableMap.of("a", NodeUtil.newNumberLiteral(1), "b", NodeUtil.newNumberLiteral(2)));

 // When mutating the function
 Node mutatedBlock = functionToBlockMutator.mutate(
 "testFunc", fnNode, callNode, "result", false, false);

 // Then the mutated block should contain the arguments aliased and inlined
 assertNotNull(mutatedBlock);
 EquivalenceChecker.assertEquivalent(
 NodeUtil.newFunctionBody(fnNode), mutatedBlock);
 }

 @Test
 public void testMutateFunctionWithoutArguments() {
 // Given a function without arguments
 Node fnNode = NodeUtil.newFunctionNode("testFunc", new Node[0], null);
 Node callNode = NodeUtil.newCallNode("testFunc");

 // When mutating the function
 Node mutatedBlock = functionToBlockMutator.mutate(
 "testFunc", fnNode, callNode, "result", false, false);

 // Then the mutated block should be an empty block
 assertNotNull(mutatedBlock);
 assertEquals(NodeUtil.newBlockNode(new Node[0]), mutatedBlock);
 }

 @Test
 public void testMutateFunctionWithCallInLoop() {
 // Given a function called in a loop
 Node fnNode = NodeUtil.newFunctionNode("testFunc", new Node[0], null);
 Node loop = NodeUtil.newForLoopNode(
 NodeUtil.newVarNode("i", NodeUtil.newNumberLiteral(0)),
 NodeUtil.newLessThanOrEqualNode(NodeUtil.newVarNode("i"), NodeUtil.newNumberLiteral(10)),
 NodeUtil.newIncrementNode(NodeUtil.newVarNode("i")),
 NodeUtil.newBlockNode(
 new Node[] {
 NodeUtil.newCallNode("testFunc", ImmutableMap.of("i", NodeUtil.newVarNode("i"))),
 NodeUtil.newReturnNode(NodeUtil.newNumberLiteral(i))
 }));

 // When mutating the function
 Node mutatedBlock = functionToBlockMutator.mutate(
 "testFunc", fnNode, null, "result", false, true);

 // Then the mutated block should be injected into the loop
 assertNotNull(mutatedBlock);
 Node injectedBlock = NodeUtil.getFunctionBody(mutatedBlock);
 assertEquals(
 NodeUtil.newBlockNode(new Node[] {
 NodeUtil.newVar("i", NodeUtil.newNumberLiteral(0)),
 NodeUtil.newAssignNode(NodeUtil.newVar("i"), NodeUtil.newNumberLiteral(0)),
 injectedBlock,
 NodeUtil.newReturnNode(NodeUtil.newVar("result"))
 }), loop);
 }

 @Test
 public void testReplaceReturns() {
 // Given a function returning a value
 Node fnNode = NodeUtil.newFunctionNode("testFunc", new Node[0], null);
 Node block = NodeUtil.newBlockNode(
 new Node[] {
 NodeUtil.newReturnNode(NodeUtil.newNumberLiteral(1)),
 NodeUtil.newReturnNode(NodeUtil.newStringLiteral("foo"))
 });
 Node callNode = NodeUtil.newCallNode("testFunc");
 String labelName = "foo";
 String resultName = "result";

 // When replacing returns with breaks and assigning to result
 Node mutatedBlock = functionToBlockMutator.mutate(
 "testFunc", fnNode, callNode, resultName, true, false);

 // Then the mutated block should contain breaks and an assignment to result
 assertNotNull(mutatedBlock);
 Node newBlock = NodeUtil.getFunctionBody(mutatedBlock);
 Node label = NodeUtil.newLabelNode(labelName);
 Node assign = NodeUtil.newAssignNode(NodeUtil.newVar(resultName), NodeUtil.newUndefinedNode());
 Node statement = NodeUtil.newStatementNode(NodeUtil.newBreakNode(label));
 Node statements = NodeUtil.newBlockNode(
 new Node[] {assign, statement}
 );
 EquivalenceChecker.assertEquivalent(newBlock, statements);
 }

 @Test
 public void testReplaceReturnsWithEmptyFunction() {
 // Given a function returning void
 Node fnNode = NodeUtil.newFunctionNode("testFunc", new Node[0], null);
 Node block = NodeUtil.newBlockNode(new Node[0]);
 Node callNode = NodeUtil.newCallNode("testFunc");
 String resultName = "result";
 String labelName = "foo";

 // When replacing returns with breaks and assigning to result
 Node mutatedBlock = functionToBlockMutator.mutate(
 "testFunc", fnNode, callNode, resultName, true, false);

 // Then the mutated block should contain breaks
 assertNotNull(mutatedBlock);
 Node newBlock = NodeUtil.getFunctionBody(mutatedBlock);
 Node label = NodeUtil.newLabelNode(labelName);
 Node statement = NodeUtil.newStatementNode(NodeUtil.newBreakNode(label));
 EquivalenceChecker.assertEquivalent(newBlock, statement);
 }
}