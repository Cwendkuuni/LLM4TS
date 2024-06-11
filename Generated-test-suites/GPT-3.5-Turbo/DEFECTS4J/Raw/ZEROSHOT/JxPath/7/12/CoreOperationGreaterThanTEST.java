import static org.junit.Assert.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class CoreOperationGreaterThanTest {

    private CoreOperationGreaterThan operationGreaterThan;

    @Before
    public void setUp() {
        operationGreaterThan = new CoreOperationGreaterThan(null, null);
    }

    @After
    public void tearDown() {
        operationGreaterThan = null;
    }

    @Test
    public void testComputeValue() {
        EvalContext context = new EvalContext();
        double arg1 = 5.0;
        double arg2 = 3.0;
        CoreOperationGreaterThan expression = new CoreOperationGreaterThan(null, null);
        expression.args[0] = new Expression() {
            public Object computeValue(EvalContext context) {
                return arg1;
            }
        };
        expression.args[1] = new Expression() {
            public Object computeValue(EvalContext context) {
                return arg2;
            }
        };

        Object result = expression.computeValue(context);

        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result);
    }

    @Test
    public void testGetSymbol() {
        CoreOperationGreaterThan expression = new CoreOperationGreaterThan(null, null);
        String symbol = expression.getSymbol();

        assertEquals(">", symbol);
    }
}