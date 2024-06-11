package com.fasterxml.jackson.databind.ser.std;

import static com.fasterxml.jackson.core.util.TestUtilities.convertValueToStandardJavaType;
import static org.junit.Assert.*;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class StdKeySerializerTest {

    @Test
    public void testSerializeDate() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        StdKeySerializer serializer = new StdKeySerializer();

        Date date = new Date();
        String expectedDateString = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(date);

        JsonGenerator jgen = mapper.getFactory().createGenerator(System.out, JsonEncoding.UTF8);
        serializer.serialize(date, jgen, mapper.getSerializerProvider());
        jgen.flush();

        String actualDateString = jgen.getOutputStr();
        assertEquals(expectedDateString, actualDateString);
    }

    @Test
    public void testSerializeString() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        StdKeySerializer serializer = new StdKeySerializer();

        String value = "test";
        String expected = "\"" + value + "\"";

        JsonGenerator jgen = mapper.getFactory().createGenerator(System.out, JsonEncoding.UTF8);
        serializer.serialize(value, jgen, mapper.getSerializerProvider());
        jgen.flush();

        String actual = jgen.getOutputStr();
        assertEquals(expected, actual);
    }

    @Test
    public void testSerializeNull() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        StdKeySerializer serializer = new StdKeySerializer();

        JsonGenerator jgen = mapper.getFactory().createGenerator(System.out, JsonEncoding.UTF8);
        serializer.serialize(null, jgen, mapper.getSerializerProvider());
        jgen.flush();

        String actual = jgen.getOutputStr();
        assertEquals("null", actual);
    }

    @Test
    public void testGetSchema() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        StdKeySerializer serializer = new StdKeySerializer();

        JavaType typeHint = mapper.getTypeFactory().constructType(String.class);
        JsonNode schema = serializer.getSchema(mapper.getSerializerProvider(), typeHint);

        ObjectNode schemaObject = (ObjectNode) schema;
        assertEquals("string", schemaObject.get("type").asText());
    }

    @Test
    public void testAcceptJsonFormatVisitor() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        StdKeySerializer serializer = new StdKeySerializer();

        JsonFormatVisitorWrapper visitor = mapper.writerWithDefaultPrettyPrinter().acceptJsonFormatVisitor(JsonFormatType.STRING);
        serializer.acceptJsonFormatVisitor(visitor, mapper.getTypeFactory().constructType(Object.class));

        String expected = "\"string\"";
        String actual = visitor.getFormattedValueAsString();

        assertEquals(expected, actual);
    }
}