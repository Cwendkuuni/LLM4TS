package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.*;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.TimeZone;

import org.jmock.lib.legacy.ClassImposteriser;
import org.jmock.lib.legacy.MockObjectTestCase;
import org.jmock.lib.legacy.Mock;
import org.jmock.lib.legacy.Test;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.introspect.Annotated;
import com.fasterxml.jackson.databind.introspect.BasicBeanDescription;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitorWrapper;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import com.fasterxml.jackson.databind.ser.std.DateTimeSerializerBase;
import com.fasterxml.jackson.databind.util.StdDateFormat;

public class DateTimeSerializerBaseTest extends MockObjectTestCase {

	private Class<Long> longType;
	private Class<String> stringType;
	private JsonGenerator mockJsonGenerator;
	private JsonParser mockJsonParser;
	private JsonSerializerProvider mockProvider;
	private BasicBeanDescription mockBeanDescription;
	private Annotated mockAnnotated;
	private JsonFormat mockJsonFormat;
	private Locale locale;
	private TimeZone timeZone;
	private SimpleDateFormat mockDateFormat;

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Test
	public void testCreateContextual() throws Exception {
		DateTimeSerializerBase<Long> serializer = new DateTimeSerializerBase<Long>(Long.TYPE, Boolean.TRUE, null);
		assertEquals(serializer.getClass(), serializer.createContextual(mockProvider, null).getClass());

		serializer = new DateTimeSerializerBase<String>(String.class, Boolean.FALSE, null);
		JsonFormat format = new JsonFormat();
		JsonFormat.Shape shape = JsonFormat.Shape.STRING;
		format.setShape(shape);
		mockAnnotated = mock(Annotated.class);
		when(mockAnnotated.getMember()).thenReturn(null);
		when(mockAnnotated.getValue()).thenReturn(format);
		when(mockProvider.getAnnotationIntrospector().findFormat(mockAnnotated)).thenReturn(format);
		assertEquals(serializer.getClass(), serializer.createContextual(mockProvider, mockBeanDescription).getClass());

		serializer = new DateTimeSerializerBase<String>(String.class, Boolean.FALSE, null);
		locale = MockObjectTestCase.mock(Locale.class);
		timeZone = MockObjectTestCase.mock(TimeZone.class);
		mockDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
		mockDateFormat.setLocale(locale);
		mockDateFormat.setTimeZone(timeZone);
		format = new JsonFormat();
		format.setLocale(locale);
		format.setTimeZone(timeZone);
		format.setPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
		mockAnnotated = mock(Annotated.class);
		when(mockAnnotated.getMember()).thenReturn(null);
		when(mockAnnotated.getValue()).thenReturn(format);
		when(mockProvider.getAnnotationIntrospector().findFormat(mockAnnotated)).thenReturn(format);
		assertEquals(serializer.getClass(), serializer.createContextual(mockProvider, mockBeanDescription).getClass());
		assertEquals(mockDateFormat, serializer.createContextual(mockProvider, mockBeanDescription)._customFormat);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Test
	public void testSerialize() throws Exception {
		DateTimeSerializerBase<Long> serializer = new DateTimeSerializerBase<Long>(Long.TYPE, Boolean.TRUE, null);

		mockJsonGenerator = mock(JsonGenerator.class);
		mockJsonParser = mock(JsonParser.class);
		mockProvider = mock(JsonSerializerProvider.class);
		JsonParser.NumberType numberType = JsonParser.NumberType.LONG;
		long value = 1234567890123L;
		when(mockJsonGenerator.getSerializerProvider()).thenReturn(mockProvider);
		when(mockJsonGenerator.writeNumber(eq(numberType), anyLong())).thenReturn(mockJsonGenerator);
		when(mockProvider.findValueSerializer(Long.TYPE)).thenReturn(serializer);
		serializer.serialize(value, mockJsonGenerator, mockProvider);
		assertNull(mockJsonGenerator._currentValue);
		assertEquals(1234567890123L, mockJsonGenerator.getCurrentValueAsLong());

		serializer = new DateTimeSerializerBase<String>(String.class, Boolean.FALSE, null);
		locale = MockObjectTestCase.mock(Locale.class);
		timeZone = MockObjectTestCase.mock(TimeZone.class);
		mockDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
		mockDateFormat.setLocale(locale);
		mockDateFormat.setTimeZone(timeZone);
		String valueString = "2015-12-31T23:59:59.999Z";
		mockJsonGenerator = mock(JsonGenerator.class);
		when(mockJsonGenerator.getSerializerProvider()).thenReturn(mockProvider);
		when(mockJsonGenerator.writeString(anyString())).thenReturn(mockJsonGenerator);
		when(mockJsonGenerator.writeRawValue(any(byte[].class))).thenReturn(mockJsonGenerator);
		when(mockProvider.findValueSerializer(String.class)).thenReturn(serializer);
		serializer.serialize(valueString, mockJsonGenerator, mockProvider);
		assertNull(mockJsonGenerator._currentValue);
		assertEquals(valueString, mockJsonGenerator.getCurrentValueAsString());
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Test
	public void testAcceptJsonFormatVisitor() throws Exception {
		DateTimeSerializerBase<Long> serializer = new DateTimeSerializerBase<Long>(Long.TYPE, Boolean.TRUE, null);
		mockJsonFormatVisitorWrapper = mock(JsonFormatVisitorWrapper.class);
		mockProvider = mock(JsonSerializerProvider.class);
		assertEquals(serializer.getClass(), serializer.acceptJsonFormatVisitor(mockJsonFormatVisitorWrapper, null).getClass());

		serializer = new DateTimeSerializerBase<String>(String.class, Boolean.FALSE, null);
		Locale locale = MockObjectTestCase.mock(Locale.class);
		TimeZone timeZone = MockObjectTestCase.mock(TimeZone.class);
		mockJsonFormatVisitorWrapper = mock(JsonFormatVisitorWrapper.class, ClassImposteriser.getInstance());
		when(mockJsonFormatVisitorWrapper.getProvider()).thenReturn(mockProvider);
		when(mockJsonFormatVisitorWrapper.getProvider().isEnabled(eq(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS))).thenReturn(true);
		assertEquals(serializer.getClass(), serializer.acceptJsonFormatVisitor(mockJsonFormatVisitorWrapper, null).getClass());

		serializer = new DateTimeSerializerBase<String>(String.class, Boolean.FALSE, null);
		locale = MockObjectTestCase.mock(Locale.class);
		timeZone = MockObjectTestCase.mock(TimeZone.class);
		mockJsonFormatVisitorWrapper = mock(JsonFormatVisitorWrapper.class, ClassImposteriser.getInstance());
		when(mockJsonFormatVisitorWrapper.getProvider()).thenReturn(mockProvider);
		when(mockJsonFormatVisitorWrapper.getProvider().isEnabled(eq(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS))).thenReturn(false);
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
		format.setLocale(locale);
		format.setTimeZone(timeZone);
		when(mockJsonFormatVisitorWrapper.getFormatVisitor(eq(JavaType.construct(String[].class, null)))).thenReturn(format);
		assertEquals(serializer.getClass(), serializer.acceptJsonFormatVisitor(mockJsonFormatVisitorWrapper, null).getClass());
		assertEquals(format, serializer.acceptJsonFormatVisitor(mockJsonFormatVisitorWrapper, null)._customFormat);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Test
	public void testGetSchema() throws Exception {
		DateTimeSerializerBase<Long> serializer = new DateTimeSerializerBase<Long>(Long.TYPE, Boolean.TRUE, null);
		JsonNode schema = serializer.getSchema(mockProvider, Long.TYPE);
		assertEquals("number", schema.getClass().getName());
		assertEquals(true, schema.getBoolean("type"));
		assertEquals("number", schema.get("properties").get(serializer.handledType().getName()).get("type").asText());
		assertTrue(schema.get("properties").get(serializer.handledType().getName()).get("items") instanceof JsonNull);

		serializer = new DateTimeSerializerBase<String>(String.class, Boolean.FALSE, null);
		schema = serializer.getSchema(mockProvider, String.class);
		assertEquals("string", schema.getClass().getName());
		assertEquals(true, schema.getBoolean("type"));
		JsonNode stringSchema = schema.get("properties").get(serializer.handledType().getName());
		assertEquals("string", stringSchema.get("type").asText());
		assertFalse(stringSchema.get("items") instanceof JsonNull);
	}

	@Before
	public void setUp() throws Exception {
		longType = Long.TYPE;
		stringType = String.class;
	}
}