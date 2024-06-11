import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import com.google.javascript.jscomp.*;

public class CheckSideEffectsTest {

  private Compiler compiler;
  private CheckSideEffects checkSideEffects;

  @Before
  public void setUp() {
    compiler = new Compiler();
    checkSideEffects = new CheckSideEffects(compiler, CheckLevel.WARNING, true);
  }

  @Test
  public void testProcess() {
    Node externs = new Node();
    Node root = new Node();
    checkSideEffects.process(externs, root);
    // add assertions here
  }

  @Test
  public void testHotSwapScript() {
    Node scriptRoot = new Node();
    Node originalRoot = new Node();
    checkSideEffects.hotSwapScript(scriptRoot, originalRoot);
    // add assertions here
  }

  @Test
  public void testVisit() {
    NodeTraversal t = new NodeTraversal(compiler, new Node(), checkSideEffects);
    Node n = new Node();
    Node parent = new Node();
    checkSideEffects.visit(t, n, parent);
    // add assertions here
  }

  @Test
  public void testProtectSideEffects() {
    checkSideEffects.protectSideEffects();
    // add assertions here
  }

  @Test
  public void testAddExtern() {
    checkSideEffects.addExtern();
    // add assertions here
  }

  @Test
  public void testStripProtection() {
    CheckSideEffects.StripProtection stripProtection = new CheckSideEffects.StripProtection(compiler);
    Node externs = new Node();
    Node root = new Node();
    stripProtection.process(externs, root);
    // add assertions here
  }
}