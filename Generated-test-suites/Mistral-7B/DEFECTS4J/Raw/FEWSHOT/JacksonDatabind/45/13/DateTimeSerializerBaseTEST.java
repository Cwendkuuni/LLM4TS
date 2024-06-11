package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.TimeZone;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.introspect.Annotated;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import com.fasterxml.jackson.databind.ser.std.DateTimeSerializerBase;
import com.fasterxml.jackson.databind.ser.std.StdScalarSerializer;
import com.fasterxml.jackson.databind.util.StdDateFormat;

public class DateTimeSerializerBaseTest {

    @SuppressWarnings("unchecked")
    @Test
    public void testDateTimeSerializerBase() {
        DateTimeSerializerBase<Long> longSerializer = new StdDateSerializer(Long.TYPE, null, null);
        DateTimeSerializerBase<String> stringSerializer = new StdDateSerializer(String.class, false, null);

        // Test with null values
        assertNull(longSerializer.serialize(null, null, null));
        assertNull(stringSerializer.serialize(null, null, null));

        // Test serialization with default settings
        Long timestamp = 123L;
        String dateString = "2022-12-31T23:59:59.999Z";

        try {
            String jsonWithTimestamp = longSerializer.serializeValueAsString(timestamp, new JsonGenerator.Overrides(), null);
            assertTrue(jsonWithTimestamp.matches("^[-\\+]?[0-9]+(\\.[0-9]+)?$")); // Numeric format

            JsonParser jsonParser = new ObjectMapper().readTree(jsonWithTimestamp);
            Long deserializedTimestamp = longSerializer.deserialize(jsonParser, Long.class, null);
            assertEquals(timestamp, deserializedTimestamp);

            String jsonWithDateString = stringSerializer.serializeValueAsString(dateString, new JsonGenerator.Overrides(), null);
            assertEquals(dateString, jsonWithDateString);

            JsonParser jsonParserDateString = new ObjectMapper().readTree(jsonWithDateString);
            String deserializedDateString = stringSerializer.deserialize(jsonParserDateString, String.class, null);
            assertEquals(dateString, deserializedDateString);

        } catch (IOException e) {
            e.printStackTrace();
        }

        // Test serialization with custom settings
        DateFormat customFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.US);
        TimeZone timeZone = TimeZone.getTimeZone("UTC");
        customFormat.setTimeZone(timeZone);

        DateTimeSerializerBase<Long> customLongSerializer = new StdDateSerializer(Long.TYPE, false, customFormat);
        DateTimeSerializerBase<String> customStringSerializer = new StdDateSerializer(String.class, false, customFormat);

        Long customTimestamp = 123L;
        String customDateString = "2022-12-31T23:59:59.999Z";

        try {
            String jsonWithCustomTimestamp = customLongSerializer.serializeValueAsString(customTimestamp, new JsonGenerator.Overrides(), null);
            assertEquals(customDateString, jsonWithCustomTimestamp);

            JsonParser jsonParserCustomTimestamp = new ObjectMapper().readTree(jsonWithCustomTimestamp);
            Long deserializedCustomTimestamp = customLongSerializer.deserialize(jsonParserCustomTimestamp, Long.class, null);
            assertEquals(customTimestamp, deserializedCustomTimestamp);

            String jsonWithCustomDateString = customStringSerializer.serializeValueAsString(customDateString, new JsonGenerator.Overrides(), null);
            assertEquals(customDateString, jsonWithCustomDateString);

            JsonParser jsonParserCustomDateString = new ObjectMapper().readTree(jsonWithCustomDateString);
            String deserializedCustomDateString = customStringSerializer.deserialize(jsonParserCustomDateString, String.class, null);
            assertEquals(customDateString, deserializedCustomDateString);

        } catch (IOException e) {
            e.printStackTrace();
        }

        // Test serialization with timestamp flag
        DateTimeSerializerBase<Long> timestampSerializer = new StdDateSerializer(Long.TYPE, true, null);
        Long timestampValue = 1234567890123L;

        try {
            String jsonWithTimestamp = timestampSerializer.serializeValueAsString(timestampValue, new JsonGenerator.Overrides(), null);
            assertTrue(jsonWithTimestamp.matches("^[-\\+]?[0-9]+(\\.[0-9]+)?$")); // Numeric format

            JsonParser jsonParserTimestamp = new ObjectMapper().readTree(jsonWithTimestamp);
            Long deserializedTimestamp = timestampSerializer.deserialize(jsonParserTimestamp, Long.class, null);
            assertEquals(timestampValue, deserializedTimestamp);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testContextualSerialization() throws JsonMappingException {
        ObjectMapper mapper = new ObjectMapper();

        // Test serialization with default settings
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
        class TestBean {
            private Long timestamp;
            private String dateString;

            public Long getTimestamp() {
                return timestamp;
            }

            public void setTimestamp(Long timestamp) {
                this.timestamp = timestamp;
            }

            public String getDateString() {
                return dateString;
            }

            public void setDateString(String dateString) {
                this.dateString = dateString;
            }
        }

        TestBean bean = new TestBean();
        bean.setTimestamp(123L);
        bean.setDateString("2022-12-31T23:59:59.999Z");

        try {
            String json = mapper.writeValueAsString(bean);
            TestBean deserializedBean = mapper.readValue(json, TestBean.class);
            assertEquals(bean.getTimestamp(), deserializedBean.getTimestamp());
            assertEquals(bean.getDateString(), deserializedBean.getDateString());

        } catch (IOException e) {
            e.printStackTrace();
        }

        // Test serialization with custom settings
        @JsonFormat(shape = JsonFormat.Shape.STRING, timezone = "UTC", pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
        class TestBeanWithCustomSettings {
            private Long timestamp;
            private String dateString;

            // Getters and setters omitted for brevity
        }

        TestBeanWithCustomSettings beanWithCustomSettings = new TestBeanWithCustomSettings();
        beanWithCustomSettings.setTimestamp(123L);
        beanWithCustomSettings.setDateString("2022-12-31T23:59:59.999Z");

        try {
            String jsonWithCustomSettings = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(beanWithCustomSettings);
            TestBeanWithCustomSettings deserializedBeanWithCustomSettings = mapper.readValue(jsonWithCustomSettings, TestBeanWithCustomSettings.class);
            assertEquals(beanWithCustomSettings.getTimestamp(), deserializedBeanWithCustomSettings.getTimestamp());
            assertEquals(beanWithCustomSettings.getDateString(), deserializedBeanWithCustomSettings.getDateString());

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}