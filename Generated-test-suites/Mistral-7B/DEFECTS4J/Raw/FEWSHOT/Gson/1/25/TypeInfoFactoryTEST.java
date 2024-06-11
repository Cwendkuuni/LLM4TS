package com.google.gson;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;

import org.junit.Test;

public class TypeInfoFactoryTest {

  @Test
  public void testTypeInfoForArray() {
    Type type = new int[5].getClass().getGenericComponentType();
    TypeInfoArray typeInfoArray = TypeInfoFactory.getTypeInfoForArray(type);
    assertEquals(TypeUtils.INT_TYPE, typeInfoArray.getType());
    assertEquals(int[].class, typeInfoArray.getRawType());
  }

  @Test
  public void testTypeInfoForField_ClassType() {
    try {
      Class<?> clazz = Class.forName("java.util.ArrayList");
      Field size = clazz.getField("size");
      Type type = size.getType();
      TypeInfo typeInfo = TypeInfoFactory.getTypeInfoForField(size, size.getDeclaringClass());
      assertEquals(int.class, typeInfo.getType());
      assertEquals(Integer.TYPE, typeInfo.getPrimitiveType());
      assertEquals(int.class, typeInfo.getRawType());
    } catch (ClassNotFoundException e) {
      throw new RuntimeException(e);
    }
  }

  @Test
  public void testTypeInfoForField_ParameterizedType() {
    try {
      Class<?> clazz = Class.forName("java.util.ArrayList");
      Field element = clazz.getField("element");
      Type elementType = element.getGenericType();
      Type parentType = clazz.getGenericSuperclass();
      TypeInfo typeInfo = TypeInfoFactory.getTypeInfoForField(element, parentType);
      assertEquals(Object.class, typeInfo.getType());
      assertNull(typeInfo.getPrimitiveType());
      assertEquals(Object.class, typeInfo.getRawType());
    } catch (ClassNotFoundException e) {
      throw new RuntimeException(e);
    }
  }

  @Test
  public void testTypeInfoForField_TypeVariable() {
    try {
      Class<?> clazz = Class.forName("com.example.MyClass$MyTypeVariable");
      Field field = clazz.getField("myField");
      Type fieldType = field.getGenericType();
      TypeVariable<?> typeVariable = (TypeVariable<?>) fieldType;
      Type parentType = clazz.getGenericSuperclass();
      Type actualType = TypeInfoFactory.getTypeInfoForField(field, parentType).getType();
      assertEquals(Integer.TYPE, actualType);
    } catch (ClassNotFoundException e) {
      throw new RuntimeException(e);
    }
  }

  @Test(expected = UnsupportedOperationException.class)
  public void testTypeInfoForField_UnsupportedType() {
    try {
      Class<?> clazz = Class.forName("java.lang.Object");
      Field field = clazz.getField("field");
      Type fieldType = field.getGenericType();
      Type parentType = clazz.getGenericSuperclass();
      TypeInfoFactory.getTypeInfoForField(field, parentType);
    } catch (ClassNotFoundException e) {
      throw new RuntimeException(e);
    }
  }
}