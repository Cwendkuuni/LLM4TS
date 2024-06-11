package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.*;

import org.junit.Test;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.TypeBindings;
import com.fasterxml.jackson.databind.type.MapType;

public class MapTypeTest {

    @Test
    public void testConstruct() throws Exception {
        JavaType keyType = JavaType.construct(int.class, int.class);
        JavaType valueType = JavaType.construct(String.class, String.class);
        JavaType mapType = MapType.construct(Map.class, TypeBindings.Default.instance(), null, null, keyType, valueType);
        assertEquals(Map.class, mapType.getRawClass());
        assertEquals(keyType, mapType.getKeyType());
        assertEquals(valueType, mapType.getValueType());
        assertEquals(TypeBindings.Default.instance(), mapType.getTypeBindings());
        assertEquals(false, mapType.isStatic());
    }

    @Test
    public void testConstructWithBindings() throws Exception {
        JavaType keyType = JavaType.construct(int.class, int.class);
        JavaType valueType = JavaType.construct(String.class, String.class);
        TypeBindings bindings = new TypeBindings();
        JavaType mapType = MapType.construct(Map.class, bindings, null, null, keyType, valueType);
        assertEquals(Map.class, mapType.getRawClass());
        assertEquals(keyType, mapType.getKeyType());
        assertEquals(valueType, mapType.getValueType());
        assertEquals(bindings, mapType.getTypeBindings());
        assertEquals(false, mapType.isStatic());
    }

    @Test
    public void testConstructWithSuperClass() throws Exception {
        JavaType keyType = JavaType.construct(int.class, int.class);
        JavaType valueType = JavaType.construct(String.class, String.class);
        JavaType superClass = JavaType.construct(HashMap.class, Object.class, List.class);
        JavaType mapType = MapType.construct(Map.class, null, superClass, null, keyType, valueType);
        assertEquals(Map.class, mapType.getRawClass());
        assertEquals(keyType, mapType.getKeyType());
        assertEquals(valueType, mapType.getValueType());
        assertEquals(superClass, mapType.getSuperClass());
        assertEquals(null, mapType.getTypeBindings());
        assertEquals(false, mapType.isStatic());
    }

    @Test
    public void testConstructWithSuperInterfaces() throws Exception {
        JavaType keyType = JavaType.construct(int.class, int.class);
        JavaType valueType = JavaType.construct(String.class, String.class);
        JavaType[] superInterfaces = new JavaType[] { JavaType.construct(Serializable.class) };
        JavaType mapType = MapType.construct(Map.class, null, null, superInterfaces, keyType, valueType);
        assertEquals(Map.class, mapType.getRawClass());
        assertEquals(keyType, mapType.getKeyType());
        assertEquals(valueType, mapType.getValueType());
        assertEquals(null, mapType.getSuperClass());
        assertEquals(superInterfaces, mapType.getSuperInterfaces());
        assertEquals(null, mapType.getTypeBindings());
        assertEquals(false, mapType.isStatic());
    }

    @Test
    public void testConstructWithKeyTypeHandler() throws Exception {
        JavaType keyType = JavaType.construct(int.class, int.class);
        JavaType valueType = JavaType.construct(String.class, String.class);
        Object keyHandler = new Object();
        JavaType mapType = MapType.construct(Map.class, null, null, null, keyType, valueType)
                .withKeyTypeHandler(keyHandler);
        assertEquals(Map.class, mapType.getRawClass());
        assertEquals(keyType, mapType.getKeyType());
        assertEquals(valueType, mapType.getValueType());
        assertNotNull(mapType.getKeyTypeHandler());
        assertEquals(keyHandler, mapType.getKeyTypeHandler());
        assertEquals(false, mapType.isStatic());
    }

    @Test
    public void testConstructWithValueHandler() throws Exception {
        JavaType keyType = JavaType.construct(int.class, int.class);
        JavaType valueType = JavaType.construct(String.class, String.class);
        Object valueHandler = new Object();
        JavaType mapType = MapType.construct(Map.class, null, null, null, keyType, valueType)
                .withValueHandler(valueHandler);
        assertEquals(Map.class, mapType.getRawClass());
        assertEquals(keyType, mapType.getKeyType());
        assertEquals(valueType, mapType.getValueType());
        assertNotNull(mapType.getValueHandler());
        assertEquals(valueHandler, mapType.getValueHandler());
        assertEquals(false, mapType.isStatic());
    }

    @Test
    public void testConstructWithTypeHandler() throws Exception {
        JavaType keyType = JavaType.construct(int.class, int.class);
        JavaType valueType = JavaType.construct(String.class, String.class);
        Object typeHandler = new Object();
        JavaType mapType = MapType.construct(Map.class, null, null, null, keyType, valueType)
                .withTypeHandler(typeHandler);
        assertEquals(Map.class, mapType.getRawClass());
        assertEquals(keyType, mapType.getKeyType());
        assertEquals(valueType, mapType.getValueType());
        assertNotNull(mapType.getTypeHandler());
        assertEquals(typeHandler, mapType.getTypeHandler());
        assertEquals(false, mapType.isStatic());
    }

    @Test
    public void testConstructWithContentTypeHandler() throws Exception {
        JavaType keyType = JavaType.construct(int.class, int.class);
        JavaType valueType = JavaType.construct(String.class, String.class);
        Object contentTypeHandler = new Object();
        JavaType mapType = MapType.construct(Map.class, null, null, null, keyType, valueType)
                .withContentTypeHandler(contentTypeHandler);
        assertEquals(Map.class, mapType.getRawClass());
        assertEquals(keyType, mapType.getKeyType());
        JavaType contentValueType = JavaType.construct(String.class, String.class);
        assertEquals(contentValueType, mapType.getValueType().withTypeHandler(contentTypeHandler));
        assertNotNull(mapType.getValueHandler());
        assertNull(mapType.getValueHandler());
        assertEquals(false, mapType.isStatic());
    }

    @Test
    public void testConstructWithContentValueHandler() throws Exception {
        JavaType keyType = JavaType.construct(int.class, int.class);
        JavaType valueType = JavaType.construct(String.class, String.class);
        Object contentValueHandler = new Object();
        JavaType mapType = MapType.construct(Map.class, null, null, null, keyType, valueType)
                .withContentValueHandler(contentValueHandler);
        assertEquals(Map.class, mapType.getRawClass());
        assertEquals(keyType, mapType.getKeyType());
        JavaType contentKeyType = JavaType.construct(int.class, int.class);
        assertEquals(contentKeyType, mapType.getKeyType().withValueHandler(contentValueHandler));
        assertNotNull(mapType.getKeyTypeHandler());
        assertNull(mapType.getKeyTypeHandler());
        assertEquals(false, mapType.isStatic());
    }

    @Test
    public void testWithStaticTyping() throws Exception {
        JavaType keyType = JavaType.construct(int.class, int.class);
        JavaType valueType = JavaType.construct(String.class, String.class);
        JavaType mapType = MapType.construct(Map.class, null, null, null, keyType, valueType);
        JavaType staticMapType = mapType.withStaticTyping();
        assertEquals(Map.class, staticMapType.getRawClass());
        assertEquals(keyType, staticMapType.getKeyType());
        assertEquals(valueType, staticMapType.getValueType());
        assertTrue(staticMapType.isStatic());
        assertFalse(mapType.isStatic());
    }

    @Test
    public void testWithContentType() throws Exception {
        JavaType keyType = JavaType.construct(int.class, int.class);
        JavaType valueType = JavaType.construct(String.class, String.class);
        JavaType mapType = MapType.construct(Map.class, null, null, null, keyType, valueType);
        JavaType contentType = JavaType.construct(String[].class, String.class);
        JavaType withContentType = mapType.withContentType(contentType);
        assertEquals(Map.class, withContentType.getRawClass());
        assertEquals(keyType, withContentType.getKeyType());
        assertEquals(contentType, withContentType.getValueType());
        assertNull(withContentType.getValueHandler());
        assertEquals(false, withContentType.isStatic());
    }

    @Test
    public void testWithKeyType() throws Exception {
        JavaType keyType = JavaType.construct(int.class, int.class);
        JavaType valueType = JavaType.construct(String.class, String.class);
        JavaType mapType = MapType.construct(Map.class, null, null, null, keyType, valueType);
        JavaType newKeyType = JavaType.construct(int[].class, int.class);
        JavaType withKeyType = mapType.withKeyType(newKeyType);
        assertEquals(Map.class, withKeyType.getRawClass());
        assertEquals(newKeyType, withKeyType.getKeyType());
        assertEquals(valueType, withKeyType.getValueType());
        assertNull(withKeyType.getValueHandler());
        assertEquals(false, withKeyType.isStatic());
    }

    @Test
    public void testRefine() throws Exception {
        JavaType keyType = JavaType.construct(int.class, int.class);
        JavaType valueType = JavaType.construct(String.class, String.class);
        JavaType mapType = MapType.construct(Map.class, null, null, null, keyType, valueType);
        JavaType refinedType = mapType.refine(HashMap.class, null, null, null);
        assertEquals(HashMap.class, refinedType.getRawClass());
        assertEquals(keyType, refinedType.getKeyType());
        assertEquals(valueType, refinedType.getValueType());
        assertNull(refinedType.getValueHandler());
        assertEquals(false, refinedType.isStatic());
    }

    @Test
    public void testWithKeyTypeHandler() throws Exception {
        JavaType keyType = JavaType.construct(int.class, int.class);
        JavaType valueType = JavaType.construct(String.class, String.class);
        Object keyHandler = new Object();
        JavaType mapType = MapType.construct(Map.class, null, null, null, keyType, valueType);
        JavaType withKeyTypeHandler = mapType.withKeyTypeHandler(keyHandler);
        assertEquals(Map.class, withKeyTypeHandler.getRawClass());
        assertEquals(keyType, withKeyTypeHandler.getKeyType());
        assertEquals(valueType, withKeyTypeHandler.getValueType());
        assertNotNull(withKeyTypeHandler.getKeyTypeHandler());
        assertEquals(keyHandler, withKeyTypeHandler.getKeyTypeHandler());
        assertEquals(false, withKeyTypeHandler.isStatic());
    }

    @Test
    public void testWithKeyValueHandler() throws Exception {
        JavaType keyType = JavaType.construct(int.class, int.class);
        JavaType valueType = JavaType.construct(String.class, String.class);
        Object keyValueHandler = new Object();
        JavaType mapType = MapType.construct(Map.class, null, null, null, keyType, valueType);
        JavaType withKeyValueHandler = mapType.withKeyValueHandler(keyValueHandler);
        assertEquals(Map.class, withKeyValueHandler.getRawClass());
        assertEquals(keyType, withKeyValueHandler.getKeyType());
        assertEquals(valueType, withKeyValueHandler.getValueType());
        assertNotNull(withKeyValueHandler.getKeyTypeHandler());
        assertNull(withKeyValueHandler.getKeyTypeHandler());
        assertNotNull(withKeyValueHandler.getValueHandler());
        assertEquals(keyValueHandler, withKeyValueHandler.getValueHandler());
        assertEquals(false, withKeyValueHandler.isStatic());
    }
}