package org.apache.commons.jxpath.ri.compiler.test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InfoSetUtil;
import org.apache.commons.jxpath.ri.axes.InitialContext;
import org.apache.commons.jxpath.ri.axes.SelfContext;
import org.apache.commons.jxpath.ri.compiler.CoreOperationRelationalExpression;
import org.apache.commons.jxpath.ri.compiler.CoreOperationRelationalExpression.CoreOperationRelationalExpressionFactory;
import org.apache.commons.jxpath.ri.compiler.Expression;
import org.apache.commons.jxpath.ri.compiler.ExpressionFactory;
import org.apache.commons.jxpath.ri.compiler.NumberValue;
import org.apache.commons.jxpath.ri.compiler.StringValue;
import org.apache.commons.jxpath.ri.compiler.Variable;
import org.junit.Test;

/**
 * Test class for CoreOperationRelationalExpression.
 *
 * @author Yasser.El-Said
 */
public class CoreOperationRelationalExpressionTest {

    /**
     * Test constructor.
     */
    @Test
    public void testConstructor() {
        Expression left = new Variable("l");
        Expression right = new NumberValue(5);
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpressionFactory()
                .createGreaterThanExpression(new Expression[]{ left, right });
        assertNotNull(expression);
    }

    /**
     * Test computeValue method.
     */
    @Test
    public void testComputeValue() {
        CoreOperationRelationalExpression expression = createExpression("5 > 3");
        EvalContext context = new EvalContext(new SelfContext(new Object()));
        Object result = expression.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result);
    }

    /**
     * Test computeValue method with false result.
     */
    @Test
    public void testComputeValueFalse() {
        CoreOperationRelationalExpression expression = createExpression("3 > 5");
        EvalContext context = new EvalContext(new SelfContext(new Object()));
        Object result = expression.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertFalse((Boolean) result);
    }

    /**
     * Test computeValue method with iterators.
     */
    @Test
    public void testComputeValueIterators() {
        CoreOperationRelationalExpression expression = createExpression("(1, 2, 3) > (1, 2, 4)");
        EvalContext context = new EvalContext(new SelfContext(new Object()));
        Iterator left = Arrays.asList(1, 2, 3).iterator();
        Iterator right = Arrays.asList(1, 2, 4).iterator();
        context.setCurrentNode(new SelfContext(Collections.singletonList(expression)));
        context.setCurrentIndex(0);
        context.setCurrentIterator(left);
        Object result = expression.computeValue(context);
        assertFalse(result instanceof Boolean);
        context.setCurrentIterator(right);
        context.next();
        result = expression.computeValue(context);
        assertFalse(result instanceof Boolean);
        context.next();
        result = expression.computeValue(context);
        assertTrue(result instanceof Boolean);
    }

    /**
     * Test computeValue method with NaN values.
     */
    @Test
    public void testComputeValueNaN() {
        CoreOperationRelationalExpression expression = createExpression("NaN > NaN");
        EvalContext context = new EvalContext(new SelfContext(new Object()));
        Object left = InfoSetUtil.createValueFromString("NaN");
        Object right = InfoSetUtil.createValueFromString("NaN");
        context.setCurrentNode(new SelfContext(Collections.singletonList(expression)));
        context.setCurrentIndex(0);
        context.setCurrentNodeValue(left);
        context.setCurrentNodeValue(right);
        Object result = expression.computeValue(context);
        assertFalse(result instanceof Boolean);
    }

    /**
     * Test getPrecedence method.
     */
    @Test
    public void testGetPrecedence() {
        CoreOperationRelationalExpression expression = createExpression("5 > 3");
        int precedence = expression.getPrecedence();
        assertEquals(RELATIONAL_EXPR_PRECEDENCE, precedence);
    }

    /**
     * Test isSymmetric method.
     */
    @Test
    public void testIsSymmetric() {
        CoreOperationRelationalExpression expression = createExpression("5 > 3");
        assertFalse(expression.isSymmetric());
    }

    /**
     * Test evaluateCompare method with equal comparison.
     */
    @Test
    public void testEvaluateCompareEqual() {
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpressionFactory()
                .createGreaterThanOrEqualExpression(new Expression[]{ new NumberValue(3), new NumberValue(3) });
        assertTrue(expression.evaluateCompare(0));
    }

    /**
     * Test evaluateCompare method with less comparison.
     */
    @Test
    public void testEvaluateCompareLess() {
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpressionFactory()
                .createLessThanExpression(new Expression[]{ new NumberValue(3), new NumberValue(5) });
        assertTrue(expression.evaluateCompare(-1));
    }

    /**
     * Test evaluateCompare method with greater comparison.
     */
    @Test
    public void testEvaluateCompareGreater() {
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpressionFactory()
                .createGreaterThanExpression(new Expression[]{ new NumberValue(5), new NumberValue(3) });
        assertTrue(expression.evaluateCompare(1));
    }

    /**
     * Test evaluateCompare method with less or equal comparison.
     */
    @Test
    public void testEvaluateCompareLessOrEqual() {
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpressionFactory()
                .createLessThanOrEqualExpression(new Expression[]{ new NumberValue(3), new NumberValue(3) });
        assertTrue(expression.evaluateCompare(0));
    }

    /**
     * Test evaluateCompare method with greater or equal comparison.
     */
    @Test
    public void testEvaluateCompareGreaterOrEqual() {
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpressionFactory()
                .createGreaterThanOrEqualExpression(new Expression[]{ new NumberValue(3), new NumberValue(3) });
        assertTrue(expression.evaluateCompare(0));
    }

    /**
     * Test evaluateCompare method with strings.
     */
    @Test
    public void testEvaluateCompareStrings() {
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpressionFactory()
                .createGreaterThanExpression(new Expression[]{ new StringValue("abc"), new StringValue("def") });
        assertFalse(expression.evaluateCompare(0));
        StringValue str1 = new StringValue("apple");
        StringValue str2 = new StringValue("banana");
        expression = new CoreOperationRelationalExpressionFactory()
                .createGreaterThanExpression(new Expression[]{ str1, str2 });
        assertTrue(expression.evaluateCompare(-1));
        StringValue str3 = new StringValue("apple");
        StringValue str4 = new StringValue("apple");
        expression = new CoreOperationRelationalExpressionFactory()
                .createGreaterThanOrEqualExpression(new Expression[]{ str3, str4 });
        assertTrue(expression.evaluateCompare(0));
    }

    /**
     * Test reduce method with initial context.
     */
    @Test
    public void testReduceInitialContext() {
        CoreOperationRelationalExpression expression = createExpression("self::node() > initial::node()");
        Object result = expression.reduce(new InitialContext());
        assertNotNull(result);
        assertTrue(result instanceof SelfContext);
    }

    /**
     * Test reduce method with collections.
     */
    @Test
    public void testReduceCollections() {
        CoreOperationRelationalExpression expression = createExpression("(1, 2, 3) > (2, 3, 4)");
        Object result = expression.reduce(Collections.singletonList(new SelfContext(new Object()))
                .iterator());
        assertFalse(result instanceof Boolean);
        result = expression.reduce(Collections.singletonList(new Iterator<Object>() {
            @Override
            public boolean hasNext() {
                return true;
            }

            @Override
            public Object next() {
                return new NumberValue(1);
            }
        }).iterator());
        result = expression.reduce(Collections.singletonList(new Iterator<Object>() {
            @Override
            public boolean hasNext() {
                return true;
            }

            @Override
            public Object next() {
                return new NumberValue(2);
            }
        }).iterator());
        result = expression.reduce(Collections.singletonList(new Iterator<Object>() {
            @Override
            public boolean hasNext() {
                return true;
            }

            @Override
            public Object next() {
                return new NumberValue(3);
            }
        }).iterator());
        result = expression.reduce(Collections.singletonList(new Iterator<Object>() {
            @Override
            public boolean hasNext() {
                return true;
            }

            @Override
            public Object next() {
                return new NumberValue(2);
            }
        }).iterator());
        result = expression.reduce(Collections.singletonList(new Iterator<Object>() {
            @Override
            public boolean hasNext() {
                return true;
            }

            @Override
            public Object next() {
                return new NumberValue(3);
            }
        }).iterator());
        result = expression.reduce(Collections.singletonList(new Iterator<Object>() {
            @Override
            public boolean hasNext() {
                return true;
            }

            @Override
            public Object next() {
                return new NumberValue(3);
            }
        }).iterator());
        result = expression.reduce(Collections.singletonList(new Iterator<Object>() {
            @Override
            public boolean hasNext() {
                return true;
            }

            @Override
            public Object next() {
                return new NumberValue(4);
            }
        }).iterator());
        assertFalse(result instanceof Boolean);
    }

    /**
     * Test containsMatch method.
     */
    @Test
    public void testContainsMatch() {
        CoreOperationRelationalExpression expression = createExpression("(1, 2, 3) > 2");
        Iterator left = Collections.singletonList(1, 2, 3).iterator();
        Object result = expression.containsMatch(left, 2);
        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result);
        left = Collections.singletonList(1, 2, 4).iterator();
        result = expression.containsMatch(left, 2);
        assertTrue(result instanceof Boolean);
        assertFalse((Boolean) result);
    }

    /**
     * Test findMatch method.
     */
    @Test
    public void testFindMatch() {
        CoreOperationRelationalExpression expression = createExpression("(1, 2, 3) > (1, 2)");
        Iterator left = Arrays.asList(1, 2, 3).iterator();
        Iterator right = Arrays.asList(1, 2).iterator();
        Object result = expression.findMatch(left, right);
        assertFalse(result instanceof Boolean);
        left = Arrays.asList(1, 2, 2).iterator();
        result = expression.findMatch(left, right);
        assertTrue(result instanceof Boolean);
    }

    /**
     * Create an expression instance.
     *
     * @param expressionString expression string
     * @return expression instance
     */
    private CoreOperationRelationalExpression createExpression(String expressionString) {
        ExpressionFactory expressionFactory = new ExpressionFactory() {
            @Override
            public Expression createStringValue(String string) {
                return new StringValue(string);
            }

            @Override
            public Expression createNumberValue(Number number) {
                return new NumberValue(number);
            }

            @Override
            public Expression createVariable(String name) {
                return new Variable(name);
            }
        };
        Expression left = parseExpression(expressionFactory, expressionString.split(" ", 2)[0]);
        Expression right = parseExpression(expressionFactory, expressionString.split(" ", 2)[1]);
        return new CoreOperationRelationalExpressionFactory()
                .createGreaterThanExpression(new Expression[]{ left, right });
    }

    /**
     * Parse an expression.
     *
     * @param expressionFactory expression factory
     * @param expressionString expression string
     * @return expression instance
     */
    private Expression parseExpression(ExpressionFactory expressionFactory, String expressionString) {
        Expression expression = null;
        int opIndex = expressionString.indexOf(" ");
        if (opIndex != -1) {
            String op = expressionString.substring(0, opIndex);
            Expression left = parseExpression(expressionFactory, expressionString.substring(opIndex + 1));
            switch (op) {
                case ">":
                    expression = new CoreOperationRelationalExpressionFactory()
                            .createGreaterThanExpression(new Expression[]{ expressionFactory.createVariable("l"), left });
                    break;
                case ">=":
                    expression = new CoreOperationRelationalExpressionFactory()
                            .createGreaterThanOrEqualExpression(new Expression[]{ expressionFactory.createVariable("l"), left });
                    break;
                case "<":
                    expression = new CoreOperationRelationalExpressionFactory()
                            .createLessThanExpression(new Expression[]{ expressionFactory.createVariable("l"), left });
                    break;
                case "<=":
                    expression = new CoreOperationRelationalExpressionFactory()
                            .createLessThanOrEqualExpression(new Expression[]{ expressionFactory.createVariable("l"), left });
                    break;
            }
        } else {
            String variableName = expressionString;
            expression = expressionFactory.createVariable(variableName);
        }
        return expression;
    }

}