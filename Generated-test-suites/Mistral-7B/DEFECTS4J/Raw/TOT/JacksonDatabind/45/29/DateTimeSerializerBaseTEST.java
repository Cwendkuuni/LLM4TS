package com.fasterxml.jackson.databind.ser.std;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.node.TextNode;
import com.fasterxml.jackson.databind.ser.std.DateTimeSerializerBase;
import com.fasterxml.jackson.databind.type.TypeFactory;

import org.junit.Test;
import static org.junit.Assert.*;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.TimeZone;

public class DateTimeSerializerBaseTest {

 @Test
 public void testSerializationWithTimestampFlag() throws JsonProcessingException {
 DateTimeSerializerBase<Long> serializer = new StdDateSerializer();
 SerializerProvider serializers = new SerializerProvider();
 Long testValue = 1561983420000L; // 2019-06-12 13:50:20

 JsonNode expectedResult = TextNode.valueOf("1561983420000");
 JsonNode result = serializer.serialize(testValue, new JsonGenerator(serializers), serializers);

 assertEquals(expectedResult, result);
 }

 @Test
 public void testSerializationWithCustomFormat() throws JsonProcessingException {
 DateTimeSerializerBase<String> serializer = new StdDateSerializer();
 SerializerProvider serializers = new SerializerProvider();
 String testValue = "2019-06-12 13:50:20";
 DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
 TimeZone timeZone = TimeZone.getTimeZone("UTC");
 dateFormat.setTimeZone(timeZone);
 serializer = serializer.withFormat(Boolean.FALSE, dateFormat);

 JsonNode expectedResult = new TextNode("2019-06-12T13:50:20Z");
 JsonNode result = serializer.serialize(testValue, new JsonGenerator(serializers), serializers);

 assertEquals(expectedResult, result);
 }

 @Test
 public void testSerializationWithJsonFormatAnnotation() throws JsonProcessingException, ParseException {
 ObjectMapper mapper = new ObjectMapper();
 DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
 TimeZone timeZone = TimeZone.getTimeZone("UTC");
 dateFormat.setTimeZone(timeZone);

 @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "UTC")
 class TestClass {
 private Long date;

 public Long getDate() {
 return date;
 }

 public void setDate(Long date) {
 this.date = date;
 }
 }
 TestClass testObject = new TestClass();
 testObject.setDate(1561983420000L); // 2019-06-12 13:50:20

 JsonNode expectedResult = new TextNode("2019-06-12T13:50:20Z");
 JsonNode result = mapper.writeValueAsTree(testObject);

 assertEquals(expectedResult, result);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSerializationWithoutProvider() throws JsonProcessingException {
 DateTimeSerializerBase<Long> serializer = new StdDateSerializer();
 Long testValue = 1561983420000L; // 2019-06-12 13:50:20
 serializer.serialize(testValue, null, null);
 }
}