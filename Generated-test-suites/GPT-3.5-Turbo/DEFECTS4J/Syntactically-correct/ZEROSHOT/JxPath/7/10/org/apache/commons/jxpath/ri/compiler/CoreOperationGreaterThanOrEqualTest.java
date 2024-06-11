import org.junit.Test;
import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InfoSetUtil;
import static org.junit.Assert.*;

public class CoreOperationGreaterThanOrEqualTest {

    @Test
    public void testComputeValue() {
        Expression arg1 = new MockExpression(10);
        Expression arg2 = new MockExpression(5);
        CoreOperationGreaterThanOrEqual operation = new CoreOperationGreaterThanOrEqual(arg1, arg2);
        
        EvalContext context = new MockEvalContext();
        Object result = operation.computeValue(context);
        
        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result);
    }

    @Test
    public void testComputeValueEqual() {
        Expression arg1 = new MockExpression(10);
        Expression arg2 = new MockExpression(10);
        CoreOperationGreaterThanOrEqual operation = new CoreOperationGreaterThanOrEqual(arg1, arg2);
        
        EvalContext context = new MockEvalContext();
        Object result = operation.computeValue(context);
        
        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result);
    }

    @Test
    public void testComputeValueLessThan() {
        Expression arg1 = new MockExpression(5);
        Expression arg2 = new MockExpression(10);
        CoreOperationGreaterThanOrEqual operation = new CoreOperationGreaterThanOrEqual(arg1, arg2);
        
        EvalContext context = new MockEvalContext();
        Object result = operation.computeValue(context);
        
        assertTrue(result instanceof Boolean);
        assertFalse((Boolean) result);
    }

    @Test
    public void testGetSymbol() {
        Expression arg1 = new MockExpression(10);
        Expression arg2 = new MockExpression(5);
        CoreOperationGreaterThanOrEqual operation = new CoreOperationGreaterThanOrEqual(arg1, arg2);
        
        String symbol = operation.getSymbol();
        
        assertEquals(">=", symbol);
    }

    private class MockExpression extends Expression {
        private Object value;

        public MockExpression(Object value) {
            this.value = value;
        }

        public Object computeValue(EvalContext context) {
            return value;
        }
    }

    private class MockEvalContext extends EvalContext {
        public MockEvalContext() {
            super(null, null);
        }

        public double doubleValue(Object obj) {
            if (obj instanceof Number) {
                return ((Number) obj).doubleValue();
            }
            return InfoSetUtil.doubleValue(obj);
        }
    }
}