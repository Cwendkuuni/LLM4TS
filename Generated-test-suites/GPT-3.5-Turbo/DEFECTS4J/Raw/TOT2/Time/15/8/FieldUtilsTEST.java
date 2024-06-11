import static org.junit.Assert.*;
import org.junit.Test;

public class FieldUtilsTest {

    @Test
    public void testSafeNegate() {
        assertEquals(-5, FieldUtils.safeNegate(5));
        assertEquals(0, FieldUtils.safeNegate(0));
    }

    @Test
    public void testSafeAdd() {
        assertEquals(10, FieldUtils.safeAdd(5, 5));
        assertEquals(-10, FieldUtils.safeAdd(-5, -5));
        assertThrows(ArithmeticException.class, () -> {
            FieldUtils.safeAdd(Integer.MAX_VALUE, 1);
        });
    }

    @Test
    public void testSafeSubtract() {
        assertEquals(5, FieldUtils.safeSubtract(10, 5));
        assertThrows(ArithmeticException.class, () -> {
            FieldUtils.safeSubtract(Long.MIN_VALUE, 1);
        });
    }

    @Test
    public void testSafeMultiply() {
        assertEquals(25, FieldUtils.safeMultiply(5, 5));
        assertEquals(-25, FieldUtils.safeMultiply(-5, 5));
        assertThrows(ArithmeticException.class, () -> {
            FieldUtils.safeMultiply(Integer.MAX_VALUE, 2);
        });
    }

    @Test
    public void testSafeToInt() {
        assertEquals(5, FieldUtils.safeToInt(5));
        assertEquals(0, FieldUtils.safeToInt(0));
        assertThrows(ArithmeticException.class, () -> {
            FieldUtils.safeToInt(Long.MAX_VALUE);
        });
    }

    @Test
    public void testVerifyValueBounds() {
        DateTimeFieldType fieldType = new DateTimeFieldType("test");
        assertDoesNotThrow(() -> {
            FieldUtils.verifyValueBounds(fieldType, 5, 0, 10);
        });
        assertThrows(IllegalFieldValueException.class, () -> {
            FieldUtils.verifyValueBounds(fieldType, 15, 0, 10);
        });
    }

    @Test
    public void testGetWrappedValue() {
        assertEquals(5, FieldUtils.getWrappedValue(15, 10, 20));
        assertEquals(10, FieldUtils.getWrappedValue(30, 10, 20));
        assertEquals(15, FieldUtils.getWrappedValue(5, 10, 20));
    }

    @Test
    public void testEquals() {
        assertTrue(FieldUtils.equals("test", "test"));
        assertFalse(FieldUtils.equals("test", null));
        assertFalse(FieldUtils.equals(null, "test"));
    }
}