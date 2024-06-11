import org.junit.Test;
import static org.junit.Assert.*;

public class CoreOperationGreaterThanTest {

    @Test
    public void testComputeValue() {
        CoreOperationGreaterThan operation = new CoreOperationGreaterThan();
        
        // Test typical use case
        double l = 5.5;
        double r = 2.5;
        Object result = operation.computeValue(l, r);
        assertEquals(Boolean.TRUE, result);
        
        // Test edge case
        l = Double.MAX_VALUE;
        r = -Double.MAX_VALUE;
        result = operation.computeValue(l, r);
        assertEquals(Boolean.TRUE, result);
        
        // Test error scenario
        l = Double.NaN;
        r = Double.NaN;
        result = operation.computeValue(l, r);
        assertEquals(Boolean.FALSE, result);
    }

    @Test
    public void testGetSymbol() {
        CoreOperationGreaterThan operation = new CoreOperationGreaterThan();
        
        String symbol = operation.getSymbol();
        assertEquals(">", symbol);
    }
}