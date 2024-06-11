import static org.junit.Assert.*;
import org.junit.Test;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitorWrapper;
import com.fasterxml.jackson.databind.ser.std.DateTimeSerializerBase;

public class DateTimeSerializerBaseTest {

    @Test
    public void testWithFormat() {
        DateTimeSerializerBase serializer = new DateTimeSerializerBaseTestImpl();
        DateTimeSerializerBase result = serializer.withFormat(true, new SimpleDateFormat("yyyy-MM-dd"));
        assertNotNull(result);
        assertEquals(true, result._useTimestamp);
        assertNull(result._customFormat);

        result = serializer.withFormat(false, new SimpleDateFormat("yyyy/MM/dd"));
        assertNotNull(result);
        assertEquals(false, result._useTimestamp);
        assertNotNull(result._customFormat);
    }

    @Test
    public void testCreateContextual() {
        SerializerProvider serializers = null;
        BeanProperty property = null;

        DateTimeSerializerBase serializer = new DateTimeSerializerBaseTestImpl();
        JsonSerializer result = serializer.createContextual(serializers, property);
        assertNull(result);
    }

    @Test
    public void testIsEmpty() {
        SerializerProvider serializers = null;
        DateTimeSerializerBase serializer = new DateTimeSerializerBaseTestImpl();
        boolean result = serializer.isEmpty(serializers, new Date());
        assertEquals(false, result);
    }

    @Test
    public void testGetSchema() {
        SerializerProvider serializers = null;
        Type typeHint = null;

        DateTimeSerializerBase serializer = new DateTimeSerializerBaseTestImpl();
        JsonNode result = serializer.getSchema(serializers, typeHint);
        assertNotNull(result);
    }

    @Test
    public void testAcceptJsonFormatVisitor() {
        JsonFormatVisitorWrapper visitor = null;
        JavaType typeHint = null;

        DateTimeSerializerBase serializer = new DateTimeSerializerBaseTestImpl();
        try {
            serializer.acceptJsonFormatVisitor(visitor, typeHint);
        } catch (Exception e) {
            fail("Exception not expected");
        }
    }

    @Test
    public void testSerialize() {
        JsonGenerator gen = null;
        SerializerProvider serializers = null;

        DateTimeSerializerBase serializer = new DateTimeSerializerBaseTestImpl();
        try {
            serializer.serialize(new Date(), gen, serializers);
        } catch (Exception e) {
            fail("Exception not expected");
        }
    }

    @Test
    public void testSerializeAsString() {
        JsonGenerator gen = null;
        SerializerProvider provider = null;

        DateTimeSerializerBase serializer = new DateTimeSerializerBaseTestImpl();
        try {
            serializer.serializeAsString(new Date(), gen, provider);
        } catch (Exception e) {
            fail("Exception not expected");
        }
    }
}

class DateTimeSerializerBaseTestImpl extends DateTimeSerializerBase<Date> {

    public DateTimeSerializerBaseTestImpl() {
        super(Date.class, false, null);
    }

    @Override
    public DateTimeSerializerBase<Date> withFormat(Boolean timestamp, DateFormat customFormat) {
        return null;
    }

    @Override
    protected long _timestamp(Date value) {
        return 0;
    }

    @Override
    public void serialize(Date value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        // do nothing
    }
}