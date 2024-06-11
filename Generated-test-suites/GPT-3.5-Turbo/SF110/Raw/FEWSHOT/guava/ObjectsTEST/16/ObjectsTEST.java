package com.google.common.base;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import java.util.Arrays;

public class ObjectsTest {

    // test equal method
    @Test
    public void testEqual() {
        assertTrue(Objects.equal(null, null));
        assertTrue(Objects.equal("test", "test"));
        assertTrue(Objects.equal(Arrays.asList(1, 2, 3), Arrays.asList(1, 2, 3)));
    }

    // test hashCode method
    @Test
    public void testHashCode() {
        assertEquals(Objects.hashCode(null), 0);
        assertEquals(Objects.hashCode("test"), "test".hashCode());
        assertEquals(Objects.hashCode("test", "test2"), Arrays.hashCode(new Object[]{"test", "test2"}));
    }

    // test toStringHelper method
    @Test
    public void testToStringHelper() {
        assertNotNull(Objects.toStringHelper("TestClass"));
        assertNotNull(Objects.toStringHelper(TestClass.class));
        assertNotNull(Objects.toStringHelper(TestClass.class.getName()));

        Objects.ToStringHelper helper = Objects.toStringHelper(TestClass.class);
        helper.omitNullValues();
        helper.add("name", "test");
        helper.addValue(true);

        String expectedToString = "TestClass{name=test, true}";
        assertEquals(helper.toString(), expectedToString);
    }

    // test firstNonNull method
    @Test
    public void testFirstNonNull() {
        assertEquals(Objects.firstNonNull(null, "test"), "test");
        assertEquals(Objects.firstNonNull("test1", "test2"), "test1");
        assertNull(Objects.firstNonNull(null, null));
    }

    private static class TestClass {
    }
}