package org.apache.commons.jxpath.ri.compiler;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.compiler.CoreOperationNotEqual;
import org.apache.commons.jxpath.ri.compiler.CoreOperationCompare;
import org.apache.commons.jxpath.ri.expression.ConstantExpression;
import org.junit.Test;
import static org.junit.Assert.*;

public class CoreOperationNotEqualTest {

    @Test
    public void testComputeValue() {
        CoreOperationNotEqual coreOperationNotEqual = new CoreOperationNotEqual(new ConstantExpression(1), new ConstantExpression(2));
        EvalContext context = new EvalContext();
        Object result = coreOperationNotEqual.computeValue(context);
        assertFalse(result.equals(Boolean.TRUE));
    }

    @Test
    public void testComputeValueSameValues() {
        CoreOperationNotEqual coreOperationNotEqual = new CoreOperationNotEqual(new ConstantExpression(1), new ConstantExpression(1));
        EvalContext context = new EvalContext();
        Object result = coreOperationNotEqual.computeValue(context);
        assertTrue(result.equals(Boolean.FALSE));
    }

    @Test
    public void testComputeValueDifferentTypes() {
        CoreOperationNotEqual coreOperationNotEqual = new CoreOperationNotEqual(new ConstantExpression(1), new ConstantExpression("1"));
        EvalContext context = new EvalContext();
        Object result = coreOperationNotEqual.computeValue(context);
        assertTrue(result.equals(Boolean.TRUE));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testComputeValueNullArgs() {
        CoreOperationNotEqual coreOperationNotEqual = new CoreOperationNotEqual(null, new ConstantExpression(1));
        EvalContext context = new EvalContext();
        coreOperationNotEqual.computeValue(context);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testComputeValueNullContext() {
        CoreOperationNotEqual coreOperationNotEqual = new CoreOperationNotEqual(new ConstantExpression(1), new ConstantExpression(2));
        coreOperationNotEqual.computeValue(null);
    }
}