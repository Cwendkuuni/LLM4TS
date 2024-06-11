package org.apache.commons.jxpath.ri.compiler;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.apache.commons.jxpath.EvalContext;
import org.apache.commons.jxpath.JXPathContext;
import org.apache.commons.jxpath.Pointer;
import org.apache.commons.jxpath.ri.axes.InitialContext;
import org.apache.commons.jxpath.ri.axes.SelfContext;
import org.junit.Test;

public class CoreOperationCompareTest {

    @Test
    public void testEqual() {
        Expression arg1 = new Literal("foo");
        Expression arg2 = new Literal("foo");
        CoreOperationCompare operation = new CoreOperationCompare(arg1, arg2);
        
        EvalContext context = new JXPathContext.PointerContext(new JXPathContext(), (Pointer) null);
        assertTrue(operation.equal(context, arg1, arg2));
    }
    
    @Test
    public void testNotEqual() {
        Expression arg1 = new Literal("foo");
        Expression arg2 = new Literal("bar");
        CoreOperationCompare operation = new CoreOperationCompare(arg1, arg2);
        
        EvalContext context = new JXPathContext.PointerContext(new JXPathContext(), (Pointer) null);
        assertFalse(operation.equal(context, arg1, arg2));
    }
    
    @Test
    public void testContains() {
        Expression arg1 = new Literal("foo");
        Expression arg2 = new Literal("bar");
        CoreOperationCompare operation = new CoreOperationCompare(arg1, arg2);
        
        EvalContext context = new JXPathContext.PointerContext(new JXPathContext(), (Pointer) null);
        assertFalse(operation.contains(context, arg1, arg2));
    }
    
    @Test
    public void testFindMatch() {
        Expression arg1 = new Literal("foo");
        Expression arg2 = new Literal("bar");
        CoreOperationCompare operation = new CoreOperationCompare(arg1, arg2);
        
        EvalContext context = new JXPathContext.PointerContext(new JXPathContext(), (Pointer) null);
        assertFalse(operation.findMatch(context, arg1, arg2));
    }
    
    @Test
    public void testEqualPointers() {
        Expression arg1 = new Literal("foo");
        Expression arg2 = new Literal("foo");
        CoreOperationCompare operation = new CoreOperationCompare(arg1, arg2);
        
        EvalContext context = new InitialContext(new JXPathContext());
        assertTrue(operation.equal(context, arg1, arg2));
    }
    
    @Test
    public void testContainsPointers() {
        Expression arg1 = new Literal("foo");
        Expression arg2 = new Literal("bar");
        CoreOperationCompare operation = new CoreOperationCompare(arg1, arg2);
        
        EvalContext context = new SelfContext(new JXPathContext());
        assertFalse(operation.contains(context, arg1, arg2));
    }
    
    @Test
    public void testFindMatchPointers() {
        Expression arg1 = new Literal("foo");
        Expression arg2 = new Literal("bar");
        CoreOperationCompare operation = new CoreOperationCompare(arg1, arg2);
        
        EvalContext context = new SelfContext(new JXPathContext());
        assertFalse(operation.findMatch(context, arg1, arg2));
    }
    
    @Test
    public void testEqualBoolean() {
        Expression arg1 = new Literal(true);
        Expression arg2 = new Literal(true);
        CoreOperationCompare operation = new CoreOperationCompare(arg1, arg2);
        
        EvalContext context = new SelfContext(new JXPathContext());
        assertTrue(operation.equal(context, arg1, arg2));
    }
    
    @Test
    public void testNotEqualBoolean() {
        Expression arg1 = new Literal(true);
        Expression arg2 = new Literal(false);
        CoreOperationCompare operation = new CoreOperationCompare(arg1, arg2);
        
        EvalContext context = new SelfContext(new JXPathContext());
        assertFalse(operation.equal(context, arg1, arg2));
    }
    
    @Test
    public void testEqualNumber() {
        Expression arg1 = new Literal(1);
        Expression arg2 = new Literal(1);
        CoreOperationCompare operation = new CoreOperationCompare(arg1, arg2);
        
        EvalContext context = new SelfContext(new JXPathContext());
        assertTrue(operation.equal(context, arg1, arg2));
    }
    
    @Test
    public void testNotEqualNumber() {
        Expression arg1 = new Literal(1);
        Expression arg2 = new Literal(2);
        CoreOperationCompare operation = new CoreOperationCompare(arg1, arg2);
        
        EvalContext context = new SelfContext(new JXPathContext());
        assertFalse(operation.equal(context, arg1, arg2));
    }
    
    @Test
    public void testEqualString() {
        Expression arg1 = new Literal("foo");
        Expression arg2 = new Literal("foo");
        CoreOperationCompare operation = new CoreOperationCompare(arg1, arg2);
        
        EvalContext context = new SelfContext(new JXPathContext());
        assertTrue(operation.equal(context, arg1, arg2));
    }
    
    @Test
    public void testNotEqualString() {
        Expression arg1 = new Literal("foo");
        Expression arg2 = new Literal("bar");
        CoreOperationCompare operation = new CoreOperationCompare(arg1, arg2);
        
        EvalContext context = new SelfContext(new JXPathContext());
        assertFalse(operation.equal(context, arg1, arg2));
    }
    
    @Test
    public void testEqualNull() {
        Expression arg1 = new Literal(null);
        Expression arg2 = new Literal(null);
        CoreOperationCompare operation = new CoreOperationCompare(arg1, arg2);
        
        EvalContext context = new SelfContext(new JXPathContext());
        assertTrue(operation.equal(context, arg1, arg2));
    }
    
    @Test
    public void testNotEqualNull() {
        Expression arg1 = new Literal(null);
        Expression arg2 = new Literal("foo");
        CoreOperationCompare operation = new CoreOperationCompare(arg1, arg2);
        
        EvalContext context = new SelfContext(new JXPathContext());
        assertFalse(operation.equal(context, arg1, arg2));
    }
    
    
}