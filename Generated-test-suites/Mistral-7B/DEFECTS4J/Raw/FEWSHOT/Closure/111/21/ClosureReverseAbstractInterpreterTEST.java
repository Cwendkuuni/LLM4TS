package org.apache.commons.cli;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.javascript.jscomp.CodingConvention;
import com.google.javascript.jscomp.type.JSType;
import com.google.javascript.jscomp.type.JSTypeRegistry;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.jstype.FunctionType;
import com.google.javascript.rhino.jstype.ObjectType;
import com.google.javascript.rhino.jstype.Visitor;

import org.junit.Before;
import org.junit.Test;

public class ClosureReverseAbstractInterpreterTest {

  private CodingConvention convention;
  private JSTypeRegistry typeRegistry;
  private ClosureReverseAbstractInterpreter interpreter;

  @Before
  public void setUp() {
    convention = mock(CodingConvention.class);
    typeRegistry = mock(JSTypeRegistry.class);
    interpreter = new ClosureReverseAbstractInterpreter(convention, typeRegistry);
  }

  @Test
  public void testIsDef() {
    JSType inputType = new ObjectType("inputType");
    JSType expectedType = new ObjectType("outputType");

    TypeRestriction typeRestriction = new TypeRestriction(inputType, true);
    JSType resultType = interpreter.restrictParameter(
        Node.newName("goog.isDef"), typeRestriction.type, null,
        interpreter.restricters.get("isDef"), typeRestriction.outcome);

    assertNotNull(resultType);
    assertEquals(expectedType, resultType);

    typeRestriction = new TypeRestriction(inputType, false);
    resultType = interpreter.restrictParameter(
        Node.newName("goog.isDef"), typeRestriction.type, null,
        interpreter.restricters.get("isDef"), typeRestriction.outcome);

    assertNull(resultType);
  }

  @Test
  public void testIsNotNull() {
    JSType inputType = new ObjectType("inputType");

    TypeRestriction typeRestriction1 = new TypeRestriction(inputType, true);
    JSType resultType1 = interpreter.restrictParameter(
        Node.newName("goog.isNull"), typeRestriction1.type, null,
        interpreter.restricters.get("isNull"), typeRestriction1.outcome);

    assertNull(resultType1);

    TypeRestriction typeRestriction2 = new TypeRestriction(null, false);
    JSType resultType2 = interpreter.restrictParameter(
        Node.newName("goog.isNull"), typeRestriction2.type, null,
        interpreter.restricters.get("isNull"), typeRestriction2.outcome);

    assertNull(resultType2);

    when(typeRegistry.createUnionType(NODE_NUMBER_STRING_BOOLEAN, NO_VOID))
        .thenReturn(new ObjectType("expectedType"));

    JSType resultType3 = interpreter.restrictParameter(
        Node.newName("goog.isNull"), typeRestriction2.type, null,
        interpreter.restricters.get("isNull"), typeRestriction2.outcome);

    assertEquals(new ObjectType("expectedType"), resultType3);
  }

  @Test
  public void testIsDefAndNotNull() {
    JSType inputType = new ObjectType("inputType");

    TypeRestriction typeRestriction1 = new TypeRestriction(inputType, true);
    JSType resultType1 = interpreter.restrictParameter(
        Node.newName("goog.isDefAndNotNull"), typeRestriction1.type, null,
        interpreter.restricters.get("isDefAndNotNull"), typeRestriction1.outcome);

    assertNull(resultType1);

    TypeRestriction typeRestriction2 = new TypeRestriction(null, false);
    JSType resultType2 = interpreter.restrictParameter(
        Node.newName("goog.isDefAndNotNull"), typeRestriction2.type, null,
        interpreter.restricters.get("isDefAndNotNull"), typeRestriction2.outcome);

    when(typeRegistry.createUnionType(NULL_VOID, NO_OBJECT_TYPE))
        .thenReturn(new ObjectType("expectedType"));

    JSType resultType3 = interpreter.restrictParameter(
        Node.newName("goog.isDefAndNotNull"), typeRestriction2.type, null,
        interpreter.restricters.get("isDefAndNotNull"), typeRestriction2.outcome);

    assertEquals(new ObjectType("expectedType"), resultType3);
  }

  @Test
  public void testIsString() {
    JSType inputType = new ObjectType("inputType");

    TypeRestriction typeRestriction1 = new TypeRestriction(inputType, true);
    JSType resultType1 = interpreter.restrictParameter(
        Node.newName("goog.isString"), typeRestriction1.type, null,
        interpreter.restricters.get("isString"), typeRestriction1.outcome);

    assertEquals(new ObjectType("expectedStringType"), resultType1);

    TypeRestriction typeRestriction2 = new TypeRestriction(null, false);
    JSType resultType2 = interpreter.restrictParameter(
        Node.newName("goog.isString"), typeRestriction2.type, null,
        interpreter.restricters.get("isString"), typeRestriction2.outcome);

    assertNull(resultType2);
  }

  @Test
  public void testIsBoolean() {
    JSType inputType = new ObjectType("inputType");

    TypeRestriction typeRestriction1 = new TypeRestriction(inputType, true);
    JSType resultType1 = interpreter.restrictParameter(
        Node.newName("goog.isBoolean"), typeRestriction1.type, null,
        interpreter.restricters.get("isBoolean"), typeRestriction1.outcome);

    assertEquals(new ObjectType("expectedBooleanType"), resultType1);

    TypeRestriction typeRestriction2 = new TypeRestriction(null, false);
    JSType resultType2 = interpreter.restrictParameter(
        Node.newName("goog.isBoolean"), typeRestriction2.type, null,
        interpreter.restricters.get("isBoolean"), typeRestriction2.outcome);

    assertNull(resultType2);
  }

  @Test
  public void testIsNumber() {
    JSType inputType = new ObjectType("inputType");

    TypeRestriction typeRestriction1 = new TypeRestriction(inputType, true);
    JSType resultType1 = interpreter.restrictParameter(
        Node.newName("goog.isNumber"), typeRestriction1.type, null,
        interpreter.restricters.get("isNumber"), typeRestriction1.outcome);

    assertEquals(new ObjectType("expectedNumberType"), resultType1);

    TypeRestriction typeRestriction2 = new TypeRestriction(null, false);
    JSType resultType2 = interpreter.restrictParameter(
        Node.newName("goog.isNumber"), typeRestriction2.type, null,
        interpreter.restricters.get("isNumber"), typeRestriction2.outcome);

    assertNull(resultType2);
  }

  @Test
  public void testIsFunction() {
    FunctionType inputType = new FunctionType(new ObjectType("input"), new ObjectType("output"));

    TypeRestriction typeRestriction1 = new TypeRestriction(inputType, true);
    JSType resultType1 = interpreter.restrictParameter(
        Node.newName("goog.isFunction"), typeRestriction1.type, null,
        interpreter.restricters.get("isFunction"), typeRestriction1.outcome);

    assertEquals(inputType, resultType1);

    TypeRestriction typeRestriction2 = new TypeRestriction(null, false);
    JSType resultType2 = interpreter.restrictParameter(
        Node.newName("goog.isFunction"), typeRestriction2.type, null,
        interpreter.restricters.get("isFunction"), typeRestriction2.outcome);

    assertNull(resultType2);
  }

  @Test
  public void testIsArray() {
    JSType inputType = new ObjectType("inputType");

    TypeRestriction typeRestriction1 = new TypeRestriction(inputType, true);
    JSType resultType1 = interpreter.restrictParameter(
        Node.newName("goog.isArray"), typeRestriction1.type, null,
        interpreter.restricters.get("isArray"), typeRestriction1.outcome);

    assertEquals(ARRAY_TYPE, resultType1);

    TypeRestriction typeRestriction2 = new TypeRestriction(null, false);
    JSType resultType2 = interpreter.restrictParameter(
        Node.newName("goog.isArray"), typeRestriction2.type, null,
        interpreter.restricters.get("isArray"), typeRestriction2.outcome);

    assertNull(resultType2);
  }

  @Test
  public void testIsObject() {
    ObjectType inputType = new ObjectType("inputType");

    TypeRestriction typeRestriction1 = new TypeRestriction(inputType, true);
    JSType resultType1 = interpreter.restrictParameter(
        Node.newName("goog.isObject"), typeRestriction1.type, null,
        interpreter.restricters.get("isObject"), typeRestriction1.outcome);

    assertEquals(inputType, resultType1);

    TypeRestriction typeRestriction2 = new TypeRestriction(null, false);
    JSType resultType2 = interpreter.restrictParameter(
        Node.newName("goog.isObject"), typeRestriction2.type, null,
        interpreter.restricters.get("isObject"), typeRestriction2.outcome);

    assertNull(resultType2);
  }

  private JSType getRestrictedWithoutUndefined(JSType type) {
    if (type.isUnionType()) {
      JSType unionType = type;
      JSType[] types = unionType.getMemberTypes();
      JSType[] newTypes = new JSType[types.length];
      int index = 0;
      for (JSType memberType : types) {
        if (!memberType.isUndefined()) {
          newTypes[index++] = memberType;
        }
      }
      return new ObjectType(unionType.getNames()[0], newTypes);
    }
    return type;
  }

  private JSType getRestrictedWithoutNull(JSType type) {
    if (type.isNullOrUndefined()) {
      return type.getGreatestSubtype(null);
    }
    return type;
  }

  private JSType getRestrictedByTypeOfResult(JSType type, String expectedType,
      boolean outcome) {
    if (outcome && type.getClass().equals(expectedType)) {
      return type;
    }
    return null;
  }

  private static final Node ARRAY_TYPE = new ObjectType("Array");
  private static final Node NO_OBJECT_TYPE = new ObjectType("Object");
  private static final Node NULL_VOID = new ObjectType("NullOrVoid");
  private static final Node NODE_NUMBER_STRING_BOOLEAN = new ObjectType("Number|String|Boolean");
}