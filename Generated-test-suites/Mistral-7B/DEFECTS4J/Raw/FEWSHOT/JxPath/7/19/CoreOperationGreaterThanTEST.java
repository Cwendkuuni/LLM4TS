package org.apache.commons.jxpath.ri.compiler;

import static org.junit.Assert.*;
import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InfoSet;
import org.apache.commons.jxpath.ri.InfoSetUtil;
import org.junit.Test;

public class CoreOperationGreaterThanTest {

    @Test
    public void testCoreOperationGreaterThan() {
        CoreOperationGreaterThan coreOperationGreaterThan = new CoreOperationGreaterThan(new ConstantExpression(new Double(1.5)), new ConstantExpression(new Double(1.0)));
        InfoSet infoSet = InfoSetUtil.createTestInfoSet();
        EvalContext context = new EvalContext();
        context.setInfoSet(infoSet);

        Boolean result = (Boolean) coreOperationGreaterThan.computeValue(context);

        assertTrue(result);
    }

    @Test
    public void testCoreOperationGreaterThanFalse() {
        CoreOperationGreaterThan coreOperationGreaterThan = new CoreOperationGreaterThan(new ConstantExpression(new Double(1.0)), new ConstantExpression(new Double(1.5)));
        InfoSet infoSet = InfoSetUtil.createTestInfoSet();
        EvalContext context = new EvalContext();
        context.setInfoSet(infoSet);

        Boolean result = (Boolean) coreOperationGreaterThan.computeValue(context);

        assertFalse(result);
    }

    @Test
    public void testCoreOperationGreaterThanNullArg1() {
        CoreOperationGreaterThan coreOperationGreaterThan = new CoreOperationGreaterThan(new ConstantExpression(null), new ConstantExpression(new Double(1.0)));

        assertNull(coreOperationGreaterThan.computeValue(new EvalContext()));
    }

    @Test
    public void testCoreOperationGreaterThanNullArg2() {
        CoreOperationGreaterThan coreOperationGreaterThan = new CoreOperationGreaterThan(new ConstantExpression(new Double(1.0)), new ConstantExpression(null));

        assertNull(coreOperationGreaterThan.computeValue(new EvalContext()));
    }

    @Test
    public void testCoreOperationGreaterThanBothNull() {
        CoreOperationGreaterThan coreOperationGreaterThan = new CoreOperationGreaterThan(new ConstantExpression(null), new ConstantExpression(null));

        assertNull(coreOperationGreaterThan.computeValue(new EvalContext()));
    }

    @Test
    public void testCoreOperationGreaterThanWithStrings() {
        CoreOperationGreaterThan coreOperationGreaterThan = new CoreOperationGreaterThan(new ConstantExpression("5"), new ConstantExpression("3"));
        InfoSet infoSet = InfoSetUtil.createTestInfoSet();
        EvalContext context = new EvalContext();
        context.setInfoSet(infoSet);

        Boolean result = (Boolean) coreOperationGreaterThan.computeValue(context);

        assertTrue(result);
    }

    @Test
    public void testCoreOperationGreaterThanWithStringsFalse() {
        CoreOperationGreaterThan coreOperationGreaterThan = new CoreOperationGreaterThan(new ConstantExpression("3"), new ConstantExpression("5"));
        InfoSet infoSet = InfoSetUtil.createTestInfoSet();
        EvalContext context = new EvalContext();
        context.setInfoSet(infoSet);

        Boolean result = (Boolean) coreOperationGreaterThan.computeValue(context);

        assertFalse(result);
    }
}