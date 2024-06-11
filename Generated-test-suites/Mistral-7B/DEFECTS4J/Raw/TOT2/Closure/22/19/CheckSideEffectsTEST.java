import org.junit.Test;
import static org.junit.Assert.*;
import com.google.javascript.jscomp.CheckLevel;
import com.google.javascript.jscomp.CheckSideEffects;
import com.google.javascript.rhino.Node;

public class CheckSideEffectsTest {

  @Test
  public void testCheckSideEffects() {
    CheckSideEffects checker = new CheckSideEffects(null, CheckLevel.WARNING, false);
    Node node = IR.literal(1); // A simple node with no side effects

    CheckSideEffects.ProcessResult result = checker.process(new Node(), node);
    assertEquals(0, result.getProblemNodes().size());

    node = IR.name("alert").call(IR.literal("Hello World")); // A node with a side effect (calling alert)

    result = checker.process(new Node(), node);
    assertEquals(1, result.getProblemNodes().size());

    // Test for exception handling
    try {
      checker = new CheckSideEffects(null, CheckLevel.ERROR, false);
    } catch (IllegalArgumentException e) {
      // Expected exception as CheckLevel.ERROR is not valid constructor argument
    }
  }
}