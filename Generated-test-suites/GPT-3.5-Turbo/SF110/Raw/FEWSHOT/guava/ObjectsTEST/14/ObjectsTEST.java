package com.google.common.base;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;
import javax.annotation.Nullable;
import java.util.Arrays;

public class ObjectsTest {
    
    @Test
    public void testEqual() {
        // Test equal objects
        Object a = new Object();
        Object b = a;
        assertEquals(true, Objects.equal(a, b));
        
        // Test unequal objects
        Object c = new Object();
        Object d = new Object();
        assertEquals(false, Objects.equal(c, d));
        
        // Test null objects
        assertEquals(true, Objects.equal(null, null));
        assertEquals(false, Objects.equal(null, a));
        assertEquals(false, Objects.equal(a, null));
    }
    
    @Test
    public void testHashCode() {
        // Test single object
        assertEquals(123456, Objects.hashCode(123456));
        
        // Test multiple objects
        assertEquals(-210123, Objects.hashCode(1, 2, 3));
        
        // Test null objects
        assertEquals(0, Objects.hashCode(null));
        assertEquals(0, Objects.hashCode(null, null));
    }
    
    @Test
    public void testToStringHelper() {
        // Test toStringHelper with class name
        Objects.ToStringHelper helper1 = Objects.toStringHelper("Test");
        assertNotNull(helper1);
        
        // Test toStringHelper with object
        Object obj = new Object();
        Objects.ToStringHelper helper2 = Objects.toStringHelper(obj);
        assertNotNull(helper2);
        
        // Test toStringHelper with class
        Class<?> clazz = Integer.class;
        Objects.ToStringHelper helper3 = Objects.toStringHelper(clazz);
        assertNotNull(helper3);
    }
    
    @Test
    public void testFirstNonNull() {
        // Test firstNonNull with both non-null objects
        Integer a = 1;
        Integer b = 2;
        assertEquals(a, Objects.firstNonNull(a, b));
        
        // Test firstNonNull with first object null and second object non-null
        Integer c = null;
        Integer d = 3;
        assertEquals(d, Objects.firstNonNull(c, d));
        
        // Test firstNonNull with first object non-null and second object null
        Integer e = 4;
        Integer f = null;
        assertEquals(e, Objects.firstNonNull(e, f));
        
        // Test firstNonNull with both null objects
        Integer g = null;
        Integer h = null;
        assertEquals(null, Objects.firstNonNull(g, h));
    }

}