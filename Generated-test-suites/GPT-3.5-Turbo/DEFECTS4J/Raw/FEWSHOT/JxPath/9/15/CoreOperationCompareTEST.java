package org.apache.commons.jxpath.ri.compiler;

import static org.junit.Assert.*;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;

import org.apache.commons.jxpath.Pointer;
import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InfoSetUtil;
import org.apache.commons.jxpath.ri.axes.InitialContext;
import org.apache.commons.jxpath.ri.axes.SelfContext;
import org.junit.Test;

public class CoreOperationCompareTest {

    @Test
    public void testCompute() {
        Expression arg1 = new Expression();
        Expression arg2 = new Expression();
        
        CoreOperationCompare operation = new CoreOperationCompare(arg1, arg2);
        
        EvalContext context = new EvalContext();
        
        Object result = operation.compute(context);
        
        assertNotNull(result);
    }
    
    @Test
    public void testGetPrecedence() {
        Expression arg1 = new Expression();
        Expression arg2 = new Expression();
        
        CoreOperationCompare operation = new CoreOperationCompare(arg1, arg2);
        
        int precedence = operation.getPrecedence();
        
        assertEquals(2, precedence);
    }
    
    @Test
    public void testIsSymmetric() {
        Expression arg1 = new Expression();
        Expression arg2 = new Expression();
        
        CoreOperationCompare operation = new CoreOperationCompare(arg1, arg2);
        
        boolean symmetric = operation.isSymmetric();
        
        assertTrue(symmetric);
    }
    
    @Test
    public void testEqual() {
        Expression arg1 = new Expression();
        Expression arg2 = new Expression();
        
        CoreOperationCompare operation = new CoreOperationCompare(arg1, arg2);
        
        EvalContext context = new EvalContext();
        
        boolean result = operation.equal(context, arg1, arg2);
        
        assertTrue(result);
    }
    
    @Test
    public void testContains() {
        Iterator it = new Iterator() {
            @Override
            public boolean hasNext() {
                return false;
            }

            @Override
            public Object next() {
                return null;
            }
        };
        
        Object value = null;
        
        Expression arg1 = new Expression();
        Expression arg2 = new Expression();
        
        CoreOperationCompare operation = new CoreOperationCompare(arg1, arg2);
        
        boolean result = operation.contains(it, value);
        
        assertFalse(result);
    }
    
    @Test
    public void testFindMatch() {
        Iterator lit = new Iterator() {
            @Override
            public boolean hasNext() {
                return false;
            }

            @Override
            public Object next() {
                return null;
            }
        };
        
        Iterator rit = new Iterator() {
            @Override
            public boolean hasNext() {
                return false;
            }

            @Override
            public Object next() {
                return null;
            }
        };
        
        Expression arg1 = new Expression();
        Expression arg2 = new Expression();
        
        CoreOperationCompare operation = new CoreOperationCompare(arg1, arg2);
        
        boolean result = operation.findMatch(lit, rit);
        
        assertFalse(result);
    }
    
    @Test
    public void testEqualWithPointers() {
        Object l = new Object();
        Object r = new Object();
        
        CoreOperationCompare operation = new CoreOperationCompare(null, null);
        
        boolean result = operation.equal(l, r);
        
        assertFalse(result);
    }
    
    @Test
    public void testEqualWithBooleanValues() {
        Object l = new Boolean(true);
        Object r = new Boolean(false);
        
        CoreOperationCompare operation = new CoreOperationCompare(null, null);
        
        boolean result = operation.equal(l, r);
        
        assertFalse(result);
    }
    
    @Test
    public void testEqualWithNumbers() {
        Object l = new Integer(1);
        Object r = new Integer(2);
        
        CoreOperationCompare operation = new CoreOperationCompare(null, null);
        
        boolean result = operation.equal(l, r);
        
        assertFalse(result);
    }
    
    @Test
    public void testEqualWithStrings() {
        Object l = new String("Hello");
        Object r = new String("World");
        
        CoreOperationCompare operation = new CoreOperationCompare(null, null);
        
        boolean result = operation.equal(l, r);
        
        assertFalse(result);
    }
    
    @Test
    public void testEqualWithNullValues() {
        Object l = null;
        Object r = null;
        
        CoreOperationCompare operation = new CoreOperationCompare(null, null);
        
        boolean result = operation.equal(l, r);
        
        assertTrue(result);
    }

}