package org.apache.commons.jxpath.ri.compiler.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InfoSetUtil;
import org.apache.commons.jxpath.ri.axes.InitialContext;
import org.apache.commons.jxpath.ri.axes.SelfContext;
import org.apache.commons.jxpath.ri.compiler.CoreOperationRelationalExpression;
import org.apache.commons.jxpath.ri.compiler.CoreOperationRelationalExpression.CoreOperationRelationalExpressionFactory;
import org.apache.commons.jxpath.ri.compiler.CoreOperationRelationalExpression.CoreOperationRelationalExpressionWithStrings;
import org.apache.commons.jxpath.ri.compiler.CoreOperationRelationalExpression.CoreOperationRelationalExpressionWithNumbers;
import org.junit.Test;

/**
 * Test cases for CoreOperationRelationalExpression class.
 *
 * @author <a href="mailto:xiaojun.xj@qq.com">Xiaojun Xu</a>
 * @version 1.0
 */
public class CoreOperationRelationalExpressionTest {

    /**
     * Test constructor of CoreOperationRelationalExpression.
     */
    @Test
    public void testConstructor() {
        Expression[] args = new Expression[2];
        CoreOperationRelationalExpression expr = new CoreOperationRelationalExpression(args);
        assertNotNull(expr);
    }

    /**
     * Test computeValue method with boolean operands.
     */
    @Test
    public void testComputeValueWithBooleanOperands() {
        CoreOperationRelationalExpression expr = new CoreOperationRelationalExpressionWithStrings(
                new StringExpression("true"), new StringExpression("false"));
        EvalContext context = new EvalContext();
        Object result = expr.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.FALSE, result);

        expr = new CoreOperationRelationalExpressionWithStrings(
                new StringExpression("false"), new StringExpression("true"));
        result = expr.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.FALSE, result);

        expr = new CoreOperationRelationalExpressionWithStrings(
                new StringExpression("true"), new StringExpression("true"));
        result = expr.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.TRUE, result);
    }

    /**
     * Test computeValue method with int operands.
     */
    @Test
    public void testComputeValueWithIntOperands() {
        CoreOperationRelationalExpression expr = new CoreOperationRelationalExpressionWithNumbers(
                new NumberExpression(1), new NumberExpression(2));
        EvalContext context = new EvalContext();
        Object result = expr.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertFalse(result);

        expr = new CoreOperationRelationalExpressionWithNumbers(
                new NumberExpression(2), new NumberExpression(1));
        result = expr.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertFalse(result);

        expr = new CoreOperationRelationalExpressionWithNumbers(
                new NumberExpression(1), new NumberExpression(1));
        result = expr.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertTrue(result);

        expr = new CoreOperationRelationalExpressionWithNumbers(
                new NumberExpression(2), new NumberExpression(2));
        result = expr.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertTrue(result);
    }

    /**
     * Test computeValue method with double operands.
     */
    @Test
    public void testComputeValueWithDoubleOperands() {
        CoreOperationRelationalExpression expr = new CoreOperationRelationalExpressionWithNumbers(
                new NumberExpression(1.0), new NumberExpression(2.0));
        EvalContext context = new EvalContext();
        Object result = expr.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertFalse(result);

        expr = new CoreOperationRelationalExpressionWithNumbers(
                new NumberExpression(2.0), new NumberExpression(1.0));
        result = expr.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertFalse(result);

        expr = new CoreOperationRelationalExpressionWithNumbers(
                new NumberExpression(1.0), new NumberExpression(1.0));
        result = expr.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertTrue(result);

        expr = new CoreOperationRelationalExpressionWithNumbers(
                new NumberExpression(2.0), new NumberExpression(2.0));
        result = expr.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertTrue(result);
    }

    /**
     * Test computeValue method with Iterator operands.
     */
    @Test
    public void testComputeValueWithIteratorOperands() {
        List<String> left = new ArrayList<>(Arrays.asList("1", "2", "3"));
        List<String> right = new ArrayList<>(Arrays.asList("1", "2"));

        CoreOperationRelationalExpression expr = new CoreOperationRelationalExpression(
                new IteratorExpression(left.iterator()),
                new IteratorExpression(right.iterator()));
        EvalContext context = new EvalContext();
        Object result = expr.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertFalse(result);

        left = new ArrayList<>(Arrays.asList("1", "2", "3"));
        right = new ArrayList<>(Arrays.asList("3", "2", "1"));

        expr = new CoreOperationRelationalExpression(
                new IteratorExpression(left.iterator()),
                new IteratorExpression(right.iterator()));
        result = expr.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertFalse(result);

        left = new ArrayList<>(Arrays.asList("1", "2", "3"));
        right = new ArrayList<>(Arrays.asList("1", "2", "3"));

        expr = new CoreOperationRelationalExpression(
                new IteratorExpression(left.iterator()),
                new IteratorExpression(right.iterator()));
        result = expr.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertTrue(result);
    }

    /**
     * Test computeValue method with InitialContext operands.
     */
    @Test
    public void testComputeValueWithInitialContextOperands() {
        InitialContext left = new InitialContext();
        InitialContext right = new InitialContext();

        CoreOperationRelationalExpression expr = new CoreOperationRelationalExpression(
                new InitialContextExpression(left), new InitialContextExpression(right));
        EvalContext context = new EvalContext();
        Object result = expr.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertFalse(result);

        left.setNode("1");
        right.setNode("2");

        result = expr.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertFalse(result);

        left.setNode("1");
        right.setNode("1");

        result = expr.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertTrue(result);
    }

    /**
     * Test getPrecedence method.
     */
    @Test
    public void testGetPrecedence() {
        CoreOperationRelationalExpression expr = new CoreOperationRelationalExpressionWithStrings(
                new StringExpression("1"), new StringExpression("2"));
        int precedence = expr.getPrecedence();
        assertEquals(RELATIONAL_EXPR_PRECEDENCE, precedence);
    }

    /**
     * Test isSymmetric method.
     */
    @Test
    public void testIsSymmetric() {
        CoreOperationRelationalExpression expr = new CoreOperationRelationalExpressionWithStrings(
                new StringExpression("1"), new StringExpression("2"));
        assertFalse(expr.isSymmetric());
    }

    /**
     * Test evaluateCompare method with int compare result.
     */
    @Test
    public void testEvaluateCompareWithIntCompareResult() {
        CoreOperationRelationalExpression expr = new CoreOperationRelationalExpressionWithNumbers(
                new NumberExpression(1), new NumberExpression(2));
        boolean result = expr.evaluateCompare(-1);
        assertFalse(result);

        result = expr.evaluateCompare(0);
        assertFalse(result);

        result = expr.evaluateCompare(1);
        assertFalse(result);

        result = expr.evaluateCompare(Integer.MIN_VALUE);
        assertFalse(result);

        result = expr.evaluateCompare(Integer.MAX_VALUE);
        assertFalse(result);

        result = expr.evaluateCompare(100);
        assertFalse(result);

        result = expr.evaluateCompare(Integer.MAX_VALUE - 1);
        assertTrue(result);
    }

    /**
     * Test compute method with null operands.
     */
    @Test
    public void testComputeWithNullOperands() {
        CoreOperationRelationalExpression expr = new CoreOperationRelationalExpression(
                null, null);
        Object left = null;
        Object right = null;

        expr.compute(left, right);
        assertNull(left);
        assertNull(right);
    }

    /**
     * Test compute method with Iterator operands.
     */
    @Test
    public void testComputeWithIteratorOperands() {
        List<String> left = new ArrayList<>(Arrays.asList("1", "2", "3"));
        List<String> right = new ArrayList<>(Arrays.asList("1", "2"));

        CoreOperationRelationalExpression expr = new CoreOperationRelationalExpression(
                new IteratorExpression(left.iterator()),
                new IteratorExpression(right.iterator()));
        Object leftObject = new SelfContext();
        Object rightObject = new SelfContext();

        boolean result = expr.compute(leftObject, rightObject);
        assertFalse(result);

        leftObject = new IteratorExpression(left.iterator());
        rightObject = new IteratorExpression(right.iterator());

        result = expr.compute(leftObject, rightObject);
        assertFalse(result);

        left = new ArrayList<>(Arrays.asList("1", "2", "3"));
        right = new ArrayList<>(Arrays.asList("3", "2", "1"));

        leftObject = new IteratorExpression(left.iterator());
        rightObject = new IteratorExpression(right.iterator());

        result = expr.compute(leftObject, rightObject);
        assertFalse(result);

        left = new ArrayList<>(Arrays.asList("1", "2", "3"));
        right = new ArrayList<>(Arrays.asList("1", "2", "3"));

        leftObject = new IteratorExpression(left.iterator());
        rightObject = new IteratorExpression(right.iterator());

        result = expr.compute(leftObject, rightObject);
        assertTrue(result);
    }

    /**
     * Test compute method with InitialContext operands.
     */
    @Test
    public void testComputeWithInitialContextOperands() {
        InitialContext left = new InitialContext();
        InitialContext right = new InitialContext();

        CoreOperationRelationalExpression expr = new CoreOperationRelationalExpression(
                new InitialContextExpression(left), new InitialContextExpression(right));
        Object leftObject = new SelfContext();
        Object rightObject = new SelfContext();

        boolean result = expr.compute(leftObject, rightObject);
        assertFalse(result);

        left.setNode("1");
        right.setNode("2");

        leftObject = new InitialContextExpression(left);
        rightObject = new InitialContextExpression(right);

        result = expr.compute(leftObject, rightObject);
        assertFalse(result);

        left.setNode("1");
        right.setNode("1");

        leftObject = new InitialContextExpression(left);
        rightObject = new InitialContextExpression(right);

        result = expr.compute(leftObject, rightObject);
        assertTrue(result);
    }

    /**
     * Test compute method with Iterator and InitialContext operands.
     */
    @Test
    public void testComputeWithIteratorAndInitialContextOperands() {
        List<String> left = new ArrayList<>(Arrays.asList("1", "2", "3"));
        InitialContext right = new InitialContext();

        CoreOperationRelationalExpression expr = new CoreOperationRelationalExpression(
                new IteratorExpression(left.iterator()),
                new InitialContextExpression(right));
        Object leftObject = new SelfContext();
        Object rightObject = new SelfContext();

        boolean result = expr.compute(leftObject, rightObject);
        assertFalse(result);

        leftObject = new IteratorExpression(left.iterator());
        rightObject = new InitialContextExpression(right);

        result = expr.compute(leftObject, rightObject);
        assertFalse(result);

        left = new ArrayList<>(Arrays.asList("1", "2", "3"));
        right.setNode("1");

        leftObject = new IteratorExpression(left.iterator());
        rightObject = new InitialContextExpression(right);

        result = expr.compute(leftObject, rightObject);
        assertTrue(result);
    }

    /**
     * Test findMatch method with Iterator operands.
     */
    @Test
    public void testFindMatchWithIteratorOperands() {
        List<String> left = new ArrayList<>(Arrays.asList("1", "2", "3"));
        List<String> right = new ArrayList<>(Arrays.asList("1", "2"));

        boolean result = CoreOperationRelationalExpressionFactory.findMatch(
                left.iterator(), right.iterator());
        assertFalse(result);

        left = new ArrayList<>(Arrays.asList("1", "2", "3"));
        right = new ArrayList<>(Collections.singletonList("1"));

        result = CoreOperationRelationalExpressionFactory.findMatch(
                left.iterator(), right.iterator());
        assertFalse(result);

        left = new ArrayList<>(Arrays.asList("1", "2", "3"));
        right = new ArrayList<>(Arrays.asList("1", "2", "3"));

        result = CoreOperationRelationalExpressionFactory.findMatch(
                left.iterator(), right.iterator());
        assertTrue(result);
    }

    /**
     * Test containsMatch method with Iterator operands.
     */
    @Test
    public void testContainsMatchWithIteratorOperands() {
        List<String> left = new ArrayList<>(Arrays.asList("1", "2", "3"));
        String right = "1";

        boolean result = CoreOperationRelationalExpressionFactory.containsMatch(
                left.iterator(), right);
        assertFalse(result);

        left = new ArrayList<>(Arrays.asList("1", "2", "3"));
        right = "2";

        result = CoreOperationRelationalExpressionFactory.containsMatch(
                left.iterator(), right);
        assertFalse(result);

        left = new ArrayList<>(Arrays.asList("1", "2", "3"));
        right = "3";

        result = CoreOperationRelationalExpressionFactory.containsMatch(
                left.iterator(), right);
        assertTrue(result);
    }

    /**
     * Test compute method with null left operand.
     */
    @Test(expected = NullPointerException.class)
    public void testComputeWithNullLeftOperand() {
        Object right = new SelfContext();
        CoreOperationRelationalExpression expr = new CoreOperationRelationalExpression(
                null, new IteratorExpression(Collections.emptyIterator().iterator()));
        expr.compute(null, right);
    }

    /**
     * Test compute method with null right operand.
     */
    @Test(expected = NullPointerException.class)
    public void testComputeWithNullRightOperand() {
        Object left = new SelfContext();
        CoreOperationRelationalExpression expr = new CoreOperationRelationalExpression(
                new IteratorExpression(Collections.emptyIterator().iterator()), null);
        expr.compute(left, null);
    }

}