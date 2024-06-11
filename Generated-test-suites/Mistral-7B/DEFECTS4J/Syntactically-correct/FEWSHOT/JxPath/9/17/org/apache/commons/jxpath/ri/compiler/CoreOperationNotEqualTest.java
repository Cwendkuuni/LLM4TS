package org.apache.commons.jxpath.ri.compiler;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.Expression;
import org.apache.commons.jxpath.ri.NodeInfo;
import org.junit.Test;

public class CoreOperationNotEqualTest {

    @Test
    public void testCoreOperationNotEqual() {
        Expression expr1 = new ConstantExpression(new Integer(1));
        Expression expr2 = new ConstantExpression(new Integer(2));
        CoreOperationNotEqual coreOperationNotEqual = new CoreOperationNotEqual(expr1, expr2);
        assertFalse(coreOperationNotEqual.computeValue(new EvalContext(new NodeInfo(null, null))).toString().equals("true"));
    }

    @Test
    public void testCoreOperationNotEqualWithNull() {
        Expression expr1 = new ConstantExpression(null);
        Expression expr2 = new ConstantExpression("foo");
        CoreOperationNotEqual coreOperationNotEqual = new CoreOperationNotEqual(expr1, expr2);
        assertNull(coreOperationNotEqual.computeValue(new EvalContext(new NodeInfo(null, null))));
    }

    @Test
    public void testCoreOperationNotEqualWithSameObjects() {
        Expression expr1 = new ConstantExpression(new Integer(1));
        Expression expr2 = expr1;
        CoreOperationNotEqual coreOperationNotEqual = new CoreOperationNotEqual(expr1, expr2);
        assertFalse(coreOperationNotEqual.computeValue(new EvalContext(new NodeInfo(null, null))).toString().equals("true"));
    }

    @Test
    public void testCoreOperationNotEqualWithSameValuesButDifferentObjects() {
        Expression expr1 = new ConstantExpression(new Integer(1));
        Expression expr2 = new ConstantExpression(new Integer(1));
        CoreOperationNotEqual coreOperationNotEqual = new CoreOperationNotEqual(expr1, expr2);
        assertTrue(coreOperationNotEqual.computeValue(new EvalContext(new NodeInfo(null, null))).toString().equals("false"));
    }

    @Test
    public void testCoreOperationNotEqualWithBooleanValues() {
        Expression expr1 = new ConstantExpression(Boolean.TRUE);
        Expression expr2 = new ConstantExpression(Boolean.FALSE);
        CoreOperationNotEqual coreOperationNotEqual = new CoreOperationNotEqual(expr1, expr2);
        assertFalse(coreOperationNotEqual.computeValue(new EvalContext(new NodeInfo(null, null))).toString().equals("true"));
    }

    @Test
    public void testCoreOperationNotEqualWithStringValues() {
        Expression expr1 = new ConstantExpression("foo");
        Expression expr2 = new ConstantExpression("bar");
        CoreOperationNotEqual coreOperationNotEqual = new CoreOperationNotEqual(expr1, expr2);
        assertFalse(coreOperationNotEqual.computeValue(new EvalContext(new NodeInfo(null, null))).toString().equals("true"));
    }

    @Test
    public void testCoreOperationNotEqualWithNumberValues() {
        Expression expr1 = new ConstantExpression(new Double(1.0));
        Expression expr2 = new ConstantExpression(new Double(2.0));
        CoreOperationNotEqual coreOperationNotEqual = new CoreOperationNotEqual(expr1, expr2);
        assertFalse(coreOperationNotEqual.computeValue(new EvalContext(new NodeInfo(null, null))).toString().equals("true"));
    }
}