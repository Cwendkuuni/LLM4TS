import org.junit.Test;
import static org.junit.Assert.*;

public class FieldUtilsTest {

    @Test
    public void testSafeNegate() {
        int value = 5;
        int negatedValue = FieldUtils.safeNegate(value);
        int expectedNegatedValue = -5;
        assertEquals("Negation failed", expectedNegatedValue, negatedValue);

        value = Integer.MIN_VALUE;
        try {
            negatedValue = FieldUtils.safeNegate(value);
            fail("Expected ArithmeticException");
        } catch (ArithmeticException e) {
            // Expected
        }
    }

    @Test
    public void testSafeAdd() {
        int val1 = 5, val2 = 3;
        int sum = FieldUtils.safeAdd(val1, val2);
        int expectedSum = 8;
        assertEquals("Addition failed", expectedSum, sum);

        val1 = Integer.MIN_VALUE;
        val2 = 5;
        try {
            sum = FieldUtils.safeAdd(val1, val2);
            fail("Expected ArithmeticException");
        } catch (ArithmeticException e) {
            // Expected
        }
    }

    // Add other test cases for safeSubtract, safeMultiply, safeAdd, safeMultiplyToInt, etc.
    
    @Test
    public void testVerifyValueBounds() {
        DateTimeFieldType fieldType = DateTimeFieldType.dayOfMonth();
        int value = 31;
        try {
            FieldUtils.verifyValueBounds(fieldType, value, 1, 30);
            fail("Expected IllegalFieldValueException");
        } catch (IllegalFieldValueException e) {
            // Expected
        }

        value = 1;
        FieldUtils.verifyValueBounds(fieldType, value, 1, 30);
    }

    // Add other test cases for verifyValueBounds with different field types and values.

    @Test
    public void testGetWrappedValue() {
        int value = 10, minValue = 0, maxValue = 5;
        int wrappedValue = FieldUtils.getWrappedValue(value, minValue, maxValue);
        int expectedWrappedValue = 2;
        assertEquals("Wrapping failed", expectedWrappedValue, wrappedValue);

        value = -5;
        wrappedValue = FieldUtils.getWrappedValue(value, minValue, maxValue);
        expectedWrappedValue = 4;
        assertEquals("Wrapping failed", expectedWrappedValue, wrappedValue);

        // Test with minValue = maxValue
        try {
            wrappedValue = FieldUtils.getWrappedValue(value, minValue, minValue);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    // Add other test cases for getWrappedValue with different minValue, maxValue and values.
}