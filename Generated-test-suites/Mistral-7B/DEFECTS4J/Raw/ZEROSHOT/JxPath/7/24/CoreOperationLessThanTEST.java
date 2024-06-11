package org.apache.commons.jxpath.ri.compiler;

import static org.junit.Assert.*;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InfoSetUtil;
import org.junit.Test;

public class CoreOperationLessThanTest {

    @Test
    public void testCoreOperationLessThanWithTwoNumbersLessThan() {
        CoreOperationLessThan coreOperationLessThan = new CoreOperationLessThan(
                new CoreConstantExpression(1.0), new CoreConstantExpression(2.0));
        assertTrue(coreOperationLessThan.computeValue(new EvalContext()).booleanValue());
    }

    @Test
    public void testCoreOperationLessThanWithTwoNumbersGreaterThanOrEqual() {
        CoreOperationLessThan coreOperationLessThan = new CoreOperationLessThan(
                new CoreConstantExpression(2.0), new CoreConstantExpression(1.0));
        assertFalse(coreOperationLessThan.computeValue(new EvalContext()).booleanValue());
    }

    @Test
    public void testCoreOperationLessThanWithTwoNumbersEqual() {
        CoreOperationLessThan coreOperationLessThan = new CoreOperationLessThan(
                new CoreConstantExpression(1.0), new CoreConstantExpression(1.0));
        assertFalse(coreOperationLessThan.computeValue(new EvalContext()).booleanValue());
    }

    @Test(expected = ClassCastException.class)
    public void testCoreOperationLessThanWithNonNumericArgs() {
        CoreOperationLessThan coreOperationLessThan = new CoreOperationLessThan(
                new CoreConstantExpression("string1"), new CoreConstantExpression("string2"));
    }

    @Test
    public void testCoreOperationLessThanWithThreeArgs() {
        CoreOperationLessThan coreOperationLessThan = new CoreOperationLessThan(
                new CoreConstantExpression(1.0), new CoreConstantExpression(2.0),
                new CoreConstantExpression(3.0));
        assertFalse(coreOperationLessThan.computeValue(new EvalContext()).booleanValue());
    }

    @Test
    public void testCoreOperationLessThanWithOneArg() {
        CoreOperationLessThan coreOperationLessThan = new CoreOperationLessThan(
                new CoreConstantExpression(1.0));
        assertFalse(coreOperationLessThan.computeValue(new EvalContext()).booleanValue());
    }
}