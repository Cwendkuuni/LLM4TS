import org.apache.commons.jxpath.ri.compiler.CoreOperationRelationalExpression;
import org.apache.commons.jxpath.ri.compiler.CoreOperation;
import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InfoSetUtil;
import org.apache.commons.jxpath.ri.axes.InitialContext;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class CoreOperationRelationalExpressionTest {

    @Test
    public void testGreaterThan() {
        CoreOperationRelationalExpression expr = new GreaterThanExpression(new ConstantExpression(5), new ConstantExpression(3));
        EvalContext context = new EvalContext(new InitialContext());
        Object result = expr.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result);
    }

    @Test
    public void testGreaterThanOrEqual() {
        CoreOperationRelationalExpression expr = new GreaterThanOrEqualExpression(new ConstantExpression(5), new ConstantExpression(3));
        EvalContext context = new EvalContext(new InitialContext());
        Object result = expr.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result);
    }

    @Test
    public void testLessThan() {
        CoreOperationRelationalExpression expr = new LessThanExpression(new ConstantExpression(3), new ConstantExpression(5));
        EvalContext context = new EvalContext(new InitialContext());
        Object result = expr.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertFalse((Boolean) result);
    }

    @Test
    public void testLessThanOrEqual() {
        CoreOperationRelationalExpression expr = new LessThanOrEqualExpression(new ConstantExpression(3), new ConstantExpression(5));
        EvalContext context = new EvalContext(new InitialContext());
        Object result = expr.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertFalse((Boolean) result);

        expr = new LessThanOrEqualExpression(new ConstantExpression(3), new ConstantExpression(3));
        result = expr.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidExpression() {
        CoreOperationRelationalExpression expr = new CoreOperationRelationalExpression(new Expression[0]);
        EvalContext context = new EvalContext(new InitialContext());
        expr.computeValue(context);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidComparison() {
        CoreOperationRelationalExpression expr = new CoreOperationRelationalExpression(new Expression[2]) {
            @Override
            protected boolean evaluateCompare(int compare) {
                throw new UnsupportedOperationException();
            }
        };
        EvalContext context = new EvalContext(new InitialContext());
        expr.computeValue(context);
    }

    @Test
    public void testDoubleCompare() {
        CoreOperationRelationalExpression expr = new GreaterThanExpression(new ConstantExpression(3.1d), new ConstantExpression(3d));
        EvalContext context = new EvalContext(new InitialContext());
        Object result = expr.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertFalse((Boolean) result);

        expr = new GreaterThanExpression(new ConstantExpression(3.1d), new ConstantExpression(3.1d));
        result = expr.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result);
    }

    @Test
    public void testIteratorCompare() {
        Integer[] array = {1, 2, 3};
        Iterator iterator = InfoSetUtil.iterator(array);

        CoreOperationRelationalExpression expr = new GreaterThanExpression(new IteratorExpression(iterator), new ConstantExpression(2));
        EvalContext context = new EvalContext(new InitialContext());
        Object result = expr.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result);

        expr = new GreaterThanOrEqualExpression(new IteratorExpression(iterator), new ConstantExpression(2));
        result = expr.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertFalse((Boolean) result);

        Expression left = new IteratorExpression(iterator);
        Expression right = new ConstantExpression(3);
        CoreOperationRelationalExpression expr2 = new GreaterThanExpression(left, right);
        result = expr2.computeValue(context);
        assertFalse(result instanceof Boolean);
    }
}