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
    TypeInfoArray typeInfo = TypeInfoFactory.getTypeInfoForArray(type);
    assertEquals(int[].class, typeInfo.getRawType());
    assertEquals(3, typeInfo.getSize());
  }

  @Test
  public void testTypeInfoForField_Class() {
    Class<?> clazz = MyClass.class;
    Field field = clazz.getField("field");
    Type type = field.getType();
    TypeInfo typeInfo = TypeInfoFactory.getTypeInfoForField(field, null);
    assertEquals(type, typeInfo.getType());
  }

  @Test
  public void testTypeInfoForField_ParameterizedType() {
    class MyClass<T> {
      T field;
    }

    Field field = MyClass.class.getField("field");
    Type type = field.getGenericType();
    TypeInfo typeInfo = TypeInfoFactory.getTypeInfoForField(field, null);
    assertEquals(new TypeToken<T>() {}.getType(), typeInfo.getType());
  }

  @Test
  public void testTypeInfoForField_GenericArrayType() {
    class MyClass<T> {
      T[] field;
    }

    Field field = MyClass.class.getField("field");
    Type type = field.getGenericType();
    TypeInfo typeInfo = TypeInfoFactory.getTypeInfoForField(field, null);
    assertEquals(new TypeToken<T[]>() {}.getType(), typeInfo.getType());
  }

  @Test
  public void testTypeInfoForField_TypeVariable() {
    class MyClass<T> {
      T field;
    }

    Field field = MyClass.class.getField("field");
    TypeVariable<?> typeVariable = (TypeVariable<?>) field.getGenericType();
    Type parentType = MyClass.class;
    TypeInfo typeInfo = TypeInfoFactory.getTypeInfoForField(field, parentType);
    assertEquals(typeVariable, typeInfo.getType());
  }

  @Test
  public void testTypeInfoForField_WildcardType() {
    class MyClass<T> {
      List<T> field;
    }

    Field field = MyClass.class.getField("field");
    Type type = field.getGenericType();
    TypeInfo typeInfo = TypeInfoFactory.getTypeInfoForField(field, null);
    assertTrue(typeInfo instanceof WildcardTypeImpl);
  }

  private static class MyClass<T> {
    T field;
  }
}