import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import com.google.javascript.jscomp.ChainableReverseAbstractInterpreter;
import com.google.javascript.jscomp.ClosureReverseAbstractInterpreter;
import com.google.javascript.jscomp.CodingConvention;
import com.google.javascript.jscomp.FunctionType;
import com.google.javascript.jscomp.JSType;
import com.google.javascript.jscomp.JSTypeNative;
import com.google.javascript.jscomp.JSTypeRegistry;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.ObjectType;
import com.google.javascript.rhino.jstype.JSTypeVisitor;

import java.util.Map;

public class ClosureReverseAbstractInterpreterTest {

  private static JSTypeRegistry typeRegistry;
  private static CodingConvention codingConvention;
  private static ClosureReverseAbstractInterpreter interpreter;

  @org.junit.BeforeClass
  public static void setUp() {
    typeRegistry = new JSTypeRegistry();
    codingConvention = new CodingConvention();
    interpreter = new ClosureReverseAbstractInterpreter(codingConvention, typeRegistry);
  }

  @org.junit.Test
  public void testIsDef() {
    JSType inputType = getType(JSTypeNative.OBJECT_TYPE);
    JSType expectedType = getType(JSTypeNative.ANY_TYPE);

    Node condition = createNode("goog.isDef(myObject)");
    FlowScope blindScope = new ChainableReverseAbstractInterpreter.FlowScope();
    FlowScope informedScope = interpreter.getPreciserScopeKnowingConditionOutcome(condition, blindScope, true);

    JSType actualType = informedScope.getVariableValue("myObject");
    assertEquals(expectedType, actualType);

    condition = createNode("goog.isDef(null)");
    informedScope = interpreter.getPreciserScopeKnowingConditionOutcome(condition, blindScope, false);
    assertNull(informedScope.getVariableValue("myObject"));

    condition = createNode("goog.isDef(undefined)");
    informedScope = interpreter.getPreciserScopeKnowingConditionOutcome(condition, blindScope, false);
    assertNull(informedScope.getVariableValue("myObject"));
  }

  @org.junit.Test
  public void testIsDefAndNotNull() {
    JSType inputType = getType(JSTypeNative.OBJECT_TYPE);
    JSType expectedType = getType(JSTypeNative.STRING_TYPE);

    Node condition = createNode("goog.isDefAndNotNull(myString)");
    FlowScope blindScope = new ChainableReverseAbstractInterpreter.FlowScope();
    FlowScope informedScope = interpreter.getPreciserScopeKnowingConditionOutcome(condition, blindScope, true);

    JSType actualType = informedScope.getVariableValue("myString");
    assertSame(expectedType, actualType);

    condition = createNode("goog.isDefAndNotNull(null)");
    informedScope = interpreter.getPreciserScopeKnowingConditionOutcome(condition, blindScope, false);
    assertNull(informedScope.getVariableValue("myString"));

    condition = createNode("goog.isDefAndNotNull(undefined)");
    informedScope = interpreter.getPreciserScopeKnowingConditionOutcome(condition, blindScope, false);
    assertNull(informedScope.getVariableValue("myString"));

    condition = createNode("goog.isDefAndNotNull(myObject)");
    informedScope = interpreter.getPreciserScopeKnowingConditionOutcome(condition, blindScope, false);
    assertNull(informedScope.getVariableValue("myString"));
  }

  @org.junit.Test
  public void testIsArray() {
    JSType inputType = getType(JSTypeNative.ARRAY_TYPE);
    JSType expectedType = getType(JSTypeNative.ARRAY_TYPE);

    Node condition = createNode("goog.isArray(myArray)");
    FlowScope blindScope = new ChainableReverseAbstractInterpreter.FlowScope();
    FlowScope informedScope = interpreter.getPreciserScopeKnowingConditionOutcome(condition, blindScope, true);

    JSType actualType = informedScope.getVariableValue("myArray");
    assertSame(expectedType, actualType);

    condition = createNode("goog.isArray(myObject)");
    informedScope = interpreter.getPreciserScopeKnowingConditionOutcome(condition, blindScope, false);
    assertNull(informedScope.getVariableValue("myArray"));
  }

  @org.junit.Test
  public void testIsObject() {
    JSType inputType = getType(JSTypeNative.OBJECT_TYPE);
    JSType expectedType = getType(JSTypeNative.OBJECT_TYPE);

    Node condition = createNode("goog.isObject(myObject)");
    FlowScope blindScope = new ChainableReverseAbstractInterpreter.FlowScope();
    FlowScope informedScope = interpreter.getPreciserScopeKnowingConditionOutcome(condition, blindScope, true);

    JSType actualType = informedScope.getVariableValue("myObject");
    assertSame(expectedType, actualType);

    condition = createNode("goog.isObject(myNumber)");
    informedScope = interpreter.getPreciserScopeKnowingConditionOutcome(condition, blindScope, false);
    assertNull(informedScope.getVariableValue("myObject"));

    condition = createNode("goog.isObject(myFunction)");
    informedScope = interpreter.getPreciserScopeKnowingConditionOutcome(condition, blindScope, true);
    JSType functionType = getType(new FunctionType(new JSType[0], JSTypeNative.VOID_TYPE));
    assertSame(functionType, informedScope.getVariableValue("myFunction").getType());
  }

  @org.junit.Test
  public void testIsString() {
    JSType inputType = getType(JSTypeNative.STRING_TYPE);
    JSType expectedType = getType(JSTypeNative.STRING_TYPE);

    Node condition = createNode("goog.isString(myString)");
    FlowScope blindScope = new ChainableReverseAbstractInterpreter.FlowScope();
    FlowScope informedScope = interpreter.getPreciserScopeKnowingConditionOutcome(condition, blindScope, true);

    JSType actualType = informedScope.getVariableValue("myString");
    assertSame(expectedType, actualType);

    condition = createNode("goog.isString(myNumber)");
    informedScope = interpreter.getPreciserScopeKnowingConditionOutcome(condition, blindScope, false);
    assertNull(informedScope.getVariableValue("myString"));
  }

  @org.junit.Test
  public void testIsNumber() {
    JSType inputType = getType(JSTypeNative.NUMBER_TYPE);
    JSType expectedType = getType(JSTypeNative.NUMBER_TYPE);

    Node condition = createNode("goog.isNumber(myNumber)");
    FlowScope blindScope = new ChainableReverseAbstractInterpreter.FlowScope();
    FlowScope informedScope = interpreter.getPreciserScopeKnowingConditionOutcome(condition, blindScope, true);

    JSType actualType = informedScope.getVariableValue("myNumber");
    assertSame(expectedType, actualType);

    condition = createNode("goog.isNumber(myString)");
    informedScope = interpreter.getPreciserScopeKnowingConditionOutcome(condition, blindScope, false);
    assertNull(informedScope.getVariableValue("myNumber"));
  }

  @org.junit.Test
  public void testIsFunction() {
    JSType inputType = getType(new FunctionType(new JSType[0], JSTypeNative.VOID_TYPE));
    JSType expectedType = getType(new FunctionType(new JSType[0], JSTypeNative.VOID_TYPE));

    Node condition = createNode("goog.isFunction(myFunction)");
    FlowScope blindScope = new ChainableReverseAbstractInterpreter.FlowScope();
    FlowScope informedScope = interpreter.getPreciserScopeKnowingConditionOutcome(condition, blindScope, true);

    JSType actualType = informedScope.getVariableValue("myFunction").getType();
    assertSame(expectedType, actualType);

    condition = createNode("goog.isFunction(myObject)");
    informedScope = interpreter.getPreciserScopeKnowingConditionOutcome(condition, blindScope, false);
    assertNull(informedScope.getVariableValue("myFunction"));
  }

  @org.junit.Test
  public void testIsBoolean() {
    JSType inputType = getType(JSTypeNative.BOOLEAN_TYPE);
    JSType expectedType = getType(JSTypeNative.BOOLEAN_TYPE);

    Node condition = createNode("goog.isBoolean(myBoolean)");
    FlowScope blindScope = new ChainableReverseAbstractInterpreter.FlowScope();
    FlowScope informedScope = interpreter.getPreciserScopeKnowingConditionOutcome(condition, blindScope, true);

    JSType actualType = informedScope.getVariableValue("myBoolean");
    assertSame(expectedType, actualType);

    condition = createNode("goog.isBoolean(myString)");
    informedScope = interpreter.getPreciserScopeKnowingConditionOutcome(condition, blindScope, false);
    assertNull(informedScope.getVariableValue("myBoolean"));
  }

  private Node createNode(String expression) {
    JSTypeRegistry registry = new JSTypeRegistry();
    Node node = new Node(expression, registry);
    registry.markTypes(node);
    return node;
  }

  private JSType getType(JSTypeNative type) {
    return typeRegistry.getNativeType(type);
  }

  private JSType getType(FunctionType functionType) {
    return typeRegistry.getTypeFactory().getFunctionType(functionType.getParameterTypes(),
        functionType.getReturnType());
  }
}