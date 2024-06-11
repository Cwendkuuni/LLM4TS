// CoreOperationLessThanOrEqualTest1.java
import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InfoSet;
import org.apache.commons.jxpath.ri.NodeInfo;
import org.apache.commons.jxpath.ri.compiler.CoreOperationLessThanOrEqual;
import org.apache.commons.jxpath.ri.compiler.Expression;
import org.junit.Test;
import static org.junit.Assert.*;

public class CoreOperationLessThanOrEqualTest1 {

    @Test
    public void testCoreOperationLessThanOrEqual() {
        Expression arg1 = new LiteralExpression(new NodeInfo(1.5));
        Expression arg2 = new LiteralExpression(new NodeInfo(2.0));
        CoreOperationLessThanOrEqual coreOperationLessThanOrEqual = new CoreOperationLessThanOrEqual(arg1, arg2);
        EvalContext context = new EvalContext(new InfoSet());
        Object result = coreOperationLessThanOrEqual.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.FALSE, result);

        arg1 = new LiteralExpression(new NodeInfo(1.5));
        arg2 = new LiteralExpression(new NodeInfo(1.5));
        coreOperationLessThanOrEqual = new CoreOperationLessThanOrEqual(arg1, arg2);
        result = coreOperationLessThanOrEqual.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.TRUE, result);

        arg1 = new LiteralExpression(new NodeInfo(1.5));
        arg2 = new LiteralExpression(new NodeInfo(1.6));
        coreOperationLessThanOrEqual = new CoreOperationLessThanOrEqual(arg1, arg2);
        result = coreOperationLessThanOrEqual.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.FALSE, result);
    }

    @Test(expected = RuntimeException.class)
    public void testCoreOperationLessThanOrEqualNullArgument() {
        Expression arg1 = null;
        Expression arg2 = new LiteralExpression(new NodeInfo(2.0));
        CoreOperationLessThanOrEqual coreOperationLessThanOrEqual = new CoreOperationLessThanOrEqual(arg1, arg2);
        EvalContext context = new EvalContext(new InfoSet());
        coreOperationLessThanOrEqual.computeValue(context);
    }
}