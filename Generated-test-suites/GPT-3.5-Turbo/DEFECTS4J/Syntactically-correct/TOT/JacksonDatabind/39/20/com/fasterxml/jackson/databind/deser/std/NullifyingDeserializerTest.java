package com.fasterxml.jackson.databind.deser.std;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.io.IOException;

import org.junit.Test;

import com.fasterxml.jackson.core.JsonNode;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class NullifyingDeserializerTest {

    private NullifyingDeserializer nullifyingDeserializer = NullifyingDeserializer.instance;
    private ObjectMapper objectMapper = new ObjectMapper();

    @Test
    public void testDeserialize_SkipsAllContent_ReturnsNull() throws IOException {
        JsonParser jsonParser = objectMapper.getFactory().createParser("{" +
                "  \"field1\": 1," +
                "  \"field2\": \"value\"," +
                "  \"field3\": true" +
                "}");

        assertNull(nullifyingDeserializer.deserialize(jsonParser, null));
    }

    @Test
    public void testDeserialize_BogusToken_ReturnsNull() throws IOException {
        JsonParser jsonParser = objectMapper.getFactory().createParser("bogus");

        assertNull(nullifyingDeserializer.deserialize(jsonParser, null));
    }

    @Test
    public void testDeserializeWithType_StartArray_CallsTypeDeserializer() throws IOException {
        JsonParser jsonParser = objectMapper.getFactory().createParser("[1,2,3]");

        assertNull(nullifyingDeserializer.deserializeWithType(jsonParser, null, null));
    }

    @Test
    public void testDeserializeWithType_StartObject_CallsTypeDeserializer() throws IOException {
        JsonParser jsonParser = objectMapper.getFactory().createParser("{\"field1\": 1}");

        assertNull(nullifyingDeserializer.deserializeWithType(jsonParser, null, null));
    }

    @Test
    public void testDeserializeWithType_FieldName_CallsTypeDeserializer() throws IOException {
        JsonParser jsonParser = objectMapper.getFactory().createParser("\"field1\"");

        assertNull(nullifyingDeserializer.deserializeWithType(jsonParser, null, null));
    }

}