package com.fasterxml.jackson.databind.type;

import com.fasterxml.jackson.databind.JavaType;

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
        assertNull(simpleType.getSuperInterfaces());
        assertNull(simpleType.getValueHandler());
        assertNull(simpleType.getTypeHandler());
        assertEquals(false, simpleType.isStaticTyping());

        TypeBindings bindings = new TypeBindings();
        JavaType superClass = SimpleType.constructUnsafe(Object.class);
        JavaType[] superInts = new JavaType[0];

        SimpleType simpleTypeWithArgs = new SimpleType(String.class, bindings, superClass, superInts, "valueHandler", "typeHandler", true);
        assertEquals(String.class, simpleTypeWithArgs.getRawClass());
        assertEquals(bindings, simpleTypeWithArgs.getBindings());
        assertEquals(superClass, simpleTypeWithArgs.getSuperClass());
        assertEquals(superInts, simpleTypeWithArgs.getSuperInterfaces());
        assertEquals("valueHandler", simpleTypeWithArgs.getValueHandler());
        assertEquals("typeHandler", simpleTypeWithArgs.getTypeHandler());
        assertEquals(true, simpleTypeWithArgs.isStaticTyping());
    }

    @Test
    public void testConstructUnsafe() {
        SimpleType simpleType = SimpleType.constructUnsafe(Integer.class);
        assertEquals(Integer.class, simpleType.getRawClass());
        assertNull(simpleType.getBindings());
        assertNull(simpleType.getSuperClass());
        assertNull(simpleType.getSuperInterfaces());
        assertNull(simpleType.getValueHandler());
        assertNull(simpleType.getTypeHandler());
        assertEquals(false, simpleType.isStaticTyping());
    }

    @Test
    public void testConstruct() {
        SimpleType simpleType = SimpleType.construct(Long.class);
        assertEquals(Long.class, simpleType.getRawClass());
        assertNull(simpleType.getBindings());
        assertNull(simpleType.getSuperClass());
        assertNull(simpleType.getSuperInterfaces());
        assertNull(simpleType.getValueHandler());
        assertNull(simpleType.getTypeHandler());
        assertEquals(false, simpleType.isStaticTyping());
    }

    @Test
    public void testWithContentType() {
        SimpleType simpleType = new SimpleType(String.class);
        try {
            simpleType.withContentType(null);
        } catch (IllegalArgumentException e) {
            assertEquals("Simple types have no content types; can not call withContentType()", e.getMessage());
        }
    }

    @Test
    public void testWithContentValueHandler() {
        SimpleType simpleType = new SimpleType(String.class);
        try {
            simpleType.withContentValueHandler(null);
        } catch (IllegalArgumentException e) {
            assertEquals("Simple types have no content types; can not call withContenValueHandler()", e.getMessage());
        }
    }

    @Test
    public void testIsContainerType() {
        SimpleType simpleType = new SimpleType(String.class);
        assertEquals(false, simpleType.isContainerType());
    }

    @Test
    public void testGetErasedSignature() {
        SimpleType simpleType = new SimpleType(String.class);
        StringBuilder sb = new StringBuilder();
        assertEquals("Ljava/lang/String;", simpleType.getErasedSignature(sb).toString());
    }

    @Test
    public void testGetGenericSignature() {
        SimpleType simpleType = new SimpleType(String.class);
        StringBuilder sb = new StringBuilder();
        assertEquals("Ljava/lang/String;", simpleType.getGenericSignature(sb).toString());
    }

    @Test
    public void testRefine() {
        SimpleType simpleType = new SimpleType(String.class);
        assertNull(simpleType.refine(Object.class, null, null, null));
    }

    @Test
    public void testBuildCanonicalName() {
        SimpleType simpleType = new SimpleType(String.class);
        assertEquals("java.lang.String", simpleType.buildCanonicalName());
    }

    @Test
    public void testEquals() {
        SimpleType simpleType1 = new SimpleType(String.class);
        SimpleType simpleType2 = new SimpleType(String.class);
        assertEquals(true, simpleType1.equals(simpleType2));
    }

    @Test
    public void testToString() {
        SimpleType simpleType = new SimpleType(String.class);
        assertEquals("[simple type, class java.lang.String]", simpleType.toString());
    }
}