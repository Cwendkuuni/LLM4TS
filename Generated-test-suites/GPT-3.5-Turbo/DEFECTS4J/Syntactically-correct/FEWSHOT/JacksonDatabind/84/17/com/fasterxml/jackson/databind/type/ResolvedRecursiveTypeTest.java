package com.fasterxml.jackson.databind.type;

import com.fasterxml.jackson.databind.JavaType;
import org.junit.Test;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

public class ResolvedRecursiveTypeTest {

    @Test
    public void testConstructor() {
        Class<?> erasedType = String.class;
        TypeBindings bindings = new TypeBindings();
        
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(erasedType, bindings);
        
        assertSame(erasedType, resolvedRecursiveType.getRawClass());
        assertSame(bindings, resolvedRecursiveType.getBindings());
        assertNull(resolvedRecursiveType.getKeyType());
        assertNull(resolvedRecursiveType.getContentType());
        assertTrue(resolvedRecursiveType.getTypeHandler() instanceof TypeBase.TypeHandler);
        assertNull(resolvedRecursiveType.getValueHandler());
        assertNull(resolvedRecursiveType.getContentValueHandler());
        assertTrue(resolvedRecursiveType.isContainerType());
    }

    @Test(expected = IllegalStateException.class)
    public void testSetReference() {
        JavaType ref = new JavaType();
        
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(String.class, new TypeBindings());
        resolvedRecursiveType.setReference(ref);
        
        assertSame(ref, resolvedRecursiveType.getSelfReferencedType());
        
        resolvedRecursiveType.setReference(new JavaType());
    }

    @Test
    public void testGetGenericSignature() {
        StringBuilder sb = new StringBuilder();
        JavaType ref = new JavaType();
        
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(String.class, new TypeBindings());
        resolvedRecursiveType.setReference(ref);
        
        assertSame(ref.getGenericSignature(sb), resolvedRecursiveType.getGenericSignature(sb));
    }

    @Test
    public void testGetErasedSignature() {
        StringBuilder sb = new StringBuilder();
        JavaType ref = new JavaType();
        
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(String.class, new TypeBindings());
        resolvedRecursiveType.setReference(ref);
        
        assertSame(ref.getErasedSignature(sb), resolvedRecursiveType.getErasedSignature(sb));
    }

    @Test
    public void testWithContentType() {
        JavaType contentType = new JavaType();
        
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(String.class, new TypeBindings());
        
        assertSame(resolvedRecursiveType, resolvedRecursiveType.withContentType(contentType));
    }

    @Test
    public void testWithTypeHandler() {
        Object h = new Object();
        
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(String.class, new TypeBindings());
        
        assertSame(resolvedRecursiveType, resolvedRecursiveType.withTypeHandler(h));
    }

    @Test
    public void testWithContentTypeHandler() {
        Object h = new Object();
        
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(String.class, new TypeBindings());
        
        assertSame(resolvedRecursiveType, resolvedRecursiveType.withContentTypeHandler(h));
    }

    @Test
    public void testWithValueHandler() {
        Object h = new Object();
        
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(String.class, new TypeBindings());
        
        assertSame(resolvedRecursiveType, resolvedRecursiveType.withValueHandler(h));
    }

    @Test
    public void testWithContentValueHandler() {
        Object h = new Object();
        
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(String.class, new TypeBindings());
        
        assertSame(resolvedRecursiveType, resolvedRecursiveType.withContentValueHandler(h));
    }

    @Test
    public void testWithStaticTyping() {
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(String.class, new TypeBindings());
        
        assertSame(resolvedRecursiveType, resolvedRecursiveType.withStaticTyping());
    }

    @Test
    public void testNarrow() {
        Class<?> subclass = String.class;
        
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(String.class, new TypeBindings());
        
        assertSame(resolvedRecursiveType, resolvedRecursiveType._narrow(subclass));
    }

    @Test
    public void testRefine() {
        Class<?> rawType = String.class;
        TypeBindings bindings = new TypeBindings();
        JavaType superClass = new JavaType();
        JavaType[] superInterfaces = new JavaType[0];
        
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(String.class, new TypeBindings());
        
        assertNull(resolvedRecursiveType.refine(rawType, bindings, superClass, superInterfaces));
    }

    @Test
    public void testToString() {
        JavaType ref = new JavaType();
        
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(String.class, new TypeBindings());
        resolvedRecursiveType.setReference(ref);
        
        String expected = "[recursive type; " + String.class.getName() + "]";
        assertTrue(expected.equals(resolvedRecursiveType.toString()));
    }

    @Test
    public void testEquals() {
        JavaType ref1 = new JavaType();
        JavaType ref2 = new JavaType();
        
        ResolvedRecursiveType resolvedRecursiveType1 = new ResolvedRecursiveType(String.class, new TypeBindings());
        resolvedRecursiveType1.setReference(ref1);
        
        ResolvedRecursiveType resolvedRecursiveType2 = new ResolvedRecursiveType(String.class, new TypeBindings());
        resolvedRecursiveType2.setReference(ref1);
        
        ResolvedRecursiveType resolvedRecursiveType3 = new ResolvedRecursiveType(String.class, new TypeBindings());
        resolvedRecursiveType3.setReference(ref2);
        
        ResolvedRecursiveType resolvedRecursiveType4 = new ResolvedRecursiveType(Integer.class, new TypeBindings());
        resolvedRecursiveType4.setReference(ref1);
        
        assertTrue(resolvedRecursiveType1.equals(resolvedRecursiveType1));
        assertTrue(resolvedRecursiveType1.equals(resolvedRecursiveType2));
        assertTrue(resolvedRecursiveType2.equals(resolvedRecursiveType1));
        assertTrue(!resolvedRecursiveType1.equals(resolvedRecursiveType3));
        assertTrue(!resolvedRecursiveType1.equals(resolvedRecursiveType4));
        assertTrue(!resolvedRecursiveType1.equals(null));
        assertTrue(!resolvedRecursiveType1.equals(new Object()));
    }
}