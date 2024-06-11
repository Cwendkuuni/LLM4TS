import org.apache.commons.jxpath.ri.compiler.CoreOperationRelationalExpression;
import org.apache.commons.jxpath.ri.compiler.CoreOperation;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

public class CoreOperationRelationalExpressionTest {

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorWithNullArgs() {
        assertThrows(IllegalArgumentException.class, () -> new CoreOperationRelationalExpression(null));
    }

    @Test
    public void testGetPrecedence() {
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(new CoreOperation[] {});
        assertEquals(3, expression.getPrecedence());
    }

    @Test
    public void testIsSymmetric() {
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(new CoreOperation[] {});
        assertFalse(expression.isSymmetric());
    }

    @Test
    public void testCoreOperationRelationalExpressionWithNullArgs() {
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(new CoreOperation[0]);
        assertNull(expression.getLeftOperand());
        assertNull(expression.getRightOperand());
    }

    @Test
    public void testCoreOperationRelationalExpressionWithValidArgs() {
        Expression left = new CoreNumberExpression(5);
        Expression right = new CoreNumberExpression(10);
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(new CoreOperation[] {left, right});
        assertEquals(left, expression.getLeftOperand());
        assertEquals(right, expression.getRightOperand());
    }

    @Test
    public void testGreaterThan() {
        Expression left = new CoreNumberExpression(5);
        Expression right = new CoreNumberExpression(10);
        CoreOperationGreaterThan greaterThan = new CoreOperationGreaterThan(new CoreOperation[] {left, right});
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(new CoreOperation[] {left, right});
        assertFalse(expression.evaluate(new Context()));
        left = new CoreNumberExpression(10);
        assertTrue(expression.evaluate(new Context()));
    }

    @Test
    public void testGreaterThanOrEqual() {
        Expression left = new CoreNumberExpression(5);
        Expression right = new CoreNumberExpression(5);
        CoreOperationGreaterThanOrEqual greaterThanOrEqual = new CoreOperationGreaterThanOrEqual(new CoreOperation[] {left, right});
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(new CoreOperation[] {left, right});
        assertTrue(expression.evaluate(new Context()));
        left = new CoreNumberExpression(4);
        assertFalse(expression.evaluate(new Context()));
        left = new CoreNumberExpression(5);
        assertTrue(expression.evaluate(new Context()));
    }

    @Test
    public void testLessThan() {
        Expression left = new CoreNumberExpression(5);
        Expression right = new CoreNumberExpression(10);
        CoreOperationLessThan lessThan = new CoreOperationLessThan(new CoreOperation[] {left, right});
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(new CoreOperation[] {left, right});
        assertFalse(expression.evaluate(new Context()));
        left = new CoreNumberExpression(4);
        assertTrue(expression.evaluate(new Context()));
    }

    @Test
    public void testLessThanOrEqual() {
        Expression left = new CoreNumberExpression(5);
        Expression right = new CoreNumberExpression(5);
        CoreOperationLessThanOrEqual lessThanOrEqual = new CoreOperationLessThanOrEqual(new CoreOperation[] {left, right});
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(new CoreOperation[] {left, right});
        assertTrue(expression.evaluate(new Context()));
        left = new CoreNumberExpression(6);
        assertFalse(expression.evaluate(new Context()));
        left = new CoreNumberExpression(5);
        assertTrue(expression.evaluate(new Context()));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCoreOperationRelationalExpressionWithUnevenArgs() {
        Expression left = new CoreNumberExpression(5);
        Expression right = new CoreNumberExpression(10);
        assertThrows(IllegalArgumentException.class, () -> new CoreOperationRelationalExpression(new CoreOperation[] {left}));
        assertThrows(IllegalArgumentException.class, () -> new CoreOperationRelationalExpression(new CoreOperation[] {right}));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCoreOperationRelationalExpressionWithMismatchedTypes() {
        Expression left = new CoreNumberExpression(5);
        Expression right = new CoreStringExpression("5");
        assertThrows(IllegalArgumentException.class, () -> new CoreOperationRelationalExpression(new CoreOperation[] {left, right}));
    }

    @Test
    public void testGreaterThanWithNullOperands() {
        Expression left = null;
        Expression right = new CoreNumberExpression(10);
        CoreOperationGreaterThan greaterThan = new CoreOperationGreaterThan(new CoreOperation[] {left, right});
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(new CoreOperation[] {left, right});
        assertNull(expression.getLeftOperand());
        assertNull(expression.getRightOperand());
        assertThrows(IllegalArgumentException.class, expression::evaluate);
    }

    @Test
    public void testGreaterThanOrEqualWithNullLeftOperand() {
        Expression left = null;
        Expression right = new CoreNumberExpression(5);
        CoreOperationGreaterThanOrEqual greaterThanOrEqual = new CoreOperationGreaterThanOrEqual(new CoreOperation[] {left, right});
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(new CoreOperation[] {left, right});
        assertNull(expression.getLeftOperand());
        assertNull(expression.getRightOperand());
        assertThrows(IllegalArgumentException.class, expression::evaluate);
    }

    @Test
    public void testGreaterThanOrEqualWithNullRightOperand() {
        Expression left = new CoreNumberExpression(5);
        Expression right = null;
        CoreOperationGreaterThanOrEqual greaterThanOrEqual = new CoreOperationGreaterThanOrEqual(new CoreOperation[] {left, right});
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(new CoreOperation[] {left, right});
        assertNull(expression.getLeftOperand());
        assertNull(expression.getRightOperand());
        assertThrows(IllegalArgumentException.class, expression::evaluate);
    }

}