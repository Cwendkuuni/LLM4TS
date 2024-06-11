package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.fasterxml.jackson.databind.AnnotationIntrospector;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.jsontype.NamedType;
import com.fasterxml.jackson.databind.jsontype.SubtypeResolver;
import com.fasterxml.jackson.databind.jsontype.impl.StdSubtypeResolver;

public class StdSubtypeResolverTest {

    private ObjectMapper objectMapper;
    private SubtypeResolver subtypeResolver;

    @Before
    public void setup() {
        subtypeResolver = new StdSubtypeResolver();
        objectMapper = new ObjectMapper();
        objectMapper.setSubtypeResolver(subtypeResolver);
        objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
    }

    @Test
    public void testRegisterSubtypesByClass() {
        Class<?> baseType = Foo.class;
        FooBar fooBar1 = new FooBar1();
        FooBar fooBar2 = new FooBar2();

        subtypeResolver.registerSubtypes(baseType, FooBar1.class, FooBar2.class);

        List<NamedType> subtypes = subtypeResolver.collectAndResolveSubtypesByClass(objectMapper.getConfig(),
                null, baseType);

        assertEquals(Arrays.asList(new NamedType(baseType, null), new NamedType(FooBar1.class, null),
                new NamedType(FooBar2.class, null)), subtypes);

        // Test registration for same class twice
        subtypeResolver.registerSubtypes(baseType, FooBar1.class, FooBar2.class);
        subtypes = subtypeResolver.collectAndResolveSubtypesByClass(objectMapper.getConfig(), null, baseType);
        assertEquals(Arrays.asList(new NamedType(baseType, null), new NamedType(FooBar1.class, null),
                new NamedType(FooBar2.class, null)), subtypes);

        // Test registration for different classes
        Class<?> baseType2 = BarFoo.class;
        BarFooBar barFooBar1 = new BarFooBar1();
        BarFooBar barFooBar2 = new BarFooBar2();

        subtypeResolver.registerSubtypes(baseType2, BarFooBar1.class, BarFooBar2.class);

        subtypes = subtypeResolver.collectAndResolveSubtypesByClass(objectMapper.getConfig(), null, baseType2);

        assertEquals(Arrays.asList(new NamedType(baseType2, null), new NamedType(BarFooBar1.class, null),
                new NamedType(BarFooBar2.class, null)), subtypes);
    }

    @Test
    public void testRegisterSubtypesAnnotated() {
        Class<?> baseType = FooAnnotated.class;

        FooAnnotatedBar1 fooAnnotatedBar1 = new FooAnnotatedBar1();
        FooAnnotatedBar2 fooAnnotatedBar2 = new FooAnnotatedBar2();

        subtypeResolver.registerSubtypes(baseType, FooAnnotatedBar1.class, FooAnnotatedBar2.class);

        List<NamedType> subtypes = subtypeResolver.collectAndResolveSubtypesByClass(objectMapper.getConfig(),
                null, baseType);

        assertEquals(Arrays.asList(new NamedType(baseType, null),
                new NamedType(FooAnnotatedBar1.class, null),
                new NamedType(FooAnnotatedBar2.class, null)), subtypes);
    }

    @Test
    public void testRegisterSubtypesByName() {
        Class<?> baseType = FooByName.class;

        FooByNameBar1 fooByNameBar1 = new FooByNameBar1();
        FooByNameBar2 fooByNameBar2 = new FooByNameBar2();

        subtypeResolver.registerSubtypes(baseType, new NamedType(FooByNameBar1.class, "fooBar1"));
        subtypeResolver.registerSubtypes(baseType, new NamedType(FooByNameBar2.class, "fooBar2"));

        List<NamedType> subtypes = subtypeResolver.collectAndResolveSubtypesByClass(objectMapper.getConfig(),
                null, baseType);

        assertEquals(Arrays.asList(new NamedType(baseType, null),
                new NamedType(FooByNameBar1.class, "fooBar1"),
                new NamedType(FooByNameBar2.class, "fooBar2")), subtypes);
    }

    @Test
    public void testCollectAndResolveSubtypesByTypeIdClass() {
        Class<?> baseType = Foo.class;
        FooBar fooBar1 = new FooBar1();
        FooBar fooBar2 = new FooBar2();

        subtypeResolver.registerSubtypes(baseType, FooBar1.class, FooBar2.class);

        String inputJson = "{\"typeId\":1,\"fooBar\":{\"name\":\"fooBar1\"}}";
        Object jsonObject = objectMapper.readValue(new ObjectMapper().writeValueAsString(inputJson),
                Map.class);

        List<NamedType> subtypes = subtypeResolver.collectAndResolveSubtypesByClass(objectMapper.getConfig(),
                null, baseType);

        MappingIterator<Map<String, Object>> it = objectMapper.readerForMapOf(Map.class).readValues(inputJson);
        Object jsonObjectDeserialized = it.next();

        assertEquals(Arrays.asList(new NamedType(baseType, null), new NamedType(FooBar1.class, null),
                new NamedType(FooBar2.class, null)), subtypes);

        assertEquals(fooBar1, objectMapper.treeToValue(jsonObjectDeserialized, FooBar.class));
    }

    @Test
    public void testCollectAndResolveSubtypesByTypeIdAnnotated() {
        Class<?> baseType = FooAnnotated.class;

        FooAnnotatedBar1 fooAnnotatedBar1 = new FooAnnotatedBar1();
        FooAnnotatedBar2 fooAnnotatedBar2 = new FooAnnotatedBar2();

        subtypeResolver.registerSubtypes(baseType, FooAnnotatedBar1.class, FooAnnotatedBar2.class);

        String inputJson = "{\"typeId\":\"fooAnnotatedBar1\",\"fooAnnotated\":{\"name\":\"fooAnnotatedBar1\"}}";
        Object jsonObject = objectMapper.readValue(new ObjectMapper().writeValueAsString(inputJson),
                Map.class);

        List<NamedType> subtypes = subtypeResolver.collectAndResolveSubtypesByClass(objectMapper.getConfig(),
                null, baseType);

        MappingIterator<Map<String, Object>> it = objectMapper.readerForMapOf(Map.class).readValues(inputJson);
        Object jsonObjectDeserialized = it.next();

        assertEquals(Arrays.asList(new NamedType(baseType, null),
                new NamedType(FooAnnotatedBar1.class, "fooAnnotatedBar1"),
                new NamedType(FooAnnotatedBar2.class, "fooAnnotatedBar2")), subtypes);

        assertEquals(fooAnnotatedBar1, objectMapper.treeToValue(jsonObjectDeserialized, FooAnnotatedBar.class));
    }

    @Test
    public void testCollectAndResolveSubtypesByTypeIdByName() {
        Class<?> baseType = FooByName.class;

        FooByNameBar1 fooByNameBar1 = new FooByNameBar1();
        FooByNameBar2 fooByNameBar2 = new FooByNameBar2();

        subtypeResolver.registerSubtypes(baseType, new NamedType(FooByNameBar1.class, "fooBar1"));
        subtypeResolver.registerSubtypes(baseType, new NamedType(FooByNameBar2.class, "fooBar2"));

        String inputJson = "{\"typeId\":\"fooBar1\",\"fooByName\":{\"name\":\"fooBar1\"}}";
        Object jsonObject = objectMapper.readValue(new ObjectMapper().writeValueAsString(inputJson),
                Map.class);

        List<NamedType> subtypes = subtypeResolver.collectAndResolveSubtypesByClass(objectMapper.getConfig(),
                null, baseType);

        MappingIterator<Map<String, Object>> it = objectMapper.readerForMapOf(Map.class).readValues(inputJson);
        Object jsonObjectDeserialized = it.next();

        assertEquals(Arrays.asList(new NamedType(baseType, null),
                new NamedType(FooByNameBar1.class, "fooBar1"),
                new NamedType(FooByNameBar2.class, "fooBar2")), subtypes);

        assertEquals(fooByNameBar1, objectMapper.treeToValue(jsonObjectDeserialized, FooByNameBar.class));
    }

    @Test
    public void testCollectAndResolveSubtypesByTypeIdEmptyList() {
        Class<?> baseType = FooByName.class;

        List<NamedType> subtypes = subtypeResolver.collectAndResolveSubtypesByClass(objectMapper.getConfig(),
                null, baseType);

        assertEquals(Collections.emptyList(), subtypes);
    }

    @JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "typeId")
    static class Foo {
    }

    @JsonTypeName("fooBar1")
    static class FooBar1 {
        String name;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }

    @JsonTypeName("fooBar2")
    static class FooBar2 {
        String name;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }

    @JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "typeId")
    @JsonTypeName("fooAnnotatedBar1")
    static class FooAnnotatedBar1 {
        @JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "typeId")
        @JsonTypeName("fooAnnotated")
        static class FooAnnotated {
            String name;

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }
        }

        String name;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }

    @JsonTypeName("fooAnnotatedBar2")
    static class FooAnnotatedBar2 {
        @JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "typeId")
        @JsonTypeName("fooAnnotated")
        static class FooAnnotated {
            String name;

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }
        }

        String name;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }

    @JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "typeId")
    @JsonTypeName("fooByNameBar1")
    static class FooByNameBar1 {
        @JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "typeId")
        static class FooByName {
            String name;

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }
        }

        String name;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }

    @JsonTypeName("fooByNameBar2")
    static class FooByNameBar2 {
        @JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "typeId")
        static class FooByName {
            String name;

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }
        }

        String name;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }

    static class BarFoo {
        String name;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }

    @JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "typeId")
    @JsonTypeName("barFooBar1")
    static class BarFooBar1 extends BarFoo {
        // Empty class
    }

    @JsonTypeName("barFooBar2")
    static class BarFooBar2 extends BarFoo {
        // Empty class
    }
}