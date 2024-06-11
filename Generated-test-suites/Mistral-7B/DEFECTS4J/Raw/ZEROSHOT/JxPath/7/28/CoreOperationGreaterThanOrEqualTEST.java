package org.apache.commons.jxpath.ri.compiler.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertNull;

import org.apache.commons.jxpath.ri.CompilerTest;
import org.apache.commons.jxpath.ri.InfoSet;
import org.apache.commons.jxpath.ri.eval.EvalContext;
import org.apache.commons.jxpath.ri.compiler.CoreOperationGreaterThanOrEqual;
import org.apache.commons.jxpath.ri.expression.Expression;

public class CoreOperationGreaterThanOrEqualTest extends ComplierTest {

    @Override
    public void setUp() throws Exception {
        super.setUp();
    }

    @Override
    public void tearDown() throws Exception {
        super.tearDown();
    }

    public void testCoreOperationGreaterThanOrEqual() {
        Expression arg1 = new LiteralExpression(new Double(5.0));
        Expression arg2 = new LiteralExpression(new Double(4.0));
        CoreOperationGreaterThanOrEqual coreOperationGreaterThanOrEqual = new CoreOperationGreaterThanOrEqual(arg1, arg2);

        InfoSet infoSet = createInfoSet();
        EvalContext context = new EvalContext(infoSet);

        Object result = coreOperationGreaterThanOrEqual.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result);
    }

    public void testCoreOperationGreaterThanOrEqualFalse() {
        Expression arg1 = new LiteralExpression(new Double(4.0));
        Expression arg2 = new LiteralExpression(new Double(5.0));
        CoreOperationGreaterThanOrEqual coreOperationGreaterThanOrEqual = new CoreOperationGreaterThanOrEqual(arg1, arg2);

        InfoSet infoSet = createInfoSet();
        EvalContext context = new EvalContext(infoSet);

        Object result = coreOperationGreaterThanOrEqual.computeValue(context);
        assertFalse(result instanceof Boolean);
        assertNull(result);
    }

    public void testCoreOperationGreaterThanOrEqualEqual() {
        Expression arg1 = new LiteralExpression(new Double(5.0));
        Expression arg2 = new LiteralExpression(new Double(5.0));
        CoreOperationGreaterThanOrEqual coreOperationGreaterThanOrEqual = new CoreOperationGreaterThanOrEqual(arg1, arg2);

        InfoSet infoSet = createInfoSet();
        EvalContext context = new EvalContext(infoSet);

        Object result = coreOperationGreaterThanOrEqual.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result);
    }

    public void testCoreOperationGreaterThanOrEqualNull() {
        Expression arg1 = new LiteralExpression(new Double(5.0));
        Expression arg2 = new LiteralExpression(null);
        CoreOperationGreaterThanOrEqual coreOperationGreaterThanOrEqual = new CoreOperationGreaterThanOrEqual(arg1, arg2);

        InfoSet infoSet = createInfoSet();
        EvalContext context = new EvalContext(infoSet);

        Object result = coreOperationGreaterThanOrEqual.computeValue(context);
        assertFalse(result instanceof Boolean);
        assertNull(result);
    }

    public void testCoreOperationGreaterThanOrEqualNaN() {
        Expression arg1 = new LiteralExpression(new Double(Double.NaN));
        Expression arg2 = new LiteralExpression(new Double(Double.NaN));
        CoreOperationGreaterThanOrEqual coreOperationGreaterThanOrEqual = new CoreOperationGreaterThanOrEqual(arg1, arg2);

        InfoSet infoSet = createInfoSet();
        EvalContext context = new EvalContext(infoSet);

        Object result = coreOperationGreaterThanOrEqual.computeValue(context);
        assertFalse(result instanceof Boolean);
        assertNull(result);
    }

}