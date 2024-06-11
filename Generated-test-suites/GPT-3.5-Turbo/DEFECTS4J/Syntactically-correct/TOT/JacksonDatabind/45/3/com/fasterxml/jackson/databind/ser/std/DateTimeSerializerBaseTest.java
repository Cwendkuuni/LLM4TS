package com.fasterxml.jackson.databind.ser.std;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.TimeZone;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

@RunWith(MockitoJUnitRunner.class)
public class DateTimeSerializerBaseTest {

    @Mock
    private SerializerProvider serializerProvider;

    @Mock
    private BeanProperty beanProperty;

    @Test
    public void testSerialize_NullValue() throws Exception {
        DateTimeSerializerBase<Long> serializer = new DateTimeSerializerBase<Long>(Long.class, false, null) {
            @Override
            public void serialize(Long value, JsonGenerator gen, SerializerProvider serializers) {
            }

            @Override
            protected long _timestamp(Long value) {
                return 0;
            }
        };

        Assert.assertTrue(serializer.isEmpty(serializerProvider, null));
    }

    @Test
    public void testSerialize_NonNullValue() throws Exception {
        DateTimeSerializerBase<Long> serializer = new DateTimeSerializerBase<Long>(Long.class, false, null) {
            @Override
            public void serialize(Long value, JsonGenerator gen, SerializerProvider serializers) {
            }

            @Override
            protected long _timestamp(Long value) {
                return value;
            }
        };

        Assert.assertFalse(serializer.isEmpty(serializerProvider, 123456L));
    }

    @Test
    public void testWithFormat_ShapeNumeric() throws Exception {
        DateTimeSerializerBase<Long> serializer = new DateTimeSerializerBase<Long>(Long.class, false, null) {
            @Override
            public void serialize(Long value, JsonGenerator gen, SerializerProvider serializers) {
            }

            @Override
            protected long _timestamp(Long value) {
                return 0;
            }
        };

        JsonSerializer<?> result = serializer.createContextual(serializerProvider, beanProperty);
        Assert.assertNotNull(result);
        Assert.assertEquals(serializer, result);
    }

    @Test
    public void testWithFormat_ShapeString() throws Exception {
        @JsonDeserialize(using = CustomDeserializer.class)
        class CustomDateFormat extends SimpleDateFormat {
            private static final long serialVersionUID = 1L;

            public TimeZone getLastTimeZone() {
                return super.getTimeZone();
            }
        }

        class CustomDeserializer extends JsonDeserializer<Void> {
            private static final long serialVersionUID = 1L;

            @Override
            public Void deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
                return null;
            }
        }

        DateTimeSerializerBase<Long> serializer = new DateTimeSerializerBase<Long>(Long.class, false, null) {
            @Override
            public void serialize(Long value, JsonGenerator gen, SerializerProvider serializers) {
            }

            @Override
            protected long _timestamp(Long value) {
                return 0;
            }
        };

        SimpleDateFormat df = Mockito.mock(CustomDateFormat.class);
        Mockito.when(serializerProvider.getAnnotationIntrospector()).thenReturn(null);
        Mockito.when(beanProperty.getMember()).thenReturn(null);

        JsonFormat.Value format = new JsonFormat.Value(null, null);
        Mockito.when(serializerProvider.getAnnotationIntrospector().findFormat(beanProperty.getMember())).thenReturn(format);
        Mockito.when(format.getShape()).thenReturn(JsonFormat.Shape.STRING);
        Mockito.when(format.getTimeZone()).thenReturn(null);
        Mockito.when(format.getPattern()).thenReturn("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
        Mockito.when(format.hasPattern()).thenReturn(true);
        Mockito.when(format.hasLocale()).thenReturn(false);
        Mockito.when(format.getLocale()).thenReturn(null);

        Mockito.when(serializerProvider.getTimeZone()).thenReturn(TimeZone.getTimeZone("GMT"));

        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, 2019);
        cal.set(Calendar.MONTH, 7);
        cal.set(Calendar.DAY_OF_MONTH, 15);

        Assert.assertFalse(serializer.createContextual(serializerProvider, beanProperty).getClass().equals(serializer.getClass()));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testWithFormat_NullProperty() throws Exception {
        DateTimeSerializerBase<Long> serializer = new DateTimeSerializerBase<Long>(Long.class, false, null) {
            @Override
            public void serialize(Long value, JsonGenerator gen, SerializerProvider serializers) {
            }

            @Override
            protected long _timestamp(Long value) {
                return 0;
            }
        };

        serializer.createContextual(serializerProvider, null);
    }
}