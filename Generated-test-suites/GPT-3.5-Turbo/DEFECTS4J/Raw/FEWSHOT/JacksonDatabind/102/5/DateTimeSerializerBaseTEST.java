package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;
import java.util.concurrent.atomic.AtomicReference;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitorWrapper;
import com.fasterxml.jackson.databind.jsonschema.JsonSerializableSchema;
import com.fasterxml.jackson.databind.jsonschema.SchemaAware;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import com.fasterxml.jackson.databind.util.StdDateFormat;

@SuppressWarnings("serial")
public abstract class DateTimeSerializerBaseTest<T> {

    DateTimeSerializerBase<T> serializer;

    @Mock
    BeanProperty beanProperty;

    @Mock
    SerializerProvider serializerProvider;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testCreateContextual() throws Exception {
        JsonSerializer<?> jsonSerializer = serializer.createContextual(serializerProvider, beanProperty);
        assertTrue(jsonSerializer instanceof DateTimeSerializerBase);
    }

    @Test
    public void testCreateContextualWithNumericShape() throws Exception {
        JsonFormat.Value format = new JsonFormat.Value().withShape(JsonFormat.Shape.NUMBER);
        serializer.createContextual(serializerProvider, format);
        assertTrue(serializer._asTimestamp(serializerProvider));
    }

    @Test
    public void testCreateContextualWithShapeString() throws Exception {
        JsonFormat.Value format = new JsonFormat.Value().withShape(JsonFormat.Shape.STRING);
        serializer.createContextual(serializerProvider, format);
        assertFalse(serializer._asTimestamp(serializerProvider));
    }

    @Test
    public void testCreateContextualWithPattern() throws Exception {
        JsonFormat.Value format = new JsonFormat.Value().withPattern("yyyy-MM-dd");
        serializer.createContextual(serializerProvider, format);
        assertFalse(serializer._asTimestamp(serializerProvider));
    }

    @Test
    public void testSerializeAsString() throws Exception {
        Date date = new Date();
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        serializer._serializeAsString(date, null, dateFormat);
        // TODO: Add assertion
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSerializeAsStringWithNullSerializerProvider() throws Exception {
        Date date = new Date();
        serializer._serializeAsString(date, null, null);
    }

    @Test
    public void testIsEmpty() throws Exception {
        Date date = new Date();
        assertFalse(serializer.isEmpty(serializerProvider, date));
    }

    @Test
    public void testGetSchema() throws Exception {
        JsonNode schema = serializer.getSchema(serializerProvider, null);
        // TODO: Add assertion
    }

    @Test
    public void testAcceptJsonFormatVisitor() throws Exception {
        JsonFormatVisitorWrapper visitor = null;
        JavaType typeHint = null;
        serializer.acceptJsonFormatVisitor(visitor, typeHint);
        // TODO: Add assertion
    }

    // Helper methods
    // TODO: Add helper methods tests

}