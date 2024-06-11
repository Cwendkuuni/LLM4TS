import org.junit.Test;
import static org.junit.Assert.assertEquals;
import com.google.javascript.jscomp.CheckSideEffects;
import com.google.javascript.rhino.Node;

public class CheckSideEffectsTest {
  @Test
  public void testCheckSideEffects() {
    CheckSideEffects checkSideEffects = new CheckSideEffects(null, CheckLevel.WARNING, false);
    Node root = Node.objectLiteral();

    // Basic functionality test
    checkSideEffects.process(null, root);
    assertEquals(0, checkSideEffects.problemNodes.size());

    // Edge case: empty root node
    checkSideEffects = new CheckSideEffects(null, CheckLevel.WARNING, false);
    root = Node.nullLiteral();
    checkSideEffects.process(null, root);
    assertEquals(0, checkSideEffects.problemNodes.size());

    // Exception handling: null externs
    try {
      checkSideEffects.process(null, root);
      fail("Expected NullPointerException");
    } catch (NullPointerException e) {
      // Expected
    }
  }
}