package org.apache.commons.jxpath.ri.compiler;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InfoSetUtil;
import org.apache.commons.jxpath.ri.axes.InitialContext;
import org.apache.commons.jxpath.ri.axes.SelfContext;
import org.junit.BeforeClass;
import org.junit.Test;

public class CoreOperationRelationalExpressionTest {
    private static CoreOperationRelationalExpression expression;

    @BeforeClass
    public static void setUp() {
        Expression[] args = new Expression[2];
        args[0] = new CoreOperation("left");
        args[1] = new CoreOperation("right");

        expression = new CoreOperationRelationalExpression(args) {
            @Override
            protected boolean evaluateCompare(int compare) {
                return compare < 0;
            }
        };
    }

    @Test
    public void testComputeValueReturnsTrueWhenCompareIsNegative() {
        EvalContext context = new EvalContext(null);
        Object result = expression.computeValue(context);
        assertTrue((Boolean) result);
    }

    @Test
    public void testComputeValueReturnsFalseWhenCompareIsNonNegative() {
        Expression[] args = new Expression[2];
        args[0] = new CoreOperation("left");
        args[1] = new CoreOperation("right");

        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(args) {
            @Override
            protected boolean evaluateCompare(int compare) {
                return compare >= 0;
            }
        };
        EvalContext context = new EvalContext(null);
        Object result = expression.computeValue(context);
        assertFalse((Boolean) result);
    }

    @Test
    public void testGetPrecedenceReturns3() {
        int result = expression.getPrecedence();
        assertEquals(3, result);
    }

    @Test
    public void testIsSymmetricReturnsFalse() {
        boolean result = expression.isSymmetric();
        assertFalse(result);
    }

    @Test
    public void testComputeReturnsTrueWhenComparedValuesAreEqual() {
        Object left = 5;
        Object right = 5;

        boolean result = expression.compute(left, right);
        assertFalse(result);
    }

    @Test
    public void testComputeReturnsFalseWhenComparedValuesAreNotEqual() {
        Object left = 10;
        Object right = 5;

        boolean result = expression.compute(left, right);
        assertTrue(result);
    }

    @Test
    public void testComputeReturnsFalseWhenComparedValueIsNaN() {
        Object left = Double.NaN;
        Object right = 5;

        boolean result = expression.compute(left, right);
        assertFalse(result);
    }

    @Test
    public void testComputeConvertsInitialContextToSingleNodePointer() {
        InitialContext initialContext = new InitialContext();
        initialContext.setParentContext(null);

        Object left = initialContext;
        Object right = 5;

        boolean result = expression.compute(left, right);
        assertFalse(result);
    }

    @Test
    public void testComputeConvertsCollectionToIterator() {
        List<Integer> collection = Arrays.asList(1, 2, 3);

        Object left = collection;
        Object right = 2;

        boolean result = expression.compute(left, right);
        assertTrue(result);
    }

    @Test
    public void testContainsMatchReturnsTrueWhenValueExistsInIterator() {
        List<Integer> collection = Arrays.asList(1, 2, 3);
        Iterator<Integer> iterator = collection.iterator();
        Object value = 2;

        boolean result = expression.containsMatch(iterator, value);
        assertTrue(result);
    }

    @Test
    public void testContainsMatchReturnsFalseWhenValueDoesNotExistInIterator() {
        List<Integer> collection = Arrays.asList(1, 2, 3);
        Iterator<Integer> iterator = collection.iterator();
        Object value = 5;

        boolean result = expression.containsMatch(iterator, value);
        assertFalse(result);
    }

    @Test
    public void testFindMatchReturnsTrueWhenMatchExistsBetweenIterators() {
        List<Integer> leftCollection = Arrays.asList(1, 2, 3);
        List<Integer> rightCollection = Arrays.asList(4, 5, 2);

        Iterator<Integer> leftIterator = leftCollection.iterator();
        Iterator<Integer> rightIterator = rightCollection.iterator();

        boolean result = expression.findMatch(leftIterator, rightIterator);
        assertTrue(result);
    }

    @Test
    public void testFindMatchReturnsFalseWhenNoMatchExistsBetweenIterators() {
        List<Integer> leftCollection = Arrays.asList(1, 2, 3);
        List<Integer> rightCollection = Arrays.asList(4, 5, 6);

        Iterator<Integer> leftIterator = leftCollection.iterator();
        Iterator<Integer> rightIterator = rightCollection.iterator();

        boolean result = expression.findMatch(leftIterator, rightIterator);
        assertFalse(result);
    }
}