package com.example.junit.test;

import static org.junit.Assert.*;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.ResolvedRecursiveType;

import org.junit.Test;

/**
 * Test class for ResolvedRecursiveType
 */
public class ResolvedRecursiveTypeTest {

    /**
     * Test constructor ResolvedRecursiveType(Class<?> erasedType, TypeBindings bindings)
     */
    @Test
    public void testConstructor() {
        Class<?> erasedType = Integer.class;
        TypeBindings bindings = new TypeBindings();
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(erasedType, bindings);
        assertNotNull(resolvedRecursiveType);
    }

    /**
     * Test method setReference(JavaType ref)
     */
    @Test
    public void testSetReference() {
        Class<?> erasedType = Integer.class;
        TypeBindings bindings = new TypeBindings();
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(erasedType, bindings);
        JavaType ref = new SimpleType("String");
        resolvedRecursiveType.setReference(ref);
        assertNotNull(resolvedRecursiveType.getSelfReferencedType());
        assertEquals(ref, resolvedRecursiveType.getSelfReferencedType());

        // Test exception
        JavaType ref2 = new SimpleType("String");
        resolvedRecursiveType.setReference(ref2);
        try {
            resolvedRecursiveType.setReference(ref);
        } catch (IllegalStateException e) {
            assertTrue(e.getMessage().contains("Trying to re-set self reference; old value = " + ref2 + ", new = " + ref));
        }
    }

    /**
     * Test method getSelfReferencedType()
     */
    @Test
    public void testGetSelfReferencedType() {
        Class<?> erasedType = Integer.class;
        TypeBindings bindings = new TypeBindings();
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(erasedType, bindings);
        JavaType ref = new SimpleType("String");
        resolvedRecursiveType.setReference(ref);
        assertNotNull(resolvedRecursiveType.getSelfReferencedType());
        assertEquals(ref, resolvedRecursiveType.getSelfReferencedType());
        assertNull(new ResolvedRecursiveType(erasedType, bindings).getSelfReferencedType());
    }

    /**
     * Test method getGenericSignature(StringBuilder sb)
     */
    @Test
    public void testGetGenericSignature() {
        Class<?> erasedType = Integer.class;
        TypeBindings bindings = new TypeBindings();
        JavaType ref = new SimpleType("String");
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(erasedType, bindings);
        resolvedRecursiveType.setReference(ref);
        StringBuilder sb = new StringBuilder(100);
        resolvedRecursiveType.getGenericSignature(sb);
        String expected = "[recursive type; " + ref.getGenericSignature(sb).toString() + "]";
        assertEquals(expected, sb.toString());
    }

    /**
     * Test method getErasedSignature(StringBuilder sb)
     */
    @Test
    public void testGetErasedSignature() {
        Class<?> erasedType = Integer.class;
        TypeBindings bindings = new TypeBindings();
        JavaType ref = new SimpleType("String");
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(erasedType, bindings);
        resolvedRecursiveType.setReference(ref);
        StringBuilder sb = new StringBuilder(100);
        resolvedRecursiveType.getErasedSignature(sb);
        String expected = ref.getErasedSignature(sb).toString();
        assertEquals(expected, sb.toString());
    }

    /**
     * Test method withContentType(JavaType contentType)
     */
    @Test
    public void testWithContentType() {
        Class<?> erasedType = Integer.class;
        TypeBindings bindings = new TypeBindings();
        JavaType ref = new SimpleType("String");
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(erasedType, bindings);
        resolvedRecursiveType.setReference(ref);
        JavaType contentType = new SimpleType("Boolean");
        ResolvedRecursiveType result = resolvedRecursiveType.withContentType(contentType);
        assertSame(resolvedRecursiveType, result);
    }

    /**
     * Test method withTypeHandler(Object h)
     */
    @Test
    public void testWithTypeHandler() {
        Class<?> erasedType = Integer.class;
        TypeBindings bindings = new TypeBindings();
        JavaType ref = new SimpleType("String");
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(erasedType, bindings);
        resolvedRecursiveType.setReference(ref);
        Object h = new Object() {
        };
        ResolvedRecursiveType result = resolvedRecursiveType.withTypeHandler(h);
        assertSame(resolvedRecursiveType, result);
    }

    /**
     * Test method withContentTypeHandler(Object h)
     */
    @Test
    public void testWithContentTypeHandler() {
        Class<?> erasedType = Integer.class;
        TypeBindings bindings = new TypeBindings();
        JavaType ref = new SimpleType("String");
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(erasedType, bindings);
        resolvedRecursiveType.setReference(ref);
        Object h = new Object() {
        };
        ResolvedRecursiveType result = resolvedRecursiveType.withContentTypeHandler(h);
        assertSame(resolvedRecursiveType, result);
    }

    /**
     * Test method withValueHandler(Object h)
     */
    @Test
    public void testWithValueHandler() {
        Class<?> erasedType = Integer.class;
        TypeBindings bindings = new TypeBindings();
        JavaType ref = new SimpleType("String");
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(erasedType, bindings);
        resolvedRecursiveType.setReference(ref);
        Object h = new Object() {
        };
        ResolvedRecursiveType result = resolvedRecursiveType.withValueHandler(h);
        assertSame(resolvedRecursiveType, result);
    }

    /**
     * Test method withContentValueHandler(Object h)
     */
    @Test
    public void testWithContentValueHandler() {
        Class<?> erasedType = Integer.class;
        TypeBindings bindings = new TypeBindings();
        JavaType ref = new SimpleType("String");
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(erasedType, bindings);
        resolvedRecursiveType.setReference(ref);
        Object h = new Object() {
        };
        ResolvedRecursiveType result = resolvedRecursiveType.withContentValueHandler(h);
        assertSame(resolvedRecursiveType, result);
    }

    /**
     * Test method withStaticTyping()
     */
    @Test
    public void testWithStaticTyping() {
        Class<?> erasedType = Integer.class;
        TypeBindings bindings = new TypeBindings();
        JavaType ref = new SimpleType("String");
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(erasedType, bindings);
        resolvedRecursiveType.setReference(ref);
        ResolvedRecursiveType result = resolvedRecursiveType.withStaticTyping();
        assertSame(resolvedRecursiveType, result);
    }

    /**
     * Test method _narrow(Class<?> subclass)
     * Deprecated method, not tested
     */

    /**
     * Test method refine(Class<?> rawType, TypeBindings bindings, JavaType superClass, JavaType[] superInterfaces)
     */
    @Test
    public void testRefine() {
        Class<?> erasedType = Integer.class;
        TypeBindings bindings = new TypeBindings();
        JavaType ref = new SimpleType("String");
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(erasedType, bindings);
        resolvedRecursiveType.setReference(ref);
        Class<?> rawType = Integer[].class;
        JavaType superClass = new SimpleType("List");
        JavaType[] superInterfaces = new JavaType[0];
        ResolvedRecursiveType result = resolvedRecursiveType.refine(rawType, bindings, superClass, superInterfaces);
        assertNull(result);
    }

    /**
     * Test method isContainerType()
     */
    @Test
    public void testIsContainerType() {
        Class<?> erasedType = Integer.class;
        TypeBindings bindings = new TypeBindings();
        JavaType ref = new SimpleType("String");
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(erasedType, bindings);
        resolvedRecursiveType.setReference(ref);
        assertFalse(resolvedRecursiveType.isContainerType());
    }

    /**
     * Test method toString()
     */
    @Test
    public void testToString() {
        Class<?> erasedType = Integer.class;
        TypeBindings bindings = new TypeBindings();
        JavaType ref = new SimpleType("String");
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(erasedType, bindings);
        resolvedRecursiveType.setReference(ref);
        String expected = "[recursive type; " + ref.getRawClass().getName() + "]";
        assertEquals(expected, resolvedRecursiveType.toString());
    }

    /**
     * Test method equals(Object o)
     */
    @Test
    public void testEquals() {
        Class<?> erasedType = Integer.class;
        TypeBindings bindings = new TypeBindings();
        JavaType ref1 = new SimpleType("String");
        JavaType ref2 = new SimpleType("Integer");
        ResolvedRecursiveType resolvedRecursiveType1 = new ResolvedRecursiveType(erasedType, bindings);
        resolvedRecursiveType1.setReference(ref1);
        ResolvedRecursiveType resolvedRecursiveType2 = new ResolvedRecursiveType(erasedType, bindings);
        resolvedRecursiveType2.setReference(ref2);
        assertFalse(resolvedRecursiveType1.equals(resolvedRecursiveType2));

        resolvedRecursiveType1 = new ResolvedRecursiveType(erasedType, bindings);
        resolvedRecursiveType1.setReference(ref1);
        resolvedRecursiveType2 = new ResolvedRecursiveType(erasedType, bindings);
        resolvedRecursiveType2.setReference(ref1);
        assertTrue(resolvedRecursiveType1.equals(resolvedRecursiveType2));

        resolvedRecursiveType1 = new ResolvedRecursiveType(erasedType, bindings);
        resolvedRecursiveType2 = new ResolvedRecursiveType(erasedType, bindings);
        assertFalse(resolvedRecursiveType1.equals(resolvedRecursiveType2));
    }
}