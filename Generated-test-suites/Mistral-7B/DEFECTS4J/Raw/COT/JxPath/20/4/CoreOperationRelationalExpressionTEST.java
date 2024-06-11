package org.apache.commons.jxpath.ri.compiler;

import static org.junit.Assert.*;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InfoSetUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

@RunWith(MockitoJUnitRunner.class)
public class CoreOperationRelationalExpressionTest {

    @Test
    public void testComputeValue_whenLeftAndRightAreBothBooleanTrue_thenReturnTrue() {
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(
                new CoreBooleanExpression(true), new CoreBooleanExpression(true));
        EvalContext context = Mockito.mock(EvalContext.class);
        Object result = expression.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.TRUE, result);
    }

    @Test
    public void testComputeValue_whenLeftAndRightAreBothBooleanFalse_thenReturnFalse() {
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(
                new CoreBooleanExpression(false), new CoreBooleanExpression(false));
        EvalContext context = Mockito.mock(EvalContext.class);
        Object result = expression.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.FALSE, result);
    }

    @Test
    public void testComputeValue_whenLeftIsBooleanTrueAndRightIsNumber_thenThrowIllegalArgumentException() {
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(
                new CoreBooleanExpression(true), new CoreNumberExpression(5));
        EvalContext context = Mockito.mock(EvalContext.class);
        try {
            expression.computeValue(context);
            fail();
        } catch (IllegalArgumentException e) {
            // Expected exception
        }
    }

    @Test
    public void testComputeValue_whenLeftIsNumberAndRightIsBooleanTrue_thenThrowIllegalArgumentException() {
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(
                new CoreNumberExpression(5), new CoreBooleanExpression(true));
        EvalContext context = Mockito.mock(EvalContext.class);
        try {
            expression.computeValue(context);
            fail();
        } catch (IllegalArgumentException e) {
            // Expected exception
        }
    }

    @Test
    public void testComputeValue_whenLeftIsNumberAndRightIsNumber_thenCallEvaluateCompare() {
        CoreOperationRelationalExpression expression = new CoreNumberComparisonExpression();
        EvalContext context = Mockito.mock(EvalContext.class);
        Object left = new Double(3.0);
        Object right = new Double(5.0);
        Mockito.when(context.getInfoSet()).thenReturn(Mockito.mock(org.apache.commons.jxpath.ri.InfoSet.class));
        Mockito.when(context.getInfoSet().getValue(Mockito.anyInt())).thenAnswer(i -> InfoSetUtil.doubleValue(left));
        Mockito.when(context.getInfoSet().getValue(Mockito.anyInt())).thenAnswer(i -> InfoSetUtil.doubleValue(right));
        Mockito.when(context.getInfoSet().size()).thenReturn(2);

        assertTrue(expression instanceof CoreNumberComparisonExpression);
        boolean result = ((CoreNumberComparisonExpression) expression).evaluateCompare(
                ((CoreNumberComparisonExpression) expression).compare(left, right));
        assertTrue(result);
    }

    @Test
    public void testComputeValue_whenLeftIsNumberAndRightIsNumberWithDifferentValues_thenCallEvaluateCompareWithCompareResultLessThanZero() {
        CoreOperationRelationalExpression expression = new CoreNumberComparisonExpression();
        EvalContext context = Mockito.mock(EvalContext.class);
        Object left = new Double(3.0);
        Object right = new Double(5.0);

        assertTrue(expression instanceof CoreNumberComparisonExpression);
        boolean result = ((CoreNumberComparisonExpression) expression).evaluateCompare(
                ((CoreNumberComparisonExpression) expression).compare(left, right));
        assertFalse(result);
    }

    @Test
    public void testComputeValue_whenLeftIsNumberAndRightIsNull_thenThrowNullPointerException() {
        CoreOperationRelationalExpression expression = new CoreNumberComparisonExpression();
        EvalContext context = Mockito.mock(EvalContext.class);
        Object left = new Double(3.0);
        Mockito.when(context.getInfoSet()).thenReturn(Mockito.mock(org.apache.commons.jxpath.ri.InfoSet.class));
        Mockito.when(context.getInfoSet().getValue(Mockito.anyInt())).thenAnswer(i -> InfoSetUtil.doubleValue(left));
        Mockito.when(context.getInfoSet().getValue(Mockito.anyInt())).thenReturn(null);

        try {
            expression.computeValue(context);
            fail();
        } catch (NullPointerException e) {
            // Expected exception
        }
    }

    @Test
    public void testComputeValue_whenLeftIsCollectionAndRightIsBooleanTrue_thenCallContainsMatch() {
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(
                new CoreCollectionExpression(new ArrayList<Object>() {{
                    add(new Double(3.0));
                    add(new Double(4.0));
                }}), new CoreBooleanExpression(true));
        EvalContext context = Mockito.mock(EvalContext.class);
        Object result = expression.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.TRUE, result);
    }

    @Test
    public void testComputeValue_whenLeftIsCollectionAndRightIsBooleanFalse_thenCallContainsMatchWithIteratorsNotMatching() {
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(
                new CoreCollectionExpression(new ArrayList<Object>() {{
                    add(new Double(3.0));
                    add(new Double(4.0));
                }}), new CoreBooleanExpression(false));
        EvalContext context = Mockito.mock(EvalContext.class);
        Iterator leftIterator = Mockito.mock(Iterator.class);
        Iterator rightIterator = Mockito.mock(Iterator.class);
        Mockito.when(context.getInfoSet()).thenReturn(Mockito.mock(org.apache.commons.jxpath.ri.InfoSet.class));
        Mockito.when(context.getInfoSet().iterator()).thenReturn(leftIterator);
        Mockito.when(context.getInfoSet()).thenReturn(context.getInfoSet()); // Reuse the same InfoSet for right
        Mockito.when(context.getInfoSet().iterator()).thenReturn(rightIterator);

        Mockito.when(leftIterator.hasNext()).thenReturn(true).thenReturn(false);
        Mockito.when(rightIterator.hasNext()).thenReturn(true).thenReturn(false);
        Mockito.when(leftIterator.next()).thenReturn(new Double(3.0));
        Mockito.when(rightIterator.next()).thenReturn(new Double(4.0));

        assertFalse(expression.compute(leftIterator, rightIterator));
    }

    @Test
    public void testComputeValue_whenLeftIsCollectionAndRightIsBooleanTrueWithIteratorsMatching_thenCallContainsMatch() {
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(
                new CoreCollectionExpression(new ArrayList<Object>() {{
                    add(new Double(3.0));
                    add(new Double(4.0));
                }}), new CoreBooleanExpression(true));
        EvalContext context = Mockito.mock(EvalContext.class);
        Iterator leftIterator = Mockito.mock(Iterator.class);
        Iterator rightIterator = Mockito.mock(Iterator.class);
        Mockito.when(context.getInfoSet()).thenReturn(Mockito.mock(org.apache.commons.jxpath.ri.InfoSet.class));
        Mockito.when(context.getInfoSet().iterator()).thenReturn(leftIterator);
        Mockito.when(context.getInfoSet()).thenReturn(context.getInfoSet()); // Reuse the same InfoSet for right
        Mockito.when(context.getInfoSet().iterator()).thenReturn(rightIterator);

        Mockito.when(leftIterator.hasNext()).thenReturn(true).thenReturn(false);
        Mockito.when(rightIterator.hasNext()).thenReturn(true).thenReturn(true);
        Mockito.when(leftIterator.next()).thenReturn(new Double(3.0));
        Mockito.when(rightIterator.next()).thenReturn(new Double(3.0));

        assertTrue(expression.compute(leftIterator, rightIterator));
    }

    @Test
    public void testComputeValue_whenLeftIsCollectionAndRightIsCollection_thenCallFindMatch() {
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(
                new CoreCollectionExpression(new ArrayList<Object>() {{
                    add(new Double(3.0));
                    add(new Double(4.0));
                }}), new CoreCollectionExpression(new ArrayList<Object>() {{
                    add(new Double(3.0));
                    add(new Double(3.0));
                }}));
        EvalContext context = Mockito.mock(EvalContext.class);
        Iterator leftIterator = Mockito.mock(Iterator.class);
        Iterator rightIterator = Mockito.mock(Iterator.class);
        Mockito.when(context.getInfoSet()).thenReturn(Mockito.mock(org.apache.commons.jxpath.ri.InfoSet.class));
        Mockito.when(context.getInfoSet().iterator()).thenReturn(leftIterator);
        Mockito.when(context.getInfoSet()).thenReturn(context.getInfoSet()); // Reuse the same InfoSet for both
        Mockito.when(context.getInfoSet().iterator()).thenReturn(rightIterator);

        Mockito.when(leftIterator.hasNext()).thenReturn(true).thenReturn(false);
        Mockito.when(rightIterator.hasNext()).thenReturn(true).thenReturn(true);
        Mockito.when(leftIterator.next()).thenReturn(new Double(3.0));
        Mockito.when(rightIterator.next()).thenReturn(new Double(3.0));

        assertTrue(expression.compute(leftIterator, rightIterator));
    }

    @Test
    public void testComputeValue_whenLeftIsCollectionAndRightIsCollectionWithEmptyCollections_thenReturnFalse() {
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(
                new CoreCollectionExpression(new ArrayList<Object>() {{}}), new CoreCollectionExpression(new ArrayList<Object>() {{}}));
        EvalContext context = Mockito.mock(EvalContext.class);
        Iterator leftIterator = Mockito.mock(Iterator.class);
        Iterator rightIterator = Mockito.mock(Iterator.class);
        Mockito.when(context.getInfoSet()).thenReturn(Mockito.mock(org.apache.commons.jxpath.ri.InfoSet.class));
        Mockito.when(context.getInfoSet().iterator()).thenReturn(leftIterator);
        Mockito.when(context.getInfoSet()).thenReturn(context.getInfoSet()); // Reuse the same InfoSet for both
        Mockito.when(context.getInfoSet().iterator()).thenReturn(rightIterator);

        Mockito.when(leftIterator.hasNext()).thenReturn(false);
        Mockito.when(rightIterator.hasNext()).thenReturn(false);

        assertFalse(expression.compute(leftIterator, rightIterator));
    }

    @Test
    public void testComputeValue_whenLeftIsCollectionAndRightIsNull_thenThrowNullPointerException() {
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(
                new CoreCollectionExpression(new ArrayList<Object>() {{}}), null);
        EvalContext context = Mockito.mock(EvalContext.class);

        try {
            expression.computeValue(context);
            fail();
        } catch (NullPointerException e) {
            // Expected exception
        }
    }

}