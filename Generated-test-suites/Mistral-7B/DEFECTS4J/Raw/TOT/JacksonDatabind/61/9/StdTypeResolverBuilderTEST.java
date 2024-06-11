package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.*;

import com.fasterxml.jackson.annotation.JsonTypeInfo.As;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.cfg.MapperFactory;
import com.fasterxml.jackson.databind.jsontype.*;
import com.fasterxml.jackson.databind.node.*;

import org.junit.Test;

public class StdTypeResolverBuilderTest {

    @Test
    public void testStdTypeResolverBuilder_PropertyType() throws Exception {
        JsonTypeInfo.Id idType = JsonTypeInfo.Id.CLASS;
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder()
                .init(idType, null)
                .typeProperty("typeProperty")
                .typeIdVisibility(true)
                .inclusion(As.PROPERTY);

        ObjectMapper mapper = new ObjectMapper();
        MapperFactory mf = mapper.getFactory();
        TypeSerializer ts = builder.buildTypeSerializer(mf.getConfig(),
                JavaType.valueOf("java.lang.String"), null);
        TypeDeserializer td = builder.buildTypeDeserializer(mapper,
                JavaType.valueOf("java.lang.String"), null);

        String json = "{\"value\": \"Hello World\", \"typeProperty\":\"StringType\"}";
        ObjectReader reader = mapper.readerFor(String.class);
        JsonNode node = mapper.readTree(json);

        Object deserializedObject = reader.readValue(node);
        assertEquals("Hello World", deserializedObject);
        assertEquals("StringType", ts.serializeValueAsString(null));

        ObjectMapper objectMapper = new ObjectMapper();
        TypeSerializer ts2 = objectMapper.getTypeFactory()
                .constructTypeSerializer(String.class);
        assertTrue(ts.isSameTypeSerializer(ts2));
    }

    @Test
    public void testStdTypeResolverBuilder_WrapperObjectType() throws Exception {
        JsonTypeInfo.Id idType = JsonTypeInfo.Id.CLASS;
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder()
                .init(idType, null)
                .typeProperty("typeProperty")
                .typeIdVisibility(true)
                .inclusion(As.WRAPPER_OBJECT);

        ObjectMapper mapper = new ObjectMapper();
        MapperFactory mf = mapper.getFactory();
        TypeSerializer ts = builder.buildTypeSerializer(mf.getConfig(),
                JavaType.valueOf("java.lang.String"), null);
        TypeDeserializer td = builder.buildTypeDeserializer(mapper,
                JavaType.valueOf("java.lang.String"), null);

        String json = "{\"value\": \"Hello World\", \"typeProperty\":{\"@class\":\"java.lang.String\", \"value\":\"StringType\"}}";
        ObjectReader reader = mapper.readerFor(String.class);
        JsonNode node = mapper.readTree(json);

        Object deserializedObject = reader.readValue(node);
        assertEquals("Hello World", deserializedObject);
        assertEquals("StringType", ts.serializeValueAsString(null));

        ObjectMapper objectMapper = new ObjectMapper();
        TypeSerializer ts2 = objectMapper.getTypeFactory()
                .constructTypeSerializer(String.class);
        assertTrue(ts.isSameTypeSerializer(ts2));
    }

    @Test
    public void testStdTypeResolverBuilder_WrapperArrayType() throws Exception {
        JsonTypeInfo.Id idType = JsonTypeInfo.Id.CLASS;
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder()
                .init(idType, null)
                .typeProperty("typeProperty")
                .typeIdVisibility(true)
                .inclusion(As.WRAPPER_ARRAY);

        ObjectMapper mapper = new ObjectMapper();
        MapperFactory mf = mapper.getFactory();
        TypeSerializer ts = builder.buildTypeSerializer(mf.getConfig(),
                JavaType.valueOf("java.lang.String[]"), null);
        TypeDeserializer td = builder.buildTypeDeserializer(mapper,
                JavaType.valueOf("java.lang.String[]"), null);

        String json = "[{\"@class\":\"java.lang.String\", \"value\":\"Hello World\"}]";
        ObjectReader reader = mapper.readerFor(String[].class);
        JsonNode node = mapper.readTree(json);

        Object deserializedObject = reader.readValue(node);
        String[] deserializedStringArray = (String[]) deserializedObject;
        assertEquals("Hello World", deserializedStringArray[0]);
        assertEquals("[\"Hello World\"]", ts.serializeValueAsString(null));

        ObjectMapper objectMapper = new ObjectMapper();
        TypeSerializer ts2 = objectMapper.getTypeFactory()
                .constructTypeSerializer(String[].class);
        assertTrue(ts.isSameTypeSerializer(ts2));
    }

    @Test
    public void testStdTypeResolverBuilder_NoTypeInfo() throws Exception {
        StdTypeResolverBuilder builder = StdTypeResolverBuilder.noTypeInfoBuilder();
        ObjectMapper mapper = new ObjectMapper();
        MapperFactory mf = mapper.getFactory();
        TypeSerializer ts = builder.buildTypeSerializer(mf.getConfig(),
                JavaType.valueOf("java.lang.String"), null);
        TypeDeserializer td = builder.buildTypeDeserializer(mapper,
                JavaType.valueOf("java.lang.String"), null);
        assertNull(ts);
        assertNull(td);
    }
}