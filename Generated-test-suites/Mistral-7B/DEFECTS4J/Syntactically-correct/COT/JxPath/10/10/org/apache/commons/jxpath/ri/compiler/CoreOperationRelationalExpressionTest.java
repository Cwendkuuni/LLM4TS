package org.apache.commons.jxpath.ri.compiler.test;

import static org.junit.Assert.*;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.axes.InitialContext;
import org.apache.commons.jxpath.ri.axes.SelfContext;
import org.apache.commons.jxpath.ri.compiler.CoreOperationRelationalExpression;
import org.apache.commons.jxpath.ri.compiler.CoreOperationRelationalExpression.CoreOperationRelationalExpressionFactory;
import org.apache.commons.jxpath.ri.iterator.ArrayIterator;
import org.apache.commons.jxpath.ri.iterator.SingletonIterator;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

/**
 * Test for CoreOperationRelationalExpression class.
 */
public class CoreOperationRelationalExpressionTest {

    private CoreOperationRelationalExpressionFactory factory;
    private EvalContext context;

    @Before
    public void init() {
        factory = new CoreOperationRelationalExpressionFactory();
        context = new EvalContext();
    }

    /**
     * Test method for {@link CoreOperationRelationalExpression#computeValue(EvalContext)}.
     */
    @Test
    public void testComputeValue() {
        Object left = new Integer(5);
        Object right = new Integer(10);
        CoreOperationRelationalExpression greaterThan = factory.createGreaterThanExpression(left, right);
        assertTrue(greaterThan.computeValue(context) instanceof Boolean && ((Boolean) greaterThan.computeValue(context)).booleanValue());

        left = new Integer(10);
        right = new Integer(5);
        CoreOperationRelationalExpression lessThan = factory.createLessThanExpression(left, right);
        assertTrue(lessThan.computeValue(context) instanceof Boolean && ((Boolean) lessThan.computeValue(context)).booleanValue());

        left = new Integer(5);
        right = new Integer(5);
        CoreOperationRelationalExpression equal = factory.createEqualToExpression(left, right);
        assertTrue(equal.computeValue(context) instanceof Boolean && ((Boolean) equal.computeValue(context)).booleanValue());

        left = new Integer(5);
        right = new Integer(6);
        CoreOperationRelationalExpression notEqual = factory.createNotEqualToExpression(left, right);
        assertTrue(notEqual.computeValue(context) instanceof Boolean && !((Boolean) notEqual.computeValue(context)).booleanValue());

        left = new String("test");
        right = new String("test");
        CoreOperationRelationalExpression stringEqual = factory.createEqualToExpression(left, right);
        assertTrue(stringEqual.computeValue(context) instanceof Boolean && ((Boolean) stringEqual.computeValue(context)).booleanValue());

        left = new String("test");
        right = new String("other");
        CoreOperationRelationalExpression stringNotEqual = factory.createNotEqualToExpression(left, right);
        assertTrue(stringNotEqual.computeValue(context) instanceof Boolean && !((Boolean) stringNotEqual.computeValue(context)).booleanValue());
    }

    /**
     * Test method for {@link CoreOperationRelationalExpression#getPrecedence()}.
     */
    @Test
    public void testGetPrecedence() {
        CoreOperationRelationalExpression greaterThan = factory.createGreaterThanExpression(new Integer(1), new Integer(2));
        assertEquals(3, greaterThan.getPrecedence());
    }

    /**
     * Test method for {@link CoreOperationRelationalExpression#isSymmetric()}.
     */
    @Test
    public void testIsSymmetric() {
        CoreOperationRelationalExpression greaterThan = factory.createGreaterThanExpression(new Integer(1), new Integer(2));
        assertFalse(greaterThan.isSymmetric());

        CoreOperationRelationalExpression equal = factory.createEqualToExpression(new Integer(1), new Integer(1));
        assertTrue(equal.isSymmetric());
    }

    /**
     * Test method for {@link CoreOperationRelationalExpression#evaluateCompare(int)}.
     */
    @Test
    public void testEvaluateCompare() {
        CoreOperationRelationalExpression greaterThan = new CoreOperationRelationalExpression() {
            @Override
            protected boolean evaluateCompare(int compare) {
                return compare > 0;
            }
        };
        assertTrue(greaterThan.evaluateCompare(1));
        assertTrue(greaterThan.evaluateCompare(-2));
        assertFalse(greaterThan.evaluateCompare(0));

        CoreOperationRelationalExpression lessThan = new CoreOperationRelationalExpression() {
            @Override
            protected boolean evaluateCompare(int compare) {
                return compare < 0;
            }
        };
        assertTrue(lessThan.evaluateCompare(-1));
        assertTrue(lessThan.evaluateCompare(-2));
        assertFalse(lessThan.evaluateCompare(0));

        CoreOperationRelationalExpression equal = new CoreOperationRelationalExpression() {
            @Override
            protected boolean evaluateCompare(int compare) {
                return compare == 0;
            }
        };
        assertTrue(equal.evaluateCompare(0));
        assertTrue(equal.evaluateCompare(-1));
        assertTrue(equal.evaluateCompare(1));
    }

    /**
     * Test method for {@link CoreOperationRelationalExpression#compute(Object, Object)}.
     */
    @Test
    public void testCompute() {
        Object left = new Integer(5);
        Object right = new Integer(10);

        CoreOperationRelationalExpression greaterThan = factory.createGreaterThanExpression(left, right);
        assertFalse(greaterThan.compute(left, right));

        left = new Integer(10);
        assertTrue(greaterThan.compute(left, right));

        left = new Integer(5);
        right = new Integer(5);
        CoreOperationRelationalExpression equal = factory.createEqualToExpression(left, right);
        assertTrue(equal.compute(left, right));

        left = new Integer(5);
        right = new Integer(6);
        CoreOperationRelationalExpression notEqual = factory.createNotEqualToExpression(left, right);
        assertFalse(notEqual.compute(left, right));

        left = new Double(3.14);
        right = new Double(3.14);
        CoreOperationRelationalExpression doubleEqual = factory.createEqualToExpression(left, right);
        assertTrue(doubleEqual.compute(left, right));

        left = new Double(3.14);
        right = new Double(3.15);
        CoreOperationRelationalExpression doubleNotEqual = factory.createNotEqualToExpression(left, right);
        assertFalse(doubleNotEqual.compute(left, right));

        left = new String("test");
        right = new String("test");
        CoreOperationRelationalExpression stringEqual = factory.createEqualToExpression(left, right);
        assertTrue(stringEqual.compute(left, right));

        left = new String("test");
        right = new String("other");
        CoreOperationRelationalExpression stringNotEqual = factory.createNotEqualToExpression(left, right);
        assertFalse(stringNotEqual.compute(left, right));

        left = new Iterator<Object>() {
            @Override
            public boolean hasNext() {
                return true;
            }

            @Override
            public Object next() {
                return new Integer(5);
            }
        };
        right = new Iterator<Object>() {
            @Override
            public boolean hasNext() {
                return true;
            }

            @Override
            public Object next() {
                return new Integer(10);
            }
        };
        CoreOperationRelationalExpression iteratorGreaterThan = factory.createGreaterThanExpression(left, right);
        assertFalse(iteratorGreaterThan.compute(left, right));

        left = new Iterator<Object>() {
            @Override
            public boolean hasNext() {
                return true;
            }

            @Override
            public Object next() {
                return new Integer(10);
            }
        };
        assertTrue(iteratorGreaterThan.compute(left, right));

        left = new Iterator<Object>() {
            @Override
            public boolean hasNext() {
                return true;
            }

            @Override
            public Object next() {
                return new Integer(5);
            }
        };
        right = Arrays.asList(new Integer(5), new Integer(6), new Integer(7)).iterator();
        CoreOperationRelationalExpression iteratorContains = factory.createContainsExpression(left, right);
        assertFalse(iteratorContains.compute(left, right));

        left = Arrays.asList(new Integer(5), new Integer(6), new Integer(7)).iterator();
        assertTrue(iteratorContains.compute(left, new Integer(6)));

        left = Arrays.asList(new Integer(5), new Integer(6), new Integer(7)).iterator();
        right = Arrays.asList(new Integer(5), new Integer(4), new Integer(3)).iterator();
        CoreOperationRelationalExpression iteratorFindMatch = factory.createFindMatchExpression(left, right);
        assertFalse(iteratorFindMatch.compute(left, right));

        left = Arrays.asList(new Integer(5), new Integer(5), new Integer(5)).iterator();
        right = Arrays.asList(new Integer(5), new Integer(6), new Integer(5)).iterator();
        assertTrue(iteratorFindMatch.compute(left, right));

        left = new SingletonIterator<Object>(new Integer(5));
        right = Arrays.asList(new Integer(5), new Integer(6), new Integer(5)).iterator();
        assertFalse(iteratorFindMatch.compute(left, right));

        left = new SingletonIterator<Object>(new Integer(5));
        right = new SingletonIterator<Object>(new Integer(5));
        assertTrue(iteratorFindMatch.compute(left, right));

        left = new SingletonIterator<Object>(new Integer(5));
        right = new SingletonIterator<Object>(new Integer(5));
        assertTrue(iteratorContains.compute(left, right));
    }
}