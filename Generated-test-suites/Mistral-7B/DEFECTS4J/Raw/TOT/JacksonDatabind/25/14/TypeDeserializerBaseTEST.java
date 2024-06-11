package com.fasterxml.jackson.databind.jsontype.impl;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
import com.fasterxml.jackson.databind.util.ClassUtil;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Test case for {@link TypeDeserializerBase} class.
 */
public class TypeDeserializerBaseTest {

    private static final ObjectMapper objectMapper;

    static {
        objectMapper = new ObjectMapper();
        objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
    }

    private TypeDeserializerBase typeDeserializerBase;

    @Before
    public void setup() {
        typeDeserializerBase = new TypeDeserializerBase(
                JavaType.constructCollectionType(Map.class, String.class, Integer.class),
                new StubTypeIdResolver(),
                "type",
                true,
                Map.class
        );
    }

    @Test
    public void testSimpleDeserialization() throws IOException, JsonProcessingException {
        String json = "{\"type\":\"com.fasterxml.jackson.databind.jsontype.impl.MapType\"}";
        JsonParser jp = objectMapper.getFactory().createParser(json);
        DeserializationContext ctxt = objectMapper.getDeserializationContext();

        Object result = typeDeserializerBase._deserializeWithNativeTypeId(jp, ctxt);
        Assert.assertNotNull(result);
        Assert.assertTrue(result instanceof Map);
        Map<String, Integer> map = (Map<String, Integer>) result;
        Assert.assertEquals("empty map", map.size());
        Assert.assertNull(map.get("key"));
    }

    @Test
    public void testDeserializationWithDefaultImpl() throws IOException, JsonProcessingException {
        String json = "{\"type\":\"com.fasterxml.jackson.databind.JsonNode\"}";
        JsonParser jp = objectMapper.getFactory().createParser(json);
        DeserializationContext ctxt = objectMapper.getDeserializationContext();

        Object result = typeDeserializerBase._deserializeWithNativeTypeId(jp, ctxt);
        Assert.assertNotNull(result);
        Assert.assertTrue(result instanceof com.fasterxml.jackson.databind.JsonNode);
    }

    @Test(expected = JsonMappingException.class)
    public void testUnknownTypeId() throws IOException, JsonProcessingException {
        String json = "{\"type\":\"com.unknown.type\"}";
        JsonParser jp = objectMapper.getFactory().createParser(json);
        DeserializationContext ctxt = objectMapper.getDeserializationContext();

        typeDeserializerBase._deserializeWithNativeTypeId(jp, ctxt);
    }

    @Test(expected = JsonMappingException.class)
    public void testMissingTypeId() throws IOException, JsonProcessingException {
        String json = "{}";
        JsonParser jp = objectMapper.getFactory().createParser(json);
        DeserializationContext ctxt = objectMapper.getDeserializationContext();

        typeDeserializerBase._deserializeWithNativeTypeId(jp, ctxt);
    }

    private static class StubTypeIdResolver implements TypeIdResolver {

        @Override
        public JavaType typeFromId(DeserializationContext context, String id) {
            if ("MapType".equals(id)) {
                return JavaType.constructCollectionType(Map.class, String.class, Integer.class);
            }
            if ("JsonNode".equals(id)) {
                return ClassUtil.weaklyGetClass(com.fasterxml.jackson.databind.JsonNode.class);
            }
            return null;
        }

        @Override
        public String idFromValue(DeserializationContext context, JavaType type) {
            return null;
        }
    }
}