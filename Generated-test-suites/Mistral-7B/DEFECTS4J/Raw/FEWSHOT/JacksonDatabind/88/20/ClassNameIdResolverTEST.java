package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.fasterxml.jackson.databind.util.ClassUtil;

public class ClassNameIdResolverTest {

    private static final TypeFactory TYPE_FACTORY = new TypeFactoryBuilder().build();

    @Test
    public void testIdFromValue() throws IOException {
        ClassNameIdResolver resolver = new ClassNameIdResolver(Object.class.getTypeName(), TYPE_FACTORY);

        Object value = new Object();
        String id = resolver.idFromValue(value);
        assertEquals("java.lang.Object", id);

        String idFromEnum = resolver.idFromValue(Enum.class);
        assertEquals("java.lang.Class", idFromEnum);

        Enum valueEnum = Enum.valueOf(Enum.class, "ONE");
        String idFromEnumValue = resolver.idFromValue(valueEnum);
        assertEquals("java.lang.Enum", idFromEnumValue);

        List<?> list = Arrays.asList(1, 2, 3);
        String idFromList = resolver.idFromValue(list);
        assertEquals("java.util.ArrayList", idFromList);

        Map<?, ?> map = new HashMap<>();
        map.put("key", "value");
        String idFromMap = resolver.idFromValue(map);
        assertEquals("java.util.HashMap", idFromMap);
    }

    @Test
    public void testIdFromValueAndType() throws IOException {
        ClassNameIdResolver resolver = new ClassNameIdResolver(Object.class.getTypeName(), TYPE_FACTORY);

        Object value = new Object();
        Class<?> clazz = String.class;
        String id = resolver.idFromValueAndType(value, clazz);
        assertEquals("java.lang.Object", id);

        Enum valueEnum = Enum.valueOf(Enum.class, "ONE");
        String idFromEnumValueAndType = resolver.idFromValueAndType(valueEnum, Enum.class);
        assertEquals("java.lang.Enum", idFromEnumValueAndType);

        List<?> list = Arrays.asList(1, 2, 3);
        String idFromListAndType = resolver.idFromValueAndType(list, List.class);
        assertEquals("java.util.ArrayList", idFromListAndType);

        Map<?, ?> map = new HashMap<>();
        map.put("key", "value");
        String idFromMapAndType = resolver.idFromValueAndType(map, Map.class);
        assertEquals("java.util.HashMap", idFromMapAndType);

        EnumMap<Enum, String> enumMap = new EnumMap<>(Enum.class);
        enumMap.put(Enum.ONE, "ONE");
        String idFromEnumMapAndType = resolver.idFromValueAndType(enumMap, EnumMap.class);
        assertEquals("java.util.EnumMap", idFromEnumMapAndType);
    }

    @Test
    public void testTypeFromId() throws IOException {
        DatabindContext context = new DatabindContext();
        ClassNameIdResolver resolver = new ClassNameIdResolver(Object.class.getTypeName(), context.getTypeFactory());

        String id = "java.lang.String";
        JavaType javaType = resolver.typeFromId(context, id);
        assertEquals(String.class, javaType.getRawClass());

        id = "java.util.HashMap";
        javaType = resolver.typeFromId(context, id);
        assertEquals(HashMap.class, javaType.getRawClass());

        id = "java.util.ArrayList";
        javaType = resolver.typeFromId(context, id);
        assertEquals(ArrayList.class, javaType.getRawClass());

        id = "java.util.EnumMap";
        javaType = resolver.typeFromId(context, id);
        assertEquals(EnumMap.class, javaType.getRawClass());

        id = "java.lang.Enum";
        javaType = resolver.typeFromId(context, id);
        assertEquals(Enum.class, javaType.getRawClass());

        id = "java.lang.Object";
        javaType = resolver.typeFromId(context, id);
        assertEquals(Object.class, javaType.getRawClass());

        id = "java.lang.Class";
        javaType = resolver.typeFromId(context, id);
        assertEquals(Class.class, javaType.getRawClass());

        id = "java.util.List";
        javaType = resolver.typeFromId(context, id);
        assertEquals(List.class, javaType.getRawClass());

        id = "java.util.Map";
        javaType = resolver.typeFromId(context, id);
        assertEquals(Map.class, javaType.getRawClass());

        id = "java.util.EnumSet";
        javaType = resolver.typeFromId(context, id);
        assertEquals(EnumSet.class, javaType.getRawClass());

        id = "java.util.Collections$UnmodifiableMap";
        javaType = resolver.typeFromId(context, id);
        assertEquals(UnmodifiableMap.class, javaType.getRawClass());

        id = "java.util.Arrays$ArrayList";
        javaType = resolver.typeFromId(context, id);
        assertEquals(ArrayList.class, javaType.getRawClass());
    }
}