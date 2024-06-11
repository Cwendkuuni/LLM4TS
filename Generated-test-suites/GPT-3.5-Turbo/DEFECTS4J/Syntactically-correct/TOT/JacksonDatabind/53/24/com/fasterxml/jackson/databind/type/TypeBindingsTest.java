package com.fasterxml.jackson.databind.type;

import org.junit.Test;
import static org.junit.Assert.*;

public class TypeBindingsTest {

    @Test
    public void testFindBoundType_typical() {
        // Typical use case: Finding a bound type with a name that exists
        TypeBindings bindings = TypeBindings.create(TestClass.class, new JavaType[] { JavaType.INT });
        JavaType result = bindings.findBoundType("T");
        assertNotNull(result);
        assertTrue(result instanceof JavaType);
        assertEquals(JavaType.INT, result);
    }

    @Test
    public void testFindBoundType_notFound() {
        // Edge case: Trying to find a bound type with a name that does not exist
        TypeBindings bindings = TypeBindings.create(TestClass.class, new JavaType[] { JavaType.INT });
        JavaType result = bindings.findBoundType("X");
        assertNull(result);
    }

    @Test
    public void testCreate_emptyBindings() {
        // Edge case: Creating empty bindings and checking if it is empty
        TypeBindings bindings = TypeBindings.emptyBindings();
        assertTrue(bindings.isEmpty());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreate_mismatchingNamesAndTypes() {
        // Error scenario: Creating bindings with mismatching names and types
        TypeBindings.create(TestClass.class, new JavaType[] { JavaType.INT, JavaType.STRING });
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateIfNeeded_mismatchingTypes() {
        // Error scenario: Creating bindings if type does not require type parameters, but types are provided
        TypeBindings.createIfNeeded(TestClass.class, new JavaType[] { JavaType.INT, JavaType.STRING });
    }

    @Test
    public void testWithUnboundVariable() {
        // Typical use case: Creating an instance with an additional unbound variable
        TypeBindings bindings = TypeBindings.create(TestClass.class, new JavaType[] { JavaType.INT });
        TypeBindings updatedBindings = bindings.withUnboundVariable("U");
        assertNotNull(updatedBindings.findBoundType("T"));
        assertNull(updatedBindings.findBoundType("U"));
    }

    @Test
    public void testToString() {
        // Typical use case: Converting bindings to string representation
        TypeBindings bindings = TypeBindings.create(TestClass.class, new JavaType[]{ JavaType.INT });
        String result = bindings.toString();
        assertNotNull(result);
        assertTrue(result.contains("T"));
    }

    @Test
    public void testHashCode() {
        // Typical use case: Getting the hash code of bindings
        TypeBindings bindings = TypeBindings.create(TestClass.class, new JavaType[]{ JavaType.INT });
        int hashCode = bindings.hashCode();
        assertNotEquals(0, hashCode);
    }

    @Test
    public void testEquals_equalBindings() {
        // Typical use case: Checking if two bindings are equal
        TypeBindings bindings1 = TypeBindings.create(TestClass.class, new JavaType[]{ JavaType.INT });
        TypeBindings bindings2 = TypeBindings.create(TestClass.class, new JavaType[]{ JavaType.INT });
        assertTrue(bindings1.equals(bindings2));
    }

    @Test
    public void testEquals_differentBindings() {
        // Typical use case: Checking if two bindings are not equal
        TypeBindings bindings1 = TypeBindings.create(TestClass.class, new JavaType[]{ JavaType.INT });
        TypeBindings bindings2 = TypeBindings.create(TestClass.class, new JavaType[]{ JavaType.STRING });
        assertFalse(bindings1.equals(bindings2));
    }

    private static class TestClass<T> {}

}