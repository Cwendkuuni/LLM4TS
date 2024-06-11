package com.fasterxml.jackson.databind.type;

import com.fasterxml.jackson.databind.JavaType;
import org.junit.Test;
import static org.junit.Assert.*;

public class ReferenceTypeTest {
    @Test
    public void testConstruct() {
        ReferenceType referenceType = ReferenceType.construct(String.class, JavaType.String, null, null);
        
        assertEquals(String.class, referenceType._class);
        assertEquals(JavaType.String, referenceType._referencedType);
        assertNull(referenceType._valueHandler);
        assertNull(referenceType._typeHandler);
        assertFalse(referenceType._asStatic);
    }

    @Test
    public void testWithTypeHandler() {
        Object handler = new Object();
        
        ReferenceType referenceType = ReferenceType.construct(String.class, JavaType.String, null, null);
        ReferenceType newReferenceType = referenceType.withTypeHandler(handler);
        
        assertSame(referenceType, newReferenceType); // should return the same instance if the handler is the same
        assertEquals(handler, newReferenceType._typeHandler);
    }

    @Test
    public void testWithContentTypeHandler() {
        Object handler = new Object();
        
        ReferenceType referenceType = ReferenceType.construct(String.class, JavaType.String, null, null);
        ReferenceType newReferenceType = referenceType.withContentTypeHandler(handler);
        
        assertSame(referenceType, newReferenceType); // should return the same instance if the handler is the same
        assertEquals(handler, newReferenceType._referencedType.getTypeHandler());
    }

    @Test
    public void testWithValueHandler() {
        Object handler = new Object();
        
        ReferenceType referenceType = ReferenceType.construct(String.class, JavaType.String, null, null);
        ReferenceType newReferenceType = referenceType.withValueHandler(handler);
        
        assertSame(referenceType, newReferenceType); // should return the same instance if the handler is the same
        assertEquals(handler, newReferenceType._valueHandler);
    }

    @Test
    public void testWithContentValueHandler() {
        Object handler = new Object();
        
        ReferenceType referenceType = ReferenceType.construct(String.class, JavaType.String, null, null);
        ReferenceType newReferenceType = referenceType.withContentValueHandler(handler);
        
        assertSame(referenceType, newReferenceType); // should return the same instance if the handler is the same
        assertEquals(handler, newReferenceType._referencedType.getValueHandler());
    }

    @Test
    public void testWithStaticTyping() {
        ReferenceType referenceType = ReferenceType.construct(String.class, JavaType.String, null, null);
        ReferenceType newReferenceType = referenceType.withStaticTyping();
        
        assertSame(referenceType, newReferenceType); // should return the same instance if already static typing
        assertTrue(newReferenceType._asStatic);
    }

    @Test
    public void testBuildCanonicalName() {
        ReferenceType referenceType = ReferenceType.construct(String.class, JavaType.String, null, null);
        String canonicalName = referenceType.buildCanonicalName();
        
        assertEquals("java.lang.String", canonicalName);
    }

    @Test
    public void testNarrow() {
        ReferenceType referenceType = ReferenceType.construct(String.class, JavaType.String, null, null);
        ReferenceType newReferenceType = (ReferenceType) referenceType._narrow(CharSequence.class);
        
        assertEquals(CharSequence.class, newReferenceType._class);
        assertEquals(JavaType.String, newReferenceType._referencedType);
        assertNull(newReferenceType._valueHandler);
        assertNull(newReferenceType._typeHandler);
        assertFalse(newReferenceType._asStatic);
    }

    @Test
    public void testGetReferencedType() {
        ReferenceType referenceType = ReferenceType.construct(String.class, JavaType.String, null, null);
        JavaType referencedType = referenceType.getReferencedType();
        
        assertEquals(JavaType.String, referencedType);
    }

    @Test
    public void testIsReferenceType() {
        ReferenceType referenceType = ReferenceType.construct(String.class, JavaType.String, null, null);
        
        assertTrue(referenceType.isReferenceType());
    }

    @Test
    public void testContainedTypeCount() {
        ReferenceType referenceType = ReferenceType.construct(String.class, JavaType.String, null, null);
        
        assertEquals(1, referenceType.containedTypeCount());
    }

    @Test
    public void testContainedType() {
        ReferenceType referenceType = ReferenceType.construct(String.class, JavaType.String, null, null);
        JavaType containedType = referenceType.containedType(0);
        
        assertEquals(JavaType.String, containedType);
    }

    @Test
    public void testContainedTypeName() {
        ReferenceType referenceType = ReferenceType.construct(String.class, JavaType.String, null, null);
        String containedTypeName = referenceType.containedTypeName(0);
        
        assertEquals("T", containedTypeName);
    }

    @Test
    public void testGetParameterSource() {
        ReferenceType referenceType = ReferenceType.construct(String.class, JavaType.String, null, null);
        Class<?> parameterSource = referenceType.getParameterSource();
        
        assertEquals(String.class, parameterSource);
    }

    @Test
    public void testGetErasedSignature() {
        ReferenceType referenceType = ReferenceType.construct(String.class, JavaType.String, null, null);
        StringBuilder sb = referenceType.getErasedSignature(new StringBuilder());
        
        assertEquals("java.lang.String", sb.toString());
    }

    @Test
    public void testGetGenericSignature() {
        ReferenceType referenceType = ReferenceType.construct(String.class, JavaType.String, null, null);
        StringBuilder sb = referenceType.getGenericSignature(new StringBuilder());
        
        assertEquals("java.lang.String<java.lang.String;>", sb.toString());
    }

    @Test
    public void testToString() {
        ReferenceType referenceType = ReferenceType.construct(String.class, JavaType.String, null, null);
        String toString = referenceType.toString();
        
        assertEquals("[reference type, class java.lang.String<java.lang.String>]", toString);
    }

    @Test
    public void testEquals() {
        ReferenceType referenceType1 = ReferenceType.construct(String.class, JavaType.String, null, null);
        ReferenceType referenceType2 = ReferenceType.construct(String.class, JavaType.String, null, null);
        
        assertTrue(referenceType1.equals(referenceType1));
        assertFalse(referenceType1.equals(null));
        assertFalse(referenceType1.equals(new Object()));
        assertTrue(referenceType1.equals(referenceType2));
    }
}