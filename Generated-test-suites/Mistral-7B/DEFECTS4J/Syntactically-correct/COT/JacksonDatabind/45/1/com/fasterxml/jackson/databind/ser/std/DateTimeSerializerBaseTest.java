package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.*;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.introspect.AnnotatedClass;
import com.fasterxml.jackson.databind.introspect.BeanProperty;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitorWrapper;
import com.fasterxml.jackson.databind.node.JsonNode;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import com.fasterxml.jackson.databind.ser.std.DateTimeSerializerBase;
import com.fasterxml.jackson.databind.util.StdDateFormat;
import com.fasterxml.jackson.test.BaseTest;
import com.fasterxml.jackson.test.jsonschema.JSONSchema;

@RunWith(BaseTest.Parametrized.class)
public class DateTimeSerializerBaseTest<T> extends BaseTest {

 private Class<T> type;
 private DateTimeSerializerBase<T> serializer;

 @Parameters(name = "{0}")
 public static Iterable<Object[]> data() {
 return Arrays.asList(new Object[][]{
 new Object[]{Date.class},
 new Object[]{LocalDate.class},
 new Object[]{LocalDateTime.class},
 new Object[]{OffsetDateTime.class},
 new Object[]{ZonedDateTime.class}
 });
 }

 @Before
 public void setUp() throws Exception {
 type = (Class<T>) getClass().getParameterizedType();
 serializer = (DateTimeSerializerBase<T>) new DateTimeSerializerBase<T>(type, null, null);
 }

 @Test
 public void testCreateContextual() throws Exception {
 ObjectMapper mapper = new ObjectMapper();
 BeanProperty property = Mockito.mock(BeanProperty.class);
 JsonFormat.Value format = new JsonFormat.Value();
 AnnotatedClass annotatedClass = Mockito.mock(AnnotatedClass.class);
 when(annotatedClass.getAnnotation(JsonFormat.class)).thenReturn(format);
 when(property.getMember()).thenReturn(annotatedClass);
 JsonFormatVisitorWrapper visitor = Mockito.mock(JsonFormatVisitorWrapper.class);
 SerializerProvider serializers = mapper.getSerializerProvider();
 JsonSerializer<T> contextualSerializer = serializer.createContextual(serializers, property);
 assertTrue(contextualSerializer instanceof DateTimeSerializerBase);
 assertSame(serializer.getClass(), contextualSerializer.getClass());

 // Test serializer with null property
 assertNull(serializer.createContextual(serializers, null));
 }

 @Test
 public void testSerializationWithFormat() throws Exception {
 ObjectMapper mapper = new ObjectMapper();
 Object value = new Date(0L);
 SimpleDateFormat customFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss z");
 DateTimeSerializerBase<Date> dateTimeSerializer = new DateTimeSerializerBase<Date>(Date.class, null, customFormat);
 JsonGenerator gen = mapper.getFactory().createGenerator(new ByteArrayOutputStream(), mapper.getSerializerProvider());
 dateTimeSerializer.serialize(value, gen, mapper.getSerializerProvider());
 String actualOutput = gen.getOutputContent();
 String expectedOutput = "2000-01-01 00:00:00 +0000";
 assertEquals(expectedOutput, actualOutput);
}

 @Test
 public void testSerializationWithDefaultFormat() throws Exception {
 ObjectMapper mapper = new ObjectMapper();
 Object value = new Date(0L);
 DateTimeSerializerBase<Date> dateTimeSerializer = new DateTimeSerializerBase<Date>(Date.class, null, null);
 JsonGenerator gen = mapper.getFactory().createGenerator(new ByteArrayOutputStream(), mapper.getSerializerProvider());
 dateTimeSerializer.serialize(value, gen, mapper.getSerializerProvider());
 String actualOutput = gen.getOutputContent();
 String expectedOutput = "0";
 assertEquals(expectedOutput, actualOutput);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSerializationWithNullProvider() throws Exception {
 DateTimeSerializerBase<Date> dateTimeSerializer = new DateTimeSerializerBase<Date>(Date.class, null, null);
 dateTimeSerializer.serialize(new Date(0L), null, null);
 }

 @Test
 public void testSerializationWithUseTimestamp() throws Exception {
 ObjectMapper mapper = new ObjectMapper();
 Object value = new Date(0L);
 DateTimeSerializerBase<Date> dateTimeSerializer = new DateTimeSerializerBase<Date>(Date.class, true, null);
 JsonGenerator gen = mapper.getFactory().createGenerator(new ByteArrayOutputStream(), mapper.getSerializerProvider());
 dateTimeSerializer.serialize(value, gen, mapper.getSerializerProvider());
 String actualOutput = gen.getOutputContent();
 String expectedOutput = "0";
 assertEquals(expectedOutput, actualOutput);
}

 @Test
 public void testSerializationWithEmptyValue() throws Exception {
 ObjectMapper mapper = new ObjectMapper();
 DateTimeSerializerBase<Date> dateTimeSerializer = new DateTimeSerializerBase<Date>(Date.class, null, null);
 JsonGenerator gen = mapper.getFactory().createGenerator(new ByteArrayOutputStream(), mapper.getSerializerProvider());
 dateTimeSerializer.serialize(null, gen, mapper.getSerializerProvider());
 String actualOutput = gen.getOutputContent();
 String expectedOutput = "null";
 assertEquals(expectedOutput, actualOutput);
}

 @Test
 public void testSerializationWithCustomLocaleAndTimeZone() throws Exception {
 ObjectMapper mapper = new ObjectMapper();
 Object value = new Date(0L);
 SimpleDateFormat customFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss 'at' zzz");
 DateTimeSerializerBase<Date> dateTimeSerializer = new DateTimeSerializerBase<Date>(Date.class, null, customFormat);
 Locale locale = Locale.US;
 TimeZone timeZone = TimeZone.getTimeZone("UTC");
 JsonGenerator gen = mapper.getFactory().createGenerator(new ByteArrayOutputStream(), mapper.getSerializerProvider());
 gen.setLocale(locale);
 gen.setTimeZone(timeZone);
 dateTimeSerializer.serialize(value, gen, mapper.getSerializerProvider());
 String actualOutput = gen.getOutputContent();
 String expectedOutput = "2000-01-01 HH:mm:ss at UTC";
 assertEquals(expectedOutput, actualOutput);
}

 @Test
 public void testSerializationWithFormatShapeNumeric() throws Exception {
 ObjectMapper mapper = new ObjectMapper();
 Object value = new Date(0L);
 DateTimeSerializerBase<Date> dateTimeSerializer = new DateTimeSerializerBase<Date>(Date.class, true, null);
 JsonFormatVisitorWrapper visitor = Mockito.mock(JsonFormatVisitorWrapper.class);
 visitor.expects(Mockito.any())
 .method("visitIntFormat")
 .withArgument(Mockito.eq(type))
 .withArgument(Mockito.eq(JsonParser.NumberType.LONG))
 .withArgument(Mockito.eq(JsonValueFormat.UTC_MILLISEC))
 .doNothing();
 JsonSerializer<T> contextualSerializer = serializer.createContextual(mapper.getSerializerProvider(), Mockito.mock(BeanProperty.class));
 JsonGenerator gen = mapper.getFactory().createGenerator(new ByteArrayOutputStream(), mapper.getSerializerProvider());
 contextualSerializer.serialize(value, gen, mapper.getSerializerProvider());
 String actualOutput = gen.getOutputContent();
 String expectedOutput = "0";
 assertEquals(expectedOutput, actualOutput);
}

 @Test
 public void testSerializationWithFormatShapeString() throws Exception {
 ObjectMapper mapper = new ObjectMapper();
 Object value = new Date(0L);
 DateTimeSerializerBase<Date> dateTimeSerializer = new DateTimeSerializerBase<Date>(Date.class, false, null);
 JsonFormatVisitorWrapper visitor = Mockito.mock(JsonFormatVisitorWrapper.class);
 visitor.expects(Mockito.any())
 .method("visitStringFormat")
 .withArgument(Mockito.eq(type))
 .withArgument(Mockito.eq(JsonValueFormat.DATE_TIME))
 .doNothing();
 JsonSerializer<T> contextualSerializer = serializer.createContextual(mapper.getSerializerProvider(), Mockito.mock(BeanProperty.class));
 JsonGenerator gen = mapper.getFactory().createGenerator(new ByteArrayOutputStream(), mapper.getSerializerProvider());
 contextualSerializer.serialize(value, gen, mapper.getSerializerProvider());
 String actualOutput = gen.getOutputContent();
 String expectedOutput = "\"2000-01-01T00:00:00.000Z\"";
 assertEquals(expectedOutput, actualOutput);
}

 @Test
 public void testGetSchema() throws Exception {
 ObjectMapper mapper = new ObjectMapper();
 JsonNode schema = serializer.getSchema(mapper.getSerializerProvider(), type);
 assertEquals("string", schema.getClass().getName());
 assertEquals("2000-01-01T00:00:00.000Z", schema.textValue());
}

}