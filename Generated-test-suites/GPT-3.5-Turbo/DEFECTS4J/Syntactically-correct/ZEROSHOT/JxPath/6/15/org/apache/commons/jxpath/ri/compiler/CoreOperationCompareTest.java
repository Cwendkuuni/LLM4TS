package org.apache.commons.jxpath.ri.compiler;

import org.apache.commons.jxpath.Pointer;
import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.axes.InitialContext;
import org.apache.commons.jxpath.ri.axes.SelfContext;
import org.junit.Assert;
import org.junit.Test;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;

public class CoreOperationCompareTest {
    
    private CoreOperationCompare coreOperationCompare;

    @Test
    public void testEqual() {
        EvalContext context = new EvalContext();
        Expression left = new Expression();
        Expression right = new Expression();
        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(left, right);
        
        Assert.assertTrue(coreOperationCompare.equal(context, left, right));
    }

    @Test
    public void testContains() {
        Iterator it = new Iterator();
        Object value = new Object();
        CoreOperationCompare coreOperationCompare = new CoreOperationCompare();
        
        Assert.assertFalse(coreOperationCompare.contains(it, value));
    }

    @Test
    public void testFindMatch() {
        Iterator lit = new Iterator();
        Iterator rit = new Iterator();
        CoreOperationCompare coreOperationCompare = new CoreOperationCompare();
        
        Assert.assertFalse(coreOperationCompare.findMatch(lit, rit));
    }

    @Test
    public void testEqualWithPointer() {
        Object l = new Object();
        Object r = new Object();
        CoreOperationCompare coreOperationCompare = new CoreOperationCompare();
        
        Assert.assertFalse(coreOperationCompare.equal(l, r));
    }
    
    @Test
    public void testEqualWithPointerValue() {
        Pointer l = new Pointer();
        l.setValue(new Object());
        Pointer r = new Pointer();
        r.setValue(new Object());
        CoreOperationCompare coreOperationCompare = new CoreOperationCompare();
        
        Assert.assertFalse(coreOperationCompare.equal(l, r));
    }

    @Test
    public void testEqualWithBoolean() {
        Object l = true;
        Object r = false;
        CoreOperationCompare coreOperationCompare = new CoreOperationCompare();
        
        Assert.assertFalse(coreOperationCompare.equal(l, r));
    }
    
    @Test
    public void testEqualWithNumber() {
        Object l = 1;
        Object r = 2;
        CoreOperationCompare coreOperationCompare = new CoreOperationCompare();
        
        Assert.assertFalse(coreOperationCompare.equal(l, r));
    }

    @Test
    public void testEqualWithString() {
        Object l = "abc";
        Object r = "def";
        CoreOperationCompare coreOperationCompare = new CoreOperationCompare();
        
        Assert.assertFalse(coreOperationCompare.equal(l, r));
    }
    
    @Test
    public void testEqualWithNull() {
        Object l = null;
        Object r = null;
        CoreOperationCompare coreOperationCompare = new CoreOperationCompare();
        
        Assert.assertTrue(coreOperationCompare.equal(l, r));
    }

}