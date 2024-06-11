package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class MapTypeTest {
    @Test
    public void testConstruct() {
        MapType mapType = MapType.construct(Map.class, null, null, null, String.class, Integer.class);
        assertTrue(Map.class.isAssignableFrom(mapType.getRawClass()));
        assertNull(mapType.getBindings());
        assertNull(mapType.getSuperClass());
        assertNull(mapType.getSuperInterfaces());
        assertEquals(String.class, mapType.getKeyType());
        assertEquals(Integer.class, mapType.getValueType());
    }

    @Test
    public void testConstructDeprecated() {
        MapType mapType = MapType.construct(Map.class, String.class, Integer.class);
        assertTrue(Map.class.isAssignableFrom(mapType.getRawClass()));
        assertNull(mapType.getBindings());
        assertEquals(_bogusSuperClass(Map.class), mapType.getSuperClass());
        assertNull(mapType.getSuperInterfaces());
        assertEquals(String.class, mapType.getKeyType());
        assertEquals(Integer.class, mapType.getValueType());
    }

    @Test
    public void testNarrow() {
        MapType baseType = new MapType(BaseType.class, null, null, null,
                String.class, Integer.class, null, null, false);
        MapType narrowType = (MapType) baseType._narrow(SubType.class);
        assertEquals(SubType.class, narrowType.getRawClass());
        assertNull(narrowType.getBindings());
        assertNull(narrowType.getSuperClass());
        assertNull(narrowType.getSuperInterfaces());
        assertEquals(String.class, narrowType.getKeyType());
        assertEquals(Integer.class, narrowType.getValueType());
    }

    @Test
    public void testWithTypeHandler() {
        MapType baseType = new MapType(BaseType.class, null, null, null,
                String.class, Integer.class, null, null, false);
        MapType typeHandlerType = baseType.withTypeHandler("typeHandler");
        assertEquals(BaseType.class, typeHandlerType.getRawClass());
        assertNull(typeHandlerType.getBindings());
        assertNull(typeHandlerType.getSuperClass());
        assertNull(typeHandlerType.getSuperInterfaces());
        assertEquals(String.class, typeHandlerType.getKeyType());
        assertEquals(Integer.class, typeHandlerType.getValueType());
        assertEquals("typeHandler", typeHandlerType.getTypeHandler());
    }

    @Test
    public void testWithContentTypeHandler() {
        MapType baseType = new MapType(BaseType.class, null, null, null,
                String.class, Integer.class, null, null, false);
        MapType contentTypeHandlerType = baseType.withContentTypeHandler("contentTypeHandler");
        assertEquals(BaseType.class, contentTypeHandlerType.getRawClass());
        assertNull(contentTypeHandlerType.getBindings());
        assertNull(contentTypeHandlerType.getSuperClass());
        assertNull(contentTypeHandlerType.getSuperInterfaces());
        assertEquals(String.class, contentTypeHandlerType.getKeyType());
        assertEquals(Integer.class, contentTypeHandlerType.getValueType());
        assertEquals("contentTypeHandler", contentTypeHandlerType.getContentTypeHandler());
    }

    @Test
    public void testWithValueHandler() {
        MapType baseType = new MapType(BaseType.class, null, null, null,
                String.class, Integer.class, null, null, false);
        MapType valueHandlerType = baseType.withValueHandler("valueHandler");
        assertEquals(BaseType.class, valueHandlerType.getRawClass());
        assertNull(valueHandlerType.getBindings());
        assertNull(valueHandlerType.getSuperClass());
        assertNull(valueHandlerType.getSuperInterfaces());
        assertEquals(String.class, valueHandlerType.getKeyType());
        assertEquals(Integer.class, valueHandlerType.getValueType());
        assertEquals("valueHandler", valueHandlerType.getValueHandler());
    }

    @Test
    public void testWithContentValueHandler() {
        MapType baseType = new MapType(BaseType.class, null, null, null,
                String.class, Integer.class, null, null, false);
        MapType contentValueHandlerType = baseType.withContentValueHandler("contentValueHandler");
        assertEquals(BaseType.class, contentValueHandlerType.getRawClass());
        assertNull(contentValueHandlerType.getBindings());
        assertNull(contentValueHandlerType.getSuperClass());
        assertNull(contentValueHandlerType.getSuperInterfaces());
        assertEquals(String.class, contentValueHandlerType.getKeyType());
        assertEquals(Integer.class, contentValueHandlerType.getValueType());
        assertEquals("contentValueHandler", contentValueHandlerType.getContentValueHandler());
    }

    @Test
    public void testWithStaticTyping() {
        MapType baseType = new MapType(BaseType.class, null, null, null,
                String.class, Integer.class, null, null, false);
        MapType staticTypingType = baseType.withStaticTyping();
        assertEquals(BaseType.class, staticTypingType.getRawClass());
        assertNull(staticTypingType.getBindings());
        assertNull(staticTypingType.getSuperClass());
        assertNull(staticTypingType.getSuperInterfaces());
        assertTrue(staticTypingType.getKeyType().isStatic());
        assertTrue(staticTypingType.getValueType().isStatic());
    }

    @Test
    public void testWithContentType() {
        MapType baseType = new MapType(BaseType.class, null, null, null,
                String.class, Integer.class, null, null, false);
        JavaType contentType = new SimpleType(Object.class);
        MapType contentTypeType = baseType.withContentType(contentType);
        assertEquals(BaseType.class, contentTypeType.getRawClass());
        assertNull(contentTypeType.getBindings());
        assertNull(contentTypeType.getSuperClass());
        assertNull(contentTypeType.getSuperInterfaces());
        assertEquals(String.class, contentTypeType.getKeyType());
        assertEquals(contentType, contentTypeType.getValueType());
    }

    @Test
    public void testWithKeyType() {
        MapType baseType = new MapType(BaseType.class, null, null, null,
                String.class, Integer.class, null, null, false);
        JavaType keyType = new SimpleType(Object.class);
        MapType keyTypeType = baseType.withKeyType(keyType);
        assertEquals(BaseType.class, keyTypeType.getRawClass());
        assertNull(keyTypeType.getBindings());
        assertNull(keyTypeType.getSuperClass());
        assertNull(keyTypeType.getSuperInterfaces());
        assertEquals(keyType, keyTypeType.getKeyType());
        assertEquals(Integer.class, keyTypeType.getValueType());
    }

    @Test
    public void testRefine() {
        MapType baseType = new MapType(BaseType.class, null, null, null,
                String.class, Integer.class, null, null, false);
        MapType refinedType = (MapType) baseType.refine(RefinedType.class, null, null, null);
        assertEquals(RefinedType.class, refinedType.getRawClass());
        assertNull(refinedType.getBindings());
        assertNull(refinedType.getSuperClass());
        assertNull(refinedType.getSuperInterfaces());
        assertEquals(String.class, refinedType.getKeyType());
        assertEquals(Integer.class, refinedType.getValueType());
    }

    @Test
    public void testWithKeyTypeHandler() {
        MapType baseType = new MapType(BaseType.class, null, null, null,
                String.class, Integer.class, null, null, false);
        MapType keyTypeHandlerType = baseType.withKeyTypeHandler("keyTypeHandler");
        assertEquals(BaseType.class, keyTypeHandlerType.getRawClass());
        assertNull(keyTypeHandlerType.getBindings());
        assertNull(keyTypeHandlerType.getSuperClass());
        assertNull(keyTypeHandlerType.getSuperInterfaces());
        assertEquals("keyTypeHandler", keyTypeHandlerType.getKeyTypeHandler());
        assertEquals(Integer.class, keyTypeHandlerType.getValueType());
    }

    @Test
    public void testWithKeyValueHandler() {
        MapType baseType = new MapType(BaseType.class, null, null, null,
                String.class, Integer.class, null, null, false);
        MapType keyValueHandlerType = baseType.withKeyValueHandler("keyValueHandler");
        assertEquals(BaseType.class, keyValueHandlerType.getRawClass());
        assertNull(keyValueHandlerType.getBindings());
        assertNull(keyValueHandlerType.getSuperClass());
        assertNull(keyValueHandlerType.getSuperInterfaces());
        assertEquals("keyValueHandler", keyValueHandlerType.getKeyValueHandler());
        assertEquals(Integer.class, keyValueHandlerType.getValueType());
    }

    @Test
    public void testToString() {
        MapType mapType = new MapType(BaseType.class, null, null, null,
                String.class, Integer.class, null, null, false);
        assertEquals("[map type; class com.fasterxml.jackson.databind.type.BaseType, java.lang.String -> java.lang.Integer]", mapType.toString());
    }

    private static class BaseType extends java.util.HashMap<String, Integer> {
        private static final long serialVersionUID = 1L;
    }

    private static class SubType extends BaseType {
        private static final long serialVersionUID = 1L;
    }

    private static class RefinedType extends java.util.HashMap<String, Integer> {
        private static final long serialVersionUID = 1L;
    }

    private static Class<?> _bogusSuperClass(Class<?> baseType) {
        return Object.class;
    }
}