package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Collection;
import java.util.Map;
import org.junit.Test;

public class SimpleTypeTest {

    @Test
    public void testConstructor_emptyBindings() {
        SimpleType simpleType = new SimpleType(Object.class);
        assertEquals(Object.class, simpleType.getRawClass());
        assertEquals(0, simpleType.containedTypeCount());
        assertEquals("java.lang.Object", simpleType.toString());
    }

    @Test
    public void testConstructor_withBindings() {
        TypeBindings bindings = new TypeBindings();
        bindings.addBoundType(0, SimpleType.constructUnsafe(String.class));
        SimpleType simpleType = new SimpleType(Map.class, bindings, null, null);
        assertEquals(Map.class, simpleType.getRawClass());
        assertEquals(1, simpleType.containedTypeCount());
        assertEquals("java.util.Map<java.lang.String>", simpleType.toString());
        assertTrue(simpleType.getContentType() instanceof SimpleType);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructor_arrayType() {
        new SimpleType(String[].class);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructor_collectionType() {
        new SimpleType(Collection.class);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructor_mapType() {
        new SimpleType(Map.class);
    }

    @Test
    public void testConstructUnsafe() {
        SimpleType simpleType = SimpleType.constructUnsafe(Object.class);
        assertEquals(Object.class, simpleType.getRawClass());
        assertEquals(0, simpleType.containedTypeCount());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstruct() {
        SimpleType.construct(Map.class);
    }

    @Test
    public void testWithContentType() {
        SimpleType simpleType = new SimpleType(Map.class);
        SimpleType withContentType = simpleType.withContentType(SimpleType.constructUnsafe(String.class));
        assertEquals("java.util.Map<java.lang.String>", withContentType.toString());
        assertTrue(withContentType.getContentType() instanceof SimpleType);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testWithContentType_exception() {
        SimpleType simpleType = new SimpleType(Map.class);
        simpleType.withContentType(SimpleType.constructUnsafe(Map.class));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testWithContentTypeHandler_exception() {
        SimpleType simpleType = new SimpleType(Map.class);
        simpleType.withContentTypeHandler(new Object());
    }

    @Test
    public void testWithStaticTyping() {
        SimpleType simpleType = new SimpleType(Map.class);
        SimpleType withStaticTyping = simpleType.withStaticTyping();
        assertTrue(withStaticTyping.isStatic());
    }

    @Test
    public void testEquals() {
        SimpleType simpleType1 = new SimpleType(Map.class);
        SimpleType simpleType2 = new SimpleType(Map.class);
        assertTrue(simpleType1.equals(simpleType2));
    }

    @Test
    public void testToString() {
        SimpleType simpleType = new SimpleType(Map.class);
        assertEquals("java.util.Map", simpleType.toString());
    }
}