package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class SimpleTypeTest {
    @Test
    public void testConstructor() {
        SimpleType simpleType = new SimpleType(String.class);
        assertEquals(String.class, simpleType.getRawClass());
        assertNull(simpleType.getBindings());
        assertNull(simpleType.getSuperClass());
        assertNull(simpleType.getInterfaces());
        assertNull(simpleType.getValueHandler());
        assertNull(simpleType.getTypeHandler());
        assertEquals(false, simpleType.isStatic());
    }

    @Test
    public void testCopyConstructor() {
        TypeBase base = new SimpleType(String.class);
        SimpleType simpleType = new SimpleType(base);
        assertEquals(base.getRawClass(), simpleType.getRawClass());
        assertEquals(base.getBindings(), simpleType.getBindings());
        assertEquals(base.getSuperClass(), simpleType.getSuperClass());
        assertEquals(base.getInterfaces(), simpleType.getInterfaces());
        assertEquals(base.getValueHandler(), simpleType.getValueHandler());
        assertEquals(base.getTypeHandler(), simpleType.getTypeHandler());
        assertEquals(base.isStatic(), simpleType.isStatic());
    }

    @Test
    public void testParameterizedConstructor() {
        TypeBase superClass = new SimpleType(String.class);
        TypeBase[] interfaces = {new SimpleType(Integer.class)};
        Object valueHandler = new Object();
        Object typeHandler = new Object();
        SimpleType simpleType = new SimpleType(String.class, null, superClass, interfaces, valueHandler, typeHandler, true);
        assertEquals(String.class, simpleType.getRawClass());
        assertNull(simpleType.getBindings());
        assertEquals(superClass, simpleType.getSuperClass());
        assertEquals(interfaces[0], simpleType.getInterfaces()[0]);
        assertEquals(valueHandler, simpleType.getValueHandler());
        assertEquals(typeHandler, simpleType.getTypeHandler());
        assertEquals(true, simpleType.isStatic());
    }

    @Test
    public void testUnsafeConstruct() {
        SimpleType simpleType = SimpleType.constructUnsafe(String.class);
        assertEquals(String.class, simpleType.getRawClass());
        assertNull(simpleType.getBindings());
        assertNull(simpleType.getSuperClass());
        assertNull(simpleType.getInterfaces());
        assertNull(simpleType.getValueHandler());
        assertNull(simpleType.getTypeHandler());
        assertEquals(false, simpleType.isStatic());
    }

    @Test
    public void testConstruct() {
        SimpleType simpleType = SimpleType.construct(String.class);
        assertEquals(String.class, simpleType.getRawClass());
        assertNull(simpleType.getBindings());
        assertNull(simpleType.getSuperClass());
        assertNull(simpleType.getInterfaces());
        assertNull(simpleType.getValueHandler());
        assertNull(simpleType.getTypeHandler());
        assertEquals(false, simpleType.isStatic());
    }

    @Test
    public void testNarrow() {
        SimpleType simpleType = new SimpleType(String.class).narrow(Object.class);
        assertEquals(Object.class, simpleType.getRawClass());
        assertEquals(String.class, simpleType.getSuperClass().getRawClass());
        assertEquals(false, simpleType.isStatic());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetContentType() {
        SimpleType simpleType = new SimpleType(String.class);
        simpleType.getContentType();
    }

    @Test
    public void testWithValueHandler() {
        Object valueHandler = new Object();
        SimpleType simpleType = new SimpleType(String.class).withValueHandler(valueHandler);
        assertEquals(valueHandler, simpleType.getValueHandler());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testWithContentValueHandler() {
        Object valueHandler = new Object();
        SimpleType simpleType = new SimpleType(String.class);
        simpleType.withContentValueHandler(valueHandler);
    }

    @Test
    public void testWithStaticTyping() {
        SimpleType simpleType = new SimpleType(String.class).withStaticTyping();
        assertEquals(true, simpleType.isStatic());
    }

    @Test
    public void testRefine() {
        SimpleType simpleType = new SimpleType(String.class).refine(UserType.class, null, null, null);
        assertNull(simpleType);
    }

    @Test
    public void testIsContainerType() {
        SimpleType simpleType = new SimpleType(String.class);
        assertEquals(false, simpleType.isContainerType());
    }

    @Test
    public void testGetErasedSignature() {
        SimpleType simpleType = new SimpleType(String.class);
        StringBuilder sb = simpleType.getErasedSignature(new StringBuilder());
        assertEquals("Ljava/lang/String;", sb.toString());
    }

    @Test
    public void testGetGenericSignature() {
        SimpleType simpleType = new SimpleType(String.class);
        StringBuilder sb = simpleType.getGenericSignature(new StringBuilder());
        assertEquals("Ljava/lang/String;", sb.toString());
    }

    @Test
    public void testToString() {
        SimpleType simpleType = new SimpleType(String.class);
        assertEquals("[simple type, class java.lang.String]", simpleType.toString());
    }

    @Test
    public void testEquals() {
        SimpleType simpleType1 = new SimpleType(String.class);
        SimpleType simpleType2 = new SimpleType(String.class);
        SimpleType simpleType3 = new SimpleType(String.class).withStaticTyping();
        SimpleType simpleType4 = new SimpleType(UserType.class);
        assertEquals(true, simpleType1.equals(simpleType2));
        assertEquals(false, simpleType1.equals(simpleType3));
        assertEquals(false, simpleType1.equals(simpleType4));
    }
}