package org.apache.commons.lang3;
import org.junit.Test;
import static org.junit.Assert.*;

public class BooleanUtilsTest {

    @Test
    public void testNegate() {
        assertNull(BooleanUtils.negate(null));
        assertTrue(BooleanUtils.negate(true));
        assertFalse(BooleanUtils.negate(false));
    }

    @Test
    public void testIsTrue() {
        assertTrue(BooleanUtils.isTrue(true));
        assertFalse(BooleanUtils.isTrue(false));
    }

    @Test
    public void testIsNotTrue() {
        assertFalse(BooleanUtils.isNotTrue(true));
        assertTrue(BooleanUtils.isNotTrue(false));
    }

    @Test
    public void testIsFalse() {
        assertTrue(BooleanUtils.isFalse(false));
        assertFalse(BooleanUtils.isFalse(true));
    }

    @Test
    public void testIsNotFalse() {
        assertFalse(BooleanUtils.isNotFalse(false));
        assertTrue(BooleanUtils.isNotFalse(true));
    }

    @Test
    public void testToBoolean() {
        assertTrue(BooleanUtils.toBoolean(true));
        assertFalse(BooleanUtils.toBoolean(false));
    }

    // Additional test cases for edge cases and exceptions can be added here

}