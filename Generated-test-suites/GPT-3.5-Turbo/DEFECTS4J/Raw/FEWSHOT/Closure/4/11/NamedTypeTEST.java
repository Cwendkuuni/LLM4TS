package com.google.javascript.rhino.jstype;

import org.junit.Test;

import static org.junit.Assert.*;

public class NamedTypeTest {

    @Test
    public void testResolveInternal() {
        NamedType namedType = new NamedType(null, "reference", "sourceName", 1, 1);
        StaticScope<JSType> enclosing = null;
        namedType.resolveInternal(null, enclosing);
        assertFalse(namedType.isResolved());
        namedType.setReferencedAndResolvedType(null, null, null);
        assertTrue(namedType.isResolved());
    }

    @Test
    public void testGetReferencedType() {
        NamedType namedType = new NamedType(null, "reference", "sourceName", 1, 1);
        namedType.setReferencedAndResolvedType(null, null, null);
        assertNull(namedType.getReferencedType());
    }

    @Test
    public void testGetReferenceName() {
        NamedType namedType = new NamedType(null, "reference", "sourceName", 1, 1);
        assertEquals("reference", namedType.getReferenceName());
    }

    @Test
    public void testToStringHelper() {
        NamedType namedType = new NamedType(null, "reference", "sourceName", 1, 1);
        namedType.setToStringHelper("toStringHelper");
        assertEquals("toStringHelper", namedType.toString());
    }

    @Test
    public void testHasReferenceName() {
        NamedType namedType = new NamedType(null, "reference", "sourceName", 1, 1);
        assertTrue(namedType.hasReferenceName());
    }

    @Test
    public void testIsNamedType() {
        NamedType namedType = new NamedType(null, "reference", "sourceName", 1, 1);
        assertTrue(namedType.isNamedType());
    }

    @Test
    public void testIsNominalType() {
        NamedType namedType = new NamedType(null, "reference", "sourceName", 1, 1);
        assertTrue(namedType.isNominalType());
    }

    @Test
    public void testHashCode() {
        NamedType namedType = new NamedType(null, "reference", "sourceName", 1, 1);
        assertEquals("reference".hashCode(), namedType.hashCode());
    }

    @Test
    public void testLookupViaProperties() {
        NamedType namedType = new NamedType(null, "reference", "sourceName", 1, 1);
        ErrorReporter t = null;
        StaticScope<JSType> enclosing = null;
        assertNull(namedType.lookupViaProperties(t, enclosing));
    }
}