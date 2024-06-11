import static org.junit.Assert.*;
import org.junit.Test;
import org.apache.commons.jxpath.ri.compiler.CoreOperationRelationalExpression;

public class CoreOperationRelationalExpressionTest {

    @Test
    public void testGetPrecedence() {
        Expression[] args = { /* add test values here */ };
        CoreOperationRelationalExpression coreOperationRelationalExpression = new CoreOperationRelationalExpression(args);
        
        int result = coreOperationRelationalExpression.getPrecedence();
        
        assertEquals(3, result);
    }

    @Test
    public void testIsSymmetric() {
        Expression[] args = { /* add test values here */ };
        CoreOperationRelationalExpression coreOperationRelationalExpression = new CoreOperationRelationalExpression(args);
        
        boolean result = coreOperationRelationalExpression.isSymmetric();
        
        assertFalse(result);
    }

}