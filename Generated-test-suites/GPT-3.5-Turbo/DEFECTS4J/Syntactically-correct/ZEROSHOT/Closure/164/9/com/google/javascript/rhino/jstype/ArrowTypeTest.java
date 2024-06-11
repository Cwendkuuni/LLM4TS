package com.google.javascript.rhino.jstype;

import static org.junit.Assert.*;

import org.junit.Test;


public class ArrowTypeTest {

    @Test
    public void testIsSubtype() {
        JSTypeRegistry registry = new JSTypeRegistry();
        ArrowType arrowType1 = new ArrowType(registry, null, null);
        ArrowType arrowType2 = new ArrowType(registry, null, null);
        ArrowType arrowType3 = new ArrowType(registry, null, null);
        arrowType1.returnType = new JSType(registry);
        arrowType2.returnType = new JSType(registry);
        
        boolean result = arrowType1.isSubtype(arrowType2);
        assertTrue(result);

        result = arrowType2.isSubtype(arrowType1);
        assertFalse(result);

        result = arrowType1.isSubtype(arrowType3);
        assertFalse(result);
    }

    @Test
    public void testHasEqualParameters() {
        JSTypeRegistry registry = new JSTypeRegistry();
        ArrowType arrowType1 = new ArrowType(registry, null, null);
        ArrowType arrowType2 = new ArrowType(registry, null, null);
        ArrowType arrowType3 = new ArrowType(registry, null, null);
        arrowType1.parameters = new Node();
        arrowType2.parameters = new Node();
        arrowType3.parameters = new Node();

        boolean result = arrowType1.hasEqualParameters(arrowType2);
        assertTrue(result);
        
        result = arrowType2.hasEqualParameters(arrowType1);
        assertTrue(result);
        
        result = arrowType1.hasEqualParameters(arrowType3);
        assertFalse(result);
    }
 }