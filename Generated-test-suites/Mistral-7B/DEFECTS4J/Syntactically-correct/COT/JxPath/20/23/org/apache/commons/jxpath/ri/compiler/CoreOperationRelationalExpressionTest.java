package org.apache.commons.jxpath.ri.compiler;

import static org.junit.Assert.*;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InfoSetUtil;
import org.apache.commons.jxpath.ri.axes.InitialContext;
import org.apache.commons.jxpath.ri.axes.SelfContext;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

public class CoreOperationRelationalExpressionTest {

    @Test
    public void testComputeValue() {
        Object left = new Integer(5);
        Object right = new Integer(10);
        CoreOperationRelationalExpression expression = new CoreOperationGreaterThan(new CoreExpressionNumber(left), new CoreExpressionNumber(right));
        EvalContext context = new EvalContext(new InitialContext());
        Object result = expression.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertFalse((Boolean) result);

        left = new Double(3.14);
        right = new Double(3.14);
        expression = new CoreOperationGreaterThanOrEqual(new CoreExpressionNumber(left), new CoreExpressionNumber(right));
        result = expression.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result);

        left = new Integer(5);
        right = new Integer(5);
        expression = new CoreOperationGreaterThan(new CoreExpressionNumber(left), new CoreExpressionNumber(right));
        result = expression.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertFalse((Boolean) result);

        left = new Integer(5);
        right = new Integer(5);
        expression = new CoreOperationGreaterThanOrEqual(new CoreExpressionNumber(left), new CoreExpressionNumber(right));
        result = expression.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result);

        left = new String("5");
        right = new String("6");
        expression = new CoreOperationGreaterThan(new CoreExpressionString(left), new CoreExpressionString(right));
        result = expression.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertFalse((Boolean) result);

        left = new String("5");
        right = new String("5");
        expression = new CoreOperationGreaterThanOrEqual(new CoreExpressionString(left), new CoreExpressionString(right));
        result = expression.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testComputeValueWithNullLeftOperand() {
        CoreOperationRelationalExpression expression = new CoreOperationGreaterThan(null, new CoreExpressionNumber(1));
        EvalContext context = new EvalContext(new InitialContext());
        expression.computeValue(context);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testComputeValueWithNullRightOperand() {
        CoreOperationRelationalExpression expression = new CoreOperationGreaterThan(new CoreExpressionNumber(1), null);
        EvalContext context = new EvalContext(new InitialContext());
        expression.computeValue(context);
    }

    @Test
    public void testGetPrecedence() {
        CoreOperationRelationalExpression expression = new CoreOperationGreaterThanOrEqual(null, null);
        assertEquals(CoreOperationRelationalExpression.RELATIONAL_EXPR_PRECEDENCE, expression.getPrecedence());
    }

    @Test
    public void testIsSymmetric() {
        CoreOperationRelationalExpression expression = new CoreOperationGreaterThanOrEqual(null, null);
        assertFalse(expression.isSymmetric());
    }

    @Test
    public void testEvaluateCompare() {
        CoreOperationRelationalExpression expression = new CoreOperationGreaterThanOrEqual(null, null);
        expression.evaluateCompare(-1);
        assertTrue(expression.evaluateCompare(0));
        assertFalse(expression.evaluateCompare(1));
    }

    @Test
    public void testComputeWithNullLeftAndRightOperands() {
        CoreOperationRelationalExpression expression = new CoreOperationGreaterThanOrEqual(null, null);
        EvalContext context = new EvalContext(new InitialContext());
        Object result = expression.compute(null, null);
        assertNull(result);
    }

    @Test
    public void testComputeWithNullLeftOperand() {
        CoreOperationRelationalExpression expression = new CoreOperationGreaterThanOrEqual(null, new CoreExpressionNumber(1));
        EvalContext context = new EvalContext(new InitialContext());
        Object result = expression.compute(null, new CoreExpressionNumber(1));
        assertFalse(result instanceof Boolean);
    }

    @Test
    public void testComputeWithNullRightOperand() {
        CoreOperationRelationalExpression expression = new CoreOperationGreaterThanOrEqual(new CoreExpressionNumber(1), null);
        EvalContext context = new EvalContext(new InitialContext());
        Object result = expression.compute(new CoreExpressionNumber(1), null);
        assertFalse(result instanceof Boolean);
    }

    @Test
    public void testComputeWithIterators() {
        List list1 = Arrays.asList(new Integer(1), new Integer(2), new Integer(3));
        List list2 = Arrays.asList(new Integer(3), new Integer(2), new Integer(1));

        Iterator it1 = list1.iterator();
        Iterator it2 = list2.iterator();

        CoreOperationRelationalExpression expression = new CoreOperationGreaterThan(it1, it2);
        EvalContext context = new EvalContext(new InitialContext());
        Object result = expression.compute(it1, it2);
        assertFalse(result instanceof Boolean);

        expression = new CoreOperationGreaterThanOrEqual(it1, it2);
        result = expression.compute(it1, it2);
        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result);
    }

    @Test
    public void testComputeWithCollectionLeftOperand() {
        Collection set1 = new HashSet();
        set1.add(new Integer(1));
        set1.add(new Integer(2));
        set1.add(new Integer(3));

        CoreExpressionNumber number = new CoreExpressionNumber(3);

        CoreOperationRelationalExpression expression = new CoreOperationGreaterThan(set1.iterator(), number);
        EvalContext context = new EvalContext(new InitialContext());
        Object result = expression.compute(set1.iterator(), number);
        assertFalse(result instanceof Boolean);

        expression = new CoreOperationGreaterThanOrEqual(set1.iterator(), number);
        result = expression.compute(set1.iterator(), number);
        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result);
    }

    @Test
    public void testComputeWithCollectionRightOperand() {
        Collection set1 = new HashSet();
        set1.add(new Integer(1));
        set1.add(new Integer(2));
        set1.add(new Integer(3));

        CoreExpressionNumber number = new CoreExpressionNumber(3);

        CoreOperationRelationalExpression expression = new CoreOperationGreaterThanOrEqual(number, set1.iterator());
        EvalContext context = new EvalContext(new InitialContext());
        Object result = expression.compute(number, set1.iterator());
        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result);
    }

    @Test
    public void testComputeWithDoubles() {
        CoreOperationRelationalExpression expression = new CoreOperationGreaterThanOrEqual(new CoreExpressionNumber(3.14), new CoreExpressionNumber(3.14));
        EvalContext context = new EvalContext(new InitialContext());
        Object result = expression.compute(new CoreExpressionNumber(3.14), new CoreExpressionNumber(3.14));
        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result);

        expression = new CoreOperationGreaterThanOrEqual(new CoreExpressionNumber(3.14), new CoreExpressionNumber(2.718));
        result = expression.compute(new CoreExpressionNumber(3.14), new CoreExpressionNumber(2.718));
        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result);

        expression = new CoreOperationGreaterThan(new CoreExpressionNumber(3.14), new CoreExpressionNumber(2.718));
        result = expression.compute(new CoreExpressionNumber(3.14), new CoreExpressionNumber(2.718));
        assertFalse(result instanceof Boolean);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testComputeWithIteratorsOfDifferentSizes() {
        List list1 = Arrays.asList(new Integer(1), new Integer(2), new Integer(3));
        Iterator it2 = Arrays.asList(new Integer(3), new Integer(2), new Integer(1)).iterator();

        CoreOperationRelationalExpression expression = new CoreOperationGreaterThan(list1.iterator(), it2);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testComputeWithCollectionsOfDifferentSizes() {
        Collection set1 = new HashSet();
        set1.add(new Integer(1));
        set1.add(new Integer(2));
        set1.add(new Integer(3));

        Collection set2 = new HashSet();
        set2.add(new Integer(3));
        set2.add(new Integer(2));
        set2.add(new Integer(1));

        CoreOperationRelationalExpression expression = new CoreOperationGreaterThan(set1.iterator(), set2.iterator());
    }

    @Test
    public void testComputeWithNaNValues() {
        CoreOperationRelationalExpression expression = new CoreOperationGreaterThanOrEqual(new CoreExpressionNumber(Double.NaN), new CoreExpressionNumber(Double.NaN));
        EvalContext context = new EvalContext(new InitialContext());
        Object result = expression.compute(new CoreExpressionNumber(Double.NaN), new CoreExpressionNumber(Double.NaN));
        assertFalse(result instanceof Boolean);
    }
}