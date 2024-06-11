package com.google.javascript.rhino.jstype;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class NamedTypeTest {

    @Test
    public void testGetReferencedType() {
        JSTypeRegistry registry = new JSTypeRegistry();
        NamedType type = new NamedType(registry, "reference", "sourceName", 10, 20);
        assertNull(type.getReferencedType());

        FunctionType funcType = new FunctionType(registry);
        registry.registerType("reference", funcType);
        assertEquals(funcType, type.getReferencedType());
    }

    @Test
    public void testGetReferenceName() {
        JSTypeRegistry registry = new JSTypeRegistry();
        NamedType type = new NamedType(registry, "reference", "sourceName", 10, 20);

        assertEquals("reference", type.getReferenceName());
    }

    @Test
    public void testHasReferenceName() {
        JSTypeRegistry registry = new JSTypeRegistry();
        NamedType type = new NamedType(registry, "reference", "sourceName", 10, 20);

        assertTrue(type.hasReferenceName());
    }

    @Test
    public void testIsNamedType() {
        JSTypeRegistry registry = new JSTypeRegistry();
        NamedType type = new NamedType(registry, "reference", "sourceName", 10, 20);

        assertTrue(type.isNamedType());
    }

    @Test
    public void testIsNominalType() {
        JSTypeRegistry registry = new JSTypeRegistry();
        NamedType type = new NamedType(registry, "reference", "sourceName", 10, 20);

        assertTrue(type.isNominalType());
    }

    @Test
    public void testResolveInternal() {
        JSTypeRegistry registry = new JSTypeRegistry();
        NamedType type = new NamedType(registry, "reference", "sourceName", 10, 20);

        ErrorReporter errorReporter = new ErrorReporter();

        type.resolveInternal(errorReporter, null);

        assertNotNull(type.getReferencedType());

        JSType referencedType = type.getReferencedType();
        assertTrue(referencedType instanceof ObjectType);
        assertFalse(referencedType.isUnknownType());
    }

    @Test
    public void testSetValidator() {
        JSTypeRegistry registry = new JSTypeRegistry();
        NamedType type = new NamedType(registry, "reference", "sourceName", 10, 20);

        assertFalse(type.setValidator(null));

        Predicate<JSType> validator = new Predicate<JSType>() {
            public boolean apply(JSType input) {
                return input.isObjectType();
            }
        };

        assertTrue(type.setValidator(validator));
    }

}