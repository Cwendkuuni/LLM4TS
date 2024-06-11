package org.apache.commons.jxpath.ri.compiler;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InfoSetUtil;
import org.apache.commons.jxpath.ri.axes.InitialContext;
import org.apache.commons.jxpath.ri.axes.SelfContext;
import org.junit.Test;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;

public class CoreOperationRelationalExpressionTest {

    @Test
    public void testComputeValue() {
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(new Expression[]{});
        assertTrue(expression.computeValue(new EvalContext()));
    }

    @Test
    public void testGetPrecedence() {
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(new Expression[]{});
        assertEquals(3, expression.getPrecedence());
    }

    @Test
    public void testIsSymmetric() {
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(new Expression[]{});
        assertFalse(expression.isSymmetric());
    }

    @Test
    public void testEvaluateCompare() {
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(new Expression[]{});
        assertTrue(expression.evaluateCompare(0));
    }

    @Test
    public void testCompute() {
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(new Expression[]{});
        assertTrue(expression.compute(1, 1));

        expression = new CoreOperationRelationalExpression(new Expression[]{});
        assertFalse(expression.compute(1, 2));
    }

    @Test
    public void testReduce() {
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(new Expression[]{});
        Object o = expression.reduce(new SelfContext());
        assertTrue(o instanceof EvalContext);

        expression = new CoreOperationRelationalExpression(new Expression[]{});
        o = expression.reduce(new HashSet<>());
        assertTrue(o instanceof Iterator);
    }

    @Test
    public void testContainsMatch() {
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(new Expression[]{});
        Iterator it = new HashSet<>().iterator();
        assertFalse(expression.containsMatch(it, new Object()));

        expression = new CoreOperationRelationalExpression(new Expression[]{});
        it = new HashSet<>().iterator();
        assertFalse(expression.containsMatch(it, null));

        expression = new CoreOperationRelationalExpression(new Expression[]{});
        it = new HashSet<>().iterator();
        assertFalse(expression.containsMatch(it, ""));

        expression = new CoreOperationRelationalExpression(new Expression[]{});
        it = new HashSet<>().iterator();
        assertFalse(expression.containsMatch(it, 1));
    }

    @Test
    public void testFindMatch() {
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(new Expression[]{});
        Iterator lit = new HashSet<>().iterator();
        Iterator rit = new HashSet<>().iterator();
        assertFalse(expression.findMatch(lit, rit));

        expression = new CoreOperationRelationalExpression(new Expression[]{});
        lit = new HashSet<>().iterator();
        rit = new HashSet<>().iterator();
        assertFalse(expression.findMatch(lit, rit));

        expression = new CoreOperationRelationalExpression(new Expression[]{});
        lit = new HashSet<>().iterator();
        rit = new HashSet<>().iterator();
        assertFalse(expression.findMatch(lit, rit));

        expression = new CoreOperationRelationalExpression(new Expression[]{});
        lit = new HashSet<>().iterator();
        rit = new HashSet<>().iterator();
        assertFalse(expression.findMatch(lit, rit));
    }

}