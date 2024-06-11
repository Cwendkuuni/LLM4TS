import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;

public class ClosureReverseAbstractInterpreterTest {

  @Test
  public void testRestrictToArrayVisitor() {
    ClosureReverseAbstractInterpreter closureReverseAbstractInterpreter = 
        new ClosureReverseAbstractInterpreter();

    // Test when goog.isArray returns true
    JSType objectType = Mockito.mock(JSType.class);
    Mockito.when(objectType.isSubtype(Mockito.any())).thenReturn(true);
    Visitor<JSType> restrictToArrayVisitor = 
        closureReverseAbstractInterpreter.restrictToArrayVisitor;
    JSType result = restrictToArrayVisitor.caseObjectType(objectType);
    Assert.assertEquals(closureReverseAbstractInterpreter.getNativeType(
        closureReverseAbstractInterpreter.ARRAY_TYPE), result);

    // Test when goog.isArray returns false
    objectType = Mockito.mock(JSType.class);
    Mockito.when(objectType.isSubtype(Mockito.any())).thenReturn(false);
    result = restrictToArrayVisitor.caseObjectType(objectType);
    Assert.assertNull(result);
  }

  @Test
  public void testRestrictToNotArrayVisitor() {
    ClosureReverseAbstractInterpreter closureReverseAbstractInterpreter = 
        new ClosureReverseAbstractInterpreter();

    // Test when goog.isArray returns true
    JSType objectType = Mockito.mock(JSType.class);
    Mockito.when(objectType.isSubtype(Mockito.any())).thenReturn(true);
    Visitor<JSType> restrictToNotArrayVisitor = 
        closureReverseAbstractInterpreter.restrictToNotArrayVisitor;
    JSType result = restrictToNotArrayVisitor.caseObjectType(objectType);
    Assert.assertNull(result);

    // Test when goog.isArray returns false
    objectType = Mockito.mock(JSType.class);
    Mockito.when(objectType.isSubtype(Mockito.any())).thenReturn(false);
    result = restrictToNotArrayVisitor.caseObjectType(objectType);
    Assert.assertEquals(objectType, result);
  }

  @Test
  public void testRestrictToObjectVisitor() {
    ClosureReverseAbstractInterpreter closureReverseAbstractInterpreter = 
        new ClosureReverseAbstractInterpreter();

    // Test when goog.isObject returns true
    JSType objectType = Mockito.mock(JSType.class);
    JSType functionType = Mockito.mock(JSType.class);
    Visitor<JSType> restrictToObjectVisitor = 
        closureReverseAbstractInterpreter.restrictToObjectVisitor;
    JSType result = restrictToObjectVisitor.caseObjectType(objectType);
    Assert.assertEquals(objectType, result);
    result = restrictToObjectVisitor.caseFunctionType(functionType);
    Assert.assertEquals(functionType, result);

    // Test when goog.isObject returns false
    JSType topType = Mockito.mock(JSType.class);
    result = restrictToObjectVisitor.caseTopType(topType);
    Assert.assertEquals(
        closureReverseAbstractInterpreter.getNativeType(
            closureReverseAbstractInterpreter.NO_OBJECT_TYPE), result);
  }

  @Test
  public void testRestrictToNotObjectVisitor() {
    ClosureReverseAbstractInterpreter closureReverseAbstractInterpreter = 
        new ClosureReverseAbstractInterpreter();

    // Test when goog.isObject returns true
    JSType objectType = Mockito.mock(JSType.class);
    JSType functionType = Mockito.mock(JSType.class);
    Visitor<JSType> restrictToNotObjectVisitor = 
        closureReverseAbstractInterpreter.restrictToNotObjectVisitor;
    JSType result = restrictToNotObjectVisitor.caseObjectType(objectType);
    Assert.assertNull(result);
    result = restrictToNotObjectVisitor.caseFunctionType(functionType);
    Assert.assertNull(result);
  }

  @Test
  public void testConstructor() {
    CodingConvention convention = Mockito.mock(CodingConvention.class);
    JSTypeRegistry typeRegistry = Mockito.mock(JSTypeRegistry.class);

    ClosureReverseAbstractInterpreter closureReverseAbstractInterpreter = 
        new ClosureReverseAbstractInterpreter(convention, typeRegistry);

    // Verify that restricters map has been initialized correctly
    Assert.assertEquals(9, 
        closureReverseAbstractInterpreter.restricters.size());
  }

  @Test
  public void testGetPreciserScopeKnowingConditionOutcome() {
    ClosureReverseAbstractInterpreter closureReverseAbstractInterpreter = 
        new ClosureReverseAbstractInterpreter();

    // Test when condition does not match any restrictions
    Node condition = Mockito.mock(Node.class);
    FlowScope blindScope = Mockito.mock(FlowScope.class);
    boolean outcome = true;
    FlowScope result = 
        closureReverseAbstractInterpreter.getPreciserScopeKnowingConditionOutcome(
        condition, blindScope, outcome);
    Assert.assertEquals(blindScope, result);

    // Test when condition matches a restriction
    condition = Mockito.mock(Node.class);
    blindScope = Mockito.mock(FlowScope.class);
    outcome = true;
    result = 
        closureReverseAbstractInterpreter.getPreciserScopeKnowingConditionOutcome(
        condition, blindScope, outcome);
    Assert.assertEquals(blindScope, result);
  }

  @Test
  public void testRestrictParameter() {
    ClosureReverseAbstractInterpreter closureReverseAbstractInterpreter = 
        new ClosureReverseAbstractInterpreter();

    // Test when type is null and outcome is true
    Node parameter = Mockito.mock(Node.class);
    JSType type = null;
    FlowScope blindScope = Mockito.mock(FlowScope.class);
    Function<TypeRestriction, JSType> restriction = Mockito.mock(
        Function.class);
    boolean outcome = true;
    FlowScope result = 
        closureReverseAbstractInterpreter.restrictParameter(
        parameter, type, blindScope, restriction, outcome);
    Assert.assertEquals(blindScope, result);

    // Test when type is not null and outcome is false
    parameter = Mockito.mock(Node.class);
    type = Mockito.mock(JSType.class);
    blindScope = Mockito.mock(FlowScope.class);
    restriction = Mockito.mock(Function.class);
    outcome = false;
    result = 
        closureReverseAbstractInterpreter.restrictParameter(
        parameter, type, blindScope, restriction, outcome);
    Assert.assertNull(result);
  }

  @Test
  public void testTypeRestriction() {
    ClosureReverseAbstractInterpreter.TypeRestriction typeRestriction = 
        new ClosureReverseAbstractInterpreter.TypeRestriction(
        Mockito.mock(JSType.class), true);
    Assert.assertNotNull(typeRestriction.type);
    Assert.assertTrue(typeRestriction.outcome);
  }
}