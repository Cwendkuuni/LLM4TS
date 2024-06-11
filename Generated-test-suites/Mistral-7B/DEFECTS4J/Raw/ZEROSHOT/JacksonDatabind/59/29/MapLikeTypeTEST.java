package com.fasterxml.jackson.databind.type.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.lang.reflect.TypeVariable;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.TypeBindings;
import com.fasterxml.jackson.databind.TypeResolverBuilder;
import com.fasterxml.jackson.databind.type.MapLikeType;
import com.fasterxml.jackson.databind.type.TypeBase;
import com.fasterxml.jackson.databind.type.TypeBindings.TypeBindingsBuilder;
import com.fasterxml.jackson.databind.type.TypeFactory;

public class MapLikeTypeTest {

    @Test
    public void testMapLikeTypeConstruction() {
        JavaType stringMapType = TypeFactory.defaultInstance()
                .constructType(Map.class, String.class, String.class);
        MapLikeType mapLikeType = new MapLikeType(stringMapType.getTypeId(),
                TypeBindings.emptyBindings(), null, null, null, null, null, null,
                false);
        assertEquals(String.class, mapLikeType.getKeyType().getRawClass());
        assertEquals(String.class, mapLikeType.getValueType().getRawClass());
    }

    @Test
    public void testMapLikeTypeWithKeyType() {
        JavaType<HashMap<String, Integer>> mapType = TypeFactory
                .defaultInstance().constructType(HashMap.class, String.class,
                        Integer.class);
        MapLikeType mapLikeType = new MapLikeType(mapType.getTypeId(),
                TypeBindings.emptyBindings(), null, null, null, null, null, null,
                false);
        JavaType<String> keyType = new MapLikeType(mapType.getTypeId(),
                mapLikeType.getBindings(), null, null, JavaType.of(String.class),
                null, null, null, false);
        assertEquals(String.class, keyType.getRawClass());
        mapLikeType = mapLikeType.withKeyType(JavaType.of(Integer.class));
        assertEquals(Integer.class, mapLikeType.getKeyType().getRawClass());
    }

    @Test
    public void testMapLikeTypeWithValueType() {
        JavaType<HashMap<String, Integer>> mapType = TypeFactory
                .defaultInstance().constructType(HashMap.class, String.class,
                        Integer.class);
        MapLikeType mapLikeType = new MapLikeType(mapType.getTypeId(),
                TypeBindings.emptyBindings(), null, null, null, null, null, null,
                false);
        JavaType<Integer> valueType = new MapLikeType(mapType.getTypeId(),
                mapLikeType.getBindings(), null, null, null, JavaType.of(Integer.class),
                null, null, false);
        assertEquals(Integer.class, valueType.getRawClass());
        mapLikeType = mapLikeType.withValueType(JavaType.of(Double.class));
        assertEquals(Double.class, mapLikeType.getValueType().getRawClass());
    }

    @Test
    public void testMapLikeTypeUpgrade() {
        JavaType listType = TypeFactory.defaultInstance()
                .constructType(ArrayList.class, String.class);
        JavaType<Map<String, Integer>> mapType = TypeFactory
                .defaultInstance().upgradeFrom(listType, JavaType.of(String.class),
                        JavaType.of(Integer.class));
        MapLikeType mapLikeType = (MapLikeType) mapType;
        assertEquals(String.class, mapLikeType.getKeyType().getRawClass());
        assertEquals(Integer.class, mapLikeType.getValueType().getRawClass());
    }

    @Test
    public void testMapLikeTypeHasHandlers() {
        JavaType<HashMap<String, Integer>> mapType = TypeFactory
                .defaultInstance().constructType(HashMap.class, String.class,
                        Integer.class);
        MapLikeType mapLikeType = new MapLikeType(mapType.getTypeId(),
                TypeBindings.emptyBindings(), null, null, null, null, null, null,
                false);
        TypeResolverBuilder builder = TypeResolverBuilder.base();
        assertNotNull(mapLikeType.getValueType().getValueHandler());
        assertNotNull(mapLikeType.getValueType().getTypeHandler());
        assertNotNull(mapLikeType.getKeyType().getValueHandler());
        assertNotNull(mapLikeType.getKeyType().getTypeHandler());
        TypeBindings bindings = new TypeBindingsBuilder()
                .bind(String.class, String.class, StringSerializer.class)
                .bind(Integer.class, Integer.TYPE, IntegerDeserializer.class)
                .build();
        mapLikeType = new MapLikeType(mapLikeType);
        mapLikeType = mapLikeType.withTypeHandler(builder.introspect(mapLikeType)
                .findValueSerializer(mapLikeType, null));
        mapLikeType = mapLikeType.withKeyTypeHandler(builder.introspect(mapLikeType)
                .findValueSerializer(mapLikeType.getKeyType(), null));
        assertEquals(StringSerializer.class, mapLikeType.getValueHandler());
        assertEquals(StringSerializer.class, mapLikeType.getKeyType()
                .getValueHandler());
    }
}