package org.apache.commons.jxpath.ri.compiler.test;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InfoSet;
import org.apache.commons.jxpath.ri.info.InfoSetUtil;
import org.apache.commons.jxpath.ri.compiler.CoreOperationLessThan;
import org.junit.Test;
import static org.junit.Assert.*;

public class CoreOperationLessThanTest {

    @Test
    public void testCoreOperationLessThanWithNumbers() {
        double num1 = 5.0;
        double num2 = 10.0;
        CoreOperationLessThan coreOperationLessThan = new CoreOperationLessThan(
                new CoreConstantExpression(num1), new CoreConstantExpression(num2));
        InfoSet infoSet = new InfoSet();
        EvalContext context = new EvalContext(infoSet);
        Boolean result = (Boolean) coreOperationLessThan.computeValue(context);
        assertTrue(result);
    }

    @Test
    public void testCoreOperationLessThanWithSameNumbers() {
        double num1 = 5.0;
        double num2 = 5.0;
        CoreOperationLessThan coreOperationLessThan = new CoreOperationLessThan(
                new CoreConstantExpression(num1), new CoreConstantExpression(num2));
        InfoSet infoSet = new InfoSet();
        EvalContext context = new EvalContext(infoSet);
        Boolean result = (Boolean) coreOperationLessThan.computeValue(context);
        assertFalse(result);
    }

    @Test(expected = ClassCastException.class)
    public void testCoreOperationLessThanWithDifferentTypes() {
        double num1 = 5.0;
        String str2 = "5";
        CoreOperationLessThan coreOperationLessThan = new CoreOperationLessThan(
                new CoreConstantExpression(num1), new CoreConstantExpression(str2));
        InfoSet infoSet = new InfoSet();
        EvalContext context = new EvalContext(infoSet);
        coreOperationLessThan.computeValue(context);
    }

    @Test
    public void testCoreOperationLessThanWithNegativeNumbers() {
        double num1 = -5.0;
        double num2 = -10.0;
        CoreOperationLessThan coreOperationLessThan = new CoreOperationLessThan(
                new CoreConstantExpression(num1), new CoreConstantExpression(num2));
        InfoSet infoSet = new InfoSet();
        EvalContext context = new EvalContext(infoSet);
        Boolean result = (Boolean) coreOperationLessThan.computeValue(context);
        assertTrue(result);
    }

    @Test
    public void testCoreOperationLessThanWithZeroAndPositiveNumber() {
        double num1 = 0.0;
        double num2 = 5.0;
        CoreOperationLessThan coreOperationLessThan = new CoreOperationLessThan(
                new CoreConstantExpression(num1), new CoreConstantExpression(num2));
        InfoSet infoSet = new InfoSet();
        EvalContext context = new EvalContext(infoSet);
        Boolean result = (Boolean) coreOperationLessThan.computeValue(context);
        assertFalse(result);
    }

    @Test
    public void testCoreOperationLessThanWithZeroAndZero() {
        double num1 = 0.0;
        double num2 = 0.0;
        CoreOperationLessThan coreOperationLessThan = new CoreOperationLessThan(
                new CoreConstantExpression(num1), new CoreConstantExpression(num2));
        InfoSet infoSet = new InfoSet();
        EvalContext context = new EvalContext(infoSet);
        Boolean result = (Boolean) coreOperationLessThan.computeValue(context);
        assertFalse(result);
    }
}