package com.google.gson;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertInstanceOf;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;

import org.junit.Test;

public class TypeInfoFactoryTest {

  @Test
  public void testTypeInfoForArray() {
    Class<int[]> intArrayType = int[].class;
    TypeInfo expectedTypeInfo = new TypeInfoArray(intArrayType);
    TypeInfo actualTypeInfo = TypeInfoFactory.getTypeInfoForArray(intArrayType);
    assertEquals(expectedTypeInfo.getType(), actualTypeInfo.getType());
    assertEquals(expectedTypeInfo.getComponentType(), actualTypeInfo.getComponentType());
  }

  @Test
  public void testTypeInfoForField_ClassType() {
    Field field = MyClass.class.getDeclaredField("intField");
    int expectedType = 0; // int type
    TypeInfo expectedTypeInfo = new TypeInfo(Type.INT_TYPE);
    Type parentType = Object.class;
    Class<?> rawParentClass = Object.class;
    TypeInfo actualTypeInfo = TypeInfoFactory.getTypeInfoForField(field, parentType);
    assertEquals(expectedTypeInfo.getType(), actualTypeInfo.getType());
  }

  @Test
  public void testTypeInfoForField_ParameterizedType() {
    class MyClass<A> {
      A field;
    }

    Field field = MyClass.class.getDeclaredField("field");
    Type[] expectedTypeArguments = new Type[] { Integer.TYPE };
    Type parentType = MyClass.class;
    Class<?> rawParentClass = MyClass.class;

    TypeInfo actualTypeInfo = TypeInfoFactory.getTypeInfoForField(field, parentType);
    Type actualType = actualTypeInfo.getType();
    assertEquals(MyClass.class, actualType.getDeclaringClass());
    assertInstanceOf(ParameterizedType.class, actualType);
    ParameterizedType actualParameterizedType = (ParameterizedType) actualType;
    Type[] actualTypeArguments = actualParameterizedType.getActualTypeArguments();
    assertEquals(expectedTypeArguments.length, actualTypeArguments.length);
    for (int i = 0; i < expectedTypeArguments.length; i++) {
      assertEquals(expectedTypeArguments[i], actualTypeArguments[i]);
    }
  }

  @Test
  public void testTypeInfoForField_GenericArrayType() {
    class MyClass<A> {
      A[] arrayField;
    }

    Field field = MyClass.class.getDeclaredField("arrayField");
    Type expectedComponentType = Integer.TYPE;
    Type parentType = MyClass.class;
    Class<?> rawParentClass = MyClass.class;

    TypeInfo actualTypeInfo = TypeInfoFactory.getTypeInfoForField(field, parentType);
    assertEquals(GenericArrayType.class, actualTypeInfo.getClass());
    GenericArrayType actualGenericArrayType = (GenericArrayType) actualTypeInfo.getType();
    Type actualComponentType = actualGenericArrayType.getGenericComponentType();
    assertEquals(expectedComponentType, actualComponentType);
  }

  @Test
  public void testTypeInfoForField_TypeVariable() {
    class MyClass<A> {
      A field;
    }

    Field field = MyClass.class.getDeclaredField("field");
    TypeVariable<A> fieldTypeVariable = MyClass.class.getTypeParameters()[0];
    Type parentType = MyClass.class;
    Class<?> rawParentClass = MyClass.class;

    TypeInfo actualTypeInfo = TypeInfoFactory.getTypeInfoForField(field, parentType);
    assertEquals(TypeVariable.class, actualTypeInfo.getClass());
    TypeVariable<?> actualTypeVariable = (TypeVariable<?>) actualTypeInfo.getType();
    assertEquals(fieldTypeVariable, actualTypeVariable);
  }

  @Test(expected = UnsupportedOperationException.class)
  public void testTypeInfoForField_UnexpectedType() {
    Field field = MyClass.class.getDeclaredField("intField");
    Type parentType = Object.class;
    Class<?> rawParentClass = Object.class;

    TypeInfoFactory.getTypeInfoForField(field, parentType);
  }

  private static class MyClass<A> {
    A field;
  }

  static class Preconditions {
    public static void checkArgument(boolean condition) {
      // Do nothing
    }
  }

  static class TypeUtils {
    public static boolean isArray(Type type) {
      // Do nothing
      return false;
    }

    public static Class<?> toRawClass(Type type) {
      // Do nothing
      return null;
    }

    public static Type wrapWithArray(Class<?> componentType) {
      // Do nothing
      return null;
    }
  }
}