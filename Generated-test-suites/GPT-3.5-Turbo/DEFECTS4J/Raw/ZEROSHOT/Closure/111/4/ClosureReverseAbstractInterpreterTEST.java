import static org.junit.Assert.assertEquals;
import org.junit.Test;

public class ClosureReverseAbstractInterpreterTest {
  
  private ClosureReverseAbstractInterpreter interpreter;
  
  @Test
  public void testGetPreciserScopeKnowingConditionOutcome() {
    FlowScope blindScope = new FlowScope();
    Node condition = new Node();
    condition.putProp("callee", new Node());
    condition.putProp("param", new Node());
    
    FlowScope resultScope = interpreter.getPreciserScopeKnowingConditionOutcome(condition, blindScope, true);
    
    assertEquals(resultScope, blindScope);
  }
  
  @Test
  public void testRestrictParameter() {
    Node parameter = new Node();
    JSType type = new JSTypeStub();
    FlowScope blindScope = new FlowScope();
    Function<TypeRestriction, JSType> restriction = new Function<TypeRestriction, JSType>() {
      @Override
      public JSType apply(TypeRestriction p) {
        return null;
      }
    };
    boolean outcome = true;
    
    FlowScope resultScope = interpreter.restrictParameter(parameter, type, blindScope, restriction, outcome);
    
    assertEquals(blindScope, resultScope);
  }
}