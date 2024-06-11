package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.fasterxml.jackson.databind.util.ClassUtil;

public class ClassNameIdResolverTest {

    private static final TypeFactory typeFactory = new TypeFactoryBuilder()
            .build();

    @Test
    public void testIdFromValue() throws IOException {
        ClassNameIdResolver resolver = new ClassNameIdResolver(Object.class.getTypeName(), typeFactory);

        Object value = "java.lang.String";
        String id = resolver.idFromValue(value);
        assertEquals("java.lang.String", id);

        value = new String[]{"java.lang.String", "java.lang.Integer"};
        id = resolver.idFromValue(value);
        assertEquals("java.lang.Object[]", id);

        value = new HashMap<>();
        id = resolver.idFromValue(value);
        assertEquals("java.util.Map", id);

        value = new EnumSet.None<>();
        id = resolver.idFromValue(value);
        assertEquals("java.util.EnumSet", id);

        value = new EnumMap<>(String.class);
        id = resolver.idFromValue(value);
        assertEquals("java.util.EnumMap<java.lang.String, java.lang.Object>", id);

        value = Arrays.asList("java.lang.String", "java.lang.Integer");
        id = resolver.idFromValue(value);
        assertEquals("java.util.ArrayList", id);
    }

    @Test
    public void testIdFromValueAndType() throws IOException {
        ClassNameIdResolver resolver = new ClassNameIdResolver(Object.class.getTypeName(), typeFactory);

        Object value = "java.lang.String";
        Class<?> type = String.class;
        String id = resolver.idFromValueAndType(value, type);
        assertEquals("java.lang.String", id);

        value = new String[]{"java.lang.String", "java.lang.Integer"};
        type = Object[].class;
        id = resolver.idFromValueAndType(value, type);
        assertEquals("java.lang.Object[]", id);

        value = new HashMap<>();
        type = Map.class;
        id = resolver.idFromValueAndType(value, type);
        assertEquals("java.util.Map", id);

        value = new EnumSet.None<>();
        type = EnumSet.class;
        id = resolver.idFromValueAndType(value, type);
        assertEquals("java.util.EnumSet", id);

        value = new EnumMap<>(String.class);
        type = Map.class;
        id = resolver.idFromValueAndType(value, type);
        assertEquals("java.util.EnumMap<java.lang.String, java.lang.Object>", id);

        value = Arrays.asList("java.lang.String", "java.lang.Integer");
        type = List.class;
        id = resolver.idFromValueAndType(value, type);
        assertEquals("java.util.ArrayList", id);
    }

    @Test
    public void testTypeFromId() throws IOException {
        DatabindContext context = new ObjectMapper().getFactory().createContext();
        ClassNameIdResolver resolver = new ClassNameIdResolver(Object.class.getTypeName(), typeFactory);

        String id = "java.lang.String";
        JavaType javaType = resolver.typeFromId(context, id);
        assertEquals(String.class, javaType.getRawClass());

        id = "java.lang.Integer[]";
        javaType = resolver.typeFromId(context, id);
        assertEquals(Integer[].class, javaType.getRawClass());

        id = "java.util.Map<java.lang.String, java.lang.Object>";
        javaType = resolver.typeFromId(context, id);
        assertEquals(Map.class, javaType.getRawClass());

        id = "java.util.EnumSet<java.lang.Enum>";
        javaType = resolver.typeFromId(context, id);
        assertEquals(EnumSet.class, javaType.getRawClass());

        id = "java.util.EnumMap<java.lang.String, java.lang.Object>";
        javaType = resolver.typeFromId(context, id);
        assertEquals(EnumMap.class, javaType.getRawClass());

        id = "java.util.ArrayList";
        javaType = resolver.typeFromId(context, id);
        assertEquals(ArrayList.class, javaType.getRawClass());
    }

    @Test
    public void testIdFromInvalidType() throws IOException {
        DatabindContext context = new ObjectMapper().getFactory().createContext();
        ClassNameIdResolver resolver = new ClassNameIdResolver(Object.class.getTypeName(), typeFactory);

        Object invalidType = new Object();
        String id = resolver.idFromValue(invalidType);
        assertNull(id);
    }
}