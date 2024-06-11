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
  public void testGetPreciserScopeKnowingConditionOutcome_isDef() {
    Node condition = createNode("goog.isDef(param)");
    FlowScope blindScope = createFlowScope();
    boolean outcome = true;
    
    FlowScope actual = interpreter.getPreciserScopeKnowingConditionOutcome(condition, blindScope, outcome);
    
    // assert statements
  }
  
  @Test
  public void testGetPreciserScopeKnowingConditionOutcome_isNull() {
    Node condition = createNode("goog.isNull(param)");
    FlowScope blindScope = createFlowScope();
    boolean outcome = true;
    
    FlowScope actual = interpreter.getPreciserScopeKnowingConditionOutcome(condition, blindScope, outcome);
    
    // assert statements
  }
  
  @Test
  public void testGetPreciserScopeKnowingConditionOutcome_isDefAndNotNull() {
    Node condition = createNode("goog.isDefAndNotNull(param)");
    FlowScope blindScope = createFlowScope();
    boolean outcome = true;
    
    FlowScope actual = interpreter.getPreciserScopeKnowingConditionOutcome(condition, blindScope, outcome);
    
    // assert statements
  }
  
  @Test
  public void testGetPreciserScopeKnowingConditionOutcome_isString() {
    Node condition = createNode("goog.isString(param)");
    FlowScope blindScope = createFlowScope();
    boolean outcome = true;
    
    FlowScope actual = interpreter.getPreciserScopeKnowingConditionOutcome(condition, blindScope, outcome);
    
    // assert statements
  }
  
  @Test
  public void testGetPreciserScopeKnowingConditionOutcome_isBoolean() {
    Node condition = createNode("goog.isBoolean(param)");
    FlowScope blindScope = createFlowScope();
    boolean outcome = true;
    
    FlowScope actual = interpreter.getPreciserScopeKnowingConditionOutcome(condition, blindScope, outcome);
    
    // assert statements
  }
  
  @Test
  public void testGetPreciserScopeKnowingConditionOutcome_isNumber() {
    Node condition = createNode("goog.isNumber(param)");
    FlowScope blindScope = createFlowScope();
    boolean outcome = true;
    
    FlowScope actual = interpreter.getPreciserScopeKnowingConditionOutcome(condition, blindScope, outcome);
    
    // assert statements
  }
  
  @Test
  public void testGetPreciserScopeKnowingConditionOutcome_isFunction() {
    Node condition = createNode("goog.isFunction(param)");
    FlowScope blindScope = createFlowScope();
    boolean outcome = true;
    
    FlowScope actual = interpreter.getPreciserScopeKnowingConditionOutcome(condition, blindScope, outcome);
    
    // assert statements
  }
  
  @Test
  public void testGetPreciserScopeKnowingConditionOutcome_isArray() {
    Node condition = createNode("goog.isArray(param)");
    FlowScope blindScope = createFlowScope();
    boolean outcome = true;
    
    FlowScope actual = interpreter.getPreciserScopeKnowingConditionOutcome(condition, blindScope, outcome);
    
    // assert statements
  }
  
  @Test
  public void testGetPreciserScopeKnowingConditionOutcome_isObject() {
    Node condition = createNode("goog.isObject(param)");
    FlowScope blindScope = createFlowScope();
    boolean outcome = true;
    
    FlowScope actual = interpreter.getPreciserScopeKnowingConditionOutcome(condition, blindScope, outcome);
    
    // assert statements
  }
  
  private Node createNode(String code) {
    // create and return new Node
  }
  
  private FlowScope createFlowScope() {
    // create and return new FlowScope
  } 
}