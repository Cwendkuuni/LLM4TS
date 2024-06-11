import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.AbstractPostOrderCallback;
import com.google.javascript.jscomp.ControlFlowAnalysis;
import com.google.javascript.jscomp.ControlFlowGraph;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.jscomp.ScopedCallback;
import org.junit.Test;
import static org.junit.Assert.*;

public class UnreachableCodeEliminationTest {

 private AbstractCompiler compiler;
 private UnreachableCodeElimination unreachableCodeElimination;

 @Before
 public void setUp() {
 compiler = new Compiler();
 unreachableCodeElimination = new UnreachableCodeElimination(compiler, true);
 }

 @Test
 public void testEmptyFunction() {
 Node root = new Node(Token.SCRIPT);
 unreachableCodeElimination.process(null, root);
 assertEquals(0, root.getChildCount());
 }

 @Test
 public void testReturnStatement() {
 Node root = new Node(Token.SCRIPT);
 root.setChild(0, new Node(Token.RETURN, null));
 unreachableCodeElimination.process(null, root);
 assertEquals(0, root.getChildCount());
 }

 @Test
 public void testFunctionWithNoOpStatements() {
 Node root = new Node(Token.SCRIPT);
 root.setChild(0, new Node(Token.FUNCTION, null, null, null, null, null, null, null, null, null));
 root.getFirstChild().setChild(0, new Node(Token.ASSIGN, null, new Node(Token.IDENT, "a"), new Node(Token.NUMBER, 5)));
 root.getFirstChild().setChild(1, new Node(Token.ASSIGN, null, new Node(Token.IDENT, "b"), new Node(Token.NUMBER, 10)));
 root.getFirstChild().setChild(2, new Node(Token.RETURN, null));
 unreachableCodeElimination.process(null, root);
 assertEquals(1, root.getChildCount());
 assertNull(root.getFirstChild().getChild(1));
 }

 @Test
 public void testFunctionWithNoSideEffectStatements() {
 Node root = new Node(Token.SCRIPT);
 root.setChild(0, new Node(Token.FUNCTION, null, null, null, null, null, null, null, null, null));
 root.getFirstChild().setChild(0, new Node(Token.IDENT, "a"));
 root.getFirstChild().setChild(1, new Node(Token.DOT, null, new Node(Token.IDENT, "b"), null));
 root.getFirstChild().setChild(1).setChild(0, new Node(Token.IDENT, "MyClass"));
 root.getFirstChild().setChild(1).setChild(1).setChild(0, new Node(Token.STRING_LITERAL, "propertyName"));
 root.getFirstChild().setChild(2, new Node(Token.RETURN, null));
 unreachableCodeElimination.process(null, root);
 assertEquals(1, root.getChildCount());
 assertNull(root.getFirstChild().getChild(0));
 }

 @Test(expected = NullPointerException.class)
 public void testNullNode() {
 Node root = null;
 unreachableCodeElimination.process(null, root);
 }

 @Test
 public void testUnreachableFunction() {
 Node root = new Node(Token.SCRIPT);
 root.setChild(0, new Node(Token.FUNCTION, null, null, null, null, null, null, null, null, null));
 root.getFirstChild().setChild(0, new Node(Token.RETURN, null));
 root.getFirstChild().setChild(1, new Node(Token.ASSIGN, null, new Node(Token.IDENT, "x"), new Node(Token.NUMBER, 5)));
 unreachableCodeElimination.process(null, root);
 assertEquals(0, root.getChildCount());
 }

 @Test
 public void testUnreachableStatementInsideFunction() {
 Node root = new Node(Token.SCRIPT);
 root.setChild(0, new Node(Token.FUNCTION, null, null, null, null, null, null, null, null, null));
 root.getFirstChild().setChild(0, new Node(Token.IF, null, new Node(Token.IDENT, "x"), null, null));
 root.getFirstChild().getFirstChild().setChild(0, new Node(Token.ASSIGN, null, new Node(Token.IDENT, "x"), new Node(Token.NUMBER, 5)));
 root.getFirstChild().getFirstChild().setChild(1, new Node(Token.RETURN, null));
 root.getFirstChild().setChild(1, new Node(Token.ASSIGN, null, new Node(Token.IDENT, "x"), new Node(Token.NUMBER, 10)));
 unreachableCodeElimination.process(null, root);
 assertEquals(1, root.getChildCount());
 assertNull(root.getFirstChild().getChild(0).getChild(0));
 }

 @Test
 public void testUnreachableStatementAfterReturn() {
 Node root = new Node(Token.SCRIPT);
 root.setChild(0, new Node(Token.FUNCTION, null, null, null, null, null, null, null, null, null));
 root.getFirstChild().setChild(0, new Node(Token.RETURN, null));
 root.getFirstChild().setChild(1, new Node(Token.ASSIGN, null, new Node(Token.IDENT, "x"), new Node(Token.NUMBER, 5)));
 unreachableCodeElimination.process(null, root);
 assertEquals(0, root.getChildCount());
 }

 @Test
 public void testUnreachableStatementOutsideFunction() {
 Node root = new Node(Token.SCRIPT);
 root.setChild(0, new Node(Token.RETURN, null));
 root.setChild(1, new Node(Token.ASSIGN, null, new Node(Token.IDENT, "x"), new Node(Token.NUMBER, 5)));
 unreachableCodeElimination.process(null, root);
 assertEquals(0, root.getChildCount());
 }

 @Test
 public void testUnreachableStatementInsideCondition() {
 Node root = new Node(Token.SCRIPT);
 root.setChild(0, new Node(Token.FUNCTION, null, null, null, null, null, null, null, null, null));
 root.getFirstChild().setChild(0, new Node(Token.IF, null, new Node(Token.IDENT, "x"), null, null));
 root.getFirstChild().getFirstChild().setChild(0, new Node(Token.ASSIGN, null, new Node(Token.IDENT, "x"), new Node(Token.NUMBER, 5)));
 root.getFirstChild().getFirstChild().setChild(1, new Node(Token.RETURN, null));
 root.getFirstChild().setChild(1, new Node(Token.ASSIGN, null, new Node(Token.IDENT, "x"), new Node(Token.NUMBER, 10)));
 unreachableCodeElimination.process(null, root);
 assertEquals(1, root.getChildCount());
 assertNull(root.getFirstChild().getChild(0).getChild(0));
 }

 @Test
 public void testUnreachableStatementInsideLoop() {
 Node root = new Node(Token.SCRIPT);
 root.setChild(0, new Node(Token.FUNCTION, null, null, null, null, null, null, null, null, null));
 root.getFirstChild().setChild(0, new Node(Token.DO, null, null, null));
 root.getFirstChild().getFirstChild().setChild(0, new Node(Token.ASSIGN, null, new Node(Token.IDENT, "x"), new Node(Token.NUMBER, 5)));
 root.getFirstChild().getFirstChild().setChild(1, new Node(Token.WHILE, null, new Node(Token.NOT_EQUAL, null, new Node(Token.IDENT, "x"), new Node(Token.NUMBER, 5)), null, null));
 root.getFirstChild().getFirstChild().getFirstChild().setChild(0, new Node(Token.ASSIGN, null, new Node(Token.IDENT, "x"), new Node(Token.NUMBER, 10)));
 root.getFirstChild().getFirstChild().getFirstChild().setChild(1, new Node(Token.RETURN, null));
 root.getFirstChild().setChild(1, new Node(Token.ASSIGN, null, new Node(Token.IDENT, "x"), new Node(Token.NUMBER, 15)));
 unreachableCodeElimination.process(null, root);
 assertEquals(1, root.getChildCount());
 assertNull(root.getFirstChild().getChild(0).getChild(0).getChild(0));
 }

 @Test
 public void testUnreachableStatementWithSideEffect() {
 Node root = new Node(Token.SCRIPT);
 root.setChild(0, new Node(Token.FUNCTION, null, null, null, null, null, null, null, null, null));
 root.getFirstChild().setChild(0, new Node(Token.ASSIGN, null, new Node(Token.IDENT, "x"), new Node(Token.NUMBER, 5)));
 root.getFirstChild().setChild(1, new Node(Token.RETURN, null));
 root.getFirstChild().getFirstChild().setChild(0, new Node(Token.ASSIGN, null, new Node(Token.IDENT, "x"), new Node(Token.NUMBER, 10)));
 unreachableCodeElimination.process(null, root);
 assertNotEquals(0, root.getChildCount());
 }

 @Test
 public void testUnreachableStatementWithSideEffectInsideFunction() {
 Node root = new Node(Token.SCRIPT);
 root.setChild(0, new Node(Token.FUNCTION, null, null, null, null, null, null, null, null, null));
 root.getFirstChild().setChild(0, new Node(Token.ASSIGN, null, new Node(Token.IDENT, "x"), new Node(Token.NUMBER, 5)));
 root.getFirstChild().setChild(1, new Node(Token.RETURN, null));
 root.getFirstChild().getFirstChild().setChild(0, new Node(Token.ASSIGN, null, new Node(Token.IDENT, "x"), new Node(Token.NUMBER, 10)));
 root.getFirstChild().setChild(1, new Node(Token.ASSIGN, null, new Node(Token.IDENT, "x"), new Node(Token.NUMBER, 15)));
 unreachableCodeElimination.process(null, root);
 assertNotEquals(0, root.getChildCount());
 }

 @Test
 public void testUnreachableStatementWithSideEffectOutsideFunction() {
 Node root = new Node(Token.SCRIPT);
 root.setChild(0, new Node(Token.ASSIGN, null, new Node(Token.IDENT, "x"), new Node(Token.NUMBER, 5)));
 root.setChild(1, new Node(Token.RETURN, null));
 root.setChild(2, new Node(Token.ASSIGN, null, new Node(Token.IDENT, "x"), new Node(Token.NUMBER, 10)));
 unreachableCodeElimination.process(null, root);
 assertNotEquals(0, root.getChildCount());
 }
}