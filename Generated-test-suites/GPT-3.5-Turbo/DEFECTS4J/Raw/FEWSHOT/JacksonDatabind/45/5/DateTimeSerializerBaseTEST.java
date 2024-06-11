package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.TimeZone;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.JsonSerializer.*;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.introspect.Annotated;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitorWrapper;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;

public class DateTimeSerializerBaseTest {
    
    private DateTimeSerializerBase<TestObject> dateTimeSerializerBase;
    private TestObject testObject;
    private SerializerProvider provider;
    
    @Before
    public void setup() {
        dateTimeSerializerBase = new DateTimeSerializerBase<TestObject>(TestObject.class, true, null) {
            @Override
            public DateTimeSerializerBase<TestObject> withFormat(Boolean timestamp, DateFormat customFormat) {
                return null;
            }

            @Override
            protected long _timestamp(TestObject value) {
                return 0;
            }

            @Override
            public void serialize(TestObject value, JsonGenerator gen, SerializerProvider serializers) {
                
            }
        };
        
        testObject = new TestObject();
        provider = new SerializerProvider();
    }

    @Test
    public void testWithContextual() {
        BeanProperty property = null;
        try {
            dateTimeSerializerBase.createContextual(provider, property);
        } catch (Exception e) {
            assertNull(e);
        }
        
        property = new BeanProperty() {
            @Override
            public JavaType getType() {
                return null;
            }

            @Override
            public <A extends java.lang.annotation.Annotation> A getAnnotation(Class<A> annotationClass) {
                return null;
            }

            @Override
            public <A extends java.lang.annotation.Annotation> A getContextAnnotation(Class<A> annotationClass) {
                return null;
            }

            @Override
            public Annotated getMember() {
                return null;
            }
        };
        try {
            dateTimeSerializerBase.createContextual(provider, property);
        } catch (Exception e) {
            assertNull(e);
        }
        
        property = new BeanProperty() {
            @Override
            public JavaType getType() {
                return null;
            }

            @Override
            public <A extends java.lang.annotation.Annotation> A getAnnotation(Class<A> annotationClass) {
                return null;
            }

            @Override
            public <A extends java.lang.annotation.Annotation> A getContextAnnotation(Class<A> annotationClass) {
                return null;
            }

            @Override
            public Annotated getMember() {
                return new Annotated() {
                    @Override
                    public <A extends java.lang.annotation.Annotation> A getAnnotation(Class<A> annotationClass) {
                        return null;
                    }
                };
            }
        };  
        
        try {
            dateTimeSerializerBase.createContextual(provider, property);
        } catch (Exception e) {
            assertNull(e);
        }
        
        JsonFormat.Value format = new JsonFormat.Value() {

            @Override
            public JsonFormat.Shape getShape() {
                return null;
            }

            @Override
            public String getPattern() {
                return null;
            }

            @Override
            public Locale getLocale() {
                return null;
            }

            @Override
            public TimeZone getTimeZone() {
                return null;
            }

            @Override
            public boolean hasPattern() {
                return false;
            }

            @Override
            public boolean hasLocale() {
                return false;
            }
            
        };
        property = new BeanProperty() {
            @Override
            public JavaType getType() {
                return null;
            }

            @Override
            public <A extends java.lang.annotation.Annotation> A getAnnotation(Class<A> annotationClass) {
                return null;
            }

            @Override
            public <A extends java.lang.annotation.Annotation> A getContextAnnotation(Class<A> annotationClass) {
                return null;
            }

            @Override
            public Annotated getMember() {
                return new Annotated() {
                    @Override
                    public <A extends java.lang.annotation.Annotation> A getAnnotation(Class<A> annotationClass) {
                        return null;
                    }
                };
            }
        }; 
        
        try {
            dateTimeSerializerBase.createContextual(provider, property);
        } catch (Exception e) {
            assertNull(e);
        }
    }
    
    @Test
    public void testIsEmpty1() {
        assertFalse(dateTimeSerializerBase.isEmpty(testObject));
    }
    
    @Test
    public void testIsEmpty2() {
        assertTrue(dateTimeSerializerBase.isEmpty(null));
    }
    
    @Test
    public void testIsEmpty3() {
        assertFalse(dateTimeSerializerBase.isEmpty(provider, testObject));
    }
    
    @Test
    public void testAcceptJsonFormatVisitor() {
        JsonFormatVisitorWrapper visitor = new JsonFormatVisitorWrapper() {

            @Override
            public SerializerProvider getProvider() {
                return null;
            }

            @Override
            public void setProvider(SerializerProvider p) {
                
            }

            @Override
            public void expectStringFormat(JavaType type) throws JsonMappingException {
                
            }

            @Override
            public void expectAnyFormat(JavaType type) throws JsonMappingException {
                
            }

            @Override
            public void expectIntegerFormat(JavaType type) throws JsonMappingException {
                
            }

            @Override
            public void expectBooleanFormat(JavaType type) throws JsonMappingException {
                
            }

            @Override
            public void expectNumberFormat(JavaType type) throws JsonMappingException {
                
            }

            @Override
            public void expectNullFormat(JavaType valueType) throws JsonMappingException {
                
            }

            @Override
            public void expectAnyFormat(JavaType type, boolean multiple) throws JsonMappingException {
                
            }

            @Override
            public void expectArrayFormat(JavaType type) throws JsonMappingException {
                
            }

            @Override
            public JsonArrayFormatVisitor expectArrayFormat(Class<?> elementType) throws JsonMappingException {
                return null;
            }

            @Override
            public JsonArrayFormatVisitor expectArrayFormat(JavaType type) throws JsonMappingException {
                return null;
            }

            @Override
            public JsonMapFormatVisitor expectMapFormat(Class<?> keyType, Class<?> valueType) throws JsonMappingException {
                return null;
            }

            @Override
            public JsonMapFormatVisitor expectMapFormat(JavaType type) throws JsonMappingException {
                return null;
            }

            @Override
            public void expectEnumFormat(JavaType type) throws JsonMappingException {
                
            }

            @Override
            public void expectEnumFormat(Class<?> enumType) throws JsonMappingException {
                
            }
        };
        
        try {
            dateTimeSerializerBase.acceptJsonFormatVisitor(visitor, null);
        } catch (Exception e) {
            assertNull(e);
        }
    }
    
    @Test
    public void testGetSchema() {
        try {
            dateTimeSerializerBase.getSchema(provider, null);
        } catch (Exception e) {
            assertNull(e);
        }
    }

}

class TestObject {
    
}