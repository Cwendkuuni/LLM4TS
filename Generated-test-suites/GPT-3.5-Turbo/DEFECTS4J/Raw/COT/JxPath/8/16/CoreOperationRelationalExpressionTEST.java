import org.apache.commons.jxpath.ri.compiler.CoreOperationRelationalExpression;
import org.junit.Assert;
import org.junit.Test;

public class CoreOperationRelationalExpressionTest {

    @Test
    public void testComputeValue() {
        Expression[] args = {new Expression(), new Expression()};
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
        Expression[] args = {new Expression(), new Expression()};
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(args) {
            @Override
            protected boolean evaluateCompare(int compare) {
                return false;
            }
        };

        int precedence = expression.getPrecedence();

        Assert.assertEquals(3, precedence);
    }

    @Test
    public void testIsSymmetric() {
        Expression[] args = {new Expression(), new Expression()};
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(args) {
            @Override
            protected boolean evaluateCompare(int compare) {
                return false;
            }
        };

        boolean isSymmetric = expression.isSymmetric();

        Assert.assertEquals(false, isSymmetric);
    }
}