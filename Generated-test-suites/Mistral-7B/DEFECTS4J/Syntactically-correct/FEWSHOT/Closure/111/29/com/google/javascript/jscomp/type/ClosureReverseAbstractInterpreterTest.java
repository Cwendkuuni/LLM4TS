package com.google.javascript.jscomp.type;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import com.google.common.base.Function;
import com.google.common.collect.ImmutableMap;
import com.google.javascript.jscomp.CodingConvention;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.jstype.JSType;
import com.google.javascript.rhino.jstype.JSTypeRegistry;
import com.google.javascript.rhino.jstype.ObjectType;
import com.google.javascript.rhino.jstype.Visitor;
import com.google.javascript.rhino.jstype.Visitor.VisitorType;

import java.util.Map;

@RunWith(JUnit4.class)
public class ClosureReverseAbstractInterpreterTest {

  @Test
  public void testIsDef() {
    JSTypeRegistry typeRegistry = new JSTypeRegistry();
    ClosureReverseAbstractInterpreter interpreter =
        new ClosureReverseAbstractInterpreter(CodingConvention.STANDARD, typeRegistry);

    JSType type = interpreter.getType(Node.NEW_INT_LITERAL(10));
    TypeRestriction restriction = new TypeRestriction(type, true);
    JSType restrictedType = interpreter.restricters.get("isDef").apply(restriction);
    assertNotNull(restrictedType);
    assertNotNull(restrictedType.getDeclaredFields());
    assertNull(restrictedType.getDeclaredFunctions());

    type = interpreter.getType(Node.NEW_NULL());
    restriction = new TypeRestriction(type, false);
    JSType nullType = interpreter.restricters.get("isDef").apply(restriction);
    assertNull(nullType);
  }

  @Test
  public void testIsDefAndNotNull() {
    JSTypeRegistry typeRegistry = new JSTypeRegistry();
    ClosureReverseAbstractInterpreter interpreter =
        new ClosureReverseAbstractInterpreter(CodingConvention.STANDARD, typeRegistry);

    JSType type = interpreter.getType(Node.NEW_INT_LITERAL(10));
    TypeRestriction restriction = new TypeRestriction(type, true);
    JSType restrictedType = interpreter.restricters.get("isDefAndNotNull").apply(restriction);
    assertNotNull(restrictedType);
    assertNotNull(restrictedType.getDeclaredFields());
    assertNull(restrictedType.getDeclaredFunctions());

    type = interpreter.getType(Node.NEW_NULL());
    restriction = new TypeRestriction(type, false);
    JSType nullVoidType = interpreter.restricters.get("isDefAndNotNull").apply(restriction);
    assertNull(nullVoidType);
  }

  @Test
  public void testIsString() {
    JSTypeRegistry typeRegistry = new JSTypeRegistry();
    ClosureReverseAbstractInterpreter interpreter =
        new ClosureReverseAbstractInterpreter(CodingConvention.STANDARD, typeRegistry);

    JSType type = interpreter.getType(Node.NEW_STRING("foo"));
    TypeRestriction restriction = new TypeRestriction(type, true);
    JSType restrictedType = interpreter.restricters.get("isString").apply(restriction);
    assertNotNull(restrictedType);
    assertNotNull(restrictedType.getDeclaredFields());
    assertNull(restrictedType.getDeclaredFunctions());

    type = interpreter.getType(Node.NEW_STRING(""));
    restriction = new TypeRestriction(type, false);
    JSType nullType = interpreter.restricters.get("isString").apply(restriction);
    assertNull(nullType);
  }

  @Test
  public void testIsBoolean() {
    JSTypeRegistry typeRegistry = new JSTypeRegistry();
    ClosureReverseAbstractInterpreter interpreter =
        new ClosureReverseAbstractInterpreter(CodingConvention.STANDARD, typeRegistry);

    JSType type = interpreter.getType(Node.NEW_BOOL(true));
    TypeRestriction restriction = new TypeRestriction(type, true);
    JSType restrictedType = interpreter.restricters.get("isBoolean").apply(restriction);
    assertNotNull(restrictedType);
    assertNotNull(restrictedType.getDeclaredFields());
    assertNull(restrictedType.getDeclaredFunctions());

    type = interpreter.getType(Node.NEW_BOOL(false));
    restriction = new TypeRestriction(type, false);
    JSType nullVoidType = interpreter.restricters.get("isBoolean").apply(restriction);
    assertNull(nullVoidType);
  }

  @Test
  public void testIsNumber() {
    JSTypeRegistry typeRegistry = new JSTypeRegistry();
    ClosureReverseAbstractInterpreter interpreter =
        new ClosureReverseAbstractInterpreter(CodingConvention.STANDARD, typeRegistry);

    JSType type = interpreter.getType(Node.NEW_INT_LITERAL(10));
    TypeRestriction restriction = new TypeRestriction(type, true);
    JSType restrictedType = interpreter.restricters.get("isNumber").apply(restriction);
    assertNotNull(restrictedType);
    assertNotNull(restrictedType.getDeclaredFields());
    assertNull(restrictedType.getDeclaredFunctions());

    type = interpreter.getType(Node.NEW_NUMBER(10.5));
    restriction = new TypeRestriction(type, false);
    JSType nullType = interpreter.restricters.get("isNumber").apply(restriction);
    assertNull(nullType);
  }

  @Test
  public void testIsFunction() {
    JSTypeRegistry typeRegistry = new JSTypeRegistry();
    ClosureReverseAbstractInterpreter interpreter =
        new ClosureReverseAbstractInterpreter(CodingConvention.STANDARD, typeRegistry);

    JSType type = interpreter.getType(Node.NEW_FUNCTION(null, null, null, null, null, null));
    TypeRestriction restriction = new TypeRestriction(type, true);
    JSType restrictedType = interpreter.restricters.get("isFunction").apply(restriction);
    assertNotNull(restrictedType);
    assertNotNull(restrictedType.getDeclaredFields());
    assertNull(restrictedType.getDeclaredFunctions());

    type = interpreter.getType(Node.NEW_NULL());
    restriction = new TypeRestriction(type, false);
    JSType nullVoidType = interpreter.restricters.get("isFunction").apply(restriction);
    assertNull(nullVoidType);
  }

  @Test
  public void testIsArray() {
    JSTypeRegistry typeRegistry = new JSTypeRegistry();
    ClosureReverseAbstractInterpreter interpreter =
        new ClosureReverseAbstractInterpreter(CodingConvention.STANDARD, typeRegistry);

    JSType type = interpreter.getType(Node.NEW_ARRAY(interpreter.getType(Node.NEW_INT_LITERAL(10))));
    TypeRestriction restriction = new TypeRestriction(type, true);
    JSType restrictedType = interpreter.restricters.get("isArray").apply(restriction);
    assertNotNull(restrictedType);
    assertNotNull(restrictedType.getDeclaredFields());
    assertNull(restrictedType.getDeclaredFunctions());

    type = interpreter.getType(Node.NEW_INT_LITERAL(10));
    restriction = new TypeRestriction(type, false);
    JSType nullType = interpreter.restricters.get("isArray").apply(restriction);
    assertNull(nullType);
  }

  @Test
  public void testIsObject() {
    JSTypeRegistry typeRegistry = new JSTypeRegistry();
    ClosureReverseAbstractInterpreter interpreter =
        new ClosureReverseAbstractInterpreter(CodingConvention.STANDARD, typeRegistry);

    JSType type = interpreter.getType(Node.NEW_OBJECT());
    TypeRestriction restriction = new TypeRestriction(type, true);
    JSType restrictedType = interpreter.restricters.get("isObject").apply(restriction);
    assertNotNull(restrictedType);
    assertNotNull(restrictedType.getDeclaredFields());
    assertNull(restrictedType.getDeclaredFunctions());

    type = interpreter.getType(Node.NEW_NULL());
    restriction = new TypeRestriction(type, false);
    JSType nullVoidType = interpreter.restricters.get("isObject").apply(restriction);
    assertNull(nullVoidType);
  }

  @Test(expected = AssertionError.class)
  public void testUnknownFunctionCall() {
    JSTypeRegistry typeRegistry = new JSTypeRegistry();
    ClosureReverseAbstractInterpreter interpreter =
        new ClosureReverseAbstractInterpreter(CodingConvention.STANDARD, typeRegistry);

    Node condition = Node.NEW_CALL(Node.NEW_IDENTIFIER("unknownFunction"));
    FlowScope blindScope = new FlowScope(VisitorType.EMPTY);
    interpreter.getPreciserScopeKnowingConditionOutcome(condition, blindScope, true);
  }
}

class TypeRestriction {
  private final JSType type;
  private final boolean outcome;

  private TypeRestriction(JSType type, boolean outcome) {
    this.type = type;
    this.outcome = outcome;
  }
}