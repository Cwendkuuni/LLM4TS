
import org.junit.Test;
import static org.junit.Assert.*;

public class CoreOperationCompareTest {

    @Test
    public void testCoreOperationCompare() {
        // Create test objects
        Expression arg1 = new ExpressionImpl();
        Expression arg2 = new ExpressionImpl();
        CoreOperationCompare operation = new CoreOperationCompare(arg1, arg2);

        // Test functionality
        assertNotNull(operation);
        assertEquals(arg1, operation.getOperands()[0]);
        assertEquals(arg2, operation.getOperands()[1]);
        assertEquals(2, operation.getPrecedence());
        assertTrue(operation.isSymmetric());
    }
}
