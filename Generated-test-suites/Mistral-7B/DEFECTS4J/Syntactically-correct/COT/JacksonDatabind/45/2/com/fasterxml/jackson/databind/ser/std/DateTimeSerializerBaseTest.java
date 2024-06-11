package com.fasterxml.jackson.databind.ser.std.test;

import static org.junit.Assert.*;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import org.junit.Test;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.introspect.Annotated;
import com.fasterxml.jackson.databind.ser.std.DateTimeSerializerBase;
import com.fasterxml.jackson.databind.util.StdDateFormat;

public class DateTimeSerializerBaseTest {

	@Test
	public void testDateTimeSerializerBaseConstructor() {
		// Test constructor with useTimestamp = false and customFormat = null
		DateFormat customFormat = null;
		Boolean useTimestamp = false;
		DateTimeSerializerBase<Date> dateTimeSerializer = new DateTimeSerializerBase<Date>(Date.class, useTimestamp, customFormat);
		assertNotNull(dateTimeSerializer);
	}

	@Test
	public void testDateTimeSerializerBaseConstructorWithUseTimestampAndNullCustomFormat() {
		// Test constructor with useTimestamp = true and customFormat = null
		DateFormat customFormat = null;
		Boolean useTimestamp = true;
		DateTimeSerializerBase<Date> dateTimeSerializer = new DateTimeSerializerBase<Date>(Date.class, useTimestamp, customFormat);
		assertNotNull(dateTimeSerializer);
	}

	@Test
	public void testDateTimeSerializerBaseConstructorWithUseTimestampAndCustomFormat() {
		// Test constructor with useTimestamp = false and customFormat = new SimpleDateFormat("dd-MM-yyyy")
		DateFormat customFormat = new SimpleDateFormat("dd-MM-yyyy");
		Boolean useTimestamp = false;
		DateTimeSerializerBase<Date> dateTimeSerializer = new DateTimeSerializerBase<Date>(Date.class, useTimestamp, customFormat);
		assertNotNull(dateTimeSerializer);
	}

	@Test
	public void testCreateContextualWithNullProperty() throws JsonMappingException {
		// Test createContextual method with null property
		JsonFactory factory = new JsonFactory();
		DateTimeSerializerBase<Date> dateTimeSerializer = new DateTimeSerializerBase<Date>(Date.class, false, null);
		assertSame(dateTimeSerializer, dateTimeSerializer.createContextual(null, null));
	}

	@Test
	public void testCreateContextualWithPropertyAnnotatedWithJsonFormat() throws JsonMappingException {
		// Test createContextual method with property annotated with @JsonFormat(shape = JsonFormat.Shape.STRING)
		JsonFactory factory = new JsonFactory();
		Date date = new Date();
		SimpleDateFormat customFormat = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss");
		JsonSerializer<Date> serializer = new DateTimeSerializerBase<Date>(Date.class, false, customFormat).withFormat(false, customFormat);
		BeanProperty property = new BeanProperty("testProperty", Date.class, null, null, null, null, null);
		Annotated member = Mockito.mock(Annotated.class);
		when(member.getValue()).thenReturn(property);
		JsonFormat jsonFormat = new JsonFormat();
		jsonFormat.setShape(JsonFormat.Shape.STRING);
		when(factory.getAnnotationIntrospector().findFormat(member)).thenReturn(jsonFormat);
		JsonSerializer<Date> expectedSerializer = serializer;
		assertEquals(expectedSerializer, dateTimeSerializer.createContextual(factory, property));
	}

	@Test
	public void testCreateContextualWithPropertyNotAnnotatedWithJsonFormat() throws JsonMappingException {
		// Test createContextual method with property not annotated with @JsonFormat
		JsonFactory factory = new JsonFactory();
		DateTimeSerializerBase<Date> dateTimeSerializer = new DateTimeSerializerBase<Date>(Date.class, false, null);
		assertSame(dateTimeSerializer, dateTimeSerializer.createContextual(factory, null));
	}

	@Test
	public void testSerializeWithUseTimestampTrue() throws IOException {
		// Test serialize method with useTimestamp = true
		JsonFactory factory = new JsonFactory();
		DateFormat customFormat = null;
		Boolean useTimestamp = true;
		DateTimeSerializerBase<Date> dateTimeSerializer = new DateTimeSerializerBase<Date>(Date.class, useTimestamp, customFormat);
		Date date = new Date();
		JsonGenerator gen = factory.createGenerator();
		dateTimeSerializer.serialize(date, gen, null);
		assertEquals("1970-01-01T00:00:00.000Z", gen.getOutputString());
	}

	@Test
	public void testSerializeWithUseTimestampFalseAndCustomFormat() throws IOException {
		// Test serialize method with useTimestamp = false and customFormat = new SimpleDateFormat("dd-MM-yyyy")
		JsonFactory factory = new JsonFactory();
		DateFormat customFormat = new SimpleDateFormat("dd-MM-yyyy");
		Boolean useTimestamp = false;
		DateTimeSerializerBase<Date> dateTimeSerializer = new DateTimeSerializerBase<Date>(Date.class, useTimestamp, customFormat);
		Date date = new Date();
		JsonGenerator gen = factory.createGenerator();
		dateTimeSerializer.serialize(date, gen, null);
		assertEquals("12-12-2022", gen.getOutputString());
	}

	@Test
	public void testEmptyMethod() {
		// Test empty method
		DateTimeSerializerBase<Date> dateTimeSerializer = new DateTimeSerializerBase<Date>(Date.class, false, null);
		assertTrue(dateTimeSerializer.isEmpty(null));
		assertTrue(dateTimeSerializer.isEmpty(null, null));
		assertFalse(dateTimeSerializer.isEmpty(new Date()));
		assertFalse(dateTimeSerializer.isEmpty(new JsonParser(), new Date()));
	}

	@Test
	public void testGetSchemaMethod() {
		// Test getSchema method
		DateTimeSerializerBase<Date> dateTimeSerializer = new DateTimeSerializerBase<Date>(Date.class, false, null);
		assertEquals("string", dateTimeSerializer.getSchema(null, null.getClass()).getClassId().getSimpleName());
	}

	@Test
	public void testAcceptJsonFormatVisitorMethodWithUseTimestampTrue() throws JsonMappingException {
		// Test acceptJsonFormatVisitor method with useTimestamp = true
		JsonFactory factory = new JsonFactory();
		DateTimeSerializerBase<Date> dateTimeSerializer = new DateTimeSerializerBase<Date>(Date.class, true, null);
		JsonFormatVisitorWrapper visitor = factory.getSerializerProvider().getSerializerProvider().getJsonParser().getCodec().getSerializationConfig().getFactory().createJsonFormatVisitorWrapper(null, null);
		dateTimeSerializer.acceptJsonFormatVisitor(visitor, null);
		assertEquals("number", visitor.getFormat().getShapeName());
		assertEquals("number", visitor.getFormat().getShape().getSimpleName());
		assertEquals("number", visitor.getFormat().getSerializableType().getSimpleName());
		assertEquals("Java int, long, double, BigDecimal, BigInteger, String, or any serializable type", visitor.getFormat().getContentType());
		assertEquals("Java int, long, double, BigDecimal, BigInteger, or JsonNode", visitor.getFormat().getContainerType().getSimpleName());
		assertEquals("Java int, long, double, BigDecimal, BigInteger, String, or any serializable type", visitor.getFormat().getSubTypes().get(0).getSimpleName());
		assertEquals("Java int, long, double, BigDecimal, BigInteger, String, or any serializable type", visitor.getFormat().getSubTypes().get(1).getSimpleName());
	}

	@Test
	public void testAcceptJsonFormatVisitorMethodWithUseTimestampFalse() throws JsonMappingException {
		// Test acceptJsonFormatVisitor method with useTimestamp = false
		JsonFactory factory = new JsonFactory();
		DateTimeSerializerBase<Date> dateTimeSerializer = new DateTimeSerializerBase<Date>(Date.class, false, null);
		JsonFormatVisitorWrapper visitor = factory.getSerializerProvider().getSerializerProvider().getJsonParser().getCodec().getSerializationConfig().getFactory().createJsonFormatVisitorWrapper(null, null);
		dateTimeSerializer.acceptJsonFormatVisitor(visitor, null);
		assertEquals("string", visitor.getFormat().getShapeName());
		assertEquals("string", visitor.getFormat().getShape().getSimpleName());
		assertEquals("string", visitor.getFormat().getSerializableType().getSimpleName());
		assertEquals("Java String, or any serializable type", visitor.getFormat().getContainerType().getSimpleName());
		assertEquals("Java String, or any serializable type", visitor.getFormat().getSubTypes().get(0).getSimpleName());
		assertEquals("Java String, or any serializable type", visitor.getFormat().getSubTypes().get(1).getSimpleName());
	}

	@Test
	public void testSerializeMethodWithNullValue() throws IOException {
		// Test serialize method with null value
		JsonFactory factory = new JsonFactory();
		DateTimeSerializerBase<Date> dateTimeSerializer = new DateTimeSerializerBase<Date>(Date.class, false, null);
		JsonGenerator gen = factory.createGenerator();
		dateTimeSerializer.serialize(null, gen, null);
		assertEquals("null", gen.getOutputString());
	}

	@Test
	public void testSerializeMethodWithEmptyDate() throws IOException {
		// Test serialize method with empty date (timestamp 0)
		JsonFactory factory = new JsonFactory();
		DateTimeSerializerBase<Date> dateTimeSerializer = new DateTimeSerializerBase<Date>(Date.class, false, null);
		Date emptyDate = new Date(0);
		JsonGenerator gen = factory.createGenerator();
		dateTimeSerializer.serialize(emptyDate, gen, null);
		assertEquals("null", gen.getOutputString());
	}

	@Test
	public void testSerializeMethodWithValidDate() throws IOException {
		// Test serialize method with valid date
		JsonFactory factory = new JsonFactory();
		DateTimeSerializerBase<Date> dateTimeSerializer = new DateTimeSerializerBase<Date>(Date.class, false, null);
		Date validDate = new Date();
		JsonGenerator gen = factory.createGenerator();
		SimpleDateFormat customFormat = new SimpleDateFormat("dd-MM-yyyy");
		dateTimeSerializer = new DateTimeSerializerBase<Date>(Date.class, false, customFormat).withFormat(false, customFormat);
		dateTimeSerializer.serialize(validDate, gen, null);
		assertEquals("12-12-2022", gen.getOutputString());
	}

	@Test
	public void testSerializeMethodWithInvalidCustomFormat() throws IOException {
		// Test serialize method with invalid custom format
		JsonFactory factory = new JsonFactory();
		DateTimeSerializerBase<Date> dateTimeSerializer = new DateTimeSerializerBase<Date>(Date.class, false, null);
		Date validDate = new Date();
		JsonGenerator gen = factory.createGenerator();
		SimpleDateFormat customFormat = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss"); // invalid format with hh:mm:ss
		dateTimeSerializer = new DateTimeSerializerBase<Date>(Date.class, false, customFormat).withFormat(false, customFormat);
		try {
			dateTimeSerializer.serialize(validDate, gen, null);
			fail("Expected IllegalArgumentException");
		} catch (IllegalArgumentException e) {
			assertEquals("Invalid format: \"dd-MM-yyyy hh:mm:ss\", must not contain hour, minute and second format specifiers", e.getMessage());
		}
	}

	@Test
	public void testSerializeMethodWithTimeZone() throws IOException {
		// Test serialize method with TimeZone
		JsonFactory factory = new JsonFactory();
		DateTimeSerializerBase<Date> dateTimeSerializer = new DateTimeSerializerBase<Date>(Date.class, false, null);
		Date validDate = new Date();
		JsonGenerator gen = factory.createGenerator();
		TimeZone timeZone = TimeZone.getTimeZone("UTC");
		dateTimeSerializer = new DateTimeSerializerBase<Date>(Date.class, false, null).withFormat(false, null).withContextualSerialization(SerializerProvider.stdInstance(), new BeanProperty("timeZoneProperty", TimeZone.class, null, null, null, null, null), timeZone);
		dateTimeSerializer.serialize(validDate, gen, null);
		assertEquals("1970-01-01T00:00:00.000Z", gen.getOutputString());
		assertEquals("1970-01-01T00:00:00.000Z", gen.getOutputString(timeZone));
	}

	@Test
	public void testSerializeMethodWithLocale() throws IOException {
		// Test serialize method with Locale
		JsonFactory factory = new JsonFactory();
		DateTimeSerializerBase<Date> dateTimeSerializer = new DateTimeSerializerBase<Date>(Date.class, false, null);
		Date validDate = new Date();
		JsonGenerator gen = factory.createGenerator();
		Locale locale = Locale.CANADA;
		dateTimeSerializer = new DateTimeSerializerBase<Date>(Date.class, false, null).withFormat(false, null).withContextualSerialization(SerializerProvider.stdInstance(), new BeanProperty("localeProperty", Locale.class, null, null, null, null, null), locale);
		dateTimeSerializer.serialize(validDate, gen, null);
		assertEquals("1970-01-01T05:00:00.000-05:00", gen.getOutputString());
		assertEquals("1970-01-01T05:00:00.000-05:00", gen.getOutputString(locale));
	}

	@Test(expected = IllegalArgumentException.class)
	public void testSerializeMethodWithNullSerializerProvider() throws IOException {
		// Test serialize method with null SerializerProvider
		DateTimeSerializerBase<Date> dateTimeSerializer = new DateTimeSerializerBase<Date>(Date.class, false, null);
		Date validDate = new Date();
		try {
			dateTimeSerializer.serialize(validDate, null, null);
			fail("Expected IllegalArgumentException");
		} catch (IllegalArgumentException e) {
			assertEquals("Null SerializerProvider passed for Date", e.getMessage());
		}
	}

	@Test
	public void testDeserializeEmptyString() throws IOException, JsonParseException {
		// Test deserialize method with empty string
		JsonFactory factory = new JsonFactory();
		DateTimeSerializerBase<Date> dateTimeSerializer = new DateTimeSerializerBase<Date>(Date.class, false, null);
		JsonParser parser = factory.createParser("\"\"");
		Date date = dateTimeSerializer.deserialize(parser, parser.getValueAsString(), null);
		assertNull(date);
	}

	@Test
	public void testDeserializeValidStringWithCustomFormat() throws IOException, JsonParseException {
		// Test deserialize method with valid string and custom format
		JsonFactory factory = new JsonFactory();
		DateTimeSerializerBase<Date> dateTimeSerializer = new DateTimeSerializerBase<Date>(Date.class, false, new SimpleDateFormat("dd-MM-yyyy"));
		JsonParser parser = factory.createParser("\"12-12-2022\"");
		Date date = dateTimeSerializer.deserialize(parser, parser.getValueAsString(), null);
		assertEquals(new Date(System.currentTimeMillis()), date);
	}

	@Test(expected = JsonMappingException.class)
	public void testDeserializeInvalidString() throws IOException, JsonParseException {
		// Test deserialize method with invalid string
		JsonFactory factory = new JsonFactory();
		DateTimeSerializerBase<Date> dateTimeSerializer = new DateTimeSerializerBase<Date>(Date.class, false, null);
		JsonParser parser = factory.createParser("\"invalid\"");
		try {
			dateTimeSerializer.deserialize(parser, parser.getValueAsString(), null);
			fail("Expected JsonMappingException");
		} catch (JsonMappingException e) {
			assertEquals("Cannot deserialize value of type [simple type, class java.util.Date] from ValueToken [null] (no class def found)", e.getMessage());
		}
	}
}