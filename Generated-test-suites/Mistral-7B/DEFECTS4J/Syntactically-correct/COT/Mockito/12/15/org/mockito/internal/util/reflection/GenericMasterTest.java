package org.mockito.internal.util.reflection.test;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.internal.util.reflection.GenericMaster;
import org.mockito.junit.MockitoJUnitRunner;

import java.lang.reflect.Field;

@RunWith(MockitoJUnitRunner.class)
public class GenericMasterTest {

    @Test
    public void testGetGenericType() {
        // Test with a valid field and a single generic type
        Field validField = null; // Assuming you have a field with a valid generic type
        Class expectedType = String.class;
        Class actualType = new GenericMaster().getGenericType(validField);
        org.junit.Assert.assertEquals(expectedType, actualType);

        // Test with a valid field but no generic type
        Field noGenericField = null; // Assuming you have a field without a generic type
        org.junit.Assert.assertEquals(Object.class, new GenericMaster().getGenericType(noGenericField));

        // Test with null field
        Field nullField = null;
        org.junit.Assert.assertEquals(Object.class, new GenericMaster().getGenericType(nullField));

        // Test with an exception thrown when accessing the field
        Field throwingField = new Field("testField", Integer.TYPE, null) {
            @Override
            public Object get(Object obj) throws IllegalAccessException {
                throw new IllegalAccessException("Access denied");
            }
        };
        try {
            new GenericMaster().getGenericType(throwingField);
            org.junit.Assert.fail("Expected exception not thrown");
        } catch (IllegalAccessException e) {
            org.junit.Assert.assertEquals("Access denied", e.getMessage());
        }
    }
}