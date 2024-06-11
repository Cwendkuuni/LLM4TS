package org.apache.commons.jxpath.ri.compiler.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import org.apache.commons.jxpath.ri.compiler.CoreOperation;
import org.apache.commons.jxpath.ri.compiler.CoreOperationRelationalExpression;
import org.apache.commons.jxpath.ri.compiler.Expression;
import org.apache.commons.jxpath.ri.compiler.TestUtils;

public class CoreOperationRelationalExpressionTest {

    @Test
    public void testCoreOperationRelationalExpression() {
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(new Expression[] { new ConstantExpression(1), new ConstantExpression(2) });
        assertEquals("(1 > 2)", expression.toString());
        assertEquals(3, expression.getPrecedence());
        assertFalse(expression.isSymmetric());
    }

    @Test
    public void testGetPrecedence() {
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(new Expression[] { new ConstantExpression(1), new ConstantExpression(2) });
        assertEquals(3, expression.getPrecedence());
    }

    @Test
    public void testIsSymmetric() {
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(new Expression[] { new ConstantExpression(1), new ConstantExpression(2) });
        assertFalse(expression.isSymmetric());
    }

    @Test
    public void testEquals() {
        Expression expr1 = new CoreOperationRelationalExpression(new Expression[] { new ConstantExpression(1), new ConstantExpression(2) });
        Expression expr2 = new CoreOperationRelationalExpression(new Expression[] { new ConstantExpression(1), new ConstantExpression(2) });
        Expression expr3 = new CoreOperationRelationalExpression(new Expression[] { new ConstantExpression(2), new ConstantExpression(1) });

        assertTrue(TestUtils.equals(expr1, expr2));
        assertFalse(TestUtils.equals(expr1, expr3));
    }

    @Test
    public void testHashCode() {
        Expression expr1 = new CoreOperationRelationalExpression(new Expression[] { new ConstantExpression(1), new ConstantExpression(2) });
        Expression expr2 = new CoreOperationRelationalExpression(new Expression[] { new ConstantExpression(1), new ConstantExpression(2) });
        Expression expr3 = new CoreOperationRelationalExpression(new Expression[] { new ConstantExpression(2), new ConstantExpression(1) });

        assertEquals(expr1.hashCode(), expr2.hashCode());
        assertNotEquals(expr1.hashCode(), expr3.hashCode());
    }

    @Test
    public void testToString() {
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(new Expression[] { new ConstantExpression(1), new ConstantExpression(2) });
        assertEquals("(1 > 2)", expression.toString());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorNullArg() {
        new CoreOperationRelationalExpression(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorEmptyArg() {
        new CoreOperationRelationalExpression(new Expression[0]);
    }

    @Test
    public void testToStringWithArgs() {
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(new Expression[] { new VariableExpression("a"), new ConstantExpression(5) });
        assertEquals("(a > 5)", expression.toString());
    }

    @Test
    public void testToStringWithOneArg() {
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(new Expression[] { new VariableExpression("a") });
        assertEquals("(a > null)", expression.toString());
    }

    @Test
    public void testToStringWithEmptyArgs() {
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(new Expression[0]);
        assertEquals("(null > null)", expression.toString());
    }

    @Test
    public void testClone() {
        Expression expr = new CoreOperationRelationalExpression(new Expression[] { new ConstantExpression(1), new ConstantExpression(2) });
        CoreOperationRelationalExpression cloned = (CoreOperationRelationalExpression) expr.clone();
        assertEquals(expr, cloned);
        assertNotEquals(expr.hashCode(), cloned.hashCode());
    }

    @Test
    public void testCloneWithArgs() {
        Expression expr = new CoreOperationRelationalExpression(new Expression[] { new VariableExpression("a"), new ConstantExpression(5) });
        CoreOperationRelationalExpression cloned = (CoreOperationRelationalExpression) expr.clone();
        assertSame(expr.getArgs(), cloned.getArgs());
        assertEquals(expr.hashCode(), cloned.hashCode());
    }

    @Test
    public void testCloneWithNullArgs() {
        Expression expr = new CoreOperationRelationalExpression(null);
        CoreOperationRelationalExpression cloned = (CoreOperationRelationalExpression) expr.clone();
        assertNull(cloned.getArgs());
        assertEquals(expr.hashCode(), cloned.hashCode());
    }

    @Test
    public void testCloneWithEmptyArgs() {
        Expression expr = new CoreOperationRelationalExpression(new Expression[0]);
        CoreOperationRelationalExpression cloned = (CoreOperationRelationalExpression) expr.clone();
        assertArrayEquals(new Expression[0], cloned.getArgs());
        assertEquals(expr.hashCode(), cloned.hashCode());
    }

    @Test
    public void testCloneWithArgsOfDifferentType() {
        Expression expr = new CoreOperationRelationalExpression(new Expression[] { new VariableExpression("a"), new ConstantExpression(5.0) });
        CoreOperationRelationalExpression cloned = (CoreOperationRelationalExpression) expr.clone();
        assertNotSame(expr.getArgs()[0].getClass(), cloned.getArgs()[0].getClass());
        assertEquals(expr.hashCode(), cloned.hashCode());
    }

    @Test
    public void testCloneWithArgsOfSameTypeButDifferentValues() {
        Expression expr = new CoreOperationRelationalExpression(new Expression[] { new ConstantExpression("foo"), new ConstantExpression("bar") });
        CoreOperationRelationalExpression cloned = (CoreOperationRelationalExpression) expr.clone();
        assertNotEquals(expr.getArgs()[0].getValue(), cloned.getArgs()[0].getValue());
        assertEquals(expr.hashCode(), cloned.hashCode());
    }
}