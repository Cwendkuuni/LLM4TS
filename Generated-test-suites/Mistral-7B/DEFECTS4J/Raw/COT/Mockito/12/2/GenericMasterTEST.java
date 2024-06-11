package org.mockito.internal.util.reflection.test;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.internal.util.reflection.GenericMaster;

import java.lang.reflect.Field;

public class GenericMasterTest {

    @Test
    public void testGetGenericTypeWithGenericField() throws NoSuchFieldException {
        Class clazz = TestClassWithGenericField.class;
        Field field = clazz.getDeclaredField("genericField");
        GenericMaster genericMaster = new GenericMaster();
        Class expectedType = String.class;
        Class actualType = genericMaster.getGenericType(field);
        Assert.assertEquals(expectedType, actualType);
    }

    @Test
    public void testGetGenericTypeWithNonGenericField() throws NoSuchFieldException {
        Class clazz = TestClassWithNonGenericField.class;
        Field field = clazz.getDeclaredField("nonGenericField");
        GenericMaster genericMaster = new GenericMaster();
        Class expectedType = Object.class;
        Class actualType = genericMaster.getGenericType(field);
        Assert.assertEquals(expectedType, actualType);
    }

    @Test(expected = NoSuchFieldException.class)
    public void testGetGenericTypeWithNonExistentField() throws NoSuchFieldException {
        Class clazz = TestClassWithGenericField.class;
        Field nonExistentField = clazz.getDeclaredField("nonExistentField");
        GenericMaster genericMaster = new GenericMaster();
        genericMaster.getGenericType(nonExistentField);
    }

    @Test
    public void testGetGenericTypeWithParameterizedTypeButNoActualTypeArguments() throws NoSuchFieldException {
        Class clazz = TestClassWithParameterizedTypeButNoActualTypeArguments.class;
        Field field = clazz.getDeclaredField("field");
        GenericMaster genericMaster = new GenericMaster();
        Class expectedType = Object.class;
        Class actualType = genericMaster.getGenericType(field);
        Assert.assertEquals(expectedType, actualType);
    }

    @Test
    public void testGetGenericTypeWithNestedGenerics() throws NoSuchFieldException, IllegalAccessException {
        Class clazz = TestClassWithNestedGenerics.class;
        Field field = clazz.getDeclaredField("field");
        GenericMaster genericMaster = new GenericMaster();
        Class expectedType = String.class;
        Class actualType = genericMaster.getGenericType(field);
        Assert.assertEquals(expectedType, actualType);
    }

    static class TestClassWithGenericField {
        @SuppressWarnings("unused")
        private final List<String> genericField;

        public TestClassWithGenericField(List<String> genericField) {
            this.genericField = genericField;
        }
    }

    static class TestClassWithNonGenericField {
        @SuppressWarnings("unused")
        private final String nonGenericField;

        public TestClassWithNonGenericField(String nonGenericField) {
            this.nonGenericField = nonGenericField;
        }
    }

    static class TestClassWithParameterizedTypeButNoActualTypeArguments {
        @SuppressWarnings("unused")
        private final Map<String, List<String>> field;

        public TestClassWithParameterizedTypeButNoActualTypeArguments(Map<String, List<String>> field) {
            this.field = field;
        }
    }

    static class TestClassWithNestedGenerics {
        @SuppressWarnings("unused")
        private final Map<List<Integer>, List<String>> field;

        public TestClassWithNestedGenerics(Map<List<Integer>, List<String>> field) {
            this.field = field;
        }
    }
}