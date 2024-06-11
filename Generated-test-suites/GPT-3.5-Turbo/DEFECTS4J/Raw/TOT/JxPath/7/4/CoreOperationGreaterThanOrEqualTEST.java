
import org.junit.Test;

import static org.junit.Assert.*;

public class CoreOperationGreaterThanOrEqualTest {

    @Test
    public void testComputeValue_typicalCase() {
        // Test with two equal non-zero integers
        CoreOperationGreaterThanOrEqual expression = new CoreOperationGreaterThanOrEqual(
                new Expression(new Integer(5)), new Expression(new Integer(5)));
        boolean result = (boolean) expression.computeValue(new EvalContext());
        assertTrue(result);

        // Test with two equal non-zero doubles
        expression = new CoreOperationGreaterThanOrEqual(
                new Expression(new Double(2.5)), new Expression(new Double(2.5)));
        result = (boolean) expression.computeValue(new EvalContext());
        assertTrue(result);

        // Test with two equal non-zero strings
        expression = new CoreOperationGreaterThanOrEqual(
                new Expression("abc"), new Expression("abc"));
        result = (boolean) expression.computeValue(new EvalContext());
        assertTrue(result);

        // Test with two equal non-zero characters
        expression = new CoreOperationGreaterThanOrEqual(
                new Expression('a'), new Expression('a'));
        result = (boolean) expression.computeValue(new EvalContext());
        assertTrue(result);
    }

    @Test
    public void testComputeValue_edgeCase() {
        // Test with two equal non-zero numbers of different types
        CoreOperationGreaterThanOrEqual expression = new CoreOperationGreaterThanOrEqual(
                new Expression(new Integer(5)), new Expression(new Double(5)));
        boolean result = (boolean) expression.computeValue(new EvalContext());
        assertTrue(result);

        // Test with two equal zero integers
        expression = new CoreOperationGreaterThanOrEqual(
                new Expression(new Integer(0)), new Expression(new Integer(0)));
        result = (boolean) expression.computeValue(new EvalContext());
        assertTrue(result);

        // Test with two equal zero doubles
        expression = new CoreOperationGreaterThanOrEqual(
                new Expression(new Double(0)), new Expression(new Double(0)));
        result = (boolean) expression.computeValue(new EvalContext());
        assertTrue(result);

        // Test with two equal zero strings
        expression = new CoreOperationGreaterThanOrEqual(
                new Expression(""), new Expression(""));
        result = (boolean) expression.computeValue(new EvalContext());
        assertTrue(result);

        // Test with two equal zero characters
        expression = new CoreOperationGreaterThanOrEqual(
                new Expression('\0'), new Expression('\0'));
        result = (boolean) expression.computeValue(new EvalContext());
        assertTrue(result);
    }

    @Test
    public void testComputeValue_errorScenario() {
        // Test with non-comparable types
        CoreOperationGreaterThanOrEqual expression = new CoreOperationGreaterThanOrEqual(
                new Expression(new Integer(5)), new Expression("abc"));
        boolean result = (boolean) expression.computeValue(new EvalContext());
        assertFalse(result);
    }

    @Test
    public void testGetSymbol() {
        CoreOperationGreaterThanOrEqual expression = new CoreOperationGreaterThanOrEqual(
                new Expression(new Integer(5)), new Expression(new Integer(3)));
        String symbol = expression.getSymbol();
        assertEquals(">=", symbol);
    }
}
