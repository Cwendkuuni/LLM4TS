package com.google.gson;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.lang.reflect.WildcardType;

import org.junit.Test;

public class TypeInfoFactoryTest {

  @Test
  public void testTypeInfoForArray() {
    Type type = new int[3].getClass().getComponentType();
    TypeInfo expected = new TypeInfoArray(type);
    TypeInfo actual = TypeInfoFactory.TypeInfoForArray(type);
    assertEquals(expected, actual);
  }

  @Test
  public void testTypeInfoForField_TypeVariable() throws NoSuchFieldException {
    class Test {
      @SuppressWarnings("rawtypes")
      TypeVariable typeVariable = new TypeVariable("T") {};
      T field;
    }

    Test testClass = new Test();
    Field field = testClass.getClass().getDeclaredField("field");
    Type type = testClass.getClass().getTypeParameters()[0];
    TypeInfo expected = new TypeInfo(type);
    TypeInfo actual = TypeInfoFactory.TypeInfoForField(field, testClass.getClass().getGenericSuperclass(), testClass.getClass());
    assertEquals(expected, actual);
  }

  @Test
  public void testTypeInfoForField_Class() throws NoSuchFieldException {
    class Test {
      int field;
    }

    Test testInstance = new Test();
    Field field = testInstance.getClass().getDeclaredField("field");
    Type type = field.getType();
    TypeInfo expected = new TypeInfo(type);
    TypeInfo actual = TypeInfoFactory.TypeInfoForField(field, testInstance.getClass().getGenericSuperclass(), testInstance.getClass());
    assertEquals(expected, actual);
  }

  @Test
  public void testTypeInfoForField_ParameterizedType() throws NoSuchFieldException {
    class Test<T> {
      T field;
    }

    Test<Integer> testInstance = new Test<>();
    Field field = testInstance.getClass().getDeclaredField("field");
    Type type = testInstance.getClass().getGenericSuperclass().getTypeName().asSubType(ParameterizedType.class);
    Type typeVariable = TypeUtils.getTypeArguments(type)[0];
    TypeInfo expected = new TypeInfo(typeVariable);
    TypeInfo actual = TypeInfoFactory.TypeInfoForField(field, type, testInstance.getClass());
    assertEquals(expected, actual);
  }

  @Test
  public void testTypeInfoForField_GenericArrayType() throws NoSuchFieldException {
    class Test {
      int[] field;

      static class InnerClass {
        Test innerTest = new Test();
      }
    }

    Test testInstance = new Test();
    Field field = testInstance.getClass().getDeclaredField("field");
    Type componentType = testInstance.getClass().getComponentType();
    TypeInfo expected = new TypeInfoArray(componentType);
    TypeInfo actual = TypeInfoFactory.TypeInfoForField(field, testInstance.getClass().getGenericSuperclass(), testInstance.getClass());
    assertEquals(expected, actual);
  }

  @Test
  public void testTypeInfoForField_WildcardType() throws NoSuchFieldException {
    class Test<T> {
      T field;
    }

    Test<?> testInstance = new Test<>();
    Field field = testInstance.getClass().getDeclaredField("field");
    Type type = testInstance.getClass().getGenericSuperclass().getTypeName().asSubType(WildcardType.class);
    Type upperBound = TypeUtils.getTypeArguments(type)[0];
    TypeInfo expected = new TypeInfo(upperBound);
    TypeInfo actual = TypeInfoFactory.TypeInfoForField(field, type, testInstance.getClass());
    assertEquals(expected, actual);
  }

  @Test
  public void testTypeInfoForField_TypeVariableInArray() throws NoSuchFieldException {
    class Test<T> {
      T[] field;

      static class InnerClass {
        Test<String> innerTest = new Test<>();
      }
    }

    Test<Number> testInstance = new Test<>();
    Field field = testInstance.getClass().getDeclaredField("field");
    Type componentType = testInstance.getClass().getComponentType();
    Type typeVariable = TypeUtils.getRawType(componentType).getTypeName().asTypeVariable();
    TypeInfo expected = new TypeInfo(typeVariable);
    TypeInfo actual = TypeInfoFactory.TypeInfoForField(field, testInstance.getClass().getGenericSuperclass(), testInstance.getClass());
    assertEquals(expected, actual);
  }

  @Test
  public void testTypeInfoForField_TypeVariableInParameterizedType() throws NoSuchFieldException {
    class Test<T> {
      T field;

      static class InnerClass<U> {
        Test<U> innerTest = new Test<>();
      }
    }

    Test<Number> testInstance = new Test<>();
    Field field = testInstance.getClass().getDeclaredField("field");
    Type type = testInstance.getClass().getGenericSuperclass().getTypeName().asSubType(ParameterizedType.class);
    Type typeVariable = TypeUtils.getTypeArguments(type)[0];
    TypeInfo expected = new TypeInfo(typeVariable);
    TypeInfo actual = TypeInfoFactory.TypeInfoForField(field, type, testInstance.getClass());
    assertEquals(expected, actual);
  }

  @Test
  public void testTypeInfoForField_TypeVariableInGenericArrayType() throws NoSuchFieldException {
    class Test<T> {
      T[] field;

      static class InnerClass {
        Test<String> innerTest = new Test<>();
      }
    }

    Test<Number> testInstance = new Test<>();
    Field field = testInstance.getClass().getDeclaredField("field");
    Type componentType = testInstance.getClass().getComponentType();
    Type typeVariable = TypeUtils.getRawType(componentType).getTypeName().asTypeVariable();
    TypeInfo expected = new TypeInfoArray(typeVariable);
    TypeInfo actual = TypeInfoFactory.TypeInfoForField(field, testInstance.getClass().getGenericSuperclass(), testInstance.getClass());
    assertEquals(expected, actual);
  }

  @Test
  public void testTypeInfoForField_TypeVariableInWildcardType() throws NoSuchFieldException {
    class Test<T> {
      T field;

      static class InnerClass<U> {
        Test<U> innerTest = new Test<>();
      }
    }

    Test<?> testInstance = new Test<>();
    Field field = testInstance.getClass().getDeclaredField("field");
    Type type = testInstance.getClass().getGenericSuperclass().getTypeName().asSubType(WildcardType.class);
    Type typeVariable = TypeUtils.getTypeArguments(type)[0];
    TypeInfo expected = new TypeInfo(typeVariable);
    TypeInfo actual = TypeInfoFactory.TypeInfoForField(field, type, testInstance.getClass());
    assertEquals(expected, actual);
  }

  @Test
  public void testTypeInfoForField_MultipleTypeArguments() throws NoSuchFieldException {
    class Test<T, U> {
      T field1;
      U field2;
    }

    Test<Integer, String> testInstance = new Test<>();
    Field field1 = testInstance.getClass().getDeclaredField("field1");
    Field field2 = testInstance.getClass().getDeclaredField("field2");
    Type type = testInstance.getClass().getGenericSuperclass().getTypeName().asSubType(ParameterizedType.class);
    Type typeArgument1 = TypeUtils.getTypeArguments(type)[0];
    Type typeArgument2 = TypeUtils.getTypeArguments(type)[1];
    TypeInfo expected1 = new TypeInfo(typeArgument1);
    TypeInfo expected2 = new TypeInfo(typeArgument2);
    TypeInfo actual1 = TypeInfoFactory.TypeInfoForField(field1, type, testInstance.getClass());
    TypeInfo actual2 = TypeInfoFactory.TypeInfoForField(field2, type, testInstance.getClass());
    assertEquals(expected1, actual1);
    assertEquals(expected2, actual2);
  }
}