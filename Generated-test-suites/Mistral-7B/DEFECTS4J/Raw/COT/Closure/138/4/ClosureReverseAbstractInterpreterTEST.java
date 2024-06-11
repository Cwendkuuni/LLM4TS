import static org.junit.Assert.*;
import static com.google.javascript.jscomp.com.google.javascript.rhino.Token.*;
import static com.google.javascript.jscomp.com.google.javascript.rhino.jstype.JSTypeNative.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import com.google.common.base.Function;
import com.google.common.collect.ImmutableMap;
import com.google.javascript.jscomp.com.google.javascript.rhino.JSType;
import com.google.javascript.jscomp.com.google.javascript.rhino.jstype.JSTypeRegistry;
import com.google.javascript.jscomp.com.google.javascript.rhino.jstype.FunctionType;
import com.google.javascript.jscomp.com.google.javascript.rhino.jstype.ObjectType;
import com.google.javascript.jscomp.com.google.javascript.rhino.jstype.Visitor;
import com.google.javascript.jscomp.com.google.javascript.rhino.Node;

import java.util.Map;

@RunWith(JUnit4.class)
public class ClosureReverseAbstractInterpreterTest {

  private static final JSTypeRegistry TYPE_REGISTRY = new JSTypeRegistry();
  private static final ClosureReverseAbstractInterpreter INTERPRETER =
      new ClosureReverseAbstractInterpreter(
          CodingConvention.INSTANCE, TYPE_REGISTRY);

  @Test
  public void testGetPreciserScopeKnowingConditionOutcome_isDef() {
    Node condition = new Node(CALL, 2);
    Node callee = new Node(GETPROP, 1);
    Node param = new Node(STRING, "isDef");

    Node left = new Node(NAME, "goog");
    Node leftLeft = new Node(STRING, "isDef");

    callee.addChild(left);
    callee.addChild(param);
    condition.addChild(callee);
    Node value = new Node(STRING, "true");
    condition.addChild(value);

    FlowScope blindScope = new FlowScope(TYPE_REGISTRY);

    JSType expected = getNativeType(OBJECT_TYPE);
    JSType actual = INTERPRETER.getPreciserScopeKnowingConditionOutcome(condition,
        blindScope, true).getVariable("param").getType();
    assertEquals(expected, actual);
  }

  @Test
  public void testGetPreciserScopeKnowingConditionOutcome_isDef_null() {
    Node condition = new Node(CALL, 2);
    Node callee = new Node(GETPROP, 1);
    Node param = new Node(STRING, "isDef");

    Node left = new Node(NAME, "goog");
    Node leftLeft = new Node(STRING, "isDef");

    callee.addChild(left);
    callee.addChild(param);
    condition.addChild(callee);
    Node value = new Node(STRING, "false");
    condition.addChild(value);

    FlowScope blindScope = new FlowScope(TYPE_REGISTRY);

    JSType expected = NULL_TYPE;
    JSType actual = INTERPRETER.getPreciserScopeKnowingConditionOutcome(condition,
        blindScope, false).getVariable("param").getType();
    assertEquals(expected, actual);
  }

  @Test
  public void testGetPreciserScopeKnowingConditionOutcome_isArray() {
    Node condition = new Node(CALL, 2);
    Node callee = new Node(GETPROP, 1);
    Node param = new Node(STRING, "isArray");

    Node left = new Node(NAME, "goog");
    Node leftLeft = new Node(STRING, "isArray");

    callee.addChild(left);
    callee.addChild(param);
    condition.addChild(callee);
    Node value = new Node(BOOLEAN, true);
    condition.addChild(value);

    FlowScope blindScope = new FlowScope(TYPE_REGISTRY);

    Node array = new Node(ARRAY, new Node[0]);
    JSType expected = array.getType();
    JSType actual = INTERPRETER.getPreciserScopeKnowingConditionOutcome(condition,
        blindScope, true).getVariable("param").getType();
    assertEquals(expected, actual);
  }

  @Test
  public void testGetPreciserScopeKnowingConditionOutcome_isArray_false() {
    Node condition = new Node(CALL, 2);
    Node callee = new Node(GETPROP, 1);
    Node param = new Node(STRING, "isArray");

    Node left = new Node(NAME, "goog");
    Node leftLeft = new Node(STRING, "isArray");

    callee.addChild(left);
    callee.addChild(param);
    condition.addChild(callee);
    Node value = new Node(BOOLEAN, false);
    condition.addChild(value);

    FlowScope blindScope = new FlowScope(TYPE_REGISTRY);

    JSType expected = NULL_TYPE;
    JSType actual = INTERPRETER.getPreciserScopeKnowingConditionOutcome(condition,
        blindScope, false).getVariable("param").getType();
    assertEquals(expected, actual);
  }

  @Test(expected = AssertionError.class)
  public void testGetPreciserScopeKnowingConditionOutcome_invalid() {
    Node condition = new Node(CALL, 2);
    Node callee = new Node(GETPROP, 1);
    Node param = new Node(STRING, "invalidFunction");

    Node left = new Node(NAME, "goog");
    Node leftLeft = new Node(STRING, "invalidFunction");

    callee.addChild(left);
    callee.addChild(param);
    condition.addChild(callee);
    Node value = new Node(BOOLEAN, true);
    condition.addChild(value);

    FlowScope blindScope = new FlowScope(TYPE_REGISTRY);

    INTERPRETER.getPreciserScopeKnowingConditionOutcome(condition, blindScope,
        true);
  }

  private JSType getNativeType(int typeCode) {
    return TYPE_REGISTRY.getNativeType(typeCode);
  }

  private JSType getRestrictedWithoutUndefined(JSType type) {
    return INTERPRETER.restricters.get("isDef").apply(
        new TypeRestriction(type, true));
  }

  private JSType getRestrictedWithoutNull(JSType type) {
    return INTERPRETER.restricters.get("isNull").apply(
        new TypeRestriction(type, false));
  }

  private JSType getRestrictedByTypeOfResult(JSType type, String expectedType,
      boolean outcome) {
    return INTERPRETER.restricters.get(expectedType).apply(
        new TypeRestriction(type, outcome));
  }

  private static class TypeRestriction {
    private final JSType type;
    private final boolean outcome;

    private TypeRestriction(JSType type, boolean outcome) {
      this.type = type;
      this.outcome = outcome;
    }
  }
}