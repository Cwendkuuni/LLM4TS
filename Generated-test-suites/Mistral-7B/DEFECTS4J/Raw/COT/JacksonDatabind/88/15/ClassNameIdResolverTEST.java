package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.EnumMap;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.fasterxml.jackson.databind.util.ClassUtil;

import org.junit.Test;

public class ClassNameIdResolverTest {

    private static final TypeFactory typeFactory = new TypeFactoryBuilder()
            .build();

    @Test
    public void testgetIdFromValue() throws IOException {
        Object value = new Object();
        ClassNameIdResolver resolver = new ClassNameIdResolver(null, typeFactory);
        String id = resolver.idFromValue(value);
        assertEquals("java.lang.Object", id);
    }

    @Test
    public void testgetIdFromValueAndType() throws IOException {
        Object value = new Object();
        Class<?> type = String.class;
        ClassNameIdResolver resolver = new ClassNameIdResolver(null, typeFactory);
        String id = resolver.idFromValueAndType(value, type);
        assertEquals("java.lang.Object", id);
    }

    @Test
    public void testTypeFromId() throws IOException {
        DatabindContext context = new JacksonAnnotationIntrospector().findContextForAnnotation(null);
        ClassNameIdResolver resolver = new ClassNameIdResolver(null, typeFactory);
        String id = "java.lang.String";
        JavaType javaType = resolver.typeFromId(context, id);
        assertEquals(String.class, javaType.getRawClass());
    }

    @Test
    public void testTypeFromIdUnknownClass() throws IOException {
        DatabindContext context = new JacksonAnnotationIntrospector().findContextForAnnotation(null);
        ClassNameIdResolver resolver = new ClassNameIdResolver(null, typeFactory);
        String id = "com.unknown.UnknownClass";
        JavaType javaType = resolver.typeFromId(context, id);
        assertNull(javaType);
    }

    @Test
    public void testTypeFromIdEnum() throws IOException {
        DatabindContext context = new JacksonAnnotationIntrospector().findContextForAnnotation(null);
        ClassNameIdResolver resolver = new ClassNameIdResolver(null, typeFactory);
        String id = "java.lang.String";
        JavaType javaType = resolver.typeFromId(context, id);
        assertEquals(String.class, javaType.getRawClass());

        Enum e = Enum.valueOf(String.class, "VALUE");
        ClassNameIdResolver resolverEnum = new ClassNameIdResolver(null, typeFactory);
        id = e.getClass().getName();
        javaType = resolverEnum.typeFromId(context, id);
        assertEquals(String.class, javaType.getRawClass());
    }

    @Test
    public void testTypeFromIdEnumMap() throws IOException {
        DatabindContext context = new JacksonAnnotationIntrospector().findContextForAnnotation(null);
        ClassNameIdResolver resolver = new ClassNameIdResolver(null, typeFactory);
        String id = "java.util.EnumMap";
        JavaType javaType = resolver.typeFromId(context, id);
        assertEquals(EnumMap.class, javaType.getRawClass());

        Enum e = Enum.valueOf(String.class, "VALUE");
        Map<Enum, String> map = new EnumMap<>(e.getClass());
        map.put(e, "Value");
        ClassNameIdResolver resolverEnumMap = new ClassNameIdResolver(null, typeFactory);
        id = map.getClass().getCanonicalName();
        javaType = resolverEnumMap.typeFromId(context, id);
        assertEquals(EnumMap.class, javaType.getRawClass());
    }

    @Test
    public void testTypeFromIdEnumSet() throws IOException {
        DatabindContext context = new JacksonAnnotationIntrospector().findContextForAnnotation(null);
        ClassNameIdResolver resolver = new ClassNameIdResolver(null, typeFactory);
        String id = "java.util.EnumSet";
        JavaType javaType = resolver.typeFromId(context, id);
        assertEquals(EnumSet.class, javaType.getRawClass());

        Enum e = Enum.valueOf(String.class, "VALUE");
        EnumSet<Enum> set = EnumSet.allOf(e.getClass());
        ClassNameIdResolver resolverEnumSet = new ClassNameIdResolver(null, typeFactory);
        id = set.getClass().getCanonicalName();
        javaType = resolverEnumSet.typeFromId(context, id);
        assertEquals(EnumSet.class, javaType.getRawClass());
    }

    @Test
    public void testTypeFromIdList() throws IOException {
        DatabindContext context = new JacksonAnnotationIntrospector().findContextForAnnotation(null);
        ClassNameIdResolver resolver = new ClassNameIdResolver(null, typeFactory);
        String id = "java.util.List";
        JavaType javaType = resolver.typeFromId(context, id);
        assertEquals(List.class, javaType.getRawClass());

        List<String> list = Arrays.asList("String1", "String2");
        ClassNameIdResolver resolverList = new ClassNameIdResolver(null, typeFactory);
        id = list.getClass().getCanonicalName();
        javaType = resolverList.typeFromId(context, id);
        assertEquals(ArrayList.class, javaType.getRawClass());
        assertEquals(String.class, javaType.getType().getComponentType());
    }

    @Test
    public void testTypeFromIdMap() throws IOException {
        DatabindContext context = new JacksonAnnotationIntrospector().findContextForAnnotation(null);
        ClassNameIdResolver resolver = new ClassNameIdResolver(null, typeFactory);
        String id = "java.util.Map";
        JavaType javaType = resolver.typeFromId(context, id);
        assertEquals(Map.class, javaType.getRawClass());

        Map<String, Integer> map = new HashMap<>();
        map.put("Key1", 1);
        map.put("Key2", 2);
        ClassNameIdResolver resolverMap = new ClassNameIdResolver(null, typeFactory);
        id = map.getClass().getCanonicalName();
        javaType = resolverMap.typeFromId(context, id);
        assertEquals(HashMap.class, javaType.getRawClass());
        assertEquals(String.class, javaType.getType().getKeyType());
        assertEquals(Integer.class, javaType.getType().getComponentType());
    }

    @Test
    public void testTypeFromIdArray() throws IOException {
        DatabindContext context = new JacksonAnnotationIntrospector().findContextForAnnotation(null);
        ClassNameIdResolver resolver = new ClassNameIdResolver(null, typeFactory);
        String id = "java.lang.Object[]";
        JavaType javaType = resolver.typeFromId(context, id);
        assertEquals(Object[].class, javaType.getRawClass());

        String[] array = new String[]{"String1", "String2"};
        ClassNameIdResolver resolverArray = new ClassNameIdResolver(null, typeFactory);
        id = array.getClass().getCanonicalName();
        javaType = resolverArray.typeFromId(context, id);
        assertEquals(String[].class, javaType.getRawClass());
    }
}