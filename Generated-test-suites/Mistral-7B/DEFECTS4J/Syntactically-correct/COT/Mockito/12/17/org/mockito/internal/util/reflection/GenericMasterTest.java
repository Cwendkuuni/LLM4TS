package org.mockito.internal.util.reflection.test;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.internal.util.reflection.GenericMaster;

import java.lang.reflect.Field;

public class GenericMasterTest {

    @Test
    public void testGetGenericType() {
        try {
            Class clazz = TestClass.class;
            Field field = clazz.getDeclaredField("fieldA");
            field.setAccessible(true);

            Class expectedType = String.class;
            Class actualType = new GenericMaster().getGenericType(field);

            Assert.assertEquals(expectedType, actualType);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            // handle exceptions
        }
    }

    @Test
    public void testGetGenericType_FieldIsNotGeneric() {
        try {
            Class clazz = TestClass.class;
            Field field = clazz.getDeclaredField("fieldB");
            field.setAccessible(true);

            Class actualType = new GenericMaster().getGenericType(field);

            Assert.assertEquals(Object.class, actualType);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            // handle exceptions
        }
    }

    @Test(expected = NullPointerException.class)
    public void testGetGenericType_NullField() {
        new GenericMaster().getGenericType(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetGenericType_InvalidField() {
        try {
            Class clazz = TestClass.class;
            Field field = new Field("invalidFieldName") {
                public Type getGenericType() {
                    return null;
                }
            };
            field.setAccessible(true);

            new GenericMaster().getGenericType(field);
        } catch (IllegalAccessException e) {
            // handle exceptions
        }
    }

    static class TestClass {
        private String fieldA;
        private int fieldB;
    }

}