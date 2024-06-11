package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

public class MapTypeTest {

    @Test
    public void testConstruct() {
        JavaType keyType = null;
        JavaType valueType = null;

        MapType mapType1 = MapType.construct(String.class, null, null, null, keyType, valueType);
        assertNull(mapType1.getKeyType());
        assertNull(mapType1.getValueType());
        assertNull(mapType1.getTypeHandler());
        assertNull(mapType1.getValueHandler());

        MapType mapType2 = MapType.construct(Map.class, null, null, null, keyType, valueType);
        assertEquals(Map.class, mapType2.getRawClass());
        assertNull(mapType2.getKeyType());
        assertNull(mapType2.getValueType());
        assertNull(mapType2.getTypeHandler());
        assertNull(mapType2.getValueHandler());

        JavaType keyTypeAnnotation = JavaType.constructType(Map.class, String.class);
        JavaType valueTypeAnnotation = JavaType.constructType(Map.class, Integer.class);
        MapType mapType3 = MapType.construct(Map.class, null, null, null, keyTypeAnnotation, valueTypeAnnotation);
        assertEquals(Map.class, mapType3.getRawClass());
        assertEquals(String.class, mapType3.getKeyType().getRawClass());
        assertEquals(Integer.class, mapType3.getValueType().getRawClass());
        assertNull(mapType3.getTypeHandler());
        assertNull(mapType3.getValueHandler());

        assertNotNull(mapType3.getValueType().getRawClass());
        assertNotNull(mapType3.getKeyType().getRawClass());
    }

    @Test
    public void testConstructWithBindings() {
        JavaType keyType = JavaType.constructType(Map.class, String.class);
        JavaType valueType = JavaType.constructType(Map.class, Integer.class);
        TypeBindings bindings = new TypeBindings();
        MapType mapType = MapType.construct(Map.class, bindings, null, null, keyType, valueType);
        assertEquals(Map.class, mapType.getRawClass());
        assertEquals(String.class, mapType.getKeyType().getRawClass());
        assertEquals(Integer.class, mapType.getValueType().getRawClass());
        assertNull(mapType.getTypeHandler());
        assertNull(mapType.getValueHandler());
        assertEquals(bindings, mapType.getTypeBindings());
    }

    @Test
    public void testWithTypeHandler() {
        JavaType keyType = JavaType.constructType(Map.class, String.class);
        JavaType valueType = JavaType.constructType(Map.class, Integer.class);
        Object typeHandler = new Object();
        MapType mapType1 = new MapType(Map.class, null, null, null, keyType, valueType, null, null, false);
        MapType mapType2 = mapType1.withTypeHandler(typeHandler);
        assertEquals(mapType1, mapType2);
        assertNull(mapType2.getTypeHandler());

        MapType mapType3 = new MapType(Map.class, null, null, null, keyType, valueType, null, typeHandler, false);
        MapType mapType4 = mapType3.withTypeHandler(null);
        assertEquals(mapType3, mapType4);
        assertNull(mapType4.getTypeHandler());
    }

    @Test
    public void testWithContentTypeHandler() {
        JavaType keyType = JavaType.constructType(Map.class, String.class);
        JavaType valueType = JavaType.constructType(Map.class, Integer.class);
        Object contentTypeHandler = new Object();
        MapType mapType1 = new MapType(Map.class, null, null, null, keyType, valueType, null, null, false);
        MapType mapType2 = mapType1.withContentTypeHandler(contentTypeHandler);
        assertEquals(mapType1, mapType2);
        assertNull(mapType2.getContentTypeHandler());

        MapType mapType3 = new MapType(Map.class, null, null, null, keyType, valueType, null, null, false);
        MapType mapType4 = mapType3.withContentType(JavaType.constructType(Map.class, String[].class));
        assertEquals(mapType3, mapType4);
        assertNull(mapType4.getContentTypeHandler());

        MapType mapType5 = new MapType(Map.class, null, null, null, keyType, valueType, null, null, false);
        MapType mapType6 = mapType5.withContentTypeHandler(contentTypeHandler);
        assertNotNull(mapType6.getContentTypeHandler());
    }

    @Test
    public void testWithValueHandler() {
        JavaType keyType = JavaType.constructType(Map.class, String.class);
        JavaType valueType = JavaType.constructType(Map.class, Integer.class);
        Object valueHandler = new Object();
        MapType mapType1 = new MapType(Map.class, null, null, null, keyType, valueType, null, null, false);
        MapType mapType2 = mapType1.withValueHandler(valueHandler);
        assertEquals(mapType1, mapType2);
        assertNull(mapType2.getValueHandler());

        MapType mapType3 = new MapType(Map.class, null, null, null, keyType, valueType, null, null, false);
        MapType mapType4 = mapType3.withValueType(JavaType.constructType(Map.class, String[].class));
        assertEquals(mapType3, mapType4);
        assertNull(mapType4.getValueHandler());

        MapType mapType5 = new MapType(Map.class, null, null, null, keyType, valueType, null, null, false);
        MapType mapType6 = mapType5.withValueHandler(valueHandler);
        assertNotNull(mapType6.getValueHandler());
    }

    @Test
    public void testWithContentValueHandler() {
        JavaType keyType = JavaType.constructType(Map.class, String.class);
        JavaType valueType = JavaType.constructType(Map.class, Integer.class);
        Object contentValueHandler = new Object();
        MapType mapType1 = new MapType(Map.class, null, null, null, keyType, valueType, null, null, false);
        MapType mapType2 = mapType1.withContentValueHandler(contentValueHandler);
        assertEquals(mapType1, mapType2);
        assertNull(mapType2.getContentValueHandler());

        MapType mapType3 = new MapType(Map.class, null, null, null, keyType, valueType, null, null, false);
        MapType mapType4 = mapType3.withValueType(JavaType.constructType(Map.class, String[].class));
        MapType mapType5 = mapType4.withValueHandler(JavaType.constructType(Map.class, String[].class));
        MapType mapType6 = mapType5.withContentValueHandler(contentValueHandler);
        assertNotNull(mapType6.getContentValueHandler());
    }

    @Test
    public void testWithStaticTyping() {
        JavaType keyType = JavaType.constructType(Map.class, String.class);
        JavaType valueType = JavaType.constructType(Map.class, Integer.class);
        MapType mapType1 = new MapType(Map.class, null, null, null, keyType, valueType, null, null, false);
        MapType mapType2 = mapType1.withStaticTyping();
        assertNotNull(mapType2);
        assertTrue(mapType2.isStatic());

        MapType mapType3 = new MapType(Map.class, null, null, null, keyType, valueType, null, null, false);
        MapType mapType4 = mapType3.withStaticTyping();
        assertEquals(mapType3, mapType4);
        assertFalse(mapType4.isStatic());
    }

    @Test
    public void testWithContentType() {
        JavaType keyType = JavaType.constructType(Map.class, String.class);
        JavaType valueType = JavaType.constructType(Map.class, Integer.class);
        JavaType contentType = JavaType.constructType(Map.class, String[].class);
        MapType mapType1 = new MapType(Map.class, null, null, null, keyType, valueType, null, null, false);
        MapType mapType2 = mapType1.withContentType(contentType);
        assertEquals(mapType1, mapType2);
        assertNull(mapType2.getContentTypeHandler());

        MapType mapType3 = new MapType(Map.class, null, null, null, keyType, valueType, null, null, false);
        MapType mapType4 = mapType3.withContentType(JavaType.constructType(Map.class, String[].class));
        assertNotNull(mapType4.getContentType());
        assertNull(mapType4.getContentTypeHandler());

        MapType mapType5 = new MapType(Map.class, null, null, null, keyType, valueType, null, null, false);
        MapType mapType6 = mapType5.withContentType(JavaType.constructType(Map.class, String[].class));
        MapType mapType7 = mapType6.withContentTypeHandler(new Object());
        assertNotNull(mapType7.getContentTypeHandler());
    }

    @Test
    public void testWithKeyType() {
        JavaType keyType = JavaType.constructType(Map.class, String.class);
        JavaType valueType = JavaType.constructType(Map.class, Integer.class);
        JavaType newKeyType = JavaType.constructType(Map.class, Integer.class);
        MapType mapType1 = new MapType(Map.class, null, null, null, keyType, valueType, null, null, false);
        MapType mapType2 = mapType1.withKeyType(newKeyType);
        assertEquals(mapType1, mapType2);
        assertNotEquals(keyType, mapType2.getKeyType());
        assertEquals(valueType, mapType2.getValueType());

        MapType mapType3 = new MapType(Map.class, null, null, null, keyType, valueType, null, null, false);
        MapType mapType4 = mapType3.withKeyType(JavaType.constructType(Map.class, Integer.class));
        assertNotEquals(keyType, mapType4.getKeyType());
        assertEquals(valueType, mapType4.getValueType());
    }

    @Test
    public void testRefine() {
        JavaType keyType = JavaType.constructType(Map.class, String.class);
        JavaType valueType = JavaType.constructType(Map.class, Integer.class);
        MapType mapType1 = new MapType(Map.class, null, null, null, keyType, valueType, null, null, false);
        Class<?> rawType = Map.class;
        TypeBindings bindings = new TypeBindings();
        JavaType superClass = JavaType.constructType(Map.class, Object.class);
        JavaType[] superInterfaces = new JavaType[] { JavaType.constructType(Map.class, Object.class) };
        MapType mapType2 = mapType1.refine(rawType, bindings, superClass, superInterfaces);
        assertEquals(mapType1, mapType2);
        assertEquals(mapType1.getKeyType(), mapType2.getKeyType());
        assertEquals(mapType1.getValueType(), mapType2.getValueType());
        assertEquals(mapType1.getTypeHandler(), mapType2.getTypeHandler());
        assertEquals(mapType1.getValueHandler(), mapType2.getValueHandler());
        assertEquals(mapType1.getTypeBindings(), mapType2.getTypeBindings());
        assertEquals(mapType1.getRawClass(), mapType2.getRawClass());
        assertEquals(mapType1.getSuperClass(), mapType2.getSuperClass());
        assertArrayEquals(mapType1.getSuperInterfaces(), mapType2.getSuperInterfaces());
    }

    @Test
    public void testWithKeyTypeHandler() {
        JavaType keyType = JavaType.constructType(Map.class, String.class);
        JavaType valueType = JavaType.constructType(Map.class, Integer.class);
        Object keyTypeHandler = new Object();
        MapType mapType1 = new MapType(Map.class, null, null, null, keyType, valueType, null, null, false);
        MapType mapType2 = mapType1.withKeyTypeHandler(keyTypeHandler);
        assertEquals(mapType1, mapType2);
        assertNull(mapType2.getKeyTypeHandler());

        MapType mapType3 = new MapType(Map.class, null, null, null, keyType, valueType, null, null, false);
        MapType mapType4 = mapType3.withKeyType(JavaType.constructType(Map.class, Integer.class));
        MapType mapType5 = mapType4.withKeyTypeHandler(keyTypeHandler);
        assertNotNull(mapType5.getKeyTypeHandler());
    }

    @Test
    public void testWithKeyValueHandler() {
        JavaType keyType = JavaType.constructType(Map.class, String.class);
        JavaType valueType = JavaType.constructType(Map.class, Integer.class);
        Object keyValueHandler = new Object();
        MapType mapType1 = new MapType(Map.class, null, null, null, keyType, valueType, null, null, false);
        MapType mapType2 = mapType1.withKeyValueHandler(keyValueHandler);
        assertEquals(mapType1, mapType2);
        assertNull(mapType2.getKeyValueHandler());

        MapType mapType3 = new MapType(Map.class, null, null, null, keyType, valueType, null, null, false);
        MapType mapType4 = mapType3.withKeyType(JavaType.constructType(Map.class, Integer.class));
        MapType mapType5 = mapType4.withKeyValueHandler(keyValueHandler);
        assertNotNull(mapType5.getKeyValueHandler());
    }
}