package org.mockito.internal.util.reflection;

import org.junit.Test;

import java.lang.reflect.Field;

import static org.junit.Assert.assertEquals;

public class GenericMasterTest {
    @Test
    public void testGetGenericType() {
        Field field1 = getField("field1");
        Field field2 = getField("field2");
        Field field3 = getField("field3");
        Field field4 = getField("field4");

        assertEquals(Object.class, GenericMaster.getGenericType(field1));
        assertEquals(String.class, GenericMaster.getGenericType(field2));
        assertEquals(Integer.class, GenericMaster.getGenericType(field3));
        assertEquals(Double.class, GenericMaster.getGenericType(field4));
    }

    private Field getField(String fieldName) {
        try {
            return TestClass.class.getDeclaredField(fieldName);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
        return null;
    }

    private class TestClass {
        // non-generic field
        private Object field1;
        // generic field with String
        private List<String> field2;
        // generic field with Integer
        private List<Integer> field3;
        // generic field with Double
        private List<List<Double>> field4;
    }
}