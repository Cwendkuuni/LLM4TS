import org.junit.Test;
import static org.junit.Assert.*;

public class CoreOperationLessThanTest {

    @Test
    public void testComputeValue() {
        CoreOperationLessThan expression = new CoreOperationLessThan(new Expression(), new Expression());
        EvalContext context = new EvalContext();
        Object result = expression.computeValue(context);
        assertEquals(Boolean.class, result.getClass());
    }

    @Test
    public void testGetSymbol() {
        CoreOperationLessThan expression = new CoreOperationLessThan(new Expression(), new Expression());
        String symbol = expression.getSymbol();
        assertEquals("<", symbol);
    }
}