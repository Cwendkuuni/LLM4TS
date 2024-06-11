import static org.junit.Assert.*;
import org.junit.Test;

public class FieldUtilsTest {

    @Test
    public void testSafeNegate() {
        int result = FieldUtils.safeNegate(5);
        assertEquals(-5, result);
        
        try {
            FieldUtils.safeNegate(Integer.MIN_VALUE);
            fail("ArithmeticException should be thrown");
        } catch (ArithmeticException e) {
            // Exception expected
        }
    }
    
    @Test
    public void testSafeAdd() {
        int result = FieldUtils.safeAdd(5, 3);
        assertEquals(8, result);
        
        try {
            FieldUtils.safeAdd(Integer.MAX_VALUE, 1);
            fail("ArithmeticException should be thrown");
        } catch (ArithmeticException e) {
            // Exception expected
        }
    }
    
    @Test
    public void testSafeSubtract() {
        long result = FieldUtils.safeSubtract(10, 5);
        assertEquals(5, result);
        
        try {
            FieldUtils.safeSubtract(Long.MIN_VALUE, 1);
            fail("ArithmeticException should be thrown");
        } catch (ArithmeticException e) {
            // Exception expected
        }
    }
    
    @Test
    public void testSafeMultiply() {
        int result = FieldUtils.safeMultiply(5, 3);
        assertEquals(15, result);
        
        try {
            FieldUtils.safeMultiply(Integer.MAX_VALUE, 2);
            fail("ArithmeticException should be thrown");
        } catch (ArithmeticException e) {
            // Exception expected
        }
    }
    
    @Test
    public void testSafeToInt() {
        int result = FieldUtils.safeToInt(1234567889);
        assertEquals(1234567889, result);
        
        try {
            FieldUtils.safeToInt(Long.MAX_VALUE);
            fail("ArithmeticException should be thrown");
        } catch (ArithmeticException e) {
            // Exception expected
        }
    }
    
    @Test
    public void testVerifyValueBounds() {
        try {
            FieldUtils.verifyValueBounds("test", 5, 1, 3);
            fail("IllegalFieldValueException should be thrown");
        } catch (IllegalFieldValueException e) {
            // Exception expected
        }
        
        try {
            FieldUtils.verifyValueBounds(null, 100, 1, 50);
            fail("IllegalFieldValueException should be thrown");
        } catch (IllegalFieldValueException e) {
            // Exception expected
        }
    }

    @Test
    public void testGetWrappedValue() {
        int result = FieldUtils.getWrappedValue(5, 1, 10);
        assertEquals(6, result);
        
        int result2 = FieldUtils.getWrappedValue(10, 8, 12);
        assertEquals(8, result2);
    }
    
    @Test
    public void testEquals() {
        assertTrue(FieldUtils.equals("test", "test"));
        assertFalse(FieldUtils.equals("test", "testing"));
        
        assertFalse(FieldUtils.equals(null, "test"));
        assertFalse(FieldUtils.equals("test", null));
        assertTrue(FieldUtils.equals(null, null));
    }
}