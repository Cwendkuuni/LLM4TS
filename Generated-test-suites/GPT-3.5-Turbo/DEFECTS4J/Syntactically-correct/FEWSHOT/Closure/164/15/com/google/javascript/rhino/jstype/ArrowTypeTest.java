package com.google.javascript.rhino.jstype;

import static org.junit.Assert.*;
import org.junit.Test;

public class ArrowTypeTest {
    @Test
    public void testIsSubtype() {
        JSTypeRegistry registry = new JSTypeRegistry();
        Node parameters = new Node();
        JSType returnType = new JSType();
        ArrowType arrowType = new ArrowType(registry, parameters, returnType);
        assertTrue(arrowType.isSubtype(new ArrowType(registry, parameters, returnType)));
        assertFalse(arrowType.isSubtype(new JSType()));
    }

    @Test
    public void testHasEqualParameters() {
        JSTypeRegistry registry = new JSTypeRegistry();
        Node parameters = new Node();
        JSType returnType = new JSType();
        ArrowType arrowType = new ArrowType(registry, parameters, returnType);
        assertTrue(arrowType.hasEqualParameters(new ArrowType(registry, parameters, returnType)));
        assertFalse(arrowType.hasEqualParameters(new ArrowType(registry, new Node(), returnType)));
    }

    @Test
    public void testIsEquivalentTo() {
        JSTypeRegistry registry = new JSTypeRegistry();
        Node parameters = new Node();
        JSType returnType = new JSType();
        ArrowType arrowType = new ArrowType(registry, parameters, returnType);
        assertTrue(arrowType.isEquivalentTo(new ArrowType(registry, parameters, returnType)));
        assertFalse(arrowType.isEquivalentTo(new ArrowType(registry, new Node(), returnType)));
        assertFalse(arrowType.isEquivalentTo(new JSType()));
    }

    @Test
    public void testHashCode() {
        JSTypeRegistry registry = new JSTypeRegistry();
        Node parameters = new Node();
        JSType returnType = new JSType();
        ArrowType arrowType = new ArrowType(registry, parameters, returnType);
        assertEquals(arrowType.hashCode(), new ArrowType(registry, parameters, returnType).hashCode());
        assertNotEquals(arrowType.hashCode(), new ArrowType(registry, new Node(), returnType).hashCode());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetLeastSupertype() {
        JSTypeRegistry registry = new JSTypeRegistry();
        Node parameters = new Node();
        JSType returnType = new JSType();
        ArrowType arrowType = new ArrowType(registry, parameters, returnType);
        arrowType.getLeastSupertype(new JSType());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetGreatestSubtype() {
        JSTypeRegistry registry = new JSTypeRegistry();
        Node parameters = new Node();
        JSType returnType = new JSType();
        ArrowType arrowType = new ArrowType(registry, parameters, returnType);
        arrowType.getGreatestSubtype(new JSType());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testTestForEquality() {
        JSTypeRegistry registry = new JSTypeRegistry();
        Node parameters = new Node();
        JSType returnType = new JSType();
        ArrowType arrowType = new ArrowType(registry, parameters, returnType);
        arrowType.testForEquality(new JSType());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testVisit() {
        JSTypeRegistry registry = new JSTypeRegistry();
        Node parameters = new Node();
        JSType returnType = new JSType();
        ArrowType arrowType = new ArrowType(registry, parameters, returnType);
        arrowType.visit(new Visitor<>());
    }

    @Test
    public void testResolveInternal() {
        JSTypeRegistry registry = new JSTypeRegistry();
        Node parameters = new Node();
        JSType returnType = new JSType();
        ArrowType arrowType = new ArrowType(registry, parameters, returnType);
        assertNull(arrowType.resolveInternal(new ErrorReporter(), new StaticScope<>()));
    }

    @Test
    public void testHasUnknownParamsOrReturn() {
        JSTypeRegistry registry = new JSTypeRegistry();
        Node parameters = new Node();
        JSType returnType = new JSType();
        ArrowType arrowType = new ArrowType(registry, parameters, returnType);
        assertFalse(arrowType.hasUnknownParamsOrReturn());
    }

    @Test
    public void testToStringHelper() {
        JSTypeRegistry registry = new JSTypeRegistry();
        Node parameters = new Node();
        JSType returnType = new JSType();
        ArrowType arrowType = new ArrowType(registry, parameters, returnType);
        assertNotNull(arrowType.toStringHelper(true));
    }
}