package com.google.gson;

import static org.junit.Assert.*;

import org.junit.Test;

import java.lang.reflect.Field;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.lang.reflect.WildcardType;

public class TypeInfoFactoryTest {

  @Test
  public void testTypeInfoForArray() {
    Type type = new int[5].getClass().getComponentType();
    TypeInfoArray typeInfo = TypeInfoFactory.TypeInfoForArray(type);
    assertEquals(int[].class, typeInfo.getType());
    assertEquals(int.class, typeInfo.getComponentType());
  }

  @Test
  public void testTypeInfoForField_TypeVariable() {
    class Foo<A> {
      A a;
    }

    try {
      Field field = Foo.class.getField("a");
      Type info = TypeInfoFactory.TypeInfoForField(field, null);
      assertEquals(TypeVariable.class, info.getType().getClass());
      assertTrue(info.getType() instanceof TypeVariable<?>);
      TypeVariable<?> typeVariable = (TypeVariable<?>) info.getType();
      assertEquals("A", typeVariable.getName());
    } catch (NoSuchFieldException e) {
      fail("Field 'a' not found");
    }
  }

  @Test
  public void testTypeInfoForField_Class() {
    class Foo {
      int i;
    }

    try {
      Field field = Foo.class.getField("i");
      Type info = TypeInfoFactory.TypeInfoForField(field, null);
      assertEquals(int.class, info.getType());
    } catch (NoSuchFieldException e) {
      fail("Field 'i' not found");
    }
  }

  @Test
  public void testTypeInfoForField_ParameterizedType() {
    class Foo<A> {
      List<A> list;
    }

    try {
      Field field = Foo.class.getField("list");
      Type parentType = Foo.class.getGenericSuperclass();
      Type info = TypeInfoFactory.TypeInfoForField(field, parentType);
      assertTrue(info instanceof ParameterizedTypeImpl);
      ParameterizedTypeImpl paramType = (ParameterizedTypeImpl) info;
      assertEquals(ArrayList.class, paramType.getRawType());
      assertEquals(new Type[] {int.class}, paramType.getActualTypeArguments());
    } catch (NoSuchFieldException e) {
      fail("Field 'list' not found");
    }
  }

  @Test
  public void testTypeInfoForField_GenericArrayType() {
    class Foo<A> {
      A[] array;
    }

    try {
      Field field = Foo.class.getField("array");
      Type parentType = Foo.class.getGenericSuperclass();
      Type info = TypeInfoFactory.TypeInfoForField(field, parentType);
      assertTrue(info instanceof GenericArrayTypeImpl);
      GenericArrayTypeImpl genericArrayType = (GenericArrayTypeImpl) info;
      assertEquals(A.class, genericArrayType.getComponentType());
    } catch (NoSuchFieldException e) {
      fail("Field 'array' not found");
    }
  }

  @Test
  public void testTypeInfoForField_WildcardType() {
    class Foo<A> {
      void foo(List<?> list) {
      }
    }

    try {
      Field field = Foo.class.getField("foo");
      Type parentType = Foo.class.getMethod("foo", List.class).getGenericParameterTypes()[0];
      Type info = TypeInfoFactory.TypeInfoForField(field, parentType);
      assertTrue(info instanceof WildcardTypeImpl);
      WildcardTypeImpl wildcardType = (WildcardTypeImpl) info;
      assertEquals(new Type[]{Object.class, Object.class}, wildcardType.getUpperBounds());
    } catch (NoSuchFieldException | NoSuchMethodException e) {
      fail("Method 'foo' not found");
    }
  }
}