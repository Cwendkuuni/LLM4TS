package org.apache.commons.jxpath.ri.compiler;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.axes.InitialContext;
import org.apache.commons.jxpath.ri.axes.SelfContext;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;

public class CoreOperationRelationalExpressionTest {
    private CoreOperationRelationalExpression coreOperationRelationalExpression;
    private Expression[] args;
    private EvalContext context;

    @Before
    public void setup() {
        args = new Expression[2];
        args[0] = new Constant(true);
        args[1] = new Constant(false);
        context = new InitialContext(new SelfContext(), null);
        coreOperationRelationalExpression = new TestCoreOperationRelationalExpression(args);
    }

    @Test
    public void testComputeValue() {
        assertEquals(Boolean.FALSE, coreOperationRelationalExpression.computeValue(context));
    }

    @Test
    public void testIsSymmetric() {
        assertFalse(coreOperationRelationalExpression.isSymmetric());
    }

    @Test
    public void testGetPrecedence() {
        assertEquals(CoreOperation.RELATIONAL_EXPR_PRECEDENCE, coreOperationRelationalExpression.getPrecedence());
    }

    @Test
    public void testEvaluateCompare() {
        assertFalse(coreOperationRelationalExpression.evaluateCompare(0));
        assertFalse(coreOperationRelationalExpression.evaluateCompare(-1));
        assertTrue(coreOperationRelationalExpression.evaluateCompare(1));
    }

    @Test
    public void testCompute() {
        assertFalse(coreOperationRelationalExpression.compute(true, false));
        assertFalse(coreOperationRelationalExpression.compute(1, 2));
        assertFalse(coreOperationRelationalExpression.compute(true, 2));
        assertFalse(coreOperationRelationalExpression.compute(1, false));
        assertTrue(coreOperationRelationalExpression.compute(1, 1));
    }

    @Test
    public void testReduce() {
        Object o = coreOperationRelationalExpression.reduce(new SelfContext());
        assertTrue(o instanceof EvalContext);

        Collection collection = Arrays.asList(new Object(), new Object());
        o = coreOperationRelationalExpression.reduce(collection);
        assertTrue(o instanceof Iterator);

        o = coreOperationRelationalExpression.reduce(1);
        assertEquals(1, o);
    }

    @Test
    public void testContainsMatch() {
        Iterator iterator = Arrays.asList(true, false).iterator();
        assertFalse(coreOperationRelationalExpression.containsMatch(iterator, true));

        iterator = Arrays.asList(true, false).iterator();
        assertTrue(coreOperationRelationalExpression.containsMatch(iterator, false));
    }

    @Test
    public void testFindMatch() {
        Iterator lit = Arrays.asList(true, false).iterator();
        Iterator rit = Arrays.asList(false, true).iterator();
        assertTrue(coreOperationRelationalExpression.findMatch(lit, rit));

        lit = Arrays.asList(true, false).iterator();
        rit = Arrays.asList(false, false).iterator();
        assertFalse(coreOperationRelationalExpression.findMatch(lit, rit));
    }

    private static class TestCoreOperationRelationalExpression extends CoreOperationRelationalExpression {
        protected TestCoreOperationRelationalExpression(Expression[] args) {
            super(args);
        }

        @Override
        protected boolean evaluateCompare(int compare) {
            return compare == 1;
        }
    }
}