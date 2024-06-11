import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class DateTimeSerializerBaseTest {

    private static final TimeZone TIMEZONE = TimeZone.getTimeZone("GMT");
    private static final String CUSTOM_PATTERN = "yyyy-MM-dd HH:mm:ss";
    private static final Locale LOCALE = Locale.US;

    private DateTimeSerializerBase<Object> serializer;

    @Test
    public void testSerializeWithoutTimestamp() throws IOException {
        // Arrange
        serializer = new DateTimeSerializerBase<Object>(Object.class, Boolean.FALSE, null) {
            @Override
            public DateTimeSerializerBase<Object> withFormat(Boolean timestamp, DateFormat customFormat) {
                return null;
            }

            @Override
            protected long _timestamp(Object value) {
                return 0;
            }

            @Override
            public void serialize(Object value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
                // Dummy implementation
            }
        };

        // Act
        boolean isEmpty = serializer.isEmpty(null);

        // Assert
        Assert.assertTrue(isEmpty);
    }

    @Test
    public void testSerializeWithTimestamp() throws IOException {
        // Arrange
        serializer = new DateTimeSerializerBase<Object>(Object.class, Boolean.TRUE, null) {
            @Override
            public DateTimeSerializerBase<Object> withFormat(Boolean timestamp, DateFormat customFormat) {
                return null;
            }

            @Override
            protected long _timestamp(Object value) {
                return 0;
            }

            @Override
            public void serialize(Object value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
                // Dummy implementation
            }
        };

        // Act
        boolean isEmpty = serializer.isEmpty(null);

        // Assert
        Assert.assertTrue(isEmpty);
    }

    @Test
    public void testSerializeWithCustomFormat() throws IOException {
        // Arrange
        DateFormat customFormat = new SimpleDateFormat(CUSTOM_PATTERN, LOCALE);
        serializer = new DateTimeSerializerBase<Object>(Object.class, Boolean.FALSE, customFormat) {
            @Override
            public DateTimeSerializerBase<Object> withFormat(Boolean timestamp, DateFormat customFormat) {
                return null;
            }

            @Override
            protected long _timestamp(Object value) {
                return 0;
            }

            @Override
            public void serialize(Object value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
                // Dummy implementation
            }
        };

        // Act
        boolean isEmpty = serializer.isEmpty(null);

        // Assert
        Assert.assertTrue(isEmpty);
    }

    @Test
    public void testGetSchemaShouldReturnNumberFormatWhenTimestampIsUsed() {
        // Arrange
        serializer = new DateTimeSerializerBase<Object>(Object.class, Boolean.TRUE, null) {
            @Override
            public DateTimeSerializerBase<Object> withFormat(Boolean timestamp, DateFormat customFormat) {
                return null;
            }

            @Override
            protected long _timestamp(Object value) {
                return 0;
            }

            @Override
            public void serialize(Object value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
                // Dummy implementation
            }
        };

        // Act
        JsonNode schema = serializer.getSchema(null, Object.class);

        // Assert
        Assert.assertEquals("number", schema.get("type").asText());
        Assert.assertTrue(schema.get("required").asBoolean());
    }

    @Test
    public void testGetSchemaShouldReturnStringFormatWhenCustomFormatIsUsed() {
        // Arrange
        DateFormat customFormat = new SimpleDateFormat(CUSTOM_PATTERN, LOCALE);
        serializer = new DateTimeSerializerBase<Object>(Object.class, Boolean.FALSE, customFormat) {
            @Override
            public DateTimeSerializerBase<Object> withFormat(Boolean timestamp, DateFormat customFormat) {
                return null;
            }

            @Override
            protected long _timestamp(Object value) {
                return 0;
            }

            @Override
            public void serialize(Object value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
                // Dummy implementation
            }
        };

        // Act
        JsonNode schema = serializer.getSchema(null, Object.class);

        // Assert
        Assert.assertEquals("string", schema.get("type").asText());
        Assert.assertTrue(schema.get("required").asBoolean());
    }

    @Test
    public void testSerializeWithNullValueShouldReturnTrueWhenUseTimestampIsNullAndCustomformatIsNull() {
        // Arrange
        serializer = new DateTimeSerializerBase<Object>(Object.class, null, null) {
            @Override
            public DateTimeSerializerBase<Object> withFormat(Boolean timestamp, DateFormat customFormat) {
                return null;
            }

            @Override
            protected long _timestamp(Object value) {
                return 0;
            }

            @Override
            public void serialize(Object value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
                // Dummy implementation
            }
        };

        // Act
        boolean isEmpty = serializer.isEmpty(null, null);

        // Assert
        Assert.assertTrue(isEmpty);
    }

    @Test
    public void testSerializeWithNullValueShouldReturnTrueWhenUseTimestampIsFalseAndCustomFormatIsNullAndSerializeIsEnabled() {
        // Arrange
        serializer = new DateTimeSerializerBase<Object>(Object.class, Boolean.FALSE, null) {
            @Override
            public DateTimeSerializerBase<Object> withFormat(Boolean timestamp, DateFormat customFormat) {
                return null;
            }

            @Override
            protected long _timestamp(Object value) {
                return 0;
            }

            @Override
            public void serialize(Object value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
                // Dummy implementation
            }
        };

        // Act
        boolean isEmpty = serializer.isEmpty(null, null);

        // Assert
        Assert.assertTrue(isEmpty);
    }

    @Test
    public void testSerializeWithNullValueShouldReturnTrueWhenUseTimestampIsFalseAndCustomFormatIsNullAndSerializeIsDisabled() {
        // Arrange
        SerializerProvider serializers = new SerializerProvider();
        serializers.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        serializer = new DateTimeSerializerBase<Object>(Object.class, Boolean.FALSE, null) {
            @Override
            public DateTimeSerializerBase<Object> withFormat(Boolean timestamp, DateFormat customFormat) {
                return null;
            }

            @Override
            protected long _timestamp(Object value) {
                return 0;
            }

            @Override
            public void serialize(Object value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
                // Dummy implementation
            }
        };

        // Act
        boolean isEmpty = serializer.isEmpty(serializers, null);

        // Assert
        Assert.assertTrue(isEmpty);
    }

    @Test
    public void testAcceptJsonFormatVisitorShouldVisitLongFormatWhenAsTimestampIsTrue() throws JsonMappingException {
        // Arrange
        serializer = new DateTimeSerializerBase<Object>(Object.class, Boolean.TRUE, null) {
            @Override
            public DateTimeSerializerBase<Object> withFormat(Boolean timestamp, DateFormat customFormat) {
                return null;
            }

            @Override
            protected long _timestamp(Object value) {
                return 0;
            }

            @Override
            public void serialize(Object value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
                // Dummy implementation
            }
        };

        MockJsonFormatVisitorWrapper visitorWrapper = new MockJsonFormatVisitorWrapper();

        // Act
        serializer.acceptJsonFormatVisitor(visitorWrapper, Object.class);

        // Assert
        Assert.assertEquals(JsonParser.NumberType.LONG, visitorWrapper.getNumberType());
        Assert.assertEquals(JsonValueFormat.UTC_MILLISEC, visitorWrapper.getValueFormat());
    }

    @Test
    public void testAcceptJsonFormatVisitorShouldVisitStringFormatWhenAsTimestampIsFalse() throws JsonMappingException {
        // Arrange
        serializer = new DateTimeSerializerBase<Object>(Object.class, Boolean.FALSE, null) {
            @Override
            public DateTimeSerializerBase<Object> withFormat(Boolean timestamp, DateFormat customFormat) {
                return null;
            }

            @Override
            protected long _timestamp(Object value) {
                return 0;
            }

            @Override
            public void serialize(Object value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
                // Dummy implementation
            }
        };

        MockJsonFormatVisitorWrapper visitorWrapper = new MockJsonFormatVisitorWrapper();

        // Act
        serializer.acceptJsonFormatVisitor(visitorWrapper, Object.class);

        // Assert
        Assert.assertEquals(JsonValueFormat.DATE_TIME, visitorWrapper.getValueFormat());
    }

    // Tests for public methods

    @Test
    public void testWithFormatShouldReturnDateTimeSerializerBaseWithNewFormat() {
        // Arrange
        serializer = new DateTimeSerializerBase<Object>(Object.class, Boolean.FALSE, null) {
            @Override
            public DateTimeSerializerBase<Object> withFormat(Boolean timestamp, DateFormat customFormat) {
                return null;
            }

            @Override
            protected long _timestamp(Object value) {
                return 0;
            }

            @Override
            public void serialize(Object value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
                // Dummy implementation
            }
        };

        // Act
        DateTimeSerializerBase<Object> newSerializer = serializer.withFormat(Boolean.TRUE, new SimpleDateFormat(CUSTOM_PATTERN, LOCALE));

        // Assert
        Assert.assertNotNull(newSerializer);
        Assert.assertTrue(newSerializer instanceof DateTimeSerializerBase);
        Assert.assertEquals(Boolean.TRUE, newSerializer._useTimestamp);
        Assert.assertNotNull(newSerializer._customFormat);
    }

    // Helper class for testing

    private static class MockJsonFormatVisitorWrapper implements JsonFormatVisitorWrapper {

        private JsonParser.NumberType numberType;
        private JsonValueFormat valueFormat;

        @Override
        public JsonObjectFormatVisitor expectObjectFormat(JavaType type) throws JsonMappingException {
            return null;
        }

        @Override
        public JsonArrayFormatVisitor expectArrayFormat(JavaType type) throws JsonMappingException {
            return null;
        }

        @Override
        public JsonStringFormatVisitor expectStringFormat(JavaType type) throws JsonMappingException {
            return null;
        }

        @Override
        public JsonNumberFormatVisitor expectNumberFormat(JavaType type) throws JsonMappingException {
            return new JsonNumberFormatVisitor() {
                @Override
                public void numberType(JsonParser.NumberType type) throws JsonMappingException {
                    numberType = type;
                }

                @Override
                public void format(JsonValueFormat format) throws JsonMappingException {
                    valueFormat = format;
                }
            };
        }

        @Override
        public JsonIntegerFormatVisitor expectIntegerFormat(JavaType type) throws JsonMappingException {
            return null;
        }

        public JsonParser.NumberType getNumberType() {
            return numberType;
        }

        public JsonValueFormat getValueFormat() {
            return valueFormat;
        }
    }
}