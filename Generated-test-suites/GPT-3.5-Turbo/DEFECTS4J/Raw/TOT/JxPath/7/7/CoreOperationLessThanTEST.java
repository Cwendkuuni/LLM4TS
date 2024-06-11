import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class CoreOperationLessThanTest {

    private CoreOperationLessThan operationLessThan;

    @Before
    public void setUp() {
        // Create a new instance of CoreOperationLessThan for each test
        operationLessThan = new CoreOperationLessThan();
    }

    @Test
    public void testComputeValueTypicalCase() {
        // Typical case: compare two numbers where the left operand is less than the right operand
        EvalContext context = new EvalContext();
        double l = 5.5;
        double r = 10.0;
        operationLessThan.setOperands(new Expression[] { new Expression(l), new Expression(r) });

        // Verify that the computeValue method returns Boolean.TRUE
        assertEquals(Boolean.TRUE, operationLessThan.computeValue(context));
    }

    @Test
    public void testComputeValueEdgeCase() {
        // Edge case: compare two numbers where the left operand is equal to the right operand
        EvalContext context = new EvalContext();
        double l = 5.5;
        double r = 5.5;
        operationLessThan.setOperands(new Expression[] { new Expression(l), new Expression(r) });

        // Verify that the computeValue method returns Boolean.FALSE
        assertEquals(Boolean.FALSE, operationLessThan.computeValue(context));
    }

    @Test
    public void testComputeValueErrorScenario() {
        // Error scenario: compare two non-numeric values
        EvalContext context = new EvalContext();
        String l = "abc";
        String r = "def";
        operationLessThan.setOperands(new Expression[] { new Expression(l), new Expression(r) });

        // Verify that the computeValue method throws a NumberFormatException
        try {
            operationLessThan.computeValue(context);
            fail("Expected NumberFormatException to be thrown");
        } catch (NumberFormatException e) {
            // Exception is expected, test passes
        }
    }

    @Test
    public void testGetSymbol() {
        // Verify that the getSymbol method returns the correct symbol "<"
        assertEquals("<", operationLessThan.getSymbol());
    }
}