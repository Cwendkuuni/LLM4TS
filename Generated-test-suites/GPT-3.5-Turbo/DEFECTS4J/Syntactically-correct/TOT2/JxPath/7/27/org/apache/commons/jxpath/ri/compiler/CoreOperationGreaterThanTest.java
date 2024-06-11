import org.junit.Test;
import static org.junit.Assert.*;

public class CoreOperationGreaterThanTest {

    @Test
    public void testComputeValue() {
        CoreOperationGreaterThan operation = new CoreOperationGreaterThan(new Constant(5), new Constant(3));
        EvalContext context = new EvalContext(null);

        assertTrue((Boolean) operation.computeValue(context));

        operation = new CoreOperationGreaterThan(new Constant(3), new Constant(5));

        assertFalse((Boolean) operation.computeValue(context));
    }

    @Test
    public void testGetSymbol() {
        CoreOperationGreaterThan operation = new CoreOperationGreaterThan(new Constant(5), new Constant(3));

        assertEquals(">", operation.getSymbol());
    }
    
    // Edge cases and exception handling tests can be added as needed

}