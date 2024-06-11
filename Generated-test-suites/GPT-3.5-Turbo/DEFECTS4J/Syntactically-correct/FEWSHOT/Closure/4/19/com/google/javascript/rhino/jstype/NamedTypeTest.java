package com.google.javascript.rhino.jstype;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class NamedTypeTest {

    @Test
    public void testResolveInternal() {
        // Create a NamedType with a reference to "testType"
        NamedType namedType = new NamedType(registry, "testType", "sourceName", 1, 1);

        // Resolve the NamedType with an ErrorReporter and an enclosing StaticScope
        namedType.resolveInternal(t, enclosing);

        // Verify that the resolved type is equal to the referenced type
        assertEquals(referencedType, namedType.getResolvedType());
    }

    @Test
    public void testLookupViaProperties() {
        // Create a NamedType with a reference to "testType.property"
        NamedType namedType = new NamedType(registry, "testType.property", "sourceName", 1, 1);

        // Lookup the type via properties with an ErrorReporter and an enclosing StaticScope
        JSType value = namedType.lookupViaProperties(t, enclosing);

        // Verify that the value is equal to the expected type
        assertEquals(expectedType, value);
    }

    @Test
    public void testSetValidator() {
        // Create a NamedType with no validator
        NamedType namedType = new NamedType(registry, "testType", "sourceName", 1, 1);

        // Set the validator
        namedType.setValidator(validator);

        // Verify that the validator is set correctly
        assertEquals(validator, namedType.getValidator());
    }
}