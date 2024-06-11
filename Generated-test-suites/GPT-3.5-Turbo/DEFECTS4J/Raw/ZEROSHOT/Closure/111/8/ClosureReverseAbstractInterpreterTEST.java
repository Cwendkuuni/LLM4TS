import org.junit.Test;
import static org.junit.Assert.*;

public class ClosureReverseAbstractInterpreterTest {

  @Test
  public void testGetPreciserScopeKnowingConditionOutcome() {
    ClosureReverseAbstractInterpreter interpreter = new ClosureReverseAbstractInterpreter();
    Node condition = new Node();
    FlowScope blindScope = new FlowScope();
    boolean outcome = true;

    FlowScope result = interpreter.getPreciserScopeKnowingConditionOutcome(condition, blindScope, outcome);

    assertNotNull(result);
  }

}