package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.*;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.TimeZone;

import org.junit.Test;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.TextNode;
import com.fasterxml.jackson.databind.ser.std.DateTimeSerializerBase;

public class DateTimeSerializerBaseTest {

	@Test
	public void testSerializeDate() throws Exception {
		DateTimeSerializerBase<Date> dateSerializer = new StdDateSerializer();
		Date date = new Date();

		ObjectMapper mapper = new ObjectMapper();
		JsonNode rootNode = mapper.createObjectNode();
		JsonGenerator gen = mapper.getFactory().createGenerator(System.out);

		dateSerializer.serialize(date, gen, mapper.getSerializerProvider());
		JsonNode node = mapper.readTree(System.out);
		TextNode expected = JsonNodeFactory.instance.textValue("\"2023-03-29T15:25:30.000Z\"");

		assertEquals(expected.asText(), node.get("").asText());
	}

	@Test
	public void testSerializeZonedDateTimeWithCustomFormat() throws Exception {
		DateTimeSerializerBase<ZonedDateTime> zonedDateTimeSerializer = new StdSerializers.BaseDateSerializer(ZonedDateTime.class, false, new SimpleDateFormat("yyyy-MM-dd HH:mm:ss z"));

		ZoneId zoneId = ZoneId.of("America/Los_Angeles");
		ZonedDateTime zonedDateTime = ZonedDateTime.now(zoneId);

		ObjectMapper mapper = new ObjectMapper();
		JsonNode rootNode = mapper.createObjectNode();
		JsonGenerator gen = mapper.getFactory().createGenerator(System.out);

		zonedDateTimeSerializer.serialize(zonedDateTime, gen, mapper.getSerializerProvider());
		JsonNode node = mapper.readTree(System.out);
		TextNode expected = JsonNodeFactory.instance.textValue("\"2023-03-29 15:25:30-07:00\"");

		assertEquals(expected.asText(), node.get("").asText());
	}

	@Test
	public void testSerializeDateWithCustomFormatAndTimeZone() throws Exception {
		DateTimeSerializerBase<Date> dateSerializer = new StdDateSerializer() {
			@Override
			protected DateFormat _customFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss z");
		};

		Date date = new Date();
		TimeZone timeZone = TimeZone.getTimeZone("Europe/Berlin");

		ObjectMapper mapper = new ObjectMapper();
		JsonNode rootNode = mapper.createObjectNode();
		JsonGenerator gen = mapper.getFactory().createGenerator(System.out);

		dateSerializer.setTimeZone(timeZone);
		dateSerializer.serialize(date, gen, mapper.getSerializerProvider());
		JsonNode node = mapper.readTree(System.out);
		TextNode expected = JsonNodeFactory.instance.textValue("\"2023-03-29 18:25:30+01:00\"");

		assertEquals(expected.asText(), node.get("").asText());
	}

	@Test
	public void testSerializeNull() throws Exception {
		DateTimeSerializerBase<Date> dateSerializer = new StdDateSerializer();

		ObjectMapper mapper = new ObjectMapper();
		JsonNode rootNode = mapper.createObjectNode();
		JsonGenerator gen = mapper.getFactory().createGenerator(System.out);

		dateSerializer.serialize(null, gen, mapper.getSerializerProvider());
		JsonNode node = mapper.readTree(System.out);
		TextNode expected = JsonNodeFactory.instance.textValue("null");

		assertEquals(expected.asText(), node.get("").asText());
	}

	@Test(expected = IllegalArgumentException.class)
	public void testSerializeWithNullProvider() throws Exception {
		DateTimeSerializerBase<Date> dateSerializer = new StdDateSerializer();

		dateSerializer.serialize(new Date(), null, null);
	}
}