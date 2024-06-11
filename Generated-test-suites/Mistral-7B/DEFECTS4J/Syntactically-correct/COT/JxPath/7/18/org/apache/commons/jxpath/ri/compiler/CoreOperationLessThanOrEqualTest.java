package org.apache.commons.jxpath.ri.compiler.test;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InfoSet;
import org.apache.commons.jxpath.ri.expression.Expression;
import org.apache.commons.jxpath.ri.expression.Variable;
import org.apache.commons.jxpath.ri.compiler.CoreOperationLessThanOrEqual;
import org.junit.Assert;
import org.junit.Test;

import java.math.BigDecimal;

/**
 * Test class for CoreOperationLessThanOrEqual.
 */
public class CoreOperationLessThanOrEqualTest {

    @Test
    public void testCoreOperationLessThanOrEqual() {
        // Test with double values
        Expression arg1 = new Variable("arg1");
        Expression arg2 = new Variable("arg2");
        CoreOperationLessThanOrEqual operation = new CoreOperationLessThanOrEqual(arg1, arg2);
        EvalContext context = new EvalContext();
        InfoSet infoSet = new InfoSet();

        context.setCurrentInfoSet(infoSet);
        double value1 = 5.0;
        double value2 = 7.0;
        infoSet.setDoubleValue(arg1.getTextValue(), value1);
        infoSet.setDoubleValue(arg2.getTextValue(), value2);

        Boolean result = (Boolean) operation.computeValue(context);
        Assert.assertFalse("Expected false, but got " + result, result);

        value1 = 5.0;
        value2 = 5.0;
        infoSet.setDoubleValue(arg1.getTextValue(), value1);
        infoSet.setDoubleValue(arg2.getTextValue(), value2);

        result = (Boolean) operation.computeValue(context);
        Assert.assertTrue("Expected true, but got " + result, result);

        // Test with BigDecimal values
        arg1 = new Variable("arg1");
        arg2 = new Variable("arg2");
        operation = new CoreOperationLessThanOrEqual(arg1, arg2);

        context = new EvalContext();
        infoSet = new InfoSet();
        context.setCurrentInfoSet(infoSet);

        BigDecimal value1BD = new BigDecimal("5.5");
        BigDecimal value2BD = new BigDecimal("7.5");
        infoSet.setItem(arg1.getTextValue(), value1BD.doubleValue());
        infoSet.setItem(arg2.getTextValue(), value2BD.doubleValue());

        result = (Boolean) operation.computeValue(context);
        Assert.assertFalse("Expected false, but got " + result, result);

        value1BD = new BigDecimal("5.5");
        value2BD = new BigDecimal("5.5");
        infoSet.setItem(arg1.getTextValue(), value1BD);
        infoSet.setItem(arg2.getTextValue(), value2BD);

        result = (Boolean) operation.computeValue(context);
        Assert.assertTrue("Expected true, but got " + result, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCoreOperationLessThanOrEqualWithWrongNumberOfArguments() {
        Expression arg = new Variable("arg");
        CoreOperationLessThanOrEqual operation = new CoreOperationLessThanOrEqual(arg);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCoreOperationLessThanOrEqualWithNullArgument() {
        Expression arg1 = new Variable("arg1");
        Expression arg2 = null;
        CoreOperationLessThanOrEqual operation = new CoreOperationLessThanOrEqual(arg1, arg2);
    }
}