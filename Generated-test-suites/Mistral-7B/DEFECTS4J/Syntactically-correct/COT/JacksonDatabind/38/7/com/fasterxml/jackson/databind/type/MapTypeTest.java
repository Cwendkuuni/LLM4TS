package com.fasterxml.jackson.databind.type.test;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.MapLikeType;
import com.fasterxml.jackson.databind.type.MapType;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(JUnit4.class)
public class MapTypeTest {

    @Test
    public void testMapTypeConstructor() {
        Class<Map<String, Integer>> mapType = Map.class;
        TypeBindings bindings = new TypeBindings();
        JavaType superClass = Object.class;
        JavaType[] superInts = new JavaType[0];
        JavaType keyT = String.class;
        JavaType valueT = Integer.class;
        Object valueHandler = null;
        Object typeHandler = null;
        MapType mapTypeObj = MapType.construct(mapType, bindings, superClass, superInts, keyT, valueT);
        assertNotNull(mapTypeObj);
        assertEquals(Map.class, mapTypeObj.getRawClass());
        assertEquals(keyT, mapTypeObj.getKeyType());
        assertEquals(valueT, mapTypeObj.getValueType());
    }

    @Test
    public void testMapTypeWithTypeHandler() {
        Class<Map<String, Integer>> mapType = Map.class;
        TypeBindings bindings = new TypeBindings();
        JavaType superClass = Object.class;
        JavaType[] superInts = new JavaType[0];
        JavaType keyT = String.class;
        JavaType valueT = Integer.class;
        Object valueHandler = new Object() {};
        Object typeHandler = new Object() {};
        MapType mapTypeObj = new MapType(mapType, bindings, superClass, superInts, keyT, valueT, valueHandler, typeHandler, false);
        MapType mapTypeObjWithTypeHandler = mapTypeObj.withTypeHandler(typeHandler);
        assertNotNull(mapTypeObjWithTypeHandler);
        assertEquals(typeHandler, mapTypeObjWithTypeHandler.getTypeHandler());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testDeprecatedConstructWithOnlyKeyTypeAndValueType() throws Exception {
        Class<Map<String, Integer>> mapType = Map.class;
        JavaType keyT = String.class;
        JavaType valueT = Integer.class;
        MapType mapTypeObj = MapType.construct(mapType, keyT, valueT);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testDeprecatedNarrowMethod() throws Exception {
        Class<Map<String, Integer>> mapType = Map.class;
        JavaType mapTypeObj = MapType.construct(mapType, null, _bogusSuperClass(mapType), null, String.class, Integer.class);
        JavaType subclass = HashMap.class;
        MapType narrowedMapTypeObj = mapTypeObj._narrow(subclass);
    }

    @Test
    public void testMapTypeWithContentTypeHandler() {
        Class<Map<String, Integer>> mapType = Map.class;
        TypeBindings bindings = new TypeBindings();
        JavaType superClass = Object.class;
        JavaType[] superInts = new JavaType[0];
        JavaType keyT = String.class;
        JavaType valueT = Integer.class;
        Object valueHandler = null;
        Object typeHandler = new Object() {};
        MapType mapTypeObj = new MapType(mapType, bindings, superClass, superInts, keyT, valueT, valueHandler, typeHandler, false);
        MapType mapTypeObjWithContentTypeHandler = mapTypeObj.withContentTypeHandler(typeHandler);
        assertNotNull(mapTypeObjWithContentTypeHandler);
        assertEquals(typeHandler, mapTypeObjWithContentTypeHandler.getContentTypeHandler());
    }

    @Test
    public void testMapTypeWithValueHandler() {
        Class<Map<String, Integer>> mapType = Map.class;
        TypeBindings bindings = new TypeBindings();
        JavaType superClass = Object.class;
        JavaType[] superInts = new JavaType[0];
        JavaType keyT = String.class;
        JavaType valueT = Integer.class;
        Object valueHandler = new Object() {};
        Object typeHandler = null;
        MapType mapTypeObj = new MapType(mapType, bindings, superClass, superInts, keyT, valueT, valueHandler, typeHandler, false);
        MapType mapTypeObjWithValueHandler = mapTypeObj.withValueHandler(valueHandler);
        assertNotNull(mapTypeObjWithValueHandler);
        assertEquals(valueHandler, mapTypeObjWithValueHandler.getValueHandler());
    }

    @Test
    public void testMapTypeWithContentValueHandler() {
        Class<Map<String, Integer>> mapType = Map.class;
        TypeBindings bindings = new TypeBindings();
        JavaType superClass = Object.class;
        JavaType[] superInts = new JavaType[0];
        JavaType keyT = String.class;
        JavaType valueT = Integer.class;
        Object valueHandler = null;
        Object typeHandler = new Object() {};
        Object contentValueHandler = new Object() {};
        MapType mapTypeObj = new MapType(mapType, bindings, superClass, superInts, keyT, valueT, valueHandler, typeHandler, false);
        MapType mapTypeObjWithContentValueHandler = mapTypeObj.withContentValueHandler(contentValueHandler);
        assertNotNull(mapTypeObjWithContentValueHandler);
        assertEquals(contentValueHandler, mapTypeObjWithContentValueHandler.getContentValueHandler());
    }

    @Test
    public void testMapTypeWithStaticTyping() {
        Class<Map<String, Integer>> mapType = Map.class;
        TypeBindings bindings = new TypeBindings();
        JavaType superClass = Object.class;
        JavaType[] superInts = new JavaType[0];
        JavaType keyT = String.class.getTypeName().equals("java.lang.String") ? String.class : new TypeFactory().constructType(String.class);
        JavaType valueT = Integer.class;
        Object valueHandler = null;
        Object typeHandler = null;
        MapType mapTypeObj = new MapType(mapType, bindings, superClass, superInts, keyT, valueT, valueHandler, typeHandler, false);
        MapType mapTypeObjWithStaticTyping = mapTypeObj.withStaticTyping();
        assertNotNull(mapTypeObjWithStaticTyping);
        assertTrue(mapTypeObjWithStaticTyping.isStatic());
    }

    @Test
    public void testMapTypeWithKeyType() {
        Class<Map<String, Integer>> mapType = Map.class;
        TypeBindings bindings = new TypeBindings();
        JavaType superClass = Object.class;
        JavaType[] superInts = new JavaType[0];
        JavaType keyT = String.class;
        JavaType valueT = Integer.class;
        Object valueHandler = null;
        Object typeHandler = null;
        MapType mapTypeObj = new MapType(mapType, bindings, superClass, superInts, keyT, valueT, valueHandler, typeHandler, false);
        JavaType newKeyT = Class.forName("java.lang.Long");
        MapType mapTypeObjWithNewKeyType = mapTypeObj.withKeyType(newKeyT);
        assertNotNull(mapTypeObjWithNewKeyType);
        assertEquals(newKeyT, mapTypeObjWithNewKeyType.getKeyType());
    }

    @Test
    public void testMapTypeWithContentType() {
        Class<Map<String, Integer>> mapType = Map.class;
        TypeBindings bindings = new TypeBindings();
        JavaType superClass = Object.class;
        JavaType[] superInts = new JavaType[0];
        JavaType keyT = String.class;
        JavaType valueT = Integer.class;
        Object valueHandler = null;
        Object typeHandler = null;
        MapType mapTypeObj = new MapType(mapType, bindings, superClass, superInts, keyT, valueT, valueHandler, typeHandler, false);
        JavaType newContentType = Integer.class.getComponentType();
        MapType mapTypeObjWithNewContentType = mapTypeObj.withContentType(newContentType);
        assertNotNull(mapTypeObjWithNewContentType);
        assertEquals(newContentType, mapTypeObjWithNewContentType.getValueType().getComponentType());
    }

    @Test
    public void testMapTypeRefine() {
        Class<Map<String, Integer>> mapType = Map.class;
        TypeBindings bindings = new TypeBindings();
        JavaType superClass = Object.class;
        JavaType[] superInts = new JavaType[0];
        JavaType keyT = String.class;
        JavaType valueT = Integer.class;
        Object valueHandler = null;
        Object typeHandler = null;
        MapType mapTypeObj = new MapType(mapType, bindings, superClass, superInts, keyT, valueT, valueHandler, typeHandler, false);
        JavaType newMapType = HashMap.class;
        MapType refinedMapTypeObj = mapTypeObj.refine(newMapType.getClass(), bindings, superClass, superInts);
        assertNotNull(refinedMapTypeObj);
        assertEquals(newMapType, refinedMapTypeObj.getRawClass());
    }

    @Test
    public void testMapTypeWithKeyTypeHandler() {
        Class<Map<String, Integer>> mapType = Map.class;
        TypeBindings bindings = new TypeBindings();
        JavaType superClass = Object.class;
        JavaType[] superInts = new JavaType[0];
        JavaType keyT = String.class;
        JavaType valueT = Integer.class;
        Object valueHandler = null;
        Object typeHandler = null;
        Object keyTypeHandler = new Object() {};
        MapType mapTypeObj = new MapType(mapType, bindings, superClass, superInts, keyT, valueT, valueHandler, typeHandler, false);
        MapType mapTypeObjWithKeyTypeHandler = mapTypeObj.withKeyTypeHandler(keyTypeHandler);
        assertNotNull(mapTypeObjWithKeyTypeHandler);
        assertEquals(keyTypeHandler, mapTypeObjWithKeyTypeHandler.getKeyTypeHandler());
    }

    @Test
    public void testMapTypeWithKeyValueHandler() {
        Class<Map<String, Integer>> mapType = Map.class;
        TypeBindings bindings = new TypeBindings();
        JavaType superClass = Object.class;
        JavaType[] superInts = new JavaType[0];
        JavaType keyT = String.class;
        JavaType valueT = Integer.class;
        Object valueHandler = new Object() {};
        Object typeHandler = null;
        Object keyValueHandler = new Object() {};
        MapType mapTypeObj = new MapType(mapType, bindings, superClass, superInts, keyT, valueT, valueHandler, typeHandler, false);
        MapType mapTypeObjWithKeyValueHandler = mapTypeObj.withKeyValueHandler(keyValueHandler);
        assertNotNull(mapTypeObjWithKeyValueHandler);
        assertEquals(keyValueHandler, mapTypeObjWithKeyValueHandler.getKeyValueHandler());
    }

    private static Class<?> _bogusSuperClass(Class<?> rawType) {
        return Object.class;
    }
}