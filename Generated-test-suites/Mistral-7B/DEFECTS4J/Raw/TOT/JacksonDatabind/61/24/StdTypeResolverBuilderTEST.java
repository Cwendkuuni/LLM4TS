package com.fasterxml.jackson.databind.jsontype.impl;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationConfig;
import com.fasterxml.jackson.databind.deser.std.AsArrayTypeDeserializer;
import com.fasterxml.jackson.databind.deser.std.AsPropertyTypeDeserializer;
import com.fasterxml.jackson.databind.deser.std.AsWrapperTypeDeserializer;
import com.fasterxml.jackson.databind.deser.std.AsExternalTypeDeserializer;
import com.fasterxml.jackson.databind.type.ClassIdResolver;
import com.fasterxml.jackson.databind.type.MinimalClassNameIdResolver;
import com.fasterxml.jackson.databind.type.TypeNameIdResolver;
import com.fasterxml.jackson.databind.type.TypeResolverBuilder;
import com.fasterxml.jackson.databind.type.TypeSerializer;
import com.fasterxml.jackson.databind.type.Types;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class StdTypeResolverBuilderTest {

    private ObjectMapper objectMapper;
    private SerializationConfig serializationConfig;
    private TypeResolverBuilder<StdTypeResolverBuilder> typeResolverBuilder;

    @Before
    public void setUp() throws IOException {
        serializationConfig = new SerializationConfig();
        objectMapper = new ObjectMapper(serializationConfig);
        typeResolverBuilder = new StdTypeResolverBuilder();
    }

    @Test
    public void testBuildTypeSerializer_Property_EmptyInput() throws IOException {
        // Test case for building TypeSerializer with inclusion type as "PROPERTY" and empty input
        JavaType baseType = Types.collectionType(Map.class, String.class);
        Collection<NamedType> subtypes = Arrays.asList(
                new NamedType(String.class, "StringType"),
                new NamedType(Integer.class, "IntegerType")
        );

        typeResolverBuilder.init(JsonTypeInfo.Id.PROPERTY, null)
                .typeProperty("typeProperty");

        TypeSerializer typeSerializer = typeResolverBuilder.buildTypeSerializer(serializationConfig, baseType, subtypes);
        Assert.assertNull(typeSerializer);
    }

    @Test
    public void testBuildTypeSerializer_Property_ValidInput() throws IOException {
        // Test case for building TypeSerializer with inclusion type as "PROPERTY" and valid input
        JavaType baseType = Types.collectionType(Map.class, String.class);
        Collection<NamedType> subtypes = Arrays.asList(
                new NamedType(String.class, "StringType"),
                new NamedType(Integer.class, "IntegerType")
        );

        typeResolverBuilder.init(JsonTypeInfo.Id.PROPERTY, null)
                .typeProperty("typeProperty");

        TypeSerializer typeSerializer = typeResolverBuilder.buildTypeSerializer(serializationConfig, baseType, subtypes);
        Assert.assertNotNull(typeSerializer);

        Map<String, Object> testMap = new HashMap<>();
        testMap.put("StringKey", "StringValue");
        testMap.put("IntegerKey", 123);

        byte[] serializedMap = objectMapper.writeValueAsBytes(testMap);
        Assert.assertArrayNotNull(serializedMap);
    }

    @Test
    public void testBuildTypeDeserializer_Property_EmptyInput() throws IOException {
        // Test case for building TypeDeserializer with inclusion type as "PROPERTY" and empty input
        JavaType baseType = Types.collectionType(Map.class, String.class);
        Collection<NamedType> subtypes = Arrays.asList(
                new NamedType(String.class, "StringType"),
                new NamedType(Integer.class, "IntegerType")
        );

        typeResolverBuilder.init(JsonTypeInfo.Id.PROPERTY, null)
                .typeProperty("typeProperty");

        TypeDeserializer typeDeserializer = typeResolverBuilder.buildTypeDeserializer(serializationConfig, baseType, subtypes);
        Assert.assertNull(typeDeserializer);
    }

    @Test
    public void testBuildTypeDeserializer_Property_ValidInput() throws IOException {
        // Test case for building TypeDeserializer with inclusion type as "PROPERTY" and valid input
        JavaType baseType = Types.collectionType(Map.class, String.class);
        Collection<NamedType> subtypes = Arrays.asList(
                new NamedType(String.class, "StringType"),
                new NamedType(Integer.class, "IntegerType")
        );

        typeResolverBuilder.init(JsonTypeInfo.Id.PROPERTY, null)
                .typeProperty("typeProperty");

        TypeDeserializer typeDeserializer = typeResolverBuilder.buildTypeDeserializer(serializationConfig, baseType, subtypes);
        Assert.assertNotNull(typeDeserializer);

        byte[] serializedMap = new byte[0]; // Empty JSON byte array
        Map<String, Object> deserializedMap = objectMapper.readValue(serializedMap, Map.class);
        Assert.assertNotNull(deserializedMap);

        byte[] serializedValidMap = this.getClass().getResourceAsStream("/test-data.json").readAllBytes();
        Map<String, Object> deserializedValidMap = objectMapper.readValue(serializedValidMap, Map.class);
        Assert.assertNotNull(deserializedValidMap);
    }

    @Test
    public void testBuildTypeSerializer_WrapperObject_EmptyInput() throws IOException {
        // Test case for building TypeSerializer with inclusion type as "WRAPPER_OBJECT" and empty input
        JavaType baseType = Types.collectionType(Map.class, String.class);

        typeResolverBuilder.init(JsonTypeInfo.Id.WRAPPER_OBJECT, null)
                .typeProperty("typeProperty");

        TypeSerializer typeSerializer = typeResolverBuilder.buildTypeSerializer(serializationConfig, baseType, null);
        Assert.assertNull(typeSerializer);
    }

    @Test
    public void testBuildTypeSerializer_WrapperObject_ValidInput() throws IOException {
        // Test case for building TypeSerializer with inclusion type as "WRAPPER_OBJECT" and valid input
        JavaType baseType = Types.collectionType(Map.class, String.class);

        typeResolverBuilder.init(JsonTypeInfo.Id.WRAPPER_OBJECT, null)
                .typeProperty("typeProperty");

        TypeSerializer typeSerializer = typeResolverBuilder.buildTypeSerializer(serializationConfig, baseType, null);
        Assert.assertNotNull(typeSerializer);

        Map<String, Object> testMap = new HashMap<>();
        testMap.put("StringKey", "StringValue");
        testMap.put("IntegerKey", 123);

        byte[] serializedMap = objectMapper.writeValueAsBytes(testMap);
        Assert.assertArrayNotNull(serializedMap);
    }

    @Test
    public void testBuildTypeDeserializer_WrapperObject_EmptyInput() throws IOException {
        // Test case for building TypeDeserializer with inclusion type as "WRAPPER_OBJECT" and empty input
        JavaType baseType = Types.collectionType(Map.class, String.class);

        typeResolverBuilder.init(JsonTypeInfo.Id.WRAPPER_OBJECT, null)
                .typeProperty("typeProperty");

        TypeDeserializer typeDeserializer = typeResolverBuilder.buildTypeDeserializer(serializationConfig, baseType, null);
        Assert.assertNull(typeDeserializer);
    }

    @Test
    public void testBuildTypeDeserializer_WrapperObject_ValidInput() throws IOException {
        // Test case for building TypeDeserializer with inclusion type as "WRAPPER_OBJECT" and valid input
        JavaType baseType = Types.collectionType(Map.class, String.class);

        typeResolverBuilder.init(JsonTypeInfo.Id.WRAPPER_OBJECT, null)
                .typeProperty("typeProperty");

        TypeDeserializer typeDeserializer = typeResolverBuilder.buildTypeDeserializer(serializationConfig, baseType, null);
        Assert.assertNotNull(typeDeserializer);

        byte[] serializedMap = new byte[0]; // Empty JSON byte array
        Map<String, Object> deserializedMap = objectMapper.readValue(serializedMap, Map.class);
        Assert.assertNotNull(deserializedMap);

        byte[] serializedValidMap = this.getClass().getResourceAsStream("/test-data.json").readAllBytes();
        Map<String, Object> deserializedValidMap = objectMapper.readValue(serializedValidMap, Map.class);
        Assert.assertNotNull(deserializedValidMap);
    }

    @Test
    public void testBuildTypeSerializer_WrapperArray_EmptyInput() throws IOException {
        // Test case for building TypeSerializer with inclusion type as "WRAPPER_ARRAY" and empty input
        JavaType baseType = Types.collectionType(int[].class);

        typeResolverBuilder.init(JsonTypeInfo.Id.WRAPPER_ARRAY, null)
                .typeProperty("typeProperty");

        TypeSerializer typeSerializer = typeResolverBuilder.buildTypeSerializer(serializationConfig, baseType, null);
        Assert.assertNull(typeSerializer);
    }

    @Test
    public void testBuildTypeSerializer_WrapperArray_ValidInput() throws IOException {
        // Test case for building TypeSerializer with inclusion type as "WRAPPER_ARRAY" and valid input
        JavaType baseType = Types.collectionType(int[].class);

        typeResolverBuilder.init(JsonTypeInfo.Id.WRAPPER_ARRAY, null)
                .typeProperty("typeProperty");

        TypeSerializer typeSerializer = typeResolverBuilder.buildTypeSerializer(serializationConfig, baseType, null);
        Assert.assertNotNull(typeSerializer);

        int[] testArray = {1, 2, 3};

        byte[] serializedArray = objectMapper.writeValueAsBytes(testArray);
        Assert.assertArrayNotNull(serializedArray);
    }

    @Test
    public void testBuildTypeDeserializer_WrapperArray_EmptyInput() throws IOException {
        // Test case for building TypeDeserializer with inclusion type as "WRAPPER_ARRAY" and empty input
        JavaType baseType = Types.collectionType(int[].class);

        typeResolverBuilder.init(JsonTypeInfo.Id.WRAPPER_ARRAY, null)
                .typeProperty("typeProperty");

        TypeDeserializer typeDeserializer = typeResolverBuilder.buildTypeDeserializer(serializationConfig, baseType, null);
        Assert.assertNull(typeDeserializer);
    }

    @Test
    public void testBuildTypeDeserializer_WrapperArray_ValidInput() throws IOException {
        // Test case for building TypeDeserializer with inclusion type as "WRAPPER_ARRAY" and valid input
        JavaType baseType = Types.collectionType(int[].class);

        typeResolverBuilder.init(JsonTypeInfo.Id.WRAPPER_ARRAY, null)
                .typeProperty("typeProperty");

        TypeDeserializer typeDeserializer = typeResolverBuilder.buildTypeDeserializer(serializationConfig, baseType, null);
        Assert.assertNotNull(typeDeserializer);

        byte[] serializedArray = new byte[0]; // Empty JSON byte array
        int[] deserializedArray = typeDeserializer.deserialize(serializedArray, int[].class);
        Assert.assertNotNull(deserializedArray);

        byte[] serializedValidArray = this.getClass().getResourceAsStream("/test-data.json").readAllBytes();
        int[] deserializedValidArray = typeDeserializer.deserialize(serializedValidArray, int[].class);
        Assert.assertNotNull(deserializedValidArray);
    }

    @Test
    public void testBuildTypeSerializer_ExternalProperty_EmptyInput() throws IOException {
        // Test case for building TypeSerializer with inclusion type as "EXTERNAL_PROPERTY" and empty input
        JavaType baseType = Types.collectionType(Map.class, String.class);

        typeResolverBuilder.init(JsonTypeInfo.Id.EXTERNAL_PROPERTY, null)
                .typeProperty("typeProperty");

        TypeSerializer typeSerializer = typeResolverBuilder.buildTypeSerializer(serializationConfig, baseType, null);
        Assert.assertNull(typeSerializer);
    }

    @Test
    public void testBuildTypeSerializer_ExternalProperty_ValidInput() throws IOException {
        // Test case for building TypeSerializer with inclusion type as "EXTERNAL_PROPERTY" and valid input
        JavaType baseType = Types.collectionType(Map.class, String.class);

        typeResolverBuilder.init(JsonTypeInfo.Id.EXTERNAL_PROPERTY, null)
                .typeProperty("typeProperty");

        TypeSerializer typeSerializer = typeResolverBuilder.buildTypeSerializer(serializationConfig, baseType, null);
        Assert.assertNotNull(typeSerializer);

        Map<String, Object> testMap = new HashMap<>();
        testMap.put("StringKey", "StringValue");
        testMap.put("IntegerKey", 123);

        byte[] serializedMap = objectMapper.writeValueAsBytes(testMap);
        Assert.assertArrayNotNull(serializedMap);
    }

    @Test
    public void testBuildTypeDeserializer_ExternalProperty_EmptyInput() throws IOException {
        // Test case for building TypeDeserializer with inclusion type as "EXTERNAL_PROPERTY" and empty input
        JavaType baseType = Types.collectionType(Map.class, String.class);

        typeResolverBuilder.init(JsonTypeInfo.Id.EXTERNAL_PROPERTY, null)
                .typeProperty("typeProperty");

        TypeDeserializer typeDeserializer = typeResolverBuilder.buildTypeDeserializer(serializationConfig, baseType, null);
        Assert.assertNull(typeDeserializer);
    }

    @Test
    public void testBuildTypeDeserializer_ExternalProperty_ValidInput() throws IOException {
        // Test case for building TypeDeserializer with inclusion type as "EXTERNAL_PROPERTY" and valid input
        JavaType baseType = Types.collectionType(Map.class, String.class);

        typeResolverBuilder.init(JsonTypeInfo.Id.EXTERNAL_PROPERTY, null)
                .typeProperty("typeProperty");

        TypeDeserializer typeDeserializer = typeResolverBuilder.buildTypeDeserializer(serializationConfig, baseType, null);
        Assert.assertNotNull(typeDeserializer);

        byte[] serializedMap = new byte[0]; // Empty JSON byte array
        Map<String, Object> deserializedMap = typeDeserializer.deserialize(serializedMap, Map.class);
        Assert.assertNotNull(deserializedMap);

        byte[] serializedValidMap = this.getClass().getResourceAsStream("/test-data.json").readAllBytes();
        Map<String, Object> deserializedValidMap = typeDeserializer.deserialize(serializedValidMap, Map.class);
        Assert.assertNotNull(deserializedValidMap);
    }
}