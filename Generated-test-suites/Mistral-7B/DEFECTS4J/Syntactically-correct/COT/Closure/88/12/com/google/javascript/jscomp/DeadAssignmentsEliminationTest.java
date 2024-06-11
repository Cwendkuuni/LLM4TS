import org.junit.Test;
import static org.junit.Assert.*;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.ControlFlowGraph;
import com.google.javascript.jscomp.DataFlowAnalysis;
import com.google.javascript.jscomp.DeadAssignmentsElimination;
import com.google.javascript.jscomp.GraphUtil;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.Scope;

public class DeadAssignmentsEliminationTest {

 private AbstractCompiler compiler;
 private DataFlowAnalysis liveness;

 @Before
 public void setUp() {
 compiler = new AbstractCompiler() {};
 liveness = new DataFlowAnalysis(new ControlFlowGraph.Builder().build(),
 compiler.getOptions());
 }

 @Test
 public void testDeadAssignmentsElimination() {
 DeadAssignmentsElimination deadAssignmentsElimination =
 new DeadAssignmentsElimination(compiler);
 Node root = createTestNode();
 deadAssignmentsElimination.process(null, root);
 verifyDeadAssignmentsElimination(root);
 }

 @Test
 public void testDeadAssignmentsEliminationWithException() {
 DeadAssignmentsElimination deadAssignmentsElimination =
 new DeadAssignmentsElimination(compiler);
 Node root = createTestNodeWithException();
 try {
 deadAssignmentsElimination.process(null, root);
 fail("DeadAssignmentsElimination should have thrown an exception.");
 } catch (RuntimeException e) {
 // expected
 }
 verifyDeadAssignmentsElimination(root);
 }

 @Test
 public void testDeadAssignmentsEliminationWithVariableDeclaration() {
 DeadAssignmentsElimination deadAssignmentsElimination =
 new DeadAssignmentsElimination(compiler);
 Node root = createTestNodeWithVariableDeclaration();
 deadAssignmentsElimination.process(null, root);
 verifyDeadAssignmentsElimination(root);
 }

 @Test
 public void testDeadAssignmentsEliminationWithFunction() {
 DeadAssignmentsElimination deadAssignmentsElimination =
 new DeadAssignmentsElimination(compiler);
 Node root = createTestNodeWithFunction();
 deadAssignmentsElimination.process(null, root);
 verifyDeadAssignmentsElimination(root);
 }

 @Test
 public void testDeadAssignmentsEliminationWithMultipleAssignments() {
 DeadAssignmentsElimination deadAssignmentsElimination =
 new DeadAssignmentsElimination(compiler);
 Node root = createTestNodeWithMultipleAssignments();
 deadAssignmentsElimination.process(null, root);
 verifyDeadAssignmentsElimination(root);
 }

 @Test
 public void testDeadAssignmentsEliminationWithWhileLoop() {
 DeadAssignmentsElimination deadAssignmentsElimination =
 new DeadAssignmentsElimination(compiler);
 Node root = createTestNodeWithWhileLoop();
 deadAssignmentsElimination.process(null, root);
 verifyDeadAssignmentsElimination(root);
 }

 @Test
 public void testDeadAssignmentsEliminationWithIfElseStatement() {
 DeadAssignmentsElimination deadAssignmentsElimination =
 new DeadAssignmentsElimination(compiler);
 Node root = createTestNodeWithIfElseStatement();
 deadAssignmentsElimination.process(null, root);
 verifyDeadAssignmentsElimination(root);
 }

 @Test
 public void testDeadAssignmentsEliminationWithNestedIfElseStatement() {
 DeadAssignmentsElimination deadAssignmentsElimination =
 new DeadAssignmentsElimination(compiler);
 Node root = createTestNodeWithNestedIfElseStatement();
 deadAssignmentsElimination.process(null, root);
 verifyDeadAssignmentsElimination(root);
 }

 @Test
 public void testDeadAssignmentsEliminationWithVariableReassignment() {
 DeadAssignmentsElimination deadAssignmentsElimination =
 new DeadAssignmentsElimination(compiler);
 Node root = createTestNodeWithVariableReassignment();
 deadAssignmentsElimination.process(null, root);
 verifyDeadAssignmentsElimination(root);
 }

 @Test
 public void testDeadAssignmentsEliminationWithFunctionCall() {
 DeadAssignmentsElimination deadAssignmentsElimination =
 new DeadAssignmentsElimination(compiler);
 Node root = createTestNodeWithFunctionCall();
 deadAssignmentsElimination.process(null, root);
 verifyDeadAssignmentsElimination(root);
 }

 private Node createTestNode() {
 Node root = new Node(Token.PROGRAM, null);
 root.addChild(new Node(Token.ASSIGN, new Node("x"),
 new Node(Token.NUM, 1)));
 return root;
}

 private Node createTestNodeWithException() {
 Node root = new Node(Token.PROGRAM, null);
 root.addChild(new Node(Token.ASSIGN, new Node("x"), null));
 return root;
}

 private Node createTestNodeWithVariableDeclaration() {
 Node root = new Node(Token.PROGRAM, null);
 root.addChild(new Node(Token.VAR, new Node("x"), null));
 root.addChild(new Node(Token.ASSIGN, new Node("x"),
 new Node(Token.NUM, 1)));
 return root;
}

 private Node createTestNodeWithFunction() {
 Node root = new Node(Token.PROGRAM, null);
 root.addChild(new Node(Token.FUNCTION, null, null, null,
 new NodeList(new Node(Token.RETURN, new Node(Token.NUM, 1))),
 new NodeList(), null));
 root.addChild(new Node(Token.ASSIGN, new Node("x"),
 new Node(Token.NUM, 1)));
 return root;
}

 private Node createTestNodeWithMultipleAssignments() {
 Node root = new Node(Token.PROGRAM, null);
 root.addChild(new Node(Token.ASSIGN, new Node("x"),
 new Node(Token.NUM, 1)));
 root.addChild(new Node(Token.ASSIGN, new Node("x"),
 new Node(Token.NUM, 2)));
 return root;
}

 private Node createTestNodeWithWhileLoop() {
 Node root = new Node(Token.PROGRAM, null);
 root.addChild(new Node(Token.ASSIGN, new Node("x"),
 new Node(Token.NUM, 0)));
 root.addChild(new Node(Token.WHILE, new Node(Token.OP_EQ, new Node("x"),
 new Node(Token.NUM, 0)), null, null, null, null, null,
 new NodeList(), new NodeList(), null));
 root.addChild(new Node(Token.ASSIGN, new Node("x"),
 new Node(Token.ADD, new Node("x"), new Node(Token.NUM, 1))));
 return root;
}

 private Node createTestNodeWithIfElseStatement() {
 Node root = new Node(Token.PROGRAM, null);
 root.addChild(new Node(Token.ASSIGN, new Node("x"), new Node(Token.NUM, 1)));
 root.addChild(new Node(Token.IF, new Node(Token.OP_LT, new Node("x"),
 new Node(Token.NUM, 1)), null, null, null, null, null, null, null,
 new NodeList(), new NodeList(), null));
 root.addChild(new Node(Token.ASSIGN, new Node("y"), new Node(Token.NUM, 2)));
 root.addChild(new Node(Token.ELSE, null, null, null, null, null, null,
 new NodeList(), new NodeList(), null));
 root.addChild(new Node(Token.ASSIGN, new Node("y"), new Node(Token.NUM, 3)));
 return root;
}

 private Node createTestNodeWithNestedIfElseStatement() {
 Node root = new Node(Token.PROGRAM, null);
 root.addChild(new Node(Token.ASSIGN, new Node("x"), new Node(Token.NUM, 1)));
 root.addChild(new Node(Token.IF, new Node(Token.OP_LT, new Node("x"),
 new Node(Token.NUM, 1)), null, null, null, null, null, null, null,
 new NodeList(), new NodeList(), null));
 root.addChild(new Node(Token.ASSIGN, new Node("y"), new Node(Token.NUM, 2)));
 root.addChild(new Node(Token.ELSE, null, null, null, null, null, null,
 new NodeList(), new NodeList(), null));
 root.addChild(new Node(Token.IF, new Node(Token.OP_GT, new Node("x"),
 new Node(Token.NUM, 1)), null, null, null, null, null, null, null,
 new NodeList(), new NodeList(), null));
 root.addChild(new Node(Token.ASSIGN, new Node("z"), new Node(Token.NUM, 3)));
 root.addChild(new Node(Token.ELSE, null, null, null, null, null, null,
 new NodeList(), new NodeList(), null));
 root.addChild(new Node(Token.ASSIGN, new Node("z"), new Node(Token.NUM, 4)));
 return root;
}

 private Node createTestNodeWithVariableReassignment() {
 Node root = new Node(Token.PROGRAM, null);
 root.addChild(new Node(Token.ASSIGN, new Node("x"),
 new Node(Token.NUM, 1)));
 root.addChild(new Node(Token.ASSIGN, new Node("x"),
 new Node(Token.NUM, 2)));
 return root;
}

 private Node createTestNodeWithFunctionCall() {
 Node root = new Node(Token.PROGRAM, null);
 root.addChild(new Node(Token.ASSIGN, new Node("x"),
 new Node(Token.CALL, new Node("myFunction"), null, null, null, null,
 new NodeList(new Node(Token.NUM, 1)), new NodeList(), null)));
 return root;
}

 private void verifyDeadAssignmentsElimination(Node root) {
 assertEquals(0, NodeUtil.countNodes(root, matchRemovableAssigns));
}
}