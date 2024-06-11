import static org.junit.Assert.*;

import com.google.javascript.jscomp.CodingConvention;
import com.google.javascript.jscomp.JSTypeRegistry;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.jstype.JSType;
import com.google.javascript.rhino.jstype.ObjectType;
import com.google.javascript.rhino.jstype.Visitor;

import java.util.Map;

import org.junit.Before;
import org.junit.Test;

public class ClosureReverseAbstractInterpreterTest {

  private ClosureReverseAbstractInterpreter interpreter;
  private JSTypeRegistry typeRegistry;
  private CodingConvention convention;

  @Before
  public void setup() {
    typeRegistry = new JSTypeRegistry();
    convention = new CodingConvention();
    interpreter = new ClosureReverseAbstractInterpreter(convention, typeRegistry);
  }

  @Test
  public void testGetPreciserScopeKnowingConditionOutcome_isDef() {
    Node condition = Node.newCall("goog.isDef", Node.newString("foo"));
    FlowScope blindScope = new FlowScope();
    FlowScope result = interpreter.getPreciserScopeKnowingConditionOutcome(condition, blindScope, true);
    JSType expected = typeRegistry.createNamedType("foo");
    JSType actual = result.getVariable("foo").getType();
    assertEquals(expected, actual);
  }

  @Test
  public void testGetPreciserScopeKnowingConditionOutcome_isDef_false() {
    Node condition = Node.newCall("goog.isDef", Node.newString("foo"));
    FlowScope blindScope = new FlowScope();
    FlowScope result = interpreter.getPreciserScopeKnowingConditionOutcome(condition, blindScope, false);
    assertNull(result.getVariable("foo"));
  }

  @Test
  public void testGetPreciserScopeKnowingConditionOutcome_isNull() {
    Node condition = Node.newCall("goog.isNull", Node.newName("x"));
    Node xNode = Node.newLocal("x");
    FlowScope blindScope = new FlowScope();
    blindScope.declareLocal(xNode, typeRegistry.createUndefinedType());
    FlowScope result = interpreter.getPreciserScopeKnowingConditionOutcome(condition, blindScope, true);
    JSType expected = typeRegistry.createNullType();
    JSType actual = result.getVariable("x").getType();
    assertEquals(expected, actual);
  }

  @Test
  public void testGetPreciserScopeKnowingConditionOutcome_isNull_false() {
    Node condition = Node.newCall("goog.isNull", Node.newName("x"));
    Node xNode = Node.newLocal("x");
    FlowScope blindScope = new FlowScope();
    blindScope.declareLocal(xNode, typeRegistry.createNumberType());
    FlowScope result = interpreter.getPreciserScopeKnowingConditionOutcome(condition, blindScope, false);
    assertNull(result.getVariable("x"));
  }

  @Test
  public void testGetPreciserScopeKnowingConditionOutcome_isDefAndNotNull() {
    Node condition = Node.newCall("goog.isDefAndNotNull", Node.newString("foo"));
    FlowScope blindScope = new FlowScope();
    FlowScope result = interpreter.getPreciserScopeKnowingConditionOutcome(condition, blindScope, true);
    JSType expected = typeRegistry.createNamedType("foo");
    JSType actual = result.getVariable("foo").getType();
    assertEquals(expected, actual);
  }

  @Test(expected = NullPointerException.class)
  public void testGetPreciserScopeKnowingConditionOutcome_isDefAndNotNull_false() {
    Node condition = Node.newCall("goog.isDefAndNotNull", Node.newString("foo"));
    FlowScope blindScope = new FlowScope();
    interpreter.getPreciserScopeKnowingConditionOutcome(condition, blindScope, false);
  }

  @Test
  public void testGetPreciserScopeKnowingConditionOutcome_isString() {
    Node condition = Node.newCall("goog.isString", Node.newString("foo"));
    FlowScope blindScope = new FlowScope();
    FlowScope result = interpreter.getPreciserScopeKnowingConditionOutcome(condition, blindScope, true);
    JSType expected = typeRegistry.createStringType();
    JSType actual = result.getVariable("foo").getType();
    assertEquals(expected, actual);
  }

  @Test
  public void testGetPreciserScopeKnowingConditionOutcome_isBoolean() {
    Node condition = Node.newCall("goog.isBoolean", Node.newNumber(1));
    FlowScope blindScope = new FlowScope();
    FlowScope result = interpreter.getPreciserScopeKnowingConditionOutcome(condition, blindScope, true);
    JSType expected = typeRegistry.createBooleanType();
    JSType actual = result.getVariable("goog_boolean1").getType();
    assertEquals(expected, actual);
  }

  @Test
  public void testGetPreciserScopeKnowingConditionOutcome_isNumber() {
    Node condition = Node.newCall("goog.isNumber", Node.newString("1"));
    FlowScope blindScope = new FlowScope();
    FlowScope result = interpreter.getPreciserScopeKnowingConditionOutcome(condition, blindScope, true);
    JSType expected = typeRegistry.createNumberType();
    JSType actual = result.getVariable("goog_number1").getType();
    assertEquals(expected, actual);
  }

  @Test
  public void testGetPreciserScopeKnowingConditionOutcome_isFunction() {
    Node condition = Node.newCall("goog.isFunction", Node.newNull());
    FlowScope blindScope = new FlowScope();
    FlowScope result = interpreter.getPreciserScopeKnowingConditionOutcome(condition, blindScope, false);
    assertNull(result.getVariable("goog_function"));

    condition = Node.newCall("goog.isFunction", Node.newName("testFunction"));
    Node testFunctionNode = Node.newFunction(Node.newName("testFunction"),
        new ObjectType(Node.newName("Function")), new ObjectType(Node.newName("Object")),
        new ObjectType(Node.newName("Object")));
    FlowScope functionScope = new FlowScope();
    functionScope.declareLocal(Node.newName("testFunction"), testFunctionNode);
    blindScope = functionScope;
    result = interpreter.getPreciserScopeKnowingConditionOutcome(condition, blindScope, true);
    JSType expected = new FunctionType(Node.newName("Function"), new ObjectType(Node.newName("Object")),
        new ObjectType(Node.newName("Object")));
    JSType actual = result.getVariable("goog_function").getType();
    assertEquals(expected, actual);
  }

  @Test
  public void testGetPreciserScopeKnowingConditionOutcome_isArray() {
    Node condition = Node.newCall("goog.isArray", Node.newString("[]"));
    FlowScope blindScope = new FlowScope();
    FlowScope result = interpreter.getPreciserScopeKnowingConditionOutcome(condition, blindScope, true);
    JSType expected = typeRegistry.createArrayType(getTypeRegistry().createArrayType(new ObjectType(Node.newName("Object"))));
    JSType actual = result.getVariable("goog_array").getType();
    assertEquals(expected, actual);
  }

  @Test
  public void testGetPreciserScopeKnowingConditionOutcome_isObject() {
    Node condition = Node.newCall("goog.isObject", Node.newNumber(1));
    FlowScope blindScope = new FlowScope();
    FlowScope result = interpreter.getPreciserScopeKnowingConditionOutcome(condition, blindScope, true);
    JSType expected = typeRegistry.createObjectType(new ObjectType(Node.newName("Object")));
    JSType actual = result.getVariable("goog_object1").getType();
    assertEquals(expected, actual);
  }
}