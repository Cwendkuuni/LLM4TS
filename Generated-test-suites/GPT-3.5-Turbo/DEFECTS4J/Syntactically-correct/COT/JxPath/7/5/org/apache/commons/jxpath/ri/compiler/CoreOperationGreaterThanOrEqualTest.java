
import org.junit.Test;
import static org.junit.Assert.*;

public class CoreOperationGreaterThanOrEqualTest {

    @Test
    public void testComputeValue() {
        CoreOperationGreaterThanOrEqual expression = new CoreOperationGreaterThanOrEqual(arg1, arg2);
        EvalContext context = new EvalContext();
        
        // Test case 1: arg1 > arg2 (positive case)
        double arg1 = 5.0;
        double arg2 = 3.0;
        assertEquals(true, expression.computeValue(context));
        
        // Test case 2: arg1 < arg2 (negative case)
        arg1 = 3.0;
        arg2 = 5.0;
        assertEquals(false, expression.computeValue(context));
        
        // Test case 3: arg1 = arg2 (border case)
        arg1 = 3.0;
        arg2 = 3.0;
        assertEquals(true, expression.computeValue(context));
    }

    @Test
    public void testGetSymbol() {
        CoreOperationGreaterThanOrEqual expression = new CoreOperationGreaterThanOrEqual(arg1, arg2);
        assertEquals(">=", expression.getSymbol());
    }

    @Test
    public void testConstructor() {
        Expression arg1 = new Expression();
        Expression arg2 = new Expression();
        CoreOperationGreaterThanOrEqual expression = new CoreOperationGreaterThanOrEqual(arg1, arg2);
        assertNotNull(expression);
    }
}
