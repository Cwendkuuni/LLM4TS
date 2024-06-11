package org.apache.commons.jxpath.ri.compiler;

import static org.junit.Assert.*;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InfoSet;
import org.apache.commons.jxpath.ri.Value;

public class CoreOperationLessThanTest {

    @Test
    public void testCoreOperationLessThan() {
        CoreOperationLessThan operation = new CoreOperationLessThan(new ConstantExpression(new Value(1.0)), new ConstantExpression(new Value(2.0)));

        EvalContext context = new EvalContext(InfoSetUtil.createInfoSet(new Object[]{"1.0", "2.0"}));
        assertEquals(Boolean.FALSE, operation.computeValue(context));

        context = new EvalContext(InfoSetUtil.createInfoSet(new Object[]{"0.5", "1.5"}));
        assertEquals(Boolean.TRUE, operation.computeValue(context));

        context = new EvalContext(InfoSetUtil.createInfoSet(new Object[]{"3.0", "2.0"}));
        assertEquals(Boolean.TRUE, operation.computeValue(context));
    }

    @Test
    public void testCoreOperationLessThanWithNull() {
        CoreOperationLessThan operation = new CoreOperationLessThan(new ConstantExpression(null), new ConstantExpression(new Value(2.0)));
        assertNull(operation.computeValue(new EvalContext(InfoSetUtil.createInfoSet(new Object[]{null, "2.0"}))));
    }

    @Test
    public void testCoreOperationLessThanWithNonNumeric() {
        CoreOperationLessThan operation = new CoreOperationLessThan(new ConstantExpression(new Value("3")), new ConstantExpression(new Value("2.5")));
        assertFalse(operation.computeValue(new EvalContext(InfoSetUtil.createInfoSet(new Object[]{"3", "2.5"}))).booleanValue());
    }
}