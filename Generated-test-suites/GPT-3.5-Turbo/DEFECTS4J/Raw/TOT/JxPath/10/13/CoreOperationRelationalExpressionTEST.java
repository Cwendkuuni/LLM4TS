import org.junit.Test;
import static org.junit.Assert.*;

public class CoreOperationRelationalExpressionTest {

    @Test
    public void testComputeValue() {
        // Typical use case
        Expression[] args = {new ExpressionMock(), new ExpressionMock()};
        CoreOperationRelationalExpression coreOpRelExp = new CoreOperationRelationalExpressionMock(args);
        EvalContext context = new EvalContext();
        Object result = coreOpRelExp.computeValue(context);
        assertEquals(Boolean.class, result.getClass());

        // Edge cases
        args = new Expression[2];
        args[0] = new ExpressionMock();
        args[1] = new ExpressionMock();
        coreOpRelExp = new CoreOperationRelationalExpressionMock(args);
        context = new EvalContext();
        result = coreOpRelExp.computeValue(context);
        assertEquals(Boolean.class, result.getClass());

        // Error scenarios
        args = new Expression[2];
        args[0] = null;
        args[1] = null;
        coreOpRelExp = new CoreOperationRelationalExpressionMock(args);
        context = new EvalContext();
        result = coreOpRelExp.computeValue(context);
        assertNull(result);
    }
}

class CoreOperationRelationalExpressionMock extends CoreOperationRelationalExpression {

    protected CoreOperationRelationalExpressionMock(Expression[] args) {
        super(args);
    }

    @Override
    protected boolean evaluateCompare(int compare) {
        return false;
    }
}

class ExpressionMock extends Expression {
    @Override
    public Object computeValue(EvalContext context) {
        return null;
    }
}