import org.junit.Assert;
import org.junit.Test;

public class CoreOperationRelationalExpressionTest {

    @Test
    public void testComputeValue() {
        Expression[] args = new Expression[2];
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(args) {
            @Override
            protected boolean evaluateCompare(int compare) {
                return compare > 0;
            }
        };

        // Test case 1
        Object result1 = expression.computeValue(null);
        Assert.assertEquals(Boolean.FALSE, result1);

        // Test case 2
        args[0] = new Expression() {
            @Override
            public Object computeValue(EvalContext context) {
                return 5;
            }
        };
        Object result2 = expression.computeValue(null);
        Assert.assertEquals(Boolean.FALSE, result2);

        // Test case 3
        args[1] = new Expression() {
            @Override
            public Object computeValue(EvalContext context) {
                return 10;
            }
        };
        Object result3 = expression.computeValue(null);
        Assert.assertEquals(Boolean.FALSE, result3);

        // Test case 4
        args[0] = new Expression() {
            @Override
            public Object computeValue(EvalContext context) {
                return 15;
            }
        };
        Object result4 = expression.computeValue(null);
        Assert.assertEquals(Boolean.TRUE, result4);
    }

    @Test
    public void testGetPrecedence() {
        Expression[] args = new Expression[2];
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(args) {
            @Override
            protected boolean evaluateCompare(int compare) {
                return compare > 0;
            }
        };

        int precedence = expression.getPrecedence();
        Assert.assertEquals(5, precedence);
    }

    @Test
    public void testIsSymmetric() {
        Expression[] args = new Expression[2];
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(args) {
            @Override
            protected boolean evaluateCompare(int compare) {
                return compare > 0;
            }
        };

        boolean symmetric = expression.isSymmetric();
        Assert.assertEquals(false, symmetric);
    }
}