import org.junit.Test;
import static org.junit.Assert.*;

public class CoreOperationLessThanTest {

    @Test
    public void testComputeValueLessThan() {
        CoreOperationLessThan lessThan = new CoreOperationLessThan(new Literal(5), new Literal(10));
        EvalContext context = new EvalContext();
        assertEquals(Boolean.TRUE, lessThan.computeValue(context));
    }

    @Test
    public void testComputeValueNotLessThan() {
        CoreOperationLessThan lessThan = new CoreOperationLessThan(new Literal(10), new Literal(5));
        EvalContext context = new EvalContext();
        assertEquals(Boolean.FALSE, lessThan.computeValue(context));
    }

    @Test
    public void testGetSymbol() {
        CoreOperationLessThan lessThan = new CoreOperationLessThan(new Literal(3), new Literal(7));
        assertEquals("<", lessThan.getSymbol());
    }
}