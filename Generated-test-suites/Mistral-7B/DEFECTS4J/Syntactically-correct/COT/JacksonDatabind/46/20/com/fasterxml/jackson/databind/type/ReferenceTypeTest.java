package com.example.referenceType; // replace with the actual package name

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import com.fasterxml.jackson.core.type.ClassTypeResolver;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.SimpleType;
import com.fasterxml.jackson.databind.type.TypeFactory;

public class ReferenceTypeTest {

    @Test
    public void testConstructor() {
        Class<String> cls = String.class;
        JavaType refType = TypeFactory.defaultInstance().constructType(int.class);
        Object valueHandler = mock(Object.class);
        Object typeHandler = mock(Object.class);
        ReferenceType referenceType = new ReferenceType(cls, refType, valueHandler, typeHandler, false);
        assertEquals(cls, referenceType.getClass());
        assertEquals(refType, referenceType.getContentType());
        assertNotNull(valueHandler);
        assertNotNull(typeHandler);
        assertFalse(referenceType.isStatic());
    }

    @Test
    public void testConstructStatic() {
        Class<String> cls = String.class;
        JavaType refType = TypeFactory.defaultInstance().constructType(int.class);
        Object typeHandler = mock(Object.class);
        ReferenceType referenceType = ReferenceType.construct(cls, refType, null, typeHandler);
        assertTrue(referenceType.isStatic());
        assertEquals(cls, referenceType.getClass());
        assertEquals(refType, referenceType.getContentType());
        assertNotNull(typeHandler);
    }

    @Test
    public void testWithTypeHandler() {
        Class<String> cls = String.class;
        JavaType refType = TypeFactory.defaultInstance().constructType(int.class);
        Object valueHandler = mock(Object.class);
        Object newTypeHandler = mock(Object.class);
        ReferenceType referenceType = new ReferenceType(cls, refType, valueHandler, null, false);
        ReferenceType newReferenceType = referenceType.withTypeHandler(newTypeHandler);
        assertSame(referenceType.getClass(), newReferenceType.getClass());
        assertSame(cls, newReferenceType.getClass());
        assertSame(refType, newReferenceType.getContentType());
        assertSame(valueHandler, newReferenceType._valueHandler);
        assertNotSame(newTypeHandler, newReferenceType._typeHandler);
        assertFalse(newReferenceType.isStatic());
    }

    @Test
    public void testWithContentTypeHandler() {
        Class<String> cls = String.class;
        JavaType refType = TypeFactory.defaultInstance().constructType(int[].class);
        Object valueHandler = mock(Object.class);
        Object newTypeHandler = mock(Object.class);
        ReferenceType referenceType = new ReferenceType(cls, refType, valueHandler, null, false);
        JavaType newContentType = TypeFactory.defaultInstance().constructType(String[].class);
        ReferenceType newReferenceType = referenceType.withContentTypeHandler(newTypeHandler);
        assertSame(referenceType.getClass(), newReferenceType.getClass());
        assertSame(cls, newReferenceType.getClass());
        assertSame(newContentType, newReferenceType.getContentType());
        assertSame(valueHandler, newReferenceType._valueHandler);
        assertNotSame(newTypeHandler, newReferenceType._referencedType);
        assertSame(newTypeHandler, newReferenceType._referencedType.<Object>getTypeHandler());
        assertFalse(newReferenceType.isStatic());
    }

    @Test
    public void testWithValueHandler() {
        Class<String> cls = String.class;
        JavaType refType = TypeFactory.defaultInstance().constructType(int.class);
        Object valueHandler = mock(Object.class);
        Object newValueHandler = mock(Object.class);
        ReferenceType referenceType = new ReferenceType(cls, refType, null, null, false);
        ReferenceType newReferenceType = referenceType.withValueHandler(newValueHandler);
        assertSame(referenceType.getClass(), newReferenceType.getClass());
        assertSame(cls, newReferenceType.getClass());
        assertSame(refType, newReferenceType.getContentType());
        assertNotSame(valueHandler, newReferenceType._valueHandler);
        assertSame(newValueHandler, newReferenceType._valueHandler);
        assertFalse(newReferenceType.isStatic());
    }

    @Test
    public void testWithContentValueHandler() {
        Class<String> cls = String.class;
        JavaType refType = TypeFactory.defaultInstance().constructType(int[].class);
        Object valueHandler = mock(Object.class);
        Object newValueHandler = mock(Object.class);
        ReferenceType referenceType = new ReferenceType(cls, refType, null, null, false);
        JavaType newContentType = TypeFactory.defaultInstance().constructType(String[].class);
        ReferenceType newReferenceType = referenceType.withContentValueHandler(newValueHandler);
        assertSame(referenceType.getClass(), newReferenceType.getClass());
        assertSame(cls, newReferenceType.getClass());
        assertSame(newContentType, newReferenceType.getContentType());
        assertNotSame(valueHandler, newReferenceType._valueHandler);
        assertSame(newValueHandler, newReferenceType._referencedType.<Object>getValueHandler());
        assertFalse(newReferenceType.isStatic());
    }

    @Test
    public void testWithStaticTyping() {
        Class<String> cls = String.class;
        JavaType refType = TypeFactory.defaultInstance().constructType(int.class);
        ReferenceType referenceType = new ReferenceType(cls, refType, null, null, false);
        ReferenceType newReferenceType = referenceType.withStaticTyping();
        assertTrue(newReferenceType.isStatic());
        assertSame(cls, newReferenceType.getClass());
        assertSame(refType, newReferenceType.getContentType());
        assertNull(newReferenceType._valueHandler);
        assertNull(newReferenceType._typeHandler);
    }

    @Test
    public void testGetReferencedType() {
        Class<String> cls = String.class;
        JavaType refType = TypeFactory.defaultInstance().constructType(int.class);
        ReferenceType referenceType = new ReferenceType(cls, refType, null, null, false);
        assertEquals(refType, referenceType.getReferencedType());
    }

    @Test
    public void testIsReferenceType() {
        Class<String> cls = String.class;
        JavaType refType = TypeFactory.defaultInstance().constructType(int.class);
        ReferenceType referenceType = new ReferenceType(cls, refType, null, null, false);
        assertTrue(referenceType.isReferenceType());
    }

    @Test(expected = IllegalStateException.class)
    public void testInvalidNarrowing() {
        Class<String> cls = String.class;
        JavaType refType = TypeFactory.defaultInstance().constructType(int.class);
        ReferenceType referenceType = new ReferenceType(cls, refType, null, null, false);
        referenceType.narrow(Double.TYPE);
    }

    @Test
    public void testNarrowing() {
        Class<String> cls = String.class;
        JavaType refType = TypeFactory.defaultInstance().constructType(int.class);
        JavaType newRefType = TypeFactory.defaultInstance().constructType(String.class);
        ReferenceType referenceType = new ReferenceType(cls, refType, null, null, false);
        JavaType narrowedType = referenceType.narrow(String.class);
        assertEquals(newRefType, narrowedType);
    }

    @Test
    public void testContainedTypeCount() {
        Class<String> cls = String.class;
        JavaType refType = TypeFactory.defaultInstance().constructType(int.class);
        ReferenceType referenceType = new ReferenceType(cls, refType, null, null, false);
        assertEquals(1, referenceType.containedTypeCount());
    }

    @Test
    public void testContainedType() {
        Class<String> cls = String.class;
        JavaType refType = TypeFactory.defaultInstance().constructType(int.class);
        ReferenceType referenceType = new ReferenceType(cls, refType, null, null, false);
        JavaType containedType = referenceType.containedType(0);
        assertEquals(refType, containedType);
    }

    @Test
    public void testContainedTypeName() {
        Class<String> cls = String.class;
        JavaType refType = TypeFactory.defaultInstance().constructType("java.util.List<java.lang.Integer>");
        ReferenceType referenceType = new ReferenceType(cls, refType, null, null, false);
        String containedTypeName = referenceType.containedTypeName(0);
        assertEquals("T", containedTypeName);
    }

    @Test
    public void testGetParameterSource() {
        Class<String> cls = String.class;
        JavaType refType = TypeFactory.defaultInstance().constructType(int.class);
        ReferenceType referenceType = new ReferenceType(cls, refType, null, null, false);
        assertEquals(cls, referenceType.getParameterSource());
    }

    @Test
    public void testGetErasedSignature() {
        Class<String> cls = String.class;
        JavaType refType = TypeFactory.defaultInstance().constructType(int.class);
        ReferenceType referenceType = new ReferenceType(cls, refType, null, null, false);
        StringBuilder expectedErasedSignature = new StringBuilder();
        expectedErasedSignature.append("java.lang.String");
        StringBuilder actualErasedSignature = referenceType.getErasedSignature(new StringBuilder());
        assertEquals(expectedErasedSignature.toString(), actualErasedSignature.toString());
    }

    @Test
    public void testGetGenericSignature() {
        Class<String> cls = String.class;
        JavaType refType = TypeFactory.defaultInstance().constructType("java.util.List<java.lang.Integer>");
        ReferenceType referenceType = new ReferenceType(cls, refType, null, null, false);
        StringBuilder expectedGenericSignature = new StringBuilder();
        expectedGenericSignature.append("java.lang.String");
        expectedGenericSignature.append('<');
        expectedGenericSignature.append("java.util.List");
        expectedGenericSignature.append('<');
        expectedGenericSignature.append("java.lang.Integer");
        expectedGenericSignature.append('>');
        expectedGenericSignature.append('>');
        StringBuilder actualGenericSignature = new StringBuilder();
        referenceType.getGenericSignature(actualGenericSignature);
        assertEquals(expectedGenericSignature.toString(), actualGenericSignature.toString());
    }

    @Test
    public void testToString() {
        Class<String> cls = String.class;
        JavaType refType = TypeFactory.defaultInstance().constructType(int.class);
        ReferenceType referenceType = new ReferenceType(cls, refType, null, null, false);
        String expectedString = "[reference type, class java.lang.String<java.lang.Integer>]";
        String actualString = referenceType.toString();
        assertEquals(expectedString, actualString);
    }

    @Test
    public void testEquals() {
        Class<String> cls1 = String.class;
        JavaType refType1 = TypeFactory.defaultInstance().constructType(int.class);
        Object valueHandler1 = mock(Object.class);
        Object typeHandler1 = mock(Object.class);
        ReferenceType referenceType1 = new ReferenceType(cls1, refType1, valueHandler1, typeHandler1, false);

        Class<String> cls2 = String.class;
        JavaType refType2 = TypeFactory.defaultInstance().constructType(int[].class);
        Object valueHandler2 = mock(Object.class);
        Object typeHandler2 = mock(Object.class);
        ReferenceType referenceType2 = new ReferenceType(cls2, refType2, valueHandler2, typeHandler2, false);

        Class<String> cls3 = String.class;
        JavaType refType3 = TypeFactory.defaultInstance().constructType(int.class);
        Object valueHandler3 = mock(Object.class);
        Object typeHandler3 = mock(Object.class);
        ReferenceType referenceType3 = new ReferenceType(cls3, refType3, valueHandler3, typeHandler3, true);

        assertEquals(referenceType1, referenceType1);
        assertTrue(referenceType1.equals(referenceType1));

        assertFalse(referenceType1.equals(referenceType2));
        assertFalse(referenceType1.equals(referenceType3));

        ReferenceType newReferenceType1 = referenceType1.withTypeHandler(typeHandler2);
        assertEquals(referenceType1, newReferenceType1);
        assertTrue(referenceType1.equals(newReferenceType1));
    }
}