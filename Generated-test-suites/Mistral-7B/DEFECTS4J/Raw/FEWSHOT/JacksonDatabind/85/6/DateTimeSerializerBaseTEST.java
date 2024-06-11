package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

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
import com.fasterxml.jackson.databind.ser.*;
import com.fasterxml.jackson.databind.util.StdDateFormat;
import org.junit.Before;
import org.junit.Test;

public class DateTimeSerializerBaseTest {

    private JsonSerializerProvider _provider;
    private JsonGenerator _generator;
    private JsonParser _parser;

    @Before
    public void setup() {
        _provider = mock(JsonSerializerProvider.class);
        _generator = mock(JsonGenerator.class);
        _parser = mock(JsonParser.class);
    }

    @Test
    public void testIsEmpty() {
        DateTimeSerializerBase<Object> serializer = new DateTimeSerializerBase<Object>(Object.class, Boolean.FALSE, null);
        assertFalse(serializer.isEmpty(null));
        assertFalse(serializer.isEmpty(new Object()));
        assertFalse(serializer.isEmpty(_provider, null));
        assertFalse(serializer.isEmpty(_provider, new Object()));
    }

    @Test
    public void testAcceptJsonFormatVisitor() throws JsonMappingException {
        DateTimeSerializerBase<Object> serializer = new DateTimeSerializerBase<Object>(Object.class, Boolean.FALSE, null);
        JsonFormatVisitorWrapper visitor = mock(JsonFormatVisitorWrapper.class);
        JavaType typeHint = mock(JavaType.class);
        boolean asNumber = true;

        serializer._acceptJsonFormatVisitor(visitor, typeHint, asNumber);

        verify(visitor).visitIntFormat(eq(typeHint), eq(JsonParser.NumberType.LONG), eq(JsonValueFormat.UTC_MILLISEC));
    }

    @Test
    public void testSerializeWithDefaultValues() throws IOException {
        DateTimeSerializerBase<Long> serializer = new DateTimeSerializerBase<Long>(Long.class, Boolean.FALSE, null);
        Long value = System.currentTimeMillis();

        when(_parser.getValueAsLong()).thenReturn(value);

        serializer.serialize(null, _generator, _provider);
        serializer.serialize(value, _generator, _provider);

        verify(_generator).writeNumber(value);
    }

    @Test
    public void testSerializeWithCustomFormat() throws IOException {
        DateTimeSerializerBase<String> serializer = new DateTimeSerializerBase<String>(String.class, Boolean.FALSE, new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
        String value = "2022-12-31 23:59:59";

        when(_parser.getText()).thenReturn(value);

        serializer.serialize(null, _generator, _provider);
        serializer.serialize(value, _generator, _provider);

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = dateFormat.parse(value);
        when(_provider.defaultSerializers().findValueSerializer(String.class)).thenReturn(new StringSerializer(_provider));
        when(_generator.writeString(date.toString())).thenCallRealMethod();

        serializer.serialize(date, _generator, _provider);

        verify(_generator).writeString(date.toString());
    }

    @Test
    public void testSerializeWithTimestamp() throws IOException {
        DateTimeSerializerBase<Long> serializer = new DateTimeSerializerBase<Long>(Long.class, Boolean.TRUE, null);
        Long value = System.currentTimeMillis();

        when(_parser.getValueAsLong()).thenReturn(value);

        serializer.serialize(null, _generator, _provider);
        serializer.serialize(value, _generator, _provider);

        verify(_generator).writeNumber(value);
    }

    @Test
    public void testCreateContextual() throws JsonMappingException {
        DateTimeSerializerBase<Object> serializer = new DateTimeSerializerBase<Object>(Object.class, Boolean.FALSE, null);
        BeanProperty property = mock(BeanProperty.class);
        JsonFormat.Value format = mock(JsonFormat.Value.class);
        Type typeHint = mock(Type.class);

        when(property.getType()).thenReturn(typeHint);
        when(format.getShape()).thenReturn(JsonFormat.Shape.STRING);
        when(format.hasPattern()).thenReturn(false);
        when(format.hasLocale()).thenReturn(false);
        when(format.hasTimeZone()).thenReturn(false);

        JsonSerializer<Object> result = serializer.createContextual(_provider, property);

        assertEquals(serializer.getClass(), result.getClass());
    }

    @Test
    public void testCreateContextualWithFormat() throws JsonMappingException {
        DateTimeSerializerBase<Object> serializer = new DateTimeSerializerBase<Object>(Object.class, Boolean.FALSE, null);
        BeanProperty property = mock(BeanProperty.class);
        JsonFormat.Value format = mock(JsonFormat.Value.class);
        Type typeHint = mock(Type.class);

        when(property.getType()).thenReturn(typeHint);
        when(format.getShape()).thenReturn(JsonFormat.Shape.STRING);
        when(format.getPattern()).thenReturn("yyyy-MM-dd HH:mm:ss");

        JsonFormat.Shape shape = JsonFormat.Shape.STRING;
        when(format.getShape()).thenReturn(shape);

        JsonSerializer<Object> result = serializer.createContextual(_provider, property);

        assertTrue(result instanceof DateTimeSerializerBase);
        DateTimeSerializerBase<Object> castedResult = (DateTimeSerializerBase<Object>) result;
        assertEquals(shape, castedResult._customFormat.getShape());
        assertEquals("yyyy-MM-dd HH:mm:ss", castedResult._customFormat.getPattern());
    }

    @Test
    public void testCreateContextualWithTimestamp() throws JsonMappingException {
        DateTimeSerializerBase<Object> serializer = new DateTimeSerializerBase<Object>(Object.class, Boolean.FALSE, null);
        BeanProperty property = mock(BeanProperty.class);
        JsonFormat.Value format = mock(JsonFormat.Value.class);
        Type typeHint = mock(Type.class);

        when(property.getType()).thenReturn(typeHint);
        when(format.getShape()).thenReturn(JsonFormat.Shape.NUMBER);

        JsonSerializer<Object> result = serializer.createContextual(_provider, property);

        assertTrue(result instanceof DateTimeSerializerBase);
        DateTimeSerializerBase<Object> castedResult = (DateTimeSerializerBase<Object>) result;
        assertNull(castedResult._customFormat);
        assertTrue(castedResult._useTimestamp);
    }
}