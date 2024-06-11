package com.fasterxml.jackson.databind.deser.std;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JacksonStdImpl;
import com.fasterxml.jackson.databind.deser.ContextualDeserializer;
import com.fasterxml.jackson.databind.deser.NullValueProvider;
import com.fasterxml.jackson.databind.deser.ValueInstantiator;
import com.fasterxml.jackson.databind.introspect.AnnotatedWithParams;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.Collection;
import java.util.ArrayList;
import static org.junit.Assert.*;

@SuppressWarnings("unchecked")
public class StringCollectionDeserializerTest {

    private ObjectMapper mapper;

    @Before
    public void setup() {
        mapper = new ObjectMapper();
    }

    @After
    public void teardown() throws Exception {
        mapper.close();
    }

    @Test
    public void testEmptyCollectionDeserialization() throws Exception {
        StringCollectionDeserializer deserializer = mapper.findValueDeserializer(Collection.class, null);
        JsonParser parser = mapper.getFactory().createParser("[ ]");
        Collection<String> result = deserializer.deserialize(parser, null);
        assertTrue(result.isEmpty());
    }

    @Test
    public void testSingleStringDeserialization() throws Exception {
        StringCollectionDeserializer deserializer = mapper.findValueDeserializer(Collection.class, null);
        JsonParser parser = mapper.getFactory().createParser("[\"Hello World\"]");
        Collection<String> result = deserializer.deserialize(parser, null);
        assertEquals(1, result.size());
        assertEquals("Hello World", result.iterator().next());
    }

    @Test
    public void testMultipleStringsDeserialization() throws Exception {
        StringCollectionDeserializer deserializer = mapper.findValueDeserializer(Collection.class, null);
        JsonParser parser = mapper.getFactory().createParser("[\"Hello World\", \"Test String\", \"Empty String\"]");
        Collection<String> result = deserializer.deserialize(parser, null);
        assertEquals(3, result.size());
        assertTrue(result.containsAll(asList("Hello World", "Test String", "")));
    }

    @Test
    public void testNullValueDeserialization() throws Exception {
        StringCollectionDeserializer deserializer = mapper.findValueDeserializer(Collection.class, null);
        JsonParser parser = mapper.getFactory().createParser("[null]");
        Collection<String> result = deserializer.deserialize(parser, null);
        assertTrue(result.isEmpty());
        assertNull(result.iterator().next());
    }

    @Test
    public void testDelegateDeserialization() throws Exception {
        StringCollectionDeserializer deserializer = mapper.findValueDeserializer(Collection.class, null);
        ObjectNode node = mapper.createObjectNode();
        node.putPOJO("delegateDeserializer", new DelegateDeserializer());
        JsonParser parser = mapper.getFactory().createParser(node.toString());
        Collection<String> result = deserializer.deserialize(parser, null);
        assertEquals(0, result.size());
    }

    @Test
    public void testCustomValueDeserialization() throws Exception {
        StringCollectionDeserializer deserializer = mapper.findValueDeserializer(Collection.class, null);
        StringCustomDeserializer customDeserializer = new StringCustomDeserializer();
        JsonParser parser = mapper.getFactory().createParser("[1, 2, 3]");
        parser.setCodec(mapper.getSerializerProvider().findValueSerializer(Integer.class));
        Collection<String> result = deserializer.deserialize(parser, null, customDeserializer);
        assertEquals(3, result.size());
        assertTrue(result.containsAll(asList("1", "2", "3")));
    }

    @Test
    public void testNonArrayDeserialization() throws Exception {
        StringCollectionDeserializer deserializer = mapper.findValueDeserializer(Collection.class, null);
        JsonParser parser = mapper.getFactory().createParser("{\"value\": \"Hello World\"}");
        Collection<String> result = deserializer.deserialize(parser, null);
        assertEquals(1, result.size());
        assertEquals("Hello World", result.iterator().next());
    }

    @Test(expected = JsonMappingException.class)
    public void testInvalidJsonDeserialization() throws Exception {
        StringCollectionDeserializer deserializer = mapper.findValueDeserializer(Collection.class, null);
        JsonParser parser = mapper.getFactory().createParser("{\"invalid\": \"Json\"}");
        deserializer.deserialize(parser, null);
    }

    static class DelegateDeserializer implements JsonDeserializer<Object> {
        @Override
        public Object deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
            return null;
        }
    }

    static class StringCustomDeserializer extends StdDeserializer<String> {
        @Override
        public String deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
            return p.getValueAsInt() + "";
        }
    }
}