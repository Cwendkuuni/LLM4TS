package org.apache.commons.jxpath.ri.compiler;

import static org.junit.Assert.*;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InfoSetUtil;
import org.apache.commons.jxpath.ri.axes.InitialContext;
import org.apache.commons.jxpath.ri.axes.SelfContext;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

/**
 * Test class for CoreOperationRelationalExpression.
 */
public class CoreOperationRelationalExpressionTest {

    private CoreOperationRelationalExpression expression;
    private EvalContext context;

    @Before
    public void setup() {
        expression = new CoreOperationRelationalExpression(new Expression[2]) {
            @Override
            protected boolean evaluateCompare(int compare) {
                return compare > 0;
            }
        };
        context = new EvalContext(new Object[0], new InitialContext(), new SelfContext());
    }

    @Test
    public void testComputeValueIntInt() {
        Object left = 5;
        Object right = 10;

        assertTrue(expression.computeValue(context));
    }

    @Test
    public void testComputeValueIntDouble() {
        Object left = 5;
        Object right = 10.5;

        assertTrue(expression.computeValue(context));
    }

    @Test
    public void testComputeValueDoubleInt() {
        Object left = 5.5;
        Object right = 10;

        assertFalse(expression.computeValue(context));
    }

    @Test
    public void testComputeValueDoubleDouble() {
        Object left = 5.5;
        Object right = 10.5;

        assertFalse(expression.computeValue(context));
    }

    @Test
    public void testComputeValueStringString() {
        Object left = "abc";
        Object right = "def";

        assertFalse(expression.computeValue(context));
    }

    @Test
    public void testComputeValueStringStringEquals() {
        Object left = "abc";
        Object right = "abc";

        assertTrue(expression.computeValue(context));
    }

    @Test
    public void testComputeValueObjectObject() {
        Object left = new Object();
        Object right = new Object();

        assertFalse(expression.computeValue(context));
    }

    @Test
    public void testComputeValueObjectObjectEquals() {
        Object left = new Object();
        Object right = new Object();
        left = left == right ? left : right;

        assertTrue(expression.computeValue(context));
    }

    @Test
    public void testComputeValueIteratorsSameElements() {
        Iterator left = Arrays.asList(1, 2, 3).iterator();
        Iterator right = Arrays.asList(1, 2, 3).iterator();

        assertTrue(expression.computeValue(context));
    }

    @Test
    public void testComputeValueIteratorsDifferentElements() {
        Iterator left = Arrays.asList(1, 2, 3).iterator();
        Iterator right = Arrays.asList(3, 2, 1).iterator();

        assertFalse(expression.computeValue(context));
    }

    @Test
    public void testComputeValueIteratorsOverlappingElements() {
        Iterator left = Arrays.asList(1, 2, 3).iterator();
        Iterator right = Arrays.asList(2, 3, 4).iterator();

        assertFalse(expression.computeValue(context));
    }

    @Test
    public void testComputeValueIteratorsDisjointElements() {
        Iterator left = Arrays.asList(1, 2, 3).iterator();
        Iterator right = Arrays.asList(4, 5, 6).iterator();

        assertFalse(expression.computeValue(context));
    }

    @Test
    public void testComputeValueIteratorsEmpty() {
        Iterator left = Collections.emptyIterator();
        Iterator right = Collections.emptyIterator();

        assertFalse(expression.computeValue(context));
    }

    @Test
    public void testComputeValueIteratorsLeftEmpty() {
        Iterator left = Collections.emptyIterator();
        Iterator right = Arrays.asList(1, 2, 3).iterator();

        assertFalse(expression.computeValue(context));
    }

    @Test
    public void testComputeValueIteratorsRightEmpty() {
        Iterator left = Arrays.asList(1, 2, 3).iterator();
        Iterator right = Collections.emptyIterator();

        assertFalse(expression.computeValue(context));
    }

    @Test
    public void testComputeValueNullLeft() {
        Object left = null;
        Object right = 5;

        assertFalse(expression.computeValue(context));
    }

    @Test
    public void testComputeValueNullRight() {
        Object left = 5;
        Object right = null;

        assertFalse(expression.computeValue(context));
    }
}