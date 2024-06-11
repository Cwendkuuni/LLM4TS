import org.junit.Test;
import static org.junit.Assert.*;

public class FieldUtilsTest {
    
    @Test
    public void testSafeNegate() {
        assertEquals(-5, FieldUtils.safeNegate(5));
        assertEquals(0, FieldUtils.safeNegate(0));
    }
    
    @Test
    public void testSafeAdd() {
        assertEquals(7, FieldUtils.safeAdd(3, 4));
        assertThrows(ArithmeticException.class, () -> FieldUtils.safeAdd(Integer.MAX_VALUE, 1));
        assertThrows(ArithmeticException.class, () -> FieldUtils.safeAdd(Integer.MIN_VALUE, -1));
    }
    
    @Test
    public void testSafeMultiply() {
        assertEquals(15, FieldUtils.safeMultiply(3, 5));
        assertThrows(ArithmeticException.class, () -> FieldUtils.safeMultiply(Integer.MAX_VALUE, 2));
    }
    
    @Test
    public void testSafeToInt() {
        assertEquals(10, FieldUtils.safeToInt(10));
        assertThrows(ArithmeticException.class, () -> FieldUtils.safeToInt(Integer.MAX_VALUE + 5L));
    }
    
    @Test
    public void testVerifyValueBounds() {
        DateTimeField field = new DateTimeField(null, 1);
        assertEquals(5, FieldUtils.verifyValueBounds(field, 5, 1, 10));
        assertThrows(IllegalFieldValueException.class, () -> FieldUtils.verifyValueBounds(field, 15, 1, 10));
    }
    
    @Test
    public void testGetWrappedValue() {
        assertEquals(5, FieldUtils.getWrappedValue(12, 3, 1, 10));
        assertEquals(3, FieldUtils.getWrappedValue(1, 2, 1, 5));
        assertEquals(1, FieldUtils.getWrappedValue(10, -10, 1, 5));
    }
    
    @Test
    public void testEquals() {
        assertTrue(FieldUtils.equals("test", "test"));
        assertFalse(FieldUtils.equals("test", "test123"));
    }
}