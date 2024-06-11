import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class CoreOperationGreaterThanTest {

    @Test
    public void testComputeValue() {
        CoreOperationGreaterThan expression = new CoreOperationGreaterThan(new Expression(), new Expression());
        EvalContext context = new EvalContext();
        
        // Test case 1
        expression.args[0] = new Expression();
        expression.args[1] = new Expression();
        context.setNodeSet(new NodeSet());
        assertEquals(Boolean.FALSE, expression.computeValue(context));
        
        // Test case 2
        expression.args[0] = new Expression();
        expression.args[1] = new Expression();
        context.setNodeSet(new NodeSet());
        assertEquals(Boolean.TRUE, expression.computeValue(context));
    }

    @Test
    public void testGetSymbol() {
        CoreOperationGreaterThan expression = new CoreOperationGreaterThan(new Expression(), new Expression());
        assertEquals(">", expression.getSymbol());
    }
}