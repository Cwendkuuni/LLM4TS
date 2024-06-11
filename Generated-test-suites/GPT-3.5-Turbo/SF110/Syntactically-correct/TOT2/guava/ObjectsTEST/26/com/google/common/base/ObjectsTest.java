package com.google.common.base;
import com.google.common.base.Objects;

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
        assertEquals(Objects.hashCode("a", "b", "c"), Objects.hashCode("a", "b", "c"));
    }

    @Test
    public void testToStringHelper() {
        assertNotNull(Objects.toStringHelper("TestClass"));
    }

    @Test
    public void testFirstNonNull() {
        assertEquals("test", Objects.firstNonNull(null, "test"));
    }

    // Add more test cases for the remaining methods

}