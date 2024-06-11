import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.AbstractNode;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.rhino.Node;

public class UnreachableCodeEliminationTest {

 @Test
 public void testUnreachableCodeElimination() {
 UnreachableCodeElimination unreachableCodeElimination =
 new UnreachableCodeElimination(mock(AbstractCompiler.class), false);

 // Test with a simple return statement and an unreachable statement
 Node rootNode = Node.newBlock(1, null);
 Node returnNode = Node.newReturn(null, Node.INT_LITERAL(42));
 Node unreachableNode = Node.newExpressionStatement(Node.IDENT("x"));
 Node blockNode = Node.newBlock(0, null);
 blockNode.addChild(returnNode);
 blockNode.addChild(unreachableNode);
 rootNode.addChild(blockNode);

 NodeTraversal nodeTraversal = mock(NodeTraversal.class);
 when(nodeTraversal.getScope()).thenReturn(new Scope(null, rootNode));
 when(nodeTraversal.getRootNode()).thenReturn(rootNode);

 unreachableCodeElimination.process(null, rootNode);
 NodeTraversal.traverse(unreachableCodeElimination, rootNode, nodeTraversal);

 // Check that the unreachable statement has been removed
 assertEquals(0, rootNode.getChildCount());
 assertEquals(1, returnNode.getChildCount());

 // Test with a break statement and an unreachable statement
 rootNode = Node.newBlock(1, null);
 Node ifStatement = Node.newIfStatement(Node.NEW_EXPR(null), null, null);
 Node statement = Node.newBlock(0, null);
 Node breakNode = Node.newBreakStatement();
 statement.addChild(unreachableNode);
 ifStatement.addChild(statement);
 rootNode.addChild(ifStatement);
 rootNode.addChild(Node.newExpressionStatement(Node.IDENT("x").setPos(0, 0)));

 unreachableCodeElimination = new UnreachableCodeElimination(mock(AbstractCompiler.class), false);
 unreachableCodeElimination.process(null, rootNode);
 NodeTraversal.traverse(unreachableCodeElimination, rootNode, nodeTraversal);

 // Check that the unreachable statement has been removed and the break statement has been processed
 assertEquals(1, rootNode.getChildCount());
 assertEquals(1, ifStatement.getChildCount());
 assertEquals(0, ifStatement.getChild(0).getChildCount());
 assertEquals(0, breakNode.getChildCount());
 }

 @Test
 public void testRemoveNoOpStatements() {
 UnreachableCodeElimination unreachableCodeElimination =
 new UnreachableCodeElimination(mock(AbstractCompiler.class), true);

 // Test with a simple return statement and an unreachable statement
 Node rootNode = Node.newBlock(1, null);
 Node returnNode = Node.newReturn(null, Node.INT_LITERAL(42));
 Node unreachableNode = Node.newExpressionStatement(Node.IDENT("x"));
 Node blockNode = Node.newBlock(0, null);
 blockNode.addChild(returnNode);
 blockNode.addChild(unreachableNode);
 rootNode.addChild(blockNode);

 NodeTraversal nodeTraversal = mock(NodeTraversal.class);
 when(nodeTraversal.getScope()).thenReturn(new Scope(null, rootNode));
 when(nodeTraversal.getRootNode()).thenReturn(rootNode);

 unreachableCodeElimination.process(null, rootNode);
 NodeTraversal.traverse(unreachableCodeElimination, rootNode, nodeTraversal);

 // Check that the unreachable statement has been removed and the return statement remains
 assertEquals(1, rootNode.getChildCount());
 assertEquals(1, returnNode.getChildCount());

 // Test with an unreachable statement that has no side effects
 rootNode = Node.newBlock(1, null);
 Node statement = Node.newBlock(0, null);
 statement.addChild(Node.NEW_EXPR(Node.IDENT("MyClass").setPos(0, 0)));
 statement.addChild(Node.NEW_EXPR(Node.IDENT("MyClass").setPos(0, 0)));
 rootNode.addChild(statement);
 rootNode.addChild(Node.newExpressionStatement(Node.IDENT("x").setPos(0, 0)));

 unreachableCodeElimination.process(null, rootNode);
 NodeTraversal.traverse(unreachableCodeElimination, rootNode, nodeTraversal);

 // Check that the unreachable statement has been removed and the statements with no side effects remain
 assertEquals(1, rootNode.getChildCount());
 assertEquals(1, statement.getChildCount());
 }
}