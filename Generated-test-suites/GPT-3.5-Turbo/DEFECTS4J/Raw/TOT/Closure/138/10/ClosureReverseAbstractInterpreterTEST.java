import org.junit.Test;
import static org.junit.Assert.*;

public class ClosureReverseAbstractInterpreterTest {

  @Test
  public void testRestrictToArrayVisitor() {
    ClosureReverseAbstractInterpreter closureInterpreter = new ClosureReverseAbstractInterpreter();
    RestrictByTrueTypeOfResultVisitor restrictToArrayVisitor = closureInterpreter.restrictToArrayVisitor;

    // Typical use case: type is an array
    JSType type = new JSType();
    JSType arrayType = new JSType();
    type.setSubtype(arrayType);
    JSType result = restrictToArrayVisitor.visit(type);
    assertEquals(arrayType, result);

    // Edge case: type is not an array
    type = new JSType();
    result = restrictToArrayVisitor.visit(type);
    assertNull(result);

    // Edge case: type is top type
    type = new JSType();
    result = restrictToArrayVisitor.visit(type);
    assertEquals(type, result);
  }

  @Test
  public void testRestrictToNotArrayVisitor() {
    ClosureReverseAbstractInterpreter closureInterpreter = new ClosureReverseAbstractInterpreter();
    RestrictByFalseTypeOfResultVisitor restrictToNotArrayVisitor = closureInterpreter.restrictToNotArrayVisitor;

    // Typical use case: type is not an array
    JSType type = new JSType();
    JSType arrayType = new JSType();
    type.setSubtype(arrayType);
    JSType result = restrictToNotArrayVisitor.visit(type);
    assertNull(result);

    // Edge case: type is an array
    type = new JSType();
    result = restrictToNotArrayVisitor.visit(type);
    assertEquals(type, result);
  }

  @Test
  public void testRestrictToObjectVisitor() {
    ClosureReverseAbstractInterpreter closureInterpreter = new ClosureReverseAbstractInterpreter();
    RestrictByTrueTypeOfResultVisitor restrictToObjectVisitor = closureInterpreter.restrictToObjectVisitor;

    // Typical use case: type is an object
    JSType type = new JSType();
    JSType objectType = new JSType();
    type.setSubtype(objectType);
    JSType result = restrictToObjectVisitor.visit(type);
    assertEquals(type, result);

    // Edge case: type is a function
    type = new JSType();
    JSType functionType = new JSType();
    type.setSubtype(functionType);
    result = restrictToObjectVisitor.visit(type);
    assertEquals(type, result);

    // Edge case: type is null
    type = new JSType();
    result = restrictToObjectVisitor.visit(type);
    assertEquals(NO_OBJECT_TYPE, result);

    // Edge case: type is top type
    type = new JSType();
    result = restrictToObjectVisitor.visit(type);
    assertEquals(NO_OBJECT_TYPE, result);
  }

  @Test
  public void testRestrictToNotObjectVisitor() {
    ClosureReverseAbstractInterpreter closureInterpreter = new ClosureReverseAbstractInterpreter();
    RestrictByFalseTypeOfResultVisitor restrictToNotObjectVisitor = closureInterpreter.restrictToNotObjectVisitor;

    // Typical use case: type is an object
    JSType type = new JSType();
    JSType objectType = new JSType();
    type.setSubtype(objectType);
    JSType result = restrictToNotObjectVisitor.visit(type);
    assertNull(result);

    // Edge case: type is a function
    type = new JSType();
    JSType functionType = new JSType();
    type.setSubtype(functionType);
    result = restrictToNotObjectVisitor.visit(type);
    assertNull(result);
  }

  @Test
  public void testRestricters() {
    ClosureReverseAbstractInterpreter closureInterpreter = new ClosureReverseAbstractInterpreter();

    // isDef: Typical use case
    TypeRestriction typeRestriction = new TypeRestriction(new JSType(), true);
    JSType result = closureInterpreter.restricters.get("isDef").apply(typeRestriction);
    assertEquals(typeRestriction.type, result);

    // isDef: Edge case
    typeRestriction = new TypeRestriction(new JSType(), false);
    result = closureInterpreter.restricters.get("isDef").apply(typeRestriction);
    assertNull(result);

    // isNull: Typical use case
    typeRestriction = new TypeRestriction(new JSType(), true);
    result = closureInterpreter.restricters.get("isNull").apply(typeRestriction);
    assertEquals(closureInterpreter.getNativeType(NULL_TYPE), result);

    // isNull: Edge case
    typeRestriction = new TypeRestriction(new JSType(), false);
    result = closureInterpreter.restricters.get("isNull").apply(typeRestriction);
    assertEquals(typeRestriction.type, result);

    // isDefAndNotNull: Typical use case
    typeRestriction = new TypeRestriction(new JSType(), true);
    result = closureInterpreter.restricters.get("isDefAndNotNull").apply(typeRestriction);
    assertEquals(closureInterpreter.getRestrictedWithoutUndefined(closureInterpreter.getRestrictedWithoutNull(typeRestriction.type)), result);

    // isDefAndNotNull: Edge case
    typeRestriction = new TypeRestriction(new JSType(), false);
    result = closureInterpreter.restricters.get("isDefAndNotNull").apply(typeRestriction);
    assertNull(result);
  }

  @Test
  public void testGetPreciserScopeKnowingConditionOutcome() {
    ClosureReverseAbstractInterpreter closureInterpreter = new ClosureReverseAbstractInterpreter();
    Node condition = new Node();
    FlowScope blindScope = new FlowScope();
    boolean outcome = true;

    // Typical use case
    FlowScope result = closureInterpreter.getPreciserScopeKnowingConditionOutcome(condition, blindScope, outcome);
    assertNotNull(result);

    // Edge case
    condition = new Node();
    blindScope = new FlowScope();
    outcome = false;
    result = closureInterpreter.getPreciserScopeKnowingConditionOutcome(condition, blindScope, outcome);
    assertNotNull(result);
  }

  @Test
  public void testRestrictParameter() {
    ClosureReverseAbstractInterpreter closureInterpreter = new ClosureReverseAbstractInterpreter();
    Node parameter = new Node();
    JSType type = new JSType();
    FlowScope blindScope = new FlowScope();
    boolean outcome = true;
    Function<TypeRestriction, JSType> restriction = closureInterpreter.restricters.get("isDef");

    // Typical use case
    FlowScope result = closureInterpreter.restrictParameter(parameter, type, blindScope, restriction, outcome);
    assertNotNull(result);

    // Edge case
    outcome = false;
    result = closureInterpreter.restrictParameter(parameter, type, blindScope, restriction, outcome);
    assertNotNull(result);
  }
}