import org.junit.Test;
import static org.junit.Assert.*;

public class CoreOperationGreaterThanTest {

    @Test
    public void testComputeValue() {
        CoreOperationGreaterThan coreOperationGreaterThan = new CoreOperationGreaterThan(new Expression(5), new Expression(3));
        EvalContext context = new EvalContext();
        Object result = coreOperationGreaterThan.computeValue(context);
        assertTrue((boolean) result);
    }

    @Test
    public void testGetSymbol() {
        CoreOperationGreaterThan coreOperationGreaterThan = new CoreOperationGreaterThan(new Expression(2), new Expression(2));
        assertEquals(">", coreOperationGreaterThan.getSymbol());
    }

    @Test
    public void testEdgeCases() {
        // Edge case: comparing two negative numbers
        CoreOperationGreaterThan coreOperationGreaterThan = new CoreOperationGreaterThan(new Expression(-5), new Expression(-3));
        EvalContext context = new EvalContext();
        Object result = coreOperationGreaterThan.computeValue(context);
        assertTrue(!(boolean) result);
        
        // Edge case: comparing two decimal numbers
        CoreOperationGreaterThan coreOperationGreaterThan2 = new CoreOperationGreaterThan(new Expression(5.5), new Expression(5.2));
        EvalContext context2 = new EvalContext();
        Object result2 = coreOperationGreaterThan2.computeValue(context2);
        assertTrue((boolean) result2);
    }

    @Test(expected = NullPointerException.class)
    public void testNullArguments() {
        CoreOperationGreaterThan coreOperationGreaterThan = new CoreOperationGreaterThan(null, null);
    }

}