import static org.junit.Assert.*;
import org.junit.Test;

public class CoreOperationGreaterThanTest {

    @Test
    public void testComputeValue() {
        // Typical use case
        CoreOperationGreaterThan op = new CoreOperationGreaterThan(new Constant(5), new Constant(3));
        EvalContext context = new EvalContext();
        assertEquals(Boolean.TRUE, op.computeValue(context));
        
        // Edge case where values are equal
        op = new CoreOperationGreaterThan(new Constant(5), new Constant(5));
        assertEquals(Boolean.FALSE, op.computeValue(context));
    }

    @Test
    public void testGetSymbol() {
        CoreOperationGreaterThan op = new CoreOperationGreaterThan(new Constant(5), new Constant(3));
        assertEquals(">", op.getSymbol());
    }

    // Additional test cases for exception handling can be added here

}