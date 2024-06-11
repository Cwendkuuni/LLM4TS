package org.apache.commons.jxpath.ri.compiler;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InfoSetUtil;
import org.junit.Test;

import static org.junit.Assert.*;

public class CoreOperationGreaterThanTest {
    @Test
    public void testComputeValue() {
        CoreOperationGreaterThan expression = new CoreOperationGreaterThan(new Constant(5), new Constant(3));
        EvalContext context = new EvalContext();
        Object result = expression.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result);

        CoreOperationGreaterThan expression2 = new CoreOperationGreaterThan(new Constant(3), new Constant(5));
        EvalContext context2 = new EvalContext();
        Object result2 = expression2.computeValue(context2);
        assertTrue(result2 instanceof Boolean);
        assertFalse((Boolean) result2);

        CoreOperationGreaterThan expression3 = new CoreOperationGreaterThan(new Constant(5), new Constant(5));
        EvalContext context3 = new EvalContext();
        Object result3 = expression3.computeValue(context3);
        assertTrue(result3 instanceof Boolean);
        assertFalse((Boolean) result3);
    }

    @Test
    public void testGetSymbol() {
        CoreOperationGreaterThan expression = new CoreOperationGreaterThan(new Constant(5), new Constant(3));
        assertEquals(">", expression.getSymbol());
    }
}