package com.fasterxml.jackson.databind.type;

import org.junit.Test;
import static org.junit.Assert.*;

public class ReferenceTypeTest {
    
    @Test
    public void testConstruct() {
        Class<?> cls = Integer.class;
        JavaType refType = JavaType.fromClass(String.class);
        Object valueHandler = null;
        Object typeHandler = null;
        ReferenceType referenceType = ReferenceType.construct(cls, refType, valueHandler, typeHandler);
        assertEquals(cls, referenceType._class);
        assertEquals(refType, referenceType._referencedType);
        assertNull(referenceType._valueHandler);
        assertNull(referenceType._typeHandler);
        assertFalse(referenceType._asStatic);
    }
    
    @Test
    public void testWithTypeHandler() {
        Object h = new Object();
        ReferenceType referenceType = new ReferenceType(Integer.class, JavaType.fromClass(String.class), null, null,false);
        ReferenceType withTypeHandlerResult = referenceType.withTypeHandler(h);
        assertSame(referenceType, withTypeHandlerResult);
        assertSame(h, withTypeHandlerResult._typeHandler);
    }
    
    @Test
    public void testWithContentTypeHandler() {
        Object h = new Object();
        ReferenceType referenceType = new ReferenceType(Integer.class, JavaType.fromClass(String.class), null, null,false);
        ReferenceType withContentTypeHandlerResult = referenceType.withContentTypeHandler(h);
        assertSame(referenceType, withContentTypeHandlerResult);
        assertSame(h, withContentTypeHandlerResult._referencedType.getTypeHandler());
    }
    
    @Test
    public void testWithValueHandler() {
        Object h = new Object();
        ReferenceType referenceType = new ReferenceType(Integer.class, JavaType.fromClass(String.class), null, null,false);
        ReferenceType withValueHandlerResult = referenceType.withValueHandler(h);
        assertSame(referenceType, withValueHandlerResult);
        assertSame(h, withValueHandlerResult._valueHandler);
    }
    
    @Test
    public void testWithContentValueHandler() {
        Object h = new Object();
        ReferenceType referenceType = new ReferenceType(Integer.class, JavaType.fromClass(String.class), null, null,false);
        ReferenceType withContentValueHandlerResult = referenceType.withContentValueHandler(h);
        assertSame(referenceType, withContentValueHandlerResult);
        assertSame(h, withContentValueHandlerResult._referencedType.getValueHandler());
    }
    
    @Test
    public void testWithStaticTyping() {
        ReferenceType referenceType = new ReferenceType(Integer.class, JavaType.fromClass(String.class), null, null,false);
        ReferenceType withStaticTypingResult = referenceType.withStaticTyping();
        assertSame(referenceType, withStaticTypingResult);
        assertTrue(withStaticTypingResult._asStatic);
    }
    
    @Test
    public void testBuildCanonicalName() {
        ReferenceType referenceType = new ReferenceType(Integer.class, JavaType.fromClass(String.class), null, null,false);
        String canonicalName = referenceType.buildCanonicalName();
        assertEquals("java.lang.Integer<java.lang.String>", canonicalName);
    }
    
    @Test
    public void testNarrow() {
        ReferenceType referenceType = new ReferenceType(Integer.class, JavaType.fromClass(String.class), null, null,false);
        Class<?> subclass = Double.class;
        ReferenceType narrowResult = (ReferenceType) referenceType._narrow(subclass);
        assertNotSame(referenceType, narrowResult);
        assertEquals(subclass, narrowResult._class);
        assertEquals(referenceType._referencedType, narrowResult._referencedType);
        assertNull(narrowResult._valueHandler);
        assertNull(narrowResult._typeHandler);
        assertFalse(narrowResult._asStatic);
    }
    
    @Test
    public void testGetReferencedType() {
        ReferenceType referenceType = new ReferenceType(Integer.class, JavaType.fromClass(String.class), null, null,false);
        JavaType referencedType = referenceType.getReferencedType();
        assertEquals(JavaType.fromClass(String.class), referencedType);
    }
    
    @Test
    public void testIsReferenceType() {
        ReferenceType referenceType = new ReferenceType(Integer.class, JavaType.fromClass(String.class), null, null,false);
        assertTrue(referenceType.isReferenceType());
    }

    @Test
    public void testContainedTypeCount() {
        ReferenceType referenceType = new ReferenceType(Integer.class, JavaType.fromClass(String.class), null, null,false);
        int count = referenceType.containedTypeCount();
        assertEquals(1, count);
    }
    
    @Test
    public void testContainedType() {
        ReferenceType referenceType = new ReferenceType(Integer.class, JavaType.fromClass(String.class), null, null,false);
        JavaType containedType = referenceType.containedType(0);
        assertEquals(JavaType.fromClass(String.class), containedType);
    }
    
    @Test
    public void testContainedTypeName() {
        ReferenceType referenceType = new ReferenceType(Integer.class, JavaType.fromClass(String.class), null, null,false);
        String typeName = referenceType.containedTypeName(0);
        assertEquals("T", typeName);
    }
    
    @Test
    public void testGetParameterSource() {
        ReferenceType referenceType = new ReferenceType(Integer.class, JavaType.fromClass(String.class), null, null,false);
        Class<?> parameterSource = referenceType.getParameterSource();
        assertEquals(Integer.class, parameterSource);
    }
    
    @Test
    public void testGetErasedSignature() {
        ReferenceType referenceType = new ReferenceType(Integer.class, JavaType.fromClass(String.class), null, null,false);
        StringBuilder sb = new StringBuilder();
        StringBuilder result = referenceType.getErasedSignature(sb);
        assertSame(sb, result);
    }
    
    @Test
    public void testGetGenericSignature() {
        ReferenceType referenceType = new ReferenceType(Integer.class, JavaType.fromClass(String.class), null, null,false);
        StringBuilder sb = new StringBuilder();
        StringBuilder result = referenceType.getGenericSignature(sb);
        assertSame(sb, result);
    }
    
    @Test
    public void testToString() {
        ReferenceType referenceType = new ReferenceType(Integer.class, JavaType.fromClass(String.class), null, null,false);
        String string = referenceType.toString();
        assertEquals("[reference type, class java.lang.Integer<java.lang.String>]", string);
    }
    
    @Test
    public void testEqualsWithEqualObjects() {
        ReferenceType referenceType1 = new ReferenceType(Integer.class, JavaType.fromClass(String.class), null, null,false);
        ReferenceType referenceType2 = new ReferenceType(Integer.class, JavaType.fromClass(String.class), null, null,false);
        assertTrue(referenceType1.equals(referenceType2));
    }
    
    @Test
    public void testEqualsWithDifferentClasses() {
        ReferenceType referenceType1 = new ReferenceType(Integer.class, JavaType.fromClass(String.class), null, null,false);
        ReferenceType referenceType2 = new ReferenceType(Double.class, JavaType.fromClass(String.class), null, null,false);
        assertFalse(referenceType1.equals(referenceType2));
    }
    
    @Test
    public void testEqualsWithDifferentReferencedTypes() {
        ReferenceType referenceType1 = new ReferenceType(Integer.class, JavaType.fromClass(String.class), null, null,false);
        ReferenceType referenceType2 = new ReferenceType(Integer.class, JavaType.fromClass(Double.class), null, null,false);
        assertFalse(referenceType1.equals(referenceType2));
    }
}