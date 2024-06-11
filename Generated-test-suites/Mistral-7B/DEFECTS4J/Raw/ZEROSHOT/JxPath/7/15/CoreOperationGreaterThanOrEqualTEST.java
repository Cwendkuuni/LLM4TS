package org.apache.commons.jxpath.ri.compiler.test;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InfoSet;
import org.apache.commons.jxpath.ri.compiler.CoreOperationGreaterThanOrEqual;
import org.apache.commons.jxpath.ri.compiler.CoreOperationRelationalExpression;
import org.apache.commons.jxpath.ri.compiler.Expression;
import org.apache.commons.jxpath.ri.compiler.ExpressionFactory;
import org.junit.Test;
import static org.junit.Assert.*;

public class CoreOperationGreaterThanOrEqualTest {

    @Test
    public void testCoreOperationGreaterThanOrEqual() {
        Expression arg1 = ExpressionFactory.newDoubleLiteralExpression(1.5);
        Expression arg2 = ExpressionFactory.newDoubleLiteralExpression(1.0);
        CoreOperationGreaterThanOrEqual coreOperationGreaterThanOrEqual = new CoreOperationGreaterThanOrEqual(arg1, arg2);
        EvalContext context = new EvalContext(new InfoSet());
        Object result = coreOperationGreaterThanOrEqual.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result);

        arg1 = ExpressionFactory.newDoubleLiteralExpression(1.0);
        arg2 = ExpressionFactory.newDoubleLiteralExpression(1.5);
        coreOperationGreaterThanOrEqual = new CoreOperationGreaterThanOrEqual(arg1, arg2);
        context = new EvalContext(new InfoSet());
        result = coreOperationGreaterThanOrEqual.computeValue(context);
        assertFalse(result instanceof Boolean);
        assertNull(result);

        arg1 = ExpressionFactory.newDoubleLiteralExpression(1.5);
        arg2 = ExpressionFactory.newDoubleLiteralExpression(1.5);
        coreOperationGreaterThanOrEqual = new CoreOperationGreaterThanOrEqual(arg1, arg2);
        context = new EvalContext(new InfoSet());
        result = coreOperationGreaterThanOrEqual.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result);

        arg1 = ExpressionFactory.newDoubleLiteralExpression(Double.MAX_VALUE);
        arg2 = ExpressionFactory.newDoubleLiteralExpression(1.0);
        coreOperationGreaterThanOrEqual = new CoreOperationGreaterThanOrEqual(arg1, arg2);
        context = new EvalContext(new InfoSet());
        result = coreOperationGreaterThanOrEqual.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result);

        arg1 = ExpressionFactory.newDoubleLiteralExpression(1.0);
        arg2 = ExpressionFactory.newDoubleLiteralExpression(Double.MIN_VALUE);
        coreOperationGreaterThanOrEqual = new CoreOperationGreaterThanOrEqual(arg1, arg2);
        context = new EvalContext(new InfoSet());
        result = coreOperationGreaterThanOrEqual.computeValue(context);
        assertFalse(result instanceof Boolean);
        assertNull(result);
    }

    @Test
    public void testGetSymbol() {
        Expression arg1 = ExpressionFactory.newDoubleLiteralExpression(1.5);
        Expression arg2 = ExpressionFactory.newDoubleLiteralExpression(1.0);
        CoreOperationGreaterThanOrEqual coreOperationGreaterThanOrEqual = new CoreOperationGreaterThanOrEqual(arg1, arg2);
        String symbol = coreOperationGreaterThanOrEqual.getSymbol();
        assertEquals(">=", symbol);
    }
}