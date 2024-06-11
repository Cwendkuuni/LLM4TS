
package com.fasterxml.jackson.databind.ser.std;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.introspect.Annotated;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitorWrapper;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import com.fasterxml.jackson.databind.util.StdDateFormat;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.TimeZone;

import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class DateTimeSerializerBaseTest {

    private DateTimeSerializerBaseTest.DateTimeSerializerBaseImpl serializer;

    @Mock
    private JsonSerializer<?> jsonSerializer;

    @Mock
    private SerializerProvider serializerProvider;

    @Mock
    private BeanProperty beanProperty;

    @Mock
    private Annotated annotated;

    @Mock
    private JsonFormat.Value format;

    @Mock
    private JsonFormatVisitorWrapper visitorWrapper;

    @Mock
    private JavaType typeHint;

    @Mock
    private JsonGenerator jsonGenerator;

    @BeforeClass
    public void setup() {
        serializer = new DateTimeSerializerBaseTest.DateTimeSerializerBaseImpl();

        when(serializerProvider.getAnnotationIntrospector().findFormat(annotated)).thenReturn(format);
        when(format.getShape()).thenReturn(JsonFormat.Shape.STRING);
        when(format.getTimeZone()).thenReturn(TimeZone.getTimeZone("GMT"));
        when(format.hasPattern()).thenReturn(true);
        when(format.getPattern()).thenReturn("yyyy-MM-dd HH:mm:ss");
        when(format.hasLocale()).thenReturn(true);
        when(format.getLocale()).thenReturn(Locale.ENGLISH);
        when(serializerProvider.getTimeZone()).thenReturn(TimeZone.getTimeZone("GMT"));
    }

    @Test
    public void testCreateDateFormattedSerializer() {
        DateTimeSerializerBase<DateTimeSerializerBaseTest.DateTimeSerializerBaseImpl> actualDateFormattedSerializer =
                serializer.withFormat(false, StdDateFormat.instance);

        assertSame(serializer, actualDateFormattedSerializer);
    }

    @Test
    public void testCreateDateFormattedSerializer_CaseDefaultFormat() {
        DateTimeSerializerBase<DateTimeSerializerBaseTest.DateTimeSerializerBaseImpl> actualDateFormattedSerializer =
                serializer.withFormat(false, null);

        assertSame(serializer, actualDateFormattedSerializer);

        actualDateFormattedSerializer =
                serializer.withFormat(true, null);

        assertSame(serializer, actualDateFormattedSerializer);
    }

    @Test
    public void testDeserialize() throws IOException {
        DateTimeSerializerBase<DateTimeSerializerBaseTest.DateTimeSerializerBaseImpl> deserializer =
                new DateTimeSerializerBaseTest.DateTimeSerializerBaseImpl().withFormat(false, StdDateFormat.instance);
        DateTimeSerializerBaseTest.DateTimeSerializerBaseImpl value = new DateTimeSerializerBaseTest.DateTimeSerializerBaseImpl();
        deserializer.serialize(value, jsonGenerator, serializerProvider);

        verify(jsonSerializer).serialize(value, jsonGenerator, serializerProvider);
    }

    @Test
    public void testIsNotEmpty() {
        DateTimeSerializerBase<DateTimeSerializerBaseTest.DateTimeSerializerBaseImpl> deserializer =
                new DateTimeSerializerBaseTest.DateTimeSerializerBaseImpl();
        DateTimeSerializerBaseTest.DateTimeSerializerBaseImpl value = new DateTimeSerializerBaseTest.DateTimeSerializerBaseImpl();
        boolean result = deserializer.isEmpty(serializerProvider, value);

        assertFalse(result);
    }

    @Test
    public void testGetSchema() {
        DateTimeSerializerBase<DateTimeSerializerBaseTest.DateTimeSerializerBaseImpl> deserializer =
                new DateTimeSerializerBaseTest.DateTimeSerializerBaseImpl().withFormat(false, StdDateFormat.instance);

        deserializer.getSchema(serializerProvider, DateTimeSerializerBaseTest.DateTimeSerializerBaseImpl.class);

        verify(visitorWrapper).setProvider(serializerProvider);
        verify(typeHint).getType();
    }

    @Test
    public void testAcceptJsonFormatVisitor() throws JsonMappingException {
        DateTimeSerializerBase<DateTimeSerializerBaseTest.DateTimeSerializerBaseImpl> deserializer =
                new DateTimeSerializerBaseTest.DateTimeSerializerBaseImpl().withFormat(false, StdDateFormat.instance);

        deserializer.acceptJsonFormatVisitor(visitorWrapper, typeHint);

        verify(visitorWrapper).setProvider(serializerProvider);
        verify(typeHint).getType();
        verify(visitorWrapper).expectStringFormat(typeHint);
    }

    private static class DateTimeSerializerBaseImpl extends DateTimeSerializerBase<DateTimeSerializerBaseTest.DateTimeSerializerBaseImpl> {
        public DateTimeSerializerBaseImpl() {
            super(DateTimeSerializerBaseTest.DateTimeSerializerBaseImpl.class, true, null);
        }

        @Override
        protected long _timestamp(DateTimeSerializerBaseTest.DateTimeSerializerBaseImpl value) {
            return 0;
        }

        @Override
        public void serialize(DateTimeSerializerBaseTest.DateTimeSerializerBaseImpl value, JsonGenerator gen, SerializerProvider serializers) throws IOException {

        }
    }

}
