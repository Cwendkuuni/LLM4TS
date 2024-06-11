package org.apache.commons.jxpath.ri.compiler;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class CoreOperationRelationalExpressionTest {

    @Test
    public void testComputeValue() {
        // Create test objects
        Object left = null;
        Object right = null;
        CoreOperationRelationalExpression expr = new ConcreteCoreOperationRelationalExpression();

        // Test cases
        assertEquals(expr.computeValue(left, right), Boolean.TRUE);
        assertEquals(expr.computeValue(left, right), Boolean.FALSE);
        assertEquals(expr.computeValue(left, right), Boolean.TRUE);
        assertEquals(expr.computeValue(left, right), Boolean.FALSE);
    }

    @Test
    public void testGetPrecedence() {
        CoreOperationRelationalExpression expr = new ConcreteCoreOperationRelationalExpression();
        assertEquals(expr.getPrecedence(), CoreOperation.RELATIONAL_EXPR_PRECEDENCE);
    }

    @Test
    public void testIsSymmetric() {
        CoreOperationRelationalExpression expr = new ConcreteCoreOperationRelationalExpression();
        assertEquals(expr.isSymmetric(), false);
    }

    @Test
    public void testReduce() {
        // Create test objects
        Object o = null;
        CoreOperationRelationalExpression expr = new ConcreteCoreOperationRelationalExpression();

        // Test cases
        assertEquals(expr.reduce(o), o);
    }

    @Test
    public void testContainsMatch() {
        // Create test objects
        Iterator it = null;
        Object value = null;
        CoreOperationRelationalExpression expr = new ConcreteCoreOperationRelationalExpression();

        // Test cases
        assertEquals(expr.containsMatch(it, value), false);
    }

    @Test
    public void testFindMatch() {
        // Create test objects
        Iterator lit = null;
        Iterator rit = null;
        CoreOperationRelationalExpression expr = new ConcreteCoreOperationRelationalExpression();

        // Test cases
        assertEquals(expr.findMatch(lit, rit), false);
    }
}