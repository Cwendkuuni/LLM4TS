import static org.junit.Assert.*;
import org.junit.Test;

public class ClosureReverseAbstractInterpreterTest {

  @Test
  public void testGetPreciserScopeKnowingConditionOutcome() {
    ClosureReverseAbstractInterpreter interpreter = new ClosureReverseAbstractInterpreter(null, null);
    FlowScope blindScope = new FlowScope();
    
    //Test condition with a valid restricter
    Node condition = new Node(
        new Node(
            new Node("goog"),
            new Node("isDef")),
        new Node("param"));
    
    FlowScope result1 = interpreter.getPreciserScopeKnowingConditionOutcome(
        condition, blindScope, true);
    assertNotNull(result1);
    
    FlowScope result2 = interpreter.getPreciserScopeKnowingConditionOutcome(
        condition, blindScope, false);
    assertNotNull(result2);
    
    //Test condition with an invalid restricter
    Node condition2 = new Node(
        new Node(
            new Node("goog"),
            new Node("isInvalid")),
        new Node("param"));
    
    FlowScope result3 = interpreter.getPreciserScopeKnowingConditionOutcome(
        condition2, blindScope, true);
    assertNotNull(result3);
    
    FlowScope result4 = interpreter.getPreciserScopeKnowingConditionOutcome(
        condition2, blindScope, false);
    assertNotNull(result4);
  }
  
  @Test
  public void testRestrictParameter() {
    ClosureReverseAbstractInterpreter interpreter = new ClosureReverseAbstractInterpreter(null, null);
    FlowScope blindScope = new FlowScope();
    Node parameter = new Node("param");
    JSType type = null;
    Boolean outcome = true;
    Function<TypeRestriction, JSType> restriction = null;
    
    //Test with valid parameters
    FlowScope result1 = interpreter.restrictParameter(
        parameter, type, blindScope, restriction, outcome);
    assertNotNull(result1);
    
    //Test with invalid parameters
    FlowScope result2 = interpreter.restrictParameter(
        null, null, blindScope, null, false);
    assertNotNull(result2);
  }
}