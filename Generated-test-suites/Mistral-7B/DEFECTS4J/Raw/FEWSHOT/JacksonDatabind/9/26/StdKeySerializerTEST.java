package com.fasterxml.jackson.databind.ser.std;

import static com.fasterxml.jackson.core.util.TestUtils.convertValueToObject;
import static org.junit.Assert.*;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonFormatVisitorWrapper;

/**
 * Test cases for StdKeySerializer class
 */
public class StdKeySerializerTest {

    @Test
    public void testSerializeDate() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        SerializerProvider provider = mapper.getSerializerProvider();

        Date date = new Date();
        StdKeySerializer serializer = new StdKeySerializer();
        JsonGenerator jgen = mapper.getFactory().createGenerator();

        serializer.serialize(date, jgen, provider);
        jgen.flush();
        String output = jgen.getOutputStr();
        assertEquals("\"\"", output); // Jackson uses empty string as date key
    }

    @Test
    public void testSerializeString() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        SerializerProvider provider = mapper.getSerializerProvider();

        String str = "testString";
        StdKeySerializer serializer = new StdKeySerializer();
        JsonGenerator jgen = mapper.getFactory().createGenerator();

        serializer.serialize(str, jgen, provider);
        jgen.flush();
        String output = jgen.getOutputStr();
        assertEquals("\"testString\"", output);
    }

    @Test
    public void testGetSchema() throws JsonMappingException {
        StdKeySerializer serializer = new StdKeySerializer();
        JsonNode schema = serializer.getSchema(null, null);
        assertEquals("\"string\"", schema.toString());
    }

    @Test
    public void testAcceptJsonFormatVisitor() {
        StdKeySerializer serializer = new StdKeySerializer();

        JsonFormatVisitorWrapper visitor = new JsonFormatVisitorWrapper(null, null, null);
        serializer.acceptJsonFormatVisitor(visitor, null);
        assertTrue(visitor.expectsStringFormat(null));
    }

    @Test
    public void testSerializeNull() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        SerializerProvider provider = mapper.getSerializerProvider();

        StdKeySerializer serializer = new StdKeySerializer();
        JsonGenerator jgen = mapper.getFactory().createGenerator();

        serializer.serialize(null, jgen, provider);
        jgen.flush();
        String output = jgen.getOutputStr();
        assertNull(output);
    }

    @Test
    public void testSerializeCustomClass() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        SerializerProvider provider = mapper.getSerializerProvider();

        class CustomClass {
            private String name;

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }
        }

        CustomClass customClass = new CustomClass();
        customClass.setName("testCustomClass");

        StdKeySerializer serializer = new StdKeySerializer();
        JsonGenerator jgen = mapper.getFactory().createGenerator();

        serializer.serialize(customClass, jgen, provider);
        jgen.flush();
        String output = jgen.getOutputStr();
        assertEquals("\"testCustomClass\"", output);
    }
}