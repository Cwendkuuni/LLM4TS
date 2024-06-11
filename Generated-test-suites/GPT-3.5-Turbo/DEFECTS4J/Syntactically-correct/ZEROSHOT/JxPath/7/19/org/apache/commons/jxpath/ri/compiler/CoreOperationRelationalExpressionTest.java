import org.junit.Test;
import static org.junit.Assert.*;

public class CoreOperationRelationalExpressionTest {

    @Test
    public void testGetPrecedence() {
        Expression[] args = new Expression[0];
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(args);
        int result = expression.getPrecedence();
        assertEquals(3, result);
    }

    @Test
    public void testIsSymmetric() {
        Expression[] args = new Expression[0];
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(args);
        boolean result = expression.isSymmetric();
        assertFalse(result);
    }
}