import static org.junit.Assert.assertEquals;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InfoSetUtil;
import org.junit.Before;
import org.junit.Test;

public class CoreOperationGreaterThanOrEqualTest {
    private CoreOperationGreaterThanOrEqual operation;

    @Before
    public void setUp() {
        Expression arg1 = new ExpressionStub();
        Expression arg2 = new ExpressionStub();
        operation = new CoreOperationGreaterThanOrEqual(arg1, arg2);
    }

    @Test
    public void testComputeValue_LeftGreaterThanOrEqualToRight_ReturnsTrue() {
        EvalContext context = new EvalContextStub();
        double l = 5.0;
        double r = 3.0;
        InfoSetUtil.setValue(context, l, operation.getArgs()[0]);
        InfoSetUtil.setValue(context, r, operation.getArgs()[1]);

        assertEquals(Boolean.TRUE, operation.computeValue(context));
    }

    @Test
    public void testComputeValue_LeftLessThanRight_ReturnsFalse() {
        EvalContext context = new EvalContextStub();
        double l = 3.0;
        double r = 5.0;
        InfoSetUtil.setValue(context, l, operation.getArgs()[0]);
        InfoSetUtil.setValue(context, r, operation.getArgs()[1]);

        assertEquals(Boolean.FALSE, operation.computeValue(context));
    }

    // Add any additional test cases to cover all code paths

    private class ExpressionStub extends Expression {
        @Override
        public Object computeValue(EvalContext context) {
            return null;
        }
    }

    private class EvalContextStub extends EvalContext {
        // Implement any required methods
    }
}