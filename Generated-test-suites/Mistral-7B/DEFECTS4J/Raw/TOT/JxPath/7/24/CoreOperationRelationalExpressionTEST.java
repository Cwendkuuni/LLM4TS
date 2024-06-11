import org.apache.commons.jxpath.JXPathContext;
import org.apache.commons.jxpath.ri.compiler.CoreOperationRelationalExpression;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class CoreOperationRelationalExpressionTest {

    @Test
    public void testGreaterThan() {
        CoreOperationRelationalExpression expression = new CoreOperationGreaterThan(new ConstantExpression(5), new ConstantExpression(3));
        JXPathContext context = new JXPathContext();
        context.setValues(new Object[]{3, 4, 5, 6});
        assertTrue(expression.evaluate(context).booleanValue());
    }

    @Test
    public void testNotGreaterThan() {
        CoreOperationRelationalExpression expression = new CoreOperationNotGreaterThan(new ConstantExpression(5), new ConstantExpression(3));
        JXPathContext context = new JXPathContext();
        context.setValues(new Object[]{3, 4, 5, 6});
        assertFalse(expression.evaluate(context).booleanValue());
    }

    @Test
    public void testGreaterThanOrEqual() {
        CoreOperationRelationalExpression expression = new CoreOperationGreaterThanOrEqual(new ConstantExpression(5), new ConstantExpression(3));
        JXPathContext context = new JXPathContext();
        context.setValues(new Object[]{3, 4, 5, 6});
        assertTrue(expression.evaluate(context).booleanValue());
    }

    @Test
    public void testNotGreaterThanOrEqual() {
        CoreOperationRelationalExpression expression = new CoreOperationNotGreaterThanOrEqual(new ConstantExpression(5), new ConstantExpression(3));
        JXPathContext context = new JXPathContext();
        context.setValues(new Object[]{3, 4, 5, 6});
        assertFalse(expression.evaluate(context).booleanValue());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidArguments() {
        new CoreOperationRelationalExpression(new Expression[0]);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidArguments2() {
        new CoreOperationRelationalExpression(new Expression[]{new ConstantExpression(1), new ConstantExpression(1), new ConstantExpression(1)});
    }

    @Test(expected = ClassCastException.class)
    public void testInvalidArguments3() {
        new CoreOperationRelationalExpression(new Expression[]{new ConstantExpression("1"), new ConstantExpression(1)});
    }

}