
package com.fasterxml.jackson.databind.type;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.databind.JavaType;

import static org.junit.Assert.*;

public class ReferenceTypeTest {

    private ReferenceType referenceType;

    @Before
    public void setUp() {
        JavaType referencedType = new SimpleType(Object.class);
        referenceType = ReferenceType.construct(getClass(), referencedType, null, null);
    }

    @Test
    public void testWithDefaultValueHandler_shouldReturnNewInstanceWithGivenValueHandler() {
        Object valueHandler = new Object();
        ReferenceType newType = referenceType.withValueHandler(valueHandler);
        assertEquals(valueHandler, newType.getValueHandler());
        assertNotEquals(referenceType, newType);
    }

    @Test
    public void testWithContentTypeHandler_shouldReturnNewInstanceWithGivenContentTypeHandler() {
        Object contentTypeHandler = new Object();
        ReferenceType newType = referenceType.withContentTypeHandler(contentTypeHandler);
        assertEquals(contentTypeHandler, newType.getReferencedType().getTypeHandler());
        assertNotEquals(referenceType, newType);
    }

    @Test
    public void testWithContentValueHandler_shouldReturnNewInstanceWithGivenContentValueHandler() {
        Object contentValueHandler = new Object();
        ReferenceType newType = referenceType.withContentValueHandler(contentValueHandler);
        assertEquals(contentValueHandler, newType.getReferencedType().getValueHandler());
        assertNotEquals(referenceType, newType);
    }

    @Test
    public void testWithStaticTyping_shouldReturnNewInstanceWithStaticTyping() {
        ReferenceType newType = referenceType.withStaticTyping();
        assertTrue(newType.isStaticTyping());
        assertNotEquals(referenceType, newType);
    }

    @Test
    public void testWithNullTypeHandler_shouldReturnSameInstance() {
        ReferenceType newType = referenceType.withTypeHandler(null);
        assertSame(referenceType, newType);
    }

    @Test
    public void testWithNullContentTypeHandler_shouldReturnSameInstance() {
        ReferenceType newType = referenceType.withContentTypeHandler(null);
        assertSame(referenceType, newType);
    }

    @Test
    public void testWithNullValueHandler_shouldReturnSameInstance() {
        ReferenceType newType = referenceType.withValueHandler(null);
        assertSame(referenceType, newType);
    }

    @Test
    public void testWithNullContentValueHandler_shouldReturnSameInstance() {
        ReferenceType newType = referenceType.withContentValueHandler(null);
        assertSame(referenceType, newType);
    }

    @Test
    public void testBuildCanonicalName_shouldReturnCorrectString() {
        String canonicalName = referenceType.buildCanonicalName();
        assertEquals("com.fasterxml.jackson.databind.type.ReferenceType<java.lang.Object>", canonicalName);
    }

    @Test
    public void testNarrow_shouldReturnNewInstanceWithSubclassReferenceType() {
        Class<?> subclass = Integer.class;
        ReferenceType newType = referenceType.narrow(subclass);
        assertEquals(subclass, newType.getRawClass());
        assertNotEquals(referenceType, newType);
    }

    @Test
    public void testGetReferencedType_shouldReturnCorrectJavaType() {
        JavaType referencedType = referenceType.getReferencedType();
        assertEquals(Object.class, referencedType.getRawClass());
    }

    @Test
    public void testIsReferenceType_shouldReturnTrue() {
        assertTrue(referenceType.isReferenceType());
    }

    @Test
    public void testContainedTypeCount_shouldReturn1() {
        int count = referenceType.containedTypeCount();
        assertEquals(1, count);
    }

    @Test
    public void testContainedType_shouldReturnReferencedTypeWhenIndexIs0() {
        JavaType containedType = referenceType.containedType(0);
        assertEquals(referenceType.getReferencedType(), containedType);
    }

    @Test
    public void testContainedTypeName_shouldReturnTWhenIndexIs0() {
        String typeName = referenceType.containedTypeName(0);
        assertEquals("T", typeName);
    }

    @Test
    public void testGetParameterSource_shouldReturnRawTypeOfClass() {
        Class<?> parameterSource = referenceType.getParameterSource();
        assertEquals(getClass(), parameterSource);
    }

    @Test
    public void testGetErasedSignature_shouldReturnClassSignature() {
        StringBuilder sb = referenceType.getErasedSignature(new StringBuilder());
        assertEquals("com.fasterxml.jackson.databind.type.ReferenceType", sb.toString());
    }

    @Test
    public void testGetGenericSignature_shouldReturnGenericTypeSignature() {
        StringBuilder sb = referenceType.getGenericSignature(new StringBuilder());
        assertEquals("com.fasterxml.jackson.databind.type.ReferenceType<java.lang.Object;>", sb.toString());
    }

    @Test
    public void testToString_shouldReturnCorrectStringRepresentation() {
        String toString = referenceType.toString();
        assertEquals("[reference type, class com.fasterxml.jackson.databind.type.ReferenceType<java.lang.Object>]", toString);
    }

    @Test
    public void testEquals_shouldReturnTrueForEqualReferenceTypes() {
        ReferenceType newType = ReferenceType.construct(getClass(), new SimpleType(Object.class), null, null);
        assertTrue(referenceType.equals(newType));
    }

    @Test
    public void testEquals_shouldReturnFalseForDifferentReferenceTypes() {
        ReferenceType newType = ReferenceType.construct(getClass(), new SimpleType(String.class), null, null);
        assertFalse(referenceType.equals(newType));
    }

}

