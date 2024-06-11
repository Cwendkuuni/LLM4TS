package com.google.gson.test;

import com.google.gson.reflect.TypeToken;
import com.google.gson.reflect.TypeInfo;
import com.google.gson.util.Preconditions;
import org.junit.Test;

import java.lang.reflect.Field;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.lang.reflect.WildcardType;
import java.util.Arrays;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;

public class TypeInfoFactoryTest {

  @Test
  public void testTypeInfoForArray() {
    Type type = new TypeToken<int[]>() {}.getType();
    TypeInfo info = TypeInfoFactory.getTypeInfoForArray(type);
    assertEquals(TypeInfo.ARRAY, info.type);
    assertEquals(int[].class, info.rawType);
    assertEquals(int.class, info.componentType);
  }

  @Test
  public void testTypeInfoForField() throws NoSuchFieldException {
    Class<?> clazz = new TypeVariableTestClass<>();
    Field field = clazz.getDeclaredField("field");
    Type type = field.getGenericType();
    TypeInfo info = TypeInfoFactory.getTypeInfoForField(field, clazz.getGenericSuperclass());
    assertNotNull(info);
    assertEquals(TypeInfo.FIELD, info.type);
    assertEquals(Integer.TYPE, info.rawType);
    assertEquals(Integer.TYPE, info.actualType);
  }

  @Test
  public void testGetActualTypeForClass() {
    Class<?> intClass = Integer.TYPE;
    TypeInfo info = TypeInfoFactory.getTypeInfoForField(null, intClass);
    assertEquals(TypeInfo.CLASS, info.type);
    assertEquals(intClass, info.rawType);
    assertEquals(intClass, info.actualType);
  }

  @Test
  public void testGetActualTypeForParameterizedType() {
    Type listType = new TypeToken<ArrayList<String>>() {}.getType();
    TypeInfo info = TypeInfoFactory.getTypeInfoForField(null, listType.getRawType());
    assertEquals(TypeInfo.PARAMEIZED_TYPE, info.type);
    assertEquals(ArrayList.class, info.rawType);
    Type[] actualTypeArguments = ((ParameterizedType) listType).getActualTypeArguments();
    assertEquals(String.class, info.actualTypeArguments[0]);
  }

  @Test
  public void testGetActualTypeForGenericArrayType() {
    Type listType = new TypeToken<ArrayList<String>>() {}.getType();
    Type componentType = ((GenericArrayType) listType.getComponentType()).getGenericComponentType();
    TypeInfo info = TypeInfoFactory.getTypeInfoForField(null, componentType);
    assertEquals(TypeInfo.GENERIC_ARRAY, info.type);
    assertEquals(String.class, info.rawType);
    assertEquals(String.class, info.actualType);
  }

  @Test
  public void testGetActualTypeForTypeVariable() {
    Class<?> typeVariableClass = new TypeVariableTestClass<>();
    Field field = typeVariableClass.getDeclaredField("field");
    Type type = field.getGenericType();
    TypeVariable<?> typeVariable = (TypeVariable<?>) type;
    int index = 0;
    Type expectedType = String.class;
    Type infoActualType = TypeInfoFactory.getTypeInfoForField(field, typeVariableClass.getGenericSuperclass())
        .actualType;
    Type actualType = TypeUtils.toRawType(infoActualType);
    if (actualType instanceof Class<?>) {
      assertEquals(expectedType, actualType);
    } else if (actualType instanceof ParameterizedType) {
      ParameterizedType castedType = (ParameterizedType) actualType;
      Type[] actualTypeArguments = castedType.getActualTypeArguments();
      assertEquals(expectedType, actualTypeArguments[index]);
    } else if (actualType instanceof WildcardType) {
      WildcardType wildcardType = (WildcardType) actualType;
      Type[] upperBounds = wildcardType.getUpperBounds();
      assertEquals(expectedType, upperBounds[0]);
    } else {
      fail("Unexpected type: " + actualType);
    }
  }

  @Test
  public void testGetActualTypeForWildcardType() {
    Class<?> clazz = new WildcardTestClass<>();
    Type fieldType = clazz.getDeclaredField("field").getGenericType();
    Type expectedType = Collection.class;
    Type infoActualType = TypeInfoFactory.getTypeInfoForField(
        clazz.getDeclaredField("field"), clazz.getGenericSuperclass())
        .actualType;
    Type actualType = TypeUtils.toRawType(infoActualType);
    if (actualType instanceof Class<?>) {
      assertEquals(expectedType, actualType);
    } else if (actualType instanceof ParameterizedType) {
      ParameterizedType castedType = (ParameterizedType) actualType;
      Type[] actualTypeArguments = castedType.getActualTypeArguments();
      TypeCollection collectionType = new TypeToken<Collection<String>>() {}.getType();
      assertEquals(collectionType.getType(), collectionType.getType().getComponentType());
      assertEquals(String.class, actualTypeArguments[0]);
    } else if (actualType instanceof WildcardType) {
      fail("Unexpected wildcard type: " + actualType);
    } else {
      fail("Unexpected type: " + actualType);
    }
  }

  @Test
  public void testGetActualTypeForUnknownType() {
    try {
      Field field = UnknownClass.class.getDeclaredField("unknownField");
      Type actualType = TypeInfoFactory.getTypeInfoForField(field, UnknownClass.class.getGenericSuperclass())
          .actualType;
      fail("Expected IllegalArgumentException but got: " + actualType);
    } catch (NoSuchFieldException e) {
      // Expected
    } catch (IllegalAccessException e) {
      fail("Unexpected access exception: " + e.getMessage());
    } catch (IllegalArgumentException e) {
      // Expected
    }
  }

  @Test
  public void testGetActualTypeForInvalidParentType() {
    try {
      Field field = InvalidParentClass.class.getDeclaredField("field");
      Type actualType = TypeInfoFactory.getTypeInfoForField(field, InvalidParentClass.class.getGenericSuperclass())
          .actualType;
      fail("Expected IllegalArgumentException but got: " + actualType);
    } catch (NoSuchFieldException e) {
      // Expected
    } catch (IllegalAccessException e) {
      fail("Unexpected access exception: " + e.getMessage());
    } catch (IllegalArgumentException e) {
      // Expected
    }
  }

  @Test
  public void testGetActualTypeForNullParentType() {
    try {
      Field field = NullParentClass.class.getDeclaredField("field");
      Type actualType = TypeInfoFactory.getTypeInfoForField(field, null).actualType;
      fail("Expected NullPointerException but got: " + actualType);
    } catch (NoSuchFieldException e) {
      // Expected
    } catch (IllegalAccessException e) {
      fail("Unexpected access exception: " + e.getMessage());
    } catch (NullPointerException e) {
      // Expected
    }
  }
}

class TypeVariableTestClass<X> {
  @SuppressWarnings("unused")
  X field;

  static class InnerClass<Y> {
    static class InnerInnerClass<Z> {
      static class InnerInnerInnerClass<W> {
        int i;
      }
    }
  }
}

class WildcardTestClass<T> {
  @SuppressWarnings("unused")
  Collection<T> field;
}

class UnknownClass {
  @SuppressWarnings("unused")
  int unknownField;
}

class InvalidParentClass<A> extends ArrayList<A> {
  @SuppressWarnings("unused")
  int field;
}

class NullParentClass<A> {
  @SuppressWarnings("unused")
  int field;
}