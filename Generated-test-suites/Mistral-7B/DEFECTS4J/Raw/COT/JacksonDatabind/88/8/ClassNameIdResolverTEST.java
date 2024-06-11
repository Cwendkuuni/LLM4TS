package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.*;

import com.fasterxml.jackson.annotation.JsonTypeInfo.Id;
import com.fasterxml.jackson.core.DatabindContext;
import com.fasterxml.jackson.core.type.TypeFactory;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.TypeIdResolver;

import java.io.IOException;
import java.util.*;

/**
 * Test class for ClassNameIdResolver
 */
public class ClassNameIdResolverTest {

    private TypeIdResolver resolver;
    private TypeFactory typeFactory;

    @org.junit.Before
    public void setup() {
        resolver = new ClassNameIdResolver(JavaType.construct(Object.class), typeFactory);
    }

    @org.junit.Test
    public void testGetMechanism() {
        assertEquals(Id.CLASS, resolver.getMechanism());
    }

    @org.junit.Test
    public void testIdFromValue() throws Exception {
        Object value = new Object();
        String id = resolver.idFromValue(value);
        JavaType javaType = resolver.typeFromId(new DatabindContext(), id);
        assertNotNull(javaType);
        assertEquals(Object.class, javaType.getRawClass());
    }

    @org.junit.Test
    public void testIdFromValueAndType() throws Exception {
        Object value = new Object();
        Class<?> type = String.class;
        String id = resolver.idFromValueAndType(value, type);
        JavaType javaType = resolver.typeFromId(new DatabindContext(), id);
        assertNotNull(javaType);
        assertEquals(String.class, javaType.getRawClass());
    }

    @org.junit.Test(expected = IllegalArgumentException.class)
    public void testInvalidTypeId() throws Exception {
        String id = "invalidTypeId";
        resolver.typeFromId(new DatabindContext(), id);
    }

    @org.junit.Test
    public void testIdFromValueEnum() throws Exception {
        Enum e = Enum.valueOf(String.class, "VALUE");
        String id = resolver.idFromValue(e);
        JavaType javaType = resolver.typeFromId(new DatabindContext(), id);
        assertNotNull(javaType);
        assertEquals(e.getClass(), javaType.getRawClass());
    }

    @org.junit.Test
    public void testIdFromValueEnumMap() throws Exception {
        Map<String, Enum> map = new EnumMap<>(String.class);
        map.put("KEY", Enum.valueOf(String.class, "VALUE"));
        String id = resolver.idFromValue(map);
        JavaType javaType = resolver.typeFromId(new DatabindContext(), id);
        assertNotNull(javaType);
        assertEquals(Map.class, javaType.getRawClass());
        assertEquals(String.class, javaType.getComponentType(0).getRawClass());
        assertEquals(Enum.class, javaType.getComponentType(1).getRawClass());
    }

    @org.junit.Test
    public void testIdFromValueArrayList() throws Exception {
        List<?> list = Arrays.asList(new Object(), new Object());
        String id = resolver.idFromValue(list);
        JavaType javaType = resolver.typeFromId(new DatabindContext(), id);
        assertNotNull(javaType);
        assertEquals(ArrayList.class, javaType.getRawClass());
        assertEquals(Object.class, javaType.getComponentType().getRawClass());
    }

    @org.junit.Test
    public void testIdFromValueInnerClass() throws Exception {
        Class<?> outerClass = Object.class;
        Class<?> innerClass = new Class<>() {
        };
        Object innerObject = new Object();
        String id = resolver.idFromValueAndType(innerObject, innerClass);
        JavaType javaType = resolver.typeFromId(new DatabindContext(), id);
        assertNotNull(javaType);
        assertEquals(innerClass, javaType.getRawClass());
    }
}