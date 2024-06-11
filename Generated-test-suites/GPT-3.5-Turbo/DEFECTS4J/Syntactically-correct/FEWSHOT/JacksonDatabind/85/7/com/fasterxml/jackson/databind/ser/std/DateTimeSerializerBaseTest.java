import static org.junit.Assert.*;
import org.junit.Test;
import java.io.IOException;
import java.lang.reflect.Type;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.TimeZone;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.jsonFormatVisitors.*;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import com.fasterxml.jackson.databind.util.StdDateFormat;

public class DateTimeSerializerBaseTest {
    @Test
    public void testConstructor() {
        DateTimeSerializerBase serializer = new DateTimeSerializerBase();
        assertNotNull(serializer);
    }
    
    @Test
    public void testWithFormat() {
        DateTimeSerializerBase serializer = new DateTimeSerializerBase();
        DateTimeSerializerBase newSerializer = serializer.withFormat(true, null);
        assertEquals(true, newSerializer._useTimestamp);
        
        newSerializer = serializer.withFormat(false, null);
        assertEquals(false, newSerializer._useTimestamp);
        
        SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd");
        newSerializer = serializer.withFormat(false, format);
        assertEquals(false, newSerializer._useTimestamp);
        assertEquals(format, newSerializer._customFormat);
    }
    
    @Test
    public void testCreateContextual() {
        DateTimeSerializerBase serializer = new DateTimeSerializerBase();
        SerializerProvider serializers = null;
        BeanProperty property = null;
        
        JsonSerializer<?> contextSerializer = serializer.createContextual(serializers, property);
        assertEquals(serializer, contextSerializer);
    }
    
    @Test
    public void testIsEmpty() {
        DateTimeSerializerBase serializer = new DateTimeSerializerBase();
        Object value = null;
        assertEquals(true, serializer.isEmpty(value));
        
        value = new Object();
        assertEquals(false, serializer.isEmpty(value));
    }
    
    @Test
    public void testGetSchema() {
        DateTimeSerializerBase serializer = new DateTimeSerializerBase();
        SerializerProvider serializers = null;
        Type typeHint = null;
        JsonNode schema = serializer.getSchema(serializers, typeHint);
        assertNotNull(schema);
    }
    
    @Test
    public void testAcceptJsonFormatVisitor() {
        DateTimeSerializerBase serializer = new DateTimeSerializerBase();
        JsonFormatVisitorWrapper visitor = null;
        JavaType typeHint = null;
        
        try {
            serializer.acceptJsonFormatVisitor(visitor, typeHint);
            assertTrue(true);
        } catch (JsonMappingException ex) {
            fail("Exception should not have been thrown.");
        }
    }
    
    @Test
    public void testSerialize() {
        DateTimeSerializerBase serializer = new DateTimeSerializerBase();
        JsonGenerator gen = null;
        SerializerProvider serializers = null;
        Object value = null;
        
        try {
            serializer.serialize(value, gen, serializers);
            assertTrue(true);
        } catch (IOException ex) {
            fail("Exception should not have been thrown.");
        }
    }
}