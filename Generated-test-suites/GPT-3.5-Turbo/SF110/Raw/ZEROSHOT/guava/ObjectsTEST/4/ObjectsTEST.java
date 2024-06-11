package com.google.common.base;

import org.junit.Test;

import static org.junit.Assert.*;

public class ObjectsTest {

    @Test
    public void testEqual_sameObject() {
        Object obj = new Object();
        assertTrue(Objects.equal(obj, obj));
    }
    
    @Test
    public void testEqual_nullObjects() {
        assertTrue(Objects.equal(null, null));
    }
    
    @Test
    public void testEqual_oneNullObject() {
        assertFalse(Objects.equal(null, new Object()));
    }
    
    @Test
    public void testEqual_differentObjects() {
        assertFalse(Objects.equal(new Object(), new Object()));
    }
    
    @Test
    public void testHashCode_emptyArray() {
        assertEquals(0, Objects.hashCode());
    }
    
    @Test
    public void testHashCode_singleObjectArray() {
        Object obj = new Object();
        assertEquals(obj.hashCode(), Objects.hashCode(obj));
    }
    
    @Test
    public void testHashCode_multipleObjectArray() {
        Object obj1 = new Object();
        Object obj2 = new Object();
        assertEquals(31 * (31 + obj1.hashCode()) + obj2.hashCode(), Objects.hashCode(obj1, obj2));
    }
    
    @Test
    public void testToStringHelper_add() {
        Object obj = new Object();
        Objects.ToStringHelper helper = Objects.toStringHelper(obj);
        helper.add("name", "John")
              .add("age", 30)
              .add("object", obj);
        
        assertEquals(obj.getClass().getSimpleName() + "{name=John, age=30, object=" + obj.toString() + "}", helper.toString());
    }
    
    @Test
    public void testToStringHelper_omitNullValues() {
        Object obj = new Object();
        Objects.ToStringHelper helper = Objects.toStringHelper(obj).omitNullValues();
        helper.add("name", "John")
              .add("age", null)
              .add("object", obj);
        
        assertEquals(obj.getClass().getSimpleName() + "{name=John, object=" + obj.toString() + "}", helper.toString());
    }
    
    @Test
    public void testFirstNonNull_firstNonNull() {
        Object obj1 = new Object();
        Object obj2 = new Object();
        assertSame(obj1, Objects.firstNonNull(obj1, obj2));
    }
    
    @Test
    public void testFirstNonNull_secondNonNull() {
        Object obj1 = new Object();
        Object obj2 = new Object();
        assertSame(obj2, Objects.firstNonNull(null, obj2));
    }
    
    @Test
    public void testFirstNonNull_bothNull() {
        assertNull(Objects.firstNonNull(null, null));
    }
}