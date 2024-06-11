package com.google.javascript.jscomp.type;

import static com.google.javascript.jscomp.ClosureReverseAbstractInterpreterTest.assertJSTypeEqual;
import static org.junit.Assert.assertNull;

import com.google.common.base.Function;
import com.google.common.collect.ImmutableMap;
import com.google.javascript.jscomp.CodingConvention;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.jstype.JSType;
import com.google.javascript.rhino.jstype.JSTypeRegistry;
import com.google.javascript.rhino.jstype.ObjectType;
import com.google.javascript.rhino.jstype.Visitor;

import java.util.Map;

import org.junit.Test;

public class ClosureReverseAbstractInterpreterTest {

  private static final JSTypeRegistry TYPE_REGISTRY = new JSTypeRegistry();
  private static final CodingConvention CONVENTION = new CodingConvention();
  private static final ClosureReverseAbstractInterpreter INTERPRETER =
      new ClosureReverseAbstractInterpreter(CONVENTION, TYPE_REGISTRY);

  @Test
  public void testIsDef() {
    JSType type = TYPE_REGISTRY.createArrayType(TYPE_REGISTRY.createStringType());
    assertJSTypeEqual(TYPE_REGISTRY.createBooleanType(),
        INTERPRETER.restrictType("isDef", TypeRestriction.TRUE, type));

    type = TYPE_REGISTRY.createNumberType();
    assertJSTypeEqual(TYPE_REGISTRY.createVoidType(),
        INTERPRETER.restrictType("isDef", TypeRestriction.FALSE, type));
  }

  @Test
  public void testIsNull() {
    JSType type = TYPE_REGISTRY.createNullType();
    assertJSTypeEqual(type, INTERPRETER.restrictType("isNull", TypeRestriction.TRUE, type));

    type = TYPE_REGISTRY.createStringType();
    assertJSTypeEqual(TYPE_REGISTRY.createVoidType(),
        INTERPRETER.restrictType("isNull", TypeRestriction.FALSE, type));
  }

  @Test
  public void testIsDefAndNotNull() {
    JSType type = TYPE_REGISTRY.createObjectType();
    assertJSTypeEqual(TYPE_REGISTRY.createVoidType(),
        INTERPRETER.restrictType("isDefAndNotNull", TypeRestriction.FALSE, type));

    type = TYPE_REGISTRY.createStringType();
    assertJSTypeEqual(TYPE_REGISTRY.createStringType(),
        INTERPRETER.restrictType("isDefAndNotNull", TypeRestriction.TRUE, type));
  }

  @Test
  public void testIsString() {
    JSType type = TYPE_REGISTRY.createNumberType();
    assertJSTypeEqual(TYPE_REGISTRY.createStringType(),
        INTERPRETER.restrictType("isString", TypeRestriction.TRUE, type));

    type = TYPE_REGISTRY.createFunctionType(new FunctionType(new ObjectType(TYPE_REGISTRY.createNumberType()), VOID_TYPE));
    assertNull(INTERPRETER.restrictType("isString", TypeRestriction.TRUE, type));
  }

  @Test
  public void testIsBoolean() {
    JSType type = TYPE_REGISTRY.createNumberType();
    assertJSTypeEqual(TYPE_REGISTRY.createBooleanType(),
        INTERPRETER.restrictType("isBoolean", TypeRestriction.TRUE, type));

    type = TYPE_REGISTRY.createFunctionType(new FunctionType(new ObjectType(TYPE_REGISTRY.createNumberType()), VOID_TYPE));
    assertNull(INTERPRETER.restrictType("isBoolean", TypeRestriction.TRUE, type));
  }

  @Test
  public void testIsNumber() {
    JSType type = TYPE_REGISTRY.createBooleanType();
    assertJSTypeEqual(TYPE_REGISTRY.createNumberType(),
        INTERPRETER.restrictType("isNumber", TypeRestriction.TRUE, type));

    type = TYPE_REGISTRY.createFunctionType(new FunctionType(new ObjectType(TYPE_REGISTRY.createNumberType()), VOID_TYPE));
    assertNull(INTERPRETER.restrictType("isNumber", TypeRestriction.TRUE, type));
  }

  @Test
  public void testIsFunction() {
    JSType type = TYPE_REGISTRY.createStringType();
    assertNull(INTERPRETER.restrictType("isFunction", TypeRestriction.TRUE, type));

    type = TYPE_REGISTRY.createFunctionType(new FunctionType(new ObjectType(TYPE_REGISTRY.createNumberType()), VOID_TYPE));
    assertJSTypeEqual(type, INTERPRETER.restrictType("isFunction", TypeRestriction.TRUE, type));
  }

  @Test
  public void testIsArray() {
    JSType type = TYPE_REGISTRY.createBooleanType();
    assertJSTypeEqual(TYPE_REGISTRY.createArrayType(TYPE_REGISTRY.createBooleanType()),
        INTERPRETER.restrictType("isArray", TypeRestriction.TRUE, type));

    type = TYPE_REGISTRY.createStringType();
    assertNull(INTERPRETER.restrictType("isArray", TypeRestriction.TRUE, type));
  }

  @Test
  public void testIsObject() {
    JSType type = TYPE_REGISTRY.createNumberType();
    assertNull(INTERPRETER.restrictType("isObject", TypeRestriction.TRUE, type));

    type = TYPE_REGISTRY.createObjectType();
    assertJSTypeEqual(type,
        INTERPRETER.restrictType("isObject", TypeRestriction.TRUE, type));
  }

  private static class TypeRestriction {
    private final JSType type;
    private final boolean outcome;

    public TypeRestriction(JSType type, boolean outcome) {
      this.type = type;
      this.outcome = outcome;
    }

    static class TRUE extends TypeRestriction {
      public TRUE(JSType type) {
        super(type, true);
      }
    }

    static class FALSE extends TypeRestriction {
      public FALSE(JSType type) {
        super(type, false);
      }
    }
  }

  private static void assertJSTypeEqual(JSType expected, JSType actual) {
    if (!expected.equals(actual)) {
      throw new AssertionError("Expected: " + expected + ", but got: " + actual);
    }
  }
}