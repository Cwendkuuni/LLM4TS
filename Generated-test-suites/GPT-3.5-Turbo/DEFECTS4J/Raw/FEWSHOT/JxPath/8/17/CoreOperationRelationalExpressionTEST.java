package org.apache.commons.jxpath.ri.compiler;

import org.apache.commons.jxpath.ri.EvalContext;

import static org.junit.Assert.*;
import org.junit.Test;

public class CoreOperationRelationalExpressionTest {

    @Test
    public void testComputeValue() {
        EvalContext context = new EvalContext();

        // test case 1
        Expression[] args1 = new Expression[]{
          new CoreOperationRelationalExpression(new Expression[]{new Constant("foo"), new Constant("bar")}){}
        };
        CoreOperationRelationalExpression expr1 = new CoreOperationRelationalExpression(args1){};
        assertEquals(Boolean.FALSE, expr1.computeValue(context));

        // test case 2
        Expression[] args2 = new Expression[]{
          new CoreOperationRelationalExpression(new Expression[]{new Constant(2), new Constant(1)}){}
        };
        CoreOperationRelationalExpression expr2 = new CoreOperationRelationalExpression(args2){};
        assertEquals(Boolean.TRUE, expr2.computeValue(context));

        // test case 3
        Expression[] args3 = new Expression[]{
          new CoreOperationRelationalExpression(new Expression[]{new Constant(5), new Constant(5)}){}
        };
        CoreOperationRelationalExpression expr3 = new CoreOperationRelationalExpression(args3){};
        assertEquals(Boolean.FALSE, expr3.computeValue(context));
    }

    @Test
    public void testGetPrecedence() {
        Expression[] args = new Expression[]{new Constant("foo"), new Constant("bar")};
        CoreOperationRelationalExpression expr = new CoreOperationRelationalExpression(args){};
        assertEquals(3, expr.getPrecedence());
    }

    @Test
    public void testIsSymmetric() {
        Expression[] args = new Expression[]{new Constant("foo"), new Constant("bar")};
        CoreOperationRelationalExpression expr = new CoreOperationRelationalExpression(args){};
        assertFalse(expr.isSymmetric());
    }

    @Test
    public void testEvaluateCompare() {
        Expression[] args = new Expression[]{new Constant(2), new Constant(3)};
        CoreOperationRelationalExpression expr = new CoreOperationRelationalExpression(args){};
        assertTrue(expr.evaluateCompare(-1));
        assertFalse(expr.evaluateCompare(0));
        assertFalse(expr.evaluateCompare(1));
    }

    @Test
    public void testCompute() {
        Expression[] args = new Expression[]{new Constant(2), new Constant(3)};
        CoreOperationRelationalExpression expr = new CoreOperationRelationalExpression(args){};
        assertTrue(expr.compute(2, 3));
        assertFalse(expr.compute(3, 3));
    }

    @Test
    public void testReduce() {
        Expression[] args = new Expression[]{new Constant(2), new Constant(3)};
        CoreOperationRelationalExpression expr = new CoreOperationRelationalExpression(args){};
        assertEquals(2, expr.reduce(2));
        assertEquals(3, expr.reduce(3));
    }

    @Test
    public void testContainsMatch() {
        Expression[] args = new Expression[]{new Constant("foo"), new Constant("bar")};
        CoreOperationRelationalExpression expr = new CoreOperationRelationalExpression(args){};
        assertFalse(expr.containsMatch(new Iterator<String>() {
            @Override
            public boolean hasNext() {
                return false;
            }

            @Override
            public String next() {
                return null;
            }
        }, "bar"));
    }

    @Test
    public void testFindMatch() {
        Expression[] args = new Expression[]{new Constant("foo"), new Constant("bar")};
        CoreOperationRelationalExpression expr = new CoreOperationRelationalExpression(args){};
        assertFalse(expr.findMatch(new Iterator<String>() {
            @Override
            public boolean hasNext() {
                return false;
            }

            @Override
            public String next() {
                return null;
            }
        }, new Iterator<String>() {
            @Override
            public boolean hasNext() {
                return false;
            }

            @Override
            public String next() {
                return null;
            }
        }));
    }
}