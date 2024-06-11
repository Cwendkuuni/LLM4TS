package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.fasterxml.jackson.databind.util.ClassUtil;

public class ClassNameIdResolverTest {

    private static final TypeFactory typeFactory = new TypeFactoryBuilder()
            .build();

    @Test
    public void testIdFromValue() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        JavaType baseType = mapper.getTypeFactory().constructType(String.class);

        ClassNameIdResolver resolver = new ClassNameIdResolver(baseType, typeFactory);

        assertEquals("java.lang.String", resolver.idFromValue("Hello"));
        assertEquals("java.lang.String", resolver.idFromValue("Hello".getClass()));
        assertEquals("java.util.Set", resolver.idFromValue(new HashSet<>()));
        assertEquals("java.util.EnumSet", resolver.idFromValue(EnumSet.of(JavaLanguageVersion.VERSION_1_8)));
        assertEquals("java.util.Map", resolver.idFromValue(new HashMap<>()));
        assertEquals("java.util.ArrayList", resolver.idFromValue(Arrays.asList(1, 2, 3)));
    }

    @Test
    public void testIdFromValueAndType() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        JavaType baseType = mapper.getTypeFactory().constructType(String.class);

        ClassNameIdResolver resolver = new ClassNameIdResolver(baseType, typeFactory);

        assertEquals("java.lang.String", resolver.idFromValueAndType("Hello", String.class));
        assertEquals("java.lang.String", resolver.idFromValueAndType("Hello".getClass(), String.class));
        assertEquals("java.util.Set<java.lang.Integer>", resolver.idFromValueAndType(new HashSet<>(), HashSet.class));
        assertEquals("java.util.EnumSet<JavaLanguageVersion>", resolver.idFromValueAndType(EnumSet.of(JavaLanguageVersion.VERSION_1_8), EnumSet.class));
        assertEquals("java.util.Map<java.lang.String, java.lang.Integer>", resolver.idFromValueAndType(new HashMap<>(), Map.class));
        assertEquals("java.util.ArrayList<java.lang.Integer>", resolver.idFromValueAndType(Arrays.asList(1, 2, 3), ArrayList.class));
    }

    @Test
    public void testTypeFromId() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        JavaType baseType = mapper.getTypeFactory().constructType(String.class);

        ClassNameIdResolver resolver = new ClassNameIdResolver(baseType, typeFactory);

        JavaType stringType = resolver.typeFromId(mapper.getDeserializationContext(), "java.lang.String");
        assertEquals(String.class, stringType.getRawClass());

        JavaType enumSetType = resolver.typeFromId(mapper.getDeserializationContext(), "java.util.EnumSet");
        assertEquals(EnumSet.class, enumSetType.getRawClass());
        assertEquals(JavaLanguageVersion.class, enumSetType.getGenericTypeArguments()[0].getRawClass());

        JavaType hashMapType = resolver.typeFromId(mapper.getDeserializationContext(), "java.util.Map");
        assertEquals(Map.class, hashMapType.getRawClass());
        assertEquals(String.class, hashMapType.getGenericTypeArguments()[0].getRawClass());
        assertEquals(Integer.class, hashMapType.getGenericTypeArguments()[1].getRawClass());

        JavaType arrayListType = resolver.typeFromId(mapper.getDeserializationContext(), "java.util.ArrayList");
        assertEquals(ArrayList.class, arrayListType.getRawClass());
        assertEquals(Integer.class, arrayListType.getGenericTypeArguments()[0].getRawClass());

        JavaType unknownType = resolver.typeFromId(mapper.getDeserializationContext(), "java.unknown.UnknownClass");
        assertNull(unknownType);
    }
}