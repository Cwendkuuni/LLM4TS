package com.google.gson;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class TypeInfoFactoryTest {

    private TypeInfoFactory.TypeInfoArray typeInfoArray;
    private TypeInfoFactory.TypeInfoField typeInfoField;

    @Before
    public void setUp() {
        typeInfoArray = TypeInfoFactory.getTypeInfoForArray(String[].class);
        typeInfoField = TypeInfoFactory.getTypeInfoForField(TestClass.class.getDeclaredFields()[0], TestClass.class);
    }

    @Test
    public void testGetTypeInfoForArray() {
        assertNotNull(typeInfoArray);
        assertEquals(String[].class, typeInfoArray.getType());
    }

    @Test
    public void testGetTypeInfoForField() {
        assertNotNull(typeInfoField);
        assertEquals(TestClass.class.getDeclaredFields()[0].getGenericType(), typeInfoField.getType());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetTypeInfoForArrayWithInvalidType() {
        TypeInfoFactory.getTypeInfoForArray(TestClass.class);
    }

    @Test(expected = IllegalStateException.class)
    public void testGetTypeInfoForFieldWithInvalidField() {
        TypeInfoFactory.getTypeInfoForField(TestClass.class.getDeclaredFields()[1], TestClass.class);
    }

    private static class TestClass {
        private String[] arr;
    }

}