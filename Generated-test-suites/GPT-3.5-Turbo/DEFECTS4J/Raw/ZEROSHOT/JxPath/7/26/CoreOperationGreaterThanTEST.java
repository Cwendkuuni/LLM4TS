import static org.junit.Assert.assertEquals;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InfoSetUtil;
import org.junit.Before;
import org.junit.Test;

public class CoreOperationGreaterThanTest {

    private CoreOperationGreaterThan operation;
    private EvalContext context;

    @Before
    public void setUp() {
        Expression arg1 = new ExpressionMock();
        Expression arg2 = new ExpressionMock();
        operation = new CoreOperationGreaterThan(arg1, arg2);
        context = new EvalContext();
    }

    @Test
    public void testComputeValue() {
        double l = 1.0;
        double r = 2.0;
        context.setArg(0, l);
        context.setArg(1, r);
        Object result = operation.computeValue(context);
        assertEquals(Boolean.FALSE, result);
    }

    @Test
    public void testGetSymbol() {
        String expected = ">";
        String result = operation.getSymbol();
        assertEquals(expected, result);
    }

    private class ExpressionMock implements Expression {

        @Override
        public Object computeValue(EvalContext context) {
            return null;
        }
    }
}