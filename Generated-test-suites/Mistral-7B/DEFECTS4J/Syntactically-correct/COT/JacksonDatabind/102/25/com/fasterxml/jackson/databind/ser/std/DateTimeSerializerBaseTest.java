package com.example.junit.test;

import static org.junit.Assert.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import org.junit.Test;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.type.TypeFactory;

import com.example.DateTimeSerializerBase.DateTimeSerializerBase;
import com.example.DateTimeSerializerBase.StdScalarSerializer;

public class DateTimeSerializerBaseTest {

	@SuppressWarnings("unchecked")
	private DateTimeSerializerBase<Date> getDateTimeSerializerBaseInstance() {
		return new DateTimeSerializerBase<Date>(Date.class, Boolean.FALSE, null);
	}

	/**
	 * Test constructor.
	 */
	@Test
	public void testConstructor() {
		DateTimeSerializerBase<Date> serializer = getDateTimeSerializerBaseInstance();
		assertNotNull(serializer);
	}

	/**
	 * Test method withFormat.
	 */
	@Test
	public void testWithFormat() {
		DateTimeSerializerBase<Date> serializer = getDateTimeSerializerBaseInstance();
		DateTimeSerializerBase<Date> newSerializer = serializer.withFormat(Boolean.TRUE, null);
		assertNotSame(serializer, newSerializer);
		assertEquals(Boolean.TRUE, newSerializer._useTimestamp);
	}

	/**
	 * Test method createContextual.
	 *
	 * @throws JsonMappingException
	 * @throws JsonProcessingException
	 * @throws JsonGenerationException
	 * @throws IOException
	 * @throws ParseException
	 */
	@Test
	public void testCreateContextual() throws JsonMappingException, JsonProcessingException, JsonGenerationException, IOException, ParseException {
		ObjectMapper mapper = new ObjectMapper();
		mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);

		DateTimeSerializerBase<Date> serializer = getDateTimeSerializerBaseInstance();
		SerializerProvider provider = mapper.getSerializerProvider();
		BeanProperty property = new BeanProperty("testProperty", Date.class, null);
		JsonFormat.Value format = new JsonFormat.Value();
		format.setShape(JsonFormat.Shape.STRING);
		format.setLocale(Locale.US);
		format.setTimeZone(TimeZone.getTimeZone("UTC"));
		JsonFormat.Value format2 = new JsonFormat.Value();
		format2.setShape(JsonFormat.Shape.STRING);
		format2.setTimeZone(TimeZone.getTimeZone("UTC"));
		JsonFormat.Value format3 = new JsonFormat.Value();
		format3.setShape(JsonFormat.Shape.STRING);
		format3.setLocale(Locale.US);
		JsonFormat.Value format4 = new JsonFormat.Value();
		format4.setShape(JsonFormat.Shape.STRING);
		JsonFormat.Value format5 = new JsonFormat.Value();
		format5.setShape(JsonFormat.Shape.STRING);
		format5.setLocale(Locale.US);
		format5.setTimeZone(TimeZone.getTimeZone("UTC"));
		JsonFormat.Value format6 = new JsonFormat.Value();
		format6.setShape(JsonFormat.Shape.STRING);
		JsonFormat.Value format7 = new JsonFormat.Value();
		JsonFormat.Value format8 = new JsonFormat.Value();
		JsonFormat.Value format9 = new JsonFormat.Value();
		JsonFormat.Value format10 = new JsonFormat.Value();
		JsonFormat.Value format11 = new JsonFormat.Value();
		JsonFormat.Value format12 = new JsonFormat.Value();
		JsonFormat.Value format13 = new JsonFormat.Value();
		JsonFormat.Value format14 = new JsonFormat.Value();
		JsonFormat.Value format15 = new JsonFormat.Value();
		JsonFormat.Value format16 = new JsonFormat.Value();
		JsonFormat.Value format17 = new JsonFormat.Value();
		JsonFormat.Value format18 = new JsonFormat.Value();
		JsonFormat.Value format19 = new JsonFormat.Value();
		JsonFormat.Value format20 = new JsonFormat.Value();

		assertEquals(serializer, serializer.createContextual(provider, null));

		JsonFormat.Value format = new JsonFormat.Value();
		format.setShape(JsonFormat.Shape.STRING);
		format.setPattern("yyyy-MM-dd'T'HH:mm:ss'Z'");
		format.setLocale(Locale.US);
		format.setTimeZone(TimeZone.getTimeZone("UTC"));
		JsonFormat.Value format2 = new JsonFormat.Value();
		format2.setShape(JsonFormat.Shape.STRING);
		format2.setPattern("yyyy-MM-dd'T'HH:mm:ss.SSSSSS'Z'");
		format2.setLocale(Locale.US);
		format2.setTimeZone(TimeZone.getTimeZone("UTC"));
		JsonFormat.Value format3 = new JsonFormat.Value();
		format3.setShape(JsonFormat.Shape.STRING);
		format3.setPattern("yyyy-MM-dd");
		format3.setLocale(Locale.US);
		JsonFormat.Value format4 = new JsonFormat.Value();
		format4.setShape(JsonFormat.Shape.STRING);
		format4.setPattern("yyyy-MM-dd'T'HH:mm:ss'Z'");
		format4.setLocale(Locale.US);
		JsonFormat.Value format5 = new JsonFormat.Value();
		format5.setShape(JsonFormat.Shape.STRING);
		format5.setPattern("yyyy-MM-dd'T'HH:mm:ss.SSSSSS'Z'");
		format5.setLocale(Locale.US);
		JsonFormat.Value format6 = new JsonFormat.Value();
		format6.setShape(JsonFormat.Shape.STRING);
		format6.setPattern("yyyy-MM-dd");
		format6.setLocale(Locale.CHINESE);
		JsonFormat.Value format7 = new JsonFormat.Value();
		format7.setShape(JsonFormat.Shape.STRING);
		format7.setPattern("yyyy-MM-dd'T'HH:mm:ss'Z'");
		format7.setLocale(Locale.CHINESE);
		JsonFormat.Value format8 = new JsonFormat.Value();
		format8.setShape(JsonFormat.Shape.STRING);
		format8.setPattern("yyyy-MM-dd'T'HH:mm:ss.SSSSSS'Z'");
		format8.setLocale(Locale.CHINESE);
		JsonFormat.Value format9 = new JsonFormat.Value();
		format9.setShape(JsonFormat.Shape.STRING);
		format9.setPattern("yyyy-MM-dd");
		format9.setLocale(Locale.GERMAN);
		JsonFormat.Value format10 = new JsonFormat.Value();
		format10.setShape(JsonFormat.Shape.STRING);
		format10.setPattern("yyyy-MM-dd'T'HH:mm:ss'Z'");
		format10.setLocale(Locale.GERMAN);
		JsonFormat.Value format11 = new JsonFormat.Value();
		format11.setShape(JsonFormat.Shape.STRING);
		format11.setPattern("yyyy-MM-dd'T'HH:mm:ss.SSSSSS'Z'");
		format11.setLocale(Locale.GERMAN);
		JsonFormat.Value format12 = new JsonFormat.Value();
		format12.setShape(JsonFormat.Shape.STRING);
		format12.setPattern("yyyy-MM-dd");
		format12.setLocale(Locale.FRENCH);
		JsonFormat.Value format13 = new JsonFormat.Value();
		format13.setShape(JsonFormat.Shape.STRING);
		format13.setPattern("yyyy-MM-dd'T'HH:mm:ss'Z'");
		format13.setLocale(Locale.FRENCH);
		JsonFormat.Value format14 = new JsonFormat.Value();
		format14.setShape(JsonFormat.Shape.STRING);
		format14.setPattern("yyyy-MM-dd'T'HH:mm:ss.SSSSSS'Z'");
		format14.setLocale(Locale.FRENCH);
		JsonFormat.Value format15 = new JsonFormat.Value();
		format15.setShape(JsonFormat.Shape.STRING);
		format15.setPattern("yyyy-MM-dd");
		format15.setLocale(Locale.ITALIAN);
		JsonFormat.Value format16 = new JsonFormat.Value();
		format16.setShape(JsonFormat.Shape.STRING);
		format16.setPattern("yyyy-MM-dd'T'HH:mm:ss'Z'");
		format16.setLocale(Locale.ITALIAN);
		JsonFormat.Value format17 = new JsonFormat.Value();
		format17.setShape(JsonFormat.Shape.STRING);
		format17.setPattern("yyyy-MM-dd'T'HH:mm:ss.SSSSSS'Z'");
		format17.setLocale(Locale.ITALIAN);
		JsonFormat.Value format18 = new JsonFormat.Value();
		format18.setShape(JsonFormat.Shape.STRING);
		format18.setPattern("yyyy-MM-dd");
		format18.setLocale(Locale.JAPANESE);
		JsonFormat.Value format19 = new JsonFormat.Value();
		format19.setShape(JsonFormat.Shape.STRING);
		format19.setPattern("yyyy-MM-dd'T'HH:mm:ss'Z'");
		format19.setLocale(Locale.JAPANESE);
		JsonFormat.Value format20 = new JsonFormat.Value();
		format20.setShape(JsonFormat.Shape.STRING);
		format20.setPattern("yyyy-MM-dd'T'HH:mm:ss.SSSSSS'Z'");
		format20.setLocale(Locale.JAPANESE);

		JsonFormat.Value formatArray[] = { format, format2, format3, format4, format5, format6, format7, format8, format9, format10, format11, format12, format13, format14, format15, format16, format17, format18, format19, format20 };

		for (JsonFormat.Value format : formatArray) {
			DateTimeSerializerBase<Date> newSerializer = serializer.createContextual(provider, property);
			assertEquals(format, newSerializer.findFormatOverrides(provider, property, TypeFactory.defaultInstance().constructValueType(Date.class)));
		}
	}

	/**
	 * Test method serialize.
	 *
	 * @throws JsonProcessingException
	 * @throws JsonGenerationException
	 * @throws IOException
	 */
	@Test
	public void testSerialize() throws JsonProcessingException, JsonGenerationException, IOException {
		ObjectMapper mapper = new ObjectMapper();
		mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
		SerializerProvider provider = mapper.getSerializerProvider();
		DateTimeSerializerBase<Date> serializer = getDateTimeSerializerBaseInstance();
		Date date = new Date();
		String expectedString = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSSS'Z'").format(date);
		String actualString = "";
		JsonGenerator gen = null;

		try {
			gen = provider.getFactory().createGenerator();
			serializer.serialize(date, gen, provider);
			actualString = gen.toString();
		} finally {
			if (gen != null) {
				gen.close();
			}
		}

		assertEquals(expectedString, actualString);
	}

	/**
	 * Test method serializeAsString.
	 *
	 * @throws JsonProcessingException
	 * @throws JsonGenerationException
	 * @throws IOException
	 */
	@Test
	public void testSerializeAsString() throws JsonProcessingException, JsonGenerationException, IOException {
		ObjectMapper mapper = new ObjectMapper();
		mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
		SerializerProvider provider = mapper.getSerializerProvider();
		DateTimeSerializerBase<Date> serializer = getDateTimeSerializerBaseInstance();
		Date date = new Date();
		String expectedString = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSSS'Z'").format(date);
		String actualString = serializer._serializeAsString(date, null, provider);

		assertEquals(expectedString, actualString);
	}
}