package com.google.common.base;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class ObjectsTest {
    @Test
    public void testEqual() {
        assertEquals(true, Objects.equal("foo", "foo"));
        assertEquals(true, Objects.equal(null, null));
        assertEquals(false, Objects.equal("foo", null));
   }

    @Test
    public void testHashCode() {
        assertEquals(0, Objects.hashCode(null));
        assertEquals(0, Objects.hashCode(null, null));
        assertEquals(1234, Objects.hashCode(1234));
        assertEquals(1234, Objects.hashCode(1234, 5678));
        assertEquals(56781111, Objects.hashCode("foo", 5678, 1111));
    }

    @Test
    public void testToStringHelper() {
        Objects.ToStringHelper helper = Objects.toStringHelper("className");
        assertEquals("className{}", helper.toString());

        helper.addValue(true);
        assertEquals("className{true}", helper.toString());

        helper.add("name", "value");
        assertEquals("className{true, name=value}", helper.toString());

        Objects.ToStringHelper omitNullHelper = Objects.toStringHelper("className").omitNullValues();
        assertEquals("className{}", omitNullHelper.toString());

        omitNullHelper.addValue(null);
        assertEquals("className{}", omitNullHelper.toString());

        omitNullHelper.add("name", "value");
        assertEquals("className{name=value}", omitNullHelper.toString());
    }
    
    @Test
    public void testFirstNonNull() {
        assertEquals("foo", Objects.firstNonNull("foo", null));
        assertEquals("bar", Objects.firstNonNull(null, "bar"));
        assertNull(Objects.firstNonNull(null, null));
    }
}