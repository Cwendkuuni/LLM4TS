package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.DateTimeSerializerBase;
import com.fasterxml.jackson.databind.type.TypeFactory;

public class DateTimeSerializerBaseTest {

 private DateTimeSerializerBase<Date> dateTimeSerializer;
 private DateTimeSerializerBase<Long> longSerializer;
 private ObjectMapper mapper;

 @Before
 public void setup() throws Exception {
 mapper = new ObjectMapper();
 dateTimeSerializer = new BaseDateSerializer(Date.class, false, null);
 longSerializer = new BaseLongSerializer(Long.class, false, null);
 }

 @Test
 public void testSerializationAsDate() throws Exception {
 Date date = new Date();
 String expectedDateString = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").format(date);

 JsonGenerator gen = mapper.getPolymorphicSerializer(dateTimeSerializer).getGenerator(mapper, null, null, null);
 mapper.writeValue(gen, date);
 gen.flush();
 gen.close();

 String actualDateString = gen.getOutputContentAsString();
 assertEquals("Actual and expected date strings do not match", expectedDateString, actualDateString);
 }

 @Test
 public void testSerializationAsLong() throws Exception {
 Long timestamp = 1633001235000L;
 String expectedTimestampString = "" + timestamp;

 JsonGenerator gen = mapper.getPolymorphicSerializer(longSerializer).getGenerator(mapper, null, null, null);
 mapper.writeValue(gen, timestamp);
 gen.flush();
 gen.close();

 String actualTimestampString = gen.getOutputContentAsString();
 assertEquals("Actual and expected timestamp strings do not match", expectedTimestampString, actualTimestampString);
 }

 @Test
 public void testSerializationAsDateWithCustomFormat() throws Exception {
 Date date = new Date();
 SimpleDateFormat customFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
 dateTimeSerializer = new BaseDateSerializer(Date.class, false, customFormat);

 String expectedDateString = customFormat.format(date);

 JsonGenerator gen = mapper.getPolymorphicSerializer(dateTimeSerializer).getGenerator(mapper, null, null, null);
 mapper.writeValue(gen, date);
 gen.flush();
 gen.close();

 String actualDateString = gen.getOutputContentAsString();
 assertEquals("Actual and expected date strings do not match", expectedDateString, actualDateString);
 }

 @Test
 public void testSerializationAsLongWithCustomFormat() throws Exception {
 Long timestamp = 1633001235000L;
 DateTimeSerializerBase<Long> customLongSerializer = new BaseLongSerializer(Long.class, true, null);

 String expectedTimestampString = "" + timestamp;

 JsonGenerator gen = mapper.getPolymorphicSerializer(customLongSerializer).getGenerator(mapper, null, null, null);
 mapper.writeValue(gen, timestamp);
 gen.flush();
 gen.close();

 String actualTimestampString = gen.getOutputContentAsString();
 assertEquals("Actual and expected timestamp strings do not match", expectedTimestampString, actualTimestampString);
 }

 @Test
 public void testDeserializationAsDate() throws Exception {
 String dateString = "2023-03-15T14:30:51.123Z";
 Date expectedDate = new Date();
 SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
 expectedDate = dateFormat.parse(dateString);

 JsonParser p = mapper.getFactory().createParser(dateString);

 JsonNode result = mapper.readTree(p);
 assertNotNull("Null result from deserialization", result);

 Date actualDate = mapper.readValue(result.toString(), Date.class);
 assertEquals("Actual and expected dates do not match", expectedDate, actualDate);
 }

 @Test
 public void testDeserializationAsLong() throws Exception {
 String timestampString = "1633001235000";
 Long expectedTimestamp = 1633001235000L;

 JsonParser p = mapper.getFactory().createParser(timestampString);

 JsonNode result = mapper.readTree(p);
 assertNotNull("Null result from deserialization", result);

 Long actualTimestamp = mapper.readValue(result.toString(), Long.class);
 assertEquals("Actual and expected timestamps do not match", expectedTimestamp, actualTimestamp);
 }

 @Test
 public void testDeserializationAsDateWithCustomFormat() throws Exception {
 String dateString = "15-03-2023 14:30:51";
 SimpleDateFormat customFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
 Date expectedDate = new Date();
 expectedDate = customFormat.parse(dateString);

 JsonParser p = mapper.getFactory().createParser(dateString);

 JsonNode result = mapper.readTree(p);
 assertNotNull("Null result from deserialization", result);

 Date actualDate = mapper.readValue(result.toString(), Date.class, customFormat);
 assertEquals("Actual and expected dates do not match", expectedDate, actualDate);
 }

 @Test
 public void testDeserializationAsLongWithCustomFormat() throws Exception {
 String timestampString = "1633001235000";
 DateTimeSerializerBase<Long> customLongSerializer = new BaseLongSerializer(Long.class, true, null);
 mapper.setDefaultSerializerProvider(mapper.getDefaultSerializerProvider().withSerializerInstance(CustomLongSerializer.class, customLongSerializer));

 Long expectedTimestamp = 1633001235000L;

 JsonParser p = mapper.getFactory().createParser(timestampString);

 JsonNode result = mapper.readTree(p);
 assertNotNull("Null result from deserialization", result);

 long actualTimestamp = mapper.readValue(result.toString(), Long.class);
 assertEquals("Actual and expected timestamps do not match", expectedTimestamp, actualTimestamp);
 }

 @JsonSerialize(using = DateTimeSerializerBase.class)
 static class CustomDate {
 private Date date;

 public CustomDate() {
 }

 public Date getDate() {
 return date;
 }

 public void setDate(Date date) {
 this.date = date;
 }
 }

 @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy HH:mm:ss")
 static class CustomLongSerializer extends JsonSerializer<Long> {
 @Override
 public void serialize(Long value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
 gen.writeStringValue(String.valueOf(value));
 }

 @Override
 public Long deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {
 return p.getValueAsLong();
 }
 }

 @Test
 public void testDeserializationWithCustomJsonSerializer() throws Exception {
 String dateString = "15-03-2023 14:30:51";
 CustomDate customDate = new CustomDate();

 JsonParser p = mapper.getFactory().createParser(dateString);

 mapper.readValue(p, CustomDate.class);

 Date expectedDate = new Date();
 SimpleDateFormat customFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
 expectedDate = customFormat.parse(dateString);

 assertEquals("Deserialized CustomDate object does not have the correct date", expectedDate, customDate.getDate());
 }
}

class BaseDateSerializer extends DateTimeSerializerBase<Date> {
 protected BaseDateSerializer(Class<Date> handledType, Boolean useTimestamp, DateFormat customFormat) {
 super(handledType, useTimestamp, customFormat);
 }

 @Override
 protected long _timestamp(Date value) {
 return value.getTime();
 }
}

class BaseLongSerializer extends DateTimeSerializerBase<Long> {
 protected BaseLongSerializer(Class<Long> handledType, Boolean useTimestamp, DateFormat customFormat) {
 super(handledType, useTimestamp, customFormat);
 }

 @Override
 protected long _timestamp(Long value) {
 return value;
 }
}