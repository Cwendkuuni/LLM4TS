package com.google.common.base;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class ObjectsTest {
    @Test
    public void testEqual() {
        // Testing when both objects are null
        boolean result = Objects.equal(null, null);
        assertEquals(true, result);
        
        // Testing when only one object is null
        result = Objects.equal(null, new Object());
        assertEquals(false, result);
        
        // Testing when objects are different
        result = Objects.equal(new Object(), new Object());
        assertEquals(false, result);
        
        // Testing when objects are equal
        Object obj = new Object();
        result = Objects.equal(obj, obj);
        assertEquals(true, result);
    }
    
    @Test
    public void testHashCode() {
        // Testing when objects array is null
        int result = Objects.hashCode(null);
        assertEquals(0, result);
        
        // Testing when objects array is empty
        result = Objects.hashCode(new Object[0]);
        assertEquals(1, result);
        
        // Testing when objects array has one element
        result = Objects.hashCode(new Object[] { new Object() });
        assertEquals(31, result);
        
        // Testing when objects array has multiple elements
        Object obj1 = new Object();
        Object obj2 = new Object();
        result = Objects.hashCode(new Object[] { obj1, obj2 });
        int expected = ((obj1.hashCode() + 31) * 31) + obj2.hashCode();
        assertEquals(expected, result);
    }
    
    @Test
    public void testToStringHelper() {
        // Testing toString when omitNullValues is false and holders are null
        Objects.ToStringHelper helper = new Objects.ToStringHelper("ClassName");
        String result = helper.toString();
        assertEquals("ClassName{}", result);
        
        // Testing toString when omitNullValues is false and holders are not null
        helper.add("name1", "value1");
        helper.add("name2", "value2");
        result = helper.toString();
        assertEquals("ClassName{name1=value1, name2=value2}", result);
        
        // Testing toString when omitNullValues is true and holders have null values
        helper = new Objects.ToStringHelper("ClassName");
        helper.omitNullValues();
        helper.add("name1", "value1");
        helper.add("name2", null);
        result = helper.toString();
        assertEquals("ClassName{name1=value1}", result);
    }
    
    @Test
    public void testFirstNonNull() {
        // Testing when both objects are null
        Object result = Objects.firstNonNull(null, null);
        assertNull(result);
        
        // Testing when first object is null
        Object obj = new Object();
        result = Objects.firstNonNull(null, obj);
        assertEquals(obj, result);
        
        // Testing when second object is null
        result = Objects.firstNonNull(obj, null);
        assertEquals(obj, result);
        
        // Testing when both objects are not null
        Object obj1 = new Object();
        Object obj2 = new Object();
        result = Objects.firstNonNull(obj1, obj2);
        assertEquals(obj1, result);
    }
}