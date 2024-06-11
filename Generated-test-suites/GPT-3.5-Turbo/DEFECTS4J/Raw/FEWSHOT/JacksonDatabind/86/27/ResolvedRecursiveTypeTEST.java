
package com.fasterxml.jackson.databind.type;

import com.fasterxml.jackson.databind.JavaType;

import org.junit.Test;
import static org.junit.Assert.*;

public class ResolvedRecursiveTypeTest {

    @Test
    public void testConstructor() {
        Class<?> erasedType = Object.class;
        TypeBindings bindings = new TypeBindings();
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(erasedType, bindings);

        assertEquals(erasedType, resolvedRecursiveType.getRawClass());
        assertEquals(bindings, resolvedRecursiveType.getTypeBindings());
        assertNull(resolvedRecursiveType.getContentType());
        assertNull(resolvedRecursiveType.getKeyType());
        assertEquals(0, resolvedRecursiveType.getBindingsCount());
        assertNull(resolvedRecursiveType.getSuperClass());
        assertNull(resolvedRecursiveType.getInterfaces());
        assertFalse(resolvedRecursiveType.isContainerType());
    }

    @Test
    public void testSetReference() {
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(Object.class, new TypeBindings());
        JavaType ref = new JavaType(Object.class);
        resolvedRecursiveType.setReference(ref);

        assertEquals(ref, resolvedRecursiveType.getSelfReferencedType());
    }

    @Test(expected = IllegalStateException.class)
    public void testSetReferenceMultipleTimes() {
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(Object.class, new TypeBindings());
        JavaType ref1 = new JavaType(Object.class);
        JavaType ref2 = new JavaType(String.class);
        resolvedRecursiveType.setReference(ref1);
        resolvedRecursiveType.setReference(ref2);
    }

    @Test
    public void testGetGenericSignature() {
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(Object.class, new TypeBindings());
        StringBuilder sb = new StringBuilder();
        resolvedRecursiveType.getGenericSignature(sb);

        assertEquals(sb.toString(), resolvedRecursiveType.getSelfReferencedType().getGenericSignature(sb).toString());
    }

    @Test
    public void testGetErasedSignature() {
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(Object.class, new TypeBindings());
        StringBuilder sb = new StringBuilder();
        resolvedRecursiveType.getErasedSignature(sb);

        assertEquals(sb.toString(), resolvedRecursiveType.getSelfReferencedType().getErasedSignature(sb).toString());
    }

    @Test
    public void testWithContentType() {
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(Object.class, new TypeBindings());
        JavaType contentType = new JavaType(Object.class);
        JavaType withContentType = resolvedRecursiveType.withContentType(contentType);

        assertEquals(resolvedRecursiveType, withContentType);
    }

    @Test
    public void testWithTypeHandler() {
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(Object.class, new TypeBindings());
        Object h = null;
        JavaType withTypeHandler = resolvedRecursiveType.withTypeHandler(h);

        assertEquals(resolvedRecursiveType, withTypeHandler);
    }

    @Test
    public void testWithContentTypeHandler() {
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(Object.class, new TypeBindings());
        Object h = null;
        JavaType withContentTypeHandler = resolvedRecursiveType.withContentTypeHandler(h);

        assertEquals(resolvedRecursiveType, withContentTypeHandler);
    }

    @Test
    public void testWithValueHandler() {
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(Object.class, new TypeBindings());
        Object h = null;
        JavaType withValueHandler = resolvedRecursiveType.withValueHandler(h);

        assertEquals(resolvedRecursiveType, withValueHandler);
    }

    @Test
    public void testWithContentValueHandler() {
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(Object.class, new TypeBindings());
        Object h = null;
        JavaType withContentValueHandler = resolvedRecursiveType.withContentValueHandler(h);

        assertEquals(resolvedRecursiveType, withContentValueHandler);
    }

    @Test
    public void testWithStaticTyping() {
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(Object.class, new TypeBindings());
        JavaType withStaticTyping = resolvedRecursiveType.withStaticTyping();

        assertEquals(resolvedRecursiveType, withStaticTyping);
    }

    @Test
    public void testNarrow() {
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(Object.class, new TypeBindings());
        Class<?> subclass = Object.class;
        JavaType narrowed = resolvedRecursiveType._narrow(subclass);

        assertEquals(resolvedRecursiveType, narrowed);
    }

    @Test
    public void testRefine() {
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(Object.class, new TypeBindings());
        Class<?> rawType = Object.class;
        TypeBindings bindings = new TypeBindings();
        JavaType superClass = null;
        JavaType[] superInterfaces = null;
        JavaType refined = resolvedRecursiveType.refine(rawType, bindings, superClass, superInterfaces);

        assertNull(refined);
    }

    @Test
    public void testToString() {
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(Object.class, new TypeBindings());
        String toString = resolvedRecursiveType.toString();

        StringBuilder sb = new StringBuilder(40)
                .append("[recursive type; ");
        assertNull(resolvedRecursiveType.getSelfReferencedType());
        assertEquals(sb.toString(), resolvedRecursiveType.toString());
    }

    @Test
    public void testEquals() {
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(Object.class, new TypeBindings());
        ResolvedRecursiveType sameType = new ResolvedRecursiveType(Object.class, new TypeBindings());
        assertTrue(resolvedRecursiveType.equals(resolvedRecursiveType));
        assertFalse(resolvedRecursiveType.equals(null));
        assertFalse(resolvedRecursiveType.equals("Not a ResolvedRecursiveType"));
        assertFalse(resolvedRecursiveType.equals(sameType));
    }

}
