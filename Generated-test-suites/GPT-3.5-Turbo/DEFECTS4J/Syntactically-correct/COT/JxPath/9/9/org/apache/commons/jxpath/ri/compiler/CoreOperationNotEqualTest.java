import static org.junit.Assert.*;
import org.junit.Test;
import org.apache.commons.jxpath.ri.compiler.*;

public class CoreOperationNotEqualTest {

    @Test
    public void testComputeValue() {
        // Basic test case, arg1 is not equal to arg2
        CoreOperationNotEqual op = new CoreOperationNotEqual(new Constant(1), new Constant(2));
        EvalContext context = new EvalContext();
        Object result = op.computeValue(context);
        assertEquals(Boolean.TRUE, result);

        // edge case - arg1 and arg2 are equal
        op = new CoreOperationNotEqual(new Constant("abc"), new Constant("abc"));
        result = op.computeValue(context);
        assertEquals(Boolean.FALSE, result);
    }

    @Test
    public void testGetSymbol() {
        CoreOperationNotEqual op = new CoreOperationNotEqual(null, null);
        String symbol = op.getSymbol();
        assertEquals("!=", symbol);
    }
}