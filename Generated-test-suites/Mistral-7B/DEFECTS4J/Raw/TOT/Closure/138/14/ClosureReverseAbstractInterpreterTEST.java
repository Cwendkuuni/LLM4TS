import static org.junit.Assert.*;
import static com.google.javascript.jscomp.ClosureReverseAbstractInterpreter.*;
import com.google.javascript.jscomp.CodingConvention;
import com.google.javascript.jscomp.JSTypeRegistry;
import com.google.javascript.rhino.Node;

import org.junit.Test;

/**
 * Test class for {@link ClosureReverseAbstractInterpreter}.
 */
public class ClosureReverseAbstractInterpreterTest {

  @Test
  public void testIsDef() {
    CodingConvention convention = new CodingConvention();
    JSTypeRegistry typeRegistry = new JSTypeRegistry();
    ClosureReverseAbstractInterpreter interpreter = new ClosureReverseAbstractInterpreter(
        convention, typeRegistry);

    Node condition = createNode("goog.isDef(myVariable)");
    FlowScope blindScope = new FlowScope();
    FlowScope informedScope = interpreter.getPreciserScopeKnowingConditionOutcome(
        condition, blindScope, true);

    JSType expectedType = getNativeType(OBJECT_TYPE);
    assertEquals(expectedType, informedScope.getType(0));
  }

  @Test
  public void testIsDefFalse() {
    CodingConvention convention = new CodingConvention();
    JSTypeRegistry typeRegistry = new JSTypeRegistry();
    ClosureReverseAbstractInterpreter interpreter = new ClosureReverseAbstractInterpreter(
        convention, typeRegistry);

    Node condition = createNode("goog.isDef(undefinedVariable)");
    FlowScope blindScope = new FlowScope();
    FlowScope informedScope = interpreter.getPreciserScopeKnowingConditionOutcome(
        condition, blindScope, false);

    assertNull(informedScope.getType(0));
  }

  @Test
  public void testIsDefNull() {
    CodingConvention convention = new CodingConvention();
    JSTypeRegistry typeRegistry = new JSTypeRegistry();
    ClosureReverseAbstractInterpreter interpreter = new ClosureReverseAbstractInterpreter(
        convention, typeRegistry);

    Node condition = createNode("goog.isDef(null)");
    FlowScope blindScope = new FlowScope();
    FlowScope informedScope = interpreter.getPreciserScopeKnowingConditionOutcome(
        condition, blindScope, false);

    assertNull(informedScope.getType(0));
  }

  @Test
  public void testIsDefArray() {
    CodingConvention convention = new CodingConvention();
    JSTypeRegistry typeRegistry = new JSTypeRegistry();
    ClosureReverseAbstractInterpreter interpreter = new ClosureReverseAbstractInterpreter(
        convention, typeRegistry);

    Node condition = createNode("goog.isDef([1, 2, 3])");
    FlowScope blindScope = new FlowScope();
    FlowScope informedScope = interpreter.getPreciserScopeKnowingConditionOutcome(
        condition, blindScope, true);

    JSType expectedType = getNativeType(ARRAY_TYPE);
    assertEquals(expectedType, informedScope.getType(0));
  }

  @Test
  public void testIsDefNotArray() {
    CodingConvention convention = new CodingConvention();
    JSTypeRegistry typeRegistry = new JSTypeRegistry();
    ClosureReverseAbstractInterpreter interpreter = new ClosureReverseAbstractInterpreter(
        convention, typeRegistry);

    Node condition = createNode("goog.isDef({name: 'John'})");
    FlowScope blindScope = new FlowScope();
    FlowScope informedScope = interpreter.getPreciserScopeKnowingConditionOutcome(
        condition, blindScope, true);

    JSType expectedType = getNativeType(OBJECT_TYPE);
    assertEquals(expectedType, informedScope.getType(0));
  }

  @Test
  public void testIsDefFunction() {
    CodingConvention convention = new CodingConvention();
    JSTypeRegistry typeRegistry = new JSTypeRegistry();
    ClosureReverseAbstractInterpreter interpreter = new ClosureReverseAbstractInterpreter(
        convention, typeRegistry);

    Node condition = createNode("goog.isDef(function() {})");
    FlowScope blindScope = new FlowScope();
    FlowScope informedScope = interpreter.getPreciserScopeKnowingConditionOutcome(
        condition, blindScope, true);

    JSType expectedType = getNativeType(FUNCTION_TYPE);
    assertEquals(expectedType, informedScope.getType(0));
  }

  @Test
  public void testIsDefFunctionNull() {
    CodingConvention convention = new CodingConvention();
    JSTypeRegistry typeRegistry = new JSTypeRegistry();
    ClosureReverseAbstractInterpreter interpreter = new ClosureReverseAbstractInterpreter(
        convention, typeRegistry);

    Node condition = createNode("goog.isDef(null)");
    FlowScope blindScope = new FlowScope();
    FlowScope informedScope = interpreter.getPreciserScopeKnowingConditionOutcome(
        condition, blindScope, false);

    assertNull(informedScope.getType(0));
  }

  @Test
  public void testIsDefFunctionUndefined() {
    CodingConvention convention = new CodingConvention();
    JSTypeRegistry typeRegistry = new JSTypeRegistry();
    ClosureReverseAbstractInterpreter interpreter = new ClosureReverseAbstractInterpreter(
        convention, typeRegistry);

    Node condition = createNode("goog.isDef(undefined)");
    FlowScope blindScope = new FlowScope();
    FlowScope informedScope = interpreter.getPreciserScopeKnowingConditionOutcome(
        condition, blindScope, false);

    assertNull(informedScope.getType(0));
  }

  @Test
  public void testIsObject() {
    CodingConvention convention = new CodingConvention();
    JSTypeRegistry typeRegistry = new JSTypeRegistry();
    ClosureReverseAbstractInterpreter interpreter = new ClosureReverseAbstractInterpreter(
        convention, typeRegistry);

    Node condition = createNode("goog.isObject(myVariable)");
    FlowScope blindScope = new FlowScope();
    FlowScope informedScope = interpreter.getPreciserScopeKnowingConditionOutcome(
        condition, blindScope, true);

    JSType expectedType = getNativeType(OBJECT_TYPE);
    assertEquals(expectedType, informedScope.getType(0));
  }

  @Test
  public void testIsObjectFalse() {
    CodingConvention convention = new CodingConvention();
    JSTypeRegistry typeRegistry = new JSTypeRegistry();
    ClosureReverseAbstractInterpreter interpreter = new ClosureReverseAbstractInterpreter(
        convention, typeRegistry);

    Node condition = createNode("goog.isObject(null)");
    FlowScope blindScope = new FlowScope();
    FlowScope informedScope = interpreter.getPreciserScopeKnowingConditionOutcome(
        condition, blindScope, false);

    assertNull(informedScope.getType(0));
  }

  @Test
  public void testIsObjectArray() {
    CodingConvention convention = new CodingConvention();
    JSTypeRegistry typeRegistry = new JSTypeRegistry();
    ClosureReverseAbstractInterpreter interpreter = new ClosureReverseAbstractInterpreter(
        convention, typeRegistry);

    Node condition = createNode("goog.isObject([1, 2, 3])");
    FlowScope blindScope = new FlowScope();
    FlowScope informedScope = interpreter.getPreciserScopeKnowingConditionOutcome(
        condition, blindScope, false);

    assertNull(informedScope.getType(0));
  }

  @Test
  public void testIsObjectFunction() {
    CodingConvention convention = new CodingConvention();
    JSTypeRegistry typeRegistry = new JSTypeRegistry();
    ClosureReverseAbstractInterpreter interpreter = new ClosureReverseAbstractInterpreter(
        convention, typeRegistry);

    Node condition = createNode("goog.isObject(function() {})");
    FlowScope blindScope = new FlowScope();
    FlowScope informedScope = interpreter.getPreciserScopeKnowingConditionOutcome(
        condition, blindScope, false);

    assertNull(informedScope.getType(0));
  }

  @Test
  public void testIsObjectObject() {
    CodingConvention convention = new CodingConvention();
    JSTypeRegistry typeRegistry = new JSTypeRegistry();
    ClosureReverseAbstractInterpreter interpreter = new ClosureReverseAbstractInterpreter(
        convention, typeRegistry);

    Node condition = createNode("goog.isObject({name: 'John'})");
    FlowScope blindScope = new FlowScope();
    FlowScope informedScope = interpreter.getPreciserScopeKnowingConditionOutcome(
        condition, blindScope, true);

    JSType expectedType = getNativeType(OBJECT_TYPE);
    assertEquals(expectedType, informedScope.getType(0));
  }

  @Test
  public void testIsArray() {
    CodingConvention convention = new CodingConvention();
    JSTypeRegistry typeRegistry = new JSTypeRegistry();
    ClosureReverseAbstractInterpreter interpreter = new ClosureReverseAbstractInterpreter(
        convention, typeRegistry);

    Node condition = createNode("goog.isArray([1, 2, 3])");
    FlowScope blindScope = new FlowScope();
    FlowScope informedScope = interpreter.getPreciserScopeKnowingConditionOutcome(
        condition, blindScope, true);

    JSType expectedType = getNativeType(ARRAY_TYPE);
    assertEquals(expectedType, informedScope.getType(0));
  }

  @Test
  public void testIsArrayFalse() {
    CodingConvention convention = new CodingConvention();
    JSTypeRegistry typeRegistry = new JSTypeRegistry();
    ClosureReverseAbstractInterpreter interpreter = new ClosureReverseAbstractInterpreter(
        convention, typeRegistry);

    Node condition = createNode("goog.isArray({name: 'John'})");
    FlowScope blindScope = new FlowScope();
    FlowScope informedScope = interpreter.getPreciserScopeKnowingConditionOutcome(
        condition, blindScope, false);

    assertNull(informedScope.getType(0));
  }

  private Node createNode(String expression) {
    Parser parser = new Parser();
    Node ast = parser.parse(expression, null);
    return ast;
  }

  private JSType getNativeType(int type) {
    return typeRegistry.types.getNativeType(type);
  }

  private JSType getNativeType(String typeName) {
    return typeRegistry.types.getNativeTypeByName(typeName);
  }

  private void declareNameInScope(FlowScope scope, Node node, JSType type) {
    scope.declareName(node.getFirstChild().getString(), type);
  }
}