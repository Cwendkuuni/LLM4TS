import static org.junit.Assert.*;
import org.junit.Test;
import org.joda.time.DateTimeField;
import org.joda.time.DateTimeFieldType;
import org.joda.time.IllegalFieldValueException;
import org.joda.time.field.FieldUtils;

public class FieldUtilsTest {

    @Test
    public void testSafeNegate() {
        int result = FieldUtils.safeNegate(10);
        assertEquals(-10, result);
    }

    @Test(expected = ArithmeticException.class)
    public void testSafeNegate_Exception() {
        FieldUtils.safeNegate(Integer.MIN_VALUE);
    }

    @Test
    public void testSafeAdd() {
        int result = FieldUtils.safeAdd(10, 20);
        assertEquals(30, result);
    }

    @Test
    public void testSafeAdd_NegativeNumbers() {
        int result = FieldUtils.safeAdd(-10, -20);
        assertEquals(-30, result);
    }

    @Test(expected = ArithmeticException.class)
    public void testSafeAdd_Overflow() {
        FieldUtils.safeAdd(Integer.MAX_VALUE, 1);
    }

    @Test
    public void testSafeAdd_LongNumbers() {
        long result = FieldUtils.safeAdd(1000000L, 2000000L);
        assertEquals(3000000L, result);
    }

    @Test
    public void testSafeSubtract() {
        long result = FieldUtils.safeSubtract(20, 10);
        assertEquals(10, result);
    }

    @Test
    public void testSafeSubtract_NegativeNumbers() {
        long result = FieldUtils.safeSubtract(-20, -10);
        assertEquals(-10, result);
    }

    @Test(expected = ArithmeticException.class)
    public void testSafeSubtract_Overflow() {
        FieldUtils.safeSubtract(Long.MIN_VALUE, 1);
    }

    @Test
    public void testSafeMultiply() {
        int result = FieldUtils.safeMultiply(10, 20);
        assertEquals(200, result);
    }

    @Test(expected = ArithmeticException.class)
    public void testSafeMultiply_Overflow() {
        FieldUtils.safeMultiply(Integer.MAX_VALUE, 2);
    }

    @Test
    public void testSafeMultiply_LongNumbers() {
        long result = FieldUtils.safeMultiply(1000000L, 2000000L);
        assertEquals(2000000000000L, result);
    }

    @Test
    public void testSafeMultiplyToInt() {
        int result = FieldUtils.safeMultiplyToInt(1000000L, 2000000L);
        assertEquals(2000000000, result);
    }

    @Test
    public void testSafeToInt() {
        int result = FieldUtils.safeToInt(100);
        assertEquals(100, result);
    }

    @Test(expected = ArithmeticException.class)
    public void testSafeToInt_Overflow() {
        FieldUtils.safeToInt(Long.MAX_VALUE);
    }

    @Test
    public void testVerifyValueBounds() {
        DateTimeField field = null;
        try {
            FieldUtils.verifyValueBounds(field, 10, 1, 20);
            assertEquals(true, true); // assert that no exception is thrown
        } catch (IllegalFieldValueException e) {
            fail("Exception thrown");
        }
    }

    @Test(expected = IllegalFieldValueException.class)
    public void testVerifyValueBounds_Exception() {
        DateTimeField field = null;
        FieldUtils.verifyValueBounds(field, 10, 20, 30);
    }

    @Test
    public void testVerifyValueBounds_DateTimeFieldType() {
        DateTimeFieldType fieldType = null;
        try {
            FieldUtils.verifyValueBounds(fieldType, 10, 1, 20);
            assertEquals(true, true); // assert that no exception is thrown
        } catch (IllegalFieldValueException e) {
            fail("Exception thrown");
        }
    }

    @Test(expected = IllegalFieldValueException.class)
    public void testVerifyValueBounds_DateTimeFieldType_Exception() {
        DateTimeFieldType fieldType = null;
        FieldUtils.verifyValueBounds(fieldType, 10, 20, 30);
    }

    @Test
    public void testVerifyValueBounds_String() {
        String fieldName = null;
        try {
            FieldUtils.verifyValueBounds(fieldName, 10, 1, 20);
            assertEquals(true, true); // assert that no exception is thrown
        } catch (IllegalFieldValueException e) {
            fail("Exception thrown");
        }
    }

    @Test(expected = IllegalFieldValueException.class)
    public void testVerifyValueBounds_String_Exception() {
        String fieldName = null;
        FieldUtils.verifyValueBounds(fieldName, 10, 20, 30);
    }

    @Test
    public void testGetWrappedValue() {
        int result = FieldUtils.getWrappedValue(10, 5, 1, 10);
        assertEquals(5, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetWrappedValue_Exception() {
        FieldUtils.getWrappedValue(10, 5, 10, 1);
    }

    @Test
    public void testGetWrappedValue_NegativeNumbers() {
        int result = FieldUtils.getWrappedValue(-10, 5, -10, -1);
        assertEquals(-5, result);
    }

    @Test
    public void testEquals() {
        boolean result = FieldUtils.equals("Hello", "Hello");
        assertEquals(true, result);
    }

    @Test
    public void testEquals_ObjectIsNull() {
        boolean result = FieldUtils.equals(null, "Hello");
        assertEquals(false, result);
    }

    @Test
    public void testEquals_Object1IsNull() {
        boolean result = FieldUtils.equals(null, null);
        assertEquals(true, result);
    }
}