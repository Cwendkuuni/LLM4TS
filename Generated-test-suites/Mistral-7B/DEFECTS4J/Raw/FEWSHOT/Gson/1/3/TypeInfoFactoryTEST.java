package com.google.gson;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertInstanceOf;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.lang.reflect.Type;
import java.lang.reflect.WildcardType;
import java.util.Arrays;

import org.junit.Test;

public class TypeInfoFactoryTest {

  @Test
  public void testTypeInfoForArray() {
    Type intTypeArray = new int[0].getClass().getComponentType();
    TypeInfo expectedTypeInfo = new TypeInfoArray(intTypeArray);
    TypeInfo actualTypeInfo = TypeInfoFactory.getTypeInfoForArray(intTypeArray);
    assertEquals(expectedTypeInfo, actualTypeInfo);
  }

  @Test
  public void testTypeInfoForField_ClassType() {
    Class<Integer> intClass = Integer.class;
    Field field = intClass.getField("value");
    Type fieldType = field.getType();
    Type definingClassType = intClass;
    TypeInfo expectedTypeInfo = new TypeInfo(fieldType);
    TypeInfo actualTypeInfo = TypeInfoFactory.getTypeInfoForField(field, definingClassType);
    assertEquals(expectedTypeInfo, actualTypeInfo);
  }

  @Test
  public void testTypeInfoForField_ParameterizedType() throws NoSuchFieldException {
    Class<Map<String, Integer>> mapClass = Map.class;
    Field field = mapClass.getDeclaredField("entrySet");
    field.setAccessible(true);
    Type fieldType = field.getGenericType();
    Type definingClassType = mapClass;
    TypeInfo expectedTypeInfo = new TypeInfo(Map.Entry.class);
    TypeInfo actualTypeInfo = TypeInfoFactory.getTypeInfoForField(field, definingClassType);
    assertEquals(expectedTypeInfo, actualTypeInfo);
  }

  @Test
  public void testTypeInfoForField_TypeVariable() throws NoSuchFieldException {
    Class<Map<String, Integer>> mapClass = Map.class;
    Field field = mapClass.getDeclaredField("entrySet");
    field.setAccessible(true);
    Type fieldType = field.getGenericType();
    Type definingClassType = mapClass;
    Field intField = Integer.class.getDeclaredField("TYPE");
    TypeVariable<Class<?>> intTypeVariable = (TypeVariable<Class<?>>) intField.getGenericDeclaration();
    Type typeVariable = fieldType.getComponentType();
    int index = TypeInfoFactory.getTypeInfoForField(field, definingClassType).getType().getTypeName().lastIndexOf('>') + 1;
    Type expectedRawType = TypeToken.get(intTypeVariable.getBound(0)).getType();
    TypeInfo expectedTypeInfo = new TypeInfo(expectedRawType);
    TypeInfo actualTypeInfo = TypeInfoFactory.getTypeInfoForField(field, definingClassType);
    assertEquals(expectedTypeInfo, actualTypeInfo);
    assertTrue(actualTypeInfo.getType().getTypeName().substring(0, index).equals(expectedRawType.getTypeName()));
  }

  @Test
  public void testTypeInfoForField_WildcardType() throws NoSuchFieldException {
    Class<List<?>> listClass = List.class;
    Field field = listClass.getDeclaredField("list");
    field.setAccessible(true);
    Type fieldType = field.getGenericType();
    Type definingClassType = listClass;
    Type expectedTypeInfo = new TypeInfo(Object.class);
    TypeInfo actualTypeInfo = TypeInfoFactory.getTypeInfoForField(field, definingClassType);
    assertEquals(expectedTypeInfo, actualTypeInfo);
  }

  @Test
  public void testTypeInfoForField_ArrayType() throws NoSuchFieldException {
    Class<Object[]> arrayClass = Object[].class;
    Field field = arrayClass.getDeclaredField("length");
    field.setAccessible(true);
    Type fieldType = field.getGenericType();
    Type definingClassType = arrayClass;
    Type expectedTypeInfo = new TypeInfoArray(int.class);
    TypeInfo actualTypeInfo = TypeInfoFactory.getTypeInfoForField(field, definingClassType);
    assertEquals(expectedTypeInfo, actualTypeInfo);
  }

  @Test
  public void testGetActualType_ClassType() {
    Type intType = Integer.TYPE;
    Type parentType = Object.class;
    Class<?> rawParentClass = Object.class;
    Type expectedType = intType;
    Type actualType = TypeInfoFactory.getActualType(intType, parentType, rawParentClass);
    assertEquals(expectedType, actualType);
  }

  @Test
  public void testGetActualType_ParameterizedType() {
    Type listOfStringType = new TypeToken<List<String>>() {}.getType();
    Type parentType = Map.class;
    Class<?> rawParentClass = Map.class;
    Type expectedType = List.class;
    Type actualType = TypeInfoFactory.getActualType(listOfStringType, parentType, rawParentClass);
    assertEquals(expectedType, actualType);
  }

  @Test
  public void testGetActualType_GenericArrayType() {
    Type intArrayType = new int[0].getClass().getComponentType();
    Type parentType = Object.class;
    Class<?> rawParentClass = Object.class;
    Type expectedType = int[].class;
    Type actualType = TypeInfoFactory.getActualType(intArrayType, parentType, rawParentClass);
    assertEquals(expectedType, actualType);
  }

  @Test(expected = UnsupportedOperationException.class)
  public void testGetActualType_TypeVariable() {
    Type typeVariable = new TypeToken<Map<String, String>>() {}.getType();
    Type parentType = Map.class;
    Class<?> rawParentClass = Map.class;
    TypeInfoFactory.getTypeInfoForField((Field) null, parentType);
  }

  @Test(expected = UnsupportedOperationException.class)
  public void testGetActualType_WildcardType() {
    Type wildcardType = new WildcardType(new Type[] { Object.class, Object.class }, null);
    Type parentType = Object.class;
    Class<?> rawParentClass = Object.class;
    TypeInfoFactory.getTypeInfoForField((Field) null, parentType);
  }

  @Test
  public void testExtractRealTypes() {
    Type[] types = new Type[] { new TypeToken<Integer>() {}.getType(),
        new TypeToken<Map<String, String>>() {}.getType() };
    Type[] expectedTypes = new Type[] { Integer.TYPE, Map.class};
    Type[] actualTypes = TypeInfoFactory.extractRealTypes(types, null, null);
    assertArrayEquals(expectedTypes, actualTypes);
  }

  @Test
  public void testExtractRealTypes_ParameterizedType() {
    Type[] types = new Type[] { new TypeToken<Map<String, List<String>>>() {}.getType() };
    Type[] expectedTypes = new Type[] { Map.class, List.class };
    Type[] actualTypes = TypeInfoFactory.extractRealTypes(types, null, null);
    assertArrayEquals(expectedTypes, actualTypes);
  }

  @Test
  public void testExtractRealTypes_WildcardType() {
    Type[] types = new Type[] { new WildcardType(new Type[] { Object.class }, null) };
    Type[] expectedTypes = new Type[] { Object.class };
    Type[] actualTypes = TypeInfoFactory.extractRealTypes(types, null, null);
    assertArrayEquals(expectedTypes, actualTypes);
  }

  @Test
  public void testExtractRealTypes_TypeVariable() {
    Type[] types = new Type[] { new TypeVariable<Class<?>>("T") {}.getGenericDeclaration() };
    throw new UnsupportedOperationException("Expecting parameterized type, got " + types[0].getClass()
        + ".\n Are you missing the use of TypeToken idiom?\n See "
        + "http://sites.google.com/site/gson/gson-user-guide#TOC-Serializing-and-Deserializing-Gener");
  }

  private static void assertArrayEquals(Type[] expected, Type[] actual) {
    assertEquals(expected.length, actual.length);
    for (int i = 0; i < expected.length; ++i) {
      assertEquals(expected[i].getTypeName(), actual[i].getTypeName());
    }
  }
}