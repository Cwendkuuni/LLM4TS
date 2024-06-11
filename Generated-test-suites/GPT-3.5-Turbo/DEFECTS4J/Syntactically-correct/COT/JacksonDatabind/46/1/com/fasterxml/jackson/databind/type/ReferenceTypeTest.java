
package com.fasterxml.jackson.databind.type;

import com.fasterxml.jackson.databind.JavaType;
import org.junit.Test;

import static org.junit.Assert.*;

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
    public void test_getReferencedType() {
        ReferenceType instance = new ReferenceType(null, null, null, null, false);
        JavaType expResult = null;
        JavaType result = instance.getReferencedType();
        assertEquals(expResult, result);
    }

    @Test
    public void test_isReferenceType() {
        ReferenceType instance = new ReferenceType(null, null, null, null, false);
        boolean expResult = true;
        boolean result = instance.isReferenceType();
        assertEquals(expResult, result);
    }

    @Test
    public void test_containedTypeCount() {
        ReferenceType instance = new ReferenceType(null, null, null, null, false);
        int expResult = 1;
        int result = instance.containedTypeCount();
        assertEquals(expResult, result);
    }

    @Test
    public void test_containedType() {
        int index = 0;
        ReferenceType instance = new ReferenceType(null, null, null, null, false);
        JavaType expResult = null;
        JavaType result = instance.containedType(index);
        assertEquals(expResult, result);
    }

    @Test
    public void test_containedTypeName() {
        int index = 0;
        ReferenceType instance = new ReferenceType(null, null, null, null, false);
        String expResult = "T";
        String result = instance.containedTypeName(index);
        assertEquals(expResult, result);
    }

    @Test
    public void test_getParameterSource() {
        ReferenceType instance = new ReferenceType(null, null, null, null, false);
        Class<?> expResult = null;
        Class<?> result = instance.getParameterSource();
        assertEquals(expResult, result);
    }

    @Test
    public void test_getErasedSignature() {
        StringBuilder sb = null;
        ReferenceType instance = new ReferenceType(null, null, null, null, false);
        StringBuilder expResult = null;
        StringBuilder result = instance.getErasedSignature(sb);
        assertEquals(expResult, result);
    }

    @Test
    public void test_getGenericSignature() {
        StringBuilder sb = null;
        ReferenceType instance = new ReferenceType(null, null, null, null, false);
        StringBuilder expResult = null;
        StringBuilder result = instance.getGenericSignature(sb);
        assertEquals(expResult, result);
    }

    @Test
    public void testToString() {
        ReferenceType instance = new ReferenceType(null, null, null, null, false);
        String expResult = "[reference type, class null<null>]";
        String result = instance.toString();
        assertEquals(expResult, result);
    }

    @Test
    public void testEquals() {
        Object o = null;
        ReferenceType instance = new ReferenceType(null, null, null, null, false);
        boolean expResult = false;
        boolean result = instance.equals(o);
        assertEquals(expResult, result);
    }
}
