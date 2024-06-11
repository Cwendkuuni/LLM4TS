package com.fasterxml.jackson.databind.deser.std;

import static com.fasterxml.jackson.core.JsonToken.*;
import static org.junit.Assert.assertNull;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import org.junit.Test;

public class NullifyingDeserializerTest {

    @Test
    public void testDeserializeNullifyingDeserializer() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonParser parser = mapper.getFactory().createParser("[]");
        NullifyingDeserializer deserializer = NullifyingDeserializer.instance;

        assertNull(deserializer.deserialize(parser, new DeserializationContext()));
        parser.nextToken(); // consume [
        parser.nextToken(); // consume ]
    }

    @Test
    public void testDeserializeNullifyingDeserializerObject() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonParser parser = mapper.getFactory().createParser("{}");
        NullifyingDeserializer deserializer = NullifyingDeserializer.instance;

        assertNull(deserializer.deserialize(parser, new DeserializationContext()));
        parser.nextToken(); // consume {
        parser.nextToken(); // consume }
    }

    @Test
    public void testDeserializeNullifyingDeserializerValue() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonParser parser = mapper.getFactory().createParser("3");
        NullifyingDeserializer deserializer = NullifyingDeserializer.instance;

        assertNull(deserializer.deserialize(parser, new DeserializationContext()));
        parser.nextToken(); // consume 3
    }

    @Test
    public void testDeserializeNullifyingDeserializerString() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonParser parser = mapper.getFactory().createParser("\"foo\"");
        NullifyingDeserializer deserializer = NullifyingDeserializer.instance;

        assertNull(deserializer.deserialize(parser, new DeserializationContext()));
        parser.nextToken(); // consume "foo"
    }

    @Test
    public void testDeserializeNullifyingDeserializerBoolean() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonParser parser = mapper.getFactory().createParser("true");
        NullifyingDeserializer deserializer = NullifyingDeserializer.instance;

        assertNull(deserializer.deserialize(parser, new DeserializationContext()));
        parser.nextToken(); // consume true
    }

    @Test
    public void testDeserializeNullifyingDeserializerNull() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonParser parser = mapper.getFactory().createParser("null");
        NullifyingDeserializer deserializer = NullifyingDeserializer.instance;

        assertNull(deserializer.deserialize(parser, new DeserializationContext()));
        parser.nextToken(); // consume null
    }

    @Test
    public void testDeserializeNullifyingDeserializerNumber() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonParser parser = mapper.getFactory().createParser("42.0");
        NullifyingDeserializer deserializer = NullifyingDeserializer.instance;

        assertNull(deserializer.deserialize(parser, new DeserializationContext()));
        parser.nextToken(); // consume 42.0
    }

    @Test
    public void testDeserializeNullifyingDeserializerArray() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonParser parser = mapper.getFactory().createParser("[1, 2, 3]");
        NullifyingDeserializer deserializer = NullifyingDeserializer.instance;

        assertNull(deserializer.deserialize(parser, new DeserializationContext()));
        parser.nextToken(); // consume [
        parser.nextToken(); // consume 1
        parser.nextToken(); // consume ,
        parser.nextToken(); // consume 2
        parser.nextToken(); // consume ,
        parser.nextToken(); // consume 3
        parser.nextToken(); // consume ]
    }

    @Test
    public void testDeserializeNullifyingDeserializerObjectWithProperties() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonParser parser = mapper.getFactory().createParser("{\"foo\": 1, \"bar\": \"foo\"}");
        NullifyingDeserializer deserializer = NullifyingDeserializer.instance;

        assertNull(deserializer.deserialize(parser, new DeserializationContext()));
        parser.nextToken(); // consume {
        parser.nextToken(); // consume FIELD_NAME: "foo"
        parser.nextToken(); // consume: 1
        parser.nextToken(); // consume ,
        parser.nextToken(); // consume FIELD_NAME: "bar"
        parser.nextToken(); // consume: "foo"
        parser.nextToken(); // consume }
    }

    @Test
    public void testDeserializeNullifyingDeserializerNestedArray() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonParser parser = mapper.getFactory().createParser("[1, [2, 3]]");
        NullifyingDeserializer deserializer = NullifyingDeserializer.instance;

        assertNull(deserializer.deserialize(parser, new DeserializationContext()));
        parser.nextToken(); // consume [
        parser.nextToken(); // consume 1
        parser.nextToken(); // consume ,
        parser.nextToken(); // consume [
        parser.nextToken(); // consume [
        parser.nextToken(); // consume 2
        parser.nextToken(); // consume ,
        parser.nextToken(); // consume 3
        parser.nextToken(); // consume ]
        parser.nextToken(); // consume ]
    }

    @Test
    public void testDeserializeNullifyingDeserializerStartArray() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonParser parser = mapper.getFactory().createParser("[");
        NullifyingDeserializer deserializer = NullifyingDeserializer.instance;

        assertNull(deserializer.deserialize(parser, new DeserializationContext()));
        parser.nextToken(); // consume [
    }

    @Test
    public void testDeserializeNullifyingDeserializerStartObject() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonParser parser = mapper.getFactory().createParser("{");
        NullifyingDeserializer deserializer = NullifyingDeserializer.instance;

        assertNull(deserializer.deserialize(parser, new DeserializationContext()));
        parser.nextToken(); // consume {
    }

    @Test
    public void testDeserializeNullifyingDeserializerFieldName() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonParser parser = mapper.getFactory().createParser("{\"foo\": 1}");
        NullifyingDeserializer deserializer = NullifyingDeserializer.instance;

        assertNull(deserializer.deserialize(parser, new DeserializationContext()));
        parser.nextToken(); // consume {
        parser.nextToken(); // consume FIELD_NAME: "foo"
        parser.nextToken(); // consume: 1
    }

    @Test
    public void testDeserializeNullifyingDeserializerWithTypeDeserializer() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        TypeDeserializer typeDeserializer = mapper.getTypeFactory().constructValueType(Object.class);
        JsonParser parser = mapper.getFactory().createParser("{\"foo\": [1, 2, 3]}");
        NullifyingDeserializer deserializer = NullifyingDeserializer.instance;

        assertNull(deserializer.deserializeWithType(parser, new DeserializationContext(), typeDeserializer));
        parser.nextToken(); // consume {
        parser.nextToken(); // consume FIELD_NAME: "foo"
        parser.nextToken(); // consume: [
        parser.nextToken(); // consume 1
        parser.nextToken(); // consume ,
        parser.nextToken(); // consume 2
        parser.nextToken(); // consume ,
        parser.nextToken(); // consume 3
        parser.nextToken(); // consume ]
    }
}