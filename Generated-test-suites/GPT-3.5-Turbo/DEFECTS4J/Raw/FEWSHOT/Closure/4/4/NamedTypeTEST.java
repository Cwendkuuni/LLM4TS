package com.google.javascript.rhino.jstype;

import com.google.common.base.Predicate;
import com.google.javascript.rhino.ErrorReporter;
import com.google.javascript.rhino.Node;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class NamedTypeTest {

    private NamedType namedType;

    @Before
    public void setUp() {
        JSTypeRegistry registry = new JSTypeRegistry();
        namedType = new NamedType(
                registry, "com.google.javascript.rhino.jstype.NamedType", "source", 1, 1);
    }

    @Test
    public void testGetReferencedType() {
        assertNull(namedType.getReferencedType());
    }

    @Test
    public void testGetReferenceName() {
        assertEquals("com.google.javascript.rhino.jstype.NamedType", namedType.getReferenceName());
    }

    @Test
    public void testToStringHelper() {
        assertEquals("com.google.javascript.rhino.jstype.NamedType", namedType.toStringHelper(false));
        assertEquals("com.google.javascript.rhino.jstype.NamedType", namedType.toStringHelper(true));
    }

    @Test
    public void testHasReferenceName() {
        assertTrue(namedType.hasReferenceName());
    }

    @Test
    public void testIsNamedType() {
        assertTrue(namedType.isNamedType());
    }

    @Test
    public void testIsNominalType() {
        assertTrue(namedType.isNominalType());
    }

    @Test
    public void testHashCode() {
        assertEquals(353994853, namedType.hashCode());
    }

    @Test
    public void testResolveInternal() {
        ErrorReporter errorReporter = new ErrorReporter();
        StaticScope<JSType> enclosing = new StaticScope<>();
        assertEquals(namedType, namedType.resolveInternal(errorReporter, enclosing));
        assertNull(namedType.getReferencedType());
    }

    @Test
    public void testSetValidator() {
        Predicate<JSType> validator = type -> true;
        assertFalse(namedType.setValidator(validator));
    }

    @Test
    public void testDefineProperty() {
        assertFalse(namedType.defineProperty("propertyName", null, false, null));
    }
}