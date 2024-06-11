import org.junit.Test;
import org.apache.commons.jxpath.ri.compiler.CoreOperationRelationalExpression;
import org.apache.commons.jxpath.ri.compiler.Expression;

import static org.junit.Assert.*;

public class CoreOperationRelationalExpressionTest {

    @Test
    public void testGetPrecedence() {
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpressionMock(new Expression[]{});
        assertEquals(3, expression.getPrecedence());
    }

    @Test
    public void testIsSymmetric() {
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpressionMock(new Expression[]{});
        assertFalse(expression.isSymmetric());
    }

    private class CoreOperationRelationalExpressionMock extends CoreOperationRelationalExpression {

        public CoreOperationRelationalExpressionMock(Expression[] args) {
            super(args);
        }
    }
}