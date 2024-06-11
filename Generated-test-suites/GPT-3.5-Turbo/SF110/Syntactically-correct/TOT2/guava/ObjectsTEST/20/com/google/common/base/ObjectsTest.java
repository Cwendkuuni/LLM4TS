package com.google.common.base;
import static org.junit.Assert.*;
import org.junit.Test;

public class ObjectsTest {

    @Test
    public void testEqual() {
        assertFalse(Objects.equal(null, "test"));
        assertTrue(Objects.equal("test", "test"));
    }

    @Test
    public void testHashCode() {
        assertEquals(Objects.hashCode("test", "case"), Objects.hashCode("test", "case"));
    }

    @Test
    public void testToStringHelper() {
        Objects.ToStringHelper toStringHelper = Objects.toStringHelper("Objects");
        assertNotNull(toStringHelper);
    }

    // Additional test cases for edge cases and exceptions

    @Test
    public void testFirstNonNull() {
        String first = "Hello";
        String second = "World";
        assertEquals(first, Objects.firstNonNull(first, null));
        assertEquals(second, Objects.firstNonNull(null, second));
    }
    
    // Add more test cases for edge cases and exceptions if needed

}