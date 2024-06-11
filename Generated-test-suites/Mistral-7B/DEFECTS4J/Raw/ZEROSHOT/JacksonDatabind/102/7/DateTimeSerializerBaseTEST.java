import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationConfig;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationConfig;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.std.StdDateFormat;
import com.fasterxml.jackson.databind.ser.std.DateTimeSerializerBase;
import com.fasterxml.jackson.databind.util.StdDateFormat;

public class DateTimeSerializerBaseTest {

 private SerializerProvider serializers;
 private DeserializationConfig deserializationConfig;
 private ObjectMapper objectMapper;
 private SerializationConfig serializationConfig;

 @Before
 public void setup() {
 serializers = mock(SerializerProvider.class);
 deserializationConfig = mock(DeserializationConfig.class);
 objectMapper = new ObjectMapper();
 serializationConfig = mock(SerializationConfig.class);
 when(deserializationConfig.getLocale()).thenReturn(Locale.US);
 when(deserializationConfig.getTimeZone()).thenReturn(TimeZone.getDefault());
 when(serializers.getConfig()).thenReturn(deserializationConfig);
 when(serializers.isEnabled(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)).thenReturn(false);
 when(serializers.getProvider()).thenReturn(serializationConfig);
 }

 @Test
 public void testDateTimeSerializerBaseConstructor() {
 Class<String> stringType = String.class;
 Boolean useTimestamp = false;
 DateFormat customFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
 DateTimeSerializerBase<String> dateTimeSerializerBase = new DateTimeSerializerBase<String>(stringType, useTimestamp, customFormat);
 assertEquals(stringType, dateTimeSerializerBase.getType());
 assertEquals(useTimestamp, dateTimeSerializerBase._useTimestamp);
 assertEquals(customFormat, dateTimeSerializerBase._customFormat);
 assertEquals(null, dateTimeSerializerBase._reusedCustomFormat);
 }

 @Test
 public void testDateTimeSerializerBaseWithFormat() {
 Class<String> stringType = String.class;
 Boolean useTimestamp = false;
 DateFormat customFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
 DateTimeSerializerBase<String> dateTimeSerializerBase = new DateTimeSerializerBase<String>(stringType, useTimestamp, customFormat);
 DateTimeSerializerBase<String> withFormatSerializer = dateTimeSerializerBase.withFormat(false, customFormat);
 assertEquals(stringType, withFormatSerializer.getType());
 assertEquals(useTimestamp, withFormatSerializer._useTimestamp);
 assertEquals(customFormat, withFormatSerializer._customFormat);
 assertEquals(customFormat, withFormatSerializer._reusedCustomFormat.get());
 }

 @Test
 public void testDateTimeSerializerBaseWithFormatWithNullCustomFormat() {
 Class<String> stringType = String.class;
 Boolean useTimestamp = false;
 DateTimeSerializerBase<String> dateTimeSerializerBase = new DateTimeSerializerBase<String>(stringType, useTimestamp, null);
 DateTimeSerializerBase<String> withFormatSerializer = dateTimeSerializerBase.withFormat(false, null);
 assertEquals(stringType, withFormatSerializer.getType());
 assertEquals(useTimestamp, withFormatSerializer._useTimestamp);
 assertEquals(null, withFormatSerializer._customFormat);
 assertEquals(null, withFormatSerializer._reusedCustomFormat);
 }

 @Test
 public void testDateTimeSerializerBaseCreateContextual() throws IOException {
 Class<Date> dateType = Date.class;
 Boolean useTimestamp = false;
 DateFormat customFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
 DateTimeSerializerBase<Date> dateTimeSerializerBase = new DateTimeSerializerBase<Date>(dateType, useTimestamp, customFormat);
 JsonFormat.Shape shape = JsonFormat.Shape.STRING;
 JsonFormat.Value format = new JsonFormat.Value();
 format.setShape(shape);
 JsonParser jsonParser = mock(JsonParser.class);
 BeanProperty property = mock(BeanProperty.class);
 when(jsonParser.getCodec()).thenReturn(objectMapper.getCodec());
 when(property.getName()).thenReturn("testProperty");
 when(property.getMember()).thenReturn(new AnnotatedMethod(getClass().getEnclosingClass().getDeclaredMethod("testMethod"), new Class[0]));
 when(property.getMember().getAnnotations()).thenReturn(new Annotation[0]);
 when(property.getType()).thenReturn(dateType);
 when(property.getParent()).thenReturn(null);
 when(property.getValue()).thenReturn(new Date(1596441600000L));
 Object testValue = new Object() {
 @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
 Date testDate;
 };
 when(jsonParser.getValueAs(dateType)).thenReturn(testValue);
 when(jsonParser.getSerializerProvider()).thenReturn(serializers);
 JsonSerializer<Date> serializer = dateTimeSerializerBase.createContextual(serializers, property);
 assertEquals(dateTimeSerializerBase.getClass(), serializer.getClass());
 }

 @Test
 public void testDateTimeSerializerBaseIsEmpty() {
 Class<Date> dateType = Date.class;
 DateTimeSerializerBase<Date> dateTimeSerializerBase = new DateTimeSerializerBase<Date>(dateType, false, null);
 Date testValue = null;
 assertFalse(dateTimeSerializerBase.isEmpty(serializers, testValue));
 }

 @Test
 public void testDateTimeSerializerBaseGetSchema() throws IOException {
 Class<Date> dateType = Date.class;
 DateTimeSerializerBase<Date> dateTimeSerializerBase = new DateTimeSerializerBase<Date>(dateType, false, null);
 assertEquals("string", dateTimeSerializerBase.getSchema(serializers, dateType).getClass().getName());
 }

 @Test
 public void testDateTimeSerializerBaseAcceptJsonFormatVisitor() throws IOException {
 Class<Date> dateType = Date.class;
 DateTimeSerializerBase<Date> dateTimeSerializerBase = new DateTimeSerializerBase<Date>(dateType, false, null);
 JsonFormatVisitorWrapper jsonFormatVisitorWrapper = mock(JsonFormatVisitorWrapper.class);
 jsonFormatVisitorWrapper.acceptNull(dateType);
 dateTimeSerializerBase.acceptJsonFormatVisitor(jsonFormatVisitorWrapper, dateType);
 }

 @Test
 public void testDateTimeSerializerBaseSerialize() throws IOException, ParseException {
 Class<Date> dateType = Date.class;
 DateTimeSerializerBase<Date> dateTimeSerializerBase = new DateTimeSerializerBase<Date>(dateType, false, null);
 Date testDate = new Date(1596441600000L);
 JsonGenerator jsonGenerator = mock(JsonGenerator.class);
 SerializerProvider serializerProvider = mock(SerializerProvider.class);
 when(jsonGenerator.writeString("2020-12-27T14:25:00.000Z")).thenReturn(jsonGenerator);
 dateTimeSerializerBase.serialize(testDate, jsonGenerator, serializerProvider);
 assertEquals("2020-12-27T14:25:00.000Z", jsonGenerator.getOutputContent().toString());
 }

 @Test
 public void testDateTimeSerializerBaseSerializeWithCustomFormat() throws IOException, ParseException {
 Class<Date> dateType = Date.class;
 DateTimeSerializerBase<Date> dateTimeSerializerBase = new DateTimeSerializerBase<Date>(dateType, false, new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"));
 Date testDate = new Date(1596441600000L);
 JsonGenerator jsonGenerator = mock(JsonGenerator.class);
 SerializerProvider serializerProvider = mock(SerializerProvider.class);
 when(jsonGenerator.writeString("2020-12-27T14:25:00.000Z")).thenReturn(jsonGenerator);
 dateTimeSerializerBase.serialize(testDate, jsonGenerator, serializerProvider);
 assertEquals("2020-12-27T14:25:00.000Z", jsonGenerator.getOutputContent().toString());
 }

 @Test
 public void testDateTimeSerializerBaseSerializeWithTimestamp() throws IOException, ParseException {
 Class<Date> dateType = Date.class;
 DateTimeSerializerBase<Date> dateTimeSerializerBase = new DateTimeSerializerBase<Date>(dateType, true, null);
 Date testDate = new Date(1596441600000L);
 JsonGenerator jsonGenerator = mock(JsonGenerator.class);
 SerializerProvider serializerProvider = mock(SerializerProvider.class);
 when(jsonGenerator.writeNumber(1596441600000L)).thenReturn(jsonGenerator);
 dateTimeSerializerBase.serialize(testDate, jsonGenerator, serializerProvider);
 assertEquals(1596441600000L, jsonGenerator.getOutputContent().longValue());
 }

 @Test
 public void testDateTimeSerializerBaseSerializeStringFormat() throws IOException, ParseException {
 Class<Date> dateType = Date.class;
 DateTimeSerializerBase<Date> dateTimeSerializerBase = new DateTimeSerializerBase<Date>(dateType, false, null);
 ZonedDateTime testDate = ZonedDateTime.ofInstant(Instant.ofEpochMilli(1596441600000L), ZoneId.systemDefault());
 JsonGenerator jsonGenerator = mock(JsonGenerator.class);
 SerializerProvider serializerProvider = mock(SerializerProvider.class);
 when(jsonGenerator.writeString(testDate.format(StdDateFormat.DEFAULT_DATE_FORMAT))).thenReturn(jsonGenerator);
 dateTimeSerializerBase._serializeAsString(testDate.toDate(), jsonGenerator, serializerProvider);
 assertEquals(testDate.format(StdDateFormat.DEFAULT_DATE_FORMAT), jsonGenerator.getOutputContent().toString());
 }

 @Test
 public void testDateTimeSerializerBaseSerializeStringFormatWithCustomFormat() throws IOException, ParseException {
 Class<Date> dateType = Date.class;
 DateTimeSerializerBase<Date> dateTimeSerializerBase = new DateTimeSerializerBase<Date>(dateType, false, new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"));
 ZonedDateTime testDate = ZonedDateTime.ofInstant(Instant.ofEpochMilli(1596441600000L), ZoneId.systemDefault());
 JsonGenerator jsonGenerator = mock(JsonGenerator.class);
 SerializerProvider serializerProvider = mock(SerializerProvider.class);
 when(jsonGenerator.writeString(testDate.format(_customFormat))).thenReturn(jsonGenerator);
 dateTimeSerializerBase._serializeAsString(testDate.toDate(), jsonGenerator, serializerProvider);
 assertEquals(testDate.format(_customFormat), jsonGenerator.getOutputContent().toString());
 }
}