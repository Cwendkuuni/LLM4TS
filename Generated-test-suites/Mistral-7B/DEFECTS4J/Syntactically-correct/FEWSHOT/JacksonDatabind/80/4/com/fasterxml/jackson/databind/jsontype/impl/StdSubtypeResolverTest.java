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
 * Test cases for {@link StdSubtypeResolver} class
 */
public class StdSubtypeResolverTest {

    @Test
    public void testRegisterSubtypesByClass() {
        StdSubtypeResolver resolver = new StdSubtypeResolver();
        Class<?> baseType = String.class;
        Class<?>[] subtypes = {Integer.class, Long.class, Float.class};
        resolver.registerSubtypes(subtypes);

        JavaType baseJavaType = ObjectMapper.getDefaultInstance().getTypeFactory().constructType(baseType);
        Collection<NamedType> subtypesByClass = resolver.collectAndResolveSubtypesByClass(
            new MapperConfig.DefaultConfig(), null, baseJavaType);

        assertEquals(3, subtypesByClass.size());
        for (NamedType subtype : subtypesByClass) {
            assertNotNull(subtype.getName());
            assertTrue(subtype.getType().isAssignableFrom(baseType));
        }
    }

    @Test
    public void testRegisterSubtypesByAnnotation() {
        StdSubtypeResolver resolver = new StdSubtypeResolver();

        // Test with a custom annotation
        @JsonSubTypes({
                @JsonSubTypes.With(value = String.class, name = "StringSubtype"),
                @JsonSubTypes.With(value = Integer.class, name = "IntSubtype"),
                @JsonSubTypes.With(value = Long.class, name = "LongSubtype"),
                @JsonSubTypes.With(value = Float.class, name = "FloatSubtype")
        })
        static class TestClass {
            // empty class
        }

        resolver.registerSubtypes(TestClass.class);

        JavaType baseJavaType = ObjectMapper.getDefaultInstance().getTypeFactory().constructType(String.class);
        Collection<NamedType> subtypesByTypeId = resolver.collectAndResolveSubtypesByTypeId(
            new MapperConfig.DefaultConfig(), null, baseJavaType);

        assertEquals(4, subtypesByTypeId.size());
        for (NamedType subtype : subtypesByTypeId) {
            assertNotNull(subtype.getName());
            assertTrue(subtype.getType().isAssignableFrom(String.class));
        }

        // Test with standard Java annotations
        @JsonTypeName("StringSubtype")
        static class StringSubtype extends String {
            // empty class
        }

        @JsonTypeName("IntSubtype")
        static class IntSubtype extends Number {
            @Override
            public int intValue() {
                return 0;
            }
        }

        @JsonTypeName("LongSubtype")
        static class LongSubtype extends Number {
            @Override
            public long longValue() {
                return 0;
            }
        }

        @JsonTypeName("FloatSubtype")
        static class FloatSubtype extends Number {
            @Override
            public float floatValue() {
                return 0;
            }
        }

        resolver = new StdSubtypeResolver();
        resolver.registerSubtypes(StringSubtype.class);
        resolver.registerSubtypes(IntSubtype.class);
        resolver.registerSubtypes(LongSubtype.class);
        resolver.registerSubtypes(FloatSubtype.class);

        baseJavaType = ObjectMapper.getDefaultInstance().getTypeFactory().constructType(String.class);
        subtypesByTypeId = resolver.collectAndResolveSubtypesByTypeId(
            new MapperConfig.DefaultConfig(), null, baseJavaType);

        assertEquals(4, subtypesByTypeId.size());
        for (NamedType subtype : subtypesByTypeId) {
            assertNotNull(subtype.getName());
            assertTrue(subtype.getType().isAssignableFrom(String.class));
        }
    }

    @Test
    public void testCombineNamedAndUnnamed() {
        StdSubtypeResolver resolver = new StdSubtypeResolver();

        // Test with a custom annotation
        @JsonSubTypes({
                @JsonSubTypes.With(value = String.class, name = "StringSubtype"),
                @JsonSubTypes.With(value = Integer.class, name = "IntSubtype"),
                @JsonSubTypes.With(value = Long.class, name = "LongSubtype"),
                @JsonSubTypes.With(value = Float.class, name = "FloatSubtype")
        })
        static class TestClass {
            // empty class
        }

        resolver.registerSubtypes(TestClass.class);

        JavaType baseJavaType = ObjectMapper.getDefaultInstance().getTypeFactory().constructType(String.class);
        Collection<NamedType> subtypesByTypeId = resolver.collectAndResolveSubtypesByTypeId(
            new MapperConfig.DefaultConfig(), null, baseJavaType);

        assertEquals(4, subtypesByTypeId.size());

        Set<Class<?>> typesHandled = new HashSet<>();
        Map<String, NamedType> byName = new LinkedHashMap<>();

        // Test with no explicitly named types
        resolver = new StdSubtypeResolver();
        resolver.registerSubtypes(String.class);
        resolver.registerSubtypes(Integer.class);
        resolver.registerSubtypes(Long.class);
        resolver.registerSubtypes(Float.class);

        Collection<NamedType> subtypes = resolver._combineNamedAndUnnamed(
                String.class, typesHandled, byName);

        assertEquals(7, subtypes.size());
        for (NamedType subtype : subtypes) {
            if (subtype.hasName()) {
                // Explicitly named types should have non-null names
                assertNotNull(subtype.getName());
                assertTrue(byName.containsKey(subtype.getName()));
            } else {
                // Unnamed types should have null names
                assertNull(subtype.getName());
            }
            assertTrue(subtype.getType().isAssignableFrom(String.class));
        }
    }
}