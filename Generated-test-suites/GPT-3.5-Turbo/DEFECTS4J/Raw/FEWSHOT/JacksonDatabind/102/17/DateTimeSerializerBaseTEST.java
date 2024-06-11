package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;
import java.util.concurrent.atomic.AtomicReference;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class DateTimeSerializerBaseTest {
    
    @Mock
    private DateFormat dateFormat;
    
    private DateTimeSerializerBase<TestObject> serializer;
    
    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        serializer = new DateTimeSerializerBase<TestObject>(TestObject.class, true, dateFormat) {
            @Override
            protected long _timestamp(TestObject value) {
                // TODO Auto-generated method stub
                return 0;
            }

            @Override
            public DateTimeSerializerBase<TestObject> withFormat(Boolean timestamp, DateFormat customFormat) {
                // TODO Auto-generated method stub
                return null;
            }

            @Override
            public void serialize(TestObject value, JsonGenerator gen, SerializerProvider serializers)
                    throws IOException {
                // TODO Auto-generated method stub
                
            }
        };
    }
    
    @Test
    public void testDateTimeSerializerBase() {
        assertEquals(TestObject.class, serializer.handledType());
        assertEquals(true, serializer._useTimestamp);
        assertEquals(dateFormat, serializer._customFormat);
        assertNull(serializer._reusedCustomFormat);
    }
    
    @Test
    public void testCreateContextualWithoutFormat() throws JsonMappingException {
        SerializerProvider serializers = mock(SerializerProvider.class);
        BeanProperty property = mock(BeanProperty.class);

        DateTimeSerializerBase<TestObject> result = serializer.createContextual(serializers, property);

        assertEquals(serializer, result);
    }
    
    @Test
    public void testCreateContextualWithNumericTimestampShape() throws JsonMappingException {
        SerializerProvider serializers = mock(SerializerProvider.class);
        BeanProperty property = mock(BeanProperty.class);

        JsonFormat.Value format = mock(JsonFormat.Value.class);
        when(format.getShape()).thenReturn(JsonFormat.Shape.NUMBER);
        
        when(serializer.findFormatOverrides(serializers, property, TestObject.class)).thenReturn(format);

        DateTimeSerializerBase<TestObject> result = serializer.createContextual(serializers, property);
        
        assertEquals(Boolean.TRUE, result._useTimestamp);
        assertNull(result._customFormat);
    }
    
    @Test
    public void testCreateContextualWithPattern() throws JsonMappingException {
        SerializerProvider serializers = mock(SerializerProvider.class);
        BeanProperty property = mock(BeanProperty.class);

        JsonFormat.Value format = mock(JsonFormat.Value.class);
        when(format.getShape()).thenReturn(JsonFormat.Shape.STRING);
        when(format.hasPattern()).thenReturn(true);
        when(format.getPattern()).thenReturn("yyyy-MM-dd");
        when(format.hasLocale()).thenReturn(true);
        when(format.getLocale()).thenReturn(Locale.US);
        when(format.hasTimeZone()).thenReturn(true);
        when(format.getTimeZone()).thenReturn(TimeZone.getDefault());
        
        when(serializer.findFormatOverrides(serializers, property, TestObject.class)).thenReturn(format);

        DateTimeSerializerBase<TestObject> result = serializer.createContextual(serializers, property);
        
        assertEquals(Boolean.FALSE, result._useTimestamp);
        assertNotNull(result._customFormat);
    }
    
    @Test
    public void testCreateContextualWithOtherChanges() throws JsonMappingException {
        SerializerProvider serializers = mock(SerializerProvider.class);
        BeanProperty property = mock(BeanProperty.class);

        JsonFormat.Value format = mock(JsonFormat.Value.class);
        when(format.getShape()).thenReturn(JsonFormat.Shape.STRING);
        when(format.hasPattern()).thenReturn(false);
        when(format.hasLocale()).thenReturn(true);
        when(format.getLocale()).thenReturn(Locale.US);
        when(format.hasTimeZone()).thenReturn(true);
        when(format.getTimeZone()).thenReturn(TimeZone.getDefault());
        
        when(serializer.findFormatOverrides(serializers, property, TestObject.class)).thenReturn(format);

        DateTimeSerializerBase<TestObject> result = serializer.createContextual(serializers, property);
        
        assertEquals(Boolean.FALSE, result._useTimestamp);
        assertNotNull(result._customFormat);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testCreateContextualWithNullProvider() throws JsonMappingException {
        serializer.createContextual(null, null);
    }
    
    @Test
    public void testIsEmpty() {
        SerializerProvider serializers = mock(SerializerProvider.class);
        TestObject value = new TestObject();
        
        boolean result = serializer.isEmpty(serializers, value);
        
        assertEquals(false, result);
    }
    
    @Test
    public void testGetSchema() {
        SerializerProvider serializers = mock(SerializerProvider.class);
        Type typeHint = mock(Type.class);
        
        JsonNode result = serializer.getSchema(serializers, typeHint);
        
        assertNotNull(result);
    }
    
    @Test
    public void testAcceptJsonFormatVisitor() throws JsonMappingException {
        JsonFormatVisitorWrapper visitor = mock(JsonFormatVisitorWrapper.class);
        JavaType typeHint = mock(JavaType.class);
        
        serializer.acceptJsonFormatVisitor(visitor, typeHint);
        
        verify(visitor).getProvider();
    }
    
    @Test
    public void testSerializeAsStringWithoutCustomFormat() throws IOException {
        JsonGenerator gen = mock(JsonGenerator.class);
        SerializerProvider provider = mock(SerializerProvider.class);
        Date value = new Date();
        
        serializer._serializeAsString(value, gen, provider);
        
        verify(provider).defaultSerializeDateValue(value, gen);
    }
    
    @Test
    public void testSerializeAsStringWithCustomFormatAndReusedFormatter() throws IOException {
        JsonGenerator gen = mock(JsonGenerator.class);
        SerializerProvider provider = mock(SerializerProvider.class);
        Date value = new Date();
        
        when(serializer._reusedCustomFormat.getAndSet(null)).thenReturn(dateFormat);
        
        serializer._serializeAsString(value, gen, provider);
        
        verify(gen).writeString(anyString());
        verify(serializer._reusedCustomFormat).compareAndSet(null, dateFormat);
    }
    
    @Test
    public void testSerializeAsStringWithCustomFormatAndNewFormatter() throws IOException {
        JsonGenerator gen = mock(JsonGenerator.class);
        SerializerProvider provider = mock(SerializerProvider.class);
        Date value = new Date();
        
        when(serializer._reusedCustomFormat.getAndSet(null)).thenReturn(null);
        when(serializer._customFormat.clone()).thenReturn(dateFormat);
        
        serializer._serializeAsString(value, gen, provider);
        
        verify(gen).writeString(anyString());
        verify(serializer._reusedCustomFormat).compareAndSet(null, dateFormat);
    }
    
    private static class TestObject {
        
    }
}