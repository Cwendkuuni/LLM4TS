import static org.junit.Assert.*;
import org.junit.Test;

public class CoreOperationGreaterThanTest {

    @Test
    public void testComputeValue() {
        CoreOperationGreaterThan operation = new CoreOperationGreaterThan(null, null);
        assertEquals(false, operation.computeValue(null));
        
        Expression arg1 = new Expression();
        Expression arg2 = new Expression();
        CoreOperationGreaterThan operation2 = new CoreOperationGreaterThan(arg1, arg2);
        assertEquals(true, operation2.computeValue(null));
    }

    @Test
    public void testGetSymbol() {
        CoreOperationGreaterThan operation = new CoreOperationGreaterThan(null, null);
        assertEquals(">", operation.getSymbol());
    }
}