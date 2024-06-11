import static org.junit.Assert.*;
import org.junit.Test;

public class ClosureReverseAbstractInterpreterTest {

  // Test restrictToArrayVisitor
  @Test
  public void testRestrictToArrayVisitor() {
    ClosureReverseAbstractInterpreter interpreter = new ClosureReverseAbstractInterpreter();
    JSType type = interpreter.restrictToArrayVisitor.caseTopType(null);
    assertNull(type);
    
    ObjectType objectType = new ObjectType();
    JSType arrayType = new JSType(ARRAY_TYPE);
    objectType.setSubtype(arrayType);
    type = interpreter.restrictToArrayVisitor.caseObjectType(objectType);
    assertEquals(arrayType, type);
  }

  // Test restrictToNotArrayVisitor
  @Test
  public void testRestrictToNotArrayVisitor() {
    ClosureReverseAbstractInterpreter interpreter = new ClosureReverseAbstractInterpreter();
    ObjectType objectType = new ObjectType();
    JSType arrayType = new JSType(ARRAY_TYPE);
    objectType.setSubtype(arrayType);
    JSType type = interpreter.restrictToNotArrayVisitor.caseObjectType(objectType);
    assertNull(type);
  }

  // Test restrictToObjectVisitor
  @Test
  public void testRestrictToObjectVisitor() {
    ClosureReverseAbstractInterpreter interpreter = new ClosureReverseAbstractInterpreter();
    JSType topType = new JSType(TOP_TYPE);
    JSType objectType = new JSType(OBJECT_TYPE);
    JSType functionType = new JSType(FUNCTION_TYPE);
    interpreter.getNativeType(NO_OBJECT_TYPE);
    
    JSType type = interpreter.restrictToObjectVisitor.caseTopType(topType);
    assertEquals(objectType, type);
    
    type = interpreter.restrictToObjectVisitor.caseObjectType(objectType);
    assertEquals(objectType, type);
    
    type = interpreter.restrictToObjectVisitor.caseFunctionType(functionType);
    assertEquals(functionType, type);
  }

  // Test restrictToNotObjectVisitor
  @Test
  public void testRestrictToNotObjectVisitor() {
    ClosureReverseAbstractInterpreter interpreter = new ClosureReverseAbstractInterpreter();
    ObjectType objectType = new ObjectType();
    JSType arrayType = new JSType(ARRAY_TYPE);
    objectType.setSubtype(arrayType);
    JSType type = interpreter.restrictToNotObjectVisitor.caseObjectType(objectType);
    assertNull(type);
    
    JSType functionType = new JSType(FUNCTION_TYPE);
    type = interpreter.restrictToNotObjectVisitor.caseFunctionType(functionType);
    assertNull(type);
  }

  // Test getPreciserScopeKnowingConditionOutcome
  @Test
  public void testGetPreciserScopeKnowingConditionOutcome() {
    ClosureReverseAbstractInterpreter interpreter = new ClosureReverseAbstractInterpreter();
    Node condition = new Node();
    FlowScope blindScope = new FlowScope();
    boolean outcome = true;
    FlowScope scope = interpreter.getPreciserScopeKnowingConditionOutcome(condition, blindScope, outcome);
    assertNotNull(scope);
  }

  // Test restrictParameter
  @Test
  public void testRestrictParameter() {
    ClosureReverseAbstractInterpreter interpreter = new ClosureReverseAbstractInterpreter();
    Node parameter = new Node();
    JSType type = new JSType(STRING);
    FlowScope blindScope = new FlowScope();
    boolean outcome = true;
    Function<TypeRestriction, JSType> restriction = new Function<TypeRestriction, JSType>() {
      public JSType apply(TypeRestriction p) {
        return getRestrictedByTypeOfResult(p.type, "string", p.outcome);
      }
    };
    FlowScope scope = interpreter.restrictParameter(parameter, type, blindScope, restriction, outcome);
    assertNotNull(scope);
  }
}