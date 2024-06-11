import org.junit.Test;
import static org.junit.Assert.*;

public class CoreOperationLessThanTest {

    @Test
    public void testComputeValue() {
        Expression arg1 = new Expression();
        Expression arg2 = new Expression();
        CoreOperationLessThan operation = new CoreOperationLessThan(arg1, arg2);
        
        assertEquals(Boolean.FALSE, operation.computeValue(new EvalContext()));
        
        arg1 = new Expression();
        arg2 = new Expression();
        operation = new CoreOperationLessThan(arg1, arg2);
        
        assertEquals(Boolean.TRUE, operation.computeValue(new EvalContext()));
    }

    @Test
    public void testGetSymbol() {
        Expression arg1 = new Expression();
        Expression arg2 = new Expression();
        CoreOperationLessThan operation = new CoreOperationLessThan(arg1, arg2);
        
        assertEquals("<", operation.getSymbol());
        
        arg1 = new Expression();
        arg2 = new Expression();
        operation = new CoreOperationLessThan(arg1, arg2);
        
        assertEquals("<", operation.getSymbol());
    }
}