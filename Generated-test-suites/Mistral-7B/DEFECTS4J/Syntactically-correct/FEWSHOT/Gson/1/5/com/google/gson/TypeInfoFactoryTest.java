package com.google.gson;

import static com.google.common.base.Preconditions.checkArgument;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

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
    Type intTypeArray = int[].class.getComponentType();
    TypeInfo actualTypeInfo = TypeInfoFactory.getTypeInfoForArray(intTypeArray);
    assertEquals(TypeInfo.ARRAY, actualTypeInfo.getType());
    assertEquals(intTypeArray, actualTypeInfo.getTypeValue());
  }

  @Test
  public void testTypeInfoForField_Class() {
    // Test for a field of a primitive type.
    Class<Integer> intClass = Integer.class;
    int field = 42;
    Field fieldObj = intClass.getField("field");
    fieldObj.setAccessible(true);
    TypeInfo actualTypeInfo = TypeInfoFactory.getTypeInfoForField(fieldObj, intClass);
    assertEquals(TypeInfo.PRIMITIVE, actualTypeInfo.getType());
    assertEquals(int.class, actualTypeInfo.getTypeValue());

    // Test for a field of a reference type.
    class OuterClass {
      int field;
    }
    Class<OuterClass> outerClass = OuterClass.class;
    Field fieldObj = outerClass.getField("field");
    fieldObj.setAccessible(true);
    actualTypeInfo = TypeInfoFactory.getTypeInfoForField(fieldObj, outerClass);
    assertEquals(TypeInfo.REFERENCE, actualTypeInfo.getType());
    assertEquals(int.class, actualTypeInfo.getTypeValue());
  }

  @Test
  public void testTypeInfoForField_ParameterizedType() {
    // Test for a field of a generic type; the field type is a raw type.
    class Foo<T> {
      T field;
    }
    class Bar extends Foo<Integer> {
    }
    Field fieldObj = Bar.class.getField("field");
    fieldObj.setAccessible(true);
    TypeInfo actualTypeInfo = TypeInfoFactory.getTypeInfoForField(fieldObj, Bar.class);
    assertEquals(TypeInfo.REFERENCE, actualTypeInfo.getType());
    assertEquals(Integer.class, actualTypeInfo.getTypeValue());

    // Test for a field of a generic type; the field type is parameterized.
    class Foo<T> {
      T field;
    }
    class Bar<U> extends Foo<U> {
    }
    Type genericSuperClass = Foo.class.getGenericSuperclass();
    Type typeVariable = ((ParameterizedType) genericSuperclass).getActualTypeArguments()[0];
    typeVariable = ((TypeVariable<?>) typeVariable).getGenericDeclaration();
    Field fieldObj2 = Bar.class.getField("field");
    fieldObj2.setAccessible(true);
    actualTypeInfo = TypeInfoFactory.getTypeInfoForField(fieldObj2, Bar.class);
    assertEquals(TypeInfo.REFERENCE, actualTypeInfo.getType());
    assertEquals(typeVariable, actualTypeInfo.getTypeValue());
  }

  @Test
  public void testTypeInfoForField_TypeVariable() {
    // Test for a field of a generic type with a type variable.
    class Foo<T> {
      T field;
    }
    class Bar extends Foo<String> {
    }
    Field fieldObj = Bar.class.getField("field");
    fieldObj.setAccessible(true);
    TypeInfo actualTypeInfo = TypeInfoFactory.getTypeInfoForField(fieldObj, Bar.class);
    assertEquals(TypeInfo.TYPE_VARIABLE, actualTypeInfo.getType());
    assertEquals(String.class, actualTypeInfo.getTypeValue());
  }

  @Test
  public void testTypeInfoForField_WildcardType() {
    // Test for a field of a generic type with a wildcard type.
    class Foo<T> {
      List<T> list;
    }
    class Bar extends Foo<String> {
    }
    Field fieldObj = Bar.class.getField("list");
    fieldObj.setAccessible(true);
    TypeInfo actualTypeInfo = TypeInfoFactory.getTypeInfoForField(fieldObj, Bar.class);
    assertEquals(TypeInfo.REFERENCE, actualTypeInfo.getType());
    assertEquals(List.class, actualTypeInfo.getTypeValue());
    assertEquals(WildcardType.class, actualTypeInfo.getRawType());
  }

  @Test
  public void testTypeInfoForField_GenericArrayType() {
    // Test for a field of a generic type with a generic array.
    class Foo<T> {
      T[] array;
    }
    class Bar extends Foo<String> {
    }
    Field fieldObj = Bar.class.getField("array");
    fieldObj.setAccessible(true);
    TypeInfo actualTypeInfo = TypeInfoFactory.getTypeInfoForField(fieldObj, Bar.class);
    assertEquals(TypeInfo.ARRAY, actualTypeInfo.getType());
    assertEquals(String[].class, actualTypeInfo.getTypeValue());

    // Test for a field of a generic type with a
    // nested generic array.
    class Foo<T> {
      T[][] array;
    }
    class Bar extends Foo<String> {
    }
    fieldObj = Bar.class.getField("array");
    fieldObj.setAccessible(true);
    actualTypeInfo = TypeInfoFactory.getTypeInfoForField(fieldObj, Bar.class);
    assertEquals(TypeInfo.ARRAY, actualTypeInfo.getType());
    assertEquals(String[][].class, actualTypeInfo.getTypeValue());
  }

  @Test
  public void testTypeInfoForField_ArrayType() {
    // Test for a field of a generic type with an array type.
    class Foo<T> {
      T[] array;
    }
    class Bar extends Foo<Integer> {
    }
    Field fieldObj = Bar.class.getField("array");
    fieldObj.setAccessible(true);
    TypeInfo actualTypeInfo = TypeInfoFactory.getTypeInfoForField(fieldObj, Bar.class);
    assertEquals(TypeInfo.ARRAY, actualTypeInfo.getType());
    assertEquals(int[].class, actualTypeInfo.getTypeValue());
  }

  @Test
  public void testGetActualType_Class() {
    // Test with a class type.
    assertEquals(String.class, TypeInfoFactory.getActualType(String.class, null, null));
  }

  @Test
  public void testGetActualType_ParameterizedType() {
    // Test with a parameterized type.
    class Foo<T> {
    }
    Type fooParameterizedType = new ParameterizedTypeImpl(Foo.class, new Type[] {String.class});
    checkArgument(fooParameterizedType != null);
    assertEquals(Foo.class, TypeInfoFactory.getActualType(fooParameterizedType, null, null));
  }

  @Test
  public void testGetActualType_GenericArrayType() {
    // Test with a generic array type.
    class Foo<T> {
    }
    Type fooGenericArrayType = new GenericArrayTypeImpl(new ParameterizedTypeImpl(Foo.class, new Type[] {Integer.class}));
    checkArgument(fooGenericArrayType != null);
    assertEquals(Foo[].class, TypeInfoFactory.getActualType(fooGenericArrayType, null, null));
  }

  @Test
  public void testGetActualType_TypeVariable() {
    // Test with a type variable.
    class Foo<T> {
    }
    TypeVariable<T> fooTypeVariable = Foo.class.getTypeParameters()[0];
    assertEquals(fooTypeVariable, TypeInfoFactory.getActualType(fooTypeVariable, null, null));
  }

  @Test
  public void testGetActualType_WildcardType() {
    // Test with a wildcard type.
    class Foo<T> {
    }
    Type wildcardType = new WildcardTypeImpl(null, new Type[] {Object.class, Object.class});
    assertEquals(wildcardType, TypeInfoFactory.getActualType(wildcardType, null, null));
  }

  @Test
  public void testGetActualType_Null() {
    // Test with null.
    assertNull(TypeInfoFactory.getActualType(null, null, null));
  }

  @Test
  public void testExtractRealTypes_EmptyArray() {
    // Test with an empty array.
    Type[] types = new Type[0];
    Type[] retTypes = TypeInfoFactory.extractRealTypes(types, null, null);
    assertArrayEquals(new Type[0], retTypes);
  }

  @Test
  public void testExtractRealTypes_SingleType() {
    // Test with a single type.
    Type intType = int.class;
    Type[] types = new Type[] {intType};
    Type[] retTypes = TypeInfoFactory.extractRealTypes(types, null, null);
    assertEquals(intType, retTypes[0]);
  }

  @Test
  public void testExtractRealTypes_ParameterizedType() {
    // Test with a parameterized type.
    class Foo<T> {
    }
    Type fooParameterizedType = new ParameterizedTypeImpl(Foo.class, new Type[] {String.class});
    Type[] types = new Type[] {fooParameterizedType};
    Type[] retTypes = TypeInfoFactory.extractRealTypes(types, null, null);
    assertEquals(Foo.class, retTypes[0].getRawType());
    assertEquals(String.class, retTypes[0].getActualTypeArguments()[0]);
  }

  @Test
  public void testExtractRealTypes_GenericArrayType() {
    // Test with a generic array type.
    class Foo<T> {
    }
    Type fooGenericArrayType = new GenericArrayTypeImpl(new ParameterizedTypeImpl(Foo.class, new Type[] {Integer.class}));
    Type[] types = new Type[] {fooGenericArrayType};
    Type[] retTypes = TypeInfoFactory.extractRealTypes(types, null, null);
    assertEquals(Foo[].class, retTypes[0].getRawType());
    assertEquals(Integer.class, retTypes[0].getActualTypeArguments()[0]);
  }

  @Test
  public void testExtractRealTypes_TypeVariable() {
    // Test with a type variable.
    class Foo<T> {
    }
    TypeVariable<T> fooTypeVariable = Foo.class.getTypeParameters()[0];
    Type[] types = new Type[] {fooTypeVariable};
    Type[] retTypes = TypeInfoFactory.extractRealTypes(types, null, null);
    assertEquals(fooTypeVariable, retTypes[0]);
  }

  @Test
  public void testExtractRealTypes_WildcardType() {
    // Test with a wildcard type.
    class Foo<T> {
    }
    Type wildcardType = new WildcardTypeImpl(null, new Type[] {Object.class, Object.class});
    Type[] types = new Type[] {wildcardType};
    Type[] retTypes = TypeInfoFactory.extractRealTypes(types, null, null);
    assertEquals(wildcardType, retTypes[0]);
  }
}