import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class CoreOperationRelationalExpressionTest {

    private CoreOperationRelationalExpression coreOpRelExp;

    @Before
    public void setup() {
        Expression[] args = new Expression[]{new Expression(), new Expression()};
        coreOpRelExp = new CoreOperationRelationalExpressionImpl(args);
    }

    @Test
    public void testGetPrecedence() {
        assertEquals(3, coreOpRelExp.getPrecedence());
    }

    @Test
    public void testIsSymmetric() {
        assertFalse(coreOpRelExp.isSymmetric());
    }

    // Define a concrete implementation for testing
    private class CoreOperationRelationalExpressionImpl extends CoreOperationRelationalExpression {

        public CoreOperationRelationalExpressionImpl(Expression[] args) {
            super(args);
        }
    }
}