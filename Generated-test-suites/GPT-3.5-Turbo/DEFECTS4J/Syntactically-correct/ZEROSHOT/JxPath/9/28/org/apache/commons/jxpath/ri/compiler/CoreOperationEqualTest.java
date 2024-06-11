import static org.junit.Assert.*;
import org.junit.Test;
import org.apache.commons.jxpath.ri.compiler.Expression;
import org.apache.commons.jxpath.ri.EvalContext;

public class CoreOperationEqualTest {

    @Test
    public void testComputeValue_equal() {
        Expression arg1 = new Expression();
        Expression arg2 = new Expression();
        CoreOperationEqual operation = new CoreOperationEqual(arg1, arg2);

        EvalContext context = new EvalContext();

        Object result = operation.computeValue(context);

        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result);
    }

    @Test
    public void testComputeValue_notEqual() {
        Expression arg1 = new Expression();
        Expression arg2 = new Expression();
        CoreOperationEqual operation = new CoreOperationEqual(arg1, arg2);

        EvalContext context = new EvalContext();

        Object result = operation.computeValue(context);

        assertTrue(result instanceof Boolean);
        assertFalse((Boolean) result);
    }

    @Test
    public void testGetSymbol() {
        Expression arg1 = new Expression();
        Expression arg2 = new Expression();
        CoreOperationEqual operation = new CoreOperationEqual(arg1, arg2);

        String symbol = operation.getSymbol();

        assertEquals("=", symbol);
    }
}