import org.apache.commons.jxpath.JXPathContext;
import org.apache.commons.jxpath.ri.compiler.CoreOperationRelationalExpression;
import org.apache.commons.jxpath.ri.compiler.CoreOperation;
import org.apache.commons.jxpath.ri.compiler.Expression;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

/**
 * Test class for CoreOperationRelationalExpression.
 */
public class CoreOperationRelationalExpressionTest {

    /**
     * Test the &gt; (greater than) operation.
     */
    @Test
    public void testGreaterThan() {
        Expression left = new CoreNumberExpression(5);
        Expression right = new CoreNumberExpression(10);

        CoreOperationRelationalExpression op = new CoreOperationGreaterThan(left, right);
        JXPathContext ctx = new JXPathContext();
        ctx.setValues("numbers", new Object[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10});

        Object result = op.evaluate(ctx);
        assertTrue("Result should be true", result instanceof Boolean);
        assertTrue("Result should be true", (boolean) result);

        left = new CoreNumberExpression(10);
        right = new CoreNumberExpression(5);

        op = new CoreOperationGreaterThan(left, right);
        result = op.evaluate(ctx);
        assertFalse("Result should be false", result instanceof Boolean);
        assertNull("Result should be null", result);
    }

    /**
     * Test the &gt;= (greater than or equal to) operation.
     */
    @Test
    public void testGreaterThanOrEqual() {
        Expression left = new CoreNumberExpression(5);
        Expression right = new CoreNumberExpression(5);

        CoreOperationRelationalExpression op = new CoreOperationGreaterThanOrEqual(left, right);
        JXPathContext ctx = new JXPathContext();
        ctx.setValues("numbers", new Object[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10});

        Object result = op.evaluate(ctx);
        assertTrue("Result should be true", result instanceof Boolean);
        assertTrue("Result should be true", (boolean) result);

        left = new CoreNumberExpression(10);
        right = new CoreNumberExpression(5);

        op = new CoreOperationGreaterThanOrEqual(left, right);
        result = op.evaluate(ctx);
        assertTrue("Result should be true", result instanceof Boolean);
        assertTrue("Result should be true", (boolean) result);

        left = new CoreNumberExpression(5);
        right = new CoreNumberExpression(10);

        op = new CoreOperationGreaterThanOrEqual(left, right);
        result = op.evaluate(ctx);
        assertFalse("Result should be false", result instanceof Boolean);
        assertNull("Result should be null", result);
    }

    /**
     * Test the &lt; (less than) operation.
     */
    @Test
    public void testLessThan() {
        Expression left = new CoreNumberExpression(5);
        Expression right = new CoreNumberExpression(1);

        CoreOperationRelationalExpression op = new CoreOperationLessThan(left, right);
        JXPathContext ctx = new JXPathContext();
        ctx.setValues("numbers", new Object[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10});

        Object result = op.evaluate(ctx);
        assertTrue("Result should be true", result instanceof Boolean);
        assertTrue("Result should be true", (boolean) result);

        left = new CoreNumberExpression(1);
        right = new CoreNumberExpression(5);

        op = new CoreOperationLessThan(left, right);
        result = op.evaluate(ctx);
        assertFalse("Result should be false", result instanceof Boolean);
        assertNull("Result should be null", result);
    }

    /**
     * Test the &lt;= (less than or equal to) operation.
     */
    @Test
    public void testLessThanOrEqual() {
        Expression left = new CoreNumberExpression(5);
        Expression right = new CoreNumberExpression(5);

        CoreOperationRelationalExpression op = new CoreOperationLessThanOrEqual(left, right);
        JXPathContext ctx = new JXPathContext();
        ctx.setValues("numbers", new Object[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10});

        Object result = op.evaluate(ctx);
        assertTrue("Result should be true", result instanceof Boolean);
        assertTrue("Result should be true", (boolean) result);

        left = new CoreNumberExpression(1);
        right = new CoreNumberExpression(5);

        op = new CoreOperationLessThanOrEqual(left, right);
        result = op.evaluate(ctx);
        assertTrue("Result should be true", result instanceof Boolean);
        assertTrue("Result should be true", (boolean) result);

        left = new CoreNumberExpression(5);
        right = new CoreNumberExpression(1);

        op = new CoreOperationLessThanOrEqual(left, right);
        result = op.evaluate(ctx);
        assertFalse("Result should be false", result instanceof Boolean);
        assertNull("Result should be null", result);
    }

    /**
     * Test the evaluation of an operation with null operands.
     */
    @Test
    public void testNullOperands() {
        Expression left = new CoreNullExpression();
        Expression right = new CoreNullExpression();

        CoreOperationRelationalExpression op = new CoreOperationGreaterThan(left, right);
        JXPathContext ctx = new JXPathContext();

        Object result = op.evaluate(ctx);
        assertNull("Result should be null", result);
    }

    /**
     * Test the evaluation of an operation with a null left operand.
     */
    @Test
    public void testNullLeftOperand() {
        Expression left = new CoreNullExpression();
        Expression right = new CoreNumberExpression(5);

        CoreOperationRelationalExpression op = new CoreOperationGreaterThan(left, right);
        JXPathContext ctx = new JXPathContext();

        Object result = op.evaluate(ctx);
        assertNull("Result should be null", result);
    }

    /**
     * Test the evaluation of an operation with a null right operand.
     */
    @Test
    public void testNullRightOperand() {
        Expression left = new CoreNumberExpression(5);
        Expression right = new CoreNullExpression();

        CoreOperationRelationalExpression op = new CoreOperationGreaterThan(left, right);
        JXPathContext ctx = new JXPathContext();

        Object result = op.evaluate(ctx);
        assertFalse("Result should be false", result instanceof Boolean);
        assertNull("Result should be null", result);
    }

}