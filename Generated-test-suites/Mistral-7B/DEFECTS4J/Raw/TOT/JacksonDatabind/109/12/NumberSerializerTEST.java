package com.fasterxml.jackson.databind.ser.std; // Import necessary classes for testing
import org.junit.Test;
import static org.junit.Assert.*;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JacksonStdImpl;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitorWrapper;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import com.fasterxml.jackson.databind.ser.std.NumberSerializer;

@SuppressWarnings("unchecked")
public class NumberSerializerTest {

 @Test
 public void testNumberSerializer_BigDecimal() throws JsonProcessingException {
 NumberSerializer numberSerializer = new NumberSerializer(BigDecimal.class);
 ObjectMapper mapper = new ObjectMapper();
 BigDecimal number = new BigDecimal("123.456");

 JsonGenerator generator = mapper.getFactory().createGenerator(new ByteArrayOutputStream(), null);
 numberSerializer.serialize(number, generator, mapper.getSerializerProvider());
 String serializedNumber = generator.getOutputBuffer().toString();
 assertEquals("123.456", serializedNumber);

 JsonParser parser = mapper.getFactory().createParser(new ByteArrayInputStream(serializedNumber.getBytes()));
 Number deserializedNumber = mapper.readValue(parser, Number.class);
 assertEquals(number, deserializedNumber);
 }

 @Test
 public void testNumberSerializer_BigInteger() throws JsonProcessingException {
 NumberSerializer numberSerializer = new NumberSerializer(BigInteger.class);
 ObjectMapper mapper = new ObjectMapper();
 BigInteger number = new BigInteger("12345678901234567890");

 JsonGenerator generator = mapper.getFactory().createGenerator(new ByteArrayOutputStream(), null);
 numberSerializer.serialize(number, generator, mapper.getSerializerProvider());
 String serializedNumber = generator.getOutputBuffer().toString();
 assertEquals("12345678901234567890", serializedNumber);

 JsonParser parser = mapper.getFactory().createParser(new ByteArrayInputStream(serializedNumber.getBytes()));
 Number deserializedNumber = mapper.readValue(parser, Number.class);
 assertEquals(number, deserializedNumber);
 }

 @Test
 public void testNumberSerializer_Integer() throws JsonProcessingException {
 NumberSerializer numberSerializer = NumberSerializer.instance;
 ObjectMapper mapper = new ObjectMapper();
 Integer number = 123;

 JsonGenerator generator = mapper.getFactory().createGenerator(new ByteArrayOutputStream(), null);
 numberSerializer.serialize(number, generator, mapper.getSerializerProvider());
 String serializedNumber = generator.getOutputBuffer().toString();
 assertEquals("123", serializedNumber);

 JsonParser parser = mapper.getFactory().createParser(new ByteArrayInputStream(serializedNumber.getBytes()));
 Number deserializedNumber = mapper.readValue(parser, Number.class);
 assertEquals(number, deserializedNumber);
 }

 @Test
 public void testNumberSerializer_Double() throws JsonProcessingException {
 NumberSerializer numberSerializer = NumberSerializer.instance;
 ObjectMapper mapper = new ObjectMapper();
 Double number = 123.456;

 JsonGenerator generator = mapper.getFactory().createGenerator(new ByteArrayOutputStream(), null);
 numberSerializer.serialize(number, generator, mapper.getSerializerProvider());
 String serializedNumber = generator.getOutputBuffer().toString();
 assertEquals("123.456", serializedNumber);

 JsonParser parser = mapper.getFactory().createParser(new ByteArrayInputStream(serializedNumber.getBytes()));
 Number deserializedNumber = mapper.readValue(parser, Number.class);
 assertEquals(number, deserializedNumber);
 }

 @Test
 public void testNumberSerializer_Float() throws JsonProcessingException {
 NumberSerializer numberSerializer = NumberSerializer.instance;
 ObjectMapper mapper = new ObjectMapper();
 Float number = 123.456f;

 JsonGenerator generator = mapper.getFactory().createGenerator(new ByteArrayOutputStream(), null);
 numberSerializer.serialize(number, generator, mapper.getSerializerProvider());
 String serializedNumber = generator.getOutputBuffer().toString();
 assertEquals("123.456", serializedNumber);

 JsonParser parser = mapper.getFactory().createParser(new ByteArrayInputStream(serializedNumber.getBytes()));
 Number deserializedNumber = mapper.readValue(parser, Number.class);
 assertEquals(number, deserializedNumber);
 }

 @Test
 public void testNumberSerializer_JsonFormat() throws JsonProcessingException {
 NumberSerializer numberSerializer = NumberSerializer.instance;
 ObjectMapper mapper = new ObjectMapper();

 JsonFormat annotation = new JsonFormat();
 annotation.setShape(JsonFormat.Shape.STRING);
 JsonSerializer<Number> customSerializer = mapper.constructType(NumberSerializer.class).withValue(numberSerializer);
 ObjectNode root = mapper.createObjectNode();
 root.put("number", 123);
 JsonNode node = mapper.treeToValues(root, new TypeReference<JsonNode>() {});

 JsonGenerator generator = mapper.getFactory().createGenerator(new ByteArrayOutputStream(), null);
 customSerializer.serialize(node.get("number"), generator, mapper.getSerializerProvider());
 String serializedNumber = generator.getOutputBuffer().toString();
 assertEquals("\"123\"", serializedNumber);

 JsonParser parser = mapper.getFactory().createParser(new ByteArrayInputStream(serializedNumber.getBytes()));
 Number deserializedNumber = mapper.readValue(parser, Number.class);
 assertEquals(123, deserializedNumber.intValue());
 }

 @Test(expected = JsonMappingException.class)
 public void testNumberSerializer_InvalidType() throws JsonProcessingException {
 NumberSerializer numberSerializer = NumberSerializer.instance;
 ObjectMapper mapper = new ObjectMapper();
 String invalidType = "invalidType";

 JsonGenerator generator = mapper.getFactory().createGenerator(new ByteArrayOutputStream(), null);
 numberSerializer.serialize(invalidType, generator, mapper.getSerializerProvider());
 }
}