import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.Expression;
import org.apache.commons.jxpath.ri.InfoSetUtil;
import org.apache.commons.jxpath.ri.compiler.CoreOperationLessThanOrEqual;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class CoreOperationLessThanOrEqualTest {
    @Test
    public void testCoreOperationLessThanOrEqual() {
        Expression arg1 = new LiteralExpression(1.0);
        Expression arg2 = new LiteralExpression(2.0);
        CoreOperationLessThanOrEqual operation = new CoreOperationLessThanOrEqual(arg1, arg2);

        EvalContext context = new EvalContext();
        Object result = operation.computeValue(context);
        assertEquals(Boolean.FALSE, result);

        arg1 = new LiteralExpression(2.0);
        arg2 = new LiteralExpression(2.0);
        operation = new CoreOperationLessThanOrEqual(arg1, arg2);

        context = new EvalContext();
        result = operation.computeValue(context);
        assertEquals(Boolean.TRUE, result);

        arg1 = new LiteralExpression(1.5);
        arg2 = new LiteralExpression(2.0);
        operation = new CoreOperationLessThanOrEqual(arg1, arg2);

        context = new EvalContext();
        result = operation.computeValue(context);
        assertEquals(Boolean.FALSE, result);
    }
}