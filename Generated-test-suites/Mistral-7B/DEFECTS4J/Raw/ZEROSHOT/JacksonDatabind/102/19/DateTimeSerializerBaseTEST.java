import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.junit.SerializerTestsBase;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.ser.std.DateTimeSerializerBase;
import com.fasterxml.jackson.databind.ser.std.DateTimeSerializers;

public class DateTimeSerializerBaseTest extends SerializerTestsBase {

 private DateTimeSerializerBase<Date> dateSerializer;
 private DateTimeSerializerBase<java.sql.Date> sqlDateSerializer;

 @Before
 public void setup() throws Exception {
 dateSerializer = new DateTimeSerializers.DateSerializer(false, null);
 sqlDateSerializer = new DateTimeSerializers.DateSerializer(false, null);
 }

 @Test
 public void testSerializationWithDefaultFormat() throws JsonMappingException, IOException {
 ObjectMapper mapper = new ObjectMapper();
 Date date = new Date();

 // Test serialization to JSON string
 String jsonString = mapper.writeValueAsString(date);
 JsonParser parser = mapper.getFactory().createParser(jsonString);
 Date dst = mapper.readValue(parser, Date.class);
 assertEquals(date, dst);

 // Test serialization to number (timestamp)
 JsonNode jsonNumber = mapper.valueToTree(date);
 assertEquals(JsonNodeType.NUMBER, jsonNumber.getType());
 long timestamp = jsonNumber.longValue();
 Date ds = new Date(timestamp);
 assertEquals(date, ds);
 }

 @Test
 public void testSerializationWithCustomFormat() throws JsonMappingException, IOException {
 ObjectMapper mapper = new ObjectMapper();
 DateFormat customFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss", Locale.US);
 DateTimeSerializerBase<Date> customSerializer = new DateTimeSerializerBase<Date>(Date.class, false, customFormat);

 // Test serialization to JSON string
 String jsonString = mapper.writeValueAsString(dateSerializer, new Date());
 JsonParser parser = mapper.getFactory().createParser(jsonString);
 Date dst = mapper.readValue(parser, Date.class);
 assertEquals(date, dst);

 // Test serialization to JSON string with custom format
 mapper.setDateFormat(customFormat);
 String jsonStringWithFormat = mapper.writeValueAsString(customSerializer, new Date());
 JsonParser parserWithFormat = mapper.getFactory().createParser(jsonStringWithFormat);
 Date dstWithFormat = mapper.readValue(parserWithFormat, Date.class);
 assertEquals(date, dstWithFormat);

 // Test serialization to number (timestamp)
 JsonNode jsonNumberWithFormat = mapper.valueToTree(customSerializer, new Date());
 assertEquals(JsonNodeType.NUMBER, jsonNumberWithFormat.getType());
 long timestampWithFormat = jsonNumberWithFormat.longValue();
 Date dsWithFormat = new Date(timestampWithFormat);
 assertEquals(date, dsWithFormat);
 }

 @Test
 public void testSerializationWithTimeZone() throws JsonMappingException, IOException {
 ObjectMapper mapper = new ObjectMapper();
 DateFormat customFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss z", Locale.US);
 DateTimeSerializerBase<Date> customSerializer = new DateTimeSerializerBase<Date>(Date.class, false, customFormat);

 // Test serialization to JSON string with custom format and timezone
 String jsonStringWithFormatAndTimezone = mapper.writeValueAsString(customSerializer, new Date(System.currentTimeMillis() + (10 * 60 * 60 * 1000)));
 JsonParser parserWithFormatAndTimezone = mapper.getFactory().createParser(jsonStringWithFormatAndTimezone);
 Date dstWithFormatAndTimezone = mapper.readValue(parserWithFormatAndTimezone, Date.class);
 assertEquals(new Date(System.currentTimeMillis() + (10 * 60 * 60 * 1000)), dstWithFormatAndTimezone);
 }

 @Test
 public void testSerializationWithLocale() throws JsonMappingException, IOException {
 ObjectMapper mapper = new ObjectMapper();
 DateFormat customFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss", Locale.FRENCH);
 DateTimeSerializerBase<Date> customSerializer = new DateTimeSerializerBase<Date>(Date.class, false, customFormat);

 // Test serialization to JSON string with custom format and locale
 String jsonStringWithFormatAndLocale = mapper.writeValueAsString(customSerializer, new Date());
 JsonParser parserWithFormatAndLocale = mapper.getFactory().createParser(jsonStringWithFormatAndLocale);
 Date dstWithFormatAndLocale = mapper.readValue(parserWithFormatAndLocale, Date.class);
 assertEquals(date, dstWithFormatAndLocale);
 }

 @Test
 public void testSerializationWithTimestamp() throws JsonMappingException, IOException {
 ObjectMapper mapper = new ObjectMapper();
 DateTimeSerializerBase<Date> timestampSerializer = new DateTimeSerializers.DateSerializer(true, null);

 // Test serialization to number (timestamp)
 JsonNode jsonNumberWithTimestamp = mapper.valueToTree(timestampSerializer, new Date());
 assertEquals(JsonNodeType.NUMBER, jsonNumberWithTimestamp.getType());
 long timestampWithTimestamp = jsonNumberWithTimestamp.longValue();
 Date dsWithTimestamp = new Date(timestampWithTimestamp);
 assertEquals(date, dsWithTimestamp);
 }

 @Test
 public void testSerializationWithCustomFormatAndTimestamp() throws JsonMappingException, IOException {
 ObjectMapper mapper = new ObjectMapper();
 DateFormat customFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss", Locale.US);
 DateTimeSerializerBase<Date> customTimestampSerializer = new DateTimeSerializerBase<Date>(Date.class, true, customFormat);

 // Test serialization to number (timestamp) with custom format
 JsonNode jsonNumberWithFormatAndTimestamp = mapper.valueToTree(customTimestampSerializer, new Date());
 assertEquals(JsonNodeType.NUMBER, jsonNumberWithFormatAndTimestamp.getType());
 long timestampWithFormatAndTimestamp = jsonNumberWithFormatAndTimestamp.longValue();
 Date dsWithFormatAndTimestamp = new Date(timestampWithFormatAndTimestamp);
 assertEquals(date, dsWithFormatAndTimestamp);
 }

 @Test
 public void testSerializationWithCustomFormatAndTimestampAndLocale() throws JsonMappingException, IOException {
 ObjectMapper mapper = new ObjectMapper();
 DateFormat customFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss", Locale.FRENCH);
 DateTimeSerializerBase<Date> customTimestampSerializerWithLocale = new DateTimeSerializerBase<Date>(Date.class, true, customFormat);

 // Test serialization to number (timestamp) with custom format and locale
 JsonNode jsonNumberWithFormatAndLocaleAndTimestamp = mapper.valueToTree(customTimestampSerializerWithLocale, new Date());
 assertEquals(JsonNodeType.NUMBER, jsonNumberWithFormatAndLocaleAndTimestamp.getType());
 long timestampWithFormatAndLocaleAndTimestamp = jsonNumberWithFormatAndLocaleAndTimestamp.longValue();
 Date dsWithFormatAndLocaleAndTimestamp = new Date(timestampWithFormatAndLocaleAndTimestamp);
 assertEquals(date, dsWithFormatAndLocaleAndTimestamp);
 }

 @Test
 public void testSerializationWithCustomFormatAndTimestampAndTimeZone() throws JsonMappingException, IOException {
 ObjectMapper mapper = new ObjectMapper();
 DateFormat customFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss z", Locale.US);
 DateTimeSerializerBase<Date> customTimestampSerializerWithTimeZone = new DateTimeSerializerBase<Date>(Date.class, true, customFormat);

 // Test serialization to number (timestamp) with custom format and timezone
 JsonNode jsonNumberWithFormatAndTimeZoneAndTimestamp = mapper.valueToTree(customTimestampSerializerWithTimeZone, new Date(System.currentTimeMillis() + (10 * 60 * 60 * 1000)));
 assertEquals(JsonNodeType.NUMBER, jsonNumberWithFormatAndTimeZoneAndTimestamp.getType());
 long timestampWithFormatAndTimeZoneAndTimestamp = jsonNumberWithFormatAndTimeZoneAndTimestamp.longValue();
 Date dsWithFormatAndTimeZoneAndTimestamp = new Date(timestampWithFormatAndTimeZoneAndTimestamp);
 assertEquals(new Date(System.currentTimeMillis() + (10 * 60 * 60 * 1000)), dsWithFormatAndTimeZoneAndTimestamp);
 }

 @Test
 public void testSerializationWithJsonFormatAnnotation() throws JsonMappingException, IOException {
 ObjectMapper mapper = new ObjectMapper();

 // Test serialization to JSON string
 @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy HH:mm:ss")
 class TestDate {
 private Date date;

 public Date getDate() {
 return date;
 }

 public void setDate(Date date) {
 this.date = date;
 }
 }
 TestDate testDate = new TestDate();
 testDate.setDate(new Date());
 String jsonStringWithAnnotation = mapper.writeValueAsString(testDate);
 JsonParser parserWithAnnotation = mapper.getFactory().createParser(jsonStringWithAnnotation);
 TestDate dstWithAnnotation = mapper.readValue(parserWithAnnotation, TestDate.class);
 assertEquals(testDate.getDate(), dstWithAnnotation.getDate());

 // Test serialization to number (timestamp)
 JsonNode jsonNumberWithAnnotation = mapper.valueToTree(testDate);
 assertEquals(JsonNodeType.OBJECT, jsonNumberWithAnnotation.getType());
 ObjectNode jsonObjectWithAnnotation = (ObjectNode) jsonNumberWithAnnotation;
 JsonNode jsonNumberWithAnnotationTimestamp = jsonObjectWithAnnotation.get("date");
 assertEquals(JsonNodeType.NUMBER, jsonNumberWithAnnotationTimestamp.getType());
 long timestampWithAnnotation = jsonNumberWithAnnotationTimestamp.longValue();
 Date dsWithAnnotation = new Date(timestampWithAnnotation);
 assertEquals(testDate.getDate(), dsWithAnnotation);
 }
}