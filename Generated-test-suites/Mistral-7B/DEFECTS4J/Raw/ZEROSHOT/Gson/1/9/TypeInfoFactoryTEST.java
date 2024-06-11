package com.google.gson.testing; // Add this package for the test file

import static org.junit.Assert.*;

import com.google.gson.reflect.TypeToken;
import com.google.gson.reflect.TypeUtils;

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
    Type type = new TypeToken<int[]>() {}.getType();
    TypeInfo expectedTypeInfo = new TypeInfoArray(type);
    TypeInfo actualTypeInfo = TypeInfoFactory.getTypeInfoForArray(type);
    assertEquals(expectedTypeInfo, actualTypeInfo);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testTypeInfoForWrongType() {
    Field field = null; // Replace with a valid field
    Type typeDefiningF = null; // Replace with a valid type
    Type type = null; // Replace with a valid type
    TypeInfo info = TypeInfoFactory.getTypeInfoForField(field, typeDefiningF, TypeUtils.toRawClass(typeDefiningF));
  }

  @Test
  public void testTypeInfoForFieldWithClassType() {
    try {
      Field field = null; // Replace with a valid field
      Type typeDefiningF = null; // Replace with a valid type
      Type type = TypeUtils.getRawType(field.getType()); // Replace with a valid type
      TypeInfo expectedTypeInfo = new TypeInfo(type);
      TypeInfo actualTypeInfo = TypeInfoFactory.getTypeInfoForField(field, typeDefiningF, TypeUtils.toRawClass(typeDefiningF));
      assertEquals(expectedTypeInfo, actualTypeInfo);
    } catch (NoSuchFieldException e) {
      e.printStackTrace();
      fail("Field not found.");
    }
  }

  @Test
  public void testTypeInfoForFieldWithParameterizedType() {
    try {
      Field field = null; // Replace with a valid field
      Type typeDefiningF = null; // Replace with a valid type with a generic type
      Type type = field.getGenericType();
      Type expectedType = new TypeToken<List<String>>() {}.getType();
      Type parentType = TypeUtils.getParameterizedType(typeDefiningF, 0, String.class);
      Type rawParentClass = TypeUtils.toRawClass(typeDefiningF);
      Type actualType = TypeInfoFactory.getActualType(type, parentType, rawParentClass);
      assertEquals(expectedType, actualType);

      TypeInfo expectedTypeInfo = new TypeInfo(expectedType);
      TypeInfo actualTypeInfo = TypeInfoFactory.getTypeInfoForField(field, typeDefiningF, rawParentClass);
      assertEquals(expectedTypeInfo, actualTypeInfo);
    } catch (NoSuchFieldException e) {
      e.printStackTrace();
      fail("Field not found.");
    }
  }

  @Test
  public void testTypeInfoForFieldWithGenericArrayType() {
    try {
      Field field = null; // Replace with a valid field
      Type typeDefiningF = null; // Replace with a valid type
      Type type = field.getGenericType();
      Type expectedType = new TypeToken<String[]>() {}.getType();
      Type parentType = TypeUtils.getParameterizedType(typeDefiningF, 0, String.class);
      Type rawParentClass = TypeUtils.toRawClass(typeDefiningF);
      Type actualType = TypeInfoFactory.getActualType(type, parentType, rawParentClass);
      assertEquals(expectedType, actualType);

      TypeInfo expectedTypeInfo = new TypeInfoArray(expectedType);
      TypeInfo actualTypeInfo = TypeInfoFactory.getTypeInfoForField(field, typeDefiningF, rawParentClass);
      assertEquals(expectedTypeInfo, actualTypeInfo);
    } catch (NoSuchFieldException e) {
      e.printStackTrace();
      fail("Field not found.");
    }
  }

  @Test
  public void testTypeInfoForFieldWithTypeVariable() {
    try {
      Field field = null; // Replace with a valid field
      Type typeDefiningF = null; // Replace with a valid type with a type variable
      Type type = field.getGenericType();
      Type parentType = TypeUtils.getParameterizedType(typeDefiningF, 0, String.class);
      Type rawParentClass = TypeUtils.toRawClass(typeDefiningF);
      TypeVariable<?> fieldTypeVariable = (TypeVariable<?>) TypeUtils.getDeclaredFieldType(type, 0);

      // Use a real type variable name and its index in the generic type definition
      String variableName = "A";
      int index = 0;

      Type expectedType = new TypeToken<List<A>>() {}.getType();
      Type actualType = TypeInfoFactory.getActualType(type, parentType, rawParentClass);
      assertEquals(expectedType, actualType);

      TypeInfo expectedTypeInfo = new TypeInfo(expectedType);
      TypeInfo actualTypeInfo = TypeInfoFactory.getTypeInfoForField(field, typeDefiningF, rawParentClass);
      assertEquals(expectedTypeInfo, actualTypeInfo);
    } catch (NoSuchFieldException e) {
      e.printStackTrace();
      fail("Field not found.");
    }
  }

  @Test
  public void testTypeInfoForFieldWithWildcardType() {
    try {
      Field field = null; // Replace with a valid field
      Type typeDefiningF = null; // Replace with a valid type with a wildcard type
      Type type = field.getGenericType();
      Type parentType = TypeUtils.getParameterizedType(typeDefiningF, 0, Object.class);
      Type rawParentClass = TypeUtils.toRawClass(typeDefiningF);
      Type expectedUpperBound = Object.class;
      WildcardType wildcardType = (WildcardType) TypeUtils.getDeclaredFieldType(type, 0);
      Type actualUpperBound = TypeInfoFactory.getActualType(wildcardType.getUpperBounds()[0], parentType, rawParentClass);
      assertEquals(expectedUpperBound, actualUpperBound);

      TypeInfo expectedTypeInfo = new TypeInfo(expectedUpperBound);
      TypeInfo actualTypeInfo = TypeInfoFactory.getTypeInfoForField(field, typeDefiningF, rawParentClass);
      assertEquals(expectedTypeInfo, actualTypeInfo);
    } catch (NoSuchFieldException e) {
      e.printStackTrace();
      fail("Field not found.");
    }
  }

  @Test
  public void testTypeInfoForWrongParentType() {
    Field field = null; // Replace with a valid field
    Type typeDefiningF = null; // Replace with a valid type
    Type type = field.getGenericType();
    Type parentType = new Object() {}; // Replace with an incompatible parent type
    Type rawParentClass = TypeUtils.toRawClass(typeDefiningF);
    try {
      TypeInfo actualTypeInfo = TypeInfoFactory.getTypeInfoForField(field, typeDefiningF, rawParentClass);
      fail("Expected IllegalArgumentException.");
    } catch (IllegalArgumentException e) {
      // Expected exception
    }
  }
}