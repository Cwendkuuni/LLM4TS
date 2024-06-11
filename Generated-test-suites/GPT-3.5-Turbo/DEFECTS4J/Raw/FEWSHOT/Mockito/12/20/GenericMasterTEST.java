package org.mockito.internal.util.reflection;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import org.junit.Before;
import org.junit.Test;

public class GenericMasterTest {

    private GenericMaster genericMaster;

    @Before
    public void setUp() {
        genericMaster = new GenericMaster();
    }

    @Test
    public void testGetGenericType_WithGenericField_ReturnsCorrectGenericType() throws NoSuchFieldException {
        // Test case for field with generic type
        Field field = MyClass.class.getDeclaredField("listField");
        Class<?> expectedType = String.class;
        Class<?> actualType = genericMaster.getGenericType(field);
        assertEquals(expectedType, actualType);
    }

    @Test
    public void testGetGenericType_WithNonGenericField_ReturnsObjectClass() throws NoSuchFieldException {
        // Test case for field without generic type
        Field field = MyClass.class.getDeclaredField("nonGenericField");
        Class<?> expectedType = Object.class;
        Class<?> actualType = genericMaster.getGenericType(field);
        assertEquals(expectedType, actualType);
    }

    @Test
    public void testGetGenericType_WithNullField_ReturnsObjectClass() {
        // Test case for null field
        Class<?> expectedType = Object.class;
        Class<?> actualType = genericMaster.getGenericType(null);
        assertEquals(expectedType, actualType);
    }

    // Helper classes
    private class MyClass<T> {
        private List<T> listField;
        private String nonGenericField;
    }
}