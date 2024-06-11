package org.apache.commons.jxpath.ri.compiler;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.axes.InitialContext;
import org.junit.Test;
import static org.junit.Assert.*;

public class CoreOperationRelationalExpressionTest {

    @Test
    public void testComputeValue() {
        Object left = Boolean.TRUE;
        Object right = Boolean.TRUE;
        EvalContext context = new EvalContext(new InitialContext());

        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(new CoreOperationConstant(left), new CoreOperationConstant(right));
        Object result = expression.computeValue(context);

        assertTrue(result.equals(Boolean.TRUE));
    }

    @Test
    public void testGetPrecedence() {
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(new CoreOperationConstant(1), new CoreOperationConstant(2));
        int precedence = expression.getPrecedence();

        assertEquals(3, precedence);
    }

    @Test
    public void testIsSymmetric() {
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(new CoreOperationConstant(1), new CoreOperationConstant(1));
        assertFalse(expression.isSymmetric());

        expression = new CoreOperationRelationalExpression(new CoreOperationConstant(3), new CoreOperationConstant(3));
        assertFalse(expression.isSymmetric());
    }

    @Test
    public void testEvaluateCompare() {
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(new CoreOperationConstant(1), new CoreOperationConstant(1));
        expression.evaluateCompare = (compare) -> compare == 0;

        assertTrue(expression.computeValue(new EvalContext(new InitialContext())));

        expression.evaluateCompare = (compare) -> compare < 0;
        assertFalse(expression.computeValue(new EvalContext(new InitialContext())));
    }

    @Test
    public void testReduce() {
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(new CoreOperationConstant("1"), new CoreOperationConstant("2"));
        Object reducedLeft = expression.reduce("1");
        Object reducedRight = expression.reduce("2");

        assertEquals("1", reducedLeft.toString());
        assertEquals("2", reducedRight.toString());
    }

    @Test
    public void testContainsMatch() {
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(new CoreOperationConstant(1), new CoreOperationConstant(1));
        Iterator it = new Iterator() {
            @Override
            public boolean hasNext() {
                return true;
            }

            @Override
            public Object next() {
                return 1;
            }
        };

        assertTrue(expression.containsMatch(it, new CoreOperationConstant(1)));
        assertFalse(expression.containsMatch(it, new CoreOperationConstant(2)));
    }

    @Test
    public void testFindMatch() {
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(new CoreOperationConstant(1), new CoreOperationConstant(1));
        Iterator lit = new Iterator() {
            @Override
            public boolean hasNext() {
                return true;
            }

            @Override
            public Object next() {
                return 1;
            }
        };

        Iterator rit = new Iterator() {
            @Override
            public boolean hasNext() {
                return true;
            }

            @Override
            public Object next() {
                return 1;
            }
        };

        assertTrue(expression.findMatch(lit, rit));

        lit = new Iterator() {
            @Override
            public boolean hasNext() {
                return true;
            }

            @Override
            public Object next() {
                return 2;
            }
        };

        assertFalse(expression.findMatch(lit, rit));
    }
}