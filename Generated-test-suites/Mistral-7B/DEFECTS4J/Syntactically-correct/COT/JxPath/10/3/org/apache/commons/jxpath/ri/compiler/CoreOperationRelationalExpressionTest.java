import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.Iterators;
import org.junit.Test;
import org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;

import static org.apache.commons.jxpath.ri.axes.AxisNames.*;

public class CoreOperationRelationalExpressionTest {

    @Test
    public void testComputeValue() {
        Expression leftExpr = new LiteralExpression(1);
        Expression rightExpr = new LiteralExpression(2);
        CoreOperationRelationalExpression expression = new GreaterExpression(new CoreOperation[] { leftExpr, rightExpr });
        EvalContext context = new EvalContext();
        Object result = expression.computeValue(context);
        Assert.assertFalse(result.equals(Boolean.TRUE));

        leftExpr = new LiteralExpression(2);
        rightExpr = new LiteralExpression(1);
        expression = new GreaterExpression(new CoreOperation[] { leftExpr, rightExpr });
        context = new EvalContext();
        result = expression.computeValue(context);
        Assert.assertFalse(result.equals(Boolean.TRUE));

        leftExpr = new LiteralExpression(2);
        rightExpr = new LiteralExpression(2);
        expression = new GreaterExpression(new CoreOperation[] { leftExpr, rightExpr });
        context = new EvalContext();
        result = expression.computeValue(context);
        Assert.assertFalse(result.equals(Boolean.TRUE));

        leftExpr = new LiteralExpression(3);
        rightExpr = new LiteralExpression(2);
        expression = new GreaterExpression(new CoreOperation[] { leftExpr, rightExpr });
        context = new EvalContext();
        result = expression.computeValue(context);
        Assert.assertTrue(result.equals(Boolean.TRUE));
    }

    @Test
    public void testGetPrecedence() {
        CoreOperationRelationalExpression expression = new GreaterExpression(new CoreOperation[0]);
        int precedence = expression.getPrecedence();
        Assert.assertEquals(3, precedence);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testIsSymmetric() {
        CoreOperationRelationalExpression expression = new GreaterExpression(new CoreOperation[0]);
        boolean symmetric = expression.isSymmetric();
        Assert.assertFalse(symmetric);
    }

    @Test
    public void testEvaluateCompare() {
        CoreOperationRelationalExpression expression = new GreaterExpression(new CoreOperation[0]);
        Assert.assertTrue(expression.evaluateCompare(1) == -1);
        Assert.assertTrue(expression.evaluateCompare(0) == -1);
        Assert.assertTrue(expression.evaluateCompare(-1) == 1);
        Assert.assertTrue(expression.evaluateCompare(0) == 0);
        Assert.assertTrue(expression.evaluateCompare(0) == 0);
    }

    @Test
    public void testCompute() {
        Expression leftExpr1 = new LiteralExpression(1);
        Expression rightExpr1 = new LiteralExpression(2);
        CoreOperationRelationalExpression expression1 = new GreaterExpression(new CoreOperation[] { leftExpr1, rightExpr1 });
        Object left1 = reduce(leftExpr1.computeValue(new EvalContext()));
        Object right1 = reduce(rightExpr1.computeValue(new EvalContext()));
        Assert.assertFalse(expression1.compute(left1, right1).equals(Boolean.TRUE));

        Expression leftExpr2 = new LiteralExpression(2);
        Expression rightExpr2 = new LiteralExpression(2);
        CoreOperationRelationalExpression expression2 = new GreaterExpression(new CoreOperation[] { leftExpr2, rightExpr2 });
        Object left2 = reduce(leftExpr2.computeValue(new EvalContext()));
        Object right2 = reduce(rightExpr2.computeValue(new EvalContext()));
        Assert.assertFalse(expression2.compute(left2, right2).equals(Boolean.TRUE));

        Expression leftExpr3 = new LiteralExpression(2);
        Expression rightExpr3 = new LiteralExpression(3);
        CoreOperationRelationalExpression expression3 = new GreaterExpression(new CoreOperation[] { leftExpr3, rightExpr3 });
        Object left3 = reduce(leftExpr3.computeValue(new EvalContext()));
        Object right3 = reduce(rightExpr3.computeValue(new EvalContext()));
        Assert.assertFalse(expression3.compute(left3, right3).equals(Boolean.TRUE));

        Expression leftExpr4 = new LiteralExpression(3);
        Expression rightExpr4 = new LiteralExpression(2);
        CoreOperationRelationalExpression expression4 = new GreaterExpression(new CoreOperation[] { leftExpr4, rightExpr4 });
        Object left4 = reduce(leftExpr4.computeValue(new EvalContext()));
        Object right4 = reduce(rightExpr4.computeValue(new EvalContext()));
        Assert.assertTrue(expression4.compute(left4, right4).equals(Boolean.TRUE));

        Expression leftExpr5 = new LiteralExpression(3);
        Expression rightExpr5 = new LiteralExpression(3);
        CoreOperationRelationalExpression expression5 = new GreaterExpression(new CoreOperation[] { leftExpr5, rightExpr5 });
        Object left5 = reduce(leftExpr5.computeValue(new EvalContext()));
        Object right5 = reduce(rightExpr5.computeValue(new EvalContext()));
        Assert.assertFalse(expression5.compute(left5, right5).equals(Boolean.TRUE));

        Expression leftExpr6 = new SelfContext();
        Expression rightExpr6 = new LiteralExpression(1);
        CoreOperationRelationalExpression expression6 = new GreaterExpression(new CoreOperation[] { leftExpr6, rightExpr6 });
        Object left6 = reduce(leftExpr6.computeValue(new EvalContext()));
        Object right6 = reduce(rightExpr6.computeValue(new EvalContext()));
        Assert.assertFalse(expression6.compute(left6, right6).equals(Boolean.TRUE));

        Expression leftExpr7 = new LiteralExpression(Iterators.EMPTY_ITERATOR);
        Expression rightExpr7 = new LiteralExpression(new ArrayList().iterator());
        CoreOperationRelationalExpression expression7 = new GreaterExpression(new CoreOperation[] { leftExpr7, rightExpr7 });
        Object left7 = reduce(leftExpr7.computeValue(new EvalContext()));
        Object right7 = reduce(rightExpr7.computeValue(new EvalContext()));
        Assert.assertFalse(expression7.compute(left7, right7).equals(Boolean.TRUE));

        Expression leftExpr8 = new LiteralExpression(new ArrayList().iterator());
        Expression rightExpr8 = new LiteralExpression(Iterators.EMPTY_ITERATOR);
        CoreOperationRelationalExpression expression8 = new GreaterExpression(new CoreOperator[] { leftExpr8, rightExpr8 });
        Object left8 = reduce(leftExpr8.computeValue(new EvalContext()));
        Object right8 = reduce(rightExpr8.computeValue(new EvalContext()));
        Assert.assertFalse(expression8.compute(left8, right8).equals(Boolean.TRUE));

        Expression leftExpr9 = new LiteralExpression(1.0);
        Expression rightExpr9 = new LiteralExpression(2.0);
        CoreOperationRelationalExpression expression9 = new GreaterExpression(new CoreOperation[] { leftExpr9, rightExpr9 });
        Object left9 = reduce(leftExpr9.computeValue(new EvalContext()));
        Object right9 = reduce(rightExpr9.computeValue(new EvalContext()));
        Assert.assertFalse(expression9.compute(left9, right9).equals(Boolean.TRUE));

        Expression leftExpr10 = new LiteralExpression(2.0);
        Expression rightExpr10 = new LiteralExpression(1.0);
        CoreOperationRelationalExpression expression10 = new GreaterExpression(new CoreOperation[] { leftExpr10, rightExpr10 });
        Object left10 = reduce(leftExpr10.computeValue(new EvalContext()));
        Object right10 = reduce(rightExpr10.computeValue(new EvalContext()));
        Assert.assertTrue(expression10.compute(left10, right10).equals(Boolean.TRUE));

        Expression leftExpr11 = new LiteralExpression(Double.NaN);
        Expression rightExpr11 = new LiteralExpression(Double.NaN);
        CoreOperationRelationalExpression expression11 = new GreaterExpression(new CoreOperation[] { leftExpr11, rightExpr11 });
        Object left11 = reduce(leftExpr11.computeValue(new EvalContext()));
        Object right11 = reduce(rightExpr11.computeValue(new EvalContext()));
        Assert.assertFalse(expression11.compute(left11, right11).equals(Boolean.TRUE));

        Expression leftExpr12 = new LiteralExpression(1);
        Expression rightExpr12 = new LiteralExpression(null);
        CoreOperationRelationalExpression expression12 = new GreaterExpression(new CoreOperation[] { leftExpr12, rightExpr12 });
        Object left12 = reduce(leftExpr12.computeValue(new EvalContext()));
        Object right12 = reduce(rightExpr12.computeValue(new EvalContext()));
        Assert.assertFalse(expression12.compute(left12, right12).equals(Boolean.TRUE));

        Expression leftExpr13 = new LiteralExpression(null);
        Expression rightExpr13 = new LiteralExpression(1);
        CoreOperationRelationalExpression expression13 = new GreaterExpression(new CoreOperation[] { leftExpr13, rightExpr13 });
        Object left13 = reduce(leftExpr13.computeValue(new EvalContext()));
        Object right13 = reduce(rightExpr13.computeValue(new EvalContext()));
        Assert.assertFalse(expression13.compute(left13, right13).equals(Boolean.TRUE));

        Expression leftExpr14 = new LiteralExpression(new ArrayList().iterator());
        Expression rightExpr14 = new LiteralExpression(new ArrayList().iterator());
        CoreOperationRelationalExpression expression14 = new GreaterExpression(new CoreOperation[] { leftExpr14, rightExpr14 });
        Object left14 = reduce(leftExpr14.computeValue(new EvalContext()));
        Object right14 = reduce(rightExpr14.computeValue(new EvalContext()));
        Assert.assertFalse(expression14.compute(left14, right14).equals(Boolean.TRUE));
    }

    private Object reduce(Object o) {
        if (o instanceof SelfContext) {
            o = ((EvalContext) o).getSingleNodePointer();
        }
        if (o instanceof Collection) {
            o = ((Collection) o).iterator();
        }
        return o;
    }

}