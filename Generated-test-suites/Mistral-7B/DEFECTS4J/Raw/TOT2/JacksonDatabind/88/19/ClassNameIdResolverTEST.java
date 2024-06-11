package com.example; // Replace with the correct package name

import org.junit.Test;
import static org.junit.Assert.*;

public class ClassNameIdResolverTest {

    @Test
    public void testIdFromValue() {
        ClassNameIdResolver resolver = new ClassNameIdResolver(null, null);

        Object obj = new Object();
        String expectedId = obj.getClass().getName();
        String actualId = resolver.idFromValue(obj);

        assertEquals(expectedId, actualId);
    }

    @Test
    public void testIdFromValueNull() {
        ClassNameIdResolver resolver = new ClassNameIdResolver(null, null);

        String actualId = resolver.idFromValue(null);
        assertNull(actualId);
    }

    @Test
    public void testIdFromValuePrimitive() {
        ClassNameIdResolver resolver = new ClassNameIdResolver(null, null);

        int value = 1;
        String expectedId = int.class.getName();
        String actualId = resolver.idFromValue(value);

        assertEquals(expectedId, actualId);
    }

    @Test
    public void testIdFromValueArray() {
        ClassNameIdResolver resolver = new ClassNameIdResolver(null, null);

        Object[] values = new Object[1];
        String expectedId = values.getClass().getName();
        String actualId = resolver.idFromValue(values);

        assertEquals(expectedId, actualId);
    }

    @Test
    public void testTypeFromIdEmptyString() {
        ClassNameIdResolver resolver = new ClassNameIdResolver(null, null);

        try {
            JavaType actualType = resolver.typeFromId(null, "");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testTypeFromIdNull() {
        ClassNameIdResolver resolver = new ClassNameIdResolver(null, null);

        try {
            JavaType actualType = resolver.typeFromId(null, Object.class);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testIdFromValueNullTypeFactory() {
        ClassNameIdResolver resolver = new ClassNameIdResolver(null, null);

        Object obj = new Object();

        try {
            String actualId = resolver._idFrom(obj, null, null);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

}