package org.apache.commons.jxpath.ri.compiler;

import static org.junit.Assert.*;

import org.junit.Test;

public class CoreOperationRelationalExpressionTest {

    @Test
    public void testComputeValue() {
        Expression[] args = new Expression[2];
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(args) {
            @Override
            protected boolean evaluateCompare(int compare) {
                // TODO Auto-generated method stub
                return false;
            }
        };
        EvalContext context = new EvalContext();
        Object result = expression.computeValue(context);
        assertEquals(Boolean.FALSE, result);
    }

    @Test
    public void testGetPrecedence() {
        Expression[] args = new Expression[2];
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(args) {
            @Override
            protected boolean evaluateCompare(int compare) {
                // TODO Auto-generated method stub
                return false;
            }
        };
        int result = expression.getPrecedence();
        assertEquals(3, result);
    }

    @Test
    public void testIsSymmetric() {
        Expression[] args = new Expression[2];
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(args) {
            @Override
            protected boolean evaluateCompare(int compare) {
                // TODO Auto-generated method stub
                return false;
            }
        };
        boolean result = expression.isSymmetric();
        assertFalse(result);
    }

    @Test
    public void testCompute() {
        Expression[] args = new Expression[2];
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(args) {
            @Override
            protected boolean evaluateCompare(int compare) {
                // TODO Auto-generated method stub
                return false;
            }
        };
        Object left = new Object();
        Object right = new Object();
        boolean result = expression.compute(left, right);
        assertFalse(result);
    }

    @Test
    public void testReduce() {
        Expression[] args = new Expression[2];
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(args) {
            @Override
            protected boolean evaluateCompare(int compare) {
                // TODO Auto-generated method stub
                return false;
            }
        };
        Object o = new Object();
        Object result = expression.reduce(o);
        assertEquals(o, result);
    }

    @Test
    public void testContainsMatch() {
        Expression[] args = new Expression[2];
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(args) {
            @Override
            protected boolean evaluateCompare(int compare) {
                // TODO Auto-generated method stub
                return false;
            }
        };
        Iterator it = new Iterator() {

            @Override
            public boolean hasNext() {
                // TODO Auto-generated method stub
                return false;
            }

            @Override
            public Object next() {
                // TODO Auto-generated method stub
                return null;
            }
        };
        Object value = new Object();
        boolean result = expression.containsMatch(it, value);
        assertFalse(result);
    }

    @Test
    public void testFindMatch() {
        Expression[] args = new Expression[2];
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(args) {
            @Override
            protected boolean evaluateCompare(int compare) {
                // TODO Auto-generated method stub
                return false;
            }
        };
        Iterator lit = new Iterator() {

            @Override
            public boolean hasNext() {
                // TODO Auto-generated method stub
                return false;
            }

            @Override
            public Object next() {
                // TODO Auto-generated method stub
                return null;
            }
        };
        Iterator rit = new Iterator() {

            @Override
            public boolean hasNext() {
                // TODO Auto-generated method stub
                return false;
            }

            @Override
            public Object next() {
                // TODO Auto-generated method stub
                return null;
            }
        };
        boolean result = expression.findMatch(lit, rit);
        assertFalse(result);
    }

}