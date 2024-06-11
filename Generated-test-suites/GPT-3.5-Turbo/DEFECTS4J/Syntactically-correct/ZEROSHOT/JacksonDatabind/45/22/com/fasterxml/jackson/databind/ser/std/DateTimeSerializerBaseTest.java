
package com.fasterxml.jackson.databind.ser.std;

import org.junit.Assert;
import org.junit.Test;

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
import com.fasterxml.jackson.databind.introspect.Annotated;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitorWrapper;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import com.fasterxml.jackson.databind.util.StdDateFormat;

@SuppressWarnings("serial")
public abstract class DateTimeSerializerBaseTest {

    private DateTimeSerializerBase<SampleClass> dateTimeSerializer = new DateTimeSerializerBase<SampleClass>(SampleClass.class, false, new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX")) {
        @Override
        public DateTimeSerializerBase<SampleClass> withFormat(Boolean timestamp, DateFormat customFormat) {
            return null;
        }

        @Override
        public void serialize(SampleClass value, JsonGenerator gen, SerializerProvider serializers) throws IOException {

        }

        @Override
        protected long _timestamp(SampleClass value) {
            return 0;
        }

    };

    @Test
    public void testWithFormat() {
        DateTimeSerializerBase<SampleClass> res = dateTimeSerializer.withFormat(true, new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX"));

        Assert.assertNull(res);
    }

    @Test
    public void testCreateContextual() throws JsonMappingException {
        SerializerProvider sp = new ObjectMapper().getSerializerProvider();
        BeanProperty bp = sp.findPropertyDescription(Annotated.class, "someProperty");

        DateTimeSerializerBase<SampleClass> res = dateTimeSerializer.createContextual(sp, bp);

        Assert.assertEquals(dateTimeSerializer, res);
    }

    @Test
    public void testIsEmpty() {
        SampleClass value = new SampleClass();

        boolean res = dateTimeSerializer.isEmpty(value);
        Assert.assertTrue(res);
    }

    @Test
    public void testGetSchema() {
        SerializerProvider sp = new ObjectMapper().getSerializerProvider();

        JsonNode res = dateTimeSerializer.getSchema(sp, SampleClass.class);

        Assert.assertNotNull(res);
        Assert.assertTrue(res.isObject());
    }

    @Test
    public void testAcceptJsonFormatVisitor() throws JsonMappingException {
        JsonFormatVisitorWrapper visitor = new ObjectMapper().getSerializerProvider().getVisitor(SampleClass.class);

        dateTimeSerializer.acceptJsonFormatVisitor(visitor, SampleClass.class);

        Assert.assertNotNull(visitor);
    }

    private static class SampleClass {

    }
}
