package com.google.gson;

import static com.google.common.base.Preconditions.checkArgument;
import static org.junit.Assert.assertInstanceOf;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.lang.reflect.Field;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.lang.reflect.WildcardType;

import org.junit.Test;

public class TypeInfoFactoryTest {

  private static final Class<?> RAW_PARENT_CLASS = Object.class;
  private static final Type PARENT_TYPE = new ParameterizedTypeImpl(Object.class, new Type[] {}, null);

  @Test
  public void testGetTypeInfoForArray() {
    Type arrayType = new GenericArrayTypeImpl(Integer.TYPE);
    TypeInfo typeInfo = TypeInfoFactory.getTypeInfoForArray(arrayType);
    assertEquals(Integer.TYPE, typeInfo.getType());
  }

  @Test
  public void testGetTypeInfoForField_ClassType() {
    Field field = mock(Field.class);
    Class<?> fieldType = String.class;
    when(field.getType()).thenReturn(fieldType);
    Type parentType = PARENT_TYPE;
    TypeInfo typeInfo = TypeInfoFactory.getTypeInfoForField(field, parentType);
    assertEquals(fieldType, typeInfo.getType());
  }

  @Test
  public void testGetTypeInfoForField_ParameterizedType() {
    Field field = mock(Field.class);
    Class<?> rawClassDefiningF = Object.class;
    Type fieldType = new ParameterizedTypeImpl(List.class, new Type[] { Integer.TYPE }, null);
    when(field.getType()).thenReturn(fieldType);
    Type parentType = PARENT_TYPE;
    TypeInfo typeInfo = TypeInfoFactory.getTypeInfoForField(field, parentType);
    assertEquals(List.class, typeInfo.getType());
  }

  @Test
  public void testGetTypeInfoForField_TypeVariable() {
    Field field = mock(Field.class);
    TypeVariable<Integer> fieldTypeVariable = mock(TypeVariable.class);
    when(field.getType()).thenReturn(fieldTypeVariable);
    Type parentType = new ParameterizedTypeImpl(List.class, new Type[] { new TypeVariable<Integer>() {} }, null);
    int index = 0;
    Type[] actualTypeArguments = new Type[] { Integer.TYPE };
    Type actualType = actualTypeArguments[index];
    TypeInfo typeInfo = TypeInfoFactory.getTypeInfoForField(field, parentType);
    assertEquals(Integer.TYPE, typeInfo.getType());
  }

  @Test(expected = UnsupportedOperationException.class)
  public void testGetTypeInfoForField_UnknownType() {
    Field field = mock(Field.class);
    Type parentType = PARENT_TYPE;
    Type typeToEvaluate = new TypeVariable<List<String>>() {};
    TypeInfoFactory.getTypeInfoForField(field, parentType, RAW_PARENT_CLASS);
  }

  @Test
  public void testGetActualType_ClassType() {
    Type typeToEvaluate = String.class;
    assertInstanceOf(Class.class, TypeInfoFactory.getActualType(typeToEvaluate, null, RAW_PARENT_CLASS));
  }

  @Test
  public void testGetActualType_ParameterizedType() {
    Type parentType = new ParameterizedTypeImpl(Map.class, new Type[] { String.class, Integer.TYPE }, null);
    Type typeToEvaluate = new ParameterizedTypeImpl(Map.class, new Type[] { String.class, Double.TYPE }, null);
    assertEquals(Map.class, TypeInfoFactory.getActualType(typeToEvaluate, parentType, RAW_PARENT_CLASS).getType());
  }

  @Test
  public void testGetActualType_GenericArrayType() {
    Type parentType = new ParameterizedTypeImpl(List.class, new Type[] { Integer.TYPE }, null);
    Type typeToEvaluate = new GenericArrayTypeImpl(Integer.TYPE);
    Type actualType = TypeInfoFactory.getActualType(typeToEvaluate, parentType, RAW_PARENT_CLASS);
    assertTrue(actualType instanceof GenericArrayType);
    assertEquals(Integer.TYPE, ((GenericArrayType) actualType).getGenericComponentType());
  }

  @Test
  public void testGetActualType_TypeVariable() {
    Type parentType = new ParameterizedTypeImpl(List.class, new Type[] { new TypeVariable<Integer>() {} }, null);
    Type typeToEvaluate = new TypeVariable<Integer>() {};
    assertEquals(Integer.TYPE, TypeInfoFactory.getActualType(typeToEvaluate, parentType, RAW_PARENT_CLASS).getType());
  }

  @Test(expected = UnsupportedOperationException.class)
  public void testGetActualType_UnknownType() {
    Type parentType = null;
    Type typeToEvaluate = new TypeVariable<List<String>>() {};
    TypeInfoFactory.getActualType(typeToEvaluate, parentType, RAW_PARENT_CLASS);
  }

  @Test
  public void testGetActualType_WildcardType() {
    Type parentType = new WildcardTypeImpl(null, JavaClass.getPrimitiveType(int.class).getComponentType());
    Type typeToEvaluate = new WildcardTypeImpl(null, JavaClass.getPrimitiveType(int.class).getComponentType());
    assertEquals(JavaClass.getPrimitiveType(int.class).getComponentType(), TypeInfoFactory.getActualType(typeToEvaluate, parentType, RAW_PARENT_CLASS).getType());
  }

  @Test
  public void testExtractRealTypes_EmptyArray() {
    Type[] actualTypeArguments = new Type[0];
    Type[] extractedTypes = TypeInfoFactory.extractRealTypes(actualTypeArguments, null, RAW_PARENT_CLASS);
    assertNull(extractedTypes);
  }

  @Test
  public void testExtractRealTypes_SingleElementArray() {
    Type[] actualTypeArguments = new Type[] { Integer.TYPE };
    Type[] extractedTypes = TypeInfoFactory.extractRealTypes(actualTypeArguments, null, RAW_PARENT_CLASS);
    assertEquals(1, extractedTypes.length);
    assertEquals(Integer.TYPE, extractedTypes[0]);
  }

  @Test
  public void testExtractRealTypes_MultipleElementArray() {
    Type[] actualTypeArguments = new Type[] { Integer.TYPE, Double.TYPE };
    Type[] extractedTypes = TypeInfoFactory.extractRealTypes(actualTypeArguments, null, RAW_PARENT_CLASS);
    assertEquals(2, extractedTypes.length);
    assertEquals(Integer.TYPE, extractedTypes[0]);
    assertEquals(Double.TYPE, extractedTypes[1]);
  }
}