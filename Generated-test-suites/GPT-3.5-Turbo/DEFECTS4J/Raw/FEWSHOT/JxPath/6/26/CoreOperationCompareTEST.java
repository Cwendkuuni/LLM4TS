package org.apache.commons.jxpath.ri.compiler;
import static org.junit.Assert.*;

import java.util.Arrays;
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
    public void testEqual() {
        Expression left = new Expression();
        Expression right = new Expression();
        EvalContext context = new EvalContext();
        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(left, right);
        
        left.compute(context);
        right.compute(context);
        
        assertFalse(coreOperationCompare.equal(context, left, right));
    }

    @Test
    public void testContains() {
        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(new Expression(), new Expression());
        Iterator it = Arrays.asList("1", "2", "3").iterator();
        Object value = "2";
        
        assertTrue(coreOperationCompare.contains(it, value));
    }

    @Test
    public void testFindMatch() {
        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(new Expression(), new Expression());
        Iterator lit = Arrays.asList("1", "2", "3").iterator();
        Iterator rit = Arrays.asList("3", "4", "5").iterator();
        
        assertFalse(coreOperationCompare.findMatch(lit, rit));
    }

    @Test
    public void testEqual_1() {
        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(new Expression(), new Expression());
        
        assertFalse(coreOperationCompare.equal(null, null));
    }

    @Test
    public void testEqual_2() {
        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(new Expression(), new Expression());
        
        Object l = new Pointer();
        Object r = new Pointer();
        
        assertFalse(coreOperationCompare.equal(l, r));
    }

    @Test
    public void testEqual_3() {
        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(new Expression(), new Expression());
        
        Object l = new Pointer();
        Object r = new Pointer();
        
        assertFalse(coreOperationCompare.equal(l, r));
    }

    @Test
    public void testEqual_4() {
        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(new Expression(), new Expression());
        
        Object l = true;
        Object r = true;
        
        assertTrue(coreOperationCompare.equal(l, r));
    }

    @Test
    public void testEqual_5() {
        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(new Expression(), new Expression());
        
        Object l = 1;
        Object r = 1;
        
        assertTrue(coreOperationCompare.equal(l, r));
    }

    @Test
    public void testEqual_6() {
        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(new Expression(), new Expression());
        
        Object l = "test";
        Object r = "test";
        
        assertTrue(coreOperationCompare.equal(l, r));
    }

    @Test
    public void testEqual_7() {
        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(new Expression(), new Expression());
        
        Object l = null;
        Object r = null;
        
        assertTrue(coreOperationCompare.equal(l, r));
    }

}