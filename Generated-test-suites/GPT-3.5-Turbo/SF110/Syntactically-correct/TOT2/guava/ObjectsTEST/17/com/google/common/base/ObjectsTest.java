package com.google.common.base;
import org.junit.Test;
import static org.junit.Assert.*;

public class ObjectsTest {
    
    @Test
    public void testEqual() {
        assertTrue(Objects.equal(null, null));
        assertFalse(Objects.equal(null, "test"));
        assertTrue(Objects.equal("test", "test"));
    }
    
    @Test
    public void testHashCode() {
        assertEquals(Objects.hashCode("test", "case"), Objects.hashCode("case", "test"));
    }
    
    @Test
    public void testToStringHelper() {
        Objects.ToStringHelper helper = Objects.toStringHelper("TestClass");
        assertNotNull(helper);
        assertNotNull(helper.toString());
    }
    
    @Test
    public void testFirstNonNull() {
        assertEquals("test", Objects.firstNonNull(null, "test"));
        assertEquals(10, (int) Objects.firstNonNull(null, 10));
    }
    
    // Add more test cases for the remaining methods here
    
    @Test
    public void testToStringHelperAdd() {
        Objects.ToStringHelper helper = Objects.toStringHelper("TestClass")
                                    .add("key", "value")
                                    .add("number", 10);
        assertNotNull(helper);
        assertNotNull(helper.toString());
    }
    
    @Test
    public void testToStringHelperOmitNullValues() {
        Objects.ToStringHelper helper = Objects.toStringHelper("TestClass")
                                    .omitNullValues();
        assertNotNull(helper);
    }
    
    // Add more test cases for edge cases and exception handling scenarios here
    
}