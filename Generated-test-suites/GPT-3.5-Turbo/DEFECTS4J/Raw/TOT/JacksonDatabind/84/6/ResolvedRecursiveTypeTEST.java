package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.databind.JavaType;

public class ResolvedRecursiveTypeTest {

    private ResolvedRecursiveType recursiveType;
    private JavaType referencedType;

    @Before
    public void setUp() {
        referencedType = new JavaType();
        recursiveType = new ResolvedRecursiveType(ResolvedRecursiveType.class, new TypeBindings());
    }

    @Test
    public void testSetReference_ValidReference_Success() {
        recursiveType.setReference(referencedType);
        assertEquals(referencedType, recursiveType.getSelfReferencedType());
    }

    @Test(expected = IllegalStateException.class)
    public void testSetReference_AlreadySet_ExceptionThrown() {
        recursiveType.setReference(referencedType);
        recursiveType.setReference(new JavaType());
    }

    @Test
    public void testGetGenericSignature_ValidReference_Success() {
        recursiveType.setReference(referencedType);
        StringBuilder sb = new StringBuilder();
        assertEquals(referencedType.getGenericSignature(sb).toString(), recursiveType.getGenericSignature(sb).toString());
    }

    @Test
    public void testGetErasedSignature_ValidReference_Success() {
        recursiveType.setReference(referencedType);
        StringBuilder sb = new StringBuilder();
        assertEquals(referencedType.getErasedSignature(sb).toString(), recursiveType.getErasedSignature(sb).toString());
    }

    @Test
    public void testWithContentType_ContentTypesNotChanged_ReturnsThis() {
        JavaType contentType = new JavaType();
        JavaType result = recursiveType.withContentType(contentType);
        assertEquals(recursiveType, result);
    }

    @Test
    public void testWithTypeHandler_TypeHandlerNotChanged_ReturnsThis() {
        Object handler = new Object();
        JavaType result = recursiveType.withTypeHandler(handler);
        assertEquals(recursiveType, result);
    }

    @Test
    public void testWithContentTypeHandler_ContentTypeHandlerNotChanged_ReturnsThis() {
        Object handler = new Object();
        JavaType result = recursiveType.withContentTypeHandler(handler);
        assertEquals(recursiveType, result);
    }

    @Test
    public void testWithValueHandler_ValueHandlerNotChanged_ReturnsThis() {
        Object handler = new Object();
        JavaType result = recursiveType.withValueHandler(handler);
        assertEquals(recursiveType, result);
    }

    @Test
    public void testWithContentValueHandler_ContentValueHandlerNotChanged_ReturnsThis() {
        Object handler = new Object();
        JavaType result = recursiveType.withContentValueHandler(handler);
        assertEquals(recursiveType, result);
    }

    @Test
    public void testWithStaticTyping_ReturnsThis() {
        JavaType result = recursiveType.withStaticTyping();
        assertEquals(recursiveType, result);
    }

    @Test
    public void testNarrow_ReturnsThis() {
        Class<?> subclass = ResolvedRecursiveType.class;
        JavaType result = recursiveType._narrow(subclass);
        assertEquals(recursiveType, result);
    }

    @Test
    public void testRefine_ReturnsNull() {
        Class<?> rawType = ResolvedRecursiveType.class;
        JavaType superClass = new JavaType();
        JavaType[] superInterfaces = new JavaType[0];
        JavaType result = recursiveType.refine(rawType, new TypeBindings(), superClass, superInterfaces);
        assertNull(result);
    }

    @Test
    public void testIsContainerType_ReturnsFalse() {
        assertFalse(recursiveType.isContainerType());
    }

    @Test
    public void testToString_UnresolvedReference_ReturnsString() {
        String expectedResult = "[recursive type; UNRESOLVED]";
        String result = recursiveType.toString();
        assertEquals(expectedResult, result);
    }

    @Test
    public void testToString_ResolvedReference_ReturnsString() {
        recursiveType.setReference(referencedType);
        String expectedResult = "[recursive type; " + referencedType.getRawClass().getName() + "]";
        String result = recursiveType.toString();
        assertEquals(expectedResult, result);
    }

    @Test
    public void testEquals_NullObject_ReturnsFalse() {
        assertFalse(recursiveType.equals(null));
    }

    @Test
    public void testEquals_SameObject_ReturnsTrue() {
        assertTrue(recursiveType.equals(recursiveType));
    }

    @Test
    public void testEquals_DifferentClass_ReturnsFalse() {
        assertFalse(recursiveType.equals(new Object()));
    }

    @Test
    public void testEquals_SameReferenceType_ReturnsTrue() {
        recursiveType.setReference(referencedType);
        ResolvedRecursiveType other = new ResolvedRecursiveType(ResolvedRecursiveType.class, new TypeBindings());
        other.setReference(referencedType);
        assertTrue(recursiveType.equals(other));
    }

    @Test
    public void testEquals_DifferentReferenceType_ReturnsFalse() {
        recursiveType.setReference(new JavaType());
        ResolvedRecursiveType other = new ResolvedRecursiveType(ResolvedRecursiveType.class, new TypeBindings());
        other.setReference(referencedType);
        assertFalse(recursiveType.equals(other));
    }

    @Test
    public void testHashCode_EqualObjects_ReturnsSameHashCode() {
        recursiveType.setReference(referencedType);
        ResolvedRecursiveType other = new ResolvedRecursiveType(ResolvedRecursiveType.class, new TypeBindings());
        other.setReference(referencedType);
        assertEquals(recursiveType.hashCode(), other.hashCode());
    }

    @Test
    public void testHashCode_DifferentObjects_ReturnsDifferentHashCode() {
        recursiveType.setReference(new JavaType());
        ResolvedRecursiveType other = new ResolvedRecursiveType(ResolvedRecursiveType.class, new TypeBindings());
        other.setReference(referencedType);
        assertNotEquals(recursiveType.hashCode(), other.hashCode());
    }
}