
import org.junit.Test;
import static org.junit.Assert.*;

public class CoreOperationGreaterThanOrEqualTest {
    
    @Test
    public void testComputeValue() {
        CoreOperationGreaterThanOrEqual operation = new CoreOperationGreaterThanOrEqual(expression1, expression2);
        EvalContext context = new EvalContext();
        Object result = operation.computeValue(context);
        // Assert result
    }
    
    @Test
    public void testGetSymbol() {
        CoreOperationGreaterThanOrEqual operation = new CoreOperationGreaterThanOrEqual(expression1, expression2);
        String symbol = operation.getSymbol();
        // Assert symbol
    }
}
