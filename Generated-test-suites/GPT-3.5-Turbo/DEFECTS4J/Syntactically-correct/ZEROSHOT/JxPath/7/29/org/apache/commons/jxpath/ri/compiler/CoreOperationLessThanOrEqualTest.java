import org.junit.Test;
import static org.junit.Assert.*;

public class CoreOperationLessThanOrEqualTest {

    @Test
    public void testComputeValue() {
        CoreOperationLessThanOrEqual op = new CoreOperationLessThanOrEqual(new ExpressionMock(), new ExpressionMock());
        
        // Test case 1
        EvalContext context1 = new EvalContext();
        context1.setNode(new NodeMock());
        Object result1 = op.computeValue(context1);
        assertEquals(Boolean.TRUE, result1);
        
        // Test case 2
        EvalContext context2 = new EvalContext();
        context2.setNode(new NodeMock());
        Object result2 = op.computeValue(context2);
        assertEquals(Boolean.FALSE, result2);
    }

    @Test
    public void testGetSymbol() {
        CoreOperationLessThanOrEqual op = new CoreOperationLessThanOrEqual(new ExpressionMock(), new ExpressionMock());
        String symbol = op.getSymbol();
        assertEquals("<=", symbol);
    }
}