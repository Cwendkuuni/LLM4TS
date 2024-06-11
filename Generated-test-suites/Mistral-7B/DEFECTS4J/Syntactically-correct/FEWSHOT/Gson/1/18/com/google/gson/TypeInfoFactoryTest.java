package com.google.gson;

import static org.junit.Assert.assertInstanceOf;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.lang.reflect.WildcardType;

import org.junit.Test;

public class TypeInfoFactoryTest {

  @Test
  public void testTypeInfoForArray() {
    Type type = new int[0].getClass().getComponentType();
    TypeInfoType actualTypeInfo = TypeInfoFactory.TypeInfoForArray(type);
    assertInstanceOf(TypeInfoArray.class, actualTypeInfo);
  }

  @Test
  public void testTypeInfoForField_ClassType() {
    Class<?> clazz = Integer.class;
    Field field = clazz.getField("value");
    Type type = field.getType();

    TypeInfo actualTypeInfo = TypeInfoFactory.getTypeInfoForField(field, type.getTypeName());
    assertInstanceOf(TypeInfoPrimitive.class, actualTypeInfo);
    assertEquals(Integer.TYPE, actualTypeInfo.getType());
  }

  @Test
  public void testTypeInfoForField_ParameterizedType() {
    Class<?> clazz = new ArrayList<Integer>().getClass();
    Field field = clazz.getField("list");
    Type type = field.getGenericType();

    TypeInfo actualTypeInfo = TypeInfoFactory.getTypeInfoForField(field, type.getTypeName());
    assertInstanceOf(TypeInfoCollection.class, actualTypeInfo);
  }

  @Test
  public void testTypeInfoForField_GenericArrayType() {
    Class<?> clazz = Integer[].class;
    Field field = clazz.getField("value");
    Type type = field.getType();

    TypeInfo actualTypeInfo = TypeInfoFactory.getTypeInfoForField(field, type.getTypeName());
    assertInstanceOf(TypeInfoArray.class, actualTypeInfo);
    assertEquals(Integer.TYPE, actualTypeInfo.getType().getComponentType());
  }

  @Test
  public void testTypeInfoForField_TypeVariable() {
    Class<MyList<T>> clazz = new MyList<T>().getClass();
    Field field = clazz.getField("list");
    Type type = field.getGenericType();

    int indexOfActualTypeArgument = 0;
    TypeVariable<T> expectedTypeVariable = clazz.getTypeParameters()[indexOfActualTypeArgument];

    TypeInfo actualTypeInfo = TypeInfoFactory.getTypeInfoForField(field, type.getTypeName());
    assertInstanceOf(TypeInfoTypeVariable.class, actualTypeInfo);
    assertEquals(expectedTypeVariable, actualTypeInfo.getTypeVariable());
  }

  @Test
  public void testTypeInfoForField_WildcardType() {
    Class<?> clazz = new MyClass<T>().getClass();
    Field field = clazz.getField("list");
    Type type = field.getGenericType();

    WildcardType wildcardType = (WildcardType) type;

    Type upperBound = wildcardType.getUpperBounds()[0];
    TypeInfo expectedTypeInfo = null;

    if (upperBound instanceof Class<?>) {
      expectedTypeInfo = TypeInfoFactory.getTypeInfoForField(field, upperBound.getTypeName());
    } else {
      expectedTypeInfo = TypeInfoFactory.getTypeInfoForField(field, upperBound);
    }

    TypeInfo actualTypeInfo = TypeInfoFactory.getTypeInfoForField(field, type.getTypeName());
    assertInstanceOf(expectedTypeInfo.getClass(), actualTypeInfo);
  }

  @Test(expected = UnsupportedOperationException.class)
  public void testTypeInfoForField_UnsupportedType() {
    Class<?> clazz = Integer.class;
    Field field = clazz.getField("notAGenericField");

    TypeInfoFactory.getTypeInfoForField(field, field.getType().getTypeName());
  }

  @Test(expected = IllegalArgumentException.class)
  public void testGetActualType_UnsupportedType() {
    Type unsupportedType = new Object() {}.getClass();
    Type parentType = Object.class;
    Class<?> rawParentClass = Object.class;

    TypeInfoFactory.getActualType(unsupportedType, parentType, rawParentClass);
  }

  @Test
  public void testExtractRealTypes() {
    Type type = new ArrayList<List<List<String>>>().getClass().getGenericSuperclass();
    Type[] actualTypeArguments = TypeInfoFactory.extractRealTypes(type.getActualTypeArguments(), type, type.getRawType());

    assertInstanceOf(ParameterizedType.class, actualTypeArguments[0]);
    assertInstanceOf(ParameterizedType.class, actualTypeArguments[1]);
    assertInstanceOf(ParameterizedType.class, actualTypeArguments[2]);
    assertInstanceOf(Class.class, actualTypeArguments[3]);
  }

  @Test
  public void testGetIndex() {
    TypeVariable<T>[] types = new TypeVariable<T>[] { new TypeVariable<T>() {}, new TypeVariable<T>() {}, new TypeVariable<T>() {} };
    TypeVariable<T> type = new TypeVariable<T>() {};

    int index = TypeInfoFactory.getIndex(types, type);

    assertEquals(2, index);
  }
}