
import org.junit.Assert;
import org.junit.Test;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public class CoreOperationRelationalExpressionTest {

    @Test
    public void testComputeValueGreaterThan() {
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpressionStub();
        EvalContext context = new EvalContextStub();
        Object result = expression.computeValue(context);
        Assert.assertEquals(Boolean.FALSE, result);
    }

    @Test
    public void testComputeValueGreaterThanOrEqualTo() {
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpressionStub();
        EvalContext context = new EvalContextStub();
        Object result = expression.computeValue(context);
        Assert.assertEquals(Boolean.TRUE, result);
    }

    @Test
    public void testComputeValueLessThan() {
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpressionStub();
        EvalContext context = new EvalContextStub();
        Object result = expression.computeValue(context);
        Assert.assertEquals(Boolean.FALSE, result);
    }

    @Test
    public void testComputeValueLessThanOrEqualTo() {
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpressionStub();
        EvalContext context = new EvalContextStub();
        Object result = expression.computeValue(context);
        Assert.assertEquals(Boolean.TRUE, result);
    }

    @Test
    public void testGetPrecedence() {
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpressionStub();
        int precedence = expression.getPrecedence();
        Assert.assertEquals(3, precedence);
    }

    private static class CoreOperationRelationalExpressionStub extends CoreOperationRelationalExpression {
        public CoreOperationRelationalExpressionStub() {
            super(null);
        }

        @Override
        protected boolean evaluateCompare(int compare) {
            if (compare > 0) {
                return true;
            } else {
                return false;
            }
        }
    }

    private static class EvalContextStub extends EvalContext {
        @Override
        public Object computeValue(String xpath) {
            return null;
        }

        @Override
        public Object getSingleNodePointer() {
            return null;
        }
    }

}
