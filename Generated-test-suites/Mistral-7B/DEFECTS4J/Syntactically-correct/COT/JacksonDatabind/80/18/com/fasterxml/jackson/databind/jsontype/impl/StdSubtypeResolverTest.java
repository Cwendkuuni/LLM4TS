package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.*;

import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.fasterxml.jackson.databind.AnnotationIntrospector;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.MapperFactoryBuilder;
import com.fasterxml.jackson.databind.MapperFactory;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.jsontype.NamedType;
import com.fasterxml.jackson.databind.jsontype.SubtypeResolverBaseTest;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
import com.fasterxml.jackson.databind.jsontype.impl.StdClassIdResolver;
import com.fasterxml.jackson.databind.jsontype.impl.StdSubtypeResolver;

public class StdSubtypeResolverTest extends SubtypeResolverBaseTest {

    private static final String PACKAGE = StdSubtypeResolver.class.getPackage().getName();

    private MapperFactory mapperFactory;
    private ObjectMapper objectMapper;
    private StdSubtypeResolver subtypeResolver;

    @Before
    public void setUp() throws Exception {
        mapperFactory = new MapperFactoryBuilder().build();
        objectMapper = mapperFactory.getObjectMapper();
        subtypeResolver = objectMapper.getTypeFactory().constructValueType(StdSubtypeResolver.class);
    }

    // Basic test cases for StdSubtypeResolver methods

    @Test
    public void testRegisterSubtypes() {
        StdSubtypeResolver stdSubtypeResolver = new StdSubtypeResolver();
        stdSubtypeResolver.registerSubtypes(new NamedType(String.class, "StringSubtype1"));
        stdSubtypeResolver.registerSubtypes(new NamedType(String.class, "StringSubtype2"));

        Set<NamedType> subtypes = stdSubtypeResolver.collectAndResolveSubtypes(null, null, null);
        assertEquals(2, subtypes.size());
    }

    @Test
    public void testRegisterSubtypesWithClasses() {
        StdSubtypeResolver stdSubtypeResolver = new StdSubtypeResolver();
        stdSubtypeResolver.registerSubtypes(StringSubtype1.class);
        stdSubtypeResolver.registerSubtypes(StringSubtype2.class);

        Set<NamedType> subtypes = stdSubtypeResolver.collectAndResolveSubtypes(null, null, null);
        assertEquals(2, subtypes.size());
    }

    @Test
    public void testCollectAndResolveSubtypesByClass() {
        StdSubtypeResolver stdSubtypeResolver = new StdSubtypeResolver();
        stdSubtypeResolver.registerSubtypes(new NamedType(String.class, "StringSubtype1"));
        stdSubtypeResolver.registerSubtypes(new NamedType(String.class, "StringSubtype2"));

        Set<NamedType> subtypes = stdSubtypeResolver.collectAndResolveSubtypes(mapperFactory, null,
                JavaType.valueOf(String.class));
        assertEquals(2, subtypes.size());
    }

    @Test
    public void testCollectAndResolveSubtypesByClassAnnotatedClass() {
        StdSubtypeResolver stdSubtypeResolver = new StdSubtypeResolver();
        stdSubtypeResolver.registerSubtypes(new NamedType(String.class, "StringSubtype1"));
        stdSubtypeResolver.registerSubtypes(new NamedType(String.class, "StringSubtype2"));

        AnnotatedClass annotatedClass = mapperFactory.getAnnotationIntrospector()
                .findAnnotation(String.class, JsonSubTypes.class);
        Set<NamedType> subtypes = stdSubtypeResolver.collectAndResolveSubtypes(mapperFactory,
                AnnotatedClass.construct(String.class), null);
        assertEquals(2, subtypes.size());
    }

    @Test
    public void testCollectAndResolveSubtypesByTypeId() {
        StdSubtypeResolver stdSubtypeResolver = new StdSubtypeResolver();
        stdSubtypeResolver.registerSubtypes(new NamedType(String.class, "StringSubtype1"));
        stdSubtypeResolver.registerSubtypes(new NamedType(String.class, "StringSubtype2"));

        Set<NamedType> subtypes = stdSubtypeResolver.collectAndResolveSubtypes(mapperFactory, null,
                JavaType.valueOf(String.class));
        assertEquals(2, subtypes.size());

        AnnotatedClass annotatedClass = mapperFactory.getAnnotationIntrospector()
                .findAnnotation(String.class, JsonSubTypes.class);
        Set<NamedType> subtypesFromAnnotation = stdSubtypeResolver.collectAndResolveSubtypes(mapperFactory,
                AnnotatedClass.construct(String.class), null);
        assertEquals(2, subtypesFromAnnotation.size());
    }

    // Edge cases and exception handling test cases for StdSubtypeResolver methods

    @Test(expected = IllegalArgumentException.class)
    public void testNullMapperConfig() {
        StdSubtypeResolver stdSubtypeResolver = new StdSubtypeResolver();
        stdSubtypeResolver.collectAndResolveSubtypes(null, null, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullProperty() {
        StdSubtypeResolver stdSubtypeResolver = new StdSubtypeResolver();
        stdSubtypeResolver.collectAndResolveSubtypes(mapperFactory, null, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullBaseType() {
        StdSubtypeResolver stdSubtypeResolver = new StdSubtypeResolver();
        stdSubtypeResolver.collectAndResolveSubtypes(mapperFactory, null, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullAnnotatedClass() {
        StdSubtypeResolver stdSubtypeResolver = new StdSubtypeResolver();
        stdSubtypeResolver.collectAndResolveSubtypes(mapperFactory, null, null);
    }

    @Test
    public void testEmptyRegisteredSubtypes() {
        StdSubtypeResolver stdSubtypeResolver = new StdSubtypeResolver();
        Set<NamedType> subtypes = stdSubtypeResolver.collectAndResolveSubtypes(mapperFactory, null, null);
        assertEquals(0, subtypes.size());
    }

    @Test
    public void testEmptySubtypesFromAnnotation() {
        String emptyAnnotationJsonSubTypes = "{" +
                "  \"value\": []" +
                "}";

        AnnotationIntrospector introspector = mapperFactory.getAnnotationIntrospector();
        AnnotatedMember property = new AnnotatedMember(null, introspector.findAnnotation(String.class,
                JsonSubTypes.class), null, null, null, null, null, null, null);

        StdSubtypeResolver stdSubtypeResolver = new StdSubtypeResolver();
        Set<NamedType> subtypes = stdSubtypeResolver.collectAndResolveSubtypes(mapperFactory, property,
                JavaType.valueOf(String.class));

        assertEquals(0, subtypes.size());
    }

    @Test
    public void testRegisterSubtypesWithDuplicateType() {
        StdSubtypeResolver stdSubtypeResolver = new StdSubtypeResolver();
        stdSubtypeResolver.registerSubtypes(new NamedType(String.class, "StringSubtype1"));
        stdSubtypeResolver.registerSubtypes(new NamedType(String.class, "StringSubtype1"));

        Set<NamedType> subtypes = stdSubtypeResolver.collectAndResolveSubtypes(mapperFactory, null, null);
        assertEquals(1, subtypes.size());
        assertTrue(subtypes.iterator().next().getName().equals("StringSubtype1"));
    }

    @Test
    public void testRegisterSubtypesWithEmptyTypeName() {
        StdSubtypeResolver stdSubtypeResolver = new StdSubtypeResolver();
        stdSubtypeResolver.registerSubtypes(new NamedType(String.class, ""));

        Set<NamedType> subtypes = stdSubtypeResolver.collectAndResolveSubtypes(mapperFactory, null, null);
        assertEquals(0, subtypes.size());
    }

    @Test
    public void testCollectAndResolveSubtypesByClassWithEmptyRegisteredSubtypes() {
        StdSubtypeResolver stdSubtypeResolver = new StdSubtypeResolver();
        stdSubtypeResolver.registerSubtypes(new NamedType(String.class, ""));

        Set<NamedType> subtypes = stdSubtypeResolver.collectAndResolveSubtypes(mapperFactory, null,
                JavaType.valueOf(String.class));
        assertEquals(0, subtypes.size());
    }

    @Test
    public void testCollectAndResolveSubtypesByClassAnnotatedWithEmptyJsonSubTypes() {
        String emptyAnnotationJsonSubTypes = "{" +
                "  \"value\": []" +
                "}";

        AnnotationIntrospector introspector = mapperFactory.getAnnotationIntrospector();
        AnnotatedMember property = new AnnotatedMember(null, introspector.findAnnotation(String.class,
                JsonSubTypes.class), null, null, null, null, null, null, null);

        StdSubtypeResolver stdSubtypeResolver = new StdSubtypeResolver();
        stdSubtypeResolver.registerSubtypes(new NamedType(String.class, ""));

        Set<NamedType> subtypes = stdSubtypeResolver.collectAndResolveSubtypes(mapperFactory, property,
                JavaType.valueOf(String.class));
        assertEquals(0, subtypes.size());
    }

    @Test
    public void testCollectAndResolveSubtypesByTypeIdWithEmptyRegisteredSubtypes() {
        StdSubtypeResolver stdSubtypeResolver = new StdSubtypeResolver();
        stdSubtypeResolver.registerSubtypes(new NamedType(String.class, ""));

        Set<NamedType> subtypes = stdSubtypeResolver.collectAndResolveSubtypes(mapperFactory, null,
                JavaType.valueOf(String.class));
        assertEquals(0, subtypes.size());
    }

    @Test
    public void testCollectAndResolveSubtypesByTypeIdWithEmptyJsonSubTypes() {
        String emptyAnnotationJsonSubTypes = "{" +
                "  \"value\": []" +
                "}";

        AnnotationIntrospector introspector = mapperFactory.getAnnotationIntrospector();
        AnnotatedMember property = new AnnotatedMember(null, introspector.findAnnotation(String.class,
                JsonSubTypes.class), null, null, null, null, null, null, null);

        StdSubtypeResolver stdSubtypeResolver = new StdSubtypeResolver();
        stdSubtypeResolver.registerSubtypes(new NamedType(String.class, ""));

        Set<NamedType> subtypes = stdSubtypeResolver.collectAndResolveSubtypes(mapperFactory, property,
                JavaType.valueOf(String.class));
        assertEquals(0, subtypes.size());
    }

    @Test
    public void testCollectAndResolveSubtypesByTypeIdWithEmptyJsonTypeName() {
        String emptyAnnotationJsonSubTypes = "{" +
                "  \"value\": [" +
                "    {" +
                "      \"name\": \"\"" +
                "    }" +
                "  ]" +
                "}";

        AnnotationIntrospector introspector = mapperFactory.getAnnotationIntrospector();
        AnnotatedMember property = new AnnotatedMember(null, introspector.findAnnotation(String.class,
                JsonSubTypes.class), null, null, null, null, null, null, null);

        StdSubtypeResolver stdSubtypeResolver = new StdSubtypeResolver();
        stdSubtypeResolver.registerSubtypes(new NamedType(String.class, ""));

        Set<NamedType> subtypes = stdSubtypeResolver.collectAndResolveSubtypes(mapperFactory, property,
                JavaType.valueOf(String.class));
        assertEquals(0, subtypes.size());
    }

    @Test
    public void testCollectAndResolveSubtypesByTypeIdWithDuplicateTypeName() {
        String duplicateTypeNameAnnotationJsonSubTypes = "{" +
                "  \"value\": [" +
                "    {" +
                "      \"name\": \"TestDuplicateTypeName\"" +
                "    }," +
                "    {" +
                "      \"name\": \"TestDuplicateTypeName\"" +
                "    }" +
                "  ]" +
                "}";

        AnnotationIntrospector introspector = mapperFactory.getAnnotationIntrospector();
        AnnotatedMember property = new AnnotatedMember(null, introspector.findAnnotation(String.class,
                JsonSubTypes.class), null, null, null, null, null, null, null);

        StdSubtypeResolver stdSubtypeResolver = new StdSubtypeResolver();
        stdSubtypeResolver.registerSubtypes(new NamedType(String.class, "TestDuplicateTypeName"));
        stdSubtypeResolver.registerSubtypes(new NamedType(String.class, "TestDuplicateTypeName"));

        Set<NamedType> subtypes = stdSubtypeResolver.collectAndResolveSubtypes(mapperFactory, property,
                JavaType.valueOf(String.class));
        assertEquals(1, subtypes.size());
        assertTrue(subtypes.iterator().next().getName().equals("TestDuplicateTypeName"));
    }

    @Test
    public void testCollectAndResolveSubtypesByTypeIdWithInvalidJsonSubTypes() {
        String invalidAnnotationJsonSubTypes = "{" +
                "  \"invalidField\": \"Value\"" +
                "}";

        AnnotationIntrospector introspector = mapperFactory.getAnnotationIntrospector();
        AnnotatedMember property = new AnnotatedMember(null, introspector.findAnnotation(String.class,
                JsonSubTypes.class), null, null, null, null, null, null, null);

        StdSubtypeResolver stdSubtypeResolver = new StdSubtypeResolver();
        stdSubtypeResolver.registerSubtypes(new NamedType(String.class, ""));

        try {
            Set<NamedType> subtypes = stdSubtypeResolver.collectAndResolveSubtypes(mapperFactory, property,
                    JavaType.valueOf(String.class));
            fail();
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testCollectAndResolveSubtypesByTypeIdWithNullJsonSubTypes() {
        String nullAnnotationJsonSubTypes = "null";

        AnnotationIntrospector introspector = mapperFactory.getAnnotationIntrospector();
        AnnotatedMember property = new AnnotatedMember(null, introspector.findAnnotation(String.class,
                JsonSubTypes.class), null, null, null, null, null, null, null);

        StdSubtypeResolver stdSubtypeResolver = new StdSubtypeResolver();
        stdSubtypeResolver.registerSubtypes(new NamedType(String.class, ""));

        try {
            Set<NamedType> subtypes = stdSubtypeResolver.collectAndResolveSubtypes(mapperFactory, property,
                    JavaType.valueOf(String.class));
            fail();
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    // Helper classes for edge cases and exception handling tests

    static class StringSubtype1 {

        @JsonTypeName("StringSubtype1")
        static class JsonSubtypes {
        }

    }

    static class StringSubtype2 {

        @JsonTypeName("StringSubtype2")
        static class JsonSubtypes {
        }

    }

}