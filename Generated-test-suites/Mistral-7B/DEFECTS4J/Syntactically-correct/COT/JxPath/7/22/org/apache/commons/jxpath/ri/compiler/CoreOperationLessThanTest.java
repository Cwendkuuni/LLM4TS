package org.apache.commons.jxpath.ri.compiler.test;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InfoSet;
import org.apache.commons.jxpath.ri.mock.MockInfoSet;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import static org.junit.Assert.*;

@RunWith(JUnit4.class)
public class CoreOperationLessThanTest {

    private CoreOperationLessThan coreOperationLessThan;
    private Expression arg1;
    private Expression arg2;
    private EvalContext context;
    private InfoSet infoSet;

    @Before
    public void setup() {
        arg1 = new ConstantExpression(new Double(5.0));
        arg2 = new ConstantExpression(new Double(10.0));
        coreOperationLessThan = new CoreOperationLessThan(arg1, arg2);
        context = new EvalContext();
        infoSet = new MockInfoSet();
    }

    // Test basic functionality
    @Test
    public void testLessThan() {
        coreOperationLessThan.computeValue(context, infoSet);
        assertTrue(coreOperationLessThan.getValue() instanceof Boolean);
        assertTrue((Boolean) coreOperationLessThan.getValue());
    }

    // Test edge cases and exception handling

    @Test(expected = IllegalStateException.class)
    public void testLessThanWithNullArgs() {
        coreOperationLessThan = new CoreOperationLessThan(null, arg2);
        coreOperationLessThan.computeValue(context, infoSet);
    }

    @Test(expected = IllegalStateException.class)
    public void testLessThanWithNullArg1() {
        coreOperationLessThan = new CoreOperationLessThan(null, arg2);
        coreOperationLessThan.computeValue(context, infoSet);
    }

    @Test(expected = IllegalStateException.class)
    public void testLessThanWithNullArg2() {
        coreOperationLessThan = new CoreOperationLessThan(arg1, null);
        coreOperationLessThan.computeValue(context, infoSet);
    }

    @Test
    public void testLessThanWithSameValues() {
        arg1 = new ConstantExpression(new Double(5.0));
        arg2 = new ConstantExpression(new Double(5.0));
        coreOperationLessThan = new CoreOperationLessThan(arg1, arg2);
        coreOperationLessThan.computeValue(context, infoSet);
        assertFalse(coreOperationLessThan.getValue() instanceof Boolean);
        assertTrue(coreOperationLessThan.getValue() == null);
    }

    @Test
    public void testLessThanWithEqualValues() {
        arg1 = new ConstantExpression(new Double(5.0));
        arg2 = new ConstantExpression(new Double(5.0));
        coreOperationLessThan = new CoreOperationLessThan(arg1, arg2);
        coreOperationLessThan.computeValue(context, infoSet);
        assertFalse(coreOperationLessThan.getValue() instanceof Boolean);
        assertTrue(coreOperationLessThan.getValue() == null);
    }

    @Test
    public void testLessThanWithZeroAndNegativeNumber() {
        arg1 = new ConstantExpression(new Double(0.0));
        arg2 = new ConstantExpression(new Double(-1.0));
        coreOperationLessThan = new CoreOperationLessThan(arg1, arg2);
        coreOperationLessThan.computeValue(context, infoSet);
        assertTrue(coreOperationLessThan.getValue() instanceof Boolean);
        assertTrue((Boolean) coreOperationLessThan.getValue());
    }

    @Test
    public void testLessThanWithZeroAndPositiveNumber() {
        arg1 = new ConstantExpression(new Double(0.0));
        arg2 = new ConstantExpression(new Double(5.0));
        coreOperationLessThan = new CoreOperationLessThan(arg1, arg2);
        coreOperationLessThan.computeValue(context, infoSet);
        assertFalse(coreOperationLessThan.getValue() instanceof Boolean);
        assertTrue(coreOperationLessThan.getValue() == null);
    }

    @Test
    public void testLessThanWithNaNAndNumber() {
        arg1 = new ConstantExpression(Double.NaN);
        arg2 = new ConstantExpression(new Double(5.0));
        coreOperationLessThan = new CoreOperationLessThan(arg1, arg2);
        coreOperationLessThan.computeValue(context, infoSet);
        assertFalse(coreOperationLessThan.getValue() instanceof Boolean);
        assertTrue(coreOperationLessThan.getValue() == null);
    }

    @Test
    public void testLessThanWithNumberAndNaN() {
        arg1 = new ConstantExpression(new Double(5.0));
        arg2 = new ConstantExpression(Double.NaN);
        coreOperationLessThan = new CoreOperationLessThan(arg1, arg2);
        coreOperationLessThan.computeValue(context, infoSet);
        assertFalse(coreOperationLessThan.getValue() instanceof Boolean);
        assertTrue(coreOperationLessThan.getValue() == null);
    }
}