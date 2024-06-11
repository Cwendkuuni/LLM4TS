import static org.junit.Assert.*;
import org.junit.Test;

public class ClosureReverseAbstractInterpreterTest {

  @Test
  public void testRestrictToArrayVisitor() {
    ClosureReverseAbstractInterpreter rai = new ClosureReverseAbstractInterpreter();

    // Typical use case: When goog.isArray returns true
    JSType result = rai.restrictToArrayVisitor.caseObjectType(new ObjectType());
    assertEquals("ARRAY_TYPE", ARRAY_TYPE, result);

    // Edge case: When goog.isArray returns false
    result = rai.restrictToArrayVisitor.caseObjectType(new ObjectType());
    assertNull(result);

    // Error scenario: When the input is not an object type
    result = rai.restrictToArrayVisitor.caseTopType(new JSType());
    assertEquals("TOP_TYPE", TOP_TYPE, result);
  }

  @Test
  public void testRestrictToNotArrayVisitor() {
    ClosureReverseAbstractInterpreter rai = new ClosureReverseAbstractInterpreter();

    // Typical use case: When goog.isArray returns false
    JSType result = rai.restrictToNotArrayVisitor.caseObjectType(new ObjectType());
    assertNull(result);

    // Edge case: When goog.isArray returns true
    result = rai.restrictToNotArrayVisitor.caseObjectType(new ObjectType());
    assertEquals("OBJECT_TYPE", OBJECT_TYPE, result);

    // Error scenario: When the input is not an object type
    result = rai.restrictToNotArrayVisitor.caseTopType(new JSType());
    assertEquals("TOP_TYPE", TOP_TYPE, result);
  }

  @Test
  public void testRestrictToObjectVisitor() {
    ClosureReverseAbstractInterpreter rai = new ClosureReverseAbstractInterpreter();

    // Typical use case: When goog.isObject returns true
    JSType result = rai.restrictToObjectVisitor.caseObjectType(new ObjectType());
    assertEquals("OBJECT_TYPE", OBJECT_TYPE, result);

    // Edge case: When goog.isObject returns false
    result = rai.restrictToObjectVisitor.caseObjectType(new ObjectType());
    assertNull(result);

    // Error scenario: When the input is not an object type
    result = rai.restrictToObjectVisitor.caseTopType(new JSType());
    assertEquals("NO_OBJECT_TYPE", NO_OBJECT_TYPE, result);
  }

  @Test
  public void testRestrictToNotObjectVisitor() {
    ClosureReverseAbstractInterpreter rai = new ClosureReverseAbstractInterpreter();

    // Typical use case: When goog.isObject returns false
    JSType result = rai.restrictToNotObjectVisitor.caseObjectType(new ObjectType());
    assertNull(result);

    // Edge case: When goog.isObject returns true
    result = rai.restrictToNotObjectVisitor.caseObjectType(new ObjectType());
    assertEquals("OBJECT_TYPE", OBJECT_TYPE, result);

    // Error scenario: When the input is not an object type
    result = rai.restrictToNotObjectVisitor.caseTopType(new JSType());
    assertEquals("ALL_TYPE", ALL_TYPE, result);
  }

  @Test
  public void testClosureReverseAbstractInterpreter() {
    // Create test instances
    CodingConvention convention = new CodingConvention();
    JSTypeRegistry typeRegistry = new JSTypeRegistry();
    ClosureReverseAbstractInterpreter rai = new ClosureReverseAbstractInterpreter(convention, typeRegistry);

    // Typical use case: When type is not null and goog.isDef returns true
    ClosureReverseAbstractInterpreter.TypeRestriction restriction = rai.new TypeRestriction(new JSType(), true);
    JSType result = rai.restricters.get("isDef").apply(restriction);
    assertNotNull(result);

    // Edge case: When type is null and goog.isDef returns true
    restriction = rai.new TypeRestriction(null, true);
    result = rai.restricters.get("isDef").apply(restriction);
    assertNull(result);

    // Error scenario: When goog.isDef is not a supported function
    restriction = rai.new TypeRestriction(new JSType(), true);
    result = rai.restricters.get("notSupportedFunction").apply(restriction);
    assertNull(result);
  }

  @Test
  public void testGetPreciserScopeKnowingConditionOutcome() {
    ClosureReverseAbstractInterpreter rai = new ClosureReverseAbstractInterpreter();

    // Typical use case: When condition is a supported call
    Node trueNode = new Node(new JSType());
    FlowScope falseScope = null;
    boolean outcome = true;
    FlowScope result = rai.getPreciserScopeKnowingConditionOutcome(trueNode, falseScope, outcome);
    assertNotNull(result);

    // Edge case: When condition is not a supported call
    Node falseNode = new Node(new JSType());
    result = rai.getPreciserScopeKnowingConditionOutcome(falseNode, falseScope, outcome);
    assertNull(result);

    // Error scenario: When outcome is not a boolean value
    outcome = null;
    result = rai.getPreciserScopeKnowingConditionOutcome(trueNode, falseScope, outcome);
    assertNull(result);
  }

  @Test
  public void testRestrictParameter() {
    ClosureReverseAbstractInterpreter rai = new ClosureReverseAbstractInterpreter();

    // Typical use case: When type is not null and outcome is true
    Node parameter = new Node();
    JSType type = new JSType();
    ClosureReverseAbstractInterpreter.TypeRestriction restriction = rai.new TypeRestriction(type, true);
    FlowScope blindScope = new FlowScope();
    Function<TypeRestriction, JSType> function = rai.restricters.get("isDef");
    boolean outcome = true;
    FlowScope result = rai.restrictParameter(parameter, type, blindScope, function, outcome);
    assertNotNull(result);

    // Edge case: When type is null and outcome is true
    restriction = rai.new TypeRestriction(null, true);
    result = rai.restrictParameter(parameter, type, blindScope, function, outcome);
    assertNull(result);

    // Error scenario: When function is not a supported restricter
    restriction = rai.new TypeRestriction(type, true);
    Function<TypeRestriction, JSType> notSupportedFunction = rai.restricters.get("notSupportedFunction");
    result = rai.restrictParameter(parameter, type, blindScope, notSupportedFunction, outcome);
    assertNull(result);
  }

  @Test
  public void testClosureReverseAbstractInterpreterConstructor() {
    // Create test instances
    CodingConvention convention = new CodingConvention();
    JSTypeRegistry typeRegistry = new JSTypeRegistry();
    ClosureReverseAbstractInterpreter rai = new ClosureReverseAbstractInterpreter(convention, typeRegistry);

    // Verify the restricters map is created correctly
    assertEquals("isDef", rai.restricters.keySet().get(0));
    assertEquals("isNull", rai.restricters.keySet().get(1));
    assertEquals("isDefAndNotNull", rai.restricters.keySet().get(2));
    assertEquals("isString", rai.restricters.keySet().get(3));
    assertEquals("isBoolean", rai.restricters.keySet().get(4));
    assertEquals("isNumber", rai.restricters.keySet().get(5));
    assertEquals("isFunction", rai.restricters.keySet().get(6));
    assertEquals("isArray", rai.restricters.keySet().get(7));
    assertEquals("isObject", rai.restricters.keySet().get(8));
    assertEquals(8, rai.restricters.keySet().size());
  }
}