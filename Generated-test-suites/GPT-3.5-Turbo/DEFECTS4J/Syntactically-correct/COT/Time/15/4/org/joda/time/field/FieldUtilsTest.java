import org.joda.time.DateTimeField;
import org.joda.time.DateTimeFieldType;
import org.joda.time.IllegalFieldValueException;
import org.junit.Test;
import static org.junit.Assert.*;

public class FieldUtilsTest {

    // Test safeNegate() method
    @Test
    public void testSafeNegate() {
        int value = 5;
        assertEquals(-value, FieldUtils.safeNegate(value));

        value = -5;
        assertEquals(-value, FieldUtils.safeNegate(value));
    }

    // Test safeAdd() method
    @Test
    public void testSafeAdd() {
        int val1 = 5;
        int val2 = 3;
        assertEquals(val1 + val2, FieldUtils.safeAdd(val1, val2));

        val1 = -5;
        val2 = -3;
        assertEquals(val1 + val2, FieldUtils.safeAdd(val1, val2));
    }

    // Test safeMultiply() method
    @Test
    public void testSafeMultiply() {
        int val1 = 5;
        int val2 = 3;
        assertEquals(val1 * val2, FieldUtils.safeMultiply(val1, val2));

        val1 = -5;
        val2 = -3;
        assertEquals(val1 * val2, FieldUtils.safeMultiply(val1, val2));
    }

    // Test safeSubtract() method
    @Test
    public void testSafeSubtract() {
        long val1 = 10;
        long val2 = 5;
        assertEquals(val1 - val2, FieldUtils.safeSubtract(val1, val2));

        val1 = -10;
        val2 = -5;
        assertEquals(val1 - val2, FieldUtils.safeSubtract(val1, val2));
    }

    // Test safeToInt() method
    @Test
    public void testSafeToInt() {
        long value = 5;
        assertEquals((int) value, FieldUtils.safeToInt(value));

        value = Integer.MAX_VALUE + 1L;
        try {
            FieldUtils.safeToInt(value);
            fail("Expected ArithmeticException to be thrown");
        } catch (ArithmeticException e) {
            // Expected exception
        }
    }

    // Test safeMultiplyToInt() method
    @Test
    public void testSafeMultiplyToInt() {
        long val1 = 5;
        long val2 = 3;
        assertEquals((int) (val1 * val2), FieldUtils.safeMultiplyToInt(val1, val2));

        val1 = Integer.MAX_VALUE + 2L;
        val2 = Integer.MAX_VALUE + 1L;
        try {
            FieldUtils.safeMultiplyToInt(val1, val2);
            fail("Expected ArithmeticException to be thrown");
        } catch (ArithmeticException e) {
            // Expected exception
        }
    }

    // Test verifyValueBounds() method
    @Test
    public void testVerifyValueBounds() {
        DateTimeFieldType fieldType = DateTimeFieldType.dayOfMonth();
        int value = 5;
        int lowerBound = 1;
        int upperBound = 31;
        FieldUtils.verifyValueBounds(fieldType, value, lowerBound, upperBound);

        value = 40;
        try {
            FieldUtils.verifyValueBounds(fieldType, value, lowerBound, upperBound);
            fail("Expected IllegalFieldValueException to be thrown");
        } catch (IllegalFieldValueException e) {
            // Expected exception
        }
    }

    // Test getWrappedValue() method
    @Test
    public void testGetWrappedValue() {
        int currentValue = 5;
        int wrapValue = 3;
        int minValue = 1;
        int maxValue = 10;
        assertEquals((currentValue + wrapValue - minValue) % (maxValue - minValue + 1) + minValue,
                FieldUtils.getWrappedValue(currentValue, wrapValue, minValue, maxValue));

        currentValue = 10;
        wrapValue = 3;
        minValue = 1;
        maxValue = 10;
        assertEquals(minValue, FieldUtils.getWrappedValue(currentValue, wrapValue, minValue, maxValue));
    }

    // Test equals() method
    @Test
    public void testEquals() {
        Object object1 = "hello";
        Object object2 = "hello";
        assertTrue(FieldUtils.equals(object1, object2));

        object1 = "hello";
        object2 = "world";
        assertFalse(FieldUtils.equals(object1, object2));
    }
}


