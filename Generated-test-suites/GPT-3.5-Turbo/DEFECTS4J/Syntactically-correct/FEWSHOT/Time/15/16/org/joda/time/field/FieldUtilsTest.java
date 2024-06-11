import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.joda.time.DateTimeField;
import org.joda.time.LocalDateTime;
import org.joda.time.IllegalFieldValueException;
import org.joda.time.DateTimeFieldType;
import org.junit.Test;

public class FieldUtilsTest {
    
    @Test
    public void testSafeNegate() {
        assertEquals(-1, FieldUtils.safeNegate(1));
        
        try {
            FieldUtils.safeNegate(Integer.MIN_VALUE);
            fail("Expected ArithmeticException");
        } catch (ArithmeticException e) {
            assertEquals("Integer.MIN_VALUE cannot be negated", e.getMessage());
        }
    }
    
    @Test
    public void testSafeAdd() {
        assertEquals(3, FieldUtils.safeAdd(1, 2));
        
        try {
            FieldUtils.safeAdd(Integer.MAX_VALUE, 1);
            fail("Expected ArithmeticException");
        } catch (ArithmeticException e) {
            assertEquals("The calculation caused an overflow: " + Integer.MAX_VALUE + " + " + 1, e.getMessage());
        }
    }
    
    @Test
    public void testSafeMultiply() {
        assertEquals(4, FieldUtils.safeMultiply(2, 2));
        
        try {
            FieldUtils.safeMultiply(Integer.MAX_VALUE, Integer.MAX_VALUE);
            fail("Expected ArithmeticException");
        } catch (ArithmeticException e) {
            assertEquals("Multiplication overflows an int: " + Integer.MAX_VALUE + " * " + Integer.MAX_VALUE, e.getMessage());
        }
    }
    
    @Test
    public void testVerifyValueBounds() {
        DateTimeFieldType fieldType = DateTimeFieldType.dayOfMonth();
        DateTimeField field = fieldType.getField(new LocalDateTime().getChronology());

        try {
            FieldUtils.verifyValueBounds(field, 32, 1, 31);
            fail("Expected IllegalFieldValueException");
        } catch (IllegalFieldValueException e) {
            assertEquals("Value 32 for dayOfMonth must be in the range [1,31]", e.getMessage());
        }
        
        try {
            FieldUtils.verifyValueBounds(fieldType, 0, 1, 31);
            fail("Expected IllegalFieldValueException");
        } catch (IllegalFieldValueException e) {
            assertEquals("Value 0 for dayOfMonth must be in the range [1,31]", e.getMessage());
        }
        
        try {
            FieldUtils.verifyValueBounds("dayOfMonth", 5, 1, 4);
            fail("Expected IllegalFieldValueException");
        } catch (IllegalFieldValueException e) {
            assertEquals("Value 5 for dayOfMonth must be in the range [1,4]", e.getMessage());
        }
    }
    
    @Test
    public void testGetWrappedValue() {
        assertEquals(5, FieldUtils.getWrappedValue(5, 1, 5));
        assertEquals(1, FieldUtils.getWrappedValue(5, -5, 5));
        
        try {
            FieldUtils.getWrappedValue(5, 6, 5);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("MIN > MAX", e.getMessage());
        }
    }
    
    @Test
    public void testEquals() {
        assertTrue(FieldUtils.equals(null, null));
        assertFalse(FieldUtils.equals(null, "test"));
        assertFalse(FieldUtils.equals("test", null));
        assertTrue(FieldUtils.equals("test", "test"));
        assertFalse(FieldUtils.equals("test", "TEST"));
    }
}