package com.fasterxml.jackson.databind.deser.std;

import static com.fasterxml.jackson.core.JsonParser.Current.FIELD_NAME;
import static com.fasterxml.jackson.core.JsonParser.Current.START_ARRAY;
import static com.fasterxml.jackson.core.JsonParser.Current.START_OBJECT;
import static org.junit.Assert.assertNull;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import org.junit.Test;

public class NullifyingDeserializerTest {

    @Test
    public void testDeserializeNull() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonParser parser = mapper.getFactory().createParser("null");
        NullifyingDeserializer deserializer = NullifyingDeserializer.instance;
        assertNull(deserializer.deserialize(parser, new DeserializationContext(new BaseJsonModule())));
    }

    @Test
    public void testDeserializeArray() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonParser parser = mapper.getFactory().createParser("[null]");
        NullifyingDeserializer deserializer = NullifyingDeserializer.instance;
        assertNull(deserializer.deserialize(parser, new DeserializationContext(new BaseJsonModule())));
    }

    @Test
    public void testDeserializeObjectStart() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonParser parser = mapper.getFactory().createParser("{\"name\":null}");
        NullifyingDeserializer deserializer = NullifyingDeserializer.instance;
        assertNull(deserializer.deserialize(parser, new DeserializationContext(new BaseJsonModule())));
    }

    @Test
    public void testDeserializeObjectFieldName() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonParser parser = mapper.getFactory().createParser("{\"name\":null,\"age\":5}");
        NullifyingDeserializer deserializer = NullifyingDeserializer.instance;
        JsonToken token = parser.nextToken();
        parser.nextToken(); // skip "name"
        Object result = deserializer.deserialize(parser, new DeserializationContext(new BaseJsonModule()));
        assertNull(result);
        parser.nextToken(); // skip "age"
        parser.nextToken(); // skip value of "age"
    }

    @Test
    public void testDeserializeWithTypeArray() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonParser parser = mapper.getFactory().createParser("[null]");
        NullifyingDeserializer deserializer = NullifyingDeserializer.instance;
        TypeDeserializer typeDeserializer = mapper.getTypeFactory().constructType(String[].class);
        Object result = deserializer.deserializeWithType(parser, new DeserializationContext(new BaseJsonModule()), typeDeserializer);
        assertNull(result);
    }

    @Test
    public void testDeserializeWithTypeObjectStart() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonParser parser = mapper.getFactory().createParser("{\"name\":null}");
        NullifyingDeserializer deserializer = NullifyingDeserializer.instance;
        TypeDeserializer typeDeserializer = mapper.getTypeFactory().constructType(Map.class);
        Object result = deserializer.deserializeWithType(parser, new DeserializationContext(new BaseJsonModule()), typeDeserializer);
        assertNull(result);
    }

    @Test
    public void testDeserializeWithTypeObjectFieldName() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonParser parser = mapper.getFactory().createParser("{\"name\":null,\"age\":5}");
        NullifyingDeserializer deserializer = NullifyingDeserializer.instance;
        TypeDeserializer typeDeserializer = mapper.getTypeFactory().constructType(Map.class);
        Object result = deserializer.deserializeWithType(parser, new DeserializationContext(new BaseJsonModule()), typeDeserializer);
        assertNull(result);
        parser.nextToken(); // skip "age"
        JsonToken token = parser.nextToken();
        if (token.isStructStart()) { // Object
            parser.nextToken(); // skip "age"
            parser.nextToken(); // skip value of "age"
        }
    }

    @Test
    public void testDeserializeWithTypeComplexObject() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonParser parser = mapper.getFactory().createParser("{\"name\":null,\"list\":[1,2,3]}");
        NullifyingDeserializer deserializer = NullifyingDeserializer.instance;
        TypeDeserializer typeDeserializer = mapper.getTypeFactory().constructType(MyComplexType.class);
        Object result = deserializer.deserializeWithType(parser, new DeserializationContext(new BaseJsonModule()), typeDeserializer);
        assertNull(result);
        parser.nextToken(); // skip "list"
        parser.nextToken(); // skip [
        parser.nextToken(); // skip 1
        parser.nextToken(); // skip 2
        parser.nextToken(); // skip 3
        parser.nextToken(); // skip ]
    }

    static class MyComplexType {
        String name;
        List<Integer> list;
    }
}