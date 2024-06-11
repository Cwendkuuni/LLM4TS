import org.junit.Test;
import static org.junit.Assert.*;

public class ClosureReverseAbstractInterpreterTest {

  @Test
  public void testRestrictToArrayVisitor_caseTopType() {
    // Typical use case
    JSType topType = new JSType();
    JSType result = restrictToArrayVisitor.caseTopType(topType);
    assertNull(result);

    // Edge case
    JSType nullType = null;
    result = restrictToArrayVisitor.caseTopType(nullType);
    assertNull(result);
  }

  @Test
  public void testRestrictToArrayVisitor_caseObjectType() {
    // Typical use case
    ObjectType type = new ObjectType();
    JSType arrayType = new JSType(ARRAY_TYPE);
    JSType result = restrictToArrayVisitor.caseObjectType(type);
    assertEquals(arrayType, result);

    // Edge case
    JSType nullType = null;
    result = restrictToArrayVisitor.caseObjectType(nullType);
    assertNull(result);
  }

  @Test
  public void testRestrictToNotArrayVisitor_caseObjectType() {
    // Typical use case
    ObjectType type = new ObjectType();
    JSType arrayType = new JSType(ARRAY_TYPE);
    JSType result = restrictToNotArrayVisitor.caseObjectType(type);
    assertNull(result);

    // Edge case
    JSType nullType = null;
    result = restrictToNotArrayVisitor.caseObjectType(nullType);
    assertNull(result);
  }

  @Test
  public void testRestrictToObjectVisitor_caseTopType() {
    // Typical use case
    JSType topType = new JSType();
    JSType noObjectType = new JSType(NO_OBJECT_TYPE);
    JSType result = restrictToObjectVisitor.caseTopType(topType);
    assertEquals(noObjectType, result);

    // Edge case
    JSType nullType = null;
    result = restrictToObjectVisitor.caseTopType(nullType);
    assertEquals(noObjectType, result);
  }

  @Test
  public void testRestrictToObjectVisitor_caseObjectType() {
    // Typical use case
    ObjectType type = new ObjectType();
    JSType result = restrictToObjectVisitor.caseObjectType(type);
    assertEquals(type, result);

    // Edge case
    JSType nullType = null;
    result = restrictToObjectVisitor.caseObjectType(nullType);
    assertNull(result);
  }

  @Test
  public void testRestrictToObjectVisitor_caseFunctionType() {
    // Typical use case
    FunctionType type = new FunctionType();
    JSType result = restrictToObjectVisitor.caseFunctionType(type);
    assertEquals(type, result);

    // Edge case
    JSType nullType = null;
    result = restrictToObjectVisitor.caseFunctionType(nullType);
    assertNull(result);
  }

  @Test
  public void testRestrictToNotObjectVisitor_caseObjectType() {
    // Typical use case
    ObjectType type = new ObjectType();
    JSType result = restrictToNotObjectVisitor.caseObjectType(type);
    assertNull(result);

    // Edge case
    JSType nullType = null;
    result = restrictToNotObjectVisitor.caseObjectType(nullType);
    assertNull(result);
  }

  @Test
  public void testRestrictToNotObjectVisitor_caseFunctionType() {
    // Typical use case
    FunctionType type = new FunctionType();
    JSType result = restrictToNotObjectVisitor.caseFunctionType(type);
    assertNull(result);

    // Edge case
    JSType nullType = null;
    result = restrictToNotObjectVisitor.caseFunctionType(nullType);
    assertNull(result);
  }

  @Test
  public void testClosureReverseAbstractInterpreter_getPreciserScopeKnowingConditionOutcome() {
    // Typical use case
    Node condition = new Node();
    FlowScope blindScope = new FlowScope();
    boolean outcome = true;
    FlowScope result = closureReverseAbstractInterpreter.getPreciserScopeKnowingConditionOutcome(condition, blindScope, outcome);
    assertNotNull(result);

    // Edge case
    Node condition2 = new Node();
    FlowScope blindScope2 = new FlowScope();
    boolean outcome2 = false;
    FlowScope result2 = closureReverseAbstractInterpreter.getPreciserScopeKnowingConditionOutcome(condition2, blindScope2, outcome2);
    assertNotNull(result2);
  }

  @Test
  public void testRestrictParameter() {
    // Typical use case
    Node parameter = new Node();
    JSType type = new JSType();
    FlowScope blindScope = new FlowScope();
    Function<TypeRestriction, JSType> restriction = new Function<TypeRestriction, JSType>() {
      public JSType apply(TypeRestriction p) {
        if (p.outcome) {
          return p.type;
        } else {
          return null;
        }
      }
    };
    boolean outcome = true;
    FlowScope result = restrictParameter(parameter, type, blindScope, restriction, outcome);
    assertNotNull(result);

    // Edge case
    Node parameter2 = new Node();
    JSType type2 = new JSType();
    FlowScope blindScope2 = new FlowScope();
    Function<TypeRestriction, JSType> restriction2 = new Function<TypeRestriction, JSType>() {
      public JSType apply(TypeRestriction p) {
        if (p.outcome) {
          return p.type;
        } else {
          return null;
        }
      }
    };
    boolean outcome2 = false;
    FlowScope result2 = restrictParameter(parameter2, type2, blindScope2, restriction2, outcome2);
    assertNotNull(result2);
  }

  @Test
  public void testTypeRestriction() {
    // Typical use case
    JSType type = new JSType();
    boolean outcome = true;
    TypeRestriction typeRestriction = new TypeRestriction(type, outcome);
    assertEquals(type, typeRestriction.type);
    assertTrue(typeRestriction.outcome);

    // Edge case
    JSType type2 = new JSType();
    boolean outcome2 = false;
    TypeRestriction typeRestriction2 = new TypeRestriction(type2, outcome2);
    assertEquals(type2, typeRestriction2.type);
    assertFalse(typeRestriction2.outcome);
  }

}