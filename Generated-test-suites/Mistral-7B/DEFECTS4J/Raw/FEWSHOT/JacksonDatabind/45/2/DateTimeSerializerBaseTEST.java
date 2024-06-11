package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.introspect.Annotated;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import com.fasterxml.jackson.databind.ser.std.DateTimeSerializerBase;
import com.fasterxml.jackson.databind.util.StdDateFormat;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.TimeZone;

public class DateTimeSerializerBaseTest {

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss.SSS Z")
    private static class TestDate {
        private Long timestamp;

        public Long getTimestamp() {
            return timestamp;
        }
    }

    @Test
    public void testDateTimeSerializerBase() {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS Z");
        TimeZone timeZone = TimeZone.getTimeZone("UTC");
        Locale locale = Locale.US;

        DateTimeSerializerBase<TestDate> serializer = new DateTimeSerializerBase<TestDate>(TestDate.class, false, dateFormat) {};

        TestDate testDate = new TestDate();
        testDate.timestamp = System.currentTimeMillis();

        ObjectNode node = mapper().createObjectNode();
        serializer.serialize(testDate, mapper().getFactory().createGenerator(node), mapper());

        ObjectNode expectedNode = mapper().createObjectNode();
        expectedNode.put("@type", "number");
        expectedNode.put("value", testDate.timestamp);

        assertTrue(node.isNumberNode());
        assertEquals(expectedNode.toString(), node.toString());

        SerializerProvider serializers = mapper();
        DateTimeSerializerBase<TestDate> contextualSerializer = serializers.findValueSerializer(TestDate.class);
        assertNotNull(contextualSerializer);
        assertEquals(serializer, contextualSerializer);

        // Test serialization with custom format and timezone
        DateTimeSerializerBase<TestDate> serializerWithCustomFormat = new DateTimeSerializerBase<TestDate>(TestDate.class, false, dateFormat) {
            @Override
            public DateTimeSerializerBase<T> withFormat(Boolean timestamp, DateFormat customFormat) {
                if (timestamp != null && customFormat == null) {
                    return new DateTimeSerializerBase<T>(getType(getClass()), timestamp, null) {};
                }
                return new DateTimeSerializerBase<T>(getType(getClass()), timestamp, customFormat) {};
            }
        }.withFormat(false, new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")).withTimeZone(timeZone);

        TestDate testDateWithCustomFormat = new TestDate();
        testDateWithCustomFormat.timestamp = System.currentTimeMillis();

        ObjectNode nodeWithCustomFormat = mapper().createObjectNode();
        serializerWithCustomFormat.serialize(testDateWithCustomFormat, mapper().getFactory().createGenerator(nodeWithCustomFormat), serializers);

        ObjectNode expectedNodeWithCustomFormat = mapper().createObjectNode();
        expectedNodeWithCustomFormat.put("@type", "string");
        expectedNodeWithCustomFormat.put("value", dateFormat.format(new Date(testDateWithCustomFormat.timestamp)));

        assertTrue(nodeWithCustomFormat.isTextualNode());
        assertEquals(expectedNodeWithCustomFormat.toString(), nodeWithCustomFormat.toString());
    }

    @Test
    public void testDateTimeSerializerBaseWithTimestamp() {
        DateTimeSerializerBase<TestDate> serializerWithTimestamp = new DateTimeSerializerBase<TestDate>(TestDate.class, true, null) {};

        TestDate testDate = new TestDate();
        testDate.timestamp = System.currentTimeMillis();

        ObjectNode nodeWithTimestamp = mapper().createObjectNode();
        serializerWithTimestamp.serialize(testDate, mapper().getFactory().createGenerator(nodeWithTimestamp), mapper());

        ObjectNode expectedNodeWithTimestamp = mapper().createObjectNode();
        expectedNodeWithTimestamp.put("@type", "number");
        expectedNodeWithTimestamp.put("value", testDate.timestamp);

        assertTrue(nodeWithTimestamp.isNumberNode());
        assertEquals(expectedNodeWithTimestamp.toString(), nodeWithTimestamp.toString());
    }

    @Test
    public void testDateTimeSerializerBaseWithNull() {
        DateTimeSerializerBase<TestDate> serializer = new DateTimeSerializerBase<TestDate>(TestDate.class, false, null) {};

        TestDate testDate = null;

        ObjectNode nodeWithNull = mapper().createObjectNode();
        serializer.serialize(testDate, mapper().getFactory().createGenerator(nodeWithNull), mapper());

        ObjectNode expectedNodeWithNull = mapper().createObjectNode();
        expectedNodeWithNull.put("@type", "null");

        assertTrue(nodeWithNull.isNull());
        assertEquals(expectedNodeWithNull.toString(), nodeWithNull.toString());
    }

    private JsonFactory mapper() {
        return new ObjectMapper();
    }

    private TypeReference<TestDate> getType(Class<?> clazz) {
        return new TypeReference<TestDate>() {};
    }

}