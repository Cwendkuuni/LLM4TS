package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.TimeZone;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.introspect.Annotated;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitorWrapper;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonValueFormat;
import com.fasterxml.jackson.databind.util.StdDateFormat;

@RunWith(MockitoJUnitRunner.class)
public class DateTimeSerializerBaseTest {

    @Mock
    private SerializerProvider serializerProviderMock;
    @Mock
    private JsonGenerator jsonGeneratorMock;
    @Mock
    private BeanProperty beanPropertyMock;
    @Mock
    private JsonFormatVisitorWrapper jsonFormatVisitorWrapperMock;
    
    private DateTimeSerializerBase<?> dateTimeSerializerBase;
    private DateFormat customDateFormat;
    
    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        customDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        dateTimeSerializerBase = new DateTimeSerializerBase<Object>(Object.class, true, customDateFormat) {
            
            private static final long serialVersionUID = 1L;

            @Override
            public DateTimeSerializerBase<Object> withFormat(Boolean timestamp, DateFormat customFormat) {
                return null;
            }

            @Override
            protected long _timestamp(Object value) {
                return 0;
            }

            @Override
            public void serialize(Object value, JsonGenerator gen, SerializerProvider serializers) {
                // Empty implementation for test purposes
            }
        };
    }
    
    @Test
    public void testCreateContextualWithFormatNull() throws JsonMappingException {
        JsonSerializer<?> serializer = dateTimeSerializerBase.createContextual(serializerProviderMock, null);
        assertTrue(serializer instanceof DateTimeSerializerBase<?>);
        DateTimeSerializerBase<?> base = (DateTimeSerializerBase<?>)serializer;
        assertNull(base._customFormat);
        assertTrue(base._useTimestamp);
    }
    
    @Test
    public void testCreateContextualWithFormatShapeNumeric() throws JsonMappingException {
        JsonFormat.Value jsonFormat = new JsonFormat.Value();
        jsonFormat = jsonFormat.withShape(JsonFormat.Shape.NUMBER);
        BeanProperty property = new BeanProperty() {
            
            @Override
            public <A extends java.lang.annotation.Annotation> A getAnnotation(Class<A> arg0) {
                return null;
            }
            
            @Override
            public Annotated getMember() {
                return null;
            }
            
            @Override
            public String getName() {
                return null;
            }
            
            @Override
            public <A extends java.lang.annotation.Annotation> A getContextAnnotation(Class<A> arg0) {
                return null;
            }
        };
        JsonSerializer<?> serializer = dateTimeSerializerBase.createContextual(serializerProviderMock, property);
        assertTrue(serializer instanceof DateTimeSerializerBase<?>);
        DateTimeSerializerBase<?> base = (DateTimeSerializerBase<?>)serializer;
        assertNull(base._customFormat);
        assertTrue(base._useTimestamp);
    }
    
    @Test
    public void testCreateContextualWithFormatShapeString() throws JsonMappingException {
        JsonFormat.Value jsonFormat = new JsonFormat.Value();
        jsonFormat = jsonFormat.withShape(JsonFormat.Shape.STRING);
        TimeZone timeZone = TimeZone.getTimeZone("GMT");
        jsonFormat = jsonFormat.withTimeZone(timeZone);
        jsonFormat = jsonFormat.withPattern("yyyy-MM-dd");
        jsonFormat = jsonFormat.withLocale(Locale.ENGLISH);
        DateTimeSerializerBase<?> serializerBase = new DateTimeSerializerBase<Object>(Object.class, true, customDateFormat) {
            
            private static final long serialVersionUID = 1L;
            
            @Override
            public DateTimeSerializerBase<Object> withFormat(Boolean timestamp,
                    DateFormat customFormat) {
                return null;
            }
            
            @Override
            protected long _timestamp(Object value) {
                return 0;
            }
            
            @Override
            public void serialize(Object value, JsonGenerator gen,
                    SerializerProvider serializers) {
                // Empty implementation for test purposes
            }
        };
        BeanProperty property = new BeanProperty() {
            
            @Override
            public <A extends java.lang.annotation.Annotation> A getAnnotation(Class<A> arg0) {
                return null;
            }
            
            @Override
            public Annotated getMember() {
                return null;
            }
            
            @Override
            public String getName() {
                return null;
            }
            
            @Override
            public <A extends java.lang.annotation.Annotation> A getContextAnnotation(Class<A> arg0) {
                return null;
            }
        };
        JsonSerializer<?> serializer = serializerBase.createContextual(serializerProviderMock, property);
        assertTrue(serializer instanceof DateTimeSerializerBase<?>);
        DateTimeSerializerBase<?> base = (DateTimeSerializerBase<?>)serializer;
        assertEquals(timeZone, ((SimpleDateFormat)base._customFormat).getTimeZone());
        assertEquals("yyyy-MM-dd", ((SimpleDateFormat)base._customFormat).toPattern());
        assertNull(base._useTimestamp);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testCreateContextualWithFormatNullProvider() throws JsonMappingException {
        dateTimeSerializerBase.createContextual(null, null);
    }
    
    
    @Test
    public void testIsEmptyWithNullValue() {
        assertTrue(dateTimeSerializerBase.isEmpty(null));
    }
    
    @Test
    public void testIsEmptyWithValueZeroTimestamp() {
        MockObject value = new MockObject();
        assertTrue(dateTimeSerializerBase.isEmpty(serializerProviderMock, value));
    }
    
    @Test
    public void testGetSchemaWithTimestamp() {
        JsonNode schema = dateTimeSerializerBase.getSchema(serializerProviderMock, Object.class);
        assertEquals("\"number\"", schema.toString());
    }
    
    @Test
    public void testGetSchemaWithString() {
        dateTimeSerializerBase = new DateTimeSerializerBase<Object>(Object.class, false, customDateFormat) {
            
            private static final long serialVersionUID = 1L;
            
            @Override
            public DateTimeSerializerBase<Object> withFormat(Boolean timestamp,
                    DateFormat customFormat) {
                return null;
            }
            
            @Override
            protected long _timestamp(Object value) {
                return 0;
            }
            
            @Override
            public void serialize(Object value, JsonGenerator gen,
                    SerializerProvider serializers) {
                // Empty implementation for test purposes
            }
        };
        JsonNode schema = dateTimeSerializerBase.getSchema(serializerProviderMock, Object.class);
        assertEquals("\"string\"", schema.toString());
    }
    
    @Test
    public void testAcceptJsonFormatVisitorWithNumeric() throws JsonMappingException {
        dateTimeSerializerBase.acceptJsonFormatVisitor(jsonFormatVisitorWrapperMock, null);
        //verify(jsonFormatVisitorWrapperMock).getProvider();
        //verify(jsonFormatVisitorWrapperMock).expectIntFormat((JavaType) argThat(anything()), eq(JsonParser.NumberType.LONG));
    }
    
    @Test
    public void testAcceptJsonFormatVisitorWithString() throws JsonMappingException {
        dateTimeSerializerBase = new DateTimeSerializerBase<Object>(Object.class, false, customDateFormat) {
            
            private static final long serialVersionUID = 1L;
            
            @Override
            public DateTimeSerializerBase<Object> withFormat(Boolean timestamp,
                    DateFormat customFormat) {
                return null;
            }
            
            @Override
            protected long _timestamp(Object value) {
                return 0;
            }
            
            @Override
            public void serialize(Object value, JsonGenerator gen,
                    SerializerProvider serializers) {
                // Empty implementation for test purposes
            }
        };
        dateTimeSerializerBase.acceptJsonFormatVisitor(jsonFormatVisitorWrapperMock, null);
        //verify(jsonFormatVisitorWrapperMock).expectStringFormat((JavaType) argThat(anything()), eq(JsonValueFormat.DATE_TIME));
    }
}