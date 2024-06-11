package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.*;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.TypeFactory;
import org.junit.Test;

public class ReferenceTypeTest {

    @Test
    public void testConstruct() {
        JavaType refType = TypeFactory.defaultInstance().constructType(int[].class);
        ReferenceType referenceType = ReferenceType.construct(String.class, refType, null, null);
        assertEquals(String.class, referenceType.getRawClass());
        assertEquals(int[].class, referenceType.getContentType().getType());
    }

    @Test
    public void testWithTypeHandler() {
        JavaType refType = TypeFactory.defaultInstance().constructType(int[].class);
        ReferenceType referenceType = new ReferenceType(String.class, refType, null, null, false);
        assertEquals(String.class, referenceType.getRawClass());
        assertEquals(int[].class, referenceType.getContentType().getType());

        JavaType newTypeHandler = TypeFactory.defaultInstance().constructType(String.class);
        ReferenceType newReferenceType = referenceType.withTypeHandler(newTypeHandler);
        assertEquals(String.class, newReferenceType.getRawClass());
        assertEquals(int[].class, newReferenceType.getContentType().getType());
        assertNotSame(refType, newReferenceType.getContentType());
        assertEquals(newTypeHandler, newReferenceType.getContentTypeHandler());
    }

    @Test
    public void testWithContentTypeHandler() {
        JavaType refType = TypeFactory.defaultInstance().constructType(int[].class);
        ReferenceType referenceType = new ReferenceType(String.class, refType, null, null, false);
        assertEquals(String.class, referenceType.getRawClass());
        assertEquals(int[].class, referenceType.getContentType().getType());

        JavaType newTypeHandler = TypeFactory.defaultInstance().constructType(String.class);
        ReferenceType newReferenceType = referenceType.withContentTypeHandler(newTypeHandler);
        assertEquals(String.class, newReferenceType.getRawClass());
        assertEquals(int[].class, newReferenceType.getContentType().getType());
        assertNotSame(refType, newReferenceType.getContentType());
        assertEquals(newTypeHandler, newReferenceType.getContentTypeHandler());
    }

    @Test
    public void testWithValueHandler() {
        JavaType refType = TypeFactory.defaultInstance().constructType(int[].class);
        ReferenceType referenceType = new ReferenceType(String.class, refType, null, null, false);
        assertEquals(String.class, referenceType.getRawClass());
        assertEquals(int[].class, referenceType.getContentType().getType());

        Object newValueHandler = new Object();
        ReferenceType newReferenceType = referenceType.withValueHandler(newValueHandler);
        assertEquals(String.class, newReferenceType.getRawClass());
        assertEquals(int[].class, newReferenceType.getContentType().getType());
        assertNotSame(referenceType, newReferenceType);
        assertEquals(newValueHandler, newReferenceType.getValueHandler());
    }

    @Test
    public void testWithContentValueHandler() {
        JavaType refType = TypeFactory.defaultInstance().constructType(int[].class);
        ReferenceType referenceType = new ReferenceType(String.class, refType, null, null, false);
        assertEquals(String.class, referenceType.getRawClass());
        assertEquals(int[].class, referenceType.getContentType().getType());

        Object newContentValueHandler = new Object();
        ReferenceType newReferenceType = referenceType.withContentValueHandler(newContentValueHandler);
        assertEquals(String.class, newReferenceType.getRawClass());
        assertEquals(int[].class, newReferenceType.getContentType().getType());
        assertNotSame(referenceType, newReferenceType);
        assertEquals(newContentValueHandler, newReferenceType.getContentValueHandler());
    }

    @Test
    public void testWithStaticTyping() {
        JavaType refType = TypeFactory.defaultInstance().constructType(int[].class);
        ReferenceType referenceType = new ReferenceType(String.class, refType, null, null, false);
        assertFalse(referenceType.isStatic());

        ReferenceType newReferenceType = referenceType.withStaticTyping();
        assertTrue(newReferenceType.isStatic());
    }

    @Test
    public void testBuildCanonicalName() {
        JavaType refType = TypeFactory.defaultInstance().constructType(int[].class);
        ReferenceType referenceType = new ReferenceType(String.class, refType, null, null, false);
        assertEquals("[reference type, class java.lang.String<[I>]", referenceType.buildCanonicalName());
    }

    @Test
    public void testNarrow() {
        JavaType refType = TypeFactory.defaultInstance().constructType(int[].class);
        ReferenceType referenceType = new ReferenceType(String.class, refType, null, null, false);
        JavaType narrowedType = referenceType._narrow(Object.class);
        assertEquals(ReferenceType.class, narrowedType.getClass());
        assertEquals(String.class, narrowedType.getRawClass());
        assertEquals(int[].class, narrowedType.getContentType().getType());
    }

    @Test
    public void testGetReferencedType() {
        JavaType refType = TypeFactory.defaultInstance().constructType(int[].class);
        ReferenceType referenceType = new ReferenceType(String.class, refType, null, null, false);
        assertEquals(refType, referenceType.getReferencedType());
    }

    @Test
    public void testIsReferenceType() {
        JavaType refType = TypeFactory.defaultInstance().constructType(int[].class);
        ReferenceType referenceType = new ReferenceType(String.class, refType, null, null, false);
        assertTrue(referenceType.isReferenceType());
    }

    @Test
    public void testContainedTypeCount() {
        JavaType refType = TypeFactory.defaultInstance().constructType(int[].class);
        ReferenceType referenceType = new ReferenceType(String.class, refType, null, null, false);
        assertEquals(1, referenceType.containedTypeCount());
    }

    @Test
    public void testContainedType() {
        JavaType refType = TypeFactory.defaultInstance().constructType(int[].class);
        ReferenceType referenceType = new ReferenceType(String.class, refType, null, null, false);
        assertEquals(refType, referenceType.containedType(0));
    }

    @Test
    public void testContainedTypeName() {
        JavaType refType = TypeFactory.defaultInstance().constructType(int[].class);
        ReferenceType referenceType = new ReferenceType(String.class, refType, null, null, false);
        assertEquals("T", referenceType.containedTypeName(0));
    }

    @Test
    public void testGetParameterSource() {
        JavaType refType = TypeFactory.defaultInstance().constructType(int[].class);
        ReferenceType referenceType = new ReferenceType(String.class, refType, null, null, false);
        assertEquals(String.class, referenceType.getParameterSource());
    }

    @Test
    public void testGetErasedSignature() {
        JavaType refType = TypeFactory.defaultInstance().constructType(int[].class);
        ReferenceType referenceType = new ReferenceType(String.class, refType, null, null, false);
        StringBuilder sb = new StringBuilder();
        referenceType.getErasedSignature(sb);
        assertEquals("[Ljava/lang/Object;", sb.toString());
    }

    @Test
    public void testGetGenericSignature() {
        JavaType refType = TypeFactory.defaultInstance().constructType(int[].class);
        ReferenceType referenceType = new ReferenceType(String.class, refType, null, null, false);
        StringBuilder sb = new StringBuilder();
        referenceType.getGenericSignature(sb);
        assertEquals("[Ljava/lang/String;<[I>", sb.toString());
    }

    @Test
    public void testToString() {
        JavaType refType = TypeFactory.defaultInstance().constructType(int[].class);
        ReferenceType referenceType = new ReferenceType(String.class, refType, null, null, false);
        assertEquals("[reference type, class java.lang.String<[I>]", referenceType.toString());
    }

    @Test
    public void testEquals() {
        JavaType refType = TypeFactory.defaultInstance().constructType(int[].class);
        ReferenceType referenceType1 = new ReferenceType(String.class, refType, null, null, false);
        ReferenceType referenceType2 = new ReferenceType(String.class, refType, null, null, false);
        JavaType refType3 = TypeFactory.defaultInstance().constructType(String[].class);
        ReferenceType referenceType3 = new ReferenceType(String.class, refType3, null, null, false);

        assertTrue(referenceType1.equals(referenceType1));
        assertTrue(referenceType1.equals(referenceType2));
        assertFalse(referenceType1.equals(referenceType3));
    }
}