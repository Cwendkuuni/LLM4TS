package com.fasterxml.jackson.databind.jsontype.impl;

import static com.fasterxml.jackson.databind.jsontype.impl.StdSubtypeResolver.*;
import static org.junit.Assert.*;

import java.lang.reflect.Modifier;
import java.util.*;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.cfg.MapperFactory;
import com.fasterxml.jackson.databind.jsontype.*;
import com.fasterxml.jackson.databind.type.*;
import com.fasterxml.jackson.databind.type.SimpleType.*;

public class StdSubtypeResolverTest {

    @Test
    public void testRegistrationAndResolution() throws Exception {
        MapperFactory mapperFactory = new MapperFactory();
        StdSubtypeResolver resolver = new StdSubtypeResolver();

        // registration
        resolver.registerSubtypes(new NamedType(String.class, "StringWithSuffix"));
        resolver.registerSubtypes(new NamedType(Integer.class, "IntWithPrefix"));
        resolver.registerSubtypes(new NamedType(Object.class, "BaseType"));

        // resolution
        JavaType baseType = TypeFactory.defaultInstance().constructType(Object.class);
        Collection<NamedType> subtypes = resolver.collectAndResolveSubtypesByClass(mapperFactory, null, baseType);
        assertEquals(3, subtypes.size());
        assertNotNull(subtypes.iterator().next().getName());

        // registration and resolution by type id
        JavaType baseTypeById = TypeFactory.defaultInstance().constructType(BaseType.class);
        Collection<NamedType> subtypesById = resolver.collectAndResolveSubtypesByTypeId(mapperFactory, null, baseTypeById);
        assertEquals(1, subtypesById.size());
        assertEquals("BaseType", subtypesById.iterator().next().getName());

        JavaType stringType = TypeFactory.defaultInstance().constructType(String.class);
        Collection<NamedType> stringSubtypes = resolver.collectAndResolveSubtypesByTypeId(mapperFactory, null, stringType);
        assertEquals(1, stringSubtypes.size());
        assertEquals("StringWithSuffix", stringSubtypes.iterator().next().getName());

        JavaType intType = TypeFactory.defaultInstance().constructType(Integer.class);
        Collection<NamedType> intSubtypes = resolver.collectAndResolveSubtypesByTypeId(mapperFactory, null, intType);
        assertEquals(1, intSubtypes.size());
        assertEquals("IntWithPrefix", intSubtypes.iterator().next().getName());
    }

    @Test
    public void testRegistrationAndResolutionWithAnnotations() throws Exception {
        MapperFactory mapperFactory = new MapperFactory();
        StdSubtypeResolver resolver = new StdSubtypeResolver();

        // registration
        resolver.registerSubtypes(new NamedType(String.class, "StringAnnotatedWithSuffix"));
        resolver.registerSubtypes(new NamedType(Integer.class, "IntAnnotatedWithPrefix"));
        resolver.registerSubtypes(new NamedType(Object.class, "BaseTypeAnnotated"));

        // resolution
        JavaType baseType = TypeFactory.defaultInstance().constructType(Object.class);
        Collection<NamedType> subtypes = resolver.collectAndResolveSubtypesByClass(mapperFactory, null, baseType);
        assertEquals(3, subtypes.size());
        assertNotNull(subtypes.iterator().next().getName());

        JavaType baseTypeAnnotation = TypeFactory.defaultInstance().constructType(BaseTypeAnnotated.class);
        Collection<NamedType> subtypesAnnotation = resolver.collectAndResolveSubtypesByClass(mapperFactory, null, baseTypeAnnotation);
        assertEquals(1, subtypesAnnotation.size());
        assertNotNull(subtypesAnnotation.iterator().next().getName());

        JavaType stringTypeAnnotation = TypeFactory.defaultInstance().constructType(StringAnnotatedWithSuffix.class);
        Collection<NamedType> stringSubtypesAnnotation = resolver.collectAndResolveSubtypesByTypeId(mapperFactory, null, stringTypeAnnotation);
        assertEquals(1, stringSubtypesAnnotation.size());
        assertEquals("StringAnnotatedWithSuffix", stringSubtypesAnnotation.iterator().next().getName());

        JavaType intTypeAnnotation = TypeFactory.defaultInstance().constructType(IntAnnotatedWithPrefix.class);
        Collection<NamedType> intSubtypesAnnotation = resolver.collectAndResolveSubtypesByTypeId(mapperFactory, null, intTypeAnnotation);
        assertEquals(1, intSubtypesAnnotation.size());
        assertEquals("IntAnnotatedWithPrefix", intSubtypesAnnotation.iterator().next().getName());
    }

    static class BaseTypeAnnotated {}

    @JsonTypeName("BaseTypeAnnotated")
    static class BaseTypeAnnotatedWithName {}

    @JsonSubTypes({
            @JsonSubTypes.Using(value = JsonTypeName.AsString.class, name = "StringAnnotatedWithSuffix"),
            @JsonSubTypes.Using(value = JsonTypeName.AsString.class, name = "String"),
            @JsonSubTypes.Using(value = JsonTypeName.AsString.class, name = "StringWithSuffix")
    })
    static class StringAnnotatedWithSuffix extends String {}

    @JsonTypeName("StringAnnotatedWithSuffix")
    static class StringWithSuffix extends String {}

    @JsonTypeName("BaseType")
    static class IntAnnotatedWithPrefix extends Integer {
        @JsonTypeName("IntWithPrefix")
        static class IntWithPrefix {}
    }

    @JsonTypeName("IntWithPrefix")
    static class IntWithPrefixAnnotation extends Integer {}
}