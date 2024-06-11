import static org.junit.Assert.*;
import com.google.javascript.rhino.*;
import com.google.javascript.rhino.jstype.*;
import com.google.javascript.jscomp.*;
import com.google.common.base.*;
import com.google.common.collect.*;
import java.util.*;

public class ClosureReverseAbstractInterpreterTest {

  @Test
  public void testGetPreciserScopeKnowingConditionOutcome() {
    ClosureReverseAbstractInterpreter interpreter = new ClosureReverseAbstractInterpreter(convention, typeRegistry);
    Node condition = new Node();
    FlowScope blindScope = new FlowScope();
    boolean outcome = true;
    FlowScope result = interpreter.getPreciserScopeKnowingConditionOutcome(condition, blindScope, outcome);
    assertEquals(expected_result, result);

    condition = new Node();
    blindScope = new FlowScope();
    outcome = false;
    result = interpreter.getPreciserScopeKnowingConditionOutcome(condition, blindScope, outcome);
    assertEquals(expected_result, result);
  }

  @Test
  public void testRestrictParameter() {
    ClosureReverseAbstractInterpreter interpreter = new ClosureReverseAbstractInterpreter(convention, typeRegistry);
    Node parameter = new Node();
    JSType type = new JSType();
    FlowScope blindScope = new FlowScope();
    Function<TypeRestriction, JSType> restriction = (TypeRestriction p) -> type;
    boolean outcome = true;
    FlowScope result = interpreter.restrictParameter(parameter, type, blindScope, restriction, outcome);
    assertEquals(expected_result, result);

    parameter = new Node();
    type = new JSType();
    blindScope = new FlowScope();
    restriction = (TypeRestriction p) -> null;
    outcome = false;
    result = interpreter.restrictParameter(parameter, type, blindScope, restriction, outcome);
    assertEquals(expected_result, result);
  }
  
}