import org.apache.commons.jxpath.ri.compiler.CoreOperation;
import org.apache.commons.jxpath.ri.compiler.CoreOperationRelationalExpression;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class CoreOperationRelationalExpressionTest {

    @Test
    public void testCoreOperationRelationalExpressionConstructor() {
        Expression[] args = new Expression[2];
        CoreOperationRelationalExpression coreOperationRelationalExpression = new CoreOperationRelationalExpression(args);
        assertEquals(3, coreOperationRelationalExpression.getPrecedence());
        assertFalse(coreOperationRelationalExpression.isSymmetric());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCoreOperationRelationalExpressionInvalidConstructor() {
        new CoreOperationRelationalExpression(null);
    }

    @Test
    public void testGetPrecedence() {
        CoreOperationRelationalExpression coreOperationRelationalExpression = new CoreOperationRelationalExpression(new Expression[2]);
        assertEquals(3, coreOperationRelationalExpression.getPrecedence());
    }

    @Test
    public void testIsSymmetric() {
        CoreOperationRelationalExpression coreOperationRelationalExpression = new CoreOperationRelationalExpression(new Expression[2]);
        assertFalse(coreOperationRelationalExpression.isSymmetric());
    }

    @Test
    public void testCoreOperationRelationalExpressionEquals() {
        CoreOperationRelationalExpression coreOperationRelationalExpression1 = new CoreOperationRelationalExpression(new Expression[2]);
        CoreOperationRelationalExpression coreOperationRelationalExpression2 = new CoreOperationRelationalExpression(new Expression[2]);
        assertTrue(coreOperationRelationalExpression1.equals(coreOperationRelationalExpression2));
        assertNull(coreOperationRelationalExpression1.hashCode());
    }

    @Test
    public void testCoreOperationRelationalExpressionToString() {
        Expression left = new LiteralExpression("5");
        Expression right = new LiteralExpression("10");
        CoreOperationRelationalExpression coreOperationRelationalExpression = new CoreOperationRelationalExpression(new Expression[]{left, right});
        String expected = "(" + left + " > " + right + ")";
        String actual = coreOperationRelationalExpression.toString();
        assertEquals(expected, actual);
    }

    @Test
    public void testCoreOperationRelationalExpressionNegate() {
        Expression left = new LiteralExpression("5");
        Expression right = new LiteralExpression("10");
        CoreOperationRelationalExpression coreOperationRelationalExpression = new CoreOperationRelationalExpression(new Expression[]{left, right});
        CoreOperation coreOperation = coreOperationRelationalExpression.negate();
        String expected = "(" + left + " < " + right + ")";
        String actual = coreOperation.toString();
        assertEquals(expected, actual);
    }

    @Test
    public void testCoreOperationRelationalExpressionEvaluate() {
        Expression left = new LiteralExpression("5");
        Expression right = new LiteralExpression("10");
        CoreOperationRelationalExpression coreOperationRelationalExpression = new CoreOperationRelationalExpression(new Expression[]{left, right});
        Object leftValue = 5;
        Object rightValue = 10;
        boolean result = coreOperationRelationalExpression.eval(new Object[]{leftValue, rightValue});
        assertFalse(result);
    }

    @Test
    public void testCoreOperationRelationalExpressionEvaluateTrue() {
        Expression left = new LiteralExpression("10");
        Expression right = new LiteralExpression("5");
        CoreOperationRelationalExpression coreOperationRelationalExpression = new CoreOperationRelationalExpression(new Expression[]{left, right});
        Object leftValue = 10;
        Object rightValue = 5;
        boolean result = coreOperationRelationalExpression.eval(new Object[]{leftValue, rightValue});
        assertTrue(result);
    }

    @Test
    public void testCoreOperationRelationalExpressionEvaluateWithNull() {
        Expression left = new LiteralExpression("5");
        Expression right = null;
        CoreOperationRelationalExpression coreOperationRelationalExpression = new CoreOperationRelationalExpression(new Expression[]{left, right});
        Object leftValue = 5;
        Object rightValue = null;
        boolean result = coreOperationRelationalExpression.eval(new Object[]{leftValue, rightValue});
        assertFalse(result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCoreOperationRelationalExpressionEvaluateWithInvalidNumberOfArguments() {
        Expression left = new LiteralExpression("5");
        CoreOperationRelationalExpression coreOperationRelationalExpression = new CoreOperationRelationalExpression(new Expression[]{left});
        Object leftValue = 5;
        Object[] arguments = new Object[]{leftValue};
        coreOperationRelationalExpression.eval(arguments);
    }
}