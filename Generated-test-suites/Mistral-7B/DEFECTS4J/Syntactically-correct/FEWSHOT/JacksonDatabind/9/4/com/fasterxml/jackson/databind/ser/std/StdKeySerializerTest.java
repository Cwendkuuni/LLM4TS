package com.fasterxml.jackson.databind.ser.std;

import static com.fasterxml.jackson.core.util.TestUtils.convertValueToStandardJsonFormat;
import static org.junit.Assert.assertEquals;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitorWrapper;

public class StdKeySerializerTest {

    @Test
    public void testSerializeDate() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Date date = new Date();
        JsonGenerator jsonGenerator = mapper.getFactory().createGenerator();
        StdKeySerializer keySerializer = new StdKeySerializer();
        keySerializer.serialize(date, jsonGenerator, mapper.getSerializerProvider());
        assertEquals("\"@jsontype\":\"Date\"", jsonGenerator.getOutputBuffer().toString());
        mapper.writeValueAsString(date); // to check that Date is serializable correctly without the key serializer
    }

    @Test
    public void testSerializeString() throws JsonGenerationException, JsonMappingException {
        ObjectMapper mapper = new ObjectMapper();
        String testString = "testString";
        JsonGenerator jsonGenerator = mapper.getFactory().createGenerator();
        StdKeySerializer keySerializer = new StdKeySerializer();
        keySerializer.serialize(testString, jsonGenerator, mapper.getSerializerProvider());
        assertEquals("testString", jsonGenerator.getOutputBuffer().toString());
    }

    @Test
    public void testSerializeNull() throws JsonGenerationException {
        ObjectMapper mapper = new ObjectMapper();
        JsonGenerator jsonGenerator = mapper.getFactory().createGenerator();
        StdKeySerializer keySerializer = new StdKeySerializer();
        keySerializer.serialize(null, jsonGenerator, mapper.getSerializerProvider());
        assertNull(jsonGenerator.getOutputBuffer().toString());
    }

    @Test
    public void testGetSchema() throws JsonMappingException {
        ObjectMapper mapper = new ObjectMapper();
        StdKeySerializer keySerializer = new StdKeySerializer();
        JsonNode schema = keySerializer.getSchema(mapper.getSerializerProvider(), JavaType.getRawClassType(Object.class));
        assertEquals("string", schema.asText());
    }

    @Test
    public void testAcceptJsonFormatVisitor() throws JsonMappingException {
        ObjectMapper mapper = new ObjectMapper();
        StdKeySerializer keySerializer = new StdKeySerializer();
        JsonFormatVisitorWrapper visitor = mapper.getSerializationConfig().getFormatFactory().getJsonFormatVisitor(null, JavaType.getRawClassType(Object.class), null);
        keySerializer.acceptJsonFormatVisitor(visitor, JavaType.getRawClassType(Object.class));
        visitor.expectStringFormat(JavaType.getRawClassType(Object.class));
    }
}