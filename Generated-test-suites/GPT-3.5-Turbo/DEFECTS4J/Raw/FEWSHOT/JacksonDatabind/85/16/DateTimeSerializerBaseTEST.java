package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.TimeZone;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;

import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.jsonFormatVisitors.*;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import com.fasterxml.jackson.databind.util.StdDateFormat;

@SuppressWarnings("serial")
public class DateTimeSerializerBaseTest {

    @Mock
    private JsonSerializer<?> mockJsonSerializer;
    
    @Mock
    private SerializerProvider mockSerializerProvider;
    
    @Mock
    private JsonFormatVisitorWrapper mockJsonFormatVisitorWrapper;
    
    @Mock
    private JavaType mockJavaType;
    
    private DateTimeSerializerBase<TestObject> dateTimeSerializerBase;
    
    private DateFormat customDateFormat;
    
    private TestObject testObject;
    
    private static final TimeZone MOCK_TIMEZONE = TimeZone.getDefault();
    private static final String MOCK_PATTERN = "yyyy-MM-dd HH:mm:ss";
    private static final Locale MOCK_LOCALE = Locale.getDefault();
    
    private static final String MOCK_DATE = "2019-01-01 12:00:00";
    
    private static class TestObject {}
    
    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        
        customDateFormat = new SimpleDateFormat(MOCK_PATTERN, MOCK_LOCALE);
        
        dateTimeSerializerBase = new DateTimeSerializerBase<TestObject>(TestObject.class,
                true, customDateFormat) {

            @Override
            public DateTimeSerializerBase<TestObject> withFormat(Boolean timestamp, DateFormat customFormat) {
                return null;
            }

            @Override
            protected long _timestamp(TestObject value) {
                return 0;
            }

            @Override
            public void serialize(TestObject value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
                // No implementation needed for this test
            }
            
        };
        
        testObject = new TestObject();
    }
    
    @Test
    public void testCreateEmptyDateTimeSerializerBase() {
        DateTimeSerializerBase<TestObject> dateTimeSerializerBase = new DateTimeSerializerBase<TestObject>(TestObject.class,
                false, null) {

            @Override
            public DateTimeSerializerBase<TestObject> withFormat(Boolean timestamp, DateFormat customFormat) {
                return null;
            }

            @Override
            protected long _timestamp(TestObject value) {
                return 0;
            }

            @Override
            public void serialize(TestObject value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
                // No implementation needed for this test
            }
            
        };
        
        assertNotNull(dateTimeSerializerBase);
    }
    
    @Test
    public void testCreateDateTimeSerializerBaseWithCustomFormat() {
        DateTimeSerializerBase<TestObject> dateTimeSerializerBase = new DateTimeSerializerBase<TestObject>(TestObject.class,
                false, customDateFormat) {

            @Override
            public DateTimeSerializerBase<TestObject> withFormat(Boolean timestamp, DateFormat customFormat) {
                return null;
            }

            @Override
            protected long _timestamp(TestObject value) {
                return 0;
            }

            @Override
            public void serialize(TestObject value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
                // No implementation needed for this test
            }
            
        };
        
        assertNotNull(dateTimeSerializerBase);
    }
    
    @Test
    public void testCreateDateTimeSerializerBaseWithContextualSerializer() throws JsonMappingException {
        JsonFormat.Value mockJsonFormatValue = mock(JsonFormat.Value.class);
        when(mockJsonFormatValue.getShape()).thenReturn(JsonFormat.Shape.STRING);
        when(mockJsonFormatValue.hasPattern()).thenReturn(true);
        when(mockJsonFormatValue.getPattern()).thenReturn(MOCK_PATTERN);
        when(mockJsonFormatValue.hasLocale()).thenReturn(true);
        when(mockJsonFormatValue.getLocale()).thenReturn(MOCK_LOCALE);
        when(mockJsonFormatValue.getTimeZone()).thenReturn(MOCK_TIMEZONE);
        
        SerializerProvider mockSerializerProvider = mock(SerializerProvider.class);
        DateTimeSerializerBase<TestObject> contextualDateTimeSerializerBase = (DateTimeSerializerBase<TestObject>) dateTimeSerializerBase.createContextual(mockSerializerProvider, null);
        
        assertNotNull(contextualDateTimeSerializerBase);
    }
    
    @Test
    public void testIsEmptyWithNullValue() {
        assertTrue(dateTimeSerializerBase.isEmpty(null));
    }
    
    @Test
    public void testIsEmptyWithValue() {
        assertFalse(dateTimeSerializerBase.isEmpty(testObject));
    }
    
    @Test
    public void testGetSchemaAsTimestamp() {
        JsonNode schema = dateTimeSerializerBase.getSchema(mockSerializerProvider, null);
        assertNotNull(schema);
        assertEquals("number", schema.get("type").asText());
    }
    
    @Test
    public void testGetSchemaAsDateTime() {
        DateTimeSerializerBase<TestObject> dateTimeSerializerBase = new DateTimeSerializerBase<TestObject>(TestObject.class,
                false, customDateFormat) {

            @Override
            public DateTimeSerializerBase<TestObject> withFormat(Boolean timestamp, DateFormat customFormat) {
                return null;
            }

            @Override
            protected long _timestamp(TestObject value) {
                return 0;
            }

            @Override
            public void serialize(TestObject value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
                // No implementation needed for this test
            }
            
        };
        
        JsonNode schema = dateTimeSerializerBase.getSchema(mockSerializerProvider, null);
        assertNotNull(schema);
        assertEquals("string", schema.get("type").asText());
    }
    
    @Test
    public void testAcceptJsonFormatVisitorAsNumber() throws JsonMappingException {
        when(mockJsonFormatVisitorWrapper.getProvider()).thenReturn(mockSerializerProvider);
        dateTimeSerializerBase.acceptJsonFormatVisitor(mockJsonFormatVisitorWrapper, mockJavaType);
        verify(mockJsonFormatVisitorWrapper).visitIntFormat(
                eq(mockJavaType), eq(JsonParser.NumberType.LONG), eq(JsonValueFormat.UTC_MILLISEC));
    }
    
    @Test
    public void testAcceptJsonFormatVisitorAsString() throws JsonMappingException {
        DateTimeSerializerBase<TestObject> dateTimeSerializerBase = new DateTimeSerializerBase<TestObject>(TestObject.class,
                false, customDateFormat) {

            @Override
            public DateTimeSerializerBase<TestObject> withFormat(Boolean timestamp, DateFormat customFormat) {
                return null;
            }

            @Override
            protected long _timestamp(TestObject value) {
                return 0;
            }

            @Override
            public void serialize(TestObject value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
                // No implementation needed for this test
            }
            
        };
        
        when(mockJsonFormatVisitorWrapper.getProvider()).thenReturn(mockSerializerProvider);
        dateTimeSerializerBase.acceptJsonFormatVisitor(mockJsonFormatVisitorWrapper, mockJavaType);
        verify(mockJsonFormatVisitorWrapper).visitStringFormat(eq(mockJavaType), eq(JsonValueFormat.DATE_TIME));
    }
    
    @Test
    public void testSerialize() throws IOException {
        JsonGenerator mockJsonGenerator = mock(JsonGenerator.class);
        SerializerProvider mockSerializerProvider = mock(SerializerProvider.class);
        dateTimeSerializerBase.serialize(testObject, mockJsonGenerator, mockSerializerProvider);
        verify(mockJsonGenerator).writeString(MOCK_DATE);
    }
    
}