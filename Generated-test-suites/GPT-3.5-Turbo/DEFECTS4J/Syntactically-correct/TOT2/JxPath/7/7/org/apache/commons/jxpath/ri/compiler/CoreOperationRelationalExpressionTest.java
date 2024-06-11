import static org.junit.Assert.*;
import org.junit.Test;

public class CoreOperationRelationalExpressionTest {

    @Test
    public void testGetPrecedence() {
        Expression[] args = {};
        CoreOperationRelationalExpression coreOperation = new CoreOperationRelationalExpression(args);
        int result = coreOperation.getPrecedence();
        assertEquals(3, result);
    }

    @Test
    public void testIsSymmetric() {
        Expression[] args = {};
        CoreOperationRelationalExpression coreOperation = new CoreOperationRelationalExpression(args);
        boolean result = coreOperation.isSymmetric();
        assertFalse(result);
    }

}