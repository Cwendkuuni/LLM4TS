package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.TimeZone;

import com.fasterxml.jackson.databind.JsonGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitorWrapper;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import com.fasterxml.jackson.databind.util.StdDateFormat;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.JsonSerializer;
import java.math.BigInteger;

import org.junit.Before;
import org.junit.Test;

@SuppressWarnings("serial")
public class DateTimeSerializerBaseTest {
    private static final ObjectMapper MAPPER = new ObjectMapper();

    private static final DateFormat CUSTOM_FORMAT = new SimpleDateFormat("yyyy-MM-dd", Locale.US);

    @Test
    public void testWithFormat() {
        DateTimeSerializerBase<DateTime> serializer = new DateTimeSerializerBase<DateTime>(
                DateTime.class, true, CUSTOM_FORMAT) {
            @Override
            public DateTimeSerializerBase<DateTime> withFormat(Boolean timestamp, DateFormat customFormat) {
                return new DateTimeSerializerBase<DateTime>(DateTime.class, timestamp, customFormat) {
                    @Override
                    public void serialize(DateTime value, JsonGenerator gen, SerializerProvider serializers) {
                        // do nothing
                    }

                    @Override
                    protected long _timestamp(DateTime value) {
                        // do nothing
                        return 0;
                    }
                };
            }

            @Override
            public void serialize(DateTime value, JsonGenerator gen, SerializerProvider serializers) {
                // do nothing
            }

            @Override
            protected long _timestamp(DateTime value) {
                // do nothing
                return 0;
            }
        };

        assertEquals(serializer, serializer.withFormat(true, CUSTOM_FORMAT));
    }

    @Test
    public void testCreateContextualWithShapeNumeric() {
        DateTimeSerializerBase<DateTime> serializer = new DateTimeSerializerBase<DateTime>(
                DateTime.class, null, null) {
            @Override
            public DateTimeSerializerBase<DateTime> withFormat(Boolean timestamp, DateFormat customFormat) {
                return new DateTimeSerializerBase<DateTime>(DateTime.class, timestamp, customFormat) {
                    @Override
                    public void serialize(DateTime value, JsonGenerator gen, SerializerProvider serializers) {
                        // do nothing
                    }

                    @Override
                    protected long _timestamp(DateTime value) {
                        // do nothing
                        return 0;
                    }

                    @Override
                    public void acceptJsonFormatVisitor(JsonFormatVisitorWrapper visitor, JavaType typeHint)
                            throws JsonMappingException {
                        // do nothing
                    }
                };
            }

            @Override
            public void serialize(DateTime value, JsonGenerator gen, SerializerProvider serializers) {
                // do nothing
            }

            @Override
            protected long _timestamp(DateTime value) {
                // do nothing
                return 0;
            }
        };

        JsonFormatVisitorWrapper visitor = MAPPER.getJsonFactory().getCodec().visitorWrapper()
                .expectNumberFormat(DateTime.class);
        assertEquals(serializer, serializer.createContextual(null, null));
    }

    @Test
    public void testCreateContextualWithStringShapeAndPattern() {
        DateTimeSerializerBase<DateTime> serializer = new DateTimeSerializerBase<DateTime>(
                DateTime.class, null, null) {
            @Override
            public DateTimeSerializerBase<DateTime> withFormat(Boolean timestamp, DateFormat customFormat) {
                return new DateTimeSerializerBase<DateTime>(DateTime.class, timestamp, customFormat) {
                    @Override
                    public void serialize(DateTime value, JsonGenerator gen, SerializerProvider serializers) {
                        // do nothing
                    }

                    @Override
                    protected long _timestamp(DateTime value) {
                        // do nothing
                        return 0;
                    }

                    @Override
                    public void acceptJsonFormatVisitor(JsonFormatVisitorWrapper visitor, JavaType typeHint)
                            throws JsonMappingException {
                        // do nothing
                    }
                };
            }

            @Override
            public void serialize(DateTime value, JsonGenerator gen, SerializerProvider serializers) {
                // do nothing
            }

            @Override
            protected long _timestamp(DateTime value) {
                // do nothing
                return 0;
            }
        };

        JsonFormatVisitorWrapper visitor = MAPPER.getJsonFactory().getCodec().visitorWrapper()
                .expectStringFormat(DateTime.class, JsonValueFormat.DATE_TIME);
        assertEquals(serializer, serializer.createContextual(null, null));
    }

    @Test
    public void testIsEmpty() {
        DateTimeSerializerBase<DateTime> serializer = new DateTimeSerializerBase<DateTime>(
                DateTime.class, null, null) {
            @Override
            public DateTimeSerializerBase<DateTime> withFormat(Boolean timestamp, DateFormat customFormat) {
                return new DateTimeSerializerBase<DateTime>(DateTime.class, timestamp, customFormat) {
                    @Override
                    public void serialize(DateTime value, JsonGenerator gen, SerializerProvider serializers) {
                        // do nothing
                    }

                    @Override
                    protected long _timestamp(DateTime value) {
                        // do nothing
                        return 0;
                    }
                };
            }

            @Override
            public void serialize(DateTime value, JsonGenerator gen, SerializerProvider serializers) {
                // do nothing
            }

            @Override
            protected long _timestamp(DateTime value) {
                // do nothing
                return 0;
            }
        };

        DateTime dateTime = DateTime.now();
        assertFalse(serializer.isEmpty(dateTime));
        assertTrue(serializer.isEmpty(null));
    }

    @Test
    public void testGetSchema() {
        DateTimeSerializerBase<DateTime> serializer = new DateTimeSerializerBase<DateTime>(
                DateTime.class, null, null) {
            @Override
            public DateTimeSerializerBase<DateTime> withFormat(Boolean timestamp, DateFormat customFormat) {
                return new DateTimeSerializerBase<DateTime>(DateTime.class, timestamp, customFormat) {
                    @Override
                    public void serialize(DateTime value, JsonGenerator gen, SerializerProvider serializers) {
                        // do nothing
                    }

                    @Override
                    protected long _timestamp(DateTime value) {
                        // do nothing
                        return 0;
                    }
                };
            }

            @Override
            public void serialize(DateTime value, JsonGenerator gen, SerializerProvider serializers) {
                // do nothing
            }

            @Override
            protected long _timestamp(DateTime value) {
                // do nothing
                return 0;
            }
        };

        assertEquals(MAPPER.createObjectNode().put("type", "string").put("format", "date-time"),
                serializer.getSchema(null, null));
    }

    @Test
    public void testAcceptJsonFormatVisitor() throws Exception {
        DateTimeSerializerBase<DateTime> serializer = new DateTimeSerializerBase<DateTime>(
                DateTime.class, null, null) {
            @Override
            public DateTimeSerializerBase<DateTime> withFormat(Boolean timestamp, DateFormat customFormat) {
                return new DateTimeSerializerBase<DateTime>(DateTime.class, timestamp, customFormat) {
                    @Override
                    public void serialize(DateTime value, JsonGenerator gen, SerializerProvider serializers) {
                        // do nothing
                    }

                    @Override
                    protected long _timestamp(DateTime value) {
                        // do nothing
                        return 0;
                    }
                };
            }

            @Override
            public void serialize(DateTime value, JsonGenerator gen, SerializerProvider serializers) {
                // do nothing
            }

            @Override
            protected long _timestamp(DateTime value) {
                // do nothing
                return 0;
            }
        };

        JsonFormatVisitorWrapper visitor = MAPPER.getFactory().getCodec().visitorWrapper()
                .expectStringFormat(DateTime.class, JsonValueFormat.DATE_TIME);
        serializer.acceptJsonFormatVisitor(visitor, DateTime.class);
    }

    @Test
    public void testSerialize() throws Exception {
        DateTimeSerializerBase<DateTime> serializer = new DateTimeSerializerBase<DateTime>(
                DateTime.class, null, null) {
            @Override
            public DateTimeSerializerBase<DateTime> withFormat(Boolean timestamp, DateFormat customFormat) {
                return new DateTimeSerializerBase<DateTime>(DateTime.class, timestamp, customFormat) {
                    @Override
                    public void serialize(DateTime value, JsonGenerator gen, SerializerProvider serializers)
                            throws IOException {
                        gen.writeStartObject();
                        gen.writeStringField("timestamp", value.toString());
                        gen.writeEndObject();
                    }

                    @Override
                    protected long _timestamp(DateTime value) {
                        // do nothing
                        return 0;
                    }
                };
            }

            @Override
            public void serialize(DateTime value, JsonGenerator gen, SerializerProvider serializers)
                    throws IOException {
                gen.writeStartObject();
                gen.writeStringField("timestamp", value.toString());
                gen.writeEndObject();
            }

            @Override
            protected long _timestamp(DateTime value) {
                // do nothing
                return 0;
            }
        };

        DateTime dateTime = DateTime.now();
        JsonSerializer<Object> nullSerializer = null;
        SerializerProvider serializers = null;
        DateTime dateTime = DateTime.now();
        String expected = "{\"timestamp\":\"" + dateTime.toString() + "\"}";
        assertEquals(expected, serialize(dateTime, nullSerializer, serializers));
    }

    private String serialize(DateTime value, JsonSerializer<Object> nullSerializer, SerializerProvider providers)
            throws Exception {
        JsonGenerator g = MAPPER.getFactory().createGenerator(new ByteArrayOutputStream());
        serializer.serialize(value, g, providers);
        g.flush();
        return g.getOutputTarget().toString();
    }


    private DateTimeSerializerBase<DateTime> serializer;

    @Before
    public void setUp() {
        serializer = new DateTimeSerializerBase<DateTime>(DateTime.class, null, null) {
            @Override
            public DateTimeSerializerBase<DateTime> withFormat(Boolean timestamp, DateFormat customFormat) {
                return new DateTimeSerializerBase<DateTime>(DateTime.class, timestamp, customFormat) {
                    @Override
                    public void serialize(DateTime value, JsonGenerator gen, SerializerProvider serializers)
                            throws IOException {
                        // do nothing
                    }

                    @Override
                    protected long _timestamp(DateTime value) {
                        // do nothing
                        return 0;
                    }
                };
            }

            @Override
            public void serialize(DateTime value, JsonGenerator gen, SerializerProvider serializers)
                    throws IOException {
                // do nothing
            }

            @Override
            protected long _timestamp(DateTime value) {
                // do nothing
                return 0;
            }
        };

    }

    @Test
    public void testStripLeadingAndTrailingQuotes() {
        assertEquals("foo", Util.stripLeadingAndTrailingQuotes("'''foo'''"));
        assertEquals("foo", Util.stripLeadingAndTrailingQuotes("'foo'"));
        assertEquals("foo", Util.stripLeadingAndTrailingQuotes("\"foo\""));
        assertEquals("foo", Util.stripLeadingAndTrailingQuotes("\"\"foo\"\""));
        assertEquals("", Util.stripLeadingAndTrailingQuotes("\"\""));
    }

    @Test
    public void testStripLeadingHyphens() {
        assertEquals("f", Util.stripLeadingHyphens("-f"));
        assertEquals("foo", Util.stripLeadingHyphens("--foo"));
        assertEquals("-foo", Util.stripLeadingHyphens("---foo"));
        assertNull(Util.stripLeadingHyphens(null));
    }
}