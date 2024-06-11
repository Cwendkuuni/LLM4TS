import org.junit.Assert;
import org.junit.Test;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;

public class CoreOperationRelationalExpressionTest {

    @Test
    public void testComputeValue() {
        Expression[] args = new Expression[2];
        args[0] = new MockExpression();
        args[1] = new MockExpression();
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(args) {
            @Override
            protected boolean evaluateCompare(int compare) {
                return false;
            }
        };
        EvalContext context = new EvalContext();
        Object result = expression.computeValue(context);
        Assert.assertEquals(Boolean.FALSE, result);
    }

    @Test
    public void testGetPrecedence() {
        Expression[] args = new Expression[2];
        args[0] = new MockExpression();
        args[1] = new MockExpression();
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(args) {
            @Override
            protected boolean evaluateCompare(int compare) {
                return false;
            }
        };
        int precedence = expression.getPrecedence();
        Assert.assertEquals(CoreOperation.RELATIONAL_EXPR_PRECEDENCE, precedence);
    }

    @Test
    public void testIsSymmetric() {
        Expression[] args = new Expression[2];
        args[0] = new MockExpression();
        args[1] = new MockExpression();
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(args) {
            @Override
            protected boolean evaluateCompare(int compare) {
                return false;
            }
        };
        boolean symmetric = expression.isSymmetric();
        Assert.assertEquals(false, symmetric);
    }

    @Test
    public void testCompute() {
        Expression[] args = new Expression[2];
        args[0] = new MockExpression();
        args[1] = new MockExpression();
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(args) {
            @Override
            protected boolean evaluateCompare(int compare) {
                return false;
            }
        };

        Object result = expression.compute(new Object(), new Object());
        Assert.assertEquals(false, result);
    }

}

class MockExpression extends Expression {
    @Override
    public Object compute(EvalContext context) {
        return null;
    }
    @Override
    public void reset() {

    }
}