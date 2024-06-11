package org.junit.experiment.tests.reflection.genericmaster;

import org.junit.Test;
import org.mockito.internal.util.reflection.GenericMaster;
import org.mockito.internal.util.reflection.FieldTestUtil;

import java.lang.reflect.Field;

import static org.junit.Assert.assertEquals;

public class GenericMasterTest {

    @Test
    public void testGetGenericTypeWithGenericField() {
        GenericMaster genericMaster = new GenericMaster();

        Class clazz = TestClassWithGenericField.class;
        Field field = null;
        try {
            field = clazz.getDeclaredField("field");
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
        field.setAccessible(true);

        Class expectedType = String.class;
        Class actualType = genericMaster.getGenericType(field);

        assertEquals(expectedType, actualType);
    }

    @Test
    public void testGetGenericTypeWithNonGenericField() {
        GenericMaster genericMaster = new GenericMaster();

        Class clazz = TestClassWithNonGenericField.class;
        Field field = null;
        try {
            field = clazz.getDeclaredField("field");
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
        field.setAccessible(true);

        Object expectedType = Object.class;
        Class actualType = genericMaster.getGenericType(field);

        assertEquals(expectedType, actualType);
    }

    @Test(expected = NullPointerException.class)
    public void testGetGenericTypeWithNullField() {
        GenericMaster genericMaster = new GenericMaster();

        Class actualType = genericMaster.getGenericType(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetGenericTypeWithNonFieldObject() {
        GenericMaster genericMaster = new GenericMaster();

        Object nonFieldObject = new Object();

        Class actualType = genericMaster.getGenericType(nonFieldObject);
    }

    @Test
    public void testGetGenericTypeWithNestedGenericType() {
        GenericMaster genericMaster = new GenericMaster();

        Class clazz = TestClassWithNestedGenericType.class;
        Field field = null;
        try {
            field = clazz.getDeclaredField("field");
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
        field.setAccessible(true);

        Class<?> expectedType = String[].class;
        Class<?> actualType = genericMaster.getGenericType(field);

        assertEquals(expectedType, actualType.getComponentType());
        assertEquals(String.class, actualType.getGenericComponentType());
    }

    static class TestClassWithGenericField {
        private String field;
    }

    static class TestClassWithNonGenericField {
        private int field;
    }

    static class TestClassWithNestedGenericType<T, V> {
        private List<T> field;
    }
}