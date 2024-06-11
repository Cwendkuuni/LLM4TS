import static org.junit.Assert.*;
import org.junit.Test;

public class ClosureReverseAbstractInterpreterTest {

  @Test
  public void testRestrictToArrayVisitor_caseTopType() {
    ClosureReverseAbstractInterpreter interpreter = new ClosureReverseAbstractInterpreter();
    JSType topType = new JSType();
    JSType result = interpreter.restrictToArrayVisitor.caseTopType(topType);
    assertNull(result);
  }

  @Test
  public void testRestrictToArrayVisitor_caseObjectType() {
    ClosureReverseAbstractInterpreter interpreter = new ClosureReverseAbstractInterpreter();
    JSType objectType = new ObjectType();
    JSType arrayType = new JSType(ARRAY_TYPE);
    interpreter.addNativeType(arrayType);
    JSType result = interpreter.restrictToArrayVisitor.caseObjectType(objectType);
    assertEquals(arrayType, result);
  }

  @Test
  public void testRestrictToNotArrayVisitor_caseObjectType() {
    ClosureReverseAbstractInterpreter interpreter = new ClosureReverseAbstractInterpreter();
    JSType objectType = new ObjectType();
    JSType arrayType = new JSType(ARRAY_TYPE);
    interpreter.addNativeType(arrayType);
    JSType result = interpreter.restrictToNotArrayVisitor.caseObjectType(objectType);
    assertNull(result);
  }

  @Test
  public void testRestrictToObjectVisitor_caseTopType() {
    ClosureReverseAbstractInterpreter interpreter = new ClosureReverseAbstractInterpreter();
    JSType topType = new JSType();
    JSType result = interpreter.restrictToObjectVisitor.caseTopType(topType);
    assertEquals(NO_OBJECT_TYPE, result);
  }

  @Test
  public void testRestrictToObjectVisitor_caseObjectType() {
    ClosureReverseAbstractInterpreter interpreter = new ClosureReverseAbstractInterpreter();
    JSType objectType = new ObjectType();
    JSType result = interpreter.restrictToObjectVisitor.caseObjectType(objectType);
    assertEquals(objectType, result);
  }

  @Test
  public void testRestrictToObjectVisitor_caseFunctionType() {
    ClosureReverseAbstractInterpreter interpreter = new ClosureReverseAbstractInterpreter();
    FunctionType functionType = new FunctionType();
    JSType result = interpreter.restrictToObjectVisitor.caseFunctionType(functionType);
    assertEquals(functionType, result);
  }

  @Test
  public void testRestrictToNotObjectVisitor_caseObjectType() {
    ClosureReverseAbstractInterpreter interpreter = new ClosureReverseAbstractInterpreter();
    JSType objectType = new ObjectType();
    JSType result = interpreter.restrictToNotObjectVisitor.caseObjectType(objectType);
    assertNull(result);
  }

  @Test
  public void testRestrictToNotObjectVisitor_caseFunctionType() {
    ClosureReverseAbstractInterpreter interpreter = new ClosureReverseAbstractInterpreter();
    FunctionType functionType = new FunctionType();
    JSType result = interpreter.restrictToNotObjectVisitor.caseFunctionType(functionType);
    assertNull(result);
  }

  @Test
  public void testClosureReverseAbstractInterpreter_getPreciserScopeKnowingConditionOutcome() {
    ClosureReverseAbstractInterpreter interpreter = new ClosureReverseAbstractInterpreter();
    Node condition = new Node();
    FlowScope blindScope = new FlowScope();
    boolean outcome = true;
    FlowScope result = interpreter.getPreciserScopeKnowingConditionOutcome(condition, blindScope, outcome);
    assertNotNull(result);
  }

  @Test
  public void testClosureReverseAbstractInterpreter_restrictParameter() {
    ClosureReverseAbstractInterpreter interpreter = new ClosureReverseAbstractInterpreter();
    Node parameter = new Node();
    JSType type = new JSType();
    FlowScope blindScope = new FlowScope();
    Function<TypeRestriction, JSType> restriction = new Function<TypeRestriction, JSType>() {
      public JSType apply(TypeRestriction p) {
        if (p.outcome) {
          return new JSType();
        } else {
          return null;
        }
      }
    };
    boolean outcome = true;
    FlowScope result = interpreter.restrictParameter(parameter, type, blindScope, restriction, outcome);
    assertNotNull(result);
  }

  @Test
  public void testClosureReverseAbstractInterpreter_TypeRestriction_constructor() {
    JSType type = new JSType();
    boolean outcome = true;
    ClosureReverseAbstractInterpreter.TypeRestriction restriction = new ClosureReverseAbstractInterpreter.TypeRestriction(type, outcome);
    assertNotNull(restriction);
    assertEquals(type, restriction.type);
    assertEquals(outcome, restriction.outcome);
  }
}