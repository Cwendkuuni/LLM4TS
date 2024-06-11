import org.junit.Test;
import static org.junit.Assert.*;
import com.google.javascript.jscomp.*;

public class UnreachableCodeEliminationTest {

  @Test
  public void testProcess() {
    AbstractCompiler compiler = new Compiler();
    UnreachableCodeElimination elimination = new UnreachableCodeElimination(compiler, true);
    assertNull(elimination.process(null, null));
  }

  @Test
  public void testVisit() {
    AbstractCompiler compiler = new Compiler();
    UnreachableCodeElimination elimination = new UnreachableCodeElimination(compiler, true);
    assertFalse(elimination.visit(null, null));
  }

  @Test
  public void testEliminationPass() {
    AbstractCompiler compiler = new Compiler();
    UnreachableCodeElimination elimination = new UnreachableCodeElimination(compiler, true);
    elimination.process(null, null);
    assertTrue(elimination.codeChanged);
  }

  @Test
  public void testTryRemoveUnconditionalBranching() {
    AbstractCompiler compiler = new Compiler();
    UnreachableCodeElimination elimination = new UnreachableCodeElimination(compiler, true);
    Node node = new Node(Token.BREAK);
    elimination.tryRemoveUnconditionalBranching(node);
    assertNull(node.getNext());
  }

  @Test
  public void testComputeFollowing() {
    AbstractCompiler compiler = new Compiler();
    UnreachableCodeElimination elimination = new UnreachableCodeElimination(compiler, true);
    Node node = new Node(Token.RETURN);
    node.addChildToBack(new Node(Token.NAME, new Node(Token.EXPR_RESULT)));
    assertNull(elimination.computeFollowing(node));
  }

  @Test
  public void testRemoveDeadExprStatementSafely() {
    AbstractCompiler compiler = new Compiler();
    UnreachableCodeElimination elimination = new UnreachableCodeElimination(compiler, true);
    Node node = new Node(Token.EMPTY);
    elimination.removeDeadExprStatementSafely(node);
    assertNull(node.getParent());
  }

}