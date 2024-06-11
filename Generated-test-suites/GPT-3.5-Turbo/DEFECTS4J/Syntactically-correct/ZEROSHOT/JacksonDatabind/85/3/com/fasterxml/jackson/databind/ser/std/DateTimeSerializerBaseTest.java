
package com.fasterxml.jackson.databind.ser.std;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
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

public class DateTimeSerializerBaseTest {

    private DateTimeSerializerBaseTest dateTimeSerializerBaseTest;

    @Before
    public void setUp() {
        Locale.setDefault(Locale.US);
        TimeZone.setDefault(TimeZone.getTimeZone("GMT"));

        dateTimeSerializerBaseTest = new DateTimeSerializerBaseTest();
    }

    @Test
    public void testCreateContextual() throws JsonMappingException {
        SerializerProvider serializers = null;
        BeanProperty property = null;
        dateTimeSerializerBaseTest.createContextual(serializers, property);
        
        // TODO: add assertions
        Assert.fail("Not implemented");
    }

    @Test
    public void testIsEmpty() {
        T value = null;
        dateTimeSerializerBaseTest.isEmpty(value);
        
        // TODO: add assertions
        Assert.fail("Not implemented");
    }

    @Test
    public void testSerialize() throws IOException {
        T value = null;
        JsonGenerator gen = null;
        SerializerProvider serializers = null;
        dateTimeSerializerBaseTest.serialize(value, gen, serializers);
        
        // TODO: add assertions
        Assert.fail("Not implemented");
    }

    @Test
    public void testGetSchema() {
        SerializerProvider serializers = null;
        Type typeHint = null;
        dateTimeSerializerBaseTest.getSchema(serializers, typeHint);
        
        // TODO: add assertions
        Assert.fail("Not implemented");
    }

    @Test
    public void testAcceptJsonFormatVisitor() {
        JsonFormatVisitorWrapper visitor = null;
        JavaType typeHint = null;
        dateTimeSerializerBaseTest.acceptJsonFormatVisitor(visitor, typeHint);
        
        // TODO: add assertions
        Assert.fail("Not implemented");
    }
}

