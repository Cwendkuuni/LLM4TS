package com.google.common.base;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class ObjectsTest {
    @Test
    public void testEqual() {
        assertTrue(Objects.equal(null, null));
        assertTrue(Objects.equal("hello", "hello"));
    }

    @Test
    public void testHashCode() {
        assertNotNull(Objects.hashCode("hello", "world"));
    }

    @Test
    public void testToStringHelper() {
        Objects.ToStringHelper toStringHelper1 = new Objects.ToStringHelper("ClassName");
        Objects.ToStringHelper toStringHelper2 = new Objects.ToStringHelper(getClass());
        Objects.ToStringHelper toStringHelper3 = new Objects.ToStringHelper("Test");

        assertNotNull(toStringHelper1);
        assertNotNull(toStringHelper2);
        assertNotNull(toStringHelper3);
    }

    @Test
    public void testFirstNonNull() {
        assertNull(Objects.firstNonNull(null, null));
        assertNotNull(Objects.firstNonNull(1, 2));
    }
}