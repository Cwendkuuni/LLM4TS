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

  private static Type getTestType(Type type) {
    switch (type.getTypeName().hashCode()) {
      case 1:
        return new ParameterizedTypeImpl(String[].class, new Type[]{Integer[].class});
      case 2:
        return new WildcardType(new Type[]{Integer.class}, null);
      case 3:
        return new TypeVariable<String>() {};
      case 4:
        return new Class<Integer>() {}.getTypeParameters()[0];
      case 5:
        return new Class<List<Integer>>() {}.getGenericSuperclass();
      case 6:
        return new Class<Map<String, Integer>>() {}.getTypeParameters()[1].getGenericComponentType();
      default:
        throw new RuntimeException("Unexpected type: " + type);
    }
  }

  @Test
  public void testTypeInfoForArray() {
    TypeInfo result = TypeInfoFactory.getInstance().getTypeInfoForArray(new Type[] { String[].class });
    TypeInfo expected = new TypeInfoArray(String[].class);
    assertEquals(expected, result);
  }

  @Test
  public void testTypeInfoForField_TypeVariable() {
    Class<?> rawParentClass = List.class;
    Field field = new List<Integer>() {}.getClass().getDeclaredField("list");
    Type typeDefiningF = rawParentClass.getGenericSuperclass();
    Type type = field.getGenericType();

    TypeInfo result = TypeInfoFactory.getInstance().getTypeInfoForField(field, typeDefiningF);
    Type expected = getTestType(TypeUtils.getRawType(type));
    assertEquals(new TypeInfo(expected), result);
  }

  @Test
  public void testTypeInfoForField_Class() {
    Class<?> rawParentClass = Map.class;
    Field field = new HashMap<String, Integer>() {}.getClass().getDeclaredField("entrySet");
    Type typeDefiningF = rawParentClass.getGenericSuperclass();
    Type type = field.getGenericType();

    TypeInfo result = TypeInfoFactory.getInstance().getTypeInfoForField(field, typeDefiningF);
    Type expected = getTestType(Map.Entry.class);
    assertEquals(new TypeInfo(expected), result);
  }

  @Test
  public void testTypeInfoForField_ParameterizedType() {
    Class<?> rawParentClass = Map.class;
    Field field = new HashMap<String, Integer>() {}.getClass().getDeclaredField("entrySet");
    Type typeDefiningF = rawParentClass.getGenericSuperclass();
    Type type = field.getGenericType();
    Type[] actualTypeArguments = ((ParameterizedType) type).getActualTypeArguments();
    Type[] expectedTypeArguments = new Type[] { String.class, Integer.class };

    TypeInfo result = TypeInfoFactory.getInstance().getTypeInfoForField(field, typeDefiningF);
    TypeInfo expected = new TypeInfo(new ParameterizedTypeImpl(Map.Entry.class, expectedTypeArguments));
    assertEquals(expected, result);
  }

  @Test
  public void testTypeInfoForField_GenericArrayType() {
    Class<?> rawParentClass = List.class;
    Field field = new ArrayList<Integer>() {}.getClass().getDeclaredField("array");
    Type typeDefiningF = rawParentClass.getGenericSuperclass();
    Type type = field.getGenericType();

    TypeInfo result = TypeInfoFactory.getInstance().getTypeInfoForField(field, typeDefiningF);
    Type expected = new TypeInfoArray(Integer[].class);
    assertEquals(expected, result);
  }

  @Test
  public void testTypeInfoForField_WildcardType() {
    Class<?> rawParentClass = List.class;
    Field field = new ArrayList<Integer>() {}.getClass().getDeclaredField("list");
    Type typeDefiningF = rawParentClass.getGenericSuperclass();
    Type type = field.getGenericType();

    TypeInfo result = TypeInfoFactory.getInstance().getTypeInfoForField(field, typeDefiningF);
    Type expected = new TypeInfoWildcard(new WildcardType(new Type[] { Integer.class }, null));
    assertEquals(expected, result);
  }

  @Test(expected = UnsupportedOperationException.class)
  public void testTypeInfoForField_NoType() {
    Class<?> rawParentClass = List.class;
    Field field = new ArrayList<Integer>() {}.getClass().getDeclaredField("list");
    Type typeDefiningF = rawParentClass.getGenericSuperclass();
    Type type = null;

    TypeInfoFactory.getInstance().getTypeInfoForField(field, typeDefiningF, type);
  }
}

class TypeInfoWildcard extends TypeInfo {
  private final WildcardType wildcardType;

  public TypeInfoWildcard(WildcardType wildcardType) {
    this.wildcardType = wildcardType;
  }

  @Override
  public String toString() {
    String wildcard =
        wildcardType.getUpperBounds().length > 0
            ? "?" + wildcardType.getUpperBounds()[0].getTypeName()
            : "?";
    return wildcardType.getLowerBounds().length > 0
        ? wildcard + ":" + wildcardType.getLowerBounds()[0].getTypeName()
        : wildcard;
  }
}

class TypeInfoArray extends TypeInfo {
  private final Type arrayType;

  public TypeInfoArray(Type arrayType) {
    this.arrayType = arrayType;
  }

  @Override
  public String toString() {
    return arrayType.getTypeName();
  }
}