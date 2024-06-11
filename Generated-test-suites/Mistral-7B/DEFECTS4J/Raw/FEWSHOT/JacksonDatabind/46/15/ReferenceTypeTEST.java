package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import com.fasterxml.jackson.core.type.ClassType;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;

import org.junit.Test;

public class ReferenceTypeTest {

    @Test
    public void testConstruct() {
        Class<?> cls = String.class;
        JavaType refType = ClassType.construct(Integer.class);

        ReferenceType referenceType = ReferenceType.construct(cls, refType, null, null);
        assertEquals(cls, referenceType.getRawClass());
        assertEquals(refType.getClass(), referenceType._referencedType.getClass());
        assertEquals(refType, referenceType.getReferencedType());
    }

    @Test
    public void testWithTypeHandler() {
        Class<?> cls = String.class;
        JavaType refType = ClassType.construct(Integer.class);
        TypeHandler<Object> typeHandler = mock(TypeHandler.class);

        ReferenceType referenceType = ReferenceType.construct(cls, refType, null, null);
        ReferenceType newReferenceType = referenceType.withTypeHandler(typeHandler);
        assertSame(referenceType, newReferenceType);

        JavaType newReferencedType = newReferenceType.getReferencedType();
        assertEquals(refType, newReferencedType);
        assertEquals(typeHandler, newReferencedType.<Object>getTypeHandler());
    }

    @Test
    public void testWithContentTypeHandler() {
        Class<?> cls = String.class;
        JavaType refType = ClassType.construct(Integer.class);
        TypeHandler<Object> typeHandler = mock(TypeHandler.class);
        TypeHandler<Object> contentTypeHandler = mock(TypeHandler.class);

        ReferenceType referenceType = ReferenceType.construct(cls, refType, null, null);
        ReferenceType newReferenceType = referenceType.withContentTypeHandler(contentTypeHandler);
        assertSame(referenceType, newReferenceType);

        JavaType newReferencedType = newReferenceType.getReferencedType();
        assertEquals(refType, newReferencedType);
        assertEquals(contentTypeHandler, newReferencedType.<Object>getTypeHandler());
    }

    @Test
    public void testWithValueHandler() {
        Class<?> cls = String.class;
        JavaType refType = ClassType.construct(Integer.class);
        ValueHandler<Object> valueHandler = mock(ValueHandler.class);

        ReferenceType referenceType = ReferenceType.construct(cls, refType, null, null);
        ReferenceType newReferenceType = referenceType.withValueHandler(valueHandler);
        assertSame(referenceType, newReferenceType);

        JavaType newReferencedType = newReferenceType.getReferencedType();
        assertEquals(refType, newReferencedType);
        assertEquals(valueHandler, newReferencedType.<Object>getValueHandler());
    }

    @Test
    public void testWithContentValueHandler() {
        Class<?> cls = String.class;
        JavaType refType = ClassType.construct(Integer.class);
        ValueHandler<Object> contentValueHandler = mock(ValueHandler.class);

        ReferenceType referenceType = ReferenceType.construct(cls, refType, null, null);
        ReferenceType newReferenceType = referenceType.withContentValueHandler(contentValueHandler);
        assertSame(referenceType, newReferenceType);

        JavaType newReferencedType = newReferenceType.getReferencedType();
        assertEquals(refType, newReferencedType);
        assertEquals(contentValueHandler, newReferencedType.<Object>getValueHandler());
    }

    @Test
    public void testWithStaticTyping() {
        Class<?> cls = String.class;
        JavaType refType = ClassType.construct(Integer.class);

        ReferenceType referenceType = new ReferenceType(cls, refType, null, null, false);
        ReferenceType newReferenceType = referenceType.withStaticTyping();
        assertSame(referenceType, newReferenceType);

        // Static typing flag should be set
        assertTrue(newReferenceType._asStatic);
    }

    @Test
    public void testBuildCanonicalName() {
        Class<?> cls = String.class;
        JavaType refType = ClassType.construct(Integer.class);

        String expectedCanonicalName = "[reference type, class java.lang.String<java.lang.Integer>]";
        assertEquals(expectedCanonicalName, new ReferenceType(cls, refType, null, null, false).buildCanonicalName());
    }

    @Test
    public void testNarrow() {
        Class<?> cls = String.class;
        JavaType refType = ClassType.construct(Integer.class);

        // Should be able to narrow to a subclass
        JavaType subClass = ClassType.construct(String.class);
        JavaType narrowedType = new ReferenceType(cls, refType, null, null)._narrow(subClass);
        assertEquals(subClass, narrowedType.getRawClass());
        assertEquals(refType, narrowedType.getReferencedType());
    }

    @Test
    public void testGetReferencedType() {
        Class<?> cls = String.class;
        JavaType refType = ClassType.construct(Integer.class);

        ReferenceType referenceType = new ReferenceType(cls, refType, null, null, false);
        assertEquals(refType, referenceType.getReferencedType());
    }

    @Test
    public void testIsReferenceType() {
        Class<?> cls = String.class;
        JavaType refType = ClassType.construct(Integer.class);

        ReferenceType referenceType = new ReferenceType(cls, refType, null, null, false);
        assertTrue(referenceType.isReferenceType());
    }

    @Test
    public void testContainedTypeCount() {
        Class<?> cls = String.class;
        JavaType refType = ClassType.construct(Integer.class);

        ReferenceType referenceType = new ReferenceType(cls, refType, null, null, false);
        assertEquals(1, referenceType.containedTypeCount());
    }

    @Test
    public void testContainedType() {
        Class<?> cls = String.class;
        JavaType refType = ClassType.construct(Integer.class);

        ReferenceType referenceType = new ReferenceType(cls, refType, null, null, false);
        JavaType containedType = referenceType.containedType(0);
        assertEquals(refType, containedType);
    }

    @Test
    public void testContainedTypeName() {
        Class<?> cls = String.class;
        JavaType refType = ClassType.construct(Integer.class);

        ReferenceType referenceType = new ReferenceType(cls, refType, null, null, false);
        assertEquals("T", referenceType.containedTypeName(0));
    }

    @Test
    public void testGetParameterSource() {
        Class<?> cls = String.class;
        JavaType refType = ClassType.construct(Integer.class);

        ReferenceType referenceType = new ReferenceType(cls, refType, null, null, false);
        assertEquals(cls, referenceType.getParameterSource());
    }

    @Test
    public void testGetErasedSignature() {
        Class<?> cls = String.class;
        JavaType refType = ClassType.construct(Integer.class);

        StringBuilder sb = new StringBuilder();
        ReferenceType referenceType = new ReferenceType(cls, refType, null, null, false);
        referenceType.getErasedSignature(sb);
        assertEquals("[Ljava.lang.String;", sb.toString());
    }

    @Test
    public void testGetGenericSignature() {
        Class<?> cls = String.class;
        JavaType refType = ClassType.construct(Integer.class);

        StringBuilder sb = new StringBuilder();
        ReferenceType referenceType = new ReferenceType(cls, refType, null, null, false);
        referenceType.getGenericSignature(sb);
        assertEquals("[reference type, class java.lang.String<java.lang.Integer>]", sb.toString());
    }

    @Test
    public void testToString() {
        Class<?> cls = String.class;
        JavaType refType = ClassType.construct(Integer.class);

        ReferenceType referenceType = new ReferenceType(cls, refType, null, null, false);
        assertEquals("[reference type, class java.lang.String<java.lang.Integer>]", referenceType.toString());
    }

    @Test
    public void testEquals() {
        Class<?> cls1 = String.class;
        JavaType refType1 = ClassType.construct(Integer.class);
        Class<?> cls2 = Integer.class;
        JavaType refType2 = ClassType.construct(String.class);

        ReferenceType referenceType1 = new ReferenceType(cls1, refType1, null, null, false);
        ReferenceType referenceType2 = new ReferenceType(cls2, refType2, null, null, false);

        assertFalse(referenceType1.equals(referenceType2));

        JavaType sameReferencedType = ClassType.construct(Integer.class);
        ReferenceType sameReferenceType = new ReferenceType(cls1, sameReferencedType, null, null, false);

        assertTrue(referenceType1.equals(sameReferenceType));
    }
}