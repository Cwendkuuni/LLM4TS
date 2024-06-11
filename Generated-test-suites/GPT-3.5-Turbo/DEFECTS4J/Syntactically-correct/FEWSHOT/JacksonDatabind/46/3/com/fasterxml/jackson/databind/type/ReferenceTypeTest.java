package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.fasterxml.jackson.databind.JavaType;

public class ReferenceTypeTest {

    @Test
    public void testConstruct() {
        JavaType referencedType = new SimpleType();
        ReferenceType referenceType = ReferenceType.construct(String.class, referencedType, null, null);
        
        assertEquals(String.class, referenceType._class);
        assertEquals(referencedType, referenceType._referencedType);
        assertNull(referenceType._valueHandler);
        assertNull(referenceType._typeHandler);
        assertFalse(referenceType._asStatic);
    }

    @Test
    public void testWithTypeHandler() {
        Object typeHandler = new Object();
        ReferenceType referenceType = new ReferenceType(String.class, null, null, null, false);
        ReferenceType newType = referenceType.withTypeHandler(typeHandler);
        
        assertEquals(referenceType._class, newType._class);
        assertEquals(referenceType._referencedType, newType._referencedType);
        assertEquals(referenceType._valueHandler, newType._valueHandler);
        assertEquals(typeHandler, newType._typeHandler);
        assertEquals(referenceType._asStatic, newType._asStatic);
    }

    @Test
    public void testWithContentTypeHandler() {
        Object contentTypeHandler = new Object();
        JavaType referencedType = new SimpleType();
        ReferenceType referenceType = new ReferenceType(String.class, referencedType, null, null, false);
        ReferenceType newType = referenceType.withContentTypeHandler(contentTypeHandler);
        
        assertEquals(referenceType._class, newType._class);
        assertEquals(referenceType._valueHandler, newType._valueHandler);
        assertEquals(referenceType._typeHandler, newType._typeHandler);
        assertEquals(referenceType._asStatic, newType._asStatic);
        assertNotEquals(referenceType._referencedType, newType._referencedType);
        assertEquals(contentTypeHandler, newType._referencedType.getTypeHandler());
    }
    
    @Test
    public void testWithValueHandler() {
        Object valueHandler = new Object();
        ReferenceType referenceType = new ReferenceType(String.class, null, null, null, false);
        ReferenceType newType = referenceType.withValueHandler(valueHandler);
        
        assertEquals(referenceType._class, newType._class);
        assertEquals(referenceType._referencedType, newType._referencedType);
        assertEquals(valueHandler, newType._valueHandler);
        assertEquals(referenceType._typeHandler, newType._typeHandler);
        assertEquals(referenceType._asStatic, newType._asStatic);
    }
    
    @Test
    public void testWithContentValueHandler() {
        Object contentValueHandler = new Object();
        JavaType referencedType = new SimpleType();
        ReferenceType referenceType = new ReferenceType(String.class, referencedType, null, null, false);
        ReferenceType newType = referenceType.withContentValueHandler(contentValueHandler);
        
        assertEquals(referenceType._class, newType._class);
        assertEquals(referenceType._valueHandler, newType._valueHandler);
        assertEquals(referenceType._typeHandler, newType._typeHandler);
        assertEquals(referenceType._asStatic, newType._asStatic);
        assertNotEquals(referenceType._referencedType, newType._referencedType);
        assertEquals(contentValueHandler, newType._referencedType.getValueHandler());
    }
    
    @Test
    public void testWithStaticTyping() {
        ReferenceType referenceType = new ReferenceType(String.class, null, null, null, false);
        ReferenceType newType = referenceType.withStaticTyping();
        
        assertEquals(referenceType._class, newType._class);
        assertEquals(referenceType._referencedType, newType._referencedType);
        assertEquals(referenceType._valueHandler, newType._valueHandler);
        assertEquals(referenceType._typeHandler, newType._typeHandler);
        assertTrue(newType._asStatic);
    }
    
    @Test
    public void testBuildCanonicalName() {
        JavaType referencedType = new SimpleType();
        ReferenceType referenceType = new ReferenceType(String.class, referencedType, null, null, false);
        String canonicalName = referenceType.buildCanonicalName();
        
        StringBuilder sb = new StringBuilder();
        sb.append(String.class.getName());
        sb.append('<');
        sb.append(referencedType.toCanonical());
        
        assertEquals(sb.toString(), canonicalName);
    }
    
    @Test
    public void testGetReferencedType() {
        JavaType referencedType = new SimpleType();
        ReferenceType referenceType = new ReferenceType(String.class, referencedType, null, null, false);
        JavaType referencedTypeFromReference = referenceType.getReferencedType();
        
        assertEquals(referencedType, referencedTypeFromReference);
    }
    
    @Test
    public void testIsReferenceType() {
        ReferenceType referenceType = new ReferenceType(String.class, null, null, null, false);
        assertTrue(referenceType.isReferenceType());
    }
    
    @Test
    public void testContainedTypeCount() {
        ReferenceType referenceType = new ReferenceType(String.class, null, null, null, false);
        int containedTypeCount = referenceType.containedTypeCount();
        
        assertEquals(1, containedTypeCount);
    }
    
    @Test
    public void testContainedType() {
        JavaType referencedType = new SimpleType();
        ReferenceType referenceType = new ReferenceType(String.class, referencedType, null, null, false);
        JavaType containedType = referenceType.containedType(0);
        
        assertEquals(referencedType, containedType);
    }
    
    @Test
    public void testContainedTypeName() {
        ReferenceType referenceType = new ReferenceType(String.class, null, null, null, false);
        String containedTypeName = referenceType.containedTypeName(0);
        
        assertEquals("T", containedTypeName);
    }
    
    @Test
    public void testGetParameterSource() {
        ReferenceType referenceType = new ReferenceType(String.class, null, null, null, false);
        Class<?> parameterSource = referenceType.getParameterSource();
        
        assertEquals(String.class, parameterSource);
    }
    
    @Test
    public void testGetErasedSignature() {
        ReferenceType referenceType = new ReferenceType(String.class, null, null, null, false);
        StringBuilder sb = new StringBuilder();
        StringBuilder erasedSignature = referenceType.getErasedSignature(sb);
        
        StringBuilder expectedSignature = new StringBuilder();
        expectedSignature.append("L").append(String.class.getName()).append(';');
        
        assertEquals(expectedSignature.toString(), erasedSignature.toString());
    }
    
    @Test
    public void testGetGenericSignature() {
        JavaType referencedType = new SimpleType();
        ReferenceType referenceType = new ReferenceType(String.class, referencedType, null, null, false);
        StringBuilder sb = new StringBuilder();
        StringBuilder genericSignature = referenceType.getGenericSignature(sb);
        
        StringBuilder expectedSignature = new StringBuilder();
        expectedSignature.append("L").append(String.class.getName()).append('<');
        expectedSignature.append(referencedType.getGenericSignature(sb));
        expectedSignature.append('>');
        
        assertEquals(expectedSignature.toString(), genericSignature.toString());
    }
    
    @Test
    public void testToString() {
        JavaType referencedType = new SimpleType();
        ReferenceType referenceType = new ReferenceType(String.class, referencedType, null, null, false);
        String typeString = referenceType.toString();
        
        StringBuilder expectedString = new StringBuilder();
        expectedString.append("[reference type, class ").append(referenceType.buildCanonicalName())
            .append('<').append(referencedType).append('>').append(']');
        
        assertEquals(expectedString.toString(), typeString);
    }
    
    @Test
    public void testEquals() {
        JavaType referencedType = new SimpleType();
        ReferenceType referenceType1 = new ReferenceType(String.class, referencedType, null, null, false);
        ReferenceType referenceType2 = new ReferenceType(String.class, referencedType, null, null, false);
        
        assertTrue(referenceType1.equals(referenceType2));
    }
}