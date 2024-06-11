import org.joda.time.DateTimeFieldType;
import org.joda.time.IllegalFieldValueException;
import org.joda.time.field.FieldUtils;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

public class FieldUtilsTest {

    @Test
    public void testSafeNegate_PositiveValue() {
        int result = FieldUtils.safeNegate(5);
        assertEquals(-5, result);
    }

    @Test
    public void testSafeNegate_IntegerMinValue() {
        try {
            FieldUtils.safeNegate(Integer.MIN_VALUE);
            fail("ArithmeticException should have been thrown");
        } catch (ArithmeticException e) {
            assertTrue(e.getMessage().contains("Integer.MIN_VALUE cannot be negated"));
        }
    }

    // Additional test cases for safeAdd, safeSubtract, safeMultiply, safeToInt, safeMultiplyToInt can be added here based on edge cases and error scenarios
    
    @Test
    public void testVerifyValueBounds_ValidValue() {
        try {
            FieldUtils.verifyValueBounds(DateTimeFieldType.year(), 2023, 2000, 3000);
        } catch (IllegalFieldValueException e) {
            fail("No exception should be thrown for valid value");
        }
    }

    @Test
    public void testVerifyValueBounds_ValueBelowRange() {
        try {
            FieldUtils.verifyValueBounds(DateTimeFieldType.year(), 1999, 2000, 3000);
            fail("IllegalFieldValueException should have been thrown");
        } catch (IllegalFieldValueException e) {
            // Exception expected
        }
    }

    // Additional test cases for verifyValueBounds, getWrappedValue, equals can be added here based on edge cases and error scenarios

}