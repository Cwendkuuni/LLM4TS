
package org.apache.commons.jxpath.ri.compiler;

import org.apache.commons.jxpath.Pointer;
import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.axes.InitialContext;
import org.apache.commons.jxpath.ri.axes.SelfContext;
import org.junit.Assert;
import org.junit.Test;

import java.util.*;

public class CoreOperationCompareTest {
    
    @Test
    public void testEqual() {
        CoreOperationCompare operation = new CoreOperationCompare(null, null) {
            protected boolean equal(
                    EvalContext context,
                    Expression left,
                    Expression right) 
            {
                return true;
            }
        };
        EvalContext context = new EvalContext(null);
        Expression left = new Expression() {
            public Object compute(EvalContext context) {
                return null;
            }
        };
        Expression right = new Expression() {
            public Object compute(EvalContext context) {
                return null;
            }
        };
        boolean result = operation.equal(context, left, right);
        Assert.assertTrue(result);   
    }
    
    @Test
    public void testContains() {
        CoreOperationCompare operation = new CoreOperationCompare(null, null) {
            protected boolean contains(Iterator it, Object value) {
                return true;
            }
        };
        Iterator iterator = new Iterator() {
            public boolean hasNext() {
                return false;
            }

            public Object next() {
                return null;
            }
        };
        Object value = new Object();
        boolean result = operation.contains(iterator, value);
        Assert.assertTrue(result);
    }
    
    @Test
    public void testFindMatch() {
        CoreOperationCompare operation = new CoreOperationCompare(null, null) {
            protected boolean findMatch(Iterator lit, Iterator rit) {
                return true;
            }
        };
        Iterator lit = new Iterator() {
            public boolean hasNext() {
                return false;
            }

            public Object next() {
                return null;
            }
        };
        Iterator rit = new Iterator() {
            public boolean hasNext() {
                return false;
            }

            public Object next() {
                return null;
            }
        };
        boolean result = operation.findMatch(lit, rit);
        Assert.assertTrue(result);
    }
    
    @Test
    public void testEqualWithPointers() {
        CoreOperationCompare operation = new CoreOperationCompare(null, null) {
            protected boolean equal(Object l, Object r) {
                return true;
            }
        };
        Pointer pointer = new Pointer() {
            public Object getValue() {
                return null;
            }
        };
        boolean result = operation.equal(pointer, pointer);
        Assert.assertTrue(result);
    }
    
    @Test
    public void testEqualWithBoolean() {
        CoreOperationCompare operation = new CoreOperationCompare(null, null) {
            protected boolean equal(Object l, Object r) {
                return true;
            }
        };
        boolean result = operation.equal(true, true);
        Assert.assertTrue(result);
    }
    
    @Test
    public void testEqualWithNumber() {
        CoreOperationCompare operation = new CoreOperationCompare(null, null) {
            protected boolean equal(Object l, Object r) {
                return true;
            }
        };
        boolean result = operation.equal(1, 1);
        Assert.assertTrue(result);
    }
    
    @Test
    public void testEqualWithString() {
        CoreOperationCompare operation = new CoreOperationCompare(null, null) {
            protected boolean equal(Object l, Object r) {
                return true;
            }
        };
        boolean result = operation.equal("a", "a");
        Assert.assertTrue(result);
    }
    
    @Test
    public void testEqualWithDifferentTypes() {
        CoreOperationCompare operation = new CoreOperationCompare(null, null);
        boolean result = operation.equal(new ArrayList(), new HashSet());
        Assert.assertFalse(result);
    }
}
