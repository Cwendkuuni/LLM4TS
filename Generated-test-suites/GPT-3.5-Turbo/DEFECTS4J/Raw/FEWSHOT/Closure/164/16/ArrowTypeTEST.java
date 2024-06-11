package com.google.javascript.rhino.jstype;

import org.junit.Test;
import static org.junit.Assert.*;

public class ArrowTypeTest {

    @Test
    public void testIsSubtype() {
        JSTypeRegistry registry = new JSTypeRegistry();
        JSType paramType1 = registry.createStringType();
        JSType paramType2 = registry.createNumberType();
        JSType returnType = registry.createBooleanType();
        
        ArrowType arrowType1 = new ArrowType(registry, null, returnType);
        ArrowType arrowType2 = new ArrowType(registry, null, returnType);
        assertTrue(arrowType1.isSubtype(arrowType2));
        assertTrue(arrowType2.isSubtype(arrowType1));
        
        ArrowType arrowType3 = new ArrowType(registry, null, paramType1);
        assertFalse(arrowType1.isSubtype(arrowType3));
        assertFalse(arrowType3.isSubtype(arrowType1));
        
        Node parameters1 = Node.newString("param1");
        parameters1.setJSType(paramType1);
        ArrowType arrowType4 = new ArrowType(registry, parameters1, returnType);
        assertFalse(arrowType4.isSubtype(arrowType1));
        assertFalse(arrowType1.isSubtype(arrowType4));
        
        Node parameters2 = Node.newNumber("param2");
        parameters2.setJSType(paramType2);
        ArrowType arrowType5 = new ArrowType(registry, parameters2, returnType);
        assertFalse(arrowType5.isSubtype(arrowType1));
        assertFalse(arrowType1.isSubtype(arrowType5));
    }

    @Test
    public void testHasEqualParameters() {
        JSTypeRegistry registry = new JSTypeRegistry();
        JSType paramType1 = registry.createStringType();
        JSType paramType2 = registry.createNumberType();
        JSType returnType = registry.createBooleanType();
        
        Node parameters1 = Node.newString("param1");
        parameters1.setJSType(paramType1);
        ArrowType arrowType1 = new ArrowType(registry, parameters1, returnType);
        Node parameters2 = Node.newString("param2");
        parameters2.setJSType(paramType2);
        ArrowType arrowType2 = new ArrowType(registry, parameters2, returnType);
        assertFalse(arrowType1.hasEqualParameters(arrowType2));
        
        Node parameters3 = Node.newString("param3");
        parameters3.setJSType(paramType1);
        ArrowType arrowType3 = new ArrowType(registry, parameters3, returnType);
        assertTrue(arrowType1.hasEqualParameters(arrowType3));
    }

    @Test
    public void testIsEquivalentTo() {
        JSTypeRegistry registry = new JSTypeRegistry();
        JSType paramType1 = registry.createStringType();
        JSType returnType = registry.createBooleanType();
        
        ArrowType arrowType1 = new ArrowType(registry, null, returnType);
        ArrowType arrowType2 = new ArrowType(registry, null, returnType);
        assertTrue(arrowType1.isEquivalentTo(arrowType2));
        assertTrue(arrowType2.isEquivalentTo(arrowType1));
        
        Node parameters1 = Node.newString("param1");
        parameters1.setJSType(paramType1);
        ArrowType arrowType3 = new ArrowType(registry, parameters1, returnType);
        assertFalse(arrowType1.isEquivalentTo(arrowType3));
        assertFalse(arrowType3.isEquivalentTo(arrowType1));
    }

    @Test
    public void testHashCode() {
        JSTypeRegistry registry = new JSTypeRegistry();
        JSType paramType1 = registry.createStringType();
        JSType returnType = registry.createBooleanType();
        
        ArrowType arrowType1 = new ArrowType(registry, null, returnType);
        ArrowType arrowType2 = new ArrowType(registry, null, returnType);
        assertEquals(arrowType1.hashCode(), arrowType2.hashCode());
        
        Node parameters1 = Node.newString("param1");
        parameters1.setJSType(paramType1);
        ArrowType arrowType3 = new ArrowType(registry, parameters1, returnType);
        assertNotEquals(arrowType1.hashCode(), arrowType3.hashCode());
    }

}