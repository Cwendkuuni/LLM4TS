import org.junit.Test;
import static org.junit.Assert.*;

public class CoreOperationGreaterThanTest {
    
    @Test
    public void testComputeValue() {
        EvalContext context = new EvalContext();
        CoreOperationGreaterThan operation = new CoreOperationGreaterThan(new Expression(), new Expression());
        Object result = operation.computeValue(context);
        // Add assertion for the expected result
    }
    
    @Test
    public void testGetSymbol() {
        CoreOperationGreaterThan operation = new CoreOperationGreaterThan(new Expression(), new Expression());
        String symbol = operation.getSymbol();
        // Add assertion for the expected symbol
    }
}