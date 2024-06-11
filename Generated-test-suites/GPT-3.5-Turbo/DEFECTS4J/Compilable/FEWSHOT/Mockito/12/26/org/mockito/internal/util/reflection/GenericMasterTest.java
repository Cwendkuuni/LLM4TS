package org.mockito.internal.util.reflection;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Before;
import org.junit.Test;
import org.mockito.internal.util.reflection.GenericMaster;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

public class GenericMasterTest {

    private GenericMaster genericMaster;

    @Before
    public void setUp() {
        genericMaster = new GenericMaster();
    }

    @Test
    public void testGetGenericType() {
        Field field = null;
        Class result = genericMaster.getGenericType(field);
        assertEquals(Object.class, result);

        field = getField("field1", String.class);
        result = genericMaster.getGenericType(field);
        assertEquals(String.class, result);

        field = getField("field2", Integer.class);
        result = genericMaster.getGenericType(field);
        assertEquals(Integer.class, result);

        field = getField("field3", Boolean.class);
        result = genericMaster.getGenericType(field);
        assertEquals(Boolean.class, result);

        field = getField("field4", Double.class);
        result = genericMaster.getGenericType(field);
        assertEquals(Double.class, result);

        field = getField("field5", Long.class);
        result = genericMaster.getGenericType(field);
        assertEquals(Long.class, result);

        field = getField("field6", Float.class);
        result = genericMaster.getGenericType(field);
        assertEquals(Float.class, result);
    }

    private Field getField(String name, Class type) {
        try {
            return GenericMaster.class.getDeclaredField(name);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
            return null;
        }
    }
}