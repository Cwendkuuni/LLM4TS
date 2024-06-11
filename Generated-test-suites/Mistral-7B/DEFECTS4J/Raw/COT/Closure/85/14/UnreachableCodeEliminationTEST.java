import static org.junit.Assert.*;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.ScopedCallback;
import org.junit.Before;
import org.junit.Test;

public class UnreachableCodeEliminationTest {
  private AbstractCompiler compiler;
  private UnreachableCodeElimination elimination;

  @Before
  public void setUp() {
    compiler = new AbstractCompiler();
    elimination = new UnreachableCodeElimination(compiler, false);
  }

  @Test
  public void testEmptyFunction() {
    Node root = new Node(Token.SCRIPT);
    elimination.process(null, root);
    assertEquals(0, root.getChildCount());
  }

  @Test
  public void testFunctionWithReturnStatement() {
    Node root = new Node(Token.SCRIPT);
    root.addChild(new Node(Token.RETURN, null, null));
    elimination.process(null, root);
    assertEquals(0, root.getChildCount());
  }

  @Test
  public void testFunctionWithNoOpStatement() {
    Node root = new Node(Token.SCRIPT);
    root.addChild(new Node(Token.BLOCK, null, null));
    root.getLastChild().addChild(new Node(Token.TRUE, null, null));
    elimination.process(null, root);
    assertEquals(0, root.getChildCount());
  }

  @Test
  public void testDeadCodeRemoval() {
    Node root = new Node(Token.SCRIPT);
    root.addChild(new Node(Token.BLOCK, null, null));
    root.getLastChild().addChild(new Node(Token.RETURN, null, null));
    root.getLastChild().getLastChild().addChild(new Node(Token.ALERT, "unreachable", null));
    elimination.process(null, root);
    assertEquals(0, root.getChildCount());
  }

  @Test(expected = NullPointerException.class)
  public void testNullNode() {
    elimination.visit(null, null, null);
  }

  @Test
  public void testRemoveBreakStatement() {
    Node root = new Node(Token.SCRIPT);
    root.addChild(new Node(Token.BLOCK, null, null));
    root.getLastChild().addChild(new Node(Token.BREAK, null, null));
    root.getLastChild().addChild(new Node(Token.RETURN, null, null));
    elimination.process(null, root);
    assertEquals(0, root.getChildCount());
  }

  @Test
  public void testRemoveContinueStatement() {
    Node root = new Node(Token.SCRIPT);
    root.addChild(new Node(Token.BLOCK, null, null));
    root.getLastChild().addChild(new Node(Token.CONTINUE, null, null));
    root.addChild(new Node(Token.RETURN, null, null));
    elimination.process(null, root);
    Node body = root.getFirstChild();
    assertEquals(0, body.getChildCount());
  }

  @Test
  public void testRemoveUnconditionalBranching() {
    Node root = new Node(Token.SCRIPT);
    root.addChild(new Node(Token.BLOCK, null, null));
    root.getLastChild().addChild(new Node(Token.BREAK, null, null));
    root.getLastChild().addChild(new Node(Token.BLOCK, null, null));
    root.getLastChild().getLastChild().addChild(new Node(Token.RETURN, null, null));
    elimination.process(null, root);
    Node firstBlock = root.getFirstChild();
    Node secondBlock = firstBlock.getNext();
    assertEquals(0, secondBlock.getChildCount());
  }

  @Test
  public void testRemoveFunctionStatement() {
    Node root = new Node(Token.SCRIPT);
    Node functionNode = new Node(Token.FUNCTION, null, null);
    root.addChild(functionNode);
    elimination.process(null, root);
    assertEquals(0, root.getChildCount());
  }
}