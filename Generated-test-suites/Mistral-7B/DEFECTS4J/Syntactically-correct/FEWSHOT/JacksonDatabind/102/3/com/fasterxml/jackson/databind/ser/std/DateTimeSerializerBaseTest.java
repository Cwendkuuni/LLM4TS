package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.jsonFormatVisitors.*;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import com.fasterxml.jackson.databind.util.StdDateFormat;
import org.junit.Test;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class DateTimeSerializerBaseTest {

    @Test
    public void testDateTimeSerializerBase() {
        DateTimeSerializerBase<Date> serializer = new DateTimeSerializerBase<Date>(Date.class, false, null);
        assertNull(serializer._customFormat);
        assertNull(serializer._reusedCustomFormat);
    }

    @Test
    public void testDateTimeSerializerBaseWithUseTimestamp() {
        DateTimeSerializerBase<Date> serializer = new DateTimeSerializerBase<Date>(Date.class, true, null);
        assertTrue(serializer._useTimestamp);
        assertNull(serializer._customFormat);
        assertNull(serializer._reusedCustomFormat);
    }

    @Test
    public void testDateTimeSerializerBaseWithCustomFormat() {
        SimpleDateFormat customFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss", Locale.ENGLISH);
        DateTimeSerializerBase<Date> serializer = new DateTimeSerializerBase<Date>(Date.class, false, customFormat);
        assertEquals(customFormat, serializer._customFormat);
        assertNotNull(serializer._reusedCustomFormat);
    }

    @Test
    public void testDateTimeSerializerBaseCreateContextual() throws JsonMappingException {
        JsonFactory factory = new JsonFactory();
        SimpleDateFormat customFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss", Locale.ENGLISH);
        DateTimeSerializerBase<Date> serializer = new DateTimeSerializerBase<Date>(Date.class, false, customFormat);

        ObjectMapper mapper = new ObjectMapper(factory);
        SerializerProvider serializers = mapper.getSerializerProvider();

        BeanProperty property = new BeanProperty("testProperty", Date.class);
        JsonFormat.Value format = new JsonFormat.Value();
        format.setShape(JsonFormat.Shape.STRING);
        format.setLocale(Locale.ENGLISH);
        format.setTimeZone(TimeZone.getTimeZone("UTC"));
        format.setPattern("dd-MM-yyyy HH:mm:ss");
        mapper.setSerializationConfig(mapper.getSerializationConfig().withPropertyValueSerializer(Date.class, serializer));

        JsonFormat overrides = mapper.findFormatOverrides(JsonParser.Feature.AUTO_CLOSE_SOURCE, null, null, property, Date.class);
        assertEquals(format, overrides);

        JsonSerializer<Date> contextualSerializer = serializer.createContextual(serializers, property);
        assertSame(serializer, contextualSerializer);
    }

    @Test
    public void testDateTimeSerializerBaseCreateContextualWithUseTimestamp() throws JsonMappingException {
        JsonFactory factory = new JsonFactory();
        DateTimeSerializerBase<Date> serializer = new DateTimeSerializerBase<Date>(Date.class, true, null);

        ObjectMapper mapper = new ObjectMapper(factory);
        SerializerProvider serializers = mapper.getSerializerProvider();

        BeanProperty property = new BeanProperty("testProperty", Date.class);
        JsonFormat.Value format = new JsonFormat.Value();
        format.setShape(JsonFormat.Shape.NUMBER);
        mapper.setSerializationConfig(mapper.getSerializationConfig().withPropertyValueSerializer(Date.class, serializer));

        JsonFormat overrides = mapper.findFormatOverrides(JsonParser.Feature.AUTO_CLOSE_SOURCE, null, null, property, Date.class);
        assertNull(overrides);

        JsonSerializer<Date> contextualSerializer = serializer.createContextual(serializers, property);
        assertSame(serializer, contextualSerializer);
    }

    @Test
    public void testDateTimeSerializerBaseCreateContextualWithCustomFormat() throws JsonMappingException {
        JsonFactory factory = new JsonFactory();
        SimpleDateFormat customFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss", Locale.ENGLISH);
        DateTimeSerializerBase<Date> serializer = new DateTimeSerializerBase<Date>(Date.class, false, customFormat);

        ObjectMapper mapper = new ObjectMapper(factory);
        SerializerProvider serializers = mapper.getSerializerProvider();

        BeanProperty property = new BeanProperty("testProperty", Date.class);
        JsonFormat.Value format = new JsonFormat.Value();
        format.setShape(JsonFormat.Shape.STRING);
        format.setLocale(Locale.ENGLISH);
        format.setTimeZone(TimeZone.getTimeZone("UTC"));
        format.setPattern("dd-MM-yyyy HH:mm:ss");
        mapper.setSerializationConfig(mapper.getSerializationConfig().withPropertyValueSerializer(Date.class, serializer));

        JsonFormat overrides = mapper.findFormatOverrides(JsonParser.Feature.AUTO_CLOSE_SOURCE, null, null, property, Date.class);
        assertNull(overrides);

        JsonSerializer<Date> contextualSerializer = serializer.createContextual(serializers, property);
        assertSame(serializer, contextualSerializer);

        ObjectWriter writer = mapper.writer();
        Date date = new Date();
        writer.writeValueAsString(new ObjectWriter.WriterFeatures().withFormatters(new SimpleTypeSerializerImpl(Date.class, serializer)), new ObjectWriter.WriterFeatures(), date);
        String result = writer.getWriter().getOutputFactory().writeValueAsString(date);
        assertEquals("\"2023-03-14T12:34:56.000Z\"", result);
    }

    @Test
    public void testDateTimeSerializerBaseSerialize() throws IOException {
        JsonFactory factory = new JsonFactory();
        SimpleDateFormat customFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss", Locale.ENGLISH);
        DateTimeSerializerBase<Date> serializer = new DateTimeSerializerBase<Date>(Date.class, false, customFormat);

        ObjectMapper mapper = new ObjectMapper(factory);
        SerializerProvider serializers = mapper.getSerializerProvider();

        JsonGenerator gen = mapper.getFactory().createGenerator(new ByteArrayOutputStream(), new DefaultSerializerProvider(), null);
        Date date = new Date();

        serializer.serialize(date, gen, serializers);
        gen.flush();
        assertEquals("\"14-03-2023 12:34:56\"", new String(gen.getOutputBuffer().base, 0, gen.getOutputBuffer().size()));
    }

    @Test
    public void testDateTimeSerializerBaseSerializeWithUseTimestamp() throws IOException {
        JsonFactory factory = new JsonFactory();
        DateTimeSerializerBase<Date> serializer = new DateTimeSerializerBase<Date>(Date.class, true, null);

        ObjectMapper mapper = new ObjectMapper(factory);
        SerializerProvider serializers = mapper.getSerializerProvider();

        JsonGenerator gen = mapper.getFactory().createGenerator(new ByteArrayOutputStream(), new DefaultSerializerProvider(), null);
        Date date = new Date();

        serializer.serialize(date, gen, serializers);
        gen.flush();
        assertEquals("1423462456000", new String(gen.getOutputBuffer().base, 0, gen.getOutputBuffer().size()));
    }

    @Test
    public void testDateTimeSerializerBaseSerializeWithCustomFormat() throws IOException {
        JsonFactory factory = new JsonFactory();
        SimpleDateFormat customFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss", Locale.ENGLISH);
        DateTimeSerializerBase<Date> serializer = new DateTimeSerializerBase<Date>(Date.class, false, customFormat);

        ObjectMapper mapper = new ObjectMapper(factory);
        SerializerProvider serializers = mapper.getSerializerProvider();

        JsonGenerator gen = mapper.getFactory().createGenerator(new ByteArrayOutputStream(), new DefaultSerializerProvider(), null);
        Date date = new Date();

        serializer.serialize(date, gen, serializers);
        gen.flush();
        assertEquals("\"14-03-2023 12:34:56\"", new String(gen.getOutputBuffer().base, 0, gen.getOutputBuffer().size()));

        AtomicReference<DateFormat> reusedFormat = new AtomicReference<>();
        DateTimeSerializerBase<Date> serializerWithReusedFormat = new DateTimeSerializerBase<Date>(Date.class, false, null) {
            @Override
            protected DateFormat _getCustomFormat() {
                DateFormat format = reusedFormat.getAndSet(null);
                if (format == null) {
                    format = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss", Locale.ENGLISH);
                }
                return format;
            }
        };

        ObjectMapper mapperWithReusedFormat = new ObjectMapper(factory);
        SerializerProvider serializersWithReusedFormat = mapperWithReusedFormat.getSerializerProvider();

        JsonGenerator genWithReusedFormat = mapperWithReusedFormat.getFactory().createGenerator(new ByteArrayOutputStream(), new DefaultSerializerProvider(), null);
        Date dateWithReusedFormat = new Date();

        serializerWithReusedFormat.serialize(dateWithReusedFormat, genWithReusedFormat, serializersWithReusedFormat);
        genWithReusedFormat.flush();
        assertEquals("\"14-03-2023 12:34:56\"", new String(genWithReusedFormat.getOutputBuffer().base, 0, genWithReusedFormat.getOutputBuffer().size()));

        assertEquals(customFormat, serializerWithReusedFormat._customFormat);
        assertEquals(customFormat, serializerWithReusedFormat._reusedCustomFormat.get());
    }
}