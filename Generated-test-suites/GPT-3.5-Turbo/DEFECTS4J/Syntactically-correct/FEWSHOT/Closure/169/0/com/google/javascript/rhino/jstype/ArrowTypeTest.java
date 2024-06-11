package com.google.javascript.rhino.jstype;

import static org.junit.Assert.*;

import org.junit.Test;

import com.google.javascript.rhino.ErrorReporter;
import com.google.javascript.rhino.Node;

public class ArrowTypeTest {
    
    @Test
    public void testIsSubtype() {
        JSTypeRegistry registry = new JSTypeRegistry();
        Node parameters = null;
        JSType returnType = null;
        ArrowType arrowType1 = new ArrowType(registry, parameters, returnType);
        ArrowType arrowType2 = new ArrowType(registry, parameters, returnType);
        
        assertTrue(arrowType1.isSubtype(arrowType2));
    }
    
    @Test
    public void testHasEqualParameters() {
        JSTypeRegistry registry = new JSTypeRegistry();
        Node parameters = null;
        JSType returnType = null;
        ArrowType arrowType1 = new ArrowType(registry, parameters, returnType);
        ArrowType arrowType2 = new ArrowType(registry, parameters, returnType);
        
        assertTrue(arrowType1.hasEqualParameters(arrowType2, true));
    }
    
    @Test
    public void testCheckArrowEquivalenceHelper() {
        JSTypeRegistry registry = new JSTypeRegistry();
        Node parameters = null;
        JSType returnType = null;
        ArrowType arrowType1 = new ArrowType(registry, parameters, returnType);
        ArrowType arrowType2 = new ArrowType(registry, parameters, returnType);
        
        assertTrue(arrowType1.checkArrowEquivalenceHelper(arrowType2, true));
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testHashCode() {
        JSTypeRegistry registry = new JSTypeRegistry();
        Node parameters = null;
        JSType returnType = null;
        ArrowType arrowType = new ArrowType(registry, parameters, returnType);
        
        arrowType.hashCode();
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testGetLeastSupertype() {
        JSTypeRegistry registry = new JSTypeRegistry();
        Node parameters = null;
        JSType returnType = null;
        ArrowType arrowType = new ArrowType(registry, parameters, returnType);
        
        arrowType.getLeastSupertype(null);
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testGetGreatestSubtype() {
        JSTypeRegistry registry = new JSTypeRegistry();
        Node parameters = null;
        JSType returnType = null;
        ArrowType arrowType = new ArrowType(registry, parameters, returnType);
        
        arrowType.getGreatestSubtype(null);
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testTestForEquality() {
        JSTypeRegistry registry = new JSTypeRegistry();
        Node parameters = null;
        JSType returnType = null;
        ArrowType arrowType = new ArrowType(registry, parameters, returnType);
        
        arrowType.testForEquality(null);
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testVisit() {
        JSTypeRegistry registry = new JSTypeRegistry();
        Node parameters = null;
        JSType returnType = null;
        ArrowType arrowType = new ArrowType(registry, parameters, returnType);
        
        arrowType.visit(null);
    }
    
    @Test
    public void testGetPossibleToBooleanOutcomes() {
        JSTypeRegistry registry = new JSTypeRegistry();
        Node parameters = null;
        JSType returnType = null;
        ArrowType arrowType = new ArrowType(registry, parameters, returnType);
        
        assertEquals(BooleanLiteralSet.TRUE, arrowType.getPossibleToBooleanOutcomes());
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testResolveInternal() {
        JSTypeRegistry registry = new JSTypeRegistry();
        Node parameters = null;
        JSType returnType = null;
        ArrowType arrowType = new ArrowType(registry, parameters, returnType);
        
        arrowType.resolveInternal(null, null);
    }
    
    @Test
    public void testHasUnknownParamsOrReturn() {
        JSTypeRegistry registry = new JSTypeRegistry();
        Node parameters = null;
        JSType returnType = null;
        ArrowType arrowType = new ArrowType(registry, parameters, returnType);
        
        assertFalse(arrowType.hasUnknownParamsOrReturn());
    }
    
    @Test
    public void testToStringHelper() {
        JSTypeRegistry registry = new JSTypeRegistry();
        Node parameters = null;
        JSType returnType = null;
        ArrowType arrowType = new ArrowType(registry, parameters, returnType);
        
        assertEquals("[ArrowType]", arrowType.toStringHelper(false));
    }
    
    @Test
    public void testHasAnyTemplateInternal() {
        JSTypeRegistry registry = new JSTypeRegistry();
        Node parameters = null;
        JSType returnType = null;
        ArrowType arrowType = new ArrowType(registry, parameters, returnType);
        
        assertFalse(arrowType.hasAnyTemplateInternal());
    }
}