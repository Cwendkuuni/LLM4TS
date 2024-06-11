package com.fasterxml.jackson.databind.ser.std;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.node.TextNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.junit.Test;
import static org.junit.Assert.*;

public class DateTimeSerializerBaseTest {

 private static final ObjectMapper mapper = new ObjectMapper();

 @Test
 public void testDateTimeSerializerBaseWithDefaultSettings() throws JsonProcessingException {
 DateTimeSerializerBase<java.util.Date> dateSerializer = mapper.findValueSerializer(java.util.Date.class);
 JavaType javaUtilDateType = mapper.getTypeFactory().constructType(java.util.Date.class);
 JsonFormatVisitorWrapper visitor = mapper.schemaFactory().getJsonFormatVisitor(javaUtilDateType);
 Date date = new Date();
 dateSerializer.acceptJsonFormatVisitor(visitor, javaUtilDateType);
 assertTrue(visitor.expectNumberType(JsonParser.NumberType.LONG).isMet());
 }

 @Test
 public void testDateTimeSerializerBaseWithCustomFormat() throws JsonProcessingException {
 DateTimeSerializerBase<java.util.Date> dateSerializer = mapper.findValueSerializer(java.util.Date.class);
 JavaType javaUtilDateType = mapper.getTypeFactory().constructType(java.util.Date.class);
 JsonFormatVisitorWrapper visitor = mapper.schemaFactory().getJsonFormatVisitor(javaUtilDateType);
 dateSerializer.acceptJsonFormatVisitor(visitor, javaUtilDateType, false);
 assertTrue(visitor.expectStringFormat(JsonValueFormat.DATE_TIME).isMet());
 }

 @Test
 public void testDateTimeSerializerBaseWithTimestamp() throws JsonProcessingException {
 DateTimeSerializerBase<java.util.Date> dateSerializer = mapper.findValueSerializer(java.util.Date.class);
 SerializerProvider serializers = mapper.getSerializerProvider();
 JavaType javaUtilDateType = mapper.getTypeFactory().constructType(java.util.Date.class);
 ObjectNode node = mapper.createObjectNode();
 node.put("format", "number");
 JsonFormat.Value jsonFormat = new JsonFormat.Value(node);
 dateSerializer = dateSerializer.withFormat(true, null);
 dateSerializer.acceptJsonFormatVisitor(visitor, javaUtilDateType, true);
 assertTrue(visitor.expectNumberType(JsonParser.NumberType.LONG).isMet());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testDateTimeSerializerBaseWithNullSerializerProvider() throws JsonProcessingException {
 DateTimeSerializerBase<java.util.Date> dateSerializer = mapper.findValueSerializer(java.util.Date.class);
 dateSerializer.acceptJsonFormatVisitor(null, mapper.getTypeFactory().constructType(java.util.Date.class), false);
 }
}