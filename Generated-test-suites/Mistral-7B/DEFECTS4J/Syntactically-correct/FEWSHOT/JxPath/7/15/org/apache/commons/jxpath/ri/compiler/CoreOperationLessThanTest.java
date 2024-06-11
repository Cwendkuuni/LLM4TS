package org.apache.commons.jxpath.ri.compiler;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InfoSetUtil;
import org.apache.commons.jxpath.ri.TestUtils;

import java.util.ArrayList;
import java.util.List;

public class CoreOperationLessThanTest {

    @Test
    public void testComputeValue() {
        EvalContext context = new EvalContext();

        // Test with primitive types
        double l = 2.5;
        double r = 3.5;
        assertEquals(Boolean.FALSE, CoreOperationLessThan.computeValue(context, new CoreOperationLessThan(new ConstantExpression(l), new ConstantExpression(r))));

        l = 2.5;
        r = 2.5;
        assertTrue(CoreOperationLessThan.computeValue(context, new CoreOperationLessThan(new ConstantExpression(l), new ConstantExpression(r))));

        l = 2.5;
        r = 2.4;
        assertTrue(CoreOperationLessThan.computeValue(context, new CoreOperationLessThan(new ConstantExpression(l), new ConstantExpression(r))));

        // Test with complex types
        Expression arg1 = new ConstantExpression(TestUtils.createNumberInfoSet(1.5));
        Expression arg2 = new ConstantExpression(TestUtils.createNumberInfoSet(2.5));
        assertEquals(Boolean.FALSE, CoreOperationLessThan.computeValue(context, new CoreOperationLessThan(arg1, arg2)));

        arg1 = new ConstantExpression(TestUtils.createNumberInfoSet(1.5));
        arg2 = new ConstantExpression(TestUtils.createNumberInfoSet(1.5));
        assertTrue(CoreOperationLessThan.computeValue(context, new CoreOperationLessThan(arg1, arg2)));

        arg1 = new ConstantExpression(TestUtils.createNumberInfoSet(1.5));
        arg2 = new ConstantExpression(TestUtils.createNumberInfoSet(1.5, 1.1));
        assertTrue(CoreOperationLessThan.computeValue(context, new CoreOperationLessThan(arg1, arg2)));

        // Test with invalid inputs
        assertNull(CoreOperationLessThan.computeValue(context, new CoreOperationLessThan(null, new ConstantExpression(1.5))));
        assertNull(CoreOperationLessThan.computeValue(context, new CoreOperationLessThan(new ConstantExpression(1.5), null)));

        Expression arg3 = new CoreOperationAdd(new ConstantExpression(1), new ConstantExpression(1));
        assertNull(CoreOperationLessThan.computeValue(context, new CoreOperationLessThan(arg1, arg3)));
    }

    @Test
    public void testGetSymbol() {
        assertEquals("<", CoreOperationLessThan.getSymbol());
    }
}