package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.*;

import java.lang.reflect.Modifier;
import java.util.*;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.cfg.*;
import com.fasterxml.jackson.databind.introspect.*;
import com.fasterxml.jackson.databind.jsontype.*;
import com.fasterxml.jackson.databind.jsontype.impl.*;

/**
 * Test class for {@link StdSubtypeResolver}.
 */
public class StdSubtypeResolverTest {

    @Test
    public void testRegisterSubtypesByClass() throws Exception {
        StdSubtypeResolver resolver = new StdSubtypeResolver();
        Class<?> baseType = Integer.class;
        Class<?> subtype1 = Long.class;
        Class<?> subtype2 = Short.class;
        Class<?> subtype3 = Integer.class;

        resolver.registerSubtypes(subtype1, subtype2, subtype3);

        MapperConfig<?> config = new MapperConfig.Builder().build();
        AnnotatedClass baseClass = AnnotatedClassResolver.resolveWithoutSuperTypes(config, baseType);
        Collection<NamedType> subtypes = resolver.collectAndResolveSubtypesByClass(config, baseClass);

        assertEquals(3, subtypes.size());
        assertTrue(subtypes.contains(new NamedType(subtype1)));
        assertTrue(subtypes.contains(new NamedType(subtype2)));
        assertTrue(subtypes.contains(new NamedType(subtype3)));
    }

    @Test
    public void testRegisterSubtypesByAnnotation() throws Exception {
        StdSubtypeResolver resolver = new StdSubtypeResolver();

        @JsonSubTypes({
            @JsonSubTypes.With(value = Integer.class, name = "int")
        })
        static class MyClass {
            @JsonProperty
            String property;
        }

        @JsonSubTypes({
            @JsonSubTypes.With(value = Short.class, name = "short"),
            @JsonSubTypes.With(value = Byte.class, name = "byte")
        })
        static class MySubClass extends MyClass {
            // empty
        }

        MapperConfig<?> config = new MapperConfig.Builder().build();
        AnnotatedClass baseClass = AnnotatedClassResolver.resolveWithoutSuperTypes(config, MyClass.class);
        Collection<NamedType> subtypes = resolver.collectAndResolveSubtypesByClass(config, baseClass);

        assertEquals(3, subtypes.size());
        assertTrue(subtypes.contains(new NamedType(Short.class, "short")));
        assertTrue(subtypes.contains(new NamedType(Byte.class, "byte")));
        assertTrue(subtypes.contains(new NamedType(Integer.class, "int")));
    }

    @Test
    public void testRegisterSubtypesByAnnotationWithSuperType() throws Exception {
        StdSubtypeResolver resolver = new StdSubtypeResolver();

        @JsonSubTypes({
            @JsonSubTypes.With(value = Integer.class, name = "int"),
            @JsonSubTypes.With(value = Long.class, name = "long", parent = "int")
        })
        static class MyClass {
            // empty
        }

        @JsonSubTypes({
            @JsonSubTypes.With(value = Short.class, name = "short"),
            @JsonSubTypes.With(value = Byte.class, name = "byte")
        })
        static class MySubClass extends MyClass {
            // empty
        }

        MapperConfig<?> config = new MapperConfig.Builder().build();
        AnnotatedClass baseClass = AnnotatedClassResolver.resolveWithoutSuperTypes(config, MyClass.class);
        Collection<NamedType> subtypes = resolver.collectAndResolveSubtypesByClass(config, baseClass);

        assertEquals(4, subtypes.size());
        assertTrue(subtypes.contains(new NamedType(Short.class, "short")));
        assertTrue(subtypes.contains(new NamedType(Byte.class, "byte")));
        assertTrue(subtypes.contains(new NamedType(Integer.class, "int")));
        assertTrue(subtypes.contains(new NamedType(Long.class, "long")));
    }

    @Test
    public void testCollectAndResolveSubtypesByClassId() throws Exception {
        StdSubtypeResolver resolver = new StdSubtypeResolver();

        MapperConfig<?> config = new MapperConfig.Builder().build();
        AnnotatedClass baseClass = AnnotatedClassResolver.resolveWithoutSuperTypes(config, String.class);
        Collection<NamedType> subtypes = resolver.collectAndResolveSubtypesByClass(config, baseClass);

        assertEquals(0, subtypes.size());

        resolver.registerSubtypes(StringBuilder.class);

        subtypes = resolver.collectAndResolveSubtypesByClass(config, baseClass);

        assertEquals(1, subtypes.size());
        assertTrue(subtypes.contains(new NamedType(StringBuilder.class)));
    }

    @Test
    public void testCollectAndResolveSubtypesByTypeId() throws Exception {
        StdSubtypeResolver resolver = new StdSubtypeResolver();

        MapperConfig<?> config = new MapperConfig.Builder().build();
        AnnotatedMember property = new AnnotatedMember(String.class, "property");
        JavaType baseType = config.getTypeFactory().constructType(String.class);

        Collection<NamedType> subtypes = resolver.collectAndResolveSubtypesByTypeId(config, property, baseType);

        assertEquals(0, subtypes.size());

        resolver.registerSubtypes(StringBuilder.class);

        subtypes = resolver.collectAndResolveSubtypesByTypeId(config, property, baseType);

        assertEquals(1, subtypes.size());
        assertTrue(subtypes.contains(new NamedType(StringBuilder.class)));
    }

    @Test
    public void testCollectAndResolveSubtypesByTypeIdWithAnnotations() throws Exception {
        StdSubtypeResolver resolver = new StdSubtypeResolver();

        @JsonSubTypes({
            @JsonSubTypes.With(value = Integer.class, name = "int"),
            @JsonSubTypes.With(value = Long.class, name = "long", parent = "int")
        })
        static class MyClass {
            @JsonProperty
            String property;
        }

        MapperConfig<?> config = new MapperConfig.Builder().build();
        AnnotatedMember property = new AnnotatedMember(MyClass.class, "property");
        JavaType baseType = config.getTypeFactory().constructType(MyClass.class);

        Collection<NamedType> subtypes = resolver.collectAndResolveSubtypesByTypeId(config, property, baseType);

        assertEquals(2, subtypes.size());
        assertTrue(subtypes.contains(new NamedType(Integer.class, "int")));
        assertTrue(subtypes.contains(new NamedType(Long.class, "long")));
    }
}