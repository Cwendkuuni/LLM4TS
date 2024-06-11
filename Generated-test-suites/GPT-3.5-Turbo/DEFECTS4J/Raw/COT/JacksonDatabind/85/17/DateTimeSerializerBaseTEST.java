import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.DateTimeSerializerBase;
import com.fasterxml.jackson.databind.util.StdDateFormat;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;

public class DateTimeSerializerBaseTest {

    private DateTimeSerializerBase serializerBase;

    @Before
    public void setUp() {
        serializerBase = new DateTimeSerializerBase<Long>(Long.class, true, new SimpleDateFormat("dd-MM-yyyy", Locale.US)) {

            @Override
            public DateTimeSerializerBase<Long> withFormat(Boolean timestamp, DateFormat customFormat) {
                return null;
            }

            @Override
            public void serialize(Long value, JsonGenerator gen, SerializerProvider serializers) throws IOException {

            }

            @Override
            protected long _timestamp(Long value) {
                return 0;
            }
        };
    }

    @Test
    public void isEmpty_NullValue_ReturnsTrue() {
        boolean isEmpty = serializerBase.isEmpty(null);
        assertEquals(true, isEmpty);
    }

    @Test
    public void isEmpty_ZeroTimestampValue_ReturnsTrue() {
        boolean isEmpty = serializerBase.isEmpty(mock(SerializerProvider.class), 0L);
        assertEquals(true, isEmpty);
    }

    @Test
    public void getSchema_AsTimestamp_ReturnsNumberNode() {
        JsonNode schema = serializerBase.getSchema(mock(SerializerProvider.class), Long.class);
        assertEquals("number", schema.asText());
    }

    @Test
    public void getSchema_NotAsTimestamp_ReturnsStringNode() {
        serializerBase = new DateTimeSerializerBase<Long>(Long.class, false, new SimpleDateFormat("dd-MM-yyyy", Locale.US)) {
            
            @Override
            public DateTimeSerializerBase<Long> withFormat(Boolean timestamp, DateFormat customFormat) {
                return null;
            }

            @Override
            public void serialize(Long value, JsonGenerator gen, SerializerProvider serializers) throws IOException {

            }

            @Override
            protected long _timestamp(Long value) {
                return 0;
            }
        };

        JsonNode schema = serializerBase.getSchema(mock(SerializerProvider.class), Long.class);
        assertEquals("string", schema.asText());
    }

    @Test
    public void acceptJsonFormatVisitor_AsTimestamp() throws JsonMappingException {
        JsonFormatVisitorWrapper visitorWrapper = mock(JsonFormatVisitorWrapper.class);
        JavaType typeHint = mock(JavaType.class);
        serializerBase.acceptJsonFormatVisitor(visitorWrapper, typeHint);
        // verify appropriate methods on visitorWrapper are called
    }

    @Test
    public void acceptJsonFormatVisitor_NotAsTimestamp() throws JsonMappingException {
        serializerBase = new DateTimeSerializerBase<Long>(Long.class, false, new SimpleDateFormat("dd-MM-yyyy", Locale.US)) {

            @Override
            public DateTimeSerializerBase<Long> withFormat(Boolean timestamp, DateFormat customFormat) {
                return null;
            }

            @Override
            public void serialize(Long value, JsonGenerator gen, SerializerProvider serializers) throws IOException {

            }

            @Override
            protected long _timestamp(Long value) {
                return 0;
            }
        };

        JsonFormatVisitorWrapper visitorWrapper = mock(JsonFormatVisitorWrapper.class);
        JavaType typeHint = mock(JavaType.class);
        serializerBase.acceptJsonFormatVisitor(visitorWrapper, typeHint);
        // verify appropriate methods on visitorWrapper are called
    }

    // TODO: Add test cases for other public methods of DateTimeSerializerBase

}