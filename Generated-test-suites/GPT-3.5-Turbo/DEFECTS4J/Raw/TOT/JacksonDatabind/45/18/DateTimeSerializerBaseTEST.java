import static org.junit.Assert.*;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

public class DateTimeSerializerBaseTest {

    private DateTimeSerializerBase<Long> serializer;
    private JsonSerializer<?> contextualSerializer;
    private SerializerProvider serializers;
    private BeanProperty property;
    private DateFormat customDateFormat;
    private Locale locale;
    private TimeZone timeZone;

    @Before
    public void setUp() {
        serializer = new DateTimeSerializerMock(Long.class, true, null);
        serializers = new SerializerProvider();
        property = new BeanProperty();
        customDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ", Locale.US);
        locale = Locale.US;
        timeZone = TimeZone.getTimeZone("GMT");
    }

    @Test
    public void testWithFormat() {
        DateTimeSerializerBase<Long> newSerializer = serializer.withFormat(true, customDateFormat);
        assertTrue(newSerializer._useTimestamp);
        assertEquals(customDateFormat, newSerializer._customFormat);
    }

    @Test
    public void testCreateContextual() {
        DateTimeSerializerBase<Long> newSerializer = (DateTimeSerializerBase<Long>) serializer
                .createContextual(serializers, property);
        assertTrue(newSerializer._useTimestamp);
        assertNull(newSerializer._customFormat);
    }

    @Test
    public void testCreateContextualWithShape() {
        JsonFormat.Value format = new JsonFormat.Value();
        format.setShape(JsonFormat.Shape.STRING);
        format.setTimeZone(timeZone);
        format.setPattern("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
        format.setLocale(locale);
        serializers.setAnnotationIntrospector(new AnnotationIntrospectorMock(format));
        DateTimeSerializerBase<Long> newSerializer = (DateTimeSerializerBase<Long>) serializer
                .createContextual(serializers, property);
        assertFalse(newSerializer._useTimestamp);
        assertEquals(customDateFormat, newSerializer._customFormat);
    }

    @Test
    public void testIsEmpty() {
        assertTrue(serializer.isEmpty(null));
        assertTrue(serializer.isEmpty(serializers, null));
        Date currentDate = new Date();
        assertFalse(serializer.isEmpty(currentDate.getTime()));
        assertFalse(serializer.isEmpty(serializers, currentDate.getTime()));
    }

    @Test
    public void testGetSchema() {
        JsonNode schema = serializer.getSchema(serializers, Long.class);
        assertNotNull(schema);
        assertEquals("number", schema.asText());
    }

    @Test
    public void testAcceptJsonFormatVisitor() throws JsonMappingException {
        JsonFormatVisitorWrapper visitor = new JsonFormatVisitorWrapper();
        serializer.acceptJsonFormatVisitor(visitor, Long.class);
        assertEquals(JsonValueFormat.UTC_MILLISEC, visitor.stringFormat);
    }

    @Test
    public void testSerialize() {
        JsonGenerator generator = new JsonGenerator();
        Date currentDate = new Date();
        try {
            serializer.serialize(currentDate.getTime(), generator, serializers);
            assertEquals(currentDate.getTime(), generator.getSerializedValue());
        } catch (Exception e) {
            fail("Exception should not be thrown");
        }
    }

    private class DateTimeSerializerMock extends DateTimeSerializerBase<Long> {

        public DateTimeSerializerMock(Class<Long> type, Boolean useTimestamp, DateFormat customFormat) {
            super(type, useTimestamp, customFormat);
        }

        @Override
        public DateTimeSerializerBase<Long> withFormat(Boolean timestamp, DateFormat customFormat) {
            return null;
        }

        @Override
        protected long _timestamp(Long value) {
            return value;
        }

        @Override
        public void serialize(Long value, JsonGenerator gen, SerializerProvider serializers) {
            gen.serializeValue(value);
        }
    }

    private class AnnotationIntrospectorMock extends AnnotationIntrospector {

        private JsonFormat.Value format;

        public AnnotationIntrospectorMock(JsonFormat.Value format) {
            this.format = format;
        }

        @Override
        public JsonFormat findFormat(Annotated annotated) {
            return format;
        }
    }

}