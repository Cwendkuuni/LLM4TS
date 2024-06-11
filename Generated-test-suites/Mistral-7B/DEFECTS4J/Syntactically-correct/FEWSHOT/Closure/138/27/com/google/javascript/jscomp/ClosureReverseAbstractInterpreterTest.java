package com.google.javascript.jscomp.test;

import static com.google.javascript.jscomp.com.google.javascript.rhino.Token.CALL;
import static com.google.javascript.jscomp.com.google.javascript.rhino.Token.GETPROP;
import static com.google.javascript.jscomp.com.google.javascript.rhino.Token.NAME;
import static com.google.javascript.jscomp.com.google.javascript.rhino.Token.STRING;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertEquals;

import com.google.common.base.Function;
import com.google.common.collect.ImmutableMap;
import com.google.javascript.jscomp.ClosureReverseAbstractInterpreter;
import com.google.javascript.jscomp.CodingConvention;
import com.google.javascript.jscomp.JSType;
import com.google.javascript.jscomp.JSTypeRegistry;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.rhino.node.CallNode;
import com.google.javascript.rhino.node.GetPropNode;
import com.google.javascript.rhino.node.NameNode;
import com.google.javascript.rhino.node.StringNode;

import java.util.Map;

/**
 * Test cases for {@link ClosureReverseAbstractInterpreter}.
 */
public class ClosureReverseAbstractInterpreterTest {

  private static final JSTypeRegistry TYPE_REGISTRY = new JSTypeRegistry();
  private static final CodingConvention CONVENTION = new CodingConvention();

  @Test
  public void testRestrictIsDef() {
    Node condition = createCondition("goog.isDef", "foo");
    JSType expectedType = getNativeType(JSType.UNDEFINED);
    Scope scope = new Scope();
    JSType actualType = new ClosureReverseAbstractInterpreter(CONVENTION, TYPE_REGISTRY)
        .getPreciserScopeKnowingConditionOutcome(condition, scope, true).getScope()
        .getType(scope, "foo").getRawType();
    assertSame(expectedType, actualType);
  }

  @Test
  public void testRestrictIsDefFalse() {
    Node condition = createCondition("goog.isDef", "bar");
    JSType expectedType = null;
    Scope scope = new Scope();
    JSType actualType = new ClosureReverseAbstractInterpreter(CONVENTION, TYPE_REGISTRY)
        .getPreciserScopeKnowingConditionOutcome(condition, scope, false).getScope()
        .getType(scope, "bar").getRawType();
    assertNull(actualType);
  }

  @Test
  public void testRestrictIsDefAndNotNull() {
    Node condition = createCondition("goog.isDefAndNotNull", "baz");
    JSType expectedType = getNativeType(JSType.OBJECT);
    Scope scope = new Scope();
    JSType actualType = new ClosureReverseAbstractInterpreter(CONVENTION, TYPE_REGISTRY)
        .getPreciserScopeKnowingConditionOutcome(condition, scope, true).getScope()
        .getType(scope, "baz").getRawType();
    assertSame(expectedType, actualType);
  }

  @Test
  public void testRestrictIsDefAndNotNullFalse() {
    Node condition = createCondition("goog.isDefAndNotNull", "qux");
    JSType expectedType = null;
    Scope scope = new Scope();
    JSType actualType = new ClosureReverseAbstractInterpreter(CONVENTION, TYPE_REGISTRY)
        .getPreciserScopeKnowingConditionOutcome(condition, scope, false).getScope()
        .getType(scope, "qux").getRawType();
    assertNull(actualType);
  }

  @Test
  public void testRestrictIsString() {
    Node condition = createCondition("goog.isString", "quux");
    JSType expectedType = getNativeType(JSType.STRING);
    Scope scope = new Scope();
    JSType actualType = new ClosureReverseAbstractInterpreter(CONVENTION, TYPE_REGISTRY)
        .getPreciserScopeKnowingConditionOutcome(condition, scope, true).getScope()
        .getType(scope, "quux").getRawType();
    assertSame(expectedType, actualType);
  }

  @Test
  public void testRestrictIsStringFalse() {
    Node condition = createCondition("goog.isString", "quuz");
    JSType expectedType = null;
    Scope scope = new Scope();
    JSType actualType = new ClosureReverseAbstractInterpreter(CONVENTION, TYPE_REGISTRY)
        .getPreciserScopeKnowingConditionOutcome(condition, scope, false).getScope()
        .getType(scope, "quuz").getRawType();
    assertNull(actualType);
  }

  @Test
  public void testRestrictIsBoolean() {
    Node condition = createCondition("goog.isBoolean", "quux");
    JSType expectedType = getNativeType(JSType.BOOLEAN);
    Scope scope = new Scope();
    JSType actualType = new ClosureReverseAbstractInterpreter(CONVENTION, TYPE_REGISTRY)
        .getPreciserScopeKnowingConditionOutcome(condition, scope, true).getScope()
        .getType(scope, "quux").getRawType();
    assertSame(expectedType, actualType);
  }

  @Test
  public void testRestrictIsBooleanFalse() {
    Node condition = createCondition("goog.isBoolean", "quuz");
    JSType expectedType = null;
    Scope scope = new Scope();
    JSType actualType = new ClosureReverseAbstractInterpreter(CONVENTION, TYPE_REGISTRY)
        .getPreciserScopeKnowingConditionOutcome(condition, scope, false).getScope()
        .getType(scope, "quuz").getRawType();
    assertNull(actualType);
  }

  @Test
  public void testRestrictIsNumber() {
    Node condition = createCondition("goog.isNumber", "quux");
    JSType expectedType = getNativeType(JSType.NUMBER);
    Scope scope = new Scope();
    JSType actualType = new ClosureReverseAbstractInterpreter(CONVENTION, TYPE_REGISTRY)
        .getPreciserScopeKnowingConditionOutcome(condition, scope, true).getScope()
        .getType(scope, "quux").getRawType();
    assertSame(expectedType, actualType);
  }

  @Test
  public void testRestrictIsNumberFalse() {
    Node condition = createCondition("goog.isNumber", "quuz");
    JSType expectedType = null;
    Scope scope = new Scope();
    JSType actualType = new ClosureReverseAbstractInterpreter(CONVENTION, TYPE_REGISTRY)
        .getPreciserScopeKnowingConditionOutcome(condition, scope, false).getScope()
        .getType(scope, "quuz").getRawType();
    assertNull(actualType);
  }

  @Test
  public void testRestrictIsFunction() {
    Node condition = createCondition("goog.isFunction", "quux");
    JSType expectedType = getNativeType(JSType.FUNCTION);
    Scope scope = new Scope();
    JSType actualType = new ClosureReverseAbstractInterpreter(CONVENTION, TYPE_REGISTRY)
        .getPreciserScopeKnowingConditionOutcome(condition, scope, true).getScope()
        .getType(scope, "quux").getRawType();
    assertSame(expectedType, actualType);
  }

  @Test
  public void testRestrictIsFunctionFalse() {
    Node condition = createCondition("goog.isFunction", "quuz");
    JSType expectedType = null;
    Scope scope = new Scope();
    JSType actualType = new ClosureReverseAbstractInterpreter(CONVENTION, TYPE_REGISTRY)
        .getPreciserScopeKnowingConditionOutcome(condition, scope, false).getScope()
        .getType(scope, "quuz").getRawType();
    assertNull(actualType);
  }

  @Test
  public void testRestrictIsArrayTrue() {
    Node condition = createCondition("goog.isArray", "[1, 2, 3]");
    JSType expectedType = getNativeType(JSType.ARRAY);
    Scope scope = new Scope();
    JSType actualType = new ClosureReverseAbstractInterpreter(CONVENTION, TYPE_REGISTRY)
        .getPreciserScopeKnowingConditionOutcome(condition, scope, true).getScope()
        .getType(scope, "[1, 2, 3]").getRawType();
    assertSame(expectedType, actualType);
  }

  @Test
  public void testRestrictIsArrayFalse() {
    Node condition = createCondition("goog.isArray", "1, 2, 3");
    JSType expectedType = null;
    Scope scope = new Scope();
    JSType actualType = new ClosureReverseAbstractInterpreter(CONVENTION, TYPE_REGISTRY)
        .getPreciserScopeKnowingConditionOutcome(condition, scope, false).getScope()
        .getType(scope, "1, 2, 3").getRawType();
    assertNull(actualType);
  }

  @Test
  public void testRestrictIsObjectTrue() {
    Node condition = createCondition("goog.isObject", "goog.String");
    JSType expectedType = getNativeType(JSType.OBJECT);
    Scope scope = new Scope();
    JSType actualType = new ClosureReverseAbstractInterpreter(CONVENTION, TYPE_REGISTRY)
        .getPreciserScopeKnowingConditionOutcome(condition, scope, true).getScope()
        .getType(scope, "goog.String").getRawType();
    assertSame(expectedType, actualType);
  }

  @Test
  public void testRestrictIsObjectFalse() {
    Node condition = createCondition("goog.isObject", "123");
    JSType expectedType = null;
    Scope scope = new Scope();
    JSType actualType = new ClosureReverseAbstractInterpreter(CONVENTION, TYPE_REGISTRY)
        .getPreciserScopeKnowingConditionOutcome(condition, scope, false).getScope()
        .getType(scope, "123").getRawType();
    assertNull(actualType);
  }

  private Node createCondition(String functionName, String argument) {
    NameNode nameNode = new NameNode(functionName);
    GetPropNode getPropNode = new GetPropNode(nameNode, new StringNode(argument));
    CallNode callNode = new CallNode(null, getPropNode, new Node[0]);
    return callNode;
  }

  private JSType getNativeType(JSType type) {
    return TYPE_REGISTRY.getNativeType(type);
  }
}