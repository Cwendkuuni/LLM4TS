import org.junit.Test;
import static org.junit.Assert.*;

public class CoreOperationGreaterThanTest {

    @Test
    public void testComputeValue() {
        CoreOperationGreaterThan operation = new CoreOperationGreaterThan(new Expression(), new Expression());
        
        // Test for l > r
        EvalContext context1 = new EvalContext();
        context1.setArg("l", 5);
        context1.setArg("r", 3);
        assertTrue((Boolean) operation.computeValue(context1));
        
        // Test for l < r
        EvalContext context2 = new EvalContext();
        context2.setArg("l", 3);
        context2.setArg("r", 5);
        assertFalse((Boolean) operation.computeValue(context2));
    }

    @Test
    public void testGetSymbol() {
        CoreOperationGreaterThan operation = new CoreOperationGreaterThan(new Expression(), new Expression());
        assertEquals(">", operation.getSymbol());
    }
}