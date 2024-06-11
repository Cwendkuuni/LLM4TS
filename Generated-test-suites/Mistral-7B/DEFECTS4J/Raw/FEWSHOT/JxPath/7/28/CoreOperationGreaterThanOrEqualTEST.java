package org.apache.commons.jxpath.ri.compiler;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.apache.commons.jxpath.ri.InfoSet;
import org.apache.commons.jxpath.ri.InfoSetUtil;
import org.apache.commons.jxpath.ri.EvalContext;

public class CoreOperationGreaterThanOrEqualTest {

    @Test
    public void testComputeValueTrue() {
        InfoSet is = new InfoSet();
        EvalContext context = new EvalContext();

        Expression arg1 = new ConstantExpression(new Double(5.0), new ObjectType(Double.TYPE));
        Expression arg2 = new ConstantExpression(new Double(4.0), new ObjectType(Double.TYPE));

        CoreOperationGreaterThanOrEqual expression = new CoreOperationGreaterThanOrEqual(arg1, arg2);

        assertTrue(expression.computeValue(context).equals(Boolean.TRUE));
    }

    @Test
    public void testComputeValueFalse() {
        InfoSet is = new InfoSet();
        EvalContext context = new EvalContext();

        Expression arg1 = new ConstantExpression(new Double(4.0), new ObjectType(Double.TYPE));
        Expression arg2 = new ConstantExpression(new Double(5.0), new ObjectType(Double.TYPE));

        CoreOperationGreaterThanOrEqual expression = new CoreOperationGreaterThanOrEqual(arg1, arg2);

        assertFalse(expression.computeValue(context).equals(Boolean.TRUE));
    }

    @Test
    public void testComputeValueNullArg1() {
        InfoSet is = new InfoSet();
        EvalContext context = new EvalContext();

        Expression arg1 = new ConstantExpression(null, new ObjectType(Double.TYPE));
        Expression arg2 = new ConstantExpression(new Double(5.0), new ObjectType(Double.TYPE));

        CoreOperationGreaterThanOrEqual expression = new CoreOperationGreaterThanOrEqual(arg1, arg2);

        assertNull(expression.computeValue(context));
    }

    @Test
    public void testComputeValueNullArg2() {
        InfoSet is = new InfoSet();
        EvalContext context = new EvalContext();

        Expression arg1 = new ConstantExpression(new Double(5.0), new ObjectType(Double.TYPE));
        Expression arg2 = new ConstantExpression(null, new ObjectType(Double.TYPE));

        CoreOperationGreaterThanOrEqual expression = new CoreOperationGreaterThanOrEqual(arg1, arg2);

        assertNull(expression.computeValue(context));
    }

    @Test
    public void testComputeValueMixedTypes() {
        InfoSet is = new InfoSet();
        EvalContext context = new EvalContext();

        Expression arg1 = new ConstantExpression(new Double(5.0), new ObjectType(Double.TYPE));
        Expression arg2 = new ConstantExpression(new Integer(4), new ObjectType(Integer.TYPE));

        CoreOperationGreaterThanOrEqual expression = new CoreOperationGreaterThanOrEqual(arg1, arg2);

        assertTrue(expression.computeValue(context).equals(Boolean.TRUE));
    }

    @Test
    public void testGetSymbol() {
        CoreOperationGreaterThanOrEqual expression = new CoreOperationGreaterThanOrEqual(new ConstantExpression(new Double(5.0), new ObjectType(Double.TYPE)), new ConstantExpression(new Double(4.0), new ObjectType(Double.TYPE)));

        assertEquals(">=", expression.getSymbol());
    }
}