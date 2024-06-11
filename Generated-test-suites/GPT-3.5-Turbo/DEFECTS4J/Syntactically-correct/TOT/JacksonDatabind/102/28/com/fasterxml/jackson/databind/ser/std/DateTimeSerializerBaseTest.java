import static org.junit.Assert.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import java.io.IOException;
import java.lang.reflect.Type;
import java.text.DateFormat;
import java.util.Date;
import java.util.TimeZone;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.util.StdDateFormat;

public class DateTimeSerializerBaseTest {

    @Mock
    private SerializerProvider mockSerializerProvider;
    
    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @After
    public void tearDown() {
    }

    @Test
    public void testIsEmpty_ReturnsFalse() {
        DateTimeSerializerBase serializer = new DateTimeSerializerBaseImpl(Boolean.TRUE, null) {
            @Override
            protected long _timestamp(T value) {
                return 0L;
            }

            @Override
            public void serialize(T value, JsonGenerator gen, SerializerProvider serializers)
                throws IOException {
            }
        };
        
        assertFalse(serializer.isEmpty(mockSerializerProvider, new Date()));
    }
    
    @Test
    public void testGetSchema_ReturnsStringSchema() {
        DateTimeSerializerBase serializer = new DateTimeSerializerBaseImpl(Boolean.FALSE, null) {
            @Override
            protected long _timestamp(T value) {
                return 0L;
            }

            @Override
            public void serialize(T value, JsonGenerator gen, SerializerProvider serializers)
                throws IOException {
            }
        };

        JsonNode schema = serializer.getSchema(mockSerializerProvider, null);
        
        assertEquals("string", schema.get("type").asText());
        assertTrue(schema.get("required").asBoolean());
        assertFalse(schema.has("format"));
    }

    @Test
    public void testGetSchema_ReturnsNumberSchema() {
        DateTimeSerializerBase serializer = new DateTimeSerializerBaseImpl(Boolean.TRUE, null) {
            @Override
            protected long _timestamp(T value) {
                return 0L;
            }

            @Override
            public void serialize(T value, JsonGenerator gen, SerializerProvider serializers)
                throws IOException {
            }
        };

        JsonNode schema = serializer.getSchema(mockSerializerProvider, null);
        
        assertEquals("number", schema.get("type").asText());
        assertTrue(schema.get("required").asBoolean());
        assertEquals("utc-millisec", schema.get("format").asText());
    }

    @Test
    public void testSerialize_WithCustomFormat() throws IOException {
        StdDateFormat format = new StdDateFormat();
        format.setTimeZone(TimeZone.getTimeZone("GMT"));
        
        DateTimeSerializerBase serializer = new DateTimeSerializerBaseImpl(Boolean.FALSE, format) {
            @Override
            protected long _timestamp(T value) {
                return 0L;
            }

            @Override
            public void serialize(T value, JsonGenerator gen, SerializerProvider serializers)
                throws IOException {
                    DateFormat df = format;
                    gen.writeString(df.format((Date)value));
            }
        };
        
        SerializerProvider provider = new ObjectMapper().getSerializerProvider();
        JsonGenerator gen = new ObjectMapper().getFactory().createGenerator(System.out);
        
        serializer.serialize(new Date(), gen, provider);
    }
    
    private static class DateTimeSerializerBaseImpl extends DateTimeSerializerBase {
        public DateTimeSerializerBaseImpl(Boolean useTimestamp, DateFormat customFormat) {
            super(Date.class, useTimestamp, customFormat);
        }

        @Override
        public DateTimeSerializerBase<Date> withFormat(Boolean timestamp, DateFormat customFormat) {
            return null;
        }

        @Override
        protected long _timestamp(Date value) {
            return 0L;
        }

        @Override
        public void serialize(Date value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        }
    }

}