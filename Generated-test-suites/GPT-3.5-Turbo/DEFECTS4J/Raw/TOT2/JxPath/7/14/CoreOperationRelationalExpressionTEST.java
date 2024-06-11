import org.junit.Test;
import static org.junit.Assert.*;

public class CoreOperationRelationalExpressionTest {

    @Test
    public void testGetPrecedence() {
        Expression[] args = {new Expression(), new Expression()};
        CoreOperationRelationalExpression coreOperation = new CoreOperationRelationalExpression(args);
        assertEquals(3, coreOperation.getPrecedence());
    }

    @Test
    public void testIsSymmetric() {
        Expression[] args = {new Expression(), new Expression()};
        CoreOperationRelationalExpression coreOperation = new CoreOperationRelationalExpression(args);
        assertFalse(coreOperation.isSymmetric());
    }

}