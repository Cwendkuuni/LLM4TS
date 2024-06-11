package com.fasterxml.jackson.databind.type;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class ResolvedRecursiveTypeTest {

    private ResolvedRecursiveType resolvedRecursiveType;

    @Before
    public void setUp() {
        resolvedRecursiveType = new ResolvedRecursiveType(null, null);
    }

    @Test
    public void testSetReference_SingleCall_ShouldSetReference() {
        JavaType ref = new JavaType("ref");
        resolvedRecursiveType.setReference(ref);
        assertEquals(ref, resolvedRecursiveType.getSelfReferencedType());
    }

    @Test(expected = IllegalStateException.class)
    public void testSetReference_MultipleCalls_ShouldThrowException() {
        JavaType ref1 = new JavaType("ref1");
        JavaType ref2 = new JavaType("ref2");
        resolvedRecursiveType.setReference(ref1);
        resolvedRecursiveType.setReference(ref2);
    }

    @Test
    public void testGetGenericSignature_ShouldReturnReferencedTypeGenericSignature() {
        JavaType ref = new JavaType("ref");
        resolvedRecursiveType.setReference(ref);
        StringBuilder sb = new StringBuilder();
        StringBuilder expected = ref.getGenericSignature(sb);
        StringBuilder actual = resolvedRecursiveType.getGenericSignature(sb);
        assertEquals(expected, actual);
    }

    @Test
    public void testGetErasedSignature_ShouldReturnReferencedTypeErasedSignature() {
        JavaType ref = new JavaType("ref");
        resolvedRecursiveType.setReference(ref);
        StringBuilder sb = new StringBuilder();
        StringBuilder expected = ref.getErasedSignature(sb);
        StringBuilder actual = resolvedRecursiveType.getErasedSignature(sb);
        assertEquals(expected, actual);
    }

    @Test
    public void testWithContentType_ShouldReturnThis() {
        JavaType contentType = new JavaType("contentType");
        assertEquals(resolvedRecursiveType, resolvedRecursiveType.withContentType(contentType));
    }
    
    @Test
    public void testWithTypeHandler_ShouldReturnThis() {
        Object handler = new Object();
        assertEquals(resolvedRecursiveType, resolvedRecursiveType.withTypeHandler(handler));
    }

    @Test
    public void testWithContentTypeHandler_ShouldReturnThis() {
        Object handler = new Object();
        assertEquals(resolvedRecursiveType, resolvedRecursiveType.withContentTypeHandler(handler));
    }

    @Test
    public void testWithValueHandler_ShouldReturnThis() {
        Object handler = new Object();
        assertEquals(resolvedRecursiveType, resolvedRecursiveType.withValueHandler(handler));
    }

    @Test
    public void testWithContentValueHandler_ShouldReturnThis() {
        Object handler = new Object();
        assertEquals(resolvedRecursiveType, resolvedRecursiveType.withContentValueHandler(handler));
    }

    @Test
    public void testWithStaticTyping_ShouldReturnThis() {
        assertEquals(resolvedRecursiveType, resolvedRecursiveType.withStaticTyping());
    }

    @Test
    public void testNarrow_ShouldReturnThis() {
        Class<?> subclass = new Class();
        assertEquals(resolvedRecursiveType, resolvedRecursiveType._narrow(subclass));
    }

    @Test
    public void testRefine_ShouldReturnNull() {
        Class<?> rawType = new Class();
        TypeBindings bindings = new TypeBindings();
        JavaType superClass = new JavaType("superClass");
        JavaType[] superInterfaces = new JavaType[0];
        assertNull(resolvedRecursiveType.refine(rawType, bindings, superClass, superInterfaces));
    }

    @Test
    public void testIsContainerType_ShouldReturnFalse() {
        assertFalse(resolvedRecursiveType.isContainerType());
    }

    @Test
    public void testToString_ReferencedTypeIsNull_ShouldReturnUnresolved() {
        String expected = "[recursive type; UNRESOLVED]";
        String actual = resolvedRecursiveType.toString();
        assertEquals(expected, actual);
    }

    @Test
    public void testToString_ReferencedTypeIsNotNull_ShouldReturnTypeErasedClass() {
        JavaType ref = new JavaType("ref");
        resolvedRecursiveType.setReference(ref);
        String expected = "[recursive type; " + ref.getRawClass().getName() + "]";
        String actual = resolvedRecursiveType.toString();
        assertEquals(expected, actual);
    }

    @Test
    public void testEquals_SameObject_ShouldReturnTrue() {
        assertTrue(resolvedRecursiveType.equals(resolvedRecursiveType));
    }

    @Test
    public void testEquals_NullObject_ShouldReturnFalse() {
        assertFalse(resolvedRecursiveType.equals(null));
    }

    @Test
    public void testEquals_DifferentClass_ShouldReturnFalse() {
        Object obj = new Object();
        assertFalse(resolvedRecursiveType.equals(obj));
    }

    @Test
    public void testEquals_ReferencedTypeIsNull_ShouldReturnFalse() {
        ResolvedRecursiveType other = new ResolvedRecursiveType(null, null);
        assertFalse(resolvedRecursiveType.equals(other));
    }

    @Test
    public void testEquals_ReferencedTypeIsNotEqual_ShouldReturnFalse() {
        JavaType ref = new JavaType("ref");
        resolvedRecursiveType.setReference(ref);
        JavaType otherRef = new JavaType("otherRef");
        ResolvedRecursiveType other = new ResolvedRecursiveType(null, null);
        other.setReference(otherRef);
        assertFalse(resolvedRecursiveType.equals(other));
    }

    @Test
    public void testEquals_ReferencedTypeIsEqual_ShouldReturnTrue() {
        JavaType ref = new JavaType("ref");
        resolvedRecursiveType.setReference(ref);
        ResolvedRecursiveType other = new ResolvedRecursiveType(null, null);
        other.setReference(ref);
        assertTrue(resolvedRecursiveType.equals(other));
    }
}