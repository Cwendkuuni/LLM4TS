import static org.junit.Assert.*;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitorWrapper;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import com.fasterxml.jackson.databind.ser.std.StdDateFormat;
import com.fasterxml.jackson.databind.ser.std.StdScalarSerializer;

public class DateTimeSerializerBaseTest {

    private DateTimeSerializerBase<Date> serializer;

    @Before
    public void setup() {
        serializer = new DateTimeSerializerBase<Date>(Date.class, Boolean.FALSE, null) {
            @Override
            public DateTimeSerializerBase<Date> withFormat(Boolean timestamp, DateFormat customFormat) {
                return null;
            }

            @Override
            protected long _timestamp(Date value) {
                return 0;
            }

            @Override
            public JsonNode getSchema(SerializerProvider serializers, Type typeHint) {
                return null;
            }

            @Override
            public void acceptJsonFormatVisitor(JsonFormatVisitorWrapper visitor, JavaType typeHint)
                    throws JsonMappingException {
                return;
            }

            @Override
            public void serialize(Date value, JsonGenerator gen, SerializerProvider serializers)
                    throws IOException {
                return;
            }
        };
    }

    @Test
    public void testGetSchema() {
        SerializerProvider serializers = null;
        Type typeHint = null;
        JsonNode schema = serializer.getSchema(serializers, typeHint);
        assertNull(schema);
    }

    @Test
    public void testAcceptJsonFormatVisitor() {
        JsonFormatVisitorWrapper visitor = null;
        JavaType typeHint = null;
        boolean asNumber = false;
        
        try {
            serializer.acceptJsonFormatVisitor(visitor, typeHint, asNumber);
        } catch (JsonMappingException e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }

    @Test
    public void testSerialize() {
        Date value = new Date();
        JsonGenerator gen = null;
        SerializerProvider serializers = null;

        try {
            serializer.serialize(value, gen, serializers);
        } catch (IOException e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }
}