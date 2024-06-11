package org.apache.commons.jxpath.ri.compiler;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.apache.commons.jxpath.Expression;
import org.apache.commons.jxpath.ri.ContextBean;
import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InfoSetUtil;
import org.junit.Before;
import org.junit.Test;

public class CoreOperationGreaterThanTest {
    private CoreOperationGreaterThan operation;

    @Before
    public void setUp() {
        Expression arg1 = new CoreVariableReference("a");
        Expression arg2 = new CoreVariableReference("b");
        operation = new CoreOperationGreaterThan(arg1, arg2);
    }

    @Test
    public void testComputeValue() {
        EvalContext context = new EvalContext(new ContextBean());

        context.getVariables().declareVariable("a", 5);
        context.getVariables().declareVariable("b", 3);
        Object result = operation.computeValue(context);
        assertTrue((Boolean) result);

        context.getVariables().declareVariable("a", 3);
        context.getVariables().declareVariable("b", 5);
        result = operation.computeValue(context);
        assertFalse((Boolean) result);

        context.getVariables().declareVariable("a", 3);
        context.getVariables().declareVariable("b", 3);
        result = operation.computeValue(context);
        assertFalse((Boolean) result);
    }

    @Test
    public void testGetSymbol() {
        String symbol = operation.getSymbol();
        assertEquals(">", symbol);
    }
}