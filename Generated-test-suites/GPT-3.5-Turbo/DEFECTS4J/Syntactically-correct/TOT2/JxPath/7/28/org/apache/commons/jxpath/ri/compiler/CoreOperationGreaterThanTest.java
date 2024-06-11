import static org.junit.Assert.*;
import org.junit.Test;

public class CoreOperationGreaterThanTest {

    @Test
    public void testComputeValueBasicCase() {
        CoreOperationGreaterThan operation = new CoreOperationGreaterThan(new Expression(2), new Expression(1));
        EvalContext context = new EvalContext();
        Object result = operation.computeValue(context);
        assertEquals(Boolean.TRUE, result);
    }

    @Test
    public void testComputeValueEdgeCase() {
        CoreOperationGreaterThan operation = new CoreOperationGreaterThan(new Expression(-3), new Expression(0));
        EvalContext context = new EvalContext();
        Object result = operation.computeValue(context);
        assertEquals(Boolean.FALSE, result);
    }

    @Test
    public void testGetSymbol() {
        CoreOperationGreaterThan operation = new CoreOperationGreaterThan(new Expression(5), new Expression(3));
        assertEquals(">", operation.getSymbol());
    }
    
    // Additional test cases for error scenarios and edge cases can be added here

}