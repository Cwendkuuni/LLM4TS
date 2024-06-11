import static org.junit.Assert.*;
import org.junit.Test;

public class CoreOperationEqualTest {
    
    @Test
    public void testComputeValue() {
        CoreOperationEqual coreOperationEqual = new CoreOperationEqual(arg1, arg2);
        
        // Test when args[0] and args[1] are equal
        EvalContext context1 = new EvalContext();
        context1.setArg(arg1, "value");
        context1.setArg(arg2, "value");
        assertEquals(Boolean.TRUE, coreOperationEqual.computeValue(context1));
        
        // Test when args[0] and args[1] are not equal
        EvalContext context2 = new EvalContext();
        context2.setArg(arg1, "value1");
        context2.setArg(arg2, "value2");
        assertEquals(Boolean.FALSE, coreOperationEqual.computeValue(context2));
    }
    
    @Test
    public void testGetSymbol() {
        CoreOperationEqual coreOperationEqual = new CoreOperationEqual(arg1, arg2);
        
        assertEquals("=", coreOperationEqual.getSymbol());
    }
}