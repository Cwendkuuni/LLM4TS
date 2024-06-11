package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.cfg.DeserializerFactoryConfig;
import com.fasterxml.jackson.databind.deser.BeanDeserializerFactory;
import com.fasterxml.jackson.databind.deser.Deserializers;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.introspect.Annotated;
import com.fasterxml.jackson.databind.introspect.BeanPropertyDefinition;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
import com.fasterxml.jackson.databind.jsontype.TypeResolverBuilder;
import com.fasterxml.jackson.databind.jsontype.impl.AsAnyTypeDeserializer;
import com.fasterxml.jackson.databind.jsontype.impl.AsArrayTypeDeserializer;
import com.fasterxml.jackson.databind.jsontype.impl.AsExternalTypeDeserializer;
import com.fasterxml.jackson.databind.jsontype.impl.AsPropertyTypeDeserializer;
import com.fasterxml.jackson.databind.jsontype.impl.AsWrapperTypeDeserializer;
import com.fasterxml.jackson.databind.jsontype.impl.LaissezFaireTokenResolver;
import com.fasterxml.jackson.databind.jsontype.impl.StdTypeResolverBuilder;
import com.fasterxml.jackson.databind.jsontype.impl.TypeDeserializerObjectIdResolver;
import com.fasterxml.jackson.databind.jsontype.impl.TypeIdResolverBase;
import com.fasterxml.jackson.databind.module.SimpleDeserializers;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.testutil.NoCheckSubTypeValidator;
import com.fasterxml.jackson.databind.type.SimpleType;
import com.fasterxml.jackson.databind.util.ClassUtil;
import com.fasterxml.jackson.databind.util.TokenBuffer;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.util.EnumMap;
import java.util.*;

public class AsPropertyTypeDeserializerTest {

    //Initial test cases
    @Test
    public void testDeserializeTypedFromObject() throws IOException {
        AsPropertyTypeDeserializer asPropertyTypeDeserializer = new AsPropertyTypeDeserializer(SimpleType.construct(Object.class), null, "type", true, null);

        JsonParser parser = new JsonFactory().createParser("{\"type\":\"Test\",\"data\":{\"test\":\"test\"}}");

        Object result = asPropertyTypeDeserializer.deserializeTypedFromObject(parser, null);

        assertEquals(LinkedHashMap.class, result.getClass());
    }

    @Test
    public void testDeserializeTypedForId() throws IOException {
        AsPropertyTypeDeserializer asPropertyTypeDeserializer = new AsPropertyTypeDeserializer(SimpleType.construct(Object.class), null, "type", true, null);

        JsonParser parser = new JsonFactory().createParser("{\"type\":\"Test\",\"data\":{\"test\":\"test\"}}");

        Object result = asPropertyTypeDeserializer.deserializeTypedForId(parser, null, null);

        assertEquals(String.class, result.getClass());
    }

    @Test
    public void testDeserializeTypedUsingDefaultImpl() throws IOException {
        AsPropertyTypeDeserializer asPropertyTypeDeserializer = new AsPropertyTypeDeserializer(SimpleType.construct(Object.class), null, "type", true, null);

        JsonParser parser = new JsonFactory().createParser("{\"type\":\"Test\",\"data\":{\"test\":\"test\"}}");

        Object result = asPropertyTypeDeserializer.deserializeTypedUsingDefaultImpl(parser, null, null);

        assertEquals(Object.class, result.getClass());
    }


    //Additional test cases
    @Test
    public void testDeserializeTypedFromObjectWithNullTokenBuffer() throws IOException {
        AsPropertyTypeDeserializer asPropertyTypeDeserializer = new AsPropertyTypeDeserializer(SimpleType.construct(Object.class), null, "type", true, null);

        JsonParser parser = new JsonFactory().createParser("{\"type\":\"Test\",\"data\":{\"test\":\"test\"}}");
      
        parser.nextToken();

        Object result = asPropertyTypeDeserializer.deserializeTypedFromObject(parser, null);

        assertEquals(String.class, result.getClass());
    }

    @Test
    public void testDeserializeTypedForIdWithNullTokenBuffer() throws IOException {
        AsPropertyTypeDeserializer asPropertyTypeDeserializer = new AsPropertyTypeDeserializer(SimpleType.construct(Object.class), null, "type", true, null);

        JsonParser parser = new JsonFactory().createParser("{\"type\":\"Test\",\"data\":{\"test\":\"test\"}}");

        parser.nextToken();

        Object result = asPropertyTypeDeserializer.deserializeTypedForId(parser, null, null);

        assertEquals(String.class, result.getClass());
    }

    @Test
    public void testDeserializeTypedUsingDefaultImplWithNullTokenBuffer() throws IOException {
        AsPropertyTypeDeserializer asPropertyTypeDeserializer = new AsPropertyTypeDeserializer(SimpleType.construct(Object.class), null, "type", true, null);

        JsonParser parser = new JsonFactory().createParser("{\"type\":\"Test\",\"data\":{\"test\":\"test\"}}");

        parser.nextToken();

        Object result = asPropertyTypeDeserializer.deserializeTypedUsingDefaultImpl(parser, null, null);

        assertEquals(Object.class, result.getClass());
    }

}