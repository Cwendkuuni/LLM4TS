package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.node.*;

public class StdKeySerializerTest {

    @Test
    public void testSerializeDate() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Date date = new Date();

        JsonGenerator gen = mapper.getFactory().createGenerator(System.out);
        StdKeySerializer serializer = new StdKeySerializer();
        serializer.serialize(date, gen, null);
        gen.flush();

        String expected = "\"" + date.toISO8601DateString() + "\"";
        assertEquals(expected, gen.getOutputContent().toString());
    }

    @Test
    public void testSerializeString() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        String str = "key";

        JsonGenerator gen = mapper.getFactory().createGenerator(System.out);
        StdKeySerializer serializer = new StdKeySerializer();
        serializer.serialize(str, gen, null);
        gen.flush();

        String expected = "\"" + str + "\"";
        assertEquals(expected, gen.getOutputContent().toString());
    }

    @Test
    public void testGetSchema() throws JsonMappingException {
        ObjectMapper mapper = new ObjectMapper();
        StdKeySerializer serializer = new StdKeySerializer();

        JsonNode schema = serializer.getSchema(mapper.getSerializerProvider(), Object.class);

        assertEquals("string", schema.asText());
    }

    @Test
    public void testAcceptJsonFormatVisitor() throws JsonMappingException {
        ObjectMapper mapper = new ObjectMapper();
        StdKeySerializer serializer = new StdKeySerializer();

        JsonFormatVisitorWrapper visitor = mapper.getSerializerProvider().getObjectWriter().getSchemaProvider().getFormatVisitor(Object.class, null, null);
        serializer.acceptJsonFormatVisitor(visitor, Object.class);

        assertTrue(visitor.wasVisited(StdKeySerializer.class));
    }
}