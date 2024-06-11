package com.fasterxml.jackson.databind.ser.std; // Make sure to replace it with the correct package name

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.TimeZone;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.JsonSerializer.Typing;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.ser.std.StdDateFormat;
import com.fasterxml.jackson.databind.util.StdDateFormat;

@RunWith(MockitoJUnitRunner.class)
public class DateTimeSerializerBaseTest {

    

    @Test
    public void testDateTimeSerializerBaseConstructor() {
        Class<String> stringType = String.class;
        DateTimeSerializerBase<String> serializer = new DateTimeSerializerBase<>(stringType, Boolean.FALSE, null);
        SerializerProvider serializers = Mockito.mock(SerializerProvider.class);
        assertEquals(stringType, serializer.handledType());
        assertFalse(serializer._useTimestamp);
        assertNull(_customFormat);
    }

    @Test
    public void testDateTimeSerializerBaseWithFormat() {
        Class<String> stringType = String.class;
        Boolean useTimestamp = Boolean.TRUE;
        DateFormat customFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        DateTimeSerializerBase<String> serializer = new DateTimeSerializerBase<>(stringType, useTimestamp, customFormat);
        assertEquals(stringType, serializer.handledType());
        assertTrue(serializer._useTimestamp);
        assertEquals(customFormat, serializer._customFormat);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDateTimeSerializerBaseWithNullSerializerProvider() {
        Class<String> stringType = String.class;
        DateTimeSerializerBase<String> serializer = new DateTimeSerializerBase<>(stringType, Boolean.FALSE, null);
        serializer.createContextual(null, null);
    }

    @Test
    public void testIsEmpty() {
        DateTimeSerializerBase<Date> serializer = Mockito.spy(new DateTimeSerializerBase<Date>(Date.class, Boolean.FALSE, null));
        Date date = new Date(0L);
        assertTrue(serializer.isEmpty(date));
        Date emptyDate = null;
        assertTrue(serializer.isEmpty(emptyDate));
        Date validDate = new Date(System.currentTimeMillis());
        assertFalse(serializer.isEmpty(validDate));
    }

    @Test
    public void testGetSchema() throws JsonMappingException {
        DateTimeSerializerBase<Date> serializer = Mockito.spy(new DateTimeSerializerBase<Date>(Date.class, Boolean.FALSE, null));
        JsonNode schema = serializer.getSchema(Mockito.mock(SerializerProvider.class), Date.class);
        assertEquals("number", schema.getClass().getSimpleName());
    }

    @Test
    public void testAcceptJsonFormatVisitor() throws JsonMappingException {
        DateTimeSerializerBase<Date> serializer = Mockito.spy(new DateTimeSerializerBase<Date>(Date.class, Boolean.FALSE, null));
        JsonFormatVisitorWrapper visitor = Mockito.mock(JsonFormatVisitorWrapper.class);
        JsonNode schema = serializer.acceptJsonFormatVisitor(visitor, Date.class);
        assertNotNull(schema);
    }

    @Test
    public void testSerialize() throws JsonGenerationException, JsonMappingException, IOException {
        DateTimeSerializerBase<Date> serializer = Mockito.spy(new DateTimeSerializerBase<Date>(Date.class, Boolean.FALSE, null));
        JsonGenerator gen = Mockito.mock(JsonGenerator.class);
        SerializerProvider serializers = Mockito.mock(SerializerProvider.class);
        Calendar calendar = new GregorianCalendar(2023, 0, 22);
        Date date = calendar.getTime();
        serializer._acceptJsonFormatVisitor(Mockito.mock(JsonFormatVisitorWrapper.class), Date.class.getType(), false);
        serializer.serialize(date, gen, serializers);
        String expectedJson = "\"2023-01-22T00:00:00.000Z\"";
        String actualJson = gen.getOutputBuffer().toString();
        assertEquals(expectedJson, actualJson);
    }

    @Test
    public void testSerializerWithCustomFormat() throws JsonGenerationException, JsonMappingException, IOException {
        DateTimeSerializerBase<Date> serializer = new DateTimeSerializerBase<Date>(Date.class, Boolean.FALSE, new SimpleDateFormat("dd-MM-yyyy HH:mm:ss"));
        JsonGenerator gen = Mockito.mock(JsonGenerator.class);
        SerializerProvider serializers = Mockito.mock(SerializerProvider.class);
        Calendar calendar = new GregorianCalendar(2023, 0, 22);
        Date date = calendar.getTime();
        serializer._acceptJsonFormatVisitor(Mockito.mock(JsonFormatVisitorWrapper.class), Date.class.getType(), false);
        serializer.serialize(date, gen, serializers);
        String expectedJson = "\"22-01-2023 HH:mm:ss\"";
        String actualJson = gen.getOutputBuffer().toString();
        assertEquals(expectedJson, actualJson);
    }

    @Test
    public void testSerializerWithUseTimestampEnabled() throws JsonGenerationException, JsonMappingException, IOException {
        DateTimeSerializerBase<Date> serializer = new DateTimeSerializerBase<Date>(Date.class, Boolean.TRUE, null);
        JsonGenerator gen = Mockito.mock(JsonGenerator.class);
        SerializerProvider serializers = Mockito.mock(SerializerProvider.class);
        Calendar calendar = new GregorianCalendar(2023, 0, 22);
        Date date = calendar.getTime();
        serializer._acceptJsonFormatVisitor(Mockito.mock(JsonFormatVisitorWrapper.class), Date.class.getType(), false);
        serializer.serialize(date, gen, serializers);
        long expectedValue = calendar.getTimeInMillis();
        long actualValue = gen.getOutputBuffer().readLong();
        assertEquals(expectedValue, actualValue);
    }

    @Test(expected = JsonMappingException.class)
    public void testSerializerWithInvalidTimestampFormat() throws JsonGenerationException, JsonMappingException, IOException {
        DateTimeSerializerBase<Date> serializer = new DateTimeSerializerBase<Date>(Date.class, Boolean.FALSE, null);
        JsonGenerator gen = Mockito.mock(JsonGenerator.class);
        SerializerProvider serializers = Mockito.mock(SerializerProvider.class);
        serializer.serialize(new Date(0L), gen, serializers);
        String unexpectedJson = "\"InvalidJsonFormat\"";
        gen.setOutputBuffer(unexpectedJson.getBytes());
        serializer.serialize(new Date(System.currentTimeMillis()), gen, serializers);
    }

    @Test(expected = JsonMappingException.class)
    public void testSerializerWithNullValue() throws JsonGenerationException, JsonMappingException, IOException {
        DateTimeSerializerBase<Date> serializer = new DateTimeSerializerBase<Date>(Date.class, Boolean.FALSE, null);
        JsonGenerator gen = Mockito.mock(JsonGenerator.class);
        SerializerProvider serializers = Mockito.mock(SerializerProvider.class);
        serializer.serialize(null, gen, serializers);
    }

    @Test
    public void testCreateContextualWithNullProperty() {
        DateTimeSerializerBase<Date> serializer = Mockito.spy(new DateTimeSerializerBase<Date>(Date.class, Boolean.FALSE, null));
        SerializerProvider serializers = Mockito.mock(SerializerProvider.class);
        BeanProperty property = null;
        JsonSerializer<Date> result = serializer.createContextual(serializers, property);
        assertEquals(serializer, result);
    }

    @Test
    public void testCreateContextualWithFormat() throws JsonMappingException {
        DateTimeSerializerBase<Date> serializer = new DateTimeSerializerBase<Date>(Date.class, Boolean.FALSE, null);
        SerializerProvider serializers = Mockito.mock(SerializerProvider.class);
        BeanProperty property = Mockito.mock(BeanProperty.class);
        JsonFormat.Value format = new JsonFormat.Value();
        format.setShape(JsonFormat.Shape.STRING);
        format.setPattern("dd-MM-yyyy HH:mm:ss");
        format.setLocale(Locale.US);
        format.setTimeZone(TimeZone.getTimeZone("UTC"));
        serializers.setSerializationConfig(Mockito.mock(SerializationConfig.class));
        Mockito.when(serializers.getSerializationConfig()).thenReturn(Mockito.mock(SerializationConfig.class));
        Mockito.when(serialized.findFormatOverrides(serializers, property, Date.class)).thenReturn(format);
        JsonSerializer<Date> result = serializer.createContextual(serializers, property);
        assertTrue(result instanceof DateTimeSerializerBase);
        SimpleDateFormat expectedFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        expectedFormat.setLocale(Locale.US);
        expectedFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        assertEquals(expectedFormat, ((DateTimeSerializerBase<Date>) result)._customFormat);
    }

    @Test
    public void testCreateContextualWithCustomFormatAndTimestamp() throws JsonMappingException {
        DateTimeSerializerBase<Date> serializer = new DateTimeSerializerBase<Date>(Date.class, Boolean.FALSE, null);
        SerializerProvider serializers = Mockito.mock(SerializerProvider.class);
        BeanProperty property = Mockito.mock(BeanProperty.class);
        JsonFormat.Value format = new JsonFormat.Value();
        format.setShape(JsonFormat.Shape.STRING);
        format.setPattern("dd-MM-yyyy HH:mm:ss");
        format.setLocale(Locale.US);
        format.setTimeZone(TimeZone.getTimeZone("UTC"));
        JsonFormat.Value formatWithTimestamp = new JsonFormat.Value();
        formatWithTimestamp.setShape(JsonFormat.Shape.NUMBER);
        formatWithTimestamp.setNumberType(JsonParser.NumberType.LONG);
        formatWithTimestamp.setValue("value");
        serializers.setSerializationConfig(Mockito.mock(SerializationConfig.class));
        Mockito.when(serializers.getSerializationConfig()).thenReturn(Mockito.mock(SerializationConfig.class));
        Mockito.when(serializers.findFormatOverrides(property, Date.class)).thenReturn(format);
        Mockito.when(serializers.findFormatOverrides(property, Date.class, Boolean.TRUE)).thenReturn(formatWithTimestamp);
        JsonSerializer<Date> result = serializer.createContextual(serializers, property);
        assertEquals(Long.TYPE, result.getSerializerProperties().getSerializerBinding().getType());
    }

    @Test
    public void testCreateContextualWithDefaultFormat() throws JsonMappingException {
        DateTimeSerializerBase<Date> serializer = new DateTimeSerializerBase<Date>(Date.class, Boolean.FALSE, null);
        SerializerProvider serializers = Mockito.mock(SerializerProvider.class);
        BeanProperty property = Mockito.mock(BeanProperty.class);
        JsonFormat.Value format = new JsonFormat.Value();
        format.setShape(JsonFormat.Shape.STRING);
        format.setPattern(StdDateFormat.DATE_FORMAT_STR_ISO8601);
        format.setLocale(Locale.US);
        format.setTimeZone(TimeZone.getTimeZone("UTC"));
        serializers.setSerializationConfig(Mockito.mock(SerializationConfig.class));
        Mockito.when(serializers.getSerializationConfig()).thenReturn(Mockito.mock(SerializationConfig.class));
        Mockito.when(serializers.findFormatOverrides(property, Date.class)).thenReturn(format);
        JsonSerializer<Date> result = serializer.createContextual(serializers, property);
        assertEquals(String.class, result.getSerializerProperties().getSerializerBinding().getType());
    }

    @Test
    public void testCreateContextualWithNullFormat() throws JsonMappingException {
        DateTimeSerializerBase<Date> serializer = new DateTimeSerializerBase<Date>(Date.class, Boolean.FALSE, null);
        SerializerProvider serializers = Mockito.mock(SerializerProvider.class);
        BeanProperty property = Mockito.mock(BeanProperty.class);
        serializers.setSerializationConfig(Mockito.mock(SerializationConfig.class));
        Mockito.when(serializers.getSerializationConfig()).thenReturn(Mockito.mock(SerializationConfig.class));
        Mockito.when(serializers.findFormatOverrides(property, Date.class)).thenReturn(null);
        JsonSerializer<Date> result = serializer.createContextual(serializers, property);
        assertEquals(String.class, result.getSerializerProperties().getSerializerBinding().getType());
    }

    @Test
    public void testCreateContextualWithNullSerializer() throws JsonMappingException {
        DateTimeSerializerBase<Date> serializer = new DateTimeSerializerBase<Date>(Date.class, Boolean.FALSE, null);
        SerializerProvider serializers = Mockito.mock(SerializerProvider.class);
        BeanProperty property = Mockito.mock(BeanProperty.class);
        Mockito.when(serializers.findFormatOverrides(property, Date.class)).thenThrow(new JsonMappingException(""));
        assertThrows(JsonMappingException.class, () -> serializer.createContextual(serializers, property));
    }

    
}