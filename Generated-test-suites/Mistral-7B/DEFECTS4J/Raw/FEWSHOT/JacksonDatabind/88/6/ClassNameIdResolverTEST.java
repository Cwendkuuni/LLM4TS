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
        JavaType baseType = typeFactory.constructType(String.class);
        ClassNameIdResolver resolver = new ClassNameIdResolver(baseType, typeFactory);

        Object value = new Object();
        String id = resolver.idFromValue(value);
        assertEquals("java.lang.Object", id);

        Class<?> clazz = Integer.class;
        id = resolver.idFromValue(clazz);
        assertEquals("java.lang.Integer", id);

        id = resolver.idFromValue(Enum.class);
        assertEquals("java.lang.Class<java.lang.Enum>", id);

        id = resolver.idFromValue(Collections.emptyList().getClass());
        assertEquals("java.util.List<>", id);

        id = resolver.idFromValue(Collections.emptyMap().getClass());
        assertEquals("java.util.Map<,>", id);

        id = resolver.idFromValue(ArrayList.class);
        assertEquals("java.util.ArrayList", id);
    }

    @Test
    public void testIdFromValueAndType() throws IOException {
        JavaType baseType = typeFactory.constructType(String.class);
        ClassNameIdResolver resolver = new ClassNameIdResolver(baseType, typeFactory);

        Object value = new Object();
        Class<?> type = String.class;
        String id = resolver.idFromValueAndType(value, type);
        assertEquals("java.lang.String", id);

        value = new Integer(1);
        id = resolver.idFromValueAndType(value, Integer.class);
        assertEquals("java.lang.Integer", id);

        value = Enum.valueOf(Enum.class, "FOO");
        id = resolver.idFromValueAndType(value, Enum.class);
        assertEquals("java.lang.Class<java.lang.Enum>", id);

        value = Collections.emptyList();
        id = resolver.idFromValueAndType(value, List.class);
        assertEquals("java.util.List<>", id);

        value = Collections.emptyMap();
        id = resolver.idFromValueAndType(value, Map.class);
        assertEquals("java.util.Map<,>", id);

        value = new ArrayList<>();
        id = resolver.idFromValueAndType(value, ArrayList.class);
        assertEquals("java.util.ArrayList", id);
    }

    @Test
    public void testTypeFromId() throws IOException {
        DatabindContext context = new ObjectMapper().getDeserializationContext();
        JavaType baseType = typeFactory.constructType(String.class);
        ClassNameIdResolver resolver = new ClassNameIdResolver(baseType, typeFactory);

        String id = "java.lang.String";
        JavaType javaType = resolver.typeFromId(context, id);
        assertEquals(String.class, javaType.getRawClass());

        id = "java.lang.Integer";
        javaType = resolver.typeFromId(context, id);
        assertEquals(Integer.class, javaType.getRawClass());

        id = "java.lang.Enum";
        javaType = resolver.typeFromId(context, id);
        assertEquals(Enum.class, javaType.getRawClass());

        id = "java.util.List>";
        javaType = resolver.typeFromId(context, id);
        assertEquals(List.class, javaType.getRawClass());

        id = "java.util.Map<,>";
        javaType = resolver.typeFromId(context, id);
        assertEquals(Map.class, javaType.getRawClass());

        id = "java.util.ArrayList";
        javaType = resolver.typeFromId(context, id);
        assertEquals(ArrayList.class, javaType.getRawClass());
    }

    @Test
    public void testTypeFromIdWithGenericType() throws IOException {
        DatabindContext context = new ObjectMapper().getDeserializationContext();
        JavaType baseType = typeFactory.constructType(String.class);
        ClassNameIdResolver resolver = new ClassNameIdResolver(baseType, typeFactory);

        String id = "java.util.List<java.lang.String>";
        JavaType javaType = resolver.typeFromId(context, id);
        assertEquals(List.class, javaType.getRawClass());
        assertEquals(String.class, javaType.getGenericTypeArguments()[0].getRawClass());

        id = "java.util.Map<java.lang.String, java.lang.Integer>";
        javaType = resolver.typeFromId(context, id);
        assertEquals(Map.class, javaType.getRawClass());
        assertEquals(String.class, javaType.getGenericTypeArguments()[0].getRawClass());
        assertEquals(Integer.class, javaType.getGenericTypeArguments()[1].getRawClass());
    }
}