
import static org.junit.Assert.*;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

public class DateTimeSerializerBaseTest {
    
    private DateTimeSerializerBase<Date> serializer;
    private SerializerProvider provider;

    @Before
    public void setUp() {
        serializer = new DateTimeSerializerBase<Date>(Date.class, null, null) {

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
                return;
            }
            
        };
        
        provider = new SerializerProvider();
    }
    
    
    // Test if isEmpty() returns true for null value
    @Test
    public void testIsEmptyForNullValue() {
        assertTrue(serializer.isEmpty(provider, null));
    }
    
    // Test if isEmpty() returns true for date with timestamp 0
    @Test
    public void testIsEmptyForZeroTimestamp() {
        assertTrue(serializer.isEmpty(provider, new Date(0)));
    }
    
    // Test if isEmpty() returns false for date with non-zero timestamp
    @Test
    public void testIsEmptyForNonZeroTimestamp() {
        assertFalse(serializer.isEmpty(provider, new Date(1498902000000L))); // 01-Jul-2017 00:00:00 GMT
    }
    
    // Test that getSchema() returns a schema node with "number" format when _useTimestamp is true
    @Test
    public void testGetSchemaWithTimestamp() {
        JsonSerializer<?> jsonSerializer = serializer;
        DateFormat customFormat = new SimpleDateFormat("dd-MM-yyyy");
        DateTimeSerializerBase<Date> dateSerializer = serializer.withFormat(true, customFormat);
        
        assertEquals("number", dateSerializer.getSchema(provider, null).get("type").asText());
    }
    
    // Test that getSchema() returns a schema node with "string" format when _useTimestamp is false
    @Test
    public void testGetSchemaWithoutTimestamp() {
        JsonSerializer<?> jsonSerializer = serializer;
        DateFormat customFormat = new SimpleDateFormat("dd-MM-yyyy");
        DateTimeSerializerBase<Date> dateSerializer = serializer.withFormat(false, customFormat);
        
        assertEquals("string", dateSerializer.getSchema(provider, null).get("type").asText());
    }
    
    // Test if acceptJsonFormatVisitor() calls visitIntFormat() when _useTimestamp is true
    @Test
    public void testAcceptJsonFormatVisitorWithTimestamp() throws JsonMappingException {
        JsonSerializer<?> jsonSerializer = serializer;
        JsonFormatVisitorWrapper visitor = new JsonFormatVisitorWrapper.Base(provider) {};
        DateTimeSerializerBase<Date> dateSerializer = serializer.withFormat(true, null);
        JavaType typeHint = null;
        
        dateSerializer.acceptJsonFormatVisitor(visitor, typeHint);
        
        assertTrue(visitor.hasNumberFormat());
        assertFalse(visitor.hasStringFormat());
    }
    
    // Test if acceptJsonFormatVisitor() calls visitStringFormat() when _useTimestamp is false
    @Test
    public void testAcceptJsonFormatVisitorWithoutTimestamp() throws JsonMappingException {
        JsonSerializer<?> jsonSerializer = serializer;
        JsonFormatVisitorWrapper visitor = new JsonFormatVisitorWrapper.Base(provider) {};
        DateTimeSerializerBase<Date> dateSerializer = serializer.withFormat(false, null);
        JavaType typeHint = null;
        
        dateSerializer.acceptJsonFormatVisitor(visitor, typeHint);
        
        assertFalse(visitor.hasNumberFormat());
        assertTrue(visitor.hasStringFormat());
    }
}
