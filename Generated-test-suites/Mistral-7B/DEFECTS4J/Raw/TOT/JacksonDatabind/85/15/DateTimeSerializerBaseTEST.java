package com.fasterxml.jackson.databind.ser.std;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class DateTimeSerializerBaseTest {

 private static final ObjectMapper mapper = new ObjectMapper();

 @Test
 public void testDateTimeSerializerBaseWithDefaultSettings() throws JsonGenerationException, IOException {
 DateTimeSerializerBase<Long> serializer = mapper.findValueSerializer(Long.class);
 Long dateInMillis = 1620525420000L; // Fri, 13 Aug 2021 03:40:20 GMT

 // Test serialization
 JsonGenerator gen = mapper.getPolymorphicSerializerProvider().getGenerator();
 ObjectWriter writer = mapper.writerWithRootValueSerializer(serializer);
 String actual = writer.writeValueAsString(dateInMillis);
 String expected = "1620525420000";
 assertEquals(expected, actual);

 // Test deserialization
 JsonParser parser = mapper.getFactory().createParser(mapper.writeValueAsString(dateInMillis));
 Long deserialized = mapper.readValue(parser, Long.class);
 assertEquals(dateInMillis, deserialized.longValue());
 }

 @Test
 public void testDateTimeSerializerBaseWithCustomFormat() throws JsonGenerationException, IOException {
 DateTimeSerializerBase<LocalDate> serializer = mapper.findValueSerializer(LocalDate.class);
 LocalDate date = LocalDate.of(2021, 8, 13);

 // Test serialization with custom format
 JsonGenerator gen = mapper.getPolymorphicSerializerProvider().getGenerator();
 ObjectWriter writer = mapper.writerWithRootValueSerializer(serializer.withFormat(Boolean.FALSE, new SimpleDateFormat("dd-MM-yyyy")));
 String actual = writer.writeValueAsString(date);
 String expected = "13-08-2021";
 assertEquals(expected, actual);

 // Test deserialization with custom format
 JsonParser parser = mapper.getFactory().createParser(mapper.writeValueAsString(expected));
 LocalDate deserialized = mapper.readValue(parser, LocalDate.class);
 assertEquals(date, deserialized);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testDateTimeSerializerBaseWithNullProvider() throws JsonGenerationException, IOException {
 DateTimeSerializerBase<LocalDateTime> serializer = mapper.findValueSerializer(LocalDateTime.class);
 ObjectProvider nullProvider = null;
 ObjectWriter writer = mapper.writerWithRootValueSerializer(serializer.withFormat(null, null).withProvider(nullProvider));
 writer.writeValueAsString(LocalDateTime.now());
 fail("Expected IllegalArgumentException");
 }
}