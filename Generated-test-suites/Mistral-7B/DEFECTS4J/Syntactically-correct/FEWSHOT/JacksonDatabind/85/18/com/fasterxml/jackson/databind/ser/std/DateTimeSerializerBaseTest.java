package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.*;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.TimeZone;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.jsonFormatVisitors.*;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import com.fasterxml.jackson.databind.util.StdDateFormat;

public class DateTimeSerializerBaseTest<T> {

    @SuppressWarnings("unchecked")
    private static final Class<DateTimeSerializerBase<T>> dtype = (Class<DateTimeSerializerBase<T>>) (Class<?>) DateTimeSerializerBase.class;

    @Test
    public void testCreateContextual() throws JsonMappingException {
        ObjectMapper mapper = new ObjectMapper();
        DateTimeSerializerBase<Object> serializer1 = mapper.findValueSerializer(Object.class);
        DateTimeSerializerBase<Object> serializer2 = mapper.findValueSerializer(Object.class);
        SerializerProvider provider = mapper.getSerializerProvider();
        assertTrue(serializer1 == serializer2);

        DateTimeSerializerBase<Object> customSerializer1 = new DateTimeSerializerBase<Object>(Object.class, Boolean.TRUE, null);
        DateTimeSerializerBase<Object> customSerializer2 = mapper.findValueSerializer(Object.class);
        assertNotSame(customSerializer1, customSerializer2);

        DateTimeSerializerBase<String> stringSerializer = new DateTimeSerializerBase<String>(String.class, Boolean.FALSE, null);
        DateTimeSerializerBase<String> stringSerializer2 = new DateTimeSerializerBase<String>(String.class, Boolean.FALSE, null)
                .withFormat(Boolean.FALSE, null);
        assertNotSame(stringSerializer, stringSerializer2);

        DateTimeSerializerBase<String> formatSerializer = new DateTimeSerializerBase<String>(String.class, Boolean.FALSE, new SimpleDateFormat("yyyy-MM-dd HH:mm:ss z"));
        DateTimeSerializerBase<String> formatSerializer2 = new DateTimeSerializerBase<String>(String.class, Boolean.FALSE, new SimpleDateFormat("yyyy-MM-dd HH:mm:ss z"));
        assertNotSame(formatSerializer, formatSerializer2);

        DateTimeSerializerBase<String> localeSerializer = new DateTimeSerializerBase<String>(String.class, Boolean.FALSE, new SimpleDateFormat("yyyy-MM-dd HH:mm:ss z"));
        DateTimeSerializerBase<String> localeSerializer2 = new DateTimeSerializerBase<String>(String.class, Boolean.FALSE, new SimpleDateFormat("yyyy-MM-dd HH:mm:ss z"))
                .withFormat(Boolean.FALSE, null).withContextualHandler(LocaleContextualSerializer.instance);
        assertNotSame(localeSerializer, localeSerializer2);

        DateTimeSerializerBase<String> timeZoneSerializer = new DateTimeSerializerBase<String>(String.class, Boolean.FALSE, new SimpleDateFormat("yyyy-MM-dd HH:mm:ss z"));
        DateTimeSerializerBase<String> timeZoneSerializer2 = new DateTimeSerializerBase<String>(String.class, Boolean.FALSE, new SimpleDateFormat("yyyy-MM-dd HH:mm:ss z"))
                .withFormat(Boolean.FALSE, null).withContextualHandler(TimeZoneContextualSerializer.instance);
        assertNotSame(timeZoneSerializer, timeZoneSerializer2);
    }

    @Test
    public void testSerialize() throws IOException {
        ObjectMapper mapper = new ObjectMapper();

        JavaType javaType = mapper.getTypeFactory().constructType(DateTime.class);
        DateTimeSerializerBase<DateTime> serializer = new DateTimeSerializerBase<DateTime>(DateTime.class, Boolean.FALSE, null);

        JsonGenerator gen = mapper.getFactory().createGenerator(new ByteArrayOutputStream(), null);
        serializer.serialize(new DateTime(System.currentTimeMillis()), gen, mapper.getSerializerProvider());
        String json = gen.getOutputBuffer().toString();
        assertEquals("\"2023-03-29T14:35:12.789Z\"", json);

        gen.reset();
        serializer.serialize(null, gen, mapper.getSerializerProvider());
        json = gen.getOutputBuffer().toString();
        assertTrue(json.isEmpty());

        gen.setCodec(mapper);
        serializer.serialize(new DateTime(Long.MIN_VALUE), gen, mapper.getSerializerProvider());
        json = gen.getOutputBuffer().toString();
        assertEquals("\"-2958464-01-19T00:00:00.000Z\"", json);

        gen.reset();
        serializer.serialize(new DateTime(Long.MAX_VALUE), gen, mapper.getSerializerProvider());
        json = gen.getOutputBuffer().toString();
        assertEquals("\"2958464-01-01T00:00:00.000Z\"", json);

        gen.reset();
        serializer.serialize(new DateTime(Long.MAX_VALUE + 3600000), gen, mapper.getSerializerProvider());
        json = gen.getOutputBuffer().toString();
        assertEquals("\"2958465-01-01T01:00:00.000Z\"", json);

        gen.reset();
        serializer.serialize(new DateTime(1611974400000L), gen, mapper.getSerializerProvider()); // 2021-01-01 00:00:00
        json = gen.getOutputBuffer().toString();
        assertEquals("\"2021-01-01T00:00:00.000Z\"", json);

        gen.reset();
        serializer.serialize(new DateTime(1611974400000L + 3600000), gen, mapper.getSerializerProvider()); // 2021-01-01 01:00:00
        json = gen.getOutputBuffer().toString();
        assertEquals("\"2021-01-01T01:00:00.000Z\"", json);

        gen.reset();
        serializer.serialize(new DateTime(1611974400000L + 7200000), gen, mapper.getSerializerProvider()); // 2021-01-01 02:00:00
        json = gen.getOutputBuffer().toString();
        assertEquals("\"2021-01-01T02:00:00.000Z\"", json);

        gen.reset();
        serializer.serialize(new DateTime(1611974400000L + 86400000), gen, mapper.getSerializerProvider()); // 2021-01-02 00:00:00
        json = gen.getOutputBuffer().toString();
        assertEquals("\"2021-01-02T00:00:00.000Z\"", json);
    }

    @Test
    public void testDeserialize() throws IOException {
        ObjectMapper mapper = new ObjectMapper();

        String json = "\"2023-03-29T14:35:12.789Z\"";
        JavaType javaType = mapper.getTypeFactory().constructType(DateTime.class);
        DateTime deserialized = mapper.readerForType(javaType).readValue(new ByteArrayInputStream(json.getBytes()));
        assertEquals(new DateTime(System.currentTimeMillis()), deserialized);

        json = "\"2023-03-29T14:35:12.789Z\"";
        deserialized = mapper.readerForType(javaType).readValue(new ByteArrayInputStream(json.getBytes()));
        assertEquals(new DateTime(System.currentTimeMillis()), deserialized);

        json = "\"\"";
        try {
            deserialized = mapper.readerForType(javaType).readValue(new ByteArrayInputStream(json.getBytes()));
            fail("Expected JsonMappingException");
        } catch (JsonMappingException e) {
            // expected
        }

        json = "\"-2958464-01-19T00:00:00.000Z\"";
        deserialized = mapper.readerForType(javaType).readValue(new ByteArrayInputStream(json.getBytes()));
        assertEquals(new DateTime(Long.MIN_VALUE), deserialized);

        json = "\"2958464-01-01T00:00:00.000Z\"";
        deserialized = mapper.readerForType(javaType).readValue(new ByteArrayInputStream(json.getBytes()));
        assertEquals(new DateTime(Long.MAX_VALUE), deserialized);

        json = "\"2958465-01-01T01:00:00.000Z\"";
        deserialized = mapper.readerForType(javaType).readValue(new ByteArrayInputStream(json.getBytes()));
        assertEquals(new DateTime(Long.MAX_VALUE + 3600000), deserialized);

        json = "\"2021-01-01T00:00:00.000Z\"";
        deserialized = mapper.readerForType(javaType).readValue(new ByteArrayInputStream(json.getBytes()));
        assertEquals(new DateTime(1611974400000L), deserialized);

        json = "\"2021-01-01T01:00:00.000Z\"";
        deserialized = mapper.readerForType(javaType).readValue(new ByteArrayInputStream(json.getBytes()));
        assertEquals(new DateTime(1611974436000L), deserialized);

        json = "\"2021-01-01T02:00:00.000Z\"";
        deserialized = mapper.readerForType(javaType).readValue(new ByteArrayInputStream(json.getBytes()));
        assertEquals(new DateTime(1611974466000L), deserialized);

        json = "\"2021-01-02T00:00:00.000Z\"";
        deserialized = mapper.readerForType(javaType).readValue(new ByteArrayInputStream(json.getBytes()));
        assertEquals(new DateTime(1611977600000L), deserialized);
    }

    @Test
    public void testIsEmpty() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        DateTimeSerializerBase<DateTime> serializer = new DateTimeSerializerBase<DateTime>(DateTime.class, Boolean.FALSE, null);

        assertTrue(serializer.isEmpty(null));
        assertTrue(serializer.isEmpty(mapper.getSerializerProvider(), null));

        DateTime dateTime = new DateTime(System.currentTimeMillis());
        assertFalse(serializer.isEmpty(dateTime));
        assertFalse(serializer.isEmpty(mapper.getSerializerProvider(), dateTime));

        DateTime emptyDate = new DateTime(0L);
        assertTrue(serializer.isEmpty(emptyDate));
        assertTrue(serializer.isEmpty(mapper.getSerializerProvider(), emptyDate));
    }

    @Test
    public void testAcceptJsonFormatVisitor() throws JsonMappingException {
        ObjectMapper mapper = new ObjectMapper();

        JsonFormatVisitorWrapper visitor = mapper.getSerializerProvider().getExtendedSerializerConfig().getFormatVisitor();
        DateTimeSerializerBase<DateTime> serializer = new DateTimeSerializerBase<DateTime>(DateTime.class, Boolean.FALSE, null);
        serializer.acceptJsonFormatVisitor(visitor, null);

        assertEquals(JsonFormat.Feature.WRITE_DATES_AS_TIMESTAMPS, visitor.getFormatFeatures().get(JsonFormat.Feature.WRITE_DATES_AS_TIMESTAMPS));
        assertEquals(JsonFormat.Feature.WRITE_BIGDECIMAL_AS_PLAIN, visitor.getFormatFeatures().get(JsonFormat.Feature.WRITE_BIGDECIMAL_AS_PLAIN));
        assertEquals(JsonFormat.Feature.WRITE_EMPTY_JSON_ARRAYS, visitor.getFormatFeatures().get(JsonFormat.Feature.WRITE_EMPTY_JSON_ARRAYS));
        assertEquals(JsonFormat.Feature.WRITE_NULL_JSON_AS_NULL, visitor.getFormatFeatures().get(JsonFormat.Feature.WRITE_NULL_JSON_AS_NULL));
        assertEquals(JsonFormat.Feature.WRITE_NUMBERS_AS_STRINGS, visitor.getFormatFeatures().get(JsonFormat.Feature.WRITE_NUMBERS_AS_STRINGS));
        assertEquals(Integer.MIN_VALUE, visitor.getNumberLikeValue());
        assertNull(visitor.getNumberFormat());
        assertEquals(JsonValueFormat.DATE_TIME, visitor.getFormatShape());
    }

    @Test
    public void testGetSchema() throws JsonMappingException {
        ObjectMapper mapper = new ObjectMapper();

        SchemaNode schema = new DateTimeSerializerBase<DateTime>(DateTime.class, Boolean.FALSE, null).getSchema(mapper.getSerializerProvider(), null);
        assertEquals("number", schema.getValueAsToken().asText());
        assertEquals(true, schema.getSchemaProvider().getNullValueToken().isNull());
    }
}