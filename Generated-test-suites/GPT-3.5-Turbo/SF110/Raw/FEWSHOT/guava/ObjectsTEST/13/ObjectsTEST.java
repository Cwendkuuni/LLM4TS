package com.google.common.base;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class ObjectsTest {
    @Test
    public void testEqual() {
        assertTrue(Objects.equal(null, null));
        assertEquals(true, Objects.equal(1, 1));
        assertEquals(false, Objects.equal(1, 2));
        assertEquals(false, Objects.equal(1, null));
        assertEquals(false, Objects.equal(null, 1));
        assertEquals(true, Objects.equal("abc", "abc"));
        assertEquals(false, Objects.equal("abc", "def"));
    }

    @Test
    public void testHashCode() {
        assertEquals(0, Objects.hashCode());
        assertEquals(1, Objects.hashCode(1));
        assertEquals(31, Objects.hashCode(31));
        assertEquals(166, Objects.hashCode(1, 2, 3));
        assertEquals(31, Objects.hashCode("abc"));
        assertEquals(965, Objects.hashCode("abc", "def", "ghi"));
    }

    @Test @Deprecated
    public void testToStringHelper() {
        Objects.ToStringHelper helper = Objects.toStringHelper("ClassName");
        helper.omitNullValues();
        helper.add("name", "value");
        helper.add("bool", true);
        helper.addValue(123);
        assertEquals("ClassName{name=value, bool=true, 123}", helper.toString());

        Objects.ToStringHelper helper2 = Objects.toStringHelper(String.class);
        helper2.addValue(null);
        assertEquals("String{null}", helper2.toString());
    }

    @Test @Deprecated
    public void testFirstNonNull() {
        assertEquals(1, Objects.firstNonNull(1, 2));
        assertEquals(2, Objects.firstNonNull(null, 2));
        assertEquals("abc", Objects.firstNonNull("abc", null));
    }
}