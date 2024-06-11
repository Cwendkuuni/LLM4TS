package com.google.common.base;

import static org.junit.Assert.*;
import org.junit.Test;
import javax.annotation.Nullable;
import java.util.*;

public class ObjectsTest {
    
    @Test
    public void testEqual() {
        assertTrue(Objects.equal(null, null));
        assertFalse(Objects.equal(null, "test"));
        assertFalse(Objects.equal("test", null));
        assertTrue(Objects.equal("test", "test"));
        assertFalse(Objects.equal("test", "Test"));
    }
    
    @Test
    public void testHashCode() {
        assertEquals(Objects.hashCode(null), 0);
        assertEquals(Objects.hashCode(), 1);
        assertEquals(Objects.hashCode("test"), "test".hashCode());
        assertEquals(Objects.hashCode("test", "Test"), Arrays.hashCode(new Object[] {"test", "Test"}));
    }
    
    @Test
    public void testToStringHelper() {
        Objects.ToStringHelper helper = Objects.toStringHelper("ClassName");
        assertEquals(helper.toString(), "ClassName{}");
        
        helper.add("name", "John");
        assertEquals(helper.toString(), "ClassName{name=John}");
        
        helper.addValue(true);
        assertEquals(helper.toString(), "ClassName{name=John, true}");
        
        helper.addValue(10);
        assertEquals(helper.toString(), "ClassName{name=John, true, 10}");
        
    }
    
    @Test
    public void testFirstNonNull() {
        assertNull(Objects.firstNonNull(null, null));
        
        String first = "First";
        String second = "Second";
        assertEquals(Objects.firstNonNull(first, second), first);
        
        first = null;
        assertEquals(Objects.firstNonNull(first, second), second);
    }
    
}