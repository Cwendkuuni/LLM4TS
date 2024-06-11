import static org.junit.Assert.*;
import org.junit.Test;

public class CoreOperationRelationalExpressionTest {

    @Test
    public void testComputeValue() {
        CoreOperationRelationalExpression coreOp = new CoreOperationRelationalExpressionTestImpl(new Expression[]{new ExpressionMock(), new ExpressionMock()});
        EvalContext context = new EvalContext();
        context.setNodeSet(new HashSet<>());
        
        Object result = coreOp.computeValue(context);
        
        assertNotNull(result);
        assertEquals(Boolean.TRUE, result);
    }

    @Test
    public void testGetPrecedence() {
        CoreOperationRelationalExpression coreOp = new CoreOperationRelationalExpressionTestImpl(new Expression[]{new ExpressionMock(), new ExpressionMock()});
        
        int precedence = coreOp.getPrecedence();
        
        assertEquals(3, precedence);
    }

    @Test
    public void testIsSymmetric() {
        CoreOperationRelationalExpression coreOp = new CoreOperationRelationalExpressionTestImpl(new Expression[]{new ExpressionMock(), new ExpressionMock()});
        
        boolean symmetric = coreOp.isSymmetric();
        
        assertFalse(symmetric);
    }

    private class CoreOperationRelationalExpressionTestImpl extends CoreOperationRelationalExpression {
        public CoreOperationRelationalExpressionTestImpl(Expression[] args) {
            super(args);
        }
        
        protected boolean evaluateCompare(int compare) {
            return compare > 0;
        }
    }

    private class ExpressionMock extends Expression {
        public Object computeValue(EvalContext context) {
            return "MockValue";
        }
    }
}