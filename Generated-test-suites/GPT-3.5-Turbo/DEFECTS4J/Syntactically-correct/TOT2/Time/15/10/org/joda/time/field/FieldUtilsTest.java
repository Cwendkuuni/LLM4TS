import static org.junit.Assert.*;
import org.junit.Test;
import org.joda.time.DateTimeFieldType;
import org.joda.time.IllegalFieldValueException;
import org.joda.time.DateTimeField;

public class FieldUtilsTest {

    @Test
    public void testSafeNegate() {
        assertEquals(-5, FieldUtils.safeNegate(5));
        assertEquals(-100, FieldUtils.safeNegate(100));
        
        try {
            FieldUtils.safeNegate(Integer.MIN_VALUE);
            fail("Expected ArithmeticException to be thrown");
        } catch (ArithmeticException e) {
            // Expected
        }
    }

    @Test
    public void testSafeAdd() {
        assertEquals(5, FieldUtils.safeAdd(2, 3));
        assertEquals(-5, FieldUtils.safeAdd(-3, -2));
        
        try {
            FieldUtils.safeAdd(Integer.MAX_VALUE, 1);
            fail("Expected ArithmeticException to be thrown");
        } catch (ArithmeticException e) {
            // Expected
        }
    }

    // Additional test cases for the remaining methods can be added here

}