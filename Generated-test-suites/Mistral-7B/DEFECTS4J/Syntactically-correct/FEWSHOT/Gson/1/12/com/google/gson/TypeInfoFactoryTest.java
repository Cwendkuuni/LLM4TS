package com.google.gson;

import static org.junit.Assert.assertInstanceOf;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;

import org.junit.Test;

public class TypeInfoFactoryTest {

  private static final Class<?> RAW_PARENT_CLASS = Object.class;

  @Test
  public void testTypeInfoForArray() {
    Type type = new int[0].getClass().getComponentType();
    TypeInfoArray info = TypeInfoFactory.TypeInfoForArray(type);
    assertEquals(int[].class, info.getRawType());
  }

  @Test
  public void testTypeInfoForField_TypeVariable() throws NoSuchFieldException, IllegalAccessException {
    Class<?> rawParentClass = Integer.class;
    Class<?> clazz = new Foo<Integer>().getClass();

    Field field = clazz.getDeclaredField("field");
    field.setAccessible(true);

    Type type = field.getGenericType();
    TypeInfo info = TypeInfoFactory.getTypeInfoForField(field, rawParentClass);
    assertEquals(Integer.TYPE, info.getType());
  }

  @Test
  public void testTypeInfoForField_Class() throws NoSuchFieldException, IllegalAccessException {
    Class<?> rawParentClass = Integer.class;
    Class<?> clazz = new Foo<Integer>().getClass();

    Field field = clazz.getDeclaredField("field");
    field.setAccessible(true);

    Type type = field.getGenericType();
    TypeInfo info = TypeInfoFactory.getTypeInfoForField(field, rawParentClass);
    assertEquals(Integer.TYPE, info.getType());
  }

  @Test
  public void testTypeInfoForField_ParameterizedType() throws NoSuchFieldException, IllegalAccessException {
    Class<?> rawParentClass = Map.class;
    Class<?> clazz = new MyMap<Integer, String>().getClass();

    Field field = clazz.getDeclaredField("entrySet");
    field.setAccessible(true);

    Type type = field.getGenericType();
    TypeInfo info = TypeInfoFactory.getTypeInfoForField(field, rawParentClass);
    assertEquals(Set.class, info.getRawType());
    assertEquals(Map.Entry.class, info.getType());
  }

  @Test
  public void testTypeInfoForField_ArrayOfParameterizedType() throws NoSuchFieldException, IllegalAccessException {
    Class<?> rawParentClass = Map.class;
    Class<?> clazz = new MyMap<Integer, String>().getClass();

    Method method = clazz.getMethod("entrySet");
    Type type = method.getGenericReturnType();

    TypeInfo info = TypeInfoFactory.getTypeInfoForField(method, rawParentClass);
    assertEquals(Set.class, info.getRawType());
    assertEquals(Map.Entry[].class, info.getType());
  }

  @Test
  public void testTypeInfoForField_GenericArrayType() throws NoSuchFieldException, IllegalAccessException {
    Class<?> rawParentClass = List.class;
    Class<?> clazz = new MyList<Integer>().getClass();

    Field field = clazz.getDeclaredField("values");
    field.setAccessible(true);

    Type type = field.getGenericType();
    TypeInfo info = TypeInfoFactory.getTypeInfoForField(field, rawParentClass);
    assertEquals(Object[].class, info.getType());
    assertEquals(Integer.class, TypeUtils.getComponentType(info.getRawType()));
  }

  @Test
  public void testTypeInfoForField_TypeVariableInParameterizedType()
      throws NoSuchFieldException, IllegalAccessException {
    Class<?> rawParentClass = List.class;
    Class<?> clazz = new MyList<Foo<Bar>>().getClass();

    Field field = clazz.getDeclaredField("values");
    field.setAccessible(true);

    Type type = field.getGenericType();
    TypeInfo info = TypeInfoFactory.getTypeInfoForField(field, rawParentClass);
    assertEquals(List.class, info.getRawType());
    assertEquals(Foo.class, info.getType().getComponentType());
  }

  @Test
  public void testTypeInfoForField_ArrayOfTypeVariableInParameterizedType()
      throws NoSuchFieldException, IllegalAccessException {
    Class<?> rawParentClass = List.class;
    Class<?> clazz = new MyList<Foo<Bar>[]>().getClass();

    Field field = clazz.getDeclaredField("values");
    field.setAccessible(true);

    Type type = field.getGenericType();
    TypeInfo info = TypeInfoFactory.getTypeInfoForField(field, rawParentClass);
    assertEquals(List.class, info.getRawType());
    assertEquals(Foo[].class, info.getType());
  }

  @Test(expected = UnsupportedOperationException.class)
  public void testTypeInfoForField_TypeVariableNoParentType()
      throws NoSuchFieldException, IllegalAccessException {
    Class<?> rawParentClass = List.class;
    Class<?> clazz = new Foo<Foo<Bar>>().getClass();

    Field field = clazz.getDeclaredField("typeVariable");
    field.setAccessible(true);

    Type type = field.getGenericType();
    TypeInfoFactory.getTypeInfoForField(field, rawParentClass);
  }

  @Test(expected = UnsupportedOperationException.class)
  public void testTypeInfoForField_WildcardType()
      throws NoSuchFieldException, IllegalAccessException {
    Class<?> rawParentClass = List.class;
    Class<?> clazz = new MyList<>()
        .getClass(); // List<capture#1-of ? extends Number>>

    Field field = clazz.getDeclaredField("values");
    field.setAccessible(true);

    Type type = field.getGenericType();
    TypeInfoFactory.getTypeInfoForField(field, rawParentClass);
  }

  @Test
  public void testGetActualType_Class() {
    assertEquals(String.class, TypeInfoFactory.getActualType(String.class, null, null));
  }

  @Test
  public void testGetActualType_ParameterizedType() {
    assertEquals(Map.Entry.class, TypeInfoFactory.getActualType(Map.Entry.class, Map.class, Map.class));
  }

  @Test
  public void testGetActualType_GenericArrayType() {
    assertEquals(Integer[].class, TypeInfoFactory.getActualType(Integer[].class, Object[].class, Object.class));
  }

  @Test
  public void testGetActualType_TypeVariable() {
    TypeVariable<String> typeVariable = String.class.getTypeParameters()[0];
    assertEquals(String.class, TypeInfoFactory.getActualType(typeVariable, Map.class, Map.class));
  }

  @Test
  public void testGetActualType_WildcardType() {
    WildcardType wildcardType = TypeToken.get(List.class).getRawType().getComponentType();
    assertEquals(Number.class, TypeInfoFactory.getActualType(wildcardType, Map.class, Map.class));
  }

  @Test
  public void testExtractRealTypes_EmptyArray() {
    // Test case when no types are present
    Type[] types = new Type[0];
    Type[] actualTypes = TypeInfoFactory.extractRealTypes(types, Map.class, Object.class);
    assertEquals(0, actualTypes.length);
  }

  @Test
  public void testExtractRealTypes() {
    Type[] types = new Type[] { new TypeVariable<String>() {}.getType(), Integer[].class };
    Type[] actualTypes = TypeInfoFactory.extractRealTypes(types, Map.class, Object.class);
    assertEquals(Integer[].class, actualTypes[0]);
  }

  private static class Foo<T> {
    T field;
  }

  private static class MyList<T> extends ArrayList<T> {
  }

  private static class MyMap<K, V> extends HashMap<K, V> {
  }
}