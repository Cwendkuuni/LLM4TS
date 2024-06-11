import org.junit.Test;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.AbstractNode;
import com.google.javascript.jscomp.ControlFlowAnalysis;
import com.google.javascript.jscomp.ControlFlowGraph;
import com.google.javascript.jscomp.ControlFlowGraph.Branch;
import com.google.javascript.jscomp.DiGraphEdge;
import com.google.javascript.jscomp.DiGraphNode;
import com.google.javascript.jscomp.GraphReachability;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.rhino.Token;

public class UnreachableCodeEliminationTest {

 private AbstractCompiler compiler;
 private UnreachableCodeElimination elimination;

 @Before
 public void setUp() {
 compiler = new AbstractCompiler();
 elimination = new UnreachableCodeElimination(compiler, false);
 }

 @Test
 public void testEmpty() {
 Node root = Node.EMPTY;
 elimination.process(null, root);
 assertTrue(NodeUtil.isEmpty(root));
 }

 @Test
 public void testReturn() {
 Node root = Node.newNode(Token.RETURN, null, null, null);
 elimination.process(null, root);
 assertNull(root.getFirstChild());
 }

 @Test
 public void testFunction() {
 Node root = Node.newNode(Token.FUNCTION, null, null, null);
 elimination.process(null, root);
 assertNull(root.getFirstChild());
 }

 @Test
 public void testBlockWithReturn() {
 Node block = Node.newBlock("{return;}");
 Node root = Node.newNode(Token.BLOCK, null, block, null);
 elimination.process(null, root);
 assertNull(block.getFirstChild());
 }

 @Test
 public void testBlockWithEmptyStatement() {
 Node block = Node.newNode(Token.EMPTY, null, null, null);
 Node statement = Node.newNode(Token.EMPTY, null, null, null);
 Node root = Node.newNode(Token.BLOCK, null, block, statement);
 elimination.process(null, root);
 assertNull(block.getFirstChild());
 }

 @Test
 public void testBlockWithStatement() {
 Node block = Node.newBlock("{x = 1; y = 2;}");
 Node root = Node.newNode(Token.BLOCK, null, block, null);
 elimination.process(null, root);
 assertTrue(NodeUtil.isNodeEquivalent(block, NodeUtil.getFirstChild(root)));
 }

 @Test
 public void testBreak() {
 Node root = Node.newNode(Token.BREAK, 1, null, null);
 elimination.process(null, root);
 assertNull(root.getParent());
 }

 @Test
 public void testContinue() {
 Node root = Node.newNode(Token.CONTINUE, 1, null, null);
 elimination.process(null, root);
 assertNull(root.getParent());
 }

 @Test
 public void testTryCatch() {
 Node tryNode = Node.newNode(Token.TRY, null, null, null);
 Node catchNode = Node.newNode(Token.CATCH, null, null, null);
 Node finallyNode = Node.newNode(Token.BLOCK, null, null, null);
 Node root = Node.newNode(Token.BLOCK, null, tryNode, catchNode, finallyNode);
 elimination.process(null, root);
 assertNull(catchNode.getFirstChild());
 assertNull(finallyNode.getFirstChild());
 }

 @Test
 public void testTryCatchWithReturn() {
 Node tryNode = Node.newNode(Token.TRY, null, null, null);
 Node catchNode = Node.newNode(Token.BLOCK, null, Node.newNode(Token.RETURN, null, null, null), null);
 Node finallyNode = Node.newNode(Token.BLOCK, null, null, null);
 Node root = Node.newNode(Token.BLOCK, null, tryNode, catchNode, finallyNode);
 elimination.process(null, root);
 assertNull(catchNode.getFirstChild());
 assertNull(finallyNode.getFirstChild());
 }

 @Test
 public void testBranching() {
 Node condition = Node.newNode(Token.IF, null, Node.newBoolean(true), null, null);
 Node thenBranch = Node.newNode(Token.BLOCK, null, Node.newNode(Token.RETURN, null, null, null), null);
 Node elseBranch = Node.newNode(Token.BLOCK, null, Node.newNode(Token.RETURN, null, null, null), null);
 Node root = Node.newNode(Token.IF, null, condition, thenBranch, elseBranch);
 elimination.process(null, root);
 assertNull(thenBranch.getFirstChild());
 assertNull(elseBranch.getFirstChild());
 }

 @Test
 public void testBranchingWithReturnInElse() {
 Node condition = Node.newNode(Token.IF, null, Node.newBoolean(true), null, null);
 Node thenBranch = Node.newNode(Token.BLOCK, null, Node.newNode(Token.RETURN, null, null, null), null);
 Node elseBranch = Node.newNode(Token.BLOCK, null, Node.newNode(Token.RETURN, null, null, null), null);
 Node root = Node.newNode(Token.IF, null, condition, thenBranch, elseBranch);
 elimination.process(null, root);
 assertNull(thenBranch.getFirstChild());
 assertNull(elseBranch.getFirstChild());
 }

 @Test
 public void testBranchingWithReturnInBoth() {
 Node condition = Node.newNode(Token.IF, null, Node.newBoolean(true), null, null);
 Node thenBranch = Node.newNode(Token.BLOCK, null, Node.newNode(Token.RETURN, null, null, null), null);
 Node elseBranch = Node.newNode(Token.BLOCK, null, Node.newNode(Token.RETURN, null, null, null), null);
 Node root = Node.newNode(Token.IF, null, condition, thenBranch, elseBranch);
 elimination.process(null, root);
 assertNull(thenBranch.getFirstChild());
 assertNull(elseBranch.getFirstChild());
 }

 @Test(expected = RuntimeException.class)
 public void testInvalidControlFlow() {
 Node root = Node.newNode(Token.IF, null, Node.newBoolean(true), null, Node.newNode(Token.RETURN, null, null, null));
 elimination.process(null, root);
 }

 @Test
 public void testControlFlowGraph() {
 ControlFlowAnalysis cfa = new ControlFlowAnalysis(compiler, false, false);
 ControlFlowGraph<Node> cfg = cfa.getCfg();
 DiGraphNode<Node, Branch> entry = cfg.getEntry();
 assertTrue(entry.getValue() instanceof Node);
 assertTrue(NodeUtil.isNodeEquivalent(entry.getValue(), cfg.getDirectedGraphNode(Node.newNode(Token.FUNCTION, null, null, null)).getValue()));
 }

 @Test
 public void testUnreachableCode() {
 Node root = Node.newNode(Token.BLOCK, null,
 Node.newBlock("{if (false) { return; }}"),
 Node.newNode(Token.RETURN, null, null, null)
 );
 elimination.process(null, root);
 assertTrue(NodeUtil.isEmpty(root));
 }

 @Test
 public void testRemoveNoOpStatement() {
 Node root = Node.newNode(Token.BLOCK, null,
 Node.newBlock("{a.b.MyClass.prototype.propertyName; return;}"),
 Node.newNode(Token.RETURN, null, null, null)
 );
 elimination.process(null, root);
 assertFalse(NodeUtil.isEmpty(NodeUtil.getFirstChild(root)));
 }

 @Test
 public void testRemoveDeadBranch() {
 Node root = Node.newNode(Token.BLOCK, null,
 Node.newBlock("{if (false) { return; }}"),
 Node.newBlock("{if (false) { return; }}"),
 Node.newNode(Token.RETURN, null, null, null)
 );
 elimination.process(null, root);
 assertFalse(NodeUtil.isEmpty(NodeUtil.getFirstChild(root)));
 }

 @Test
 public void testRemoveDeadBranchWithMultipleBranches() {
 Node root = Node.newNode(Token.BLOCK, null,
 Node.newBlock("{if (false) { return; }}"),
 Node.newBlock("{if (false) { return; }}"),
 Node.newBlock("{if (false) { return; }}"),
 Node.newNode(Token.RETURN, null, null, null)
 );
 elimination.process(null, root);
 assertFalse(NodeUtil.isEmpty(NodeUtil.getFirstChild(root)));
 }

 @Test
 public void testRemoveDeadBranchWithConditional() {
 Node root = Node.newNode(Token.BLOCK, null,
 Node.newBlock("{if (x) { if (false) { return; } }}"),
 Node.newNode(Token.RETURN, null, null, null)
 );
 elimination.process(null, root);
 assertFalse(NodeUtil.isEmpty(NodeUtil.getFirstChild(root)));
 }

 @Test
 public void testRemoveDeadBranchWithConditionalAndStatement() {
 Node root = Node.newNode(Token.BLOCK, null,
 Node.newBlock("{if (x) { if (false) { return; } } x = 1; }"),
 Node.newNode(Token.RETURN, null, null, null)
 );
 elimination.process(null, root);
 assertFalse(NodeUtil.isEmpty(NodeUtil.getFirstChild(root)));
 }

 @Test
 public void testRemoveDeadBranchWithException() {
 Node root = Node.newNode(Token.BLOCK, null,
 Node.newBlock("{try { if (false) { throw new Error(); }} catch (e) {} return;}"),
 Node.newNode(Token.RETURN, null, null, null)
 );
 elimination.process(null, root);
 assertFalse(NodeUtil.isEmpty(NodeUtil.getFirstChild(root)));
 }

 @Test
 public void testRemoveDeadBranchWithExceptionAndStatement() {
 Node root = Node.newNode(Token.BLOCK, null,
 Node.newBlock("{try { if (false) { throw new Error(); }} catch (e) {} x = 1;}"),
 Node.newNode(Token.RETURN, null, null, null)
 );
 elimination.process(null, root);
 assertFalse(NodeUtil.isEmpty(NodeUtil.getFirstChild(root)));
 }

 @Test
 public void testRemoveDeadBranchWithExceptionAndMultipleStatements() {
 Node root = Node.newNode(Token.BLOCK, null,
 Node.newBlock("{try { if (false) { throw new Error(); }} catch (e) {} x = 1; y = 2;}"),
 Node.newNode(Token.RETURN, null, null, null)
 );
 elimination.process(null, root);
 assertFalse(NodeUtil.isEmpty(NodeUtil.getFirstChild(root)));
 }

 @Test
 public void testRemoveDeadBranchWithMultipleConditionalAndStatement() {
 Node root = Node.newNode(Token.BLOCK, null,
 Node.newBlock("{if (x) { if (y) { if (false) { return; } } } x = 1; }"),
 Node.newNode(Token.RETURN, null, null, null)
 );
 elimination.process(null, root);
 assertFalse(NodeUtil.isEmpty(NodeUtil.getFirstChild(root)));
 }
}