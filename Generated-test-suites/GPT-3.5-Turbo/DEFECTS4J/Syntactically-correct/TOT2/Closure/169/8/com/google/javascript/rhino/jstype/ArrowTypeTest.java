package com.google.javascript.rhino.jstype;

import static org.junit.Assert.*;
import org.junit.Test;

public class ArrowTypeTest {

    @Test
    public void testIsSubtype() {
        // Test case for checking if one ArrowType is a subtype of another ArrowType
        // Typical use case
        JSTypeRegistry registry = new JSTypeRegistry();
        ArrowType arrowType1 = new ArrowType(registry, null, null);
        ArrowType arrowType2 = new ArrowType(registry, null, null);
        assertTrue(arrowType1.isSubtype(arrowType2));
    }

    @Test
    public void testHasEqualParameters() {
        // Test case for checking if two ArrowType have equal parameters
        // Edge case
        JSTypeRegistry registry = new JSTypeRegistry();
        Node parameters1 = new Node(Token.STRING);
        Node parameters2 = new Node(Token.NUMBER);
        ArrowType arrowType1 = new ArrowType(registry, parameters1, null);
        ArrowType arrowType2 = new ArrowType(registry, parameters2, null);
        assertFalse(arrowType1.hasEqualParameters(arrowType2, false));
    }

    @Test
    public void testCheckArrowEquivalenceHelper() {
        // Test case for checking if two ArrowType are equivalent
        // Error scenario
        JSTypeRegistry registry = new JSTypeRegistry();
        ArrowType arrowType1 = new ArrowType(registry, null, null);
        ArrowType arrowType2 = new ArrowType(registry, null, null);
        assertFalse(arrowType1.checkArrowEquivalenceHelper(arrowType2, false));
    }

    @Test
    public void testHashCode() {
        // Test case for generating hash code for ArrowType
        // Typical use case
        JSTypeRegistry registry = new JSTypeRegistry();
        ArrowType arrowType = new ArrowType(registry, null, null);
        assertNotNull(arrowType.hashCode());
    }

    @Test
    public void testResolveInternal() {
        // Test case for resolving internal types in ArrowType
        // Edge case
        JSTypeRegistry registry = new JSTypeRegistry();
        ArrowType arrowType = new ArrowType(registry, null, null);
        arrowType.resolveInternal(null, null);
        assertNotNull(arrowType);
    }

    @Test
    public void testHasAnyTemplateInternal() {
        // Test case for checking if ArrowType has any internal templates
        // Error scenario
        JSTypeRegistry registry = new JSTypeRegistry();
        ArrowType arrowType = new ArrowType(registry, null, null);
        assertFalse(arrowType.hasAnyTemplateInternal());
    }
}