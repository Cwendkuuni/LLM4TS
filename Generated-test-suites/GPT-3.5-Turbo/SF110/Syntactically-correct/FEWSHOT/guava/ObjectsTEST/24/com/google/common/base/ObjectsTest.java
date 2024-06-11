package com.google.common.base;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class ObjectsTest {
    
    @Test
    public void testEqual() {
        Object obj1 = new Object();
        Object obj2 = new Object();
        Object obj3 = null;
        
        assertEquals(true, Objects.equal(obj1, obj1));
        assertEquals(false, Objects.equal(obj1, obj2));
        assertEquals(false, Objects.equal(obj1, obj3));
        assertEquals(true, Objects.equal(obj3, obj3));
    }
    
    @Test
    public void testHashCode() {
        Object obj1 = new Object();
        Object obj2 = null;
        
        assertEquals(obj1.hashCode(), Objects.hashCode(obj1));
        assertEquals(0, Objects.hashCode(obj2));
        assertEquals(0, Objects.hashCode());
        assertEquals(Objects.hashCode(obj1, obj2), Objects.hashCode(obj1, obj2));
    }
    
    @Test
    public void testToStringHelper() {
        String className = "ClassName";
        Objects.ToStringHelper helper1 = Objects.toStringHelper(className);
        Objects.ToStringHelper helper2 = Objects.toStringHelper(Object.class);
        
        assertEquals("ClassName{}", helper1.toString());
        assertEquals("Object{}", helper2.toString());
        
        helper1.addValue("Value1");
        helper1.addValue(true);
        helper1.addValue(42);
        
        assertEquals("ClassName{Value1, true, 42}", helper1.toString());
        
        helper2.add("Name1", "Value1");
        helper2.add("Name2", 3.14);
        helper2.omitNullValues();
        
        assertEquals("Object{Name1=Value1, Name2=3.14}", helper2.toString());
    }
    
    @Test
    public void testFirstNonNull() {
        assertEquals("Value1", Objects.firstNonNull("Value1", "Value2"));
        assertEquals("Value2", Objects.firstNonNull(null, "Value2"));
        assertEquals("Value3", Objects.firstNonNull(null, null, "Value3"));
        assertEquals(null, Objects.firstNonNull((String) null));
    }
}