import static org.junit.Assert.*;
import static com.google.javascript.jscomp.NodeTraversal.AbstractPostOrderCallback.VISIT_ALL;
import static com.google.javascript.rhino.NodeType.*;
import com.google.common.base.Preconditions;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.ControlFlowAnalysis;
import com.google.javascript.jscomp.ControlFlowGraph;
import com.google.javascript.jscomp.ControlFlowGraph.Branch;
import com.google.javascript.jscomp.GraphReachability;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.jscomp.ScopedCallback;
import java.util.Deque;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class UnreachableCodeEliminationTest {

 private static final Logger logger =
   Logger.getLogger(UnreachableCodeEliminationTest.class.getName());

 @SuppressWarnings("unchecked")
 private static <T> T newInstance(Class<T> clazz, Object... params) {
   try {
     return clazz.getDeclaredConstructor(params.class).newInstance(params);
   } catch (Exception e) {
     throw new RuntimeException(e);
   }
 }

 @BeforeClass
 public static void setUp() {
   // Set up a mock compiler to make the tests runnable without a real compiler
   AbstractCompiler compiler = newInstance(AbstractCompiler.class);
   CompilerPass unreachableCodeElimination =
       newInstance(UnreachableCodeElimination.class, compiler, false);
   Node root = Node.createTree(BLOCK, null);
   NodeTraversal.traverse(VISIT_ALL, root, unreachableCodeElimination);
 }

 @Test
 public void testNoOpStatementsAreRemovedIfUnreachable() {
   Node root = Node.createTree(
       BLOCK,
       BLOCK,
       EXPR_STATEMENT,
       IDENT, "x",
       OP_ASSIGN,
       NUM_INT, 5,
       COMMA,
       EXPR_STATEMENT,
       IDENT, "y",
       OP_ASSIGN,
       NUM_INT, 10,
       RETURN_STATEMENT,
       NULL_LITERAL,
       BLOCK_CLOSING,
       RETURN_STATEMENT,
       IDENT, "x"
   );

   NodeTraversal.traverse(VISIT_ALL, root, new UnreachableCodeElimination(null, false));

   assertEquals("x: number = 5,\ny: number = 10;", root.toSource());
 }

 @Test
 public void testReturnStatementPrecededByDeadCodeIsRemoved() {
   Node root = Node.createTree(
       BLOCK,
       BLOCK,
       IDENT, "x",
       IF_STATEMENT,
       EXPR_STATEMENT,
       IDENT, "x",
       OP_EQUALS,
       NUM_INT, 1,
       BOOL_LITERAL, true,
       BLOCK_CLOSING,
       RETURN_STATEMENT,
       NUM_INT, 42,
       BLOCK_CLOSING,
       RETURN_STATEMENT
   );

   NodeTraversal.traverse(VISIT_ALL, root, new UnreachableCodeElimination(null, false));

   assertEquals("if (x == 1) {}", root.toSource());
 }

 @Test
 public void testUnreachableCodeInNestedIfStatementsIsRemoved() {
   Node root = Node.createTree(
       BLOCK,
       BLOCK,
       IF_STATEMENT,
       EXPR_STATEMENT,
       IDENT, "x",
       OP_LESS,
       NUM_INT, 5,
       BLOCK_CLOSING,
       BLOCK,
       IF_STATEMENT,
       EXPR_STATEMENT,
       IDENT, "y",
       OP_LESS,
       NUM_INT, 5,
       BLOCK_CLOSING,
       RETURN_STATEMENT,
       NUM_INT, 42
   );

   NodeTraversal.traverse(VISIT_ALL, root, new UnreachableCodeElimination(null, false));

   assertEquals("if (x < 5) {}", root.toSource());
 }

 @Test
 public void testReturnStatementFollowedByDeadCodeIsRemoved() {
   Node root = Node.createTree(
       BLOCK,
       BLOCK,
       IDENT, "x",
       IF_STATEMENT,
       EXPR_STATEMENT,
       IDENT, "x",
       OP_EQUALS,
       NUM_INT, 1,
       BOOL_LITERAL, true,
       BLOCK_CLOSING,
       RETURN_STATEMENT,
       NUM_INT, 42,
       EXPR_STATEMENT,
       IDENT, "x",
       OP_ASSIGN,
       NUM_INT, 5
   );

   NodeTraversal.traverse(VISIT_ALL, root, new UnreachableCodeElimination(null, false));

   assertEquals("if (x == 1) {}", root.toSource());
 }

 @Test
 public void testReturnStatementInNestedIfStatementsIsRemoved() {
   Node root = Node.createTree(
       BLOCK,
       BLOCK,
       IF_STATEMENT,
       EXPR_STATEMENT,
       IDENT, "x",
       OP_LESS,
       NUM_INT, 5,
       BLOCK_CLOSING,
       BLOCK,
       IF_STATEMENT,
       EXPR_STATEMENT,
       IDENT, "x",
       OP_EQUALS,
       NUM_INT, 1,
       BOOL_LITERAL, true,
       BLOCK_CLOSING,
       RETURN_STATEMENT,
       NUM_INT, 42
   );

   NodeTraversal.traverse(VISIT_ALL, root, new UnreachableCodeElimination(null, false));

   assertEquals("if (x < 5) {}", root.toSource());
 }

 @Test
 public void testUnreachableCodeInSwitchStatementIsRemoved() {
   Node root = Node.createTree(
       BLOCK,
       BLOCK,
       EXPR_STATEMENT,
       SWITCH_STATEMENT,
       EXPR_STATEMENT,
       IDENT, "x",
       CASE_STATEMENT,
       NUM_INT, 1,
       EXPR_STATEMENT,
       IDENT, "y",
       OP_ASSIGN,
       NUM_INT, 5,
       BREAK_STATEMENT,
       CASE_STATEMENT,
       NUM_INT, 2,
       RETURN_STATEMENT,
       NUM_INT, 42
   );

   NodeTraversal.traverse(VISIT_ALL, root, new UnreachableCodeElimination(null, false));

   assertEquals("switch (x) {\n case 1:\n}", root.toSource());
 }

 @Test
 public void testNoOpStatementsAreNotRemovedIfReachable() {
   Node root = Node.createTree(
       BLOCK,
       BLOCK,
       IDENT, "x",
       IF_STATEMENT,
       EXPR_STATEMENT,
       IDENT, "x",
       OP_EQUALS,
       NUM_INT, 1,
       BOOL_LITERAL, false,
       BLOCK_CLOSING,
       RETURN_STATEMENT,
       EXPR_STATEMENT,
       IDENT, "x",
       OP_ASSIGN,
       NUM_INT, 5,
       BLOCK_CLOSING,
       RETURN_STATEMENT
   );

   NodeTraversal.traverse(VISIT_ALL, root, new UnreachableCodeElimination(null, false));

   assertEquals("if (x != 1) return; x = 5; return;", root.toSource());
 }

 @Test
 public void testAlwaysTrueConditionIsNotReplaced() {
   Node root = Node.createTree(
       BLOCK,
       BLOCK,
       IF_STATEMENT,
       EXPR_STATEMENT,
       BOOL_LITERAL, true,
       BLOCK_CLOSING,
       RETURN_STATEMENT,
       NUM_INT, 42
   );

   NodeTraversal.traverse(VISIT_ALL, root, new UnreachableCodeElimination(null, false));

   assertEquals("if (true) return 42;", root.toSource());
 }

 @Test
 public void testAlwaysFalseConditionIsNotReplaced() {
   Node root = Node.createTree(
       BLOCK,
       BLOCK,
       IF_STATEMENT,
       EXPR_STATEMENT,
       BOOL_LITERAL, false,
       BLOCK_CLOSING,
       RETURN_STATEMENT,
       NUM_INT, 42
   );

   NodeTraversal.traverse(VISIT_ALL, root, new UnreachableCodeElimination(null, false));

   assertEquals("if (false) return 42;", root.toSource());
 }

 @Test
 public void testIfStatementWithElseIsNotChanged() {
   Node root = Node.createTree(
       BLOCK,
       BLOCK,
       IF_STATEMENT,
       EXPR_STATEMENT,
       IDENT, "x",
       OP_EQUALS,
       NUM_INT, 1,
       BLOCK,
       IDENT, "y",
       OP_ASSIGN,
       NUM_INT, 5,
       ELSE_STATEMENT,
       EXPR_STATEMENT,
       IDENT, "y",
       OP_ASSIGN,
       NUM_INT, 10,
       BLOCK_CLOSING,
       RETURN_STATEMENT,
       NUM_INT, 42
   );

   NodeTraversal.traverse(VISIT_ALL, root, new UnreachableCodeElimination(null, false));

   assertEquals("if (x == 1) {\n y = 5;\n} else {\n y = 10;\n}", root.toSource());
 }

 @Test
 public void testUnreachableCodeIsLogged() {
   Node root = Node.createTree(
       BLOCK,
       BLOCK,
       IF_STATEMENT,
       EXPR_STATEMENT,
       IDENT, "x",
       OP_EQUALS,
       NUM_INT, 1,
       BLOCK,
       EXPR_STATEMENT,
       IDENT, "y",
       OP_ASSIGN,
       NUM_INT, 5,
       ELSE_STATEMENT,
       RETURN_STATEMENT,
       IDENT, "x"
   );

   logger.setLevel(Level.FINE);

   NodeTraversal.traverse(VISIT_ALL, root, new UnreachableCodeElimination(null, false));

   logger.info("Log: " + logger.getMessages().get(0).getMessage());
   logger.setLevel(Level.INFO);

   assertEquals("Unreachable code: x != 1", logger.getMessages().get(0).getMessage());
 }

 @Test(expected = RuntimeException.class)
 public void testIllegalArguments() {
   new UnreachableCodeElimination(null, true, null);
 }
}