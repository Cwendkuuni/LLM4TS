package org.mockito.internal.util.reflection.test;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.internal.util.reflection.GenericMaster;

import java.lang.reflect.Field;

public class GenericMasterTest {

    @Test
    public void testGetGenericType() {
        GenericMaster genericMaster = new GenericMaster();

        // Test with a generic field
        try {
            Field field = SomeClassWithGenericField.class.getDeclaredField("myField");
            field.setAccessible(true);
            Class expectedType = String.class;
            Class actualType = genericMaster.getGenericType(field);
            Assert.assertEquals(expectedType, actualType);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }

        // Test with a non-generic field
        try {
            Field field = SomeClassWithNonGenericField.class.getDeclaredField("myField");
            field.setAccessible(true);
            Class expectedType = Object.class;
            Class actualType = genericMaster.getGenericType(field);
            Assert.assertEquals(expectedType, actualType);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }

        // Test with null field
        Assert.assertNull(genericMaster.getGenericType(null));
    }

    // Add more test cases for edge cases and exception handling scenarios here

    // Nested generic type test case
    static class NestedGenerics {
        private List<Map<String, List<Integer>>> nestedList;

        public List<Map<String, List<Integer>>> getNestedList() {
            return nestedList;
        }
    }

    @Test
    public void testGetGenericTypeNested() {
        try {
            Field field = NestedGenerics.class.getDeclaredField("nestedList");
            field.setAccessible(true);
            Class expectedType = List.class;
            Type genericType = field.getGenericType();
            Assert.assertTrue(genericType instanceof ParameterizedType);
            Type typeArg1 = ((ParameterizedType) genericType).getActualTypeArguments()[0];
            Assert.assertTrue(typeArg1 instanceof ParameterizedType);
            Type typeArg2 = ((ParameterizedType) typeArg1).getActualTypeArguments()[0];
            Class actualType = (Class) typeArg2;
            Assert.assertEquals(expectedType, actualType);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }
}