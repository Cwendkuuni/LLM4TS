package com.fasterxml.jackson.databind.jsontype.impl;

import com.fasterxml.jackson.annotation.JsonTypeName;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.jsontype.NamedType;
import com.fasterxml.jackson.databind.jsontype.StdSubtypeResolver;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
import com.fasterxml.jackson.databind.type.TypeBindings;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

public class StdSubtypeResolverTest {

    private ObjectMapper objectMapper;
    private StdSubtypeResolver subtypeResolver;

    @Before
    public void setup() {
        ObjectMapper.DefaultTypingResolver defaultTypingResolver = new ObjectMapper.DefaultTypingResolver();
        MappingIteratorFactory miFactory = new MappingIteratorFactory(new DefaultSerializers.BaseSerializerFinder(), new DefaultDeserializers.BaseDeserializerFinder());
        DefaultSerializers serializers = new DefaultSerializers();
        DefaultDeserializers deserializers = new DefaultDeserializers();

        TypeIdResolver typeIdResolver = new MapperDefaultTypeIdResolver();
        DefaultSerializers.BaseSerializerFinder serializerFinder = new DefaultSerializers.BaseSerializerFinder();
        serializerFinder.findRootValueSerializer(String.class);
        DefaultDeserializers.BaseDeserializerFinder deserializerFinder = new DefaultDeserializers.BaseDeserializerFinder();
        deserializerFinder.findRootValueDeserializer(String.class);
        ObjectMapper.DefaultSerializerProvider defaultProvider = new ObjectMapper.DefaultSerializerProvider(defaultTypingResolver, serializerFinder, miFactory, serializers);
        ObjectMapper.DefaultDeserializerProvider defaultDeserializerProvider = new ObjectMapper.DefaultDeserializerProvider(typeIdResolver, deserializerFinder, deserializers);

        this.objectMapper = new ObjectMapper(defaultProvider, defaultDeserializerProvider);
        this.subtypeResolver = new StdSubtypeResolver();
    }

    @Test
    public void testRegisterSubtypesByClass() {
        this.subtypeResolver.registerSubtypes(MyClass1.class);
        this.subtypeResolver.registerSubtypes(MyClass2.class);

        MapperConfig<?> config = this.objectMapper.getConfiguration();
        List<NamedType> subtypes = this.subtypeResolver.collectAndResolveSubtypesByClass(config, null, null);
        assertEquals(2, subtypes.size());
        assertTrue(subtypes.contains(new NamedType(MyClass1.class)));
        assertTrue(subtypes.contains(new NamedType(MyClass2.class)));
    }

    @Test
    public void testRegisterSubtypesByName() {
        this.subtypeResolver.registerSubtypes(MyClassWithCustomName.class, "CUSTOM_NAME");

        MapperConfig<?> config = this.objectMapper.getConfiguration();
        List<NamedType> subtypes = this.subtypeResolver.collectAndResolveSubtypesByClass(config, null, null);
        assertEquals(1, subtypes.size());
        assertEquals("CUSTOM_NAME", subtypes.get(0).getName());
        assertEquals(MyClassWithCustomName.class, subtypes.get(0).getType());
    }

    @Test
    public void testCollectAndResolveSubtypesByClass() {
        MyClass1 myClass1 = new MyClass1();
        MyClass2 myClass2 = new MyClass2();

        this.subtypeResolver.registerSubtypes(MyClass1.class);
        this.subtypeResolver.registerSubtypes(MyClass2.class);

        MapperConfig<?> config = this.objectMapper.getConfiguration();
        List<NamedType> subtypes = this.subtypeResolver.collectAndResolveSubtypesByClass(config, null, null);

        List<Object> inheritedSubtypes = Arrays.asList(new MyClass1(), new MyClass2());
        List<Object> deserializedSubtypes = this.objectMapper.readValue(new ObjectMapper().writeValueAsString(subtypes), new TypeReference<List<Object>>(){});
        assertEquals(inheritedSubtypes, deserializedSubtypes);
    }

    @Test
    public void testCollectAndResolveSubtypesByTypeId() {
        MyClass1WithId myClass1 = new MyClass1WithId();
        MyClass2WithId myClass2 = new MyClass2WithId();

        this.subtypeResolver.registerSubtypes(MyClass1WithId.class, "ID_1");
        this.subtypeResolver.registerSubtypes(MyClass2WithId.class, "ID_2");

        MapperConfig<?> config = this.objectMapper.getConfiguration();
        List<NamedType> subtypes = this.subtypeResolver.collectAndResolveSubtypesByTypeId(config, null, null);

        List<Object> inheritedSubtypes = Arrays.asList(new MyClass1WithId(), new MyClass2WithId());
        List<Object> deserializedSubtypes = this.objectMapper.readValue(new ObjectMapper().writeValueAsString(subtypes), new TypeReference<List<Object>>(){});
        assertEquals(inheritedSubtypes, deserializedSubtypes);
    }

    @Test
    public void testCollectAndResolveSubtypesByTypeIdWithCustomName() {
        MyClassWithCustomNameWithId customNameClass = new MyClassWithCustomNameWithId();

        this.subtypeResolver.registerSubtypes(MyClassWithCustomNameWithId.class, "ID_3", "CUSTOM_NAME");

        MapperConfig<?> config = this.objectMapper.getConfiguration();
        List<NamedType> subtypes = this.subtypeResolver.collectAndResolveSubtypesByTypeId(config, null, null);

        assertEquals(1, subtypes.size());
        assertEquals("CUSTOM_NAME", subtypes.get(0).getName());
        assertEquals(MyClassWithCustomNameWithId.class, subtypes.get(0).getType());
    }

    @JsonTypeName("ID_1")
    static class MyClass1 {
    }

    @JsonTypeName("ID_2")
    static class MyClass2 {
    }

    @JsonTypeName("ID_3")
    static class MyClassWithCustomName {
        public String name = "CustomName";
    }

    static class MyClassWithCustomNameWithId extends MyClassWithCustomName {
        public String id = "ID_3";
    }
}