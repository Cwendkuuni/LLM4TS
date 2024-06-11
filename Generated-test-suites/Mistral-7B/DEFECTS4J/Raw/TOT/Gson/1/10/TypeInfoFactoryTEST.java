package com.google.gson.tests;

import static org.junit.Assert.*;

import java.lang.reflect.Field;
import java.lang.reflect.Type;

import org.junit.Test;

import com.google.gson.exclusions.Preconditions;
import com.google.gson.reflect.TypeUtils;
import com.google.gson.reflect.TypeInfoFactory;
import com.google.gson.reflect.TypeVariable;
import com.google.gson.reflect.WildcardType;
import com.google.gson.reflect.WrappedType;

public class TypeInfoFactoryTest {

  @Test
  public void testTypeInfoForArray() {
    Type type = new int[3].getClass().getGenericComponentType();
    TypeInfoArray typeInfoArray = TypeInfoFactory.getTypeInfoForArray(type);
    assertEquals(new Class<?>[] { int[].class }, typeInfoArray.getRawTypes());
    assertEquals(int[].class, typeInfoArray.getComponentType());
  }

  @Test
  public void testTypeInfoForField_TypeVariable() throws Exception {
    Class<?> clazz = new Foo<Integer>().getClass();
    Field field = clazz.getDeclaredField("a");
    field.setAccessible(true);
    Type type = field.getGenericType();

    TypeInfo typeInfo = TypeInfoFactory.getTypeInfoForField(field, clazz.getGenericSuperclass());
    assertEquals(Integer.TYPE, typeInfo.getType());
  }

  @Test
  public void testTypeInfoForField_TypeVariable_2() throws Exception {
    Class<?> clazz = new Foo<List<Integer>>().getClass();
    Field field = clazz.getDeclaredField("a");
    field.setAccessible(true);
    Type type = field.getGenericType();

    TypeInfo typeInfo = TypeInfoFactory.getTypeInfoForField(field, clazz.getGenericSuperclass());
    Type listType = new TypeToken<List<Integer>>() {}.getType();
    assertEquals(listType, typeInfo.getType());
  }

  @Test(expected = UnsupportedOperationException.class)
  public void testTypeInfoForField_NoTypeVariable() throws Exception {
    Class<?> clazz = Foo.class;
    Field field = clazz.getDeclaredField("a");
    field.setAccessible(true);
    Type type = field.getGenericType();

    TypeInfo typeInfo = TypeInfoFactory.getTypeInfoForField(field, clazz.getGenericSuperclass());
  }

  @Test
  public void testTypeInfoForField_ParameterizedType() throws Exception {
    Class<?> clazz = new Foo<List<Integer>>().getClass();
    Field field = clazz.getDeclaredField("b");
    field.setAccessible(true);
    Type type = field.getGenericType();

    TypeInfo typeInfo = TypeInfoFactory.getTypeInfoForField(field, clazz.getGenericSuperclass());
    Type listType = new TypeToken<List<Integer>>() {}.getType();
    Type parameterizedType = new ParameterizedTypeImpl(List.class, new Type[] { Integer.TYPE }, null);
    assertEquals(parameterizedType, typeInfo.getType());
  }

  @Test
  public void testTypeInfoForField_GenericArrayType() throws Exception {
    Class<?> clazz = new Foo<List<Integer[]>>().getClass();
    Field field = clazz.getDeclaredField("c");
    field.setAccessible(true);
    Type type = field.getGenericType();

    TypeInfo typeInfo = TypeInfoFactory.getTypeInfoForField(field, clazz.getGenericSuperclass());
    Type genericArrayType = new GenericArrayTypeImpl(Integer[].class);
    assertEquals(genericArrayType, typeInfo.getType());
  }

  @Test
  public void testTypeInfoForField_WildcardType() throws Exception {
    Class<?> clazz = new Foo<List<?>>().getClass();
    Field field = clazz.getDeclaredField("d");
    field.setAccessible(true);
    Type type = field.getGenericType();

    TypeInfo typeInfo = TypeInfoFactory.getTypeInfoForField(field, clazz.getGenericSuperclass());
    Type wildcardType = new WildcardTypeImpl(WildcardType.UNBOUNDED, null);
    assertEquals(wildcardType, typeInfo.getType());
  }

  static class Foo<T> {
    T a;
    List<T> b;
    T[] c;
    List<?> d;
  }

}