package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.*;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.jsonFormatVisitors.*;
import com.fasterxml.jackson.databind.ser.*;
import com.fasterxml.jackson.databind.util.StdDateFormat;

public class DateTimeSerializerBaseTest<T> {

    private static final Class<T> HANDLED_TYPE = Integer.TYPE;

    @SuppressWarnings("unchecked")
    private DateTimeSerializerBase<T> createSerializer(Boolean useTimestamp, DateFormat customFormat) {
        return new DateTimeSerializerBase<T>() {
            {
                super.handledType = HANDLED_TYPE;
                this._useTimestamp = useTimestamp;
                this._customFormat = customFormat;
                this._reusedCustomFormat = new AtomicReference<DateFormat>();
            }
        };
    }

    @Test
    public void testCreateContextual() throws Exception {
        JsonFactory jsonFactory = new JsonFactory();
        ObjectMapper mapper = new ObjectMapper(jsonFactory);
        SerializerProvider serializers = mapper.getSerializerProvider();

        // Test with null values
        DateTimeSerializerBase<T> serializer1 = createSerializer(null, null);
        JsonSerializer<?> contextualSerializer1 = serializer1.createContextual(serializers, null);
        assertEquals(serializer1, contextualSerializer1);

        DateTimeSerializerBase<T> serializer2 = createSerializer(Boolean.TRUE, null);
        JsonSerializer<?> contextualSerializer2 = serializer2.createContextual(serializers, null);
        assertEquals(serializer2, contextualSerializer2);

        // Test with custom format
        SimpleDateFormat customFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        DateTimeSerializerBase<T> serializer3 = createSerializer(Boolean.FALSE, customFormat);
        JsonSerializer<?> contextualSerializer3 = serializer3.createContextual(serializers, null);
        assertEquals(serializer3, contextualSerializer3);
        assertTrue(contextualSerializer3 instanceof StdSerializers.StandardDateSerializer);

        // Test with serializers has custom format
        serializers.setDateFormat(customFormat);
        DateTimeSerializerBase<T> serializer4 = createSerializer(Boolean.FALSE, null);
        JsonSerializer<?> contextualSerializer4 = serializer4.createContextual(serializers, null);
        assertEquals(serializer4, contextualSerializer4);
        assertTrue(contextualSerializer4 instanceof StdSerializers.StandardDateSerializer);
        assertEquals(customFormat, contextualSerializer4.getFormat());
    }

    @Test
    public void testSerialize() throws IOException, ParseException {
        ObjectMapper mapper = new ObjectMapper();
        SerializerProvider serializers = mapper.getSerializerProvider();

        SimpleDateFormat customFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        DateTimeSerializerBase<Date> serializer = createSerializer(Boolean.FALSE, customFormat);

        JsonGenerator gen = mapper.getFactory().createGenerator(System.out);
        Date value = new Date(System.currentTimeMillis());

        serializer.serialize(value, gen, serializers);
        gen.flush();

        JsonParser jsonParser = mapper.getFactory().createParser(new StringReader(gen.getOutputBuffer().toString()));
        assertTrue(jsonParser.nextValue().isTextual());
        assertEquals(customFormat.format(value), jsonParser.getText());

        // Test with custom format not set
        jsonParser = mapper.getFactory().createParser(new StringReader("\"2023-03-31 12:34:56\""));
        Date deserializedValue = mapper.readValue(jsonParser, Date.class);
        assertEquals(value.getTime(), deserializedValue.getTime());

        // Test with useTimestamp
        serializer = createSerializer(Boolean.TRUE, null);
        gen = mapper.getFactory().createGenerator(System.out);
        serializer.serialize(value, gen, serializers);
        gen.flush();

        jsonParser = mapper.getFactory().createParser(new StringReader(gen.getOutputBuffer().toString()));
        assertEquals(value.getTime(), jsonParser.nextLongValue());
    }
}