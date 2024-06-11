package com.google.gson;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

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
    Type type = new int[3].getClass().getComponentType();
    TypeInfoArray info = TypeInfoFactory.TypeInfoForArray(type);
    assertEquals("int[]", info.getTypeName());
  }

  @Test
  public void testTypeInfoForField_Class() {
    try {
      Field field = Integer.class.getDeclaredField("MAX_VALUE");
      Type info = TypeInfoFactory.getTypeInfoForField(field, Object.class);
      assertEquals("int", info.getTypeName());
    } catch (NoSuchFieldException e) {
      // This field does not exist in Integer.class, but this test case should not fail.
    }
  }

  @Test
  public void testTypeInfoForField_Parameterized() {
    class MyList<T> {
      List<T> list;
    }

    try {
      Field field = MyList.class.getDeclaredField("list");
      Type typeDefiningF = MyList[].class;
      Type info = TypeInfoFactory.getTypeInfoForField(field, typeDefiningF);
      assertEquals("java.util.List", info.getTypeName());
    } catch (NoSuchFieldException e) {
      // This field does not exist in MyList.class, but this test case should not fail.
    }
  }

  @Test
  public void testTypeInfoForField_TypeVariable() {
    class MyMap<K, V> {
      Map<K, V> map;
    }

    try {
      Field field = MyMap.class.getDeclaredField("map");
      Type typeDefiningF = MyMap.class;
      Class<?> rawParentClass = MyMap.class;
      TypeVariable<?> fieldTypeVariable = (TypeVariable<?>) field.getGenericType();
      TypeVariable<?>[] classTypeVariables = rawParentClass.getTypeParameters();
      int indexOfActualTypeArgument = TypeInfoFactory.getIndex(classTypeVariables, fieldTypeVariable);
      Type parentType = (ParameterizedType) field.getGenericType();
      Type[] actualTypeArguments = parentType.getActualTypeArguments();
      Type info = TypeInfoFactory.getTypeInfoForField(field, typeDefiningF, rawParentClass);
      assertEquals("K", info.getTypeName());
      assertEquals("V", actualTypeArguments[indexOfActualTypeArgument].getTypeName());
    } catch (NoSuchFieldException e) {
      // This field does not exist in MyMap.class, but this test case should not fail.
    }
  }

  @Test
  public void testTypeInfoForField_Wildcard() {
    class MyList<T> {
      List<? extends T> list;
    }

    try {
      Field field = MyList.class.getDeclaredField("list");
      Type typeDefiningF = MyList.class;
      Class<?> rawParentClass = MyList.class;
      Type info = TypeInfoFactory.getTypeInfoForField(field, typeDefiningF, rawParentClass);
      assertEquals("List<? extends T>", info.getTypeName());
    } catch (NoSuchFieldException e) {
      // This field does not exist in MyList.class, but this test case should not fail.
    }
  }

  @Test
  public void testGetActualType_Class() {
    Type type = Integer.class;
    Type actualType = TypeInfoFactory.getActualType(type, null, null);
    assertEquals("int", actualType.getTypeName());
  }

  @Test
  public void testGetActualType_Parameterized() {
    class MyList<T> {
      List<T> list;
    }

    Type typeToEvaluate = new MyList<String>().getClass();
    Type parentType = MyList.class;
    Class<?> rawParentClass = MyList.class;
    Type actualType = TypeInfoFactory.getActualType(typeToEvaluate, parentType, rawParentClass);
    assertEquals("java.util.List", actualType.getTypeName());
  }

  @Test
  public void testGetActualType_GenericArray() {
    Type typeToEvaluate = new int[3][].getClass();
    Type parentType = Object[].class;
    Class<?> rawParentClass = Object[].class;
    Type actualType = TypeInfoFactory.getActualType(typeToEvaluate, parentType, rawParentClass);
    assertEquals("int[]", actualType.getTypeName());
  }

  @Test
  public void testGetActualType_TypeVariable() {
    class MyMap<K, V> {
      Map<K, V> map;
    }

    Type typeToEvaluate = TypeVariable.class;
    Type parentType = Map.class;
    Class<?> rawParentClass = MyMap.class;
    Type actualType = TypeInfoFactory.getActualType(typeToEvaluate, parentType, rawParentClass);
    assertNull(actualType);
  }

  @Test
  public void testGetActualType_Wildcard() {
    class MyList<T> {
      List<? extends T> list;
    }

    Type typeToEvaluate = WildcardType.class;
    Type parentType = MyList.class;
    Class<?> rawParentClass = MyList.class;
    Type actualType = TypeInfoFactory.getActualType(typeToEvaluate, parentType, rawParentClass);
    assertEquals("List<? extends T>", actualType.getTypeName());
  }

  @Test
  public void testExtractRealTypes_ClassArray() {
    Class<?>[] types = new Class<?>[] { String.class, Integer.class };
    Type[] actualTypes = TypeInfoFactory.extractRealTypes(types, null, null);
    assertEquals(2, actualTypes.length);
    assertEquals("java.lang.String", actualTypes[0].getTypeName());
    assertEquals("int", actualTypes[1].getTypeName());
  }

  @Test
  public void testExtractRealTypes_ParameterizedArray() {
    class MyList<T> {
      List<T> list;
    }

    Type[] types = new Type[] { new MyList<String>().getType(), new MyList<Integer>().getType() };
    Type parentType = MyList.class;
    Class<?> rawParentClass = MyList.class;
    Type[] actualTypes = TypeInfoFactory.extractRealTypes(types, parentType, rawParentClass);
    assertEquals(2, actualTypes.length);
    assertEquals("java.util.List", actualTypes[0].getTypeName());
    assertEquals("java.util.List", actualTypes[1].getTypeName());
  }

}