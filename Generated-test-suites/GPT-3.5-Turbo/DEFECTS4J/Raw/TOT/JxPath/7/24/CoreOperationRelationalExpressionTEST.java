import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class CoreOperationRelationalExpressionTest {

    CoreOperationRelationalExpression expression;

    @Before
    public void setUp() {
        expression = new CoreOperationRelationalExpressionMock(null);
    }

    @Test
    public void testGetPrecedence() {
        assertEquals(3, expression.getPrecedence());
    }

    @Test
    public void testIsSymmetric() {
        assertFalse(expression.isSymmetric());
    }

    @Test
    public void testCoreOperationRelationalExpression() {
        assertNotNull(expression);
    }

    /**
     * Mock class for CoreOperationRelationalExpression
     */
    private class CoreOperationRelationalExpressionMock extends CoreOperationRelationalExpression {

        public CoreOperationRelationalExpressionMock(Expression[] args) {
            super(args);
        }

        @Override
        protected final int getPrecedence() {
            return 3;
        }

        @Override
        protected final boolean isSymmetric() {
            return false;
        }
    }
}