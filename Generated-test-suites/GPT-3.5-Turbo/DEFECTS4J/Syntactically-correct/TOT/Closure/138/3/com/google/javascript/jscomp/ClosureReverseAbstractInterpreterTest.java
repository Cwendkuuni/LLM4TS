import static org.junit.Assert.*;
import org.junit.Test;
import com.google.javascript.jscomp.ClosureReverseAbstractInterpreter;

public class ClosureReverseAbstractInterpreterTest {

  @Test
  public void testRestrictToArrayVisitor() {
    ClosureReverseAbstractInterpreter interpreter = new ClosureReverseAbstractInterpreter();
    
    // typical use case: input type is an array
    JSType inputType = getArrayType();
    JSType expectedType = getArrayType();
    JSType resultType = interpreter.restrictToArrayVisitor.visit(inputType);
    assertEquals(expectedType, resultType);
    
    // edge case: input type is not an array
    inputType = getObjectType();
    expectedType = null;
    resultType = interpreter.restrictToArrayVisitor.visit(inputType);
    assertEquals(expectedType, resultType);
  }

  @Test
  public void testRestrictToNotArrayVisitor() {
    ClosureReverseAbstractInterpreter interpreter = new ClosureReverseAbstractInterpreter();
    
    // typical use case: input type is not an array
    JSType inputType = getObjectType();
    JSType expectedType = getObjectType();
    JSType resultType = interpreter.restrictToNotArrayVisitor.visit(inputType);
    assertEquals(expectedType, resultType);
    
    // edge case: input type is an array
    inputType = getArrayType();
    expectedType = null;
    resultType = interpreter.restrictToNotArrayVisitor.visit(inputType);
    assertEquals(expectedType, resultType);
  }

  @Test
  public void testRestrictToObjectVisitor() {
    ClosureReverseAbstractInterpreter interpreter = new ClosureReverseAbstractInterpreter();
    
    // typical use case: input type is an object
    JSType inputType = getObjectType();
    JSType expectedType = getObjectType();
    JSType resultType = interpreter.restrictToObjectVisitor.visit(inputType);
    assertEquals(expectedType, resultType);
    
    // edge cases: input type is not an object
    inputType = getArrayType();
    expectedType = null;
    resultType = interpreter.restrictToObjectVisitor.visit(inputType);
    assertEquals(expectedType, resultType);
    
    inputType = getPrimitiveType();
    expectedType = null;
    resultType = interpreter.restrictToObjectVisitor.visit(inputType);
    assertEquals(expectedType, resultType);
  }

  @Test
  public void testRestrictToNotObjectVisitor() {
    ClosureReverseAbstractInterpreter interpreter = new ClosureReverseAbstractInterpreter();
    
    // typical use case: input type is not an object
    JSType inputType = getArrayType();
    JSType expectedType = null;
    JSType resultType = interpreter.restrictToNotObjectVisitor.visit(inputType);
    assertEquals(expectedType, resultType);
    
    // edge cases: input type is an object
    inputType = getObjectType();
    expectedType = null;
    resultType = interpreter.restrictToNotObjectVisitor.visit(inputType);
    assertEquals(expectedType, resultType);
    
    inputType = getFunctionType();
    expectedType = null;
    resultType = interpreter.restrictToNotObjectVisitor.visit(inputType);
    assertEquals(expectedType, resultType);
  }

  @Test
  public void testGetPreciserScopeKnowingConditionOutcome() {
    ClosureReverseAbstractInterpreter interpreter = new ClosureReverseAbstractInterpreter();
    Node condition = createConditionNode();
    FlowScope blindScope = createBlindScope();
    boolean outcome = true;
    
    // typical use case
    FlowScope expectedScope = createExpectedScope();
    FlowScope resultScope = interpreter.getPreciserScopeKnowingConditionOutcome(condition, blindScope, outcome);
    assertEquals(expectedScope, resultScope);
    
    // edge case
    condition = createNonMatchingConditionNode();
    expectedScope = blindScope;
    resultScope = interpreter.getPreciserScopeKnowingConditionOutcome(condition, blindScope, outcome);
    assertEquals(expectedScope, resultScope);
  }

  /**
   * Helper method to create a sample array type.
   */
  private JSType getArrayType() {
    // implementation details
  }
  
  /**
   * Helper method to create a sample object type.
   */
  private JSType getObjectType() {
    // implementation details
  }
  
  /**
   * Helper method to create a sample primitive type.
   */
  private JSType getPrimitiveType() {
    // implementation details
  }
  
  /**
   * Helper method to create a sample function type.
   */
  private JSType getFunctionType() {
    // implementation details
  }
  
  /**
   * Helper method to create a sample condition node.
   */
  private Node createConditionNode() {
    // implementation details
  }
  
  /**
   * Helper method to create a sample blind scope.
   */
  private FlowScope createBlindScope() {
    // implementation details
  }
  
  /**
   * Helper method to create a sample expected scope.
   */
  private FlowScope createExpectedScope() {
    // implementation details
  }
  
  /**
   * Helper method to create a sample non-matching condition node.
   */
  private Node createNonMatchingConditionNode() {
    // implementation details
  }
}