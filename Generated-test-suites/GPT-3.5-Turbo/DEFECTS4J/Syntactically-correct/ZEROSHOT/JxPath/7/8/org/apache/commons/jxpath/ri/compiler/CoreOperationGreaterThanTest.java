import org.junit.Test;
import static org.junit.Assert.*;

public class CoreOperationGreaterThanTest {

    @Test
    public void testComputeValue() {
        CoreOperationGreaterThan expression = new CoreOperationGreaterThan(new Expression(), new Expression());
        
        EvalContext context = new EvalContext();
        context.setNodeSet(new NodeSet());
        
        // Test case 1
        expression.args[0] = new Expression();
        expression.args[1] = new Expression();
        double[] values1 = {1.0, 2.0};
        context.setValues(values1);
        
        Object result1 = expression.computeValue(context);
        assertEquals(Boolean.FALSE, result1);
        
        // Test case 2
        expression.args[0] = new Expression();
        expression.args[1] = new Expression();
        double[] values2 = {2.0, 1.0};
        context.setValues(values2);
        
        Object result2 = expression.computeValue(context);
        assertEquals(Boolean.TRUE, result2);
    }

    @Test
    public void testGetSymbol() {
        CoreOperationGreaterThan expression = new CoreOperationGreaterThan(new Expression(), new Expression());
        String symbol = expression.getSymbol();
        assertEquals(">", symbol);
    }
}