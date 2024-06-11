package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.*;

import org.junit.Test;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.ReferenceType;

public class ReferenceTypeTest {

    @Test
    public void testReferenceTypeConstructor() {
        Class<String> cls = String.class;
        JavaType refType = JavaType.construct(int.class);
        ReferenceType referenceType = ReferenceType.construct(cls, refType, null, null);
        assertEquals(cls, referenceType.getRawClass());
        assertEquals(refType.getClass().getCanonicalName(), referenceType.getContentType().getClass().getCanonicalName());
    }

    @Test
    public void testGetReferencedType() {
        Class<String> cls = String.class;
        JavaType refType = JavaType.construct(int.class);
        ReferenceType referenceType = new ReferenceType(cls, refType, null, null, false);
        assertEquals(refType, referenceType.getReferencedType());
    }

    @Test
    public void testIsReferenceType() {
        Class<String> cls = String.class;
        JavaType refType = JavaType.construct(int.class);
        ReferenceType referenceType = new ReferenceType(cls, refType, null, null, false);
        assertTrue(referenceType.isReferenceType());
    }

    @Test
    public void testConstructWithTypeHandler() {
        Class<String> cls = String.class;
        JavaType refType = JavaType.construct(int.class);
        Object typeHandler = new Object();
        ReferenceType referenceType = ReferenceType.construct(cls, refType, null, typeHandler);
        assertEquals(typeHandler, referenceType.getTypeHandler());
    }

    @Test
    public void testWithTypeHandler() {
        Class<String> cls = String.class;
        JavaType refType = JavaType.construct(int.class);
        ReferenceType referenceType = new ReferenceType(cls, refType, null, null, false);
        Object newTypeHandler = new Object();
        ReferenceType newReferenceType = referenceType.withTypeHandler(newTypeHandler);
        assertEquals(newTypeHandler, newReferenceType.getTypeHandler());
        assertNotSame(referenceType, newReferenceType);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testWithTypeHandlerWithSameHandler() {
        Class<String> cls = String.class;
        JavaType refType = JavaType.construct(int.class);
        ReferenceType referenceType = new ReferenceType(cls, refType, null, null, false);
        Object typeHandler = referenceType.getTypeHandler();
        referenceType.withTypeHandler(typeHandler);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testWithContentTypeHandlerWithSameHandler() {
        Class<String> cls = String.class;
        JavaType refType = JavaType.construct(int.class);
        ReferenceType referenceType = new ReferenceType(cls, refType, null, null, false);
        Object typeHandler = refType.<Object>getTypeHandler();
        referenceType.withContentTypeHandler(typeHandler);
    }

    @Test
    public void testWithContentTypeHandler() {
        Class<String> cls = String.class;
        JavaType refType = JavaType.construct(int.class);
        Object contentTypeHandler = new Object();
        ReferenceType referenceType = new ReferenceType(cls, refType, null, null, false);
        ReferenceType newReferenceType = referenceType.withContentTypeHandler(contentTypeHandler);
        assertEquals(contentTypeHandler, newReferenceType.<Object>getTypeHandler());
        assertNotSame(referenceType, newReferenceType);
    }

    @Test
    public void testWithValueHandler() {
        Class<String> cls = String.class;
        JavaType refType = JavaType.construct(int.class);
        Object valueHandler = new Object();
        ReferenceType referenceType = new ReferenceType(cls, refType, null, null, false);
        ReferenceType newReferenceType = referenceType.withValueHandler(valueHandler);
        assertEquals(valueHandler, newReferenceType.getValueHandler());
        assertNotSame(referenceType, newReferenceType);
    }

    @Test
    public void testWithContentValueHandler() {
        Class<String> cls = String.class;
        JavaType refType = JavaType.construct(int.class);
        Object contentValueHandler = new Object();
        ReferenceType referenceType = new ReferenceType(cls, refType, null, null, false);
        ReferenceType newReferenceType = referenceType.withContentValueHandler(contentValueHandler);
        assertEquals(contentValueHandler, newReferenceType.<Object>getValueHandler());
        assertNotSame(referenceType, newReferenceType);
    }

    @Test
    public void testWithStaticTyping() {
        Class<String> cls = String.class;
        JavaType refType = JavaType.construct(int.class);
        ReferenceType referenceType = new ReferenceType(cls, refType, null, null, false);
        ReferenceType newReferenceType = referenceType.withStaticTyping();
        assertTrue(newReferenceType.isStatic());
        assertFalse(referenceType.isStatic());
        assertNotSame(referenceType, newReferenceType);
    }

    @Test
    public void testBuildCanonicalName() {
        Class<String> cls = String.class;
        JavaType refType = JavaType.construct(int.class);
        ReferenceType referenceType = new ReferenceType(cls, refType, null, null, false);
        String canonicalName = referenceType.buildCanonicalName();
        String expectedCanonicalName = "[reference type, class java.lang.String<T>, class com.fasterxml.jackson.core.type.TypeReferenceType$1]";
        assertEquals(expectedCanonicalName, canonicalName);
    }

    @Test
    public void testNarrow() {
        Class<String> cls = String.class;
        JavaType refType = JavaType.construct(int[].class);
        ReferenceType referenceType = new ReferenceType(cls, refType, null, null, false);
        JavaType expectedNarrowedType = new ReferenceType(cls, refType, null, null, true);
        JavaType narrowedType = referenceType.narrow(int[].class);
        assertEquals(expectedNarrowedType, narrowedType);
    }

    @Test(expected = ClassCastException.class)
    public void testNarrowWithInvalidSubclass() {
        Class<String> cls = String.class;
        JavaType refType = JavaType.construct(int.class);
        ReferenceType referenceType = new ReferenceType(cls, refType, null, null, false);
        referenceType.narrow(Object.class);
    }
}