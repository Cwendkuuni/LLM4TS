package com.google.common.base;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;

import javax.annotation.Nullable;

import org.junit.Test;

public class ObjectsTest {

    @Test
    public void testEqual() {
        // Test when a and b are equal
        Object a = mock(Object.class); 
        Object b = a;
        assertEquals(true, Objects.equal(a, b));

        // Test when a and b are not equal
        Object c = mock(Object.class);
        assertEquals(false, Objects.equal(a, c));

        // Test when a is null
        assertNull(Objects.equal(null, b));

        // Test when b is null
        assertNull(Objects.equal(a, null));

        // Test when both a and b are null
        assertNull(Objects.equal(null, null));
    }

    @Test
    public void testHashCode() {
        // Test when objects array is empty
        Object[] objects1 = {};
        assertEquals(Objects.hashCode(objects1), Arrays.hashCode(objects1));

        // Test when objects array is not empty
        Object[] objects2 = {1, 2, 3};
        assertEquals(Objects.hashCode(objects2), Arrays.hashCode(objects2));

        // Test when objects array is null
        assertNull(Objects.hashCode(null));
    }

    @SuppressWarnings("deprecation")
    @Test
    public void testToStringHelper() {
        // Test add method for null name and value
        Objects.ToStringHelper toStringHelper = Objects.toStringHelper("Test");
        toStringHelper.add(null, null);
        assertEquals("Test{}", toStringHelper.toString());

        // Test add method for null name and non-null value
        toStringHelper = Objects.toStringHelper("Test");
        toStringHelper.add(null, "value");
        assertEquals("Test{}", toStringHelper.toString());

        // Test add method for non-null name and null value
        toStringHelper = Objects.toStringHelper("Test");
        toStringHelper.add("name", null);
        assertEquals("Test{name=null}", toStringHelper.toString());

        // Test add method for non-null name and non-null value
        toStringHelper = Objects.toStringHelper("Test");
        toStringHelper.add("name", "value");
        assertEquals("Test{name=value}", toStringHelper.toString());

        // Test addValue method for null value
        toStringHelper = Objects.toStringHelper("Test");
        toStringHelper.addValue(null);
        assertEquals("Test{}", toStringHelper.toString());

        // Test addValue method for non-null value
        toStringHelper = Objects.toStringHelper("Test");
        toStringHelper.addValue("value");
        assertEquals("Test{value}", toStringHelper.toString());
    }

    @SuppressWarnings("deprecation")
    @Test
    public void testFirstNonNull() {
        // Test when first parameter is null
        Object first = null;
        Object second = "second";
        assertEquals(Objects.firstNonNull(first, second), second);

        // Test when first parameter is not null
        first = "first";
        assertNull(Objects.firstNonNull(first, second));

        // Test when both parameters are null
        assertNull(Objects.firstNonNull(null, null));
    }
}