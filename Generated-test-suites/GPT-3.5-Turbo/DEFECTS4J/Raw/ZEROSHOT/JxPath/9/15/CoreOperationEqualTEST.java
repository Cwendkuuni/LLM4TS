import static org.junit.Assert.*;
import org.junit.Test;

public class CoreOperationEqualTest {

    @Test
    public void testComputeValue() {
        CoreOperationEqual operationEqual = new CoreOperationEqual(new Expression(), new Expression());
        
        EvalContext context = new EvalContext();
        assertEquals(Boolean.TRUE, operationEqual.computeValue(context));
    }
    
    @Test
    public void testGetSymbol() {
        CoreOperationEqual operationEqual = new CoreOperationEqual(new Expression(), new Expression());
        
        assertEquals("=", operationEqual.getSymbol());
    }
}