package com.fasterxml.jackson.databind.ser.std;

import java.io.IOException;
import java.lang.reflect.Type;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.TimeZone;

import com.fasterxml.jackson.annotation.JsonFormat;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;

import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.jsonFormatVisitors.*;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import com.fasterxml.jackson.databind.util.StdDateFormat;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

@SuppressWarnings("serial")
public class DateTimeSerializerBaseTest {

    private DateTimeSerializerBase<String> serializer;

    @Before
    public void setUp() {
        serializer = new DateTimeSerializerBase<String>(String.class, false, null) {
            @Override
            public DateTimeSerializerBase<String> withFormat(Boolean timestamp, DateFormat customFormat) {
                return null;
            }

            @Override
            protected long _timestamp(String value) {
                return 0;
            }

            @Override
            public void serialize(String value, JsonGenerator gen, SerializerProvider serializers) throws IOException {

            }

            @Override
            public boolean isEmpty(SerializerProvider serializers, String value) {
                return false;
            }
        };
    }

    @Test
    public void testContextualSerializer() throws JsonMappingException {
        SerializerProvider serializerProvider = new SerializerProvider();
        BeanProperty beanProperty = new BeanProperty();

        JsonSerializer<?> result = serializer.createContextual(serializerProvider, beanProperty);
        
        Assert.assertEquals(serializer, result);
    }

    @Test
    public void testIsEmpty() {
        String nullValue = null;
        String notNullValue = "test";

        boolean result1 = serializer.isEmpty(nullValue);
        boolean result2 = serializer.isEmpty(notNullValue);

        Assert.assertTrue(result1);
        Assert.assertFalse(result2);
    }

    @Test
    public void testGetSchema() {
        SerializerProvider serializerProvider = new SerializerProvider();
        Type type = String.class;

        JsonNode result = serializer.getSchema(serializerProvider, type);

        Assert.assertNotNull(result);
    }

    @Test
    public void testAcceptJsonFormatVisitor() throws JsonMappingException {
        JsonFormatVisitorWrapper visitor = new JsonFormatVisitorWrapper();
        JavaType typeHint = JsonUtil.stringValueOf("test");

        serializer.acceptJsonFormatVisitor(visitor, typeHint);
    }
}