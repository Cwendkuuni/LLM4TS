import static org.junit.Assert.*;
import org.junit.Test;

public class ClosureReverseAbstractInterpreterTest {

  @Test
  public void testRestrictToArrayVisitor_caseTopType() {
    ClosureReverseAbstractInterpreter closureRAI 
        = new ClosureReverseAbstractInterpreter();
    JSType topType = new JSType();
    JSType result = closureRAI.restrictToArrayVisitor.caseTopType(topType);
    assertNull(result);
  }

  @Test
  public void testRestrictToArrayVisitor_caseObjectType() {
    ClosureReverseAbstractInterpreter closureRAI 
        = new ClosureReverseAbstractInterpreter();
    JSType objectType = new ObjectType();
    objectType.addProperty("arrayType", new JSType(ARRAY_TYPE));
    JSType result = closureRAI.restrictToArrayVisitor.caseObjectType(objectType);
    assertEquals(new JSType(ARRAY_TYPE), result);
  }

  @Test
  public void testRestrictToNotArrayVisitor_caseObjectType() {
    ClosureReverseAbstractInterpreter closureRAI 
        = new ClosureReverseAbstractInterpreter();
    JSType objectType = new ObjectType();
    objectType.addProperty("arrayType", new JSType(ARRAY_TYPE));
    JSType result = closureRAI.restrictToNotArrayVisitor.caseObjectType(objectType);
    assertNull(result);
  }

  @Test
  public void testRestrictToObjectVisitor_caseTopType() {
    ClosureReverseAbstractInterpreter closureRAI 
        = new ClosureReverseAbstractInterpreter();
    JSType topType = new JSType();
    JSType result = closureRAI.restrictToObjectVisitor.caseTopType(topType);
    assertEquals(new JSType(NO_OBJECT_TYPE), result);
  }

  @Test
  public void testRestrictToObjectVisitor_caseObjectType() {
    ClosureReverseAbstractInterpreter closureRAI 
        = new ClosureReverseAbstractInterpreter();
    JSType objectType = new ObjectType();
    JSType result = closureRAI.restrictToObjectVisitor.caseObjectType(objectType);
    assertEquals(new JSType(OBJECT_TYPE), result);
  }

  @Test
  public void testRestrictToObjectVisitor_caseFunctionType() {
    ClosureReverseAbstractInterpreter closureRAI 
        = new ClosureReverseAbstractInterpreter();
    FunctionType functionType = new FunctionType();
    JSType result = closureRAI.restrictToObjectVisitor.caseFunctionType(functionType);
    assertEquals(new JSType(FUNCTION_TYPE), result);
  }

  @Test
  public void testRestrictToNotObjectVisitor_caseObjectType() {
    ClosureReverseAbstractInterpreter closureRAI 
        = new ClosureReverseAbstractInterpreter();
    JSType objectType = new ObjectType();
    JSType result = closureRAI.restrictToNotObjectVisitor.caseObjectType(objectType);
    assertNull(result);
  }

  @Test
  public void testRestrictToNotObjectVisitor_caseFunctionType() {
    ClosureReverseAbstractInterpreter closureRAI 
        = new ClosureReverseAbstractInterpreter();
    FunctionType functionType = new FunctionType();
    JSType result = closureRAI.restrictToNotObjectVisitor.caseFunctionType(functionType);
    assertNull(result);
  }

  @Test
  public void testGetPreciserScopeKnowingConditionOutcome() {
    ClosureReverseAbstractInterpreter closureRAI 
        = new ClosureReverseAbstractInterpreter();
    Node condition = new Node();
    FlowScope blindScope = new FlowScope();
    boolean outcome = true;
    FlowScope result = closureRAI.getPreciserScopeKnowingConditionOutcome(condition, blindScope, outcome);
    assertEquals(blindScope, result);
  }

  @Test
  public void testRestrictParameter() {
    ClosureReverseAbstractInterpreter closureRAI 
        = new ClosureReverseAbstractInterpreter();
    Node parameter = new Node();
    JSType type = new JSType();
    FlowScope blindScope = new FlowScope();
    boolean outcome = true;
    Function<TypeRestriction, JSType> restriction = new Function<TypeRestriction, JSType>() {
        public JSType apply(TypeRestriction p) {
          if (p.outcome) {
            return getRestrictedWithoutUndefined(p.type);
          } else {
            return null;
          }
         }
      };
    FlowScope result = closureRAI.restrictParameter(parameter, type, blindScope, restriction, outcome);
    assertEquals(blindScope, result);
  }

  private static class JSType extends Type {}

  private static class ObjectType extends Type {}

  private static class FunctionType extends Type {}

  private static class Type {}

}