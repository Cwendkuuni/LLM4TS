import static org.junit.Assert.*;
import static com.google.javascript.jscomp.type.JSTypeNative.*;
import static com.google.javascript.rhino.NodeFactory.*;
import static com.google.javascript.rhino.jstype.JSTypeRegistry.*;

import org.junit.Before;
import org.junit.Test;

import com.google.common.collect.ImmutableMap;
import com.google.javascript.jscomp.CodingConvention;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.jstype.*;
import com.google.javascript.rhino.jstype.visitor.Visitor;

public class ClosureReverseAbstractInterpreterTest {
  private ClosureReverseAbstractInterpreter interpreter;
  private JSTypeRegistry typeRegistry;
  private CodingConvention convention;

  @Before
  public void setUp() {
    typeRegistry = TypeFactory.getRegistry();
    convention = new CodingConvention();
    interpreter = new ClosureReverseAbstractInterpreter(convention, typeRegistry);
  }

  /**
   * Test getPreciserScopeKnowingConditionOutcome method when condition is null.
   */
  @Test
  public void testGetPreciserScopeKnowingConditionOutcomeNullCondition() {
    Node condition = null;
    FlowScope blindScope = TypeFactory.createRootScope();
    boolean outcome = true;
    FlowScope result = interpreter.getPreciserScopeKnowingConditionOutcome(condition, blindScope, outcome);
    assertNull(result);
  }

  /**
   * Test getPreciserScopeKnowingConditionOutcome method when condition is not a call.
   */
  @Test
  public void testGetPreciserScopeKnowingConditionOutcomeNotCall() {
    Node condition = numberLiteral(10);
    FlowScope blindScope = TypeFactory.createRootScope();
    boolean outcome = true;
    FlowScope result = interpreter.getPreciserScopeKnowingConditionOutcome(condition, blindScope, outcome);
    assertNull(result);
  }

  /**
   * Test getPreciserScopeKnowingConditionOutcome method when condition call has incorrect number of children.
   */
  @Test
  public void testGetPreciserScopeKnowingConditionOutcomeIncorrectChildren() {
    Node condition = call("goog.isDef", arrayLiteral(numberLiteral(1), numberLiteral(2)), numberLiteral(3));
    FlowScope blindScope = TypeFactory.createRootScope();
    boolean outcome = true;
    FlowScope result = interpreter.getPreciserScopeKnowingConditionOutcome(condition, blindScope, outcome);
    assertNull(result);
  }

  /**
   * Test getPreciserScopeKnowingConditionOutcome method when condition call has wrong callee name.
   */
  @Test
  public void testGetPreciserScopeKnowingConditionOutcomeWrongCalleeName() {
    Node condition = call("foo.isDef", arrayLiteral(numberLiteral(1), numberLiteral(2)), numberLiteral(3));
    FlowScope blindScope = TypeFactory.createRootScope();
    boolean outcome = true;
    FlowScope result = interpreter.getPreciserScopeKnowingConditionOutcome(condition, blindScope, outcome);
    assertNull(result);
  }

  /**
   * Test getPreciserScopeKnowingConditionOutcome method when condition call has wrong parameter name.
   */
  @Test
  public void testGetPreciserScopeKnowingConditionOutcomeWrongParamName() {
    Node condition = call("goog.isDef", arrayLiteral(numberLiteral(1), stringLiteral("foo")), numberLiteral(3));
    FlowScope blindScope = TypeFactory.createRootScope();
    boolean outcome = true;
    FlowScope result = interpreter.getPreciserScopeKnowingConditionOutcome(condition, blindScope, outcome);
    assertNull(result);
  }

  /**
   * Test getPreciserScopeKnowingConditionOutcome method with true outcome and isDef function.
   */
  @Test
  public void testGetPreciserScopeKnowingConditionOutcomeTrueOutcomeIsDef() {
    Node condition = call("goog.isDef", arrayLiteral(numberLiteral(1), null), booleanLiteral(true));
    FlowScope blindScope = TypeFactory.createRootScope();
    boolean outcome = true;
    FlowScope result = interpreter.getPreciserScopeKnowingConditionOutcome(condition, blindScope, outcome);
    assertNotNull(result);

    JSType expectedType = getNativeType(NUMBER_TYPE);
    JSType actualType = result.getVariable("param").getType();
    assertEquals(expectedType, actualType);
  }

  /**
   * Test getPreciserScopeKnowingConditionOutcome method with false outcome and isDef function.
   */
  @Test
  public void testGetPreciserScopeKnowingConditionOutcomeFalseOutcomeIsDef() {
    Node condition = call("goog.isDef", arrayLiteral(numberLiteral(1), numberLiteral(1)), booleanLiteral(false));
    FlowScope blindScope = TypeFactory.createRootScope();
    boolean outcome = true;
    FlowScope result = interpreter.getPreciserScopeKnowingConditionOutcome(condition, blindScope, outcome);
    assertNull(result);
  }

  /**
   * Test restrictParameter method with null parameter.
   */
  @Test
  public void testRestrictParameterNullParameter() {
    Node parameter = nullNode();
    JSType type = getNativeType(NUMBER_TYPE);
    FlowScope blindScope = TypeFactory.createRootScope();
    Function<TypeRestriction, JSType> restriction = (TypeRestriction p) -> p.type != null ? p.type : null;
    boolean outcome = false;
    FlowScope result = interpreter.restrictParameter(parameter, type, blindScope, restriction, outcome);
    assertNull(result);
  }

  /**
   * Test restrictParameter method with non-boolean outcome.
   */
  @Test
  public void testRestrictParameterNonBooleanOutcome() {
    Node parameter = numberLiteral(1);
    JSType type = getNativeType(STRING_TYPE);
    FlowScope blindScope = TypeFactory.createRootScope();
    Function<TypeRestriction, JSType> restriction = (TypeRestriction p) -> p.outcome ? getRestrictedByTypeOfResult(p.type, "string", p.outcome) : null;
    boolean outcome = true;
    FlowScope result = interpreter.restrictParameter(parameter, type, blindScope, restriction, outcome);
    assertNull(result);
  }

  /**
   * Test restrictParameter method with string restricter.
   */
  @Test
  public void testRestrictParameterStringRestricter() {
    Node parameter = stringLiteral("foo");
    JSType type = getNativeType(NUMBER_TYPE);
    FlowScope blindScope = TypeFactory.createRootScope();
    Function<TypeRestriction, JSType> restriction = restricters.get("isString");
    boolean outcome = false;
    FlowScope result = interpreter.restrictParameter(parameter, type, blindScope, restriction, outcome);
    assertEquals(getNativeType(STRING_TYPE), result.getVariable("param").getType());
  }

  /**
   * Test restrictParameter method with null restricter.
   */
  @Test
  public void testRestrictParameterNullRestricter() {
    Node parameter = numberLiteral(1);
    JSType type = getNativeType(NUMBER_TYPE);
    FlowScope blindScope = TypeFactory.createRootScope();
    boolean outcome = false;
    Function<TypeRestriction, JSType> restriction = null;
    FlowScope result = interpreter.restrictParameter(parameter, type, blindScope, restriction, outcome);
    assertNull(result);
  }
}