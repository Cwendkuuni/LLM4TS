package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.TimeZone;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.AnnotationIntrospector;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitorWrapper;
import com.fasterxml.jackson.databind.type.JavaType;
import com.fasterxml.jackson.databind.util.StdDateFormat;

public class DateTimeSerializerBaseTest {

    @SuppressWarnings("unchecked")
    @Test
    public void testDateTimeSerializerBase() throws IOException, JsonMappingException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);

        // Test serialization with no custom format or timestamp flag
        DateTimeSerializerBase<Long> serializer = new StdSerializers.LongSerializer();
        assertEquals("2013-12-12T12:12:12.123Z", serializeDate("2013-12-12 12:12:12.123", serializer, mapper));

        // Test serialization with custom format
        DateTimeSerializerBase<String> serializerWithFormat = new StdSerializers.StringSerializer() {
            @Override
            public DateTimeSerializerBase<String> withFormat(Boolean timestamp, DateFormat customFormat) {
                return new StdSerializers.StringSerializer() {
                    @Override
                    public void serialize(String value, JsonGenerator gen, SerializerProvider serializers)
                            throws IOException {
                        if (customFormat != null) {
                            gen.writeString(customFormat.format(new Date(value.getTime())));
                        } else {
                            super.serialize(value, gen, serializers);
                        }
                    }
                };
            }
        }.withFormat(Boolean.FALSE, new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS"));
        assertEquals("2013-12-12T12:12:12.123Z", serializeDate("2013-12-12 12:12:12.123", serializerWithFormat, mapper));
        assertEquals("2013-12-12-12:12:12.123", serializeDate("2013-12-12 12:12:12.123", serializerWithFormat, mapper, Locale.US));

        // Test serialization with timestamp flag
        DateTimeSerializerBase<Long> serializerWithTimestamp = new StdSerializers.LongSerializer() {
            @Override
            public DateTimeSerializerBase<Long> withFormat(Boolean timestamp, DateFormat customFormat) {
                return new StdSerializers.LongSerializer();
            }
        }.withFormat(Boolean.TRUE, null);
        assertEquals("1386832032000", serializeDate("2013-12-12 12:12:12.123", serializerWithTimestamp, mapper));

        // Test serialization with custom format and timestamp flag
        DateTimeSerializerBase<Long> serializerWithCustomTimestampFormat = new StdSerializers.LongSerializer() {
            @Override
            public DateTimeSerializerBase<Long> withFormat(Boolean timestamp, DateFormat customFormat) {
                return new StdSerializers.LongSerializer() {
                    @Override
                    public void serialize(Long value, JsonGenerator gen, SerializerProvider serializers)
                            throws IOException {
                        if (timestamp) {
                            gen.writeNumber(value.longValue() * 1000);
                        } else {
                            gen.writeString(customFormat.format(new Date(value.longValue() * 1000)));
                        }
                    }
                };
            }
        }.withFormat(Boolean.TRUE, new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS"));
        assertEquals("1386832032000", serializeDate("2013-12-12 12:12:12.123", serializerWithCustomTimestampFormat, mapper));
        assertEquals("2013-12-12T12:12:12.123Z", serializeDate("2013-12-12 12:12:12.123", serializerWithCustomTimestampFormat, mapper, Locale.US));

        // Test deserialization with no custom format or timestamp flag
        Long deserializedValue = deserializeDate("2013-12-12T12:12:12.123", serializer, mapper);
        assertEquals(new Date(1386832032000L).getTime(), deserializedValue.longValue() * 1000);

        // Test deserialization with custom format
        String deserializedValueWithFormat = deserializeDate("2013-12-12-12:12:12.123", serializerWithFormat, mapper, Locale.US);
        assertEquals("2013-12-12 12:12:12.123", deserializedValueWithFormat);

        // Test deserialization with timestamp flag
        Long deserializedValueWithTimestamp = deserializeDate("1386832032000", serializerWithTimestamp, mapper);
        assertEquals(new Date(1386832032000L).getTime(), deserializedValueWithTimestamp.longValue() * 1000);

        // Test deserialization with custom format and timestamp flag
        Long deserializedValueWithCustomTimestampFormat = deserializeDate("2013-12-12T12:12:12.123", serializerWithCustomTimestampFormat, mapper);
        assertEquals(new Date(1386832032000L).getTime(), deserializedValueWithCustomTimestampFormat.longValue() * 1000);
        deserializedValueWithCustomTimestampFormat = deserializeDate("2013-12-12-12:12:12.123", serializerWithCustomTimestampFormat, mapper, Locale.US);
        assertEquals(new Date(1386832032000L).getTime(), deserializedValueWithCustomTimestampFormat.longValue() * 1000);

        // Test deserialization with null provider
        try {
            deserializeDate("2013-12-12T12:12:12.123", serializerWithCustomTimestampFormat, null);
            fail();
        } catch (JsonMappingException e) {
            // Expected
        }

        // Test deserialization with provider that has write_dates_as_timestamps enabled
        ObjectMapper mapperWithTimestamp = new ObjectMapper();
        mapperWithTimestamp.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, true);
        Long deserializedValueWithTimestampProvider = deserializeDate("2013-12-12T12:12:12.123", serializerWithCustomTimestampFormat, mapperWithTimestamp);
        assertEquals(new Date(1386832032000L).getTime(), deserializedValueWithTimestampProvider.longValue() * 1000);
    }

    private String serializeDate(String dateString, DateTimeSerializerBase<?> serializer, ObjectMapper mapper)
            throws JsonMappingException, IOException {
        StringWriter writer = new StringWriter();
        JsonGenerator gen = mapper.getFactory().createGenerator(writer);
        serializer.serialize(null, gen, mapper);
        gen.writeFieldName("value");
        serializer.serialize(new Date(System.currentTimeMillis()), gen, mapper);
        gen.writeEndObject();
        gen.close();
        gen = mapper.getFactory().createGenerator(new ByteArrayOutputStream());
        mapper.writeValue(gen, mapper.readValue(new ByteArrayInputStream(writer.getBuffer().getBytes()), JavaType.valueOf(serializer.handledType().getCanonicalName())));
        return gen.getOutputBuffer().toString();
    }

    private Object deserializeDate(String dateString, DateTimeSerializerBase<?> serializer, ObjectMapper mapper)
            throws JsonMappingException, IOException {
        ObjectReader reader = mapper.getFactory().createObjectReader(new ByteArrayInputStream(dateString.getBytes()));
        return reader.readValueAs(JavaType.valueOf(serializer.handledType().getCanonicalName()));
    }

    private Object deserializeDate(String dateString, DateTimeSerializerBase<?> serializer, ObjectMapper mapper, Locale locale)
            throws JsonMappingException, IOException {
        ObjectReader reader = mapper.readerFor(JavaType.valueOf(serializer.handledType().getCanonicalName()))
                .with(new JsonDeserializer.DateDeserializer() {
                    @Override
                    public Date deserialize(JsonParser p, DeserializationContext ctxt)
                            throws IOException, JsonProcessingException {
                        JsonNode node = p.readValueAs(JsonNode.class);
                        String formattedDate = node.get("value").asText();
                        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS", locale);
                        return df.parse(formattedDate);
                    }
                });
        return reader.readValue(JsonFactory.getInstance());
    }
}