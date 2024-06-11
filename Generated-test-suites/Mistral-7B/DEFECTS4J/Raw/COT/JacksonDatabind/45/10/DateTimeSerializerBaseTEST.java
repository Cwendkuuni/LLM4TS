package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.*;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import org.joda.time.DateTime;
import org.joda.time.format.ISODateTimeFormat;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.type.TypeFactory;

import com.fasterxml.jackson.databind.ser.std.DateTimeSerializerBase;
import com.fasterxml.jackson.databind.ser.std.DateTimeSerializerBase.JsonFormatShape;

@RunWith(MockitoJUnitRunner.class)
public class DateTimeSerializerBaseTest {

    @SuppressWarnings("unchecked")
    @Mock
    private SerializerProvider serializers;

    @Mock
    private JsonParser parser;

    @Mock
    private JsonGenerator gen;

    @Mock
    private ObjectMapper mapper;

    @InjectMocks
    private DateTimeSerializerBase<DateTime> dateTimeSerializer;

    @Test
    public void testDefaultConstructor() {
        DateTimeSerializerBase<DateTime> serializer = new DateTimeSerializerBase<>();
        assertNull(serializer._useTimestamp);
        assertNull(serializer._customFormat);
    }

    @Test
    public void testConstructorWithUseTimestampAndNullFormat() {
        JsonFormatShape shape = JsonFormat.Shape.NUMBER;
        DateTimeSerializerBase<DateTime> serializer = new DateTimeSerializerBase<>(DateTime.class, true, null);
        assertEquals(true, serializer._useTimestamp);
        assertNull(serializer._customFormat);
    }

    @Test
    public void testConstructorWithNullUseTimestampAndFormat() {
        DateTimeSerializerBase<DateTime> serializer = new DateTimeSerializerBase<>(DateTime.class, null, null);
        assertNull(serializer._useTimestamp);
        assertNull(serializer._customFormat);
    }

    @Test
    public void testConstructorWithUseTimestampAndFormat() {
        JsonFormatShape shape = JsonFormat.Shape.STRING;
        DateFormat customFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        DateTimeSerializerBase<DateTime> serializer = new DateTimeSerializerBase<>(DateTime.class, true, customFormat);
        assertEquals(true, serializer._useTimestamp);
        assertEquals(customFormat, serializer._customFormat);
    }

    @Test
    public void testWithFormat_useTimestampTrue() throws JsonProcessingException {
        DateFormat customFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        DateTimeSerializerBase<DateTime> serializer = new DateTimeSerializerBase<>(DateTime.class, true, null);
        DateTimeSerializerBase<DateTime> result = serializer.withFormat(Boolean.TRUE, customFormat);
        assertEquals(serializer, result);
        assertEquals(true, result._useTimestamp);
        assertNull(result._customFormat);
    }

    @Test
    public void testWithFormat_useTimestampFalse() throws JsonProcessingException {
        DateFormat customFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        DateTimeSerializerBase<DateTime> serializer = new DateTimeSerializerBase<>(DateTime.class, false, null);
        DateTimeSerializerBase<DateTime> result = serializer.withFormat(Boolean.FALSE, customFormat);
        assertEquals(serializer, result);
        assertEquals(false, result._useTimestamp);
        assertEquals(customFormat, result._customFormat);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testWithFormat_NullProvider() throws JsonProcessingException {
        DateTimeSerializerBase<DateTime> serializer = new DateTimeSerializerBase<>(DateTime.class, false, null);
        serializer.withFormat(null, null);
    }

    @Test
    public void testIsEmpty_NullValue() {
        assertTrue(dateTimeSerializer.isEmpty(null));
    }

    @Test
    public void testIsEmpty_ZeroTimestamp() {
        DateTime value = new DateTime();
        value.setMillis(-1);
        assertTrue(dateTimeSerializer.isEmpty(value));
    }

    @Test
    public void testIsEmpty_NonEmptyValue() {
        DateTime value = new DateTime();
        value.setMillis(System.currentTimeMillis());
        assertFalse(dateTimeSerializer.isEmpty(value));
    }

    @Test
    public void testGetSchema() throws JsonMappingException {
        String expectedSchema = "number";
        SerializerProvider serializers = Mockito.mock(SerializerProvider.class);
        Mockito.when(serializers.isEnabled(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)).thenReturn(true);
        assertEquals(expectedSchema, dateTimeSerializer.getSchema(serializers, DateTime.class.getType()));

        String expectedSchemaString = "string";
        Mockito.when(serializers.isEnabled(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)).thenReturn(false);
        assertEquals(expectedSchemaString, dateTimeSerializer.getSchema(serializers, DateTime.class.getType()));
    }

    @Test
    public void testAcceptJsonFormatVisitor() throws JsonMappingException {
        JsonFormatVisitorWrapper visitor = Mockito.mock(JsonFormatVisitorWrapper.class);
        JavaType typeHint = TypeFactory.defaultInstance().constructType(DateTime.class);
        boolean asNumber = true;
        _acceptJsonFormatVisitorTest(visitor, typeHint, asNumber);

        asNumber = false;
        _acceptJsonFormatVisitorTest(visitor, typeHint, asNumber);
    }

    private void _acceptJsonFormatVisitorTest(JsonFormatVisitorWrapper visitor, JavaType typeHint, boolean asNumber)
            throws JsonMappingException {
        Mockito.when(visitor.getProvider()).thenReturn(serializers);
        dateTimeSerializer._acceptJsonFormatVisitor(visitor, typeHint, asNumber);

        if (asNumber) {
            Mockito.verify(visitor).visitIntFormat(Mockito.eq(visitor), Mockito.eq(typeHint),
                    Mockito.eq(JsonParser.NumberType.LONG), Mockito.eq(JsonValueFormat.UTC_MILLISEC));
        } else {
            Mockito.verify(visitor).visitStringFormat(Mockito.eq(visitor), Mockito.eq(typeHint),
                    Mockito.eq(JsonValueFormat.DATE_TIME));
        }
    }

    @Test
    public void testSerialize() throws JsonProcessingException, IOException {
        DateTime value = new DateTime();
        DateTimeSerializerBase<DateTime> serializer = new DateTimeSerializerBase<>(DateTime.class, false, null);

        // serialize as string
        String expectedString = ISODateTimeFormat.dateTimeParser().print(value);
        byte[] actualBytes = new byte[0];
        StringWriter stringWriter = new StringWriter();
        JsonGenerator jsonGenerator = new JsonFactory(mapper.getConfig()).createGenerator(stringWriter);
        serializer.serialize(value, jsonGenerator, serializers);
        jsonGenerator.flush();
        actualBytes = stringWriter.toString().getBytes();
        assertEquals(expectedString, new String(actualBytes));

        // serialize as number
        JsonGenerator jsonGenerator2 = new JsonFactory(mapper.getConfig()).createGenerator(mapper.getOutputFactory());
        jsonGenerator2.writeNumberValue(value.getMillis());
        byte[] actualBytes2 = jsonGenerator2.getOutputFactory().getBufferWriter().toString().getBytes();
        assertEquals(expectedString, new String(actualBytes2));
    }

    @Test(expected = JsonProcessingException.class)
    public void testSerialize_NullValue() throws JsonProcessingException, IOException {
        DateTimeSerializerBase<DateTime> serializer = new DateTimeSerializerBase<>(DateTime.class, false, null);
        serializer.serialize(null, gen, serializers);
    }

    @Test(expected = JsonMappingException.class)
    public void testCreateContextual_NullProperty() throws JsonMappingException {
        DateTimeSerializerBase<DateTime> serializer = new DateTimeSerializerBase<>(DateTime.class, false, null);
        serializer.createContextual(serializers, null);
    }

    @Test
    public void testCreateContextual_JsonFormatAnnotationPresent() throws JsonMappingException {
        JsonFormat jsonFormatAnnotation = new JsonFormat();
        DateTime value = new DateTime();
        Annotated property = Mockito.mock(Annotated.class);
        Mockito.when(property.getMember()).thenReturn(value);
        Mockito.when(property.getValue()).thenReturn(value);
        Mockito.when(serializers.getAnnotationIntrospector().findFormat(property)).thenReturn(jsonFormatAnnotation);

        DateTimeSerializerBase<DateTime> serializer = new DateTimeSerializerBase<>(DateTime.class, false, null);
        JsonSerializer<?> result = serializer.createContextual(serializers, property);

        SimpleDateFormat expectedCustomFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        assertTrue(result instanceof DateTimeSerializerBase);
        DateTimeSerializerBase<DateTime> castedResult = (DateTimeSerializerBase<DateTime>) result;
        assertEquals(expectedCustomFormat, castedResult._customFormat);
    }

    @Test
    public void testCreateContextual_JsonFormatAnnotationAbsent() throws JsonMappingException {
        DateTime value = new DateTime();
        Annotated property = Mockito.mock(Annotated.class);
        Mockito.when(property.getMember()).thenReturn(value);
        Mockito.when(property.getValue()).thenReturn(value);
        Mockito.when(serializers.getAnnotationIntrospector().findFormat(property)).thenReturn(null);

        DateTimeSerializerBase<DateTime> serializer = new DateTimeSerializerBase<>(DateTime.class, false, null);
        JsonSerializer<?> result = serializer.createContextual(serializers, property);

        assertNull(result._customFormat);
    }
}