package com.yourpackagename.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

import java.lang.ClassCastException;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import com.fasterxml.jackson.core.type.TypeBindings;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.MapType;
import com.fasterxml.jackson.databind.type.TypeFactory;

@RunWith(Parameterized.class)
public class MapTypeTest {

    private JavaType keyType;
    private JavaType valueType;
    private JavaType mapType;

    @Parameters
    public static Iterable<Object[]> data() {
        TypeFactory tf = TypeFactory.defaultInstance();
        return new Iterable<Object[]>() {
            @Override
            public Iterator<Object[]> iterator() {
                return new Iterator<Object[]>() {
                    int index = 0;
                    @Override
                    public boolean hasNext() {
                        return index < 3;
                    }

                    @Override
                    public Object[] next() {
                        JavaType keyType = tf.constructType(Map.class, String.class);
                        JavaType valueType = tf.constructType(Map.class, Integer.class);
                        JavaType mapType = MapType.construct(Map.class, TypeBindings.empty(), null, new Class[0], keyType, valueType);
                        return new Object[] { keyType, valueType, mapType };
                    }
                };
            }
        };
    }

    @Test
    public void testConstructMethod() {
        assertEquals(Map.class, mapType.getRawClass());
        assertEquals(keyType, mapType.getKeyType());
        assertEquals(valueType, mapType.getValueType());
        assertEquals(TypeBindings.empty(), mapType.getTypeBindings());
        assertNull(mapType.getValueHandler());
        assertNull(mapType.getTypeHandler());
        assertFalse(mapType.isStatic());
    }

    @Test
    public void testDeprecatedConstructors() {
        try {
            MapType deprecatedMapType1 = MapType.construct(String[].class, keyType, valueType);
            fail("Expected ClassCastException");
        } catch (ClassCastException e) {
            // expected
        }

        try {
            MapType deprecatedMapType2 = new MapType(String[].class, null, _bogusSuperClass(String[].class), null, keyType, valueType);
            fail("Expected ClassCastException");
        } catch (ClassCastException e) {
            // expected
        }
    }

    @Test
    public void testNarrowMethod() throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        JavaType subclass = tf.constructType(HashMap.class, String.class, Integer.class);
        MapType narrowedMapType = mapType.narrow(subclass.getRawClass());
        assertEquals(subclass.getRawClass(), narrowedMapType.getRawClass());
        assertEquals(keyType, narrowedMapType.getKeyType());
        assertEquals(valueType, narrowedMapType.getValueType());
        assertEquals(TypeBindings.empty(), narrowedMapType.getTypeBindings());
        assertNull(narrowedMapType.getValueHandler());
        assertNull(narrowedMapType.getTypeHandler());
        assertFalse(narrowedMapType.isStatic());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testNarrowMethodWithIncompatibleType() throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        JavaType incompatibleType = tf.constructType(ArrayList.class, String.class, Integer.class);
        mapType.narrow(incompatibleType.getRawClass());
    }

    @Test
    public void testWithTypeHandlerMethod() {
        Object handler = new Object();
        MapType withTypeHandlerMapType = mapType.withTypeHandler(handler);
        assertEquals(mapType.getRawClass(), withTypeHandlerMapType.getRawClass());
        assertEquals(keyType, withTypeHandlerMapType.getKeyType());
        assertEquals(valueType, withTypeHandlerMapType.getValueType());
        assertEquals(TypeBindings.empty(), withTypeHandlerMapType.getTypeBindings());
        assertEquals(handler, withTypeHandlerMapType.getTypeHandler());
        assertNull(withTypeHandlerMapType.getValueHandler());
        assertFalse(withTypeHandlerMapType.isStatic());
    }

    @Test
    public void testWithValueHandlerMethod() {
        Object handler = new Object();
        MapType withValueHandlerMapType = mapType.withValueHandler(handler);
        assertEquals(mapType.getRawClass(), withValueHandlerMapType.getRawClass());
        assertEquals(keyType, withValueHandlerMapType.getKeyType());
        assertEquals(valueType, withValueHandlerMapType.getValueType());
        assertEquals(TypeBindings.empty(), withValueHandlerMapType.getTypeBindings());
        assertNull(withTypeHandlerMapType.getTypeHandler());
        assertEquals(handler, withValueHandlerMapType.getValueHandler());
        assertFalse(withValueHandlerMapType.isStatic());
    }

    @Test
    public void testWithContentTypeHandlerMethod() {
        Object handler = new Object();
        JavaType contentType = tf.constructType(HashMap.class, String.class, String.class);
        MapType withContentTypeHandlerMapType = mapType.withContentTypeHandler(handler);
        assertEquals(mapType.getRawClass(), withContentTypeHandlerMapType.getRawClass());
        assertEquals(keyType, withContentTypeHandlerMapType.getKeyType());
        JavaType withContentTypeHandlerValueType = withContentTypeHandlerMapType.getValueType();
        assertEquals(valueType.getClass(), withContentTypeHandlerValueType.getClass());
        assertEquals(TypeBindings.empty(), withContentTypeHandlerMapType.getTypeBindings());
        assertNull(withContentTypeHandlerMapType.getTypeHandler());
        assertNull(withContentTypeHandlerMapType.getValueHandler());
        assertFalse(withContentTypeHandlerMapType.isStatic());
        assertEquals(handler, withContentTypeHandlerMapType.getValueHandler());
    }

    @Test
    public void testWithContentValueHandlerMethod() {
        Object handler = new Object();
        JavaType contentValueType = tf.constructType(HashMap.class, String.class, Float.class);
        MapType withContentValueHandlerMapType = mapType.withContentValueHandler(handler);
        assertEquals(mapType.getRawClass(), withContentValueHandlerMapType.getRawClass());
        assertEquals(keyType, withContentValueHandlerMapType.getKeyType());
        JavaType withContentValueHandlerValueType = withContentValueHandlerMapType.getValueType();
        assertEquals(valueType.getClass(), withContentValueHandlerValueType.getClass());
        assertEquals(TypeBindings.empty(), withContentValueHandlerMapType.getTypeBindings());
        assertNull(withContentValueHandlerMapType.getTypeHandler());
        assertEquals(handler, withContentValueHandlerMapType.getValueHandler());
        assertFalse(withContentValueHandlerMapType.isStatic());
    }

    @Test
    public void testWithStaticTypingMethod() {
        MapType withStaticTypingMapType = mapType.withStaticTyping();
        assertEquals(mapType.getRawClass(), withStaticTypingMapType.getRawClass());
        assertEquals(keyType.withStaticTyping(), withStaticTypingMapType.getKeyType());
        assertEquals(valueType.withStaticTyping(), withStaticTypingMapType.getValueType());
        assertEquals(TypeBindings.empty(), withStaticTypingMapType.getTypeBindings());
        assertNull(withStaticTypingMapType.getValueHandler());
        assertNull(withStaticTypingMapType.getTypeHandler());
        assertTrue(withStaticTypingMapType.isStatic());
    }

    @Test
    public void testWithValueHandlerMethodWithNullHandler() {
        assertThrows(NullPointerException.class, () -> mapType.withValueHandler(null));
    }

    @Test
    public void testWithTypeHandlerMethodWithNullHandler() {
        assertThrows(NullPointerException.class, () -> mapType.withTypeHandler(null));
    }

    @Test
    public void testToStringMethod() {
        String expectedToString = "[map type; class java.util.Map, java.lang.String -> java.lang.Integer]";
        assertEquals(expectedToString, mapType.toString());
    }
}