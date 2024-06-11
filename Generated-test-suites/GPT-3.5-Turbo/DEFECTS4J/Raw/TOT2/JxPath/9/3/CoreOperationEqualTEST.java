import org.junit.Test;

import static org.junit.Assert.*;

public class CoreOperationEqualTest {

    @Test
    public void testComputeValue() {
        CoreOperationEqual operationEqual = new CoreOperationEqual(new Expression(), new Expression());
        EvalContext context = new EvalContext();

        // Typical use case
        context.setVariable("var1", "value");
        context.setVariable("var2", "value");
        assertTrue((Boolean) operationEqual.computeValue(context));

        // Edge case - variables with different values
        context.setVariable("var2", "differentValue");
        assertFalse((Boolean) operationEqual.computeValue(context));

        // Edge case - variable not present
        context.removeVariable("var2");
        assertFalse((Boolean) operationEqual.computeValue(context));
    }

    @Test
    public void testGetSymbol() {
        CoreOperationEqual operationEqual = new CoreOperationEqual(new Expression(), new Expression());

        // Typical use case
        assertEquals("=", operationEqual.getSymbol());

        // Edge case - custom symbol
        // operationEqual.symbol = "+";
        // assertEquals("+", operationEqual.getSymbol());
    }
}