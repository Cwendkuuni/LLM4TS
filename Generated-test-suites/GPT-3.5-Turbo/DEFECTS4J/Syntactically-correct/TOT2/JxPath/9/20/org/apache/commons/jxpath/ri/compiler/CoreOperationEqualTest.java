import org.junit.Test;
import static org.junit.Assert.*;

public class CoreOperationEqualTest {

    @Test
    public void testComputeValue() {
        CoreOperationEqual coreOperationEqual = new CoreOperationEqual(new Expression("arg1"), new Expression("arg2"));
        EvalContext context = new EvalContext();
        
        // Test case for equal values
        context.setContextNode(1);
        context.setNodeSet(new Object[]{1});
        assertTrue((Boolean) coreOperationEqual.computeValue(context));
        
        // Test case for unequal values
        context.setContextNode(1);
        context.setNodeSet(new Object[]{2});
        assertFalse((Boolean) coreOperationEqual.computeValue(context));
        
        // Test case for null values
        context.setContextNode(null);
        context.setNodeSet(null);
        assertTrue((Boolean) coreOperationEqual.computeValue(context));
    }

    @Test
    public void testGetSymbol() {
        CoreOperationEqual coreOperationEqual = new CoreOperationEqual(new Expression("arg1"), new Expression("arg2"));
        
        // Test case for getting the symbol
        assertEquals("=", coreOperationEqual.getSymbol());
    }
}