package org.apache.commons.jxpath.ri.compiler.test;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InfoSet;
import org.apache.commons.jxpath.ri.InfoSetUtil;
import org.apache.commons.jxpath.ri.compiler.CoreOperationLessThan;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Test class for CoreOperationLessThan.
 */
public class CoreOperationLessThanTest {

    @Test
    public void testCoreOperationLessThanWithValidDoubleValues() {
        CoreOperationLessThan coreOperationLessThan = new CoreOperationLessThan(
                new CoreNumberExpression(new ConstantExpression(1.5)),
                new CoreNumberExpression(new ConstantExpression(3.5))
        );

        EvalContext context = new EvalContext(new InfoSet());
        Object result = coreOperationLessThan.computeValue(context);

        assertTrue(result instanceof Boolean);
        assertTrue((boolean) result);

        // Test with reversed arguments
        CoreOperationLessThan coreOperationLessThanReverse = new CoreOperationLessThan(
                new CoreNumberExpression(new ConstantExpression(3.5)),
                new CoreNumberExpression(new ConstantExpression(1.5))
        );

        result = coreOperationLessThanReverse.computeValue(context);

        assertTrue(result instanceof Boolean);
        assertFalse((boolean) result);
    }

    @Test
    public void testCoreOperationLessThanWithInvalidDoubleValueOne() {
        CoreOperationLessThan coreOperationLessThan = new CoreOperationLessThan(
                new CoreNumberExpression(new ConstantExpression("invalidStringValue")),
                new CoreNumberExpression(new ConstantExpression(1.5))
        );

        EvalContext context = new EvalContext(new InfoSet());
        Object result = coreOperationLessThan.computeValue(context);

        assertFalse(result instanceof Boolean);
        assertNull(result);
    }

    @Test(expected = ClassCastException.class)
    public void testCoreOperationLessThanWithInvalidDoubleValueTwo() {
        CoreOperationLessThan coreOperationLessThan = new CoreOperationLessThan(
                new CoreNumberExpression(new ConstantExpression(1.5)),
                new CoreNumberExpression(new CoreListExpression(new Object[] {new ConstantExpression("invalidStringValue")}))
        );

        EvalContext context = new EvalContext(new InfoSet());
        coreOperationLessThan.computeValue(context);
    }

    @Test
    public void testCoreOperationLessThanWithNaN() {
        CoreOperationLessThan coreOperationLessThan = new CoreOperationLessThan(
                new CoreNumberExpression(new ConstantExpression(Double.NaN)),
                new CoreNumberExpression(new ConstantExpression(Double.NaN))
        );

        EvalContext context = new EvalContext(new InfoSet());
        Object result = coreOperationLessThan.computeValue(context);

        assertFalse(result instanceof Boolean);
        assertNull(result);
    }

    @Test
    public void testCoreOperationLessThanWithNegativeNumbers() {
        CoreOperationLessThan coreOperationLessThan = new CoreOperationLessThan(
                new CoreNumberExpression(new ConstantExpression(-1.5)),
                new CoreNumberExpression(new ConstantExpression(-2.5))
        );

        EvalContext context = new EvalContext(new InfoSet());
        Object result = coreOperationLessThan.computeValue(context);

        assertTrue(result instanceof Boolean);
        assertTrue((boolean) result);

        // Test with reversed arguments
        CoreOperationLessThan coreOperationLessThanReverse = new CoreOperationLessThan(
                new CoreNumberExpression(new ConstantExpression(-2.5)),
                new CoreNumberExpression(new ConstantExpression(-1.5))
        );

        result = coreOperationLessThanReverse.computeValue(context);

        assertTrue(result instanceof Boolean);
        assertFalse((boolean) result);
    }

    @Test
    public void testCoreOperationLessThanWithZeroAndPositiveNumber() {
        CoreOperationLessThan coreOperationLessThan = new CoreOperationLessThan(
                new CoreNumberExpression(new ConstantExpression(0.0)),
                new CoreNumberExpression(new ConstantExpression(1.5))
        );

        EvalContext context = new EvalContext(new InfoSet());
        Object result = coreOperationLessThan.computeValue(context);

        assertTrue(result instanceof Boolean);
        assertFalse((boolean) result);
    }

    @Test
    public void testCoreOperationLessThanWithZeroAndNegativeNumber() {
        CoreOperationLessThan coreOperationLessThan = new CoreOperationLessThan(
                new CoreNumberExpression(new ConstantExpression(0.0)),
                new CoreNumberExpression(new ConstantExpression(-1.5))
        );

        EvalContext context = new EvalContext(new InfoSet());
        Object result = coreOperationLessThan.computeValue(context);

        assertTrue(result instanceof Boolean);
        assertTrue((boolean) result);
    }

    @Test
    public void testCoreOperationLessThanWithEqualNumbers() {
        CoreOperationLessThan coreOperationLessThan = new CoreOperationLessThan(
                new CoreNumberExpression(new ConstantExpression(1.5)),
                new CoreNumberExpression(new ConstantExpression(1.5))
        );

        EvalContext context = new EvalContext(new InfoSet());
        Object result = coreOperationLessThan.computeValue(context);

        assertFalse(result instanceof Boolean);
        assertNull(result);
    }
}