package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.fasterxml.jackson.databind.util.ClassUtil;

import java.io.IOException;
import java.util.*;

public class ClassNameIdResolverTest {
    private static final JsonFactory JSON_FACTORY = new JsonFactory();
    private static final ObjectMapper MAPPER = new ObjectMapper(JSON_FACTORY);

    @Test
    public void testIdFromValue() throws IOException {
        ClassNameIdResolver resolver = new ClassNameIdResolver(String.class.getTypeName(), MAPPER.getTypeFactory());

        Object value = new Object();
        String id = resolver.idFromValue(value);
        assertNull(id);

        String className = Integer.class.getName();
        id = resolver.idFromValue(Integer.valueOf(1));
        assertEquals(className, id);

        className = List.class.getName();
        List list = Arrays.asList(1, 2, 3);
        id = resolver.idFromValue(list);
        assertEquals(className + "<Integer>", id);

        className = EnumSet.class.getName();
        EnumSet<DayOfWeek> set = EnumSet.allOf(DayOfWeek.class);
        id = resolver.idFromValue(set);
        assertEquals(className + "<DayOfWeek>", id);

        className = EnumMap.class.getName();
        EnumMap<DayOfWeek, String> map = new EnumMap<>(DayOfWeek.class);
        map.put(DayOfWeek.MONDAY, "Monday");
        id = resolver.idFromValue(map);
        assertEquals(className + "<DayOfWeek, java.lang.String>", id);

        className = Map.class.getName();
        Map<String, String> map2 = new HashMap<>();
        id = resolver.idFromValue(map2);
        assertEquals(className + "<java.lang.String, java.lang.String>", id);

        className = Collections.class.getName() + ".unmodifiableMap";
        Map<String, String> unmodifiableMap = Collections.unmodifiableMap(map2);
        id = resolver.idFromValue(unmodifiableMap);
        assertEquals(className + "<java.lang.String, java.lang.String>", id);

        className = Arrays.class.getName() + ".asList";
        List<Integer> list2 = Arrays.asList(1, 2, 3);
        id = resolver.idFromValue(list2);
        assertEquals("java.util.ArrayList", id);
    }

    @Test
    public void testIdFromValueAndType() throws IOException {
        ClassNameIdResolver resolver = new ClassNameIdResolver(String.class.getTypeName(), MAPPER.getTypeFactory());

        Object value = new Object();
        Class<?> type = String.class;
        String id = resolver.idFromValueAndType(value, type);
        assertNull(id);

        value = Integer.valueOf(1);
        id = resolver.idFromValueAndType(value, Integer.class);
        assertEquals(Integer.class.getName(), id);

        value = Arrays.asList(1, 2, 3);
        id = resolver.idFromValueAndType(value, List.class);
        assertEquals(List.class.getName() + "<Integer>", id);

        value = EnumSet.allOf(DayOfWeek.class);
        id = resolver.idFromValueAndType(value, EnumSet.class);
        assertEquals(EnumSet.class.getName() + "<DayOfWeek>", id);

        value = new EnumMap<>(DayOfWeek.class);
        id = resolver.idFromValueAndType(value, EnumMap.class);
        assertEquals(EnumMap.class.getName() + "<DayOfWeek, java.lang.Object>", id);

        value = new HashMap<>();
        id = resolver.idFromValueAndType(value, Map.class);
        assertEquals(Map.class.getName() + "<java.lang.Object, java.lang.Object>", id);

        value = Collections.unmodifiableMap(new HashMap<>());
        id = resolver.idFromValueAndType(value, Map.class);
        assertEquals(Map.class.getName() + "<java.lang.Object, java.lang.Object>", id);

        value = Arrays.asList(1, 2, 3);
        id = resolver.idFromValueAndType(value, List.class);
        assertEquals("java.util.ArrayList", id);
    }

    @Test
    public void testTypeFromId() throws IOException {
        ClassNameIdResolver resolver = new ClassNameIdResolver(String.class.getTypeName(), MAPPER.getTypeFactory());

        String id = "java.lang.String";
        JavaType javaType = resolver.typeFromId(JSON_FACTORY, id);
        assertEquals(String.class, javaType.getRawClass());

        id = "java.util.List<java.lang.Integer>";
        javaType = resolver.typeFromId(JSON_FACTORY, id);
        assert javaType.isCollectionType();
        assertEquals(List.class, javaType.getRawClass());
        assertEquals(Integer.class, javaType.getContentType());

        id = "java.util.EnumSet< java.dayofweek >";
        javaType = resolver.typeFromId(JSON_FACTORY, id);
        assert javaType.isContainerType();
        assert javaType.hasGenericTypes();
        assertEquals(EnumSet.class, javaType.getRawClass());
        assertEquals(DayOfWeek.class, javaType.getContentType());

        id = "java.util.EnumMap< java.dayofweek, java.lang.String >";
        javaType = resolver.typeFromId(JSON_FACTORY, id);
        assert javaType.isContainerType();
        assert javaType.hasGenericTypes();
        assertEquals(EnumMap.class, javaType.getRawClass());
        assertEquals(DayOfWeek.class, javaType.getKeyType());
        assertEquals(String.class, javaType.getContentType());

        id = "java.util.Map< java.lang.String, java.lang.String >";
        javaType = resolver.typeFromId(JSON_FACTORY, id);
        assert javaType.isMapType();
        assert javaType.hasGenericTypes();
        assertEquals(Map.class, javaType.getRawClass());
        assertEquals(String.class, javaType.getKeyType());
        assertEquals(String.class, javaType.getContentType());

        id = "java.util.Collections$UnmodifiableMap< java.lang.String, java.lang.String >";
        javaType = resolver.typeFromId(JSON_FACTORY, id);
        assert javaType.isMapType();
        assert javaType.hasGenericTypes();
        assertEquals(HashMap.class, javaType.getRawClass());
        assertEquals(String.class, javaType.getKeyType());
        assertEquals(String.class, javaType.getContentType());

        id = "java.util.Arrays$ArrayList";
        javaType = resolver.typeFromId(JSON_FACTORY, id);
        assert javaType.isCollectionType();
        assertEquals(ArrayList.class, javaType.getRawClass());
    }
}