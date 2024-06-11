package org.apache.commons.lang3;
import static org.junit.Assert.*;
import org.junit.Test;

public class BooleanUtilsTest {

    @Test
    public void testNegate() {
        assertNull(BooleanUtils.negate(null));
        assertTrue(BooleanUtils.negate(Boolean.FALSE));
        assertFalse(BooleanUtils.negate(Boolean.TRUE));
    }

    @Test
    public void testIsTrue() {
        assertTrue(BooleanUtils.isTrue(Boolean.TRUE));
        assertFalse(BooleanUtils.isTrue(Boolean.FALSE));
    }

    @Test
    public void testIsNotTrue() {
        assertFalse(BooleanUtils.isNotTrue(Boolean.TRUE));
        assertTrue(BooleanUtils.isNotTrue(Boolean.FALSE));
    }

    @Test
    public void testIsFalse() {
        assertTrue(BooleanUtils.isFalse(Boolean.FALSE));
        assertFalse(BooleanUtils.isFalse(Boolean.TRUE));
    }

    @Test
    public void testIsNotFalse() {
        assertFalse(BooleanUtils.isNotFalse(Boolean.FALSE));
        assertTrue(BooleanUtils.isNotFalse(Boolean.TRUE));
    }

    @Test
    public void testToBoolean() {
        assertTrue(BooleanUtils.toBoolean(true));
        assertFalse(BooleanUtils.toBoolean(false));
    }

    @Test
    public void testToBooleanDefaultIfNull() {
        assertTrue(BooleanUtils.toBooleanDefaultIfNull(Boolean.TRUE, false));
        assertFalse(BooleanUtils.toBooleanDefaultIfNull(null, false));
    }

    @Test
    public void testToBooleanIntValue() {
        assertTrue(BooleanUtils.toBoolean(1));
        assertFalse(BooleanUtils.toBoolean(0));
    }

    //...continue with similar test cases for each method in BooleanUtils class...

    @Test
    public void testOr() {
        assertTrue(BooleanUtils.or(true, true));
        assertTrue(BooleanUtils.or(true, false));
        assertTrue(BooleanUtils.or(false, true));
        assertFalse(BooleanUtils.or(false, false));
    }

    @Test
    public void testXor() {
        assertTrue(BooleanUtils.xor(true, false));
        assertTrue(BooleanUtils.xor(false, true));
        assertFalse(BooleanUtils.xor(true, true));
        assertFalse(BooleanUtils.xor(false, false));
    }

    @Test
    public void testAndWithArray() {
        assertTrue(BooleanUtils.and(true, true, true));
        assertFalse(BooleanUtils.and(true, false, true));
    }

    @Test
    public void testAndWithArrayObject() {
        assertTrue(BooleanUtils.and(Boolean.TRUE, Boolean.TRUE, Boolean.TRUE));
        assertFalse(BooleanUtils.and(Boolean.FALSE, Boolean.TRUE, Boolean.TRUE));
    }

    @Test
    public void testOrWithArray() {
        assertTrue(BooleanUtils.or(true, true, false));
        assertFalse(BooleanUtils.or(false, false, false));
    }

    @Test
    public void testOrWithArrayObject() {
        assertTrue(BooleanUtils.or(Boolean.TRUE, Boolean.FALSE, Boolean.FALSE));
        assertFalse(BooleanUtils.or(Boolean.FALSE, Boolean.FALSE, Boolean.FALSE));
    }

    //...continue with similar test cases for array and object methods...

}