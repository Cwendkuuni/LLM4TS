import org.junit.Test;
import static org.junit.Assert.*;

public class CoreOperationGreaterThanOrEqualTest {

    @Test
    public void testComputeValue() {
        Expression arg1 = new Expression();
        Expression arg2 = new Expression();
        CoreOperationGreaterThanOrEqual coreOperationGreaterThanOrEqual = new CoreOperationGreaterThanOrEqual(arg1, arg2);
        EvalContext context = new EvalContext();
        
        // Test when arg1.computeValue(context) >= arg2.computeValue(context)
        double arg1Value = 5.0;
        double arg2Value = 3.0;
        arg1.setValue(arg1Value);
        arg2.setValue(arg2Value);
        
        assertEquals(Boolean.TRUE, coreOperationGreaterThanOrEqual.computeValue(context));
        
        // Test when arg1.computeValue(context) < arg2.computeValue(context)
        arg1Value = 3.0;
        arg2Value = 5.0;
        arg1.setValue(arg1Value);
        arg2.setValue(arg2Value);
        
        assertEquals(Boolean.FALSE, coreOperationGreaterThanOrEqual.computeValue(context));
    }

    @Test
    public void testGetSymbol() {
        Expression arg1 = new Expression();
        Expression arg2 = new Expression();
        CoreOperationGreaterThanOrEqual coreOperationGreaterThanOrEqual = new CoreOperationGreaterThanOrEqual(arg1, arg2);
        
        assertEquals(">=", coreOperationGreaterThanOrEqual.getSymbol());
    }
}