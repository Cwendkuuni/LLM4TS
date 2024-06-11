package org.apache.commons.jxpath.ri.compiler;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InfoSet;
import org.apache.commons.jxpath.ri.InfoSetUtil;
import org.apache.commons.jxpath.ri.NodeInfo;
import org.apache.commons.jxpath.ri.Path;
import org.apache.commons.jxpath.ri.compiler.CoreOperationLessThanOrEqual;
import org.apache.commons.jxpath.ri.compiler.Expression;
import org.apache.commons.jxpath.ri.exc.XPathExpressionException;

import java.util.ArrayList;
import java.util.List;

/**
 * Test file for CoreOperationLessThanOrEqual class.
 */
public class CoreOperationLessThanOrEqualTest {

    /**
     * Test method for {@link CoreOperationLessThanOrEqual#CoreOperationLessThanOrEqual(Expression, Expression)}.
     */
    @org.junit.Test
    public void testCoreOperationLessThanOrEqualConstruction() {
        Expression arg1 = new LiteralExpression(1.0);
        Expression arg2 = new LiteralExpression(2.0);
        CoreOperationLessThanOrEqual coreOperationLessThanOrEqual = new CoreOperationLessThanOrEqual(arg1, arg2);
        assertNotNull(coreOperationLessThanOrEqual);
        assertNull(coreOperationLessThanOrEqual.getSymbol());
        assertEquals(2, coreOperationLessThanOrEqual.getArity());
        assertEquals(arg1, coreOperationLessThanOrEqual.getArgs()[0]);
        assertEquals(arg2, coreOperationLessThanOrEqual.getArgs()[1]);
    }

    /**
     * Test method for {@link CoreOperationLessThanOrEqual#getSymbol()}.
     */
    @org.junit.Test
    public void testGetSymbol() {
        Expression arg1 = new LiteralExpression(1.0);
        Expression arg2 = new LiteralExpression(2.0);
        CoreOperationLessThanOrEqual coreOperationLessThanOrEqual = new CoreOperationLessThanOrEqual(arg1, arg2);
        String symbol = coreOperationLessThanOrEqual.getSymbol();
        assertTrue(symbol.equals("<="));
    }

    /**
     * Test method for {@link CoreOperationLessThanOrEqual#computeValue(EvalContext)}.
     */
    @org.junit.Test
    public void testComputeValue() throws XPathExpressionException {
        // Test with Boolean values
        Expression arg1 = new LiteralExpression(true);
        Expression arg2 = new LiteralExpression(true);
        CoreOperationLessThanOrEqual coreOperationLessThanOrEqual = new CoreOperationLessThanOrEqual(arg1, arg2);
        Object result = coreOperationLessThanOrEqual.computeValue(new EvalContext(new InfoSet(), new Path()));
        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.TRUE, result);

        arg1 = new LiteralExpression(true);
        arg2 = new LiteralExpression(false);
        coreOperationLessThanOrEqual = new CoreOperationLessThanOrEqual(arg1, arg2);
        result = coreOperationLessThanOrEqual.computeValue(new EvalContext(new InfoSet(), new Path()));
        assertFalse(result instanceof Boolean);
        assertNull(result);

        arg1 = new LiteralExpression(false);
        arg2 = new LiteralExpression(true);
        coreOperationLessThanOrEqual = new CoreOperationLessThanOrEqual(arg1, arg2);
        result = coreOperationLessThanOrEqual.computeValue(new EvalContext(new InfoSet(), new Path()));
        assertFalse(result instanceof Boolean);
        assertNull(result);

        arg1 = new LiteralExpression(false);
        arg2 = new LiteralExpression(false);
        coreOperationLessThanOrEqual = new CoreOperationLessThanOrEqual(arg1, arg2);
        result = coreOperationLessThanOrEqual.computeValue(new EvalContext(new InfoSet(), new Path()));
        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.TRUE, result);

        // Test with Number values
        arg1 = new LiteralExpression(1.0);
        arg2 = new LiteralExpression(2.0);
        coreOperationLessThanOrEqual = new CoreOperationLessThanOrEqual(arg1, arg2);
        result = coreOperationLessThanOrEqual.computeValue(new EvalContext(new InfoSet(), new Path()));
        assertFalse(result instanceof Boolean);
        assertNull(result);

        arg1 = new LiteralExpression(1.0);
        arg2 = new LiteralExpression(1.0);
        coreOperationLessThanOrEqual = new CoreOperationLessThanOrEqual(arg1, arg2);
        result = coreOperationLessThanOrEqual.computeValue(new EvalContext(new InfoSet(), new Path()));
        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.TRUE, result);

        arg1 = new LiteralExpression(1.0);
        arg2 = new LiteralExpression(2.0);
        coreOperationLessThanOrEqual = new CoreOperationLessThanOrEqual(arg1, arg2);
        result = coreOperationLessThanOrEqual.computeValue(new EvalContext(new InfoSet(), new Path()));
        assertFalse(result instanceof Boolean);
        assertNull(result);

        arg1 = new LiteralExpression(2.0);
        arg2 = new LiteralExpression(1.0);
        coreOperationLessThanOrEqual = new CoreOperationLessThanOrEqual(arg1, arg2);
        result = coreOperationLessThanOrEqual.computeValue(new EvalContext(new InfoSet(), new Path()));
        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.FALSE, result);

        arg1 = new LiteralExpression(2.0);
        arg2 = new LiteralExpression(2.0);
        coreOperationLessThanOrEqual = new CoreOperationLessThanOrEqual(arg1, arg2);
        result = coreOperationLessThanOrEqual.computeValue(new EvalContext(new InfoSet(), new Path()));
        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.TRUE, result);

        // Test with Node values
        arg1 = new VariableExpression("x");
        arg2 = new LiteralExpression(2.0);
        InfoSet infoSet = new InfoSet();
        NodeInfo nodeInfo = new NodeInfo("root", new int[0], new int[0], null, new int[0], null);
        infoSet.addNode(nodeInfo);
        EvalContext context = new EvalContext(infoSet, new Path());
        context.setVariables(new ArrayList<String, Integer>() {{
            add("x", nodeInfo);
        }});
        coreOperationLessThanOrEqual = new CoreOperationLessThanOrEqual(arg1, arg2);
        result = coreOperationLessThanOrEqual.computeValue(context);
        assertFalse(result instanceof Boolean);
        assertNull(result);

        arg1 = new VariableExpression("x");
        arg2 = new LiteralExpression(1.0);
        coreOperationLessThanOrEqual = new CoreOperationLessThanOrEqual(arg1, arg2);
        result = coreOperationLessThanOrEqual.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.TRUE, result);

        arg1 = new LiteralExpression(1.0);
        arg2 = new VariableExpression("x");
        coreOperationLessThanOrEqual = new CoreOperationLessThanOrEqual(arg1, arg2);
        result = coreOperationLessThanOrEqual.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.FALSE, result);

        arg1 = new VariableExpression("x");
        arg2 = new VariableExpression("y");
        context.setVariables(new ArrayList<String, Integer>() {{
            add("x", nodeInfo);
            add("y", nodeInfo);
        }});
        coreOperationLessThanOrEqual = new CoreOperationLessThanOrEqual(arg1, arg2);
        result = coreOperationLessThanOrEqual.computeValue(context);
        assertFalse(result instanceof Boolean);
        assertNull(result);

        arg1 = new VariableExpression("x");
        arg2 = new VariableExpression("y");
        double xValue = 2.0;
        double yValue = 1.0;
        nodeInfo.setValue(xValue);
        nodeInfo.setType(NodeInfo.NUMBER_TYPE);
        context.setVariables(new ArrayList<String, Integer>() {{
            add("x", nodeInfo);
            add("y", new NodeInfo("y", new int[0], new int[0], null, new int[0], new Double(yValue)));
        }});
        coreOperationLessThanOrEqual = new CoreOperationLessThanOrEqual(arg1, arg2);
        result = coreOperationLessThanOrEqual.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.TRUE, result);

        nodeInfo.setValue(yValue);
        context.setVariables(new ArrayList<String, Integer>() {{
            add("x", nodeInfo);
            add("y", new NodeInfo("y", new int[0], new int[0], null, new int[0], new Double(xValue)));
        }});
        coreOperationLessThanOrEqual = new CoreOperationLessThanOrEqual(arg1, arg2);
        result = coreOperationLessThanOrEqual.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.FALSE, result);
    }
}