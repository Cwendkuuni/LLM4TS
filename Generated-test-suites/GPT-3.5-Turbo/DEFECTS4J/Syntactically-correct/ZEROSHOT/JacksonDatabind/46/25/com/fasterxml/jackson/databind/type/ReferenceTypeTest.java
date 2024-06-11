package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.*;

import com.fasterxml.jackson.databind.JavaType;
import org.junit.Test;

public class ReferenceTypeTest {

    @Test
    public void testConstruct() {
        Class<?> cls = null;
        JavaType refType = null;
        Object valueHandler = null;
        Object typeHandler = null;
        ReferenceType result = ReferenceType.construct(cls, refType, valueHandler, typeHandler);
        assertNotNull(result);
    }

    @Test
    public void testWithTypeHandler() {
        Object h = null;
        ReferenceType instance = new ReferenceType(null, null, null, null, false);
        ReferenceType result = instance.withTypeHandler(h);
        assertNotNull(result);
    }

    @Test
    public void testWithContentTypeHandler() {
        Object h = null;
        ReferenceType instance = new ReferenceType(null, null, null, null, false);
        ReferenceType result = instance.withContentTypeHandler(h);
        assertNotNull(result);
    }

    @Test
    public void testWithValueHandler() {
        Object h = null;
        ReferenceType instance = new ReferenceType(null, null, null, null, false);
        ReferenceType result = instance.withValueHandler(h);
        assertNotNull(result);
    }

    @Test
    public void testWithContentValueHandler() {
        Object h = null;
        ReferenceType instance = new ReferenceType(null, null, null, null, false);
        ReferenceType result = instance.withContentValueHandler(h);
        assertNotNull(result);
    }

    @Test
    public void testWithStaticTyping() {
        ReferenceType instance = new ReferenceType(null, null, null, null, false);
        ReferenceType result = instance.withStaticTyping();
        assertNotNull(result);
    }

    @Test
    public void testBuildCanonicalName() {
        ReferenceType instance = new ReferenceType(null, null, null, null, false);
        String result = instance.buildCanonicalName();
        assertNotNull(result);
    }

    @Test
    public void test_narrow() {
        Class<?> subclass = null;
        ReferenceType instance = new ReferenceType(null, null, null, null, false);
        JavaType result = instance._narrow(subclass);
        assertNotNull(result);
    }

    @Test
    public void testGetReferencedType() {
        ReferenceType instance = new ReferenceType(null, null, null, null, false);
        JavaType result = instance.getReferencedType();
        assertNotNull(result);
    }

    @Test
    public void testIsReferenceType() {
        ReferenceType instance = new ReferenceType(null, null, null, null, false);
        boolean result = instance.isReferenceType();
        assertTrue(result);
    }

    @Test
    public void testContainedTypeCount() {
        ReferenceType instance = new ReferenceType(null, null, null, null, false);
        int result = instance.containedTypeCount();
        assertEquals(1, result);
    }

    @Test
    public void testContainedType() {
        int index = 0;
        ReferenceType instance = new ReferenceType(null, null, null, null, false);
        JavaType result = instance.containedType(index);
        assertNotNull(result);
    }

    @Test
    public void testContainedTypeName() {
        int index = 0;
        ReferenceType instance = new ReferenceType(null, null, null, null, false);
        String result = instance.containedTypeName(index);
        assertNotNull(result);
    }

    @Test
    public void testGetParameterSource() {
        ReferenceType instance = new ReferenceType(null, null, null, null, false);
        Class<?> result = instance.getParameterSource();
        assertNotNull(result);
    }

    @Test
    public void testGetErasedSignature() {
        StringBuilder sb = null;
        ReferenceType instance = new ReferenceType(null, null, null, null, false);
        StringBuilder result = instance.getErasedSignature(sb);
        assertNotNull(result);
    }

    @Test
    public void testGetGenericSignature() {
        StringBuilder sb = null;
        ReferenceType instance = new ReferenceType(null, null, null, null, false);
        StringBuilder result = instance.getGenericSignature(sb);
        assertNotNull(result);
    }

    @Test
    public void testToString() {
        ReferenceType instance = new ReferenceType(null, null, null, null, false);
        String result = instance.toString();
        assertNotNull(result);
    }

    @Test
    public void testEquals() {
        Object o = null;
        ReferenceType instance = new ReferenceType(null, null, null, null, false);
        boolean result = instance.equals(o);
        assertFalse(result);
    }

}