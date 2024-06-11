package com.google.javascript.rhino.jstype;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class ArrowTypeTest {
    @Test
    public void testIsSubtype_returnTypeIsSubtype() {
        JSTypeRegistry registry = new JSTypeRegistry();
        ArrowType arrowType1 = new ArrowType(registry, null, registry.getNativeType(JSTypeNative.STRING_TYPE));
        ArrowType arrowType2 = new ArrowType(registry, null, registry.getNativeType(JSTypeNative.OBJECT_TYPE));
        
        assertFalse(arrowType1.isSubtype(arrowType2));
        assertTrue(arrowType2.isSubtype(arrowType1));
    }

    @Test
    public void testIsSubtype_parametersAreSubtype() {
        JSTypeRegistry registry = new JSTypeRegistry();
        Node parameters1 = new Node(Token.BLOCK);
        Node parameters2 = new Node(Token.BLOCK);
        parameters1.addChildToBack(new Node(Token.NAME, new FunctionType(registry).resolve(registry))); // NativeType.UNKNOWN_TYPE
        parameters2.addChildToBack(new Node(Token.NAME, new FunctionType(registry).resolve(registry))); // NativeType.UNKNOWN_TYPE
        ArrowType arrowType1 = new ArrowType(registry, parameters1, null);
        ArrowType arrowType2 = new ArrowType(registry, parameters2, null);

        assertFalse(arrowType1.isSubtype(arrowType2));
        assertFalse(arrowType2.isSubtype(arrowType1));

        parameters1.addChildToBack(new Node(Token.NAME, registry.getNativeType(JSTypeNative.OBJECT_TYPE)));
        parameters2.addChildToBack(new Node(Token.NAME, registry.getNativeType(JSTypeNative.OBJECT_TYPE)));
        assertTrue(arrowType1.isSubtype(arrowType2));
        assertTrue(arrowType2.isSubtype(arrowType1));
    }

    @Test
    public void testIsSubtype_parametersMissingOrExtra() {
        JSTypeRegistry registry = new JSTypeRegistry();
        Node parameters1 = new Node(Token.BLOCK);
        Node parameters2 = new Node(Token.BLOCK);
        parameters1.addChildToBack(new Node(Token.NAME, registry.getNativeType(JSTypeNative.OBJECT_TYPE)));
        parameters2.addChildToBack(new Node(Token.NAME, registry.getNativeType(JSTypeNative.OBJECT_TYPE)));
        ArrowType arrowType1 = new ArrowType(registry, parameters1, null);
        ArrowType arrowType2 = new ArrowType(registry, parameters2, null);

        assertFalse(arrowType1.isSubtype(arrowType2));
        assertFalse(arrowType2.isSubtype(arrowType1));

        parameters1.addChildToBack(new Node(Token.NAME, registry.getNativeType(JSTypeNative.OBJECT_TYPE)));
        assertFalse(arrowType1.isSubtype(arrowType2));
        assertTrue(arrowType2.isSubtype(arrowType1));
    }

    @Test
    public void testIsEquivalentTo_differentReturnTypes() {
        JSTypeRegistry registry = new JSTypeRegistry();
        ArrowType arrowType1 = new ArrowType(registry, null, registry.getNativeType(JSTypeNative.STRING_TYPE));
        ArrowType arrowType2 = new ArrowType(registry, null, registry.getNativeType(JSTypeNative.NUMBER_TYPE));

        assertFalse(arrowType1.isEquivalentTo(arrowType2));
        assertFalse(arrowType2.isEquivalentTo(arrowType1));
    }

    @Test
    public void testIsEquivalentTo_differentParameters() {
        JSTypeRegistry registry = new JSTypeRegistry();
        Node parameters1 = new Node(Token.BLOCK);
        Node parameters2 = new Node(Token.BLOCK);
        parameters1.addChildToBack(new Node(Token.NAME, registry.getNativeType(JSTypeNative.STRING_TYPE)));
        parameters2.addChildToBack(new Node(Token.NAME, registry.getNativeType(JSTypeNative.NUMBER_TYPE)));
        ArrowType arrowType1 = new ArrowType(registry, parameters1, null);
        ArrowType arrowType2 = new ArrowType(registry, parameters2, null);

        assertFalse(arrowType1.isEquivalentTo(arrowType2));
        assertFalse(arrowType2.isEquivalentTo(arrowType1));
    }

    @Test
    public void testHashCode() {
        JSTypeRegistry registry = new JSTypeRegistry();
        Node parameters = new Node(Token.BLOCK);
        parameters.addChildToBack(new Node(Token.NAME, registry.getNativeType(JSTypeNative.STRING_TYPE)));
        ArrowType arrowType = new ArrowType(registry, parameters, registry.getNativeType(JSTypeNative.STRING_TYPE));

        int hashCode = arrowType.getReturnType().hashCode() + arrowType.getParameters().getChildCount();
        assertEquals(hashCode, arrowType.hashCode());
    }

    @Test
    public void testGetLeastSupertype() {
        JSTypeRegistry registry = new JSTypeRegistry();
        ArrowType arrowType1 = new ArrowType(registry, null, registry.getNativeType(JSTypeNative.OBJECT_TYPE));
        ArrowType arrowType2 = new ArrowType(registry, null, registry.getNativeType(JSTypeNative.NUMBER_TYPE));

        assertNull(arrowType1.getLeastSupertype(arrowType2));
    }

    @Test
    public void testGetGreatestSubtype() {
        JSTypeRegistry registry = new JSTypeRegistry();
        ArrowType arrowType1 = new ArrowType(registry, null, registry.getNativeType(JSTypeNative.OBJECT_TYPE));
        ArrowType arrowType2 = new ArrowType(registry, null, registry.getNativeType(JSTypeNative.NUMBER_TYPE));

        assertNull(arrowType1.getGreatestSubtype(arrowType2));
    }

    @Test
    public void testToStringHelper() {
        JSTypeRegistry registry = new JSTypeRegistry();
        ArrowType arrowType = new ArrowType(registry, null, registry.getNativeType(JSTypeNative.STRING_TYPE));

        String expected = "com.google.javascript.rhino.jstype.ArrowType@" + arrowType.hashCode();
        assertEquals(expected, arrowType.toStringHelper(false));
    }
}