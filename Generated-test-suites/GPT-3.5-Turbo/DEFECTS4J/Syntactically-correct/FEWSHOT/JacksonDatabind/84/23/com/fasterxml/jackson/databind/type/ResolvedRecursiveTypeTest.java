package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class ResolvedRecursiveTypeTest {

    @Test
    public void testConstructor() {
        TypeBindings bindings = new TypeBindings();
        ResolvedRecursiveType type = new ResolvedRecursiveType(String.class, bindings);

        assertEquals(String.class, type.getRawClass());
        assertEquals(bindings, type.getTypeBindings());
        assertNull(type.getContentType());
        assertNull(type.getKeyType());
        assertFalse(type.isFinal());
        assertFalse(type.isContainerType());
    }

    @Test
    public void testSetReference() {
        ResolvedRecursiveType type = new ResolvedRecursiveType(Integer.class, new TypeBindings());
        JavaType ref = new JavaType(Integer.class);

        type.setReference(ref);
        assertSame(ref, type.getSelfReferencedType());
    }

    @Test(expected = IllegalStateException.class)
    public void testSetReferenceTwice() {
        ResolvedRecursiveType type = new ResolvedRecursiveType(Float.class, new TypeBindings());
        JavaType ref1 = new JavaType(Float.class);
        JavaType ref2 = new JavaType(Double.class);

        type.setReference(ref1);
        type.setReference(ref2);
    }

    @Test
    public void testGetGenericSignature() {
        ResolvedRecursiveType type = new ResolvedRecursiveType(Boolean.class, new TypeBindings());
        StringBuilder sb = new StringBuilder();

        JavaType ref = new JavaType(Boolean.class);
        ref.setGenericSignature("Ljava/lang/Boolean;");
        type.setReference(ref);

        StringBuilder expected = new StringBuilder();
        expected.append("Ljava/lang/Boolean;");
        type.getGenericSignature(sb);

        assertEquals(expected.toString(), sb.toString());
    }

    @Test
    public void testGetErasedSignature() {
        ResolvedRecursiveType type = new ResolvedRecursiveType(Double.class, new TypeBindings());
        StringBuilder sb = new StringBuilder();

        JavaType ref = new JavaType(Double.class);
        ref.setErasedSignature("D");
        type.setReference(ref);

        StringBuilder expected = new StringBuilder();
        expected.append("D");
        type.getErasedSignature(sb);

        assertEquals(expected.toString(), sb.toString());
    }

    @Test
    public void testWithContentType() {
        ResolvedRecursiveType type = new ResolvedRecursiveType(Long.class, new TypeBindings());
        JavaType contentType = new JavaType(Long.class);

        assertSame(type, type.withContentType(contentType));
    }

    @Test
    public void testWithTypeHandler() {
        ResolvedRecursiveType type = new ResolvedRecursiveType(Object.class, new TypeBindings());
        Object handler = new Object();

        assertSame(type, type.withTypeHandler(handler));
    }

    @Test
    public void testWithContentTypeHandler() {
        ResolvedRecursiveType type = new ResolvedRecursiveType(Object.class, new TypeBindings());
        Object handler = new Object();

        assertSame(type, type.withContentTypeHandler(handler));
    }

    @Test
    public void testWithValueHandler() {
        ResolvedRecursiveType type = new ResolvedRecursiveType(Object.class, new TypeBindings());
        Object handler = new Object();

        assertSame(type, type.withValueHandler(handler));
    }

    @Test
    public void testWithContentValueHandler() {
        ResolvedRecursiveType type = new ResolvedRecursiveType(Object.class, new TypeBindings());
        Object handler = new Object();

        assertSame(type, type.withContentValueHandler(handler));
    }

    @Test
    public void testWithStaticTyping() {
        ResolvedRecursiveType type = new ResolvedRecursiveType(Object.class, new TypeBindings());

        assertSame(type, type.withStaticTyping());
    }

    @Test
    public void testNarrow() {
        ResolvedRecursiveType type = new ResolvedRecursiveType(Byte.class, new TypeBindings());
        Class<?> subclass = Byte.class;

        assertSame(type, type._narrow(subclass));
    }

    @Test
    public void testRefine() {
        ResolvedRecursiveType type = new ResolvedRecursiveType(Character.class, new TypeBindings());
        JavaType superClass = new JavaType(Character.class);
        JavaType[] superInterfaces = new JavaType[0];

        assertNull(type.refine(Character.class, new TypeBindings(), superClass, superInterfaces));
    }

    @Test
    public void testToString() {
        ResolvedRecursiveType type = new ResolvedRecursiveType(Byte.class, new TypeBindings());
        
        StringBuilder sb = new StringBuilder();
        sb.append("[recursive type; UNRESOLVED]");
        
        assertEquals(sb.toString(), type.toString());
        
        JavaType ref = new JavaType(Byte.class);
        type.setReference(ref);

        sb = new StringBuilder();
        sb.append("[recursive type; java.lang.Byte]");
        
        assertEquals(sb.toString(), type.toString());
    }

    @Test
    public void testEquals() {
        ResolvedRecursiveType type1 = new ResolvedRecursiveType(Integer.class, new TypeBindings());
        ResolvedRecursiveType type2 = new ResolvedRecursiveType(Integer.class, new TypeBindings());
        ResolvedRecursiveType type3 = new ResolvedRecursiveType(Double.class, new TypeBindings());

        JavaType ref = new JavaType(Integer.class);
        type1.setReference(ref);
        type2.setReference(ref);

        assertTrue(type1.equals(type2));
        assertTrue(type2.equals(type1));
        assertFalse(type1.equals(type3));
        assertFalse(type1.equals(null));
    }
}