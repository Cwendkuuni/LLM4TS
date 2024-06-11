package org.apache.commons.jxpath.ri.compiler.test;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InfoSet;
import org.apache.commons.jxpath.ri.TestUtils;
import org.apache.commons.jxpath.ri.compiler.CoreOperationLessThanOrEqual;
import org.apache.commons.jxpath.ri.compiler.CoreOperationRelationalExpression;
import org.apache.commons.jxpath.ri.compiler.Expression;
import org.apache.commons.jxpath.ri.compiler.ExpressionFactory;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Test class for CoreOperationLessThanOrEqual.
 */
public class CoreOperationLessThanOrEqualTest {

    @Test
    public void testCoreOperationLessThanOrEqual() {
        Expression arg1 = ExpressionFactory.createLiteralExpression(1.0);
        Expression arg2 = ExpressionFactory.createLiteralExpression(2.0);
        CoreOperationLessThanOrEqual coreOperationLessThanOrEqual = new CoreOperationLessThanOrEqual(arg1, arg2);
        assertNotNull(coreOperationLessThanOrEqual);
        EvalContext context = TestUtils.createContext();
        Object result = coreOperationLessThanOrEqual.computeValue(context);
        assertEquals(Boolean.FALSE, result);

        arg1 = ExpressionFactory.createLiteralExpression(1.0);
        arg2 = ExpressionFactory.createLiteralExpression(1.0);
        coreOperationLessThanOrEqual = new CoreOperationLessThanOrEqual(arg1, arg2);
        result = coreOperationLessThanOrEqual.computeValue(context);
        assertEquals(Boolean.TRUE, result);

        arg1 = ExpressionFactory.createLiteralExpression(1.0);
        arg2 = ExpressionFactory.createLiteralExpression(1.0);
        coreOperationLessThanOrEqual = new CoreOperationLessThanOrEqual(arg2, arg1);
        result = coreOperationLessThanOrEqual.computeValue(context);
        assertEquals(Boolean.FALSE, result);

        arg1 = ExpressionFactory.createLiteralExpression(-1.0);
        arg2 = ExpressionFactory.createLiteralExpression(-2.0);
        coreOperationLessThanOrEqual = new CoreOperationLessThanOrEqual(arg1, arg2);
        result = coreOperationLessThanOrEqual.computeValue(context);
        assertEquals(Boolean.TRUE, result);

        arg1 = ExpressionFactory.createLiteralExpression(-1.0);
        arg2 = ExpressionFactory.createLiteralExpression(-1.0);
        coreOperationLessThanOrEqual = new CoreOperationLessThanOrEqual(arg1, arg2);
        result = coreOperationLessThanOrEqual.computeValue(context);
        assertEquals(Boolean.TRUE, result);

        arg1 = ExpressionFactory.createLiteralExpression(Double.POSITIVE_INFINITY);
        arg2 = ExpressionFactory.createLiteralExpression(Double.NEGATIVE_INFINITY);
        coreOperationLessThanOrEqual = new CoreOperationLessThanOrEqual(arg1, arg2);
        result = coreOperationLessThanOrEqual.computeValue(context);
        assertEquals(Boolean.TRUE, result);

        arg1 = ExpressionFactory.createLiteralExpression(Double.POSITIVE_INFINITY);
        arg2 = ExpressionFactory.createLiteralExpression(Double.POSITIVE_INFINITY);
        coreOperationLessThanOrEqual = new CoreOperationLessThanOrEqual(arg1, arg2);
        result = coreOperationLessThanOrEqual.computeValue(context);
        assertEquals(Boolean.FALSE, result);

        arg1 = ExpressionFactory.createLiteralExpression(Double.NaN);
        arg2 = ExpressionFactory.createLiteralExpression(Double.NaN);
        coreOperationLessThanOrEqual = new CoreOperationLessThanOrEqual(arg1, arg2);
        result = coreOperationLessThanOrEqual.computeValue(context);
        // NaN is not less than or equal to NaN
        assertEquals(Boolean.FALSE, result);

        arg1 = new CoreOperationAdd(ExpressionFactory.createLiteralExpression(1.0),
                ExpressionFactory.createLiteralExpression(1.0));
        arg2 = ExpressionFactory.createLiteralExpression(2.0);
        coreOperationLessThanOrEqual = new CoreOperationLessThanOrEqual(arg1, arg2);
        result = coreOperationLessThanOrEqual.computeValue(context);
        assertEquals(Boolean.FALSE, result);

        arg1 = ExpressionFactory.createLiteralExpression(1.0);
        arg2 = new CoreOperationAdd(ExpressionFactory.createLiteralExpression(1.0),
                ExpressionFactory.createLiteralExpression(1.0));
        coreOperationLessThanOrEqual = new CoreOperationLessThanOrEqual(arg1, arg2);
        result = coreOperationLessThanOrEqual.computeValue(context);
        assertEquals(Boolean.FALSE, result);
    }
}