package com.fasterxml.jackson.databind.type.test;

import static org.junit.Assert.*;

import java.lang.reflect.TypeVariable;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.TypeBase;
import com.fasterxml.jackson.databind.TypeBindings;
import com.fasterxml.jackson.databind.TypeModifier;
import com.fasterxml.jackson.databind.type.MapLikeType;
import com.fasterxml.jackson.databind.type.TypeBase.TypeHandler;

public class MapLikeTypeTest {

    @Test
    public void testMapLikeTypeConstruction() {
        // Test default construction from Map type
        JavaType mapType = new TypeFactory().constructType(Map.class, String.class, Integer.class);
        MapLikeType mapLikeType = new MapLikeType(mapType, TypeBindings.emptyBindings(), _bogusSuperClass(mapType.getRawClass()), null, null, null, null, null, false);
        assertEquals("Map<String, Integer>", mapLikeType.getGenericSignature().toString());

        // Test construction from TypeBase
        JavaType baseType = new TypeFactory().constructType(HashMap.class, String.class, Integer.class);
        TypeBase base = new TypeBase(baseType.getRawClass(), TypeBindings.emptyBindings(), _bogusSuperClass(baseType.getRawClass()), null, null, null, false);
        MapLikeType mapLikeType2 = new MapLikeType(base, null, null, null, null, null, null, null, false);
        assertEquals("Map<String, Integer>", mapLikeType2.getGenericSignature().toString());

        // Test upgrade from JavaType
        JavaType keyType = new TypeFactory().constructType(String.class);
        JavaType valueType = new TypeFactory().constructType(Integer.class);
        JavaType javaType = new TypeFactory().constructType(Map.class, keyType, valueType);
        MapLikeType mapLikeType3 = MapLikeType.upgradeFrom(javaType, keyType, valueType);
        assertEquals("Map<String, Integer>", mapLikeType3.getGenericSignature().toString());
    }

    @Test
    public void testMapLikeTypeMethods() {
        JavaType keyType = new TypeFactory().constructType(String.class);
        JavaType valueType = new TypeFactory().constructType(Integer.class);
        JavaType mapType = new TypeFactory().constructType(Map.class, keyType, valueType);
        MapLikeType mapLikeType = new MapLikeType(mapType, TypeBindings.emptyBindings(), _bogusSuperClass(mapType.getRawClass()), null, keyType, valueType, null, null, false);

        // Test isContainerType and isMapLikeType
        assertTrue(mapLikeType.isContainerType());
        assertTrue(mapLikeType.isMapLikeType());

        // Test getKeyType, getContentType, getContentValueHandler, and getContentTypeHandler
        assertEquals(keyType, mapLikeType.getKeyType());
        assertEquals(valueType, mapLikeType.getContentType());
        assertNull(mapLikeType.getContentValueHandler());
        assertNull(mapLikeType.getContentTypeHandler());

        // Test hasHandlers
        assertFalse(mapLikeType.hasHandlers());

        // Test withKeyType, withContentType, withTypeHandler, withContentTypeHandler, withValueHandler, and withContentValueHandler
        JavaType newKeyType = new TypeFactory().constructType(String[].class);
        JavaType newValueType = new TypeFactory().constructType(Integer[].class);
        MapLikeType newMapLikeType = mapLikeType.withKeyType(newKeyType)
                                              .withContentType(newValueType)
                                              .withTypeHandler(new TypeHandler() {
                                                  @Override
                                                  public Object instantiate(BeanProperty property, JavaType type, BeanDescription beanDesc) throws InstantiationException, IllegalAccessException {
                                                      return new Object();
                                                  }
                                              })
                                              .withContentTypeHandler(new TypeHandler() {
                                                  @Override
                                                  public Object instantiate(BeanProperty property, JavaType type, BeanDescription beanDesc) throws InstantiationException, IllegalAccessException {
                                                      return new Object();
                                                  }
                                              })
                                              .withValueHandler(new TypeHandler() {
                                                  @Override
                                                  public Object instantiate(BeanProperty property, JavaType type, BeanDescription beanDesc) throws InstantiationException, IllegalAccessException {
                                                      return new Object();
                                                  }
                                              })
                                              .withContentValueHandler(new TypeHandler() {
                                                  @Override
                                                  public Object instantiate(BeanProperty property, JavaType type, BeanDescription beanDesc) throws InstantiationException, IllegalAccessException {
                                                      return new Object();
                                                  }
                                              })
                                              .withKeyTypeHandler(new TypeHandler() {
                                                  @Override
                                                  public Object instantiate(BeanProperty property, JavaType type, BeanDescription beanDesc) throws InstantiationException, IllegalAccessException {
                                                      return new Object();
                                                  }
                                              })
                                              .withKeyValueHandler(new TypeHandler() {
                                                  @Override
                                                  public Object instantiate(BeanProperty property, JavaType type, BeanDescription beanDesc) throws InstantiationException, IllegalAccessException {
                                                      return new Object();
                                                  }
                                              });
        assertEquals(newKeyType, newMapLikeType.getKeyType());
        assertEquals(newValueType, newMapLikeType.getContentType());
        assertNotNull(newMapLikeType.getContentValueHandler());
        assertNotNull(newMapLikeType.getContentTypeHandler());

        // Test isTrueMapType
        assertTrue(mapLikeType.isTrueMapType());
        assertFalse(newMapLikeType.isTrueMapType());

        // Test toString
        assertEquals("[map-like type; class java.util.Map, java.lang.String -> java.lang.Integer]", mapLikeType.toString());
        assertEquals("[map-like type; class java.util.HashMap, [Ljava.lang.String; -> [I]", newMapLikeType.toString());

        // Test equals
        MapLikeType equalMapLikeType = mapLikeType;
        assertSame(mapLikeType, equalMapLikeType);
        assertNotSame(mapLikeType, newMapLikeType);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMapLikeTypeInvalidConstruction() {
        JavaType keyType = new TypeFactory().constructType(String.class);
        JavaType valueType = new TypeFactory().constructType(Integer.class);
        JavaType javaType = new TypeFactory().constructType(Map.class, keyType, valueType);
        MapLikeType mapLikeType = new MapLikeType(javaType.getRawClass(), TypeBindings.emptyBindings(), _bogusSuperClass(javaType.getRawClass()), null, keyType, valueType, null, null, false);
        fail("Should have thrown IllegalArgumentException");
    }

    private static Class<?> _bogusSuperClass(Class<?> clazz) {
        return Object.class;
    }
}