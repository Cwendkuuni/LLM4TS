import static org.junit.Assert.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.AbstractCompiler;

public class DeadAssignmentsEliminationTest {

  private DeadAssignmentsElimination elimination;
  private AbstractCompiler compiler;

  @Before
  public void setUp() {
    compiler = new AbstractCompiler();
    elimination = new DeadAssignmentsElimination(compiler);
  }

  @After
  public void tearDown() {
    elimination = null;
    compiler = null;
  }

  @Test
  public void testProcess() {
    Node externs = null;
    Node root = null;
    elimination.process(externs, root);
    // TODO: Add appropriate assertions
  }

  @Test
  public void testEnterScope() {
    NodeTraversal t = null;
    elimination.enterScope(t);
    // TODO: Add appropriate assertions
  }

  @Test
  public void testExitScope() {
    NodeTraversal t = null;
    elimination.exitScope(t);
    // TODO: Add appropriate assertions
  }

  @Test
  public void testVisit() {
    NodeTraversal t = null;
    Node n = null;
    Node parent = null;
    elimination.visit(t, n, parent);
    // TODO: Add appropriate assertions
  }

}