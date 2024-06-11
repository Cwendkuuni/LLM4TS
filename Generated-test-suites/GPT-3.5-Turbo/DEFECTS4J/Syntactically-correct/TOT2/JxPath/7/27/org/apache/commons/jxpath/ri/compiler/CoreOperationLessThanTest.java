import org.junit.Test;
import static org.junit.Assert.*;

public class CoreOperationLessThanTest {

    @Test
    public void testComputeValue() {
        // typical case
        CoreOperationLessThan operation = new CoreOperationLessThan(new Literal(5), new Literal(10));
        EvalContext context = new EvalContext();
        assertTrue((boolean) operation.computeValue(context));

        // edge case
        operation = new CoreOperationLessThan(new Literal(10), new Literal(10));
        assertFalse((boolean) operation.computeValue(context));
    }

    @Test
    public void testGetSymbol() {
        CoreOperationLessThan operation = new CoreOperationLessThan(new Literal(5), new Literal(10));
        assertEquals("<", operation.getSymbol());
    }

}