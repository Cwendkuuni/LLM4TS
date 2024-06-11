package com.google.gson;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.lang.reflect.WildcardType;

import org.junit.Test;

public class TypeInfoFactoryTest {

  @Test
  public void testTypeInfoForArray() {
    Type type = new int[0].getClass().getComponentType();
    TypeInfo typeInfo = TypeInfoFactory.TypeInfoForArray(type);
    assertEquals(int[].class, typeInfo.getType());
  }

  @Test
  public void testTypeInfoForField() throws NoSuchFieldException, IllegalAccessException {
    Class<?> clazz = Integer.class;
    Field field = clazz.getField("VALUE");
    Type type = field.getGenericType();
    Type parentType = clazz.getGenericSuperclass();
    TypeInfo typeInfo = TypeInfoFactory.getTypeInfoForField(field, parentType);
    assertEquals(int.class, typeInfo.getType());
  }

  @Test
  public void testGetActualTypeForClass() {
    Type fieldType = Double.TYPE;
    Type type = TypeToken.get(Double.class).getType();
    TypeInfo typeInfo = TypeInfoFactory.getTypeInfoForField(null, type, null);
    assertEquals(fieldType, typeInfo.getType());
  }

  @Test
  public void testGetActualTypeForParameterizedType() {
    Class<?> clazz = List.class;
    Field field = clazz.getField("TYPE");
    Type type = field.getGenericType();
    Type parentType = clazz.getGenericSuperclass();
    Type rawParentClass = TypeUtils.toRawClass(parentType);
    TypeInfo typeInfo = TypeInfoFactory.getTypeInfoForField(field, parentType, rawParentClass);
    assertEquals(java.util.List.class, typeInfo.getType());
  }

  @Test
  public void testGetActualTypeForGenericArrayType() {
    Class<?> clazz = String[].class;
    int index = 0;
    Type type = clazz.getComponentType();
    Type parentType = clazz.getComponentType().getClass();
    Type rawParentClass = TypeUtils.toRawClass(parentType);
    TypeInfo typeInfo = TypeInfoFactory.getTypeInfoForField(null, parentType, rawParentClass);
    assertEquals(String.class, typeInfo.getType());
  }

  @Test
  public void testGetActualTypeForTypeVariable() throws NoSuchFieldException, IllegalAccessException {
    Class<?> clazz = Map.Entry.class;
    Field field = clazz.getField("value");
    Type type = field.getGenericType();
    Type parentType = clazz.getGenericSuperclass();
    Type rawParentClass = TypeUtils.toRawClass(parentType);
    TypeVariable<?> typeVariable = (TypeVariable<?>) type;
    int index = TypeInfoFactory.getIndex(clazz.getTypeParameters(), typeVariable);
    Type actualTypeArgument = TypeUtils.boxedType(int.class);
    Type[] actualTypeArguments = { actualTypeArgument };
    Type parentTypeParameterized = new ParameterizedTypeImpl(clazz, actualTypeArguments, null);
    Type[] extractedRealTypes = TypeInfoFactory.extractRealTypes(new Type[] { type }, parentType, rawParentClass);
    assertEquals(actualTypeArgument, extractedRealTypes[index]);
  }

  @Test(expected = UnsupportedOperationException.class)
  public void testGetActualTypeForWildcardType() {
    Class<?> clazz = List.class;
    Field field = clazz.getField("TYPE");
    Type type = field.getGenericType();
    Type parentType = clazz.getGenericSuperclass();
    Type rawParentClass = TypeUtils.toRawClass(parentType);
    TypeInfo typeInfo = TypeInfoFactory.getTypeInfoForField(field, parentType, rawParentClass);
    // This test should throw an UnsupportedOperationException
  }

  @Test
  public void testGetActualTypeForInvalidType() {
    Class<?> clazz = Integer.class;
    Field field = clazz.getField("VALUE");
    Type type = field.getGenericType();
    Type parentType = clazz.getGenericSuperclass();
    Type rawParentClass = TypeUtils.toRawClass(parentType);
    // This test should throw an IllegalArgumentException
  }

}