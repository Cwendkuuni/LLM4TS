package com.fasterxml.jackson.databind.type;

import com.fasterxml.jackson.databind.JavaType;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class ReferenceTypeTest {

    @Test
    public void testConstruct() {
        Class<?> cls = String.class;
        JavaType refType = new JavaType();
        Object valueHandler = new Object();
        Object typeHandler = new Object();
        boolean asStatic = true;

        ReferenceType referenceType = ReferenceType.construct(cls, refType, valueHandler, typeHandler, asStatic);

        assertEquals(cls, referenceType.getClass());
        assertEquals(refType, referenceType.getReferencedType());
        assertEquals(valueHandler, referenceType.getValueHandler());
        assertEquals(typeHandler, referenceType.getTypeHandler());
        assertEquals(asStatic, referenceType.getAsStatic());
    }
    
    @Test
    public void testWithTypeHandler() {
        Object h = new Object();
        
        ReferenceType referenceType = new ReferenceType();
        ReferenceType result = referenceType.withTypeHandler(h);
        
        assertNotEquals(referenceType, result);
        assertEquals(h, result.getTypeHandler());
    }
    
    @Test
    public void testWithContentTypeHandler() {
        Object h = new Object();
        
        ReferenceType referenceType = new ReferenceType();
        ReferenceType result = referenceType.withContentTypeHandler(h);
        
        assertNotEquals(referenceType, result);
        assertEquals(h, result.getReferencedType().getTypeHandler());
    }
    
    @Test
    public void testWithValueHandler() {
        Object h = new Object();
        
        ReferenceType referenceType = new ReferenceType();
        ReferenceType result = referenceType.withValueHandler(h);
        
        assertNotEquals(referenceType, result);
        assertEquals(h, result.getValueHandler());
    }
    
    @Test
    public void testWithContentValueHandler() {
        Object h = new Object();
        
        ReferenceType referenceType = new ReferenceType();
        ReferenceType result = referenceType.withContentValueHandler(h);
        
        assertNotEquals(referenceType, result);
        assertEquals(h, result.getReferencedType().getValueHandler());
    }
    
    @Test
    public void testWithStaticTyping() {
        ReferenceType referenceType = new ReferenceType();
        ReferenceType result = referenceType.withStaticTyping();
        
        assertNotEquals(referenceType, result);
        assertTrue(result.isAsStatic());
    }
    
    @Test
    public void testBuildCanonicalName() {
        Class<?> cls = String.class;
        JavaType refType = new JavaType();
        
        ReferenceType referenceType = new ReferenceType(cls, refType);
        String expected = cls.getName() + "<" + refType.toCanonical() + ">";
        
        assertEquals(expected, referenceType.buildCanonicalName());
    }
    
    @Test
    public void testNarrow() {
        Class<?> subclass = Integer.class;
        JavaType refType = new JavaType();
        
        ReferenceType referenceType = new ReferenceType();
        ReferenceType result = referenceType._narrow(subclass);
        
        assertNotEquals(referenceType, result);
        assertEquals(subclass, result.getClass());
        assertEquals(refType, result.getReferencedType());
    }
    
    @Test
    public void testGetParameterSource() {
        Class<?> cls = String.class;
        
        ReferenceType referenceType = new ReferenceType(cls, new JavaType());
        Class<?> result = referenceType.getParameterSource();
        
        assertEquals(cls, result);
    }
    
    @Test
    public void testGetErasedSignature() {
        Class<?> cls = String.class;
        
        ReferenceType referenceType = new ReferenceType(cls, new JavaType());
        StringBuilder sb = new StringBuilder();
        StringBuilder result = referenceType.getErasedSignature(sb);
        
        assertNotNull(result);
        assertEquals(sb, result);
    }
    
    @Test
    public void testGetGenericSignature() {
        Class<?> cls = String.class;
        JavaType refType = new JavaType();
        
        ReferenceType referenceType = new ReferenceType(cls, refType);
        StringBuilder sb = new StringBuilder();
        StringBuilder result = referenceType.getGenericSignature(sb);
        
        assertNotNull(result);
        assertEquals(sb, result);
    }
    
    @Test
    public void testToString() {
        Class<?> cls = String.class;
        JavaType refType = new JavaType();
        
        ReferenceType referenceType = new ReferenceType(cls, refType);
        String expected = "[reference type, class " + referenceType.buildCanonicalName() + "]";
        
        assertEquals(expected, referenceType.toString());
    }
    
    @Test
    public void testEquals() {
        Class<?> cls = String.class;
        JavaType refType = new JavaType();
        
        ReferenceType referenceType1 = new ReferenceType(cls, refType);
        ReferenceType referenceType2 = new ReferenceType(cls, refType);
        
        assertEquals(referenceType1, referenceType2);
    }
    
    @Test
    public void testContainedTypeCount() {
        ReferenceType referenceType = new ReferenceType();
        int count = referenceType.containedTypeCount();
        
        assertEquals(1, count);
    }
    
    @Test
    public void testContainedType() {
        JavaType refType = new JavaType();
        
        ReferenceType referenceType = new ReferenceType();
        JavaType result = referenceType.containedType(0);
        
        assertEquals(refType, result);
    }
    
    @Test
    public void testContainedTypeName() {
        ReferenceType referenceType = new ReferenceType();
        String result = referenceType.containedTypeName(0);
        
        assertEquals("T", result);
    }
}