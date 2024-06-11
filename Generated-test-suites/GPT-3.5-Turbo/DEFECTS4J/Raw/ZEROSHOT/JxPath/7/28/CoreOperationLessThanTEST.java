import org.junit.Test;
import static org.junit.Assert.*;

public class CoreOperationLessThanTest {

    @Test
    public void testComputeValue() {
        CoreOperationLessThan operation = new CoreOperationLessThan(new ExpressionStub(), new ExpressionStub());
        EvalContext context = new EvalContext();
        
        // Test when left value is less than right value
        ExpressionStub arg1 = new ExpressionStub();
        ExpressionStub arg2 = new ExpressionStub();
        arg1.setValue(3);
        arg2.setValue(5);
        operation.setArgs(arg1, arg2);
        Object result = operation.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertTrue((Boolean)result);

        // Test when left value is greater than right value
        arg1.setValue(10);
        arg2.setValue(7);
        operation.setArgs(arg1, arg2);
        result = operation.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertFalse((Boolean)result);

        // Test when left value is equal to right value
        arg1.setValue(5);
        arg2.setValue(5);
        operation.setArgs(arg1, arg2);
        result = operation.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertFalse((Boolean)result);
    }

    @Test
    public void testGetSymbol() {
        CoreOperationLessThan operation = new CoreOperationLessThan(new ExpressionStub(), new ExpressionStub());
        String symbol = operation.getSymbol();
        assertEquals("<", symbol);
    }

    private class ExpressionStub extends Expression {
        private Object value;

        public void setValue(Object value) {
            this.value = value;
        }

        public Object computeValue(EvalContext context) {
            return value;
        }
    }
}