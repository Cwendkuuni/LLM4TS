package com.fasterxml.jackson.databind.ser.std;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitorWrapper;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import com.fasterxml.jackson.databind.util.StdDateFormat;

import org.junit.Test;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;
import java.util.concurrent.atomic.AtomicReference;

import static org.assertj.core.api.Assertions.assertThat;

public class DateTimeSerializerBaseTest {

    @Test
    public void testConstructor() {
        DateTimeSerializerBase<DateTime> serializer = new DateTimeSerializerBase<DateTime>(DateTime.class, true, null) {
            @Override
            public DateTimeSerializerBase<DateTime> withFormat(Boolean timestamp, DateFormat customFormat) {
                return null;
            }

            @Override
            protected long _timestamp(DateTime value) {
                return 0;
            }

            @Override
            public void serialize(DateTime value, JsonGenerator gen, SerializerProvider serializers) throws IOException {

            }
        };

        assertThat(serializer).isNotNull();
        assertThat(serializer.getSupportedType()).isEqualTo(DateTime.class);
        assertThat(serializer._useTimestamp).isEqualTo(true);
        assertThat(serializer._customFormat).isNull();
        assertThat(serializer._reusedCustomFormat).isNull();
    }

    @Test
    public void testContextualSerializerWithNumericFormat() throws JsonMappingException {
        DateTimeSerializerBase<DateTime> serializer = new DateTimeSerializerBase<DateTime>(DateTime.class, null, null) {
            @Override
            public DateTimeSerializerBase<DateTime> withFormat(Boolean timestamp, DateFormat customFormat) {
                return null;
            }

            @Override
            protected long _timestamp(DateTime value) {
                return 0;
            }

            @Override
            public void serialize(DateTime value, JsonGenerator gen, SerializerProvider serializers) throws IOException {

            }
        };

        SerializerProvider provider = null;
        BeanProperty property = null;

        JsonSerializer<?> contextualSerializer = serializer.createContextual(provider, property);

        assertThat(contextualSerializer).isEqualTo(serializer);
        assertThat(serializer._useTimestamp).isEqualTo(true);
        assertThat(serializer._customFormat).isNull();
        assertThat(serializer._reusedCustomFormat).isNull();
    }

    @Test
    public void testContextualSerializerWithCustomFormat() throws JsonMappingException {
        final DateFormat customFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.US);

        DateTimeSerializerBase<DateTime> serializer = new DateTimeSerializerBase<DateTime>(DateTime.class, null, null) {
            @Override
            public DateTimeSerializerBase<DateTime> withFormat(Boolean timestamp, DateFormat customFormat) {
                return null;
            }

            @Override
            protected long _timestamp(DateTime value) {
                return 0;
            }

            @Override
            public void serialize(DateTime value, JsonGenerator gen, SerializerProvider serializers) throws IOException {

            }
        };

        SerializerProvider provider = null;
        BeanProperty property = null;

        JsonSerializer<?> contextualSerializer = serializer.createContextual(provider, property);

        assertThat(contextualSerializer).isEqualTo(serializer);
        assertThat(serializer._useTimestamp).isEqualTo(false);
        assertThat(serializer._customFormat).isEqualTo(customFormat);
        assertThat(serializer._reusedCustomFormat).isNull();
    }

    @Test
    public void testGetSchemaWithTimestamp() {
        DateTimeSerializerBase<DateTime> serializer = new DateTimeSerializerBase<DateTime>(DateTime.class, true, null) {
            @Override
            public DateTimeSerializerBase<DateTime> withFormat(Boolean timestamp, DateFormat customFormat) {
                return null;
            }

            @Override
            protected long _timestamp(DateTime value) {
                return 0;
            }

            @Override
            public void serialize(DateTime value, JsonGenerator gen, SerializerProvider serializers) throws IOException {

            }
        };

        SerializerProvider provider = null;
        String expectedFormat = "number";

        JsonNode schema = serializer.getSchema(provider, null);

        assertThat(schema.get("type").asText()).isEqualTo(expectedFormat);
        assertThat(schema.get("format")).isNull();
    }

    @Test
    public void testGetSchemaWithCustomFormat() {
        DateTimeSerializerBase<DateTime> serializer = new DateTimeSerializerBase<DateTime>(DateTime.class, false, null) {
            @Override
            public DateTimeSerializerBase<DateTime> withFormat(Boolean timestamp, DateFormat customFormat) {
                return null;
            }

            @Override
            protected long _timestamp(DateTime value) {
                return 0;
            }

            @Override
            public void serialize(DateTime value, JsonGenerator gen, SerializerProvider serializers) throws IOException {

            }
        };

        SerializerProvider provider = null;
        String expectedFormat = "string";

        JsonNode schema = serializer.getSchema(provider, null);

        assertThat(schema.get("type").asText()).isEqualTo(expectedFormat);
        assertThat(schema.get("format").asText()).isEqualTo("date-time");
    }

    @Test
    public void testAcceptJsonFormatVisitorWithTimestamp() throws JsonMappingException {
        DateTimeSerializerBase<DateTime> serializer = new DateTimeSerializerBase<DateTime>(DateTime.class, true, null) {
            @Override
            public DateTimeSerializerBase<DateTime> withFormat(Boolean timestamp, DateFormat customFormat) {
                return null;
            }

            @Override
            protected long _timestamp(DateTime value) {
                return 0;
            }

            @Override
            public void serialize(DateTime value, JsonGenerator gen, SerializerProvider serializers) throws IOException {

            }
        };

        JsonFormatVisitorWrapper visitor = null;
        JavaType typeHint = null;

        serializer.acceptJsonFormatVisitor(visitor, typeHint);

        // Uncomment and fill in expected format and type
        // verify(visitor, times(1)).visitIntFormat(eq(typeHint), eq(JsonParser.NumberType.LONG), eq(JsonValueFormat.UTC_MILLISEC));
        // verify(visitor, never()).visitStringFormat(any(), any());
    }

    @Test
    public void testAcceptJsonFormatVisitorWithStringFormat() throws JsonMappingException {
        DateTimeSerializerBase<DateTime> serializer = new DateTimeSerializerBase<DateTime>(DateTime.class, false, null) {
            @Override
            public DateTimeSerializerBase<DateTime> withFormat(Boolean timestamp, DateFormat customFormat) {
                return null;
            }

            @Override
            protected long _timestamp(DateTime value) {
                return 0;
            }

            @Override
            public void serialize(DateTime value, JsonGenerator gen, SerializerProvider serializers) throws IOException {

            }
        };

        JsonFormatVisitorWrapper visitor = null;
        JavaType typeHint = null;

        serializer.acceptJsonFormatVisitor(visitor, typeHint);

        // Uncomment and fill in expected format and type
        // verify(visitor, never()).visitIntFormat(any(), any(), any());
        // verify(visitor, times(1)).visitStringFormat(eq(typeHint), eq(JsonValueFormat.DATE_TIME));
    }

    // Add more test methods to cover all methods in DateTimeSerializerBase
}