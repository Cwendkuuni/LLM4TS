package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;

import org.junit.Test;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.JsonSerializable;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.jsonFormatVisitors.DialerWrapper;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatParticipant;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatTypes;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitorWrapper;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonValueFormatVisitor;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonValueFormatVisitor.Base;
import com.fasterxml.jackson.databind.ser.std.StdScalarSerializer;
import com.fasterxml.jackson.databind.ser.std.NumberSerializer;

public class NumberSerializerTest {
    @Test
    public void testSerialize() throws IOException {
        NumberSerializer numberSerializer = new NumberSerializer(Number.class);
        JsonGenerator jsonGenerator = new JsonFactory().createGenerator(System.out);
        SerializerProvider serializerProvider = new ObjectMapper().getSerializerProvider();
        numberSerializer.serialize(123, jsonGenerator, serializerProvider);
        jsonGenerator.flush();
        
        jsonGenerator = new JsonFactory().createGenerator(System.out);
        numberSerializer.serialize(123.456, jsonGenerator, serializerProvider);
        jsonGenerator.flush();
        
        jsonGenerator = new JsonFactory().createGenerator(System.out);
        numberSerializer.serialize(new BigInteger("1234567890"), jsonGenerator, serializerProvider);
        jsonGenerator.flush();
        
        jsonGenerator = new JsonFactory().createGenerator(System.out);
        numberSerializer.serialize(new BigDecimal("1234567890.1234567890"), jsonGenerator, serializerProvider);
        jsonGenerator.flush();
        
        jsonGenerator = new JsonFactory().createGenerator(System.out);
        numberSerializer.serialize(123L, jsonGenerator, serializerProvider);
        jsonGenerator.flush();
        
        jsonGenerator = new JsonFactory().createGenerator(System.out);
        numberSerializer.serialize(123.456f, jsonGenerator, serializerProvider);
        jsonGenerator.flush();
    }
    
    @Test
    public void testGetSchema() {
        NumberSerializer numberSerializer = new NumberSerializer(Number.class);
        SerializerProvider serializerProvider = new ObjectMapper().getSerializerProvider();
        JsonNode schema = numberSerializer.getSchema(serializerProvider, null);
        
        assertTrue(schema.isObject());
        assertTrue(schema.has("type"));
        assertEquals("number", schema.get("type").asText());
        assertTrue(schema.has("nullable"));
        assertTrue(schema.get("nullable").asBoolean());
    }
    
    @Test
    public void testAcceptJsonFormatVisitor() throws JsonParser.NumberType {
        NumberSerializer numberSerializer = new NumberSerializer(Number.class);
        JsonFormatVisitorWrapper visitor = new DialerWrapper();
        JavaType typeHint = null;
        
        numberSerializer.acceptJsonFormatVisitor(visitor, typeHint);
        
        assertTrue(visitor instanceof JsonValueFormatVisitor);
        assertTrue(visitor instanceof Base);
        assertTrue(visitor instanceof JsonValueFormatVisitor.Base);
        assertTrue(visitor instanceof JsonFormatParticipant);
        assertTrue(visitor.types().equals(JsonFormatTypes.INTEGER) || visitor.types().equals(JsonFormatTypes.NUMBER));
    }
}