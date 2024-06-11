import org.junit.Test;
import static org.junit.Assert.*;

public class FieldUtilsTest {

    @Test
    public void testSafeNegate() {
        // Basic test case
        int value = 5;
        int expected = -5;
        int result = FieldUtils.safeNegate(value);
        assertEquals(expected, result);

        // Edge case: Integer.MIN_VALUE
        int minValue = Integer.MIN_VALUE;
        try {
            FieldUtils.safeNegate(minValue);
            fail("Expected ArithmeticException");
        } catch (ArithmeticException e) {
            // Expected
        }

        // Error scenario: negative value
        int negativeValue = -1;
        int expectedNegative = -(-negativeValue);
        int resultNegative = FieldUtils.safeNegate(negativeValue);
        assertEquals(expectedNegative, resultNegative);
    }

    // Add other test methods for safeAdd, safeSubtract, safeMultiply, etc. here

    @Test
    public void testVerifyValueBounds() {
        DateTimeFieldType fieldType = DateTimeFieldType.dayOfMonth();

        // Basic test cases
        int value1 = 1;
        int lowerBound1 = 1;
        int upperBound1 = 31;
        FieldUtils.verifyValueBounds(fieldType, value1, lowerBound1, upperBound1);

        int value2 = 31;
        FieldUtils.verifyValueBounds(fieldType, value2, lowerBound1, upperBound1);

        int value3 = 32;
        try {
            FieldUtils.verifyValueBounds(fieldType, value3, lowerBound1, upperBound1);
            fail("Expected IllegalFieldValueException");
        } catch (IllegalFieldValueException e) {
            // Expected
        }

        // Edge case: lowerBound = upperBound
        int value4 = 31;
        int lowerBound2 = 31;
        int upperBound2 = 31;
        try {
            FieldUtils.verifyValueBounds(fieldType, value4, lowerBound2, upperBound2);
            fail("Expected IllegalFieldValueException");
        } catch (IllegalFieldValueException e) {
            // Expected
        }

        // Error scenario: null fieldType
        try {
            FieldUtils.verifyValueBounds(null, value1, lowerBound1, upperBound1);
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            // Expected
        }
    }

    // Add other test methods for other methods in the class here

    // 

    @Test
    public void testAllMethods() {
        // Add test cases for all methods here
    }

    // 
}