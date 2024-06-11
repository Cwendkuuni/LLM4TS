package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.fasterxml.jackson.databind.JavaType;

public class ReferenceTypeTest {

    @Test
    public void testConstruct() {
        Class<?> cls = String.class;
        JavaType refType = JavaType.genericType(cls);
        Object valueHandler = new Object();
        Object typeHandler = new Object();
        
        ReferenceType referenceType = ReferenceType.construct(cls, refType, valueHandler, typeHandler);
        
        assertNotNull(referenceType);
        assertEquals(cls, referenceType.getRawClass());
        assertEquals(refType.hashCode(), referenceType.hashCode());
        assertEquals(valueHandler, referenceType.getValueHandler());
        assertEquals(typeHandler, referenceType.getTypeHandler());
        assertFalse(referenceType.isPrimitive());
        assertFalse(referenceType.isContainerType());
        assertTrue(referenceType.isReferenceType());
        assertEquals(1, referenceType.containedTypeCount());
        assertEquals(refType, referenceType.containedType(0));
        assertEquals("T", referenceType.containedTypeName(0));
        assertEquals(cls, referenceType.getParameterSource());
        assertEquals(cls.getName() + "<" + refType.toCanonical() + ">", referenceType.toString());
    }
    
    @Test
    public void testWithTypeHandler() {
        Class<?> cls = String.class;
        JavaType refType = JavaType.genericType(cls);
        Object valueHandler = new Object();
        Object typeHandler = new Object();
        
        ReferenceType referenceType = new ReferenceType(cls, refType, valueHandler, typeHandler, false);
        ReferenceType newReferenceType = referenceType.withTypeHandler(new Object());
        
        assertNotNull(newReferenceType);
        assertEquals(cls, newReferenceType.getRawClass());
        assertEquals(refType.hashCode(), newReferenceType.hashCode());
        assertEquals(valueHandler, newReferenceType.getValueHandler());
        assertNotEquals(typeHandler, newReferenceType.getTypeHandler());
        assertFalse(newReferenceType.isPrimitive());
        assertFalse(newReferenceType.isContainerType());
        assertTrue(newReferenceType.isReferenceType());
    }
    
    @Test
    public void testWithContentTypeHandler() {
        Class<?> cls = String.class;
        JavaType refType = JavaType.genericType(cls);
        Object valueHandler = new Object();
        Object typeHandler = new Object();
        
        ReferenceType referenceType = new ReferenceType(cls, refType, valueHandler, typeHandler, false);
        ReferenceType newReferenceType = referenceType.withContentTypeHandler(new Object());
        
        assertNotNull(newReferenceType);
        assertEquals(cls, newReferenceType.getRawClass());
        assertEquals(refType.hashCode(), newReferenceType.hashCode());
        assertEquals(valueHandler, newReferenceType.getValueHandler());
        assertEquals(typeHandler, newReferenceType.getTypeHandler());
        assertFalse(newReferenceType.isPrimitive());
        assertFalse(newReferenceType.isContainerType());
        assertTrue(newReferenceType.isReferenceType());
    }
    
    @Test
    public void testWithValueHandler() {
        Class<?> cls = String.class;
        JavaType refType = JavaType.genericType(cls);
        Object valueHandler = new Object();
        Object typeHandler = new Object();
        
        ReferenceType referenceType = new ReferenceType(cls, refType, valueHandler, typeHandler, false);
        ReferenceType newReferenceType = referenceType.withValueHandler(new Object());
        
        assertNotNull(newReferenceType);
        assertEquals(cls, newReferenceType.getRawClass());
        assertEquals(refType.hashCode(), newReferenceType.hashCode());
        assertNotEquals(valueHandler, newReferenceType.getValueHandler());
        assertEquals(typeHandler, newReferenceType.getTypeHandler());
        assertFalse(newReferenceType.isPrimitive());
        assertFalse(newReferenceType.isContainerType());
        assertTrue(newReferenceType.isReferenceType());
    }
    
    @Test
    public void testWithContentValueHandler() {
        Class<?> cls = String.class;
        JavaType refType = JavaType.genericType(cls);
        Object valueHandler = new Object();
        Object typeHandler = new Object();
        
        ReferenceType referenceType = new ReferenceType(cls, refType, valueHandler, typeHandler, false);
        ReferenceType newReferenceType = referenceType.withContentValueHandler(new Object());
        
        assertNotNull(newReferenceType);
        assertEquals(cls, newReferenceType.getRawClass());
        assertEquals(refType.hashCode(), newReferenceType.hashCode());
        assertEquals(valueHandler, newReferenceType.getValueHandler());
        assertEquals(typeHandler, newReferenceType.getTypeHandler());
        assertFalse(newReferenceType.isPrimitive());
        assertFalse(newReferenceType.isContainerType());
        assertTrue(newReferenceType.isReferenceType());
    }
    
    @Test
    public void testWithStaticTyping() {
        Class<?> cls = String.class;
        JavaType refType = JavaType.genericType(cls);
        Object valueHandler = new Object();
        Object typeHandler = new Object();
        
        ReferenceType referenceType = new ReferenceType(cls, refType, valueHandler, typeHandler, false);
        ReferenceType newReferenceType = referenceType.withStaticTyping();
        
        assertNotNull(newReferenceType);
        assertEquals(cls, newReferenceType.getRawClass());
        assertEquals(refType.hashCode(), newReferenceType.hashCode());
        assertEquals(valueHandler, newReferenceType.getValueHandler());
        assertEquals(typeHandler, newReferenceType.getTypeHandler());
        assertFalse(newReferenceType.isPrimitive());
        assertFalse(newReferenceType.isContainerType());
        assertTrue(newReferenceType.isReferenceType());
    }

    @Test
    public void testGetReferencedType() {
        Class<?> cls = String.class;
        JavaType refType = JavaType.genericType(cls);
        Object valueHandler = new Object();
        Object typeHandler = new Object();
        
        ReferenceType referenceType = new ReferenceType(cls, refType, valueHandler, typeHandler, false);
        
        JavaType referencedType = referenceType.getReferencedType();
        
        assertNotNull(referencedType);
        assertEquals(cls, referencedType.getRawClass());
    }
    
    @Test
    public void testContainedTypeCount() {
        Class<?> cls = String.class;
        JavaType refType = JavaType.genericType(cls);
        Object valueHandler = new Object();
        Object typeHandler = new Object();
        
        ReferenceType referenceType = new ReferenceType(cls, refType, valueHandler, typeHandler, false);
        
        int count = referenceType.containedTypeCount();
        
        assertEquals(1, count);
    }
    
    @Test
    public void testContainedType() {
        Class<?> cls = String.class;
        JavaType refType = JavaType.genericType(cls);
        Object valueHandler = new Object();
        Object typeHandler = new Object();
        
        ReferenceType referenceType = new ReferenceType(cls, refType, valueHandler, typeHandler, false);
        
        JavaType containedType = referenceType.containedType(0);
        
        assertNotNull(containedType);
        assertEquals(cls, containedType.getRawClass());
    }
    
    @Test
    public void testContainedTypeName() {
        Class<?> cls = String.class;
        JavaType refType = JavaType.genericType(cls);
        Object valueHandler = new Object();
        Object typeHandler = new Object();
        
        ReferenceType referenceType = new ReferenceType(cls, refType, valueHandler, typeHandler, false);
        
        String containedTypeName = referenceType.containedTypeName(0);
        
        assertNotNull(containedTypeName);
        assertEquals("T", containedTypeName);
    }
    
    @Test
    public void testGetParameterSource() {
        Class<?> cls = String.class;
        JavaType refType = JavaType.genericType(cls);
        Object valueHandler = new Object();
        Object typeHandler = new Object();
        
        ReferenceType referenceType = new ReferenceType(cls, refType, valueHandler, typeHandler, false);
        
        Class<?> parameterSource = referenceType.getParameterSource();
        
        assertNotNull(parameterSource);
        assertEquals(cls, parameterSource);
    }
}