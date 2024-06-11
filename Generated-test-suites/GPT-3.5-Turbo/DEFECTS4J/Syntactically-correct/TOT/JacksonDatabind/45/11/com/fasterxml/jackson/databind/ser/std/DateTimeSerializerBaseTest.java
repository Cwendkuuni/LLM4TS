package com.fasterxml.jackson.databind.ser.std;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.introspect.Annotated;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitorWrapper;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import com.fasterxml.jackson.databind.util.StdDateFormat;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.lang.reflect.Type;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class DateTimeSerializerBaseTest {
    
    private final ObjectMapper objectMapper = new ObjectMapper();
    
    @Test
    public void testCreateContextualWithNullProperty() throws Exception {
        DateTimeSerializerBase<?> serializer = new MockDateTimeSerializerBase(null, Boolean.TRUE, null);
        SerializerProvider provider = objectMapper.getSerializerProvider();
        ContextualSerializer contextualSerializer = serializer.createContextual(provider, null);
        Assert.assertEquals(serializer, contextualSerializer);
    }
    
    @Test
    public void testCreateContextualWithNullFormatAnnotation() throws Exception {
        Annotated annotated = new MockAnnotated(null);
        DateTimeSerializerBase<?> serializer = new MockDateTimeSerializerBase(null, Boolean.TRUE, null);
        SerializerProvider provider = objectMapper.getSerializerProvider();
        ContextualSerializer contextualSerializer = serializer.createContextual(provider, new BeanProperty() {
            @Override
            public Member getMember() {
                return null;
            }
    
            @Override
            public String getName() {
                return null;
            }
    
            @Override
            public PropertyMetadata getMetadata() {
                return null;
            }
    
            @Override
            public Type getType() {
                return null;
            }
    
            @Override
            public AnnotatedMember getMember() {
                return null;
            }
    
            @Override
            public Annotated getMember() {
                return annotated;
            }
    
            @Override
            public PropertyName getWrapperName() {
                return null;
            }
    
            @Override
            public boolean isRequired() {
                return false;
            }
        });
        Assert.assertEquals(serializer, contextualSerializer);
    }
    
    @Test
    public void testCreateContextualWithNumericShape() throws Exception {
        DateTimeSerializerBase<?> serializer = new MockDateTimeSerializerBase(null, Boolean.TRUE, null);
        Annotated annotated = new MockAnnotated(new MockJsonFormat.Shape(JsonFormat.Shape.NUMBER));
        SerializerProvider provider = objectMapper.getSerializerProvider();
        ContextualSerializer contextualSerializer = serializer.createContextual(provider, new BeanProperty() {
            @Override
            public Member getMember() {
                return null;
            }
    
            @Override
            public String getName() {
                return null;
            }
    
            @Override
            public PropertyMetadata getMetadata() {
                return null;
            }
    
            @Override
            public Type getType() {
                return null;
            }
    
            @Override
            public AnnotatedMember getMember() {
                return null;
            }
    
            @Override
            public Annotated getMember() {
                return annotated;
            }
    
            @Override
            public PropertyName getWrapperName() {
                return null;
            }
    
            @Override
            public boolean isRequired() {
                return false;
            }
        });
        Assert.assertEquals(serializer, contextualSerializer);
    }
    
    @Test
    public void testCreateContextualWithStringShape() throws Exception {
        TimeZone timeZone = TimeZone.getTimeZone("EST");
        String pattern = "yyyy-MM-dd";
        Locale locale = Locale.US;
        SimpleDateFormat dateFormat = new SimpleDateFormat(pattern, locale);
        dateFormat.setTimeZone(timeZone);
        DateTimeSerializerBase<?> serializer = new MockDateTimeSerializerBase(null, Boolean.FALSE, dateFormat);
        Annotated annotated = new MockAnnotated(new MockJsonFormat.Shape(JsonFormat.Shape.STRING), timeZone, pattern, locale);
        SerializerProvider provider = objectMapper.getSerializerProvider();
        ContextualSerializer contextualSerializer = serializer.createContextual(provider, new BeanProperty() {
            @Override
            public Member getMember() {
                return null;
            }
    
            @Override
            public String getName() {
                return null;
            }
    
            @Override
            public PropertyMetadata getMetadata() {
                return null;
            }
    
            @Override
            public Type getType() {
                return null;
            }
    
            @Override
            public AnnotatedMember getMember() {
                return null;
            }
    
            @Override
            public Annotated getMember() {
                return annotated;
            }
    
            @Override
            public PropertyName getWrapperName() {
                return null;
            }
    
            @Override
            public boolean isRequired() {
                return false;
            }
        });
        Assert.assertEquals(serializer, contextualSerializer);
    }
    
    @Test
    public void testIsEmptyWithNullValue() throws Exception {
        DateTimeSerializerBase<?> serializer = new MockDateTimeSerializerBase(null, null, null);
        Assert.assertTrue(serializer.isEmpty((Object)null));
    }
    
    @Test
    public void testIsEmptyWithNullValueAndTimeStampNotUsed() throws Exception {
        DateTimeSerializerBase<?> serializer = new MockDateTimeSerializerBase(null, Boolean.FALSE, null);
        Assert.assertTrue(serializer.isEmpty((Object)null));
    }
    
    @Test
    public void testIsEmptyWithEmptyValueAndTimeStampNotUsed() throws Exception {
        DateTimeSerializerBase<?> serializer = new MockDateTimeSerializerBase(null, Boolean.FALSE, null);
        Assert.assertTrue(serializer.isEmpty(objectMapper, ""));
    }
    
    @Test
    public void testGetSchemaReturnsNumberSchema() throws Exception {
        DateTimeSerializerBase<?> serializer = new MockDateTimeSerializerBase(null, Boolean.TRUE, null);
        Assert.assertTrue(serializer.getSchema(objectMapper.getSerializerProvider(), null).isNumber());
    }
    
    @Test
    public void testGetSchemaReturnsStringSchema() throws Exception {
        DateTimeSerializerBase<?> serializer = new MockDateTimeSerializerBase(null, Boolean.FALSE, null);
        Assert.assertTrue(serializer.getSchema(objectMapper.getSerializerProvider(), null).isTextual());
    }
    
    @Test
    public void testAcceptJsonFormatVisitorWithNumeric() throws Exception {
        DateTimeSerializerBase<?> serializer = new MockDateTimeSerializerBase(null, Boolean.TRUE, null);
        JsonFormatVisitorWrapper visitor = new MockJsonFormatVisitorWrapper();
        serializer.acceptJsonFormatVisitor(visitor, objectMapper.constructType(Date.class));
        Assert.assertTrue(visitor.isNumberFormat());
    }
    
    @Test
    public void testAcceptJsonFormatVisitorWithString() throws Exception {
        DateTimeSerializerBase<?> serializer = new MockDateTimeSerializerBase(null, Boolean.FALSE, null);
        JsonFormatVisitorWrapper visitor = new MockJsonFormatVisitorWrapper();
        serializer.acceptJsonFormatVisitor(visitor, objectMapper.constructType(Date.class));
        Assert.assertTrue(visitor.isStringFormat());
    }
    
    @Test
    public void testSerializeWithJsonGenerator() throws Exception {
        DateTimeSerializerBase<?> serializer = new MockDateTimeSerializerBase(null, null, null);
        JsonGenerator generator = new MockJsonGenerator();
        SerializerProvider provider = objectMapper.getSerializerProvider();
        serializer.serialize(new Object(), generator, provider);
        // todo: Add assertions here
    }
    
    /*
    /**********************************************************
    /* Helper classes
    /**********************************************************
     */
    
    private static class MockDateTimeSerializerBase extends DateTimeSerializerBase<Object> {
        
        protected MockDateTimeSerializerBase(Class<Object> type, Boolean useTimestamp, DateFormat customFormat) {
            super(type, useTimestamp, customFormat);
        }
    
        @SuppressWarnings("unchecked")
        @Override
        public DateTimeSerializerBase<Object> withFormat(Boolean timestamp, DateFormat customFormat) {
            return (DateTimeSerializerBase<Object>)super.withFormat(timestamp, customFormat);
        }
    
        @Override
        protected long _timestamp(Object value) {
            return 0;
        }
    
        @Override
        public void serialize(Object value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
            // Mock serialization implementation
        }
    }
    
    private static class MockAnnotated implements Annotated {
        
        private final MockJsonFormat.Shape shape;
    
        public MockAnnotated(MockJsonFormat.Shape shape) {
            this(shape, null, null, null);
        }
    
        public MockAnnotated(MockJsonFormat.Shape shape, TimeZone timeZone, String pattern, Locale locale) {
            this.shape = shape;
        }
    
        @Override
        public <A extends Annotation> A getAnnotation(Class<A> acls) {
            return null;
        }
    
        @Override
        public boolean hasAnnotation(Class<?> acls) {
            return false;
        }
    
        @Override
        public boolean hasOneOf(Class<? extends Annotation>[] annoClasses) {
            return false;
        }
    
        @Override
        public <A extends Annotation> A[] getAnnotationsByType(Class<A> acls) {
            return null;
        }
    
        @Override
        public Class<?> getDeclaringClass() {
            return null;
        }
    
        @Override
        public Member getMember() {
            return null;
        }
    
        @Override
        public void fixAccess() {
    
        }
    
        @Override
        public Type getGenericType() {
            return null;
        }
    }
    
    private static class MockJsonFormat {
        
        private final Shape shape;
        private final TimeZone timeZone;
        private final String pattern;
        private final Locale locale;
    
        public MockJsonFormat(Shape shape) {
            this(shape, null, null, null);
        }
    
        public MockJsonFormat(Shape shape, TimeZone timeZone, String pattern, Locale locale) {
            this.shape = shape;
            this.timeZone = timeZone;
            this.pattern = pattern;
            this.locale = locale;
        }
    
        public Shape getShape() {
            return shape;
        }
    
        public TimeZone getTimeZone() {
            return timeZone;
        }
    
        public String getPattern() {
            return pattern;
        }
    
        public Locale getLocale() {
            return locale;
        }
        
        public boolean hasPattern() {
            return pattern != null;
        }
        
        public boolean hasLocale() {
            return locale != null;
        }
        
        public enum Shape {
            STRING, NUMBER, OBJECT, ARRAY, BOOLEAN, NULL;
            
            public boolean isNumeric() {
                return this == NUMBER;
            }
        }
    }
    
    private static class MockJsonFormatVisitorWrapper implements JsonFormatVisitorWrapper {
        
        private boolean isNumberFormat = false;
        private boolean isStringFormat = false;
    
        @Override
        public JsonObjectFormatVisitor expectObjectFormat(JavaType type) throws JsonMappingException {
            return null;
        }
    
        @Override
        public JsonArrayFormatVisitor expectArrayFormat(JavaType type) throws JsonMappingException {
            return null;
        }
    
        @Override
        public JsonStringFormatVisitor expectStringFormat(JavaType type) throws JsonMappingException {
            return null;
        }
    
        @Override
        public JsonNumberFormatVisitor expectNumberFormat(JavaType type) throws JsonMappingException {
            this.isNumberFormat = true;
            return null;
        }
    
        @Override
        public JsonIntegerFormatVisitor expectIntegerFormat(JavaType type) throws JsonMappingException {
            return expectNumberFormat(type);
        }
    
        @Override
        public JsonBooleanFormatVisitor expectBooleanFormat(JavaType type) throws JsonMappingException {
            return null;
        }
    
        @Override
        public JsonNullFormatVisitor expectNullFormat(JavaType type) throws JsonMappingException {
            return null;
        }
    
        public boolean isNumberFormat() {
            return isNumberFormat;
        }
    
        public boolean isStringFormat() {
            return isStringFormat;
        }
    }
    
    private static class MockJsonGenerator extends JsonGenerator {
        
        @Override
        public void writeString(String text) throws IOException {
    
        }
    
        /* ... implement all other abstract methods of JsonGenerator ... */
    }
}