package com.fasterxml.jackson.databind.type;

import org.junit.Test;
import static org.junit.Assert.*;

public class MapTypeTest {

    @Test
    public void testConstruct() {
        // Test if the MapType construct method returns a MapType object
        // with the correct raw type, bindings, super class,
        // super interfaces, key type and value type
        Class<?> rawType = MapTypeTest.class; // Placeholder class
        TypeBindings bindings = null; // Placeholder type bindings
        JavaType superClass = null; // Placeholder super class
        JavaType[] superInts = null; // Placeholder super interfaces
        JavaType keyT = null; // Placeholder key type
        JavaType valueT = null; // Placeholder value type

        MapType mapType = MapType.construct(rawType, bindings, superClass, superInts, keyT, valueT);

        assertEquals(rawType, mapType.getRawClass());
        assertEquals(bindings, mapType.getBindings());
        assertEquals(superClass, mapType.getSuperClass());
        assertEquals(superInts, mapType.getInterfaces());
        assertEquals(keyT, mapType.getKeyType());
        assertEquals(valueT, mapType.getContentType());
    }

    @Test
    public void testConstructDeprecated() {
        // Test if the deprecated MapType construct method returns a MapType object
        // with the correct raw type, key type and value type
        Class<?> rawType = MapTypeTest.class; // Placeholder class
        JavaType keyT = null; // Placeholder key type
        JavaType valueT = null; // Placeholder value type

        MapType mapType = MapType.construct(rawType, keyT, valueT);

        assertEquals(rawType, mapType.getRawClass());
        assertEquals(keyT, mapType.getKeyType());
        assertEquals(valueT, mapType.getContentType());
    }

    @Test
    public void testNarrow() {
        // Test if the _narrow method returns a MapType object
        // with the correct subclass, bindings, super class,
        // super interfaces, key type and value type
        Class<?> subclass = MapTypeTest.class; // Placeholder subclass

        MapType mapType = mapType._narrow(subclass);

        assertEquals(subclass, mapType.getSubclass());
        assertEquals(bindings, mapType.getBindings());
        assertEquals(superClass, mapType.getSuperClass());
        assertEquals(superInts, mapType.getInterfaces());
        assertEquals(keyT, mapType.getKeyType());
        assertEquals(valueT, mapType.getContentType());
        assertEquals(valueHandler, mapType.getContentValueHandler());
        assertEquals(typeHandler, mapType.getTypeHandler());
        assertEquals(asStatic, mapType.isStatic());
    }

    @Test
    public void testWithTypeHandler() {
        // Test if the withTypeHandler method returns a MapType object
        // with the correct class, bindings, super class,
        // super interfaces, key type, value type and type handler
        Object h = null; // Placeholder type handler

        MapType mapType = mapType.withTypeHandler(h);

        assertEquals(_class, mapType.getRawClass());
        assertEquals(_bindings, mapType.getBindings());
        assertEquals(_superClass, mapType.getSuperClass());
        assertEquals(_superInterfaces, mapType.getInterfaces());
        assertEquals(_keyType, mapType.getKeyType());
        assertEquals(_valueType, mapType.getContentType());
        assertEquals(_valueHandler, mapType.getContentValueHandler());
        assertEquals(h, mapType.getTypeHandler());
        assertEquals(_asStatic, mapType.isStatic());
    }

    @Test
    public void testWithContentTypeHandler() {
        // Test if the withContentTypeHandler method returns a MapType object
        // with the correct class, bindings, super class,
        // super interfaces, key type, content type and value handler
        Object h = null; // Placeholder value handler

        MapType mapType = mapType.withContentTypeHandler(h);

        assertEquals(_class, mapType.getRawClass());
        assertEquals(_bindings, mapType.getBindings());
        assertEquals(_superClass, mapType.getSuperClass());
        assertEquals(_superInterfaces, mapType.getInterfaces());
        assertEquals(_keyType, mapType.getKeyType());
        assertEquals(_valueType.withTypeHandler(h), mapType.getContentType());
        assertEquals(_valueHandler, mapType.getContentValueHandler());
        assertEquals(_typeHandler, mapType.getTypeHandler());
        assertEquals(_asStatic, mapType.isStatic());
    }

    @Test
    public void testWithValueHandler() {
        // Test if the withValueHandler method returns a MapType object
        // with the correct class, bindings, super class,
        // super interfaces, key type, value type and value handler
        Object h = null; // Placeholder value handler

        MapType mapType = mapType.withValueHandler(h);

        assertEquals(_class, mapType.getRawClass());
        assertEquals(_bindings, mapType.getBindings());
        assertEquals(_superClass, mapType.getSuperClass());
        assertEquals(_superInterfaces, mapType.getInterfaces());
        assertEquals(_keyType, mapType.getKeyType());
        assertEquals(_valueType, mapType.getContentType());
        assertEquals(h, mapType.getContentValueHandler());
        assertEquals(_typeHandler, mapType.getTypeHandler());
        assertEquals(_asStatic, mapType.isStatic());
    }

    @Test
    public void testWithContentValueHandler() {
        // Test if the withContentValueHandler method returns a MapType object
        // with the correct class, bindings, super class,
        // super interfaces, key type, content type and content value handler
        Object h = null; // Placeholder content value handler

        MapType mapType = mapType.withContentValueHandler(h);

        assertEquals(_class, mapType.getRawClass());
        assertEquals(_bindings, mapType.getBindings());
        assertEquals(_superClass, mapType.getSuperClass());
        assertEquals(_superInterfaces, mapType.getInterfaces());
        assertEquals(_keyType, mapType.getKeyType());
        assertEquals(_valueType.withValueHandler(h), mapType.getContentType());
        assertEquals(_valueHandler, mapType.getContentValueHandler());
        assertEquals(_typeHandler, mapType.getTypeHandler());
        assertEquals(_asStatic, mapType.isStatic());
    }

    @Test
    public void testWithStaticTyping() {
        // Test if the withStaticTyping method returns a MapType object
        // with the correct class, bindings, super class,
        // super interfaces, static key type, static value type and value handler
        boolean originalAsStatic = _asStatic;

        MapType mapType = mapType.withStaticTyping();

        assertEquals(originalAsStatic, mapType.isStatic());
        if (!originalAsStatic) {
            assertTrue(mapType.isStatic());
        }
    }

    @Test
    public void testWithContentType() {
        // Test if the withContentType method returns a MapType object
        // with the correct class, bindings, super class,
        // super interfaces, key type and content type
        JavaType contentType = null; // Placeholder content type

        MapType mapType = mapType.withContentType(contentType);

        assertEquals(_class, mapType.getRawClass());
        assertEquals(_bindings, mapType.getBindings());
        assertEquals(_superClass, mapType.getSuperClass());
        assertEquals(_superInterfaces, mapType.getInterfaces());
        assertEquals(_keyType, mapType.getKeyType());
        assertEquals(contentType, mapType.getContentType());
    }

    @Test
    public void testWithKeyType() {
        // Test if the withKeyType method returns a MapType object
        // with the correct class, bindings, super class,
        // super interfaces, key type and value type
        JavaType keyType = null; // Placeholder key type

        MapType mapType = mapType.withKeyType(keyType);

        assertEquals(_class, mapType.getRawClass());
        assertEquals(_bindings, mapType.getBindings());
        assertEquals(_superClass, mapType.getSuperClass());
        assertEquals(_superInterfaces, mapType.getInterfaces());
        assertEquals(keyType, mapType.getKeyType());
        assertEquals(_valueType, mapType.getContentType());
    }

    @Test
    public void testToString() {
        // Test if the toString method returns a string in the correct format
        String expectedString = "[map type; class " + _class.getName() + ", " + _keyType + " -> " + _valueType + "]";

        assertEquals(expectedString, mapType.toString());
    }

}