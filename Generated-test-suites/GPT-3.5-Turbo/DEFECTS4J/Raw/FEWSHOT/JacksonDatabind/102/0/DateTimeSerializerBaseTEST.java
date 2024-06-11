package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;
import java.util.concurrent.atomic.AtomicReference;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonFormat;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitorWrapper;

public class DateTimeSerializerBaseTest {
    
    private static final DateFormat CUSTOM_FORMAT = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS", Locale.ENGLISH);
    private static final Date DATE_VALUE = new Date(0);
    private DateTimeSerializerBase<Date> serializer;
    private JsonSerializer<?> contextualSerializer;
    
    @Before
    public void setup() {
        serializer = new DateTimeSerializerBase<Date>(Date.class, true, null) {

            @Override
            public DateTimeSerializerBase<Date> withFormat(Boolean timestamp, DateFormat customFormat) {
                return null;
            }

            @Override
            protected long _timestamp(Date value) {
                return 0;
            }

            @Override
            public void serialize(Date value, JsonGenerator gen, SerializerProvider serializers)
                    throws IOException {
            }
        };
        contextualSerializer = new JsonSerializer<Object>() {
            
            @Override
            public void serialize(Object value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
            }
            
            @Override
            public void acceptJsonFormatVisitor(JsonFormatVisitorWrapper visitor, JavaType typeHint) throws JsonMappingException {
            }
            
            @Override
            public JsonNode getSchema(SerializerProvider serializers, java.lang.reflect.Type typeHint) throws JsonMappingException {
                return null;
            }
            
            @Override
            public JsonSerializer<?> createContextual(SerializerProvider prov, BeanProperty property)
                    throws JsonMappingException {
                return null;
            }
        };
    }
    
    @Test
    public void testEmpty() {
        SerializerProvider serializerProvider = mock(SerializerProvider.class);
        boolean result = serializer.isEmpty(serializerProvider, DATE_VALUE);
        assertEquals(false, result);
    }
    
    @Test
    public void testGetSchemaAsTimestamp() {
        SerializerProvider serializerProvider = mock(SerializerProvider.class);
        JsonNode jsonNode = serializer.getSchema(serializerProvider, null);
        assertEquals("number", jsonNode.asText());
    }
    
    @Test
    public void testGetSchemaAsString() {
        SerializationFeature serializationFeature = mock(SerializationFeature.class);
        when(serializerProvider.isEnabled(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)).thenReturn(false);
        JsonNode jsonNode = serializer.getSchema(serializerProvider, null);
        assertEquals("string", jsonNode.asText());
    }
    
    @Test
    public void testAcceptJsonFormatVisitorAsTimestamp() throws JsonMappingException {
        JsonFormatVisitorWrapper jsonFormatVisitorWrapper = mock(JsonFormatVisitorWrapper.class);
        JavaType javaType = mock(JavaType.class);
        serializer.acceptJsonFormatVisitor(jsonFormatVisitorWrapper, javaType);
        verify(jsonFormatVisitorWrapper).visitIntFormat(javaType, JsonParser.NumberType.LONG, JsonValueFormat.UTC_MILLISEC);
    }
    
    @Test
    public void testAcceptJsonFormatVisitorAsString() throws JsonMappingException {
        JsonFormatVisitorWrapper jsonFormatVisitorWrapper = mock(JsonFormatVisitorWrapper.class);
        JavaType javaType = mock(JavaType.class);
        serializer.acceptJsonFormatVisitor(jsonFormatVisitorWrapper, javaType);
        verify(jsonFormatVisitorWrapper).visitStringFormat(javaType, JsonValueFormat.DATE_TIME);
    }
    
    @Test
    public void testSerializeWithCustomFormatInPool() throws IOException {
        JsonGenerator jsonGenerator = mock(JsonGenerator.class);
        SerializerProvider serializerProvider = mock(SerializerProvider.class);
        serializer._reusedCustomFormat.set(CUSTOM_FORMAT);
        serializer._serializeAsString(DATE_VALUE, jsonGenerator, serializerProvider);
        verify(jsonGenerator).writeString(CUSTOM_FORMAT.format(DATE_VALUE));
    }
    
    @Test
    public void testSerializeWithoutCustomFormatInPool() throws IOException {
        JsonGenerator jsonGenerator = mock(JsonGenerator.class);
        SerializerProvider serializerProvider = mock(SerializerProvider.class);
        serializer._reusedCustomFormat.set(null);
        serializer._customFormat = CUSTOM_FORMAT;
        serializer._serializeAsString(DATE_VALUE, jsonGenerator, serializerProvider);
        verify(jsonGenerator).writeString(CUSTOM_FORMAT.format(DATE_VALUE));
    }
    
    //Incomplete...
}