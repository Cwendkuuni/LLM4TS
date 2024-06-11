package org.apache.commons.jxpath.ri.compiler;

import org.apache.commons.jxpath.ri.compiler.CoreOperationGreaterThanOrEqual;
import org.apache.commons.jxpath.ri.compiler.CoreOperationRelationalExpression;
import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.Expression;
import org.apache.commons.jxpath.ri.InfoSetUtil;
import org.junit.Test;
import static org.junit.Assert.*;

public class CoreOperationGreaterThanOrEqualTest {

    @Test
    public void testCoreOperationGreaterThanOrEqual() {
        CoreOperationGreaterThanOrEqual coreOperationGreaterThanOrEqual = new CoreOperationGreaterThanOrEqual(
                new ConstantExpression(new Double(5.0)), new ConstantExpression(new Double(5.0)));
        EvalContext context = new EvalContext();
        Object result = coreOperationGreaterThanOrEqual.computeValue(context);
        assertEquals(Boolean.TRUE, result);
    }

    @Test
    public void testCoreOperationGreaterThanOrEqual_InvalidInput() {
        CoreOperationGreaterThanOrEqual coreOperationGreaterThanOrEqual = new CoreOperationGreaterThanOrEqual(
                new ConstantExpression(new Integer(5)), new ConstantExpression(new String("5.5")));
        EvalContext context = new EvalContext();
        Object result = coreOperationGreaterThanOrEqual.computeValue(context);
        assertEquals(Boolean.FALSE, result);
    }

    @Test(expected = ClassCastException.class)
    public void testCoreOperationGreaterThanOrEqual_TypeMismatch() {
        CoreOperationGreaterThanOrEqual coreOperationGreaterThanOrEqual = new CoreOperationGreaterThanOrEqual(
                new ConstantExpression(new Integer(5)), new ConstantExpression(new Integer(4)));
        EvalContext context = new EvalContext();
        Object result = coreOperationGreaterThanOrEqual.computeValue(context);
        // This test case should fail with a ClassCastException
    }
}