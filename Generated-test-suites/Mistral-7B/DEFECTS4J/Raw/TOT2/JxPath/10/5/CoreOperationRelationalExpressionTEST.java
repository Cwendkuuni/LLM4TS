package org.apache.commons.jxpath.ri.compiler;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.SimpleContext;
import org.junit.Test;
import static org.junit.Assert.*;

public class CoreOperationRelationalExpressionTest {
    @Test
    public void testComputeValue_Basic() {
        CoreOperationRelationalExpression expression = new MyCoreOperationRelationalExpression(new ConstantExpression(5), new ConstantExpression(10));
        EvalContext context = new SimpleContext();
        Object result = expression.computeValue(context);
        assertEquals(Boolean.FALSE, result);
    }

    @Test
    public void testComputeValue_EdgeCase_NullInput() {
        CoreOperationRelationalExpression expression = new MyCoreOperationRelationalExpression(new ConstantExpression(null), new ConstantExpression(5));
        EvalContext context = new SimpleContext();
        Object result = expression.computeValue(context);
        assertEquals(Boolean.FALSE, result);
    }

    @Test(expected = ClassCastException.class)
    public void testComputeValue_Exception_DifferentTypes() {
        CoreOperationRelationalExpression expression = new MyCoreOperationRelationalExpression(new ConstantExpression("Hello"), new ConstantExpression(5));
        EvalContext context = new SimpleContext();
        expression.computeValue(context);
    }
}