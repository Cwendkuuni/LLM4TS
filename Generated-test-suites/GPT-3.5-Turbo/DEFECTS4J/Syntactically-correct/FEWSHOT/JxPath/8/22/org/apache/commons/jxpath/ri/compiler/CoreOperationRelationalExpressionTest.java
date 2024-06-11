package org.apache.commons.jxpath.ri.compiler;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InfoSetUtil;
import org.apache.commons.jxpath.ri.axes.InitialContext;
import org.apache.commons.jxpath.ri.axes.NodeSetContext;
import org.apache.commons.jxpath.ri.axes.SelfContext;
import org.junit.Test;

public class CoreOperationRelationalExpressionTest {

    @Test
    public void testComputeValue() {
        Expression[] args = new Expression[2];
        Expression left = new CoreOperationRelationalExpressionTest.DummyExpression(5);
        Expression right = new CoreOperationRelationalExpressionTest.DummyExpression(3);
        args[0] = left;
        args[1] = right;
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpressionTest.DummyCoreOperationRelationalExpression(args);
        
        EvalContext context = new InitialContext(null);
        assertEquals(Boolean.TRUE, expression.computeValue(context));
    
        right = new CoreOperationRelationalExpressionTest.DummyExpression(7);
        args[1] = right;
        expression = new CoreOperationRelationalExpressionTest.DummyCoreOperationRelationalExpression(args);
        
        context = new InitialContext(null);
        assertEquals(Boolean.FALSE, expression.computeValue(context));
    }

    @Test
    public void testGetPrecedence() {
        Expression[] args = new Expression[2];
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpressionTest.DummyCoreOperationRelationalExpression(args);
        
        assertEquals(3, expression.getPrecedence());
    }

    @Test
    public void testIsSymmetric() {
        Expression[] args = new Expression[2];
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpressionTest.DummyCoreOperationRelationalExpression(args);
        
        assertFalse(expression.isSymmetric());
    }

    @Test
    public void testCompute() {
        Expression[] args = new Expression[2];
        Expression left = new CoreOperationRelationalExpressionTest.DummyExpression(5);
        Expression right = new CoreOperationRelationalExpressionTest.DummyExpression(3);
        args[0] = left;
        args[1] = right;
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpressionTest.DummyCoreOperationRelationalExpression(args);
        
        assertTrue(expression.compute(5, 3));
        assertFalse(expression.compute(5, 7));
        
        left = new CoreOperationRelationalExpressionTest.DummyExpression(Arrays.asList(1, 2, 3, 4));
        right = new CoreOperationRelationalExpressionTest.DummyExpression(3);
        args[0] = left;
        args[1] = right;
        expression = new CoreOperationRelationalExpressionTest.DummyCoreOperationRelationalExpression(args);
        
        assertTrue(expression.compute(Arrays.asList(1, 2, 3, 4), 3));
        assertFalse(expression.compute(Arrays.asList(1, 2, 4), 3));
        
        left = new CoreOperationRelationalExpressionTest.DummyExpression(Arrays.asList(1, 2, 3, 4));
        right = new CoreOperationRelationalExpressionTest.DummyExpression(Arrays.asList(4, 5, 6));
        args[0] = left;
        args[1] = right;
        expression = new CoreOperationRelationalExpressionTest.DummyCoreOperationRelationalExpression(args);
        
        assertTrue(expression.compute(Arrays.asList(1, 2, 3, 4), Arrays.asList(4, 5, 6)));
        assertFalse(expression.compute(Arrays.asList(1, 2, 3), Arrays.asList(4, 5, 6)));
        
        left = new CoreOperationRelationalExpressionTest.DummyExpression(3.5);
        right = new CoreOperationRelationalExpressionTest.DummyExpression(2.5);
        args[0] = left;
        args[1] = right;
        expression = new CoreOperationRelationalExpressionTest.DummyCoreOperationRelationalExpression(args);
        
        assertTrue(expression.compute(3.5, 2.5));
        assertFalse(expression.compute(1.5, 2.5));
    }

    @Test
    public void testReduce() {
        Expression[] args = new Expression[2];
        Expression left = new CoreOperationRelationalExpressionTest.DummyExpression(Arrays.asList(1, 2, 3, 4));
        Expression right = new CoreOperationRelationalExpressionTest.DummyExpression(3);
        args[0] = left;
        args[1] = right;
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpressionTest.DummyCoreOperationRelationalExpression(args);
        
        assertTrue(expression.reduce(Arrays.asList(1, 2, 3, 4)) instanceof Iterator);
        assertTrue(expression.reduce(new NodeSetContext(null, null, null, true)) instanceof Iterator);
        assertTrue(expression.reduce(new SelfContext(null, null, null, true)) instanceof Object);
        assertTrue(expression.reduce(null) == null);
    }

    @Test
    public void testContainsMatch() {
        Expression[] args = new Expression[2];
        Expression left = new CoreOperationRelationalExpressionTest.DummyExpression(Arrays.asList(1, 2, 3, 4));
        Expression right = new CoreOperationRelationalExpressionTest.DummyExpression(3);
        args[0] = left;
        args[1] = right;
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpressionTest.DummyCoreOperationRelationalExpression(args);
        
        Iterator it = Arrays.asList(1, 2, 3, 4).iterator();
        assertTrue(expression.containsMatch(it, 3));
        
        it = Arrays.asList(1, 2, 4).iterator();
        assertFalse(expression.containsMatch(it, 3));
    }

    @Test
    public void testFindMatch() {
        Expression[] args = new Expression[2];
        Expression left = new CoreOperationRelationalExpressionTest.DummyExpression(Arrays.asList(1, 2, 3, 4));
        Expression right = new CoreOperationRelationalExpressionTest.DummyExpression(Arrays.asList(4, 5, 6));
        args[0] = left;
        args[1] = right;
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpressionTest.DummyCoreOperationRelationalExpression(args);
        
        Iterator leftIt = Arrays.asList(1, 2, 3, 4).iterator();
        Iterator rightIt = Arrays.asList(4, 5, 6).iterator();
        assertTrue(expression.findMatch(leftIt, rightIt));
        
        leftIt = Arrays.asList(1, 2, 3).iterator();
        rightIt = Arrays.asList(4, 5, 6).iterator();
        assertFalse(expression.findMatch(leftIt, rightIt));
    }

    private static class DummyCoreOperationRelationalExpression extends CoreOperationRelationalExpression {

        public DummyCoreOperationRelationalExpression(Expression[] args) {
            super(args);
        }

        @Override
        protected boolean evaluateCompare(int compare) {
            return compare > 0;
        }

    }
    
    private static class DummyExpression extends Expression {

        private Object value;
        
        public DummyExpression(Object value) {
            this.value = value;
        }
        
        @Override
        public Object computeValue(EvalContext context) {
            return value;
        }
        
    }
}