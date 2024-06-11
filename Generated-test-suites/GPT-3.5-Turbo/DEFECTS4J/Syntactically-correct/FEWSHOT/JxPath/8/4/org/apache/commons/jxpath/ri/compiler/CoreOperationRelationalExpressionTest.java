package org.apache.commons.jxpath.ri.compiler;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.axes.InitialContext;
import org.apache.commons.jxpath.ri.axes.SelfContext;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;

public class CoreOperationRelationalExpressionTest {
    
    @Test
    public void testComputeValue() {
        Expression[] args = new Expression[] {new TestExpression(), new TestExpression()};
        CoreOperationRelationalExpression expression = new TestCoreOperationRelationalExpression(args);
        
        EvalContext context = new EvalContext();
        
        args[0].computeValue(context);
        args[1].computeValue(context);
        
        assertTrue(expression.computeValue(context));
        
    }
    
    @Test
    public void testGetPrecedence() {
        Expression[] args = new Expression[] {new TestExpression(), new TestExpression()};
        CoreOperationRelationalExpression expression = new TestCoreOperationRelationalExpression(args);
        
        assertEquals(3, expression.getPrecedence());
    }
    
    @Test
    public void testIsSymmetric() {
        Expression[] args = new Expression[] {new TestExpression(), new TestExpression()};
        CoreOperationRelationalExpression expression = new TestCoreOperationRelationalExpression(args);
        
        assertFalse(expression.isSymmetric());
    }
    
    @Test
    public void testEvaluateCompare() {
        Expression[] args = new Expression[] {new TestExpression(), new TestExpression()};
        TestCoreOperationRelationalExpression expression = new TestCoreOperationRelationalExpression(args);
        
        assertTrue(expression.evaluateCompare(0));
    }
    
    @Test
    public void testCompute() {
        Expression[] args = new Expression[] {new TestExpression(), new TestExpression()};
        TestCoreOperationRelationalExpression expression = new TestCoreOperationRelationalExpression(args);
        
        Object result = expression.compute(new InitialContext(), new InitialContext());
        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result);
        
        result = expression.compute(new InitialContext(), new TestExpression());
        assertTrue(result instanceof Boolean);
        assertFalse((Boolean) result);
        
        result = expression.compute(new TestExpression(), new InitialContext());
        assertTrue(result instanceof Boolean);
        assertFalse((Boolean) result);
        
        result = expression.compute(new TestExpression(), new TestExpression());
        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result);
    }
    
    @Test
    public void testReduce() {
        Expression[] args = new Expression[0];
        TestCoreOperationRelationalExpression expression = new TestCoreOperationRelationalExpression(args);
        
        Object result = expression.reduce(new InitialContext());
        assertTrue(result instanceof InitialContext);
        
        result = expression.reduce(new SelfContext());
        assertTrue(result instanceof SelfContext);
        
        result = expression.reduce(new ArrayList<>());
        assertTrue(result instanceof Iterator);
        
        result = expression.reduce(new HashSet<>());
        assertTrue(result instanceof Iterator);
    }
    
    @Test
    public void testContainsMatch() {
        Expression[] args = new Expression[0];
        TestCoreOperationRelationalExpression expression = new TestCoreOperationRelationalExpression(args);
        
        Iterator it = new ArrayList<Object>().iterator();
        assertFalse(expression.containsMatch(it, new TestExpression()));
        
        it = new ArrayList<Object>() {{
            add(new TestExpression());
        }}.iterator();
        assertTrue(expression.containsMatch(it, new TestExpression()));
        
        it = new ArrayList<Object>() {{
            add(new TestExpression());
        }}.iterator();
        assertFalse(expression.containsMatch(it, new InitialContext()));
        
    }
    
    @Test
    public void testFindMatch() {
        Expression[] args = new Expression[0];
        TestCoreOperationRelationalExpression expression = new TestCoreOperationRelationalExpression(args);
        
        Iterator lit = new ArrayList<Object>() {{
            add(new TestExpression());
        }}.iterator();
        Iterator rit = new HashSet<Object>() {{
            add(new TestExpression());
        }}.iterator();
        assertTrue(expression.findMatch(lit, rit));
        
        lit = new ArrayList<Object>() {{
            add(new InitialContext());
        }}.iterator();
        rit = new HashSet<Object>() {{
            add(new TestExpression());
        }}.iterator();
        assertFalse(expression.findMatch(lit, rit));
    }
    
    private static class TestExpression implements Expression {
        public Object computeValue(EvalContext context) {
            return new Object();
        }
    }
    
    private static class TestCoreOperationRelationalExpression extends CoreOperationRelationalExpression {
        public TestCoreOperationRelationalExpression(Expression[] args) {
            super(args);
        }

        @Override
        protected boolean evaluateCompare(int compare) {
            return compare == 0;
        }
    }
}