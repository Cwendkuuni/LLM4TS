package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertNull;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitorWrapper;

import org.junit.Test;
import org.mockito.Mockito;

public class DateTimeSerializerBaseTest {

    @Test
    public void testConstructor() {
        DateTimeSerializerBase<TestValue> serializer = new TestDateTimeSerializer(TestValue.class, Boolean.TRUE, null);

        assertEquals(TestValue.class, serializer.handledType());
        assertTrue(serializer._useTimestamp);
        assertNull(serializer._customFormat);
    }

    @Test
    public void testWithFormat() {
        DateTimeSerializerBase<TestValue> serializer = new TestDateTimeSerializer(TestValue.class, Boolean.TRUE, null);
        DateTimeSerializerBase<TestValue> updatedSerializer = serializer.withFormat(Boolean.FALSE, new SimpleDateFormat());

        assertEquals(TestValue.class, updatedSerializer.handledType());
        assertFalse(updatedSerializer._useTimestamp);
        assertNotNull(updatedSerializer._customFormat);
    }

    @Test
    public void testCreateContextual() throws JsonMappingException {
        DateTimeSerializerBase<TestValue> serializer = new TestDateTimeSerializer(TestValue.class, Boolean.TRUE, null);
        SerializerProvider provider = Mockito.mock(SerializerProvider.class);
        BeanProperty property = Mockito.mock(BeanProperty.class);
        Annotated annotated = Mockito.mock(Annotated.class);
        JsonFormat.Value format = new JsonFormat.Value(null, null, null, JsonFormat.Shape.STRING, null, null);

        Mockito.when(provider.getAnnotationIntrospector()).thenReturn(Mockito.mock(AnnotationIntrospector.class));
        Mockito.when(property.getMember()).thenReturn(annotated);
        Mockito.when(provider.getLocale()).thenReturn(Locale.ENGLISH);
        Mockito.when(property.getMember()).thenReturn(annotated);
        Mockito.when(provider.getTimeZone()).thenReturn(TimeZone.getDefault());
        Mockito.when(provider.isEnabled(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)).thenReturn(Boolean.FALSE);

        DateTimeSerializerBase<TestValue> updatedSerializer = serializer.createContextual(provider, property);

        assertEquals(TestValue.class, updatedSerializer.handledType());
        assertFalse(updatedSerializer._useTimestamp);
        assertNotNull(updatedSerializer._customFormat);
    }

    @Test
    public void testIsEmpty() {
        DateTimeSerializerBase<TestValue> serializer = new TestDateTimeSerializer(TestValue.class, Boolean.TRUE, null);
        TestValue value = new TestValue();

        assertTrue(serializer.isEmpty(value));
    }

    @Test
    public void testGetSchema() {
        DateTimeSerializerBase<TestValue> serializer = new TestDateTimeSerializer(TestValue.class, Boolean.TRUE, null);
        SerializerProvider provider = Mockito.mock(SerializerProvider.class);
        JsonNode schema = serializer.getSchema(provider, TestValue.class);

        assertEquals("string", schema.asText());
    }

    @Test
    public void testAcceptJsonFormatVisitor() throws JsonMappingException {
        DateTimeSerializerBase<TestValue> serializer = new TestDateTimeSerializer(TestValue.class, Boolean.TRUE, null);
        JsonFormatVisitorWrapper visitor = Mockito.mock(JsonFormatVisitorWrapper.class);
        JavaType typeHint = Mockito.mock(JavaType.class);

        serializer.acceptJsonFormatVisitor(visitor, typeHint);

        Mockito.verify(visitor, Mockito.times(1)).visitStringFormat(Mockito.any(JavaType.class), Mockito.any(JsonValueFormat.class));
    }

    @Test
    public void testSerialize() throws Exception {
        DateTimeSerializerBase<TestValue> serializer = new TestDateTimeSerializer(TestValue.class, Boolean.TRUE, null);
        JsonGenerator gen = Mockito.mock(JsonGenerator.class);
        SerializerProvider provider = Mockito.mock(SerializerProvider.class);
        TestValue value = new TestValue();

        serializer.serialize(value, gen, provider);

        Mockito.verify(gen, Mockito.times(1)).writeString(Mockito.anyString());
    }
    
    private static class TestValue {
    	Date value = new Date();
    }
    
    private static class TestDateTimeSerializer extends DateTimeSerializerBase<TestValue> {

        public TestDateTimeSerializer(Class<TestValue> type, Boolean useTimestamp, DateFormat customFormat) {
            super(type, useTimestamp, customFormat);
        }

		@Override
		public DateTimeSerializerBase<TestValue> withFormat(Boolean timestamp, DateFormat customFormat) {
			return null;
		}

		@Override
		protected long _timestamp(TestValue value) {
			return 0;
		}

		@Override
		public void serialize(TestValue value, JsonGenerator gen, SerializerProvider serializers)
				throws Exception {
			gen.writeString("test");
		}
    	
    }
}