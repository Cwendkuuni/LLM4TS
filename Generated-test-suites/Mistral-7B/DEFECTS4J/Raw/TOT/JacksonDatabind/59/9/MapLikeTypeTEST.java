package com.fasterxml.jackson.databind.type.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.MapLikeType;
import com.fasterxml.jackson.databind.type.TypeBase;
import com.fasterxml.jackson.databind.type.TypeBindings;
import com.fasterxml.jackson.databind.type.TypeFactory;

public class MapLikeTypeTest {

    @Test
    public void testMapLikeTypeCreation() {
        // Test creation from Map class
        JavaType mapType = TypeFactory.defaultInstance().constructCollectionType(Map.class, String.class, Integer.class);
        MapLikeType mapLikeType = new MapLikeType(mapType.getRawClass(), TypeBindings.emptyBindings(), null, null, StringType.instance, IntegerType.instance);
        assertEquals(String.class, mapLikeType.getKeyType().getRawClass());
        assertEquals(Integer.class, mapLikeType.getValueType().getRawClass());

        // Test creation from TypeBase
        TypeBase baseType = new TypeBase(Integer.class, null, null, 0, null, null, false);
        JavaType javaType = MapLikeType.upgradeFrom(baseType, StringType.instance, IntegerType.instance);
        MapLikeType upgradedType = (MapLikeType) javaType;
        assertEquals(Integer.class, upgradedType.getKeyType().getRawClass());
        assertEquals(Integer.class, upgradedType.getValueType().getRawClass());
    }

    @Test
    public void testMapLikeTypeModifiers() {
        // Test withKeyType()
        JavaType mapType = TypeFactory.defaultInstance().constructCollectionType(Map.class, String.class, Integer.class);
        MapLikeType mapLikeType = new MapLikeType(mapType.getRawClass(), TypeBindings.emptyBindings(), null, null, StringType.instance, IntegerType.instance);
        JavaType newKeyType = TypeFactory.defaultInstance().constructType(Long.class);
        MapLikeType modifiedType = mapLikeType.withKeyType(newKeyType);
        assertEquals(Long.class, modifiedType.getKeyType().getRawClass());
        assertEquals(Integer.class, modifiedType.getValueType().getRawClass());

        // Test withContentType()
        JavaType newValueType = TypeFactory.defaultInstance().constructType(Double.class);
        MapLikeType newMapLikeType = modifiedType.withContentType(newValueType);
        assertEquals(Long.class, newMapLikeType.getKeyType().getRawClass());
        assertEquals(Double.class, newMapLikeType.getValueType().getRawClass());

        // Test withTypeHandler()
        Object handler = new Object() {
            public void handle(MapLikeType mapType) {
                // handle implementation
            }
        };
        MapLikeType withHandlerType = newMapLikeType.withTypeHandler(handler);
        assertNotNull(withHandlerType.getTypeHandler());

        // Test withContentTypeHandler()
        MapLikeType withContentHandlerType = newMapLikeType.withContentTypeHandler(handler);
        assertNotNull(withContentHandlerType.getContentTypeHandler());

        // Test withValueHandler()
        MapLikeType withValueHandlerType = newMapLikeType.withValueHandler(handler);
        assertNotNull(withValueHandlerType.getValueHandler());

        // Test withContentValueHandler()
        MapLikeType withContentValueHandlerType = newmapLikeType.withContentValueHandler(handler);
        assertNotNull(withContentValueHandlerType.getContentValueHandler());

        // Test withStaticTyping()
        MapLikeType staticType = mapLikeType.withStaticTyping();
        assertTrue(staticType.isStatic());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidBaseType() {
        JavaType baseType = TypeFactory.defaultInstance().constructType(Long.class);
        JavaType keyType = TypeFactory.defaultInstance().constructType(String.class);
        JavaType valueType = TypeFactory.defaultInstance().constructType(Integer.class);
        MapLikeType mapLikeType = new MapLikeType(baseType, TypeBindings.emptyBindings(), null, null, keyType, valueType);
        MapLikeType invalidMapLikeType = mapLikeType.withKeyType(valueType);
        // This should throw an IllegalArgumentException
    }

    @Test
    public void testToString() {
        JavaType mapType = TypeFactory.defaultInstance().constructCollectionType(Map.class, String.class, Integer.class);
        MapLikeType mapLikeType = new MapLikeType(mapType.getRawClass(), TypeBindings.emptyBindings(), null, null, StringType.instance, IntegerType.instance);
        assertEquals("{map-like type; class class java.util.Map<java.lang.String, java.lang.Integer>, <java.lang.String, java.lang.Integer>}", mapLikeType.toString());
    }

    @Test
    public void testEquals() {
        JavaType mapType1 = TypeFactory.defaultInstance().constructCollectionType(Map.class, String.class, Integer.class);
        MapLikeType mapLikeType1 = new MapLikeType(mapType1.getRawClass(), TypeBindings.emptyBindings(), null, null, StringType.instance, IntegerType.instance);

        JavaType mapType2 = TypeFactory.defaultInstance().constructCollectionType(Map.class, String.class, Integer.class);
        MapLikeType mapLikeType2 = new MapLikeType(mapType2.getRawClass(), TypeBindings.emptyBindings(), null, null, StringType.instance, IntegerType.instance);

        JavaType mapType3 = TypeFactory.defaultInstance().constructCollectionType(List.class, String.class, Integer.class);
        MapLikeType mapLikeType3 = new MapLikeType(mapType3.getRawClass(), TypeBindings.emptyBindings(), null, null, StringType.instance, IntegerType.instance);

        assertEquals(mapLikeType1, mapLikeType1);
        assertEquals(mapLikeType1, mapLikeType2);
        assertNotEquals(mapLikeType1, mapLikeType3);
    }
}