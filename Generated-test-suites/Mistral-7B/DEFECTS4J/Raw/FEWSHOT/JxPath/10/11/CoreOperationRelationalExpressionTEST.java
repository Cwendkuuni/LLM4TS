package org.apache.commons.jxpath.ri.compiler;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InfoSetUtil;
import org.apache.commons.jxpath.ri.axes.InitialContext;
import org.apache.commons.jxpath.ri.axes.SelfContext;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class CoreOperationRelationalExpressionTest {
    @Test
    public void testComputeValue() {
        CoreOperationRelationalExpression expr = new CoreOperationGreaterThan(new VariableExpression("a"), new VariableExpression("b"));

        Object result = expr.computeValue(new EvalContext(new SelfContext(1), null));
        assertEquals(Boolean.FALSE, result);

        result = expr.computeValue(new EvalContext(new SelfContext(2), null));
        assertEquals(Boolean.TRUE, result);

        result = expr.computeValue(new EvalContext(new SelfContext(1.5), null));
        assertEquals(Boolean.FALSE, result);

        result = expr.computeValue(new EvalContext(new SelfContext(1.6), null));
        assertEquals(Boolean.TRUE, result);

        result = expr.computeValue(new EvalContext(new InitialContext(), null));
        assertNull(result);

        result = expr.computeValue(new EvalContext(new InitialContext(), new Object[]{1, 2, 3}));
        assertNull(result);

        result = expr.computeValue(new EvalContext(new IteratorContext(new ArrayList(List.of(1, 2, 3)).iterator()), null));
        assertNull(result);
    }

    @Test
    public void testGetPrecedence() {
        CoreOperationRelationalExpression expr = new CoreOperationGreaterThan(new VariableExpression("a"), new VariableExpression("b"));
        assertEquals(3, expr.getPrecedence());
    }

    @Test
    public void testIsSymmetric() {
        CoreOperationRelationalExpression expr = new CoreOperationGreaterThan(new VariableExpression("a"), new VariableExpression("b"));
        assertFalse(expr.isSymmetric());
    }

    @Test
    public void testEvaluateCompare_Equals() {
        CoreOperationRelationalExpression expr = new CoreOperationGreaterThanOrEqual(new VariableExpression("a"), new VariableExpression("b"));
        assertTrue(expr.evaluateCompare(0));
    }

    @Test
    public void testEvaluateCompare_LessThan() {
        CoreOperationRelationalExpression expr = new CoreOperationGreaterThanOrEqual(new VariableExpression("a"), new VariableExpression("b"));
        assertFalse(expr.evaluateCompare(-1));
    }

    @Test
    public void testEvaluateCompare_GreaterThan() {
        CoreOperationRelationalExpression expr = new CoreOperationGreaterThanOrEqual(new VariableExpression("a"), new VariableExpression("b"));
        assertFalse(expr.evaluateCompare(1));
    }

    @Test
    public void testEvaluateCompare_LessThanOrEqual() {
        CoreOperationRelationalExpression expr = new CoreOperationLessThanOrEqual(new VariableExpression("a"), new VariableExpression("b"));
        assertTrue(expr.evaluateCompare(-1));
    }

    @Test
    public void testEvaluateCompare_GreaterThanOrEqual() {
        CoreOperationRelationalExpression expr = new CoreOperationLessThanOrEqual(new VariableExpression("a"), new VariableExpression("b"));
        assertFalse(expr.evaluateCompare(1));
    }

    @Test
    public void testEvaluateCompare_Equal() {
        CoreOperationRelationalExpression expr = new CoreOperationEqual(new VariableExpression("a"), new VariableExpression("b"));
        assertTrue(expr.evaluateCompare(0));
    }

    @Test
    public void testEvaluateCompare_NotEqual() {
        CoreOperationRelationalExpression expr = new CoreOperationNotEqual(new VariableExpression("a"), new VariableExpression("b"));
        assertFalse(expr.evaluateCompare(0));
    }

    @Test
    public void testCompute() {
        CoreOperationRelationalExpression expr = new CoreOperationGreaterThan(new VariableExpression("a"), new VariableExpression("b"));

        assertTrue(expr.compute(1, 2));
        assertFalse(expr.compute(1, 1));
        assertFalse(expr.compute(1.5, 1.6));
        assertTrue(expr.compute(1.6, 1.5));

        Object left = new InitialContext();
        Object right = new InitialContext();
        assertFalse(expr.compute(left, right));

        left = new IteratorContext(new ArrayList(List.of(1, 2, 3)).iterator());
        right = new Integer(3);
        assertFalse(expr.compute(left, right));

        left = new Integer(3);
        right = new IteratorContext(new ArrayList(List.of(1, 2, 3)).iterator());
        assertFalse(expr.compute(left, right));

        left = new Double(1.5);
        right = new Double(1.6);
        assertFalse(expr.compute(left, right));

        left = new Double(1.6);
        right = new Double(1.5);
        assertTrue(expr.compute(left, right));
    }

    @Test
    public void testReduce() {
        Object input = new SelfContext(1);
        Object result = CoreOperationRelationalExpression.this.reduce(input);
        assertEquals(input, result);

        input = new SelfContext(1.5);
        result = CoreOperationRelationalExpression.this.reduce(input);
        assertEquals(input, result);

        input = new InitialContext();
        result = CoreOperationRelationalExpression.this.reduce(input);
        assertNull(result);

        input = new ArrayList(List.of(1, 2, 3)).iterator();
        result = CoreOperationRelationalExpression.this.reduce(input);
        assertNull(result);
    }

    @Test
    public void testContainsMatch() {
        Set set = new HashSet();
        set.add(new Integer(1));
        set.add(new Integer(2));
        set.add(new Integer(3));

        Iterator it = set.iterator();

        assertTrue(CoreOperationRelationalExpression.this.containsMatch(it, new Integer(1)));
        assertTrue(CoreOperationRelationalExpression.this.containsMatch(it, new Integer(2)));
        assertTrue(CoreOperationRelationalExpression.this.containsMatch(it, new Integer(3)));

        assertFalse(CoreOperationRelationalExpression.this.containsMatch(it, new Integer(4)));
    }

    @Test
    public void testFindMatch() {
        Set setA = new HashSet();
        setA.add(new Integer(1));
        setA.add(new Integer(2));

        Set setB = new HashSet();
        setB.add(new Integer(1));
        setB.add(new Integer(3));

        Iterator itA = setA.iterator();
        Iterator itB = setB.iterator();

        assertTrue(CoreOperationRelationalExpression.this.findMatch(itA, itB));

        setB.add(new Integer(2));
        assertFalse(CoreOperationRelationalExpression.this.findMatch(itA, itB));

        itB = setB.iterator();
        assertFalse(CoreOperationRelationalExpression.this.findMatch(itA, itB));
    }
}