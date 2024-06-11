package org.apache.commons.jxpath.ri.compiler;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InfoSetUtil;
import org.apache.commons.jxpath.ri.axes.InitialContext;
import org.apache.commons.jxpath.ri.axes.SelfContext;
import org.junit.Test;

public class CoreOperationRelationalExpressionTest {
    
    @Test
    public void testComputeValue() {
        Expression[] args1 = new Expression[2];
        EvalContext context = new EvalContext();
        
        InitialContext initialContextLeft = new InitialContext(context);
        initialContextLeft.reset();
        InitialContext initialContextRight = new InitialContext(context);
        initialContextRight.reset();
        
        args1[0] = new CoreOperationRelationalExpressionTestWrapper(new Object(), initialContextLeft);
        args1[1] = new CoreOperationRelationalExpressionTestWrapper(new Object(), initialContextRight);
        
        CoreOperationRelationalExpression relation = new CoreOperationRelationalExpressionTestWrapper(args1);
        
        assertEquals(Boolean.FALSE, relation.computeValue(context));
    }
    
    @Test
    public void testGetPrecedence() {
        CoreOperationRelationalExpression relation = new CoreOperationRelationalExpressionTestWrapper(null);
        assertEquals(3, relation.getPrecedence());
    }
    
    @Test
    public void testIsSymmetric() {
        CoreOperationRelationalExpression relation = new CoreOperationRelationalExpressionTestWrapper(null);
        assertFalse(relation.isSymmetric());
    }
    
    @Test
    public void testEvaluateCompare() {
        CoreOperationRelationalExpression relation = new CoreOperationRelationalExpressionTestWrapper(null);
        assertTrue(relation.evaluateCompare(0));
        assertFalse(relation.evaluateCompare(-1));
        assertFalse(relation.evaluateCompare(1));
    }
    
    @Test
    public void testCompute() {
        CoreOperationRelationalExpression relation = new CoreOperationRelationalExpressionTestWrapper(null);
        Object result1 = relation.compute(null, null);
        Object result2 = relation.compute(new Object(), new Object());
        assertFalse((Boolean)result1);
        assertFalse((Boolean)result2);
    }
    
    @Test
    public void testReduce() {
        CoreOperationRelationalExpression relation = new CoreOperationRelationalExpressionTestWrapper(null);
        Object result1 = relation.reduce(new SelfContext());
        Object result2 = relation.reduce(new HashSet<>());
        Object result3 = relation.reduce(Arrays.asList("1", "2"));
        assertTrue(result1 instanceof SelfContext);
        assertTrue(result2 instanceof Iterator);
        assertTrue(result3 instanceof Iterator);
    }
    
    @Test
    public void testContainsMatch() {
        CoreOperationRelationalExpression relation = new CoreOperationRelationalExpressionTestWrapper(null);
        Iterator it = Arrays.asList("1", "2", "3").iterator();
        boolean result = relation.containsMatch(it, "2");
        assertTrue(result);
    }
    
    @Test
    public void testFindMatch() {
        CoreOperationRelationalExpression relation = new CoreOperationRelationalExpressionTestWrapper(null);
        Iterator lit = Arrays.asList("1", "2", "3").iterator();
        Iterator rit = Arrays.asList("4", "2", "5").iterator();
        boolean result = relation.findMatch(lit, rit);
        assertTrue(result);
    }

    private class CoreOperationRelationalExpressionTestWrapper extends CoreOperationRelationalExpression {
        
        public CoreOperationRelationalExpressionTestWrapper(Expression[] args) {
            super(args);
        }

        @Override
        protected boolean evaluateCompare(int compare) {
            return compare == 0;
        }
    }
}