import org.joda.time.DateTimeFieldType;
import org.joda.time.IllegalFieldValueException;
import org.junit.Assert;
import org.junit.Test;

public class FieldUtilsTest {
    
    // Test safeNegate(int value) method
    @Test
    public void testSafeNegate() {
        int value = 10;
        int result = FieldUtils.safeNegate(value);
        Assert.assertEquals(-10, result);
    }
    
    // Test safeAdd(int val1, int val2) method
    @Test
    public void testSafeAdd() {
        int val1 = 5;
        int val2 = 10;
        int result = FieldUtils.safeAdd(val1, val2);
        Assert.assertEquals(15, result);
    }
    
    // Test safeAdd(long val1, long val2) method
    @Test
    public void testSafeAddLong() {
        long val1 = 5000000000L;
        long val2 = 1000000000L;
        long result = FieldUtils.safeAdd(val1, val2);
        Assert.assertEquals(6000000000L, result);
    }
    
    // Test safeSubtract(long val1, long val2) method
    @Test
    public void testSafeSubtract() {
        long val1 = 10000000000L;
        long val2 = 5000000000L;
        long result = FieldUtils.safeSubtract(val1, val2);
        Assert.assertEquals(5000000000L, result);
    }
    
    // Test safeMultiply(int val1, int val2) method
    @Test
    public void testSafeMultiply() {
        int val1 = 5;
        int val2 = 10;
        int result = FieldUtils.safeMultiply(val1, val2);
        Assert.assertEquals(50, result);
    }
    
    // Test safeMultiply(long val1, int val2) method
    @Test
    public void testSafeMultiplyLong() {
        long val1 = 5000000000L;
        int val2 = 2;
        long result = FieldUtils.safeMultiply(val1, val2);
        Assert.assertEquals(10000000000L, result);
    }
    
    // Test safeMultiply(long val1, long val2) method
    @Test
    public void testSafeMultiplyLongLong() {
        long val1 = 5000000000L;
        long val2 = 2L;
        long result = FieldUtils.safeMultiply(val1, val2);
        Assert.assertEquals(10000000000L, result);
    }
    
    // Test safeToInt(long value) method
    @Test
    public void testSafeToInt() {
        long value = 100;
        int result = FieldUtils.safeToInt(value);
        Assert.assertEquals(100, result);
    }
    
    // Test safeMultiplyToInt(long val1, long val2) method
    @Test
    public void testSafeMultiplyToInt() {
        long val1 = 5000000000L;
        long val2 = 2L;
        int result = FieldUtils.safeMultiplyToInt(val1, val2);
        Assert.assertEquals(10000000000L, result);
    }
    
    // Test verifyValueBounds(DateTimeField field, int value, int lowerBound, int upperBound) method
    @Test
    public void testVerifyValueBoundsDateTimeField() {
        DateTimeFieldType fieldType = DateTimeFieldType.hourOfDay();
        int value = 5;
        int lowerBound = 0;
        int upperBound = 10;
        FieldUtils.verifyValueBounds(fieldType, value, lowerBound, upperBound);
    }
    
    // Test verifyValueBounds(DateTimeFieldType fieldType, int value, int lowerBound, int upperBound) method
    @Test
    public void testVerifyValueBoundsDateTimeFieldType() {
        DateTimeFieldType fieldType = DateTimeFieldType.hourOfDay();
        int value = 15;
        int lowerBound = 0;
        int upperBound = 12;
        try {
            FieldUtils.verifyValueBounds(fieldType, value, lowerBound, upperBound);
            Assert.fail("Expected IllegalFieldValueException was not thrown");
        } catch (IllegalFieldValueException e) {
            Assert.assertEquals(fieldType, e.getFieldType());
        }
    }
    
    // Test verifyValueBounds(String fieldName, int value, int lowerBound, int upperBound) method
    @Test
    public void testVerifyValueBoundsString() {
        String fieldName = "hour";
        int value = 5;
        int lowerBound = 0;
        int upperBound = 10;
        FieldUtils.verifyValueBounds(fieldName, value, lowerBound, upperBound);
    }
    
    // Test getWrappedValue(int currentValue, int wrapValue, int minValue, int maxValue) method
    @Test
    public void testGetWrappedValue() {
        int currentValue = 8;
        int wrapValue = 4;
        int minValue = 2;
        int maxValue = 10;
        int result = FieldUtils.getWrappedValue(currentValue, wrapValue, minValue, maxValue);
        Assert.assertEquals(2, result);
    }
    
    // Test getWrappedValue(int value, int minValue, int maxValue) method
    @Test
    public void testGetWrappedValueWithMinAndMaxValue() {
        int value = 12;
        int minValue = 10;
        int maxValue = 15;
        int result = FieldUtils.getWrappedValue(value, minValue, maxValue);
        Assert.assertEquals(10, result);
    }
    
    // Test equals(Object object1, Object object2) method
    @Test
    public void testEquals() {
        Object object1 = "Hello";
        Object object2 = "Hello";
        boolean result = FieldUtils.equals(object1, object2);
        Assert.assertTrue(result);
    }
    
}