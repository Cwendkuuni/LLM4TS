package com.google.javascript.rhino.jstype;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import com.google.javascript.rhino.ErrorReporter;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.jstype.JSType;
import com.google.javascript.rhino.jstype.JSTypeRegistry;
import com.google.javascript.rhino.jstype.JSTypeNative;

import org.junit.Test;

public class ArrowTypeTest {

  private static JSTypeRegistry registry;
  static {
    registry = new JSTypeRegistry();
  }

  @Test
  public void testArrowTypeCreation() {
    JSType returnType = JSTypeNative.getBoolean();
    ArrowType arrowType = new ArrowType(registry, null, returnType, false);
    assertNotNull(arrowType);
    assertEquals(returnType, arrowType.getReturnType());
    assertTrue(arrowType.isArrowType());
  }

  @Test
  public void testArrowTypeCreationWithParameters() {
    JSType paramType1 = registry.createStringType();
    JSType paramType2 = registry.createNumberType();
    JSType returnType = JSTypeNative.getBoolean();
    ArrowType arrowType = new ArrowType(registry,
        registry.createListType(paramType1, paramType2), returnType, false);
    assertNotNull(arrowType);
    assertEquals(returnType, arrowType.getReturnType());
    assertTrue(arrowType.isArrowType());
    assertNotNull(arrowType.getParameters());
    assertEquals(2, arrowType.getParameterCount());
    assertEquals(paramType1, arrowType.getParameterType(0));
    assertEquals(paramType2, arrowType.getParameterType(1));
  }

  @Test
  public void testArrowTypeCreationWithInferredReturnType() {
    JSType paramType1 = registry.createStringType();
    JSType paramType2 = registry.createNumberType();
    ArrowType arrowType = new ArrowType(registry,
        registry.createListType(paramType1, paramType2), null, true);
    assertNotNull(arrowType);
    assertNull(arrowType.getReturnType());
    assertTrue(arrowType.isArrowType());
    assertNotNull(arrowType.getParameters());
    assertEquals(2, arrowType.getParameterCount());
    assertEquals(paramType1, arrowType.getParameterType(0));
    assertEquals(paramType2, arrowType.getParameterType(1));
  }

  @Test
  public void testArrowTypeEquals() {
    JSType paramType1 = registry.createStringType();
    JSType paramType2 = registry.createNumberType();
    JSType returnType1 = JSTypeNative.getBoolean();
    JSType returnType2 = JSTypeNative.getInt();

    ArrowType arrowType1 = new ArrowType(registry,
        registry.createListType(paramType1, paramType2), returnType1, false);
    ArrowType arrowType2 = new ArrowType(registry,
        registry.createListType(paramType1, paramType2), returnType2, false);

    ArrowType arrowType3 = new ArrowType(registry,
        registry.createListType(paramType1, paramType2), returnType1, false);

    ArrowType arrowType4 = new ArrowType(registry,
        registry.createListType(paramType1, paramType2), null, false);

    ArrowType arrowType5 = new ArrowType(registry, null, null, false);

    ArrowType arrowType6 = new ArrowType(registry, null, returnType1, false);

    ArrowType arrowType7 = new ArrowType(registry,
        registry.createListType(paramType2, paramType1), returnType1, false);

    ArrowType arrowType8 = new ArrowType(registry,
        registry.createListType(paramType1, paramType2, paramType1), returnType1, false);

    assertTrue(arrowType1.equals(arrowType3));
    assertFalse(arrowType1.equals(arrowType2));
    assertFalse(arrowType1.equals(arrowType4));
    assertFalse(arrowType1.equals(arrowType5));
    assertFalse(arrowType1.equals(arrowType6));
    assertFalse(arrowType1.equals(arrowType7));
    assertFalse(arrowType1.equals(arrowType8));
  }

  @Test
  public void testArrowTypeHashCode() {
    JSType paramType1 = registry.createStringType();
    JSType paramType2 = registry.createNumberType();
    JSType returnType1 = JSTypeNative.getBoolean();
    JSType returnType2 = JSTypeNative.getInt();

    ArrowType arrowType1 = new ArrowType(registry,
        registry.createListType(paramType1, paramType2), returnType1, false);
    ArrowType arrowType2 = new ArrowType(registry,
        registry.createListType(paramType1, paramType2), returnType2, false);

    int hashCode1 = arrowType1.hashCode();
    int hashCode2 = arrowType2.hashCode();

    ArrowType arrowType3 = new ArrowType(registry,
        registry.createListType(paramType1, paramType2), returnType1, false);

    ArrowType arrowType4 = new ArrowType(registry,
        registry.createListType(paramType1, paramType2), null, false);

    ArrowType arrowType5 = new ArrowType(registry, null, null, false);

    ArrowType arrowType6 = new ArrowType(registry, null, returnType1, false);

    ArrowType arrowType7 = new ArrowType(registry,
        registry.createListType(paramType2, paramType1), returnType1, false);

    ArrowType arrowType8 = new ArrowType(registry,
        registry.createListType(paramType1, paramType2, paramType1), returnType1, false);

    assertEquals(hashCode1, arrowType3.hashCode());
    assertNotEquals(hashCode1, hashCode2);
  }

  @Test
  public void testArrowTypeSubtype() {
    JSType paramType1 = registry.createStringType();
    JSType paramType2 = registry.createNumberType();
    JSType returnType1 = JSTypeNative.getBoolean();
    JSType returnType2 = JSTypeNative.getInt();
    JSType returnType3 = JSTypeNative.getString();

    ArrowType arrowType1 = new ArrowType(registry,
        registry.createListType(paramType1, paramType2), returnType1, false);
    ArrowType arrowType2 = new ArrowType(registry,
        registry.createListType(paramType1, paramType2), returnType2, false);
    ArrowType arrowType3 = new ArrowType(registry,
        registry.createListType(paramType1, paramType2), returnType3, false);
    ArrowType arrowType4 = new ArrowType(registry, null, null, false);

    assertTrue(arrowType1.isSubtype(arrowType1));
    assertTrue(arrowType1.isSubtype(arrowType2));
    assertFalse(arrowType1.isSubtype(arrowType3));
    assertFalse(arrowType1.isSubtype(arrowType4));
  }

  @Test
  public void testArrowTypeHasEqualParameters() {
    JSType paramType1 = registry.createStringType();
    JSType paramType2 = registry.createNumberType();
    JSType paramType3 = registry.createBooleanType();
    ArrowType arrowType1 = new ArrowType(registry,
        registry.createListType(paramType1, paramType2), JSTypeNative.getBoolean(), false);
    ArrowType arrowType2 = new ArrowType(registry,
        registry.createListType(paramType1, paramType3), JSTypeNative.getBoolean(), false);
    ArrowType arrowType3 = new ArrowType(registry,
        registry.createListType(paramType1, paramType2), JSTypeNative.getBoolean(), false);

    assertTrue(arrowType1.hasEqualParameters(arrowType3));
    assertFalse(arrowType1.hasEqualParameters(arrowType2));
  }

  @Test
  public void testArrowTypeToString() {
    JSType paramType1 = registry.createStringType();
    JSType paramType2 = registry.createNumberType();
    JSType returnType = JSTypeNative.getBoolean();

    ArrowType arrowType = new ArrowType(registry,
        registry.createListType(paramType1, paramType2), returnType, false);

    String string = arrowType.toString();

    assertTrue(string.contains("ArrowType"));
    assertTrue(string.contains("["));
    assertTrue(string.contains(paramType1.toString()));
    assertTrue(string.contains(","));
    assertTrue(string.contains(paramType2.toString()));
    assertTrue(string.contains("] -> "));
    assertTrue(string.contains(returnType.toString()));
  }

  @Test
  public void testArrowTypeResolve() {
    JSType paramType1 = registry.createStringType();
    JSType paramType2 = registry.createNumberType();
    JSType returnType = JSTypeNative.getBoolean();
    ErrorReporter errorReporter = new ErrorReporter();

    ArrowType arrowType = new ArrowType(registry,
        registry.createListType(paramType1, paramType2), returnType, false);

    JSType resolvedArrowType = arrowType.resolve(errorReporter, null);

    assertTrue(resolvedArrowType instanceof ArrowType);
    assertEquals(returnType, resolvedArrowType.getReturnType());
    assertEquals(2, resolvedArrowType.getParameterCount());
    assertEquals(paramType1, resolvedArrowType.getParameterType(0));
    assertEquals(paramType2, resolvedArrowType.getParameterType(1));
  }

  @Test
  public void testArrowTypeWithUnknownParamsOrReturn() {
    JSType paramType1 = registry.createUnknownType();
    JSType paramType2 = registry.createNumberType();
    JSType returnType = JSTypeNative.getBoolean();

    ArrowType arrowType = new ArrowType(registry,
        registry.createListType(paramType1, paramType2), returnType, false);

    assertTrue(arrowType.hasUnknownParamsOrReturn());

    JSType resolvedArrowType = arrowType.resolve(null, null);

    assertFalse(resolvedArrowType.hasUnknownParamsOrReturn());
  }
}