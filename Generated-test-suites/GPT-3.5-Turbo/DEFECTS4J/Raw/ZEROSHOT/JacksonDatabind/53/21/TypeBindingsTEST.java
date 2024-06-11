
package com.fasterxml.jackson.databind.type;

import org.junit.Test;

import static org.junit.Assert.*;

public class TypeBindingsTest {
    @Test
    public void testEmptyBindings() {
        TypeBindings bindings = TypeBindings.emptyBindings();
        assertTrue(bindings.isEmpty());
        assertEquals(0, bindings.size());
    }

    @Test
    public void testCreateWithTypes() {
        JavaType[] types = {new JavaType(), new JavaType()};
        TypeBindings bindings = TypeBindings.create(ExampleClass.class, types);
        assertFalse(bindings.isEmpty());
        assertEquals(2, bindings.size());
        assertArrayEquals(types, bindings.typeParameterArray());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateWithMismatchingNamesAndTypes() {
        String[] names = {"one", "two"};
        JavaType[] types = {new JavaType()};
        TypeBindings.create(ExampleClass.class, names, types);
    }

    @Test
    public void testFindBoundType() {
        JavaType type1 = new JavaType();
        JavaType type2 = new JavaType();
        String[] names = {"one", "two"};
        JavaType[] types = {type1, type2};
        TypeBindings bindings = TypeBindings.create(ExampleClass.class, names, types);

        assertEquals(type1, bindings.findBoundType("one"));
        assertEquals(type2, bindings.findBoundType("two"));
        assertNull(bindings.findBoundType("three"));
    }

    @Test
    public void testHasUnbound() {
        String[] names = {"name"};
        TypeBindings bindings = TypeBindings.create(ExampleClass.class, names, null);
        assertTrue(bindings.hasUnbound("name"));
        assertFalse(bindings.hasUnbound("otherName"));
    }

    @Test
    public void testWithUnboundVariable() {
        String[] names = {"name"};
        JavaType[] types = {new JavaType()};
        TypeBindings bindings = TypeBindings.create(ExampleClass.class, names, types);
        assertFalse(bindings.hasUnbound("otherName"));

        TypeBindings newBindings = bindings.withUnboundVariable("otherName");
        assertNotSame(bindings, newBindings);
        assertTrue(newBindings.hasUnbound("otherName"));
        assertFalse(newBindings.hasUnbound("name"));
    }

    @Test
    public void testToString() {
        String[] names = {"name1", "name2"};
        JavaType[] types = {new JavaType(), new JavaType()};
        TypeBindings bindings = TypeBindings.create(ExampleClass.class, names, types);
        assertEquals("<JavaType,JavaType>", bindings.toString());
    }

    @Test
    public void testHashCodeAndEquals() {
        String[] names1 = {"one", "two"};
        JavaType[] types1 = {new JavaType(), new JavaType()};
        TypeBindings bindings1 = TypeBindings.create(ExampleClass.class, names1, types1);

        String[] names2 = {"one", "two"};
        JavaType[] types2 = {new JavaType(), new JavaType()};
        TypeBindings bindings2 = TypeBindings.create(ExampleClass.class, names2, types2);

        String[] names3 = {"one"};
        JavaType[] types3 = {new JavaType()};
        TypeBindings bindings3 = TypeBindings.create(ExampleClass.class, names3, types3);

        assertEquals(bindings1.hashCode(), bindings2.hashCode());
        assertNotEquals(bindings1.hashCode(), bindings3.hashCode());

        assertEquals(bindings1, bindings2);
        assertNotEquals(bindings1, bindings3);
    }

    private static final class ExampleClass {
    }

    private static final class JavaType {
    }
}
