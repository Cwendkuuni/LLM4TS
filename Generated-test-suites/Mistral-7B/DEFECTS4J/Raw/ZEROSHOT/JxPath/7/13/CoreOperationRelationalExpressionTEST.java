package org.apache.commons.jxpath.ri.compiler.test;

import org.apache.commons.jxpath.ri.compiler.CoreOperationRelationalExpression;
import org.apache.commons.jxpath.ri.compiler.CoreOperation;
import org.apache.commons.jxpath.ri.compiler.Expression;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class CoreOperationRelationalExpressionTest {

    @Test
    public void testCoreOperationRelationalExpressionConstruction() {
        Expression[] args = new Expression[2];
        Expression arg1 = new CoreNumberExpression(1);
        Expression arg2 = new CoreNumberExpression(2);
        args[0] = arg1;
        args[1] = arg2;

        CoreOperationRelationalExpression expression = new CoreOperationGreaterThanExpression(args);
        assertNotNull(expression);
        assertEquals(CoreOperationRelationalExpression.class, expression.getClass());
        assertEquals(2, expression.getArgs().length);
        assertEquals(arg1.getClass(), expression.getArgs()[0].getClass());
        assertEquals(arg2.getClass(), expression.getArgs()[1].getClass());
        assertEquals(CoreOperation.Type.RELATIONAL_EXPRESSION, expression.getType());
        assertEquals(3, expression.getPrecedence());
        assertFalse(expression.isSymmetric());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCoreOperationRelationalExpressionNullArgs() {
        Expression[] args = null;
        CoreOperationRelationalExpression expression = new CoreOperationGreaterThanExpression(args);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCoreOperationRelationalExpressionEmptyArgs() {
        Expression[] args = new Expression[0];
        CoreOperationRelationalExpression expression = new CoreOperationGreaterThanExpression(args);
    }

    @Test
    public void testCoreOperationRelationalExpressionEquals() {
        Expression[] args1 = new Expression[2];
        Expression arg1 = new CoreNumberExpression(1);
        Expression arg2 = new CoreNumberExpression(2);
        args1[0] = arg1;
        args1[1] = arg2;

        CoreOperationRelationalExpression expression1 = new CoreOperationGreaterThanExpression(args1);

        Expression[] args2 = new Expression[2];
        args2[0] = arg1;
        args2[1] = arg2;

        CoreOperationRelationalExpression expression2 = new CoreOperationGreaterThanExpression(args2);

        Expression[] args3 = new Expression[2];
        args3[0] = arg2;
        args3[1] = arg1;

        CoreOperationRelationalExpression expression3 = new CoreOperationGreaterThanExpression(args3);

        assertEquals(expression1, expression1);
        assertEquals(expression1, expression1);
        assertEquals(expression1, expression1);

        assertEquals(expression1, expression2);
        assertEquals(expression2, expression1);

        assertNotEquals(expression1, expression3);
        assertNotEquals(expression3, expression1);
    }

    @Test
    public void testCoreOperationRelationalExpressionHashCode() {
        Expression[] args = new Expression[2];
        Expression arg1 = new CoreNumberExpression(1);
        Expression arg2 = new CoreNumberExpression(2);
        args[0] = arg1;
        args[1] = arg2;

        CoreOperationRelationalExpression expression1 = new CoreOperationGreaterThanExpression(args);
        CoreOperationRelationalExpression expression2 = new CoreOperationGreaterThanExpression(args);

        int hashCode1 = expression1.hashCode();
        int hashCode2 = expression2.hashCode();

        assertEquals(hashCode1, hashCode2);
    }

    @Test
    public void testCoreOperationRelationalExpressionToString() {
        Expression[] args = new Expression[2];
        Expression arg1 = new CoreNumberExpression("1");
        Expression arg2 = new CoreNumberExpression("2");
        args[0] = arg1;
        args[1] = arg2;

        CoreOperationRelationalExpression expression = new CoreOperationGreaterThanExpression(args);

        String result = expression.toString();
        String expected = ">(1, 2)";
        assertEquals(expected, result);
    }

    @Test
    public void testCoreOperationRelationalExpressionEvaluate() {
        Expression[] args = new Expression[2];
        Expression arg1 = new CoreNumberExpression(1);
        Expression arg2 = new CoreNumberExpression(2);
        args[0] = arg1;
        args[1] = arg2;

        CoreOperationRelationalExpression expression = new CoreOperationGreaterThanExpression(args);

        Object result = expression.evaluate(new Object[] {new Object() {
            public int value;

            public int getValue() {
                return value;
            }
        }, new Object() {
            public int value;

            public int getValue() {
                return value;
            }
        }});

        assertNull(result);

        arg1.setValue(3);
        arg2.setValue(2);

        result = expression.evaluate(new Object[] {arg1, arg2});

        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.TRUE, result);

        arg1.setValue(2);
        arg2.setValue(3);

        result = expression.evaluate(new Object[] {arg1, arg2});

        assertNull(result);

        arg1.setValue(3);
        arg2.setValue(3);

        result = expression.evaluate(new Object[] {arg1, arg2});

        assertNull(result);
    }

    @Test
    public void testCoreOperationRelationalExpressionEvaluateWithNullValues() {
        Expression[] args = new Expression[2];
        Expression arg1 = new CoreNumberExpression(1);
        Expression arg2 = new CoreNumberExpression(null);
        args[0] = arg1;
        args[1] = arg2;

        CoreOperationRelationalExpression expression = new CoreOperationGreaterThanExpression(args);

        Object[] values = new Object[] {new Object() {
            public Integer value;

            public Integer getValue() {
                return value;
            }
        }, null};

        Object result = expression.evaluate(values);

        assertNull(result);
    }

    @Test
    public void testCoreOperationRelationalExpressionEvaluateFalse() {
        Expression[] args = new Expression[2];
        Expression arg1 = new CoreNumberExpression(2);
        Expression arg2 = new CoreNumberExpression(3);
        args[0] = arg1;
        args[1] = arg2;

        CoreOperationRelationalExpression expression = new CoreOperationGreaterThanExpression(args);

        Object[] values = new Object[] {arg1, arg2};

        Object result = expression.evaluate(values);

        assertNull(result);

        arg1.setValue(3);
        arg2.setValue(3);

        result = expression.evaluate(values);

        assertNull(result);
    }

    @Test
    public void testCoreOperationRelationalExpressionEvaluateTrue() {
        Expression[] args = new Expression[2];
        Expression arg1 = new CoreNumberExpression(3);
        Expression arg2 = new CoreNumberExpression(2);
        args[0] = arg1;
        args[1] = arg2;

        CoreOperationRelationalExpression expression = new CoreOperationGreaterThanExpression(args);

        Object[] values = new Object[] {arg1, arg2};

        Object result = expression.evaluate(values);

        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.TRUE, result);
    }

}