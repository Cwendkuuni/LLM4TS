package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.TypeBindings;

public class SimpleTypeTest {
    @Test
    public void testConstructUnsafe() {
        SimpleType type = SimpleType.constructUnsafe(String.class);
        assertEquals(String.class, type.getRawClass());
        assertNull(type.getBindings());
        assertNull(type.getSuperClass());
        assertNull(type.getSuperInterfaces());
        assertNull(type.getValueHandler());
        assertNull(type.getTypeHandler());
        assertFalse(type.isContainerType());
    }

    @Test
    public void testConstructThrowsExceptionForMapClass() {
        try {
            SimpleType.construct(Map.class);
        } catch (IllegalArgumentException e) {
            assertEquals("Can not construct SimpleType for a Map (class: java.util.Map)", e.getMessage());
        }
    }

    @Test
    public void testConstructThrowsExceptionForCollectionClass() {
        try {
            SimpleType.construct(Collection.class);
        } catch (IllegalArgumentException e) {
            assertEquals("Can not construct SimpleType for a Collection (class: java.util.Collection)", e.getMessage());
        }
    }

    @Test
    public void testConstructThrowsExceptionForArrayClass() {
        try {
            SimpleType.construct(String[].class);
        } catch (IllegalArgumentException e) {
            assertEquals("Can not construct SimpleType for an array (class: [Ljava.lang.String;)", e.getMessage());
        }
    }

    @Test
    public void testNarrow() {
        SimpleType base = SimpleType.constructUnsafe(Number.class);
        SimpleType narrow = base.narrow(Integer.class);
        assertTrue(narrow instanceof SimpleType);
        assertEquals(Integer.class, narrow.getRawClass());
        assertNull(narrow.getBindings());
        assertEquals(base, narrow.getSuperClass());
        assertNull(narrow.getSuperInterfaces());
        assertNull(narrow.getValueHandler());
        assertEquals(base.getTypeHandler(), narrow.getTypeHandler());
        assertFalse(narrow.isContainerType());
    }

    @Test
    public void testWithTypeHandler() {
        SimpleType type = SimpleType.constructUnsafe(String.class);
        SimpleType withTypeHandler = type.withTypeHandler("typeHandler");
        assertTrue(withTypeHandler instanceof SimpleType);
        assertEquals(String.class, withTypeHandler.getRawClass());
        assertNull(withTypeHandler.getBindings());
        assertNull(withTypeHandler.getSuperClass());
        assertNull(withTypeHandler.getSuperInterfaces());
        assertNull(withTypeHandler.getValueHandler());
        assertEquals("typeHandler", withTypeHandler.getTypeHandler());
        assertFalse(withTypeHandler.isContainerType());
    }

    @Test
    public void testWithStaticTyping() {
        SimpleType type = SimpleType.constructUnsafe(String.class);
        SimpleType withStaticTyping = type.withStaticTyping();
        assertTrue(withStaticTyping instanceof SimpleType);
        assertEquals(String.class, withStaticTyping.getRawClass());
        assertNull(withStaticTyping.getBindings());
        assertNull(withStaticTyping.getSuperClass());
        assertNull(withStaticTyping.getSuperInterfaces());
        assertNull(withStaticTyping.getValueHandler());
        assertNull(withStaticTyping.getTypeHandler());
        assertTrue(withStaticTyping.isStaticTyping());
        assertFalse(withStaticTyping.isContainerType());
    }

    @Test
    public void testRefine() {
        SimpleType type = SimpleType.constructUnsafe(String.class);
        JavaType refinedType = type.refine(Object.class, TypeBindings.emptyBindings(), null, null);
        assertNull(refinedType);
    }
    
    @Test
    public void testBuildCanonicalNameWithEmptyBindings() {
        SimpleType type = SimpleType.constructUnsafe(String.class);
        StringBuilder sb = new StringBuilder();
        type.buildCanonicalName(sb);
        assertEquals("java.lang.String", sb.toString());
    }
    
    @Test
    public void testBuildCanonicalNameWithNotEmptyBindings() {
        SimpleType type = SimpleType.constructUnsafe(Map.class).withContentType(String.class);
        StringBuilder sb = new StringBuilder();
        type.buildCanonicalName(sb);
        assertEquals("java.util.Map<java.lang.String>", sb.toString());
    }

    @Test
    public void testGetErasedSignature() {
        SimpleType type = SimpleType.constructUnsafe(String.class);
        StringBuilder sb = new StringBuilder();
        type.getErasedSignature(sb);
        assertEquals("Ljava/lang/String;", sb.toString());
    }
    
    @Test
    public void testGetGenericSignatureWithEmptyBindings() {
        SimpleType type = SimpleType.constructUnsafe(String.class);
        StringBuilder sb = new StringBuilder();
        type.getGenericSignature(sb);
        assertEquals("Ljava/lang/String;", sb.toString());
    }
    
    @Test
    public void testGetGenericSignatureWithNotEmptyBindings() {
        SimpleType type = SimpleType.construct(Map.class).withContentType(String.class);
        StringBuilder sb = new StringBuilder();
        type.getGenericSignature(sb);
        assertEquals("Ljava/util/Map<Ljava/lang/String;>;", sb.toString());
    }

    @Test
    public void testToString() {
        SimpleType type = SimpleType.constructUnsafe(String.class);
        assertEquals("[simple type, class java.lang.String]", type.toString());
    }

    @Test
    public void testEquals() {
        SimpleType type = SimpleType.constructUnsafe(String.class);
        SimpleType sameType = SimpleType.constructUnsafe(String.class);
        SimpleType differentType = SimpleType.constructUnsafe(Integer.class);
        TypeBindings bindings = TypeBindings.emptyBindings();
        SimpleType typeWithBindings = new SimpleType(String.class, bindings, null, null);
        SimpleType differentTypeWithBindings = new SimpleType(Integer.class, bindings, null, null);
        assertTrue(type.equals(sameType));
        assertFalse(type.equals(differentType));
        assertFalse(type.equals(null));
        assertFalse(type.equals("string"));
        assertFalse(type.equals(typeWithBindings));
        assertFalse(typeWithBindings.equals(differentTypeWithBindings));
    }
}