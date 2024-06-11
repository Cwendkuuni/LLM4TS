package org.mockito.internal.util.reflection;

import static org.junit.Assert.assertEquals;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import org.junit.Test;

public class GenericMasterTest {
    @Test
    public void testGetGenericType() throws NoSuchFieldException {
        Field field1 = GenericMaster.class.getDeclaredField("field1");
        Class expected1 = Object.class;
        assertEquals(expected1, GenericMaster.getGenericType(field1));

        Field field2 = GenericMaster.class.getDeclaredField("field2");
        Class expected2 = String.class;
        assertEquals(expected2, GenericMaster.getGenericType(field2));

        Field field3 = GenericMaster.class.getDeclaredField("field3");
        Class expected3 = Integer.class;
        assertEquals(expected3, GenericMaster.getGenericType(field3));
    }
}