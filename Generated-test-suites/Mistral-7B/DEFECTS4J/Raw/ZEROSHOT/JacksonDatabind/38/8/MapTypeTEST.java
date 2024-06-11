package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.*;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import com.fasterxml.jackson.core.type.TypeBinding;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;

public class MapTypeTest {

    @Test
    public void testConstruct() {
        Type rawType = Map.class;
        Type keyType = String.class;
        Type valueType = Integer.class;

        MapType mapType = MapType.construct(rawType, null, null, null, keyType, valueType);
        JavaType javaMapType = mapType.getRawType();
        assertEquals(Map.class, javaMapType.getRawClass());
        assertEquals(String.class, mapType.getKeyType().getRawType());
        assertEquals(Integer.class, mapType.getValueType().getRawType());
    }

    @Test
    public void testConstructWithBindings() {
        Type rawType = Map.class;
        JavaType superClass = null;
        Type[] superInterfaces = new Type[] { List.class };
        Type keyType = String.class;
        Type valueType = Integer.class;

        MapBindings bindings = new MapBindings();
        bindings.addBinding(String.class, new StringDeserializer());
        bindings.addBinding(Integer.class, new IntDeserializer());

        MapType mapType = MapType.construct(rawType, bindings, superClass, superInterfaces, keyType, valueType);
        JavaType javaMapType = mapType.getRawType();
        assertEquals(Map.class, javaMapType.getRawClass());
        assertEquals(String.class, mapType.getKeyType().getRawType());
        assertEquals(Integer.class, mapType.getValueType().getRawType());
    }

    @Test
    public void testNarrow() {
        Type rawType = HashMap.class;
        JavaType parentMapType = MapType.construct(Map.class, null, null, null, String.class, Integer.class);

        MapType narrowedMapType = (MapType) parentMapType.narrow(HashMap.class);
        JavaType javaNarrowedMapType = narrowedMapType.getRawType();
        assertEquals(HashMap.class, javaNarrowedMapType.getRawClass());
    }

    @Test
    public void testWithTypeHandler() {
        Type rawType = Map.class;
        JavaType keyType = String.class;
        JavaType valueType = Integer.class;
        Object valueHandler = new ValueHandler();

        MapType mapType = new MapType(rawType, null, null, null, keyType, valueType);
        MapType withValueHandlerMapType = mapType.withValueHandler(valueHandler);
        assertEquals(valueHandler, withValueHandlerMapType.getValueHandler());
    }

    @Test
    public void testWithContentTypeHandler() {
        Type rawType = Map.class;
        JavaType keyType = String.class;
        JavaType valueType = Integer.class;
        Object contentTypeHandler = new ContentTypeHandler();

        MapType mapType = new MapType(rawType, null, null, null, keyType, valueType);
        MapType withContentTypeHandlerMapType = mapType.withContentTypeHandler(contentTypeHandler);
        assertEquals(contentTypeHandler, withContentTypeHandlerMapType.getTypeHandler());
    }

    @Test
    public void testWithStaticTyping() {
        Type rawType = Map.class;
        JavaType keyType = String.class;
        JavaType valueType = Integer.class;

        MapType dynamicMapType = new MapType(rawType, null, null, null, keyType, valueType);
        MapType staticMapType = dynamicMapType.withStaticTyping();
        assertTrue(staticMapType.isStatic());
    }

    @Test
    public void testWithContentType() {
        Type rawType = Map.class;
        JavaType keyType = String.class;
        JavaType valueType = Integer.class;
        JavaType contentType = String[].class;

        MapType mapType = new MapType(rawType, null, null, null, keyType, valueType);
        MapType withContentTypeMapType = mapType.withContentType(contentType);
        assertEquals(contentType, withContentTypeMapType.getValueType());
    }

    @Test
    public void testWithKeyType() {
        Type rawType = Map.class;
        JavaType keyType1 = String.class;
        JavaType valueType = Integer.class;
        JavaType keyType2 = Long.class;

        MapType mapType = new MapType(rawType, null, null, null, keyType1, valueType);
        MapType withKeyTypeMapType = mapType.withKeyType(keyType2);
        assertEquals(keyType2, withKeyTypeMapType.getKeyType());
    }

    @Test
    public void testRefine() {
        Type rawType = HashMap.class;
        JavaType superClass = null;
        Type[] superInterfaces = new Type[] { List.class };
        JavaType keyType = String.class;
        JavaType valueType = Integer.class;

        MapType mapType = new MapType(rawType, null, superClass, superInterfaces, keyType, valueType);
        JavaType refinedMapType = mapType.refine(HashSet.class, null, null, null);
        JavaType javaRefinedMapType = refinedMapType.getRawType();
        assertEquals(HashSet.class, javaRefinedMapType.getRawClass());
    }

    @Test
    public void testToString() {
        Type rawType = Map.class;
        JavaType keyType = String.class;
        JavaType valueType = Integer.class;

        MapType mapType = new MapType(rawType, null, null, null, keyType, valueType);
        String expected = "[map type; class " + Map.class.getName() + ", " + String.class.getName() + " -> " + Integer.class.getName() + "]";
        String actual = mapType.toString();
        assertEquals(expected, actual);
    }

    private static class MapBindings extends TypeBindings {
        // empty implementation
    }

    private static class StringDeserializer implements TypeDeserializer {
        // empty implementation
    }

    private static class IntDeserializer implements TypeDeserializer {
        // empty implementation
    }

    private static class ValueHandler implements TypeSerializer {
        // empty implementation
    }

    private static class ContentTypeHandler implements TypeSerializer {
        // empty implementation
    }
}