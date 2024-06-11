import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class ClosureReverseAbstractInterpreterTest {
  
  private ClosureReverseAbstractInterpreter interpreter;
  
  @Before
  public void setUp() {
    interpreter = new ClosureReverseAbstractInterpreter(convention, typeRegistry);
  }
  
  @Test
  public void testGetPreciserScopeKnowingConditionOutcome() {
    Node condition = new Node();
    FlowScope blindScope = new FlowScope();
    boolean outcome = true;
    
    FlowScope result = interpreter.getPreciserScopeKnowingConditionOutcome(condition, blindScope, outcome);
    
    assertNotNull(result);
  }
  
  @Test
  public void testRestrictParameter() {
    Node parameter = new Node();
    JSType type = new JSType();
    FlowScope blindScope = new FlowScope();
    Function<TypeRestriction, JSType> restriction = new Function<TypeRestriction, JSType>() {
      public JSType apply(TypeRestriction p) {
        return null;
      }
    };
    boolean outcome = true;
    
    FlowScope result = interpreter.restrictParameter(parameter, type, blindScope, restriction, outcome);
    
    assertNotNull(result);
  }
}