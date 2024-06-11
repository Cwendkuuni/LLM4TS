import org.junit.Test;
import static org.junit.Assert.assertTrue;
import com.google.javascript.jscomp.CheckLevel;
import com.google.javascript.jscomp.CheckSideEffects;
import com.google.javascript.rhino.Node;

public class CheckSideEffectsTest {

  @Test
  public void testCheckSideEffects_SimpleOp() {
    CheckSideEffects checkSideEffects = new CheckSideEffects(null, CheckLevel.WARNING, false);
    Node rootNode = createNodeForTest("1 + 1;");
    checkSideEffects.process(null, rootNode);
    assertTrue(checkSideEffects.problemNodes.isEmpty());
  }

  @Test
  public void testCheckSideEffects_UselessCode() {
    CheckSideEffects checkSideEffects = new CheckSideEffects(null, CheckLevel.WARNING, false);
    Node rootNode = createNodeForTest(";");
    checkSideEffects.process(null, rootNode);
    assertTrue(checkSideEffects.problemNodes.isEmpty());
  }

  @Test
  public void testCheckSideEffects_SideEffectFreeCode() {
    CheckSideEffects checkSideEffects = new CheckSideEffects(null, CheckLevel.WARNING, false);
    Node rootNode = createNodeForTest("var x = 1;");
    checkSideEffects.process(null, rootNode);
    assertTrue(checkSideEffects.problemNodes.isEmpty());
  }

  @Test
  public void testCheckSideEffects_SideEffectCode() {
    CheckSideEffects checkSideEffects = new CheckSideEffects(null, CheckLevel.WARNING, false);
    Node rootNode = createNodeForTest("document.write('Hello World');");
    checkSideEffects.process(null, rootNode);
    assertFalse(checkSideEffects.problemNodes.isEmpty());
  }

  @Test(expected = IllegalStateException.class)
  public void testCheckSideEffects_ProtectedCode() {
    CheckSideEffects checkSideEffects = new CheckSideEffects(null, CheckLevel.WARNING, true);
    Node rootNode = createNodeForTest("JSCOMPILER_PRESERVE(1 + 1);");
    checkSideEffects.process(null, rootNode);
    assertTrue(checkSideEffects.problemNodes.isEmpty());
  }

  private Node createNodeForTest(String code) {
    // Implementation to create Node object from given code.
    // This is not shown here as it is beyond the scope of this example.
    // You can use any Node creation utility available to you.
  }

  
  // Add any additional tests that you believe are necessary here.
  
}