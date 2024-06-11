package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.assertEquals;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.TimeZone;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatterBuilder;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;

/**
 * Test cases for {@link DateTimeSerializerBase}.
 */
public class DateTimeSerializerBaseTest {

	private static final DateTimeSerializerBase<DateTime> DATE_TIME_SERIALIZER
		= new DateTimeSerializerBase<DateTime>(DateTime.class, false, null);
	private static final DateTimeSerializerBase<Instant> INSTANT_SERIALIZER
		= new DateTimeSerializerBase<Instant>(Instant.class, false, null);

	@org.junit.Test
	public void testDefaultSerialization() throws Exception {
		ObjectMapper mapper = new ObjectMapper();
		SerializationFeature serializationFeature = mapper.getSerializationConfig().getDefaultSerializationFeature(
				SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
		mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
		JsonGenerator gen = new JsonFactory().createGenerator(System.out);

		DateTime dateTime = new DateTime(System.currentTimeMillis());
		assertEquals("2023-03-25T14:30:45.123Z", dateTime.toString("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"));
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
		SerializerProvider serializers = mapper.getSerializationConfig().getDefaultSerializerProvider();
		JsonSerializer<?> serializer = DATE_TIME_SERIALIZER.createContextual(serializers, null);
		serializer.serialize(dateTime, gen, serializers);
		gen.flush();

		String json = mapper.writeValueAsString(dateTime);
		assertEquals("\"2023-03-25T14:30:45.123Z\"", json);
		JsonParser parser = mapper.getFactory().createParser(json);
		DateTime parsedDateTime = mapper.readValue(parser, DateTime.class);
		assertEquals(dateTime, parsedDateTime);

		mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, serializationFeature);
		gen = new JsonFactory().createGenerator(System.out);
		Instant instant = Instant.now();
		assertEquals(instant.toString(), instant.toString());
		serializer = INSTANT_SERIALIZER.createContextual(serializers, null);
		serializer.serialize(instant, gen, serializers);
		gen.flush();

		json = mapper.writeValueAsString(instant);
		assertEquals("1679782645000", json.substring(0, json.length() - 1));
		parser = mapper.getFactory().createParser(json);
		assertEquals(instant.getEpochSecond() * 1000 + instant.getNano() / 1000000L,
				mapper.readValue(parser, Long.TYPE));
	}

	@org.junit.Test
	public void testJsonFormatAnnotationSerialization() throws Exception {
		ObjectMapper mapper = new ObjectMapper();
		JsonFormat jsonFormat = new JsonFormat();
		jsonFormat.setShape(JsonFormat.Shape.STRING);
		jsonFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
		jsonFormat.setPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");

		MyDate date = new MyDate();
		date.setDateValue(new DateTime());
		date.setJsonFormat(jsonFormat);

		JsonGenerator gen = new JsonFactory().createGenerator(System.out);
		serializer = DATE_TIME_SERIALIZER.createContextual(mapper.getSerializationConfig().getDefaultSerializerProvider(),
				new PropertyName("dateValue"));
		serializer.serialize(date, gen, mapper.getSerializationConfig().getDefaultSerializerProvider());
		gen.flush();

		String json = mapper.writeValueAsString(date);
		assertEquals("\"2023-03-25T14:30:45.123Z\"", json);
		parser = mapper.getFactory().createParser(json);
		MyDate parsedDate = mapper.readValue(parser, MyDate.class);
		assertEquals(date, parsedDate);
	}

	@org.junit.Test
	public void testCustomSerializationWithDefaultSettings() throws Exception {
		ObjectMapper mapper = new ObjectMapper();
		JsonFormat jsonFormat = new JsonFormat();
		jsonFormat.setShape(JsonFormat.Shape.STRING);
		jsonFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
		jsonFormat.setPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");

		MyDate date = new MyDate();
		date.setDateValue(new DateTime());
		date.setJsonFormat(jsonFormat);

		JsonGenerator gen = new JsonFactory().createGenerator(System.out);
		serializer = DATE_TIME_SERIALIZER.createContextual(mapper.getSerializationConfig().getDefaultSerializerProvider(),
				new PropertyName("dateValue"));
		mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
		serializer = DATE_TIME_SERIALIZER.withFormat(Boolean.FALSE, jsonFormat)
				.createContextual(mapper.getSerializationConfig().getDefaultSerializerProvider(), new PropertyName("dateValue"));
		serializer.serialize(date, gen, mapper.getSerializationConfig().getDefaultSerializerProvider());
		gen.flush();

		String json = mapper.writeValueAsString(date);
		assertEquals("\"2023-03-25T14:30:45.123Z\"", json);
		parser = mapper.getFactory().createParser(json);
		MyDate parsedDate = mapper.readValue(parser, MyDate.class);
		assertEquals(date, parsedDate);
	}

	@org.junit.Test
	public void testCustomSerializationWithTimestampSetting() throws Exception {
		ObjectMapper mapper = new ObjectMapper();
		JsonFormat jsonFormat = new JsonFormat();
		jsonFormat.setShape(JsonFormat.Shape.NUMBER);
		jsonFormat.setTimeZone(TimeZone.getTimeZone("UTC"));

		MyDate date = new MyDate();
		date.setDateValue(new DateTime());
		date.setJsonFormat(jsonFormat);

		JsonGenerator gen = new JsonFactory().createGenerator(System.out);
		serializer = DATE_TIME_SERIALIZER.createContextual(mapper.getSerializationConfig().getDefaultSerializerProvider(),
				new PropertyName("dateValue"));
		mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, true);
		serializer = DATE_TIME_SERIALIZER.withFormat(Boolean.TRUE, null).createContextual(mapper.getSerializationConfig().getDefaultSerializerProvider(),
				new PropertyName("dateValue"));
		serializer.serialize(date, gen, mapper.getSerializationConfig().getDefaultSerializerProvider());
		gen.flush();

		String json = mapper.writeValueAsString(date);
		assertEquals("1679782645000", json);
		parser = mapper.getFactory().createParser(json);
		MyDate parsedDate = mapper.readValue(parser, MyDate.class);
		assertEquals(date, parsedDate);
	}

	private static class MyDate {
		private DateTime dateValue;
		private JsonFormat jsonFormat;

		public DateTime getDateValue() {
			return dateValue;
		}

		public void setDateValue(DateTime dateValue) {
			this.dateValue = dateValue;
		}

		public JsonFormat getJsonFormat() {
			return jsonFormat;
		}

		public void setJsonFormat(JsonFormat jsonFormat) {
			this.jsonFormat = jsonFormat;
		}
	}

	private static class JsonFormat {
		private JsonFormat.Shape shape;
		private TimeZone timeZone;
		private String pattern;

		public JsonFormat() {
			this.shape = JsonFormat.Shape.STRING;
		}

		public JsonFormat.Shape getShape() {
			return shape;
		}

		public void setShape(JsonFormat.Shape shape) {
			this.shape = shape;
		}

		public TimeZone getTimeZone() {
			return timeZone;
		}

		public void setTimeZone(TimeZone timeZone) {
			this.timeZone = timeZone;
		}

		public String getPattern() {
			return pattern;
		}

		public void setPattern(String pattern) {
			this.pattern = pattern;
		}
	}
}