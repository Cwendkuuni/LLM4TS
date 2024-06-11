import org.junit.Test;
import org.junit.Assert.*;
import static org.joda.time.field.FieldUtils.*;

public class FieldUtilsTest {

    @Test
    public void testSafeNegateInt() {
        int val = 5;
        int expected = -val;
        int result = safeNegate(val);
        assertEquals(expected, result);
    }

    @Test(expected = ArithmeticException.class)
    public void testSafeNegateMinInt() {
        int val = Integer.MIN_VALUE;
        safeNegate(val);
    }

    @Test
    public void testSafeAddIntInt() {
        int val1 = 5;
        int val2 = 3;
        int expected = val1 + val2;
        int result = safeAdd(val1, val2);
        assertEquals(expected, result);
    }

    @Test(expected = ArithmeticException.class)
    public void testSafeAddIntOverflow() {
        int val1 = Integer.MAX_VALUE;
        int val2 = 1;
        int result = safeAdd(val1, val2);
    }

    @Test
    public void testSafeAddLongInt() {
        long val1 = 5L;
        long val2 = 3L;
        long expected = val1 + val2;
        long result = safeAdd(val1, (int) val2);
        assertEquals(expected, result);
    }

    @Test(expected = ArithmeticException.class)
    public void testSafeAddLongOverflow() {
        long val1 = Long.MAX_VALUE;
        long val2 = 1L;
        long result = safeAdd(val1, (int) val2);
    }

    @Test
    public void testSafeSubtractLongLong() {
        long val1 = 10L;
        long val2 = 5L;
        long expected = val1 - val2;
        long result = safeSubtract(val1, val2);
        assertEquals(expected, result);
    }

    @Test(expected = ArithmeticException.class)
    public void testSafeSubtractLongUnderflow() {
        long val1 = 5L;
        long val2 = Long.MAX_VALUE;
        long result = safeSubtract(val1, val2);
    }

    @Test
    public void testSafeMultiplyIntInt() {
        int val1 = 2;
        int val2 = 3;
        int expected = val1 * val2;
        int result = safeMultiply(val1, val2);
        assertEquals(expected, result);
    }

    @Test(expected = ArithmeticException.class)
    public void testSafeMultiplyIntOverflow() {
        int val1 = Integer.MAX_VALUE;
        int val2 = 1;
        int result = safeMultiply(val1, val2);
    }

    @Test
    public void testSafeMultiplyLongInt() {
        long val1 = 5L;
        int val2 = 3;
        long expected = val1 * val2;
        long result = safeMultiply(val1, val2);
        assertEquals(expected, result);
    }

    @Test(expected = ArithmeticException.class)
    public void testSafeMultiplyLongLongOverflow() {
        long val1 = Long.MAX_VALUE;
        long val2 = 1L;
        long result = safeMultiply(val1, val2);
    }

    @Test
    public void testSafeToIntLong() {
        long val = 123L;
        int expected = (int) val;
        int result = safeToInt(val);
        assertEquals(expected, result);
    }

    @Test(expected = ArithmeticException.class)
    public void testSafeToIntLongOverflow() {
        long val = Integer.MAX_VALUE + 1;
        int result = safeToInt(val);
    }

    @Test
    public void testSafeMultiplyToIntLongInt() {
        long val1 = 5L;
        long val2 = 3L;
        int expected = (int) (val1 * val2);
        int result = safeMultiplyToInt(val1, val2);
        assertEquals(expected, result);
    }

    @Test(expected = ArithmeticException.class)
    public void testSafeMultiplyToIntLongOverflow() {
        long val1 = Integer.MAX_VALUE;
        long val2 = 1L;
        int result = safeMultiplyToInt(val1, val2);
    }

    @Test
    public void testVerifyValueBounds() {
        DateTimeField field = new DateTimeField(DateTimeFieldType.dayOfMonth(), "dayOfMonth", 1, 31);
        int value = 25;
        int lowerBound = 1;
        int upperBound = 31;
        try {
            verifyValueBounds(field, value, lowerBound, upperBound);
        } catch (IllegalArgumentException e) {
            fail("Should not throw an IllegalArgumentException");
        }

        try {
            verifyValueBounds(field, value - 1, lowerBound, upperBound);
        } catch (IllegalFieldValueException e) {
            assertEquals("dayOfMonth", e.getFieldName());
            assertEquals(value - 1, e.getValue());
            assertEquals(lowerBound, e.getLowerBound());
            assertEquals(upperBound, e.getUpperBound());
        }

        try {
            verifyValueBounds(field, value + 1, lowerBound, upperBound);
        } catch (IllegalFieldValueException e) {
            assertEquals("dayOfMonth", e.getFieldName());
            assertEquals(value + 1, e.getValue());
            assertEquals(lowerBound, e.getLowerBound());
            assertEquals(upperBound, e.getUpperBound());
        }
    }

    @Test(expected = IllegalFieldValueException.class)
    public void testVerifyValueBoundsInvalid() {
        DateTimeField field = new DateTimeField(DateTimeFieldType.dayOfMonth(), "dayOfMonth", 1, 31);
        int value = 32;
        int lowerBound = 1;
        int upperBound = 31;
        verifyValueBounds(field, value, lowerBound, upperBound);
    }

    @Test
    public void testEquals() {
        Object obj1 = new Object();
        Object obj2 = new Object();
        Object obj3 = obj1;
        assertTrue(equals(obj1, obj1));
        assertTrue(equals(obj1, obj3));
        assertFalse(equals(obj1, obj2));
    }
}