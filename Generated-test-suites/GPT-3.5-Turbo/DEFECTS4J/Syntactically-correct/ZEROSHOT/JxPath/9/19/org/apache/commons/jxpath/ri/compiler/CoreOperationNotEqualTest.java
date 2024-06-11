import org.junit.Test;
import static org.junit.Assert.*;

public class CoreOperationNotEqualTest {

    @Test
    public void testComputeValue() {
        // Test case for equal values
        {
            Expression arg1 = new Expression();
            Expression arg2 = new Expression();
            CoreOperationNotEqual operation = new CoreOperationNotEqual(arg1, arg2);
            EvalContext context = new EvalContext();

            arg1.setValue("value");
            arg2.setValue("value");

            Object result = operation.computeValue(context);

            assertFalse((boolean) result);
        }

        // Test case for different values
        {
            Expression arg1 = new Expression();
            Expression arg2 = new Expression();
            CoreOperationNotEqual operation = new CoreOperationNotEqual(arg1, arg2);
            EvalContext context = new EvalContext();

            arg1.setValue("value1");
            arg2.setValue("value2");

            Object result = operation.computeValue(context);

            assertTrue((boolean) result);
        }
    }

    @Test
    public void testGetSymbol() {
        Expression arg1 = new Expression();
        Expression arg2 = new Expression();
        CoreOperationNotEqual operation = new CoreOperationNotEqual(arg1, arg2);

        String symbol = operation.getSymbol();

        assertEquals("!=", symbol);
    }
}