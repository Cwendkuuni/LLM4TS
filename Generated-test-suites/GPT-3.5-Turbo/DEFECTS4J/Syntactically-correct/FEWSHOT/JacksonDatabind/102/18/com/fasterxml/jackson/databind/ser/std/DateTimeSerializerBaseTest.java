package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;
import java.util.concurrent.atomic.AtomicReference;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.JsonSerializer.None;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitorWrapper;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import com.fasterxml.jackson.databind.util.StdDateFormat;

public class DateTimeSerializerBaseTest {

    private DateTimeSerializerBase serializer;
    private JsonSerializer.ContextualSerializer contextualSerializer;
    private SerializerProvider provider;
    
    @Before
    public void setup() {
    	DateFormat customFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");
        serializer = new DateTimeSerializerBaseTestImpl(TestClass.class, true, customFormat);
        provider = mock(SerializerProvider.class);
        contextualSerializer = serializer.createContextual(provider, mock(BeanProperty.class));
    }

    @Test
    public void testCreateContextualWithNullProperty() throws JsonMappingException {
    	assertEquals(serializer, serializer.createContextual(provider, null));
    }

    @Test
    public void testCreateContextualWithNumericShape() throws JsonMappingException {
    	DateTimeSerializerBase contextualSerializer = (DateTimeSerializerBase)serializer.createContextual(provider, mock(BeanProperty.class));
    	assertTrue(contextualSerializer._asTimestamp(true));
    }
    
    @Test
    public void testCreateContextualWithDateFormat() throws JsonMappingException {
    	JsonFormat.Value format = new JsonFormat.Value();
    	format.setPattern("yyyy-MM-dd");
    	contextualSerializer = serializer.createContextual(provider, mock(BeanProperty.class));
    	assertEquals("yyyy-MM-dd", contextualSerializer._customFormat.toPattern());
    }
    
    @Test
    public void testCreateContextualWithStringShape() throws JsonMappingException {
    	JsonFormat.Value format = new JsonFormat.Value();
    	format.setShape(JsonFormat.Shape.STRING);
    	contextualSerializer = serializer.createContextual(provider, mock(BeanProperty.class));
    	assertFalse(contextualSerializer._asTimestamp(true));
    }
    
    
    @Test(expected = IllegalArgumentException.class)
    public void testCreateContextualWithNullSerilizerProvider() throws JsonMappingException {
    	serializer.createContextual(null, mock(BeanProperty.class));
    }
    
    @Test
    public void testIsEmpty() {
    	assertFalse(serializer.isEmpty(provider, new Date()));
    }
    
    @Test
    public void testGetSchema() {
    	JsonNode jsonNode = serializer.getSchema(provider, Date.class);
    	assertNotNull(jsonNode);
    }
    
    @Test
    public void testAcceptJsonFormatVisitor() throws JsonMappingException {
    	JsonFormatVisitorWrapper visitor = mock(JsonFormatVisitorWrapper.class);
    	JavaType typeHint = mock(JavaType.class);
    	serializer.acceptJsonFormatVisitor(visitor, typeHint);
    }
    
    @Test
    public void testSerializeAsString() throws Exception {
    	JsonGenerator g = mock(JsonGenerator.class);
    	serializer.serializeAsString(new Date(), g, provider);
    }

    static class DateTimeSerializerBaseTestImpl extends DateTimeSerializerBase<Test> {
        private static final long serialVersionUID = 1L;

        protected DateTimeSerializerBaseTestImpl(Class<Test> type, Boolean useTimestamp, DateFormat customFormat) {
            super(type, useTimestamp, customFormat);
        }

        @Override
        public DateTimeSerializerBase<Test> withFormat(Boolean timestamp, DateFormat customFormat) {
            return null;
        }

        @Override
        protected long _timestamp(Test value) {
            return 0;
        }

		@Override
		public void serialize(Test value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
            // your implementation
		}
		
		private static class Test {
		    // your implementation
		}
    }
}