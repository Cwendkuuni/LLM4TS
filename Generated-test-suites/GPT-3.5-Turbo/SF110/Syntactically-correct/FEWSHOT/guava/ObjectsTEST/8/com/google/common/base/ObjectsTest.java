package com.google.common.base;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import java.util.Arrays;

public class ObjectsTest {
    @Test
    public void testEqual() {
        assertTrue(Objects.equal(null, null));
        assertTrue(Objects.equal("foo", "foo"));
        assertTrue(Objects.equal("foo", "bar"));

        Object obj = new Object();
        assertTrue(Objects.equal(obj, obj));
    }

    @Test
    public void testHashCode() {
        assertEquals(0, Objects.hashCode());
        assertEquals(Arrays.hashCode(new Object[]{"foo", "bar"}), Objects.hashCode("foo", "bar"));

        Object obj = new Object();
        assertEquals(obj.hashCode(), Objects.hashCode(obj));
    }

    @Test
    public void testToStringHelper() {
        assertEquals("Objects{, omitNullValues=false}", Objects.toStringHelper("Objects").toString());
        assertEquals("Objects{, omitNullValues=true}", Objects.toStringHelper("Objects").omitNullValues().toString());

        assertEquals("Objects{value=1}", Objects.toStringHelper("Objects").add("value", 1).toString());
        assertEquals("Objects{value=null}", Objects.toStringHelper("Objects").add("value", null).toString());

        assertEquals("Objects{value=1, flag=true}", Objects.toStringHelper("Objects").add("value", 1).add("flag", true).toString());

        assertNull(Objects.toStringHelper("Objects").add(null, 1).toString());
    }

    @Test
    public void testFirstNonNull() {
        assertEquals("foo", Objects.firstNonNull("foo", "bar"));
        assertEquals("bar", Objects.firstNonNull(null, "bar"));

        Integer i = new Integer(1);
        assertEquals(i, Objects.firstNonNull(null, i));
    }
}