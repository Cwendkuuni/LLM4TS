package org.apache.commons.jxpath.ri.compiler;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InfoSetUtil;
import org.apache.commons.jxpath.ri.axes.InitialContext;
import org.apache.commons.jxpath.ri.axes.SelfContext;
import org.apache.commons.jxpath.ri.compiler.CoreOperationRelationalExpression;
import org.junit.Before;
import org.junit.Test;

/**
 * Test cases for CoreOperationRelationalExpression.
 */
public class CoreOperationRelationalExpressionTest {
    private Collection<Object> leftValues;
    private Collection<Object> rightValues;
    private EvalContext context;

    @Before
    public void setUp() {
        leftValues = new ArrayList<>();
        rightValues = new ArrayList<>();
        context = mock(EvalContext.class);
    }

    @Test
    public void testComputeValue_TwoInitialContexts() {
        InitialContext leftInitialContext = mock(InitialContext.class);
        InitialContext rightInitialContext = mock(InitialContext.class);

        when(leftInitialContext.getSingleNodePointer()).thenReturn("node1");
        when(rightInitialContext.getSingleNodePointer()).thenReturn("node2");

        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(
                new CoreOperation[] { new CoreConstantExpression("node1"), new CoreConstantExpression("node2") });

        Object result = expression.computeValue(context);

        assertTrue(result instanceof Boolean);
    }

    @Test
    public void testComputeValue_TwoIteratorsWithMatchingValues() {
        Object leftValue1 = mock(Object.class);
        Object leftValue2 = mock(Object.class);
        Object rightValue1 = mock(Object.class);
        Object rightValue2 = mock(Object.class);

        when(leftValues.iterator().next()).thenReturn(leftValue1);
        when(leftValues.iterator().next()).thenReturn(leftValue2);
        when(rightValues.iterator().next()).thenReturn(rightValue1);
        when(rightValues.iterator().next()).thenReturn(rightValue2);
        when(InfoSetUtil.doubleValue(leftValue1)).thenReturn(1.0);
        when(InfoSetUtil.doubleValue(leftValue2)).thenReturn(1.0);
        when(InfoSetUtil.doubleValue(rightValue1)).thenReturn(1.0);
        when(InfoSetUtil.doubleValue(rightValue2)).thenReturn(1.0);

        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(
                new CoreOperation[] { new CoreCollectionExpression(leftValues), new CoreCollectionExpression(rightValues) });

        Object result = expression.computeValue(context);

        assertTrue(result instanceof Boolean);
        assertTrue(((Boolean) result).booleanValue());
    }

    @Test
    public void testComputeValue_TwoIteratorsWithNoMatchingValues() {
        Object leftValue1 = mock(Object.class);
        Object leftValue2 = mock(Object.class);
        Object rightValue1 = mock(Object.class);
        Object rightValue2 = mock(Object.class);

        when(leftValues.iterator().next()).thenReturn(leftValue1);
        when(leftValues.iterator().next()).thenReturn(leftValue2);
        when(rightValues.iterator().next()).thenReturn(rightValue1);
        when(rightValues.iterator().next()).thenReturn(rightValue2);
        when(InfoSetUtil.doubleValue(leftValue1)).thenReturn(1.0);
        when(InfoSetUtil.doubleValue(leftValue2)).thenReturn(2.0);
        when(InfoSetUtil.doubleValue(rightValue1)).thenReturn(3.0);
        when(InfoSetUtil.doubleValue(rightValue2)).thenReturn(4.0);

        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(
                new CoreOperation[] { new CoreCollectionExpression(leftValues), new CoreCollectionExpression(rightValues) });

        Object result = expression.computeValue(context);

        assertTrue(result instanceof Boolean);
        assertFalse(((Boolean) result).booleanValue());
    }

    @Test
    public void testComputeValue_TwoIteratorsWithOneMatchingValue() {
        Object leftValue1 = mock(Object.class);
        Object leftValue2 = mock(Object.class);
        Object rightValue1 = mock(Object.class);
        Object rightValue2 = mock(Object.class);

        when(leftValues.iterator().next()).thenReturn(leftValue1);
        when(leftValues.iterator().next()).thenReturn(leftValue2);
        when(rightValues.iterator().next()).thenReturn(rightValue1);
        when(rightValues.iterator().next()).thenReturn(rightValue2);
        when(InfoSetUtil.doubleValue(leftValue1)).thenReturn(1.0);
        when(InfoSetUtil.doubleValue(leftValue2)).thenReturn(2.0);
        when(InfoSetUtil.doubleValue(rightValue1)).thenReturn(1.0);
        when(InfoSetUtil.doubleValue(rightValue2)).thenReturn(2.0);

        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(
                new CoreOperation[] { new CoreCollectionExpression(leftValues), new CoreCollectionExpression(rightValues) });

        Object result = expression.computeValue(context);

        assertTrue(result instanceof Boolean);
        assertTrue(((Boolean) result).booleanValue());
    }

    @Test
    public void testComputeValue_TwoObjectsWithEqualValues() {
        Object leftValue = mock(Object.class);
        Object rightValue = mock(Object.class);

        when(InfoSetUtil.doubleValue(leftValue)).thenReturn(1.0);
        when(InfoSetUtil.doubleValue(rightValue)).thenReturn(1.0);

        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(
                new CoreOperation[] { new CoreConstantExpression(leftValue), new CoreConstantExpression(rightValue) });

        Object result = expression.computeValue(context);

        assertTrue(result instanceof Boolean);
        assertTrue(((Boolean) result).booleanValue());
    }

    @Test
    public void testComputeValue_TwoObjectsWithUnequalValues() {
        Object leftValue = mock(Object.class);
        Object rightValue = mock(Object.class);

        when(InfoSetUtil.doubleValue(leftValue)).thenReturn(1.0);
        when(InfoSetUtil.doubleValue(rightValue)).thenReturn(2.0);

        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(
                new CoreOperation[] { new CoreConstantExpression(leftValue), new CoreConstantExpression(rightValue) });

        Object result = expression.computeValue(context);

        assertTrue(result instanceof Boolean);
        assertFalse(((Boolean) result).booleanValue());
    }

    @Test
    public void testComputeValue_TwoObjectsWithOneNullValue() {
        Object leftValue = null;
        Object rightValue = mock(Object.class);

        when(InfoSetUtil.doubleValue(rightValue)).thenReturn(1.0);

        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(
                new CoreOperation[] { new CoreConstantExpression(leftValue), new CoreConstantExpression(rightValue) });

        Object result = expression.computeValue(context);

        assertNull(result);
    }

    @Test
    public void testComputeValue_TwoObjectsWithBothNullValues() {
        Object leftValue = null;
        Object rightValue = null;

        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(
                new CoreOperation[] { new CoreConstantExpression(leftValue), new CoreConstantExpression(rightValue) });

        Object result = expression.computeValue(context);

        assertNull(result);
    }
}