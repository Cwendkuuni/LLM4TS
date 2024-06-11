package com.fasterxml.jackson.databind.jsontype.impl;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.Map;

import static com.fasterxml.jackson.databind.DeserializationFeature.FAIL_ON_EMPTY_BEANS;
import static com.fasterxml.jackson.databind.DeserializationFeature.FAIL_ON_INVALID_SUBTYPE;

public class TypeDeserializerBaseTest {

    private ObjectMapper mapper;

    @Before
    public void setup() {
        mapper = new ObjectMapper();
        mapper.findAndRegisterModules();
        mapper.configure(FAIL_ON_INVALID_SUBTYPE, false);
        mapper.configure(FAIL_ON_EMPTY_BEANS, false);
    }

    @Test
    public void testTypeDeserializerBase() throws IOException {
        String json = "{\"name\":\"John\",\"type\":\"Person\"}";
        TypeIdResolver idResolver = mapper.getTypeIdResolver();
        JavaType javaType = mapper.getTypeFactory().constructType(TypeDeserializerBase.class);
        TypeDeserializerBase typeDeserializer = mapper.readValue(json, javaType);
        Assert.assertNotNull(typeDeserializer);
        Assert.assertEquals("Person", typeDeserializer.baseTypeName());
        Assert.assertNotNull(typeDeserializer.getTypeIdResolver());

        JsonParser jp = mapper.getFactory().createParser(json);
        Object result = typeDeserializer._deserializeWithNativeTypeId(jp, mapper.getDeserializationContext());
        Assert.assertNotNull(result);
        Assert.assertEquals("John", result.getClass().getDeclaredField("name").get(result));

        // Test unknown type id
        json = "{\"name\":\"John\",\"type\":\"UnknownType\"}";
        jp = mapper.getFactory().createParser(json);
        try {
            typeDeserializer._deserializeWithNativeTypeId(jp, mapper.getDeserializationContext());
            Assert.fail();
        } catch (JsonMappingException e) {
            Assert.assertEquals("Unknown type id 'UnknownType' for type id 'UnknownType', " +
                    "no contextual handler found of type com.fasterxml.jackson.databind.JsonDeserializer", e.getMessage());
        }

        // Test missing type id
        json = "{\"name\":\"John\"}";
        jp = mapper.getFactory().createParser(json);
        try {
            typeDeserializer._deserializeWithNativeTypeId(jp, mapper.getDeserializationContext());
            Assert.fail();
        } catch (JsonMappingException e) {
            Assert.assertEquals("No (native) type id found when one was expected for polymorphic type handling", e.getMessage());
        }
    }
}