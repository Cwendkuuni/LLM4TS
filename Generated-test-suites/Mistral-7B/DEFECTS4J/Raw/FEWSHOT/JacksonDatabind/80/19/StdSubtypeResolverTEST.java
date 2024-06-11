package com.fasterxml.jackson.databind.jsontype.impl;

import static com.fasterxml.jackson.databind.jsontype.util.CollectionMatcher.hasSize;
import static org.junit.Assert.*;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.fasterxml.jackson.databind.AnnotationIntrospector;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.jsontype.NamedType;
import com.fasterxml.jackson.databind.jsontype.SubtypeResolver;
import com.fasterxml.jackson.databind.jsontype.impl.*;

import java.lang.reflect.Modifier;
import java.util.*;

public class StdSubtypeResolverTest {

    private static ObjectMapper mapper = new ObjectMapper();

    @Test
    public void testRegisterSubtypesByClasses() throws Exception {
        StdSubtypeResolver resolver = new StdSubtypeResolver();
        Class<?> base = TestSubtype.class;
        Class<?> subtype1 = Subtype1.class;
        Class<?> subtype2 = Subtype2.class;
        resolver.registerSubtypes(base, subtype1, subtype2);

        AnnotationIntrospector ai = mapper.getAnnotationIntrospector();
        SubtypeResolver subresolver = mapper.getTypeFactory().constructValueType(SubtypeResolver.class, resolver);

        List<NamedType> types = subresolver.collectAndResolveSubtypesByClass(null, null, base);
        assertEquals(2, types.size());
        assertTrue(types.get(0) instanceof NamedType);
        assertTrue(types.get(1) instanceof NamedType);
    }

    @Test
    public void testRegisterSubtypesByName() throws Exception {
        StdSubtypeResolver resolver = new StdSubtypeResolver();
        resolver.registerSubtypes(new NamedType(Subtype1.class, "Subtype1"));
        resolver.registerSubtypes(new NamedType(Subtype2.class, "Subtype2"));

        AnnotationIntrospector ai = mapper.getAnnotationIntrospector();
        SubtypeResolver subresolver = mapper.getTypeFactory().constructValueType(SubtypeResolver.class, resolver);

        List<NamedType> types = subresolver.collectAndResolveSubtypesByClass(null, null, TestSubtype.class);
        assertEquals(2, types.size());
        assertTrue(types.get(0) instanceof NamedType);
        assertTrue(types.get(1) instanceof NamedType);

        assertEquals("Subtype1", types.get(0).getName());
        assertEquals("Subtype2", types.get(1).getName());
    }

    @Test
    public void testCollectAndResolveSubtypesByClass() throws Exception {
        StdSubtypeResolver resolver = new StdSubtypeResolver();
        Class<?> base = TestSubtype.class;
        Class<?> subtype1 = Subtype1.class;
        Class<?> subtype2 = Subtype2.class;

        resolver.registerSubtypes(base, subtype1, subtype2);

        AnnotationIntrospector ai = mapper.getAnnotationIntrospector();
        SubtypeResolver subresolver = mapper.getTypeFactory().constructValueType(SubtypeResolver.class, resolver);

        List<NamedType> types = subresolver.collectAndResolveSubtypesByClass(null, null, base);
        assertEquals(2, types.size());
        assertTrue(types.get(0) instanceof NamedType);
        assertTrue(types.get(1) instanceof NamedType);

        assertEquals(base, types.get(0).getType().getSuperclass());
        assertEquals(subtype1, types.get(0).getType());
        assertEquals(subtype1, types.get(1).getType().getSuperclass());
        assertEquals(base, types.get(1).getType());

        // Test empty list when no subtypes are registered
        List<NamedType> emptyTypes = subresolver.collectAndResolveSubtypesByClass(null, null, Object.class);
        assertTrue(emptyTypes.isEmpty());
    }

    @Test
    public void testCollectAndResolveSubtypesByTypeId() throws Exception {
        StdSubtypeResolver resolver = new StdSubtypeResolver();
        Class<?> base = TestSubtype.class;
        Class<?> subtype1 = Subtype1.class;
        Class<?> subtype2 = Subtype2.class;

        resolver.registerSubtypes(base, subtype1, subtype2);

        AnnotationIntrospector ai = mapper.getAnnotationIntrospector();
        SubtypeResolver subresolver = mapper.getTypeFactory().constructValueType(SubtypeResolver.class, resolver);

        List<NamedType> types = subresolver.collectAndResolveSubtypesByTypeId(null, null, base);
        assertEquals(2, types.size());
        assertTrue(types.get(0) instanceof NamedType);
        assertTrue(types.get(1) instanceof NamedType);

        assertEquals(base, types.get(0).getType().getSuperclass());
        assertEquals(subtype1, types.get(0).getType());
        assertEquals(subtype1, types.get(1).getType().getSuperclass());
        assertEquals(base, types.get(1).getType());

        // Test empty list when no subtypes are registered
        List<NamedType> emptyTypes = subresolver.collectAndResolveSubtypesByTypeId(null, null, Object.class);
        assertTrue(emptyTypes.isEmpty());
    }

    @Test
    public void testCollectAndResolveSubtypesByTypeIdWithJsonSubTypes() throws Exception {
        Class<?> base = TestWithJsonSubTypes.class;
        SubtypeResolver subresolver = mapper.getTypeFactory().constructValueType(SubtypeResolver.class, new StdSubtypeResolver());

        List<NamedType> types = subresolver.collectAndResolveSubtypesByTypeId(null, null, base);
        assertEquals(3, types.size());
        assertTrue(types.get(0) instanceof NamedType);
        assertTrue(types.get(1) instanceof NamedType);
        assertTrue(types.get(2) instanceof NamedType);

        assertEquals(base, types.get(0).getType().getSuperclass());
        assertEquals(Subtype1WithJsonSubTypes.class, types.get(0).getType());
        assertEquals("Subtype1", types.get(0).getName());

        assertEquals(base, types.get(1).getType().getSuperclass());
        assertEquals(Subtype2WithJsonSubTypes.class, types.get(1).getType());
        assertEquals("Subtype2", types.get(1).getName());

        assertEquals(base, types.get(2).getType().getSuperclass());
        assertEquals(Subtype3WithJsonSubTypes.class, types.get(2).getType());
        assertEquals("Subtype3", types.get(2).getName());
    }

    @JsonTypeName("Subtype1")
    static class Subtype1 {}

    @JsonSubTypes({@JsonSubTypes.Type(value = Subtype2.class, name = "Subtype2")})
    static class TestSubtype {
        // no empty constructor
    }

    static class Subtype2 {
        // no empty constructor
    }

    static class TestWithJsonSubTypes {
        // no empty constructor

        @JsonTypeName("Subtype1")
        static class Subtype1WithJsonSubTypes {}

        @JsonTypeName("Subtype2")
        static class Subtype2WithJsonSubTypes {}

        @JsonTypeName("Subtype3")
        static class Subtype3WithJsonSubTypes {}
    }
}