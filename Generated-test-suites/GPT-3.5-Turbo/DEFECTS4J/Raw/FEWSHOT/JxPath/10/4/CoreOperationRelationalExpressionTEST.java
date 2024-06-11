package org.apache.commons.jxpath.ri.compiler;

import static org.junit.Assert.*;
import java.util.ArrayList;
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
        EvalContext context = new EvalContext();
        Expression[] args = new Expression[2];
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(args) {
            protected boolean evaluateCompare(int compare) {
                return compare > 0;
            }
        };
        
        args[0] = new Expression() {
            public Object computeValue(EvalContext context) {
                return 5;
            }
        };
        
        args[1] = new Expression() {
            public Object computeValue(EvalContext context) {
                return 10;
            }
        };
        
        assertFalse(expression.computeValue(context));
    }
    
    @Test
    public void testGetPrecedence() {
        Expression[] args = new Expression[2];
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(args) {
            protected boolean evaluateCompare(int compare) {
                return compare > 0;
            }
        };
        
        assertEquals(3, expression.getPrecedence());
    }
    
    @Test
    public void testIsSymmetric() {
        Expression[] args = new Expression[2];
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(args) {
            protected boolean evaluateCompare(int compare) {
                return compare > 0;
            }
        };
        
        assertFalse(expression.isSymmetric());
    }
    
    @Test
    public void testCompute_LeftIteratorRightIterator() {
        Expression[] args = new Expression[2];
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(args) {
            protected boolean evaluateCompare(int compare) {
                return compare > 0;
            }
        };
        
        Iterator<Object> left = Arrays.asList(1, 2, 3).iterator();
        Iterator<Object> right = Arrays.asList(4, 5, 6).iterator();
        assertTrue(expression.compute(left, right));
        
        left = Arrays.asList(1, 2, 3).iterator();
        right = Arrays.asList(2, 3, 4).iterator();
        assertFalse(expression.compute(left, right));
    }
    
    @Test
    public void testCompute_LeftIteratorRightObject() {
        Expression[] args = new Expression[2];
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(args) {
            protected boolean evaluateCompare(int compare) {
                return compare > 0;
            }
        };
        
        Iterator<Object> left = Arrays.asList(1, 2, 3).iterator();
        Object right = 1;
        assertTrue(expression.compute(left, right));
        
        left = Arrays.asList(1, 2, 3).iterator();
        right = 4;
        assertFalse(expression.compute(left, right));
    }
    
    @Test
    public void testCompute_LeftObjectRightIterator() {
        Expression[] args = new Expression[2];
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(args) {
            protected boolean evaluateCompare(int compare) {
                return compare > 0;
            }
        };
        
        Object left = 3;
        Iterator<Object> right = Arrays.asList(1, 2, 3).iterator();
        assertFalse(expression.compute(left, right));
        
        left = 4;
        right = Arrays.asList(1, 2, 3).iterator();
        assertFalse(expression.compute(left, right));
    }
    
    @Test
    public void testCompute_LeftObjectRightObject() {
        Expression[] args = new Expression[2];
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(args) {
            protected boolean evaluateCompare(int compare) {
                return compare > 0;
            }
        };
        
        Object left = 5;
        Object right = 10;
        assertFalse(expression.compute(left, right));
        
        left = 10;
        right = 5;
        assertTrue(expression.compute(left, right));
    }
    
    @Test
    public void testReduce_SelfContext() {
        Expression[] args = new Expression[2];
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(args) {
            protected boolean evaluateCompare(int compare) {
                return compare > 0;
            }
        };
        
        SelfContext selfContext = new SelfContext();
        Object result = expression.reduce(selfContext);
        assertEquals(selfContext, result);
    }
    
    @Test
    public void testReduce_Collection() {
        Expression[] args = new Expression[2];
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(args) {
            protected boolean evaluateCompare(int compare) {
                return compare > 0;
            }
        };
        
        Collection<Object> collection = new ArrayList<>();
        collection.add(1);
        collection.add(2);
        Object result = expression.reduce(collection);
        Iterator<?> iterator = collection.iterator();
        assertEquals(iterator, result);
    }
    
    @Test
    public void testReduce_Object() {
        Expression[] args = new Expression[2];
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(args) {
            protected boolean evaluateCompare(int compare) {
                return compare > 0;
            }
        };
        
        Object object = new Object();
        Object result = expression.reduce(object);
        assertEquals(object, result);
    }
    
    @Test
    public void testContainsMatch() {
        Expression[] args = new Expression[2];
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(args) {
            protected boolean evaluateCompare(int compare) {
                return compare > 0;
            }
        };
        
        Iterator<Object> iterator = Arrays.asList(1, 2, 3).iterator();
        assertTrue(expression.containsMatch(iterator, 1));
        assertFalse(expression.containsMatch(iterator, 4));
    }
    
    @Test
    public void testFindMatch() {
        Expression[] args = new Expression[2];
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(args) {
            protected boolean evaluateCompare(int compare) {
                return compare > 0;
            }
        };
        
        Iterator<Object> left = Arrays.asList(1, 2, 3).iterator();
        Iterator<Object> right = Arrays.asList(4, 5, 6).iterator();
        assertFalse(expression.findMatch(left, right));
        
        left = Arrays.asList(1, 2, 3).iterator();
        right = Arrays.asList(2, 3, 4).iterator();
        assertTrue(expression.findMatch(left, right));
    }
}