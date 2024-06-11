package com.google.common.base;
import org.junit.Test;
import static org.junit.Assert.*;
import com.google.common.base.Predicates;

public class PredicatesTest {
    
    @Test
    public void testAlwaysTrue() {
        assertTrue(Predicates.alwaysTrue().apply("test"));
        assertFalse(Predicates.alwaysTrue().apply(null));
    }
    
    @Test
    public void testAlwaysFalse() {
        assertFalse(Predicates.alwaysFalse().apply("test"));
        assertFalse(Predicates.alwaysFalse().apply(null));
    }
    
    @Test
    public void testIsNull() {
        assertTrue(Predicates.isNull().apply(null));
        assertFalse(Predicates.isNull().apply("test"));
    }
    
    @Test
    public void testNotNull() {
        assertFalse(Predicates.notNull().apply(null));
        assertTrue(Predicates.notNull().apply("test"));
    }
    
    // Add test cases for other methods as well
    
}