package com.google.common.base;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class ObjectsTest {
    @Test
    public void testEqual() {
        assertEquals(true, Objects.equal("foo", "foo"));
        assertEquals(false, Objects.equal("foo", "bar"));
        assertEquals(true, Objects.equal(null, null));
        assertEquals(false, Objects.equal("foo", null));
        assertEquals(false, Objects.equal(null, "bar"));
    }

    @Test
    public void testHashCode() {
        assertEquals(0, Objects.hashCode());
        assertEquals(123456, Objects.hashCode(123456));
        assertEquals(123456789, Objects.hashCode(123, 456, 789));
    }

    @Test
    public void testToStringHelper() {
        // test toString
        Objects.ToStringHelper helper = Objects.toStringHelper("ClassName");
        assertEquals("ClassName{}", helper.toString());

        // test add
        helper.add("name", "value");
        assertEquals("ClassName{name=value}", helper.toString());

        // test addValue
        helper.addValue(true);
        assertEquals("ClassName{name=value, true}", helper.toString());

        // test omitNullValues
        helper.omitNullValues();
        assertEquals("ClassName{name=value, true}", helper.toString());

        // test add with primitive types
        helper.add("intValue", 123);
        helper.add("doubleValue", 123.456);
        helper.add("booleanValue", true);
        helper.add("charValue", 'c');
        helper.add("floatValue", 123.456f);
        helper.add("longValue", 1234567890L);

        assertEquals("ClassName{name=value, true, intValue=123, doubleValue=123.456, booleanValue=true, charValue=c, floatValue=123.456, longValue=1234567890}", helper.toString());

        // test null values
        helper.addValue(null);
        helper.add("nullValue", null);
        assertEquals("ClassName{name=value, true, intValue=123, doubleValue=123.456, booleanValue=true, charValue=c, floatValue=123.456, longValue=1234567890}", helper.toString());
    }

    @Test
    public void testFirstNonNull() {
        assertEquals("foo", Objects.firstNonNull("foo", "bar"));
        assertEquals("bar", Objects.firstNonNull(null, "bar"));
        assertNull(Objects.firstNonNull(null, null));
    }
}