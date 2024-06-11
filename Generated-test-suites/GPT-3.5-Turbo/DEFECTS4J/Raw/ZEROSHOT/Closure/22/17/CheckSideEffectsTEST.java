import org.junit.*;
import static org.junit.Assert.*;
import com.google.javascript.jscomp.*;

public class CheckSideEffectsTest {
  
  CheckSideEffects checkSideEffects;
  
  @Before
  public void setUp() {
    Compiler compiler = new Compiler();
    checkSideEffects = new CheckSideEffects(compiler, CheckLevel.WARNING, true);
  }
  
  @Test
  public void testProcess() {
    Node externs = new Node(Token.EXTERN);
    Node root = new Node(Token.ROOT);
    checkSideEffects.process(externs, root);
    // Assert statements here
  }
  
  @Test
  public void testHotSwapScript() {
    Node scriptRoot = new Node(Token.SCRIPT);
    Node originalRoot = new Node(Token.SCRIPT);
    checkSideEffects.hotSwapScript(scriptRoot, originalRoot);
    // Assert statements here
  }
  
  @Test
  public void testVisit() {
    NodeTraversal t = new NodeTraversal();
    Node n = new Node(Token.VAR);
    Node parent = new Node(Token.ROOT);
    checkSideEffects.visit(t, n, parent);
    // Assert statements here
  }
  
  @Test
  public void testProtectSideEffects() {
    checkSideEffects.protectSideEffects();
    // Assert statements here
  }
  
  @Test
  public void testStripProtection() {
    Node externs = new Node(Token.EXTERN);
    Node root = new Node(Token.ROOT);
    CheckSideEffects.StripProtection stripProtection = new CheckSideEffects.StripProtection(new Compiler());
    stripProtection.process(externs, root);
    // Assert statements here
  }

  
}