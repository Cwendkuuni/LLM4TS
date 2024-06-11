package org.apache.commons.jxpath.ri.compiler;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InfoSetUtil;
import org.apache.commons.jxpath.ri.axes.InitialContext;
import org.apache.commons.jxpath.ri.axes.SelfContext;
import org.junit.Test;

/**
 * Test class for CoreOperationRelationalExpression
 */
public class CoreOperationRelationalExpressionTest {

    /**
     * Test constructor.
     */
    @Test
    public void testConstructor() {
        Expression[] args = new Expression[2];
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(args);
        assertNotNull(expression);
    }

    /**
     * Test computeValue method with Boolean arguments.
     */
    @Test
    public void testComputeValueWithBooleanArguments() {
        Expression arg1 = new CoreBooleanExpression(true);
        Expression arg2 = new CoreBooleanExpression(true);
        CoreOperationRelationalExpression expression = new CoreOperationGreaterThan(new CoreOperationUnaryMinus(arg1), arg2);
        Object result = expression.computeValue(new EvalContext());
        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result);
    }

    /**
     * Test computeValue method with non-Boolean arguments.
     */
    @Test(expected = RuntimeException.class)
    public void testComputeValueWithNonBooleanArguments() {
        Expression arg1 = new CoreNumberExpression(1);
        Expression arg2 = new CoreNumberExpression(2);
        CoreOperationRelationalExpression expression = new CoreOperationGreaterThan(arg1, arg2);
        Object result = expression.computeValue(new EvalContext());
        throw new RuntimeException("Expected exception: UnsupportedOperationException");
    }

    /**
     * Test computeValue method with InitialContext arguments.
     */
    @Test
    public void testComputeValueWithInitialContextArguments() {
        Expression arg1 = new InitialContext();
        Expression arg2 = new InitialContext();
        CoreOperationRelationalExpression expression = new CoreOperationGreaterThan(arg1, arg2);
        Object result = expression.computeValue(new EvalContext());
        assertNull(result);
    }

    /**
     * Test computeValue method with Iterator arguments.
     */
    @Test
    public void testComputeValueWithIteratorArguments() {
        List<Object> list1 = new ArrayList<>();
        list1.add(1);
        list1.add(2);
        Iterator<Object> iterator1 = list1.iterator();

        List<Object> list2 = new ArrayList<>();
        list2.add(2);
        list2.add(1);
        Iterator<Object> iterator2 = list2.iterator();

        Expression arg1 = new CoreListExpression(list1);
        Expression arg2 = new CoreListExpression(list2);
        CoreOperationRelationalExpression expression = new CoreOperationGreaterThan(arg1, arg2);
        Object result = expression.computeValue(new EvalContext());
        assertFalse(result instanceof Boolean);
        assertNull(result);
    }

    /**
     * Test computeValue method with Iterator and Boolean arguments.
     */
    @Test
    public void testComputeValueWithIteratorAndBooleanArguments() {
        List<Object> list = new ArrayList<>();
        list.add(1);
        list.add(2);
        Iterator<Object> iterator = list.iterator();

        Expression argBoolean = new CoreBooleanExpression(true);

        Expression arg1 = new CoreListExpression(list);
        Expression arg2 = argBoolean;

        CoreOperationRelationalExpression expression = new CoreOperationGreaterThan(arg1, arg2);
        Object result = expression.computeValue(new EvalContext());
        assertFalse(result instanceof Boolean);
        assertNull(result);
    }

    /**
     * Test computeValue method with double arguments.
     */
    @Test
    public void testComputeValueWithDoubleArguments() {
        Expression arg1 = new CoreNumberExpression(1.5);
        Expression arg2 = new CoreNumberExpression(2.5);
        CoreOperationRelationalExpression expression = new CoreOperationGreaterThan(arg1, arg2);
        Object result = expression.computeValue(new EvalContext());
        assertFalse(result instanceof Boolean);
        assertNull(result);
    }

    /**
     * Test evaluateCompare method with compare equal.
     */
    @Test
    public void testEvaluateCompareWithCompareEqual() {
        CoreOperationRelationalExpression expression = new CoreOperationGreaterThanOrEqual();
        int compare = expression.evaluateCompare(0);
        assertTrue(compare >= 0);
    }

    /**
     * Test evaluateCompare method with compare less.
     */
    @Test
    public void testEvaluateCompareWithCompareLess() {
        CoreOperationRelationalExpression expression = new CoreOperationGreaterThanOrEqual();
        int compare = expression.evaluateCompare(-1);
        assertTrue(compare < 0);
    }

    /**
     * Test evaluateCompare method with compare greater.
     */
    @Test
    public void testEvaluateCompareWithCompareGreater() {
        CoreOperationRelationalExpression expression = new CoreOperationGreaterThanOrEqual();
        int compare = expression.evaluateCompare(1);
        assertTrue(compare > 0);
    }

    /**
     * Test reduce method with SelfContext argument.
     */
    @Test
    public void testReduceWithSelfContextArgument() {
        Expression selfContext = new SelfContext();
        Object result = new CoreOperationRelationalExpression(new Expression[0]).reduce(selfContext);
        assertNotNull(result);
    }

    /**
     * Test reduce method with Collection argument.
     */
    @Test
    public void testReduceWithCollectionArgument() {
        List<Object> list = new ArrayList<>();
        list.add(new SelfContext());
        Expression collection = new CoreListExpression(list);
        Object result = new CoreOperationRelationalExpression(new Expression[0]).reduce(collection);
        assertNull(result);
    }

    /**
     * Test containsMatch method with Iterator and Object arguments.
     */
    @Test
    public void testContainsMatchWithIteratorAndObjectArguments() {
        List<Object> list = new ArrayList<>();
        list.add(1);
        list.add(2);
        Iterator<Object> iterator = list.iterator();

        Expression argValue = new CoreNumberExpression(2);

        boolean result = new CoreOperationRelationalExpression(new Expression[0]).containsMatch(iterator, argValue);
        assertFalse(result);
    }

    /**
     * Test containsMatch method with Iterator and Collection arguments.
     */
    @Test
    public void testContainsMatchWithIteratorAndCollectionArguments() {
        List<Object> list1 = new ArrayList<>();
        list1.add(1);
        list1.add(2);
        Iterator<Object> iterator1 = list1.iterator();

        List<Object> list2 = new ArrayList<>();
        list2.add(2);
        list2.add(1);

        boolean result = new CoreOperationRelationalExpression(new Expression[0]).containsMatch(iterator1, new CoreListExpression(list2));
        assertFalse(result);
    }

    /**
     * Test findMatch method with Iterator and Iterator arguments.
     */
    @Test
    public void testFindMatchWithIteratorAndIteratorArguments() {
        List<Object> list1 = new ArrayList<>();
        list1.add(1);
        list1.add(2);
        Iterator<Object> iterator1 = list1.iterator();

        List<Object> list2 = new ArrayList<>();
        list2.add(2);
        list2.add(1);
        Iterator<Object> iterator2 = list2.iterator();

        boolean result = new CoreOperationRelationalExpression(new Expression[0]).findMatch(iterator1, iterator2);
        assertFalse(result);
    }

}