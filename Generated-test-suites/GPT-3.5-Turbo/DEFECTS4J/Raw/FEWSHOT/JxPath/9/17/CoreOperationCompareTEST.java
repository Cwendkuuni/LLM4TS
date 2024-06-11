package org.apache.commons.jxpath.ri.compiler;

import static org.junit.Assert.*;

import org.apache.commons.jxpath.Pointer;
import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.axes.InitialContext;
import org.apache.commons.jxpath.ri.axes.SelfContext;
import org.junit.Before;
import org.junit.Test;

import java.util.*;

public class CoreOperationCompareTest {

    private CoreOperationCompare operationCompare;
    private Expression arg1;
    private Expression arg2;
    private EvalContext context;
    
    @Before
    public void setUp() {
        arg1 = new CoreOperation(new Expression[]{});
        arg2 = new CoreOperation(new Expression[]{});
        operationCompare = new CoreOperationCompare(arg1, arg2);
        context = new EvalContext();
    }
    
    @Test
    public void testGetPrecedence() {
        assertEquals(2, operationCompare.getPrecedence());
    }
    
    @Test
    public void testIsSymmetric() {
        assertTrue(operationCompare.isSymmetric());
    }
    
    @Test
    public void testEqual_PointersAreEqual() {
        Object pointer1 = new Pointer();
        Object pointer2 = new Pointer();
        
        assertTrue(operationCompare.equal((EvalContext) pointer1, arg1, arg2));
    }
    
    @Test
    public void testEqual_LeftIsInitialContext() {
        Object initialContext = new InitialContext();
        
        assertTrue(operationCompare.equal((EvalContext) initialContext, arg1, arg2));
    }
    
    @Test
    public void testEqual_LeftIsSelfContext() {
        Object selfContext = new SelfContext();
        
        assertTrue(operationCompare.equal((EvalContext) selfContext, arg1, arg2));
    }
    
    @Test
    public void testEqual_RightIsInitialContext() {
        Object initialContext = new InitialContext();
        
        assertTrue(operationCompare.equal(context, arg1, (EvalContext) initialContext));
    }
    
    @Test
    public void testEqual_RightIsSelfContext() {
        Object selfContext = new SelfContext();
        
        assertTrue(operationCompare.equal(context, arg1, (EvalContext) selfContext));
    }
    
    @Test
    public void testEqual_CollectionsAreEqual() {
        List<Object> collection1 = Arrays.asList("A", "B", "C");
        List<Object> collection2 = Arrays.asList("X", "Y", "Z");
        
        assertTrue(operationCompare.equal(collection1.iterator(), collection1.iterator()));
        assertTrue(operationCompare.equal(collection2.iterator(), collection2.iterator()));
    }
    
    @Test
    public void testEqual_LandRAreNull() {
        Object l = null;
        Object r = null;
        
        assertTrue(operationCompare.equal(l, r));
    }
    
    @Test
    public void testEqual_LandRAreBooleans() {
        Object l = true;
        Object r = false;
        
        assertFalse(operationCompare.equal(l, r));
    }
    
    @Test
    public void testEqual_LandRAreNaN() {
        Object l = Double.NaN;
        Object r = Double.NaN;
        
        assertFalse(operationCompare.equal(l, r));
    }
    
    @Test
    public void testEqual_LandRAreNumbers() {
        Object l = 5;
        Object r = 5.0;
        
        assertTrue(operationCompare.equal(l, r));
    }
    
    @Test
    public void testEqual_LandRAreStrings() {
        Object l = "Hello";
        Object r = "World";
        
        assertFalse(operationCompare.equal(l, r));
    }
    
    @Test
    public void testEqual_LandRNotEqual() {
        Object l = new Object();
        Object r = new Object();
        
        assertFalse(operationCompare.equal(l, r));
    }
}