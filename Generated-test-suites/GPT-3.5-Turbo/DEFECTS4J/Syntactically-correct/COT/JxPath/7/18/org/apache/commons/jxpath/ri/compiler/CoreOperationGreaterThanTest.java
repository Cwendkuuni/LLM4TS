import static org.junit.Assert.*;
import org.junit.Test;

public class CoreOperationGreaterThanTest {

    @Test
    public void testConstructor() {
        // Create test instances
        Expression arg1 = new Expression();
        Expression arg2 = new Expression();
        CoreOperationGreaterThan operation = new CoreOperationGreaterThan(arg1, arg2);

        // Verify that arguments are correctly assigned
        assertEquals(arg1, operation.args[0]);
        assertEquals(arg2, operation.args[1]);
    }

    @Test
    public void testComputeValue() {
        // Create test instance
        CoreOperationGreaterThan operation = new CoreOperationGreaterThan(new Expression(), new Expression());

        // Provide a dummy EvalContext
        EvalContext context = new EvalContext();

        // Test greater than case
        context.setValue(args[0].computeValue(context), 5);
        context.setValue(args[1].computeValue(context), 3);
        assertEquals(Boolean.TRUE, operation.computeValue(context));

        // Test not greater than case
        context.setValue(args[0].computeValue(context), 3);
        context.setValue(args[1].computeValue(context), 5);
        assertEquals(Boolean.FALSE, operation.computeValue(context));
    }

    @Test
    public void testGetSymbol() {
        // Create test instance
        CoreOperationGreaterThan operation = new CoreOperationGreaterThan(new Expression(), new Expression());

        // Verify the symbol is ">"
        assertEquals(">", operation.getSymbol());
    }

    @Test
    public void testComputeValueWithEqualValues() {
        // Create test instance
        CoreOperationGreaterThan operation = new CoreOperationGreaterThan(new Expression(), new Expression());

        // Provide a dummy EvalContext
        EvalContext context = new EvalContext();

        // Test equal values
        context.setValue(args[0].computeValue(context), 5);
        context.setValue(args[1].computeValue(context), 5);
        assertEquals(Boolean.FALSE, operation.computeValue(context));
    }

    @Test
    public void testComputeValueWithNegativeValues() {
        // Create test instance
        CoreOperationGreaterThan operation = new CoreOperationGreaterThan(new Expression(), new Expression());

        // Provide a dummy EvalContext
        EvalContext context = new EvalContext();

        // Test negative values
        context.setValue(args[0].computeValue(context), -5);
        context.setValue(args[1].computeValue(context), -2);
        assertEquals(Boolean.FALSE, operation.computeValue(context));

        context.setValue(args[0].computeValue(context), -2);
        context.setValue(args[1].computeValue(context), -5);
        assertEquals(Boolean.TRUE, operation.computeValue(context));
    }

    @Test
    public void testComputeValueWithZeroValues() {
        // Create test instance
        CoreOperationGreaterThan operation = new CoreOperationGreaterThan(new Expression(), new Expression());

        // Provide a dummy EvalContext
        EvalContext context = new EvalContext();

        // Test zero values
        context.setValue(args[0].computeValue(context), 0);
        context.setValue(args[1].computeValue(context), 2);
        assertEquals(Boolean.FALSE, operation.computeValue(context));

        context.setValue(args[0].computeValue(context), 2);
        context.setValue(args[1].computeValue(context), 0);
        assertEquals(Boolean.TRUE, operation.computeValue(context));
    }
}