package org.apache.commons.jxpath.ri.compiler.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.Expression;
import org.apache.commons.jxpath.ri.InfoSet;
import org.apache.commons.jxpath.ri.NodeInfo;
import org.apache.commons.jxpath.ri.Path;
import org.apache.commons.jxpath.ri.compiler.CoreOperationLessThanOrEqual;
import org.junit.Test;

/**
 * Test cases for CoreOperationLessThanOrEqual class
 */
public class CoreOperationLessThanOrEqualTest {

    @Test
    public void testCoreOperationLessThanOrEqualConstructor() {
        Expression arg1 = new Path("1.0");
        Expression arg2 = new Path("2.0");
        CoreOperationLessThanOrEqual operation = new CoreOperationLessThanOrEqual(arg1, arg2);
        assertNotNull(operation);
    }

    @Test
    public void testComputeValue_true() {
        EvalContext context = new EvalContext(new InfoSet(){
            {
                addNodeInfo(new NodeInfo("root", null, null));
                addNodeInfo(new NodeInfo("number1", null, new Double(1.0)));
                addNodeInfo(new NodeInfo("number2", null, new Double(2.0)));
            }
        });
        Expression arg1 = new Path("number1");
        Expression arg2 = new Path("number2");
        CoreOperationLessThanOrEqual operation = new CoreOperationLessThanOrEqual(arg1, arg2);
        Object result = operation.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.TRUE, result);
    }

    @Test
    public void testComputeValue_false() {
        EvalContext context = new EvalContext(new InfoSet(){
            {
                addNodeInfo(new NodeInfo("root", null, null));
                addNodeInfo(new NodeInfo("number1", null, new Double(3.0)));
                addNodeInfo(new NodeInfo("number2", null, new Double(2.0)));
            }
        });
        Expression arg1 = new Path("number1");
        Expression arg2 = new Path("number2");
        CoreOperationLessThanOrEqual operation = new CoreOperationLessThanOrEqual(arg1, arg2);
        Object result = operation.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.FALSE, result);
    }

    @Test
    public void testComputeValue_null() {
        EvalContext context = new EvalContext(new InfoSet());
        Expression arg1 = new Path("number1");
        Expression arg2 = new Path("number2");
        CoreOperationLessThanOrEqual operation = new CoreOperationLessThanOrEqual(arg1, arg2);
        Object result = operation.computeValue(context);
        assertNull(result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testComputeValue_lessThanTwoArguments() {
        EvalContext context = new EvalContext(new InfoSet());
        Expression arg1 = new Path("number1");
        CoreOperationLessThanOrEqual operation = new CoreOperationLessThanOrEqual(arg1);
        operation.computeValue(context);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testComputeValue_argumentsAreNotDouble() {
        EvalContext context = new EvalContext(new InfoSet());
        Expression arg1 = new Path("number1");
        Expression arg2 = new Path("string1");
        CoreOperationLessThanOrEqual operation = new CoreOperationLessThanOrEqual(arg1, arg2);
        operation.computeValue(context);
    }

    @Test
    public void testGetSymbol() {
        CoreOperationLessThanOrEqual operation = new CoreOperationLessThanOrEqual(null, null);
        String symbol = operation.getSymbol();
        assertEquals("<=", symbol);
    }
}