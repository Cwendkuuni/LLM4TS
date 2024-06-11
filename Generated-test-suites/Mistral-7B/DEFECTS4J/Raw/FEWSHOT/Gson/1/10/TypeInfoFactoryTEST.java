package com.google.gson;

import static com.google.common.base.Preconditions.checkArgument;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertInstanceOf;
import static org.junit.Assert.assertNull;

import com.google.common.base.Preconditions;
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
    TypeInfoArray typeInfoArray = TypeInfoFactory.getTypeInfoForArray(type);
    assertEquals(int[].class, typeInfoArray.getRawType());
    assertEquals(int.class, typeInfoArray.getComponentType());
    assertEquals(int.class, typeInfoArray.getComponentType().getComponentType());
  }

  @Test
  public void testTypeInfoForField_TypeVariable() {
    class Test {
      int a;
      String b;
    }

    try {
      Field field = Test.class.getDeclaredField("b");
      field.setAccessible(true);
      Type type = field.getGenericType();
      TypeInfo typeInfo = TypeInfoFactory.getTypeInfoForField(field, Test.class.getTypeParameters()[0]);
      assertEquals(String.class, typeInfo.getType());
    } catch (NoSuchFieldException | IllegalAccessException e) {
      throw new RuntimeException(e);
    }
  }

  @Test
  public void testTypeInfoForField_ParameterizedType() {
    class Test<T> {
      T a;
    }

    Type type = Test.class.getGenericSuperclass();
    TypeInfo typeInfo = TypeInfoFactory.getTypeInfoForField(Test.class.getDeclaredField("a"), type);
    assertEquals(Type.class, typeInfo.getType().getClass());
  }

  @Test
  public void testTypeInfoForField_GenericArrayType() {
    class Test {
      int[] a;
    }

    try {
      Field field = Test.class.getDeclaredField("a");
      field.setAccessible(true);
      Type type = field.getGenericType();
      TypeInfo typeInfo = TypeInfoFactory.getTypeInfoForField(field, Test.class);
      assertEquals(int[].class, typeInfo.getRawType());
      assertEquals(int.class, typeInfo.getComponentType().getComponentType());
      assertEquals(int.class, typeInfo.getComponentType());
    } catch (NoSuchFieldException | IllegalAccessException e) {
      throw new RuntimeException(e);
    }
  }

  @Test
  public void testTypeInfoForField_TypeVariableWithParent() {
    class Test<A> {
      A a;
    }

    class TestInner {
      Test<String> test = new Test<String>();
    }

    Type testClassType = TestInner.class.getTypeName();
    Type testInnerClassType = TestInner.class.getGenericSuperclass();
    Type testVariable = TypeToken.get(String.class).getType();
    Field field = TestInner.class.getDeclaredField("test");
    try {
      Type actualType = TypeInfoFactory.getTypeInfoForField(field, testClassType)
          .getType();
      assertEquals(String.class, actualType);
    } catch (NoSuchFieldException | IllegalAccessException e) {
      throw new RuntimeException(e);
    }
  }

  @Test
  public void testTypeInfoForField_WildcardType() {
    class Test<T> {
      List<T> list;
    }

    Type type = Test.class.getGenericSuperclass();
    TypeInfo typeInfo = TypeInfoFactory.getTypeInfoForField(Test.class.getDeclaredField("list"), type);
    assertInstanceOf(List.class, typeInfo.getRawType());
  }

  @Test(expected = UnsupportedOperationException.class)
  public void testTypeInfoForField_ThrowException() {
    class Test {
      int a;
    }

    Field field = Test.class.getDeclaredField("b");
    TypeInfoFactory.getTypeInfoForField(field, Test.class);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testTypeInfoForField_TypeArgumentIsNull() {
    class Test {
      int a;
    }

    Field field = Test.class.getDeclaredField("a");
    TypeInfoFactory.getTypeInfoForField(field, null);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testTypeInfoForField_ParentTypeIsNull() {
    class Test<A> {
      A a;
    }

    Field field = Test.class.getDeclaredField("a");
    TypeInfoFactory.getTypeInfoForField(field, null);
  }

  @Test
  public void testGetActualType_Class() {
    Type actualType = TypeInfoFactory.getActualType(String.class, null, String.class);
    assertEquals(String.class, actualType);
  }

  @Test
  public void testGetActualType_ParameterizedType() {
    Type type = new Test<String>().getClass().getGenericSuperclass();
    Type actualType = TypeInfoFactory.getActualType(type, null, String.class);
    assertEquals(TypeVariable.class, actualType.getClass());
  }

  @Test
  public void testGetActualType_GenericArrayType() {
    Type type = new int[3].getClass().getComponentType();
    Type actualType = TypeInfoFactory.getActualType(type, null, int[].class);
    assertEquals(int.class, actualType);
  }

  @Test
  public void testGetActualType_TypeVariable() {
    class Test<A> {
      A a;
    }

    TypeVariable<?> typeVariable = Test.class.getTypeParameters()[0];
    Type actualType = TypeInfoFactory.getActualType(typeVariable, null, Test.class);
    assertEquals(String.class, actualType);
  }

  @Test
  public void testGetActualType_WildcardType() {
    class Test<T> {
      List<T> list;
    }

    Type wildcardType = Test.class.getGenericSuperclass();
    Type[] actualTypes = TypeInfoFactory.extractRealTypes(wildcardType.getActualTypeArguments(), null, Object.class);
    assertEquals(List.class, actualTypes[0].getClass());
  }

  @Test(expected = IllegalArgumentException.class)
  public void testGetActualType_IllegalArgument() {
    Type typeToEvaluate = null;
    Type actualType = TypeInfoFactory.getActualType(typeToEvaluate, null, Object.class);
  }
}