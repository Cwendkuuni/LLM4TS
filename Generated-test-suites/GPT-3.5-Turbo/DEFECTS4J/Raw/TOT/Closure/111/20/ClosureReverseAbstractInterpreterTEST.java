import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.jstype.JSType;
import com.google.javascript.jscomp.CodingConvention;
import com.google.javascript.rhino.jstype.JSTypeRegistry;

public class ClosureReverseAbstractInterpreterTest {

  private ClosureReverseAbstractInterpreter rai;
  
  @Before
  public void setUp() {
    CodingConvention convention = new CodingConvention();
    JSTypeRegistry typeRegistry = new JSTypeRegistry();
    rai = new ClosureReverseAbstractInterpreter(convention, typeRegistry);
  }
  
  // Test case for getPreciserScopeKnowingConditionOutcome() method
  
  @Test
  public void testGetPreciserScopeKnowingConditionOutcome() {
    FlowScope flowScope = new FlowScope();
    
    // Test when condition is "goog.isDef(variable)"
    Node condition1 = Node.newString(Token.STRING, "goog.isDef(variable)");
    boolean outcome1 = true;
    FlowScope result1 = rai.getPreciserScopeKnowingConditionOutcome(condition1, flowScope, outcome1);
    // Assert the outcome
    assertTrue(result1.isPresent(variable)); // Test typical use case
    
    boolean outcome2 = false;
    FlowScope result2 = rai.getPreciserScopeKnowingConditionOutcome(condition1, flowScope, outcome2);
    // Assert the outcome
    assertFalse(result2.isPresent(variable)); // Test typical use case
    
    // Test when condition is "goog.isNull(variable)"
    Node condition2 = Node.newString(Token.STRING, "goog.isNull(variable)");
    boolean outcome3 = true;
    FlowScope result3 = rai.getPreciserScopeKnowingConditionOutcome(condition2, flowScope, outcome3);
    // Assert the outcome
    assertTrue(result3.isPresent(variable)); // Test typical use case
    
    boolean outcome4 = false;
    FlowScope result4 = rai.getPreciserScopeKnowingConditionOutcome(condition2, flowScope, outcome4);
    // Assert the outcome
    assertFalse(result4.isPresent(variable)); // Test typical use case
    
    // Test when condition is "goog.isDefAndNotNull(variable)"
    Node condition3 = Node.newString(Token.STRING, "goog.isDefAndNotNull(variable)");
    boolean outcome5 = true;
    FlowScope result5 = rai.getPreciserScopeKnowingConditionOutcome(condition3, flowScope, outcome5);
    // Assert the outcome
    assertTrue(result5.isPresent(variable)); // Test typical use case
    
    boolean outcome6 = false;
    FlowScope result6 = rai.getPreciserScopeKnowingConditionOutcome(condition3, flowScope, outcome6);
    // Assert the outcome
    assertFalse(result6.isPresent(variable)); // Test typical use case
    
    // Test when condition is "goog.isString(variable)"
    Node condition4 = Node.newString(Token.STRING, "goog.isString(variable)");
    boolean outcome7 = true;
    FlowScope result7 = rai.getPreciserScopeKnowingConditionOutcome(condition4, flowScope, outcome7);
    // Assert the outcome
    assertTrue(result7.isPresent(variable)); // Test typical use case
    
    boolean outcome8 = false;
    FlowScope result8 = rai.getPreciserScopeKnowingConditionOutcome(condition4, flowScope, outcome8);
    // Assert the outcome
    assertFalse(result8.isPresent(variable)); // Test typical use case
    
    // Test when condition is "goog.isBoolean(variable)"
    Node condition5 = Node.newString(Token.STRING, "goog.isBoolean(variable)");
    boolean outcome9 = true;
    FlowScope result9 = rai.getPreciserScopeKnowingConditionOutcome(condition5, flowScope, outcome9);
    // Assert the outcome
    assertTrue(result9.isPresent(variable)); // Test typical use case
    
    boolean outcome10 = false;
    FlowScope result10 = rai.getPreciserScopeKnowingConditionOutcome(condition5, flowScope, outcome10);
    // Assert the outcome
    assertFalse(result10.isPresent(variable)); // Test typical use case
    
    // Test when condition is "goog.isNumber(variable)"
    Node condition6 = Node.newString(Token.STRING, "goog.isNumber(variable)");
    boolean outcome11 = true;
    FlowScope result11 = rai.getPreciserScopeKnowingConditionOutcome(condition6, flowScope, outcome11);
    // Assert the outcome
    assertTrue(result11.isPresent(variable)); // Test typical use case
    
    boolean outcome12 = false;
    FlowScope result12 = rai.getPreciserScopeKnowingConditionOutcome(condition6, flowScope, outcome12);
    // Assert the outcome
    assertFalse(result12.isPresent(variable)); // Test typical use case
    
    // Test when condition is "goog.isFunction(variable)"
    Node condition7 = Node.newString(Token.STRING, "goog.isFunction(variable)");
    boolean outcome13 = true;
    FlowScope result13 = rai.getPreciserScopeKnowingConditionOutcome(condition7, flowScope, outcome13);
    // Assert the outcome
    assertTrue(result13.isPresent(variable)); // Test typical use case
    
    boolean outcome14 = false;
    FlowScope result14 = rai.getPreciserScopeKnowingConditionOutcome(condition7, flowScope, outcome14);
    // Assert the outcome
    assertFalse(result14.isPresent(variable)); // Test typical use case
    
    // Test when condition is "goog.isArray(variable)"
    Node condition8 = Node.newString(Token.STRING, "goog.isArray(variable)");
    boolean outcome15 = true;
    FlowScope result15 = rai.getPreciserScopeKnowingConditionOutcome(condition8, flowScope, outcome15);
    // Assert the outcome
    assertTrue(result15.isSubtype(variable)); // Test typical use case
    
    boolean outcome16 = false;
    FlowScope result16 = rai.getPreciserScopeKnowingConditionOutcome(condition8, flowScope, outcome16);
    // Assert the outcome
    assertFalse(result16.isSubtype(variable)); // Test typical use case
    
    // Test when condition is "goog.isObject(variable)"
    Node condition9 = Node.newString(Token.STRING, "goog.isObject(variable)");
    boolean outcome17 = true;
    FlowScope result17 = rai.getPreciserScopeKnowingConditionOutcome(condition9, flowScope, outcome17);
    // Assert the outcome
    assertTrue(result17.isSubtype(variable)); // Test typical use case
    
    boolean outcome18 = false;
    FlowScope result18 = rai.getPreciserScopeKnowingConditionOutcome(condition9, flowScope, outcome18);
    // Assert the outcome
    assertFalse(result18.isSubtype(variable)); // Test typical use case
  }
}