package org.apache.commons.lang3;

import org.junit.Test;
import static org.junit.Assert.*;

public class BooleanUtilsTest {

    @Test
    public void testNegate() {
        assertEquals(Boolean.TRUE, BooleanUtils.negate(Boolean.FALSE));
        assertEquals(Boolean.FALSE, BooleanUtils.negate(Boolean.TRUE));
        assertNull(BooleanUtils.negate(null));
    }

    @Test
    public void testIsTrue() {
        assertTrue(BooleanUtils.isTrue(Boolean.TRUE));
        assertFalse(BooleanUtils.isTrue(Boolean.FALSE));
    }

    @Test
    public void testIsNotTrue() {
        assertTrue(BooleanUtils.isNotTrue(Boolean.FALSE));
        assertFalse(BooleanUtils.isNotTrue(Boolean.TRUE));
    }

    @Test
    public void testIsFalse() {
        assertTrue(BooleanUtils.isFalse(Boolean.FALSE));
        assertFalse(BooleanUtils.isFalse(Boolean.TRUE));
    }

    @Test
    public void testIsNotFalse() {
        assertTrue(BooleanUtils.isNotFalse(Boolean.TRUE));
        assertFalse(BooleanUtils.isNotFalse(Boolean.FALSE));
    }

    // Add more test cases for the other methods in the BooleanUtils class

    @Test
    public void testToBoolean() {
        assertTrue(BooleanUtils.toBoolean(1, 1, 0));
        assertFalse(BooleanUtils.toBoolean(0, 1, 0));
        try {
            BooleanUtils.toBoolean(2, 1, 0);
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException ex) {
            assertEquals("The Integer did not match either specified value", ex.getMessage());
        }
    }

    @Test
    public void testToBooleanWithInteger() {
        assertTrue(BooleanUtils.toBoolean(Integer.valueOf(1), Integer.valueOf(1), Integer.valueOf(0)));
        assertFalse(BooleanUtils.toBoolean(Integer.valueOf(0), Integer.valueOf(1), Integer.valueOf(0)));
        try {
            BooleanUtils.toBoolean(Integer.valueOf(2), Integer.valueOf(1), Integer.valueOf(0));
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException ex) {
            assertEquals("The Integer did not match either specified value", ex.getMessage());
        }
    }

    @Test
    public void testToBooleanObject() {
        // Write test cases for toBooleanObject method
    }

    @Test
    public void testToBooleanObjectWithInteger() {
        // Write test cases for toBooleanObject method with Integer parameter
    }

    @Test
    public void testToInteger() {
        // Write test cases for toInteger method
    }

    // Add more test cases for the other methods in the BooleanUtils class

}