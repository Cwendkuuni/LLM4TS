package com.google.javascript.rhino.jstype;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

public class NamedTypeTest {

    private JSTypeRegistry registry;
    private NamedType namedType;

    @Before
    public void setup() {
        registry = new JSTypeRegistry();
        namedType = new NamedType(registry, "test", "source", 1, 1);
    }

    @Test
    public void testResolveInternal() {
        StaticScope<JSType> enclosing = null;
        ErrorReporter errorReporter = null;

        assertNull(namedType.resolveInternal(errorReporter, enclosing));
    }

    @Test
    public void testResolveViaRegistry() {
        StaticScope<JSType> enclosing = null;
        ErrorReporter errorReporter = null;

        assertFalse(namedType.resolveViaRegistry(errorReporter, enclosing));
    }

    @Test
    public void testResolveViaProperties() {
        StaticScope<JSType> enclosing = null;
        ErrorReporter errorReporter = null;

        namedType.resolveViaProperties(errorReporter, enclosing);
    }

    @Test
    public void testSetReferencedAndResolvedType() {
        JSType type = new JSType(registry, "test");
        ErrorReporter errorReporter = null;
        StaticScope<JSType> enclosing = null;

        namedType.setReferencedAndResolvedType(type, errorReporter, enclosing);
        assertEquals(type, namedType.getReferencedType());
        assertEquals(type, namedType.getResolvedTypeInternal());
    }

    @Test
    public void testCheckEnumElementCycle() {
        ErrorReporter errorReporter = null;

        namedType.checkEnumElementCycle(errorReporter);
    }

    @Test
    public void testHandleTypeCycle() {
        ErrorReporter errorReporter = null;

        namedType.handleTypeCycle(errorReporter);
    }

    @Test
    public void testHandleUnresolvedType() {
        ErrorReporter errorReporter = null;
        boolean ignoreForwardReferencedTypes = false;

        namedType.handleUnresolvedType(errorReporter, ignoreForwardReferencedTypes);
        assertTrue(namedType.getReferencedType().isNoResolvedType());
        assertTrue(namedType.isResolved());
    }
}