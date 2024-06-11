import org.junit.Test;
import static org.junit.Assert.*;

public class CoreOperationGreaterThanTest {

    @Test
    public void testComputeValue() {
        EvalContext context = new EvalContext();
        CoreOperationGreaterThan expression = new CoreOperationGreaterThan(new Expression(), new Expression());
        Object result = expression.computeValue(context);
        assertTrue(result instanceof Boolean);
    }

    @Test
    public void testGetSymbol() {
        CoreOperationGreaterThan expression = new CoreOperationGreaterThan(new Expression(), new Expression());
        String symbol = expression.getSymbol();
        assertNotNull(symbol);
    }
}