package org.apache.commons.jxpath.ri.compiler.test;

import static org.junit.Assert.*;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InfoSetUtil;
import org.apache.commons.jxpath.ri.compiler.CoreOperationLessThan;
import org.apache.commons.jxpath.ri.compiler.Expression;

public class CoreOperationLessThanTest {

    @org.junit.Test
    public void testCoreOperationLessThan() {
        // Test with double values
        Expression arg1 = new ConstantExpression(1.5);
        Expression arg2 = new ConstantExpression(2.5);
        CoreOperationLessThan lessThan = new CoreOperationLessThan(arg1, arg2);
        EvalContext context = new EvalContext(InfoSetUtil.createInfoSet());
        Boolean result = (Boolean) lessThan.computeValue(context);
        assertTrue(result.booleanValue());

        arg1 = new ConstantExpression(2.5);
        arg2 = new ConstantExpression(1.5);
        lessThan = new CoreOperationLessThan(arg1, arg2);
        context = new EvalContext(InfoSetUtil.createInfoSet());
        result = (Boolean) lessThan.computeValue(context);
        assertFalse(result.booleanValue());

        arg1 = new ConstantExpression(1.5);
        arg2 = new ConstantExpression(1.5);
        lessThan = new CoreOperationLessThan(arg1, arg2);
        context = new EvalContext(InfoSetUtil.createInfoSet());
        result = (Boolean) lessThan.computeValue(context);
        assertFalse(result.booleanValue());

        // Test with different types
        arg1 = new ConstantExpression(1);
        arg2 = new ConstantExpression(1.5);
        lessThan = new CoreOperationLessThan(arg1, arg2);
        context = new EvalContext(InfoSetUtil.createInfoSet());
        try {
            lessThan.computeValue(context);
            fail("Expected TypeMismatchException");
        } catch (Exception e) {
            assertTrue(e instanceof org.apache.commons.jxpath.ri.compiler.TypeMismatchException);
        }

        arg1 = new ConstantExpression("1.5");
        arg2 = new ConstantExpression(1);
        lessThan = new CoreOperationLessThan(arg1, arg2);
        context = new EvalContext(InfoSetUtil.createInfoSet());
        try {
            lessThan.computeValue(context);
            fail("Expected TypeMismatchException");
        } catch (Exception e) {
            assertTrue(e instanceof org.apache.commons.jxpath.ri.compiler.TypeMismatchException);
        }

        arg1 = new ConstantExpression("1.5");
        arg2 = new ConstantExpression("2.5");
        lessThan = new CoreOperationLessThan(arg1, arg2);
        context = new EvalContext(InfoSetUtil.createInfoSet());
        try {
            lessThan.computeValue(context);
            fail("Expected TypeMismatchException");
        } catch (Exception e) {
            assertTrue(e instanceof org.apache.commons.jxpath.ri.compiler.TypeMismatchException);
        }
    }

}