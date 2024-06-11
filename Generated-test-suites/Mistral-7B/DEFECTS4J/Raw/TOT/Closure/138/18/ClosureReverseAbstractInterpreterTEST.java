import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import com.google.javascript.jscomp.ClosureReverseAbstractInterpreter;
import com.google.javascript.jscomp.CodingConvention;
import com.google.javascript.jscomp.JSType;
import com.google.javascript.jscomp.JSTypeRegistry;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.jstype.FunctionType;
import com.google.javascript.rhino.jstype.ObjectType;

import org.junit.Test;

public class ClosureReverseAbstractInterpreterTest {
  @Test
  public void testIsDef() {
    JSTypeRegistry typeRegistry = new JSTypeRegistry();
    CodingConvention convention = new CodingConvention();
    ClosureReverseAbstractInterpreter interpreter = new ClosureReverseAbstractInterpreter(
        convention, typeRegistry);

    Node condition1 = Node.newCall("goog.isDef", Node.newUndefinedNode());
    FlowScope blindScope = new FlowScope();
    FlowScope informedScope = interpreter.getPreciserScopeKnowingConditionOutcome(
        condition1, blindScope, true);
    JSType type1 = informedScope.getVariable("goog.isDef").getType();
    assertTrue(type1.isSubtype(JSType.BOOLEAN));

    Node condition2 = Node.newCall("goog.isDef", Node.newNull());
    informedScope = interpreter.getPreciserScopeKnowingConditionOutcome(
        condition2, informedScope, false);
    type1 = informedScope.getVariable("goog.isDef").getType();
    assertNull(type1);

    Node condition3 = Node.newCall("goog.isDef", Node.newBoolean(true));
    informedScope = interpreter.getPreciserScopeKnowingConditionOutcome(
        condition3, informedScope, true);
    type1 = informedScope.getVariable("goog.isDef").getType();
    assertTrue(type1.isSubtype(JSType.BOOLEAN));

    Node condition4 = Node.newCall("goog.isDef", Node.newBoolean(false));
    informedScope = interpreter.getPreciserScopeKnowingConditionOutcome(
        condition4, informedScope, false);
    type1 = informedScope.getVariable("goog.isDef").getType();
    assertNull(type1);
  }

  @Test
  public void testIsDefAndNotNull() {
    JSTypeRegistry typeRegistry = new JSTypeRegistry();
    CodingConvention convention = new CodingConvention();
    ClosureReverseAbstractInterpreter interpreter = new ClosureReverseAbstractInterpreter(
        convention, typeRegistry);

    Node condition1 = Node.newCall("goog.isDefAndNotNull", Node.newNull());
    FlowScope blindScope = new FlowScope();
    FlowScope informedScope = interpreter.getPreciserScopeKnowingConditionOutcome(
        condition1, blindScope, false);
    JSType type1 = informedScope.getVariable("goog.isDefAndNotNull").getType();
    assertNull(type1);

    Node condition2 = Node.newCall("goog.isDefAndNotNull", Node.newBoolean(true));
    informedScope = interpreter.getPreciserScopeKnowingConditionOutcome(
        condition2, informedScope, true);
    type1 = informedScope.getVariable("goog.isDefAndNotNull").getType();
    assertTrue(type1.isSubtype(JSType.BOOLEAN));
  }

  @Test
  public void testIsArray() {
    JSTypeRegistry typeRegistry = new JSTypeRegistry();
    CodingConvention convention = new CodingConvention();
    ClosureReverseAbstractInterpreter interpreter = new ClosureReverseAbstractInterpreter(
        convention, typeRegistry);

    Node condition1 = Node.newCall("goog.isArray", Node.newBoolean(true));
    FlowScope blindScope = new FlowScope();
    FlowScope informedScope = interpreter.getPreciserScopeKnowingConditionOutcome(
        condition1, blindScope, true);
    JSType type1 = informedScope.getVariable("goog.isArray").getType();
    assertTrue(type1.isSubtype(JSType.BOOLEAN));

    Node condition2 = Node.newCall("goog.isArray", Node.newNull());
    informedScope = interpreter.getPreciserScopeKnowingConditionOutcome(
        condition2, informedScope, false);
    type1 = informedScope.getVariable("goog.isArray").getType();
    assertNull(type1);

    Node condition3 = Node.newCall("goog.isArray", Node.newObject(
        new FunctionType(new ObjectType(new JSType[0]), new ObjectType(null))));
    informedScope = interpreter.getPreciserScopeKnowingConditionOutcome(
        condition3, informedScope, false);
    type1 = informedScope.getVariable("goog.isArray").getType();
    assertNull(type1);

    Node condition4 = Node.newCall("goog.isArray", Node.newArray(new Object[0]));
    informedScope = interpreter.getPreciserScopeKnowingConditionOutcome(
        condition4, informedScope, true);
    type1 = informedScope.getVariable("goog.isArray").getType();
    assertTrue(type1.isSubtype(JSType.BOOLEAN));
  }

  @Test
  public void testIsObject() {
    JSTypeRegistry typeRegistry = new JSTypeRegistry();
    CodingConvention convention = new CodingConvention();
    ClosureReverseAbstractInterpreter interpreter = new ClosureReverseAbstractInterpreter(
        convention, typeRegistry);

    Node condition1 = Node.newCall("goog.isObject", Node.newBoolean(true));
    FlowScope blindScope = new FlowScope();
    FlowScope informedScope = interpreter.getPreciserScopeKnowingConditionOutcome(
        condition1, blindScope, true);
    JSType type1 = informedScope.getVariable("goog.isObject").getType();
    assertTrue(type1.isSubtype(JSType.BOOLEAN));

    Node condition2 = Node.newCall("goog.isObject", Node.newNull());
    informedScope = interpreter.getPreciserScopeKnowingConditionOutcome(
        condition2, informedScope, false);
    type1 = informedScope.getVariable("goog.isObject").getType();
    assertNull(type1);

    Node condition3 = Node.newCall("goog.isObject", Node.newFunction(
        new ObjectType(new JSType[0]), new ObjectType(null)));
    informedScope = interpreter.getPreciserScopeKnowingConditionOutcome(
        condition3, informedScope, true);
    type1 = informedScope.getVariable("goog.isObject").getType();
    assertTrue(type1.isSubtype(JSType.BOOLEAN));

    Node condition4 = Node.newCall("goog.isObject", Node.newObject(
        new ObjectType(new JSType[0])));
    informedScope = interpreter.getPreciserScopeKnowingConditionOutcome(
        condition4, informedScope, true);
    type1 = informedScope.getVariable("goog.isObject").getType();
    assertTrue(type1.isSubtype(JSType.BOOLEAN));
  }

  @Test
  public void testIsString() {
    JSTypeRegistry typeRegistry = new JSTypeRegistry();
    CodingConvention convention = new CodingConvention();
    ClosureReverseAbstractInterpreter interpreter = new ClosureReverseAbstractInterpreter(
        convention, typeRegistry);

    Node condition1 = Node.newCall("goog.isString", Node.newNull());
    FlowScope blindScope = new FlowScope();
    FlowScope informedScope = interpreter.getPreciserScopeKnowingConditionOutcome(
        condition1, blindScope, false);
    JSType type1 = informedScope.getVariable("goog.isString").getType();
    assertNull(type1);

    Node condition2 = Node.newCall("goog.isString", Node.newString(""));
    informedScope = interpreter.getPreciserScopeKnowingConditionOutcome(
        condition2, informedScope, true);
    type1 = informedScope.getVariable("goog.isString").getType();
    assertTrue(type1.isSubtype(JSType.BOOLEAN));

    Node condition3 = Node.newCall("goog.isString", Node.newString("abc"));
    informedScope = interpreter.getPreciserScopeKnowingConditionOutcome(
        condition3, informedScope, true);
    type1 = informedScope.getVariable("goog.isString").getType();
    assertTrue(type1.isSubtype(JSType.BOOLEAN));
  }

  @Test
  public void testIsBoolean() {
    JSTypeRegistry typeRegistry = new JSTypeRegistry();
    CodingConvention convention = new CodingConvention();
    ClosureReverseAbstractInterpreter interpreter = new ClosureReverseAbstractInterpreter(
        convention, typeRegistry);

    Node condition1 = Node.newCall("goog.isBoolean", Node.newBoolean(true));
    FlowScope blindScope = new FlowScope();
    FlowScope informedScope = interpreter.getPreciserScopeKnowingConditionOutcome(
        condition1, blindScope, true);
    JSType type1 = informedScope.getVariable("goog.isBoolean").getType();
    assertTrue(type1.isSubtype(JSType.BOOLEAN));

    Node condition2 = Node.newCall("goog.isBoolean", Node.newNull());
    informedScope = interpreter.getPreciserScopeKnowingConditionOutcome(
        condition2, informedScope, false);
    type1 = informedScope.getVariable("goog.isBoolean").getType();
    assertNull(type1);

    Node condition3 = Node.newCall("goog.isBoolean", Node.newNumber(1));
    informedScope = interpreter.getPreciserScopeKnowingConditionOutcome(
        condition3, informedScope, false);
    type1 = informedScope.getVariable("goog.isBoolean").getType();
    assertNull(type1);
  }

  @Test
  public void testIsNumber() {
    JSTypeRegistry typeRegistry = new JSTypeRegistry();
    CodingConvention convention = new CodingConvention();
    ClosureReverseAbstractInterpreter interpreter = new ClosureReverseAbstractInterpreter(
        convention, typeRegistry);

    Node condition1 = Node.newCall("goog.isNumber", Node.newBoolean(true));
    FlowScope blindScope = new FlowScope();
    FlowScope informedScope = interpreter.getPreciserScopeKnowingConditionOutcome(
        condition1, blindScope, true);
    JSType type1 = informedScope.getVariable("goog.isNumber").getType();
    assertTrue(type1.isSubtype(JSType.BOOLEAN));

    Node condition2 = Node.newCall("goog.isNumber", Node.newNull());
    informedScope = interpreter.getPreciserScopeKnowingConditionOutcome(
        condition2, informedScope, false);
    type1 = informedScope.getVariable("goog.isNumber").getType();
    assertNull(type1);

    Node condition3 = Node.newCall("goog.isNumber", Node.newString(""));
    informedScope = interpreter.getPreciserScopeKnowingConditionOutcome(
        condition3, informedScope, false);
    type1 = informedScope.getVariable("goog.isNumber").getType();
    assertNull(type1);

    Node condition4 = Node.newCall("goog.isNumber", Node.newNumber(1));
    informedScope = interpreter.getPreciserScopeKnowingConditionOutcome(
        condition4, informedScope, true);
    type1 = informedScope.getVariable("goog.isNumber").getType();
    assertTrue(type1.isSubtype(JSType.BOOLEAN));
  }

  @Test
  public void testIsFunction() {
    JSTypeRegistry typeRegistry = new JSTypeRegistry();
    CodingConvention convention = new CodingConvention();
    ClosureReverseAbstractInterpreter interpreter = new ClosureReverseAbstractInterpreter(
        convention, typeRegistry);

    Node condition1 = Node.newCall("goog.isFunction", Node.newFunction(
        new ObjectType(new JSType[0]), new ObjectType(null)));
    FlowScope blindScope = new FlowScope();
    FlowScope informedScope = interpreter.getPreciserScopeKnowingConditionOutcome(
        condition1, blindScope, true);
    JSType type1 = informedScope.getVariable("goog.isFunction").getType();
    assertTrue(type1.isSubtype(JSType.BOOLEAN));

    Node condition2 = Node.newCall("goog.isFunction", Node.newNull());
    informedScope = interpreter.getPreciserScopeKnowingConditionOutcome(
        condition2, informedScope, false);
    type1 = informedScope.getVariable("goog.isFunction").getType();
    assertNull(type1);

    Node condition3 = Node.newCall("goog.isFunction", Node.newObject(
        new ObjectType(new JSType[0])));
    informedScope = interpreter.getPreciserScopeKnowingConditionOutcome(
        condition3, informedScope, false);
    type1 = informedScope.getVariable("goog.isFunction").getType();
    assertNull(type1);
  }
}