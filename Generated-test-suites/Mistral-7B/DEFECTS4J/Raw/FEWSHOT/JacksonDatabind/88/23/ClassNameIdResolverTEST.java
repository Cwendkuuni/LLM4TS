package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.*;

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

        Object value = new Object();
        String id = resolver.idFromValue(value);
        assertEquals("java.lang.Object", id);

        Enum e = Enum.valueOf("java.lang.Enum", "FOO");
        id = resolver.idFromValue(e);
        assertEquals("java.lang.Enum", id);

        List list = Arrays.asList(1, 2, 3);
        id = resolver.idFromValue(list);
        assertEquals("java.util.ArrayList", id);

        EnumSet enumSet = EnumSet.allOf(Enum.class);
        id = resolver.idFromValue(enumSet);
        assertEquals("java.util.EnumSet", id);

        EnumMap enumMap = new EnumMap<>(Enum.class) {{ put(Enum.VALUES[0], 1); }};
        id = resolver.idFromValue(enumMap);
        assertEquals("java.util.EnumMap", id);
    }

    @Test
    public void testIdFromValueAndType() throws IOException {
        ClassNameIdResolver resolver = new ClassNameIdResolver(String.class.getTypeName(), typeFactory);

        Object value = new Object();
        String typeName = String.class.getTypeName();
        String id = resolver.idFromValueAndType(value, typeName);
        assertEquals("java.lang.Object", id);

        Enum e = Enum.valueOf("java.lang.Enum", "FOO");
        id = resolver.idFromValueAndType(e, typeName);
        assertEquals("java.lang.Enum", id);

        List list = Arrays.asList(1, 2, 3);
        id = resolver.idFromValueAndType(list, typeFactory.constructCollectionType(List.class, Number.class).toCanonical());
        assertEquals("java.util.ArrayList", id);

        EnumSet enumSet = EnumSet.allOf(Enum.class);
        id = resolver.idFromValueAndType(enumSet, typeFactory.constructCollectionType(EnumSet.class, Enum.class).toCanonical());
        assertEquals("java.util.EnumSet", id);

        EnumMap enumMap = new EnumMap<>(Enum.class) {{ put(Enum.VALUES[0], 1); }};
        id = resolver.idFromValueAndType(enumMap, typeFactory.constructMapType(EnumMap.class, Enum.class, Number.class).toCanonical());
        assertEquals("java.util.EnumMap", id);
    }

    @Test
    public void testTypeFromId() throws IOException {
        DatabindContext context = new JacksonAnnotationIntrospector().findValueTypeAnnotation(Object.class, null);
        ClassNameIdResolver resolver = new ClassNameIdResolver(Object.class.getTypeName(), typeFactory);

        String id = "java.lang.Object";
        JavaType javaType = resolver.typeFromId(context, id);
        assertEquals(Object.class, javaType.getRawClass());

        id = "java.lang.Enum";
        javaType = resolver.typeFromId(context, id);
        assertEquals(Enum.class, javaType.getRawClass());

        id = "java.util.ArrayList";
        javaType = resolver.typeFromId(context, id);
        assertEquals(List.class, javaType.getRawClass());

        id = "java.util.EnumSet";
        javaType = resolver.typeFromId(context, id);
        assertEquals(EnumSet.class, javaType.getRawClass());

        id = "java.util.EnumMap";
        javaType = resolver.typeFromId(context, id);
        assertEquals(Map.class, javaType.getRawClass());
        assertEquals(Enum.class, javaType.getParameterTypes()[0]);
        assertEquals(Object.class, javaType.getParameterTypes()[1]);

        id = "java.util.HashMap";
        javaType = resolver.typeFromId(context, id);
        assertEquals(Map.class, javaType.getRawClass());
        assertEquals(Object.class, javaType.getParameterTypes()[0]);
        assertEquals(Object.class, javaType.getParameterTypes()[1]);

        id = "java.util.ArrayList<java.lang.Integer>";
        javaType = resolver.typeFromId(context, id);
        assertEquals(List.class, javaType.getRawClass());
        assertEquals(Integer.class, javaType.getParameterTypes()[0]);
    }
}