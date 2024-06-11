import static org.junit.Assert.*;
import org.junit.Test;

public class CoreOperationRelationalExpressionTest {
    
    @Test
    public void testGetPrecedence() {
        CoreOperationRelationalExpression coreOperationRelationalExpression = new CoreOperationRelationalExpression(null);
        assertEquals(3, coreOperationRelationalExpression.getPrecedence());
    }
    
    @Test
    public void testIsSymmetric() {
        CoreOperationRelationalExpression coreOperationRelationalExpression = new CoreOperationRelationalExpression(null);
        assertFalse(coreOperationRelationalExpression.isSymmetric());
    }
    
}