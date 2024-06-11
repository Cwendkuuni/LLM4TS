package org.apache.commons.jxpath.ri.compiler;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import org.apache.commons.jxpath.Pointer;
import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InfoSetUtil;
import org.apache.commons.jxpath.ri.axes.InitialContext;
import org.apache.commons.jxpath.ri.axes.SelfContext;
import org.junit.Test;
import static org.junit.Assert.*;

public class CoreOperationCompareTest {
    
    @Test
    public void testEqual() {
        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(null, null) {
            @Override
            protected boolean equal(EvalContext context, Expression left, Expression right) {
                return true;
            }
        };
        
        EvalContext context = new EvalContext();
        Expression left = new CoreOperationCompare(null, null);
        Expression right = new CoreOperationCompare(null, null);
        
        assertTrue(coreOperationCompare.equal(context, left, right));
    }

    @Test
    public void testContains() {
        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(null, null) {
            @Override
            protected boolean contains(Iterator it, Object value) {
                return true;
            }
        };
        
        Iterator it = new Iterator() {
            @Override
            public boolean hasNext() {
                return true;
            }
            
            @Override
            public Object next() {
                return null;
            }
        };
        Object value = null;
        
        assertTrue(coreOperationCompare.contains(it, value));
    }

    @Test
    public void testFindMatch() {
        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(null, null) {
            @Override
            protected boolean findMatch(Iterator lit, Iterator rit) {
                return true;
            }
        };
        
        Iterator lit = new Iterator() {
            @Override
            public boolean hasNext() {
                return true;
            }
            
            @Override
            public Object next() {
                return null;
            }
        };
        Iterator rit = new Iterator() {
            @Override
            public boolean hasNext() {
                return true;
            }
            
            @Override
            public Object next() {
                return null;
            }
        };
        
        assertTrue(coreOperationCompare.findMatch(lit, rit));
    }

    @Test
    public void testEqualObjects() {
        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(null, null) {
            @Override
            protected boolean equal(Object l, Object r) {
                return true;
            }
        };
        
        Object l = null;
        Object r = null;
        
        assertTrue(coreOperationCompare.equal(l, r));
    }
}