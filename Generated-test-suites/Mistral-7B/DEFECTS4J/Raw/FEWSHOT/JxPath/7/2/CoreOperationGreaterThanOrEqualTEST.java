package org.apache.commons.jxpath.ri.compiler;

import static org.junit.Assert.*;
import org.apache.commons.jxpath.ri.InfoSet;
import org.apache.commons.jxpath.ri.InfoSetUtil;
import org.apache.commons.jxpath.ri.EvalContext;
import org.junit.Test;

public class CoreOperationGreaterThanOrEqualTest {

    @Test
    public void testCoreOperationGreaterThanOrEqual() {
        InfoSet infoSet = new InfoSet();
        EvalContext context = new EvalContext(infoSet);

        Expression arg1 = new ConstantExpression(1.5);
        Expression arg2 = new ConstantExpression(1.0);
        CoreOperationGreaterThanOrEqual expression = new CoreOperationGreaterThanOrEqual(arg1, arg2);

        Boolean result = (Boolean) expression.computeValue(context);
        assertTrue(result);

        arg2 = new ConstantExpression(2.0);
        result = (Boolean) expression.computeValue(context);
        assertFalse(result);

        arg1 = new ConstantExpression(2.5);
        arg2 = new ConstantExpression(2.0);
        result = (Boolean) expression.computeValue(context);
        assertTrue(result);

        arg1 = new ConstantExpression(0.5);
        arg2 = new ConstantExpression(1.0);
        result = (Boolean) expression.computeValue(context);
        assertFalse(result);
    }

    @Test
    public void testCoreOperationGreaterThanOrEqualWithNull() {
        InfoSet infoSet = new InfoSet();
        EvalContext context = new EvalContext(infoSet);

        Expression arg1 = new ConstantExpression(1.5);
        Expression arg2 = null;
        CoreOperationGreaterThanOrEqual expression = new CoreOperationGreaterThanOrEqual(arg1, arg2);

        assertNull(expression.computeValue(context));
    }
}