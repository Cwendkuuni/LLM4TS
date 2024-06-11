package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.*;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;

import org.junit.Test;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitorWrapper;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;

/**
 * Comprehensive test file for NumberSerializer class
 */
public class NumberSerializerTest {

 @Test
 public void testNumberSerializer() throws JsonProcessingException {
 ObjectMapper mapper = new ObjectMapper();

 // Test serialization of various Number types
 Number number1 = 1;
 Number number2 = 1L;
 Number number3 = 1.0;
 Number number4 = 1.0f;
 Number number5 = new BigDecimal("1.23");
 Number number6 = new BigInteger("12345");

 JsonGenerator generator = mapper.getFactory().createGenerator();
 mapper.writeValue(generator, number1);
 mapper.writeValue(generator, number2);
 mapper.writeValue(generator, number3);
 mapper.writeValue(generator, number4);
 mapper.writeValue(generator, number5);
 mapper.writeValue(generator, number6);
 generator.close();

 // Test deserialization of various Number types
 JsonParser parser = mapper.getFactory().createParser("1");
 Number deserializedNumber1 = mapper.readValue(parser, Number.class);
 parser.nextToken(); // skip value "1"
 Number deserializedNumber2 = mapper.readValue(parser, Number.class);
 parser.nextToken(); // skip value "1" (long)
 Number deserializedNumber3 = mapper.readValue(parser, Number.class);
 parser.nextToken(); // skip value "1.0" (double)
 Number deserializedNumber4 = mapper.readValue(parser, Number.class);
 parser.nextToken(); // skip value "1.0" (float)
 JsonToken token = parser.nextToken();
 assertNotNull(token);
 BigDecimal deserializedNumber5 = mapper.readValue(parser, BigDecimal.class);
 parser.nextToken(); // skip value "1.23"
 BigInteger deserializedNumber6 = mapper.readValue(parser, BigInteger.class);
 parser.nextToken(); // skip value "12345"

 // Test ContextualSerializer
 JsonFormat.Shape shape = JsonFormat.Shape.STRING;
 ObjectMapper mapperWithFormat = new ObjectMapper();
 mapperWithFormat.setAnnotationIntrospector(new StandardAnnotationIntrospector());
 mapperWithFormat.findAndRegisterModules();
 JsonSerializer<Number> serializer = mapperWithFormat.findValueSerializer(BigDecimal.class);
 JsonFormat<Number> jsonFormat = new JsonFormat.Value();
 jsonFormat.setShape(shape);
 JsonGenerator generatorWithFormat = mapperWithFormat.getFactory().createGenerator();
 mapperWithFormat.writeValue(generatorWithFormat, new BigDecimal("1.23"), jsonFormat);
 generatorWithFormat.close();

 assertTrue(serializer instanceof ContextualSerializer);
 ContextualSerializer contextualSerializer = (ContextualSerializer) serializer;
 JsonSerializer<Number> serializedNumberSerializer = contextualSerializer.createContextual(mapperWithFormat.getSerializerProvider(), null);
 assertEquals(ToStringSerializer.instance.getClass(), serializedNumberSerializer.getClass());
 }

 @Test(expected = JsonProcessingException.class)
 public void testNumberSerializerInvalid() throws JsonProcessingException {
 ObjectMapper mapper = new ObjectMapper();

 // Test deserialization failure for unsupported types
 JsonParser parser = mapper.getFactory().createParser("[1, 2]");
 mapper.readValue(parser, Number.class);

 // Test serialization failure for unsupported types
 Object value = new Object();
 JsonGenerator generator = mapper.getFactory().createGenerator();
 mapper.writeValue(generator, value);
 generator.close();
 }
}