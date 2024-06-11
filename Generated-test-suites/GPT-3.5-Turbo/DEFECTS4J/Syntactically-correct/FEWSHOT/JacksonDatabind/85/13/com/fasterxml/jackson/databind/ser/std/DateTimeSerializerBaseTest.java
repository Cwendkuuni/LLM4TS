package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ser.SerializerProvider;
import java.io.IOException;
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
    
    private DateTimeSerializerBase<TestDateTime> serializer;
    private JsonSerializer<?> contextualSerializer;
    
    @Before
    public void setUp() {
        serializer = new DateTimeSerializerBase<TestDateTime>(TestDateTime.class, false, null) {
            private static final long serialVersionUID = 1L;

			@Override
			public DateTimeSerializerBase<TestDateTime> withFormat(Boolean timestamp, DateFormat customFormat) {
				return null;
			}

			@Override
			protected long _timestamp(TestDateTime value) {
				return 0;
			}

			@Override
			public void serialize(TestDateTime value, JsonGenerator gen, SerializerProvider serializers)
					throws IOException {
			}
        };
        
        PropertyAccessorTest mockPropertyAccessor = mock(PropertyAccessorTest.class);
        when(mockPropertyAccessor.getName()).thenReturn("timestamp");
    }
    
    @Test
    public void testWithFormat() {
        DateTimeSerializerBase<TestDateTime> newSerializer = serializer.withFormat(true, null);
        assertEquals(true, newSerializer._useTimestamp);
        assertNull(newSerializer._customFormat);
        
        newSerializer = serializer.withFormat(false, mock(DateFormat.class));
        assertEquals(false, newSerializer._useTimestamp);
        assertNotNull(newSerializer._customFormat);
    }
    
    @Test
    public void testCreateContextual() throws JsonMappingException {
        SerializerProvider serializers = mock(SerializerProvider.class);
        BeanProperty property = mock(BeanProperty.class);
        
        DateTimeSerializerBase<?> newSerializer = serializer.createContextual(serializers, property);
        assertEquals(DateTimeSerializerBase.class, newSerializer.getClass());
        assertSame(serializer, newSerializer);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testCreateContextualWithNullProperty() throws JsonMappingException {
        SerializerProvider serializers = mock(SerializerProvider.class);
        
        serializer.createContextual(serializers, null);
    }
    
    @Test
    public void testIsEmptyWithValue() {
        assertFalse(serializer.isEmpty(new TestDateTime()));
    }
    
    @Test
    public void testIsEmptyWithNullValue() {
        assertTrue(serializer.isEmpty(null));
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testIsEmptyWithNullSerializerProvider() {
        serializer.isEmpty(null, new TestDateTime());
    }
    
    @Test
    public void testGetSchema() {
        SerializerProvider serializers = mock(SerializerProvider.class);
        
        JsonNode schema = serializer.getSchema(serializers, TestDateTime.class);
        assertEquals("string", schema.path("type").asText());
        assertTrue(schema.path("required").asBoolean());
    }
    
    @Test
    public void testAcceptJsonFormatVisitorWithNumericTimestamp() throws JsonMappingException {
        JsonFormatVisitorWrapper visitor = mock(JsonFormatVisitorWrapper.class);
        JavaType typeHint = mock(JavaType.class);
        SerializerProvider provider = mock(SerializerProvider.class);
        
        when(serializer._asTimestamp(provider)).thenReturn(true);
        
        serializer.acceptJsonFormatVisitor(visitor, typeHint);
        
        verify(visitor, times(1)).getProvider();
        verify(visitor, times(1)).expectInteger();
    }
    
    @Test
    public void testAcceptJsonFormatVisitorWithStringTimestamp() throws JsonMappingException {
        JsonFormatVisitorWrapper visitor = mock(JsonFormatVisitorWrapper.class);
        JavaType typeHint = mock(JavaType.class);
        SerializerProvider provider = mock(SerializerProvider.class);
        
        when(serializer._asTimestamp(provider)).thenReturn(false);
        
        serializer.acceptJsonFormatVisitor(visitor, typeHint);
        
        verify(visitor, times(1)).getProvider();
        verify(visitor, times(1)).expectString();
    }
}

private class TestDateTime {
    private long timestamp;
}