import static org.junit.Assert.*;
import org.junit.Test;

public class CoreOperationGreaterThanTest {

    // Test computeValue method
    @Test
    public void testComputeValue() {
        CoreOperationGreaterThan operation = new CoreOperationGreaterThan(new Expression(), new Expression());
        EvalContext context = new EvalContext();

        // Test when first argument is greater than second argument
        Expression arg1 = new Expression(5);
        Expression arg2 = new Expression(3);
        operation.setArgs(new Expression[] {arg1, arg2});
        assertTrue((boolean) operation.computeValue(context));

        // Test when first argument is equal to second argument
        arg1 = new Expression(5);
        arg2 = new Expression(5);
        operation.setArgs(new Expression[] {arg1, arg2});
        assertFalse((boolean) operation.computeValue(context));

        // Test when first argument is less than second argument
        arg1 = new Expression(3);
        arg2 = new Expression(5);
        operation.setArgs(new Expression[] {arg1, arg2});
        assertFalse((boolean) operation.computeValue(context));

        // Test when first argument is not a number
        arg1 = new Expression("5");
        arg2 = new Expression(3);
        operation.setArgs(new Expression[] {arg1, arg2});
        assertFalse((boolean) operation.computeValue(context));

        // Test when second argument is not a number
        arg1 = new Expression(5);
        arg2 = new Expression("3");
        operation.setArgs(new Expression[] {arg1, arg2});
        assertFalse((boolean) operation.computeValue(context));

        // Test when both arguments are not numbers
        arg1 = new Expression("5");
        arg2 = new Expression("3");
        operation.setArgs(new Expression[] {arg1, arg2});
        assertFalse((boolean) operation.computeValue(context));
    }

    // Test getSymbol method
    @Test
    public void testGetSymbol() {
        CoreOperationGreaterThan operation = new CoreOperationGreaterThan(new Expression(), new Expression());

        assertEquals(">", operation.getSymbol());
    }
}