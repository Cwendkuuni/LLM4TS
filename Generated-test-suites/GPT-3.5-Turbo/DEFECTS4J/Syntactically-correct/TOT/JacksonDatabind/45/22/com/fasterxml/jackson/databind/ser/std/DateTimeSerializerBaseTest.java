import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;

import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.ser.std.*;

public class DateTimeSerializerBaseTest {

    private static final SimpleDateFormat ISO_FORMAT = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");
    
    private DateTimeSerializerBase<Date> serializer;
    
    @Before
    public void setUp() {
        serializer = new DateTimeSerializer();
    }
    
    @Test
    public void testWithFormat_Timestamp_True() {
        serializeFormat();
        assertTrue(serializer._useTimestamp);
        assertEquals(null, serializer._customFormat);
    }
    
    @Test
    public void testWithFormat_String_Format() {
        DateFormat customFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US);
        serializeFormat();
        assertTrue(!serializer._useTimestamp);
        assertEquals(customFormat, serializer._customFormat);
    }
    
    @Test
    public void testWithFormat_String_Default_Format() {
        serializeFormat();
        assertTrue(!serializer._useTimestamp);
        assertEquals(StdDateFormat.DATE_FORMAT_STR_ISO8601, ((SimpleDateFormat)serializer._customFormat).toPattern());
    }
    
    @Test
    public void testIsEmpty_NullDate_Empty() {
        assertTrue(serializer.isEmpty(null));
    }
    
    @Test
    public void testIsEmpty_ZeroTimestamp_Empty() {
        Date date = new Date(0L);
        assertTrue(serializer.isEmpty(date));
    }
    
    @Test
    public void testIsEmpty_NonZeroTimestamp_NotEmpty() {
        Date date = new Date(1000L);
        assertTrue(!serializer.isEmpty(date));
    }
    
    @Test
    public void testIsEmpty_NullValue_Empty() {
        assertTrue(serializer.isEmpty(null, null));
    }
    
    @Test
    public void testIsEmpty_NullValueWithSerializer_Empty() {
        assertTrue(serializer.isEmpty(null, new DummySerializerProvider()));
    }
    
    @Test
    public void testIsEmpty_ZeroTimestampWithSerializer_Empty() {
        Date date = new Date(0L);
        assertTrue(serializer.isEmpty(new DummySerializerProvider(), date));
    }
    
    @Test
    public void testIsEmpty_NonZeroTimestampWithSerializer_NotEmpty() {
        Date date = new Date(1000L);
        assertTrue(!serializer.isEmpty(new DummySerializerProvider(), date));
    }
    
    @Test
    public void testGetSchema_AsTimestamp_NumberFormat() {
        JsonNode schema = serializer.getSchema(new DummySerializerProvider(), null);
        assertEquals("number", schema.get("type").asText());
    }
    
    @Test
    public void testGetSchema_AsString_StringFormat() {
        DateFormat customFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US);
        serializer = new DateTimeSerializer(DateTimeSerializerBaseTest.class, false, customFormat);
        
        JsonNode schema = serializer.getSchema(new DummySerializerProvider(), null);
        assertEquals("string", schema.get("type").asText());
    }
    
    @Test
    public void testAcceptJsonFormatVisitor_AsTimestamp() throws JsonMappingException {
        MockJsonFormatVisitorWrapper visitor = new MockJsonFormatVisitorWrapper();
        serializer.acceptJsonFormatVisitor(visitor, null);
        
        assertEquals(JsonParser.NumberType.LONG, visitor.getIntegerFormat().getType());
        assertEquals(JsonValueFormat.UTC_MILLISEC, visitor.getIntegerFormat().getFormat());
    }
    
    @Test
    public void testAcceptJsonFormatVisitor_AsString() throws JsonMappingException {
        DateFormat customFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US);
        serializer = new DateTimeSerializer(DateTimeSerializerBaseTest.class, false, customFormat);
        
        MockJsonFormatVisitorWrapper visitor = new MockJsonFormatVisitorWrapper();
        serializer.acceptJsonFormatVisitor(visitor, null);
        
        assertEquals(JsonValueFormat.DATE_TIME, visitor.getStringFormat().getFormat());
    }
    
    @Test
    public void testSerialize_Timestamp_AsString() throws Exception {
        DateFormat customFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US);
        serializer = new DateTimeSerializer(DateTimeSerializerBaseTest.class, true, customFormat);
        
        JsonFactory jsonFactory = new JsonFactory();
        StringWriter writer = new StringWriter();
        JsonGenerator jsonGenerator = jsonFactory.createGenerator(writer);
        
        long timestamp = 1598745200000L; // Timestamp for 2020-08-30 00:00:00
        Date date = new Date(timestamp);
        
        serializer.serialize(date, jsonGenerator, null);
        jsonGenerator.flush();

        assertEquals("\"2020-08-30 00:00:00\"", writer.toString());
    }
    
    @Test
    public void testSerialize_String_AsString() throws Exception {
        DateFormat customFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US);
        serializer = new DateTimeSerializer(DateTimeSerializerBaseTest.class, false, customFormat);
        
        JsonFactory jsonFactory = new JsonFactory();
        StringWriter writer = new StringWriter();
        JsonGenerator jsonGenerator = jsonFactory.createGenerator(writer);
        
        long timestamp = 1598745200000L; // Timestamp for 2020-08-30 00:00:00
        Date date = new Date(timestamp);
        
        serializer.serialize(date, jsonGenerator, null);
        jsonGenerator.flush();

        assertEquals("\"2020-08-30 00:00:00\"", writer.toString());
    }
    
    private void serializeFormat() {
        DateFormat customFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US);
        serializer.withFormat(false, customFormat);
    }
    
    private class DateTimeSerializer extends DateTimeSerializerBase<Date> {
        
        public DateTimeSerializer() {
            super(Date.class, false, null);
        }
        
        public DateTimeSerializer(Class<Date> type, Boolean useTimestamp, DateFormat customFormat) {
            super(type, useTimestamp, customFormat);
        }
        
        @Override
        public DateTimeSerializerBase<Date> withFormat(Boolean timestamp, DateFormat customFormat) {
            return new DateTimeSerializer(Date.class, timestamp, customFormat);
        }
        
        @Override
        protected long _timestamp(Date value) {
            return value.getTime();
        }
        
        @Override
        public void serialize(Date value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
            String dateString = ISO_FORMAT.format(value);
            gen.writeString(dateString);
        }
    }
    
    private class DummySerializerProvider extends SerializerProvider {
        
        protected DummySerializerProvider() {
            super(new ObjectMapper(), new JsonFactory(), false);
        }
        
        @Override
        public Locale getLocale() {
            return Locale.US;
        }
        
        @Override
        public TimeZone getTimeZone() {
            return TimeZone.getDefault();
        }
    }
    
    private class MockJsonFormatVisitorWrapper implements JsonFormatVisitorWrapper {
        
        private JsonStringFormatVisitor stringFormatVisitor;
        private JsonIntegerFormatVisitor integerFormatVisitor;
        
        public JsonStringFormatVisitor getStringFormat() {
            return stringFormatVisitor;
        }
        
        public JsonIntegerFormatVisitor getIntegerFormat() {
            return integerFormatVisitor;
        }
        
        @Override
        public SerializerProvider getProvider() {
            return new DummySerializerProvider();
        }
        
        @Override
        public JsonFormatVisitorWrapper setProvider(SerializerProvider provider) {
            return null;
        }
        
        @Override
        public void expectStringFormat(Class<?> type) throws JsonMappingException {
            stringFormatVisitor = new MockJsonStringFormatVisitor();
        }
        
        @Override
        public void expectIntegerFormat(Class<?> type) throws JsonMappingException {
            integerFormatVisitor = new MockJsonIntegerFormatVisitor();
        }
        
        @Override
        public void expectAnyFormat(Class<?> type) throws JsonMappingException {
            
        }
        
        @Override
        public void expectArrayFormat(Class<?> elementType) throws JsonMappingException {
            
        }
        
        @Override
        public void expectArrayFormat(Class<?> type, JsonFormatTypes itemType) throws JsonMappingException {
            
        }
        
        @Override
        public void expectMapFormat(Class<?> keyType, Class<?> valueType) throws JsonMappingException {
            
        }
        
        @Override
        public void expectMapFormat(Class<?> type, JsonFormatTypes valueType) throws JsonMappingException {
            
        }
        
        @Override
        public void expectEnumFormat(Class<?> enumType) throws JsonMappingException {
            
        }
        
        @Override
        public void expectNumberFormat(Class<?> type) throws JsonMappingException {
            
        }
        
    }
    
    private class MockJsonStringFormatVisitor extends JsonStringFormatVisitor.Base implements JsonStringFormatVisitor {
        
        private JsonValueFormat format;
        
        @Override
        public void format(JsonValueFormat format) {
            this.format = format;
        }
        
        public JsonValueFormat getFormat() {
            return format;
        }
        
        @Override
        public void enumTypes(Class<?> type, Iterable<Class<?>> enumTypes) throws JsonMappingException {
            
        }
        
        @Override
        public void enumTypes(Class<?> type, Class<?>... enumTypes) throws JsonMappingException {
            
        }
        
    }
    
    private class MockJsonIntegerFormatVisitor implements JsonIntegerFormatVisitor.Base {
        
        private JsonValueFormat format;
        private JsonParser.NumberType type;
        
        @Override
        public void format(JsonValueFormat format) {
            this.format = format;
        }
        
        @Override
        public void numberType(JsonParser.NumberType type) {
            this.type = type;
        }
    }
}