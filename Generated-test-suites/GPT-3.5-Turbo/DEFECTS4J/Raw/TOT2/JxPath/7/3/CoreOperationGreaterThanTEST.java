import org.junit.Test;
import static org.junit.Assert.*;

public class CoreOperationGreaterThanTest {

    @Test
    public void testComputeValue() {
        CoreOperationGreaterThan coreOperation = new CoreOperationGreaterThan(new Expression(), new Expression());
        
        // Typical use case
        EvalContext context = new EvalContext();
        context.setVariable("arg1", 5);
        context.setVariable("arg2", 3);
        assertEquals(Boolean.TRUE, coreOperation.computeValue(context));
        
        // Edge cases
        context.setVariable("arg1", 3);
        context.setVariable("arg2", 5);
        assertEquals(Boolean.FALSE, coreOperation.computeValue(context));
        
        // Error scenario (exception handling)
        context.setVariable("arg1", "Invalid");
        context.setVariable("arg2", 2);
        try {
            coreOperation.computeValue(context);
            fail("Should throw an exception here");
        } catch (Exception e) {
            assertEquals("Some error message", e.getMessage());
        }
    }

    @Test
    public void testGetSymbol() {
        CoreOperationGreaterThan coreOperation = new CoreOperationGreaterThan(new Expression(), new Expression());
        
        // Typical use case
        assertEquals(">", coreOperation.getSymbol());
        
        // Edge cases
        CoreOperationGreaterThan coreOperation2 = new CoreOperationGreaterThan(null, null);
        assertNull(coreOperation2.getSymbol());
    }
}