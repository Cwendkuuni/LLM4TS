package com.fasterxml.jackson.databind.jsontype.impl;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.introspect.AnnotationIntrospectorPair;
import com.fasterxml.jackson.databind.introspect.JacksonAnnotationIntrospector;
import com.fasterxml.jackson.databind.jsontype.As;
import com.fasterxml.jackson.databind.jsontype.TypeResolverBuilder;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.jsontype.impl.AsPropertyTypeDeserializer;
import com.fasterxml.jackson.databind.jsontype.impl.AsWrapperTypeDeserializer;
import com.fasterxml.jackson.databind.jsontype.impl.AsArrayTypeDeserializer;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class AsPropertyTypeDeserializerTest {

    private ObjectMapper mapper;
    private AsPropertyTypeDeserializer deserializer;

    @Before
    public void setUp() {
        mapper = new ObjectMapper();
        deserializer = new AsPropertyTypeDeserializer(null, null, null, true, null, As.PROPERTY);
    }

    @After
    public void tearDown() {
        mapper = null;
        deserializer = null;
    }

    @Test
    public void testDeserializeTypedFromObject() throws Exception {
        String json = "{\"property1\":\"value1\",\"property2\":2}";
        JsonFactory jsonFactory = new JsonFactory();
        JsonGenerator generator = jsonFactory.createGenerator(json);
        ObjectNode objectNode = mapper.readValue(generator, ObjectNode.class);

        Object result = deserializer.deserializeTypedFromObject(mapper.treeAsTokens(objectNode), null);

        Assert.assertNotNull(result);
        Assert.assertEquals(AsPropertyTypeDeserializer.class, result.getClass());
    }

    @Test
    public void testDeserializeTypedForId() throws Exception {
        String typeId = "testTypeId";
        String json = "{\"property1\":\"value1\",\"property2\":2}";
        JsonFactory jsonFactory = new JsonFactory();
        JsonGenerator generator = jsonFactory.createGenerator(json);
        ObjectNode objectNode = mapper.readValue(generator, ObjectNode.class);

        Object result = deserializer.deserializeTypedForId(mapper.treeAsTokens(objectNode), null, typeId);

        Assert.assertNotNull(result);
        Assert.assertEquals(AsPropertyTypeDeserializer.class, result.getClass());
    }

    @Test
    public void testDeserializeTypedUsingDefaultImpl() throws Exception {
        String json = "5";
        JsonFactory jsonFactory = new JsonFactory();
        JsonGenerator generator = jsonFactory.createGenerator(json);
        ObjectNode objectNode = mapper.readValue(generator, ObjectNode.class);

        Object result = deserializer.deserializeTypedUsingDefaultImpl(mapper.treeAsTokens(objectNode), null, null);

        Assert.assertNotNull(result);
        Assert.assertEquals(AsPropertyTypeDeserializer.class, result.getClass());
    }

    @Test
    public void testDeserializeTypedFromAny() throws Exception {
        String json = "{\"property1\":\"value1\",\"property2\":2}";
        JsonFactory jsonFactory = new JsonFactory();
        JsonGenerator generator = jsonFactory.createGenerator(json);
        ObjectNode objectNode = mapper.readValue(generator, ObjectNode.class);

        Object result = deserializer.deserializeTypedFromAny(mapper.treeAsTokens(objectNode), null);

        Assert.assertNotNull(result);
        Assert.assertEquals(AsPropertyTypeDeserializer.class, result.getClass());
    }

    @Test
    public void testDeserializeTypedFromArray() throws Exception {
        String json = "[{\"property1\":\"value1\",\"property2\":2}]";
        JsonFactory jsonFactory = new JsonFactory();
        JsonGenerator generator = jsonFactory.createGenerator(json);
        ObjectNode objectNode = mapper.readValue(generator, ObjectNode.class);

        Object result = deserializer.deserializeTypedFromArray(mapper.treeAsTokens(objectNode), null);

        Assert.assertNotNull(result);
        Assert.assertEquals(AsPropertyTypeDeserializer.class, result.getClass());
    }
}