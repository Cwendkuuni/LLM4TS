package org.apache.commons.jxpath.ri.compiler;

import static org.junit.Assert.assertEquals;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.axes.InitialContext;
import org.apache.commons.jxpath.ri.axes.SelfContext;
import org.junit.Before;
import org.junit.Test;

public class CoreOperationRelationalExpressionTest {

    private CoreOperationRelationalExpression expression;

    @Before
    public void setUp() {
        expression = new CoreOperationRelationalExpression(null) {
            @Override
            protected boolean evaluateCompare(int compare) {
                return false;
            }
        };
    }

    @Test
    public void testComputeValue() {
        EvalContext context = new EvalContext(new InitialContext(), 1, 2);

        Object result = expression.computeValue(context);

        assertEquals(Boolean.FALSE, result);
    }

    @Test
    public void testGetPrecedence() {
        int result = expression.getPrecedence();

        assertEquals(CoreOperation.RELATIONAL_EXPR_PRECEDENCE, result);
    }

    @Test
    public void testIsSymmetric() {
        boolean result = expression.isSymmetric();

        assertEquals(false, result);
    }

    @Test
    public void testReduce() {
        Object result = expression.reduce(null);

        assertEquals(null, result);
    }

    @Test
    public void testContainsMatch() {
        boolean result = expression.containsMatch(null, null);

        assertEquals(false, result);
    }

    @Test
    public void testFindMatch() {
        boolean result = expression.findMatch(null, null);

        assertEquals(false, result);
    }
}