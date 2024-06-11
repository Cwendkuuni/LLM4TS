package com.google.common.base;

import org.junit.Test;
import static org.junit.Assert.*;

public class ObjectsTest {

    @Test
    public void equal_test() {
        // Typical use case
        assertTrue(Objects.equal("abc", "abc"));

        // Edge case: both objects are null
        assertTrue(Objects.equal(null, null));

        // Edge case: one object is null
        assertFalse(Objects.equal("abc", null));

        // Error scenario: types are different
        assertFalse(Objects.equal("123", 123));
    }

    @Test
    public void hashCode_test() {
        // Typical use case
        assertEquals(Objects.hashCode("abc", 123, true), Objects.hashCode("abc", 123, true));

        // Edge case: no arguments
        assertEquals(Objects.hashCode(), Objects.hashCode());

        // Error scenario: different arguments
        assertNotEquals(Objects.hashCode("abc"), Objects.hashCode("def"));
    }

    @Test
    public void toStringHelper_test() {
        // Typical use case
        assertEquals("ObjectsTest{example=null}", Objects.toStringHelper(this).add("example", null).toString());

        // Edge case: empty class name
        assertEquals("{example=null}", Objects.toStringHelper("").add("example", null).toString());

        // Error scenario: omitted null values
        assertNotEquals("ObjectsTest{example=null}", Objects.toStringHelper(this).omitNullValues().add("example", null).toString());
    }

    @Test
    public void firstNonNull_test() {
        // Typical use case
        assertEquals("abc", Objects.firstNonNull(null, "abc"));
        
        // Edge case: both objects are null
        assertNull(Objects.firstNonNull(null, null));

        // Edge case: one object is null
        assertEquals("def", Objects.firstNonNull("def", null));

        // Error scenario: types are different
        assertNotEquals(123, Objects.firstNonNull(123, "456"));
    }

}