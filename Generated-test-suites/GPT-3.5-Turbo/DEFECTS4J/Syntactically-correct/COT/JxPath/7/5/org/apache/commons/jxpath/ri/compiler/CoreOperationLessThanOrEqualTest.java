import static org.junit.Assert.*;
import org.junit.Test;

public class CoreOperationLessThanOrEqualTest {

    @Test
    public void testComputeValue() {
        CoreOperationLessThanOrEqual operation = new CoreOperationLessThanOrEqual(new Expression(), new Expression());

        // Test when both arguments are integers
        EvalContext context1 = new EvalContext();
        context1.setValue("arg1", 5);
        context1.setValue("arg2", 10);
        assertEquals(true, operation.computeValue(context1));

        // Test when the first argument is an integer and the second argument is a double
        EvalContext context2 = new EvalContext();
        context2.setValue("arg1", 5);
        context2.setValue("arg2", 5.0);
        assertEquals(true, operation.computeValue(context2));

        // Test when the first argument is a double and the second argument is an integer
        EvalContext context3 = new EvalContext();
        context3.setValue("arg1", 10.5);
        context3.setValue("arg2", 10);
        assertEquals(false, operation.computeValue(context3));

        // Test when both arguments are doubles
        EvalContext context4 = new EvalContext();
        context4.setValue("arg1", 10.5);
        context4.setValue("arg2", 5.0);
        assertEquals(false, operation.computeValue(context4));
    }

    @Test
    public void testGetSymbol() {
        CoreOperationLessThanOrEqual operation = new CoreOperationLessThanOrEqual(new Expression(), new Expression());
        assertEquals("<=", operation.getSymbol());
    }
}

