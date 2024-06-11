package com.fasterxml.jackson.databind.jsontype.impl;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.AnnotationIntrospector;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.MapperFactory;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.jsontype.NamedType;
import com.fasterxml.jackson.databind.jsontype.StdSubtypeResolver;
import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class StdSubtypeResolverTest {

    private MapperFactory mapperFactory;
    private ObjectMapper objectMapper;
    private StdSubtypeResolver subtypeResolver;

    @Before
    public void setup() {
        mapperFactory = new MapperFactory();
        mapperFactory.setAnnotationIntrospector(new AnnotationIntrospector() {
            @Override
            public List<NamedType> findSubtypes(AnnotatedMember member) {
                return Arrays.asList(new NamedType(String.class, "IntegerType"),
                        new NamedType(String.class, "StringType"));
            }

            @Override
            public List<NamedType> findSubtypes(AnnotatedClass clazz) {
                return Arrays.asList(new NamedType(Integer.class),
                        new NamedType(String.class, "StringListType"));
            }
        });
        objectMapper = mapperFactory.getMapper();
        subtypeResolver = new StdSubtypeResolver();
    }

    @Test
    public void testCollectAndResolveSubtypesByClass() {
        JavaType baseType = objectMapper.getTypeFactory().constructType(String.class);
        AnnotatedClass stringClass = AnnotatedClassResolver.resolveWithoutSuperTypes(objectMapper.getConfig(), String.class);

        // Test registration of subtypes by class
        subtypeResolver.registerSubtypes(new NamedType(Integer.class));
        subtypeResolver.registerSubtypes(new NamedType(String.class, "StringSubtype1"));
        subtypeResolver.registerSubtypes(new NamedType(String.class, "StringSubtype2"));

        List<NamedType> subtypes = subtypeResolver.collectAndResolveSubtypesByClass(objectMapper.getConfig(), stringClass, baseType);

        assertEquals(3, subtypes.size());
        assertEquals(Integer.class, subtypes.get(0).getType().getRawClass());
        assertEquals("StringSubtype1", subtypes.get(1).getName());
        assertEquals("StringSubtype2", subtypes.get(2).getName());
    }

    @Test
    public void testCollectAndResolveSubtypesByClassWithJsonTypeInfo() {
        // Test registration of subtypes with JsonTypeInfo
        JavaType baseType = objectMapper.getTypeFactory().constructType(JsonTypeInfo.Id.class);

        subtypeResolver.registerSubtypes(new NamedType(String.class, "StringSubtype1")
                .withAnnotation(new JsonTypeInfo.IdAnnotation(JsonTypeInfo.Id.CLASS, "class")));
        subtypeResolver.registerSubtypes(new NamedType(String.class, "StringSubtype2")
                .withAnnotation(new JsonTypeInfo.IdAnnotation(JsonTypeInfo.Id.NAME, "subtype1")));

        AnnotatedClass jsonTypeInfoClass = AnnotatedClassResolver.resolveWithoutSuperTypes(objectMapper.getConfig(), JsonTypeInfo.Id.class);
        List<NamedType> subtypes = subtypeResolver.collectAndResolveSubtypesByClass(objectMapper.getConfig(), jsonTypeInfoClass, baseType);

        assertEquals(2, subtypes.size());
        assertEquals("StringSubtype1", subtypes.get(0).getName());
        assertEquals("StringSubtype2", subtypes.get(1).getName());
    }

    @Test
    public void testCollectAndResolveSubtypesByTypeId() {
        JavaType baseType = objectMapper.getTypeFactory().constructType(String.class);

        // Test registration of subtypes by type id
        subtypeResolver.registerSubtypes(new NamedType(Integer.class));
        subtypeResolver.registerSubtypes(new NamedType(String.class, "StringSubtype1"));
        subtypeResolver.registerSubtypes(new NamedType(String.class, "StringSubtype2"));

        AnnotatedClass stringClass = AnnotatedClassResolver.resolveWithoutSuperTypes(objectMapper.getConfig(), String.class);
        List<NamedType> subtypes = subtypeResolver.collectAndResolveSubtypesByTypeId(objectMapper.getConfig(), stringClass, baseType);

        assertEquals(3, subtypes.size());
        assertEquals(Integer.class, subtypes.get(0).getType().getRawClass());
        assertEquals("StringSubtype1", subtypes.get(1).getName());
        assertEquals("StringSubtype2", subtypes.get(2).getName());
    }

    @Test
    public void testCollectAndResolveSubtypesByTypeIdWithJsonTypeInfo() {
        // Test registration of subtypes with JsonTypeInfo
        JavaType baseType = objectMapper.getTypeFactory().constructType(JsonTypeInfo.Id.class);

        subtypeResolver.registerSubtypes(new NamedType(String.class, "StringSubtype1")
                .withAnnotation(new JsonTypeInfo.IdAnnotation(JsonTypeInfo.Id.CLASS, "class")));
        subtypeResolver.registerSubtypes(new NamedType(String.class, "StringSubtype2")
                .withAnnotation(new JsonTypeInfo.IdAnnotation(JsonTypeInfo.Id.NAME, "subtype1")));

        AnnotatedClass jsonTypeInfoClass = AnnotatedClassResolver.resolveWithoutSuperTypes(objectMapper.getConfig(), JsonTypeInfo.Id.class);
        List<NamedType> subtypes = subtypeResolver.collectAndResolveSubtypesByTypeId(objectMapper.getConfig(), jsonTypeInfoClass, baseType);

        assertEquals(2, subtypes.size());
        assertEquals("StringSubtype1", subtypes.get(0).getName());
        assertEquals("StringSubtype2", subtypes.get(1).getName());
    }

    @Test
    public void testEmptyRegisteredSubtypes() {
        JavaType baseType = objectMapper.getTypeFactory().constructType(String.class);

        StdSubtypeResolver subtypeResolver = new StdSubtypeResolver();
        List<NamedType> subtypes = subtypeResolver.collectAndResolveSubtypesByClass(objectMapper.getConfig(), null, baseType);

        assertEquals(0, subtypes.size());
    }

    @Test(expected = IllegalStateException.class)
    public void testNullBaseType() {
        JavaType baseType = null;

        StdSubtypeResolver subtypeResolver = new StdSubtypeResolver();
        subtypeResolver.collectAndResolveSubtypesByClass(objectMapper.getConfig(), null, baseType);
    }
}