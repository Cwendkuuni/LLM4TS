import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.Expression;
import org.apache.commons.jxpath.ri.InfoSet;
import org.apache.commons.jxpath.ri.InfoSetUtil;
import org.apache.commons.jxpath.ri.NodeInfo;
import org.junit.Test;
import static org.junit.Assert.*;

public class CoreOperationLessThanOrEqualTest {

    @Test
    public void testLessThanOrEqual() {
        Expression arg1 = new LiteralExpression(new Double(5.0));
        Expression arg2 = new LiteralExpression(new Double(10.0));
        Expression expression = new CoreOperationLessThanOrEqual(arg1, arg2);

        EvalContext context = new EvalContext(new InfoSet());
        context.setCurrentNode(new NodeInfo(new Object(), ""));

        Object result = expression.computeValue(context);
        assertTrue("5.0 <= 10.0 should be true", result instanceof Boolean && ((Boolean) result).booleanValue());

        arg1 = new LiteralExpression(new Double(15.0));
        arg2 = new LiteralExpression(new Double(10.0));
        expression = new CoreOperationLessThanOrEqual(arg1, arg2);

        context.setCurrentNode(new NodeInfo(new Object(), ""));
        result = expression.computeValue(context);
        assertFalse("15.0 <= 10.0 should be false", result instanceof Boolean && !((Boolean) result).booleanValue());
    }

    @Test
    public void testLessThanOrEqualWithNaN() {
        Expression arg1 = new LiteralExpression(Double.NaN);
        Expression arg2 = new LiteralExpression(Double.NaN);
        Expression expression = new CoreOperationLessThanOrEqual(arg1, arg2);

        EvalContext context = new EvalContext(new InfoSet());
        context.setCurrentNode(new NodeInfo(new Object(), ""));

        Object result = expression.computeValue(context);
        assertTrue("NaN <= NaN should be true according to IEEE 754 standard", result instanceof Boolean && ((Boolean) result).booleanValue());
    }

    @Test(expected = RuntimeException.class)
    public void testLessThanOrEqualWithNullArguments() {
        Expression arg1 = new LiteralExpression(null);
        Expression arg2 = new LiteralExpression(new Double(5.0));
        Expression expression = new CoreOperationLessThanOrEqual(arg1, arg2);

        EvalContext context = new EvalContext(new InfoSet());
        context.setCurrentNode(new NodeInfo(new Object(), ""));

        expression.computeValue(context);
    }
}