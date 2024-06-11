package org.apache.commons.jxpath.ri.compiler.test;

import org.apache.commons.jxpath.EvalContext;
import org.apache.commons.jxpath.ri.compiler.CoreOperationEqual;
import org.apache.commons.jxpath.ri.compiler.CoreOperationEqualarg1TypeMismatchException;
import org.apache.commons.jxpath.ri.compiler.CoreOperationEqualNullException;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Test class for CoreOperationEqual.
 */
public class CoreOperationEqualTest {

    /**
     * Test method for {@link CoreOperationEqual#CoreOperationEqual(Expression, Expression)} .
     */
    @Test
    public void testCoreOperationEqualConstructor() {
        Expression arg1 = new ConstantExpression("1");
        Expression arg2 = new ConstantExpression("1");
        CoreOperationEqual coreOperationEqual = new CoreOperationEqual(arg1, arg2);
        assertNotNull(coreOperationEqual);
    }

    /**
     * Test method for {@link CoreOperationEqual#computeValue(EvalContext)} with equal values.
     */
    @Test
    public void testComputeValueEqual() {
        Expression arg1 = new ConstantExpression("1");
        Expression arg2 = new ConstantExpression("1");
        CoreOperationEqual coreOperationEqual = new CoreOperationEqual(arg1, arg2);
        Object result = coreOperationEqual.computeValue(new EvalContext());
        assertTrue(result instanceof Boolean);
        assertTrue((boolean) result);
    }

    /**
     * Test method for {@link CoreOperationEqual#computeValue(EvalContext)} with not equal values.
     */
    @Test
    public void testComputeValueNotEqual() {
        Expression arg1 = new ConstantExpression("1");
        Expression arg2 = new ConstantExpression("2");
        CoreOperationEqual coreOperationEqual = new CoreOperationEqual(arg1, arg2);
        Object result = coreOperationEqual.computeValue(new EvalContext());
        assertTrue(result instanceof Boolean);
        assertFalse((boolean) result);
    }

    /**
     * Test method for {@link CoreOperationEqual#computeValue(EvalContext)} with null argument.
     */
    @Test(expected = CoreOperationEqualNullException.class)
    public void testComputeValueNullArg1() {
        Expression arg1 = new ConstantExpression(null);
        Expression arg2 = new ConstantExpression("1");
        CoreOperationEqual coreOperationEqual = new CoreOperationEqual(arg1, arg2);
        coreOperationEqual.computeValue(new EvalContext());
    }

    /**
     * Test method for {@link CoreOperationEqual#computeValue(EvalContext)} with null argument 2.
     */
    @Test(expected = CoreOperationEqualNullException.class)
    public void testComputeValueNullArg2() {
        Expression arg1 = new ConstantExpression("1");
        Expression arg2 = new ConstantExpression(null);
        CoreOperationEqual coreOperationEqual = new CoreOperationEqual(arg1, arg2);
        coreOperationEqual.computeValue(new EvalContext());
    }

    /**
     * Test method for {@link CoreOperationEqual#computeValue(EvalContext)} with type mismatch.
     */
    @Test(expected = CoreOperationEqualarg1TypeMismatchException.class)
    public void testComputeValueTypeMismatch() {
        Expression arg1 = new ConstantExpression("1");
        Expression arg2 = new ConstantExpression(1);
        CoreOperationEqual coreOperationEqual = new CoreOperationEqual(arg1, arg2);
        coreOperationEqual.computeValue(new EvalContext());
    }
}