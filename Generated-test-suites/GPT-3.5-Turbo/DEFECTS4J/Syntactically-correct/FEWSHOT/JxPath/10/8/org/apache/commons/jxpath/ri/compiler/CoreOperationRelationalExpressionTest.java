package org.apache.commons.jxpath.ri.compiler;

import org.junit.Test;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class CoreOperationRelationalExpressionTest {

    @Test
    public void testComputeValue() {
        Expression[] args = new Expression[2];
        args[0] = new Constant(5);
        args[1] = new Constant(3);
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(args) {
            @Override
            protected boolean evaluateCompare(int compare) {
                return compare > 0;
            }
        };
        assertEquals(Boolean.TRUE, expression.computeValue(null));

        args[0] = new Constant(3);
        args[1] = new Constant(5);
        expression = new CoreOperationRelationalExpression(args) {
            @Override
            protected boolean evaluateCompare(int compare) {
                return compare > 0;
            }
        };
        assertEquals(Boolean.FALSE, expression.computeValue(null));
    }

    @Test
    public void testGetPrecedence() {
        Expression[] args = new Expression[2];
        args[0] = new Constant(5);
        args[1] = new Constant(3);
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(args) {
            @Override
            protected boolean evaluateCompare(int compare) {
                return compare > 0;
            }
        };
        assertEquals(3, expression.getPrecedence());
    }

    @Test
    public void testIsSymmetric() {
        Expression[] args = new Expression[2];
        args[0] = new Constant(5);
        args[1] = new Constant(3);
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(args) {
            @Override
            protected boolean evaluateCompare(int compare) {
                return compare > 0;
            }
        };
        assertEquals(false, expression.isSymmetric());
    }

    @Test
    public void testReduce() {
        Expression[] args = new Expression[2];
        args[0] = new Constant(5);
        args[1] = new Constant(3);
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(args) {
            @Override
            protected boolean evaluateCompare(int compare) {
                return compare > 0;
            }
        };

        Object result = expression.reduce(new SelfContext());
        assertNull(result);

        List<Integer> list = new ArrayList<Integer>();
        list.add(1);
        list.add(2);
        result = expression.reduce(list);
        assertTrue(result instanceof Iterator);
    }

    @Test
    public void testContainsMatch() {
        Expression[] args = new Expression[2];
        args[0] = new Constant(5);
        args[1] = new Constant(3);
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(args) {
            @Override
            protected boolean evaluateCompare(int compare) {
                return compare > 0;
            }
        };

        List<Integer> list = new ArrayList<Integer>();
        list.add(1);
        list.add(2);
        Iterator<Integer> iterator = list.iterator();
        boolean result = expression.containsMatch(iterator, 1);
        assertTrue(result);

        result = expression.containsMatch(iterator, 3);
        assertFalse(result);
    }

    @Test
    public void testFindMatch() {
        Expression[] args = new Expression[2];
        args[0] = new Constant(5);
        args[1] = new Constant(3);
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(args) {
            @Override
            protected boolean evaluateCompare(int compare) {
                return compare > 0;
            }
        };

        List<Integer> list1 = new ArrayList<Integer>();
        list1.add(1);
        list1.add(2);

        List<Integer> list2 = new ArrayList<Integer>();
        list2.add(3);
        list2.add(4);

        Iterator<Integer> iterator1 = list1.iterator();
        Iterator<Integer> iterator2 = list2.iterator();

        boolean result = expression.findMatch(iterator1, iterator2);
        assertTrue(result);

        list2.add(1);
        iterator2 = list2.iterator();
        result = expression.findMatch(iterator1, iterator2);
        assertFalse(result);
    }
}