import static org.junit.Assert.*;
import org.junit.Test;
import org.apache.commons.jxpath.ri.EvalContext;

public class CoreOperationNotEqualTest {

    @Test
    public void testComputeValue() {
        CoreOperationNotEqual operation = new CoreOperationNotEqual(arg1, arg2);
        EvalContext context = new EvalContext();

        // Test case 1: args[0] equals args[1]
        context.getNodeSet().add(arg1);
        context.getNodeSet().add(arg2);
        Object result = operation.computeValue(context);
        assertEquals(Boolean.FALSE, result);

        // Test case 2: args[0] does not equal args[1]
        context.getNodeSet().clear();
        context.getNodeSet().add(arg1);
        context.getNodeSet().add(arg3);
        result = operation.computeValue(context);
        assertEquals(Boolean.TRUE, result);
    }

    @Test
    public void testGetSymbol() {
        CoreOperationNotEqual operation = new CoreOperationNotEqual(null, null);
        String symbol = operation.getSymbol();
        assertEquals("!=", symbol);
    }
}