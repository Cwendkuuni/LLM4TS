package org.apache.commons.jxpath.ri.compiler;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;

import org.apache.commons.jxpath.Pointer;
import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.axes.InitialContext;
import org.apache.commons.jxpath.ri.axes.SelfContext;
import org.junit.Test;

public class CoreOperationCompareTest {
    
    @Test
    public void testEqual() {
        CoreOperationCompare compare = new CoreOperationCompare(null, null) {
            @Override
            protected boolean equal(EvalContext context, Expression left, Expression right) {
                return false;
            }
        };
        
        assertFalse(compare.equal(null, null, null));
        
        compare = new CoreOperationCompare(null, null) {
            @Override
            protected boolean equal(EvalContext context, Expression left, Expression right) {
                return true;
            }
        };
        
        assertTrue(compare.equal(null, null, null));
    }
    
    @Test
    public void testContains() {
        CoreOperationCompare compare = new CoreOperationCompare(null, null) {
            @Override
            protected boolean contains(Iterator it, Object value) {
                return false;
            }
        };
        
        assertFalse(compare.contains(null, null));
        
        compare = new CoreOperationCompare(null, null) {
            @Override
            protected boolean contains(Iterator it, Object value) {
                return true;
            }
        };
        
        assertTrue(compare.contains(null, null));
    }
    
    @Test
    public void testFindMatch() {
        CoreOperationCompare compare = new CoreOperationCompare(null, null) {
            @Override
            protected boolean findMatch(Iterator lit, Iterator rit) {
                return false;
            }
        };
        
        assertFalse(compare.findMatch(null, null));
        
        compare = new CoreOperationCompare(null, null) {
            @Override
            protected boolean findMatch(Iterator lit, Iterator rit) {
                return true;
            }
        };
        
        assertTrue(compare.findMatch(null, null));
    }
    
    @Test
    public void testEqualObjects() {
        CoreOperationCompare compare = new CoreOperationCompare(null, null) {
            @Override
            protected boolean equal(Object l, Object r) {
                return false;
            }
        };
        
        assertFalse(compare.equal(null, null));
        
        compare = new CoreOperationCompare(null, null) {
            @Override
            protected boolean equal(Object l, Object r) {
                return true;
            }
        };
        
        assertTrue(compare.equal(null, null));
    }
}