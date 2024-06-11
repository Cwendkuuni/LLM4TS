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

import com.fasterxml.jackson.databind.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitorWrapper;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonValueFormat;
import com.fasterxml.jackson.databind.ser.std.DateTimeSerializerBase;

public class DateTimeSerializerBaseTest {
    
    private DateTimeSerializerBase<Object> serializer;
    private Object value;
    private JsonGenerator gen;
    private SerializerProvider provider;
    private DateFormat dateFormat;
    private SimpleDateFormat simpleDateFormat;
    
    @Before
    public void setup() {
        serializer = new DateTimeSerializerBase<Object>(Object.class, false, null) {

            @Override
            public DateTimeSerializerBase<Object> withFormat(Boolean timestamp, DateFormat customFormat) {
                return null;
            }

            @Override
            protected long _timestamp(Object value) {
                return 0;
            }

            @Override
            public void serialize(Object value, JsonGenerator gen, SerializerProvider serializers)
                    throws IOException {
            }
            
        };
        
        value = new Object();
        gen = mock(JsonGenerator.class);
        provider = mock(SerializerProvider.class);
        
        dateFormat = mock(DateFormat.class);
        simpleDateFormat = mock(SimpleDateFormat.class);
    }
    
    @Test
    public void testIsEmpty() {
        assertTrue(serializer.isEmpty(null));
        
        assertFalse(serializer.isEmpty(provider, value));
    }
    
    @Test
    public void testGetSchema() {
        assertFalse(serializer.getSchema(provider, null).isNull());
    }
    
    @Test
    public void testAcceptJsonFormatVisitor_Number() throws IOException {
        JsonFormatVisitorWrapper visitor = mock(JsonFormatVisitorWrapper.class);
        when(visitor.getProvider()).thenReturn(provider);
        
        serializer.acceptJsonFormatVisitor(visitor, null);
        
        verify(visitor).visitIntFormat(eq(provider), isNull(), eq(JsonParser.NumberType.LONG), eq(JsonValueFormat.UTC_MILLISEC));
    }
    
    @Test
    public void testAcceptJsonFormatVisitor_String() throws IOException {
    	JsonFormatVisitorWrapper visitor = mock(JsonFormatVisitorWrapper.class);
        when(visitor.getProvider()).thenReturn(provider);
        
        serializer = new DateTimeSerializerBase<Object>(Object.class, false, dateFormat) {

            @Override
            public DateTimeSerializerBase<Object> withFormat(Boolean timestamp, DateFormat customFormat) {
                return null;
            }

            @Override
            protected long _timestamp(Object value) {
                return 0;
            }

            @Override
            public void serialize(Object value, JsonGenerator gen, SerializerProvider serializers)
                    throws IOException {
            }
            
        };
        
        serializer.acceptJsonFormatVisitor(visitor, null);
        
        verify(visitor).visitStringFormat(eq(provider), isNull(), eq(JsonValueFormat.DATE_TIME));
    }
    
    @Test
    public void testSerialize() throws IOException {
        serializer.serialize(value, gen, provider);
        
        verify(serializer).serialize(value, gen, provider);
    }

}