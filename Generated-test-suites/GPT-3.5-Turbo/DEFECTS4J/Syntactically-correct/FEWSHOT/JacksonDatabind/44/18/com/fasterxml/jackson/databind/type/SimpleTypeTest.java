package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertNull;

import java.util.Collection;
import java.util.Map;

import org.junit.Test;

public class SimpleTypeTest {

    @Test
    public void testConstruct() {
        // Test construction of simple type
        SimpleType simpleType = SimpleType.construct(String.class);

        assertEquals(String.class, simpleType.getRawClass());
        assertNull(simpleType.getBindings());
        assertNull(simpleType.getSuperClass());
        assertNull(simpleType.getInterfaces());
        assertNull(simpleType.getValueHandler());
        assertNull(simpleType.getTypeHandler());
        assertFalse(simpleType.isStaticTyping());
        assertFalse(simpleType.isContainerType());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructWithMap() {
        // Test constructing a SimpleType with a Map
        SimpleType.construct(Map.class);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructWithCollection() {
        // Test constructing a SimpleType with a Collection
        SimpleType.construct(Collection.class);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructWithArray() {
        // Test constructing a SimpleType with an array
        SimpleType.construct(int[].class);
    }

    @Test
    public void testConstructUnsafe() {
        // Test constructing SimpleType unsafely
        SimpleType simpleType = SimpleType.constructUnsafe(String.class);

        assertEquals(String.class, simpleType.getRawClass());
        assertNull(simpleType.getBindings());
        assertNull(simpleType.getSuperClass());
        assertNull(simpleType.getInterfaces());
        assertNull(simpleType.getValueHandler());
        assertNull(simpleType.getTypeHandler());
        assertFalse(simpleType.isStaticTyping());
        assertFalse(simpleType.isContainerType());
    }

    @Test
    public void testNarrow() {
        // Test narrowing a SimpleType
        SimpleType simpleType = SimpleType.construct(String.class);
        SimpleType narrowedType = (SimpleType) simpleType._narrow(Object.class);

        assertEquals(Object.class, narrowedType.getRawClass());
        assertEquals(simpleType.getBindings(), narrowedType.getBindings());
        assertNull(narrowedType.getSuperClass());
        assertNull(narrowedType.getInterfaces());
        assertEquals(simpleType.getValueHandler(), narrowedType.getValueHandler());
        assertEquals(simpleType.getTypeHandler(), narrowedType.getTypeHandler());
        assertEquals(simpleType.isStaticTyping(), narrowedType.isStaticTyping());
        assertFalse(narrowedType.isContainerType());
    }

    @Test
    public void testWithContentType() {
        // Test withContentType() method on SimpleType
        SimpleType simpleType = SimpleType.construct(String.class);
        SimpleType newType = (SimpleType) simpleType.withContentType(Map.class);

        assertEquals(Map.class, newType.getRawClass());
        assertEquals(simpleType.getBindings(), newType.getBindings());
        assertNull(newType.getSuperClass());
        assertNull(newType.getInterfaces());
        assertEquals(simpleType.getValueHandler(), newType.getValueHandler());
        assertEquals(simpleType.getTypeHandler(), newType.getTypeHandler());
        assertEquals(simpleType.isStaticTyping(), newType.isStaticTyping());
        assertFalse(newType.isContainerType());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testWithContentTypeThrowsException() {
        // Test withContentType() method on SimpleType with content type
        SimpleType simpleType = SimpleType.construct(String.class);
        simpleType.withContentType(Map.class);
    }

    @Test
    public void testWithStaticTyping() {
        // Test withStaticTyping() method on SimpleType
        SimpleType simpleType = SimpleType.construct(String.class);
        SimpleType newType = (SimpleType) simpleType.withStaticTyping();

        assertEquals(String.class, newType.getRawClass());
        assertEquals(simpleType.getBindings(), newType.getBindings());
        assertNull(newType.getSuperClass());
        assertNull(newType.getInterfaces());
        assertEquals(simpleType.getValueHandler(), newType.getValueHandler());
        assertEquals(simpleType.getTypeHandler(), newType.getTypeHandler());
        assertTrue(newType.isStaticTyping());
        assertFalse(newType.isContainerType());
    }

    @Test
    public void testRefine() {
        // Test refine() method on SimpleType
        SimpleType simpleType = SimpleType.construct(String.class);
        JavaType refinedType = simpleType.refine(Object.class, null, null, null);

        assertNull(refinedType);
    }

    @Test
    public void testToString() {
        // Test toString() method on SimpleType
        SimpleType simpleType = SimpleType.construct(String.class);

        assertEquals("[simple type, class java.lang.String]", simpleType.toString());
    }

    @Test
    public void testEquals() {
        // Test equals() method on SimpleType
        SimpleType simpleType1 = SimpleType.construct(String.class);
        SimpleType simpleType2 = SimpleType.construct(String.class);
        SimpleType simpleType3 = SimpleType.construct(Object.class);

        assertTrue(simpleType1.equals(simpleType2));
        assertFalse(simpleType1.equals(simpleType3));
    }

    @Test
    public void testGetErasedSignature() {
        // Test getErasedSignature() method on SimpleType
        SimpleType simpleType = SimpleType.construct(String.class);
        StringBuilder sb = new StringBuilder();
        StringBuilder result = simpleType.getErasedSignature(sb);

        assertSame(sb, result);
    }

    @Test
    public void testGetGenericSignature() {
        // Test getGenericSignature() method on SimpleType
        SimpleType simpleType = SimpleType.construct(String.class);
        StringBuilder sb = new StringBuilder();
        StringBuilder result = simpleType.getGenericSignature(sb);

        assertSame(sb, result);
    }

    @Test
    public void testBuildCanonicalName() {
        // Test buildCanonicalName() method on SimpleType
        SimpleType simpleType = SimpleType.construct(String.class);

        assertEquals("java.lang.String", simpleType.buildCanonicalName());
    }

    @Test
    public void testIsContainerType() {
        // Test isContainerType() method on SimpleType
        SimpleType simpleType = SimpleType.construct(String.class);

        assertFalse(simpleType.isContainerType());
    }
}