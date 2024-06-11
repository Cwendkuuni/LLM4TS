package org.apache.commons.jxpath.ri.compiler.test;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.compiler.CoreOperationNotEqual;
import org.apache.commons.jxpath.ri.compiler.CoreOperationCompare;
import org.apache.commons.jxpath.ri.compiler.Expression;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class CoreOperationNotEqualTest {

    @Test
    public void testCoreOperationNotEqualConstructor() {
        CoreOperationNotEqual coreOperationNotEqual = new CoreOperationNotEqual(new CoreOperationCompare(new ConstantExpression(1), new ConstantExpression(2)), new CoreOperationCompare(new ConstantExpression(2), new ConstantExpression(1)));
        assertNotNull(coreOperationNotEqual);
    }

    @Test
    public void testComputeValue() {
        CoreOperationNotEqual coreOperationNotEqual = new CoreOperationNotEqual(new ConstantExpression(1), new ConstantExpression(2));
        EvalContext context = new EvalContext();
        Object result = coreOperationNotEqual.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.FALSE, result);

        coreOperationNotEqual = new CoreOperationNotEqual(new ConstantExpression(2), new ConstantExpression(1));
        result = coreOperationNotEqual.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.TRUE, result);
    }

    @Test(expected = NullPointerException.class)
    public void testComputeValueNullArg() {
        CoreOperationNotEqual coreOperationNotEqual = new CoreOperationNotEqual(null, new ConstantExpression(1));
        EvalContext context = new EvalContext();
        coreOperationNotEqual.computeValue(context);
    }

    @Test(expected = NullPointerException.class)
    public void testComputeValueNullArg2() {
        CoreOperationNotEqual coreOperationNotEqual = new CoreOperationNotEqual(new ConstantExpression(1), null);
        EvalContext context = new EvalContext();
        coreOperationNotEqual.computeValue(context);
    }

    @Test
    public void testGetSymbol() {
        CoreOperationNotEqual coreOperationNotEqual = new CoreOperationNotEqual(new ConstantExpression(1), new ConstantExpression(2));
        assertEquals("!=", coreOperationNotEqual.getSymbol());
    }
}