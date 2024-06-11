package com.google.javascript.rhino.jstype;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.google.javascript.rhino.ErrorReporter;
import com.google.javascript.rhino.Node;

public class ArrowTypeTest {
    @Test
    public void testIsSubtype() {
        JSTypeRegistry registry = new JSTypeRegistry();
        ArrowType arrowType1 = new ArrowType(registry, null, null);
        ArrowType arrowType2 = new ArrowType(registry, null, null);

        assertTrue(arrowType1.isSubtype(arrowType2));
    }

    @Test
    public void testHasEqualParameters() {
        JSTypeRegistry registry = new JSTypeRegistry();
        Node parameters1 = new Node(Token.STRING);
        Node parameters2 = new Node(Token.STRING);
        ArrowType arrowType1 = new ArrowType(registry, parameters1, null);
        ArrowType arrowType2 = new ArrowType(registry, parameters2, null);

        assertTrue(arrowType1.hasEqualParameters(arrowType2));
    }

    @Test
    public void testIsEquivalentTo() {
        JSTypeRegistry registry = new JSTypeRegistry();
        Node parameters1 = new Node(Token.STRING);
        Node parameters2 = new Node(Token.STRING);
        JSType returnType = new JSType(registry);
        ArrowType arrowType1 = new ArrowType(registry, parameters1, returnType);
        ArrowType arrowType2 = new ArrowType(registry, parameters2, returnType);

        assertTrue(arrowType1.isEquivalentTo(arrowType2));
    }

    @Test
    public void testHashCode() {
        JSTypeRegistry registry = new JSTypeRegistry();
        Node parameters = new Node(Token.STRING);
        JSType returnType = new JSType(registry);
        ArrowType arrowType = new ArrowType(registry, parameters, returnType);

        assertEquals(arrowType.hashCode(), arrowType.hashCode());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetLeastSupertype() {
        JSTypeRegistry registry = new JSTypeRegistry();
        ArrowType arrowType1 = new ArrowType(registry, null, null);
        ArrowType arrowType2 = new ArrowType(registry, null, null);

        arrowType1.getLeastSupertype(arrowType2);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetGreatestSubtype() {
        JSTypeRegistry registry = new JSTypeRegistry();
        ArrowType arrowType1 = new ArrowType(registry, null, null);
        ArrowType arrowType2 = new ArrowType(registry, null, null);

        arrowType1.getGreatestSubtype(arrowType2);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testTestForEquality() {
        JSTypeRegistry registry = new JSTypeRegistry();
        ArrowType arrowType1 = new ArrowType(registry, null, null);
        ArrowType arrowType2 = new ArrowType(registry, null, null);

        arrowType1.testForEquality(arrowType2);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testVisit() {
        JSTypeRegistry registry = new JSTypeRegistry();
        ArrowType arrowType = new ArrowType(registry, null, null);

        arrowType.visit(null);
    }

    @Test
    public void testGetPossibleToBooleanOutcomes() {
        JSTypeRegistry registry = new JSTypeRegistry();
        ArrowType arrowType = new ArrowType(registry, null, null);

        assertTrue(arrowType.getPossibleToBooleanOutcomes().includesTrue());
        assertFalse(arrowType.getPossibleToBooleanOutcomes().includesFalse());
    }

    @Test
    public void testResolveInternal() {
        JSTypeRegistry registry = new JSTypeRegistry();
        ErrorReporter errorReporter = null;
        StaticScope<JSType> scope = null;
        ArrowType arrowType = new ArrowType(registry, null, null);

        JSType resolvedType = arrowType.resolveInternal(errorReporter, scope);

        assertEquals(arrowType, resolvedType);
    }

    @Test
    public void testHasUnknownParamsOrReturn() {
        JSTypeRegistry registry = new JSTypeRegistry();
        ArrowType arrowType = new ArrowType(registry, null, null);

        assertFalse(arrowType.hasUnknownParamsOrReturn());
    }

    @Test
    public void testToStringHelper() {
        JSTypeRegistry registry = new JSTypeRegistry();
        ArrowType arrowType = new ArrowType(registry, null, null);

        assertEquals(super.toString(), arrowType.toStringHelper(false));
    }
}