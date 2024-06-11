package com.fasterxml.jackson.databind.type;

import org.junit.Test;
import static org.junit.Assert.*;

public class ReferenceTypeTest {

    // Test case for the ReferenceType construct() method
    @Test
    public void testConstruct() {
        Class<?> cls = String.class;
        JavaType refType = JavaType.makeEmptyType();
        Object valueHandler = new Object();
        Object typeHandler = new Object();
        
        ReferenceType referenceType = ReferenceType.construct(cls, refType, valueHandler, typeHandler);
        
        assertEquals(cls, referenceType.getRawClass());
        assertEquals(refType, referenceType.getReferencedType());
        assertEquals(valueHandler, referenceType.getValueHandler());
        assertEquals(typeHandler, referenceType.getTypeHandler());
        assertFalse(referenceType.isStaticTyping());
    }

    // Test case for the ReferenceType withTypeHandler() method
    @Test
    public void testWithTypeHandler() {
        Class<?> cls = String.class;
        JavaType refType = JavaType.makeEmptyType();
        Object valueHandler = new Object();
        Object typeHandler = new Object();
        
        ReferenceType referenceType = ReferenceType.construct(cls, refType, valueHandler, typeHandler);
        
        Object newTypeHandler = new Object();
        ReferenceType updatedType = referenceType.withTypeHandler(newTypeHandler);
        
        assertSame(referenceType, updatedType);
        assertEquals(newTypeHandler, updatedType.getTypeHandler());
    }

    // Test case for the ReferenceType withContentTypeHandler() method
    @Test
    public void testWithContentTypeHandler() {
        Class<?> cls = String.class;
        JavaType refType = JavaType.makeEmptyType();
        Object valueHandler = new Object();
        Object typeHandler = new Object();
        
        ReferenceType referenceType = ReferenceType.construct(cls, refType, valueHandler, typeHandler);
        
        Object newContentTypeHandler = new Object();
        ReferenceType updatedType = referenceType.withContentTypeHandler(newContentTypeHandler);
        
        assertSame(referenceType, updatedType);
        assertEquals(newContentTypeHandler, updatedType.getReferencedType().getTypeHandler());
    }

    // Test case for the ReferenceType withValueHandler() method
    @Test
    public void testWithValueHandler() {
        Class<?> cls = String.class;
        JavaType refType = JavaType.makeEmptyType();
        Object valueHandler = new Object();
        Object typeHandler = new Object();
        
        ReferenceType referenceType = ReferenceType.construct(cls, refType, valueHandler, typeHandler);
        
        Object newValueHandler = new Object();
        ReferenceType updatedType = referenceType.withValueHandler(newValueHandler);
        
        assertSame(referenceType, updatedType);
        assertEquals(newValueHandler, updatedType.getValueHandler());
    }

    // Test case for the ReferenceType withContentValueHandler() method
    @Test
    public void testWithContentValueHandler() {
        Class<?> cls = String.class;
        JavaType refType = JavaType.makeEmptyType();
        Object valueHandler = new Object();
        Object typeHandler = new Object();
        
        ReferenceType referenceType = ReferenceType.construct(cls, refType, valueHandler, typeHandler);
        
        Object newContentValueHandler = new Object();
        ReferenceType updatedType = referenceType.withContentValueHandler(newContentValueHandler);
        
        assertSame(referenceType, updatedType);
        assertEquals(newContentValueHandler, updatedType.getReferencedType().getValueHandler());
    }

    // Test case for the ReferenceType withStaticTyping() method
    @Test
    public void testWithStaticTyping() {
        Class<?> cls = String.class;
        JavaType refType = JavaType.makeEmptyType();
        Object valueHandler = new Object();
        Object typeHandler = new Object();
        
        ReferenceType referenceType = ReferenceType.construct(cls, refType, valueHandler, typeHandler);
        
        ReferenceType updatedType = referenceType.withStaticTyping();
        
        assertSame(referenceType, updatedType);
        assertTrue(updatedType.isStaticTyping());
    }

    // Test case for the ReferenceType buildCanonicalName() method
    @Test
    public void testBuildCanonicalName() {
        Class<?> cls = String.class;
        JavaType refType = JavaType.makeEmptyType();
        Object valueHandler = new Object();
        Object typeHandler = new Object();
        
        ReferenceType referenceType = ReferenceType.construct(cls, refType, valueHandler, typeHandler);
        
        StringBuilder sb = new StringBuilder();
        sb.append(cls.getName());
        sb.append('<');
        sb.append(refType.toCanonical());
        
        assertEquals(sb.toString(), referenceType.buildCanonicalName());
    }

    // Test case for the ReferenceType _narrow() method
    @Test
    public void testNarrow() {
        Class<?> cls = String.class;
        JavaType refType = JavaType.makeEmptyType();
        Object valueHandler = new Object();
        Object typeHandler = new Object();
        
        ReferenceType referenceType = ReferenceType.construct(cls, refType, valueHandler, typeHandler);
        
        Class<?> newClass = Integer.class;
        ReferenceType narrowedType = referenceType._narrow(newClass);
        
        assertNotSame(referenceType, narrowedType);
        assertEquals(newClass, narrowedType.getRawClass());
        assertEquals(refType, narrowedType.getReferencedType());
        assertEquals(valueHandler, narrowedType.getValueHandler());
        assertEquals(typeHandler, narrowedType.getTypeHandler());
        assertFalse(narrowedType.isStaticTyping());
    }

    // Test case for the ReferenceType getReferencedType() method
    @Test
    public void testGetReferencedType() {
        Class<?> cls = String.class;
        JavaType refType = JavaType.makeEmptyType();
        Object valueHandler = new Object();
        Object typeHandler = new Object();
        
        ReferenceType referenceType = ReferenceType.construct(cls, refType, valueHandler, typeHandler);
        
        assertEquals(refType, referenceType.getReferencedType());
    }

    // Test case for the ReferenceType isReferenceType() method
    @Test
    public void testIsReferenceType() {
        Class<?> cls = String.class;
        JavaType refType = JavaType.makeEmptyType();
        Object valueHandler = new Object();
        Object typeHandler = new Object();
        
        ReferenceType referenceType = ReferenceType.construct(cls, refType, valueHandler, typeHandler);
        
        assertTrue(referenceType.isReferenceType());
    }

    // Test case for the ReferenceType containedTypeCount() method
    @Test
    public void testContainedTypeCount() {
        Class<?> cls = String.class;
        JavaType refType = JavaType.makeEmptyType();
        Object valueHandler = new Object();
        Object typeHandler = new Object();
        
        ReferenceType referenceType = ReferenceType.construct(cls, refType, valueHandler, typeHandler);
        
        assertEquals(1, referenceType.containedTypeCount());
    }

    // Test case for the ReferenceType containedType() method
    @Test
    public void testContainedType() {
        Class<?> cls = String.class;
        JavaType refType = JavaType.makeEmptyType();
        Object valueHandler = new Object();
        Object typeHandler = new Object();
        
        ReferenceType referenceType = ReferenceType.construct(cls, refType, valueHandler, typeHandler);
        
        assertEquals(refType, referenceType.containedType(0));
        assertNull(referenceType.containedType(1));
    }

    // Test case for the ReferenceType containedTypeName() method
    @Test
    public void testContainedTypeName() {
        Class<?> cls = String.class;
        JavaType refType = JavaType.makeEmptyType();
        Object valueHandler = new Object();
        Object typeHandler = new Object();
        
        ReferenceType referenceType = ReferenceType.construct(cls, refType, valueHandler, typeHandler);
        
        assertEquals("T", referenceType.containedTypeName(0));
        assertNull(referenceType.containedTypeName(1));
    }

    // Test case for the ReferenceType getParameterSource() method
    @Test
    public void testGetParameterSource() {
        Class<?> cls = String.class;
        JavaType refType = JavaType.makeEmptyType();
        Object valueHandler = new Object();
        Object typeHandler = new Object();
        
        ReferenceType referenceType = ReferenceType.construct(cls, refType, valueHandler, typeHandler);
        
        assertEquals(cls, referenceType.getParameterSource());
    }

    // Test case for the ReferenceType getErasedSignature() method
    @Test
    public void testGetErasedSignature() {
        Class<?> cls = String.class;
        JavaType refType = JavaType.makeEmptyType();
        Object valueHandler = new Object();
        Object typeHandler = new Object();
        
        ReferenceType referenceType = ReferenceType.construct(cls, refType, valueHandler, typeHandler);
        
        StringBuilder sb = new StringBuilder();
        sb.append('L');
        sb.append(cls.getName().replace('.', '/'));
        sb.append(';');
        
        assertEquals(sb.toString(), referenceType.getErasedSignature(new StringBuilder()).toString());
    }

    // Test case for the ReferenceType getGenericSignature() method
    @Test
    public void testGetGenericSignature() {
        Class<?> cls = String.class;
        JavaType refType = JavaType.makeEmptyType();
        Object valueHandler = new Object();
        Object typeHandler = new Object();
        
        ReferenceType referenceType = ReferenceType.construct(cls, refType, valueHandler, typeHandler);
        
        StringBuilder sb = new StringBuilder();
        sb.append('L');
        sb.append(cls.getName().replace('.', '/'));
        sb.append(';');
        sb.append('<');
        sb.append(refType.getGenericSignature(new StringBuilder()));
        sb.append(';');
        
        assertEquals(sb.toString(), referenceType.getGenericSignature(new StringBuilder()).toString());
    }

    // Test case for the ReferenceType toString() method
    @Test
    public void testToString() {
        Class<?> cls = String.class;
        JavaType refType = JavaType.makeEmptyType();
        Object valueHandler = new Object();
        Object typeHandler = new Object();
        
        ReferenceType referenceType = ReferenceType.construct(cls, refType, valueHandler, typeHandler);
        
        StringBuilder sb = new StringBuilder(40)
            .append("[reference type, class ")
            .append(referenceType.buildCanonicalName())
            .append('<')
            .append(refType)
            .append('>')
            .append(']');
        
        assertEquals(sb.toString(), referenceType.toString());
    }

    // Test case for the ReferenceType equals() method
    @Test
    public void testEquals() {
        Class<?> cls = String.class;
        JavaType refType = JavaType.makeEmptyType();
        Object valueHandler = new Object();
        Object typeHandler = new Object();
        
        ReferenceType referenceType1 = ReferenceType.construct(cls, refType, valueHandler, typeHandler);
        ReferenceType referenceType2 = ReferenceType.construct(cls, refType, valueHandler, typeHandler);
        
        assertTrue(referenceType1.equals(referenceType2));
        assertTrue(referenceType2.equals(referenceType1));
    }

}