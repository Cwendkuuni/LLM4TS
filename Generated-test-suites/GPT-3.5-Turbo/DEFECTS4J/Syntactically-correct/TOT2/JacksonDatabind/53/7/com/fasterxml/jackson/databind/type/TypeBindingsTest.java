package com.fasterxml.jackson.databind.type;

import org.junit.Test;
import static org.junit.Assert.*;

public class TypeBindingsTest {

    @Test
    public void testEmptyBindings() {
        TypeBindings empty = TypeBindings.emptyBindings();
        assertTrue(empty.isEmpty());
    }

    @Test
    public void testCreateWithTypeList() {
        TypeBindings typeBindings = TypeBindings.create(ArrayList.class, Collections.singletonList(JavaType.construct(String.class)));
        assertNotNull(typeBindings);
        assertEquals(1, typeBindings.size());
    }

    @Test
    public void testCreateWithTypeArray() {
        JavaType[] types = { JavaType.construct(String.class), JavaType.construct(Integer.class) };
        TypeBindings typeBindings = TypeBindings.create(HashMap.class, types);
        assertNotNull(typeBindings);
        assertEquals(2, typeBindings.size());
    }

    @Test
    public void testCreateIfNeededWithNoTypeParams() {
        TypeBindings typeBindings = TypeBindings.createIfNeeded(LinkedList.class, JavaType.construct(String.class));
        assertTrue(typeBindings.isEmpty());
    }

    @Test
    public void testWithUnboundVariable() {
        TypeBindings empty = TypeBindings.emptyBindings();
        TypeBindings withUnbound = empty.withUnboundVariable("T");
        assertEquals(1, withUnbound.size());
    }

    @Test
    public void testFindBoundType() {
        TypeBindings typeBindings = TypeBindings.create(ArrayList.class, Collections.singletonList(JavaType.construct(String.class)));
        JavaType boundType = typeBindings.findBoundType("E");
        assertNotNull(boundType);
    }

    @Test
    public void testEqualsAndHashCode() {
        TypeBindings typeBindings1 = TypeBindings.create(ArrayList.class, Collections.singletonList(JavaType.construct(String.class)));
        TypeBindings typeBindings2 = TypeBindings.create(ArrayList.class, Collections.singletonList(JavaType.construct(String.class)));
        assertEquals(typeBindings1, typeBindings2);
        assertEquals(typeBindings1.hashCode(), typeBindings2.hashCode());
    }

}