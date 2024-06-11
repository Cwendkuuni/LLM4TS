import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InfoSet;
import org.apache.commons.jxpath.ri.nodes.NumberNode;
import org.apache.commons.jxpath.ri.nodes.VariableRefNode;
import org.apache.commons.jxpath.ri.test.AbstractJXPathTest;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * Test cases for CoreOperationGreaterThanOrEqual class.
 */
public class CoreOperationGreaterThanOrEqualTest extends AbstractJXpathTest {

    @Test
    public void testGreaterThanOrEqual() {
        EvalContext context = new EvalContext();
        Expression arg1 = new VariableRefNode("x", new NumberNode(10.5));
        Expression arg2 = new VariableRefNode("y", new NumberNode(5.5));
        CoreOperationGreaterThanOrEqual expression = new CoreOperationGreaterThanOrEqual(arg1, arg2);
        Object result = expression.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result);
    }

    @Test
    public void testLessThan() {
        EvalContext context = new EvalContext();
        Expression arg1 = new VariableRefNode("x", new NumberNode(5.5));
        Expression arg2 = new VariableRefNode("y", new NumberNode(10.5));
        CoreOperationGreaterThanOrEqual expression = new CoreOperationGreaterThanOrEqual(arg1, arg2);
        Object result = expression.computeValue(context);
        assertFalse(result instanceof Boolean);
        assertNotNull(result);
    }

    @Test
    public void testEqual() {
        EvalContext context = new EvalContext();
        Expression arg1 = new VariableRefNode("x", new NumberNode(10.5));
        Expression arg2 = new VariableRefNode("y", new NumberNode(10.5));
        CoreOperationGreaterThanOrEqual expression = new CoreOperationGreaterThanOrEqual(arg1, arg2);
        Object result = expression.computeValue(context);
        assertEquals(result, Boolean.TRUE);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidArgumentTypes() {
        EvalContext context = new EvalContext();
        Expression arg1 = new VariableRefNode("x", new NumberNode(10));
        Expression arg2 = new VariableRefNode("y", new StringNode("hello"));
        CoreOperationGreaterThanOrEqual expression = new CoreOperationGreaterThanOrEqual(arg1, arg2);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullArguments() {
        EvalContext context = new EvalContext();
        Expression arg1 = new VariableRefNode("x", null);
        Expression arg2 = new VariableRefNode("y", null);
        CoreOperationGreaterThanOrEqual expression = new CoreOperationGreaterThanOrEqual(arg1, arg2);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMismatchedArgumentTypes() {
        EvalContext context = new EvalContext();
        Expression arg1 = new VariableRefNode("x", new NumberNode(10.5));
        Expression arg2 = new VariableRefNode("y", new NumberNode(5));
        CoreOperationGreaterThanOrEqual expression = new CoreOperationGreaterThanOrEqual(arg1, arg2);
    }
}