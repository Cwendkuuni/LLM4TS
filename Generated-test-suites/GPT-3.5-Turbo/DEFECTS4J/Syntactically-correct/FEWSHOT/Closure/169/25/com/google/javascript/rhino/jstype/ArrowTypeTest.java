package com.google.javascript.rhino.jstype;

import static org.junit.Assert.assertEquals;

import com.google.javascript.rhino.ErrorReporter;
import com.google.javascript.rhino.Node;
import org.junit.Test;

public class ArrowTypeTest {

    @Test
    public void testIsSubtype() {
        JSTypeRegistry registry = new JSTypeRegistry();
        ArrowType arrowType1 = new ArrowType(
                registry, null, registry.createUnknownType());
        ArrowType arrowType2 = new ArrowType(
                registry, null, registry.createUnknownType());
        ArrowType arrowType3 = new ArrowType(
                registry, null, registry.createNoType());
        
        assertEquals(true, arrowType1.isSubtype(arrowType1));
        assertEquals(true, arrowType1.isSubtype(arrowType2));
        assertEquals(true, arrowType1.isSubtype(arrowType3));
        assertEquals(false, arrowType3.isSubtype(arrowType1));
    }

    @Test
    public void testHasEqualParameters() {
        JSTypeRegistry registry = new JSTypeRegistry();
        Node node1 = null;
        Node node2 = null;
        ArrowType arrowType1 = new ArrowType(
                registry, node1, registry.createUnknownType());
        ArrowType arrowType2 = new ArrowType(
                registry, node2, registry.createUnknownType());
        ArrowType arrowType3 = new ArrowType(
                registry, node1, registry.createNoType());
        
        assertEquals(true, arrowType1.hasEqualParameters(arrowType2, true));
        assertEquals(true, arrowType1.hasEqualParameters(arrowType3, true));
        assertEquals(true, arrowType3.hasEqualParameters(arrowType1, true));
    }

    @Test
    public void testCheckArrowEquivalenceHelper() {
        JSTypeRegistry registry = new JSTypeRegistry();
        Node node1 = null;
        Node node2 = null;
        ArrowType arrowType1 = new ArrowType(
                registry, node1, registry.createUnknownType());
        ArrowType arrowType2 = new ArrowType(
                registry, node2, registry.createUnknownType());
        ArrowType arrowType3 = new ArrowType(
                registry, node1, registry.createNoType());
        
        assertEquals(true, arrowType1.checkArrowEquivalenceHelper(arrowType2, true));
        assertEquals(true, arrowType1.checkArrowEquivalenceHelper(arrowType3, true));
        assertEquals(true, arrowType3.checkArrowEquivalenceHelper(arrowType1, true));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetLeastSupertype() {
        JSTypeRegistry registry = new JSTypeRegistry();
        ArrowType arrowType = new ArrowType(
                registry, null, registry.createUnknownType());
        arrowType.getLeastSupertype(null);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetGreatestSubtype() {
        JSTypeRegistry registry = new JSTypeRegistry();
        ArrowType arrowType = new ArrowType(
                registry, null, registry.createUnknownType());
        arrowType.getGreatestSubtype(null);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testTestForEquality() {
        JSTypeRegistry registry = new JSTypeRegistry();
        ArrowType arrowType = new ArrowType(
                registry, null, registry.createUnknownType());
        arrowType.testForEquality(null);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testVisit() {
        JSTypeRegistry registry = new JSTypeRegistry();
        ArrowType arrowType = new ArrowType(
                registry, null, registry.createUnknownType());
        arrowType.visit(null);
    }

    @Test
    public void testHashCode() {
        JSTypeRegistry registry = new JSTypeRegistry();
        ArrowType arrowType1 = new ArrowType(
                registry, null, registry.createUnknownType());
        ArrowType arrowType2 = new ArrowType(
                registry, null, registry.createUnknownType());
        
        assertEquals(arrowType1.hashCode(), arrowType2.hashCode());
    }

    @Test
    public void testResolveInternal() {
        JSTypeRegistry registry = new JSTypeRegistry();
        ArrowType arrowType = new ArrowType(
                registry, null, registry.createUnknownType());
        arrowType.resolveInternal(null, null);
    }

    @Test
    public void testHasAnyTemplateInternal() {
        JSTypeRegistry registry = new JSTypeRegistry();
        ArrowType arrowType = new ArrowType(
                registry, null,
                registry.createObjectType(registry.getType("T")));
        assertEquals(true, arrowType.hasAnyTemplateInternal());
    }
}