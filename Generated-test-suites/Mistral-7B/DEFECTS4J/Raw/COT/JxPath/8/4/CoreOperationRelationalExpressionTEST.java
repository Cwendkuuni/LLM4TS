package org.apache.commons.jxpath.ri.compiler.test;

import static org.junit.Assert.*;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InfoSet;
import org.apache.commons.jxpath.ri.IteratorIter;
import org.apache.commons.jxpath.ri.Node;
import org.apache.commons.jxpath.ri.axes.InitialContext;
import org.apache.commons.jxpath.ri.compiler.CoreOperationRelationalExpression;
import org.apache.commons.jxpath.ri.compiler.CoreOperationRelationalExpressionTest;
import org.apache.commons.jxpath.ri.compiler.CoreOperation;
import org.apache.commons.jxpath.ri.compiler.Constant;
import org.apache.commons.jxpath.ri.compiler.Expression;
import org.apache.commons.jxpath.ri.compiler.NumberValue;
import org.apache.commons.jxpath.ri.compiler.StringValue;
import org.apache.commons.jxpath.ri.expr.ExpressionVisitor;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

/**
 * Test for CoreOperationRelationalExpression class.
 */
public class CoreOperationRelationalExpressionTest {

    /**
     * Test method for {@link CoreOperationRelationalExpression#computeValue(EvalContext)}.
     */
    @org.junit.Test
    public void testComputeValue() throws Exception {
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(new Expression[]{
            new Constant(new InitialContext()),
            new Constant(new NumberValue(5))
        }) {
            @Override
            protected boolean evaluateCompare(int compare) {
                return compare > 0;
            }
        };

        EvalContext context = new EvalContext(new InfoSet(), new InitialContext());
        Object result = expression.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result);

        expression = new CoreOperationRelationalExpression(new Expression[]{
            new Constant(new InitialContext()),
            new Constant(new NumberValue(5))
        }) {
            @Override
            protected boolean evaluateCompare(int compare) {
                return compare < 0;
            }
        };

        result = expression.computeValue(context);
        assertFalse(result instanceof Boolean);

        expression = new CoreOperationRelationalExpression(new Expression[]{
            new Constant(new NumberValue(5)),
            new Constant(new NumberValue(5))
        }) {
            @Override
            protected boolean evaluateCompare(int compare) {
                return compare == 0;
            }
        };

        result = expression.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertFalse((Boolean) result);

        expression = new CoreOperationRelationalExpression(new Expression[]{
            new Constant(new NumberValue(5)),
            new Constant(new NumberValue(5))
        }) {
            @Override
            protected boolean evaluateCompare(int compare) {
                return compare == 0;
            }
        };

        result = expression.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result);
    }

    /**
     * Test method for {@link CoreOperationRelationalExpression#computeValue(EvalContext)}.
     */
    @org.junit.Test
    public void testComputeValueWithIterators() throws Exception {
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(new Expression[]{
            new Constant(new IteratorIter(new ArrayList<>(List.of(1, 2, 3)).iterator())),
            new Constant(new IteratorIter(new ArrayList<>(List.of(3, 2, 1)).iterator()))
        }) {
            @Override
            protected boolean evaluateCompare(int compare) {
                return compare > 0;
            }
        };

        EvalContext context = new EvalContext(new InfoSet(), new InitialContext());
        Object result = expression.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result);

        expression = new CoreOperationRelationalExpression(new Expression[]{
            new Constant(new IteratorIter(new ArrayList<>(List.of(1, 2, 3)).iterator())),
            new Constant(new IteratorIter(new ArrayList<>(List.of(3, 2, 1)).iterator()))
        }) {
            @Override
            protected boolean evaluateCompare(int compare) {
                return compare < 0;
            }
        };

        result = expression.computeValue(context);
        assertFalse(result instanceof Boolean);

        expression = new CoreOperationRelationalExpression(new Expression[]{
            new Constant(new IteratorIter(new ArrayList<>(List.of(1, 2, 3)).iterator())),
            new Constant(new IteratorIter(new ArrayList<>(List.of(3, 2, 1)).iterator()))
        }) {
            @Override
            protected boolean evaluateCompare(int compare) {
                return compare == 0;
            }
        };

        result = expression.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertFalse((Boolean) result);

        expression = new CoreOperationRelationalExpression(new Expression[]{
            new Constant(new IteratorIter(new ArrayList<>(List.of(1, 2, 3)).iterator())),
            new Constant(new IteratorIter(new ArrayList<>(List.of(3, 2, 1)).iterator()))
        }) {
            @Override
            protected boolean evaluateCompare(int compare) {
                return compare == 0;
            }
        };

        result = expression.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result);
    }

    /**
     * Test method for {@link CoreOperationRelationalExpression#computeValue(EvalContext)}.
     */
    @org.junit.Test
    public void testComputeValueWithStrings() throws Exception {
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(new Expression[]{
            new Constant(new StringValue("abc")),
            new Constant(new StringValue("abc"))
        }) {
            @Override
            protected boolean evaluateCompare(int compare) {
                return compare == 0;
            }
        };

        EvalContext context = new EvalContext(new InfoSet(), new InitialContext());
        Object result = expression.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result);

        expression = new CoreOperationRelationalExpression(new Expression[]{
            new Constant(new StringValue("abc")),
            new Constant(new StringValue("def"))
        }) {
            @Override
            protected boolean evaluateCompare(int compare) {
                return compare < 0;
            }
        };

        result = expression.computeValue(context);
        assertFalse(result instanceof Boolean);

        expression = new CoreOperationRelationalExpression(new Expression[]{
            new Constant(new StringValue("abc")),
            new Constant(new StringValue("abc"))
        }) {
            @Override
            protected boolean evaluateCompare(int compare) {
                return compare > 0;
            }
        };

        try {
            expression.computeValue(context);
            fail("Expected exception");
        } catch (AssertionError e) {
            // Expected
        }

        expression = new CoreOperationRelationalExpression(new Expression[]{
            new Constant(new StringValue("abc")),
            new Constant(new StringValue("def"))
        }) {
            @Override
            protected boolean evaluateCompare(int compare) {
                return compare > 0;
            }
        };

        try {
            expression.computeValue(context);
            fail("Expected exception");
        } catch (AssertionError e) {
            // Expected
        }

        expression = new CoreOperationRelationalExpression(new Expression[]{
            new Constant(new InitialContext()),
            new CoreOperationRelationalExpression(new Expression[]{
                new Constant(new StringValue("abc")),
                new Constant(new StringValue("def"))
            })
        }) {
            @Override
            protected boolean evaluateCompare(int compare) {
                return compare > 0;
            }
        };

        try {
            expression.computeValue(context);
            fail("Expected exception");
        } catch (Exception e) {
            // Expected
        }
    }

    /**
     * Test method for {@link CoreOperationRelationalExpression#getPrecedence()}.
     */
    @org.junit.Test
    public void testGetPrecedence() throws Exception {
        assertEquals(3, new CoreOperationRelationalExpression(null).getPrecedence());
    }

    /**
     * Test method for {@link CoreOperationRelationalExpression#isSymmetric()}.
     */
    @org.junit.Test
    public void testIsSymmetric() throws Exception {
        assertFalse(new CoreOperationRelationalExpression(null).isSymmetric());
    }

    /**
     * Test method for {@link CoreOperationRelationalExpression#equals(Object)}.
     */
    @org.junit.Test
    public void testEquals() throws Exception {
        CoreOperationRelationalExpression expression1 = new CoreOperationRelationalExpression(new Expression[]{
            new Constant(new NumberValue(5)),
            new Constant(new NumberValue(5))
        }) {
            @Override
            protected boolean evaluateCompare(int compare) {
                return compare == 0;
            }
        };

        CoreOperationRelationalExpression expression2 = new CoreOperationRelationalExpression(new Expression[]{
            new Constant(new NumberValue(5)),
            new Constant(new NumberValue(5))
        }) {
            @Override
            protected boolean evaluateCompare(int compare) {
                return compare == 0;
            }
        };

        assertTrue(expression1.equals(expression2));
        assertTrue(expression2.equals(expression1));

        CoreOperationRelationalExpression expression3 = new CoreOperationRelationalExpression(new Expression[]{
            new Constant(new NumberValue(5)),
            new Constant(new NumberValue(6))
        }) {
            @Override
            protected boolean evaluateCompare(int compare) {
                return compare == 0;
            }
        };

        assertFalse(expression1.equals(expression3));
        assertFalse(expression3.equals(expression1));
    }

    /**
     * Test method for {@link CoreOperationRelationalExpression#toString()}.
     */
    @org.junit.Test
    public void testToString() throws Exception {
        Expression expr = new CoreOperationRelationalExpression(new Expression[]{
            new Constant(new NumberValue(5)),
            new Constant(new NumberValue(6))
        }) {
            @Override
            protected boolean evaluateCompare(int compare) {
                return compare > 0;
            }
        };

        String expected = "(> (5) (6))";
        String actual = expr.toString(new ExpressionVisitor() {
            @Override
            public String visitBinaryOperator(CoreOperation operator) {
                return operator.getOperatorTokenName() + " " + operator.getLeftOperand().toString() + " " + operator.getRightOperand().toString();
            }
        });

        assertEquals(expected, actual);
    }
}