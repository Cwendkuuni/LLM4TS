package com.fasterxml.jackson.databind.deser.std;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class NullifyingDeserializerTest {

    @Test
    public void testDeserializeNull() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonParser parser = mapper.getFactory().createParser("null");
        DeserializationContext ctxt = mapper.getDeserializationContext();
        assertEquals(NullifyingDeserializer.instance, parser.getCurrentNamespace().findValueDeserializer(Object.class, ctxt));
        assertNull(NullifyingDeserializer.instance.deserialize(parser, ctxt));
    }

    @Test
    public void testDeserializeEmptyArray() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonParser parser = mapper.getFactory().createParser("[]");
        DeserializationContext ctxt = mapper.getDeserializationContext();
        assertEquals(NullifyingDeserializer.instance, parser.getCurrentNamespace().findValueDeserializer(Object[].class, ctxt));
        assertNull(NullifyingDeserializer.instance.deserialize(parser, ctxt));
    }

    @Test
    public void testDeserializeEmptyObject() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonParser parser = mapper.getFactory().createParser("{}");
        DeserializationContext ctxt = mapper.getDeserializationContext();
        assertEquals(NullifyingDeserializer.instance, parser.getCurrentNamespace().findValueDeserializer(Object.class, ctxt));
        assertNull(NullifyingDeserializer.instance.deserialize(parser, ctxt));
    }

    @Test
    public void testDeserializeArrayWithValues() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonParser parser = mapper.getFactory().createParser("[1, 2, 3]");
        DeserializationContext ctxt = mapper.getDeserializationContext();
        assertNull(NullifyingDeserializer.instance.deserialize(parser, ctxt));
    }

    @Test
    public void testDeserializeObjectWithFields() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonParser parser = mapper.getFactory().createParser("{\"field1\": \"value1\", \"field2\": \"value2\"}");
        DeserializationContext ctxt = mapper.getDeserializationContext();
        assertNull(NullifyingDeserializer.instance.deserialize(parser, ctxt));
    }

    @Test
    public void testDeserializeObjectWithEmptyFields() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonParser parser = mapper.getFactory().createParser("{\"field1\": \"\", \"field2\": null}");
        DeserializationContext ctxt = mapper.getDeserializationContext();
        assertNull(NullifyingDeserializer.instance.deserialize(parser, ctxt));
    }

    @Test
    public void testDeserializeArrayWithComplexValues() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        String json = "[1, [2, [3, null]], 4], {\"field1\": [1, 2], \"field2\": null}, false]";
        JsonParser parser = mapper.getFactory().createParser(json);
        DeserializationContext ctxt = mapper.getDeserializationContext();
        assertNull(NullifyingDeserializer.instance.deserialize(parser, ctxt));
    }

    @Test
    public void testDeserializeObjectWithComplexValues() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        String json = "{\"field1\": [1, 2], \"field2\": { \"field3\": 3, \"field4\": null }, \"field5\": false}";
        JsonParser parser = mapper.getFactory().createParser(json);
        DeserializationContext ctxt = mapper.getDeserializationContext();
        assertNull(NullifyingDeserializer.instance.deserialize(parser, ctxt));
    }

    @Test
    public void testDeserializeWithType() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonParser parser = mapper.getFactory().createParser("[]");
        DeserializationContext ctxt = mapper.getDeserializationContext();
        TypeDeserializer typeDeserializer = mapper.getTypeFactory().constructCollectionType(List.class, Integer.class);
        assertNull(NullifyingDeserializer.instance.deserializeWithType(parser, ctxt, typeDeserializer));
    }

    @Test
    public void testDeserializeWithTypeObject() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonParser parser = mapper.getFactory().createParser("{\"field1\": 1, \"field2\": \"value2\"}");
        DeserializationContext ctxt = mapper.getDeserializationContext();
        TypeDeserializer typeDeserializer = mapper.getTypeFactory().constructType(MyClass.class);
        assertNull(NullifyingDeserializer.instance.deserializeWithType(parser, ctxt, typeDeserializer));
    }

    static class MyClass {
        int field1;
        String field2;
    }
}