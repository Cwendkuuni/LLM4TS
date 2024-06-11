import static org.junit.Assert.*;
import org.junit.Test;

public class CoreOperationLessThanTest {

    @Test
    public void testComputeValue() {
        CoreOperationLessThan operation = new CoreOperationLessThan(new Constant(5), new Constant(10));
        EvalContext context = new EvalContext();
        
        // Typical use case
        assertEquals(Boolean.TRUE, operation.computeValue(context));
        
        // Edge case
        operation = new CoreOperationLessThan(new Constant(-5), new Constant(0));
        assertEquals(Boolean.TRUE, operation.computeValue(context));
        
        // Error scenario
        operation = new CoreOperationLessThan(new Constant("error"), new Constant(10));
        assertEquals(Boolean.FALSE, operation.computeValue(context));
    }

    @Test
    public void testGetSymbol() {
        CoreOperationLessThan operation = new CoreOperationLessThan(new Constant(5), new Constant(10));
        
        // Typical use case
        assertEquals("<", operation.getSymbol());
        
        // Edge case
        operation = new CoreOperationLessThan(new Constant(-5), new Constant(0));
        assertEquals("<", operation.getSymbol());
        
        // Error scenario - Not applicable

    }

}