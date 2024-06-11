import static org.junit.Assert.*;
import org.junit.Test;

public class CoreOperationCompareTest {

    @Test
    public void testEqual() {
        // Test for equal values
        CoreOperationCompare compare = new CoreOperationCompare(arg1, arg2);
        boolean result = compare.equal(context, left, right);
        assertTrue(result);
    }

    @Test
    public void testNotEqual() {
        // Test for not equal values
        CoreOperationCompare compare = new CoreOperationCompare(arg1, arg2);
        Expression left = new Expression(value1);
        Expression right = new Expression(value2);
        boolean result = compare.equal(context, left, right);
        assertFalse(result);
    }

    @Test
    public void testExceptionHandling() {
        // Test for exception handling when computing values
        CoreOperationCompare compare = new CoreOperationCompare(arg1, arg2);
        Expression left = new Expression(null);
        Expression right = new Expression(null);
        boolean result = compare.equal(context, left, right);
        // Handle the exception
        assertEquals(false, result);
    }

    @Test
    public void testEdgeCases() {
        // Test for edge cases like NaN values
        CoreOperationCompare compare = new CoreOperationCompare(arg1, arg2);
        Expression left = new Expression(Double.NaN);
        Expression right = new Expression(Double.NaN);
        boolean result = compare.equal(context, left, right);
        assertTrue(result);
    }
    
}