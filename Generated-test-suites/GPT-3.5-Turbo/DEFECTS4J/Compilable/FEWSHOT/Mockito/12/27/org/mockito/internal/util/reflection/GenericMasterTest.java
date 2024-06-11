package org.mockito.internal.util.reflection;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import org.junit.Test;
import org.mockito.internal.util.reflection.GenericMaster;

public class GenericMasterTest {
    @Test
    public void testGetGenericType() throws NoSuchFieldException {
        GenericMaster genericMaster = new GenericMaster();
        Field field = genericMaster.getClass().getDeclaredField("genericTypeField");

        // Test case 1: generic type field is not generic
        Class expected1 = Object.class;
        Class actual1 = genericMaster.getGenericType(field);
        assertEquals(expected1, actual1);

        // Test case 2: generic type field is generic with single parameterized type
        field = genericMaster.getClass().getDeclaredField("genericTypeFieldWithParameterizedType");
        Class expected2 = Integer.class;
        Class actual2 = genericMaster.getGenericType(field);
        assertEquals(expected2, actual2);

        // Test case 3: generic type field is generic with nested parameterized types
        field = genericMaster.getClass().getDeclaredField("genericTypeFieldWithNestedParameterizedTypes");
        Class expected3 = String.class;
        Class actual3 = genericMaster.getGenericType(field);
        assertEquals(expected3, actual3);

        // Test case 4: generic type field is null
        field = genericMaster.getClass().getDeclaredField("nullGenericTypeField");
        Class expected4 = Object.class;
        Class actual4 = genericMaster.getGenericType(field);
        assertEquals(expected4, actual4);
    }
}