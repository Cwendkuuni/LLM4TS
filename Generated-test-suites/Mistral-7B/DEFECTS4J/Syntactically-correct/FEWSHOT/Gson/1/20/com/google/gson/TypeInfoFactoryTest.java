package com.google.gson;

import static com.google.common.base.Preconditions.checkArgument;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;

import java.lang.reflect.Field;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.lang.reflect.WildcardType;

import org.junit.Test;

public class TypeInfoFactoryTest {

  @Test
  public void testTypeInfoForArray() {
    Type type = new int[5].getClass().getComponentType();
    TypeInfoArray typeInfo = TypeInfoFactory.getTypeInfoForArray(type);
    assertNotNull(typeInfo);
    assertEquals(int[].class, typeInfo.getType());
  }

  @Test
  public void testTypeInfoForField() throws NoSuchFieldException {
    Class<Foo> clazz = Foo.class;
    Field field = clazz.getDeclaredField("stringField");
    Type type = field.getGenericType();
    TypeInfo typeInfo = TypeInfoFactory.getTypeInfoForField(field, clazz.getGenericSuperclass());
    assertNotNull(typeInfo);
    assertEquals(String.class, typeInfo.getType());
  }

  @Test(expected = IllegalArgumentException.class)
  public void testTypeInfoForInvalidType() {
    Object obj = new Object();
    Type type = obj.getClass().getDeclaredField("invalidTypeField")
        .getGenericType();
    TypeInfoFactory.getTypeInfoForField(null, obj.getClass().getGenericSuperclass());
  }

  @Test
  public void testGetActualTypeForArray() {
    Type type = new int[5].getClass().getComponentType();
    Type actualType = TypeInfoFactory.getActualType(type, null, null);
    assertEquals(int.class, actualType);
  }

  @Test
  public void testGetActualTypeForParameterizedType() {
    Class<Bar> barClass = Bar.class;
    Type type = barClass.getDeclaredField("listField").getGenericType();
    Type actualType = TypeInfoFactory.getActualType(type, null, null);
    assertEquals(java.util.List.class, actualType);
  }

  @Test
  public void testGetActualTypeForTypeVariable() throws NoSuchFieldException {
    Class<Foo> clazz = Foo.class;
    Field field = clazz.getDeclaredField("stringField");
    TypeVariable<?> fieldTypeVariable = (TypeVariable<?>) field.getGenericType();
    Type parentType = clazz.getGenericSuperclass();
    Type actualType = TypeInfoFactory.getActualType(fieldTypeVariable, parentType, clazz);
    assertEquals(String.class, actualType);
  }

  @Test
  public void testGetActualTypeForWildcardType() throws NoSuchFieldException {
    Class<Foo> clazz = Foo.class;
    Field field = clazz.getDeclaredField("wildcardField");
    Type type = field.getGenericType();
    Type actualType = TypeInfoFactory.getActualType(type, null, clazz);
    assertEquals(Object.class, actualType);
  }

  @Test
  public void testExtractRealTypes() {
    Type[] inputTypes = new Type[] { new Integer[3].getClass(), "String".getClass(), new Foo[5].getClass() };
    Type[] outputTypes = TypeInfoFactory.extractRealTypes(inputTypes, null, null);
    assertArrayEquals(new Type[] { Integer.TYPE, String.class, Foo.class }, outputTypes);
  }

  @Test(expected = UnsupportedOperationException.class)
  public void testExtractRealTypesInvalidParentType() {
    Type[] inputTypes = new Type[] { new Integer[3].getClass(), "String".getClass(), new Foo[5].getClass() };
    Type parentType = Integer.TYPE;
    Type[] outputTypes = TypeInfoFactory.extractRealTypes(inputTypes, parentType, null);
  }

  @Test
  public void testGetIndex() {
    TypeVariable<?>[] types = String.class.getTypeParameters();
    TypeVariable<?> type = TypeVariable.class;
    int index = TypeInfoFactory.getIndex(types, type);
    assertEquals(1, index);
  }

  static class Foo {
    String stringField;
    List<Bar> listField;
    Type wildcardField;
  }

  static class Bar {
    List<String> listField;
  }
}