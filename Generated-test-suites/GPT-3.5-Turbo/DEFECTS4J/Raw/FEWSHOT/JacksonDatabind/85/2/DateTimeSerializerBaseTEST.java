package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.TimeZone;

import org.junit.Test;

import com.fasterxml.jackson.databind.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitorWrapper;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import com.fasterxml.jackson.databind.util.StdDateFormat;

public class DateTimeSerializerBaseTest {
   
    @Test
    public void testConstructor() {
        DateTimeSerializerBase<SampleClass> serializer = new DateTimeSerializerChildClass(SampleClass.class, true, null);
        
        assertTrue(serializer instanceof ContextualSerializer);
        assertTrue(serializer instanceof JsonSerializer);
    }

    @Test
    public void testWithFormat() {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ", Locale.getDefault());
        DateTimeSerializerBase<SampleClass> serializer = new DateTimeSerializerChildClass(SampleClass.class, true, df);

        DateTimeSerializerBase<SampleClass> newSerializer = serializer.withFormat(false, df);

        assertEquals(false, newSerializer._useTimestamp);
        assertEquals(df, newSerializer._customFormat);
    }

    @Test
    public void testCreateContextualWithNullProperty() {
        DateTimeSerializerBase<SampleClass> serializer = new DateTimeSerializerChildClass(SampleClass.class, true, null);

        assertNull(serializer.createContextual(null, null));
    }

    @Test
    public void testCreateContextualWithNullFormat() {
        DateTimeSerializerBase<SampleClass> serializer = new DateTimeSerializerChildClass(SampleClass.class, true, null);
        BeanProperty property = new BeanProperty() {

            @Override
            public String getName() {
                return "testProperty";
            }

            @Override
            public JavaType getType() {
                return null;
            }

            @Override
            public PropertyName getWrapperName() {
                return null;
            }

            @Override
            public AnnotatedMember getMember() {
                return null;
            }

            @Override
            public PropertyMetadata getMetadata() {
                return null;
            }

            @Override
            public ObjectIdInfo getObjectIdInfo() {
                return null;
            }

            @Override
            public boolean isRequired() {
                return false;
            }

            @Override
            public <A extends Annotation> A getAnnotation(Class<A> arg0) {
                return null;
            }

            @Override
            public AnnotatedElement getAnnotated() {
                return null;
            }

            @Override
            public JsonFormat.Value findPropertyFormat(SerializerProvider provider) {
                return null;
            }

        };

        assertTrue(serializer.createContextual(new SerializerProvider(), property) instanceof DateTimeSerializerBase);

    }

    @Test
    public void testIsEmpty() {
        DateTimeSerializerBase<SampleClass> serializer = new DateTimeSerializerChildClass(SampleClass.class, true, null);
        
        assertTrue(serializer.isEmpty(null, new SampleClass()));
        assertTrue(serializer.isEmpty(new SerializerProvider(), new SampleClass()));
    }

    @Test
    public void testGetSchema() {
        DateTimeSerializerBase<SampleClass> serializer = new DateTimeSerializerChildClass(SampleClass.class, true, null);
        JsonSerializer defaultSerializer = new ObjectMapper().findSerializerFor(serializer.handledType());

        assertEquals(defaultSerializer.getSchema(new SerializerProvider(), null), serializer.getSchema(new SerializerProvider(), null));
    }

    @Test
    public void testAcceptJsonFormatVisitor() {
        DateTimeSerializerBase<SampleClass> serializer = new DateTimeSerializerChildClass(SampleClass.class, true, null);
        JsonFormatVisitorWrapper jsonFormatVisitorWrapper = new JsonFormatVisitorWrapper.Base();

        serializer.acceptJsonFormatVisitor(jsonFormatVisitorWrapper, null);
    }

    @Test
    public void testSerialize() throws Exception {
        DateTimeSerializerBase<SampleClass> serializer = new DateTimeSerializerChildClass(SampleClass.class, true, null);
        JsonGenerator jsonGenerator = new ObjectMapper().getFactory().createGenerator(System.out);

        SampleClass obj = new SampleClass();
        serializer.serialize(obj, jsonGenerator, new SerializerProvider());

    }

    class DateTimeSerializerChildClass extends DateTimeSerializerBase<SampleClass> {

        public DateTimeSerializerChildClass(Class<SampleClass> type, Boolean useTimestamp, DateFormat customFormat) {
            super(type, useTimestamp, customFormat);
        }

        @Override
        public DateTimeSerializerBase<SampleClass> withFormat(Boolean timestamp, DateFormat customFormat) {
            return new DateTimeSerializerChildClass(this.handledType(), timestamp, customFormat);
        }

        @Override
        protected long _timestamp(SampleClass value) {
            return 0;
        }

        @Override
        public void serialize(SampleClass value, JsonGenerator gen, SerializerProvider serializers) {
            // TODO Auto-generated method stub

        }

    }

    class SampleClass {
        
    }
}