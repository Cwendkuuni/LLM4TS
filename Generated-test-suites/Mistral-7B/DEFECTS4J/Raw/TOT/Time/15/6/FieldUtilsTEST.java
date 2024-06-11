import org.junit.Test;
import org.joda.time.field.FieldUtils;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

public class FieldUtilsTest {

    @Test
    public void testSafeNegate() {
        // Test with positive values
        for (int i = 0; i <= Integer.MAX_VALUE; i++) {
            int result = FieldUtils.safeNegate(i);
            assertEquals(-i, result);
        }

        // Test with minimum int value
        int minInt = Integer.MIN_VALUE;
        assertThrows(ArithmeticException.class, () -> FieldUtils.safeNegate(minInt));
    }

    @Test
    public void testSafeAdd() {
        // Test with positive values
        for (int i = 0; i <= Integer.MAX_VALUE; i++) {
            for (int j = 0; j <= Integer.MAX_VALUE; j++) {
                int result = FieldUtils.safeAdd(i, j);
                assertEquals(i + j, result);
            }
        }

        // Test overflow
        int maxInt = Integer.MAX_VALUE;
        int minInt = Integer.MIN_VALUE;
        assertThrows(ArithmeticException.class, () -> FieldUtils.safeAdd(maxInt, minInt));
    }

    @Test
    public void testSafeMultiply() {
        // Test with positive values
        for (int i = 0; i <= Integer.MAX_VALUE; i++) {
            for (int j = 0; j <= Integer.MAX_VALUE; j++) {
                int result = FieldUtils.safeMultiply(i, j);
                assertEquals(i * j, result);
            }
        }

        // Test overflow
        int maxInt = Integer.MAX_VALUE;
        int minInt = Integer.MIN_VALUE;
        assertThrows(ArithmeticException.class, () -> FieldUtils.safeMultiply(maxInt, minInt));
    }

    @Test
    public void testSafeSubtract() {
        // Test with positive values
        for (int i = 0; i <= Integer.MAX_VALUE; i++) {
            for (int j = 0; j <= i; j++) {
                int result = FieldUtils.safeSubtract(i, j);
                assertEquals(i - j, result);
            }
        }

        // Test negative values
        int i = Integer.MIN_VALUE;
        int j = 1;
        int result = FieldUtils.safeSubtract(i, j);
        assertEquals(i - j, result);

        // Test overflow
        int maxInt = Integer.MAX_VALUE;
        int minInt = Integer.MIN_VALUE;
        assertThrows(ArithmeticException.class, () -> FieldUtils.safeSubtract(maxInt, minInt));
    }

    @Test
    public void testSafeToInt() {
        // Test valid long values
        for (long i = Long.MIN_VALUE; i <= Long.MAX_VALUE; i++) {
            int result = FieldUtils.safeToInt((int) i);
            assertEquals((int) i, result);
        }

        // Test invalid long values
        long maxLong = Long.MAX_VALUE;
        long minLong = Long.MIN_VALUE;
        assertThrows(ArithmeticException.class, () -> FieldUtils.safeToInt((int) maxLong));
        assertThrows(ArithmeticException.class, () -> FieldUtils.safeToInt((int) minLong));
    }

    @Test
    public void testVerifyValueBounds() {
        DateTimeField field = new DateTimeField(DateTimeFieldType.dayOfWeek(), "dayOfWeek");

        // Test valid values
        for (int i = 0; i <= field.getBounds().getMaximumValue(); i++) {
            FieldUtils.verifyValueBounds(field, i, 0, field.getBounds().getMaximumValue());
        }

        // Test invalid values
        for (int i = field.getBounds().getMinimumValue() - 1; i >= -1; i--) {
            assertThrows(IllegalFieldValueException.class, () -> FieldUtils.verifyValueBounds(field, i, 0, field.getBounds().getMaximumValue()));
        }
    }
}