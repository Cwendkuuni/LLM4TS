package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.TextNode;
import com.fasterxml.jackson.databind.ser.DefaultSerializers;
import com.fasterxml.jackson.databind.ser.std.DateTimeSerializerBase;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.fasterxml.jackson.databind.util.StdDateFormat;

import org.junit.Test;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.TimeZone;

public class DateTimeSerializerBaseTest {

    @Test
    public void testDateTimeSerializerBase() {
        assertNotNull(DateTimeSerializerBase.class);
    }

    @Test
    public void testEmpty() {
        DateTimeSerializerBase<Object> serializer = new StdScalarSerializer.EmptyArraySerializer<>(Object.class);
        assertNull(serializer.isEmpty(null));
        assertNull(serializer.isEmpty(null, null));
    }

    @Test
    public void test_asTimestamp() {
        JsonSerializationConfig config = new JsonSerializationConfig();
        JsonSerializerProvider serializers = config.getSerializerProvider();
        DateTimeSerializerBase<Long> longSerializer = new StdScalarSerializer<Long>(Long.class);
        DateTimeSerializerBase<String> stringSerializer = new DateTimeSerializerBase<String>(String.class, true, null);

        assertFalse(longSerializer._asTimestamp(null));
        assertFalse(longSerializer._asTimestamp(serializers));
        assertTrue(longSerializer._asTimestamp(serializers, null));

        assertFalse(stringSerializer._asTimestamp(null));
        assertFalse(stringSerializer._asTimestamp(serializers));
        assertFalse(stringSerializer._asTimestamp(serializers, null));

        assertTrue(longSerializer._asTimestamp(serializers, new JsonFormat.Value()));

        JsonFormat.Value format = new JsonFormat.Value();
        format.setShape(JsonFormat.Shape.STRING);
        format.setTimeZone(TimeZone.getTimeZone("UTC"));
        format.setLocale(Locale.US);
        format.setPattern("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ", Locale.US);
        dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        format.setFormat(dateFormat);

        assertTrue(stringSerializer._asTimestamp(serializers, format));
    }

    @Test
    public void testAcceptJsonFormatVisitor() throws JsonMappingException {
        JsonSerializationConfig config = new JsonSerializationConfig();
        JsonSerializerProvider serializers = config.getSerializerProvider();

        DateTimeSerializerBase<String> stringSerializer = new DateTimeSerializerBase<String>(String.class, false, null);
        DateTimeSerializerBase<Long> longSerializer = new StdScalarSerializer<Long>(Long.class);

        JsonFormatVisitorWrapper visitor = new JsonFormatter.DefaultSerializerProvider().getROOT_VALUE_SERIALIZER().getProvider().getDefaultSerializerProvider().getJsonFormatVisitorWrapper(new TypeDeserializer.EmptyCreator(), null);
        JavaType typeHint = serializers.constructType(String.class);

        stringSerializer.acceptJsonFormatVisitor(visitor, typeHint);
        assertEquals("String", visitor.getCurrentName());
        assertEquals(JsonValueFormat.DATE_TIME, visitor.getCurrentValueFormat());

        typeHint = serializers.constructType(Long.class);
        longSerializer.acceptJsonFormatVisitor(visitor, typeHint);
        assertEquals("number", visitor.getCurrentName());
        assertEquals(JsonParser.NumberType.LONG, visitor.getCurrentType());
        assertEquals(JsonValueFormat.UTC_MILLISEC, visitor.getCurrentValueFormat());
    }

    @Test
    public void testSerialize() throws IOException {
        JsonSerializationConfig config = new JsonSerializationConfig();
        JsonSerializerProvider serializers = config.getSerializerProvider();

        DateTimeSerializerBase<String> stringSerializer = new DateTimeSerializerBase<String>(String.class, false, null);
        DateTimeSerializerBase<Long> longSerializer = new StdScalarSerializer<Long>(Long.class);

        ObjectMapper mapper = new ObjectMapper(serializers);

        String dateString = "2023-03-31T15:45:23.123Z";
        Long timestamp = 1679554923123L;

        String result = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(new Object() {
            @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSZ", timezone = "UTC")
            String date;

            @JsonFormat(shape = JsonFormat.Shape.NUMBER)
            Long timestamp;

            {
                date = dateString;
                timestamp = timestamp;
            }
        });

        assertEquals(result, "{\"date\":\"2023-03-31T15:45:23.123Z\",\"timestamp\":1679554923123}");

        Object deserialized = mapper.readValue(result, Object.class);

        assertEquals(dateString, deserialized.getClass().getDeclaredField("date").get(deserialized));
        assertEquals(timestamp, deserialized.getClass().getDeclaredField("timestamp").get(deserialized));

        String dateStringSerialized = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(deserialized);
        Long timestampDeserialized = mapper.readValue(new TextNode(dateStringSerialized), Long.class);

        assertNotNull(dateStringSerialized);
        assertNotNull(timestampDeserialized);

        assertEquals(dateString, mapper.readerForField("date").getValue().getValueAsString());
        assertEquals(timestamp, mapper.readerForField("timestamp").getValue().asLong());

        TextNode dateNode = JsonNodeFactory.instance.textNode(dateString);
        Long timestampNode = mapper.readerForTypeTree(dateNode.getClass()).readValueAs(Long.class);

        assertNotNull(timestampNode);
        assertEquals(timestamp, timestampNode);

        String resultStringSerializer = stringSerializer.serialize(dateString, null, serializers);
        String resultTimestampSerializer = longSerializer.serialize(timestamp, null, serializers);

        assertNotNull(resultStringSerializer);
        assertNotNull(resultTimestampSerializer);

        assertEquals(dateString, new String(resultStringSerializer));
        assertEquals(Long.toString(timestamp), new String(resultTimestampSerializer));

        assertEquals(resultStringSerializer.length(), resultStringSerializer.getBytes().length);
        assertEquals(resultTimestampSerializer.length(), resultTimestampSerializer.getBytes().length);

        assertTrue(resultStringSerializer.startsWith("\"2023-03-31T15:45:23.123Z\"".getBytes()));
        assertTrue(resultTimestampSerializer.startsWith(Long.toString(timestamp).getBytes()));

        assertNull(stringSerializer.serialize(null, null, serializers));
        assertNull(longSerializer.serialize(null, null, serializers));
    }

    @Test
    public void testSerializeWithCustomFormat() throws IOException {
        JsonSerializationConfig config = new JsonSerializationConfig();
        JsonSerializerProvider serializers = config.getSerializerProvider();

        DateTimeSerializerBase<String> stringSerializer = new DateTimeSerializerBase<String>(String.class, false, new StdDateFormat(new SimpleDateFormat("yyyyMMddHHmmssSSS").parse("yyyyMMddHHmmssSSS"), Locale.US, TimeZone.getTimeZone("UTC")));

        ObjectMapper mapper = new ObjectMapper(serializers);

        String dateString = "20230331154523123";

        String result = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(new Object() {
            @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyyMMddHHmmssSSS", timezone = "UTC")
            String date;

            {
                date = dateString;
            }
        });

        assertEquals(result, "{\"date\":\"20230331154523123\"}");

        Object deserialized = mapper.readValue(result, Object.class);

        assertEquals(dateString, deserialized.getClass().getDeclaredField("date").get(deserialized));

        String resultStringSerializer = stringSerializer.serialize(dateString, null, serializers);

        assertNotNull(resultStringSerializer);

        assertEquals(dateString, new String(resultStringSerializer));

        assertNull(stringSerializer.serialize(null, null, serializers));
    }
}