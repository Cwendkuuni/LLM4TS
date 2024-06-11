package com.fasterxml.jackson.databind.type.test;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.MapLikeType;
import com.fasterxml.jackson.databind.type.MapType;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotSame;

@RunWith(JUnit4.class)
public class MapTypeTest {

    @Test
    public void testMapTypeConstruction() {
        Class<?> rawType = String.class;
        TypeBindings bindings = TypeBindings.empty();
        JavaType superClass = JavaType.construct(Object.class);
        JavaType[] superInts = new JavaType[0];
        JavaType keyType = JavaType.construct(Integer.class);
        JavaType valueType = JavaType.construct(String.class);

        MapType mapType = MapType.construct(rawType, bindings, superClass, superInts, keyType, valueType);
        assertNotNull(mapType);
    }

    @Test
    public void testDeprecatedConstructors() {
        Class<?> rawType = String.class;
        JavaType keyType = JavaType.construct(Integer.class);
        JavaType valueType = JavaType.construct(String.class);

        MapType mapType = MapType.construct(rawType, keyType, valueType);
        MapType mapTypeDeprecated = MapType.construct(rawType, keyType, valueType);

        assertNotSame(mapType, mapTypeDeprecated);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testNarrowMethod() {
        Class<?>rawType = String.class;
        JavaType mapType = MapType.construct(rawType, null, _bogusSuperClass(rawType), null, JavaType.construct(Integer.class), JavaType.construct(String.class));
        mapType._narrow(Object.class);
    }

    @Test
    public void testWithTypeHandlerMethod() {
        Class<?>rawType = String.class;
        JavaType keyType = JavaType.construct(Integer.class);
        JavaType valueType = JavaType.construct(String.class);
        Object typeHandler = new Object();

        MapType mapType = new MapType(rawType, null, _bogusSuperClass(rawType), null, keyType, valueType, null, null, false);
        MapType mapTypeWithTypeHandler = mapType.withTypeHandler(typeHandler);

        assertNotSame(mapType, mapTypeWithTypeHandler);
    }

    @Test
    public void testWithContentTypeHandlerMethod() {
        Class<?>rawType = String.class;
        JavaType keyType = JavaType.construct(Integer.class);
        JavaType valueType = JavaType.construct(String.class);
        Object contentTypeHandler = new Object();

        MapType mapType = new MapType(rawType, null, _bogusSuperClass(rawType), null, keyType, valueType, null, null, false);
        JavaType contentType = mapType.getValueType().withTypeHandler(contentTypeHandler);
        MapType mapTypeWithContentTypeHandler = mapType.withContentTypeHandler(contentTypeHandler);

        assertNotSame(mapType, mapTypeWithContentTypeHandler);
        assertSame(mapType.getValueType(), mapTypeWithContentTypeHandler.getValueType());
    }

    @Test
    public void testWithValueHandlerMethod() {
        Class<?>rawType = String.class;
        JavaType keyType = JavaType.construct(Integer.class);
        JavaType valueType = JavaType.construct(String.class);
        Object valueHandler = new Object();

        MapType mapType = new MapType(rawType, null, _bogusSuperClass(rawType), null, keyType, valueType, null, null, false);
        MapType mapTypeWithValueHandler = mapType.withValueHandler(valueHandler);

        assertNotSame(mapType, mapTypeWithValueHandler);
    }

    @Test
    public void testWithContentValueHandlerMethod() {
        Class<?>rawType = String.class;
        JavaType keyType = JavaType.construct(Integer.class);
        JavaType valueType = JavaType.construct(String.class);
        Object contentValueHandler = new Object();

        MapType mapType = new MapType(rawType, null, _bogusSuperClass(rawType), null, keyType, valueType, null, null, false);
        JavaType contentValueType = mapType.getKeyType().withValueHandler(contentValueHandler);
        MapType mapTypeWithContentValueHandler = mapType.withContentValueHandler(contentValueHandler);

        assertNotSame(mapType, mapTypeWithContentValueHandler);
        assertSame(mapType.getKeyType(), mapTypeWithContentValueHandler.getKeyType());
    }

    @Test
    public void testWithStaticTypingMethod() {
        Class<?> rawType = String.class;
        JavaType keyType = JavaType.construct(Integer.class);
        JavaType valueType = JavaType.construct(String.class);

        MapType mapType = new MapType(rawType, null, _bogusSuperClass(rawType), null, keyType, valueType, null, null, false);
        MapType mapTypeWithStaticTyping = mapType.withStaticTyping();

        assertNotSame(mapType, mapTypeWithStaticTyping);
    }

    @Test
    public void testWithContentTypeMethod() {
        Class<?> rawType = String.class;
        JavaType mapType = MapType.construct(rawType, null, _bogusSuperClass(rawType), null, JavaType.construct(Integer.class), JavaType.construct(String.class));
        JavaType contentType = JavaType.construct(Double.class);

        MapType mapTypeWithContentType = mapType.withContentType(contentType);

        assertNotSame(mapType, mapTypeWithContentType);
        assertSame(mapType.getValueType(), mapTypeWithContentType.getValueType());
    }

    @Test
    public void testWithKeyTypeMethod() {
        Class<?> rawType = String.class;
        JavaType mapType = MapType.construct(rawType, null, _bogusSuperClass(rawType), null, JavaType.construct(Integer.class), JavaType.construct(String.class));
        JavaType keyType = JavaType.construct(Long.class);

        MapType mapTypeWithKeyType = mapType.withKeyType(keyType);

        assertNotSame(mapType, mapTypeWithKeyType);
        assertSame(mapType.getKeyType(), mapTypeWithKeyType.getKeyType());
    }

    @Test
    public void testRefineMethod() {
        Class<?> rawType = String.class;
        TypeBindings bindings = TypeBindings.empty();
        JavaType superClass = JavaType.construct(Object.class);
        JavaType[] superInterfaces = new JavaType[0];

        MapType mapType = MapType.construct(rawType, bindings, _bogusSuperClass(rawType), superInterfaces, JavaType.construct(Integer.class), JavaType.construct(String.class));
        JavaType refinedMapType = mapType.refine(rawType, bindings, superClass, superInterfaces);

        assertNotSame(mapType, refinedMapType);
        assertSame(mapType.getClass(), refinedMapType.getClass());
    }

    @Test
    public void testWithKeyTypeHandlerMethod() {
        Class<?> rawType = String.class;
        JavaType keyType = JavaType.construct(Integer.class);
        JavaType valueType = JavaType.construct(String.class);
        Object keyTypeHandler = new Object();

        MapType mapType = new MapType(rawType, null, _bogusSuperClass(rawType), null, keyType, valueType, null, null, false);
        MapType mapTypeWithKeyTypeHandler = mapType.withKeyTypeHandler(keyTypeHandler);

        assertNotSame(mapType, mapTypeWithKeyTypeHandler);
    }

    @Test
    public void testWithKeyValueHandlerMethod() {
        Class<?> rawType = String.class;
        JavaType keyType = JavaType.construct(Integer.class);
        JavaType valueType = JavaType.construct(String.class);
        Object keyValueHandler = new Object();

        MapType mapType = new MapType(rawType, null, _bogusSuperClass(rawType), null, keyType, valueType, null, null, false);
        MapType mapTypeWithKeyValueHandler = mapType.withKeyValueHandler(keyValueHandler);

        assertNotSame(mapType, mapTypeWithKeyValueHandler);
    }

    @Test
    public void testToStringMethod() {
        Class<?> rawType = String.class;
        JavaType keyType = JavaType.construct(Integer.class);
        JavaType valueType = JavaType.construct(String.class);
        MapType mapType = new MapType(rawType, null, _bogusSuperClass(rawType), null, keyType, valueType, null, null, false);

        String expected = "[map type; class " + rawType.getName() + ", " + keyType + " -> " + valueType + "]";
        String result = mapType.toString();

        assertSame(expected, result);
    }

    private static Class<?> _bogusSuperClass(Class<?> rawType) {
        return Object.class;
    }
}