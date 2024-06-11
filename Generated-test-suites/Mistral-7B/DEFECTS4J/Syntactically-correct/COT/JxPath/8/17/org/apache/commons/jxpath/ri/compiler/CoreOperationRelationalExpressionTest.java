package org.apache.commons.jxpath.ri.compiler.test;

import static org.junit.Assert.*;

import org.apache.commons.jxpath.ri.axes.InitialContext;
import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InfoSetUtil;
import org.apache.commons.jxpath.ri.Iterator;
import org.apache.commons.jxpath.ri.axes.SelfContext;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

public class CoreOperationRelationalExpressionTest {

    @Test
    public void testComputeValueGreaterThan() {
        CoreOperationRelationalExpression expression = new CoreOperationGreaterThan(new CoreVariable("a"), new CoreVariable("b"));
        EvalContext context = new EvalContext();
        Object left = 5;
        Object right = 10;
        assertFalse(expression.computeValue(context));
        context.setItems(new Object[]{left, right});
        assertTrue(expression.computeValue(context));
    }

    @Test
    public void testComputeValueGreaterThanOrEqual() {
        CoreOperationRelationalExpression expression = new CoreOperationGreaterThanOrEqual(new CoreVariable("a"), new CoreVariable("b"));
        EvalContext context = new EvalContext();
        Object left = 5;
        Object right = 5;
        assertTrue(expression.computeValue(context));
        context.setItems(new Object[]{left, right});
        assertFalse(expression.computeValue(context));
        context.setItems(new Object[]{left, 5.0});
        assertTrue(expression.computeValue(context));
        context.setItems(new Object[]{5.0, right});
        assertTrue(expression.computeValue(context));
    }

    @Test
    public void testComputeValueLessThan() {
        CoreOperationRelationalExpression expression = new CoreOperationLessThan(new CoreVariable("a"), new CoreVariable("b"));
        EvalContext context = new EvalContext();
        Object left = 5;
        Object right = 10;
        assertFalse(expression.computeValue(context));
        context.setItems(new Object[]{left, right});
        assertFalse(expression.computeValue(context));
        context.setItems(new Object[]{left, 5.0});
        assertFalse(expression.computeValue(context));
        context.setItems(new Object[]{5.0, right});
        assertTrue(expression.computeValue(context));
    }

    @Test
    public void testComputeValueLessThanOrEqual() {
        CoreOperationRelationalExpression expression = new CoreOperationLessThanOrEqual(new CoreVariable("a"), new CoreVariable("b"));
        EvalContext context = new EvalContext();
        Object left = 5;
        Object right = 5;
        assertTrue(expression.computeValue(context));
        context.setItems(new Object[]{left, right});
        assertTrue(expression.computeValue(context));
        context.setItems(new Object[]{left, 5.0});
        assertFalse(expression.computeValue(context));
        context.setItems(new Object[]{5.0, right});
        assertTrue(expression.computeValue(context));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testComputeValueInvalidArguments() {
        CoreOperationRelationalExpression expression = new CoreOperationGreaterThan(new CoreVariable("a"), null);
        EvalContext context = new EvalContext();
        expression.computeValue(context);
    }

    @Test
    public void testEvaluateCompareInt() {
        CoreOperationRelationalExpression expression = new CoreOperationGreaterThan(new CoreVariable("a"), new CoreVariable("b"));
        assertTrue(expression.evaluateCompare(1));
    }

    @Test
    public void testEvaluateCompareEqualInt() {
        CoreOperationRelationalExpression expression = new CoreOperationGreaterThanOrEqual(new CoreVariable("a"), new CoreVariable("b"));
        assertTrue(expression.evaluateCompare(0));
    }

    @Test
    public void testEvaluateCompareLessInt() {
        CoreOperationRelationalExpression expression = new CoreOperationLessThan(new CoreVariable("a"), new CoreVariable("b"));
        assertFalse(expression.evaluateCompare(1));
    }

    @Test
    public void testEvaluateCompareLessOrEqualInt() {
        CoreOperationRelationalExpression expression = new CoreOperationLessThanOrEqual(new CoreVariable("a"), new CoreVariable("b"));
        assertTrue(expression.evaluateCompare(0));
        assertTrue(expression.evaluateCompare(1));
    }

    @Test
    public void testEvaluateCompareDouble() {
        CoreOperationRelationalExpression expression = new CoreOperationGreaterThan(new CoreVariable("a"), new CoreVariable("b"));
        assertTrue(expression.evaluateCompare(InfoSetUtil.doubleValue(new Double(5.1)) > InfoSetUtil.doubleValue(new Double(5.0)) ? 1 : -1) > 0);
    }

    @Test
    public void testEvaluateCompareEqualDouble() {
        CoreOperationRelationalExpression expression = new CoreOperationGreaterThanOrEqual(new CoreVariable("a"), new CoreVariable("b"));
        assertTrue(expression.evaluateCompare(InfoSetUtil.doubleValue(new Double(5.1)) == InfoSetUtil.doubleValue(new Double(5.1)) ? 0 : -1) >= 0);
    }

    @Test
    public void testEvaluateCompareLessDouble() {
        CoreOperationRelationalExpression expression = new CoreOperationLessThan(new CoreVariable("a"), new CoreVariable("b"));
        assertTrue(expression.evaluateCompare(InfoSetUtil.doubleValue(new Double(5.0)) < InfoSetUtil.doubleValue(new Double(5.1)) ? -1 : 1) < 0);
    }

    @Test
    public void testEvaluateCompareLessOrEqualDouble() {
        CoreOperationRelationalExpression expression = new CoreOperationLessThanOrEqual(new CoreVariable("a"), new CoreVariable("b"));
        assertTrue(expression.evaluateCompare(InfoSetUtil.doubleValue(new Double(5.0)) <= InfoSetUtil.doubleValue(new Double(5.1)) ? 0 : -1) >= 0);
        assertTrue(expression.evaluateCompare(InfoSetUtil.doubleValue(new Double(5.1)) <= InfoSetUtil.doubleValue(new Double(5.1)) ? 0 : -1) == 0);
    }

    @Test
    public void testComputeWithInitialContext() {
        CoreOperationRelationalExpression expression = new CoreOperationGreaterThan(new CoreVariable("a"), new CoreVariable("b"));
        EvalContext context = new EvalContext();
        context.setItems(new Object[]{new InitialContext(), new InitialContext()});
        assertFalse(expression.computeValue(context));
    }

    @Test
    public void testComputeWithSelfContext() {
        CoreOperationRelationalExpression expression = new CoreOperationGreaterThan(new CoreVariable("a"), new CoreVariable("b"));
        EvalContext context = new EvalContext();
        context.setItems(new Object[]{new SelfContext(), new SelfContext()});
        assertFalse(expression.computeValue(context));
    }

    @Test
    public void testComputeWithIterator() {
        CoreOperationRelationalExpression expression = new CoreOperationGreaterThan(new CoreVariable("a"), new CoreVariable("b"));
        EvalContext context = new EvalContext();
        Collection collection = new ArrayList();
        collection.add(1);
        collection.add(2);
        context.setItems(new Object[]{collection.iterator(), collection.iterator()});
        assertFalse(expression.computeValue(context));
    }

    @Test
    public void testComputeWithTwoIteratorsContainingSameValue() {
        CoreOperationRelationalExpression expression = new CoreOperationGreaterThan(new CoreVariable("a"), new CoreVariable("b"));
        EvalContext context = new EvalContext();
        Collection collectionA = new ArrayList();
        collectionA.add(new Integer(5));
        Collection collectionB = new ArrayList();
        collectionB.add(new Integer(5));
        context.setItems(new Object[]{collectionA.iterator(), collectionB.iterator()});
        assertFalse(expression.computeValue(context));
    }

    @Test
    public void testComputeWithTwoIteratorsContainingDifferentValues() {
        CoreOperationRelationalExpression expression = new CoreOperationGreaterThan(new CoreVariable("a"), new CoreVariable("b"));
        EvalContext context = new EvalContext();
        Collection collectionA = new ArrayList();
        collectionA.add(new Integer(5));
        Collection collectionB = new ArrayList();
        collectionB.add(new Integer(6));
        context.setItems(new Object[]{collectionA.iterator(), collectionB.iterator()});
        assertFalse(expression.computeValue(context));
    }

    @Test
    public void testComputeWithTwoIteratorsOneNull() {
        CoreOperationRelationalExpression expression = new CoreOperationGreaterThan(new CoreVariable("a"), new CoreVariable("b"));
        EvalContext context = new EvalContext();
        Collection collectionA = new ArrayList();
        collectionA.add(new Integer(5));
        Collection collectionB = new ArrayList();
        context.setItems(new Object[]{collectionA.iterator(), null});
        assertFalse(expression.computeValue(context));
    }

    @Test
    public void testComputeWithTwoIteratorsOneEmpty() {
        CoreOperationRelationalExpression expression = new CoreOperationGreaterThan(new CoreVariable("a"), new CoreVariable("b"));
        EvalContext context = new EvalContext();
        Collection collectionA = new ArrayList();
        Collection collectionB = new ArrayList();
        context.setItems(new Object[]{null, collectionB.iterator()});
        assertFalse(expression.computeValue(context));
    }

    @Test
    public void testComputeWithTwoIteratorsOneHasNextFalse() {
        CoreOperationRelationalExpression expression = new CoreOperationGreaterThan(new CoreVariable("a"), new CoreVariable("b"));
        EvalContext context = new EvalContext();
        Collection collectionA = new ArrayList();
        collectionA.add(new Integer(5));
        Iterator iteratorB = new Iterator() {
            private boolean hasNext = false;

            @Override
            public boolean hasNext() {
                return hasNext;
            }

            @Override
            public Object next() {
                hasNext = true;
                return new Integer(6);
            }
        };
        context.setItems(new Object[]{collectionA.iterator(), iteratorB});
        assertFalse(expression.computeValue(context));
    }

    @Test
    public void testComputeWithTwoIteratorsBothHasNextFalse() {
        CoreOperationRelationalExpression expression = new CoreOperationGreaterThan(new CoreVariable("a"), new CoreVariable("b"));
        EvalContext context = new EvalContext();
        Collection collectionA = new ArrayList();
        collectionA.add(new Integer(5));
        Iterator iteratorB = new Iterator() {
            private boolean hasNext = false;

            @Override
            public boolean hasNext() {
                return hasNext;
            }

            @Override
            public Object next() {
                hasNext = true;
                return new Integer(5);
            }
        };
        context.setItems(new Object[]{collectionA.iterator(), iteratorB});
        assertFalse(expression.computeValue(context));
    }

    @Test
    public void testComputeWithTwoIteratorsBothHasNextTrue() {
        CoreOperationRelationalExpression expression = new CoreOperationGreaterThan(new CoreVariable("a"), new CoreVariable("b"));
        EvalContext context = new EvalContext();
        Collection collectionA = new ArrayList();
        collectionA.add(new Integer(5));
        Iterator iteratorB = new Iterator() {
            private boolean hasNext = true;

            @Override
            public boolean hasNext() {
                return hasNext;
            }

            @Override
            public Object next() {
                hasNext = false;
                return new Integer(6);
            }
        };
        context.setItems(new Object[]{collectionA.iterator(), iteratorB});
        assertFalse(expression.computeValue(context));
    }

    @Test
    public void testComputeWithTwoIteratorsBothHasNextTrueMatched() {
        CoreOperationRelationalExpression expression = new CoreOperationGreaterThan(new CoreVariable("a"), new CoreVariable("b"));
        EvalContext context = new EvalContext();
        Collection collectionA = new ArrayList();
        collectionA.add(new Integer(5));
        Iterator iteratorB = new Iterator() {
            private boolean hasNext = true;

            @Override
            public boolean hasNext() {
                return hasNext;
            }

            @Override
            public Object next() {
                hasNext = false;
                return new Integer(6);
            }
        };
        context.setItems(new Object[]{collectionA.iterator(), iteratorB});
        Iterator iteratorA = collectionA.iterator();
        assertTrue(expression.computeValue(context) == compute(iteratorA.next(), iteratorB.next()));
    }

    private boolean compute(Object left, Object right) {
        return expression.compute(left, right);
    }

    private CoreOperationRelationalExpression expression = null;

    @Before
    public void setUp() throws Exception {
        expression = new CoreOperationGreaterThan(new CoreVariable("a"), new CoreVariable("b"));
    }
}