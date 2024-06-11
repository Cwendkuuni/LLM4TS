package com.google.javascript.rhino.jstype;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import com.google.javascript.rhino.ErrorReporter;
import com.google.javascript.rhino.Node;
import org.junit.Before;
import org.junit.Test;

public class ArrowTypeTest {
    private ArrowType arrowType;

    @Before
    public void setUp() {
        JSTypeRegistry registry = new JSTypeRegistry();
        Node parameters = new Node();
        JSType returnType = new JSType(registry, "returnType");
        boolean returnTypeInferred = true;
        arrowType = new ArrowType(registry, parameters, returnType, returnTypeInferred);
    }

    @Test
    public void testIsSubtype() {
        assertFalse(arrowType.isSubtype(new JSType(new JSTypeRegistry(), "otherType")));
    }

    @Test
    public void testHasEqualParameters() {
        ArrowType otherType = new ArrowType(new JSTypeRegistry(), new Node(), new JSType(new JSTypeRegistry(), "returnType"));
        assertTrue(arrowType.hasEqualParameters(otherType));
    }

    @Test
    public void testIsEquivalentTo() {
        assertFalse(arrowType.isEquivalentTo(new JSType(new JSTypeRegistry(), "otherType")));
    }

    @Test
    public void testHashCode() {
        assertEquals(-799770608, arrowType.hashCode());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetLeastSupertype() {
        arrowType.getLeastSupertype(new JSType(new JSTypeRegistry(), "otherType"));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetGreatestSubtype() {
        arrowType.getGreatestSubtype(new JSType(new JSTypeRegistry(), "otherType"));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testTestForEquality() {
        arrowType.testForEquality(new JSType(new JSTypeRegistry(), "otherType"));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testVisit() {
        arrowType.visit(new JSType.Visitor() {});
    }

    @Test
    public void testGetPossibleToBooleanOutcomes() {
        assertEquals(BooleanLiteralSet.TRUE, arrowType.getPossibleToBooleanOutcomes());
    }

    @Test
    public void testResolveInternal() {
        ErrorReporter t = new ErrorReporter();
        StaticScope<JSType> scope = new StaticScope<>();
        JSType resolvedType = arrowType.resolveInternal(t, scope);
        assertTrue(resolvedType.hasUnknownParamsOrReturn());
    }

    @Test
    public void testToStringHelper() {
        String str = arrowType.toStringHelper(true);
        assertEquals("super.toString()", str);
    }
}