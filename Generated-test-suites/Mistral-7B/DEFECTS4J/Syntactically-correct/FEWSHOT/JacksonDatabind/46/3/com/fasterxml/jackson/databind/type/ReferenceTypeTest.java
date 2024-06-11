package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.*;

import com.fasterxml.jackson.databind.JavaType;
import org.junit.Test;

public class ReferenceTypeTest {

    @Test
    public void testConstruct() {
        JavaType refType = new SimpleType(Integer.class);
        ReferenceType referenceType = ReferenceType.construct(String.class, refType, null, null);

        assertEquals(String.class, referenceType.getClass());
        assertEquals(Integer.class, referenceType.getContentType().getType());
    }

    @Test
    public void testGetReferencedType() {
        JavaType refType = new SimpleType(Integer.class);
        ReferenceType referenceType = new ReferenceType(String.class, refType, null, null, false);

        assertEquals(refType, referenceType.getReferencedType());
    }

    @Test
    public void testIsReferenceType() {
        JavaType refType = new SimpleType(Integer.class);
        ReferenceType referenceType = new ReferenceType(String.class, refType, null, null, false);

        assertTrue(referenceType.isReferenceType());
    }

    @Test
    public void testContainedTypeCount() {
        JavaType refType = new SimpleType(Integer.class);
        ReferenceType referenceType = new ReferenceType(String.class, refType, null, null, false);

        assertEquals(1, referenceType.containedTypeCount());
    }

    @Test
    public void testContainedType() {
        JavaType refType = new SimpleType(Integer.class);
        ReferenceType referenceType = new ReferenceType(String.class, refType, null, null, false);

        assertEquals(refType, referenceType.containedType(0));
    }

    @Test
    public void testGetParameterSource() {
        JavaType refType = new SimpleType(Integer.class);
        ReferenceType referenceType = new ReferenceType(String.class, refType, null, null, false);

        assertEquals(String.class, referenceType.getParameterSource());
    }

    @Test
    public void testGetErasedSignature() {
        JavaType refType = new SimpleType(Integer.class);
        ReferenceType referenceType = new ReferenceType(String.class, refType, null, null, false);

        StringBuilder expected = new StringBuilder("[Ljava/lang/String;");
        StringBuilder actual = referenceType.getErasedSignature(new StringBuilder());

        assertEquals(expected.toString(), actual.toString());
    }

    @Test
    public void testGetGenericSignature() {
        JavaType refType = new SimpleType(Integer.class);
        ReferenceType referenceType = new ReferenceType(String.class, refType, null, null, false);

        StringBuilder expected = new StringBuilder("[Ljava/lang/String;<T extends java.lang.Object>");
        StringBuilder actual = referenceType.getGenericSignature(new StringBuilder());

        assertEquals(expected.toString(), actual.toString());
    }

    @Test
    public void testToString() {
        JavaType refType = new SimpleType(Integer.class);
        ReferenceType referenceType = new ReferenceType(String.class, refType, null, null, false);

        String expected = "[reference type, class [Ljava.lang.String;<T extends java.lang.Object>> of class java.lang.String, referenced type, class int]";
        String actual = referenceType.toString();

        assertEquals(expected, actual);
    }

    @Test
    public void testEquals() {
        JavaType refType1 = new SimpleType(Integer.class);
        JavaType refType2 = new SimpleType(Integer.class);
        ReferenceType referenceType1 = new ReferenceType(String.class, refType1, null, null, false);
        ReferenceType referenceType2 = new ReferenceType(String.class, refType2, null, null, false);

        JavaType refType3 = new SimpleType(Long.class);
        ReferenceType referenceType3 = new ReferenceType(String.class, refType3, null, null, false);

        assertEquals(referenceType1, referenceType1);
        assertEquals(referenceType1, referenceType1);
        assertNotEquals(referenceType1, referenceType2);
        assertNotEquals(referenceType1, referenceType3);
        assertNotEquals(referenceType1, null);
    }

    @Test
    public void testNarrow() {
        JavaType refType = new SimpleType(Integer.class);
        JavaType subClass = new SimpleType(Long.class);
        ReferenceType referenceType = new ReferenceType(String.class, refType, null, null, false);

        JavaType narrowed = referenceType.narrow(subClass.getType());

        assertEquals(subClass, narrowed.getContentType());
    }

    @Test
    public void testWithTypeHandler() {
        JavaType refType = new SimpleType(Integer.class);
        Object handler = new Object();
        ReferenceType referenceType = new ReferenceType(String.class, refType, null, null, false);

        ReferenceType withHandler = referenceType.withTypeHandler(handler);

        assertEquals(referenceType.getClass(), withHandler.getClass());
        assertNotEquals(referenceType._typeHandler, withHandler._typeHandler);
    }

    @Test
    public void testWithContentTypeHandler() {
        JavaType refType = new SimpleType(Integer.class);
        Object handler = new Object();
        ReferenceType referenceType = new ReferenceType(String.class, refType, null, null, false);

        ReferenceType withHandler = referenceType.withContentTypeHandler(handler);

        assertEquals(referenceType.getClass(), withHandler.getClass());
        assertNotEquals(referenceType._referencedType._typeHandler, withHandler._referencedType._typeHandler);
    }

    @Test
    public void testWithValueHandler() {
        JavaType refType = new SimpleType(Integer.class);
        Object handler = new Object();
        ReferenceType referenceType = new ReferenceType(String.class, refType, null, null, false);

        ReferenceType withHandler = referenceType.withValueHandler(handler);

        assertEquals(referenceType.getClass(), withHandler.getClass());
        assertNotEquals(referenceType._valueHandler, withHandler._valueHandler);
    }

    @Test
    public void testWithContentValueHandler() {
        JavaType refType = new SimpleType(Integer.class);
        Object handler = new Object();
        ReferenceType referenceType = new ReferenceType(String.class, refType, null, null, false);

        ReferenceType withHandler = referenceType.withContentValueHandler(handler);

        assertEquals(referenceType.getClass(), withHandler.getClass());
        assertNotEquals(referenceType._referencedType._valueHandler, withHandler._referencedType._valueHandler);
    }

    @Test
    public void testWithStaticTyping() {
        JavaType refType = new SimpleType(Integer.class);
        ReferenceType referenceType = new ReferenceType(String.class, refType, null, null, false);

        ReferenceType withStaticTyping = referenceType.withStaticTyping();

        assertEquals(referenceType.getClass(), withStaticTyping.getClass());
        assertTrue(withStaticTyping._asStatic);
    }
}