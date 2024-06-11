
package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import com.fasterxml.jackson.databind.JavaType;
import org.junit.Test;

public class ResolvedRecursiveTypeTest {
    @Test
    public void testConstructor() {
        TypeBindings bindings = new TypeBindings();
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(String.class, bindings);

        assertEquals(String.class, resolvedRecursiveType.getRawClass());
        assertEquals(bindings, resolvedRecursiveType.getBindings());
        assertNull(resolvedRecursiveType.getKeyType());
        assertNull(resolvedRecursiveType.getValueType());
        assertEquals(0, resolvedRecursiveType.getArrayDepth());
        assertNull(resolvedRecursiveType.getSuperClass());
        assertNull(resolvedRecursiveType.getSuperInterfaces());
        assertFalse(resolvedRecursiveType.isContainerType());
    }

    @Test
    public void testSetReference() {
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(String.class, new TypeBindings());
        JavaType ref = new SimpleType("Reference", new TypeBindings());
        resolvedRecursiveType.setReference(ref);

        assertEquals(ref, resolvedRecursiveType.getSelfReferencedType());
    }

    @Test(expected = IllegalStateException.class)
    public void testSetReferenceTwice() {
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(String.class, new TypeBindings());
        JavaType ref1 = new SimpleType("Reference1", new TypeBindings());
        JavaType ref2 = new SimpleType("Reference2", new TypeBindings());
        resolvedRecursiveType.setReference(ref1);
        resolvedRecursiveType.setReference(ref2);
    }

    @Test
    public void testGetGenericSignature() {
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(String.class, new TypeBindings());
        JavaType ref = new SimpleType("Reference", new TypeBindings());
        resolvedRecursiveType.setReference(ref);

        StringBuilder sb = new StringBuilder();
        StringBuilder expected = new StringBuilder();
        ref.getGenericSignature(expected);
        assertEquals(expected.toString(), resolvedRecursiveType.getGenericSignature(sb).toString());
    }

    @Test
    public void testGetErasedSignature() {
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(String.class, new TypeBindings());
        JavaType ref = new SimpleType("Reference", new TypeBindings());
        resolvedRecursiveType.setReference(ref);

        StringBuilder sb = new StringBuilder();
        StringBuilder expected = new StringBuilder();
        ref.getErasedSignature(expected);
        assertEquals(expected.toString(), resolvedRecursiveType.getErasedSignature(sb).toString());
    }

    @Test
    public void testWithContentType() {
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(String.class, new TypeBindings());
        JavaType contentType = new SimpleType("ContentType", new TypeBindings());

        assertEquals(resolvedRecursiveType, resolvedRecursiveType.withContentType(contentType));
    }

    @Test
    public void testWithTypeHandler() {
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(String.class, new TypeBindings());
        Object handler = new Object();

        assertEquals(resolvedRecursiveType, resolvedRecursiveType.withTypeHandler(handler));
    }

    @Test
    public void testWithContentTypeHandler() {
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(String.class, new TypeBindings());
        Object handler = new Object();

        assertEquals(resolvedRecursiveType, resolvedRecursiveType.withContentTypeHandler(handler));
    }

    @Test
    public void testWithValueHandler() {
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(String.class, new TypeBindings());
        Object handler = new Object();

        assertEquals(resolvedRecursiveType, resolvedRecursiveType.withValueHandler(handler));
    }

    @Test
    public void testWithContentValueHandler() {
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(String.class, new TypeBindings());
        Object handler = new Object();

        assertEquals(resolvedRecursiveType, resolvedRecursiveType.withContentValueHandler(handler));
    }

    @Test
    public void testWithStaticTyping() {
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(String.class, new TypeBindings());

        assertEquals(resolvedRecursiveType, resolvedRecursiveType.withStaticTyping());
    }

    @Test
    public void testEquals() {
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(String.class, new TypeBindings());
        JavaType ref1 = new SimpleType("Reference1", new TypeBindings());
        JavaType ref2 = new SimpleType("Reference2", new TypeBindings());
        resolvedRecursiveType.setReference(ref1);

        ResolvedRecursiveType resolvedRecursiveType1 = new ResolvedRecursiveType(String.class, new TypeBindings());
        resolvedRecursiveType1.setReference(ref1);

        assertTrue(resolvedRecursiveType.equals(resolvedRecursiveType1));

        ResolvedRecursiveType resolvedRecursiveType2 = new ResolvedRecursiveType(String.class, new TypeBindings());
        resolvedRecursiveType2.setReference(ref2);

        assertFalse(resolvedRecursiveType.equals(resolvedRecursiveType2));
    }

    @Test
    public void testRefine() {
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(String.class, new TypeBindings());
        assertNull(resolvedRecursiveType.refine(null, new TypeBindings(), null, null));
    }

    @Test
    public void testToStringUnresolved() {
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(String.class, new TypeBindings());
        assertEquals("[recursive type; UNRESOLVED]", resolvedRecursiveType.toString());
    }

    @Test
    public void testToStringResolved() {
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(String.class, new TypeBindings());
        JavaType ref = new SimpleType("Reference", new TypeBindings());
        resolvedRecursiveType.setReference(ref);

        assertEquals("[recursive type; Reference]", resolvedRecursiveType.toString());
    }
}
