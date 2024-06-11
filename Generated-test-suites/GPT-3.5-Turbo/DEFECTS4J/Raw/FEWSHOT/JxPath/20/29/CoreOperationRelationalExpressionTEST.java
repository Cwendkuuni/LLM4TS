package org.apache.commons.jxpath.ri.compiler;

import static org.junit.Assert.*;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InfoSetUtil;
import org.junit.Before;
import org.junit.Test;

public class CoreOperationRelationalExpressionTest {

    private CoreOperationRelationalExpression expression;

    @Before
    public void setUp() {
        Expression[] args = { new CoreOperation(), new CoreOperation() };
        expression = new CoreOperationRelationalExpression(args) {
            @Override
            protected boolean evaluateCompare(int compare) {
                return false;
            }
        };
    }

    @Test
    public void testComputeValue() {
        EvalContext context = null;
        Object result = expression.computeValue(context);
        assertEquals(Boolean.FALSE, result);
    }

    @Test
    public void testGetPrecedence() {
        int precedence = expression.getPrecedence();
        assertEquals(2, precedence);
    }

    @Test
    public void testIsSymmetric() {
        boolean symmetric = expression.isSymmetric();
        assertFalse(symmetric);
    }

    @Test
    public void testReduce() {
        Object o = new Object();
        Object result = expression.reduce(o);
        assertSame(o, result);
    }

    @Test
    public void testContainsMatch() {
        Iterator it = null;
        Object value = null;
        boolean result = expression.containsMatch(it, value);
        assertFalse(result);
    }

    @Test
    public void testFindMatch() {
        Iterator lit = null;
        Iterator rit = null;
        boolean result = expression.findMatch(lit, rit);
        assertFalse(result);
    }
}