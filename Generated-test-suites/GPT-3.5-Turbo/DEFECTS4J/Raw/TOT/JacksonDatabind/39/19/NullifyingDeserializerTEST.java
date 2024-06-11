package com.fasterxml.jackson.databind.deser.std;

import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.Test;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;

public class NullifyingDeserializerTest {

    @Test
    public void testDeserialize() throws IOException {
        JsonFactory factory = new JsonFactory();
        String json = "{\"name\":\"John\", \"age\":30}";
        JsonParser parser = factory.createParser(json);

        assertNull(NullifyingDeserializer.instance.deserialize(parser, mock(DeserializationContext.class)));

        parser.close();
    }

    @Test
    public void testDeserializeWithType() throws IOException {
        JsonFactory factory = new JsonFactory();
        String json = "{\"name\":\"John\", \"age\":30}";
        JsonParser parser = factory.createParser(json);

        assertNull(NullifyingDeserializer.instance.deserializeWithType(parser, mock(DeserializationContext.class), mock(TypeDeserializer.class)));

        parser.close();
    }

    @Test
    public void testDeserializeWithType_StartArray() throws IOException {
        JsonFactory factory = new JsonFactory();
        String json = "[\"John\", \"Doe\"]";
        JsonParser parser = factory.createParser(json);

        assertNull(NullifyingDeserializer.instance.deserializeWithType(parser, mock(DeserializationContext.class), mock(TypeDeserializer.class)));

        parser.close();
    }

    @Test
    public void testDeserializeWithType_StartObject() throws IOException {
        JsonFactory factory = new JsonFactory();
        String json = "{\"name\":\"John\", \"age\":30}";
        JsonParser parser = factory.createParser(json);

        assertNull(NullifyingDeserializer.instance.deserializeWithType(parser, mock(DeserializationContext.class), mock(TypeDeserializer.class)));

        parser.close();
    }

    @Test
    public void testDeserializeWithType_FieldName() throws IOException {
        JsonFactory factory = new JsonFactory();
        String json = "{\"name\":\"John\", \"age\":30}";
        JsonParser parser = factory.createParser(json);

        assertNull(NullifyingDeserializer.instance.deserializeWithType(parser, mock(DeserializationContext.class), mock(TypeDeserializer.class)));

        parser.close();
    }

    @Test
    public void testDeserializeWithType_InvalidToken() throws IOException {
        JsonFactory factory = new JsonFactory();
        String json = "123";
        JsonParser parser = factory.createParser(json);

        assertNull(NullifyingDeserializer.instance.deserializeWithType(parser, mock(DeserializationContext.class), mock(TypeDeserializer.class)));

        parser.close();
    }
}