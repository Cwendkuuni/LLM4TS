import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class ClosureReverseAbstractInterpreterTest {
  
  private ClosureReverseAbstractInterpreter interpreter;
  
  @Before
  public void setUp() {
    interpreter = new ClosureReverseAbstractInterpreter(new CodingConvention(), new JSTypeRegistry());
  }
  
  @Test
  public void testGetPreciserScopeKnowingConditionOutcome_withCallNodeWithRightRestricter_shouldRestrictParameter() {
    Node condition = new Node(Node.CALL, new Node(Node.GETPROP, new Node(Node.NAME, "goog"), new Node(Node.STRING, "isDef")), new Node(Node.NAME, "param"));
    FlowScope blindScope = new FlowScope();
    boolean outcome = true;
    
    FlowScope result = interpreter.getPreciserScopeKnowingConditionOutcome(condition, blindScope, outcome);
    
    // Assert statements here
  }
  
  @Test
  public void testGetPreciserScopeKnowingConditionOutcome_withCallNodeWithWrongRestricter_shouldNotRestrictParameter() {
    Node condition = new Node(Node.CALL, new Node(Node.GETPROP, new Node(Node.NAME, "goog"), new Node(Node.STRING, "isFoo")), new Node(Node.NAME, "param"));
    FlowScope blindScope = new FlowScope();
    boolean outcome = true;
    
    FlowScope result = interpreter.getPreciserScopeKnowingConditionOutcome(condition, blindScope, outcome);
    
    // Assert statements here
  }
  
  @Test
  public void testGetPreciserScopeKnowingConditionOutcome_withNonCallNode_shouldReturnNextPreciserScope() {
    Node condition = new Node(Node.NAME, "param");
    FlowScope blindScope = new FlowScope();
    boolean outcome = true;
    
    FlowScope result = interpreter.getPreciserScopeKnowingConditionOutcome(condition, blindScope, outcome);
    
    // Assert statements here
  }
  
  @Test
  public void testGetPreciserScopeKnowingConditionOutcome_withCallNodeWithNullType_shouldReturnNextPreciserScope() {
    Node condition = new Node(Node.CALL, new Node(Node.GETPROP, new Node(Node.NAME, "goog"), new Node(Node.STRING, "isNull")), new Node(Node.NAME, "param"));
    FlowScope blindScope = new FlowScope();
    boolean outcome = false;
    
    FlowScope result = interpreter.getPreciserScopeKnowingConditionOutcome(condition, blindScope, outcome);
    
    // Assert statements here
  }
  
  @Test
  public void testGetPreciserScopeKnowingConditionOutcome_withCallNodeWithArrayRestricterAndOutcomeTrue_shouldRestrictParameterToArray() {
    Node condition = new Node(Node.CALL, new Node(Node.GETPROP, new Node(Node.NAME, "goog"), new Node(Node.STRING, "isArray")), new Node(Node.NAME, "param"));
    FlowScope blindScope = new FlowScope();
    boolean outcome = true;
    
    FlowScope result = interpreter.getPreciserScopeKnowingConditionOutcome(condition, blindScope, outcome);
    
    // Assert statements here
  }
  
  @Test
  public void testGetPreciserScopeKnowingConditionOutcome_withCallNodeWithArrayRestricterAndOutcomeFalse_shouldNotRestrictParameterToArray() {
    Node condition = new Node(Node.CALL, new Node(Node.GETPROP, new Node(Node.NAME, "goog"), new Node(Node.STRING, "isArray")), new Node(Node.NAME, "param"));
    FlowScope blindScope = new FlowScope();
    boolean outcome = false;
    
    FlowScope result = interpreter.getPreciserScopeKnowingConditionOutcome(condition, blindScope, outcome);
    
    // Assert statements here
  }
  
  @Test
  public void testGetPreciserScopeKnowingConditionOutcome_withCallNodeWithObjectRestricterAndOutcomeTrue_shouldRestrictParameterToObject() {
    Node condition = new Node(Node.CALL, new Node(Node.GETPROP, new Node(Node.NAME, "goog"), new Node(Node.STRING, "isObject")), new Node(Node.NAME, "param"));
    FlowScope blindScope = new FlowScope();
    boolean outcome = true;
    
    FlowScope result = interpreter.getPreciserScopeKnowingConditionOutcome(condition, blindScope, outcome);
    
    // Assert statements here
  }
  
  @Test
  public void testGetPreciserScopeKnowingConditionOutcome_withCallNodeWithObjectRestricterAndOutcomeFalse_shouldNotRestrictParameterToObject() {
    Node condition = new Node(Node.CALL, new Node(Node.GETPROP, new Node(Node.NAME, "goog"), new Node(Node.STRING, "isObject")), new Node(Node.NAME, "param"));
    FlowScope blindScope = new FlowScope();
    boolean outcome = false;
    
    FlowScope result = interpreter.getPreciserScopeKnowingConditionOutcome(condition, blindScope, outcome);
    
    // Assert statements here
  }
  
  @Test
  public void testGetPreciserScopeKnowingConditionOutcome_withCallNodeWithOtherRestricter_shouldNotRestrictParameter() {
    Node condition = new Node(Node.CALL, new Node(Node.GETPROP, new Node(Node.NAME, "goog"), new Node(Node.STRING, "isString")), new Node(Node.NAME, "param"));
    FlowScope blindScope = new FlowScope();
    boolean outcome = true;
    
    FlowScope result = interpreter.getPreciserScopeKnowingConditionOutcome(condition, blindScope, outcome);
    
    // Assert statements here
  }
  
  @Test
  public void testRestrictParameter_withValidParametersAndOutcomeTrue_shouldRestrictParameter() {
    Node parameter = new Node(Node.NAME, "param");
    JSType type = new JSType();
    FlowScope blindScope = new FlowScope();
    Function<TypeRestriction, JSType> restriction = interpreter.getRestricters().get("isDef");
    boolean outcome = true;
    
    FlowScope result = interpreter.restrictParameter(parameter, type, blindScope, restriction, outcome);
    
    // Assert statements here
  }
  
  @Test
  public void testRestrictParameter_withValidParametersAndOutcomeFalse_shouldNotRestrictParameter() {
    Node parameter = new Node(Node.NAME, "param");
    JSType type = new JSType();
    FlowScope blindScope = new FlowScope();
    Function<TypeRestriction, JSType> restriction = interpreter.getRestricters().get("isDef");
    boolean outcome = false;
    
    FlowScope result = interpreter.restrictParameter(parameter, type, blindScope, restriction, outcome);
    
    // Assert statements here
  }
  
  @Test
  public void testRestrictParameter_withNullType_shouldNotRestrictParameter() {
    Node parameter = new Node(Node.NAME, "param");
    JSType type = null;
    FlowScope blindScope = new FlowScope();
    Function<TypeRestriction, JSType> restriction = interpreter.getRestricters().get("isDef");
    boolean outcome = true;
    
    FlowScope result = interpreter.restrictParameter(parameter, type, blindScope, restriction, outcome);
    
    // Assert statements here
  }
  
  private static class TypeRestriction {
    private final JSType type;
    private final boolean outcome;

    private TypeRestriction(JSType type, boolean outcome) {
      this.type = type;
      this.outcome = outcome;
    }
  }
  
}