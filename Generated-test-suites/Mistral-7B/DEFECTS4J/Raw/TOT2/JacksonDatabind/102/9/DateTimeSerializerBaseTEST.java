import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.junit.jupiter.api.*;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

@TestInstancetype(DateTimeSerializerBase.class)
public class DateTimeSerializerBaseTest {

 @BeforeEach
 void setup() {
 ObjectMapper objectMapper = new ObjectMapper();
 }

 @Test
 void testConstructor() {
 // Test default constructor
 DateTimeSerializerBase<Date> dateTimeSerializerBase = new DateTimeSerializerBase<>(Date.class, null, null);
 assertNotNull(dateTimeSerializerBase);
 }

 @Test
 void testWithFormat() {
 DateTimeSerializerBase<Date> dateTimeSerializerBase = new DateTimeSerializerBase<>(Date.class, false, null);
 DateTimeSerializerBase<Date> customFormatSerializer = dateTimeSerializerBase.withFormat(false, new SimpleDateFormat("dd/MM/yyyy HH:mm:ss"));
 assertNotNull(customFormatSerializer);
 }

 @Test
 void testCreateContextual() throws JsonMappingException, IOException {
 // Test serialization with default settings
 DateTimeSerializerBase<Date> dateTimeSerializerBase = new DateTimeSerializerBase<>(Date.class, false, null);
 JsonSerializer<?> serializer = dateTimeSerializerBase.createContextual(null, null);
 Date date = new Date();
 String json = "\"2022-12-31T23:59:59.000Z\""; // A valid ISO-8601 formatted date-time string

 ObjectMapper objectMapper = new ObjectMapper();
 JsonGenerator gen = objectMapper.getFactory().createGenerator(System.out);
 serializer.serialize(date, gen, null);
 gen.flush(); // Flush buffer
 assertEquals(json, gen.getOutputContent().toString());

 // Test serialization with custom format
 DateTimeSerializerBase<Date> customFormatSerializer = new DateTimeSerializerBase<>(Date.class, false, new SimpleDateFormat("dd/MM/yyyy HH:mm:ss"));
 JsonSerializer<?> customSerializer = customFormatSerializer.createContextual(null, null);
 serializer = customSerializer;
 date = new Date();
 gen = objectMapper.getFactory().createGenerator(System.out);
 serializer.serialize(date, gen, null);
 gen.flush(); // Flush buffer
 assertEquals("31/12/2022 23:59:59", gen.getOutputContent().toString());
 }

 @Test
 void testIsEmpty() {
 DateTimeSerializerBase<Date> dateTimeSerializerBase = new DateTimeSerializerBase<>(Date.class, false, null);
 Date date = new Date();
 assertFalse(dateTimeSerializerBase.isEmpty(null, date));
 Date emptyDate = null;
 assertTrue(dateTimeSerializerBase.isEmpty(null, emptyDate));
 }

 @Test
 void testGetSchema() {
 DateTimeSerializerBase<Date> dateTimeSerializerBase = new DateTimeSerializerBase<>(Date.class, false, null);
 JsonNode schema = dateTimeSerializerBase.getSchema(null, null);
 assertEquals("string", schema.getType().getClass().getSimpleName());
 }

 @Test
 void testAcceptJsonFormatVisitor() throws JsonMappingException {
 DateTimeSerializerBase<Date> dateTimeSerializerBase = new DateTimeSerializerBase<>(Date.class, false, null);
 JsonFormatVisitorWrapper visitor = new JsonFormatVisitorWrapper(null, null, null);
 dateTimeSerializerBase.acceptJsonFormatVisitor(visitor, null);
 assertEquals("date-time", visitor.getFormatName());
 }

 @Test
 void testSerialize() throws JsonGenerationException, JsonMappingException, IOException {
 DateTimeSerializerBase<Date> dateTimeSerializerBase = new DateTimeSerializerBase<>(Date.class, false, null);
 ObjectMapper objectMapper = new ObjectMapper();
 SerializerProvider serializers = objectMapper.getSerializerProvider();
 Date date = new Date();
 String json = "\"2022-12-31T23:59:59.000Z\""; // A valid ISO-8601 formatted date-time string
 String expectedJson = "\"2022-12-31T23:59:59.000Z\"";
 JsonGenerator gen = objectMapper.getFactory().createGenerator(System.out);
 dateTimeSerializerBase.serialize(date, gen, serializers);
 gen.flush(); // Flush buffer
 assertEquals(expectedJson, gen.getOutputContent().toString());
 }

 @Test
 void testSerializeAsString() throws JsonGenerationException, JsonMappingException, IOException {
 DateTimeSerializerBase<Date> dateTimeSerializerBase = new DateTimeSerializerBase<>(Date.class, false, null);
 ObjectMapper objectMapper = new ObjectMapper();
 SerializerProvider serializers = objectMapper.getSerializerProvider();
 Date date = new Date();
 String json = "\"dd/MM/yyyy HH:mm:ss\""; // A custom format string
 String customFormat = "25/12/2022 23:59:59";
 String expectedJson = customFormat;
 Date customDate = new SimpleDateFormat(json.substring(1, json.length()-1)).parse(customFormat);
 JsonGenerator gen = objectMapper.getFactory().createGenerator(System.out);
 dateTimeSerializerBase._serializeAsString(date, gen, serializers);
 gen.flush(); // Flush buffer
 assertEquals(expectedJson, gen.getOutputContent().toString());
 }

 @Test
 void testSerializeInvalidCustomFormat() {
 DateTimeSerializerBase<Date> dateTimeSerializerBase = new DateTimeSerializerBase<>(Date.class, false, null);
 ObjectMapper objectMapper = new ObjectMapper();
 SerializerProvider serializers = objectMapper.getSerializerProvider();
 Date date = new Date();
 assertThrows(JsonProcessingException.class, () -> {
 dateTimeSerializerBase._serializeAsString(date, null, serializers);
 });
 }

 @Test
 void testSerializeNullDate() {
 DateTimeSerializerBase<Date> dateTimeSerializerBase = new DateTimeSerializerBase<>(Date.class, false, null);
 ObjectMapper objectMapper = new ObjectMapper();
 SerializerProvider serializers = objectMapper.getSerializerProvider();
 JsonGenerator gen = objectMapper.getFactory().createGenerator(System.out);
 dateTimeSerializerBase.serialize(null, gen, serializers);
 gen.flush(); // Flush buffer
 assertTrue(gen.getOutputContent().toString().isEmpty());
 }

 @Test
 void testSerializeEmptyDate() {
 DateTimeSerializerBase<Date> dateTimeSerializerBase = new DateTimeSerializerBase<>(Date.class, false, null);
 ObjectMapper objectMapper = new ObjectMapper();
 SerializerProvider serializers = objectMapper.getSerializerProvider();
 JsonGenerator gen = objectMapper.getFactory().createGenerator(System.out);
 Date emptyDate = new Date(0);
 dateTimeSerializerBase.serialize(emptyDate, gen, serializers);
 gen.flush(); // Flush buffer
 assertTrue(gen.getOutputContent().toString().isEmpty());
 }

 @Test
 void testSerializeLocaleAndTimeZone() throws JsonGenerationException, JsonMappingException, IOException {
 DateTimeSerializerBase<ZonedDateTime> dateTimeSerializerBase = new DateTimeSerializerBase<>(ZonedDateTime.class, false, null);
 ObjectMapper objectMapper = new ObjectMapper();
 SerializerProvider serializers = objectMapper.getSerializerProvider();
 ZonedDateTime zonedDateTime = ZonedDateTime.now();
 String json = "\"2022-12-31T23:59:59+02:00\""; // A valid ISO-8601 with timezone formatted date-time string
 String expectedJson = "\"2022-12-31T23:59:59+02:00\"";
 JsonGenerator gen = objectMapper.getFactory().createGenerator(System.out);
 DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
 dateFormat.setTimeZone(TimeZone.getTimeZone("Europe/Madrid"));
 dateFormat.setLocale(Locale.forLanguageTag("es"));
 dateTimeSerializerBase._serializeAsString(Date.from(zonedDateTime.atZone(ZoneId.systemDefault()).toInstant()), gen, serializers);
 gen.flush(); // Flush buffer
 assertEquals(expectedJson, gen.getOutputContent().toString());
 }
}