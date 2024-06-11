import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.TimeZone;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitorWrapper;
import com.fasterxml.jackson.databind.ser.BeanProperty;
import com.fasterxml.jackson.databind.ser.std.DateTimeSerializerBase;
import com.fasterxml.jackson.databind.util.StdDateFormat;

public class DateTimeSerializerBaseTest {

    private DateTimeSerializerBase<Integer> serializer;
    private JsonSerializer<?> contextualSerializer;
    private SerializerProvider provider;
    private BeanProperty property;
    private JsonFormat.Value format;
    private JsonGenerator generator;

    @Before
    public void setup() {
        TimeZone timeZone = TimeZone.getTimeZone("GMT");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.US);
        sdf.setTimeZone(timeZone);
        serializer = new DateTimeSerializerBase<>(Integer.class, false, sdf) {
            @Override
            public DateTimeSerializerBase<Integer> withFormat(Boolean timestamp, SimpleDateFormat customFormat) {
                return null;
            }

            @Override
            protected long _timestamp(Integer value) {
                return 0L;
            }

            @Override
            public void serialize(Integer value, JsonGenerator gen, SerializerProvider serializers) {
            }

            @Override
            public JsonNode getSchema(SerializerProvider serializers, JavaType typeHint) {
                return null;
            }

            @Override
            public void acceptJsonFormatVisitor(JsonFormatVisitorWrapper visitor, JavaType typeHint) {
            }
        };
        format = new JsonFormat.Value();
        generator = null;
    }

    @Test
    public void testCreateContextual() {
        try {
            contextualSerializer = serializer.createContextual(provider, property);
        } catch (JsonMappingException e) {
            fail("Exception thrown");
        }
        assertNotNull(contextualSerializer);
    }

    @Test
    public void testIsEmpty() {
        assertTrue(serializer.isEmpty(0));
        assertTrue(serializer.isEmpty(provider, 0));
    }

    @Test
    public void testGetSchema() {
        JsonNode node = serializer.getSchema(provider, JavaType.simple(Object.class));
        assertNotNull(node);
    }

    @Test
    public void testAcceptJsonFormatVisitor() {
        try {
            serializer.acceptJsonFormatVisitor(null, JavaType.simple(Object.class));
        } catch (JsonMappingException e) {
            fail("Exception thrown");
        }
    }
}