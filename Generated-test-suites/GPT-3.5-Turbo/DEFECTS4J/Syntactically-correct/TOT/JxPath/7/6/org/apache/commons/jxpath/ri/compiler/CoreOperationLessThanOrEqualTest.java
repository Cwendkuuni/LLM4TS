import org.junit.Test;
import static org.junit.Assert.*;

public class CoreOperationLessThanOrEqualTest {

    @Test
    public void testComputeValue() {
        // Typical use case
        Expression arg1 = new Expression();
        Expression arg2 = new Expression();
        CoreOperationLessThanOrEqual operation = new CoreOperationLessThanOrEqual(arg1, arg2);
        EvalContext context = new EvalContext();
        arg1.setValue(10);
        arg2.setValue(20);
        assertTrue((boolean) operation.computeValue(context));

        // Edge case
        arg1.setValue(20);
        arg2.setValue(20);
        assertTrue((boolean) operation.computeValue(context));

        // Error scenario
        arg1.setValue(30);
        arg2.setValue(20);
        assertFalse((boolean) operation.computeValue(context));
    }

    @Test
    public void testGetSymbol() {
        Expression arg1 = new Expression();
        Expression arg2 = new Expression();
        CoreOperationLessThanOrEqual operation = new CoreOperationLessThanOrEqual(arg1, arg2);
        assertEquals("<=", operation.getSymbol());
    }
}