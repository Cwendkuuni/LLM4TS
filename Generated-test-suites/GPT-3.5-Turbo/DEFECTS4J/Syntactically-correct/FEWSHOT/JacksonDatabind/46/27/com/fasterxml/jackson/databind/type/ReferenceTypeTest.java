package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class ReferenceTypeTest {

    @Test
    public void testConstruct() {
        Class<?> cls = String.class;
        JavaType refType = new SimpleType(Object.class);
        Object valueHandler = new Object();
        Object typeHandler = new Object();
        ReferenceType referenceType = ReferenceType.construct(cls, refType, valueHandler, typeHandler);
        
        assertNotNull(referenceType);
        assertEquals(cls, referenceType.getClass());
        assertEquals(refType, referenceType.getReferencedType());
        assertEquals(valueHandler, referenceType.getValueHandler());
        assertEquals(typeHandler, referenceType.getTypeHandler());
        assertTrue(referenceType.isReferenceType());
    }

    @Test
    public void testWithTypeHandler() {
        ReferenceType referenceType = new ReferenceType(String.class, new SimpleType(Object.class), new Object(), null, true);
        Object newTypeHandler = new Object();
        ReferenceType updatedType = referenceType.withTypeHandler(newTypeHandler);
        
        assertNotNull(updatedType);
        assertEquals(referenceType.getClass(), updatedType.getClass());
        assertEquals(referenceType.getReferencedType(), updatedType.getReferencedType());
        assertEquals(referenceType.getValueHandler(), updatedType.getValueHandler());
        assertEquals(newTypeHandler, updatedType.getTypeHandler());
        assertEquals(referenceType.isReferenceType(), updatedType.isReferenceType());
    }

    @Test
    public void testWithContentTypeHandler() {
        ReferenceType referenceType = new ReferenceType(String.class, new SimpleType(Object.class), new Object(), null, true);
        Object newContentHandler = new Object();
        ReferenceType updatedType = referenceType.withContentTypeHandler(newContentHandler);
        
        assertNotNull(updatedType);
        assertEquals(referenceType.getClass(), updatedType.getClass());
        assertEquals(referenceType.getReferencedType().withTypeHandler(newContentHandler), updatedType.getReferencedType());
        assertEquals(referenceType.getValueHandler(), updatedType.getValueHandler());
        assertEquals(referenceType.getTypeHandler(), updatedType.getTypeHandler());
        assertEquals(referenceType.isReferenceType(), updatedType.isReferenceType());
    }

    @Test
    public void testWithValueHandler() {
        ReferenceType referenceType = new ReferenceType(String.class, new SimpleType(Object.class), new Object(), null, true);
        Object newValueHandler = new Object();
        ReferenceType updatedType = referenceType.withValueHandler(newValueHandler);
        
        assertNotNull(updatedType);
        assertEquals(referenceType.getClass(), updatedType.getClass());
        assertEquals(referenceType.getReferencedType(), updatedType.getReferencedType());
        assertEquals(newValueHandler, updatedType.getValueHandler());
        assertEquals(referenceType.getTypeHandler(), updatedType.getTypeHandler());
        assertEquals(referenceType.isReferenceType(), updatedType.isReferenceType());
    }

    @Test
    public void testWithContentValueHandler() {
        ReferenceType referenceType = new ReferenceType(String.class, new SimpleType(Object.class), new Object(), null, true);
        Object newContentValueHandler = new Object();
        ReferenceType updatedType = referenceType.withContentValueHandler(newContentValueHandler);
        
        assertNotNull(updatedType);
        assertEquals(referenceType.getClass(), updatedType.getClass());
        assertEquals(referenceType.getReferencedType().withValueHandler(newContentValueHandler), updatedType.getReferencedType());
        assertEquals(referenceType.getValueHandler(), updatedType.getValueHandler());
        assertEquals(referenceType.getTypeHandler(), updatedType.getTypeHandler());
        assertEquals(referenceType.isReferenceType(), updatedType.isReferenceType());
    }

    @Test
    public void testWithStaticTyping() {
        ReferenceType referenceType = new ReferenceType(String.class, new SimpleType(Object.class), new Object(), null, true);
        ReferenceType updatedType = referenceType.withStaticTyping();
        
        assertNotNull(updatedType);
        assertEquals(referenceType.getClass(), updatedType.getClass());
        assertEquals(referenceType.getReferencedType().withStaticTyping(), updatedType.getReferencedType());
        assertEquals(referenceType.getValueHandler(), updatedType.getValueHandler());
        assertEquals(referenceType.getTypeHandler(), updatedType.getTypeHandler());
        assertTrue(updatedType.isReferenceType());
    }

    @Test
    public void testBuildCanonicalName() {
        Class<?> cls = String.class;
        JavaType refType = new SimpleType(Object.class);
        ReferenceType referenceType = new ReferenceType(cls, refType, null, null, false);
        
        String canonicalName = referenceType.buildCanonicalName();
        
        assertEquals("java.lang.String<java.lang.Object>", canonicalName);
    }

    @Test
    public void testGetReferencedType() {
        Class<?> cls = String.class;
        JavaType refType = new SimpleType(Object.class);
        ReferenceType referenceType = new ReferenceType(cls, refType, null, null, false);
        
        JavaType referencedType = referenceType.getReferencedType();
        
        assertEquals(refType, referencedType);
    }

    @Test
    public void testIsReferenceType() {
        Class<?> cls = String.class;
        JavaType refType = new SimpleType(Object.class);
        ReferenceType referenceType = new ReferenceType(cls, refType, null, null, false);
        
        assertTrue(referenceType.isReferenceType());
    }

    @Test
    public void testContainedTypeCount() {
        ReferenceType referenceType = new ReferenceType(String.class, new SimpleType(Object.class), new Object(), null, true);
        
        int count = referenceType.containedTypeCount();
        
        assertEquals(1, count);
    }

    @Test
    public void testContainedType() {
        ReferenceType referenceType = new ReferenceType(String.class, new SimpleType(Object.class), new Object(), null, true);
        
        JavaType containedType = referenceType.containedType(0);
        
        assertEquals(referenceType.getReferencedType(), containedType);
    }

    @Test
    public void testContainedTypeName() {
        ReferenceType referenceType = new ReferenceType(String.class, new SimpleType(Object.class), new Object(), null, true);
        
        String typeName = referenceType.containedTypeName(0);
        
        assertEquals("T", typeName);
    }

    @Test
    public void testGetParameterSource() {
        ReferenceType referenceType = new ReferenceType(String.class, new SimpleType(Object.class), new Object(), null, true);
        
        Class<?> parameterSource = referenceType.getParameterSource();
        
        assertEquals(referenceType.getClass(), parameterSource);
    }

    @Test
    public void testGetErasedSignature() {
        ReferenceType referenceType = new ReferenceType(String.class, new SimpleType(Object.class), new Object(), null, true);
        StringBuilder signature = new StringBuilder();
        
        StringBuilder erasedSignature = referenceType.getErasedSignature(signature);
        
        assertEquals("java.lang.String", erasedSignature.toString());
    }

    @Test
    public void testGetGenericSignature() {
        ReferenceType referenceType = new ReferenceType(String.class, new SimpleType(Object.class), new Object(), null, true);
        StringBuilder signature = new StringBuilder();
        
        StringBuilder genericSignature = referenceType.getGenericSignature(signature);
        
        assertEquals("java.lang.String<java.lang.Object;>", genericSignature.toString());
    }

    @Test
    public void testToString() {
        Class<?> cls = String.class;
        JavaType refType = new SimpleType(Object.class);
        ReferenceType referenceType = new ReferenceType(cls, refType, null, null, false);
        
        String typeString = referenceType.toString();
        
        String expectedString = "[reference type, class java.lang.String<java.lang.Object>]";
        assertEquals(expectedString, typeString);
    }

    @Test
    public void testEqualsSameObject() {
        ReferenceType referenceType = new ReferenceType(String.class, new SimpleType(Object.class), new Object(), null, true);
        
        boolean result = referenceType.equals(referenceType);
        
        assertTrue(result);
    }

    @Test
    public void testEqualsNullObject() {
        ReferenceType referenceType = new ReferenceType(String.class, new SimpleType(Object.class), new Object(), null, true);
        
        boolean result = referenceType.equals(null);
        
        assertFalse(result);
    }

    @Test
    public void testEqualsDifferentType() {
        ReferenceType referenceType = new ReferenceType(String.class, new SimpleType(Object.class), new Object(), null, true);
        SimpleType simpleType = new SimpleType(Object.class);
        
        boolean result = referenceType.equals(simpleType);
        
        assertFalse(result);
    }

    @Test
    public void testEqualsDifferentClass() {
        ReferenceType referenceType = new ReferenceType(String.class, new SimpleType(Object.class), new Object(), null, true);
        ReferenceType differentType = new ReferenceType(Integer.class, new SimpleType(Object.class), new Object(), null, true);
        
        boolean result = referenceType.equals(differentType);
        
        assertFalse(result);
    }

    @Test
    public void testEqualsDifferentReferencedType() {
        ReferenceType referenceType = new ReferenceType(String.class, new SimpleType(Object.class), new Object(), null, true);
        ReferenceType differentType = new ReferenceType(String.class, new SimpleType(Integer.class), new Object(), null, true);
        
        boolean result = referenceType.equals(differentType);
        
        assertFalse(result);
    }

    @Test
    public void testEqualsSameValues() {
        ReferenceType referenceType = new ReferenceType(String.class, new SimpleType(Object.class), new Object(), null, true);
        ReferenceType sameType = new ReferenceType(String.class, new SimpleType(Object.class), new Object(), null, true);
        
        boolean result = referenceType.equals(sameType);
        
        assertTrue(result);
    }
}