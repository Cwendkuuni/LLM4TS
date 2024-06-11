import org.junit.Test;
import static org.junit.Assert.*;

public class FieldUtilsTest {

    @Test
    public void testSafeNegate_validValue_returnsNegatedValue() {
        assertEquals(-5, FieldUtils.safeNegate(5));
    }

    @Test(expected = ArithmeticException.class)
    public void testSafeNegate_minValue_throwsArithmeticException() {
        FieldUtils.safeNegate(Integer.MIN_VALUE);
    }

    @Test
    public void testSafeAdd_validValues_returnsSum() {
        assertEquals(10, FieldUtils.safeAdd(5, 5));
    }

    @Test(expected = ArithmeticException.class)
    public void testSafeAdd_positiveOverflowValue_throwsArithmeticException() {
        FieldUtils.safeAdd(Integer.MAX_VALUE, 1);
    }

    @Test(expected = ArithmeticException.class)
    public void testSafeAdd_negativeOverflowValue_throwsArithmeticException() {
        FieldUtils.safeAdd(Integer.MIN_VALUE, -1);
    }

    @Test
    public void testSafeAddLong_validValues_returnsSum() {
        assertEquals(10L, FieldUtils.safeAdd(5L, 5L));
    }

    @Test(expected = ArithmeticException.class)
    public void testSafeAddLong_positiveOverflowValue_throwsArithmeticException() {
        FieldUtils.safeAdd(Long.MAX_VALUE, 1L);
    }

    @Test(expected = ArithmeticException.class)
    public void testSafeAddLong_negativeOverflowValue_throwsArithmeticException() {
        FieldUtils.safeAdd(Long.MIN_VALUE, -1L);
    }

    @Test
    public void testSafeSubtract_validValues_returnsDifference() {
        assertEquals(5L, FieldUtils.safeSubtract(10L, 5L));
    }

    @Test(expected = ArithmeticException.class)
    public void testSafeSubtract_positiveOverflowValue_throwsArithmeticException() {
        FieldUtils.safeSubtract(Long.MIN_VALUE, 1L);
    }

    @Test
    public void testSafeMultiply_validValues_returnsProduct() {
        assertEquals(20, FieldUtils.safeMultiply(5, 4));
    }

    @Test(expected = ArithmeticException.class)
    public void testSafeMultiply_overflowValue_throwsArithmeticException() {
        FieldUtils.safeMultiply(Integer.MAX_VALUE, 2);
    }

    @Test
    public void testSafeMultiplyLong_validValues_returnsProduct() {
        assertEquals(20L, FieldUtils.safeMultiply(5L, 4L));
    }

    @Test(expected = ArithmeticException.class)
    public void testSafeMultiplyLong_overflowValue_throwsArithmeticException() {
        FieldUtils.safeMultiply(Long.MAX_VALUE, 2L);
    }

    @Test
    public void testSafeMultiplyLong_negativeValue_returnsNegativeProduct() {
        assertEquals(-20L, FieldUtils.safeMultiply(5L, -4L));
    }

    @Test
    public void testSafeMultiplyLong_zeroValue_returnsZeroProduct() {
        assertEquals(0L, FieldUtils.safeMultiply(5L, 0L));
    }

    @Test
    public void testSafeToInt_validValue_returnsIntValue() {
        assertEquals(5, FieldUtils.safeToInt(5L));
    }

    @Test(expected = ArithmeticException.class)
    public void testSafeToInt_overflowValue_throwsArithmeticException() {
        FieldUtils.safeToInt(Long.MAX_VALUE);
    }

    @Test
    public void testSafeMultiplyToInt_validValues_returnsIntProduct() {
        assertEquals(20, FieldUtils.safeMultiplyToInt(5L, 4L));
    }

    @Test(expected = ArithmeticException.class)
    public void testSafeMultiplyToInt_overflowValue_throwsArithmeticException() {
        FieldUtils.safeMultiplyToInt(Long.MAX_VALUE, 2L);
    }

    @Test(expected = IllegalFieldValueException.class)
    public void testVerifyValueBounds_valueBelowLowerBound_throwsIllegalFieldValueException() {
        FieldUtils.verifyValueBounds("field", 10, 15, 20);
    }

    @Test(expected = IllegalFieldValueException.class)
    public void testVerifyValueBounds_valueAboveUpperBound_throwsIllegalFieldValueException() {
        FieldUtils.verifyValueBounds("field", 25, 15, 20);
    }

    @Test
    public void testVerifyValueBounds_validValue_doesNotThrowException() {
        FieldUtils.verifyValueBounds("field", 17, 15, 20);
    }

    @Test
    public void testGetWrappedValue_positiveValues_returnsWrappedValueWithinRange() {
        assertEquals(2, FieldUtils.getWrappedValue(13, 10, 20));
    }

    @Test
    public void testGetWrappedValue_negativeValues_returnsWrappedValueWithinRange() {
        assertEquals(18, FieldUtils.getWrappedValue(-7, 10, 20));
    }

    @Test
    public void testEquals_equalObjects_returnsTrue() {
        Object obj1 = new Object();
        Object obj2 = obj1;
        assertTrue(FieldUtils.equals(obj1, obj2));
    }

    @Test
    public void testEquals_notEqualObjects_returnsFalse() {
        Object obj1 = new Object();
        Object obj2 = new Object();
        assertFalse(FieldUtils.equals(obj1, obj2));
    }

}