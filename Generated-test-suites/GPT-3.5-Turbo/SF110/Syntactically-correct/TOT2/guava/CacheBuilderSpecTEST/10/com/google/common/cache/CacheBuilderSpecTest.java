package com.google.common.cache;
import org.junit.Test;
import static org.junit.Assert.*;

public class CacheBuilderSpecTest {
    
    @Test
    public void testParse() {
        // Typical Use Case
        CacheBuilderSpec spec = CacheBuilderSpec.parse("initialCapacity=10,maximumSize=100");
        assertNotNull(spec);
        
        // Edge Case - Empty string
        spec = CacheBuilderSpec.parse("");
        assertNotNull(spec);
        
        // Error Scenario - Invalid key-value pair
        try {
            spec = CacheBuilderSpec.parse("initialCapacity=abc");
            fail("Expected IllegalArgumentException for invalid key-value pair");
        } catch (IllegalArgumentException e) {
            assertTrue(true);
        }
    }
    
    @Test
    public void testDisableCaching() {
        CacheBuilderSpec spec = CacheBuilderSpec.disableCaching();
        
        assertNotNull(spec);
        assertEquals(0, spec.maximumSize.intValue());
    }
    
    @Test
    public void testToCacheBuilder() {
        CacheBuilderSpec spec = CacheBuilderSpec.parse("initialCapacity=10,maximumSize=100");
        CacheBuilder<Object, Object> builder = spec.toCacheBuilder();
        
        assertNotNull(builder);
    }
    
    @Test
    public void testToParsableString() {
        CacheBuilderSpec spec = CacheBuilderSpec.parse("initialCapacity=10,maximumSize=100");
        String parsableString = spec.toParsableString();
        
        assertNotNull(parsableString);
        assertTrue(parsableString.contains("initialCapacity=10"));
        assertTrue(parsableString.contains("maximumSize=100"));
    }
    
    @Test
    public void testEquals() {
        CacheBuilderSpec spec1 = CacheBuilderSpec.parse("initialCapacity=10,maximumSize=100");
        CacheBuilderSpec spec2 = CacheBuilderSpec.parse("initialCapacity=10,maximumSize=100");
        
        assertTrue(spec1.equals(spec2));
    }
    
    @Test
    public void testHashCode() {
        CacheBuilderSpec spec = CacheBuilderSpec.parse("initialCapacity=10,maximumSize=100");
        
        assertNotNull(spec.hashCode());
    }
}