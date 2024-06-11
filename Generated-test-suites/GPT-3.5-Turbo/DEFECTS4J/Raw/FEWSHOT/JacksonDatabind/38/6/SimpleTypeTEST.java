package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Collection;

import org.junit.Test;

public class SimpleTypeTest {

    @Test
    public void testConstructUnsafe() {
        SimpleType simpleType = SimpleType.constructUnsafe(Object.class);
        assertEquals(Object.class, simpleType.getRawClass());
        assertNull(simpleType.getBindings());
        assertNull(simpleType.getSuperClass());
        assertNull(simpleType.getSuperInterfaces());
        assertFalse(simpleType.asStatic());
        assertNull(simpleType.getValueHandler());
        assertNull(simpleType.getTypeHandler());
    }

    @Test
    public void testConstruct() {
        SimpleType simpleType = SimpleType.construct(Object.class);
        assertEquals(Object.class, simpleType.getRawClass());
        assertNull(simpleType.getBindings());
        assertNull(simpleType.getSuperClass());
        assertNull(simpleType.getSuperInterfaces());
        assertFalse(simpleType.asStatic());
        assertNull(simpleType.getValueHandler());
        assertNull(simpleType.getTypeHandler());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructWithMap() {
        SimpleType.construct(java.util.Map.class);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructWithCollection() {
        SimpleType.construct(Collection.class);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructWithArray() {
        SimpleType.construct(Object[].class);
    }

    @Test
    public void testNarrow() {
        SimpleType simpleType = SimpleType.construct(Object.class);
        SimpleType narrowedSimpleType = simpleType.narrow(String.class);
        assertEquals(String.class, narrowedSimpleType.getRawClass());
        assertNull(narrowedSimpleType.getBindings());
        assertEquals(simpleType, narrowedSimpleType.getSuperClass());
        assertNull(narrowedSimpleType.getSuperInterfaces());
        assertFalse(narrowedSimpleType.asStatic());
        assertNull(narrowedSimpleType.getValueHandler());
        assertNull(narrowedSimpleType.getTypeHandler());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNarrowWithIncompatibleClass() {
        SimpleType simpleType = SimpleType.construct(Object.class);
        simpleType.narrow(Integer.class);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testWithContentType() {
        SimpleType simpleType = SimpleType.construct(Object.class);
        simpleType.withContentType(String.class);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testWithContentTypeHandler() {
        SimpleType simpleType = SimpleType.construct(Object.class);
        simpleType.withContentTypeHandler(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testWithContentValueHandler() {
        SimpleType simpleType = SimpleType.construct(Object.class);
        simpleType.withContentValueHandler(null);
    }

    @Test
    public void testWithStaticTyping() {
        SimpleType simpleType = SimpleType.construct(Object.class);
        SimpleType staticSimpleType = simpleType.withStaticTyping();
        assertEquals(simpleType.getRawClass(), staticSimpleType.getRawClass());
        assertEquals(simpleType.getBindings(), staticSimpleType.getBindings());
        assertEquals(simpleType.getSuperClass(), staticSimpleType.getSuperClass());
        assertEquals(simpleType.getSuperInterfaces(), staticSimpleType.getSuperInterfaces());
        assertFalse(simpleType.asStatic());
        assertTrue(staticSimpleType.asStatic());
        assertEquals(simpleType.getValueHandler(), staticSimpleType.getValueHandler());
        assertEquals(simpleType.getTypeHandler(), staticSimpleType.getTypeHandler());
    }

    @Test
    public void testRefine() {
        SimpleType simpleType = SimpleType.construct(Object.class);
        assertNull(simpleType.refine(String.class, null, null, null));
    }

    @Test
    public void testIsContainerType() {
        SimpleType simpleType = SimpleType.construct(Object.class);
        assertFalse(simpleType.isContainerType());
    }

    @Test
    public void testGetErasedSignature() {
        SimpleType simpleType = SimpleType.construct(Object.class);
        StringBuilder sb = simpleType.getErasedSignature(new StringBuilder());
        assertEquals("Ljava/lang/Object;", sb.toString());
    }

    @Test
    public void testGetGenericSignature() {
        SimpleType simpleType = SimpleType.construct(Object.class);
        StringBuilder sb = simpleType.getGenericSignature(new StringBuilder());
        assertEquals("Ljava/lang/Object;", sb.toString());
    }

    @Test
    public void testBuildCanonicalName() {
        SimpleType simpleType = SimpleType.construct(Object.class);
        StringBuilder sb = new StringBuilder();
        simpleType.buildCanonicalName(sb);
        assertEquals("java.lang.Object", sb.toString());
    }

    @Test
    public void testToString() {
        SimpleType simpleType = SimpleType.construct(Object.class);
        assertEquals("[simple type, class java.lang.Object]", simpleType.toString());
    }

    @Test
    public void testEquals() {
        SimpleType simpleType1 = SimpleType.construct(Object.class);
        SimpleType simpleType2 = SimpleType.construct(Object.class);
        assertTrue(simpleType1.equals(simpleType2));
    }

    @Test
    public void testEqualsWithDifferentClass() {
        SimpleType simpleType1 = SimpleType.construct(Object.class);
        SimpleType simpleType2 = SimpleType.construct(String.class);
        assertFalse(simpleType1.equals(simpleType2));
    }

    @Test
    public void testEqualsWithDifferentBindings() {
        SimpleType simpleType1 = SimpleType.construct(Object.class);
        TypeBindings typeBindings = new TypeBindings();
        SimpleType simpleType2 = new SimpleType(Object.class, typeBindings, null, null);
        assertFalse(simpleType1.equals(simpleType2));
    }
}