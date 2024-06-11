import org.junit.Test;
import static org.junit.Assert.*;

public class ClosureReverseAbstractInterpreterTest {

  @Test
  public void testRestrictToArrayVisitor() {
    ClosureReverseAbstractInterpreter interpreter = new ClosureReverseAbstractInterpreter();
    JSTypeRegistry typeRegistry = new JSTypeRegistry();
    JSType objectType = typeRegistry.createObjectType();
    JSType arrayType = typeRegistry.createArrayType(objectType);
    
    // Test when visitor returns array type
    JSType result1 = interpreter.restrictToArrayVisitor.caseObjectType(objectType);
    assertEquals(arrayType, result1);
    
    // Test when visitor returns null
    JSType result2 = interpreter.restrictToArrayVisitor.caseObjectType(arrayType);
    assertNull(result2);
  }
  
  @Test
  public void testRestrictToNotArrayVisitor() {
    ClosureReverseAbstractInterpreter interpreter = new ClosureReverseAbstractInterpreter();
    JSTypeRegistry typeRegistry = new JSTypeRegistry();
    JSType objectType = typeRegistry.createObjectType();
    JSType arrayType = typeRegistry.createArrayType(objectType);
    
    // Test when visitor returns null
    JSType result1 = interpreter.restrictToNotArrayVisitor.caseObjectType(objectType);
    assertNull(result1);
    
    // Test when visitor returns object type
    JSType result2 = interpreter.restrictToNotArrayVisitor.caseObjectType(arrayType);
    assertEquals(objectType, result2);
  }
  
  @Test
  public void testRestrictToObjectVisitor() {
    ClosureReverseAbstractInterpreter interpreter = new ClosureReverseAbstractInterpreter();
    JSTypeRegistry typeRegistry = new JSTypeRegistry();
    JSType objectType = typeRegistry.createObjectType();
    JSType functionType = typeRegistry.createFunctionType();
    
    // Test when visitor returns object type
    JSType result1 = interpreter.restrictToObjectVisitor.caseObjectType(objectType);
    assertEquals(objectType, result1);
    
    // Test when visitor returns function type
    JSType result2 = interpreter.restrictToObjectVisitor.caseObjectType(functionType);
    assertEquals(functionType, result2);
  }
  
  @Test
  public void testRestrictToNotObjectVisitor() {
    ClosureReverseAbstractInterpreter interpreter = new ClosureReverseAbstractInterpreter();
    JSTypeRegistry typeRegistry = new JSTypeRegistry();
    JSType objectType = typeRegistry.createObjectType();
    JSType functionType = typeRegistry.createFunctionType();
    
    // Test when visitor returns null for object type
    JSType result1 = interpreter.restrictToNotObjectVisitor.caseObjectType(objectType);
    assertNull(result1);
    
    // Test when visitor returns null for function type
    JSType result2 = interpreter.restrictToNotObjectVisitor.caseObjectType(functionType);
    assertNull(result2);
  }

  @Test
  public void testGetPreciserScopeKnowingConditionOutcome() {
    ClosureReverseAbstractInterpreter interpreter = new ClosureReverseAbstractInterpreter();
    CodingConvention convention = new CodingConvention();
    JSTypeRegistry typeRegistry = new JSTypeRegistry();
    
    // Test when condition is a CALL node with valid restrictions
    Node condition1 = new Node();
    Node callee1 = new Node(Token.GETPROP);
    callee1.addChildToFront(new Node(Token.NAME, "goog"));
    callee1.addChildToBack(new Node(Token.STRING, "isDef"));
    condition1.addChildToFront(callee1);
    Node param1 = new Node(Token.NAME, "param1");
    condition1.addChildToBack(param1);
    FlowScope blindScope1 = new FlowScope();
    blindScope1.createChildFlowScope();
    interpreter.getPreciserScopeKnowingConditionOutcome(condition1, blindScope1, true);
    // Assertions
   
    
    // Test when condition is a CALL node with invalid restrictions
    Node condition2 = new Node();
    Node callee2 = new Node(Token.GETPROP);
    callee2.addChildToFront(new Node(Token.NAME, "goog"));
    callee2.addChildToBack(new Node(Token.STRING, "isDef"));
    condition2.addChildToFront(callee2);
    Node param2 = new Node(Token.NAME, "param2");
    condition2.addChildToBack(param2);
    FlowScope blindScope2 = new FlowScope();
    blindScope2.createChildFlowScope();
    interpreter.getPreciserScopeKnowingConditionOutcome(condition2, blindScope2, false);
    // Assertions
    
    // Test when condition is not a CALL node
    Node condition3 = new Node();
    Node callee3 = new Node(Token.GETPROP);
    callee3.addChildToFront(new Node(Token.NAME, "notGoog"));
    callee3.addChildToBack(new Node(Token.STRING, "isDef"));
    condition3.addChildToFront(callee3);
    Node param3 = new Node(Token.NAME, "param3");
    condition3.addChildToBack(param3);
    FlowScope blindScope3 = new FlowScope();
    blindScope3.createChildFlowScope();
    interpreter.getPreciserScopeKnowingConditionOutcome(condition3, blindScope3, true);
    // Assertions
  }
  
  @Test
  public void testRestrictParameter() {
    ClosureReverseAbstractInterpreter interpreter = new ClosureReverseAbstractInterpreter();
    Node parameter = new Node(Token.NAME, "param");
    JSTypeRegistry typeRegistry = new JSTypeRegistry();
    JSType restrictedType = typeRegistry.createTypeByName("RestrictedType");
    FlowScope blindScope = new FlowScope();
    
    // Test when restriction outcome is true
    Function<TypeRestriction, JSType> restriction1 = new Function<TypeRestriction, JSType>(){
      public JSType apply(TypeRestriction p) {
        if (p.outcome) {
          return restrictedType;
        } else {
          return null;
        }
      }
    };
    FlowScope result1 = interpreter.restrictParameter(parameter, restrictedType, blindScope, restriction1, true);
    // Assertions
    
    // Test when restriction outcome is false
    Function<TypeRestriction, JSType> restriction2 = new Function<TypeRestriction, JSType>(){
      public JSType apply(TypeRestriction p) {
        if (p.outcome) {
          return restrictedType;
        } else {
          return null;
        }
      }
    };
    FlowScope result2 = interpreter.restrictParameter(parameter, restrictedType, blindScope, restriction2, false);
    // Assertions
  }
  
  @Test
  public void testTypeRestriction() {
    JSTypeRegistry typeRegistry = new JSTypeRegistry();
    JSType restrictedType = typeRegistry.createTypeByName("RestrictedType");
    TypeRestriction restriction = new TypeRestriction(restrictedType, true);
    assertEquals(restrictedType, restriction.type);
    assertTrue(restriction.outcome);
  }
}