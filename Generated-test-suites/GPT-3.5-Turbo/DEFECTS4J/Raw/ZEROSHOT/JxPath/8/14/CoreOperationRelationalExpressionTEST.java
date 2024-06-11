import static org.junit.Assert.*;
import org.junit.Test;

public class CoreOperationRelationalExpressionTest {

    @Test
    public void testComputeValue() {
        Expression arg1 = new ExpressionStub();
        Expression arg2 = new ExpressionStub();
        Expression[] args = { arg1, arg2 };
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpressionStub(args);

        EvalContext context = new EvalContextStub();
        Object result = expression.computeValue(context);

        assertNotNull(result);
    }

    @Test
    public void testGetPrecedence() {
        Expression arg1 = new ExpressionStub();
        Expression arg2 = new ExpressionStub();
        Expression[] args = { arg1, arg2 };
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpressionStub(args);

        int result = expression.getPrecedence();

        assertTrue(result >= 0);
    }

    @Test
    public void testIsSymmetric() {
        Expression arg1 = new ExpressionStub();
        Expression arg2 = new ExpressionStub();
        Expression[] args = { arg1, arg2 };
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpressionStub(args);

        boolean result = expression.isSymmetric();

        assertFalse(result);
    }

    @Test
    public void testEvaluateCompare() {
        Expression arg1 = new ExpressionStub();
        Expression arg2 = new ExpressionStub();
        Expression[] args = { arg1, arg2 };
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpressionStub(args);

        boolean result = expression.evaluateCompare(0);

        assertTrue(result);
    }

    @Test
    public void testCompute() {
        Expression arg1 = new ExpressionStub();
        Expression arg2 = new ExpressionStub();
        Expression[] args = { arg1, arg2 };
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpressionStub(args);

        Object result = expression.compute("left", "right");

        assertNotNull(result);
    }

    @Test
    public void testReduce() {
        Expression arg1 = new ExpressionStub();
        Expression arg2 = new ExpressionStub();
        Expression[] args = { arg1, arg2 };
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpressionStub(args);

        Object result = expression.reduce("object");

        assertNotNull(result);
    }

    @Test
    public void testContainsMatch() {
        Expression arg1 = new ExpressionStub();
        Expression arg2 = new ExpressionStub();
        Expression[] args = { arg1, arg2 };
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpressionStub(args);

        boolean result = expression.containsMatch(null, "value");

        assertFalse(result);
    }

    @Test
    public void testFindMatch() {
        Expression arg1 = new ExpressionStub();
        Expression arg2 = new ExpressionStub();
        Expression[] args = { arg1, arg2 };
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpressionStub(args);

        boolean result = expression.findMatch(null, null);

        assertFalse(result);
    }
}

class CoreOperationRelationalExpressionStub extends CoreOperationRelationalExpression {

    public CoreOperationRelationalExpressionStub(Expression[] args) {
        super(args);
    }

    @Override
    protected boolean evaluateCompare(int compare) {
        return true;
    }
}

class ExpressionStub extends Expression {

    @Override
    public Object computeValue(EvalContext context) {
        return null;
    }
}

class EvalContextStub extends EvalContext {

    @Override
    public Object getSingleNodePointer() {
        return null;
    }
}