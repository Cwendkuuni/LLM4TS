package org.apache.commons.jxpath.ri.compiler.test;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InfoSetUtil;
import org.apache.commons.jxpath.ri.axes.InitialContext;
import org.apache.commons.jxpath.ri.axes.SelfContext;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

@RunWith(Parameterized.class)
public class CoreOperationRelationalExpressionTest {

    private CoreOperationRelationalExpression expression;
    private Object leftValue;
    private Object rightValue;
    private Object expectedResult;
    private EvalContext context;

    public CoreOperationRelationalExpressionTest(Object leftValue, Object rightValue, Object expectedResult) {
        this.leftValue = leftValue;
        this.rightValue = rightValue;
        this.expectedResult = expectedResult;
    }

    @Parameterized.Parameters(name = "{index}: testComputeValue({0}, {1}) => {2}")
    public static Iterator<Object[]> data() {
        return new Iterator<Object[]>() {
            private final List<Object[]> data = new ArrayList<>();

            {
                data.add(new Object[]{new Integer(1), new Integer(2), Boolean.FALSE});
                data.add(new Object[]{new Integer(1), new Integer(1), Boolean.TRUE});
                data.add(new Object[]{new Integer(1), new Double("2.1"), Boolean.FALSE});
                data.add(new Object[]{new Double("2.1"), new Double("2.1"), Boolean.TRUE});
                data.add(new Object[]{new String("a"), new String("a"), Boolean.TRUE});
                data.add(new Object[]{new Integer(1), new Collection<Object>() {{ add(new Integer(2)); }}, Boolean.FALSE});
                data.add(new Object[]{new Collection<Object>() {{ add(new Integer(1)); add(new Integer(1)); }}, new Integer(1), Boolean.TRUE});
                data.add(new Object[]{new InitialContext(), new InitialContext(), Boolean.TRUE});
                data.add(new Object[]{new SelfContext(), new SelfContext(), Boolean.TRUE});
                data.add(new Object[]{new Iterator<Object>() {{ add(new Integer(1)); }}, new Integer(1), Boolean.TRUE});
                data.add(new Object[]{new Integer(1), new Iterator<Object>() {{ add(new Integer(1)); }}, Boolean.FALSE});
                data.add(new Object[]{new Double("NaN"), new Double("NaN"), Boolean.FALSE});
                data.add(new Object[]{new Double("NaN"), new Double("0.0"), Boolean.FALSE});
                data.add(new Object[]{new Double("0.0"), new Double("NaN"), Boolean.FALSE});
            }

            @Override
            public Object next() {
                return data.remove(0);
            }
        };
    }

    @Test
    public void testComputeValue() {
        context = Mockito.mock(EvalContext.class);
        expression = Mockito.spy(new CoreOperationGt(new CoreOperationConstantExpression(leftValue), new CoreOperationConstantExpression(rightValue)));
        Mockito.when(context.getSingleNodePointer()).thenReturn(leftValue);

        Object result = expression.computeValue(context);
        assertEquals(expectedResult, result);

        Mockito.verify(expression).compute(leftValue, rightValue);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testIsSymmetric() {
        expression = new CoreOperationGt(new CoreOperationConstantExpression(leftValue), new CoreOperationConstantExpression(rightValue));
        assertFalse(expression.isSymmetric());
    }

    @Test
    public void testGetPrecedence() {
        expression = new CoreOperationGt(new CoreOperationConstantExpression(leftValue), new CoreOperationConstantExpression(rightValue));
        int precedence = expression.getPrecedence();
        assertEquals(3, precedence);
    }

    @Test
    public void testFindMatch_IteratorsWithCommonElements() {
        Iterator<Object> iteratorLeft = Mockito.mock(Iterator.class);
        Iterator<Object> iteratorRight = Mockito.mock(Iterator.class);
        Mockito.when(iteratorLeft.hasNext()).thenReturn(true).thenReturn(false);
        Mockito.when(iteratorRight.hasNext()).thenReturn(true).thenReturn(true).thenReturn(true).thenReturn(false);
        Mockito.when(iteratorLeft.next()).thenReturn(new Integer(1)).thenReturn(new Integer(1)).thenReturn(new Integer(1));
        Mockito.when(iteratorRight.next()).thenReturn(new Integer(1)).thenReturn(new Integer(1)).thenReturn(new Integer(1));

        expression = new CoreOperationGt(new CoreOperationConstantExpression(iteratorLeft), new CoreOperationConstantExpression(iteratorRight));

        assertTrue(expression.evaluateCompare(-1));
    }

    @Test
    public void testFindMatch_IteratorsWithoutCommonElements() {
        Iterator<Object> iteratorLeft = Mockito.mock(Iterator.class);
        Iterator<Object> iteratorRight = Mockito.mock(Iterator.class);
        Mockito.when(iteratorLeft.hasNext()).thenReturn(true).thenReturn(false);
        Mockito.when(iteratorRight.hasNext()).thenReturn(true).thenReturn(true).thenReturn(false);
        Mockito.when(iteratorLeft.next()).thenReturn(new Integer(1));
        Mockito.when(iteratorRight.next()).thenReturn(new Integer(2));

        expression = new CoreOperationGt(new CoreOperationConstantExpression(iteratorLeft), new CoreOperationConstantExpression(iteratorRight));

        assertFalse(expression.evaluateCompare(-1));
    }

    @Test
    public void testFindMatch_IteratorsWithNoElements() {
        Iterator<Object> iteratorLeft = Mockito.mock(Iterator.class);
        Iterator<Object> iteratorRight = Mockito.mock(Iterator.class);
        Mockito.when(iteratorLeft.hasNext()).thenReturn(false);
        Mockito.when(iteratorRight.hasNext()).thenReturn(false);

        expression = new CoreOperationGt(new CoreOperationConstantExpression(iteratorLeft), new CoreOperationConstantExpression(iteratorRight));

        assertTrue(expression.evaluateCompare(0));
    }

    @Test
    public void testContainsMatch_IteratorWithCommonElements() {
        Iterator<Object> iterator = Mockito.mock(Iterator.class);
        Mockito.when(iterator.hasNext()).thenReturn(true).thenReturn(true).thenReturn(false);
        Mockito.when(iterator.next()).thenReturn(new Integer(1)).thenReturn(new Integer(1)).thenReturn(new Integer(2));

        expression = new CoreOperationGt(new CoreOperationConstantExpression(iterator), new CoreOperationConstantExpression(new Integer(1)));

        assertTrue(expression.evaluateCompare(-1));
    }

    @Test
    public void testContainsMatch_IteratorWithNoCommonElements() {
        Iterator<Object> iterator = Mockito.mock(Iterator.class);
        Mockito.when(iterator.hasNext()).thenReturn(true).thenReturn(false);
        Mockito.when(iterator.next()).thenReturn(new Integer(1));

        expression = new CoreOperationGt(new CoreOperationConstantExpression(iterator), new CoreOperationConstantExpression(new Integer(2)));

        assertFalse(expression.evaluateCompare(-1));
    }

    @Test
    public void testContainsMatch_IteratorWithSingleElement() {
        Iterator<Object> iterator = Mockito.mock(Iterator.class);
        Mockito.when(iterator.hasNext()).thenReturn(true).thenReturn(false);
        Mockito.when(iterator.next()).thenReturn(new Integer(1));

        expression = new CoreOperationGt(new CoreOperationConstantExpression(iterator), new CoreOperationConstantExpression(new Integer(1)));

        assertTrue(expression.evaluateCompare(0));
    }

    @Test
    public void testContainsMatch_CollectionWithCommonElements() {
        Collection<Object> collection = new ArrayList<>();
        collection.add(new Integer(1));
        collection.add(new Integer(1));

        expression = new CoreOperationGt(new CoreOperationConstantExpression(collection.iterator()), new CoreOperationConstantExpression(new Integer(1)));

        assertTrue(expression.evaluateCompare(-1));
    }

    @Test
    public void testContainsMatch_CollectionWithNoCommonElements() {
        Collection<Object> collection = new ArrayList<>();
        collection.add(new Integer(1));

        expression = new CoreOperationGt(new CoreOperationConstantExpression(collection.iterator()), new CoreOperationConstantExpression(new Integer(2)));

        assertFalse(expression.evaluateCompare(-1));
    }

    @Test
    public void testContainsMatch_CollectionWithSingleElement() {
        Collection<Object> collection = new ArrayList<>();
        collection.add(new Integer(1));

        expression = new CoreOperationGt(new CoreOperationConstantExpression(collection.iterator()), new CoreOperationConstantExpression(new Integer(1)));

        assertTrue(expression.evaluateCompare(0));
    }

    @Test
    public void testEvaluateCompare_DoubleEqual() {
        expression = new CoreOperationGt(new CoreOperationConstantExpression(InfoSetUtil.createDoubleValue(2.1)), new CoreOperationConstantExpression(InfoSetUtil.createDoubleValue(2.1)));

        assertTrue(expression.evaluateCompare(0));
    }

    @Test
    public void testEvaluateCompare_DoubleNotEqual() {
        expression = new CoreOperationGt(new CoreOperationConstantExpression(InfoSetUtil.createDoubleValue(2.1)), new CoreOperationConstantExpression(InfoSetUtil.createDoubleValue(2.2)));

        assertFalse(expression.evaluateCompare(-1));
    }

    @Test
    public void testEvaluateCompare_DoubleLessThan() {
        expression = new CoreOperationGt(new CoreOperationConstantExpression(InfoSetUtil.createDoubleValue(2.1)), new CoreOperationConstantExpression(InfoSetUtil.createDoubleValue(2.2)));

        assertFalse(expression.evaluateCompare(-1));
    }

    @Test
    public void testEvaluateCompare_DoubleGreaterThan() {
        expression = new CoreOperationGt(new CoreOperationConstantExpression(InfoSetUtil.createDoubleValue(2.1)), new CoreOperationConstantExpression(InfoSetUtil.createDoubleValue(2.1)));

        assertFalse(expression.evaluateCompare(-1));
    }

    @Test
    public void testEvaluateCompare_NullLeft() {
        expression = new CoreOperationGt(new CoreOperationConstantExpression(null), new CoreOperationConstantExpression(new Integer(1)));

        assertNull(expression.evaluateCompare(-1));
    }

    @Test
    public void testEvaluateCompare_NullRight() {
        expression = new CoreOperationGt(new CoreOperationConstantExpression(new Integer(1)), new CoreOperationConstantExpression(null));

        assertNull(expression.evaluateCompare(-1));
    }

    @Test
    public void testEvaluateCompare_NullBoth() {
        expression = new CoreOperationGt(new CoreOperationConstantExpression(null), new CoreOperationConstantExpression(null));

        assertNull(expression.evaluateCompare(-1));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testEvaluateCompare_NAnalogousValues() {
        expression = new CoreOperationGt(new CoreOperationConstantExpression(InfoSetUtil.createDoubleValue(Double.NaN)), new CoreOperationConstantExpression(InfoSetUtil.createDoubleValue(Double.NaN)));

        assertNull(expression.evaluateCompare(-1));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testEvaluateCompare_NAnalogousTypes() {
        expression = new CoreOperationGt(new CoreOperationConstantExpression(InfoSetUtil.createDoubleValue(Double.NaN)), new CoreOperationConstantExpression(new String("a")));

        assertNull(expression.evaluateCompare(-1));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testEvaluateCompare_DifferentTypes() {
        expression = new CoreOperationGt(new CoreOperationConstantExpression(InfoSetUtil.createDoubleValue(2.1)), new CoreOperationConstantExpression(new Integer(1)));

        assertNull(expression.evaluateCompare(-1));
    }
}