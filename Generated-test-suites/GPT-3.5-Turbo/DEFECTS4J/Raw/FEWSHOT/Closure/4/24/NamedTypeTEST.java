package com.google.javascript.rhino.jstype;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

public class NamedTypeTest {
    private JSTypeNative nativeType;
    private NamedType namedType;
    private JSTypeRegistry registry;
    private ErrorReporter errorReporter;

    @Before
    public void setup() {
        nativeType = JSTypeNative.UNKNOWN_TYPE;
        Registry reg = new Registry();
        registry = reg.getJSTypeRegistry();
        errorReporter = reg.getErrorReporter();
    }

    @Test
    public void testGetType() {
        namedType = new NamedType(registry, "TestType", "Source", 1, 1);
        assertEquals(nativeType, namedType.getType());
    }

    @Test
    public void testSetReferenceAndResolveType() {
        namedType = new NamedType(registry, "Test", "Source", 1, 1);
        assertNull(namedType.getReferencedType());
        assertNull(namedType.getResolveType());

        namedType.setReferenceAndResolveType(nativeType, errorReporter);
        assertEquals(nativeType, namedType.getReferencedType());
        assertEquals(nativeType, namedType.getResolveType());
    }

    @Test
    public void testGetReferenceName() {
        namedType = new NamedType(registry, "TestName", "Source", 1, 1);
        assertEquals("TestName", namedType.getReferenceName());
    }

    @Test
    public void testIsNamedType() {
        namedType = new NamedType(registry, "Test", "Source", 1, 1);
        assertTrue(namedType.isNamedType());
    }

    @Test
    public void testIsNominalType() {
        namedType = new NamedType(registry, "Test", "Source", 1, 1);
        assertTrue(namedType.isNominalType());
    }

    @Test
    public void testResolveInternal() {
        namedType = new NamedType(registry, "Test", "Source", 1, 1);
        assertNull(namedType.resolveInternal(errorReporter, null));
    }

    @Test
    public void testToStringHelper() {
        namedType = new NamedType(registry, "Test", "Source", 1, 1);
        assertEquals("Test", namedType.toStringHelper(true));
        assertEquals("Test", namedType.toStringHelper(false));
    }

    @Test
    public void testHasReferenceName() {
        namedType = new NamedType(registry, "Test", "Source", 1, 1);
        assertTrue(namedType.hasReferenceName());
    }
}