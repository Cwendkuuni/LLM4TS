package com.fasterxml.jackson.databind.deser.std;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertNull;

public class NullifyingDeserializerTest {

    private ObjectMapper objectMapper;

    @Before
    public void setup() {
        objectMapper = new ObjectMapper();
    }

    @Test
    public void testDeserializeWithEmptyJson() throws Exception {
        JsonParser jsonParser = objectMapper.getFactory().createParser("{}");
        NullifyingDeserializer nullifyingDeserializer = NullifyingDeserializer.instance;
        assertNull(nullifyingDeserializer.deserialize(jsonParser, null));
    }

    @Test
    public void testDeserializeWithNullInput() throws Exception {
        JsonParser jsonParser = objectMapper.getFactory().createParser(null);
        NullifyingDeserializer nullifyingDeserializer = NullifyingDeserializer.instance;
        assertNull(nullifyingDeserializer.deserialize(jsonParser, null));
    }

    @Test
    public void testDeserializeWithArrayToken() throws Exception {
        JsonParser jsonParser = objectMapper.getFactory().createParser("[{}]");
        NullifyingDeserializer nullifyingDeserializer = NullifyingDeserializer.instance;
        assertNull(nullifyingDeserializer.deserialize(jsonParser, null));
    }

    @Test
    public void testDeserializeWithObjectStartToken() throws Exception {
        JsonParser jsonParser = objectMapper.getFactory().createParser("{\"name\":\"John\",\"age\":30}");
        NullifyingDeserializer nullifyingDeserializer = NullifyingDeserializer.instance;
        assertNull(nullifyingDeserializer.deserialize(jsonParser, null));
    }

    @Test
    public void testDeserializeWithFieldNameToken() throws Exception {
        JsonParser jsonParser = objectMapper.getFactory().createParser("{\"name\":\"John\"}");
        NullifyingDeserializer nullifyingDeserializer = NullifyingDeserializer.instance;
        assertNull(nullifyingDeserializer.deserialize(jsonParser, null));
    }

    @Test
    public void testDeserializeWithValueTokens() throws Exception {
        JsonParser jsonParser = objectMapper.getFactory().createParser("\"Hello\"");
        NullifyingDeserializer nullifyingDeserializer = NullifyingDeserializer.instance;
        assertNull(nullifyingDeserializer.deserialize(jsonParser, null));

        jsonParser = objectMapper.getFactory().createParser("true");
        assertNull(nullifyingDeserializer.deserialize(jsonParser, null));

        jsonParser = objectMapper.getFactory().createParser("123");
        assertNull(nullifyingDeserializer.deserialize(jsonParser, null));

        jsonParser = objectMapper.getFactory().createParser("null");
        assertNull(nullifyingDeserializer.deserialize(jsonParser, null));
    }

    @Test
    public void testDeserializeWithCustomTypeDeserializer() throws Exception {
        JsonParser jsonParser = objectMapper.getFactory().createParser("{\"@class\":\"java.lang.String\",\"value\":\"Hello\"}");
        ObjectMapper objectMapper = new ObjectMapper();
        TypeDeserializer stringTypeDeserializer = objectMapper.getTypeFactory().constructValueTypeDeserializer(String.class, null);
        NullifyingDeserializer nullifyingDeserializer = NullifyingDeserializer.instance;
        assertNull(nullifyingDeserializer.deserializeWithType(jsonParser, null, stringTypeDeserializer));
    }

    @Test
    public void testDeserializeWithUnknownToken() throws Exception {
        JsonParser jsonParser = objectMapper.getFactory().createParser("X");
        NullifyingDeserializer nullifyingDeserializer = NullifyingDeserializer.instance;
        assertNull(nullifyingDeserializer.deserialize(jsonParser, null));
    }
}