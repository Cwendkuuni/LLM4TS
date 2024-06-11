import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;

public class DateTimeSerializerBaseTest {

 private DateTimeSerializerBase<Date> dateTimeSerializer;
 private JsonFactory jsonFactory;

 @Before
 public void setUp() throws Exception {
 jsonFactory = new ObjectMapper().getFactory();
 dateTimeSerializer = new DateTimeSerializerBase<Date>(Date.class, false, null);
 }

 @Test
 public void testCreateContextualWithDefaultSettings() throws Exception {
 JsonSerializer<Date> serializer = dateTimeSerializer.createContextual(jsonFactory.getSerializerProvider(), null);
 assertEquals(dateTimeSerializer, serializer);
 }

 @Test
 public void testCreateContextualWithCustomFormat() throws Exception {
 JsonFormat.Value format = new JsonFormat.Value();
 format.setShape(JsonFormat.Shape.STRING);
 format.setPattern("dd-MM-yyyy HH:mm:ss z");
 Locale locale = Locale.ENGLISH;
 TimeZone timeZone = TimeZone.getTimeZone("UTC");
 format.setLocale(locale);
 format.setTimeZone(timeZone);

 JsonSerializer<Date> serializer = dateTimeSerializer.createContextual(jsonFactory.getSerializerProvider(), null);
 assertTrue(serializer instanceof DateTimeSerializerBase);
 DateTimeSerializerBase<Date> customSerializer = (DateTimeSerializerBase<Date>) serializer;
 assertEquals(format.getPattern(), customSerializer._customFormat.toPattern());
 assertEquals(locale, customSerializer._customFormat.getLocale());
 assertEquals(timeZone, customSerializer._customFormat.getTimeZone());
 }

 @Test
 public void testSerializeWithDefaultSettings() throws Exception {
 Date date = new Date();
 JsonGenerator jsonGenerator = jsonFactory.createGenerator();
 dateTimeSerializer.serialize(date, jsonGenerator, jsonFactory.getSerializerProvider());
 JsonParser jsonParser = jsonFactory.createParser(jsonGenerator.getOutputStreamAsString());
 assertEquals(date.toInstant().toEpochMilli(), jsonParser.nextValue().asLong());
 }

 @Test
 public void testSerializeWithCustomFormat() throws Exception {
 DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss z", Locale.ENGLISH);
 dateTimeSerializer = new DateTimeSerializerBase<Date>(Date.class, false, dateFormat);
 Date date = new Date();
 JsonGenerator jsonGenerator = jsonFactory.createGenerator();
 dateTimeSerializer.serialize(date, jsonGenerator, jsonFactory.getSerializerProvider());
 JsonParser jsonParser = jsonFactory.createParser(jsonGenerator.getOutputStreamAsString());
 String expectedValue = dateFormat.format(date);
 assertEquals(expectedValue, jsonParser.nextValue().asText());
 }

 @Test
 public void testSerializeWithTimestampEnabled() throws Exception {
 dateTimeSerializer = new DateTimeSerializerBase<Date>(Date.class, true, null);
 Date date = new Date();
 JsonGenerator jsonGenerator = jsonFactory.createGenerator();
 dateTimeSerializer.serialize(date, jsonGenerator, jsonFactory.getSerializerProvider());
 JsonParser jsonParser = jsonFactory.createParser(jsonGenerator.getOutputStreamAsString());
 assertEquals(date.getTime(), jsonParser.nextValue().asLong());
 }

 @Test
 public void testSerializeWithCustomFormatAndTimestampEnabled() throws Exception {
 DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss z", Locale.ENGLISH);
 dateTimeSerializer = new DateTimeSerializerBase<Date>(Date.class, true, dateFormat);
 Date date = new Date();
 JsonGenerator jsonGenerator = jsonFactory.createGenerator();
 dateTimeSerializer.serialize(date, jsonGenerator, jsonFactory.getSerializerProvider());
 JsonParser jsonParser = jsonFactory.createParser(jsonGenerator.getOutputStreamAsString());
 String expectedValue = dateFormat.format(date);
 assertEquals(Long.parseLong(expectedValue), jsonParser.nextValue().asLong());
 }
}