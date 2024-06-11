import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.expression.Expression;
import org.junit.Test;
import static org.junit.Assert.*;

public class CoreOperationEqualTest {

    @Test
    public void testComputeValue() {
        Expression arg1 = new LiteralExpression(1);
        Expression arg2 = new LiteralExpression(1);
        CoreOperationEqual coreOperationEqual = new CoreOperationEqual(arg1, arg2);
        EvalContext context = new EvalContext();
        Boolean expected = Boolean.TRUE;
        Object result = coreOperationEqual.computeValue(context);
        assertEquals(expected, result);
    }

    @Test
    public void testEdgeCases() {
        // Test cases for edge cases and exception handling scenarios
    }
}