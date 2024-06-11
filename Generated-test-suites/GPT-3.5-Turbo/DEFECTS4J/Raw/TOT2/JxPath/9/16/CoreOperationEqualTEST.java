import org.junit.Test;
import static org.junit.Assert.*;

public class CoreOperationEqualTest {

    @Test
    public void testComputeValue() {
        CoreOperationEqual coreOperationEqual = new CoreOperationEqual(new ExpressionMock(), new ExpressionMock());
        EvalContext context = new EvalContext();

        // Typical use case
        context.setVariable("var1", "value");
        context.setVariable("var2", "value");
        assertTrue((Boolean) coreOperationEqual.computeValue(context));

        // Edge case - values are null
        context.setVariable("var1", null);
        context.setVariable("var2", null);
        assertTrue((Boolean) coreOperationEqual.computeValue(context));

        // Error scenario - variables are not set in the context
        assertFalse((Boolean) coreOperationEqual.computeValue(context));
    }

    @Test
    public void testGetSymbol() {
        CoreOperationEqual coreOperationEqual = new CoreOperationEqual(new ExpressionMock(), new ExpressionMock());
        
        // Typical use case
        assertEquals("=", coreOperationEqual.getSymbol());
    }

    private class ExpressionMock extends Expression {
        @Override
        public Object computeValue(EvalContext context) {
            return null;
        }
    }
}