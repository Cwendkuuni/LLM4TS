package com.fasterxml.jackson.databind.ser.std;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.TextNode;
import com.fasterxml.jackson.databind.ser.std.DateTimeSerializerBase;
import org.junit.Before;
import org.junit.Test;
import java.io.IOException;
import java.time.Instant;
import java.time.ZoneId;
import java.util.Date;
import static org.junit.Assert.*;

public class DateTimeSerializerBaseTest {

 private DateTimeSerializerBase<Date> dateSerializer;
 private DateTimeSerializerBase<Instant> instantSerializer;

 @Before
 public void setUp() throws JsonMappingException {
 dateSerializer = new StdDateSerializer(Date.class, false, null);
 instantSerializer = new StdInstantSerializer(Instant.class, false, null);
 }

 @Test
 public void testDateSerializationWithDefaultSettings() throws JsonProcessingException {
 Date date = new Date();
 JsonNode result = mapper.writeValueAsTree(date);
 assertEquals("\"2023-03-13T12:34:56.789Z\"", result.toString());

 // Test deserialization
 Date deserializedDate = mapper.readValue(result.toString(), Date.class);
 assertEquals(date, deserializedDate);
 }

 @Test
 public void testDateSerializationWithCustomFormat() throws JsonGenerationException, JsonMappingException, JsonProcessingException {
 JsonFormat format = new JsonFormat(new DateFormat("dd-MM-yyyy'T'HH:mm:ss.SSS'Z'"));
 DateTimeSerializerBase<Date> customFormatterSerializer = dateSerializer.withFormat(Boolean.FALSE, new SimpleDateFormat("dd-MM-yyyy'T'HH:mm:ss.SSS'Z'"));

 Date date = new Date();
 JsonNode result = mapper.writeValueAsTree(date, customFormatterSerializer);
 assertEquals("\"13-03-2023T12:34:56.789Z\"", result.toString());

 // Test deserialization
 Date deserializedDate = mapper.readValue(result.toString(), Date.class, customFormatterSerializer);
 assertEquals(date, deserializedDate);
 }

 @Test
 public void testInstantSerializationWithDefaultSettings() throws JsonProcessingException {
 Instant instant = Instant.now();
 JsonNode result = mapper.writeValueAsTree(instant);
 assertEquals("\"2023-03-13T12:34:56.789Z\"", result.toString());

 // Test deserialization
 Instant deserializedInstant = mapper.readValue(result.toString(), Instant.class);
 assertEquals(instant, deserializedInstant);
 }

 @Test
 public void testInstantSerializationWithCustomFormat() throws JsonGenerationException, JsonMappingException, JsonProcessingException {
 JsonFormat format = new JsonFormat(new DateFormat("dd-MM-yyyy'T'HH:mm:ss.SSS'Z'"));
 DateTimeSerializerBase<Instant> customFormatterSerializer = instantSerializer.withFormat(Boolean.FALSE, new SimpleDateFormat("dd-MM-yyyy'T'HH:mm:ss.SSS'Z'"));

 Instant instant = Instant.now();
 JsonNode result = mapper.writeValueAsTree(instant, customFormatterSerializer);
 assertEquals("\"13-03-2023T12:34:56.789Z\"", result.toString());

 // Test deserialization
 Instant deserializedInstant = mapper.readValue(result.toString(), Instant.class, customFormatterSerializer);
 assertEquals(instant, deserializedInstant);
 }

 @Test
 public void testSerializationWithUseTimestampFlag() throws JsonProcessingException {
 Date date = new Date();
 JsonNode result = mapper.writeValueAsTree(date, dateSerializer.withFormat(Boolean.TRUE, null));
 assertEquals("2023-03-13T12:34:56789", result.toString());

 // Test deserialization
 Date deserializedDate = mapper.readValue(result.toString(), Date.class);
 assertEquals(date, deserializedDate);
 }

 @Test
 public void testSerializationWithNullValue() throws JsonProcessingException {
 JsonNode result = mapper.writeValueAsTree(null, dateSerializer);
 assertEquals("null", result.toString());

 JsonNode deserializedResult = mapper.readTree(result.toString());
 assertNull(deserializedResult.get("@type"));
 }

 @Test
 public void testSerializationWithEmptyList() throws JsonProcessingException {
 JsonNode result = mapper.writeValueAsTree(new ArrayList<>(), new TypeReference<List<Date>>(){});
 assertEquals("[null]", result.toString());
 }

 @Test
 public void testSerializationWithEmptyArray() throws JsonProcessingException {
 Date[] dates = new Date[0];
 JsonNode result = mapper.writeValueAsTree(dates, new TypeReference<Date[]>(){});
 assertEquals("[null]", result.toString());
 }

 @Test(expected = JsonMappingException.class)
 public void testSerializationWithInvalidDate() throws JsonProcessingException {
 // This test should fail, as the provided date is not valid
 Date invalidDate = new Date(Long.MIN_VALUE);
 JsonNode result = mapper.writeValueAsTree(invalidDate, dateSerializer);
 fail();
 }

 @Test(expected = JsonMappingException.class)
 public void testDeserializationWithInvalidJsonString() throws JsonProcessingException {
 String invalidJsonString = "\"invalidJsonString\"";
 try {
 mapper.readValue(new TextNode(invalidJsonString), Date.class);
 fail();
 } catch (JsonMappingException e) {
 // Expected exception
 }
 }
}