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
        assertEquals(refType.getClass(), referenceType._referencedType.getClass());
    }

    @Test
    public void testGetReferencedType() {
        Class<String> cls = String.class;
        JavaType refType = JavaType.construct(int.class);
        ReferenceType referenceType = new ReferenceType(cls, refType, null, null, false);
        assertEquals(refType, referenceType.getReferencedType());
        assertEquals(true, referenceType.isReferenceType());
    }

    @Test
    public void testWithTypeHandler() {
        Class<String> cls = String.class;
        JavaType refType = JavaType.construct(int.class);
        Object handler = new Object();
        ReferenceType referenceType = new ReferenceType(cls, refType, null, null, false);
        ReferenceType newReferenceType = referenceType.withTypeHandler(handler);
        assertNotEquals(referenceType, newReferenceType);
        assertEquals(handler, newReferenceType._typeHandler);
    }

    @Test
    public void testWithContentTypeHandler() {
        Class<String> cls = String.class;
        JavaType refType = JavaType.construct(int.class);
        Object handler = new Object();
        ReferenceType referenceType = new ReferenceType(cls, refType, null, null, false);
        ReferenceType newReferenceType = referenceType.withContentTypeHandler(handler);
        assertNotEquals(referenceType, newReferenceType);
        assertEquals(handler, newReferenceType._referencedType.<Object>getTypeHandler());
    }

    @Test
    public void testWithValueHandler() {
        Class<String> cls = String.class;
        JavaType refType = JavaType.construct(int.class);
        Object handler = new Object();
        ReferenceType referenceType = new ReferenceType(cls, refType, null, null, false);
        ReferenceType newReferenceType = referenceType.withValueHandler(handler);
        assertNotEquals(referenceType, newReferenceType);
        assertEquals(handler, newReferenceType._valueHandler);
    }

    @Test
    public void testWithContentValueHandler() {
        Class<String> cls = String.class;
        JavaType refType = JavaType.construct(int.class);
        Object handler = new Object();
        ReferenceType referenceType = new ReferenceType(cls, refType, null, null, false);
        ReferenceType newReferenceType = referenceType.withContentValueHandler(handler);
        assertNotEquals(referenceType, newReferenceType);
        assertEquals(handler, newReferenceType._referencedType.<Object>getValueHandler());
    }

    @Test
    public void testWithStaticTyping() {
        Class<String> cls = String.class;
        JavaType refType = JavaType.construct(int.class);
        ReferenceType referenceType = new ReferenceType(cls, refType, null, null, false);
        ReferenceType newReferenceType = referenceType.withStaticTyping();
        assertNotEquals(referenceType, newReferenceType);
        assertEquals(true, newReferenceType._asStatic);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNarrowWithNoSubClassRelationship() {
        Class<String> cls = String.class;
        JavaType refType = JavaType.construct(int[].class);
        ReferenceType referenceType = new ReferenceType(cls, refType, null, null, false);
        referenceType._narrow(cls.getClass());
    }

    @Test
    public void testContainsType() {
        Class<String> cls = String.class;
        JavaType refType = JavaType.construct(int.class);
        ReferenceType referenceType = new ReferenceType(cls, refType, null, null, false);
        assertEquals(1, referenceType.containedTypeCount());
        JavaType containedType = referenceType.containedType(0);
        assertEquals(refType, containedType);
    }

    @Test
    public void testToString() {
        Class<String> cls = String.class;
        JavaType refType = JavaType.construct(int.class);
        ReferenceType referenceType = new ReferenceType(cls, refType, null, null, false);
        String result = referenceType.toString();
        assertTrue(result.contains("reference type, class"));
        assertTrue(result.contains(cls.getName()));
        assertTrue(result.contains("[reference type, class"));
        assertTrue(result.contains("<int>]"));
    }

    @Test
    public void testEquals() {
        Class<String> cls1 = String.class;
        JavaType refType1 = JavaType.construct(int.class);
        ReferenceType referenceType1 = new ReferenceType(cls1, refType1, null, null, false);

        Class<String> cls2 = String.class;
        JavaType refType2 = JavaType.construct(int.class);
        ReferenceType referenceType2 = new ReferenceType(cls2, refType2, null, null, false);

        Class<String> cls3 = String.class;
        JavaType refType3 = JavaType.construct(int[].class);
        ReferenceType referenceType3 = new ReferenceType(cls3, refType3, null, null, false);

        assertEquals(referenceType1, referenceType1);
        assertEquals(referenceType1, referenceType1);
        assertNotEquals(referenceType1, referenceType2);
        assertNotEquals(referenceType1, referenceType3);
        assertNotEquals(referenceType2, referenceType3);

        ReferenceType newReferenceType1 = new ReferenceType(cls1, refType1, null, null, false);
        assertEquals(referenceType1, newReferenceType1);
    }

    @Test
    public void testBuildCanonicalName() {
        Class<String> cls = String.class;
        JavaType refType = JavaType.construct(int.class);
        ReferenceType referenceType = new ReferenceType(cls, refType, null, null, false);
        String result = referenceType.buildCanonicalName();
        assertTrue(result.contains("class"));
        assertTrue(result.contains(cls.getName()));
        assertTrue(result.contains("<int>"));
    }
}