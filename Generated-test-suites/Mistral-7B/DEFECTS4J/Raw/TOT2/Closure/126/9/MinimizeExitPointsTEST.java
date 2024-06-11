import static org.junit.Assert.*;
import static com.google.javascript.jscomp.NodeUtil.*;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.MinimizeExitPoints;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import org.junit.Before;
import org.junit.Test;

public class MinimizeExitPointsTest {

 private AbstractCompiler compiler;
 private MinimizeExitPoints minimizer;

 @Before
 public void setUp() {
 compiler = new AbstractCompiler();
 minimizer = new MinimizeExitPoints(compiler);
 }

 @Test
 public void testMinimizeExitPoints_SimpleIfStatement() {
 Node root = new Node(Token.BLOCK, null);
 Node ifNode = new Node(Token.IF, null);
 Node trueBlock = new Node(Token.BLOCK, null);
 Node falseBlock = new Node(Token.BLOCK, null);
 new Node(Token.RETURN, null).setParent(trueBlock);
 new Node(Token.PRINT, null).setParent(falseBlock);
 new Node(Token.IF, null).setParent(ifNode);
 new Node(Token.BLOCK, null).setParent(ifNode);
 new Node(Token.BLOCK, null).addChildren(trueBlock, falseBlock).setParent(root);
 NodeTraversal.traverse(compiler, root, minimizer);
 assertTrue(trueBlock.isEmpty());
 assertFalse(falseBlock.isEmpty());
 assertEquals(1, falseBlock.getFirstChildCount());
 assertEquals(Token.PRINT, falseBlock.getFirstChild(0).getType());
 }

 @Test
 public void testMinimizeExitPoints_IfElseStatement() {
 Node root = new Node(Token.BLOCK, null);
 Node ifNode = new Node(Token.IF, null);
 Node trueBlock = new Node(Token.BLOCK, null);
 Node falseBlock = new Node(Token.BLOCK, null);
 new Node(Token.RETURN, null).setParent(trueBlock);
 new Node(Token.PRINT, null).setParent(falseBlock);
 new Node(Token.IF, null).setParent(ifNode);
 new Node(Token.BLOCK, null).setParent(ifNode);
 new Node(Token.BLOCK, null).setParent(ifNode);
 new Node(Token.BLOCK, null).addChildren(trueBlock, falseBlock).setParent(root);
 NodeTraversal.traverse(compiler, root, minimizer);
 assertTrue(trueBlock.isEmpty());
 assertFalse(falseBlock.isEmpty());
 assertEquals(1, falseBlock.getFirstChildCount());
 assertEquals(Token.PRINT, falseBlock.getFirstChild(0).getType());
 }

 @Test
 public void testMinimizeExitPoints_IfElseIfStatement() {
 Node root = new Node(Token.BLOCK, null);
 Node ifNode1 = new Node(Token.IF, null);
 Node trueBlock1 = new Node(Token.BLOCK, null);
 Node falseBlock1 = new Node(Token.BLOCK, null);
 new Node(Token.RETURN, null).setParent(trueBlock1);
 new Node(Token.IF, null).setParent(ifNode1);
 new Node(Token.BLOCK, null).setParent(ifNode1);
 new Node(Token.BLOCK, null).setParent(ifNode1);
 new Node(Token.ELSE_IF, null).setParent(ifNode1);
 new Node(Token.BLOCK, null).setParent(ifNode1);
 new Node(Token.BLOCK, null).addChildren(trueBlock1, falseBlock1).setParent(root);
 new Node(Token.IF, null).setParent(ifNode1);
 new Node(Token.BLOCK, null).setParent(ifNode1);
 new Node(Token.RETURN, null).setParent(ifNode1.getLastChild());
 new Node(Token.BLOCK, null).addChildren(trueBlock1, falseBlock1).setParent(root);
 NodeTraversal.traverse(compiler, root, minimizer);
 assertTrue(trueBlock1.isEmpty());
 assertFalse(falseBlock1.isEmpty());
 assertEquals(1, falseBlock1.getFirstChildCount());
 assertEquals(Token.RETURN, falseBlock1.getFirstChild(0).getType());
 }

 @Test
 public void testMinimizeExitPoints_LabeledStatement() {
 Node root = new Node(Token.BLOCK, null);
 Node label1 = new Node(Token.LABEL, "label1", null);
 Node ifNode = new Node(Token.IF, null);
 Node trueBlock = new Node(Token.BLOCK, null);
 new Node(Token.RETURN, null).setParent(trueBlock);
 new Node(Token.IF, null).setParent(ifNode);
 new Node(Token.BLOCK, null).setParent(ifNode);
 new Node(Token.BLOCK, null).setParent(ifNode);
 new Node(Token.LABEL, "label2", null).setParent(ifNode);
 new Node(Token.BREAK, null).setParent(ifNode.getLastChild());
 new Node(Token.LABEL, label1.getString(), null).setParent(trueBlock);
 new Node(Token.BLOCK, null).addChildren(ifNode, trueBlock).setParent(root);
 NodeTraversal.traverse(compiler, root, minimizer);
 assertTrue(trueBlock.isEmpty());
 assertFalse(ifNode.getLastChild().isEmpty());
 assertEquals(1, ifNode.getLastChild().getFirstChildCount());
 assertEquals(Token.BREAK, ifNode.getLastChild().getFirstChild(0).getType());
 }

 @Test
 public void testMinimizeExitPoints_SimpleForStatement() {
 Node root = new Node(Token.BLOCK, null);
 Node forNode = new Node(Token.FOR, null);
 Node init = new Node(Token.BLOCK, null);
 new Node(Token.VAR, "i", null).setParent(init);
 new Node(Token.RETURN, null).setParent(init);
 new Node(Token.IF, null).setParent(forNode);
 new Node(Token.BLOCK, null).setParent(forNode);
 new Node(Token.BLOCK, null).setParent(forNode);
 new Node(Token.BLOCK, null).addChildren(init, new Node(Token.BLOCK, null)).setParent(root);
 new Node(Token.WHILE, null).setParent(forNode);
 new Node(Token.BLOCK, null).setParent(forNode);
 NodeTraversal.traverse(compiler, root, minimizer);
 assertTrue(init.isEmpty());
 assertFalse(forNode.getLastChild().getLastChild().isEmpty());
 assertEquals(1, forNode.getLastChild().getLastChild().getFirstChildCount());
 assertEquals(Token.RETURN, forNode.getLastChild().getLastChild().getFirstChild(0).getType());
 }

 @Test
 public void testMinimizeExitPoints_LabeledForStatement() {
 Node root = new Node(Token.BLOCK, null);
 Node label = new Node(Token.LABEL, "label", null);
 Node forNode = new Node(Token.FOR, null);
 Node init = new Node(Token.BLOCK, null);
 new Node(Token.VAR, "i", null).setParent(init);
 new Node(Token.RETURN, null).setParent(init);
 new Node(Token.IF, null).setParent(forNode);
 new Node(Token.BLOCK, null).setParent(forNode);
 new Node(Token.BLOCK, null).setParent(forNode);
 new Node(Token.BLOCK, null).addChildren(init, new Node(Token.BLOCK, null)).setParent(root);
 new Node(Token.WHILE, null).setParent(forNode);
 new Node(Token.BLOCK, null).setParent(forNode);
 new Node(Token.LABEL, label.getString(), null).setParent(init);
 new Node(Token.BREAK, null).setParent(forNode.getLastChild());
 new Node(Token.LABEL, label.getString(), null).setParent(init);
 NodeTraversal.traverse(compiler, root, minimizer);
 assertTrue(init.isEmpty());
 assertFalse(forNode.getLastChild().isEmpty());
 assertEquals(1, forNode.getLastChild().getFirstChildCount());
 assertEquals(Token.BREAK, forNode.getLastChild().getFirstChild(0).getType());
 }

 @Test
 public void testMinimizeExitPoints_EmptyBlock() {
 Node root = new Node(Token.BLOCK, null);
 Node ifNode = new Node(Token.IF, null);
 Node trueBlock = new Node(Token.BLOCK, null);
 new Node(Token.RETURN, null).setParent(trueBlock);
 new Node(Token.BLOCK, null).setParent(ifNode);
 new Node(Token.BLOCK, null).setParent(ifNode);
 new Node(Token.BLOCK, null).addChildren(ifNode, trueBlock).setParent(root);
 NodeTraversal.traverse(compiler, root, minimizer);
 assertTrue(trueBlock.isEmpty());
 assertFalse(ifNode.getLastChild().isEmpty());
 assertEquals(1, ifNode.getLastChild().getFirstChildCount());
 assertEquals(Token.RETURN, ifNode.getLastChild().getFirstChild(0).getType());
 }

 @Test(expected = AssertionError.class)
 public void testMinimizeExitPoints_InvalidStatementType() {
 Node root = new Node(Token.BLOCK, null);
 Node ifNode = new Node(Token.IF, null);
 new Node(Token.RETURN, null).setParent(ifNode);
 new Node(Token.BLOCK, null).setParent(ifNode);
 new Node(Token.BLOCK, null).setParent(ifNode);
 new Node(Token.BLOCK, null).addChildren(ifNode).setParent(root);
 NodeTraversal.traverse(compiler, root, minimizer);
 fail("Expected AssertionError");
 }

 @Test
 public void testMinimizeExitPoints_NestedConditionals() {
 Node root = new Node(Token.BLOCK, null);
 Node ifNode1 = new Node(Token.IF, null);
 Node trueBlock1 = new Node(Token.BLOCK, null);
 new Node(Token.IF, null).setParent(ifNode1);
 new Node(Token.BLOCK, null).setParent(ifNode1);
 new Node(Token.BLOCK, null).setParent(ifNode1);
 new Node(Token.BLOCK, null).addChildren(ifNode1, trueBlock1).setParent(root);
 Node ifNode2 = new Node(Token.IF, null);
 new Node(Token.BLOCK, null).setParent(ifNode2);
 new Node(Token.BLOCK, null).setParent(ifNode2);
 new Node(Token.BLOCK, null).setParent(ifNode2);
 new Node(Token.IF, null).setParent(ifNode2);
 new Node(Token.BLOCK, null).setParent(ifNode2);
 new Node(Token.BLOCK, null).addChildren(ifNode2, trueBlock1).setParent(root);
 NodeTraversal.traverse(compiler, root, minimizer);
 assertTrue(trueBlock1.isEmpty());
 assertFalse(ifNode1.getLastChild().isEmpty());
 assertEquals(1, ifNode1.getLastChild().getFirstChildCount());
 assertEquals(Token.IF, ifNode1.getLastChild().getFirstChild(0).getType());
 assertFalse(ifNode2.getLastChild().isEmpty());
 assertEquals(1, ifNode2.getLastChild().getFirstChildCount());
 assertEquals(Token.IF, ifNode2.getLastChild().getFirstChild(0).getType());
 }

 @Test
 public void testMinimizeExitPoints_NestedLoops() {
 Node root = new Node(Token.BLOCK, null);
 Node forNode1 = new Node(Token.FOR, null);
 Node init1 = new Node(Token.BLOCK, null);
 new Node(Token.VAR, "i1", null).setParent(init1);
 new Node(Token.RETURN, null).setParent(init1);
 new Node(Token.IF, null).setParent(forNode1);
 new Node(Token.BLOCK, null).setParent(forNode1);
 new Node(Token.BLOCK, null).setParent(forNode1);
 new Node(Token.BLOCK, null).addChildren(init1, new Node(Token.BLOCK, null)).setParent(root);
 new Node(Token.WHILE, null).setParent(forNode1);
 new Node(Token.BLOCK, null).setParent(forNode1);
 Node forNode2 = new Node(Token.FOR, null);
 new Node(Token.BLOCK, null).setParent(forNode2);
 new Node(Token.VAR, "i2", null).setParent(init1);
 new Node(Token.RETURN, null).setParent(init1);
 new Node(Token.IF, null).setParent(forNode2);
 new Node(Token.BLOCK, null).setParent(forNode2);
 new Node(Token.BLOCK, null).setParent(forNode2);
 new Node(Token.BLOCK, null).addChildren(init1, new Node(Token.BLOCK, null)).setParent(root);
 new Node(Token.WHILE, null).setParent(forNode2);
 new Node(Token.BLOCK, null).setParent(forNode2);
 NodeTraversal.traverse(compiler, root, minimizer);
 assertTrue(init1.isEmpty());
 assertFalse(init1.getParent().getLastChild().isEmpty());
 assertEquals(1, init1.getParent().getLastChild().getFirstChildCount());
 assertEquals(Token.IF, init1.getParent().getLastChild().getFirstChild(0).getType());
 assertTrue(init1.getFirstChild().isEmpty());
 assertTrue(forNode1.getLastChild().isEmpty());
 assertTrue(forNode2.getLastChild().isEmpty());
 assertFalse(forNode1.getLastChild().getLastChild().isEmpty());
 assertEquals(1, forNode1.getLastChild().getLastChild().getFirstChildCount());
 assertEquals(Token.FOR, forNode1.getLastChild().getLastChild().getFirstChild(0).getType());
 assertFalse(forNode2.getLastChild().getLastChild().isEmpty());
 assertEquals(1, forNode2.getLastChild().getLastChild().getFirstChildCount());
 assertEquals(Token.FOR, forNode2.getLastChild().getLastChild().getFirstChild(0).getType());
 }

 @Test
 public void testMinimizeExitPoints_NestedTryCatchFinally() {
 Node root = new Node(Token.BLOCK, null);
 Node tryNode = new Node(Token.TRY, null);
 Node tryBlock = new Node(Token.BLOCK, null);
 new Node(Token.RETURN, null).setParent(tryBlock);
 new Node(Token.BLOCK, null).setParent(tryNode);
 new Node(Token.BLOCK, null).setParent(tryNode);
 new Node(Token.BLOCK, null).addChildren(tryNode, tryBlock).setParent(root);
 Node catchNode = new Node(Token.CATCH, null);
 new Node(Token.BLOCK, null).setParent(catchNode);
 new Node(Token.RETURN, null).setParent(catchNode);
 new Node(Token.BLOCK, null).setParent(tryNode);
 new Node(Token.BLOCK, null).setParent(tryNode);
 new Node(Token.FINALLY, null).setParent(tryNode);
 Node finallyBlock = new Node(Token.BLOCK, null);
 new Node(Token.RETURN, null).setParent(finallyBlock);
 new Node(Token.BLOCK, null).setParent(finallyBlock);
 new Node(Token.BLOCK, null).addChildren(finallyBlock, tryBlock).setParent(root);
 NodeTraversal.traverse(compiler, root, minimizer);
 assertTrue(tryBlock.isEmpty());
 assertFalse(finallyBlock.isEmpty());
 assertEquals(1, finallyBlock.getFirstChildCount());
 assertEquals(Token.RETURN, finallyBlock.getFirstChild(0).getType());
 }
}