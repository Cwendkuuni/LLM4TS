package org.apache.commons.jxpath.ri.compiler;

import org.apache.commons.jxpath.Pointer;
import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InfoSetUtil;
import org.apache.commons.jxpath.ri.axes.InitialContext;
import org.apache.commons.jxpath.ri.axes.SelfContext;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;

public class CoreOperationCompareTest {
    @Test
    public void testEqual() {
        EvalContext context = new EvalContext();
        Expression arg1 = new Expression();
        Expression arg2 = new Expression();
        CoreOperationCompare operator = new CoreOperationCompare(arg1, arg2);
        
        // Test when l and r are Pointer and they are equal
        Pointer pointer1 = new Pointer();
        Pointer pointer2 = new Pointer();
        assertFalse(operator.equal(context, pointer1, pointer2));
        
        // Test when l and r are Pointer and they are not equal
        Pointer pointer3 = new Pointer();
        assertFalse(operator.equal(context, pointer1, pointer3));
        
        // Test when l and r are Boolean and isEqual returns true
        Object l1 = true;
        Object r1 = true;
        assertTrue(operator.equal(l1, r1));
        
        // Test when l and r are Boolean and isEqual returns false
        Object l2 = true;
        Object r2 = false;
        assertFalse(operator.equal(l2, r2));
        
        // Test when l is a Number and r is a Boolean
        Object l3 = 1;
        Object r3 = true;
        assertFalse(operator.equal(l3, r3));
        
        // Test when l and r are Number and isEqual returns true
        Object l4 = 1;
        Object r4 = 1;
        assertTrue(operator.equal(l4, r4));
        
        // Test when l and r are Number and isEqual returns false
        Object l5 = 1;
        Object r5 = 2;
        assertFalse(operator.equal(l5, r5));
        
        // Test when l is a String and r is a Number
        Object l6 = "1";
        Object r6 = 1;
        assertFalse(operator.equal(l6, r6));
        
        // Test when l and r are String and isEqual returns true
        Object l7 = "1";
        Object r7 = "1";
        assertTrue(operator.equal(l7, r7));
        
        // Test when l and r are String and isEqual returns false
        Object l8 = "1";
        Object r8 = "2";
        assertFalse(operator.equal(l8, r8));
        
        // Test when l and r are not equal
        Object l9 = new Object();
        Object r9 = new Object();
        assertFalse(operator.equal(l9, r9));
    }
    
    @Test
    public void testContains() {
        CoreOperationCompare operator = new CoreOperationCompare(null, null);
        Iterator<Object> iterator = new Iterator<Object>() {
            @Override
            public boolean hasNext() {
                return true;
            }

            @Override
            public Object next() {
                return null;
            }
        };
        
        // Test when iterator contains the value
        Object value = new Object();
        assertTrue(operator.contains(iterator, value));
        
        // Test when iterator does not contain the value
        Object value2 = new Object();
        iterator = new Iterator<Object>() {
            @Override
            public boolean hasNext() {
                return false;
            }

            @Override
            public Object next() {
                return null;
            }
        };
        assertFalse(operator.contains(iterator, value2));
    }
    
    @Test
    public void testFindMatch() {
        CoreOperationCompare operator = new CoreOperationCompare(null, null);
        Iterator<Object> lit = new Iterator<Object>() {
            @Override
            public boolean hasNext() {
                return true;
            }

            @Override
            public Object next() {
                return null;
            }
        };
        
        // Test when rit contains a match
        Iterator<Object> rit = new Iterator<Object>() {
            @Override
            public boolean hasNext() {
                return true;
            }

            @Override
            public Object next() {
                return null;
            }
        };
        assertTrue(operator.findMatch(lit, rit));
        
        // Test when rit does not contain a match
        Iterator<Object> rit2 = new Iterator<Object>() {
            @Override
            public boolean hasNext() {
                return false;
            }

            @Override
            public Object next() {
                return null;
            }
        };
        assertFalse(operator.findMatch(lit, rit2));
    }
    

}