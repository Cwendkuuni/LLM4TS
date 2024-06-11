package com.google.gson.tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.lang.reflect.Field;
import java.lang.reflect.Type;

import org.junit.Test;

import com.google.gson.TypeInfo;
import com.google.gson.TypeInfoArray;
import com.google.gson.TypeInfoFactory;
import com.google.gson.TypeUtils;

public class TypeInfoFactoryTest {

  @Test
  public void testTypeInfoForArray() {
    Type type = int[].class;
    TypeInfoArray info = TypeInfoFactory.getTypeInfoForArray(type);
    assertNotNull(info);
    assertEquals(TypeInfoArray.class, info.getClass());
    assertEquals(int[].class, info.getType());
  }

  @Test
  public void testTypeInfoForField_TypeVariable() {
    try {
      Class<?> clazz = new Foo<Integer, String>().getClass();
      Field field = clazz.getField("intField");
      Type type = field.getGenericType();
      TypeInfo info = TypeInfoFactory.getTypeInfoForField(field, clazz.getGenericSuperclass());
      assertEquals(TypeInfo.class, info.getClass());
      assertEquals(Integer.TYPE, info.getType());
    } catch (NoSuchFieldException | IllegalAccessException e) {
      throw new RuntimeException(e);
    }
  }

  @Test
  public void testTypeInfoForField_ParameterizedType() {
    try {
      Class<?> clazz = new Foo<Integer, String>().getClass();
      Field field = clazz.getField("stringField");
      Type type = field.getGenericType();
      TypeInfo info = TypeInfoFactory.getTypeInfoForField(field, clazz.getGenericSuperclass());
      assertEquals(TypeInfo.class, info.getClass());
      assertEquals(String.class, info.getType());
    } catch (NoSuchFieldException | IllegalAccessException e) {
      throw new RuntimeException(e);
    }
  }

  @Test
  public void testTypeInfoForField_RawType() {
    try {
      Class<?> clazz = new Foo<Integer, String>().getClass();
      Field field = clazz.getField("foo");
      Type type = field.getGenericType();
      TypeInfo info = TypeInfoFactory.getTypeInfoForField(field, clazz.getGenericSuperclass());
      assertEquals(TypeInfo.class, info.getClass());
      assertEquals(Foo.class, info.getType());
    } catch (NoSuchFieldException | IllegalAccessException e) {
      throw new RuntimeException(e);
    }
  }

  @Test(expected = UnsupportedOperationException.class)
  public void testTypeInfoForField_InvalidType() {
    try {
      Class<?> clazz = new Foo<Integer, String>().getClass();
      Field field = clazz.getField("notAField");
      Type type = field.getGenericType();
      TypeInfoFactory.getTypeInfoForField(field, clazz.getGenericSuperclass());
    } catch (NoSuchFieldException | IllegalAccessException e) {
      throw new RuntimeException(e);
    }
  }

  @Test
  public void testGetActualType_RawType() {
    Class<?> rawParentClass = Object.class;
    Type parentType = Object.class;
    Type rawType = String.class;
    Type actualType = TypeInfoFactory.getActualType(rawType, parentType, rawParentClass);
    assertEquals(String.class, actualType);
  }

  @Test
  public void testGetActualType_ParameterizedType() {
    Class<?> rawParentClass = List.class;
    Type parentType = new TypeToken<List<String>>() {}.getType();
    Type rawType = new TypeToken<List<Integer>>() {}.getType();
    Type actualType = TypeInfoFactory.getActualType(rawType, parentType, rawParentClass);
    assertEquals(List.class, actualType);
  }

  @Test
  public void testGetActualType_GenericArrayType() {
    Class<?> rawParentClass = Object.class;
    Type parentType = Object[].class;
    Type componentType = Integer.TYPE;
    Type rawType = new GenericArrayTypeImpl(componentType);
    Type actualType = TypeInfoFactory.getActualType(rawType, parentType, rawParentClass);
    assertEquals(Integer[].class, actualType);
  }

  @Test
  public void testGetActualType_TypeVariable() {
    Class<?> rawParentClass = List.class;
    Type parentType = new TypeToken<List<String>>() {}.getType();
    TypeVariable<?> fieldTypeVariable = TypeVariables.A.getType();
    Type actualType = TypeInfoFactory.getActualType(fieldTypeVariable, parentType, rawParentClass);
    assertEquals(String.class, actualType);
  }

  @Test
  public void testGetActualType_WildcardType() {
    Class<?> rawParentClass = List.class;
    Type parentType = new TypeToken<List<?>>() {}.getType();
    WildcardType castedType = (WildcardType) parentType;
    Type upperBound = Object.class;
    Type actualType = TypeInfoFactory.getActualType(castedType.getUpperBounds()[0], parentType, rawParentClass);
    assertEquals(Object.class, actualType);
  }

  static class Foo<A, B> {
    A intField;
    B stringField;
    Foo<Integer, String> foo;
  }

  static class Foo$1 {}

  static class TypeVariables<A> {
    public static final TypeVariable<Class<A>> A = new TypeVariable<Class<A>>() {};
  }

  static class TypeInfoArrayImpl extends TypeInfoArray {
  }

  static class TypeInfo extends TypeInfo {
  }

  static class ParameterizedTypeImpl extends ParameterizedType {
  }

  static class GenericArrayTypeImpl extends GenericArrayType {
  }

}