import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.junit.Test;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class DateTimeSerializerBaseTest {

    private ObjectMapper mapper;

    public DateTimeSerializerBaseTest() {
        mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
    }

    @Test
    public void testDefaultSerializationWithCustomFormat() throws JsonGenerationException, JsonMappingException, IOException {
        DateTimeSerializerBase<LocalDateTime> serializer = new DateTimeSerializerBase<>() {
            @Override
            protected long _timestamp(LocalDateTime value) {
                return value.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
            }

            @Override
            protected void _serializeAsString(Date value, JsonGenerator g, SerializerProvider provider) throws IOException {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                g.writeString(formatter.format(value.toInstant().atZone(ZoneId.systemDefault())));
            }

            @Override
            public DateTimeSerializerBase<LocalDateTime> withFormat(Boolean timestamp, DateFormat customFormat) {
                return new DateTimeSerializerBase<LocalDateTime>(LocalDateTime.class, false, formatter) {
                    @Override
                    protected void _serializeAsString(Date value, JsonGenerator g, SerializerProvider provider) throws IOException {
                        super._serializeAsString(value, g, provider);
                    }
                };
            }
        };

        ObjectNode node = mapper.writeValueAsTree(LocalDateTime.now(), serializer);
        ObjectNode actual = node.path("@class");
        ObjectNode expected = mapper.valueToTree("com.fasterxml.jackson.databind.ser.std.DateTimeSerializerBase");
        assertEquals(expected, actual);

        actual = node.path("value");
        LocalDateTime localDateTime = mapper.readValue(actual.toString(), LocalDateTime.class);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String expectedString = formatter.format(LocalDateTime.now());
        assertEquals(expectedString, actual.toString());
        assertEquals(localDateTime, LocalDateTime.now());
    }

    @Test
    public void testSerializationWithCustomLocaleAndTimeZone() throws JsonGenerationException, JsonMappingException, IOException {
        DateTimeSerializerBase<LocalDateTime> serializer = new DateTimeSerializerBase<>() {
            @Override
            protected long _timestamp(LocalDateTime value) {
                return value.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
            }

            @Override
            public DateTimeSerializerBase<LocalDateTime> withFormat(Boolean timestamp, DateFormat customFormat) {
                return new DateTimeSerializerBase<LocalDateTime>(LocalDateTime.class, false, customFormat) {
                    @Override
                    protected void _serializeAsString(Date value, JsonGenerator g, SerializerProvider provider) throws IOException {
                        super._serializeAsString(value, g, provider);
                    }
                };
            }
        };

        DateFormat customFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss", Locale.FRENCH);
        customFormat.setTimeZone(TimeZone.getTimeZone("Europe/Paris"));

        ObjectNode node = mapper.writeValueAsTree(LocalDateTime.now(), serializer.withFormat(false, customFormat));
        ObjectNode actual = node.path("value");
        String expectedString = "12-12-2022 12:12:12";
        assertEquals(expectedString, actual.toString());
        LocalDateTime localDateTime = mapper.readValue(actual.toString(), LocalDateTime.class);
        assertEquals(LocalDateTime.now(), localDateTime);
    }

    @Test
    public void testSerializationWithNullValue() throws JsonGenerationException, JsonMappingException, IOException {
        DateTimeSerializerBase<LocalDateTime> serializer = new DateTimeSerializerBase<>() {
            @Override
            protected long _timestamp(LocalDateTime value) {
                return value.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
            }

            @Override
            public DateTimeSerializerBase<LocalDateTime> withFormat(Boolean timestamp, DateFormat customFormat) {
                return new DateTimeSerializerBase<LocalDateTime>(LocalDateTime.class, false, null) {
                    @Override
                    public void serialize(LocalDateTime value, JsonGenerator g, SerializerProvider provider)
                            throws IOException {
                        if (value == null) {
                            g.writeNull();
                        } else {
                            super.serialize(value, g, provider);
                        }
                    }
                };
            }
        };

        ObjectNode node = mapper.writeValueAsTree(null, serializer);
        assertNull(node.path("value"));
    }

    @Test(expected = JsonMappingException.class)
    public void testSerializationWithInvalidFormat() throws JsonGenerationException, JsonMappingException, IOException {
        DateTimeSerializerBase<LocalDateTime> serializer = new DateTimeSerializerBase<>() {
            @Override
            protected long _timestamp(LocalDateTime value) {
                return value.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
            }

            @Override
            public DateTimeSerializerBase<LocalDateTime> withFormat(Boolean timestamp, DateFormat customFormat) {
                return new DateTimeSerializerBase<LocalDateTime>(LocalDateTime.class, false, new SimpleDateFormat("invalid")) {
                    @Override
                    protected void _serializeAsString(Date value, JsonGenerator g, SerializerProvider provider) throws IOException {
                        super._serializeAsString(value, g, provider);
                    }
                };
            }
        };

        ObjectNode node = mapper.writeValueAsTree(LocalDateTime.now(), serializer);
        throw node.get("@type").asText().equals("com.fasterxml.jackson.core.JsonParseException")
                ? (JsonParseException) node.path("__value")
                : new JsonMappingException(node.toString());
    }

    @Test
    public void testDeserializationWithCustomFormat() throws JsonGenerationException, JsonMappingException, IOException {
        DateTimeSerializerBase<LocalDateTime> serializer = new DateTimeSerializerBase<>() {
            @Override
            protected long _timestamp(LocalDateTime value) {
                return value.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
            }

            @Override
            public DateTimeSerializerBase<LocalDateTime> withFormat(Boolean timestamp, DateFormat customFormat) {
                return new DateTimeSerializerBase<LocalDateTime>(LocalDateTime.class, false, customFormat) {
                    @Override
                    protected void _deserializeFromString(String text, DeserializationContext ctxt, BeanProperty property)
                            throws IOException {
                        if (text == null) {
                            throw ctxt.wrongBaseType(LocalDateTime.class, null);
                        }
                        LocalDateTime localDateTime = LocalDateTime.parse(text, customFormat);
                        return localDateTime;
                    }
                };
            }
        };

        ObjectNode node = mapper.createObjectNode();
        node.put("value", "2022-12-12 12:12:12");

        LocalDateTime localDateTime = mapper.readValue(node, LocalDateTime.class, serializer);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String expectedString = formatter.format(LocalDateTime.now());
        assertEquals(expectedString, localDateTime.toString());
        assertEquals(LocalDateTime.parse("2022-12-12 12:12:12", formatter), localDateTime);
    }

    @Test
    public void testDeserializationWithNullValue() throws JsonGenerationException, JsonMappingException, IOException {
        DateTimeSerializerBase<LocalDateTime> serializer = new DateTimeSerializerBase<>() {
            @Override
            protected long _timestamp(LocalDateTime value) {
                return value.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
            }

            @Override
            public DateTimeSerializerBase<LocalDateTime> withFormat(Boolean timestamp, DateFormat customFormat) {
                return new DateTimeSerializerBase<LocalDateTime>(LocalDateTime.class, false, null) {
                    @Override
                    protected LocalDateTime _deserializeFromString(String text, DeserializationContext ctxt, BeanProperty property)
                            throws IOException {
                        if (text == null) {
                            return null;
                        }
                        return LocalDateTime.parse(text, formatter);
                    }
                };
            }
        };

        ObjectNode node = mapper.createObjectNode();
        node.put("value", null);

        LocalDateTime localDateTime = mapper.readValue(node, LocalDateTime.class, serializer);
        assertNull(localDateTime);
    }

    @Test(expected = JsonMappingException.class)
    public void testDeserializationWithInvalidFormat() throws JsonGenerationException, JsonMappingException, IOException {
        DateTimeSerializerBase<LocalDateTime> serializer = new DateTimeSerializerBase<>() {
            @Override
            protected long _timestamp(LocalDateTime value) {
                return value.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
            }

            @Override
            public DateTimeSerializerBase<LocalDateTime> withFormat(Boolean timestamp, DateFormat customFormat) {
                return new DateTimeSerializerBase<LocalDateTime>(LocalDateTime.class, false, new SimpleDateFormat("invalid")) {
                    @Override
                    protected LocalDateTime _deserializeFromString(String text, DeserializationContext ctxt, BeanProperty property)
                            throws IOException {
                        return LocalDateTime.parse(text, customFormat);
                    }
                };
            }
        };

        ObjectNode node = mapper.createObjectNode();
        node.put("value", "invalid_date");

        throw node.get("@type").asText().equals("com.fasterxml.jackson.databind.JsonMappingException")
                ? (JsonMappingException) node.path("__value")
                : new JsonMappingException(node.toString());
    }
}