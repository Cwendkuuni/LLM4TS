import org.junit.Test;
import static org.junit.Assert.*;

public class CoreOperationGreaterThanTest {

    @Test
    public void testComputeValue() {
        CoreOperationGreaterThan core = new CoreOperationGreaterThan(new Expression(), new Expression());
        
        // Typical use case
        EvalContext context = new EvalContext();
        context.setContextNodePointer(new NodePointer());
        context.setJXPathContext(new JXPathContext());
        assertEquals(Boolean.TRUE, core.computeValue(context));
        
        // Edge case for l = 1 and r = 1
        context = new EvalContext();
        context.setContextNodePointer(new NodePointer());
        context.setJXPathContext(new JXPathContext());
        core = new CoreOperationGreaterThan(new Expression() {
            public Object computeValue(EvalContext c) {
                return 1;
            }
        }, new Expression() {
            public Object computeValue(EvalContext c) {
                return 1;
            }
        });
        assertEquals(Boolean.FALSE, core.computeValue(context));
        
        // Error scenario for null context
        try {
            core.computeValue(null);
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            // Pass
        }
    }

    @Test
    public void testGetSymbol() {
        CoreOperationGreaterThan core = new CoreOperationGreaterThan(new Expression(), new Expression());
        
        assertEquals(">", core.getSymbol());
    }
}