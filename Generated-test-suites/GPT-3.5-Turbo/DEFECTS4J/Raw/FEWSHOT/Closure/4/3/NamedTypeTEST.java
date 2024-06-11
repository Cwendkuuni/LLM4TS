package com.google.javascript.rhino.jstype;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class NamedTypeTest {
    @Test
    public void testResolveInternal() {
        JSTypeRegistry registry = new JSTypeRegistry();
        NamedType namedType = new NamedType(registry, "test.ReferenceType", "", 0, 0);
        namedType.resolveViaProperties(null, null);
        assertNull(namedType.getReferencedType());
        
        registry.registerType("test.ReferenceType", registry.getNativeObjectType(JSTypeNative.UNKNOWN_TYPE));
        namedType.resolveInternal(null, null);
        assertEquals(registry.getNativeObjectType(JSTypeNative.UNKNOWN_TYPE), namedType.getReferencedType());
    }

    @Test
    public void testToStringHelper() {
        JSTypeRegistry registry = new JSTypeRegistry();
        NamedType namedType = new NamedType(registry, "test.ReferenceType", "", 0, 0);
        String result = namedType.toStringHelper(false);
        assertEquals("test.ReferenceType", result);
    }

    @Test
    public void testHashCode() {
        JSTypeRegistry registry = new JSTypeRegistry();
        NamedType namedType = new NamedType(registry, "test.ReferenceType", "", 0, 0);
        int hashCode = namedType.hashCode();
        assertEquals("test.ReferenceType".hashCode(), hashCode);
    }

    @Test
    public void testSetValidator() {
        JSTypeRegistry registry = new JSTypeRegistry();
        NamedType namedType = new NamedType(registry, "test.ReferenceType", "", 0, 0);
        Predicate<JSType> validator = (type) -> true;
        namedType.setValidator(validator);
        assertEquals(validator, namedType.getValidator());
    }
}