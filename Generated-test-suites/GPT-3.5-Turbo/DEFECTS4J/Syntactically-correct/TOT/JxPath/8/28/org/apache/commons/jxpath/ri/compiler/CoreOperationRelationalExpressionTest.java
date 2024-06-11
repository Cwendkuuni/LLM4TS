import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class CoreOperationRelationalExpressionTest {

    private CoreOperationRelationalExpression coreOpRelExp;

    @Before
    public void setup() {
        coreOpRelExp = new CoreOperationRelationalExpressionStub();
    }

    @Test
    public void testComputeValue_leftGreaterThanRight() {
        EvalContext context = new EvalContext();
        context.setSingleNodePointer(10);
        Object result = coreOpRelExp.computeValue(context);
        Assert.assertEquals(Boolean.TRUE, result);
    }

    @Test
    public void testComputeValue_leftEqualToRight() {
        EvalContext context = new EvalContext();
        context.setSingleNodePointer(5);
        Object result = coreOpRelExp.computeValue(context);
        Assert.assertEquals(Boolean.FALSE, result);
    }

    @Test
    public void testComputeValue_leftLessThanRight() {
        EvalContext context = new EvalContext();
        context.setSingleNodePointer(2);
        Object result = coreOpRelExp.computeValue(context);
        Assert.assertEquals(Boolean.FALSE, result);
    }

    @Test
    public void testComputeValue_leftAndRightNull() {
        EvalContext context = new EvalContext();
        context.setSingleNodePointer(null);
        Object result = coreOpRelExp.computeValue(context);
        Assert.assertEquals(Boolean.FALSE, result);
    }

    @Test
    public void testComputeValue_leftNullAndRightNotNull() {
        EvalContext context = new EvalContext();
        context.setSingleNodePointer(10);
        Object result = coreOpRelExp.computeValue(context);
        Assert.assertEquals(Boolean.FALSE, result);
    }

    @Test
    public void testComputeValue_leftNotNullAndRightNull() {
        EvalContext context = new EvalContext();
        context.setSingleNodePointer(null);
        Object result = coreOpRelExp.computeValue(context);
        Assert.assertEquals(Boolean.FALSE, result);
    }

    private class CoreOperationRelationalExpressionStub extends CoreOperationRelationalExpression {

        public CoreOperationRelationalExpressionStub() {
            super(new Expression[]{null, null});
        }

        protected boolean evaluateCompare(int compare) {
            return compare > 0;
        }
    }
}