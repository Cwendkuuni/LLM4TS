package com.google.common.base;

import static org.junit.Assert.*;
import org.junit.Test;

public class ObjectsTest {

    @Test
    public void testEqual() {
        Object a = "hello";
        Object b = "hello";
        assertTrue(Objects.equal(a, b));
        
        a = null;
        assertFalse(Objects.equal(a, b));
        
        b = null;
        assertTrue(Objects.equal(a, b));
        
        a = "hello";
        b = "world";
        assertFalse(Objects.equal(a, b));
    }

    @Test
    public void testHashCode() {
        Object[] objects = {1, 2, 3};
        int expectedHash = objects.hashCode();
        int actualHash = Objects.hashCode(objects);
        assertEquals(expectedHash, actualHash);
        
        objects = null;
        expectedHash = 0;
        actualHash = Objects.hashCode(objects);
        assertEquals(expectedHash, actualHash);
        
        objects = new Object[0];
        expectedHash = objects.hashCode();
        actualHash = Objects.hashCode(objects);
        assertEquals(expectedHash, actualHash);
    }

    @Test
    public void testToStringHelper() {
        Objects.ToStringHelper helper = Objects.toStringHelper("TestClass");
        assertNotNull(helper);
    }

    @Test
    public void testFirstNonNull() {
        Object a = "hello";
        Object b = "world";
        Object expected = a;
        Object actual = Objects.firstNonNull(a, b);
        assertEquals(expected, actual);
        
        a = null;
        expected = b;
        actual = Objects.firstNonNull(a, b);
        assertEquals(expected, actual);
        
        b = null;
        assertNull(Objects.firstNonNull(a, b));
    }
}