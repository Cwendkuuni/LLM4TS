import org.apache.commons.jxpath.ri.compiler.CoreOperationRelationalExpression;
import org.apache.commons.jxpath.ri.compiler.CoreOperation;
import org.apache.commons.jxpath.ri.EvalContext;
import org.junit.Test;

public class CoreOperationRelationalExpressionTest {
    @Test
    public void testComputeValue() {
        CoreOperationRelationalExpression expr = new CoreOperationRelationalExpression(new CoreOperation[] {new CoreOperation(1), new CoreOperation(2)});
        Object result = expr.computeValue(new EvalContext());
        assert (result instanceof Boolean);
        assertTrue(result.equals(Boolean.TRUE)); // typical use case: true result
        expr = new CoreOperationRelationalExpression(new CoreOperation[] {new CoreOperation(2), new CoreOperation(1)});
        result = expr.computeValue(new EvalContext());
        assert (result instanceof Boolean);
        assertFalse(result.equals(Boolean.TRUE)); // typical use case: false result
    }
}