
package com.google.gson;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.lang.reflect.Field;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.lang.reflect.WildcardType;

import org.junit.Before;
import org.junit.Test;

public class TypeInfoFactoryTest {

    private Field field;
    private Type typeDefiningF;
    private Class<?> classDefiningF;

    @Before
    public void setUp() throws NoSuchFieldException {
        field = TypeInfoFactory.class.getDeclaredField("field");
        typeDefiningF = field.getGenericType();
        classDefiningF = TypeInfoFactory.class;
    }

    @Test
    public void testGetTypeInfoForArray() {
        Type type = new GenericArrayTypeImpl(Integer.TYPE);
        TypeInfoArray expected = new TypeInfoArray(type);
        TypeInfoArray actual = TypeInfoFactory.getTypeInfoForArray(type);
        assertEquals(expected, actual);
    }

    @Test
    public void testGetTypeInfoForField() {
        TypeInfo expected = new TypeInfo(Integer.TYPE);
        TypeInfo actual = TypeInfoFactory.getTypeInfoForField(field, typeDefiningF);
        assertEquals(expected, actual);
    }

    @Test
    public void testGetActualType_Class() {
        Type type = Integer.TYPE;
        TypeInfo expected = new TypeInfo(type);
        TypeInfo actual = TypeInfoFactory.getActualType(type, null, null);
        assertEquals(expected, actual);
    }

    @Test
    public void testGetActualType_ParameterizedType() {
        Type owner = TypeInfoFactory.class;
        Type rawType = ParameterizedTypeImpl.class;
        Type[] actualTypeParameters = {Integer.TYPE};
        Type type = new ParameterizedTypeImpl(rawType, actualTypeParameters, owner);
        TypeInfo expected = new TypeInfo(type);
        TypeInfo actual = TypeInfoFactory.getActualType(type, null, null);
        assertEquals(expected, actual);
    }

    @Test
    public void testGetActualType_GenericArrayType() {
        Type componentType = Integer.TYPE;
        Type type = new GenericArrayTypeImpl(componentType);
        TypeInfo expected = new TypeInfo(type);
        TypeInfo actual = TypeInfoFactory.getActualType(type, null, null);
        assertEquals(expected, actual);
    }

    @Test
    public void testGetActualType_TypeVariable_ParameterizedType() {
        Type rawType = TypeInfoFactory.class;
        TypeVariable<?> fieldTypeVariable = TypeInfoFactory.class.getTypeParameters()[0];
        Type[] actualTypeArguments = {Integer.TYPE};
        ParameterizedType parentType = new ParameterizedTypeImpl(rawType, actualTypeArguments, null);
        TypeVariable<?>[] classTypeVariables = rawType.getTypeParameters();
        int indexOfActualTypeArgument = 0;
        Type type = actualTypeArguments[indexOfActualTypeArgument];
        TypeInfo expected = new TypeInfo(type);
        TypeInfo actual = TypeInfoFactory.getActualType(fieldTypeVariable, parentType, rawType);
        assertEquals(expected, actual);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetActualType_TypeVariable_NotParameterizedType() {
        Type type = TypeInfoFactory.class.getTypeParameters()[0];
        TypeInfoFactory.getActualType(type, null, null);
    }

    @Test
    public void testGetActualType_WildcardType() {
        WildcardType wildcardType = new WildcardTypeImpl(new Type[]{Integer.TYPE}, new Type[]{});
        TypeInfo expected = new TypeInfo(Integer.TYPE);
        TypeInfo actual = TypeInfoFactory.getActualType(wildcardType, null, null);
        assertEquals(expected, actual);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetActualType_InvalidType() {
        TypeInfoFactory.getActualType(String.class, null, null);
    }

    @Test
    public void testExtractRealTypes() {
        Type[] actualTypeArguments = {Integer.TYPE};
        Type parentType = TypeInfoFactory.class;
        Class<?> rawParentClass = TypeInfoFactory.class;
        Type[] expected = {Integer.TYPE};
        Type[] actual = TypeInfoFactory.extractRealTypes(actualTypeArguments, parentType, rawParentClass);
        assertArrayEquals(expected, actual);
    }

    @Test
    public void testGetIndex() {
        TypeVariable<?>[] types = TypeInfoFactory.class.getTypeParameters();
        TypeVariable<?> type = TypeInfoFactory.class.getTypeParameters()[0];
        int expected = 0;
        int actual = TypeInfoFactory.getIndex(types, type);
        assertEquals(expected, actual);
    }
}
