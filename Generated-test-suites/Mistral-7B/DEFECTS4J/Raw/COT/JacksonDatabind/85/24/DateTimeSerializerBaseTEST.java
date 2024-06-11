import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.DateTimeSerializerBase;
import com.fasterxml.jackson.databind.ser.std.StdScalarSerializer;
import com.fasterxml.jackson.databind.util.StdDateFormat;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.TimeZone;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class DateTimeSerializerBaseTest {

    private DateTimeSerializerBase<Long> longSerializer;
    private DateTimeSerializerBase<String> stringSerializer;
    private SerializerProvider serializers;
    private ObjectMapper objectMapper;

    @Before
    public void setUp() throws Exception {
        serializers = mock(SerializerProvider.class);
        objectMapper = new ObjectMapper();

        longSerializer = new DateTimeSerializerBase<Long>(Long.TYPE, false, null);
        longSerializer = longSerializer.withFormat(false, null);

        stringSerializer = new DateTimeSerializerBase<String>(String.class, false, null);
        stringSerializer = stringSerializer.withFormat(false, null);

        objectMapper.setSerializerProvider(serializers);
    }

    @Test
    public void testEmpty() {
        assertFalse(longSerializer.isEmpty(null));
        assertFalse(longSerializer.isEmpty(serializers, null));
        assertFalse(stringSerializer.isEmpty(null));
        assertFalse(stringSerializer.isEmpty(serializers, null));
    }

    @Test
    public void test_timestamp() {
        when(serializers.isEnabled(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)).thenReturn(true);

        long timestamp = 1611358400000L; // Wed, 13 Jan 2021 00:00:00 GMT

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        JsonGenerator gen = objectMapper.getFactory().createGenerator(outputStream);

        longSerializer.serialize(timestamp, gen, serializers);
        gen.flush();

        assertEquals(Long.toString(timestamp), outputStream.toString().trim());
    }

    @Test
    public void test_customFormat() throws IOException {
        DateFormat customFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss z");

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        JsonGenerator gen = objectMapper.getFactory().createGenerator(outputStream);

        DateTimeSerializerBase<String> serializer = new DateTimeSerializerBase<String>(String.class, false, customFormat);
        serializer = serializer.withFormat(false, customFormat);

        String date = "2021-01-13 12:34:56 EST";
        String testValue = "2021-01-13 12:34:56";

        serializer.serialize(testValue, gen, serializers);
        gen.flush();

        assertEquals(date, outputStream.toString().trim());
    }

    @Test(expected = IllegalArgumentException.class)
    public void test_nullSerializerProvider() {
        DateTimeSerializerBase<String> serializer = new DateTimeSerializerBase<String>(String.class, false, null);
        serializer = serializer.withFormat(false, null);
        serializer.createContextual(null, null);
    }

    @Test
    public void test_acceptJsonFormatVisitor() throws JsonMappingException {
        JsonFormatVisitorWrapper visitor = mock(JsonFormatVisitorWrapper.class);

        longSerializer._acceptJsonFormatVisitor(visitor, null, _asTimestamp(serializers));
        stringSerializer._acceptJsonFormatVisitor(visitor, null, _asTimestamp(serializers));

        assertEquals("number", visitor.getFormatName());
        assertSame(Long.TYPE, visitor.getType());

        stringSerializer._acceptJsonFormatVisitor(visitor, null, _asTimestamp(serializers));
        assertEquals("string", visitor.getFormatName());
        assertSame(String.class, visitor.getType());
    }
}