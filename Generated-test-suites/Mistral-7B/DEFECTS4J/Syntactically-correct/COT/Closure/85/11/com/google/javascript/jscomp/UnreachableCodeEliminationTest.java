package com.google.javascript.jscomp.test;

import static org.junit.Assert.*;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.UnreachableCodeElimination;
import com.google.javascript.rhino.Token;

import java.util.logging.Logger;

public class UnreachableCodeEliminationTest {

 private static final Logger logger = Logger.getLogger(UnreachableCodeEliminationTest.class.getName());
 private Compiler compiler;

 @Before
 public void setUp() {
 compiler = new Compiler();
 }

 @Test
 public void testEmptyFunction() {
 AbstractCompiler abstractCompiler = new Compiler();
 UnreachableCodeElimination elimination = new UnreachableCodeElimination(abstractCompiler, false);

 Node rootNode = Node.newBlock(null);
 NodeTraversal traversal = new NodeTraversal(null, rootNode);
 elimination.process(null, rootNode);
 NodeTraversal.traverse(elimination, rootNode, traversal);

 assertTrue(rootNode.getFirstChild() == null);
 }

 @Test
 public void testReturnStatement() {
 Node rootNode = Node.newBlock(null);
 rootNode.addChild(Node.newReturnStatement(null));
 NodeTraversal traversal = new NodeTraversal(null, rootNode);

 AbstractCompiler abstractCompiler = new Compiler();
 UnreachableCodeElimination elimination = new UnreachableCodeElimination(abstractCompiler, false);
 elimination.process(null, rootNode);
 NodeTraversal.traverse(elimination, rootNode, traversal);

 Node resultNode = rootNode.getFirstChild();
 assertEquals(Node.RETURN_STATEMENT, resultNode.getType());
 assertNull(resultNode.getNext());
 }

 @Test
 public void testRemoveNoOpStatementInFunction() {
 Node rootNode = Node.newBlock(null);
 rootNode.addChild(Node.newExpressionStatement(Node.newName("x"), null));
 rootNode.addChild(Node.newExpressionStatement(Node.newName("y"), null));
 rootNode.addChild(Node.newExpressionStatement(Node.newName("x"), null));
 rootNode.addChild(Node.newReturnStatement(null));
 NodeTraversal traversal = new NodeTraversal(null, rootNode);

 AbstractCompiler abstractCompiler = new Compiler();
 UnreachableCodeElimination elimination = new UnreachableCodeElimination(abstractCompiler, true);
 elimination.process(null, rootNode);
 NodeTraversal.traverse(elimination, rootNode, traversal);

 Node resultNode = rootNode;
 assertEquals(Node.RETURN_STATEMENT, resultNode.getType());
 while (resultNode.getNext() != null) {
 resultNode = resultNode.getNext();
 assertNull(resultNode.getFirstChild());
 }
 }

 @Test
 public void testRemoveEmptyBlock() {
 Node rootNode = Node.newBlock(null);
 rootNode.addChild(Node.newBlock(null));
 NodeTraversal traversal = new NodeTraversal(null, rootNode);

 AbstractCompiler abstractCompiler = new Compiler();
 UnreachableCodeElimination elimination = new UnreachableCodeElimination(abstractCompiler, true);
 elimination.process(null, rootNode);
 NodeTraversal.traverse(elimination, rootNode, traversal);

 Node resultNode = rootNode;
 assertEquals(Node.BLOCK, resultNode.getType());
 assertNull(resultNode.getFirstChild());
 }

 @Test(expected = AssertionError.class)
 public void testRemoveDeadCodeInCycle() {
 Node rootNode = Node.newBlock(null);
 Node a = Node.newName("a", null);
 Node b = Node.newName("b", null);
 rootNode.addChild(Node.newExpressionStatement(a, null));
 rootNode.addChild(Node.newExpressionStatement(b, null));
 rootNode.addChild(Node.newExpressionStatement(a, null));
 rootNode.addChild(Node.newReturnStatement(null));

 NodeTraversal traversal = new NodeTraversal(null, rootNode);
 AbstractCompiler abstractCompiler = new Compiler();
 UnreachableCodeElimination elimination = new UnreachableCodeElimination(abstractCompiler, true);
 elimination.process(null, rootNode);
 NodeTraversal.traverse(elimination, rootNode, traversal);
 }

 @Test
 public void testRemoveDeadCodeInControlFlowGraph() {
 Node rootNode = Node.newBlock(null);
 Node a = Node.newName("a", null);
 Node b = Node.newName("b", null);
 Node c = Node.newName("c", null);
 Node d = Node.newName("d", null);
 Node e = Node.newName("e", null);
 rootNode.addChild(Node.newExpressionStatement(a, null));
 rootNode.addChild(Node.newIfStatement(Node.newBinaryOperator(a, Token.EQ, Node.newNumber(5)),
         Node.newBlock(null), Node.newBlock(null)));
 rootNode.addChild(Node.newExpressionStatement(b, null));
 rootNode.addChild(Node.newExpressionStatement(c, null));
 rootNode.addChild(Node.newExpressionStatement(d, null));
 rootNode.addChild(Node.newExpressionStatement(e, null));
 rootNode.addChild(Node.newReturnStatement(null));
 NodeTraversal traversal = new NodeTraversal(null, rootNode);

 AbstractCompiler abstractCompiler = new Compiler();
 UnreachableCodeElimination elimination = new UnreachableCodeElimination(abstractCompiler, true);
 elimination.process(null, rootNode);
 NodeTraversal.traverse(elimination, rootNode, traversal);

 Node resultNode = rootNode;
 assertEquals(Node.IF_STATEMENT, resultNode.getType());
 resultNode = resultNode.getFirstChild();
 assertEquals(Node.BLOCK, resultNode.getType());
 resultNode = resultNode.getFirstChild();
 assertEquals(Node.RETURN_STATEMENT, resultNode.getType());
 assertNull(resultNode.getNext());
 }

 @Test
 public void testRemoveDeadCodeInSwitchStatement() {
 Node rootNode = Node.newBlock(null);
 Node a = Node.newName("a", null);
 Node b = Node.newName("b", null);
 Node c = Node.newName("c", null);
 Node switchNode = Node.newSwitchStatement(a, null);
 switchNode.setSwitchValue(Node.newBinaryOperator(a, Token.EQ, Node.newNumber(5)));
 switchNode.addCase(Node.newCaseLabel(Node.newNumber(1), null));
 switchNode.addCase(Node.newCaseLabel(Node.newNumber(2), null));
 rootNode.addChild(Node.newExpressionStatement(a, null));
 rootNode.addChild(switchNode);
 rootNode.addChild(Node.newExpressionStatement(b, null));
 rootNode.addChild(Node.newReturnStatement(null));
 NodeTraversal traversal = new NodeTraversal(null, rootNode);

 AbstractCompiler abstractCompiler = new Compiler();
 UnreachableCodeElimination elimination = new UnreachableCodeElimination(abstractCompiler, true);
 elimination.process(null, rootNode);
 NodeTraversal.traverse(elimination, rootNode, traversal);

 Node resultNode = rootNode;
 assertEquals(Node.SWITCH_STATEMENT, resultNode.getType());
 resultNode = resultNode.getFirstChild();
 assertEquals(Node.BLOCK, resultNode.getType());
 resultNode = resultNode.getFirstChild();
 assertEquals(Node.RETURN_STATEMENT, resultNode.getType());
 assertNull(resultNode.getNext());
 }

 @Test
 public void testRemoveDeadCodeInWhileStatement() {
 Node rootNode = Node.newBlock(null);
 Node a = Node.newName("a", null);
 Node b = Node.newName("b", null);
 Node whileNode = Node.newWhileStatement(Node.newBinaryOperator(a, Token.LT, Node.newNumber(5)),
         Node.newBlock(null), null);
 rootNode.addChild(Node.newExpressionStatement(a, null));
 rootNode.addChild(Node.newExpressionStatement(b, null));
 rootNode.addChild(whileNode);
 rootNode.addChild(Node.newExpressionStatement(Node.newName("x", null), null));
 rootNode.addChild(Node.newReturnStatement(null));
 NodeTraversal traversal = new NodeTraversal(null, rootNode);

 AbstractCompiler abstractCompiler = new Compiler();
 UnreachableCodeElimination elimination = new UnreachableCodeElimination(abstractCompiler, true);
 elimination.process(null, rootNode);
 NodeTraversal.traverse(elimination, rootNode, traversal);

 Node resultNode = rootNode;
 assertEquals(Node.WHILE_STATEMENT, resultNode.getType());
 resultNode = resultNode.getFirstChild();
 assertEquals(Node.BLOCK, resultNode.getType());
 resultNode = resultNode.getFirstChild();
 assertNull(resultNode);
 resultNode = resultNode.getNext();
 assertEquals(Node.RETURN_STATEMENT, resultNode.getType());
 assertNull(resultNode.getNext());
 }

 @Test
 public void testRemoveDeadCodeInConditionalExpression() {
 Node rootNode = Node.newBlock(null);
 Node a = Node.newName("a", null);
 Node b = Node.newName("b", null);
 Node c = Node.newName("c", null);
 Node d = Node.newName("d", null);
 rootNode.addChild(Node.newExpressionStatement(a, null));
 rootNode.addChild(Node.newExpressionStatement(b, null));
 rootNode.addChild(Node.newExpressionStatement(c, null));
 rootNode.addChild(Node.newExpressionStatement(Node.newConditionalExpression(a, Node.newName("x", null), Node.newName("y", null)), null));
 rootNode.addChild(Node.newExpressionStatement(d, null));
 rootNode.addChild(Node.newReturnStatement(null));
 NodeTraversal traversal = new NodeTraversal(null, rootNode);

 AbstractCompiler abstractCompiler = new Compiler();
 UnreachableCodeElimination elimination = new UnreachableCodeElimination(abstractCompiler, true);
 elimination.process(null, rootNode);
 NodeTraversal.traverse(elimination, rootNode, traversal);

 Node resultNode = rootNode;
 assertEquals(Node.BLOCK, resultNode.getType());
 resultNode = resultNode.getFirstChild();
 assertEquals(Node.RETURN_STATEMENT, resultNode.getType());
 assertNull(resultNode.getNext());
 }
}