package org.apache.commons.cli;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import com.google.common.collect.ImmutableMap;
import com.google.javascript.jscomp.CodingConvention;
import com.google.javascript.jscomp.type.JSType;
import com.google.javascript.jscomp.type.JSTypeRegistry;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.jstype.ObjectType;
import com.google.javascript.rhino.jstype.Visitor;

import java.util.Map;

@RunWith(JUnit4.class)
public class ClosureReverseAbstractInterpreterTest {

  private static final CodingConvention CONVENTION = new CodingConvention();
  private static final JSTypeRegistry TYPE_REGISTRY = new JSTypeRegistry();
  private static final ClosureReverseAbstractInterpreter INTERPRETER =
      new ClosureReverseAbstractInterpreter(CONVENTION, TYPE_REGISTRY);

  @Test
  public void testIsDef() {
    JSType originalType = TYPE_REGISTRY.createObjectType("{a: number}");
    TypeRestriction typeRestriction = new TypeRestriction(originalType, true);
    JSType expectedType = INTERPRETER.getRestrictedWithoutUndefined(originalType);

    JSType actualType = INTERPRETER.restricters.get("isDef").apply(typeRestriction);
    assertNotNull(actualType);
    assertEquals(expectedType, actualType);

    typeRestriction = new TypeRestriction(originalType, false);
    expectedType = originalType.isSubtype(INTERPRETER.getNativeType(VOID_TYPE)) ?
        INTERPRETER.getNativeType(VOID_TYPE) : null;

    actualType = INTERPRETER.restricters.get("isDef").apply(typeRestriction);
    assertNull(actualType);
  }

  @Test
  public void testIsNull() {
    JSType originalType = TYPE_REGISTRY.createObjectType("{a: number}");
    TypeRestriction typeRestriction = new TypeRestriction(originalType, true);
    JSType expectedType = originalType.isSubtype(INTERPRETER.getNativeType(NULL_TYPE)) ?
        INTERPRETER.getNativeType(NULL_TYPE) : null;

    JSType actualType = INTERPRETER.restricters.get("isNull").apply(typeRestriction);
    assertNull(actualType);

    typeRestriction = new TypeRestriction(null, false);
    expectedType = INTERPRETER.getRestrictedWithoutNull(originalType);

    actualType = INTERPRETER.restricters.get("isNull").apply(typeRestriction);
    assertNotNull(actualType);
    assertEquals(expectedType, actualType);
  }

  @Test
  public void testIsDefAndNotNull() {
    JSType originalType = TYPE_REGISTRY.createObjectType("{a: number}");
    TypeRestriction typeRestriction = new TypeRestriction(originalType, true);
    JSType expectedType = INTERPRETER.getRestrictedWithoutUndefined(
        INTERPRETER.getRestrictedWithoutNull(originalType));

    JSType actualType = INTERPRETER.restricters.get("isDefAndNotNull").apply(typeRestriction);
    assertNull(actualType);

    typeRestriction = new TypeRestriction(null, false);
    expectedType = INTERPRETER.getNativeType(NULL_VOID);

    actualType = INTERPRETER.restricters.get("isDefAndNotNull").apply(typeRestriction);
    assertNotNull(actualType);
    assertEquals(expectedType, actualType);
  }

  @Test
  public void testIsString() {
    JSType originalType = TYPE_REGISTRY.createObjectType("{a: number}");
    TypeRestriction typeRestriction = new TypeRestriction(originalType, true);
    String expectedString = "string";

    JSType actualType = INTERPRETER.restricters.get("isString").apply(typeRestriction);
    assertNotNull(actualType);
    assertEquals(INTERPRETER.getNativeType(NUMBER_STRING_BOOLEAN), actualType);

    Node parameter = new Node();
    parameter.setType(originalType);
    Node stringParam = new Node();
    stringParam.setType(INTERPRETER.getNativeType(JSType.STRING));
    parameter = new Node("setProperty", ImmutableMap.of(), new Node[] { parameter, stringParam });

    JSType actualType2 = INTERPRETER.restricters.get("isString").apply(
        new TypeRestriction(parameter.getType(), true));

    assertEquals(INTERPRETER.getNativeType(JSType.STRING), actualType2);
  }

  @Test
  public void testIsBoolean() {
    JSType originalType = TYPE_REGISTRY.createObjectType("{a: number}");
    TypeRestriction typeRestriction = new TypeRestriction(originalType, true);

    JSType actualType = INTERPRETER.restricters.get("isBoolean").apply(typeRestriction);
    assertNotNull(actualType);
    assertEquals(INTERPRETER.getNativeType(JSType.BOOLEAN), actualType);
  }

  @Test
  public void testIsNumber() {
    JSType originalType = TYPE_REGISTRY.createObjectType("{a: number}");
    TypeRestriction typeRestriction = new TypeRestriction(originalType, true);

    JSType actualType = INTERPRETER.restricters.get("isNumber").apply(typeRestriction);
    assertNotNull(actualType);
    assertEquals(INTERPRETER.getNativeType(JSType.NUMBER), actualType);
  }

  @Test
  public void testIsFunction() {
    JSType originalType = TYPE_REGISTRY.createObjectType("{a: number}");
    TypeRestriction typeRestriction = new TypeRestriction(originalType, true);

    JSType actualType = INTERPRETER.restricters.get("isFunction").apply(typeRestriction);
    assertNotNull(actualType);
    assertEquals(INTERPRETER.getNativeType(JSType.FUNCTION), actualType);
  }

  @Test
  public void testArray() {
    JSType originalType = TYPE_REGISTRY.createObjectType("{a: number}");
    TypeRestriction typeRestriction = new TypeRestriction(originalType, true);

    JSType expectedType = INTERPRETER.getNativeType(JSType.ARRAY);

    JSType actualType = INTERPRETER.restricters.get("isArray").apply(typeRestriction);
    assertNotNull(actualType);
    assertEquals(expectedType, actualType);

    typeRestriction = new TypeRestriction(null, false);
    actualType = INTERPRETER.restricters.get("isArray").apply(typeRestriction);
    assertNull(actualType);
  }

  @Test
  public void testObject() {
    JSType originalType = TYPE_REGISTRY.createObjectType("{a: number}");
    TypeRestriction typeRestriction = new TypeRestriction(originalType, true);

    JSType expectedType = INTERPRETER.getNativeType(JSType.OBJECT);

    JSType actualType = INTERPRETER.restricters.get("isObject").apply(typeRestriction);
    assertNotNull(actualType);
    assertEquals(expectedType, actualType);

    typeRestriction = new TypeRestriction(null, false);
    actualType = INTERPRETER.restricters.get("isObject").apply(typeRestriction);
    assertNull(actualType);
  }
}