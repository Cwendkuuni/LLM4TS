import static org.junit.Assert.assertEquals;
import org.junit.Test;

public class CoreOperationLessThanTest {
    @Test
    public void testComputeValue() {
        CoreOperationLessThan operation = new CoreOperationLessThan(new Expression(5), new Expression(10));
        assertEquals(Boolean.TRUE, operation.computeValue(new EvalContext()));
        
        CoreOperationLessThan operation2 = new CoreOperationLessThan(new Expression(10), new Expression(5));
        assertEquals(Boolean.FALSE, operation2.computeValue(new EvalContext()));
    }

    @Test
    public void testGetSymbol() {
        CoreOperationLessThan operation = new CoreOperationLessThan(new Expression(5), new Expression(10));
        assertEquals("<", operation.getSymbol());
    }
}