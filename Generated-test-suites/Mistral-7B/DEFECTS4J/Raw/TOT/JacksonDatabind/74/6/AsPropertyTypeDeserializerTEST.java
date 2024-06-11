package com.fasterxml.jackson.databind.jsontype.impl;

import com.fasterxml.jackson.annotation.JsonTypeInfo.As;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
import com.fasterxml.jackson.databind.util.TokenBuffer;
import org.junit.Before;
import org.junit.Test;

import static com.fasterxml.jackson.core.util.TestUtils.convertObjectToText;
import static org.junit.Assert.*;

public class AsPropertyTypeDeserializerTest {

    private ObjectMapper mapper;
    private AsPropertyTypeDeserializer deserializer;

    @Before
    public void setup() throws Exception {
        mapper = new ObjectMapper();
        JavaType javaType = mapper.getTypeFactory().constructType(AsPropertyTypeDeserializer.class);
        deserializer = new AsPropertyTypeDeserializer(
                mapper.getTypeFactory().constructType(String.class),
                mapper.getTypeFactory(),
                "type",
                true,
                mapper.getTypeFactory().constructType(Integer.class),
                As.PROPERTY);
    }

    @Test
    public void testDeserializeNormalUseCase() throws Exception {
        String json = "{\"type\":\"String\",\"value\":\"Hello World\"}";
        Object result = deserializer.deserialize(mapper.getFactory().createParser(json), null);
        assertEquals("Hello World", result);
    }

    @Test
    public void testDeserializeWithNativeTypeId() throws Exception {
        String json = "{\"type\":\"String\",\"value\":\"Hello World\"}";
        Object result = deserializer.deserialize(mapper.getFactory().createParser(json), null);
        assertEquals("Hello World", result);

        String jsonWithNativeTypeId = "{\"@class\":\"com.fasterxml.jackson.databind.jsontype.impl.AsPropertyTypeDeserializer\",\"type\":\"String\",\"value\":\"Hello World\"}";
        Object resultWithNativeTypeId = deserializer.deserialize(mapper.getFactory().createParser(jsonWithNativeTypeId), null);
        assertEquals("Hello World", resultWithNativeTypeId);
        assertTrue("Should have used native type id deserialization", deserializer.getTypeIdVisible());
    }

    @Test
    public void testDeserializeWithMissingTypeProperty() throws Exception {
        String json = "{\"value\":\"Hello World\"}";
        try {
            deserializer.deserialize(mapper.getFactory().createParser(json), null);
            fail("Expected MissingPropertyException");
        } catch (MissingPropertyException e) {
            // Expected
        }
    }

    @Test
    public void testDeserializeWithExtraProperties() throws Exception {
        String json = "{\"type\":\"String\",\"value\":\"Hello World\",\"extra\":\"Property\"}";
        Object result = deserializer.deserialize(mapper.getFactory().createParser(json), null);
        assertEquals("Hello World", result);
    }

    @Test
    public void testDeserializeWithEmptyJson() throws Exception {
        String json = "{}";
        Object result = deserializer.deserialize(mapper.getFactory().createParser(json), null);
        assertNull(result);
    }

    @Test
    public void testDeserializeWithNullJson() throws Exception {
        String json = null;
        try {
            deserializer.deserialize(mapper.getFactory().createParser(json), null);
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            // Expected
        }
    }

    @Test
    public void testDeserializeWithUnexpectedToken() throws Exception {
        String json = "[1,2,3]";
        try {
            deserializer.deserialize(mapper.getFactory().createParser(json), null);
            fail("Expected JsonMappingException with message: Cannot deserialize value of type java.lang.String from Object value (no De
serializer found for this type. Type defined as: String)");
        } catch (JsonMappingException e) {
            // Expected
        }
    }
}