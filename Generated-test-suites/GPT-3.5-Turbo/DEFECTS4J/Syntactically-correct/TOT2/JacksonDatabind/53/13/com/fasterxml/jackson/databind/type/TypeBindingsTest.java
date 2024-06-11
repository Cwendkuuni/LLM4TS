package com.fasterxml.jackson.databind.type;

import org.junit.Test;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import static org.junit.Assert.*;

public class TypeBindingsTest {

    @Test
    public void testEmptyBindings() {
        TypeBindings bindings = TypeBindings.emptyBindings();
        assertTrue(bindings.isEmpty());
    }

    @Test
    public void testCreateWithNoTypeParameters() {
        TypeBindings bindings = TypeBindings.create(ArrayList.class, null);
        assertTrue(bindings.isEmpty());
    }

    @Test
    public void testCreateWithOneTypeParameter() {
        JavaType type1 = new JavaType();
        TypeBindings bindings = TypeBindings.create(ArrayList.class, type1);
        assertEquals(1, bindings.size());
        assertEquals("E", bindings.getBoundName(0));
        assertEquals(type1, bindings.getBoundType(0));
    }

    @Test
    public void testCreateWithTwoTypeParameters() {
        JavaType type1 = new JavaType();
        JavaType type2 = new JavaType();
        TypeBindings bindings = TypeBindings.create(HashMap.class, type1, type2);
        assertEquals(2, bindings.size());
        assertEquals("K", bindings.getBoundName(0));
        assertEquals(type1, bindings.getBoundType(0));
        assertEquals("V", bindings.getBoundName(1));
        assertEquals(type2, bindings.getBoundType(1));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateWithMismatchingNamesAndTypes() {
        JavaType type1 = new JavaType();
        JavaType type2 = new JavaType();
        TypeBindings.create(ArrayList.class, type1, type2);
    }

    @Test
    public void testToStringWithNoTypes() {
        TypeBindings bindings = TypeBindings.emptyBindings();
        assertEquals("<>", bindings.toString());
    }

    @Test
    public void testToStringWithTypes() {
        JavaType type1 = new JavaType();
        JavaType type2 = new JavaType();
        TypeBindings bindings = TypeBindings.create(HashMap.class, type1, type2);
        assertEquals("<,>", bindings.toString());
    }

    @Test
    public void testFindBoundType() {
        JavaType type1 = new JavaType();
        TypeBindings bindings = TypeBindings.create(ArrayList.class, type1);
        assertEquals(type1, bindings.findBoundType("E"));
    }

    @Test
    public void testWithUnboundVariable() {
        JavaType type1 = new JavaType();
        TypeBindings bindings = TypeBindings.create(ArrayList.class, type1);
        TypeBindings newBindings = bindings.withUnboundVariable("T");
        assertTrue(newBindings.hasUnbound("T"));
    }

    @Test
    public void testHashCode() {
        JavaType type1 = new JavaType();
        TypeBindings bindings = TypeBindings.create(ArrayList.class, type1);
        TypeBindings sameBindings = TypeBindings.create(ArrayList.class, type1);
        assertEquals(bindings.hashCode(), sameBindings.hashCode());
    }

    @Test
    public void testEquals() {
        JavaType type1 = new JavaType();
        TypeBindings bindings = TypeBindings.create(ArrayList.class, type1);
        TypeBindings sameBindings = TypeBindings.create(ArrayList.class, type1);
        assertEquals(bindings, sameBindings);
    }

}