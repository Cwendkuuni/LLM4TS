package org.apache.commons.cli; // This package is different from the given code, please replace it with the correct package name for ClosureReverseAbstractInterpreter.

import static com.google.javascript.jscomp.ClosureReverseAbstractInterpreter.TypeRestriction;
import static com.google.javascript.rhino.Token.CALL;
import static com.google.javascript.rhino.Token.GETPROP;
import static com.google.javascript.rhino.Token.NAME;
import static com.google.javascript.rhino.Token.STRING;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;

import com.google.common.base.Function;
import com.google.common.collect.ImmutableMap;
import com.google.javascript.rhino.jstype.JSType;
import com.google.javascript.rhino.jstype.JSTypeRegistry;
import com.google.javascript.rhino.jstype.FunctionType;
import com.google.javascript.rhino.jstype.ObjectType;
import com.google.javascript.rhino.jstype.Visitor;
import com.google.javascript.rhino.Node;

import java.util.Map;

public class ClosureReverseAbstractInterpreterTest {

  private static final ClosureReverseAbstractInterpreter interpreter =
      new ClosureReverseAbstractInterpreter(null, null);

  @Test
  public void testRestrictToArray() {
    Node condition = new Node(CALL, null, new Node(GETPROP, null, new Node(NAME, "goog")), new Node(STRING, "isArray"));
    Node param = new Node(new Node(NAME, "param"), new Node(STRING, "[1, 2, 3]"));
    JSType type = interpreter.getTypeIfRefinable(param, null);
    TypeRestriction restriction = new TypeRestriction(type, true);
    JSType expected = interpreter.getNativeType(JSTypeRegistry.ARRAY_TYPE);
    JSType actual = interpreter.restrictionToArrayVisitor.caseArrayType(type).visit(restriction);
    assertEquals(expected, actual);
  }

  @Test
  public void testRestrictToNotArray() {
    Node condition = new Node(CALL, null, new Node(GETPROP, null, new Node(NAME, "goog")), new Node(STRING, "isArray"));
    Node param = new Node(new Node(NAME, "param"), new Node(STRING, "\"foo\""));
    JSType type = interpreter.getTypeIfRefinable(param, null);
    TypeRestriction restriction = new TypeRestriction(type, true);
    JSType actual = interpreter.restrictToNotArrayVisitor.caseObjectType(type).visit(restriction);
    assertNull(actual);
  }

  @Test
  public void testRestrictToObject() {
    Node condition = new Node(CALL, null, new Node(GETPROP, null, new Node(NAME, "goog")), new Node(STRING, "isObject"));
    Node param = new Node(new Node(NAME, "param"), null);
    JSType type = interpreter.getTypeIfRefinable(param, null);
    TypeRestriction restriction = new TypeRestriction(type, true);
    JSType expected = interpreter.getNativeType(JSTypeRegistry.NO_OBJECT_TYPE);
    JSType actual = interpreter.restrictToObjectVisitor.caseObjectType(type).visit(restriction);
    assertEquals(expected, actual);
  }

  @Test
  public void testRestrictToNotObject() {
    Node condition = new Node(CALL, null, new Node(GETPROP, null, new Node(NAME, "goog")), new Node(STRING, "isObject"));
    Node param = new Node(new Node(NAME, "param"), null);
    JSType type = interpreter.getTypeIfRefinable(param, null);
    TypeRestriction restriction = new TypeRestriction(type, false);
    JSType actual = interpreter.restrictToNotObjectVisitor.caseObjectType(type).visit(restriction);
    assertNull(actual);
  }

  @Test
  public void testRestrictIsDef() {
    Node condition = new Node(CALL, null, new Node(GETPROP, null, new Node(NAME, "goog")), new Node(STRING, "isDef"));
    Node param = new Node(new Node(NAME, "param"), null);
    JSType type = null;
    TypeRestriction restriction = new TypeRestriction(type, true);
    JSType expected = interpreter.getRestrictedWithoutUndefined(interpreter.getNativeType(JSTypeRegistry.OBJECT_TYPE));
    JSType actual = interpreter.restricters.get("isDef").apply(restriction).visit(condition);
    assertEquals(expected, actual);
  }

  @Test
  public void testRestrictIsDefFalse() {
    Node condition = new Node(CALL, null, new Node(GETPROP, null, new Node(NAME, "goog")), new Node(STRING, "isDef"));
    Node param = new Node(new Node(NAME, "param"), null);
    JSType type = null;
    TypeRestriction restriction = new TypeRestriction(type, false);
    JSType expected = null;
    JSType actual = interpreter.restricters.get("isDef").apply(restriction).visit(condition);
    assertEquals(expected, actual);
  }

  @Test
  public void testGetPreciserScopeKnowingConditionOutcome() {
    Node condition = new Node(CALL, null, new Node(GETPROP, null, new Node(NAME, "goog")), new Node(STRING, "isDef"),
        new Node(new Node(NAME, "param"), null));
    FlowScope blindScope = new FlowScope();
    boolean outcome = true;
    FlowScope informedScope = interpreter.getPreciserScopeKnowingConditionOutcome(condition, blindScope, outcome);
    JSType expected = interpreter.getRestrictedWithoutUndefined(interpreter.getNativeType(JSTypeRegistry.OBJECT_TYPE));
    JSType actual = informedScope.getVariable("param").getType();
    assertEquals(expected, actual);
  }

  @Test
  public void testGetPreciserScopeKnowingConditionOutcomeFalse() {
    Node condition = new Node(CALL, null, new Node(GETPROP, null, new Node(NAME, "goog")), new Node(STRING, "isDef"),
        new Node(new Node(NAME, "param"), null));
    FlowScope blindScope = new FlowScope();
    boolean outcome = false;
    FlowScope informedScope = interpreter.getPreciserScopeKnowingConditionOutcome(condition, blindScope, outcome);
    assertNull(informedScope.getVariable("param"));
  }
}