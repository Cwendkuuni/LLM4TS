package com.fasterxml.jackson.databind.deser.std;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import org.junit.*;
import static org.junit.Assert.*;

public class NullifyingDeserializerTest {
    private static ObjectMapper objectMapper;
    private static JsonParser jsonParser;

    @BeforeClass
    public static void setup() {
        objectMapper = new ObjectMapper();
        jsonParser = objectMapper.getFactory().createParser("{}");
    }

    @Test
    public void testDeserialize() throws JsonProcessingException {
        assertNull(NullifyingDeserializer.instance.deserialize(jsonParser, new DeserializationContext(new AnnotationIntrospector())));
    }

    @Test
    public void testDeserializeWithType() throws JsonProcessingException {
        String jsonString = "[]";
        JsonParser jsonParser = objectMapper.getFactory().createParser(jsonString);

        TypeDeserializer typeDeserializer = objectMapper.getTypeFactory().constructValueType(Object[].class);

        assertNull(NullifyingDeserializer.instance.deserializeWithType(jsonParser, new DeserializationContext(new AnnotationIntrospector()), typeDeserializer));
    }

    @Test
    public void testDeserializeWithUnexpectedToken() throws JsonProcessingException {
        String jsonString = "5";
        JsonParser jsonParser = objectMapper.getFactory().createParser(jsonString);

        assertNull(NullifyingDeserializer.instance.deserializeWithType(jsonParser, new DeserializationContext(new AnnotationIntrospector()), typeDeserializer));
    }

    @Test
    public void testDeserializeWithNullJsonParser() throws JsonProcessingException {
        assertNull(NullifyingDeserializer.instance.deserialize(null, new DeserializationContext(new AnnotationIntrospector())));
    }

    @Test
    public void testDeserializeWithUnexpectedJsonToken() throws JsonProcessingException {
        JsonParser jsonParser = objectMapper.getFactory().createParser("\"name\":\"John\"");

        assertNull(NullifyingDeserializer.instance.deserializeWithType(jsonParser, new DeserializationContext(new AnnotationIntrospector()), typeDeserializer));
    }

    @AfterClass
    public static void tearDown() {
        jsonParser.close();
        objectMapper = null;
    }
}