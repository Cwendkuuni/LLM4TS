import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.common.collect.ImmutableMap;
import com.google.javascript.jscomp.CodingConvention;
import com.google.javascript.jscomp.JSType;
import com.google.javascript.jscomp.JSTypeRegistry;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.Visitor;

import java.util.Map;

import org.junit.Test;

public class ClosureReverseAbstractInterpreterTest {

  @Test
  public void testGetPreciserScopeKnowingConditionOutcome_isDef_true() {
    // Setup
    CodingConvention convention = mock(CodingConvention.class);
    JSTypeRegistry typeRegistry = mock(JSTypeRegistry.class);
    ClosureReverseAbstractInterpreter interpreter = new ClosureReverseAbstractInterpreter(
        convention, typeRegistry);

    Node condition = createNode("goog.isDef(x)");
    Node blindScope = createNode("blindScope"); // any FlowScope

    JSType expectedResult = getNativeType(JSTypeNative.BOOLEAN_TYPE);

    // Test
    FlowScope result = interpreter.getPreciserScopeKnowingConditionOutcome(
        condition, blindScope, true);

    // Assert
    assertSame(expectedResult, result.getDeclaredType());
  }

  @Test
  public void testGetPreciserScopeKnowingConditionOutcome_isDef_false() {
    // Setup
    CodingConvention convention = mock(CodingConvention.class);
    JSTypeRegistry typeRegistry = mock(JSTypeRegistry.class);
    ClosureReverseAbstractInterpreter interpreter = new ClosureReverseAbstractInterpreter(
        convention, typeRegistry);

    Node condition = createNode("goog.isDef(x)");
    Node blindScope = createNode("blindScope"); // any FlowScope

    // Test
    FlowScope result = interpreter.getPreciserScopeKnowingConditionOutcome(
        condition, blindScope, false);

    // Assert
    assertNull(result.getDeclaredType());
  }

  @Test
  public void testGetPreciserScopeKnowingConditionOutcome_isNull_true() {
    // Setup
    CodingConvention convention = mock(CodingConvention.class);
    JSTypeRegistry typeRegistry = mock(JSTypeRegistry.class);
    ClosureReverseAbstractInterpreter interpreter = new ClosureReverseAbstractInterpreter(
        convention, typeRegistry);

    Node condition = createNode("goog.isNull(x)");
    Node blindScope = createNode("blindScope"); // any FlowScope

    JSType expectedResult = getNativeType(JSTypeNative.NULL_TYPE);

    // Test
    FlowScope result = interpreter.getPreciserScopeKnowingConditionOutcome(
        condition, blindScope, true);

    // Assert
    assertSame(expectedResult, result.getDeclaredType());
  }

  @Test
  public void testGetPreciserScopeKnowingConditionOutcome_isNull_false() {
    // Setup
    CodingConvention convention = mock(CodingConvention.class);
    JSTypeRegistry typeRegistry = mock(JSTypeRegistry.class);
    ClosureReverseAbstractInterpreter interpreter = new ClosureReverseAbstractInterpreter(
        convention, typeRegistry);

    Node condition = createNode("goog.isNull(x)");
    Node blindScope = createNode("blindScope"); // any FlowScope

    // Test
    FlowScope result = interpreter.getPreciserScopeKnowingConditionOutcome(
        condition, blindScope, false);

    // Assert
    assertNull(result.getDeclaredType());
  }

  @Test
  public void testGetPreciserScopeKnowingConditionOutcome_isDefAndNotNull_true() {
    // Setup
    CodingConvention convention = mock(CodingConvention.class);
    JSTypeRegistry typeRegistry = mock(JSTypeRegistry.class);
    ClosureReverseAbstractInterpreter interpreter = new ClosureReverseAbstractInterpreter(
        convention, typeRegistry);

    Node condition = createNode("goog.isDefAndNotNull(x)");
    Node blindScope = createNode("blindScope"); // any FlowScope

    JSType expectedResult = getNativeType(JSTypeNative.BOOLEAN_TYPE);

    // Test
    FlowScope result = interpreter.getPreciserScopeKnowingConditionOutcome(
        condition, blindScope, true);

    // Assert
    assertSame(expectedResult, result.getDeclaredType());
  }

  @Test
  public void testGetPreciserScopeKnowingConditionOutcome_isDefAndNotNull_false() {
    // Setup
    CodingConvention convention = mock(CodingConvention.class);
    JSTypeRegistry typeRegistry = mock(JSTypeRegistry.class);
    ClosureReverseAbstractInterpreter interpreter = new ClosureReverseAbstractInterpreter(
        convention, typeRegistry);

    Node condition = createNode("goog.isDefAndNotNull(x)");
    Node blindScope = createNode("blindScope"); // any FlowScope

    // Test
    FlowScope result = interpreter.getPreciserScopeKnowingConditionOutcome(
        condition, blindScope, false);

    // Assert
    assertNull(result.getDeclaredType());
  }

  @Test
  public void testGetPreciserScopeKnowingConditionOutcome_isString_true() {
    // Setup
    CodingConvention convention = mock(CodingConvention.class);
    JSTypeRegistry typeRegistry = mock(JSTypeRegistry.class);
    ClosureReverseAbstractInterpreter interpreter = new ClosureReverseAbstractInterpreter(
        convention, typeRegistry);

    Node condition = createNode("goog.isString(x)");
    Node blindScope = createNode("blindScope"); // any FlowScope

    JSType expectedResult = getNativeType(JSTypeNative.STRING_TYPE);

    // Test
    FlowScope result = interpreter.getPreciserScopeKnowingConditionOutcome(
        condition, blindScope, true);

    // Assert
    assertSame(expectedResult, result.getDeclaredType());
  }

  @Test
  public void testGetPreciserScopeKnowingConditionOutcome_isString_false() {
    // Setup
    CodingConvention convention = mock(CodingConvention.class);
    JSTypeRegistry typeRegistry = mock(JSTypeRegistry.class);
    ClosureReverseAbstractInterpreter interpreter = new ClosureReverseAbstractInterpreter(
        convention, typeRegistry);

    Node condition = createNode("goog.isString(x)");
    Node blindScope = createNode("blindScope"); // any FlowScope

    // Test
    FlowScope result = interpreter.getPreciserScopeKnowingConditionOutcome(
        condition, blindScope, false);

    // Assert
    assertNull(result.getDeclaredType());
  }

  @Test
  public void testGetPreciserScopeKnowingConditionOutcome_isBoolean_true() {
    // Setup
    CodingConvention convention = mock(CodingConvention.class);
    JSTypeRegistry typeRegistry = mock(JSTypeRegistry.class);
    ClosureReverseAbstractInterpreter interpreter = new ClosureReverseAbstractInterpreter(
        convention, typeRegistry);

    Node condition = createNode("goog.isBoolean(x)");
    Node blindScope = createNode("blindScope"); // any FlowScope

    JSType expectedResult = getNativeType(JSTypeNative.BOOLEAN_TYPE);

    // Test
    FlowScope result = interpreter.getPreciserScopeKnowingConditionOutcome(
        condition, blindScope, true);

    // Assert
    assertSame(expectedResult, result.getDeclaredType());
  }

  @Test
  public void testGetPreciserScopeKnowingConditionOutcome_isBoolean_false() {
    // Setup
    CodingConvention convention = mock(CodingConvention.class);
    JSTypeRegistry typeRegistry = mock(JSTypeRegistry.class);
    ClosureReverseAbstractInterpreter interpreter = new ClosureReverseAbstractInterpreter(
        convention, typeRegistry);

    Node condition = createNode("goog.isBoolean(x)");
    Node blindScope = createNode("blindScope"); // any FlowScope

    // Test
    FlowScope result = interpreter.getPreciserScopeKnowingConditionOutcome(
        condition, blindScope, false);

    // Assert
    assertNull(result.getDeclaredType());
  }

  @Test
  public void testGetPreciserScopeKnowingConditionOutcome_isNumber_true() {
    // Setup
    CodingConvention convention = mock(CodingConvention.class);
    JSTypeRegistry typeRegistry = mock(JSTypeRegistry.class);
    ClosureReverseAbstractInterpreter interpreter = new ClosureReverseAbstractInterpreter(
        convention, typeRegistry);

    Node condition = createNode("goog.isNumber(x)");
    Node blindScope = createNode("blindScope"); // any FlowScope

    JSType expectedResult = getNativeType(JSTypeNative.NUMBER_TYPE);

    // Test
    FlowScope result = interpreter.getPreciserScopeKnowingConditionOutcome(
        condition, blindScope, true);

    // Assert
    assertSame(expectedResult, result.getDeclaredType());
  }

  @Test
  public void testGetPreciserScopeKnowingConditionOutcome_isNumber_false() {
    // Setup
    CodingConvention convention = mock(CodingConvention.class);
    JSTypeRegistry typeRegistry = mock(JSTypeRegistry.class);
    ClosureReverseAbstractInterpreter interpreter = new ClosureReverseAbstractInterpreter(
        convention, typeRegistry);

    Node condition = createNode("goog.isNumber(x)");
    Node blindScope = createNode("blindScope"); // any FlowScope

    // Test
    FlowScope result = interpreter.getPreciserScopeKnowingConditionOutcome(
        condition, blindScope, false);

    // Assert
    assertNull(result.getDeclaredType());
  }

  @Test
  public void testGetPreciserScopeKnowingConditionOutcome_isFunction_true() {
    // Setup
    CodingConvention convention = mock(CodingConvention.class);
    JSTypeRegistry typeRegistry = mock(JSTypeRegistry.class);
    ClosureReverseAbstractInterpreter interpreter = new ClosureReverseAbstractInterpreter(
        convention, typeRegistry);

    Node condition = createNode("goog.isFunction(x)");
    Node blindScope = createNode("blindScope"); // any FlowScope

    JSType expectedResult = getNativeType(JSTypeNative.FUNCTION_TYPE);

    // Test
    FlowScope result = interpreter.getPreciserScopeKnowingConditionOutcome(
        condition, blindScope, true);

    // Assert
    assertSame(expectedResult, result.getDeclaredType());
  }

  @Test
  public void testGetPreciserScopeKnowingConditionOutcome_isFunction_false() {
    // Setup
    CodingConvention convention = mock(CodingConvention.class);
    JSTypeRegistry typeRegistry = mock(JSTypeRegistry.class);
    ClosureReverseAbstractInterpreter interpreter = new ClosureReverseAbstractInterpreter(
        convention, typeRegistry);

    Node condition = createNode("goog.isFunction(x)");
    Node blindScope = createNode("blindScope"); // any FlowScope

    // Test
    FlowScope result = interpreter.getPreciserScopeKnowingConditionOutcome(
        condition, blindScope, false);

    // Assert
    assertNull(result.getDeclaredType());
  }

  @Test
  public void testGetPreciserScopeKnowingConditionOutcome_isArray_true() {
    // Setup
    CodingConvention convention = mock(CodingConvention.class);
    JSTypeRegistry typeRegistry = mock(JSTypeRegistry.class);
    ClosureReverseAbstractInterpreter interpreter = new ClosureReverseAbstractInterpreter(
        convention, typeRegistry);

    Node condition = createNode("goog.isArray(x)");
    Node blindScope = createNode("blindScope"); // any FlowScope

    JSType expectedResult = getNativeType(JSTypeNative.ARRAY_TYPE);

    // Test
    FlowScope result = interpreter.getPreciserScopeKnowingConditionOutcome(
        condition, blindScope, true);

    // Assert
    assertSame(expectedResult, result.getDeclaredType());
  }

  @Test
  public void testGetPreciserScopeKnowingConditionOutcome_isArray_false() {
    // Setup
    CodingConvention convention = mock(CodingConvention.class);
    JSTypeRegistry typeRegistry = mock(JSTypeRegistry.class);
    ClosureReverseAbstractInterpreter interpreter = new ClosureReverseAbstractInterpreter(
        convention, typeRegistry);

    Node condition = createNode("goog.isArray(x)");
    Node blindScope = createNode("blindScope"); // any FlowScope

    // Test
    FlowScope result = interpreter.getPreciserScopeKnowingConditionOutcome(
        condition, blindScope, false);

    // Assert
    assertNull(result.getDeclaredType());
  }

  @Test
  public void testGetPreciserScopeKnowingConditionOutcome_isObject_true() {
    // Setup
    CodingConvention convention = mock(CodingConvention.class);
    JSTypeRegistry typeRegistry = mock(JSTypeRegistry.class);
    ClosureReverseAbstractInterpreter interpreter = new ClosureReverseAbstractInterpreter(
        convention, typeRegistry);

    Node condition = createNode("goog.isObject(x)");
    Node blindScope = createNode("blindScope"); // any FlowScope

    JSType expectedResult = getNativeType(JSTypeNative.OBJECT_TYPE);

    // Test
    FlowScope result = interpreter.getPreciserScopeKnowingConditionOutcome(
        condition, blindScope, true);

    // Assert
    assertSame(expectedResult, result.getDeclaredType());
  }

  @Test
  public void testGetPreciserScopeKnowingConditionOutcome_isObject_false() {
    // Setup
    CodingConvention convention = mock(CodingConvention.class);
    JSTypeRegistry typeRegistry = mock(JSTypeRegistry.class);
    ClosureReverseAbstractInterpreter interpreter = new ClosureReverseAbstractInterpreter(
        convention, typeRegistry);

    Node condition = createNode("goog.isObject(x)");
    Node blindScope = createNode("blindScope"); // any FlowScope

    // Test
    FlowScope result = interpreter.getPreciserScopeKnowingConditionOutcome(
        condition, blindScope, false);

    // Assert
    assertNull(result.getDeclaredType());
  }

  private Node createNode(String nodeTypeAndValue) {
    Node node = new Node();
    node.setType(parseNodeType(nodeTypeAndValue));
    node.setLineNumber(1);
    node.setColumnNumber(1);
    return node;
  }

  private static int parseNodeType(String nodeTypeAndValue) {
    if (nodeTypeAndValue.charAt(0) == 'N') {
      return NAME;
    }
    if (nodeTypeAndValue.charAt(0) == 'C') {
      return CALL;
    }
    if (nodeTypeAndValue.charAt(0) == 'G') {
      return GETPROP;
    }
    if (nodeTypeAndValue.charAt(0) == 'S') {
      return STRING;
    }
    throw new IllegalArgumentException("Invalid node type: " + nodeTypeAndValue);
  }

  private JSType getNativeType(int jsType) {
    return getNativeTypeRegistry().lookupNativeType(jsType);
  }

  private JSTypeRegistry getNativeTypeRegistry() {
    return mock(JSTypeRegistry.class);
  }
}