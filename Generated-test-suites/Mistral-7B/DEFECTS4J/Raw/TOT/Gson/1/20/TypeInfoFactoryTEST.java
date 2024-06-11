package com.google.gson.tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.lang.reflect.Field;
import java.lang.reflect.Type;

import org.junit.Test;

import com.google.gson.annotations.Exclude;
import com.google.gson.reflect.TypeUtils;
import com.google.gson.reflect.TypeInfoFactory;
import com.google.gson.reflect.TypeInfo;

/**
 * Tests for {@link TypeInfoFactory}.
 *
 * @author Joel Leitch
 */
public class TypeInfoFactoryTest {

  @Test
  public void testTypeInfoForArray() {
    Type type = new int[0].getClass().getComponentType();
    TypeInfoArray info = TypeInfoFactory.getTypeInfoForArray(type);
    assertNotNull(info);
    assertEquals(int[].class, info.boxedType());
    assertEquals(int.class, info.elementType());
  }

  @Test
  public void testTypeInfoForField_Primitive() throws NoSuchFieldException {
    Class<Integer> clazz = Integer.class;
    Field f = clazz.getField("value");
    Type type = f.getGenericType();
    TypeInfo info = TypeInfoFactory.getTypeInfoForField(f, type, clazz);
    assertNotNull(info);
    assertEquals(Integer.TYPE, info.type());
    assertEquals(int.class, info.boxedType());
  }

  @Test
  public void testTypeInfoForField_Class() throws NoSuchFieldException {
    Class<MyClass> clazz = MyClass.class;
    Field f = clazz.getField("myField");
    Type type = f.getGenericType();
    MyClass myObject = new MyClass();
    TypeInfo info = TypeInfoFactory.getTypeInfoForField(f, type, clazz, myObject);
    assertNotNull(info);
    assertEquals(MyClass.class, info.type());
    assertEquals(MyClass.class, info.boxedType());
  }

  @Test
  public void testTypeInfoForField_Parameterized() throws NoSuchFieldException {
    class MyContainer<T> {
      T myField;
    }

    MyContainer<String> myObject = new MyContainer<>();
    Field f = MyContainer.class.getField("myField");
    Type type = f.getGenericType();
    TypeInfo info = TypeInfoFactory.getTypeInfoForField(f, type, MyContainer.class, myObject);
    assertNotNull(info);
    assertEquals(String.class, info.type());
    assertEquals(String.class, info.boxedType());
  }

  @Test(expected = UnsupportedOperationException.class)
  public void testTypeInfoForField_Unsupported() throws NoSuchFieldException {
    class MyContainer<T> {
      @Exclude
      T myField;
    }

    MyContainer<String> myObject = new MyContainer<>();
    Field f = MyContainer.class.getField("myField");
    Type type = f.getGenericType();
    TypeInfoFactory.getTypeInfoForField(f, type, MyContainer.class, myObject);
  }

  private static class MyClass {
    int myField;
  }
}