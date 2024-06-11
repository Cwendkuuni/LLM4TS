package com.google.gson;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertInstanceOf;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

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
    Type intArrayType = int[].class.getComponentType();
    TypeInfoArray intTypeInfoArray = TypeInfoFactory.getTypeInfoForArray(intArrayType);
    assertEquals(int[].class, intTypeInfoArray.getType());
    assertEquals("int[]", intTypeInfoArray.getCanonicalName());
  }

  @Test
  public void testTypeInfoForField_ClassType() {
    try {
      Class<?> clazz = Class.forName("java.lang.String");
      Field value = clazz.getField("value");
      Type type = value.getGenericType();
      TypeInfo typeInfo = TypeInfoFactory.getTypeInfoForField(value, type.getClass());
      assertInstanceOf(TypeInfoPrimitive.class, typeInfo);
      assertEquals("java.lang.String", typeInfo.getType().getName());
    } catch (ClassNotFoundException e) {
      throw new RuntimeException(e);
    }
  }

  @Test
  public void testTypeInfoForField_ParameterizedType() throws NoSuchFieldException, IllegalAccessException {
    try {
      Class<?> clazz = Class.forName("com.example.MyClass");
      Field field = clazz.getField("myField");
      Type type = field.getGenericType();
      Type parentType = field.getType().getGenericSuperclass();
      TypeInfo typeInfo = TypeInfoFactory.getTypeInfoForField(field, parentType);
      assertEquals(TypeVariable.class, typeInfo.getType().getComponentType());
      assertEquals("T", typeInfo.getCanonicalName());
    } catch (ClassNotFoundException e) {
      throw new RuntimeException(e);
    }
  }

  @Test
  public void testTypeInfoForField_GenericArrayType() throws NoSuchFieldException, IllegalAccessException {
    try {
      Class<?> clazz = Class.forName("com.example.MyClass");
      Field field = clazz.getField("myField");
      Type type = field.getGenericType();
      Type parentType = field.getType().getGenericSuperclass();
      Type rawParentClass = TypeUtils.toRawType(parentType);
      Type componentType = TypeUtils.getComponentType(type);
      TypeInfo typeInfo = TypeInfoFactory.getTypeInfoForField(field, parentType);
      assertEquals(GenericArrayType.class, typeInfo.getClass());
      GenericArrayType actualType = (GenericArrayType) componentType;
      GenericArrayType expectedType = TypeUtils.wrapWithArray(TypeUtils.toRawClass(typeInfo.getType()));
      assertSame(actualType, expectedType);
    } catch (ClassNotFoundException e) {
      throw new RuntimeException(e);
    }
  }

  @Test
  public void testTypeInfoForField_TypeVariable() throws NoSuchFieldException, IllegalAccessException {
    try {
      Class<?> clazz = Class.forName("com.example.MyClassWithTypeVariable");
      Field field = clazz.getField("myField");
      Type type = field.getGenericType();
      Type parentType = field.getType().getGenericSuperclass();
      Type rawParentClass = TypeUtils.toRawType(parentType);
      TypeVariable<?> fieldTypeVariable = (TypeVariable<?>) type;
      TypeVariable<?>[] classTypeVariables = rawParentClass.getTypeParameters();
      int index = TypeInfoFactory.getTypeInfoForField(field, parentType, rawParentClass).getIndex();
      assertEquals(fieldTypeVariable, classTypeVariables[index]);
    } catch (ClassNotFoundException e) {
      throw new RuntimeException(e);
    }
  }

  @Test
  public void testTypeInfoForField_WildcardType() throws NoSuchFieldException, IllegalAccessException {
    try {
      Class<?> clazz = Class.forName("com.example.MyClassWithWildcardType");
      Field field = clazz.getField("myField");
      Type type = field.getGenericType();
      Type parentType = field.getType().getGenericSuperclass();
      Type rawParentClass = TypeUtils.toRawType(parentType);
      WildcardType wildcardType = (WildcardType) type;
      Type upperBound = wildcardType.getUpperBounds()[0];
      TypeInfo typeInfo = TypeInfoFactory.getTypeInfoForField(field, parentType, rawParentClass);
      assertEquals(upperBound, typeInfo.getType());
    } catch (ClassNotFoundException e) {
      throw new RuntimeException(e);
    }
  }

  @Test
  public void testGetActualType_ClassType() {
    assertEquals(String.class, TypeInfoFactory.getActualType(String.class, null, null));
  }

  @Test
  public void testGetActualType_ParameterizedType() {
    Type listType = new ParameterizedTypeImpl(List.class, new Type[]{Integer.class}, null);
    assertEquals(List.class, TypeInfoFactory.getActualType(listType, null, null));
  }

  @Test
  public void testGetActualType_GenericArrayType() {
    Type arrayType = new GenericArrayTypeImpl(Integer.class);
    assertEquals(Integer.class, TypeInfoFactory.getActualType(arrayType, null, null));
  }

  @Test
  public void testGetActualType_TypeVariable() {
    TypeVariable<String> typeVariable = TypeVariable.class.getDeclaredField("elementType").getType();
    Type[] actualTypeArguments = new Type[]{Integer.class};
    Type parentType = new ParameterizedTypeImpl(List.class, actualTypeArguments, null);
    assertEquals(Integer.class, TypeInfoFactory.getActualType(typeVariable, parentType, null));
  }

  @Test
  public void testGetActualType_WildcardType() {
    Type wildcardType = new WildcardTypeImpl(null, new Type[]{Object.class});
    Type parentType = new ParameterizedTypeImpl(List.class, new Type[]{Integer.class}, null);
    assertEquals(List.class, TypeInfoFactory.getActualType(wildcardType, parentType, null));
  }

  @Test
  public void testExtractRealTypes() {
    Type[] arrayTypes = new Type[]{new ArrayTypeImpl(String[].class), new ArrayTypeImpl(Integer[].class)};
    Type[] types = TypeInfoFactory.extractRealTypes(arrayTypes, null, null);
    assertArrayEquals(types, new Type[]{String[].class, Integer[].class});
  }
}