package com.fasterxml.jackson.databind.type;

import org.junit.Test;
import static org.junit.Assert.*;

public class ReferenceTypeTest {
    
    @Test
    public void testConstruct() {
        Class<?> cls = String.class;
        JavaType refType = new SimpleType(Integer.class);
        Object valueHandler = new Object();
        Object typeHandler = new Object();
        
        ReferenceType referenceType = ReferenceType.construct(cls, refType, valueHandler, typeHandler);
        
        assertEquals(cls, referenceType.getRawClass());
        assertEquals(refType, referenceType.getReferencedType());
        assertEquals(valueHandler, referenceType.getValueHandler());
        assertEquals(typeHandler, referenceType.getTypeHandler());
    }
    
    @Test
    public void testWithContentTypeHandler() {
        Class<?> cls = String.class;
        JavaType refType = new SimpleType(Integer.class);
        Object valueHandler = new Object();
        Object typeHandler = new Object();
        
        ReferenceType originalType = ReferenceType.construct(cls, refType, valueHandler, typeHandler);
        Object newContentTypeHandler = new Object();
        
        ReferenceType referenceType = originalType.withContentTypeHandler(newContentTypeHandler);
        
        assertEquals(cls, referenceType.getRawClass());
        assertEquals(refType.withTypeHandler(newContentTypeHandler), referenceType.getReferencedType());
        assertEquals(valueHandler, referenceType.getValueHandler());
        assertEquals(typeHandler, referenceType.getTypeHandler());
    }
    
    @Test
    public void testWithValueHandler() {
        Class<?> cls = String.class;
        JavaType refType = new SimpleType(Integer.class);
        Object valueHandler = new Object();
        Object typeHandler = new Object();
        
        ReferenceType originalType = ReferenceType.construct(cls, refType, valueHandler, typeHandler);
        Object newValueHandler = new Object();
        
        ReferenceType referenceType = originalType.withValueHandler(newValueHandler);
        
        assertEquals(cls, referenceType.getRawClass());
        assertEquals(refType, referenceType.getReferencedType());
        assertEquals(newValueHandler, referenceType.getValueHandler());
        assertEquals(typeHandler, referenceType.getTypeHandler());
    }
    
    @Test
    public void testWithStaticTyping() {
        Class<?> cls = String.class;
        JavaType refType = new SimpleType(Integer.class);
        Object valueHandler = new Object();
        Object typeHandler = new Object();
        
        ReferenceType originalType = ReferenceType.construct(cls, refType, valueHandler, typeHandler);
        
        ReferenceType referenceType = originalType.withStaticTyping();
        
        assertEquals(cls, referenceType.getRawClass());
        assertEquals(refType.withStaticTyping(), referenceType.getReferencedType());
        assertEquals(valueHandler, referenceType.getValueHandler());
        assertEquals(typeHandler, referenceType.getTypeHandler());
    }
    
    @Test
    public void testNarrow() {
        Class<?> cls = String.class;
        JavaType refType = new SimpleType(Integer.class);
        Object valueHandler = new Object();
        Object typeHandler = new Object();
        
        ReferenceType originalType = ReferenceType.construct(cls, refType, valueHandler, typeHandler);
        Class<?> subclass = Object.class;
        
        ReferenceType referenceType = (ReferenceType) originalType._narrow(subclass);
        
        assertEquals(subclass, referenceType.getRawClass());
        assertEquals(refType, referenceType.getReferencedType());
        assertEquals(valueHandler, referenceType.getValueHandler());
        assertEquals(typeHandler, referenceType.getTypeHandler());
    }
    
    @Test
    public void testContainedTypeCount() {
        Class<?> cls = String.class;
        JavaType refType = new SimpleType(Integer.class);
        Object valueHandler = new Object();
        Object typeHandler = new Object();
        
        ReferenceType referenceType = ReferenceType.construct(cls, refType, valueHandler, typeHandler);
        
        assertEquals(1, referenceType.containedTypeCount());
    }
    
    @Test
    public void testContainedType() {
        Class<?> cls = String.class;
        JavaType refType = new SimpleType(Integer.class);
        Object valueHandler = new Object();
        Object typeHandler = new Object();
        
        ReferenceType referenceType = ReferenceType.construct(cls, refType, valueHandler, typeHandler);
        
        assertEquals(refType, referenceType.containedType(0));
        assertNull(referenceType.containedType(1));
    }
    
    @Test
    public void testContainedTypeName() {
        Class<?> cls = String.class;
        JavaType refType = new SimpleType(Integer.class);
        Object valueHandler = new Object();
        Object typeHandler = new Object();
        
        ReferenceType referenceType = ReferenceType.construct(cls, refType, valueHandler, typeHandler);
        
        assertEquals("T", referenceType.containedTypeName(0));
        assertNull(referenceType.containedTypeName(1));
    }
    
    @Test
    public void testGetParameterSource() {
        Class<?> cls = String.class;
        JavaType refType = new SimpleType(Integer.class);
        Object valueHandler = new Object();
        Object typeHandler = new Object();
        
        ReferenceType referenceType = ReferenceType.construct(cls, refType, valueHandler, typeHandler);
        
        assertEquals(cls, referenceType.getParameterSource());
    }
    
    @Test
    public void testGetErasedSignature() {
        Class<?> cls = String.class;
        JavaType refType = new SimpleType(Integer.class);
        Object valueHandler = new Object();
        Object typeHandler = new Object();
        
        ReferenceType referenceType = ReferenceType.construct(cls, refType, valueHandler, typeHandler);
        
        StringBuilder sb = new StringBuilder();
        StringBuilder expected = new StringBuilder();
        clsSignature(cls, expected, true);
        
        assertEquals(expected.toString(), referenceType.getErasedSignature(sb).toString());   
    }
    
    @Test
    public void testGetGenericSignature() {
        Class<?> cls = String.class;
        JavaType refType = new SimpleType(Integer.class);
        Object valueHandler = new Object();
        Object typeHandler = new Object();
        
        ReferenceType referenceType = ReferenceType.construct(cls, refType, valueHandler, typeHandler);
        
        StringBuilder sb = new StringBuilder();
        StringBuilder expected = new StringBuilder();
        clsSignature(cls, expected, false);
        expected.append('<');
        refType.getGenericSignature(expected);
        expected.append(';');
        
        assertEquals(expected.toString(), referenceType.getGenericSignature(sb).toString());
    }
    
    @Test
    public void testToString() {
        Class<?> cls = String.class;
        JavaType refType = new SimpleType(Integer.class);
        Object valueHandler = new Object();
        Object typeHandler = new Object();
        
        ReferenceType referenceType = ReferenceType.construct(cls, refType, valueHandler, typeHandler);
        
        StringBuilder expected = new StringBuilder(40);
        expected.append("[reference type, class ");
        expected.append(referenceType.buildCanonicalName());
        expected.append('<');
        expected.append(refType);
        expected.append('>');
        expected.append(']');
        
        assertEquals(expected.toString(), referenceType.toString());
    }
    
    @Test
    public void testEquals() {
        Class<?> cls = String.class;
        JavaType refType = new SimpleType(Integer.class);
        Object valueHandler = new Object();
        Object typeHandler = new Object();
        
        ReferenceType type1 = ReferenceType.construct(cls, refType, valueHandler, typeHandler);
        ReferenceType type2 = ReferenceType.construct(cls, refType, valueHandler, typeHandler);
        ReferenceType type3 = ReferenceType.construct(Object.class, refType, valueHandler, typeHandler);
        ReferenceType type4 = ReferenceType.construct(cls, new SimpleType(Boolean.class), valueHandler, typeHandler);
        ReferenceType type5 = ReferenceType.construct(cls, refType, new Object(), typeHandler);
        ReferenceType type6 = ReferenceType.construct(cls, refType, valueHandler, new Object());
        
        assertTrue(type1.equals(type1));
        assertTrue(type1.equals(type2));
        assertFalse(type1.equals(type3));
        assertFalse(type1.equals(type4));
        assertFalse(type1.equals(type5));
        assertFalse(type1.equals(type6));
    }
    
    private void clsSignature(Class<?> cls, StringBuilder sb, boolean isErased) {
        if (!isErased) {
            sb.append('L');
        }
        sb.append(cls.getName().replace('.', '/'));
        if (!isErased) {
            sb.append(';');
        }
    }
}