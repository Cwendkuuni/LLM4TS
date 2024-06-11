import static org.junit.Assert.*;
import org.junit.Test;

public class FieldUtilsTest {

    @Test
    public void testSafeNegate() {
        assertEquals(-5, FieldUtils.safeNegate(5));
        assertEquals(Integer.MIN_VALUE, FieldUtils.safeNegate(-Integer.MIN_VALUE));
        try {
            FieldUtils.safeNegate(Integer.MIN_VALUE);
            fail("ArithmeticException not thrown");
        } catch (ArithmeticException e) {
            // pass
        }
    }

    @Test
    public void testSafeAddInt() {
        assertEquals(10, FieldUtils.safeAdd(5, 5));
        try {
            FieldUtils.safeAdd(Integer.MAX_VALUE, 1);
            fail("ArithmeticException not thrown");
        } catch (ArithmeticException e) {
            // pass
        }
    }

    // Add more test cases for other methods following this pattern

}