import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.Expression;
import org.apache.commons.jxpath.ri.InfoSet;
import org.apache.commons.jxpath.ri.NodeInfo;
import org.apache.commons.jxpath.ri.PathContext;
import org.apache.commons.jxpath.ri.compiler.CoreOperationGreaterThanOrEqual;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Tests for {@link CoreOperationGreaterThanOrEqual} class.
 */
public class CoreOperationGreaterThanOrEqualTest {

    private Expression arg1;
    private Expression arg2;
    private CoreOperationGreaterThanOrEqual operation;
    private EvalContext context;
    private InfoSet infoSet;

    @Before
    public void setup() {
        arg1 = new LiteralExpression(new Double(5.0));
        arg2 = new LiteralExpression(new Double(3.0));
        operation = new CoreOperationGreaterThanOrEqual(arg1, arg2);
        context = new EvalContext();
        infoSet = new PathContext(new NodeInfo(1), new Object[]{"root"}, new String[]{"/"}, new Object[0]);
    }

    @Test
    public void testGreaterThanOrEqual() {
        infoSet.setCurrentInfo(new NodeInfo(1));
        context.setInfoSet(infoSet);
        Object result = operation.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result);
    }

    @Test
    public void testLessThan() {
        arg2 = new LiteralExpression(new Double(7.0));
        infoSet.setCurrentInfo(new NodeInfo(1));
        context.setInfoSet(infoSet);
        Object result = operation.computeValue(context);
        assertFalse(result instanceof Boolean);
        assertFalse(result == Boolean.TRUE);
    }

    @Test
    public void testEqual() {
        arg2 = new LiteralExpression(new Double(5.0));
        infoSet.setCurrentInfo(new NodeInfo(1));
        context.setInfoSet(infoSet);
        Object result = operation.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertTrue(result == Boolean.FALSE);
    }

    @Test(expected = ClassCastException.class)
    public void testNonNumericArg1() {
        arg1 = new LiteralExpression("test");
        infoSet.setCurrentInfo(new NodeInfo(1));
        context.setInfoSet(infoSet);
        operation.computeValue(context);
    }

    @Test(expected = ClassCastException.class)
    public void testNonNumericArg2() {
        arg2 = new LiteralExpression("test");
        infoSet.setCurrentInfo(new NodeInfo(1));
        context.setInfoSet(infoSet);
        operation.computeValue(context);
    }
}