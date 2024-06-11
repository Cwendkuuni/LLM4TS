import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class CoreOperationGreaterThanTest {

    private CoreOperationGreaterThan operation;

    @Before
    public void setUp() {
        Expression arg1 = new CustomExpression();
        Expression arg2 = new CustomExpression();
        operation = new CoreOperationGreaterThan(arg1, arg2);
    }

    @Test
    public void testComputeValue() {
        EvalContext context = new EvalContext();
        Object result = operation.computeValue(context);
        // Add assertions here to test the computeValue method
    }

    @Test
    public void testGetSymbol() {
        String symbol = operation.getSymbol();
        // Add assertions here to test the getSymbol method
    }

    private class CustomExpression extends Expression {

        public Object computeValue(EvalContext context) {
            // Add implementation here for custom expression computation
        }
    }
}