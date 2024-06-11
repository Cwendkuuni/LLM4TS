import static org.junit.Assert.*;
import static com.google.javascript.rhino.Token.*;
import static com.google.javascript.rhino.jstype.JSTypeNative.*;

import com.google.common.base.Function;
import com.google.common.collect.ImmutableMap;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.com.google.javascript.rhino.jstype.JSType;
import com.google.javascript.jscomp.com.google.javascript.rhino.jstype.JSTypeRegistry;
import com.google.javascript.jscomp.com.google.javascript.rhino.jstype.FunctionType;
import com.google.javascript.jscomp.com.google.javascript.rhino.jstype.ObjectType;
import com.google.javascript.jscomp.com.google.javascript.rhino.jstype.Visitor;
import com.google.javascript.jscomp.com.google.javascript.rhino.Node;

import java.util.Map;

import org.junit.Test;

public class ClosureReverseAbstractInterpreterTest {

  private static final JSTypeRegistry typeRegistry = new JSTypeRegistry();

  @Test
  public void testGetPreciserScopeKnowingConditionOutcome() {
    ClosureReverseAbstractInterpreter interpreter =
        new ClosureReverseAbstractInterpreter(new CodingConvention(), typeRegistry);

    // Test for isDef
    Node condition = createNode("goog.isDef(x)", new Object[]{"x", OBJECT_TYPE});
    FlowScope blindScope = new FlowScope(typeRegistry);
    FlowScope informedScope = interpreter.getPreciserScopeKnowingConditionOutcome(condition, blindScope, true);
    JSType expectedType = getNativeType(OBJECT_TYPE);
    JSType actualType = informedScope.getVariable("x").getType();
    assertEquals(expectedType, actualType);

    // Test for isDef with false condition
    condition = createNode("goog.isDef(x)", new Object[]{"x", NULL_TYPE});
    informedScope = interpreter.getPreciserScopeKnowingConditionOutcome(condition, blindScope, false);
    assertEquals(null, informedScope);

    // Test for isNull
    condition = createNode("goog.isNull(x)", new Object[]{"x", OBJECT_TYPE});
    informedScope = interpreter.getPreciserScopeKnowingConditionOutcome(condition, blindScope, true);
    expectedType = getNativeType(NULL_TYPE);
    actualType = informedScope.getVariable("x").getType();
    assertEquals(expectedType, actualType);

    // Test for isNull with non-null value
    condition = createNode("goog.isNull(x)", new Object[]{"x", INTEGER_TYPE});
    informedScope = interpreter.getPreciserScopeKnowingConditionOutcome(condition, blindScope, false);
    assertEquals(null, informedScope);

    // Test for isDefAndNotNull
    condition = createNode("goog.isDefAndNotNull(x)", new Object[]{"x", OBJECT_TYPE});
    informedScope = interpreter.getPreciserScopeKnowingConditionOutcome(condition, blindScope, true);
    expectedType = getNativeType(OBJECT_TYPE);
    actualType = informedScope.getVariable("x").getType();
    assertEquals(expectedType, actualType);

    // Test for isDefAndNotNull with non-object value
    condition = createNode("goog.isDefAndNotNull(x)", new Object[]{"x", INTEGER_TYPE});
    informedScope = interpreter.getPreciserScopeKnowingConditionOutcome(condition, blindScope, false);
    assertEquals(null, informedScope);

    // Test for isString
    condition = createNode("goog.isString(x)", new Object[]{"x", STRING_TYPE});
    informedScope = interpreter.getPreciserScopeKnowingConditionOutcome(condition, blindScope, true);
    expectedType = STRING_TYPE;
    actualType = informedScope.getVariable("x").getType();
    assertEquals(expectedType, actualType);

    // Test for isString with non-string value
    condition = createNode("goog.isString(x)", new Object[]{"x", INTEGER_TYPE});
    informedScope = interpreter.getPreciserScopeKnowingConditionOutcome(condition, blindScope, false);
    assertEquals(null, informedScope);

    // Test for isBoolean
    condition = createNode("goog.isBoolean(x)", new Object[]{"x", BOOLEAN_TYPE});
    informedScope = interpreter.getPreciserScopeKnowingConditionOutcome(condition, blindScope, true);
    expectedType = BOOLEAN_TYPE;
    actualType = informedScope.getVariable("x").getType();
    assertEquals(expectedType, actualType);

    // Test for isBoolean with non-boolean value
    condition = createNode("goog.isBoolean(x)", new Object[]{"x", INTEGER_TYPE});
    informedScope = interpreter.getPreciserScopeKnowingConditionOutcome(condition, blindScope, false);
    assertEquals(null, informedScope);

    // Test for isNumber
    condition = createNode("goog.isNumber(x)", new Object[]{"x", NUMBER_TYPE});
    informedScope = interpreter.getPreciserScopeKnowingConditionOutcome(condition, blindScope, true);
    expectedType = NUMBER_TYPE;
    actualType = informedScope.getVariable("x").getType();
    assertEquals(expectedType, actualType);

    // Test for isNumber with non-number value
    condition = createNode("goog.isNumber(x)", new Object[]{"x", BOOLEAN_TYPE});
    informedScope = interpreter.getPreciserScopeKnowingConditionOutcome(condition, blindScope, false);
    assertEquals(null, informedScope);

    // Test for isFunction
    condition = createNode("goog.isFunction(x)", new Object[]{"x", FUNCTION_TYPE});
    informedScope = interpreter.getPreciserScopeKnowingConditionOutcome(condition, blindScope, true);
    expectedType = FUNCTION_TYPE;
    actualType = informedScope.getVariable("x").getType();
    assertEquals(expectedType, actualType);

    // Test for isFunction with non-function value
    condition = createNode("goog.isFunction(x)", new Object[]{"x", STRING_TYPE});
    informedScope = interpreter.getPreciserScopeKnowingConditionOutcome(condition, blindScope, false);
    assertEquals(null, informedScope);

    // Test for isArray
    condition = createNode("goog.isArray(x)", new Object[]{"x", ARRAY_TYPE});
    informedScope = interpreter.getPreciserScopeKnowingConditionOutcome(condition, blindScope, true);
    expectedType = ARRAY_TYPE;
    actualType = informedScope.getVariable("x").getType();
    assertEquals(expectedType, actualType);

    // Test for isArray with non-array value
    condition = createNode("goog.isArray(x)", new Object[]{"x", STRING_TYPE});
    informedScope = interpreter.getPreciserScopeKnowingConditionOutcome(condition, blindScope, false);
    assertEquals(null, informedScope);

    // Test for isObject
    condition = createNode("goog.isObject(x)", new Object[]{"x", OBJECT_TYPE});
    informedScope = interpreter.getPreciserScopeKnowingConditionOutcome(condition, blindScope, true);
    expectedType = OBJECT_TYPE;
    actualType = informedScope.getVariable("x").getType();
    assertEquals(expectedType, actualType);

    // Test for isObject with non-object value
    condition = createNode("goog.isObject(x)", new Object[]{"x", NUMBER_TYPE});
    informedScope = interpreter.getPreciserScopeKnowingConditionOutcome(condition, blindScope, false);
    assertEquals(null, informedScope);
  }

  private Node createNode(String expression, Object[] arguments) {
    Node node = new Node();
    node.setType(CALL);
    node.setLineNumber(1);
    node.setColumnNumber(1);
    Node callee = new Node();
    callee.setType(GETPROP);
    callee.setLineNumber(1);
    callee.setColumnNumber(1);
    callee.setParent(node);
    callee.setName("goog");
    Node left = new Node();
    left.setType(NAME);
    left.setLineNumber(1);
    left.setColumnNumber(5);
    left.setParent(callee);
    left.setString(expression.substring(0, 4));
    Node right = new Node();
    right.setType(STRING);
    right.setLineNumber(1);
    right.setColumnNumber(expression.length());
    right.setParent(callee);
    right.setString(expression.substring(expression.lastIndexOf("(") + 1, expression.length() - 1));
    Node params = new Node();
    for (Object arg : arguments) {
      Node argNode = new Node();
      argNode.setType(ARGUMENTS);
      argNode.setLineNumber(1);
      argNode.setColumnNumber(expression.lastIndexOf("(") + expression.length() - argument.toString().length());
      argNode.setParent(params);
      argNode.setChildren(new Object[]{createNode(String.format("arguments[%d]", arguments.length - 1 - IndexOf.indexOf(arguments, arg)), new Object[]{INT_TYPE})});
      params.setParent(callee);
      params.setChildren(new Object[]{argNode});
    }
    node.setChildren(new Object[]{callee, params});
    return node;
  }

  private int IndexOf(Object[] array, Object o) {
    for (int i = 0; i < array.length; ++i) {
      if (array[i].equals(o)) {
        return i;
      }
    }
    return -1;
  }
}