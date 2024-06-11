package com.google.gson;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class TypeInfoFactoryTest {

  @Test
  public void testTypeInfoForArray() {
    Type type = new int[3].getClass();
    TypeInfo expected = new TypeInfoArray(type);
    TypeInfo actual = TypeInfoFactory.getTypeInfoForArray(type);
    assertEquals(expected, actual);
  }

  @Test
  public void testTypeInfoForField_ClassType() {
    Class<?> rawParentClass = String[].class;
    Field field = rawParentClass.getComponentType().getDeclaredField("length");
    Type typeDefiningF = field.getDeclaringClass();
    Type type = field.getType();

    TypeInfo expected = new TypeInfo(int.class);
    TypeInfo actual = TypeInfoFactory.getTypeInfoForField(field, typeDefiningF, rawParentClass);
    assertEquals(expected, actual);
  }

  @Test
  public void testTypeInfoForField_ParameterizedType() {
    Class<?> rawParentClass = List.class;
    Type typeDefiningF = List.class.getGenericSuperclass();
    Field field = rawParentClass.getDeclaredField("size");
    Type type = field.getType();

    TypeInfo expected = new TypeInfo(int.class);
    TypeInfo actual = TypeInfoFactory.getTypeInfoForField(field, typeDefiningF, rawParentClass);
    assertEquals(expected, actual);
  }

  @Test
  public void testTypeInfoForField_GenericArrayType() {
    Class<?> rawParentClass = String[].class;
    Field field = rawParentClass.getDeclaredField("length");
    Type typeDefiningF = field.getDeclaringClass();
    Type type = field.getType();

    TypeInfo expected = new TypeInfoArray(int.class);
    TypeInfo actual = TypeInfoFactory.getTypeInfoForField(field, typeDefiningF, rawParentClass);
    assertEquals(expected, actual);
  }

  @Test
  public void testTypeInfoForField_TypeVariable() {
    Class<?> rawParentClass = List.class;
    Type typeDefiningF = List.class.getGenericSuperclass();
    Field field = rawParentClass.getDeclaredField("list");
    Type type = field.getType();

    TypeVariable<List<String>> fieldTypeVariable = (TypeVariable<List<String>>) type.getComponentType();
    Type[] actualTypeArguments = ((ParameterizedType) typeDefiningF).getActualTypeArguments();

    Type expected = String.class;
    int indexOfActualTypeArgument = getIndex(rawParentClass.getTypeParameters(), fieldTypeVariable);
    Type actual = actualTypeArguments[indexOfActualTypeArgument];
    TypeInfo infoExpected = new TypeInfo(expected);
    TypeInfo infoActual = new TypeInfo(actual);

    TypeInfo expectedTypeInfo = new TypeInfo(expected.getClass());
    TypeInfo actualTypeInfo = TypeInfoFactory.getTypeInfoForField(field, typeDefiningF, rawParentClass);
    assertEquals(expectedTypeInfo, actualTypeInfo);
  }

  @Test(expected = UnsupportedOperationException.class)
  public void testTypeInfoForField_MissingTypeParameter() {
    Class<?> rawParentClass = List.class;
    Type typeDefiningF = List.class.getGenericSuperclass();
    Field field = rawParentClass.getDeclaredField("size2"); // doesn't exist
    Type type = field.getType();
    TypeInfo actual = TypeInfoFactory.getTypeInfoForField(field, typeDefiningF, rawParentClass);
  }

  @Test
  public void testGetActualType() {
    Class<?> rawParentClass = List.class;
    Type typeToEvaluate = List.class.getGenericSuperclass();

    TypeInfo expected = new TypeInfo(List.class);
    TypeInfo actual =
        TypeInfoFactory.getActualType(typeToEvaluate, null, rawParentClass).getTypeInfo();
    assertEquals(expected, actual);
  }

  @Test
  public void testGetActualType_ClassType() {
    Class<?> rawParentClass = List.class;
    Type typeToEvaluate = String[].class;

    TypeInfo expected = new TypeInfoArray(String.class);
    TypeInfo actual =
        TypeInfoFactory.getActualType(typeToEvaluate, null, rawParentClass).getTypeInfo();
    assertEquals(expected, actual);
  }

  @Test
  public void testGetActualType_ParameterizedType() {
    Class<?> rawParentClass = List.class;
    Type parentType = List.class.getGenericSuperclass();
    Type typeToEvaluate = List.class.getDeclaredField("list").getType();

    TypeInfo expected = new TypeInfoArray(String.class);
    TypeInfo actual =
        TypeInfoFactory.getActualType(typeToEvaluate, parentType, rawParentClass).getTypeInfo();
    assertEquals(expected, actual);
  }

  @Test
  public void testGetActualType_GenericArrayType() {
    Class<?> rawParentClass = String[].class;
    Type parentType = rawParentClass.getComponentType();
    Type typeToEvaluate = rawParentClass;

    TypeInfo expected = new TypeInfoArray(String.class);
    TypeInfo actual =
        TypeInfoFactory.getActualType(typeToEvaluate, parentType, rawParentClass).getTypeInfo();
    assertEquals(expected, actual);
  }

  @Test
  public void testGetActualType_TypeVariable() {
    Class<?> rawParentClass = List.class;
    Type parentType = List.class.getGenericSuperclass();
    Type typeToEvaluate = List.class.getDeclaredField("list").getType();

    TypeVariable<List<String>> fieldTypeVariable = (TypeVariable<List<String>>) typeToEvaluate.getComponentType();
    Type[] actualTypeArguments = ((ParameterizedType) parentType).getActualTypeArguments();

    Type expected = String.class;
    int indexOfActualTypeArgument = getIndex(rawParentClass.getTypeParameters(), fieldTypeVariable);
    Type actual = actualTypeArguments[indexOfActualTypeArgument];
    TypeInfo expectedTypeInfo = new TypeInfo(expected);
    TypeInfo actualTypeInfo =
        TypeInfoFactory.getActualType(typeToEvaluate, parentType, rawParentClass).getTypeInfo();
    assertEquals(expectedTypeInfo, actualTypeInfo);
  }

  @Test
  public void testGetActualType_WildcardType() {
    Class<?> rawParentClass = List.class;
    Type parentType = List.class.getGenericSuperclass();
    Type typeToEvaluate = List.class.getDeclaredField("list").getType();
    Type wildcardType = new WildcardType(null, List.class);

    Type expected = Object.class;
    TypeInfo expectedTypeInfo = new TypeInfo(expected);
    TypeInfo actualTypeInfo =
        TypeInfoFactory.getActualType(wildcardType, parentType, rawParentClass).getTypeInfo();
    assertEquals(expectedTypeInfo, actualTypeInfo);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testGetActualType_InvalidType() {
    Class<?> rawParentClass = List.class;
    Type parentType = List.class.getGenericSuperclass();
    Type typeToEvaluate = new Object();

    TypeInfo actual = TypeInfoFactory.getActualType(typeToEvaluate, parentType, rawParentClass).getTypeInfo();
  }

  private int getIndex(TypeVariable<?>[] types, TypeVariable<?> type) {
    for (int i = 0; i < types.length; ++i) {
      if (type.equals(types[i])) {
        return i;
      }
    }
    throw new IllegalStateException("How can the type variable not be present in the class declaration!");
  }
}