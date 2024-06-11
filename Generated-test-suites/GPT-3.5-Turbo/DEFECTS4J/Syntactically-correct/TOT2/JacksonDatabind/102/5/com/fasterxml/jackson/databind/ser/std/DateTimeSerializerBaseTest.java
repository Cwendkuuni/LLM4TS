package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.exc.JsonMappingException;

@RunWith(MockitoJUnitRunner.class)
public class DateTimeSerializerBaseTest {

    @Mock
    private SerializerProvider provider;

    @Mock
    private JsonGenerator jsonGenerator;

    @Test
    public void testSerialize_CustomFormat() throws Exception {
        DateFormat customFormat = new SimpleDateFormat("yyyy-MM-dd");
        DateTimeSerializerBase<Date> serializer = new CustomDateTimeSerializer(Date.class, false, customFormat);
        Date date = new Date();
        
        serializer.serialize(date, jsonGenerator, provider);
        
        verify(jsonGenerator, times(1)).writeString("2021-01-01");
    }

    @Test
    public void testCreateContextual_NullProperty() throws JsonMappingException {
        DateTimeSerializerBase<Date> serializer = new CustomDateTimeSerializer(Date.class, true, null);
        
        JsonSerializer<?> result = serializer.createContextual(provider, null);
        
        assertSame(serializer, result);
    }

    @Test
    public void testGetSchema_Timestamp() {
        DateTimeSerializerBase<Date> serializer = new CustomDateTimeSerializer(Date.class, true, null);
        
        JsonNode schema = serializer.getSchema(provider, null);
        
        assertNotNull(schema);
        assertEquals("number", schema.asText());
    }

    @Test
    public void testGetSchema_String() {
        DateTimeSerializerBase<Date> serializer = new CustomDateTimeSerializer(Date.class, false, null);
        
        JsonNode schema = serializer.getSchema(provider, null);
        
        assertNotNull(schema);
        assertEquals("string", schema.asText());
    }

    @Test
    public void testAcceptJsonFormatVisitor() throws JsonMappingException {
        DateTimeSerializerBase<Date> serializer = new CustomDateTimeSerializer(Date.class, true, null);
        JsonFormatVisitorWrapper visitor = mock(JsonFormatVisitorWrapper.class);
        
        serializer.acceptJsonFormatVisitor(visitor, DateType);

        verify(visitor, times(1)).visitIntFormat(any(), any(), any(), any());
    }

    private class CustomDateTimeSerializer extends DateTimeSerializerBase<Date> {
    
        protected CustomDateTimeSerializer(Class<Date> type, Boolean useTimestamp, DateFormat customFormat) {
            super(type, useTimestamp, customFormat);
        }
    
        @Override
        protected long _timestamp(Date value) {
            return value.getTime();
        }
    
        @Override
        public void serialize(Date value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
            if (_useTimestamp != null && _useTimestamp) {
                gen.writeNumber(value.getTime());
            } else {
                _serializeAsString(value, gen, serializers);
            }
        }

        @Override
        public DateTimeSerializerBase<Date> withFormat(Boolean timestamp, DateFormat customFormat) {
            return new CustomDateTimeSerializer(Date.class, timestamp, customFormat);
        }
    }
}