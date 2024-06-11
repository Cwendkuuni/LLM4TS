package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.TypeResolverBuilder;
import com.fasterxml.jackson.databind.type.SimpleType;

import org.junit.Test;

public class ReferenceTypeTest {

    @Test
    public void testConstruct() {
        Class<String> stringClass = String.class;
        JavaType refType = SimpleType.construct(String.class, TypeResolverBuilder.base());
        ReferenceType referenceType = ReferenceType.construct(stringClass, refType, null);
        assertEquals(stringClass, referenceType.getRawClass());
        assertEquals(refType.getClass(), referenceType.getContentType().getClass());
        assertNull(referenceType.getValueHandler());
        assertNull(referenceType.getTypeHandler());
        assertFalse(referenceType.isStatic());
    }

    @Test
    public void testWithTypeHandler() {
        Class<String> stringClass = String.class;
        JavaType refType = SimpleType.construct(String.class, TypeResolverBuilder.base());
        Object typeHandler = new Object();
        ReferenceType referenceType = new ReferenceType(stringClass, refType, null, null, false);
        ReferenceType newReferenceType = referenceType.withTypeHandler(typeHandler);
        assertNotSame(referenceType, newReferenceType);
        assertSame(referenceType.getRawClass(), newReferenceType.getRawClass());
        assertSame(referenceType.getContentType().getClass(), newReferenceType.getContentType().getClass());
        assertSame(typeHandler, newReferenceType.getTypeHandler());
        assertNull(newReferenceType.getValueHandler());
        assertFalse(newReferenceType.isStatic());
    }

    @Test
    public void testGetReferencedType() {
        Class<String> stringClass = String.class;
        JavaType refType = SimpleType.construct(String.class, TypeResolverBuilder.base());
        ReferenceType referenceType = new ReferenceType(stringClass, refType, null, null, false);
        JavaType referencedType = referenceType.getReferencedType();
        assertEquals(refType.getClass(), referencedType.getClass());
    }

    @Test
    public void testIsReferenceType() {
        Class<String> stringClass = String.class;
        JavaType refType = SimpleType.construct(String.class, TypeResolverBuilder.base());
        ReferenceType referenceType = new ReferenceType(stringClass, refType, null, null, false);
        assertTrue(referenceType.isReferenceType());
    }

    @Test
    public void testGetRawClass() {
        Class<String> stringClass = String.class;
        JavaType refType = SimpleType.construct(String.class, TypeResolverBuilder.base());
        ReferenceType referenceType = new ReferenceType(stringClass, refType, null, null, false);
        assertEquals(stringClass, referenceType.getRawClass());
    }

    @Test
    public void testContainedTypeCount() {
        Class<String> stringClass = String.class;
        JavaType refType = SimpleType.construct(String.class, TypeResolverBuilder.base());
        ReferenceType referenceType = new ReferenceType(stringClass, refType, null, null, false);
        assertEquals(1, referenceType.containedTypeCount());
    }

    @Test
    public void testContainedType() {
        Class<String> stringClass = String.class;
        JavaType refType = SimpleType.construct(String.class, TypeResolverBuilder.base());
        ReferenceType referenceType = new ReferenceType(stringClass, refType, null, null, false);
        JavaType containedType = referenceType.containedType(0);
        assertEquals(refType, containedType);
    }

    @Test
    public void testContainedTypeName() {
        Class<String> stringClass = String.class;
        JavaType refType = SimpleType.construct(String.class, TypeResolverBuilder.base());
        ReferenceType referenceType = new ReferenceType(stringClass, refType, null, null, false);
        assertEquals("T", referenceType.containedTypeName(0));
    }

    @Test
    public void testGetParameterSource() {
        Class<String> stringClass = String.class;
        JavaType refType = SimpleType.construct(String.class, TypeResolverBuilder.base());
        ReferenceType referenceType = new ReferenceType(stringClass, refType, null, null, false);
        assertEquals(stringClass, referenceType.getParameterSource());
    }

    @Test
    public void testGetErasedSignature()
    {
        Class<String> stringClass = String.class;
        JavaType refType = SimpleType.construct(String.class, TypeResolverBuilder.base());
        ReferenceType referenceType = new ReferenceType(stringClass, refType, null, null, false);
        StringBuilder sb = new StringBuilder();
        referenceType.getErasedSignature(sb);
        assertEquals("Ljava/lang/String;", sb.toString());
    }

    @Test
    public void testGetGenericSignature()
    {
        Class<String> stringClass = String.class;
        JavaType refType = SimpleType.construct(String.class, TypeResolverBuilder.base());
        ReferenceType referenceType = new ReferenceType(stringClass, refType, null, null, false);
        StringBuilder sb = new StringBuilder();
        referenceType.getGenericSignature(sb);
        assertEquals("[Ljava/lang/String;<T>]", sb.toString());
    }

    @Test
    public void testToString() {
        Class<String> stringClass = String.class;
        JavaType refType = SimpleType.construct(String.class, TypeResolverBuilder.base());
        ReferenceType referenceType = new ReferenceType(stringClass, refType, null, null, false);
        String expected = "[reference type, class " + stringClass.getName() + "<T>"
                + "[" + refType + "]>";
        String actual = referenceType.toString();
        assertEquals(expected, actual);
    }

    @Test
    public void testEquals() {
        Class<String> stringClass = String.class;
        JavaType refType = SimpleType.construct(String.class, TypeResolverBuilder.base());
        ReferenceType referenceType1 = new ReferenceType(stringClass, refType, null, null, false);
        ReferenceType referenceType2 = new ReferenceType(stringClass, refType, null, null, false);
        JavaType refType2 = SimpleType.construct(String.class, TypeResolverBuilder.base());
        ReferenceType referenceType3 = new ReferenceType(stringClass, refType2, null, null, false);
        assertTrue(referenceType1.equals(referenceType1));
        assertTrue(referenceType1.equals(referenceType2));
        assertFalse(referenceType1.equals(referenceType3));
        assertFalse(referenceType1.equals(null));
        assertFalse(referenceType1.equals("foo"));
    }
}