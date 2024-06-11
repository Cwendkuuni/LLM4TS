import static org.junit.Assert.*;
import static com.google.javascript.jscomp.ClosureReverseAbstractInterpreter.*;

import com.google.common.collect.ImmutableMap;
import com.google.javascript.jscomp.Node;
import com.google.javascript.rhino.NodeOperations;
import com.google.javascript.rhino.Token;

import java.util.Map;

/**
 * Tests for the {@link ClosureReverseAbstractInterpreter} class.
 */
public class ClosureReverseAbstractInterpreterTest {

  private static final JSTypeRegistry typeRegistry = new JSTypeRegistry();
  private static final CodingConvention codingConvention = new CodingConvention();

  @org.junit.Test
  public void testIsDef() {
    Node condition = NodeOperations.newCall("goog.isDef", NodeOperations.newQualifiedName("testObject"));
    FlowScope blindScope = new FlowScope(typeRegistry, codingConvention);
    FlowScope informedScope = new ClosureReverseAbstractInterpreter(codingConvention, typeRegistry)
        .getPreciserScopeKnowingConditionOutcome(condition, blindScope, true);
    assertEquals(NodeOperations.newObjectLiteral(), informedScope.getVariable("testObject"));
  }

  @org.junit.Test
  public void testIsDefFalse() {
    Node condition = NodeOperations.newCall("goog.isDef", NodeOperations.newNull());
    FlowScope blindScope = new FlowScope(typeRegistry, codingConvention);
    FlowScope informedScope = new ClosureReverseAbstractInterpreter(codingConvention, typeRegistry)
        .getPreciserScopeKnowingConditionOutcome(condition, blindScope, false);
    assertNull(informedScope.getVariable("testObject"));
  }

  @org.junit.Test
  public void testIsNull() {
    Node condition = NodeOperations.newCall("goog.isNull", NodeOperations.newQualifiedName("testObject"));
    FlowScope blindScope = new FlowScope(typeRegistry, codingConvention);
    FlowScope informedScope = new ClosureReverseAbstractInterpreter(codingConvention, typeRegistry)
        .getPreciserScopeKnowingConditionOutcome(condition, blindScope, true);
    assertNull(informedScope.getVariable("testObject"));
  }

  @org.junit.Test
  public void testIsNotNull() {
    Node condition = NodeOperations.newCall("goog.isNull", NodeOperations.newUndefined());
    FlowScope blindScope = new FlowScope(typeRegistry, codingConvention);
    FlowScope informedScope = new ClosureReverseAbstractInterpreter(codingConvention, typeRegistry)
        .getPreciserScopeKnowingConditionOutcome(condition, blindScope, false);
    assertNull(informedScope.getVariable("testObject"));
  }

  @org.junit.Test
  public void testIsDefAndNotNull() {
    Node condition = NodeOperations.newCall("goog.isDefAndNotNull", NodeOperations.newQualifiedName("testObject"));
    FlowScope blindScope = new FlowScope(typeRegistry, codingConvention);
    FlowScope informedScope = new ClosureReverseAbstractInterpreter(codingConvention, typeRegistry)
        .getPreciserScopeKnowingConditionOutcome(condition, blindScope, true);
    assertEquals(NodeOperations.newObjectLiteral(), informedScope.getVariable("testObject"));
  }

  @org.junit.Test(expected = AssertionError.class)
  public void testIsDefAndNotNullNull() {
    Node condition = NodeOperations.newCall("goog.isDefAndNotNull", NodeOperations.newNull());
    FlowScope blindScope = new FlowScope(typeRegistry, codingConvention);
    new ClosureReverseAbstractInterpreter(codingConvention, typeRegistry)
        .getPreciserScopeKnowingConditionOutcome(condition, blindScope, true);
  }

  @org.junit.Test
  public void testIsString() {
    Node condition = NodeOperations.newCall("goog.isString", NodeOperations.newString("test"));
    FlowScope blindScope = new FlowScope(typeRegistry, codingConvention);
    FlowScope informedScope = new ClosureReverseAbstractInterpreter(codingConvention, typeRegistry)
        .getPreciserScopeKnowingConditionOutcome(condition, blindScope, true);
    assertEquals(NODE_STRING, informedScope.getVariable("test"));
  }

  @org.junit.Test
  public void testIsStringFalse() {
    Node condition = NodeOperations.newCall("goog.isString", NodeOperations.newNumber(1));
    FlowScope blindScope = new FlowScope(typeRegistry, codingConvention);
    FlowScope informedScope = new ClosureReverseAbstractInterpreter(codingConvention, typeRegistry)
        .getPreciserScopeKnowingConditionOutcome(condition, blindScope, false);
    assertNull(informedScope.getVariable("test"));
  }

  @org.junit.Test
  public void testIsBoolean() {
    Node condition = NodeOperations.newCall("goog.isBoolean", NodeOperations.newBoolean(true));
    FlowScope blindScope = new FlowScope(typeRegistry, codingConvention);
    FlowScope informedScope = new ClosureReverseAbstractInterpreter(codingConvention, typeRegistry)
        .getPreciserScopeKnowingConditionOutcome(condition, blindScope, true);
    assertEquals(NODE_BOOL, informedScope.getVariable("test"));
  }

  @org.junit.Test
  public void testIsBooleanFalse() {
    Node condition = NodeOperations.newCall("goog.isBoolean", NodeOperations.newNumber(1));
    FlowScope blindScope = new FlowScope(typeRegistry, codingConvention);
    FlowScope informedScope = new ClosureReverseAbstractInterpreter(codingConvention, typeRegistry)
        .getPreciserScopeKnowingConditionOutcome(condition, blindScope, false);
    assertNull(informedScope.getVariable("test"));
  }

  @org.junit.Test
  public void testIsNumber() {
    Node condition = NodeOperations.newCall("goog.isNumber", NodeOperations.newNumber(1));
    FlowScope blindScope = new FlowScope(typeRegistry, codingConvention);
    FlowScope informedScope = new ClosureReverseAbstractInterpreter(codingConvention, typeRegistry)
        .getPreciserScopeKnowingConditionOutcome(condition, blindScope, true);
    assertEquals(NODE_NUMBER, informedScope.getVariable("test"));
  }

  @org.junit.Test
  public void testIsNumberFalse() {
    Node condition = NodeOperations.newCall("goog.isNumber", NodeOperations.newString("test"));
    FlowScope blindScope = new FlowScope(typeRegistry, codingConvention);
    FlowScope informedScope = new ClosureReverseAbstractInterpreter(codingConvention, typeRegistry)
        .getPreciserScopeKnowingConditionOutcome(condition, blindScope, false);
    assertNull(informedScope.getVariable("test"));
  }

  @org.junit.Test
  public void testIsFunction() {
    Node condition = NodeOperations.newCall("goog.isFunction", NodeOperations.newFunction(null, null, null, null));
    FlowScope blindScope = new FlowScope(typeRegistry, codingConvention);
    FlowScope informedScope = new ClosureReverseAbstractInterpreter(codingConvention, typeRegistry)
        .getPreciserScopeKnowingConditionOutcome(condition, blindScope, true);
    assertEquals(NODE_FUNCTION, informedScope.getVariable("test"));
  }

  @org.junit.Test
  public void testIsFunctionFalse() {
    Node condition = NodeOperations.newCall("goog.isFunction", NodeOperations.newNumber(1));
    FlowScope blindScope = new FlowScope(typeRegistry, codingConvention);
    FlowScope informedScope = new ClosureReverseAbstractInterpreter(codingConvention, typeRegistry)
        .getPreciserScopeKnowingConditionOutcome(condition, blindScope, false);
    assertNull(informedScope.getVariable("test"));
  }

  @org.junit.Test
  public void testIsArrayTrue() {
    Node condition = NodeOperations.newCall("goog.isArray", NodeOperations.newArrayLiteral(new Object[]{}));
    FlowScope blindScope = new FlowScope(typeRegistry, codingConvention);
    FlowScope informedScope = new ClosureReverseAbstractInterpreter(codingConvention, typeRegistry)
        .getPreciserScopeKnowingConditionOutcome(condition, blindScope, true);
    assertEquals(NODE_ARRAY, informedScope.getVariable("test"));
  }

  @org.junit.Test
  public void testIsArrayFalse() {
    Node condition = NodeOperations.newCall("goog.isArray", NodeOperations.newObjectLiteral());
    FlowScope blindScope = new FlowScope(typeRegistry, codingConvention);
    FlowScope informedScope = new ClosureReverseAbstractInterpreter(codingConvention, typeRegistry)
        .getPreciserScopeKnowingConditionOutcome(condition, blindScope, false);
    assertNull(informedScope.getVariable("test"));
  }

  @org.junit.Test
  public void testIsObjectTrue() {
    Node condition = NodeOperations.newCall("goog.isObject", NodeOperations.newObjectLiteral());
    FlowScope blindScope = new FlowScope(typeRegistry, codingConvention);
    FlowScope informedScope = new ClosureReverseAbstractInterpreter(codingConvention, typeRegistry)
        .getPreciserScopeKnowingConditionOutcome(condition, blindScope, true);
    assertEquals(NODE_OBJECT, informedScope.getVariable("test"));
  }

  @org.junit.Test
  public void testIsObjectFalse() {
    Node condition = NodeOperations.newCall("goog.isObject", NodeOperations.newNumber(1));
    FlowScope blindScope = new FlowScope(typeRegistry, codingConvention);
    FlowScope informedScope = new ClosureReverseAbstractInterpreter(codingConvention, typeRegistry)
        .getPreciserScopeKnowingConditionOutcome(condition, blindScope, false);
    assertNull(informedScope.getVariable("test"));
  }
}