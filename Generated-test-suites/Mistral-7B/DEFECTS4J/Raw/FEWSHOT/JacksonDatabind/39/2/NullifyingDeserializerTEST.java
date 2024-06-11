package com.fasterxml.jackson.databind.deser.std;

import static com.fasterxml.jackson.core.JsonToken.*;
import static org.junit.Assert.*;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import org.junit.Test;

public class NullifyingDeserializerTest {

    @Test
    public void testDeserializeNull() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        JsonParser parser = mapper.getFactory().createParser("[]");
        JsonDeserializer<Object> deserializer = NullifyingDeserializer.instance;
        Object result = deserializer.deserialize(parser, new DeserializationContext());
        assertNull(result);
    }

    @Test
    public void testDeserializeArray() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        JsonParser parser = mapper.getFactory().createParser("[1, 2, 3]");
        JsonDeserializer<Object> deserializer = NullifyingDeserializer.instance;
        Object result = deserializer.deserialize(parser, new DeserializationContext());
        assertNull(result);
    }

    @Test
    public void testDeserializeObject() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        JsonParser parser = mapper.getFactory().createParser("{\"name\": \"John\", \"age\": 30}");
        JsonDeserializer<Object> deserializer = NullifyingDeserializer.instance;
        Object result = deserializer.deserialize(parser, new DeserializationContext());
        assertNull(result);
    }

    @Test
    public void testDeserializeArrayWithType() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        JsonParser parser = mapper.getFactory().createParser("[1, 2, 3]");
        JsonDeserializer<List<Integer>> deserializer = NullifyingDeserializer.instance;
        TypeDeserializer typeDeserializer = new StdTypeDeserializer().findValueDeserializer(List.class, mapper.getTypeFactory());
        Object result = deserializer.deserializeWithType(parser, new DeserializationContext(), typeDeserializer);
        assertNull(result);
    }

    @Test
    public void testDeserializeObjectWithType() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        JsonParser parser = mapper.getFactory().createParser("{\"name\": \"John\", \"age\": 30}");
        JsonDeserializer<Map<String, Object>> deserializer = NullifyingDeserializer.instance;
        TypeDeserializer typeDeserializer = new StdTypeDeserializer().findValueDeserializer(Map.class, mapper.getTypeFactory());
        Object result = deserializer.deserializeWithType(parser, new DeserializationContext(), typeDeserializer);
        assertNull(result);
    }

    @Test
    public void testDeserializeStartArray() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        JsonParser parser = mapper.getFactory().createParser("[");
        JsonDeserializer<Object> deserializer = NullifyingDeserializer.instance;
        Object result = deserializer.deserialize(parser, new DeserializationContext());
        assertNull(result);
    }

    @Test
    public void testDeserializeStartObject() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        JsonParser parser = mapper.getFactory().createParser("{");
        JsonDeserializer<Object> deserializer = NullifyingDeserializer.instance;
        Object result = deserializer.deserialize(parser, new DeserializationContext());
        assertNull(result);
    }

    @Test
    public void testDeserializeFieldName() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        JsonParser parser = mapper.getFactory().createParser("{\"name\": \"John\"}");
        JsonDeserializer<Object> deserializer = NullifyingDeserializer.instance;
        Object result = deserializer.deserialize(parser, new DeserializationContext());
        assertNull(result);
    }

    @Test(expected = IOException.class)
    public void testDeserializeUnexpectedToken() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        JsonParser parser = mapper.getFactory().createParser("true");
        JsonDeserializer<Object> deserializer = NullifyingDeserializer.instance;
        deserializer.deserialize(parser, new DeserializationContext());
    }

    @Test
    public void testDeserializeWithTypeUnexpectedToken1() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        JsonParser parser = mapper.getFactory().createParser("true");
        JsonDeserializer<Object> deserializer = NullifyingDeserializer.instance;
        TypeDeserializer typeDeserializer = new StdTypeDeserializer();
        Object result = deserializer.deserializeWithType(parser, new DeserializationContext(), typeDeserializer);
        assertNull(result);
    }

    @Test
    public void testDeserializeWithTypeUnexpectedToken2() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        JsonParser parser = mapper.getFactory().createParser("true");
        JsonDeserializer<Object> deserializer = NullifyingDeserializer.instance;
        TypeDeserializer typeDeserializer = new StdTypeDeserializer();
        typeDeserializer = mapper.getTypeFactory().constructValueType(List.class, Integer[].class);
        Object result = deserializer.deserializeWithType(parser, new DeserializationContext(), typeDeserializer);
        assertNull(result);
    }
}