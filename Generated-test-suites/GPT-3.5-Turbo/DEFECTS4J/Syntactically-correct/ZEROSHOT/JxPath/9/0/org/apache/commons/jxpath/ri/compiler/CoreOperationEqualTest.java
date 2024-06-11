
import org.apache.commons.jxpath.ri.compiler.Expression;
import org.apache.commons.jxpath.ri.EvalContext;
import static org.junit.Assert.*;
import org.junit.Test;

public class CoreOperationEqualTest {

    @Test
    public void testComputeValue_equalValues() {
        // Create instances of CoreOperationEqual and Expression
        Expression arg1 = new Expression();
        Expression arg2 = new Expression();
        CoreOperationEqual coreOperationEqual = new CoreOperationEqual(arg1, arg2);

        // Create EvalContext instance
        EvalContext context = new EvalContext();

        // Call the computeValue method
        Object result = coreOperationEqual.computeValue(context);

        // Check if the result is true
        assertTrue((Boolean) result);
    }

    @Test
    public void testComputeValue_unequalValues() {
        // Create instances of CoreOperationEqual and Expression
        Expression arg1 = new Expression();
        Expression arg2 = new Expression();
        CoreOperationEqual coreOperationEqual = new CoreOperationEqual(arg1, arg2);

        // Create EvalContext instance
        EvalContext context = new EvalContext();

        // Call the computeValue method
        Object result = coreOperationEqual.computeValue(context);

        // Check if the result is false
        assertFalse((Boolean) result);
    }

    @Test
    public void testGetSymbol() {
        // Create instances of CoreOperationEqual and Expression
        Expression arg1 = new Expression();
        Expression arg2 = new Expression();
        CoreOperationEqual coreOperationEqual = new CoreOperationEqual(arg1, arg2);

        // Call the getSymbol method
        String symbol = coreOperationEqual.getSymbol();

        // Check if the symbol is "="
        assertEquals("=", symbol);
    }
}
