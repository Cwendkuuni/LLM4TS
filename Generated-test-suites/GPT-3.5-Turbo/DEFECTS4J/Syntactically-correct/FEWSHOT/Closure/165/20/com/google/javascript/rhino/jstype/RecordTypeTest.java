package com.google.javascript.rhino.jstype;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertNull;

import org.junit.Before;
import org.junit.Test;

public class RecordTypeTest {
    private JSTypeRegistry registry;
    private RecordType typeA;
    private RecordType typeB;
    private RecordType typeC;

    @Before
    public void setUp() {
        registry = new JSTypeRegistry();
        typeA = new RecordType(registry, new HashSet<>());
        typeB = new RecordType(registry, new HashSet<>());
        typeC = new RecordType(registry, new HashSet<>());
    }

    @Test
    public void testIsSubtype() {
        assertTrue(typeA.isSubtype(typeA));
        assertTrue(typeA.isSubtype(registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE)));
        assertFalse(typeA.isSubtype(typeB));
    }

    @Test
    public void testGetImplicitPrototype() {
        ObjectType object = registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE);
        assertEquals(object, typeA.getImplicitPrototype());
    }

    @Test
    public void testIsEquivalentTo() {
        assertTrue(typeA.isEquivalentTo(typeA));
        assertFalse(typeA.isEquivalentTo(typeB));
    }

    @Test
    public void testResolveInternal() {
        ErrorReporter reporter = mock(ErrorReporter.class);
        StaticScope<JSType> scope = mock(StaticScope.class);

        typeA.resolveInternal(reporter, scope);

        verify(reporter, times(0)).report(anyString());
    }

    @Test
    public void testProperty() {
        String property = "property";
        JSType type = registry.getNativeType(JSTypeNative.UNKNOWN_TYPE);

        assertNull(typeA.getPropertyType(property));

        typeA.defineProperty(property, type, false, null);

        assertEquals(type, typeA.getPropertyType(property));
    }
}