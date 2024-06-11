import org.junit.Test;
import static org.junit.Assert.*;

public class CoreOperationGreaterThanOrEqualTest {

    @Test
    public void testComputeValue() {
        EvalContext context = new EvalContext();
        CoreOperationGreaterThanOrEqual expression = new CoreOperationGreaterThanOrEqual(new Expression(), new Expression());
        
        // Test case 1
        Expression arg1 = new Expression();
        Expression arg2 = new Expression();
        expression.setArgs(new Expression[] {arg1, arg2});
        arg1.setValue(5);
        arg2.setValue(3);
        assertEquals(Boolean.TRUE, expression.computeValue(context));
        
        // Test case 2
        arg1.setValue(5);
        arg2.setValue(5);
        assertEquals(Boolean.TRUE, expression.computeValue(context));
        
        // Test case 3
        arg1.setValue(3);
        arg2.setValue(5);
        assertEquals(Boolean.FALSE, expression.computeValue(context));
    }

    @Test
    public void testGetSymbol() {
        CoreOperationGreaterThanOrEqual expression = new CoreOperationGreaterThanOrEqual(new Expression(), new Expression());
        
        assertEquals(">=", expression.getSymbol());
    }
}