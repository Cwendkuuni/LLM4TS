import org.junit.Before;
import org.junit.Test;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonNumberFormatVisitor;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonStringFormatVisitor;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import com.fasterxml.jackson.databind.util.StdDateFormat;

import java.io.IOException;
import java.lang.reflect.Type;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.TimeZone;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;

public class DateTimeSerializerBaseTest {

    private DateTimeSerializerBase<Long> longSerializer;
    private DateTimeSerializerBase<String> stringSerializer;

    @Before
    public void setup() {
        JsonFormat format = new JsonFormat(new StandardSerializers.Instance(), null, null, null, null, null, null, null);
        longSerializer = new DateTimeSerializerBase<Long>(Long.TYPE, true, null) {
            @Override
            protected long _timestamp(Long value) {
                return value.longValue();
            }
        };
        stringSerializer = new DateTimeSerializerBase<String>(String.class, false, null) {
            @Override
            protected long _timestamp(String value) {
                return System.currentTimeMillis();
            }
        };
    }

    @Test
    public void testDefaultSerialization() throws IOException {
        ObjectWriter writer = new JsonFactory().writerWithRootValueSerializer(longSerializer);
        JsonGenerator gen = writer.getGenerator();

        long value = 1633028800000L; // Thu, 18 Mar 2021 00:00:00 GMT

        longSerializer.serialize(value, gen, null);
        gen.flush();

        String json = writer.getOutputFactory().getJsonString();
        assertEquals("1633028800000", json);
    }

    @Test
    public void testCustomSerializationAsString() throws IOException {
        ObjectWriter writer = new JsonFactory().writerWithRootValueSerializer(stringSerializer);
        JsonGenerator gen = writer.getGenerator();

        String value = "2021-03-18";

        stringSerializer.serialize(value, gen, null);
        gen.flush();

        String json = writer.getOutputFactory().getJsonString();
        assertEquals("\"2021-03-18\"", json);
    }

    @Test(expected = IllegalStateException.class)
    public void testCannotChangeDateFormatInstance() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        JsonSerializer<String> serializer = mapper.findValueSerializer(String.class, null);
        serializer.setContextualHandler(new StandardSerializers.DateSerializer(df));

        JsonFormat format = new JsonFormat(new StandardSerializers.Instance(), null, null, null, null, null, null, null);
        JsonFormatVisitorWrapper visitor = new JsonFormatVisitorWrapper(JsonSerializers.instance, format);
        DateTimeSerializerBase<String> serializerUnderTest = new DateTimeSerializerBase<String>(String.class, false, null) {
            @Override
            public DateTimeSerializerBase<String> withFormat(Boolean timestamp, DateFormat customFormat) {
                throw new UnsupportedOperationException();
            }
        };
        serializerUnderTest.acceptJsonFormatVisitor(visitor, null);
    }

    @Test
    public void testSerializationAsTimestampWhenEnabled() throws IOException {
        ObjectWriter writer = new JsonFactory().writerWithRootValueSerializer(longSerializer);
        JsonGenerator gen = writer.getGenerator();

        long value = 1633028800000L; // Thu, 18 Mar 2021 00:00:00 GMT

        when(longSerializer._useTimestamp).thenReturn(true);

        longSerializer.serialize(value, gen, null);
        gen.flush();

        String json = writer.getOutputFactory().getJsonString();
        assertEquals("1633028800000", json);
    }

    @Test
    public void testSerializationAsStringWhenDisabled() throws IOException {
        ObjectWriter writer = new JsonFactory().writerWithRootValueSerializer(stringSerializer);
        JsonGenerator gen = writer.getGenerator();

        String value = "2021-03-18";

        when(stringSerializer._useTimestamp).thenReturn(false);

        stringSerializer.serialize(value, gen, null);
        gen.flush();

        String json = writer.getOutputFactory().getJsonString();
        assertEquals("\"2021-03-18\"", json);
    }

    @Test
    public void testSerializationWithCustomFormatAsString() throws IOException {
        ObjectWriter writer = new JsonFactory().writerWithRootValueSerializer(stringSerializer);
        JsonGenerator gen = writer.getGenerator();

        String value = "dd/MM/yyyy HH:mm:ss";

        SimpleDateFormat customFormat = new SimpleDateFormat(value);
        when(stringSerializer._customFormat).thenReturn(customFormat);

        stringSerializer.serialize(null, gen, null);
        gen.flush();

        String json = writer.getOutputFactory().getJsonString();
        assertEquals("\"dd/MM/yyyy HH:mm:ss\"", json);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullSerializerProviderPassedToSerializeMethod() throws IOException {
        JsonGenerator gen = mock(JsonGenerator.class);
        longSerializer.serialize(null, gen, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullValuePassedToSerializeMethod() throws IOException {
        ObjectWriter writer = new JsonFactory().writerWithRootValueSerializer(longSerializer);
        JsonGenerator gen = writer.getGenerator();

        longSerializer.serialize(null, gen, null);
    }

    @Test
    public void testSerializeMethodWithValueGreaterThanLongMax() throws IOException {
        ObjectWriter writer = new JsonFactory().writerWithRootValueSerializer(longSerializer);
        JsonGenerator gen = writer.getGenerator();

        long maxLongValue = Long.MAX_VALUE;
        long value = maxLongValue + 1;

        longSerializer.serialize(value, gen, null);
        gen.flush();

        String json = writer.getOutputFactory().getJsonString();
        assertEquals("\"null\"", json);
    }

    @Test
    public void testSerializationWithJsonNumberFormatVisitor() throws IOException {
        ObjectWriter writer = new JsonFactory().writerWithRootValueSerializer(longSerializer);
        JsonGenerator gen = writer.getGenerator();
        JsonNumberFormatVisitor visitor = new JsonNumberFormatVisitor(0, JsonParser.NumberType.LONG);

        long value = 1633028800000L; // Thu, 18 Mar 2021 00:00:00 GMT

        longSerializer.serialize(value, gen, null);
        longSerializer.acceptJsonFormatVisitor(visitor, null);
        gen.flush();

        ObjectNode node = (ObjectNode) visitor.getSchemaNode();
        assertEquals("number", node.get("type").asText());
        assertEquals("long", node.get("items").get("type").asText());
    }

    @Test
    public void testSerializationWithJsonStringFormatVisitor() throws IOException {
        ObjectWriter writer = new JsonFactory().writerWithRootValueSerializer(stringSerializer);
        JsonGenerator gen = writer.getGenerator();
        JsonStringFormatVisitor visitor = new JsonStringFormatVisitor(JsonValueFormat.DATE_TIME);

        String value = "2021-03-18";

        stringSerializer.serialize(value, gen, null);
        stringSerializer.acceptJsonFormatVisitor(visitor, null);
        gen.flush();

        ObjectNode node = (ObjectNode) visitor.getSchemaNode();
        assertEquals("string", node.get("type").asText());
        assertEquals("date-time", node.get("format").asText());
    }
}