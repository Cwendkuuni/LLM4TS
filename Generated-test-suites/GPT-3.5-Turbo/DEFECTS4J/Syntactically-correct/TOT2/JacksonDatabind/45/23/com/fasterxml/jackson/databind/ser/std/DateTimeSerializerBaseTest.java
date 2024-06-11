import static org.junit.Assert.*;
import org.junit.Test;
import org.mockito.Mock;
import static org.mockito.Mockito.*;
import com.fasterxml.jackson.databind.ser.std.DateTimeSerializerBase;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import com.fasterxml.jackson.databind.ser.std.StdScalarSerializer;

public class DateTimeSerializerBaseTest {
    
    @Test
    public void testConstructor() {
        DateTimeSerializerBase<Object> serializer = new DateTimeSerializerBase<Object>(Object.class, true, null) {
            @Override
            public DateTimeSerializerBase<Object> withFormat(Boolean timestamp, DateFormat customFormat) {
                return null;
            }

            @Override
            protected long _timestamp(Object value) {
                return 0;
            }

            @Override
            public void serialize(Object value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
                // Do nothing
            }
        };
        
        assertNotNull(serializer);
    }
    
    @Test
    public void testIsEmpty() {
        DateTimeSerializerBase<Object> serializer = new DateTimeSerializerBase<Object>(Object.class, true, null) {
            @Override
            protected long _timestamp(Object value) {
                return 0;
            }
            
            @Override
            public void serialize(Object value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
                // Do nothing
            }
        };
        
        assertTrue(serializer.isEmpty(null));
    }
    
    @Test
    public void testGetSchema() {
        DateTimeSerializerBase<Object> serializer = new DateTimeSerializerBase<Object>(Object.class, true, null) {
            @Override
            protected long _timestamp(Object value) {
                return 0;
            }
            
	    @Override
            public void serialize(Object value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
                // Do nothing
            }
        };
        
        assertNotNull(serializer.getSchema(null, null));
    }
    
    @Test
    public void testAcceptJsonFormatVisitor() {
        DateTimeSerializerBase<Object> serializer = new DateTimeSerializerBase<Object>(Object.class, true, null) {
            @Override
            protected long _timestamp(Object value) {
                return 0;
            }

            @Override
            public void serialize(Object value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
                // Do nothing
            }
            
	    @Override
            protected void _acceptJsonFormatVisitor(JsonFormatVisitorWrapper visitor, JavaType typeHint, boolean asNumber) throws JsonMappingException {
                // Do nothing
            }
        };
        
        // Need to find a way to test this method
    }
    
    @Test
    public void testSerialize() {
        DateTimeSerializerBase<Object> serializer = new DateTimeSerializerBase<Object>(Object.class, true, null) {
            @Override
            protected long _timestamp(Object value) {
                return 0;
            }

            @Override
            public void serialize(Object value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
                // Do nothing
            }
        };
        
        // Need to find a way to test this method
    }
}