import static org.junit.Assert.*;
import org.junit.Test;

public class CoreOperationRelationalExpressionTest {

    @Test
    public void testComputeValue() {
        CoreOperationRelationalExpression coreOp = new CoreOperationRelationalExpression(new Expression[]{new MockExpression(5), new MockExpression(3)});
        EvalContext context = new EvalContext();
        Object result = coreOp.computeValue(context);
        assertEquals(Boolean.FALSE, result);
    }

    @Test
    public void testEdgeCases() {
        // Test when both arguments are the same
        CoreOperationRelationalExpression coreOp = new CoreOperationRelationalExpression(new Expression[]{new MockExpression(2.5), new MockExpression(2.5)});
        EvalContext context = new EvalContext();
        Object result = coreOp.computeValue(context);
        assertEquals(Boolean.FALSE, result);
        
        // Test when one argument is NaN
        coreOp = new CoreOperationRelationalExpression(new Expression[]{new MockExpression(Double.NaN), new MockExpression(5)});
        context = new EvalContext();
        result = coreOp.computeValue(context);
        assertEquals(Boolean.FALSE, result);
        
        // Test when one argument is a collection
        coreOp = new CoreOperationRelationalExpression(new Expression[]{new MockExpression(new HashSet<>()), new MockExpression(5)});
        context = new EvalContext();
        result = coreOp.computeValue(context);
        assertEquals(Boolean.FALSE, result);
    }

    @Test
    public void testErrorScenarios() {
        // Test when both arguments are Iterator
        CoreOperationRelationalExpression coreOp = new CoreOperationRelationalExpression(new Expression[]{new MockExpression(new HashSet<>()), new MockExpression(new HashSet<>())});
        EvalContext context = new EvalContext();
        Object result = coreOp.computeValue(context);
        assertEquals(Boolean.FALSE, result);
    }

    private class MockExpression extends Expression {
        private Object value;

        MockExpression(Object value) {
            this.value = value;
        }

        public Object computeValue(EvalContext context) {
            return value;
        }

        protected int getPrecedence() {
            return 0;
        }

        protected boolean isSymmetric() {
            return false;
        }

        protected boolean evaluateCompare(int compare) {
            return false;
        }
    }

}