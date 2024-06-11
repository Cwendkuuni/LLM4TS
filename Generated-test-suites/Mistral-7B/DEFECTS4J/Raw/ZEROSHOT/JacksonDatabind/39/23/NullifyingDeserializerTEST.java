package com.fasterxml.jackson.databind.deser.std;

import static org.junit.Assert.assertNull;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.type.TypeFactory;

import org.junit.Before;
import org.junit.Test;

/**
 * Test class for NullifyingDeserializer.
 */
public class NullifyingDeserializerTest {

    private ObjectMapper objectMapper;

    @Before
    public void setup() {
        objectMapper = new ObjectMapper();
    }

    /**
     * Test deserialize method with empty JsonParser.
     * @throws JsonProcessingException
     */
    @Test
    public void testEmptyParserDeserialize() throws JsonProcessingException {
        JsonParser jsonParser = objectMapper.getFactory().createParser("{}");
        JsonDeserializer<Object> deserializer = NullifyingDeserializer.instance;
        Object result = deserializer.deserialize(jsonParser, new DeserializationContext(objectMapper, null, null));
        assertNull(result);
    }

    /**
     * Test deserialize method with JsonParser containing array.
     * @throws JsonProcessingException
     */
    @Test
    public void testArrayParserDeserialize() throws JsonProcessingException {
        JsonParser jsonParser = objectMapper.getFactory().createParser("[1, 2, 3]");
        JsonDeserializer<Object> deserializer = NullifyingDeserializer.instance;
        Object result = deserializer.deserialize(jsonParser, new DeserializationContext(objectMapper, null, null));
        assertNull(result);
    }

    /**
     * Test deserialize method with JsonParser containing object.
     * @throws JsonProcessingException
     */
    @Test
    public void testObjectParserDeserialize() throws JsonProcessingException {
        JsonParser jsonParser = objectMapper.getFactory().createParser("{\"key\": \"value\"}");
        JsonDeserializer<Object> deserializer = NullifyingDeserializer.instance;
        Object result = deserializer.deserialize(jsonParser, new DeserializationContext(objectMapper, null, null));
        assertNull(result);
    }

    /**
     * Test deserializeWithType method with JsonParser containing array.
     * @throws JsonProcessingException
     */
    @Test
    public void testArrayParserDeserializeWithType() throws JsonProcessingException {
        JsonParser jsonParser = objectMapper.getFactory().createParser("[\"one\",\"two\",\"three\"]");
        TypeDeserializer typeDeserializer = objectMapper.getTypeFactory().constructValueTypeDeserializer(String[].class);
        JsonDeserializer<Object> deserializer = NullifyingDeserializer.instance;
        Object result = deserializer.deserializeWithType(jsonParser, new DeserializationContext(objectMapper, null, typeDeserializer), typeDeserializer);
        assertNull(result);
    }

    /**
     * Test deserializeWithType method with JsonParser containing object.
     * @throws JsonProcessingException
     */
    @Test
    public void testObjectParserDeserializeWithType() throws JsonProcessingException {
        JsonParser jsonParser = objectMapper.getFactory().createParser("{\"name\":\"John\",\"age\":30}");
        TypeFactory typeFactory = objectMapper.getTypeFactory();
        Object result = deserializer.deserializeWithType(jsonParser, new DeserializationContext(objectMapper, null, null), typeFactory.constructType(YourInnerClass.class));
        assertNull(result);
    }

    /**
     * Test deserializeWithType method with JsonParser containing invalid value.
     * @throws JsonProcessingException
     */
    @Test(expected = JsonProcessingException.class)
    public void testInvalidParserDeserializeWithType() throws JsonProcessingException {
        JsonParser jsonParser = objectMapper.getFactory().createParser("invalidJSON");
        TypeDeserializer typeDeserializer = objectMapper.getTypeFactory().constructValueTypeDeserializer(String[].class);
        JsonDeserializer<Object> deserializer = NullifyingDeserializer.instance;
        deserializer.deserializeWithType(jsonParser, new DeserializationContext(objectMapper, null, typeDeserializer), typeDeserializer);
    }

    // Replace YourInnerClass with the actual class you want to test deserialization with NullifyingDeserializer
}