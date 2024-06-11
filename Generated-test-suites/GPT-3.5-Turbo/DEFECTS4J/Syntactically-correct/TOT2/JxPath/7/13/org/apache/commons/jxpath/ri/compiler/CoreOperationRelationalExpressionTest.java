import org.junit.Test;
import static org.junit.Assert.*;

public class CoreOperationRelationalExpressionTest {

    @Test
    public void testGetPrecedence() {
        Expression[] args = {};
        CoreOperationRelationalExpression coreOpRelExp = new CoreOperationRelationalExpressionImpl(args);
        assertEquals(3, coreOpRelExp.getPrecedence());
    }

    @Test
    public void testIsSymmetric() {
        Expression[] args = {};
        CoreOperationRelationalExpression coreOpRelExp = new CoreOperationRelationalExpressionImpl(args);
        assertFalse(coreOpRelExp.isSymmetric());
    }

    private static class CoreOperationRelationalExpressionImpl extends CoreOperationRelationalExpression {
        CoreOperationRelationalExpressionImpl(Expression[] args) {
            super(args);
        }
    }

}