package com.google.gson;

import static com.google.common.base.Preconditions.checkArgument;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertInstanceOf;
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
    Type type = new int[5].getClass().getComponentType();
    TypeInfo expected = new TypeInfoArray(type);
    TypeInfo actual = TypeInfoFactory.getInstanceForArray(type);
    assertEquals(expected, actual);
  }

  @Test
  public void testTypeInfoForField_ParameterizedType() {
    class Foo<A> {
      A a;
    }

    Foo<Integer> foo = new Foo<>();
    Field field = Foo.class.getField("a");
    Class<?> rawType = TypeUtils.toRawClass(foo.getClass());
    Type type = field.getGenericType();
    Type parentType = Foo.class;
    TypeInfo expected = new TypeInfo(TypeVariable.class, "A");
    TypeInfo actual = TypeInfoFactory.getInstanceForField(field, type, parentType, rawType);
    assertEquals(expected.getType(), actual.getType());
  }

  @Test
  public void testTypeInfoForField_ParameterizedTypeWithActualType() {
    class Foo<A> {
      A a;
    }

    Foo<Integer> foo = new Foo<>();
    Field field = Foo.class.getField("a");
    Class<?> rawType = TypeUtils.toRawClass(foo.getClass());
    Type type = field.getGenericType();
    Type parentType = Foo.class;
    TypeInfo expected = new TypeInfo(Integer.TYPE);
    TypeInfo actual = TypeInfoFactory.getInstanceForField(field, type, parentType, rawType);
    assertEquals(expected.getType(), actual.getType());
  }

  @Test
  public void testTypeInfoForField_Class() {
    class Foo {
      int a;
    }

    Foo foo = new Foo();
    Field field = Foo.class.getField("a");
    Class<?> rawType = TypeUtils.toRawClass(foo.getClass());
    Type type = field.getGenericType();
    if (type != null) {
      throw new IllegalStateException("Expected a class type, but got: " + type);
    }
    TypeInfo expected = new TypeInfo(Integer.TYPE);
    TypeInfo actual = TypeInfoFactory.getInstanceForField(field, type, parentType, rawType);
    assertEquals(expected.getType(), actual.getType());
  }

  @Test
  public void testTypeInfoForField_TypeVariable() {
    class Foo<A> {
      A a;
    }

    Foo<String> foo = new Foo<>();
    Field field = Foo.class.getField("a");
    Class<?> rawType = TypeUtils.toRawClass(foo.getClass());
    Type type = field.getGenericType();
    Type parentType = Foo.class;
    TypeVariable<?> fieldTypeVariable = (TypeVariable<?>) type;
    TypeVariable<?>[] classTypeVariables = rawType.getTypeParameters();
    ParameterizedType objParameterizedType = (ParameterizedType) parentType;
    int indexOfActualTypeArgument = TypeInfoFactory.getIndex(classTypeVariables, fieldTypeVariable);
    Type[] actualTypeArguments = objParameterizedType.getActualTypeArguments();
    Type actual = actualTypeArguments[indexOfActualTypeArgument];
    TypeInfo expected = new TypeInfo(actual);
    TypeInfo actualTypeInfo = TypeInfoFactory.getInstanceForField(field, type, parentType, rawType);
    assertEquals(expected, actualTypeInfo);
  }

  @Test
  public void testTypeInfoForField_WildcardType() {
    class Foo {
      List<? extends Number> numbers;
    }

    Foo foo = new Foo();
    Field field = Foo.class.getField("numbers");
    Class<?> rawType = TypeUtils.toRawClass(foo.getClass());
    Type type = field.getGenericType();
    Type parentType = Foo.class;
    WildcardType castedType = (WildcardType) type;
    Type upperBound = castedType.getUpperBounds()[0];
    TypeInfo expected = new TypeInfo(Number.class);
    TypeInfo actual = TypeInfoFactory.getInstanceForField(field, type, parentType, rawType);
    assertEquals(expected, actual);
  }

  @Test
  public void testTypeInfoForField_GenericArrayType() {
    class Foo {
      String[] strings;
    }

    Foo foo = new Foo();
    Field field = Foo.class.getField("strings");
    Class<?> rawType = TypeUtils.toRawClass(foo.getClass());
    Type type = field.getGenericType();
    if (type == null) {
      // Java 8 and later
      type = field.getType().getComponentType();
    }
    Type parentType = Foo.class;
    TypeInfo expected =
        new TypeInfoArray(TypeUtils.wrapWithArray(TypeUtils.toRawClass(String.class)));
    TypeInfo actual = TypeInfoFactory.getInstanceForField(field, type, parentType, rawType);
    assertEquals(expected, actual);
  }

  @Test
  public void testTypeInfoForField_ArrayType() {
    class Foo {
      String[] strings;
    }

    Foo foo = new Foo();
    Field field = Foo.class.getField("strings");
    Class<?> rawType = TypeUtils.toRawClass(foo.getClass());
    Type type = field.getGenericType();
    if (type != null) {
      throw new IllegalStateException("Expected a raw array type, but got: " + type);
    }
    Type parentType = Foo.class;
    TypeInfo expected =
        new TypeInfoArray(TypeUtils.wrapWithArray(TypeUtils.toRawClass(String.class)));
    TypeInfo actual = TypeInfoFactory.getInstanceForField(field, type, parentType, rawType);
    assertEquals(expected, actual);
  }

  @Test
  public void testGetActualType_Class() {
    Type type = Integer.TYPE;
    Type actual = TypeInfoFactory.getActualType(type, null, null);
    assertEquals(type, actual);
  }

  @Test
  public void testGetActualType_ParameterizedType() {
    class Foo<A> {
      A a;
    }

    Foo<Integer> foo = new Foo<>();
    Field field = Foo.class.getField("a");
    Class<?> rawType = TypeUtils.toRawClass(foo.getClass());
    Type type = field.getGenericType();
    Type parentType = Foo.class;
    Type actual = TypeInfoFactory.getActualType(type, parentType, rawType);
    assertEquals(Integer.TYPE, actual);
  }

  @Test
  public void testGetActualType_GenericArrayType() {
    Type type = new int[5].getClass().getComponentType();
    Type actual = TypeInfoFactory.getActualType(type, null, null);
    assertEquals(Integer.TYPE, actual);
  }

  @Test
  public void testGetActualType_TypeVariable() {
    class Foo<A> {
      A a;
    }

    Foo<Integer> foo = new Foo<>();
    Field field = Foo.class.getField("a");
    Class<?> rawType = TypeUtils.toRawClass(foo.getClass());
    Type type = field.getGenericType();
    TypeVariable<?> fieldTypeVariable = (TypeVariable<?>) type;
    TypeVariable<?>[] classTypeVariables = rawType.getTypeParameters();
    ParameterizedType objParameterizedType = (ParameterizedType) parentType;
    int indexOfActualTypeArgument = TypeInfoFactory.getIndex(classTypeVariables, fieldTypeVariable);
    Type[] actualTypeArguments = objParameterizedType.getActualTypeArguments();
    Type actual = actualTypeArguments[indexOfActualTypeArgument];
    Type expected = Integer.TYPE;
    Type actualType = TypeInfoFactory.getActualType(type, parentType, rawType);
    assertEquals(expected, actual);
  }

  @Test
  public void testGetActualType_WildcardType() {
    class Foo {
      List<? extends Number> numbers;
    }

    Foo foo = new Foo();
    Field field = Foo.class.getField("numbers");
    Class<?> rawType = TypeUtils.toRawClass(foo.getClass());
    Type type = field.getGenericType();
    WildcardType castedType = (WildcardType) type;
    Type upperBound = castedType.getUpperBounds()[0];
    Type expected = Number.class;
    Type actual = TypeInfoFactory.getActualType(type, parentType, rawType);
    assertEquals(expected, actual);
  }

  @Test(expected = UnsupportedOperationException.class)
  public void testGetActualType_UnsupportedType() {
    class Foo {}

    Field field = Foo.class.getField("a");
    Type type = field.getGenericType();
    Type parentType = Foo.class;
    Class<?> rawType = TypeUtils.toRawClass(new Foo());
    Type actual = TypeInfoFactory.getActualType(type, parentType, rawType);
  }

  @Test
  public void testExtractRealTypes() {
    Type[] types = new Type[] { new TypeVariable<Integer>("A"), new TypeVariable<String>("B") };
    Type expected[] = { Integer.TYPE, String.class };
    TypeInfo actual = TypeInfoFactory.getInstanceForArray(types);
    TypeInfo[] extracted = actual.getComponentTypes();
    for (int i = 0; i < extracted.length; ++i) {
      assertEquals(expected[i], extracted[i].getType());
    }
  }

  @Test
  public void testExtractRealTypes_NullParentType() {
    Type[] types = new Type[] { new TypeVariable<Integer>("A"), new TypeVariable<String>("B") };
    TypeInfo actual = TypeInfoFactory.getInstanceForArray(types);
    TypeInfo[] extracted = actual.getComponentTypes();
    for (int i = 0; i < extracted.length; ++i) {
      assertEquals(TypeVariable.class, extracted[i].getType().getClass());
    }
  }

  @Test
  public void testExtractRealTypes_ParameterizedType() {
    class Foo<A> {
      A a;
    }

    Foo<Integer> foo = new Foo<>();
    Type[] expected = { Integer.TYPE };
    Type parentType = Foo.class;
    TypeInfo actual = TypeInfoFactory.getInstanceForField(foo.getClass().getField("a"), null, parentType, null);
    TypeInfo[] extracted = actual.getComponentTypes();
    assertEquals(expected.length, extracted.length);
    for (int i = 0; i < extracted.length; ++i) {
      assertEquals(expected[i], extracted[i].getType());
    }
  }

  @Test
  public void testExtractRealTypes_GenericArrayType() {
    Type type = new int[5].getClass().getComponentType();
    Type parentType = Object.class;
    TypeInfo actual = TypeInfoFactory.getInstanceForArray(type);
    TypeInfo[] extracted = actual.getComponentTypes();
    assertEquals(1, extracted.length);
    assertEquals(Integer.TYPE, extracted[0].getType());
  }

  @Test
  public void testExtractRealTypes_ArrayType() {
    Type type = new int[5].getClass();
    Type parentType = Object.class;
    TypeInfo actual = TypeInfoFactory.getInstanceForArray(type);
    TypeInfo[] extracted = actual.getComponentTypes();
    assertEquals(1, extracted.length);
    assertEquals(Integer.TYPE, extracted[0].getType());
  }

  @Test
  public void testExtractRealTypes_WildcardType() {
    class Foo {
      List<? extends Number> numbers;
    }

    Foo foo = new Foo();
    Type parentType = Foo.class;
    Type type = foo.getClass().getField("numbers").getGenericType();
    WildcardType castedType = (WildcardType) type;
    Type[] expected = { Number.class };
    TypeInfo actual = TypeInfoFactory.getInstanceForField(foo.getClass().getField("numbers"), type, parentType, null);
    TypeInfo[] extracted = actual.getComponentTypes();
    assertEquals(expected.length, extracted.length);
    for (int i = 0; i < extracted.length; ++i) {
      assertEquals(expected[i], extracted[i].getType());
    }
  }

  @Test
  public void testExtractRealTypes_ArrayTypeWithWildcard() {
    class Foo<A> {
      List<A> numbers;
    }

    Foo<Number> foo = new Foo<>();
    Type parentType = Foo.class;
    Type type = foo.getClass().getField("numbers").getGenericType();
    TypeInfo actual = TypeInfoFactory.getInstanceForField(foo.getClass().getField("numbers"), type, parentType, null);
    TypeInfo[] extracted = actual.getComponentTypes();
    assertEquals(1, extracted.length);
    assertEquals(Number.class, extracted[0].getType());
  }

  @Test
  public void testExtractRealTypes_ArrayTypeWithGenericArray() {
    class Foo<A> {
      List<List<A>> numbers;
    }

    Foo<Number> foo = new Foo<>();
    Type parentType = Foo.class;
    Type type = foo.getClass().getField("numbers").getGenericType();
    Type componentType = ((ParameterizedType) type).getActualTypeArguments()[0];
    TypeInfo actual = TypeInfoFactory.getInstanceForField(foo.getClass().getField("numbers"), type, parentType, null);
    TypeInfo[] extracted = actual.getComponentTypes();
    assertEquals(1, extracted.length);
    assertEquals(
        new TypeInfoArray(TypeUtils.wrapWithArray(new TypeInfo(Number.class))), extracted[0]);
  }

  @Test
  public void testExtractRealTypes_ArrayTypeWithArray() {
    class Foo<A> {
      List<List<A[]>> numbers;
    }

    Foo<Number> foo = new Foo<>();
    Type parentType = Foo.class;
    Type type = foo.getClass().getField("numbers").getGenericType();
    Type componentType = ((ParameterizedType) type).getActualTypeArguments()[0];
    Type componentType2 = ((ArrayType) componentType).getComponentType();
    TypeInfo actual = TypeInfoFactory.getInstanceForField(foo.getClass().getField("numbers"), type, parentType, null);
    TypeInfo[] extracted = actual.getComponentTypes();
    assertEquals(1, extracted.length);
    assertEquals(
        new TypeInfoArray(TypeUtils.wrapWithArray(new TypeInfoArray(TypeUtils.wrapWithArray(new TypeInfo(Number.class))))), extracted[0]);
  }
}