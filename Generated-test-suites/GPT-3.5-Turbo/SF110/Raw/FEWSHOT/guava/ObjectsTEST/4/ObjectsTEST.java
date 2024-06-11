package com.google.common.base;

import static org.junit.Assert.*;
import org.junit.Test;
import javax.annotation.Nullable;
import java.util.Arrays;

public class ObjectsTest {

    @Test
    public void testEqual() {
        assertTrue(Objects.equal(null, null));
        assertFalse(Objects.equal(null, ""));
        assertFalse(Objects.equal("", null));
        assertTrue(Objects.equal("", ""));
        assertTrue(Objects.equal("foo", "foo"));
        assertFalse(Objects.equal("foo", "bar"));
    }

    @Test
    public void testHashCode() {
        assertEquals(0, Objects.hashCode());
        assertEquals(1, Objects.hashCode(null));
        assertEquals(31, Objects.hashCode(31));
        assertEquals(31 + 32, Objects.hashCode(31, 32));
        assertEquals(31 * 31 * 31 + 32 * 32 + 33, Objects.hashCode(31, 32, 33));
    }

    @Test
    public void testToStringHelper() {
        Objects.ToStringHelper helper = Objects.toStringHelper(null);
        assertNotNull(helper);
        
        helper = Objects.toStringHelper("ClassName");
        assertNotNull(helper);
        
        helper = Objects.toStringHelper(ObjectsTest.class);
        assertNotNull(helper);
        
        helper = helper.omitNullValues();
        assertNotNull(helper);
        
        helper = helper.add("name", "value");
        assertNotNull(helper);
        
        helper = helper.add("bool", true);
        assertNotNull(helper);
        
        helper = helper.add("char", 'c');
        assertNotNull(helper);
        
        helper = helper.add("double", 1.23);
        assertNotNull(helper);
        
        helper = helper.add("float", 1.23f);
        assertNotNull(helper);
        
        helper = helper.add("int", 123);
        assertNotNull(helper);
        
        helper = helper.add("long", 123L);
        assertNotNull(helper);
        
        helper = helper.addValue("value");
        assertNotNull(helper);
        
        helper = helper.addValue(true);
        assertNotNull(helper);
        
        helper = helper.addValue('c');
        assertNotNull(helper);
        
        helper = helper.addValue(1.23);
        assertNotNull(helper);
        
        helper = helper.addValue(1.23f);
        assertNotNull(helper);
        
        helper = helper.addValue(123);
        assertNotNull(helper);
        
        helper = helper.addValue(123L);
        assertNotNull(helper);
        
        assertEquals("ClassName{}", helper.toString());
    }

    @Test
    public void testFirstNonNull() {
        assertNull(Objects.firstNonNull(null, null));
        assertEquals("", Objects.firstNonNull(null, ""));
        assertEquals("", Objects.firstNonNull("", null));
        assertEquals("foo", Objects.firstNonNull("", "foo"));
        assertEquals("foo", Objects.firstNonNull("foo", "bar"));
    }
}