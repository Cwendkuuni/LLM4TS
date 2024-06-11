package org.apache.commons.jxpath.ri.compiler.test;

import static org.junit.Assert.*;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InfoSetUtil;
import org.apache.commons.jxpath.ri.axes.InitialContext;
import org.junit.Test;

import org.apache.commons.jxpath.ri.compiler.CoreOperationRelationalExpression;
import org.apache.commons.jxpath.ri.compiler.CoreOperationRelationalExpression.CoreOperationGreaterThan;
import org.apache.commons.jxpath.ri.compiler.CoreOperationRelationalExpression.CoreOperationGreaterThanOrEqual;
import org.apache.commons.jxpath.ri.compiler.CoreOperationRelationalExpression.CoreOperationLessThan;
import org.apache.commons.jxpath.ri.compiler.CoreOperationRelationalExpression.CoreOperationLessThanOrEqual;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Set;

/**
 * Test file for CoreOperationRelationalExpression class.
 *
 * @author zjw
 */
public class CoreOperationRelationalExpressionTest {

    @Test
    public void testCoreOperationGreaterThan() {
        CoreOperationGreaterThan greaterThan = new CoreOperationGreaterThan(new Expression[]{new ConstantExpression(5), new ConstantExpression(3)});
        EvalContext context = new EvalContext(new InitialContext(), new Object[]{1, 2, 3, 4, 5, 6});
        Object result = greaterThan.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result);
    }

    @Test
    public void testCoreOperationGreaterThanFalse() {
        CoreOperationGreaterThan greaterThan = new CoreOperationGreaterThan(new Expression[]{new ConstantExpression(3), new ConstantExpression(5)});
        EvalContext context = new EvalContext(new InitialContext(), new Object[]{1, 2, 3, 4, 5, 6});
        Object result = greaterThan.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertFalse((Boolean) result);
    }

    @Test
    public void testCoreOperationGreaterThanOrEqual() {
        CoreOperationGreaterThanOrEqual greaterThanOrEqual = new CoreOperationGreaterThanOrEqual(new Expression[]{new ConstantExpression(5), new ConstantExpression(3)});
        EvalContext context = new EvalContext(new InitialContext(), new Object[]{1, 2, 3, 4, 5, 6});
        Object result = greaterThanOrEqual.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result);
    }

    @Test
    public void testCoreOperationGreaterThanOrEqualTrue() {
        CoreOperationGreaterThanOrEqual greaterThanOrEqual = new CoreOperationGreaterThanOrEqual(new Expression[]{new ConstantExpression(3), new ConstantExpression(5)});
        EvalContext context = new EvalContext(new InitialContext(), new Object[]{1, 2, 3, 4, 5, 6});
        Object result = greaterThanOrEqual.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertFalse((Boolean) result);
        assertTrue(greaterThanOrEqual.isSymmetric() == false);
    }

    @Test
    public void testCoreOperationLessThan() {
        CoreOperationLessThan lessThan = new CoreOperationLessThan(new Expression[]{new ConstantExpression(3), new ConstantExpression(5)});
        EvalContext context = new EvalContext(new InitialContext(), new Object[]{1, 2, 3, 4, 5, 6});
        Object result = lessThan.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertFalse((Boolean) result);
    }

    @Test
    public void testCoreOperationLessThanTrue() {
        CoreOperationLessThan lessThan = new CoreOperationLessThan(new Expression[]{new ConstantExpression(5), new ConstantExpression(3)});
        EvalContext context = new EvalContext(new InitialContext(), new Object[]{1, 2, 3, 4, 5, 6});
        Object result = lessThan.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertFalse((Boolean) result);
        assertTrue(lessThan.isSymmetric() == false);
    }

    @Test
    public void testCoreOperationLessThanOrEqual() {
        CoreOperationLessThanOrEqual lessThanOrEqual = new CoreOperationLessThanOrEqual(new Expression[]{new ConstantExpression(3), new ConstantExpression(5)});
        EvalContext context = new EvalContext(new InitialContext(), new Object[]{1, 2, 3, 4, 5, 6});
        Object result = lessThanOrEqual.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertFalse((Boolean) result);
        assertTrue(lessThanOrEqual.isSymmetric() == false);
    }

    @Test
    public void testCoreOperationLessThanOrEqualTrue() {
        CoreOperationLessThanOrEqual lessThanOrEqual = new CoreOperationLessThanOrEqual(new Expression[]{new ConstantExpression(5), new ConstantExpression(3)});
        EvalContext context = new EvalContext(new InitialContext(), new Object[]{1, 2, 3, 4, 5, 6});
        Object result = lessThanOrEqual.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result);
    }

    @Test
    public void testDoubleComparison() {
        CoreOperationGreaterThan greaterThan = new CoreOperationGreaterThan(new Expression[]{new ConstantExpression(5.1), new ConstantExpression(3.1)});
        EvalContext context = new EvalContext(new InitialContext(), new Object[]{1.5, 2.2, 3.1, 4.5, 5.1, 6.2});
        Object result = greaterThan.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result);
    }

    @Test
    public void testDoubleComparisonFalse() {
        CoreOperationGreaterThan greaterThan = new CoreOperationGreaterThan(new Expression[]{new ConstantExpression(3.1), new ConstantExpression(5.1)});
        EvalContext context = new EvalContext(new InitialContext(), new Object[]{1.5, 2.2, 3.1, 4.5, 5.1, 6.2});
        Object result = greaterThan.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertFalse((Boolean) result);
    }

    @Test
    public void testDoubleComparisonGreaterThanOrEqual() {
        CoreOperationGreaterThanOrEqual greaterThanOrEqual = new CoreOperationGreaterThanOrEqual(new Expression[]{new ConstantExpression(5.1), new ConstantExpression(3.1)});
        EvalContext context = new EvalContext(new InitialContext(), new Object[]{1.5, 2.2, 3.1, 4.5, 5.1, 6.2});
        Object result = greaterThanOrEqual.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result);
        assertTrue(greaterThanOrEqual.isSymmetric() == false);
    }

    @Test
    public void testDoubleComparisonLessThan() {
        CoreOperationLessThan lessThan = new CoreOperationLessThan(new Expression[]{new ConstantExpression(3.1), new ConstantExpression(5.1)});
        EvalContext context = new EvalContext(new InitialContext(), new Object[]{1.5, 2.2, 3.1, 4.5, 5.1, 6.2});
        Object result = lessThan.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertFalse((Boolean) result);
        assertTrue(lessThan.isSymmetric() == false);
    }

    @Test
    public void testDoubleComparisonLessThanOrEqual() {
        CoreOperationLessThanOrEqual lessThanOrEqual = new CoreOperationLessThanOrEqual(new Expression[]{new ConstantExpression(5.1), new ConstantExpression(3.1)});
        EvalContext context = new EvalContext(new InitialContext(), new Object[]{1.5, 2.2, 3.1, 4.5, 5.1, 6.2});
        Object result = lessThanOrEqual.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertFalse((Boolean) result);
        assertTrue(lessThanOrEqual.isSymmetric() == false);
    }

    @Test
    public void testDoubleComparisonEqual() {
        CoreOperationLessThanOrEqual lessThanOrEqual = new CoreOperationLessThanOrEqual(new Expression[]{new ConstantExpression(3.1), new ConstantExpression(3.1)});
        EvalContext context = new EvalContext(new InitialContext(), new Object[]{1.5, 2.2, 3.1, 4.5, 5.1, 6.2});
        Object result = lessThanOrEqual.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result);
        assertTrue(lessThanOrEqual.isSymmetric() == true);
    }

    @Test
    public void testIteratorsComparison() {
        List<Object> list1 = new ArrayList<>();
        list1.add(1);
        list1.add(2);
        list1.add(3);
        List<Object> list2 = new ArrayList<>();
        list2.add(3);
        list2.add(2);
        list2.add(1);
        CoreOperationGreaterThan greaterThan = new CoreOperationGreaterThan(new Expression[]{new IteratorExpression(list1.iterator()), new IteratorExpression(list2.iterator())});
        EvalContext context = new EvalContext(new InitialContext(), new Object[]{list1, list2});
        Object result = greaterThan.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertFalse((Boolean) result);
        assertTrue(greaterThan.isSymmetric() == false);

        CoreOperationGreaterThanOrEqual greaterThanOrEqual = new CoreOperationGreaterThanOrEqual(new Expression[]{new IteratorExpression(list1.iterator()), new IteratorExpression(list2.iterator())});
        Object result1 = greaterThanOrEqual.computeValue(context);
        assertTrue(result1 instanceof Boolean);
        assertFalse((Boolean) result1);

        Collections.sort(list1, new Comparator<Object>() {
            @Override
            public int compare(Object o1, Object o2) {
                return ((Integer) o1).compareTo((Integer) o2);
            }
        });
        Collections.sort(list2, new Comparator<Object>() {
            @Override
            public int compare(Object o1, Object o2) {
                return ((Integer) o1).compareTo((Integer) o2);
            }
        });

        Object result2 = greaterThanOrEqual.computeValue(context);
        assertTrue(result2 instanceof Boolean);
        assertTrue((Boolean) result2);
    }
}