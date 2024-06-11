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
    Type type = new int[5].getClass().getComponentType();
    TypeInfo typeInfo = TypeInfoFactory.getTypeInfoForArray(type);
    assertEquals("java.lang.int[]", typeInfo.getClassName());
  }

  @Test
  public void testTypeInfoForField_ClassType() {
    Class<?> clazz = Integer.class;
    Field field = clazz.getField("value");
    Type type = field.getType();
    TypeInfo typeInfo = TypeInfoFactory.getTypeInfoForField(field, clazz.getGenericSuperclass());
    assertEquals("java.lang.Integer", typeInfo.getClassName());
  }

  @Test
  public void testTypeInfoForField_ParameterizedType() {
    class MyClass<T> {
      T myField;
    }

    Field field = MyClass.class.getField("myField");
    Type type = field.getGenericType();
    TypeInfo typeInfo = TypeInfoFactory.getTypeInfoForField(field, MyClass.class.getGenericSuperclass());
    assertEquals("TypeVariable<T>", typeInfo.getClassName());
  }

  @Test
  public void testTypeInfoForField_GenericArrayType() {
    class MyClass<T> {
      T[] myField;
    }

    Field field = MyClass.class.getField("myField");
    Type type = field.getGenericType();
    TypeInfo typeInfo = TypeInfoFactory.getTypeInfoForField(field, MyClass.class.getGenericSuperclass());
    assertEquals("java.lang.Class<T[]>", typeInfo.getClassName());
  }

  @Test
  public void testTypeInfoForField_TypeVariable() {
    class MyClass<T> {
      T myField;
    }

    Field field = MyClass.class.getField("myField");
    TypeVariable<T> typeVariable = (TypeVariable<T>) field.getGenericType();
    Type parentType = MyClass.class.getGenericSuperclass();
    TypeInfo typeInfo = TypeInfoFactory.getTypeInfoForField(field, parentType);
    assertEquals(typeVariable.getName(), typeInfo.getClassName().substring(0, typeInfo.getClassName().lastIndexOf('<')));
  }

  @Test
  public void testTypeInfoForField_WildcardType() {
    class MyClass {
      List<? extends Number> myField;
    }

    Field field = MyClass.class.getField("myField");
    Type type = field.getGenericType();
    WildcardType wildcardType = (WildcardType) type;
    Type[] upperBounds = wildcardType.getUpperBounds();
    Type parentType = MyClass.class.getGenericSuperclass();
    TypeInfo typeInfo = TypeInfoFactory.getTypeInfoForField(field, parentType);
    assertEquals(upperBounds[0].getTypeName(), typeInfo.getClassName());
  }

  @Test
  public void testTypeInfoForField_ParameterizedTypeWithEmptyTypeArguments() {
    class MyClass<T, U> {
      List<T> myList;
    }

    Field field = MyClass.class.getField("myList");
    Type parentType = MyClass.class.getGenericSuperclass();
    Type[] emptyTypeArguments = new Type[0];
    Type type = new ParameterizedTypeImpl(List.class, emptyTypeArguments, parentType.getRawType());
    TypeInfo typeInfo = TypeInfoFactory.getTypeInfoForField(field, parentType);
    assertEquals(List.class.getName().asSubtype(List.class.getTypeName()), typeInfo.getClassName());
  }

  @Test
  public void testTypeInfoForField_ParameterizedTypeWithTypeArguments() {
    class MyClass<T, U> {
      MyInnerClass<T, U> innerClass;
    }

    class MyInnerClass<S, V> {
      List<S> myList;
    }

    Field field = MyClass.class.getField("innerClass");
    Type parentType = MyClass.class.getGenericSuperclass();
    Type type = field.getGenericType();
    Type[] actualTypeArguments = ((ParameterizedType) type).getActualTypeArguments();
    TypeInfo typeInfo = TypeInfoFactory.getTypeInfoForField(field, parentType);
    assertEquals(MyInnerClass.class.getName(), typeInfo.getClassName());
  }
}