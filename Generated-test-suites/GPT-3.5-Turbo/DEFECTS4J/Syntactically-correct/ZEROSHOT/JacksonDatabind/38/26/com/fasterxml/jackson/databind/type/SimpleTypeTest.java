package com.fasterxml.jackson.databind.type;

import org.junit.Test;
import static org.junit.Assert.*;

public class SimpleTypeTest {

    @Test
    public void testConstructor1() {
        SimpleType type = new SimpleType(null);
        assertNull(type.getSuperClass());
        assertEquals(0, type.getSuperInterfaces().length);
        assertNull(type.getValueHandler());
        assertNull(type.getTypeHandler());
        assertFalse(type.isStatic());
        assertFalse(type.isContainerType());
    }

    @Test
    public void testConstructor2() {
        TypeBindings bindings = TypeBindings.emptyBindings();
        JavaType superClass = null;
        JavaType[] superInterfaces = null;
        
        SimpleType type = new SimpleType(null, bindings, superClass, superInterfaces);
        assertNull(type.getClass());
        assertEquals(0, type.getSuperInterfaces().length);
        assertNull(type.getValueHandler());
        assertNull(type.getTypeHandler());
        assertFalse(type.isStatic());
        assertFalse(type.isContainerType());
    }

    @Test
    public void testConstructor3() {
        TypeBindings bindings = TypeBindings.emptyBindings();
        JavaType superClass = null;
        JavaType[] superInterfaces = null;
        Object valueHandler = null;
        Object typeHandler = null;
        boolean asStatic = false;
        
        SimpleType type = new SimpleType(null, bindings, superClass, superInterfaces, valueHandler, typeHandler, asStatic);
        assertNull(type.getClass());
        assertEquals(0, type.getSuperInterfaces().length);
        assertNull(type.getValueHandler());
        assertNull(type.getTypeHandler());
        assertFalse(type.isStatic());
        assertFalse(type.isContainerType());
    }

    @Test
    public void testConstructor4() {
        TypeBindings bindings = TypeBindings.emptyBindings();
        JavaType superClass = null;
        JavaType[] superInterfaces = null;
        int extraHash = 0;
        Object valueHandler = null;
        Object typeHandler = null;
        boolean asStatic = false;
        
        SimpleType type = new SimpleType(null, bindings, superClass, superInterfaces, extraHash, valueHandler, typeHandler, asStatic);
        assertNull(type.getClass());
        assertEquals(0, type.getSuperInterfaces().length);
        assertNull(type.getValueHandler());
        assertNull(type.getTypeHandler());
        assertFalse(type.isStatic());
        assertFalse(type.isContainerType());
    }

    @Test
    public void testConstructUnsafe() {
        SimpleType type = SimpleType.constructUnsafe(null);
        assertNull(type.getClass());
        assertEquals(0, type.getSuperInterfaces().length);
        assertNull(type.getValueHandler());
        assertNull(type.getTypeHandler());
        assertFalse(type.isStatic());
        assertFalse(type.isContainerType());
    }

    @Test
    public void testConstruct() {
        try {
            SimpleType type = SimpleType.construct(null);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("Can not construct SimpleType for a Map (class: null)", e.getMessage());
        }

        try {
            SimpleType type = SimpleType.construct(null);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("Can not construct SimpleType for a Collection (class: null)", e.getMessage());
        }

        try {
            SimpleType type = SimpleType.construct(null);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("Can not construct SimpleType for an array (class: null)", e.getMessage());
        }
        
        TypeBindings bindings = TypeBindings.emptyBindings();
        JavaType superClass = null;
        JavaType[] superInterfaces = null;
        SimpleType type = new SimpleType(null, bindings, superClass, superInterfaces);
        assertNull(type.getClass());
        assertEquals(0, type.getSuperInterfaces().length);
        assertNull(type.getValueHandler());
        assertNull(type.getTypeHandler());
        assertFalse(type.isStatic());
        assertFalse(type.isContainerType());
    }

    @Test
    public void testNarrow() {
        Class<?> subclass = null;
        SimpleType type = new SimpleType(null);
        JavaType result = type._narrow(subclass);
        assertEquals(type, result);
    }

    @Test
    public void testWithContentType() {
        JavaType contentType = null;
        SimpleType type = new SimpleType(null);
        try {
            type.withContentType(contentType);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("Simple types have no content types; can not call withContentType()", e.getMessage());
        }
    }

    @Test
    public void testWithTypeHandler() {
        Object h = null;
        SimpleType type = new SimpleType(null);
        SimpleType result = type.withTypeHandler(h);
        assertEquals(type, result);
    }

    @Test
    public void testWithContentTypeHandler() {
        Object h = null;
        SimpleType type = new SimpleType(null);
        try {
            type.withContentTypeHandler(h);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("Simple types have no content types; can not call withContentTypeHandler()", e.getMessage());
        }
    }

    @Test
    public void testWithValueHandler() {
        Object h = null;
        SimpleType type = new SimpleType(null);
        SimpleType result = type.withValueHandler(h);
        assertEquals(type, result);
    }

    @Test
    public void testWithContentValueHandler() {
        Object h = null;
        SimpleType type = new SimpleType(null);
        try {
            type.withContentValueHandler(h);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("Simple types have no content types; can not call withContentValueHandler()", e.getMessage());
        }
    }

    @Test
    public void testWithStaticTyping() {
        SimpleType type = new SimpleType(null);
        SimpleType result = type.withStaticTyping();
        assertEquals(type, result);
    }

    @Test
    public void testRefine() {
        Class<?> rawType = null;
        TypeBindings bindings = TypeBindings.emptyBindings();
        JavaType superClass = null;
        JavaType[] superInterfaces = null;
        
        SimpleType type = new SimpleType(null);
        JavaType result = type.refine(rawType, bindings, superClass, superInterfaces);
        assertNull(result);
    }

}