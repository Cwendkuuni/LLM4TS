import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.joda.time.DateTimeFieldType;
import org.joda.time.IllegalFieldValueException;
import org.junit.Test;

public class FieldUtilsTest {

    @Test
    public void testSafeNegate() {
        assertEquals("Negative value should be negated correctly", -5, FieldUtils.safeNegate(5));
        assertEquals("Zero should be negated to zero", 0, FieldUtils.safeNegate(0));
        try {
            FieldUtils.safeNegate(Integer.MIN_VALUE);
            fail("ArithmeticException should be thrown for Integer.MIN_VALUE");
        } catch (ArithmeticException e) {
            // expected
        }
    }

    @Test
    public void testSafeAdd() {
        assertEquals("Sum of two positive integers should be calculated correctly", 10, FieldUtils.safeAdd(5, 5));
        assertEquals("Sum of positive and negative integers should be calculated correctly", 0, FieldUtils.safeAdd(5, -5));
        try {
            FieldUtils.safeAdd(Integer.MAX_VALUE, 1);
            fail("ArithmeticException should be thrown for overflow");
        } catch (ArithmeticException e) {
            // expected
        }
    }

    @Test
    public void testSafeSubtract() {
        assertEquals("Positive value should be subtracted correctly", 5, FieldUtils.safeSubtract(10, 5));
        assertEquals("Negative value should be subtracted correctly", -5, FieldUtils.safeSubtract(-10, -5));
        try {
            FieldUtils.safeSubtract(Long.MIN_VALUE, 1);
            fail("ArithmeticException should be thrown for overflow");
        } catch (ArithmeticException e) {
            // expected
        }
    }

    @Test
    public void testSafeMultiply() {
        assertEquals("Product of two integers should be calculated correctly", 20, FieldUtils.safeMultiply(4, 5));
        try {
            FieldUtils.safeMultiply(Integer.MAX_VALUE, 2);
            fail("ArithmeticException should be thrown for overflow");
        } catch (ArithmeticException e) {
            // expected
        }
    }

    @Test
    public void testSafeToInt() {
        assertEquals("Long value should be safely converted to int", 10, FieldUtils.safeToInt(10L));
        try {
            FieldUtils.safeToInt(Long.MAX_VALUE);
            fail("ArithmeticException should be thrown for overflow");
        } catch (ArithmeticException e) {
            // expected
        }
    }

    @Test
    public void testGetWrappedValue() {
        assertEquals("Wrapped value should be calculated correctly", 5, FieldUtils.getWrappedValue(15, 5, 10));
        assertEquals("Wrapped value should be calculated correctly for negative numbers", -5, FieldUtils.getWrappedValue(-15, -10, -5));
    }

    @Test
    public void testEquals() {
        assertTrue("Both objects are null, should be equal", FieldUtils.equals(null, null));
        assertFalse("First object is null, second object is not null, should not be equal", FieldUtils.equals(null, "test"));
        assertFalse("First object is not null, second object is null, should not be equal", FieldUtils.equals("test", null));
        assertTrue("Both objects are the same, should be equal", FieldUtils.equals("test", "test"));
    }

    @Test
    public void testVerifyValueBounds() {
        FieldUtils.verifyValueBounds(DateTimeFieldType.secondOfDay(), 30, 0, 59); // within range
        try {
            FieldUtils.verifyValueBounds(DateTimeFieldType.secondOfDay(), 70, 0, 59); // out of range
            fail("IllegalFieldValueException should be thrown for out of bounds value");
        } catch (IllegalFieldValueException e) {
            // expected
        }
    }

}