import static org.junit.Assert.*;
import org.junit.Test;

public class CoreOperationRelationalExpressionTest {

    @Test
    public void testGetPrecedence() {
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpressionMock();
        assertEquals(3, expression.getPrecedence());
    }
    
    @Test
    public void testIsSymmetric() {
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpressionMock();
        assertFalse(expression.isSymmetric());
    }

    // Mock class for CoreOperationRelationalExpression
    private class CoreOperationRelationalExpressionMock extends CoreOperationRelationalExpression {
        public CoreOperationRelationalExpressionMock() {
            super(null);
        }
    }

}