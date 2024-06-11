import org.junit.Test;
import static org.junit.Assert.*;

public class CoreOperationLessThanTest {

    @Test
    public void testComputeValue() {
        EvalContext context = new EvalContext();

        Expression arg1 = new Expression();
        arg1.setValue(5); // Set arg1 value
        Expression arg2 = new Expression();
        arg2.setValue(10); // Set arg2 value

        CoreOperationLessThan operation = new CoreOperationLessThan(arg1, arg2);

        Object result = operation.computeValue(context);

        assertTrue((Boolean) result); // Check if result is true
    }

    @Test
    public void testGetSymbol() {
        Expression arg1 = new Expression();
        Expression arg2 = new Expression();

        CoreOperationLessThan operation = new CoreOperationLessThan(arg1, arg2);

        String symbol = operation.getSymbol();

        assertEquals("<", symbol); // Symbol should be less than sign
    }
}