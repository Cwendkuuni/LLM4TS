package com.fasterxml.jackson.databind.jsontype.impl;

import static com.fasterxml.jackson.databind.JsonTest.MAPPER;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import com.fasterxml.jackson.annotation.JsonTypeInfo.As;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;

public class AsWrapperTypeDeserializerTest {

    private static final ObjectMapper MAPPER_WRAPPER = new ObjectMapper();

    @Test
    public void testDeserializeTypedFromObject() throws Exception {
        AsWrapperTypeDeserializer asWrapperTypeDeserializer = new AsWrapperTypeDeserializer(
                MAPPER_WRAPPER.getTypeFactory().constructType(String.class),
                MAPPER_WRAPPER.getTypeIdResolver(),
                "type",
                true,
                String.class
        );
        JsonParser jsonParser = MAPPER_WRAPPER.getFactory().createParser("{\"type\":\"java.lang.String\",\"value\":\"TestString\"}");
        assertEquals("TestString", asWrapperTypeDeserializer.deserializeTypedFromObject(jsonParser, new DeserializationContext(MAPPER_WRAPPER, null, null, null, null)));
    }

    @Test
    public void testDeserializeTypedFromArray() throws Exception {
        AsWrapperTypeDeserializer asWrapperTypeDeserializer = new AsWrapperTypeDeserializer(
                MAPPER_WRAPPER.getTypeFactory().constructType(int[].class),
                MAPPER_WRAPPER.getTypeIdResolver(),
                "type",
                true,
                int[].class
        );
        JsonParser jsonParser = MAPPER_WRAPPER.getFactory().createParser("[[\"type\",\"java.lang.Integer\"],[1]]");
        assertNotNull(asWrapperTypeDeserializer.deserializeTypedFromArray(jsonParser, new DeserializationContext(MAPPER_WRAPPER, null, null, null, null)));
    }

    @Test
    public void testDeserializeTypedFromScalar() throws Exception {
        AsWrapperTypeDeserializer asWrapperTypeDeserializer = new AsWrapperTypeDeserializer(
                MAPPER_WRAPPER.getTypeFactory().constructType(double.class),
                MAPPER_WRAPPER.getTypeIdResolver(),
                "type",
                true,
                double.class
        );
        JsonParser jsonParser = MAPPER_WRAPPER.getFactory().createParser("{3.14}");
        assertEquals(3.14, asWrapperTypeDeserializer.deserializeTypedFromScalar(jsonParser, new DeserializationContext(MAPPER_WRAPPER, null, null, null, null)));
    }

    @Test
    public void testDeserializeTypedFromAny() throws Exception {
        AsWrapperTypeDeserializer asWrapperTypeDeserializer = new AsWrapperTypeDeserializer(
                MAPPER_WRAPPER.getTypeFactory().constructType(Boolean.class),
                MAPPER_WRAPPER.getTypeIdResolver(),
                "type",
                true,
                Boolean.class
        );
        JsonParser jsonParser = MAPPER_WRAPPER.getFactory().createParser("{true}");
        assertEquals(true, asWrapperTypeDeserializer.deserializeTypedFromAny(jsonParser, new DeserializationContext(MAPPER_WRAPPER, null, null, null, null)));
    }

    @Test(expected = JsonMappingException.class)
    public void testInvalidJsonObjectFormat() throws Exception {
        AsWrapperTypeDeserializer asWrapperTypeDeserializer = new AsWrapperTypeDeserializer(
                MAPPER_WRAPPER.getTypeFactory().constructType(String.class),
                MAPPER_WRAPPER.getTypeIdResolver(),
                "type",
                true,
                String.class
        );
        JsonParser jsonParser = MAPPER_WRAPPER.getFactory().createParser("{value:\"TestString\"}");
        asWrapperTypeDeserializer.deserializeTypedFromObject(jsonParser, new DeserializationContext(MAPPER_WRAPPER, null, null, null, null));
    }

    @Test(expected = JsonMappingException.class)
    public void testInvalidJsonFieldNameFormat() throws Exception {
        AsWrapperTypeDeserializer asWrapperTypeDeserializer = new AsWrapperTypeDeserializer(
                MAPPER_WRAPPER.getTypeFactory().constructType(String.class),
                MAPPER_WRAPPER.getTypeIdResolver(),
                "type",
                true,
                String.class
        );
        JsonParser jsonParser = MAPPER_WRAPPER.getFactory().createParser("{\"InvalidType\":\"TestString\"}");
        asWrapperTypeDeserializer.deserializeTypedFromObject(jsonParser, new DeserializationContext(MAPPER_WRAPPER, null, null, null, null));
    }

    @Test(expected = JsonMappingException.class)
    public void testMissingTypeId() throws Exception {
        AsWrapperTypeDeserializer asWrapperTypeDeserializer = new AsWrapperTypeDeserializer(
                MAPPER_WRAPPER.getTypeFactory().constructType(String.class),
                MAPPER_WRAPPER.getTypeIdResolver(),
                "type",
                true,
                String.class
        );
        JsonParser jsonParser = MAPPER_WRAPPER.getFactory().createParser("{value:\"TestString\"}");
        asWrapperTypeDeserializer.deserializeTypedFromObject(jsonParser, new DeserializationContext(MAPPER_WRAPPER, null, null, null, null));
    }

    @Test(expected = JsonMappingException.class)
    public void testInvalidTypeId() throws Exception {
        AsWrapperTypeDeserializer asWrapperTypeDeserializer = new AsWrapperTypeDeserializer(
                MAPPER_WRAPPER.getTypeFactory().constructType(String.class),
                MAPPER_WRAPPER.getTypeIdResolver(),
                "type",
                true,
                String.class
        );
        JsonParser jsonParser = MAPPER_WRAPPER.getFactory().createParser("{\"type\":\"invalidTypeId\",\"value\":\"TestString\"}");
        asWrapperTypeDeserializer.deserializeTypedFromObject(jsonParser, new DeserializationContext(MAPPER_WRAPPER, null, null, null, null));
    }
}