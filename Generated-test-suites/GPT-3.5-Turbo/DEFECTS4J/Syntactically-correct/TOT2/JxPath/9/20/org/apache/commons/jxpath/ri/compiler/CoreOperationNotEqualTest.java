import static org.junit.Assert.*;
import org.junit.Test;

public class CoreOperationNotEqualTest {

    @Test
    public void testComputeValue() {
        Expression arg1 = new SomeExpression();
        Expression arg2 = new SomeOtherExpression();
        CoreOperationNotEqual operation = new CoreOperationNotEqual(arg1, arg2);
        EvalContext context = new EvalContext();

        // Typical case
        context.setValue(arg1, 10);
        context.setValue(arg2, 20);
        assertEquals(Boolean.TRUE, operation.computeValue(context));

        // Edge case
        context.setValue(arg1, "abc");
        context.setValue(arg2, "abc");
        assertEquals(Boolean.FALSE, operation.computeValue(context));

        // Error scenario
        try {
            context.setValue(arg1, null);
            context.setValue(arg2, 30);
            operation.computeValue(context);
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            // Test passed
        }
    }

    @Test
    public void testGetSymbol() {
        Expression arg1 = new SomeExpression();
        Expression arg2 = new SomeOtherExpression();
        CoreOperationNotEqual operation = new CoreOperationNotEqual(arg1, arg2);

        assertEquals("!=", operation.getSymbol());
    }
}