package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.*;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.exc.InvalidDefinitionException;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitorWrapper;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import com.fasterxml.jackson.databind.util.StdDateFormat;

import org.junit.Before;
import org.junit.Test;

public class DateTimeSerializerBaseTest {

    private DateTimeSerializerBaseTestClass serializer;

    @Before
    public void setUp() {
        serializer = new DateTimeSerializerBaseTestClass();
    }

    @Test
    public void testWithFormat() {
        DateTimeSerializerBaseTestClass newSerializer = serializer.withFormat(true, null);
        assertNotNull(newSerializer);
        assertEquals(true, newSerializer._useTimestamp);
        assertNull(newSerializer._customFormat);
    }

    @Test
    public void testCreateContextualNoFormat() throws JsonProcessingException {
        JsonSerializer<?> newSerializer = serializer.createContextual(null, null);
        assertNotNull(newSerializer);
        assertEquals(serializer, newSerializer);
    }

    @Test
    public void testCreateContextualNumericFormat() throws JsonProcessingException {
        BeanProperty property = new TestBeanProperty();
        JsonFormat.Value format = JsonFormat.Value.forShape(JsonFormat.Shape.NUMBER);
        JsonSerializer<?> newSerializer = serializer.createContextual(null, property, format);
        assertNotNull(newSerializer);
        assertTrue(newSerializer instanceof DateTimeSerializerBaseTestClass);
        assertEquals(true, ((DateTimeSerializerBaseTestClass) newSerializer)._useTimestamp);
        assertNull(((DateTimeSerializerBaseTestClass) newSerializer)._customFormat);
    }

    @Test
    public void testCreateContextualStringFormat() throws JsonProcessingException {
        BeanProperty property = new TestBeanProperty();
        JsonFormat.Value format = new JsonFormat.Value();
        format = format.withShape(JsonFormat.Shape.STRING)
                .withPattern("yyyy-MM-dd")
                .withLocale(Locale.US)
                .withTimeZone(TimeZone.getDefault());
        JsonSerializer<?> newSerializer = serializer.createContextual(null, property, format);
        assertNotNull(newSerializer);
        assertTrue(newSerializer instanceof DateTimeSerializerBaseTestClass);
        assertEquals(false, ((DateTimeSerializerBaseTestClass) newSerializer)._useTimestamp);
        SimpleDateFormat expectedFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        expectedFormat.setTimeZone(TimeZone.getDefault());
        assertEquals(expectedFormat, ((DateTimeSerializerBaseTestClass) newSerializer)._customFormat);
    }

    @Test(expected = InvalidDefinitionException.class)
    public void testCreateContextualInvalidFormat() throws JsonProcessingException {
        BeanProperty property = new TestBeanProperty();
        JsonFormat.Value format = new JsonFormat.Value();
        format = format.withShape(JsonFormat.Shape.ARRAY);
        serializer.createContextual(null, property, format);
    }

    @Test
    public void testIsEmptyNull() {
        assertTrue(serializer.isEmpty(null));
    }

    @Test
    public void testIsEmptyTimestampZero() {
        DateTimeSerializerBaseTestClassTestClass value = new DateTimeSerializerBaseTestClassTestClass(0L);
        assertTrue(serializer.isEmpty(value));
    }

    @Test
    public void testIsEmptyTimestampNonZero() {
        DateTimeSerializerBaseTestClassTestClass value = new DateTimeSerializerBaseTestClassTestClass(1000L);
        assertFalse(serializer.isEmpty(value));
    }

    @Test
    public void testGetSchemaTimestamp() {
        SerializerProvider serializers = null;
        assertEquals("number", serializer.getSchema(serializers, null).get("type").asText());
    }

    @Test
    public void testGetSchemaString() {
        serializer.withFormat(false, new SimpleDateFormat("yyyy-MM-dd", Locale.US));
        SerializerProvider serializers = null;
        assertEquals("string", serializer.getSchema(serializers, null).get("type").asText());
    }

    @Test
    public void testAcceptJsonFormatVisitorNumeric() throws JsonProcessingException {
        TestJsonFormatVisitor visitor = new TestJsonFormatVisitor();
        serializer.acceptJsonFormatVisitor(visitor, null);

        assertEquals(JsonParser.NumberType.LONG, visitor.getNumberType());
        assertEquals(JsonValueFormat.UTC_MILLISEC, visitor.getFormat());
    }

    @Test
    public void testAcceptJsonFormatVisitorString() throws JsonProcessingException {
        serializer.withFormat(false, new SimpleDateFormat("yyyy-MM-dd", Locale.US));
        TestJsonFormatVisitor visitor = new TestJsonFormatVisitor();
        serializer.acceptJsonFormatVisitor(visitor, null);

        assertNull(visitor.getNumberType());
        assertEquals(JsonValueFormat.DATE_TIME, visitor.getFormat());
    }

    @Test
    public void testSerialize() throws JsonProcessingException {
        Date value = new Date();
        TestJsonGenerator generator = new TestJsonGenerator();
        SerializerProvider serializers = null;
        serializer.serialize(value, generator, serializers);
        assertNotNull(generator.getSerializedValue());
        assertEquals(value.toString(), generator.getSerializedValue());
    }

    class DateTimeSerializerBaseTestClass extends DateTimeSerializerBase<Date>
            implements ContextualSerializer {

        protected DateTimeSerializerBaseTestClass() {
            super(Date.class, false, null);
        }

        protected DateTimeSerializerBaseTestClass(Boolean useTimestamp, SimpleDateFormat customFormat) {
            super(Date.class, useTimestamp, customFormat);
        }

        public DateTimeSerializerBaseTestClass withFormat(Boolean timestamp, SimpleDateFormat customFormat) {
            return new DateTimeSerializerBaseTestClass(timestamp, customFormat);
        }

        @Override
        public JsonSerializer<?> createContextual(SerializerProvider serializers,
                BeanProperty property) {
            return super.createContextual(serializers, property);
        }

        @Override
        public boolean isEmpty(Date value) {
            return super.isEmpty(value);
        }

        @Override
        public boolean isEmpty(SerializerProvider serializers, Date value) {
            return super.isEmpty(serializers, value);
        }

        @Override
        protected long _timestamp(Date value) {
            return value.getTime();
        }

        @Override
        public JsonNode getSchema(SerializerProvider serializers, java.lang.reflect.Type typeHint) {
            return super.getSchema(serializers, typeHint);
        }

        @Override
        public void acceptJsonFormatVisitor(JsonFormatVisitorWrapper visitor,
                JavaType typeHint) {
            super.acceptJsonFormatVisitor(visitor, typeHint);
        }

        @Override
        public void serialize(Date value, JsonGenerator gen, SerializerProvider serializers) {
            try {
                gen.writeString(value.toString());
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

    class DateTimeSerializerBaseTestClassTestClass extends Date {
        public DateTimeSerializerBaseTestClassTestClass(long timestamp) {
            super(timestamp);
        }
    }

    class TestBeanProperty implements BeanProperty {
        public java.lang.annotation.Annotation getAnnotation(java.lang.Class<?> cls) {
            return null;
        }

        public <A extends java.lang.annotation.Annotation> A getAnnotation(
                Class<A> annotationClass) {
            return null;
        }

        public java.util.Iterator<java.lang.annotation.Annotation> getAnnotations() {
            return null;
        }

        public java.util.Iterator<java.lang.annotation.Annotation> getDeclaredAnnotations() {
            return null;
        }

        public PropertyName getFullName() {
            return null;
        }

        public ObjectIdInfo getObjectIdInfo() {
            return null;
        }

        public boolean hasConstructorParameter() {
            return false;
        }

        public boolean hasField() {
            return false;
        }

        public boolean hasGetter() {
            return false;
        }

        public boolean hasSetter() {
            return false;
        }

        public boolean isExplicitlyNamed() {
            return false;
        }

        public boolean isRequired() {
            return false;
        }

        public Object set(Object arg0, Object arg1) throws UnsupportedOperationException {
            return null;
        }

        public <A extends java.lang.annotation.Annotation> A getConstructorParameterAnnotation(
                Class<A> annotationClass) {
            return null;
        }

        public <A extends java.lang.annotation.Annotation> A getDeclaringClassAnnotation(
                Class<A> annotationClass) {
            return null;
        }

        public AnnotatedMember getMember() {
            return null;
        }

        public String getName() {
            return null;
        }

        public com.fasterxml.jackson.databind.PropertyMetadata getMetadata() {
            return null;
        }

        public JavaType getType() {
            return null;
        }
    }

    class TestJsonFormatVisitor implements JsonFormatVisitorWrapper {
        private JsonParser.NumberType numberType;
        private JsonValueFormat format;

        @Override
        public SerializerProvider getProvider() {
            return null;
        }

        @Override
        public String getPropertyName() {
            return null;
        }

        @Override
        public void setProvider(SerializerProvider provider) {

        }

        @Override
        public Object expectArrayFormat(JavaType type) {
            return null;
        }

        @Override
        public Object expectCommentFormat(JavaType type) {
            return null;
        }

        @Override
        public Object expectIntegerFormat(JavaType type) {
            numberType = JsonParser.NumberType.INT;
            return null;
        }

        @Override
        public Object expectNumberFormat(JavaType type) {
            numberType = JsonParser.NumberType.DOUBLE;
            return null;
        }

        @Override
        public Object expectStringFormat(JavaType type) {
            format = JsonValueFormat.DATE_TIME;
            return null;
        }

        @Override
        public Object expectNullFormat(JavaType type) {
            return null;
        }

        @Override
        public void handleError(String msg) {

        }

        public JsonParser.NumberType getNumberType() {
            return numberType;
        }

        public JsonValueFormat getFormat() {
            return format;
        }
    }

    class TestJsonGenerator extends JsonGenerator {
        private String serializedValue;

        @Override
        public void writeString(String text) {
            serializedValue = text;
        }

        public String getSerializedValue() {
            return serializedValue;
        }

        // Other overridden methods of JsonGenerator

    }
}