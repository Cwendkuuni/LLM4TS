package com.google.javascript.rhino.jstype;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class NamedTypeTest {
    @Test
    public void testName() {
        NamedType namedType = new NamedType(null, "testType", "source", 1, 1);
        assertEquals("testType", namedType.getReferenceName());
    }

    @Test
    public void testNamedType() {
        NamedType namedType = new NamedType(null, "testType", "source", 1, 1);
        assertNull(namedType.getReferencedType());
        assertNull(namedType.getTypedefType(null, null, null));
        namedType.setValidator(null);
        assertFalse(namedType.setValidator(null));
        namedType.toString();
        namedType.toStringHelper(false);
        namedType.isNominalType();

        namedType = new NamedType(null, "testType", "source", 1, 1);
        namedType.defineProperty("testProperty", null, false, null);
        assertFalse(namedType.isNamedType());
        namedType.hasReferenceName();
        namedType.hashCode();
        namedType.resolveInternal(null, null);
    }
}