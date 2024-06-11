package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.*;

import java.util.Arrays;

import org.junit.Test;

public class SimpleTypeTest {

    @Test
    public void testConstructUnsafe() {
        Class<?> cls = String.class;
        SimpleType type = SimpleType.constructUnsafe(cls);
        assertEquals(cls, type.getRawClass());
        assertNull(type.getSuperClass());
        assertNull(type.getSuperInterfaces());
        assertFalse(type.isContainerType());
        assertEquals(0, type.getContentTypeHandler().hashCode());
        assertEquals(0, type.getTypeHandler().hashCode());
        assertEquals(0, type.getValueHandler().hashCode());
    }

    @Test
    public void testConstruct() {
        Class<?> cls = String.class;
        SimpleType type = SimpleType.construct(cls);
        assertEquals(cls, type.getRawClass());
        assertNull(type.getSuperClass());
        assertNull(type.getSuperInterfaces());
        assertFalse(type.isContainerType());
        assertEquals(0, type.getContentTypeHandler().hashCode());
        assertEquals(0, type.getTypeHandler().hashCode());
        assertEquals(0, type.getValueHandler().hashCode());

        try {
            SimpleType mapType = SimpleType.construct(HashMap.class);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testNarrow() {
        Class<?> stringClass = String.class;
        Class<?> stringBufferClass = StringBuffer.class;
        SimpleType stringType = new SimpleType(stringClass);
        SimpleType stringBufferType = new SimpleType(stringBufferClass);

        SimpleType narrowed = stringType._narrow(StringBuffer.class);
        assertEquals(stringBufferType, narrowed);

        narrowed = stringBufferType._narrow(String.class);
        assertEquals(stringType, narrowed);

        try {
            narrowed = stringType._narrow(ArrayList.class);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testIsContainerType() {
        assertTrue(new SimpleType(ArrayList.class).isContainerType());
        assertFalse(new SimpleType(String.class).isContainerType());
    }

    @Test
    public void testGetErasedSignature() {
        Class<?> cls = String.class;
        SimpleType type = new SimpleType(cls);
        StringBuilder sb = new StringBuilder();
        type.getErasedSignature(sb);
        assertEquals("Ljava/lang/String;", sb.toString());
    }

    @Test
    public void testGetGenericSignature() {
        Class<?> cls = String.class;
        SimpleType type = new SimpleType(cls);
        StringBuilder sb = new StringBuilder();
        type.getGenericSignature(sb);
        assertEquals("Ljava/lang/String;", sb.toString());

        cls = HashMap.class;
        type = new SimpleType(cls);
        sb = new StringBuilder();
        type.getGenericSignature(sb);
        assertEquals("Ljava/util/Map<Ljava/lang/Object;Ljava/lang/Object;>;", sb.toString());
    }

    @Test
    public void testToString() {
        Class<?> cls = String.class;
        SimpleType type = new SimpleType(cls);
        assertEquals("[simple type, class " + cls.getName() + "]", type.toString());
    }

    @Test
    public void testEquals() {
        Class<?> cls1 = String.class;
        Class<?> cls2 = StringBuffer.class;
        SimpleType type1 = new SimpleType(cls1);
        SimpleType type2 = new SimpleType(cls2);
        assertFalse(type1.equals(type2));

        SimpleType type3 = new SimpleType(cls1);
        assertTrue(type1.equals(type3));
    }
}