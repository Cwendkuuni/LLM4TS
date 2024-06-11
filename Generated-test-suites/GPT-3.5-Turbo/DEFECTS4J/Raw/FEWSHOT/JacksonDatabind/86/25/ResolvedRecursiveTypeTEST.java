package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import org.junit.Test;

public class ResolvedRecursiveTypeTest {

    @Test
    public void testSetReference() {
        JavaType ref = new ResolvedRecursiveType(String.class, new TypeBindings());
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(ResolvedRecursiveType.class, new TypeBindings());
        resolvedRecursiveType.setReference(ref);
        assertEquals(ref, resolvedRecursiveType.getSelfReferencedType());
    }

    @Test(expected = IllegalStateException.class)
    public void testSetReferenceMultipleTimes() {
        JavaType ref = new ResolvedRecursiveType(String.class, new TypeBindings());
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(ResolvedRecursiveType.class, new TypeBindings());
        resolvedRecursiveType.setReference(ref);
        resolvedRecursiveType.setReference(ref);
    }

    @Test
    public void testGetGenericSignature() {
        JavaType ref = new ResolvedRecursiveType(String.class, new TypeBindings());
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(ResolvedRecursiveType.class, new TypeBindings());
        resolvedRecursiveType.setReference(ref);
        StringBuilder sb = new StringBuilder();
        assertEquals(ref.getGenericSignature(sb), resolvedRecursiveType.getGenericSignature(sb));
    }

    @Test
    public void testGetErasedSignature() {
        JavaType ref = new ResolvedRecursiveType(String.class, new TypeBindings());
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(ResolvedRecursiveType.class, new TypeBindings());
        resolvedRecursiveType.setReference(ref);
        StringBuilder sb = new StringBuilder();
        assertEquals(ref.getErasedSignature(sb), resolvedRecursiveType.getErasedSignature(sb));
    }

    @Test
    public void testWithContentType() {
        JavaType ref = new ResolvedRecursiveType(String.class, new TypeBindings());
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(ResolvedRecursiveType.class, new TypeBindings());
        resolvedRecursiveType.setReference(ref);
        assertEquals(resolvedRecursiveType, resolvedRecursiveType.withContentType(null));
    }

    @Test
    public void testWithTypeHandler() {
        JavaType ref = new ResolvedRecursiveType(String.class, new TypeBindings());
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(ResolvedRecursiveType.class, new TypeBindings());
        resolvedRecursiveType.setReference(ref);
        assertEquals(resolvedRecursiveType, resolvedRecursiveType.withTypeHandler(null));
    }

    @Test
    public void testWithContentTypeHandler() {
        JavaType ref = new ResolvedRecursiveType(String.class, new TypeBindings());
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(ResolvedRecursiveType.class, new TypeBindings());
        resolvedRecursiveType.setReference(ref);
        assertEquals(resolvedRecursiveType, resolvedRecursiveType.withContentTypeHandler(null));
    }

    @Test
    public void testWithValueHandler() {
        JavaType ref = new ResolvedRecursiveType(String.class, new TypeBindings());
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(ResolvedRecursiveType.class, new TypeBindings());
        resolvedRecursiveType.setReference(ref);
        assertEquals(resolvedRecursiveType, resolvedRecursiveType.withValueHandler(null));
    }

    @Test
    public void testWithContentValueHandler() {
        JavaType ref = new ResolvedRecursiveType(String.class, new TypeBindings());
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(ResolvedRecursiveType.class, new TypeBindings());
        resolvedRecursiveType.setReference(ref);
        assertEquals(resolvedRecursiveType, resolvedRecursiveType.withContentValueHandler(null));
    }

    @Test
    public void testWithStaticTyping() {
        JavaType ref = new ResolvedRecursiveType(String.class, new TypeBindings());
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(ResolvedRecursiveType.class, new TypeBindings());
        resolvedRecursiveType.setReference(ref);
        assertEquals(resolvedRecursiveType, resolvedRecursiveType.withStaticTyping());
    }

    @Test
    public void testNarrow() {
        FakeResolvedRecursiveType fakeResolvedRecursiveType = new FakeResolvedRecursiveType(ResolvedRecursiveType.class, new TypeBindings());
        JavaType narrowedType = fakeResolvedRecursiveType._narrow(String.class);
        assertEquals(fakeResolvedRecursiveType, narrowedType);
    }

    @Test
    public void testRefine() {
        JavaType ref = new ResolvedRecursiveType(String.class, new TypeBindings());
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(ResolvedRecursiveType.class, new TypeBindings());
        resolvedRecursiveType.setReference(ref);
        assertNull(resolvedRecursiveType.refine(String.class, new TypeBindings(), null, null));
    }

    @Test
    public void testIsContainerType() {
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(ResolvedRecursiveType.class, new TypeBindings());
        assertEquals(false, resolvedRecursiveType.isContainerType());
    }

    @Test
    public void testToString() {
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(ResolvedRecursiveType.class, new TypeBindings());
        assertEquals("[recursive type; UNRESOLVED]", resolvedRecursiveType.toString());
    }

    @Test
    public void testEquals() {
        JavaType ref = new ResolvedRecursiveType(String.class, new TypeBindings());
        ResolvedRecursiveType resolvedRecursiveType1 = new ResolvedRecursiveType(ResolvedRecursiveType.class, new TypeBindings());
        resolvedRecursiveType1.setReference(ref);
        ResolvedRecursiveType resolvedRecursiveType2 = new ResolvedRecursiveType(ResolvedRecursiveType.class, new TypeBindings());
        resolvedRecursiveType2.setReference(ref);
        assertEquals(true, resolvedRecursiveType1.equals(resolvedRecursiveType2));
    }

    private class FakeResolvedRecursiveType extends ResolvedRecursiveType {
        private static final long serialVersionUID = 1L;

        public FakeResolvedRecursiveType(Class<?> erasedType, TypeBindings bindings) {
            super(erasedType, bindings);
        }
    }
}