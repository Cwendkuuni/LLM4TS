import static org.junit.Assert.*;
import org.junit.Test;

public class CoreOperationEqualTest {

    @Test
    public void testComputeValue() {
        Expression arg1 = new DummyExpression();
        Expression arg2 = new DummyExpression();
        CoreOperationEqual coreOperationEqual = new CoreOperationEqual(arg1, arg2);

        // Test for equal values
        arg1.setValue("test");
        arg2.setValue("test");
        assertTrue((Boolean) coreOperationEqual.computeValue(new EvalContext()));
        
        // Test for unequal values
        arg2.setValue("different");
        assertFalse((Boolean) coreOperationEqual.computeValue(new EvalContext()));
    }

    @Test
    public void testGetSymbol() {
        Expression arg1 = new DummyExpression();
        Expression arg2 = new DummyExpression();
        CoreOperationEqual coreOperationEqual = new CoreOperationEqual(arg1, arg2);

        assertEquals("=", coreOperationEqual.getSymbol());
    }

    // Edge cases and exception handling scenarios can be added here as needed

    private class DummyExpression extends Expression {
        private Object value;

        @Override
        public Object computeValue(EvalContext context) {
            return value;
        }

        public void setValue(Object value) {
            this.value = value;
        }
    }
}