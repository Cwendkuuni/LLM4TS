import org.junit.Test;
import org.joda.time.DateTimeField;
import org.joda.time.DateTimeFieldType;
import org.joda.time.IllegalFieldValueException;
import static org.junit.Assert.*;

public class FieldUtilsTest {

    @Test
    public void testSafeNegate() {
        int value = 5;
        int expected = -5;
        int result = FieldUtils.safeNegate(value);
        assertEquals("Negate test failed.", expected, result);

        value = Integer.MIN_VALUE;
        try {
            FieldUtils.safeNegate(value);
            fail("Expected ArithmeticException.");
        } catch (ArithmeticException e) {
            // Expected
        }
    }

    @Test
    public void testSafeAdd() {
        int val1 = 10, val2 = 3;
        int expected = 13;
        int result = FieldUtils.safeAdd(val1, val2);
        assertEquals("Add test failed.", expected, result);

        val1 = Integer.MIN_VALUE;
        val2 = 2;
        try {
            FieldUtils.safeAdd(val1, val2);
            fail("Expected ArithmeticException.");
        } catch (ArithmeticException e) {
            // Expected
        }

        val1 = 1;
        val2 = Integer.MAX_VALUE;
        try {
            FieldUtils.safeAdd(val1, val2);
            fail("Expected ArithmeticException.");
        } catch (ArithmeticException e) {
            // Expected
        }
    }

    @Test
    public void testSafeSubtract() {
        long val1 = 10L, val2 = 3L;
        long expected = 7L;
        long result = FieldUtils.safeSubtract(val1, val2);
        assertEquals("Subtract test failed.", expected, result);

        val1 = Long.MIN_VALUE;
        val2 = 2L;
        try {
            FieldUtils.safeSubtract(val1, val2);
            fail("Expected ArithmeticException.");
        } catch (ArithmeticException e) {
            // Expected
        }

        val1 = 1L;
        val2 = Long.MAX_VALUE;
        try {
            FieldUtils.safeSubtract(val1, val2);
            fail("Expected ArithmeticException.");
        } catch (ArithmeticException e) {
            // Expected
        }
    }

    @Test
    public void testSafeMultiply() {
        int val1 = 5, val2 = 2;
        int expected = 10;
        int result = FieldUtils.safeMultiply(val1, val2);
        assertEquals("Multiply test failed.", expected, result);

        val1 = Integer.MIN_VALUE;
        val2 = 2;
        try {
            FieldUtils.safeMultiply(val1, val2);
            fail("Expected ArithmeticException.");
        } catch (ArithmeticException e) {
            // Expected
        }

        val1 = Integer.MAX_VALUE;
        val2 = 2;
        try {
            FieldUtils.safeMultiply(val1, val2);
            fail("Expected ArithmeticException.");
        } catch (ArithmeticException e) {
            // Expected
        }
    }

    @Test
    public void testVerifyValueBounds() {
        DateTimeField field = new DateTimeField(DateTimeFieldType.dayOfWeek(), "test");
        int value = 7;

        try {
            FieldUtils.verifyValueBounds(field, value, 1, 7);
            fail("Expected IllegalFieldValueException.");
        } catch (IllegalFieldValueException e) {
            // Expected
        }

        try {
            FieldUtils.verifyValueBounds(field, value, 1, 8);
            fail("Expected IllegalFieldValueException.");
        } catch (IllegalFieldValueException e) {
            // Expected
        }

        value = 6;
        FieldUtils.verifyValueBounds(field, value, 1, 8);
    }

    @Test
    public void testGetWrappedValue() {
        int value = 15, minValue = 0, maxValue = 5;
        int expected = 3;
        int result = FieldUtils.getWrappedValue(value, minValue, maxValue);
        assertEquals("GetWrappedValue test failed.", expected, result);

        value = Integer.MIN_VALUE;
        try {
            FieldUtils.getWrappedValue(value, minValue, maxValue);
            fail("Expected IllegalArgumentException.");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        minValue = maxValue;
        try {
            FieldUtils.getWrappedValue(value, minValue, maxValue);
            fail("Expected IllegalArgumentException.");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testEquals() {
        Object obj1 = new Object();
        Object obj2 = new Object();
        Object obj3 = obj1;
        assertTrue("Equals test failed.", FieldUtils.equals(obj1, obj1));
        assertTrue("Equals test failed.", FieldUtils.equals(obj1, obj3));
        assertFalse("Equals test failed.", FieldUtils.equals(obj1, obj2));
        assertFalse("Equals test failed.", FieldUtils.equals(null, obj1));
    }
}