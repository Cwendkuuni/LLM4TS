package org.mockito.internal.util.reflection.test;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.internal.util.reflection.GenericMaster;

import java.lang.reflect.Field;

public class GenericMasterTest {

    @Test
    public void testGetGenericType() {
        GenericMaster genericMaster = new GenericMaster();

        // Test with a field that is not generic
        Field field1 = getField(SomeClass.class, "nonGenericField");
        Class expectedType1 = Object.class;
        Class actualType1 = genericMaster.getGenericType(field1);
        Assert.assertEquals(expectedType1, actualType1);

        // Test with a field that is generic and returns the expected type
        Field field2 = getField(SomeGenericClass.class, "genericField");
        Class expectedType2 = String.class;
        Class actualType2 = genericMaster.getGenericType(field2);
        Assert.assertEquals(expectedType2, actualType2);

        // Test with a field that is generic and returns an unexpected type (edge case)
        Field field3 = getField(SomeOtherGenericClass.class, "genericField");
        Class expectedType3 = Integer.class;
        Class actualType3 = genericMaster.getGenericType(field3);
        Assert.assertNotEquals(expectedType3, actualType3);

        // Test with a null field (exception handling)
        Field field4 = null;
        try {
            genericMaster.getGenericType(field4);
            Assert.fail("Expected IllegalAccessException");
        } catch (IllegalAccessException e) {
            // Expected
        }
    }

    private Field getField(Class clazz, String fieldName) {
        try {
            return clazz.getDeclaredField(fieldName);
        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        }
    }

    // SomeClass.java
    static class SomeClass {
        private int nonGenericField;
    }

    // SomeGenericClass.java
    static class SomeGenericClass<T> {
        private T genericField;
    }

    // SomeOtherGenericClass.java
    static class SomeOtherGenericClass<U> {
        private U genericField;
    }
}