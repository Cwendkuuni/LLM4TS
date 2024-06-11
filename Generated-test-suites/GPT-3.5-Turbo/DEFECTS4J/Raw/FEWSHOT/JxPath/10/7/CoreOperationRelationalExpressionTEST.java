package org.apache.commons.jxpath.ri.compiler;

import static org.junit.Assert.assertEquals;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InfoSetUtil;
import org.apache.commons.jxpath.ri.axes.InitialContext;
import org.apache.commons.jxpath.ri.axes.SelfContext;
import org.junit.Test;

public class CoreOperationRelationalExpressionTest {

    @Test
    public void testComputeValue() {
        Expression[] args = new Expression[2];
        args[0] = new CoreOperationRelationalExpressionTest.MyExpression();
        args[1] = new CoreOperationRelationalExpressionTest.MyExpression();
        
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpressionTest.CoreOperationRelationalExpressionImpl(args);
        
        EvalContext context = new CoreOperationRelationalExpressionTest.EvalContextImpl();
        
        Object result = expression.computeValue(context);
        
        assertEquals(Boolean.TRUE, result);
    }
    
    @Test
    public void testGetPrecedence() {
        Expression[] args = new Expression[2];
        args[0] = new CoreOperationRelationalExpressionTest.MyExpression();
        args[1] = new CoreOperationRelationalExpressionTest.MyExpression();
        
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpressionTest.CoreOperationRelationalExpressionImpl(args);
        
        int precedence = expression.getPrecedence();
        
        assertEquals(3, precedence);
    }
    
    @Test
    public void testIsSymmetric() {
        Expression[] args = new Expression[2];
        args[0] = new CoreOperationRelationalExpressionTest.MyExpression();
        args[1] = new CoreOperationRelationalExpressionTest.MyExpression();
        
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpressionTest.CoreOperationRelationalExpressionImpl(args);
        
        boolean isSymmetric = expression.isSymmetric();
        
        assertEquals(false, isSymmetric);
    }
    
    @Test
    public void testEvaluateCompare() {
        Expression[] args = new Expression[2];
        args[0] = new CoreOperationRelationalExpressionTest.MyExpression();
        args[1] = new CoreOperationRelationalExpressionTest.MyExpression();
        
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpressionTest.CoreOperationRelationalExpressionImpl(args);
        
        boolean isCompare = expression.evaluateCompare(0);
        
        assertEquals(false, isCompare);
    }
    
    @Test
    public void testCompute() {
        Expression[] args = new Expression[2];
        args[0] = new CoreOperationRelationalExpressionTest.MyExpression();
        args[1] = new CoreOperationRelationalExpressionTest.MyExpression();
        
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpressionTest.CoreOperationRelationalExpressionImpl(args);
        
        Object result = expression.compute(new Object(), new Object());
        
        assertEquals(Boolean.FALSE, result);
    }
    
    @Test
    public void testReduce() {
        Expression[] args = new Expression[2];
        args[0] = new CoreOperationRelationalExpressionTest.MyExpression();
        args[1] = new CoreOperationRelationalExpressionTest.MyExpression();
        
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpressionTest.CoreOperationRelationalExpressionImpl(args);
        
        Object result = expression.reduce(new CoreOperationRelationalExpressionTest.MyExpression());
        
        assertEquals(new CoreOperationRelationalExpressionTest.MyExpression(), result);
    }
    
    @Test
    public void testContainsMatch() {
        Expression[] args = new Expression[2];
        args[0] = new CoreOperationRelationalExpressionTest.MyExpression();
        args[1] = new CoreOperationRelationalExpressionTest.MyExpression();
        
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpressionTest.CoreOperationRelationalExpressionImpl(args);
        
        Set<Object> set = new HashSet<>();
        set.add(new CoreOperationRelationalExpressionTest.MyExpression());
        Iterator<Object> it = set.iterator();
        Object value = new CoreOperationRelationalExpressionTest.MyExpression();
        
        boolean match = expression.containsMatch(it, value);
        
        assertEquals(true, match);
    }
    
    @Test
    public void testFindMatch() {
        Expression[] args = new Expression[2];
        args[0] = new CoreOperationRelationalExpressionTest.MyExpression();
        args[1] = new CoreOperationRelationalExpressionTest.MyExpression();
        
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpressionTest.CoreOperationRelationalExpressionImpl(args);
        
        Set<Object> left = new HashSet<>();
        left.add(new CoreOperationRelationalExpressionTest.MyExpression());
        Iterator<Object> lit = left.iterator();
        Iterator<Object> rit = left.iterator();
        
        boolean match = expression.findMatch(lit, rit);
        
        assertEquals(true, match);
    }
    
    public static class MyExpression extends Expression {
        @Override
        public Object computeValue(EvalContext context) {
            return new Object();
        }
    }
    
    public static class CoreOperationRelationalExpressionImpl extends CoreOperationRelationalExpression {
        public CoreOperationRelationalExpressionImpl(Expression[] args) {
            super(args);
        }
        
        @Override
        protected boolean evaluateCompare(int compare) {
            return false;
        }
    }
    
    public static class EvalContextImpl extends EvalContext {
        @Override
        public Object getSingleNodePointer() {
            return new Object();
        }
    }
}