package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class ResolvedRecursiveTypeTest {
    @Test
    public void testConstructor() {
        ResolvedRecursiveType type = new ResolvedRecursiveType(String.class, new TypeBindings());
        assertNotNull(type);
        assertEquals(String.class, type.getRawClass());
        assertNotNull(type.getBindings());
        assertNull(type.getSuperClass());
        assertNotNull(type.getInterfaces());
        assertEquals(0, type.getInterfaces().length);
        assertEquals(0, type.getBindings().size());
        assertFalse(type.isArrayType());
        assertFalse(type.isContainerType());
    }

    @Test
    public void testSetReference() {
        ResolvedRecursiveType type = new ResolvedRecursiveType(String.class, new TypeBindings());
        assertNull(type.getSelfReferencedType());

        JavaType ref = new SimpleType(String.class);
        type.setReference(ref);
        assertEquals(ref, type.getSelfReferencedType());
    }

    @Test(expected = IllegalStateException.class)
    public void testSetReferenceMultipleTimes() {
        ResolvedRecursiveType type = new ResolvedRecursiveType(String.class, new TypeBindings());
        assertNull(type.getSelfReferencedType());

        JavaType ref1 = new SimpleType(String.class);
        JavaType ref2 = new SimpleType(Integer.class);

        type.setReference(ref1);
        type.setReference(ref2);
    }

    @Test
    public void testGetGenericSignature() {
        ResolvedRecursiveType type = new ResolvedRecursiveType(String.class, new TypeBindings());

        StringBuilder sb = new StringBuilder();
        StringBuilder expected = new StringBuilder();
        JavaType ref = new SimpleType(String.class);
        ref.getGenericSignature(expected);

        type.setReference(ref);
        type.getGenericSignature(sb);

        assertEquals(expected.toString(), sb.toString());
    }

    @Test
    public void testGetErasedSignature() {
        ResolvedRecursiveType type = new ResolvedRecursiveType(String.class, new TypeBindings());

        StringBuilder sb = new StringBuilder();
        StringBuilder expected = new StringBuilder();
        JavaType ref = new SimpleType(String.class);
        ref.getErasedSignature(expected);

        type.setReference(ref);
        type.getErasedSignature(sb);

        assertEquals(expected.toString(), sb.toString());
    }

    @Test
    public void testWithContentType() {
        ResolvedRecursiveType type = new ResolvedRecursiveType(String.class, new TypeBindings());

        JavaType contentType = new SimpleType(String.class);
        JavaType result = type.withContentType(contentType);

        assertEquals(type, result);
    }

    @Test
    public void testWithTypeHandler() {
        ResolvedRecursiveType type = new ResolvedRecursiveType(String.class, new TypeBindings());

        Object handler = new Object();
        JavaType result = type.withTypeHandler(handler);

        assertEquals(type, result);
    }

    @Test
    public void testWithContentTypeHandler() {
        ResolvedRecursiveType type = new ResolvedRecursiveType(String.class, new TypeBindings());

        Object handler = new Object();
        JavaType result = type.withContentTypeHandler(handler);

        assertEquals(type, result);
    }

    @Test
    public void testWithValueHandler() {
        ResolvedRecursiveType type = new ResolvedRecursiveType(String.class, new TypeBindings());

        Object handler = new Object();
        JavaType result = type.withValueHandler(handler);

        assertEquals(type, result);
    }

    @Test
    public void testWithContentValueHandler() {
        ResolvedRecursiveType type = new ResolvedRecursiveType(String.class, new TypeBindings());

        Object handler = new Object();
        JavaType result = type.withContentValueHandler(handler);

        assertEquals(type, result);
    }
    
    @Test
    public void testWithStaticTyping() {
        ResolvedRecursiveType type = new ResolvedRecursiveType(String.class, new TypeBindings());

        JavaType result = type.withStaticTyping();

        assertEquals(type, result);
    }

    @Test
    public void testNarrow() {
        ResolvedRecursiveType type = new ResolvedRecursiveType(String.class, new TypeBindings());

        JavaType result = type._narrow(String.class);

        assertEquals(type, result);
    }
    
    @Test
    public void testRefine() {
        ResolvedRecursiveType type = new ResolvedRecursiveType(String.class, new TypeBindings());

        JavaType result = type.refine(String.class, new TypeBindings(), null, new JavaType[0]);

        assertNull(result);
    }

    @Test
    public void testIsContainerType() {
        ResolvedRecursiveType type = new ResolvedRecursiveType(String.class, new TypeBindings());

        assertFalse(type.isContainerType());
    }

    @Test
    public void testToString() {
        ResolvedRecursiveType type1 = new ResolvedRecursiveType(String.class, new TypeBindings());
        assertEquals("[recursive type; UNRESOLVED]", type1.toString());

        JavaType ref = new SimpleType(String.class);
        ResolvedRecursiveType type2 = new ResolvedRecursiveType(String.class, new TypeBindings());
        type2.setReference(ref);
        assertEquals("[recursive type; java.lang.String]", type2.toString());
    }

    @Test
    public void testEquals() {
        JavaType ref1 = new SimpleType(String.class);
        JavaType ref2 = new SimpleType(Integer.class);
        
        ResolvedRecursiveType type1 = new ResolvedRecursiveType(String.class, new TypeBindings());
        type1.setReference(ref1);

        ResolvedRecursiveType type2 = new ResolvedRecursiveType(String.class, new TypeBindings());
        type2.setReference(ref1);

        ResolvedRecursiveType type3 = new ResolvedRecursiveType(String.class, new TypeBindings());
        type3.setReference(ref2);

        assertTrue(type1.equals(type2));
        assertFalse(type1.equals(type3));
    }
}