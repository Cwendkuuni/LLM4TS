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
        assertEquals(Objects.hashCode("test", "test"), Objects.hashCode("test", "test"));
    }

    // More test cases for hashCode method can be added for edge cases

    @Test
    public void testToStringHelper() {
        Objects.ToStringHelper helper = Objects.toStringHelper("TestClass");
        assertNotNull(helper);
        // Add more test cases for different methods of ToStringHelper
    }

    @Test
    public void testFirstNonNull() {
        assertEquals("test", Objects.firstNonNull(null, "test"));
        assertEquals(null, Objects.firstNonNull(null, null));
        // Add more test cases for edge scenarios
    }

    // Add more test cases for other methods in the Objects class

}