package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.*;

import com.fasterxml.jackson.annotation.JsonTypeInfo.As;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
import com.fasterxml.jackson.databind.util.TokenBuffer;
import org.junit.Test;

public class AsPropertyTypeDeserializerTest {

    @Test
    public void testDeserializeTypedFromObject() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        AsPropertyTypeDeserializer deserializer = new AsPropertyTypeDeserializer(
                mapper.getTypeFactory().constructType(String.class),
                mapper.getTypeIdResolver(),
                "typePropertyName",
                true,
                mapper.getTypeFactory().constructType(Integer.class),
                As.PROPERTY);

        String json = "{\"typePropertyName\":\"StringType\"}";
        JsonParser parser = mapper.getFactory().createParser(json);

        Object result = deserializer.deserializeTypedFromObject(parser, mapper.getDeserializationContext());
        assertEquals("StringType", result);
    }

    @Test
    public void testDeserializeTypedForId() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        AsPropertyTypeDeserializer deserializer = new AsPropertyTypeDeserializer(
                mapper.getTypeFactory().constructType(String.class),
                mapper.getTypeIdResolver(),
                "typePropertyName",
                true,
                mapper.getTypeFactory().constructType(Integer.class),
                As.PROPERTY);

        String json = "{\"typePropertyName\":{\"@type\":\"StringType\"}}";
        JsonParser parser = mapper.getFactory().createParser(json);

        Object result = deserializer.deserializeTypedForId(parser, mapper.getDeserializationContext(), null);
        assertEquals("StringType", result);
    }

    @Test
    public void testDeserializeTypedUsingDefaultImpl() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        AsPropertyTypeDeserializer deserializer = new AsPropertyTypeDeserializer(
                mapper.getTypeFactory().constructType(String.class),
                mapper.getTypeIdResolver(),
                "typePropertyName",
                true,
                mapper.getTypeFactory().constructType(Integer.class),
                As.PROPERTY);

        String json = "{\"typePropertyName\":42}";
        JsonParser parser = mapper.getFactory().createParser(json);

        Object result = deserializer.deserializeTypedUsingDefaultImpl(parser, mapper.getDeserializationContext(), null);
        assertEquals(42, result);
    }

    @Test
    public void testDeserializeTypedUsingDefaultImplWithTokenBuffer() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        AsPropertyTypeDeserializer deserializer = new AsPropertyTypeDeserializer(
                mapper.getTypeFactory().constructType(String.class),
                mapper.getTypeIdResolver(),
                "typePropertyName",
                true,
                mapper.getTypeFactory().constructType(Integer.class),
                As.PROPERTY);

        String json = "{\"typePropertyName\":42}";
        JsonParser parser = mapper.getFactory().createParser(json);
        TokenBuffer tokenBuffer = new TokenBuffer(parser, mapper.getDeserializationContext());

        Object result = deserializer.deserializeTypedUsingDefaultImpl(parser, mapper.getDeserializationContext(), tokenBuffer);
        assertEquals(42, result);
    }

    @Test
    public void testDeserializeTypedFromArray() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        AsPropertyTypeDeserializer deserializer = new AsPropertyTypeDeserializer(
                mapper.getTypeFactory().constructType(String[].class),
                mapper.getTypeIdResolver(),
                "typePropertyName",
                true,
                mapper.getTypeFactory().constructType(Integer[].class),
                As.PROPERTY);

        String json = "[{\"typePropertyName\":[1,2,3]}]";
        JsonParser parser = mapper.getFactory().createParser(json);

        Object result = deserializer.deserializeTypedFromArray(parser, mapper.getDeserializationContext());
        TypeReference<Integer[]> typeReference = new TypeReference<Integer[]>() {};
        assertEquals(typeReference.getType(), result.getClass());
        assertArrayEquals(new Integer[]{1, 2, 3}, (Integer[]) result);
    }

    @Test
    public void testDeserializeTypedFromAny() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        AsPropertyTypeDeserializer deserializer = new AsPropertyTypeDeserializer(
                mapper.getTypeFactory().constructType(String.class),
                mapper.getTypeIdResolver(),
                "typePropertyName",
                true,
                mapper.getTypeFactory().constructType(Integer.class),
                As.PROPERTY);

        String json = "{\"typePropertyName\":42}";
        JsonParser parser = mapper.getFactory().createParser(json);

        Object result = deserializer.deserializeTypedFromAny(parser, mapper.getDeserializationContext());
        assertEquals(42, result);
    }
}