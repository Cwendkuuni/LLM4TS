import org.junit.Test;
import static org.junit.Assert.*;

public class CoreOperationGreaterThanTest {

    @Test
    public void testComputeValue() {
        CoreOperationGreaterThan coreOperationGreaterThan = new CoreOperationGreaterThan(new Expression(), new Expression());
        EvalContext context = new EvalContext();
        
        // Typical use case
        context.setContextNodePointer(new NodePointer());
        context.setJXPathContext(new JXPathContext());
        context.setCurrentNodePointer(new NodePointer());
        context.setPosition(1);
        context.setContextSize(1);
        assertTrue(coreOperationGreaterThan.computeValue(context));
        
        // Edge case
        context.setContextNodePointer(null);
        assertFalse(coreOperationGreaterThan.computeValue(context));
        
        // Error scenario
        context.setContextNodePointer(new NodePointer());
        context.setJXPathContext(null);
        assertThrows(NullPointerException.class, () -> coreOperationGreaterThan.computeValue(context));
    }

    @Test
    public void testGetSymbol() {
        CoreOperationGreaterThan coreOperationGreaterThan = new CoreOperationGreaterThan(new Expression(), new Expression());
        
        // Typical use case
        assertEquals(">", coreOperationGreaterThan.getSymbol());
        
        // Edge case - No edge cases for getSymbol method
    }
}