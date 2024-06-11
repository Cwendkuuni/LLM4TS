package com.google.common.base;
import static org.junit.Assert.*;
import org.junit.Test;

public class ObjectsTest {

    @Test
    public void testEqual() {
        assertTrue(Objects.equal(null, null));
        assertFalse(Objects.equal("hello", "world"));
        assertTrue(Objects.equal("hello", "hello"));
    }

    @Test
    public void testHashCode() {
        assertEquals(Objects.hashCode(null), 0);
        assertEquals(Objects.hashCode(new String[]{"abc", "def"}), new String[]{"abc", "def"}.hashCode());
    }

    @Test
    public void testToStringHelper() {
        Objects.ToStringHelper helper = Objects.toStringHelper("TestClass");
        helper.add("key1", "value1");
        helper.add("key2", 123);
        assertEquals(helper.toString(), "TestClass{key1=value1, key2=123}");
    }

    @Test
    public void testFirstNonNull() {
        assertEquals(Objects.firstNonNull(null, "default"), "default");
        assertEquals(Objects.firstNonNull("value", "default"), "value");
    }
}