package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.Date;
import java.util.Map;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class StdKeySerializerTest {

    @Test
    public void testSerialize_Date() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);

        Date date = new Date();

        JsonGenerator jgen = mapper.getFactory().createGenerator(System.out);
        StdKeySerializer serializer = new StdKeySerializer();

        serializer.serialize(date, jgen, mapper.getSerializerProvider());
        jgen.flush();

        // Verify that the default DateKeySerializer was used instead
        String output = System.out.getOutputStream().toString();
        assertTrue(output.contains("\"@type\":\"Date\""));
    }

    @Test
    public void testSerialize_String() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);

        String key = "my_key";

        JsonGenerator jgen = mapper.getFactory().createGenerator(System.out);
        StdKeySerializer serializer = new StdKeySerializer();

        serializer.serialize(key, jgen, mapper.getSerializerProvider());
        jgen.flush();

        // Verify that the key was serialized as a string
        String output = System.out.getOutputStream().toString();
        assertEquals("\"my_key\"", output);
    }

    @Test
    public void testSerialize_Null() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);

        JsonGenerator jgen = mapper.getFactory().createGenerator(System.out);
        StdKeySerializer serializer = new StdKeySerializer();

        serializer.serialize(null, jgen, mapper.getSerializerProvider());
        jgen.flush();

        // Verify that null was not serialized
        String output = System.out.getOutputStream().toString();
        assertNull(output);
    }

    @Test
    public void testGetSchema() throws JsonMappingException {
        ObjectMapper mapper = new ObjectMapper();

        StdKeySerializer serializer = new StdKeySerializer();
        JsonNode schema = serializer.getSchema(mapper.getSerializerProvider(Object.class), Object.class);

        // Verify that the schema specifies a string format
        String type = schema.path("type").asText();
        assertEquals("string", type);
    }

    @Test
    public void testAcceptJsonFormatVisitor() throws JsonMappingException {
        ObjectMapper mapper = new ObjectMapper();

        StdKeySerializer serializer = new StdKeySerializer();
        JsonFormatVisitorWrapper visitor = mapper.getSerializerProvider().getInputFactory().getJsonFormatVisitor(null, Object.class);

        serializer.acceptJsonFormatVisitor(visitor, Object.class);

        // Verify that the visitor expects a string format
        JsonFormat.Value format = visitor.getFormat();
        assertTrue(format.getShape() == JsonFormat.Value.Shape.STRING);
    }

    @Test
    public void testSerialize_MapEntry() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);

        Map<String, Integer> map = Map.ofEntries(Map.entry("key1", 1), Map.entry("key2", 2));

        JsonGenerator jgen = mapper.getFactory().createGenerator(System.out);
        StdKeySerializer serializer = new StdKeySerializer();
        SimpleBeanPropertyFilter filter = SimpleBeanPropertyFilter.filterOutAllExcept("key1", "key2");

        mapper.writerWithView(filter).writeValue(jgen, map);
        jgen.flush();

        // Verify that the keys were serialized correctly
        String output = System.out.getOutputStream().toString();
        JsonParser parser = mapper.getFactory().createParser(output);
        ObjectNode rootNode = parser.readValueAs(ObjectNode.class);

        assertEquals(2, rootNode.size());
        assertTrue(rootNode.has("key1"));
        assertTrue(rootNode.has("key2"));
    }
}