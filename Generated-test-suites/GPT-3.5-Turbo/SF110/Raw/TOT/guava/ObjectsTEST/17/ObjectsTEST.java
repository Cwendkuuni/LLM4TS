package com.google.common.base;

import org.junit.Test;
import static org.junit.Assert.*;

public class ObjectsTest {

    @Test
    public void testEqual_sameObjects() {
        Object a = new Object();
        Object b = a;
        
        assertTrue(Objects.equal(a, b));
    }
    
    @Test
    public void testEqual_nullObjects() {
        Object a = null;
        Object b = null;
        
        assertTrue(Objects.equal(a, b));
    }
    
    @Test
    public void testEqual_oneNullObject() {
        Object a = null;
        Object b = new Object();
        
        assertFalse(Objects.equal(a, b));
    }
    
    @Test
    public void testEqual_differentObjects() {
        Object a = new Object();
        Object b = new Object();
        
        assertFalse(Objects.equal(a, b));
    }
    
    @Test
    public void testHashCode_emptyArray() {
        Object[] objects = new Object[0];
        
        assertEquals(0, Objects.hashCode(objects));
    }
    
    @Test
    public void testHashCode_multipleObjects() {
        Object[] objects = {1, "string", true};
        
        assertEquals(1150903248, Objects.hashCode(objects));
    }
    
    @Test
    public void testToStringHelper_omitNullValues() {
        Object object = null;
        String expected = "ObjectsTest{}";
        
        assertEquals(expected, Objects.toStringHelper(object).omitNullValues().toString());
    }
    
    @Test
    public void testToStringHelper_addString() {
        Object object = "test";
        String expected = "ObjectsTest{object=test}";
        
        assertEquals(expected, Objects.toStringHelper(object).add("object", object).toString());
    }
    
    @Test
    public void testToStringHelper_addBoolean() {
        boolean value = true;
        String expected = "ObjectsTest{value=true}";
        
        assertEquals(expected, Objects.toStringHelper(value).add("value", value).toString());
    }
    
    @Test
    public void testToStringHelper_addChar() {
        char value = 'c';
        String expected = "ObjectsTest{value=c}";
        
        assertEquals(expected, Objects.toStringHelper(value).add("value", value).toString());
    }
    
    @Test
    public void testToStringHelper_addDouble() {
        double value = 1.23;
        String expected = "ObjectsTest{value=1.23}";
        
        assertEquals(expected, Objects.toStringHelper(value).add("value", value).toString());
    }
    
    @Test
    public void testToStringHelper_addFloat() {
        float value = 1.23f;
        String expected = "ObjectsTest{value=1.23}";
        
        assertEquals(expected, Objects.toStringHelper(value).add("value", value).toString());
    }
    
    @Test
    public void testToStringHelper_addInt() {
        int value = 123;
        String expected = "ObjectsTest{value=123}";
        
        assertEquals(expected, Objects.toStringHelper(value).add("value", value).toString());
    }
    
    @Test
    public void testToStringHelper_addLong() {
        long value = 1234567890L;
        String expected = "ObjectsTest{value=1234567890}";
        
        assertEquals(expected, Objects.toStringHelper(value).add("value", value).toString());
    }
    
    @Test
    public void testFirstNonNull_bothNull() {
        Object first = null;
        Object second = null;
        Object expected = null;
        
        assertNull(Objects.firstNonNull(first, second));
    }
    
    @Test
    public void testFirstNonNull_firstNull() {
        Object first = null;
        Object second = new Object();
        Object expected = second;
        
        assertEquals(expected, Objects.firstNonNull(first, second));
    }
    
    @Test
    public void testFirstNonNull_secondNull() {
        Object first = new Object();
        Object second = null;
        Object expected = first;
        
        assertEquals(expected, Objects.firstNonNull(first, second));
    }
    
    @Test
    public void testFirstNonNull_noNull() {
        Object first = new Object();
        Object second = new Object();
        Object expected = first;
        
        assertEquals(expected, Objects.firstNonNull(first, second));
    }
}