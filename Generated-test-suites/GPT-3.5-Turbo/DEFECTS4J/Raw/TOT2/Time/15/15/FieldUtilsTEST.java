import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class FieldUtilsTest {

    @Before
    public void setUp() {
        // Setup code here
    }

    @Test
    public void testSafeNegate() {
        assertEquals("Negating positive value should return negative value", -5, FieldUtils.safeNegate(5));
        assertEquals("Negating negative value should return positive value", 5, FieldUtils.safeNegate(-5));
        try {
            FieldUtils.safeNegate(Integer.MIN_VALUE);
            fail("Expected ArithmeticException for negating Integer.MIN_VALUE");
        } catch (ArithmeticException e) {
            // Expected
        }
    }

    @Test
    public void testSafeAdd() {
        assertEquals("Adding two positive values should return correct sum", 7, FieldUtils.safeAdd(3, 4));
        assertEquals("Adding positive and negative value should return correct sum", -1, FieldUtils.safeAdd(3, -4));
        try {
            FieldUtils.safeAdd(Integer.MAX_VALUE, 1);
            fail("Expected ArithmeticException for overflow during addition");
        } catch (ArithmeticException e) {
            // Expected
        }
    }

    // Additional test cases for other methods

}