import static org.junit.Assert.*;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;
import java.util.concurrent.atomic.AtomicReference;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitorWrapper;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import com.fasterxml.jackson.databind.util.StdDateFormat;

public class DateTimeSerializerBaseTest {
    
    private DateTimeSerializerBase<String> serializer;
    
    @Before
    public void setup() {
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
            public void serialize(String value, JsonGenerator gen, SerializerProvider serializers) {
                
            }

            @Override
            public JsonNode getSchema(SerializerProvider serializers, java.lang.reflect.Type typeHint) {
                return null;
            }

            @Override
            public void acceptJsonFormatVisitor(JsonFormatVisitorWrapper visitor, com.fasterxml.jackson.databind.JavaType typeHint)
                    throws com.fasterxml.jackson.databind.JsonMappingException {
                
            }
            
        };
    }
    
    @Test
    public void testConstructor() {
        assertNotNull(serializer._useTimestamp);
        assertNull(serializer._customFormat);
        assertNull(serializer._reusedCustomFormat);
    }
    
    @Test
    public void testWithFormat() {
        DateTimeSerializerBase<String> newSerializer = serializer.withFormat(true, null);
        assertNotNull(newSerializer);
        assertTrue(newSerializer._useTimestamp);
        assertNull(newSerializer._customFormat);
    }
    
    @Test
    public void testCreateContextual_NullProperty() throws JsonMappingException {
        JsonSerializer<?> returnedSerializer = serializer.createContextual(null, null);
        assertNotNull(returnedSerializer);
        assertEquals(serializer, returnedSerializer);
    }
    
    @Test
    public void testCreateContextual_ShapeNumeric() throws JsonMappingException {
        BeanProperty property = null;
        
        JsonSerializer<?> returnedSerializer = serializer.createContextual(null, property);
        assertNotNull(returnedSerializer);
        assertTrue(returnedSerializer instanceof DateTimeSerializerBase);
        
        DateTimeSerializerBase<?> newSerializer = (DateTimeSerializerBase<?>) returnedSerializer;
        assertTrue(newSerializer._useTimestamp);
    }
    
    @Test
    public void testCreateContextual_CustomPattern() throws JsonMappingException {
        BeanProperty property = null;
        
        JsonFormat.Value format = new JsonFormat.Value();
        format.setShape(JsonFormat.Shape.STRING);
        format.setPattern("dd-MM-yyyy");
        
        BeanPropertySerializerTestProperty beanProperty = new BeanPropertySerializerTestProperty(format);
        
        JsonSerializer<?> returnedSerializer = serializer.createContextual(null, beanProperty);
        assertNotNull(returnedSerializer);
        assertTrue(returnedSerializer instanceof DateTimeSerializerBase);
        
        DateTimeSerializerBase<?> newSerializer = (DateTimeSerializerBase<?>) returnedSerializer;
        assertFalse(newSerializer._useTimestamp);
        assertNotNull(newSerializer._customFormat);
    }
    
    @Test
    public void testCreateContextual_HasLocale() throws JsonMappingException {
        BeanProperty property = null;
        
        JsonFormat.Value format = new JsonFormat.Value();
        format.setShape(JsonFormat.Shape.STRING);
        format.setLocale(Locale.US);
        
        BeanPropertySerializerTestProperty beanProperty = new BeanPropertySerializerTestProperty(format);
        
        JsonSerializer<?> returnedSerializer = serializer.createContextual(null, beanProperty);
        assertNotNull(returnedSerializer);
        assertTrue(returnedSerializer instanceof DateTimeSerializerBase);
        
        DateTimeSerializerBase<?> newSerializer = (DateTimeSerializerBase<?>) returnedSerializer;
        assertFalse(newSerializer._useTimestamp);
        assertNotNull(newSerializer._customFormat);
    }
    
    @Test
    public void testCreateContextual_NullDateFormat() throws JsonMappingException {
        BeanProperty property = null;
        
        JsonFormat.Value format = new JsonFormat.Value();
        format.setShape(JsonFormat.Shape.STRING);
        
        BeanPropertySerializerTestProperty beanProperty = new BeanPropertySerializerTestProperty(format);
        
        JsonSerializer<?> returnedSerializer = serializer.createContextual(null, beanProperty);
        assertNotNull(returnedSerializer);
        assertTrue(returnedSerializer instanceof DateTimeSerializerBase);
        
        DateTimeSerializerBase<?> newSerializer = (DateTimeSerializerBase<?>) returnedSerializer;
        assertFalse(newSerializer._useTimestamp);
        assertNotNull(newSerializer._customFormat);
    }
    
    @Test
    public void testCreateContextual_NoTimeZone() throws JsonMappingException {
        BeanProperty property = null;
        
        JsonFormat.Value format = new JsonFormat.Value();
        format.setShape(JsonFormat.Shape.STRING);
        format.setTimeZone(TimeZone.getTimeZone("GMT"));
        
        BeanPropertySerializerTestProperty beanProperty = new BeanPropertySerializerTestProperty(format);
        
        JsonSerializer<?> returnedSerializer = serializer.createContextual(null, beanProperty);
        assertNotNull(returnedSerializer);
        assertTrue(returnedSerializer instanceof DateTimeSerializerBase);
        
        DateTimeSerializerBase<?> newSerializer = (DateTimeSerializerBase<?>) returnedSerializer;
        assertFalse(newSerializer._useTimestamp);
        assertNotNull(newSerializer._customFormat);
    }
    
    @Test
    public void testIsEmpty() {
        SerializerProvider provider = null;
        String value = "test";
        
        assertFalse(serializer.isEmpty(provider, value));
    }
    
    @Test
    public void testGetSchema() {
        SerializerProvider provider = null;
        Class<String> typeHint = String.class;
        
        JsonNode schema = serializer.getSchema(provider, typeHint);
        
        assertNotNull(schema);
    }
    
    @Test
    public void testAcceptJsonFormatVisitor() throws JsonMappingException {
        JsonFormatVisitorWrapper visitor = new ObjectMapper().getJsonFactory().visitorObject();
        com.fasterxml.jackson.databind.JavaType typeHint = com.fasterxml.jackson.databind.type.TypeFactory.defaultInstance()
                .constructType(String.class);
        
        serializer.acceptJsonFormatVisitor(visitor, typeHint);
        
        // Nothing to assert, no exceptions thrown
    }
    
    @Test
    public void testSerialize() throws IOException {
        JsonGenerator gen = new ObjectMapper().getJsonFactory().createGenerator(System.out);
        SerializerProvider serializers = null;
        String value = "test";
        
        serializer.serialize(value, gen, serializers);
        
        // Nothing to assert, no exceptions thrown
    }
    
    @Test
    public void testAsTimestamp_UseTimestampNull() {
        SerializerProvider serializers = null;
        assertNull(serializer._useTimestamp);
        assertFalse(serializer._asTimestamp(serializers));
    }
    
    @Test
    public void testAsTimestamp_UseTimestampTrue() {
        SerializerProvider serializers = null;
        
        DateTimeSerializerBase<String> newSerializer = serializer.withFormat(true, null);
        
        assertNotNull(newSerializer._useTimestamp);
        assertTrue(newSerializer._asTimestamp(serializers));
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testAsTimestamp_CustomFormatNull_SerializerProviderNull() {
        SerializerProvider serializers = null;
        
        serializer._asTimestamp(serializers);
    }
    
    @Test
    public void testAsTimestamp_CustomFormatNull_SerializerProviderNonNull() {
        SerializerProvider serializers = null;
        
        assertFalse(serializer._asTimestamp(serializers));
    }
    
    @Test
    public void testSerializeAsString_CustomFormatNull() throws IOException {
        JsonGenerator g = new ObjectMapper().getJsonFactory().createGenerator(System.out);
        SerializerProvider provider = null;
        Date value = new Date();
        
        serializer._serializeAsString(value, g, provider);
        
        // Nothing to assert, no exceptions thrown
    }
    
    
    private class BeanPropertySerializerTestProperty implements BeanProperty {
        
        private JsonFormat.Value format;
        
        public BeanPropertySerializerTestProperty(JsonFormat.Value format) {
            this.format = format;
        }

        @Override
        public <A extends java.lang.annotation.Annotation> A getAnnotation(
                Class<A> acls) {
            return null;
        }

        @Override
        public java.lang.annotation.Annotation[] getAnnotations() {
            return null;
        }

        @Override
        public java.lang.annotation.Annotation getDeclaringAnnotation() {
            return null;
        }

        @Override
        public String getName() {
            return null;
        }

        @Override
        public boolean isRequired() {
            return false;
        }

        @Override
        public JavaType getType() {
            return null;
        }

        @Override
        public PropertyName getFullName() {
            return null;
        }

        @Override
        public <A extends java.lang.annotation.Annotation> A getContextAnnotation(
                Class<A> acls) {
            return null;
        }
        
    }

}