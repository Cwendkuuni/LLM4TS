import static org.junit.Assert.*;
import static com.google.javascript.rhino.Token.*;
import static com.google.javascript.rhino.jstype.JSTypeNative.*;

import com.google.common.base.Function;
import com.google.common.collect.ImmutableMap;
import com.google.javascript.jscomp.CodingConvention;
import com.google.javascript.jscomp.JSType;
import com.google.javascript.jscomp.JSTypeRegistry;
import com.google.javascript.jscomp.Visitor;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.jstype.JSTypeNative;
import com.google.javascript.rhino.jstype.FunctionType;
import com.google.javascript.rhino.jstype.ObjectType;
import com.google.javascript.rhino.jstype.Visitor;
import org.junit.Before;
import org.junit.Test;

import java.util.Map;

public class ClosureReverseAbstractInterpreterTest {
  private JSTypeRegistry typeRegistry;
  private CodingConvention convention;
  private ClosureReverseAbstractInterpreter interpreter;

  @Before
  public void setup() {
    typeRegistry = new JSTypeRegistry();
    convention = new CodingConvention(typeRegistry);
    interpreter = new ClosureReverseAbstractInterpreter(convention, typeRegistry);
  }

  @Test
  public void testIsDef() {
    Node condition = Node.newCall(
        Node.newName("goog"), Node.newName("isDef"), Node.newUndefined());
    FlowScope blindScope = new FlowScope(typeRegistry, convention);
    FlowScope informedScope = interpreter.getPreciserScopeKnowingConditionOutcome(condition, blindScope, true);
    JSType returnedType = informedScope.getVariable("goog.isDef").getType();
    assertTrue(returnedType.isSubtype(getNativeType(BOOLEAN_TYPE)));
  }

  @Test
  public void testIsDefFalse() {
    Node condition = Node.newCall(
        Node.newName("goog"), Node.newName("isDef"), Node.newInt(0));
    FlowScope blindScope = new FlowScope(typeRegistry, convention);
    FlowScope informedScope = interpreter.getPreciserScopeKnowingConditionOutcome(condition, blindScope, false);
    assertTrue(informedScope == blindScope);
  }

  @Test
  public void testIsString() {
    Node condition = Node.newCall(
        Node.newName("goog"), Node.newName("isString"), Node.newString("hello"));
    FlowScope blindScope = new FlowScope(typeRegistry, convention);
    FlowScope informedScope = interpreter.getPreciserScopeKnowingConditionOutcome(condition, blindScope, true);
    JSType returnedType = informedScope.getVariable("goog.isString").getType();
    assertTrue(returnedType.isSubtype(STRING_TYPE));
  }

  @Test
  public void testIsStringFalse() {
    Node condition = Node.newCall(
        Node.newName("goog"), Node.newName("isString"), Node.newInt(10));
    FlowScope blindScope = new FlowScope(typeRegistry, convention);
    FlowScope informedScope = interpreter.getPreciserScopeKnowingConditionOutcome(condition, blindScope, false);
    assertTrue(informedScope == blindScope);
  }

  @Test
  public void testIsArrayTrue() {
    Node condition = Node.newCall(
        Node.newName("goog"), Node.newName("isArray"), Node.newArray(
            new Object(){}.getClass().getName()));
    FlowScope blindScope = new FlowScope(typeRegistry, convention);
    FlowScope informedScope = interpreter.getPreciserScopeKnowingConditionOutcome(condition, blindScope, true);
    JSType returnedType = informedScope.getVariable("goog.isArray").getType();
    assertTrue(returnedType.isSubtype(ARRAY_TYPE));
  }

  @Test
  public void testIsArrayFalse() {
    Node condition = Node.newCall(
        Node.newName("goog"), Node.newName("isArray"), Node.newString("hello"));
    FlowScope blindScope = new FlowScope(typeRegistry, convention);
    FlowScope informedScope = interpreter.getPreciserScopeKnowingConditionOutcome(condition, blindScope, false);
    assertTrue(informedScope == blindScope);
  }

  @Test(expected = AssertionError.class)
  public void testIsFunction() {
    Node condition = Node.newCall(
        Node.newName("goog"), Node.newName("isFunction"), Node.newInt(0));
    FlowScope blindScope = new FlowScope(typeRegistry, convention);
    FlowScope informedScope = interpreter.getPreciserScopeKnowingConditionOutcome(condition, blindScope, true);
    fail("Expected an assertion error");
  }

  @Test
  public void testIsFunctionTrue() {
    Node condition = Node.newCall(
        Node.newName("goog"), Node.newName("isFunction"), Node.newFunction(
            null, null, null));
    FlowScope blindScope = new FlowScope(typeRegistry, convention);
    FlowScope informedScope = interpreter.getPreciserScopeKnowingConditionOutcome(condition, blindScope, true);
    JSType returnedType = informedScope.getVariable("goog.isFunction").getType();
    assertTrue(returnedType.isSubtype(FUNCTION_TYPE));
  }

  @Test
  public void testIsFunctionFalse() {
    Node condition = Node.newCall(
        Node.newName("goog"), Node.newName("isFunction"), Node.newString("hello"));
    FlowScope blindScope = new FlowScope(typeRegistry, convention);
    FlowScope informedScope = interpreter.getPreciserScopeKnowingConditionOutcome(condition, blindScope, false);
    assertTrue(informedScope == blindScope);
  }

  @Test
  public void testIsObjectTrue() {
    Node condition = Node.newCall(
        Node.newName("goog"), Node.newName("isObject"), Node.newObject());
    FlowScope blindScope = new FlowScope(typeRegistry, convention);
    FlowScope informedScope = interpreter.getPreciserScopeKnowingConditionOutcome(condition, blindScope, true);
    JSType returnedType = informedScope.getVariable("goog.isObject").getType();
    assertTrue(returnedType.isSubtype(OBJECT_TYPE));
  }

  @Test
  public void testIsObjectFalse() {
    Node condition = Node.newCall(
        Node.newName("goog"), Node.newName("isObject"), Node.newInt(0));
    FlowScope blindScope = new FlowScope(typeRegistry, convention);
    FlowScope informedScope = interpreter.getPreciserScopeKnowingConditionOutcome(condition, blindScope, false);
    assertTrue(informedScope == blindScope);
  }

  @Test
  public void testIsDefAndNotNull() {
    Node condition = Node.newCall(
        Node.newName("goog"), Node.newName("isDefAndNotNull"), Node.newUndefined());
    FlowScope blindScope = new FlowScope(typeRegistry, convention);
    FlowScope informedScope = interpreter.getPreciserScopeKnowingConditionOutcome(condition, blindScope, true);
    JSType returnedType = informedScope.getVariable("goog.isDefAndNotNull").getType();
    assertTrue(returnedType.isSubtype(getNativeType(BOOLEAN_TYPE)));
  }

  @Test
  public void testIsDefAndNotNullFalse() {
    Node condition = Node.newCall(
        Node.newName("goog"), Node.newName("isDefAndNotNull"), Node.newInt(0));
    FlowScope blindScope = new FlowScope(typeRegistry, convention);
    FlowScope informedScope = interpreter.getPreciserScopeKnowingConditionOutcome(condition, blindScope, false);
    assertTrue(informedScope == blindScope);
  }

  @Test
  public void testIsNull() {
    Node condition = Node.newCall(
        Node.newName("goog"), Node.newName("isNull"), Node.newNull());
    FlowScope blindScope = new FlowScope(typeRegistry, convention);
    FlowScope informedScope = interpreter.getPreciserScopeKnowingConditionOutcome(condition, blindScope, true);
    JSType returnedType = informedScope.getVariable("goog.isNull").getType();
    assertTrue(returnedType.isSubtype(getNativeType(BOOLEAN_TYPE)));
  }

  @Test
  public void testIsNullFalse() {
    Node condition = Node.newCall(
        Node.newName("goog"), Node.newName("isNull"), Node.newFunction(
            null, null, null));
    FlowScope blindScope = new FlowScope(typeRegistry, convention);
    FlowScope informedScope = interpreter.getPreciserScopeKnowingConditionOutcome(condition, blindScope, false);
    assertTrue(informedScope == blindScope);
  }
}