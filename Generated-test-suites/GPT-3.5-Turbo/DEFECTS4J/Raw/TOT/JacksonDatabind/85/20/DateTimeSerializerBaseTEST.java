import static org.junit.Assert.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.DateTimeSerializerBase;
import com.fasterxml.jackson.databind.util.StdDateFormat;
import org.junit.Test;

public class DateTimeSerializerBaseTest {

    private class DummyClass extends DateTimeSerializerBase<Date> {

        public DummyClass(Class<Date> type, Boolean useTimestamp, DateFormat customFormat) {
            super(type, useTimestamp, customFormat);
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
        public void serialize(Date value, JsonGenerator gen, SerializerProvider serializers) {
        }
    }

    @Test
    public void testEmptyEmpty() {
        DummyClass dummy = new DummyClass(Date.class, false, null);
        SerializerProvider provider = new ObjectMapper().getSerializerProvider();
        assertTrue(dummy.isEmpty(provider, null));
    }

    @Test
    public void testEmptyValidTimestamp() {
        DummyClass dummy = new DummyClass(Date.class, true, null);
        SerializerProvider provider = new ObjectMapper().getSerializerProvider();
        assertFalse(dummy.isEmpty(provider, new Date()));
    }

    @Test
    public void testGetSchemaNumber() {
        DummyClass dummy = new DummyClass(Date.class, true, null);
        SerializerProvider provider = new ObjectMapper().getSerializerProvider();
        assertEquals("number", dummy.getSchema(provider, Date.class).get("type").asText());
    }

    @Test
    public void testGetSchemaString() {
        DummyClass dummy = new DummyClass(Date.class, false, null);
        SerializerProvider provider = new ObjectMapper().getSerializerProvider();
        assertEquals("string", dummy.getSchema(provider, Date.class).get("type").asText());
    }

    @Test
    public void testAcceptJsonFormatVisitorInt() {
        DummyClass dummy = new DummyClass(Date.class, true, null);
        DummyJsonFormatVisitor visitor = new DummyJsonFormatVisitor();
        dummy.acceptJsonFormatVisitor(visitor, null);
        assertEquals("LONG", visitor.getNumberType().name());
        assertEquals("UTC_MILLISEC", visitor.getValueFormat().name());
    }

    @Test
    public void testAcceptJsonFormatVisitorString() {
        DummyClass dummy = new DummyClass(Date.class, false, null);
        DummyJsonFormatVisitor visitor = new DummyJsonFormatVisitor();
        dummy.acceptJsonFormatVisitor(visitor, null);
        assertEquals("DATE_TIME", visitor.getValueFormat().name());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAsTimestampNullProvider() {
        DummyClass dummy = new DummyClass(Date.class, null, null);
        dummy._asTimestamp(null);
    }

    @Test
    public void testAsTimestampUseTimestampNotNull() {
        DummyClass dummy = new DummyClass(Date.class, true, null);
        SerializerProvider provider = new ObjectMapper().getSerializerProvider();
        assertTrue(dummy._asTimestamp(provider));
    }

    @Test
    public void testAsTimestampCustomFormatNull() {
        DummyClass dummy = new DummyClass(Date.class, null, null);
        SerializerProvider provider = new ObjectMapper().getSerializerProvider();
        assertTrue(dummy._asTimestamp(provider));
    }

    @Test
    public void testAsTimestampDefaultFormatAsTimestampTrue() {
        DummyClass dummy = new DummyClass(Date.class, null, null);
        SerializerProvider provider = new ObjectMapper().getSerializerProvider();
        ObjectMapper mapper = new ObjectMapper();
        mapper.enable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        provider = mapper.getSerializerProvider();
        assertTrue(dummy._asTimestamp(provider));
    }

    @Test
    public void testAsTimestampDefaultFormatAsTimestampFalse() {
        DummyClass dummy = new DummyClass(Date.class, null, null);
        SerializerProvider provider = new ObjectMapper().getSerializerProvider();
        ObjectMapper mapper = new ObjectMapper();
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        provider = mapper.getSerializerProvider();
        assertFalse(dummy._asTimestamp(provider));
    }

    @Test
    public void testSerialize() {
        DummyClass dummy = new DummyClass(Date.class, true, null);
        SerializerProvider provider = new ObjectMapper().getSerializerProvider();
        Date date = new Date();
        dummy.serialize(date, null, provider);
    }

    private class DummyJsonFormatVisitor implements JsonFormatVisitorWrapper {

        private JsonValueFormat valueFormat;
        private JsonParser.NumberType numberType;

        public DummyJsonFormatVisitor() {
        }

        public JsonValueFormat getValueFormat() {
            return valueFormat;
        }

        public JsonParser.NumberType getNumberType() {
            return numberType;
        }

        @Override
        public SerializerProvider getProvider() {
            return null;
        }

        @Override
        public void setProvider(SerializerProvider provider) {
        }

        @Override
        public JsonObjectFormatVisitor expectObjectFormat(JavaType type) throws JsonMappingException {
            return null;
        }

        @Override
        public JsonArrayFormatVisitor expectArrayFormat(JavaType type) throws JsonMappingException {
            return null;
        }

        @Override
        public JsonStringFormatVisitor expectStringFormat(JavaType type) throws JsonMappingException {
            valueFormat = JsonValueFormat.DATE_TIME;
            return null;
        }

        @Override
        public JsonNumberFormatVisitor expectNumberFormat(JavaType type) throws JsonMappingException {
            numberType = JsonParser.NumberType.LONG;
            return null;
        }

        @Override
        public JsonIntegerFormatVisitor expectIntegerFormat(JavaType type) throws JsonMappingException {
            return null;
        }

        @Override
        public JsonBooleanFormatVisitor expectBooleanFormat(JavaType type) throws JsonMappingException {
            return null;
        }

        @Override
        public JsonNullFormatVisitor expectNullFormat(JavaType type) throws JsonMappingException {
            return null;
        }

    }
}