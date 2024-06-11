package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class ResolvedRecursiveTypeTest {

    @Test
    public void testSetReference() {
        ResolvedRecursiveType type = new ResolvedRecursiveType(Object.class, null);
        JavaType referencedType = new SimpleType(String.class);
        type.setReference(referencedType);
        assertNotNull(type.getSelfReferencedType());
        assertEquals(referencedType, type.getSelfReferencedType());
    }

    @Test(expected = IllegalStateException.class)
    public void testSetReferenceTwice() {
        ResolvedRecursiveType type = new ResolvedRecursiveType(Object.class, null);
        JavaType referencedType = new SimpleType(String.class);
        type.setReference(referencedType);
        type.setReference(new SimpleType(Integer.class));
    }

    @Test
    public void testGetSelfReferencedType() {
        ResolvedRecursiveType type = new ResolvedRecursiveType(Object.class, null);
        JavaType referencedType = new SimpleType(String.class);
        type.setReference(referencedType);
        assertNotNull(type.getSelfReferencedType());
        assertEquals(referencedType, type.getSelfReferencedType());
    }

    @Test
    public void testGetSelfReferencedTypeNull() {
        ResolvedRecursiveType type = new ResolvedRecursiveType(Object.class, null);
        assertNull(type.getSelfReferencedType());
    }

    @Test
    public void testGetGenericSignature() {
        JavaType javaType = new SimpleType(List.class);
        ResolvedRecursiveType recursiveType = new ResolvedRecursiveType(javaType.getRawClass(), null);
        javaType.getGenericSignature(new StringBuilder());
        recursiveType.getGenericSignature(new StringBuilder());
    }

    @Test
    public void testGetErasedSignature() {
        JavaType javaType = new SimpleType(List.class);
        ResolvedRecursiveType recursiveType = new ResolvedRecursiveType(javaType.getRawClass(), null);
        javaType.getErasedSignature(new StringBuilder());
        recursiveType.getErasedSignature(new StringBuilder());
    }

    @Test
    public void testWithContentType() {
        JavaType javaType = new SimpleType(List.class);
        ResolvedRecursiveType recursiveType = new ResolvedRecursiveType(javaType.getRawClass(), null);
        JavaType contentType = new SimpleType(String.class);
        recursiveType.withContentType(contentType);
    }

    @Test
    public void testWithTypeHandler() {
        JavaType javaType = new SimpleType(List.class);
        ResolvedRecursiveType recursiveType = new ResolvedRecursiveType(javaType.getRawClass(), null);
        Object handler = new Object();
        recursiveType.withTypeHandler(handler);
    }

    @Test
    public void testWithContentTypeHandler() {
        JavaType javaType = new SimpleType(List.class);
        ResolvedRecursiveType recursiveType = new ResolvedRecursiveType(javaType.getRawClass(), null);
        Object handler = new Object();
        recursiveType.withContentTypeHandler(handler);
    }

    @Test
    public void testWithValueHandler() {
        JavaType javaType = new SimpleType(List.class);
        ResolvedRecursiveType recursiveType = new ResolvedRecursiveType(javaType.getRawClass(), null);
        Object handler = new Object();
        recursiveType.withValueHandler(handler);
    }

    @Test
    public void testWithContentValueHandler() {
        JavaType javaType = new SimpleType(List.class);
        ResolvedRecursiveType recursiveType = new ResolvedRecursiveType(javaType.getRawClass(), null);
        Object handler = new Object();
        recursiveType.withContentValueHandler(handler);
    }

    @Test
    public void testWithStaticTyping() {
        JavaType javaType = new SimpleType(List.class);
        ResolvedRecursiveType recursiveType = new ResolvedRecursiveType(javaType.getRawClass(), null);
        recursiveType.withStaticTyping();
    }

    @Test
    public void testNarrow() {
        JavaType javaType = new SimpleType(List.class);
        ResolvedRecursiveType recursiveType = new ResolvedRecursiveType(javaType.getRawClass(), null);
        Class<?> subclass = ArrayList.class;
        recursiveType._narrow(subclass);
    }

    @Test(expected = Deprecated.class)
    public void testDeprecatedNarrow() {
        JavaType javaType = new SimpleType(List.class);
        ResolvedRecursiveType recursiveType = new ResolvedRecursiveType(javaType.getRawClass(), null);
        Class<?> subclass = ArrayList.class;
        recursiveType._narrow(subclass);
    }

    @Test
    public void testRefine() {
        JavaType javaType = new SimpleType(List.class);
        ResolvedRecursiveType recursiveType = new ResolvedRecursiveType(javaType.getRawClass(), null);
        Class<?> rawType = Map.class;
        TypeBindings bindings = new TypeBindings();
        JavaType superClass = new SimpleType(HashTable.class);
        JavaType[] superInterfaces = new JavaType[0];
        recursiveType.refine(rawType, bindings, superClass, superInterfaces);
    }

    @Test
    public void testIsContainerType() {
        JavaType javaType = new SimpleType(List.class);
        ResolvedRecursiveType recursiveType = new ResolvedRecursiveType(javaType.getRawClass(), null);
        assertFalse(recursiveType.isContainerType());
    }

    @Test
    public void testToString() {
        JavaType javaType = new SimpleType(List.class);
        ResolvedRecursiveType recursiveType = new ResolvedRecursiveType(javaType.getRawClass(), null);
        assertEquals("[recursive type; UNRESOLVED]", recursiveType.toString());
        JavaType referencedType = new SimpleType(String.class);
        recursiveType.setReference(referencedType);
        assertEquals("[recursive type; " + referencedType.getRawClass().getName() + "]", recursiveType.toString());
    }

    @Test
    public void testEquals() {
        JavaType javaType1 = new SimpleType(List.class);
        JavaType javaType2 = new SimpleType(List.class);
        ResolvedRecursiveType recursiveType1 = new ResolvedRecursiveType(javaType1.getRawClass(), null);
        ResolvedRecursiveType recursiveType2 = new ResolvedRecursiveType(javaType2.getRawClass(), null);
        assertFalse(recursiveType1.equals(recursiveType2));

        JavaType referencedType1 = new SimpleType(String.class);
        JavaType referencedType2 = new SimpleType(String.class);
        JavaType selfReferencedType1 = new ResolvedRecursiveType(referencedType1.getRawClass(), null);
        JavaType selfReferencedType2 = new ResolvedRecursiveType(referencedType2.getRawClass(), null);
        selfReferencedType1.setReference(referencedType1);
        selfReferencedType2.setReference(referencedType2);

        ResolvedRecursiveType recursiveType1WithReference = new ResolvedRecursiveType(javaType1.getRawClass(), null);
        recursiveType1WithReference.setReference(selfReferencedType1);

        ResolvedRecursiveType recursiveType2WithReference = new ResolvedRecursiveType(javaType2.getRawClass(), null);
        recursiveType2WithReference.setReference(selfReferencedType2);

        assertTrue(recursiveType1WithReference.equals(recursiveType2WithReference));
    }
}