package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class ReferenceTypeTest {
    @Test
    public void testConstruct() {
        ReferenceType type = ReferenceType.construct(String.class, JavaType.STRING, null, null);
        assertEquals(String.class, type._class);
        assertEquals(JavaType.STRING, type._referencedType);
        assertEquals(null, type._valueHandler);
        assertEquals(null, type._typeHandler);
        assertEquals(false, type._asStatic);
    }
    
    @Test
    public void testWithTypeHandler() {
        ReferenceType type1 = new ReferenceType(String.class, JavaType.STRING, null, null, false);
        ReferenceType type2 = type1.withTypeHandler(null);
        assertEquals(type1._class, type2._class);
        assertEquals(type1._referencedType, type2._referencedType);
        assertEquals(type1._valueHandler, type2._valueHandler);
        assertEquals(null, type2._typeHandler);
        assertEquals(type1._asStatic, type2._asStatic);
    }
    
    @Test
    public void testWithContentTypeHandler() {
        ReferenceType type1 = new ReferenceType(String.class, JavaType.STRING, null, null, false);
        ReferenceType type2 = type1.withContentTypeHandler(null);
        assertEquals(type1._class, type2._class);
        assertEquals(type1._referencedType.withTypeHandler(null), type2._referencedType);
        assertEquals(type1._valueHandler, type2._valueHandler);
        assertEquals(type1._typeHandler, type2._typeHandler);
        assertEquals(type1._asStatic, type2._asStatic);
    }
    
    @Test
    public void testWithValueHandler() {
        ReferenceType type1 = new ReferenceType(String.class, JavaType.STRING, null, null, false);
        ReferenceType type2 = type1.withValueHandler(null);
        assertEquals(type1._class, type2._class);
        assertEquals(type1._referencedType, type2._referencedType);
        assertEquals(null, type2._valueHandler);
        assertEquals(type1._typeHandler, type2._typeHandler);
        assertEquals(type1._asStatic, type2._asStatic);
    }
    
    @Test
    public void testWithContentValueHandler() {
        ReferenceType type1 = new ReferenceType(String.class, JavaType.STRING, null, null, false);
        ReferenceType type2 = type1.withContentValueHandler(null);
        assertEquals(type1._class, type2._class);
        assertEquals(type1._referencedType.withValueHandler(null), type2._referencedType);
        assertEquals(type1._valueHandler, type2._valueHandler);
        assertEquals(type1._typeHandler, type2._typeHandler);
        assertEquals(type1._asStatic, type2._asStatic);
    }
    
    @Test
    public void testWithStaticTyping() {
        ReferenceType type1 = new ReferenceType(String.class, JavaType.STRING, null, null, false);
        ReferenceType type2 = type1.withStaticTyping();
        assertEquals(type1._class, type2._class);
        assertEquals(type1._referencedType.withStaticTyping(), type2._referencedType);
        assertEquals(type1._valueHandler, type2._valueHandler);
        assertEquals(type1._typeHandler, type2._typeHandler);
        assertEquals(true, type2._asStatic);
    }
    
    @Test
    public void testBuildCanonicalName() {
        ReferenceType type = new ReferenceType(String.class, JavaType.STRING, null, null, false);
        assertEquals("java.lang.String<java.lang.String>", type.buildCanonicalName());
    }
    
    @Test
    public void testReferencedType() {
        ReferenceType type = new ReferenceType(String.class, JavaType.STRING, null, null, false);
        assertEquals(JavaType.STRING, type.getReferencedType());
    }
    
    @Test
    public void testIsReferenceType() {
        ReferenceType type = new ReferenceType(String.class, JavaType.STRING, null, null, false);
        assertEquals(true, type.isReferenceType());
    }
    
    @Test
    public void testContainedTypeCount() {
        ReferenceType type = new ReferenceType(String.class, JavaType.STRING, null, null, false);
        assertEquals(1, type.containedTypeCount());
    }
    
    @Test
    public void testContainedType() {
        ReferenceType type = new ReferenceType(String.class, JavaType.STRING, null, null, false);
        assertEquals(JavaType.STRING, type.containedType(0));
    }
    
    @Test
    public void testContainedTypeName() {
        ReferenceType type = new ReferenceType(String.class, JavaType.STRING, null, null, false);
        assertEquals("T", type.containedTypeName(0));
    }
    
    @Test
    public void testGetParameterSource() {
        ReferenceType type = new ReferenceType(String.class, JavaType.STRING, null, null, false);
        assertEquals(String.class, type.getParameterSource());
    }
    
    @Test
    public void testGetErasedSignature() {
        ReferenceType type = new ReferenceType(String.class, JavaType.STRING, null, null, false);
        StringBuilder sb = new StringBuilder();
        StringBuilder result = type.getErasedSignature(sb);
        assertEquals(sb, result);
    }
    
    @Test
    public void testGetGenericSignature() {
        ReferenceType type = new ReferenceType(String.class, JavaType.STRING, null, null, false);
        StringBuilder sb = new StringBuilder();
        StringBuilder result = type.getGenericSignature(sb);
        assertEquals(sb, result);
    }
    
    @Test
    public void testToString() {
        ReferenceType type = new ReferenceType(String.class, JavaType.STRING, null, null, false);
        assertEquals("[reference type, class java.lang.String<java.lang.String>]", type.toString());
    }
    
    @Test
    public void testEquals() {
        ReferenceType type1 = new ReferenceType(String.class, JavaType.STRING, null, null, false);
        ReferenceType type2 = new ReferenceType(String.class, JavaType.STRING, null, null, false);
        assertEquals(true, type1.equals(type2));
    }
}