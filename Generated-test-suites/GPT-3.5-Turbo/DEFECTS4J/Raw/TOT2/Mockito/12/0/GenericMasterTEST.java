package org.mockito.internal.util.reflection;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;

public class GenericMasterTest {

    @Test
    public void testGetGenericType() {
        GenericMaster genericMaster = new GenericMaster();
        Field field = TestClass.class.getDeclaredField("genericField");
        ParameterizedType parameterizedType = (ParameterizedType) field.getGenericType();

        Class result = genericMaster.getGenericType(field);

        assertEquals(parameterizedType.getActualTypeArguments()[0], result);
    }

    @Test
    public void testGetGenericTypeNonGenericField() {
        GenericMaster genericMaster = new GenericMaster();
        try {
            Field field = TestClass.class.getDeclaredField("nonGenericField");
            Class result = genericMaster.getGenericType(field);

            assertEquals(Object.class, result);
        } catch (NoSuchFieldException e) {
            assertTrue(false);
        }
    }

    private static class TestClass {
        private List<String> genericField;
        private String nonGenericField;
    }
}