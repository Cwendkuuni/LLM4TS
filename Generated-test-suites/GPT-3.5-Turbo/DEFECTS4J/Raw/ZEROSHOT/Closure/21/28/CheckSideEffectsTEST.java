import static org.junit.Assert.*;
import org.junit.Test;
import org.junit.Before;
import com.google.javascript.jscomp.*;

public class CheckSideEffectsTest {
  
  private CheckSideEffects checkSideEffects;
  private Compiler compiler;
  
  @Before
  public void setUp() {
    compiler = new Compiler();
    checkSideEffects = new CheckSideEffects(compiler, CheckLevel.WARNING, true);
  }
  
  @Test
  public void testProcess() {
    Node externs = null;
    Node root = null;
    checkSideEffects.process(externs, root);
    
    // add assertions here
    
  }
  
  @Test
  public void testHotSwapScript() {
    Node scriptRoot = null;
    Node originalRoot = null;
    checkSideEffects.hotSwapScript(scriptRoot, originalRoot);
    
    // add assertions here
    
  }
  
  @Test
  public void testVisit() {
    NodeTraversal t = null;
    Node n = null;
    Node parent = null;
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
    Node externs = null;
    Node root = null;
    CheckSideEffects.StripProtection stripProtection = new CheckSideEffects.StripProtection(compiler);
    stripProtection.process(externs, root);
    
    // add assertions here
    
  }
}