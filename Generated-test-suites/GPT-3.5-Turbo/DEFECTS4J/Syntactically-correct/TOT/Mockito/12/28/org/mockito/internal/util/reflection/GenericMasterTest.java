package org.mockito.internal.util.reflection;

import org.junit.Test;

import java.lang.reflect.Field;

import static org.junit.Assert.assertEquals;

public class GenericMasterTest {

    @Test
    public void testGetGenericType_withGenericField() throws NoSuchFieldException {
        GenericMaster genericMaster = new GenericMaster();
        Field field = MyClass.class.getDeclaredField("genericField");

        Class expectedType = Integer.class;
        Class actualType = genericMaster.getGenericType(field);

        assertEquals(expectedType, actualType);
    }

    @Test
    public void testGetGenericType_withNonGenericField() throws NoSuchFieldException {
        GenericMaster genericMaster = new GenericMaster();
        Field field = MyClass.class.getDeclaredField("nonGenericField");

        Class expectedType = Object.class;
        Class actualType = genericMaster.getGenericType(field);

        assertEquals(expectedType, actualType);
    }

    @Test
    public void testGetGenericType_withNestedGenericField() throws NoSuchFieldException {
        GenericMaster genericMaster = new GenericMaster();
        Field field = MyClass.class.getDeclaredField("nestedGenericField");

        Class expectedType = String.class;
        Class actualType = genericMaster.getGenericType(field);

        assertEquals(expectedType, actualType);
    }

    @Test(expected = NoSuchFieldException.class)
    public void testGetGenericType_withNonExistingField() throws NoSuchFieldException {
        GenericMaster genericMaster = new GenericMaster();
        Field field = MyClass.class.getDeclaredField("nonExistingField");

        genericMaster.getGenericType(field);
    }

    private static class MyClass<T> {
        private T genericField;
        private Object nonGenericField;
        private MyClass<String> nestedGenericField;
    }
}