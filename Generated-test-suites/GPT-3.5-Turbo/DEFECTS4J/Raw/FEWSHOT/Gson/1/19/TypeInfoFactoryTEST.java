package com.google.gson;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.lang.reflect.WildcardType;

import org.junit.Test;

public class TypeInfoFactoryTest {
    
    @Test
    public void testGetTypeForArray() {
        Type type = String[].class;
        TypeInfoArray actual = TypeInfoFactory.getTypeInfoForArray(type);
        assertEquals(type, actual.getType());
    }

    @Test
    public void testGetTypeForField_ClassType() throws NoSuchFieldException {
        Field field = TestClass.class.getDeclaredField("stringField");
        Type type = field.getGenericType();
        ParameterizedType parameterizedType = (ParameterizedType) type;
        TypeInfo expected = new TypeInfo(parameterizedType.getActualTypeArguments()[0]);
        TypeInfo actual = TypeInfoFactory.getTypeInfoForField(field, TestClass.class);
        assertEquals(expected, actual);
    }

    @Test
    public void testGetTypeForField_ParameterizedType() throws NoSuchFieldException {
        Field field = TestClass.class.getDeclaredField("listField");
        Type type = field.getGenericType();
        TypeInfo expected = new TypeInfo(((ParameterizedType) type).getActualTypeArguments()[0]);
        TypeInfo actual = TypeInfoFactory.getTypeInfoForField(field, TestClass.class);
        assertEquals(expected, actual);
    }

    @Test
    public void testGetTypeForField_TypeVariable() throws NoSuchFieldException {
        Field field = TestClass.class.getDeclaredField("typeVariableField");
        Type type = field.getGenericType();
        TypeInfo expected = new TypeInfo(((TypeVariable<?>) type).getBounds()[0]);
        TypeInfo actual = TypeInfoFactory.getTypeInfoForField(field, TestClass.class);
        assertEquals(expected, actual);
    }

    @Test
    public void testGetTypeForField_WildcardType() throws NoSuchFieldException {
        Field field = TestClass.class.getDeclaredField("wildcardTypeField");
        Type type = field.getGenericType();
        TypeInfo expected = new TypeInfo(((WildcardType) type).getUpperBounds()[0]);
        TypeInfo actual = TypeInfoFactory.getTypeInfoForField(field, TestClass.class);
        assertEquals(expected, actual);
    }

    private class TestClass {
        private String stringField;
        private List<String> listField;
        private T typeVariableField;
        private List<? extends Number> wildcardTypeField;
    }
}