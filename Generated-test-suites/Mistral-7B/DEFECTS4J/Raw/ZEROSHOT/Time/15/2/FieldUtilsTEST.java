import org.joda.time.DateTimeField;
import org.joda.time.DateTimeFieldType;
import org.junit.Test;
import org.junit.Assert.*;

public class FieldUtilsTest {

    @Test
    public void testSafeNegate() {
        int value = 5;
        int result = FieldUtils.safeNegate(value);
        int expected = -5;
        assertEquals(expected, result);

        value = Integer.MIN_VALUE;
        try {
            FieldUtils.safeNegate(value);
            fail("Expected ArithmeticException");
        } catch (ArithmeticException e) {
            //expected exception
        }
    }

    @Test
    public void testSafeAdd() {
        int val1 = 3;
        int val2 = 2;
        long result = FieldUtils.safeAdd(val1, val2);
        long expected = 5;
        assertEquals(expected, result);

        val1 = Integer.MIN_VALUE;
        val2 = 1;
        try {
            FieldUtils.safeAdd(val1, val2);
            fail("Expected ArithmeticException");
        } catch (ArithmeticException e) {
            //expected exception
        }

        val1 = Integer.MIN_VALUE;
        val2 = Integer.MIN_VALUE;
        try {
            FieldUtils.safeAdd(val1, val2);
            fail("Expected ArithmeticException");
        } catch (ArithmeticException e) {
            //expected exception
        }
    }

    @Test
    public void testSafeSubtract() {
        long val1 = 10;
        long val2 = 5;
        long result = FieldUtils.safeSubtract(val1, val2);
        long expected = 5;
        assertEquals(expected, result);

        val1 = Long.MIN_VALUE;
        val2 = 1;
        try {
            FieldUtils.safeSubtract(val1, val2);
            fail("Expected ArithmeticException");
        } catch (ArithmeticException e) {
            //expected exception
        }
    }

    @Test
    public void testSafeMultiply() {
        int val1 = 3;
        int val2 = 2;
        int result = FieldUtils.safeMultiply(val1, val2);
        int expected = 6;
        assertEquals(expected, result);

        val1 = Integer.MIN_VALUE;
        val2 = Integer.MIN_VALUE;
        try {
            FieldUtils.safeMultiply(val1, val2);
            fail("Expected ArithmeticException");
        } catch (ArithmeticException e) {
            //expected exception
        }

        val1 = Integer.MIN_VALUE;
        val2 = 2;
        try {
            FieldUtils.safeMultiply(val1, val2);
            fail("Expected ArithmeticException");
        } catch (ArithmeticException e) {
            //expected exception
        }
    }

    @Test
    public void testSafeMultiplyAndSafeToInt() {
        long val1 = 10;
        long val2 = 5;
        int result = FieldUtils.safeMultiplyToInt(val1, val2);
        int expected = 50;
        assertEquals(expected, result);

        val1 = Long.MAX_VALUE;
        val2 = 2;
        try {
            FieldUtils.safeMultiplyToInt(val1, val2);
            fail("Expected ArithmeticException");
        } catch (ArithmeticException e) {
            //expected exception
        }
    }

    @Test
    public void testVerifyValueBounds() {
        DateTimeField field = new DateTimeField(DateTimeFieldType.monthOfYear(), 1);
        try {
            FieldUtils.verifyValueBounds(field, -1, 1, 12);
            fail("Expected IllegalFieldValueException");
        } catch (IllegalFieldValueException e) {
            //expected exception
        }

        try {
            FieldUtils.verifyValueBounds(field, 13, 1, 12);
            fail("Expected IllegalFieldValueException");
        } catch (IllegalFieldValueException e) {
            //expected exception
        }

        DateTimeFieldType fieldType = DateTimeFieldType.monthOfYear();
        try {
            FieldUtils.verifyValueBounds(fieldType, -1, 1, 12);
            fail("Expected IllegalFieldValueException");
        } catch (IllegalFieldValueException e) {
            //expected exception
        }

        try {
            FieldUtils.verifyValueBounds(fieldType, 13, 1, 12);
            fail("Expected IllegalFieldValueException");
        } catch (IllegalFieldValueException e) {
            //expected exception
        }

        String fieldName = "test";
        try {
            FieldUtils.verifyValueBounds(fieldName, -1, 1, 12);
            fail("Expected IllegalFieldValueException");
        } catch (IllegalFieldValueException e) {
            //expected exception
        }

        try {
            FieldUtils.verifyValueBounds(fieldName, 13, 1, 12);
            fail("Expected IllegalFieldValueException");
        } catch (IllegalFieldValueException e) {
            //expected exception
        }
    }

    @Test
    public void testGetWrappedValue() {
        int value = 5;
        int result = FieldUtils.getWrappedValue(value, 1, 1, 5);
        assertEquals(value, result);

        value = 0;
        result = FieldUtils.getWrappedValue(value, 1, 1, 5);
        assertEquals(value, result);

        value = 6;
        result = FieldUtils.getWrappedValue(value, 1, 1, 5);
        assertEquals(1, result);

        value = 11;
        result = FieldUtils.getWrappedValue(value, 1, 1, 5);
        assertEquals(5, result);

        value = 12;
        try {
            FieldUtils.getWrappedValue(value, 1, 1, 5);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            //expected exception
        }
    }

    @Test
    public void testEquals() {
        Object obj1 = new Object();
        Object obj2 = new Object();
        assertFalse(FieldUtils.equals(obj1, obj2));

        obj2 = obj1;
        assertTrue(FieldUtils.equals(obj1, obj2));

        obj1 = null;
        assertFalse(FieldUtils.equals(obj1, obj2));
    }
}