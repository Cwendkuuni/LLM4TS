
package org.apache.commons.jxpath.ri.compiler;

import org.apache.commons.jxpath.ri.EvalContext;
import org.junit.Test;

import static org.junit.Assert.*;

public class CoreOperationLessThanTest {

    @Test
    public void testComputeValueLeftLessThanRight() {
        CoreOperationLessThan expression = new CoreOperationLessThan(new ConstantExpression(5), new ConstantExpression(10));
        EvalContext context = new EvalContext(null);
        assertTrue((boolean) expression.computeValue(context));

        expression = new CoreOperationLessThan(new ConstantExpression(-5), new ConstantExpression(0));
        assertTrue((boolean) expression.computeValue(context));

        expression = new CoreOperationLessThan(new ConstantExpression(0), new ConstantExpression(0));
        assertFalse((boolean) expression.computeValue(context));
    }

    @Test
    public void testComputeValueLeftNotLessThanRight() {
        CoreOperationLessThan expression = new CoreOperationLessThan(new ConstantExpression(10), new ConstantExpression(5));
        EvalContext context = new EvalContext(null);
        assertFalse((boolean) expression.computeValue(context));

        expression = new CoreOperationLessThan(new ConstantExpression(0), new ConstantExpression(-5));
        assertFalse((boolean) expression.computeValue(context));
    }

    @Test
    public void testComputeValueWithVariables() {
        CoreOperationLessThan expression = new CoreOperationLessThan(new VariableExpression("a"), new VariableExpression("b"));
        EvalContext context = new EvalContext(null);
        context.setVariable("a", 5);
        context.setVariable("b", 10);
        assertTrue((boolean) expression.computeValue(context));

        context.setVariable("a", -5);
        context.setVariable("b", 0);
        assertTrue((boolean) expression.computeValue(context));

        context.setVariable("a", 0);
        context.setVariable("b", 0);
        assertFalse((boolean) expression.computeValue(context));

        context.setVariable("a", 10);
        context.setVariable("b", 5);
        assertFalse((boolean) expression.computeValue(context));

        context.setVariable("a", 0);
        context.setVariable("b", -5);
        assertFalse((boolean) expression.computeValue(context));
    }

    @Test
    public void testGetSymbol() {
        CoreOperationLessThan expression = new CoreOperationLessThan(new ConstantExpression(5), new ConstantExpression(10));
        assertEquals("<", expression.getSymbol());
    }
}
