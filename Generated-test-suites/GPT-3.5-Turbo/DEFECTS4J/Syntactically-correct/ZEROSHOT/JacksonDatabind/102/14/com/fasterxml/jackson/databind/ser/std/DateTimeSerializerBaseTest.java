import static org.junit.Assert.*;
import org.junit.Test;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;
import java.util.concurrent.atomic.AtomicReference;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.jsonFormatVisitors.*;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import com.fasterxml.jackson.databind.util.StdDateFormat;

public class DateTimeSerializerBaseTest {
    
    @Test
    public void testWithFormat() {
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        DateTimeSerializerBase<TestObject> serializer = new DateTimeSerializerBase<TestObject>(TestObject.class, false, format) {
            @Override
            public DateTimeSerializerBase<TestObject> withFormat(Boolean timestamp, DateFormat customFormat) {
                return new DateTimeSerializerBase<TestObject>(TestObject.class, timestamp, customFormat) {
                    @Override
                    public void serialize(TestObject value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
                        
                    }
                    
                    @Override
                    protected long _timestamp(TestObject value) {
                        return 0;
                    }
                    
                    @Override
                    public JsonSerializer<?> createContextual(SerializerProvider serializers, BeanProperty property) throws JsonMappingException {
                        return this;
                    }
                    
                    @Override
                    public JsonNode getSchema(SerializerProvider serializers, Type typeHint) {
                        return null;
                    }
                    
                    @Override
                    public void acceptJsonFormatVisitor(JsonFormatVisitorWrapper visitor, JavaType typeHint) throws JsonMappingException {
                        
                    }
                    
                    @Override
                    protected void _serializeAsString(Date value, JsonGenerator g, SerializerProvider provider) throws IOException {
                        
                    }
                };
            }
            
            @Override
            public void serialize(TestObject value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
                
            }
            
            @Override
            protected long _timestamp(TestObject value) {
                return 0;
            }
            
            @Override
            public JsonSerializer<?> createContextual(SerializerProvider serializers, BeanProperty property) throws JsonMappingException {
                return this;
            }
            
            @Override
            public JsonNode getSchema(SerializerProvider serializers, Type typeHint) {
                return null;
            }
            
            @Override
            public void acceptJsonFormatVisitor(JsonFormatVisitorWrapper visitor, JavaType typeHint) throws JsonMappingException {
                
            }
            
            @Override
            protected void _serializeAsString(Date value, JsonGenerator g, SerializerProvider provider) throws IOException {
                
            }
        };
        
        DateTimeSerializerBase<TestObject> result = serializer.withFormat(false, format);
        
        assertNotNull(result);
        assertEquals(TestObject.class, result.handledType());
        assertFalse(result._asTimestamp(null));
    }
    
    private class TestObject {
        private Date date;
        
        public void setDate(Date date) {
            this.date = date;
        }
        
        public Date getDate() {
            return date;
        }
    }
}