package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.fasterxml.jackson.databind.util.ClassUtil;

public class ClassNameIdResolverTest {
    private static final TypeFactory typeFactory = new TypeFactoryBuilder().build();

    @Test
    public void testIdFromValue() {
        Object value = new Object();
        ClassNameIdResolver resolver = new ClassNameIdResolver(Object.class.getTypeName(), typeFactory);
        String id = resolver.idFromValue(value);
        assertEquals("java.lang.Object", id);
    }

    @Test
    public void testIdFromValueAndType() {
        Object value = new Object();
        Class<?> type = String.class;
        ClassNameIdResolver resolver = new ClassNameIdResolver(type, typeFactory);
        String id = resolver.idFromValueAndType(value, type);
        assertEquals("java.lang.Object", id);
    }

    @Test
    public void testTypeFromId() throws IOException {
        DatabindContext context = new ObjectMapper().getDeserializationContext();
        JavaType baseType = Object.class.getTypeName();
        ClassNameIdResolver resolver = new ClassNameIdResolver(baseType, typeFactory);

        String id = "java.lang.String";
        JavaType javaType = resolver.typeFromId(context, id);
        assertEquals(String.class, javaType.getRawClass());

        id = "java.util.List";
        javaType = resolver.typeFromId(context, id);
        assertEquals(List.class, javaType.getRawClass());

        id = "java.util.ArrayList";
        javaType = resolver.typeFromId(context, id);
        assertEquals(ArrayList.class, javaType.getRawClass());

        id = "java.util.EnumSet";
        javaType = resolver.typeFromId(context, id);
        assertEquals(EnumSet.class, javaType.getRawClass());

        id = "java.util.EnumMap";
        javaType = resolver.typeFromId(context, id);
        assertEquals(EnumMap.class, javaType.getRawClass());

        id = "java.lang.Enum";
        javaType = resolver.typeFromId(context, id);
        assertEquals(Enum.class, javaType.getRawClass());

        id = "com.example.MyClass";
        javaType = resolver.typeFromId(context, id);
        assertNull(javaType);
    }

    @Test
    public void testIdFrom() {
        Object value = new Object();
        Class<?> type = String.class;
        ClassNameIdResolver resolver = new ClassNameIdResolver(type, typeFactory);

        String id = resolver._idFrom(value, type, typeFactory);
        assertEquals("java.lang.Object", id);
    }

    @Test
    public void testIdFromEnumSet() {
        EnumSet<?> enumSet = EnumSet.allOf(Enum.class);
        ClassNameIdResolver resolver = new ClassNameIdResolver(Object.class.getTypeName(), typeFactory);

        String id = resolver._idFrom(enumSet, EnumSet.class, typeFactory);
        assertEquals("java.util.EnumSet<java.lang.Enum>", id);
    }

    @Test
    public void testIdFromEnumMap() {
        EnumMap<Enum, Integer> enumMap = new EnumMap<>(Enum.class);
        enumMap.put(Enum.VAL1, 1);
        ClassNameIdResolver resolver = new ClassNameIdResolver(Object.class.getTypeName(), typeFactory);

        String id = resolver._idFrom(enumMap, EnumMap.class, typeFactory);
        assertEquals("java.util.EnumMap<java.lang.Enum, java.lang.Integer>", id);
    }

    @Test
    public void testIdFromInnerClass() {
        Class<?> outerClass = Object.class;
        Class<?> innerClass = OuterClass.InnerClass.class;
        ClassNameIdResolver resolver = new ClassNameIdResolver(outerClass.getTypeName(), typeFactory);

        String id = resolver._idFrom(null, innerClass, typeFactory);
        assertEquals("outerClass$InnerClass", id);
    }
}