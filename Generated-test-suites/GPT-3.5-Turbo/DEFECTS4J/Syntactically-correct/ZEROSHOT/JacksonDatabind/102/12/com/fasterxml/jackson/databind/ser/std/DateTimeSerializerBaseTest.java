package com.fasterxml.jackson.databind.ser.std;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitorWrapper;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import com.fasterxml.jackson.databind.util.StdDateFormat;

import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;
import java.util.concurrent.atomic.AtomicReference;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class DateTimeSerializerBaseTest {
    
    private DateTimeSerializerBase serializer;
    private Class<Foo> type;
    private Boolean useTimestamp;
    private DateFormat customFormat;
    private SerializerProvider provider;
    private BeanProperty property;

    @Before
    public void setUp() {
        type = Foo.class;
        useTimestamp = true;
        customFormat = mock(DateFormat.class);
        provider = mock(SerializerProvider.class);
        property = mock(BeanProperty.class);
        
        serializer = new DateTimeSerializerBase<Foo>(type, useTimestamp, customFormat) {
            @Override
            public DateTimeSerializerBase<Foo> withFormat(Boolean timestamp, DateFormat customFormat) {
                return null;
            }

            @Override
            protected long _timestamp(Foo value) {
                return 0;
            }

            @Override
            public void serialize(Foo value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
                
            }
        };
    }
    
    @Test
    public void testCreateContextual_timestampShape_ReturnsWithFormatTrueAndNullCustomFormat() throws JsonMappingException {
        when(property.getMember()).thenReturn(null);
        JsonFormat.Value format = new JsonFormat.Value();
        format = format.withShape(JsonFormat.Shape.NUMBER);
        when(serializer.findFormatOverrides(provider, property, type)).thenReturn(format);
        
        ContextualSerializer returnedSerializer = serializer.createContextual(provider, property);
        
        assertEquals(Boolean.TRUE, returnedSerializer.withFormat);
        assertEquals(null, returnedSerializer.customFormat);
    }
    
    @Test
    public void testCreateContextual_customPattern_ReturnsWithFormatFalseAndNewCustomFormat() throws JsonMappingException {
        DateFormat expectedDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        JsonFormat.Value format = new JsonFormat.Value();
        format = format.withPattern("yyyy-MM-dd");
        format = format.withLocale(Locale.US);
        when(serializer.findFormatOverrides(provider, property, type)).thenReturn(format);
        
        ContextualSerializer returnedSerializer = serializer.createContextual(provider, property);
        
        assertEquals(Boolean.FALSE, returnedSerializer.withFormat);
        assertEquals(expectedDateFormat, returnedSerializer.customFormat);
    }
    
    @Test
    public void testCreateContextual_changeTimeZone_ReturnsWithFormatFalseAndNewCustomFormatWithTimeZone() throws JsonMappingException {
        TimeZone newTimeZone = TimeZone.getTimeZone("GMT+3");
        DateFormat expectedDateFormat = Mockito.spy(DateFormat.class);
        Mockito.doReturn(expectedDateFormat.getTimeZone()).when(newTimeZone);
        JsonFormat.Value format = new JsonFormat.Value();
        format = format.withTimeZone(newTimeZone);
        when(serializer.findFormatOverrides(provider, property, type)).thenReturn(format);
        
        ContextualSerializer returnedSerializer = serializer.createContextual(provider, property);
        
        assertEquals(Boolean.FALSE, returnedSerializer.withFormat);
        assertEquals(expectedDateFormat, returnedSerializer.customFormat);
    }
    
    @Test
    public void testCreateContextual_changeLocale_ReturnsWithFormatFalseAndNewCustomFormatWithLocale() throws JsonMappingException {
        Locale newLocale = Locale.FRANCE;
        DateFormat expectedDateFormat = Mockito.spy(DateFormat.class);
        Mockito.doReturn(expectedDateFormat.getLocale()).when(newLocale);
        JsonFormat.Value format = new JsonFormat.Value();
        format = format.withLocale(newLocale);
        when(serializer.findFormatOverrides(provider, property, type)).thenReturn(format);
        
        ContextualSerializer returnedSerializer = serializer.createContextual(provider, property);
        
        assertEquals(Boolean.FALSE, returnedSerializer.withFormat);
        assertEquals(expectedDateFormat, returnedSerializer.customFormat);
    }
    
    @Test
    public void testCreateContextual_jsonShape_ReturnsWithFormatFalseAndNullCustomFormat() throws JsonMappingException {
        JsonFormat.Value format = new JsonFormat.Value();
        format = format.withShape(JsonFormat.Shape.STRING);
        when(serializer.findFormatOverrides(provider, property, type)).thenReturn(format);
        
        ContextualSerializer returnedSerializer = serializer.createContextual(provider, property);
        
        assertEquals(Boolean.FALSE, returnedSerializer.withFormat);
        assertEquals(null, returnedSerializer.customFormat);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testCreateContextual_nullProperty_ThrowsIllegalArgumentException() throws JsonMappingException {
        when(serializer.findFormatOverrides(provider, null, type)).thenReturn(null);
        
        serializer.createContextual(provider, null);
    }
    
    public class Foo {
        
    }
}