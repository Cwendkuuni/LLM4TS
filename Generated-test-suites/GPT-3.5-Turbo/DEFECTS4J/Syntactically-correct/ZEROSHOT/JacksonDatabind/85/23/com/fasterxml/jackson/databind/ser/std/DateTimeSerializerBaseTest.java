package com.fasterxml.jackson.databind.ser.std;

import java.io.IOException;
import java.lang.reflect.Type;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.TimeZone;

import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.jsonFormatVisitors.*;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import com.fasterxml.jackson.databind.util.StdDateFormat;

import org.junit.Assert;
import org.junit.Test;
import org.junit.Before;

@SuppressWarnings("serial")
public class DateTimeSerializerBaseTest {
    
    private DateTimeSerializerBase<Object> serializer;

    @Before
    public void setup() {
        serializer = new DateTimeSerializerBase<Object>(Object.class, null, null) {

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
    }

    @Test
    public void testCreateContextualWithoutFormat() throws JsonMappingException {
        SerializerProvider serializers = null;
        BeanProperty property = null;
        JsonSerializer<?> result = serializer.createContextual(serializers, property);
        Assert.assertEquals(serializer, result);
    }

    @Test
    public void testCreateContextualWithNumericShape() throws JsonMappingException {
        SerializerProvider serializers = null;
        BeanProperty property = new BeanProperty() {
            @Override
            public <A extends Annotation> A getAnnotation(Class<A> annotationClass) {
                return null;
            }

            @Override
            public AnnotatedMember getMember() {
                return null;
            }

            @Override
            public PropertyName getFullName() {
                return null;
            }

            @Override
            public boolean isRequired() {
                return false;
            }

            @Override
            public Class<?> getRawType() {
                return null;
            }

            @Override
            public Type getType() {
                return null;
            }
        };
        JsonSerializer<?> result = serializer.createContextual(serializers, property);
        Assert.assertTrue(result instanceof DateTimeSerializerBase);
        DateTimeSerializerBase<?> dateTimeSerializerBase = (DateTimeSerializerBase<?>) result;
        Assert.assertTrue(dateTimeSerializerBase._useTimestamp);
    }

    @Test
    public void testCreateContextualWithStringShape() throws JsonMappingException {
        SerializerProvider serializers = null;
        BeanProperty property = new BeanProperty() {
            @Override
            public <A extends Annotation> A getAnnotation(Class<A> annotationClass) {
                return null;
            }

            @Override
            public AnnotatedMember getMember() {
                return null;
            }

            @Override
            public PropertyName getFullName() {
                return null;
            }

            @Override
            public boolean isRequired() {
                return false;
            }

            @Override
            public Class<?> getRawType() {
                return null;
            }

            @Override
            public Type getType() {
                return null;
            }
        };
        JsonFormat.Value format = new JsonFormat.Value();
        format.setShape(JsonFormat.Shape.STRING);
        JsonSerializer<?> result = serializer.createContextual(serializers, property);
        Assert.assertTrue(result instanceof DateTimeSerializerBase);
        DateTimeSerializerBase<?> dateTimeSerializerBase = (DateTimeSerializerBase<?>) result;
        Assert.assertFalse(dateTimeSerializerBase._useTimestamp);
    }

    @Test
    public void testCreateContextualWithCustomPattern() throws JsonMappingException {
        SerializerProvider serializers = null;
        BeanProperty property = new BeanProperty() {
            @Override
            public <A extends Annotation> A getAnnotation(Class<A> annotationClass) {
                return null;
            }

            @Override
            public AnnotatedMember getMember() {
                return null;
            }

            @Override
            public PropertyName getFullName() {
                return null;
            }

            @Override
            public boolean isRequired() {
                return false;
            }

            @Override
            public Class<?> getRawType() {
                return null;
            }

            @Override
            public Type getType() {
                return null;
            }
        };
        JsonFormat.Value format = new JsonFormat.Value();
        format.setShape(JsonFormat.Shape.STRING);
        format.setPattern("yyyy-MM-dd");
        JsonSerializer<?> result = serializer.createContextual(serializers, property);
        Assert.assertTrue(result instanceof DateTimeSerializerBase);
        DateTimeSerializerBase<?> dateTimeSerializerBase = (DateTimeSerializerBase<?>) result;
        Assert.assertFalse(dateTimeSerializerBase._useTimestamp);
        Assert.assertNotNull(dateTimeSerializerBase._customFormat);
        Assert.assertEquals("yyyy-MM-dd", ((SimpleDateFormat) dateTimeSerializerBase._customFormat).toPattern());
    }

    @Test
    public void testCreateContextualWithCustomLocale() throws JsonMappingException {
        SerializerProvider serializers = null;
        BeanProperty property = new BeanProperty() {
            @Override
            public <A extends Annotation> A getAnnotation(Class<A> annotationClass) {
                return null;
            }

            @Override
            public AnnotatedMember getMember() {
                return null;
            }

            @Override
            public PropertyName getFullName() {
                return null;
            }

            @Override
            public boolean isRequired() {
                return false;
            }

            @Override
            public Class<?> getRawType() {
                return null;
            }

            @Override
            public Type getType() {
                return null;
            }
        };
        JsonFormat.Value format = new JsonFormat.Value();
        format.setShape(JsonFormat.Shape.STRING);
        format.setLocale(Locale.US);
        JsonSerializer<?> result = serializer.createContextual(serializers, property);
        Assert.assertTrue(result instanceof DateTimeSerializerBase);
        DateTimeSerializerBase<?> dateTimeSerializerBase = (DateTimeSerializerBase<?>) result;
        Assert.assertFalse(dateTimeSerializerBase._useTimestamp);
        Assert.assertNotNull(dateTimeSerializerBase._customFormat);
        Assert.assertEquals(Locale.US, ((SimpleDateFormat) dateTimeSerializerBase._customFormat).getLocale());
    }

    @Test
    public void testCreateContextualWithCustomTimeZone() throws JsonMappingException {
        SerializerProvider serializers = null;
        BeanProperty property = new BeanProperty() {
            @Override
            public <A extends Annotation> A getAnnotation(Class<A> annotationClass) {
                return null;
            }

            @Override
            public AnnotatedMember getMember() {
                return null;
            }

            @Override
            public PropertyName getFullName() {
                return null;
            }

            @Override
            public boolean isRequired() {
                return false;
            }

            @Override
            public Class<?> getRawType() {
                return null;
            }

            @Override
            public Type getType() {
                return null;
            }
        };
        JsonFormat.Value format = new JsonFormat.Value();
        format.setShape(JsonFormat.Shape.STRING);
        format.setTimeZone(TimeZone.getTimeZone("PST"));
        JsonSerializer<?> result = serializer.createContextual(serializers, property);
        Assert.assertTrue(result instanceof DateTimeSerializerBase);
        DateTimeSerializerBase<?> dateTimeSerializerBase = (DateTimeSerializerBase<?>) result;
        Assert.assertFalse(dateTimeSerializerBase._useTimestamp);
        Assert.assertNotNull(dateTimeSerializerBase._customFormat);
        Assert.assertEquals(TimeZone.getTimeZone("PST"), ((SimpleDateFormat) dateTimeSerializerBase._customFormat).getTimeZone());
    }

    @Test
    public void testIsEmptyWithValueNull() {
        Assert.assertTrue(serializer.isEmpty(null));
    }

    @Test
    public void testIsEmptyWithValueNotNullButTimestampZero() {
        Object value = new Object() {
            @SuppressWarnings("unused")
            public long _timestamp() {
                return 0L;
            }
        };
        Assert.assertTrue(serializer.isEmpty(value));
    }

    @Test
    public void testIsEmptyWithValueNotNullButTimestampNotZero() {
        Object value = new Object() {
            @SuppressWarnings("unused")
            public long _timestamp() {
                return 1L;
            }
        };
        Assert.assertFalse(serializer.isEmpty(value));
    }

    @Test
    public void testGetSchemaWithTimestamp() {
        SerializerProvider serializers = null;
        Type typeHint = null;
        JsonNode schema = serializer.getSchema(serializers, typeHint);
        Assert.assertEquals("number", schema.get("type").asText());
    }

    @Test
    public void testGetSchemaWithoutTimestamp() {
        DateTimeSerializerBase<Object> customSerializer = new DateTimeSerializerBase<Object>(Object.class, false, null) {

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
        SerializerProvider serializers = null;
        Type typeHint = null;
        JsonNode schema = customSerializer.getSchema(serializers, typeHint);
        Assert.assertEquals("string", schema.get("type").asText());
    }

    @Test
    public void testAcceptJsonFormatVisitorWithTimestamp() throws JsonMappingException {
        JsonFormatVisitorWrapper visitor = null;
        JavaType typeHint = null;
        serializer.acceptJsonFormatVisitor(visitor, typeHint);
    }

    @Test
    public void testAcceptJsonFormatVisitorWithoutTimestamp() throws JsonMappingException {
        DateTimeSerializerBase<Object> customSerializer = new DateTimeSerializerBase<Object>(Object.class, false, null) {

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
        JsonFormatVisitorWrapper visitor = null;
        JavaType typeHint = null;
        customSerializer.acceptJsonFormatVisitor(visitor, typeHint);
    }

    @Test
    public void testSerialize() throws IOException {
        Object value = new Object();
        JsonGenerator gen = null;
        SerializerProvider serializers = null;
        serializer.serialize(value, gen, serializers);
    }

}