package com.fasterxml.jackson.databind.test;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonMappingException.Reference;
import com.fasterxml.jackson.databind.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
import com.fasterxml.jackson.databind.ser.BeanPropertyWriter;
import com.fasterxml.jackson.databind.ser.BeanSerializerFactory;
import com.fasterxml.jackson.databind.ser.BeanSerializerModifier;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.JsonToken;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class JsonMappingExceptionTest {

    private ObjectMapper objectMapper;

    @BeforeClass
    public void setup() {
        ObjectMapper.getDefaultInstance().configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        objectMapper = new ObjectMapper();
    }

    @Test
    public void testNormalSerialization() throws JsonProcessingException {
        JsonMappingException expectedException = JsonMappingException.from(JsonProcessingException.fromObject(new Object()));
        String json = objectMapper.writeValueAsString(expectedException);
        Assert.assertNull(JsonMappingException.fromString(json));
    }

    @Test
    public void testNormalDeserialization() throws IOException {
        String json = "{\"message\":\"Unexpected IOException (of type java.lang.NullPointerException): null\",\"path\":[],\"rootCause\":\"null\"}";
        JsonMappingException expectedException = new JsonMappingException(null, "Unexpected IOException (of type java.lang.NullPointerException): null");
        JsonMappingException actualException = JsonMappingException.fromString(json);
        Assert.assertEquals(actualException, expectedException);
    }

    @Test
    public void testSerializationWithPath() throws JsonProcessingException {
        JsonMappingException expectedException = JsonMappingException.wrapWithPath(new IOException("test"), new Object(), "testField");
        String json = objectMapper.writeValueAsString(expectedException);
        JsonMappingException actualException = JsonMappingException.fromString(json);

        List<Reference> expectedPath = Arrays.asList(
                new Reference(new Object(), "testField")
        );
        List<Reference> actualPath = actualException.getPath();
        Assert.assertEquals(actualPath, expectedPath);
    }

    @Test
    public void testDeserializationWithPath() throws IOException {
        String json = "{\"message\":\"Unexpected IOException (of type java.lang.NullPointerException): null\",\"path\":[{\"from\":null,\"fieldName\":\"testField\",\"index\":-1}],\"rootCause\":\"null\"}";
        JsonMappingException expectedException = JsonMappingException.wrapWithPath(new IOException("test"), new Object(), "testField");
        JsonMappingException actualException = JsonMappingException.fromString(json);

        List<Reference> expectedPath = Arrays.asList(
                new Reference(new Object(), "testField")
        );
        List<Reference> actualPath = actualException.getPath();
        Assert.assertEquals(actualPath, expectedPath);
    }

    @Test
    public void testSerializationWithComplexPath() throws JsonProcessingException {
        // Create a map with a nested map
        Map<String, Map<String, String>> nestedMap = new LinkedHashMap<>();
        Map<String, String> innerMap = new HashMap<>();
        innerMap.put("test", "test");
        nestedMap.put("test", innerMap);

        // Create a serializer that throws a JsonMappingException
        JsonMappingException expectedException = JsonMappingException.wrapWithPath(new IOException("test"), nestedMap, "test.test");
        StdSerializer<Map<String, String>> mapSerializer = new StdSerializer<Map<String, String>>(Map.class) {
            @Override
            public void serializeFields(Map<String, String> map, BeanPropertyWriter[] props, BeanSerializerFactory serializers, JsonSerializer serializers, String _localName, BeanSerializerModifier modifier) throws IOException {
                throw expectedException;
            }
        };
        BeanSerializerModifier modifier = new BeanSerializerModifier() {
            @Override
            public BeanPropertyWriter findBeanPropertyWriter(BeanPropertyWriter propertyWriter, BeanSerializerFactory serializers, JsonSerializer serializers, Object value, JsonSerializerProvider provider) throws IOException {
                if (value instanceof Map) {
                    return mapSerializer;
                }
                return super.findBeanPropertyWriter(propertyWriter, serializers, serializers, value, provider);
            }
        };

        // Serialize the map with the custom serializer
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.getSerializerProvider().setBeanSerializerModifier(modifier);
        String json = objectMapper.writeValueAsString(nestedMap);
        JsonMappingException actualException = JsonMappingException.fromString(json);

        List<Reference> expectedPath = Arrays.asList(
                new Reference(nestedMap, "test.test")
        );
        List<Reference> actualPath = actualException.getPath();
        Assert.assertEquals(actualPath, expectedPath);
    }

    @Test
    public void testDeserializationWithComplexPath() throws IOException {
        // Create a JSON string with a complex path
        String json = "{\"message\":\"Unexpected IOException (of type java.lang.NullPointerException): null\",\"path\":[{\"from\":{},\"fieldName\":\"test.test\",\"index\":-1}],\"rootCause\":\"null\"}";

        // Create a deserializer that throws a JsonMappingException
        ObjectMapper objectMapper = new ObjectMapper();
        JsonSerializer<Map<String, String>> mapDeserializer = new JsonDeserializer<Map<String, String>>() {
            @Override
            public Map<String, String> deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
                throw new JsonMappingException(ctxt, "testMessage", null, null, null, new Reference(p, "test.test"));
            }
        };
        BeanSerializerModifier modifier = new BeanSerializerModifier() {
            @Override
            public BeanPropertyWriter findBeanPropertyWriter(BeanPropertyWriter propertyWriter, BeanSerializerFactory serializers, JsonSerializer serializers, Object value, JsonSerializerProvider provider) throws IOException {
                if (value instanceof Map) {
                    BeanPropertyWriter mapWriter = propertyWriter;
                    BeanSerializerFactory mapSerializers = serializers;
                    JsonSerializer mapSerializer = serializers;
                    return mapDeserializer.handleNullValue(null, propertyWriter, mapSerializers, mapSerializer, null, ctxt.getInputFactory(), JsonToken.START_OBJECT);
                }
                return super.findBeanPropertyWriter(propertyWriter, serializers, serializers, value, provider);
            }
        };

        // Deserialize the JSON string with the custom deserializer
        JsonMappingException expectedException = new JsonMappingException(null, "testMessage", null, null, null, new Reference(null, "test.test"));
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.getDeserializationConfig().setRootValueFactory(DeserializationConfig.FieldNameBasedRootValueFactory.class);
        objectMapper.getSerializerProvider().setBeanSerializerModifier(modifier);
        Map<String, String> actualValue = objectMapper.readValue(json, Map.class);
        JsonMappingException actualException = ((JsonMappingException) actualValue.getClass().cast(actualValue).getValue());

        Assert.assertEquals(actualException, expectedException);
    }
}