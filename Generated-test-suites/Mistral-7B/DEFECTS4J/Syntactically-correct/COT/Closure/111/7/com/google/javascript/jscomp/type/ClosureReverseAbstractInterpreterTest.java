package com.google.javascript.jscomp.type.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import com.google.common.base.Function;
import com.google.common.collect.ImmutableMap;
import com.google.javascript.jscomp.CodingConvention;
import com.google.javascript.jscomp.JSTypeRegistry;
import com.google.javascript.jscomp.type.JSType;
import com.google.javascript.jscomp.type.ObjectType;
import com.google.javascript.jscomp.type.Type;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.jstype.FunctionType;
import com.google.javascript.rhino.jstype.JSTypeNative;
import com.google.javascript.rhino.jstype.ObjectTypeVisitor;
import com.google.javascript.rhino.jstype.Visitor;

import java.util.Map;

import org.junit.Before;
import org.junit.Test;

public class ClosureReverseAbstractInterpreterTest {

  private JSTypeRegistry typeRegistry;
  private CodingConvention codingConvention;
  private ClosureReverseAbstractInterpreter interpreter;

  @Before
  public void setUp() {
    typeRegistry = new JSTypeRegistry();
    codingConvention = new CodingConvention();
    interpreter = new ClosureReverseAbstractInterpreter(codingConvention, typeRegistry);
  }

  @Test
  public void testIsDef() {
    JSType type = interpreter.getType(Node.newIntLiteral(42));
    assertEquals(typeRegistry.createUnionType(typeRegistry.createUndefinedType(),
        typeRegistry.createNumberType()), interpreter.isDef(new TypeRestriction(type, true)).getType());

    type = interpreter.getType(Node.newNull());
    assertNull(interpreter.isDef(new TypeRestriction(type, true)).getType());

    type = interpreter.getType(Node.newBooleanLiteral(true));
    assertNull(interpreter.isDef(new TypeRestriction(type, true)).getType());
  }

  @Test
  public void testIsDefAndNotNull() {
    JSType type = interpreter.getType(Node.newIntLiteral(42));
    assertEquals(typeRegistry.createNumberType(), interpreter.isDefAndNotNull(new TypeRestriction(type, true)).getType());

    type = interpreter.getType(Node.newNull());
    assertNull(interpreter.isDefAndNotNull(new TypeRestriction(type, true)).getType());

    type = interpreter.getType(Node.newBooleanLiteral(false));
    assertEquals(typeRegistry.createVoidType(), interpreter.isDefAndNotNull(new TypeRestriction(type, true)).getType());
  }

  @Test
  public void testIsString() {
    JSType type = interpreter.getType(Node.newStringLiteral("hello"));
    assertEquals(typeRegistry.createStringType(), interpreter.isString(new TypeRestriction(type, true)).getType());

    type = interpreter.getType(Node.newIntLiteral(42));
    assertNull(interpreter.isString(new TypeRestriction(type, true)).getType());
  }

  @Test
  public void testIsBoolean() {
    JSType type = interpreter.getType(Node.newBooleanLiteral(true));
    assertEquals(typeRegistry.createBooleanType(), interpreter.isBoolean(new TypeRestriction(type, true)).getType());

    type = interpreter.getType(Node.newIntLiteral(42));
    assertNull(interpreter.isBoolean(new TypeRestriction(type, true)).getType());
  }

  @Test
  public void testIsNumber() {
    JSType type = interpreter.getType(Node.newIntLiteral(42));
    assertEquals(typeRegistry.createNumberType(), interpreter.isNumber(new TypeRestriction(type, true)).getType());

    type = interpreter.getType(Node.newStringLiteral("42"));
    assertNull(interpreter.isNumber(new TypeRestriction(type, true)).getType());
  }

  @Test
  public void testIsFunction() {
    Node node = Node.newName("function");
    FunctionType functionType = new FunctionType(typeRegistry.createContextType(),
        new FunctionType(new ObjectType(typeRegistry.createContextType()),
            new ObjectType(typeRegistry.createContextType())), null);
    JSType type = functionType.getRawType();

    assertEquals(typeRegistry.createFunctionType(new ObjectType(typeRegistry.createContextType()),
        new ObjectType(typeRegistry.createContextType())), interpreter.isFunction(new TypeRestriction(type, true)).getType());

    type = interpreter.getType(Node.newNull());
    assertNull(interpreter.isFunction(new TypeRestriction(type, true)).getType());
  }

  @Test
  public void testIsArray() {
    JSType type = interpreter.getType(Node.newArrayLiteral());
    assertEquals(typeRegistry.createArrayType(new JSType[] { typeRegistry.createNumberType() }),
        interpreter.isArray(new TypeRestriction(type, true)).getType());

    type = interpreter.getType(Node.newNull());
    assertNull(interpreter.isArray(new TypeRestriction(type, true)).getType());
  }

  @Test
  public void testIsObject() {
    JSType type = interpreter.getType(Node.newObjectLiteral(null));
    assertEquals(typeRegistry.createObjectType(), interpreter.isObject(new TypeRestriction(type, true)).getType());

    type = interpreter.getType(Node.newNull());
    assertNull(interpreter.isObject(new TypeRestriction(type, true)).getType());
  }

  @Test(expected = AssertionError.class)
  public void testInvalidFunctionCall() {
    JSType type = interpreter.getType(Node.newName("goog.isDefAndNotNull"));
    interpreter.isDefAndNotNull(new TypeRestriction(type, true));
  }
}

class TypeRestriction {
  private final JSType type;
  private final boolean outcome;

  public TypeRestriction(JSType type, boolean outcome) {
    this.type = type;
    this.outcome = outcome;
  }
}