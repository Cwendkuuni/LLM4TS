
import org.apache.commons.jxpath.ri.EvalContext;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class CoreOperationLessThanOrEqualTest {

    private CoreOperationLessThanOrEqual lessThanOrEqual;

    @Before
    public void setup() {
        EvalContext evalContext = new EvalContext();
        Expression arg1 = new MockExpression();
        Expression arg2 = new MockExpression();
        lessThanOrEqual = new CoreOperationLessThanOrEqual(arg1, arg2);
        lessThanOrEqual.setContext(evalContext);
    }

    @Test
    public void testComputeValue_LessThanOrEqual_CorrectComparison() {
        MockExpression arg1 = new MockExpression(5);
        MockExpression arg2 = new MockExpression(10);
        lessThanOrEqual.setArguments(new Expression[]{arg1, arg2});

        Object result = lessThanOrEqual.computeValue(lessThanOrEqual.getContext());

        assertEquals(Boolean.TRUE, result);
    }

    @Test
    public void testComputeValue_NotLessThanOrEqual_CorrectComparison() {
        MockExpression arg1 = new MockExpression(10);
        MockExpression arg2 = new MockExpression(5);
        lessThanOrEqual.setArguments(new Expression[]{arg1, arg2});

        Object result = lessThanOrEqual.computeValue(lessThanOrEqual.getContext());

        assertEquals(Boolean.FALSE, result);
    }

    @Test
    public void testComputeValue_Equal_CorrectComparison() {
        MockExpression arg1 = new MockExpression(10);
        MockExpression arg2 = new MockExpression(10);
        lessThanOrEqual.setArguments(new Expression[]{arg1, arg2});

        Object result = lessThanOrEqual.computeValue(lessThanOrEqual.getContext());

        assertEquals(Boolean.TRUE, result);
    }

    @Test
    public void testComputeValue_NotComparable_NullArguments() {
        MockExpression arg1 = new MockExpression(null);
        MockExpression arg2 = new MockExpression(null);
        lessThanOrEqual.setArguments(new Expression[]{arg1, arg2});

        Object result = lessThanOrEqual.computeValue(lessThanOrEqual.getContext());

        assertEquals(Boolean.FALSE, result);
    }

    @Test
    public void testComputeValue_NotComparable_OneArgumentIsNull() {
        MockExpression arg1 = new MockExpression(5);
        MockExpression arg2 = new MockExpression(null);
        lessThanOrEqual.setArguments(new Expression[]{arg1, arg2});

        Object result = lessThanOrEqual.computeValue(lessThanOrEqual.getContext());

        assertEquals(Boolean.FALSE, result);
    }

    @Test(expected = NullPointerException.class)
    public void testComputeValue_NullContext_ThrowsNullPointerException() {
        lessThanOrEqual.setContext(null);
        lessThanOrEqual.computeValue(null);
    }

    private class MockExpression extends Expression {

        private final Object value;

        public MockExpression(Object value) {
            this.value = value;
        }

        @Override
        public Object computeValue(EvalContext context) {
            return value;
        }

        @Override
        public String getSymbol() {
            return "";
        }
    }
}
