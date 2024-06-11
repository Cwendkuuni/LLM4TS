import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import org.apache.commons.cli.Option;
import org.junit.Test;

public class DateTimeSerializerBaseTest {

    private final DateTimeSerializerBase<?> serializer;

    public DateTimeSerializerBaseTest() {
        serializer = new DateTimeSerializerBase<String>(String.class, false, null) {
            @Override
            public DateTimeSerializerBase<String> withFormat(Boolean timestamp, DateFormat customFormat) {
                return null;
            }

            @Override
            protected long _timestamp(String value) {
                return 0;
            }

            @Override
            public void serialize(String value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
                
            }
        };
    }

    @Test
    public void testWithFormat() {
        DateTimeSerializerBase<String> newSerializer = serializer.withFormat(false, null);
        assertFalse(newSerializer._asTimestamp(null));

        newSerializer = serializer.withFormat(true, null);
        assertTrue(newSerializer._asTimestamp(null));
    }

    @Test
    public void testIsEmpty() {
        assertTrue(serializer.isEmpty((String) null));
        assertTrue(serializer.isEmpty(null, null));
    }

    @Test
    public void testGetSchema() {
        JsonNode schema = serializer.getSchema(null, null);
        assertNotNull(schema);
        assertTrue(schema.isObject());
    }

    @Test
    public void testAcceptJsonFormatVisitor() throws JsonMappingException {
        JsonFormatVisitorWrapper visitor = new JsonFormatVisitorWrapper(null);
        serializer.acceptJsonFormatVisitor(visitor, null);
        assertNotNull(visitor);
        assertNotNull(visitor.getProvider());
        assertNotNull(visitor.getProvider().getFormatProvider());
    }

    @Test
    public void testSerialize() throws IOException {
        JsonGenerator gen = new JsonGenerator(null);
        serializer.serialize(null, gen, null);
        assertNotNull(gen);
    }
}