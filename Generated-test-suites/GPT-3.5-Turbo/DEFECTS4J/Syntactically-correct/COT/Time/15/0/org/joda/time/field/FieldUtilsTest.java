import static org.junit.Assert.*;

import org.joda.time.DateTimeField;
import org.joda.time.DateTimeFieldType;
import org.joda.time.IllegalFieldValueException;
import org.junit.Test;

public class FieldUtilsTest {

    @Test
    public void testSafeNegate() {
        int value = 5;
        int expected = -5;
        int result = FieldUtils.safeNegate(value);
        assertEquals(expected, result);
    }

    @Test(expected = ArithmeticException.class)
    public void testSafeNegateNegateMinValue() {
        int value = Integer.MIN_VALUE;
        FieldUtils.safeNegate(value);
    }

    @Test
    public void testSafeAdd() {
        int val1 = 5;
        int val2 = 10;
        int expected = 15;
        int result = FieldUtils.safeAdd(val1, val2);
        assertEquals(expected, result);
    }

    @Test(expected = ArithmeticException.class)
    public void testSafeAddOverflow() {
        int val1 = Integer.MAX_VALUE;
        int val2 = 1;
        FieldUtils.safeAdd(val1, val2);
    }

    @Test
    public void testSafeMultiply() {
        int val1 = 5;
        int val2 = 10;
        int expected = 50;
        int result = FieldUtils.safeMultiply(val1, val2);
        assertEquals(expected, result);
    }

    @Test(expected = ArithmeticException.class)
    public void testSafeMultiplyOverflow() {
        int val1 = Integer.MAX_VALUE;
        int val2 = 2;
        FieldUtils.safeMultiply(val1, val2);
    }

    @Test
    public void testSafeToInt() {
        long value = 100;
        int expected = 100;
        int result = FieldUtils.safeToInt(value);
        assertEquals(expected, result);
    }

    @Test(expected = ArithmeticException.class)
    public void testSafeToIntOverflow() {
        long value = Long.MAX_VALUE;
        FieldUtils.safeToInt(value);
    }

    @Test
    public void testVerifyValueBounds() {
        DateTimeField field = null; // replace with actual field
        int value = 5;
        int lowerBound = 1;
        int upperBound = 10;
        FieldUtils.verifyValueBounds(field, value, lowerBound, upperBound);
        // assert no exception thrown
    }

    @Test(expected = IllegalFieldValueException.class)
    public void testVerifyValueBoundsLowerBound() {
        DateTimeField field = null; // replace with actual field
        int value = 0;
        int lowerBound = 1;
        int upperBound = 10;
        FieldUtils.verifyValueBounds(field, value, lowerBound, upperBound);
    }

    @Test(expected = IllegalFieldValueException.class)
    public void testVerifyValueBoundsUpperBound() {
        DateTimeField field = null; // replace with actual field
        int value = 15;
        int lowerBound = 1;
        int upperBound = 10;
        FieldUtils.verifyValueBounds(field, value, lowerBound, upperBound);
    }

    @Test
    public void testGetWrappedValue() {
        int currentValue = 1;
        int wrapValue = 5;
        int minValue = 0;
        int maxValue = 10;
        int expected = 6;
        int result = FieldUtils.getWrappedValue(currentValue, wrapValue, minValue, maxValue);
        assertEquals(expected, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetWrappedValueMinGreaterThanMax() {
        int currentValue = 1;
        int wrapValue = 5;
        int minValue = 10;
        int maxValue = 0;
        FieldUtils.getWrappedValue(currentValue, wrapValue, minValue, maxValue);
    }

    @Test
    public void testEquals() {
        Object object1 = new Object();
        Object object2 = object1;
        assertTrue(FieldUtils.equals(object1, object2));
    }

    @Test
    public void testEqualsNullObject() {
        Object object1 = new Object();
        Object object2 = null;
        assertFalse(FieldUtils.equals(object1, object2));
    }

    @Test
    public void testEqualsBothNull() {
        Object object1 = null;
        Object object2 = null;
        assertTrue(FieldUtils.equals(object1, object2));
    }

}