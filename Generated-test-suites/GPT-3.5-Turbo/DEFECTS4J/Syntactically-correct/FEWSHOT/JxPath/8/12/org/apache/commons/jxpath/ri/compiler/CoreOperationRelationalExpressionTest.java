package org.apache.commons.jxpath.ri.compiler;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;

import org.apache.commons.jxpath.JXPathContext;
import org.apache.commons.jxpath.JXPathException;
import org.apache.commons.jxpath.ri.InfoSetUtil;
import org.apache.commons.jxpath.ri.compiler.CoreOperationRelationalExpression;
import org.junit.Test;

public class CoreOperationRelationalExpressionTest {

    @Test
    public void testComputeValue() {
        Expression[] args = new Expression[2];
        args[0] = new Constant(5);
        args[1] = new Constant(10);
        CoreOperationRelationalExpression expr = new CoreOperationRelationalExpression(args) {

            @Override
            protected boolean evaluateCompare(int compare) {
                return compare < 0;
            }
        };
        JXPathContext context = JXPathContext.newContext(new Object());
        Object result = expr.computeValue(context);
        assertEquals(Boolean.TRUE, result);

        args[0] = new Constant(10);
        args[1] = new Constant(5);
        expr = new CoreOperationRelationalExpression(args) {

            @Override
            protected boolean evaluateCompare(int compare) {
                return compare > 0;
            }
        };
        result = expr.computeValue(context);
        assertEquals(Boolean.TRUE, result);

        args[0] = new Constant(5);
        args[1] = new Constant(5);
        expr = new CoreOperationRelationalExpression(args) {

            @Override
            protected boolean evaluateCompare(int compare) {
                return compare == 0;
            }
        };
        result = expr.computeValue(context);
        assertEquals(Boolean.FALSE, result);
    }

    @Test
    public void testGetPrecedence() {
        Expression[] args = new Expression[2];
        CoreOperationRelationalExpression expr = new CoreOperationRelationalExpression(args) {

            @Override
            protected boolean evaluateCompare(int compare) {
                return compare < 0;
            }
        };
        int result = expr.getPrecedence();
        assertEquals(3, result);
    }

    @Test
    public void testIsSymmetric() {
        Expression[] args = new Expression[2];
        CoreOperationRelationalExpression expr = new CoreOperationRelationalExpression(args) {

            @Override
            protected boolean evaluateCompare(int compare) {
                return compare < 0;
            }
        };
        boolean result = expr.isSymmetric();
        assertFalse(result);
    }

    @Test
    public void testCompute() {
        Expression[] args = new Expression[2];
        CoreOperationRelationalExpression expr = new CoreOperationRelationalExpression(args) {

            @Override
            protected boolean evaluateCompare(int compare) {
                return compare < 0;
            }
        };

        Object result = expr.compute(5, 10);
        assertTrue((Boolean) result);

        result = expr.compute(10, 5);
        assertFalse((Boolean) result);

        result = expr.compute(5, 5);
        assertFalse((Boolean) result);

        result = expr.compute(5.0, 10.0);
        assertTrue((Boolean) result);

        result = expr.compute(10.0, 5.0);
        assertFalse((Boolean) result);

        result = expr.compute(5.0, 5.0);
        assertFalse((Boolean) result);

        args[0] = new CoreOperationRelationalExpression(args) {

            @Override
            protected boolean evaluateCompare(int compare) {
                return compare == 0;
            }
        };
        args[1] = new Constant(null);
        result = expr.compute(args[0], args[1]);
        assertFalse((Boolean) result);

        args[0] = new CoreOperationRelationalExpression(args) {

            @Override
            protected boolean evaluateCompare(int compare) {
                return compare == 0;
            }
        };
        args[1] = new CoreOperationRelationalExpression(args) {

            @Override
            protected boolean evaluateCompare(int compare) {
                return compare == 0;
            }
        };
        result = expr.compute(args[0], args[1]);
        assertFalse((Boolean) result);

        args[0] = new InitialContext(new ArrayList());
        args[1] = new InitialContext(new ArrayList());
        result = expr.compute(args[0], args[1]);
        assertFalse((Boolean) result);

        args[0] = new InitialContext(new ArrayList());
        args[1] = new Constant(10);
        result = expr.compute(args[0], args[1]);
        assertFalse((Boolean) result);

        args[0] = new Constant(10);
        args[1] = new InitialContext(new ArrayList());
        result = expr.compute(args[0], args[1]);
        assertFalse((Boolean) result);

        args[0] = new InitialContext(new ArrayList());
        args[1] = new InitialContext(new ArrayList());
        assertTrue((Boolean) result);

        
        args[0] = new Iterator() {

            @Override
            public boolean hasNext() {
                return true;
            }

            @Override
            public Object next() {
                return 5;
            }
        };
        args[1] = new Constant(10);
        result = expr.compute(args[0], args[1]);
        assertTrue((Boolean) result);

        args[0] = new Iterator() {

            @Override
            public boolean hasNext() {
                return true;
            }

            @Override
            public Object next() {
                return 10;
            }
        };
        args[1] = new Constant(5);
        result = expr.compute(args[0], args[1]);
        assertFalse((Boolean) result);

        args[0] = new Iterator() {

            @Override
            public boolean hasNext() {
                return true;
            }

            @Override
            public Object next() {
                return 5;
            }
        };
        args[1] = new Constant(5);
        result = expr.compute(args[0], args[1]);
        assertFalse((Boolean) result);

        args[0] = new Iterator() {

            @Override
            public boolean hasNext() {
                return true;
            }

            @Override
            public Object next() {
                return "foo";
            }
        };
        args[1] = new Constant("foo");
        result = expr.compute(args[0], args[1]);
        assertFalse((Boolean) result);

        args[0] = new Iterator() {

            @Override
            public boolean hasNext() {
                return true;
            }

            @Override
            public Object next() {
                return "foo";
            }
        };
        args[1] = new Constant(null);
        result = expr.compute(args[0], args[1]);
        assertFalse((Boolean) result);
    }

    @Test
    public void testReduce() {
        CoreOperationRelationalExpressionTest exprTest = new CoreOperationRelationalExpressionTest();
        Object result = exprTest.reduce(new SelfContext(new ArrayList()));
        assertTrue(result instanceof ArrayList);

        result = exprTest.reduce(new ArrayList());
        assertFalse(result instanceof ArrayList);
    }

    @Test
    public void testContainsMatch() {
        CoreOperationRelationalExpressionTest exprTest = new CoreOperationRelationalExpressionTest();
        Iterator it = new ArrayList().iterator();
        Object value = null;
        boolean result = exprTest.containsMatch(it, value);
        assertFalse(result);

        ArrayList list = new ArrayList();
        list.add(5);
        it = list.iterator();
        value = 5;
        result = exprTest.containsMatch(it, value);
        assertTrue(result);
    }

    @Test
    public void testFindMatch() {
        CoreOperationRelationalExpressionTest exprTest = new CoreOperationRelationalExpressionTest();
        Iterator lit = new ArrayList().iterator();
        Iterator rit = new ArrayList().iterator();
        boolean result = exprTest.findMatch(lit, rit);
        assertFalse(result);

        ArrayList list1 = new ArrayList();
        list1.add(5);
        ArrayList list2 = new ArrayList();
        list2.add(5);
        list2.add(10);
        lit = list1.iterator();
        rit = list2.iterator();
        result = exprTest.findMatch(lit, rit);
        assertTrue(result);
    }
}