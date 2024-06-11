package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class ReferenceTypeTest {

    @Test
    public void testConstruct() {
        ReferenceType referenceType = ReferenceType.construct(String.class, null, null, null, null);
        assertEquals(String.class, referenceType.getRawClass());
        assertNull(referenceType.getContentType());
        assertNull(referenceType.getReferencedType());
        assertFalse(referenceType.hasContentType());
        assertTrue(referenceType.isReferenceType());
        assertFalse(referenceType.isAnchorType());
    }
    
    @Test
    public void testUpgradeFrom() {
        JavaType baseType = SimpleType.constructUnsafe(String.class);
        JavaType refdType = SimpleType.constructUnsafe(Integer.class);
        ReferenceType referenceType = ReferenceType.upgradeFrom(baseType, refdType);
        assertEquals(baseType, referenceType.getAnchorType());
    }
    
    @Test
    public void testWithContentType() {
        JavaType contentType = SimpleType.constructUnsafe(Integer.class);
        ReferenceType referenceType = ReferenceType.construct(String.class, null, null, null, null);
        ReferenceType newReferenceType = referenceType.withContentType(contentType);
        assertEquals(referenceType.getRawClass(), newReferenceType.getRawClass());
        assertEquals(contentType, newReferenceType.getContentType());
        assertEquals(referenceType.getReferencedType(), newReferenceType.getReferencedType());
    }
    
    @Test
    public void testWithTypeHandler() {
        Object typeHandler = new Object();
        ReferenceType referenceType = ReferenceType.construct(String.class, null, null, null, null);
        ReferenceType newReferenceType = referenceType.withTypeHandler(typeHandler);
        assertEquals(referenceType.getRawClass(), newReferenceType.getRawClass());
        assertEquals(referenceType.getContentType(), newReferenceType.getContentType());
        assertEquals(typeHandler, newReferenceType.getTypeHandler());
    }
    
    @Test
    public void testWithContentTypeHandler() {
        Object typeHandler = new Object();
        ReferenceType referenceType = ReferenceType.construct(String.class, null, null, null, null);
        ReferenceType newReferenceType = referenceType.withContentTypeHandler(typeHandler);
        assertEquals(referenceType.getRawClass(), newReferenceType.getRawClass());
        assertEquals(referenceType.getContentType(), newReferenceType.getContentType());
        assertEquals(typeHandler, newReferenceType.getContentType().getTypeHandler());
    }
    
    @Test
    public void testWithValueHandler() {
        Object valueHandler = new Object();
        ReferenceType referenceType = ReferenceType.construct(String.class, null, null, null, null);
        ReferenceType newReferenceType = referenceType.withValueHandler(valueHandler);
        assertEquals(referenceType.getRawClass(), newReferenceType.getRawClass());
        assertEquals(referenceType.getContentType(), newReferenceType.getContentType());
        assertEquals(valueHandler, newReferenceType.getValueHandler());
    }
    
    @Test
    public void testWithContentValueHandler() {
        Object valueHandler = new Object();
        ReferenceType referenceType = ReferenceType.construct(String.class, null, null, null, null);
        ReferenceType newReferenceType = referenceType.withContentValueHandler(valueHandler);
        assertEquals(referenceType.getRawClass(), newReferenceType.getRawClass());
        assertEquals(referenceType.getContentType(), newReferenceType.getContentType());
        assertEquals(valueHandler, newReferenceType.getContentType().getValueHandler());
    }
    
    @Test
    public void testWithStaticTyping() {
        ReferenceType referenceType = ReferenceType.construct(String.class, null, null, null, null);
        ReferenceType newReferenceType = referenceType.withStaticTyping();
        assertEquals(referenceType.getRawClass(), newReferenceType.getRawClass());
        assertEquals(referenceType.getContentType(), newReferenceType.getContentType());
        assertTrue(newReferenceType.isStaticTyping());
    }
    
    @Test
    public void testGetErasedSignature() {
        ReferenceType referenceType = ReferenceType.construct(String.class, null, null, null, null);
        StringBuilder sb = referenceType.getErasedSignature(new StringBuilder());
        assertEquals("java.lang.StringReferenceType", sb.toString());
    }
    
    @Test
    public void testGetGenericSignature() {
        ReferenceType referenceType = ReferenceType.construct(String.class, null, null, null, null);
        StringBuilder sb = referenceType.getGenericSignature(new StringBuilder());
        assertEquals("java.lang.StringReferenceType<java.lang.String>;", sb.toString());
    }
    
    @Test
    public void testGetAnchorType() {
        ReferenceType referenceType = ReferenceType.construct(String.class, null, null, null, null);
        assertEquals(referenceType, referenceType.getAnchorType());
    }
    
    @Test
    public void testIsAnchorType() {
        ReferenceType referenceType = ReferenceType.construct(String.class, null, null, null, null);
        assertTrue(referenceType.isAnchorType());
    }
    
    @Test
    public void testToString() {
        ReferenceType referenceType = ReferenceType.construct(String.class, null, null, null, null);
        assertEquals("[reference type, class java.lang.StringReferenceType<String>]", referenceType.toString());
    }
    
    @Test
    public void testEquals() {
        ReferenceType referenceType1 = ReferenceType.construct(String.class, null, null, null, null);
        ReferenceType referenceType2 = ReferenceType.construct(String.class, null, null, null, null);
        assertTrue(referenceType1.equals(referenceType2));
    }
}