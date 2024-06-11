package com.google.gson;

import static com.google.common.base.Preconditions.checkArgument;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

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
    Type type = new int[0].getClass().getComponentType();
    TypeInfoArray info = TypeInfoFactory.getTypeInfoForArray(type);
    assertEquals(int[].class, info.getRawType());
    assertEquals(int.class, info.getComponentType());
  }

  @Test
  public void testTypeInfoForField_ClassType() {
    try {
      Field field = Integer.class.getDeclaredField("VALUE");
      Type parentType = field.getDeclaringClass().getType();
      Type actualType = TypeInfoFactory.getActualType(field.getType(), parentType, Integer.TYPE);
      assertEquals(int.class, actualType);
    } catch (NoSuchFieldException e) {
      throw new RuntimeException(e);
    }
  }

  @Test
  public void testTypeInfoForField_ParameterizedType() {
    class Foo<A> {
      A a;
    }

    Type parentType = Foo.class;
    Type fieldType = Foo.class.getDeclaredField("a").getGenericType();

    checkArgument(fieldType instanceof ParameterizedType, "Expected ParameterizedType, got %s", fieldType);
    TypeVariable<A> fieldTypeVariable = (TypeVariable<A>) fieldType;
    Type actualType = TypeInfoFactory.getTypeInfoForField(
        Foo.class.getDeclaredField("a"), parentType).getType();
    assertEquals(fieldTypeVariable.getGenericDeclaration().getType(), actualType);
  }

  @Test
  public void testTypeInfoForField_GenericArrayType() {
    class Foo<A> {
      A[] a;
    }

    Type parentType = Foo.class;
    Field field = Foo.class.getDeclaredField("a");
    Type componentType = field.getGenericType();

    checkArgument(componentType instanceof TypeVariable, "Expected TypeVariable, got %s", componentType);
    TypeVariable<A> componentTypeVariable = (TypeVariable<A>) componentType;
    Type actualType = TypeInfoFactory.getTypeInfoForField(field, parentType).getType();
    assertEquals(TypeUtils.wrapWithArray(componentTypeVariable.getGenericDeclaration().getType()), actualType);
  }

  @Test
  public void testTypeInfoForField_TypeVariable() {
    // Test with the same class, to make sure that this.getClass() is correctly ignored.
    class Foo<A, B> {
      A a;
      B b;
    }

    Type parentType = Foo.class;
    Field field = Foo.class.getDeclaredField("a");
    TypeVariable<?> fieldTypeVariable = (TypeVariable<?>) field.getGenericType();

    Type actualType = TypeInfoFactory.getTypeInfoForField(field, parentType).getType();
    assertEquals(fieldTypeVariable.getGenericDeclaration().getType(), actualType);
  }

  @Test(expected = UnsupportedOperationException.class)
  public void testTypeInfoForField_UnsupportedType() {
    class Foo<A> {
      int a;
    }

    Type parentType = Foo.class;
    Field field = Foo.class.getDeclaredField("a");
    assertEquals(int.class, field.getType());
    Type actualType = TypeInfoFactory.getTypeInfoForField(field, parentType).getType();
  }

  @Test
  public void testGetActualType_ClassType() {
    Type parentType = int[].class;
    Type type = int.class;
    assertEquals(type, TypeInfoFactory.getActualType(type, parentType, int[].class));
  }

  @Test
  public void testGetActualType_ParameterizedType() {
    class MyList<T> extends ArrayList<T> {}

    Type parentType = MyList.class;
    Type type = MyList.class;
    assertEquals(new ParameterizedTypeImpl(type, new Class<?>[] { Object.class }, null),
        TypeInfoFactory.getActualType(type, parentType, Object.class));
  }

  @Test
  public void testGetActualType_GenericArrayType() {
    Type parentType = int[].class;
    Type componentType = int.class;
    Type genericArrayType = new GenericArrayTypeImpl(componentType);

    Type actualType = TypeInfoFactory.getActualType(genericArrayType, parentType, int[].class);
    assertEquals(genericArrayType, actualType);
  }

  @Test
  public void testGetActualType_TypeVariable() {
    class Foo<A> {
      A a;
    }

    Type parentType = Foo.class;
    Field field = Foo.class.getDeclaredField("a");
    TypeVariable<A> fieldTypeVariable = (TypeVariable<A>) field.getGenericType();

    Type actualType = TypeInfoFactory.getActualType(fieldTypeVariable, parentType, Foo.class);
    assertEquals(fieldTypeVariable.getGenericDeclaration().getType(), actualType);
  }

  @Test(expected = UnsupportedOperationException.class)
  public void testGetActualType_WildcardType() {
    Type parentType = Object.class;
    Type type = List.class;
    Type wildcardType = new WildcardTypeImpl(TypeUtils.UNKNOWN_TYPE, TypeUtils.UNKNOWN_TYPE);

    Type actualType = TypeInfoFactory.getActualType(wildcardType, parentType, type);
  }

  @Test
  public void testExtractRealTypes() {
    Type[] actualTypeArguments = new Type[] { int[].class, String[].class };
    Type parentType = new ArrayList<Integer>().getClass();
    Type[] expectedTypes = new Type[] { int[].class, String.class };

    Type[] actualTypes = TypeInfoFactory.extractRealTypes(actualTypeArguments, parentType, ArrayList.class);
    assertArrayEquals(expectedTypes, actualTypes);
  }

  @Test
  public void testGetIndex() {
    TypeVariable<?>[] typeVariables = Integer.class.getTypeParameters();
    TypeVariable<?> fieldTypeVariable = new TypeVariable<Integer>("I") {}.getType();

    int index = TypeInfoFactory.getIndex(typeVariables, fieldTypeVariable);
    assertEquals(0, index);
  }

  @Test(expected = IllegalStateException.class)
  public void testGetIndex_UnknownTypeVariable() {
    TypeVariable<?>[] typeVariables = Integer.class.getTypeParameters();
    TypeVariable<?> unknownTypeVariable = new TypeVariable<String>("S") {}.getType();

    TypeInfoFactory.getIndex(typeVariables, unknownTypeVariable);
  }
}