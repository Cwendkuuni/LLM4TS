package com.fasterxml.jackson.databind.ser.std;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.TimeZone;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.SerializerProvider;

public class DateTimeSerializerBaseTest {

    private DateTimeSerializerBase<String> serializer;
    private SerializerProvider serializers;
    private DateFormat dateFormat;

    @Before
    public void setUp() {
        serializer = new DateTimeSerializerStub();
        serializers = new CustomSerializerProviderStub();
        dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
    }

    @Test
    public void testWithFormat_WhenCustomFormatIsNull_ThenReturnNewSerializerWithUseTimestampTrueAndNullCustomFormat() {
        DateTimeSerializerBase<String> newSerializer = serializer.withFormat(Boolean.TRUE, null);

        assertTrue(newSerializer._useTimestamp);
        assertNull(newSerializer._customFormat);
    }

    @Test
    public void testWithFormat_WhenCustomFormatIsNotNull_ThenReturnNewSerializerWithUseTimestampFalseAndCustomFormat() {
        DateTimeSerializerBase<String> newSerializer = serializer.withFormat(Boolean.FALSE, dateFormat);

        assertFalse(newSerializer._useTimestamp);
        assertEquals(dateFormat, newSerializer._customFormat);
    }

    @Test
    public void testCreateContextual_WhenPropertyIsNull_ThenReturnSameSerializer() throws JsonMappingException {
        DateTimeSerializerBase<String> newSerializer = (DateTimeSerializerBase<String>) serializer.createContextual(serializers, null);

        assertSame(serializer, newSerializer);
    }

    @Test
    public void testCreateContextual_WhenFormatIsNull_ThenReturnSameSerializer() throws JsonMappingException {
        DateTimeSerializerBase<String> newSerializer = (DateTimeSerializerBase<String>) serializer.createContextual(serializers, new BeanPropertyStub());

        assertSame(serializer, newSerializer);
    }

    @Test
    public void testCreateContextual_WhenShapeIsNumeric_ThenReturnNewSerializerWithUseTimestampTrueAndNullCustomFormat() throws JsonMappingException {
        JsonFormat.Value format = new JsonFormat.Value(JsonFormat.Shape.NUMBER, null, null);

        DateTimeSerializerBase<String> newSerializer = (DateTimeSerializerBase<String>) serializer.createContextual(serializers, new BeanPropertyStub(format));

        assertTrue(newSerializer._useTimestamp);
        assertNull(newSerializer._customFormat);
    }

    @Test
    public void testCreateContextual_WhenShapeIsStringAndFormatHasPatternAndLocaleAndTimeZone_ThenReturnNewSerializerWithUseTimestampFalseAndNewCustomFormat() throws JsonMappingException {
        TimeZone timeZone = TimeZone.getTimeZone("UTC");
        JsonFormat.Value format = new JsonFormat.Value(JsonFormat.Shape.STRING, "yyyy-MM-dd'T'HH:mm:ss.SSSZ", Locale.US, timeZone);

        DateTimeSerializerBase<String> newSerializer = (DateTimeSerializerBase<String>) serializer.createContextual(serializers, new BeanPropertyStub(format));

        assertFalse(newSerializer._useTimestamp);
        assertNotEquals(dateFormat, newSerializer._customFormat);
        assertEquals(format.getTimeZone(), newSerializer._customFormat.getTimeZone());
        assertEquals(format.getPattern(), ((SimpleDateFormat) newSerializer._customFormat).toPattern());
    }

    @Test
    public void testCreateContextual_WhenShapeIsNotEmptyStringAndFormatHasPattern_ThenReturnNewSerializerWithUseTimestampFalseAndNewCustomFormatWithPattern() throws JsonMappingException {
        JsonFormat.Value format = new JsonFormat.Value(JsonFormat.Shape.STRING, "yyyy-MM-dd'T'HH:mm:ss.SSSZ", null, null);

        DateTimeSerializerBase<String> newSerializer = (DateTimeSerializerBase<String>) serializer.createContextual(serializers, new BeanPropertyStub(format));

        assertFalse(newSerializer._useTimestamp);
        assertNull(newSerializer._customFormat.getTimeZone());
        assertEquals(format.getPattern(), ((SimpleDateFormat) newSerializer._customFormat).toPattern());
    }

    @Test
    public void testCreateContextual_WhenShapeIsNotEmptyStringAndFormatHasLocale_ThenReturnNewSerializerWithUseTimestampFalseAndNewCustomFormatWithLocale() throws JsonMappingException {
        Locale locale = Locale.US;
        JsonFormat.Value format = new JsonFormat.Value(JsonFormat.Shape.STRING, null, locale, null);

        DateTimeSerializerBase<String> newSerializer = (DateTimeSerializerBase<String>) serializer.createContextual(serializers, new BeanPropertyStub(format));

        assertFalse(newSerializer._useTimestamp);
        assertNull(newSerializer._customFormat.getTimeZone());
        assertEquals(locale, newSerializer._customFormat.getLocale());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateContextual_WhenShapeIsNotEmptyStringAndFormatDoesNotHaveTimeZoneAndTimeZoneFromSerializerProviderIsNull_ThenThrowIllegalArgumentException() throws JsonMappingException {
        JsonFormat.Value format = new JsonFormat.Value(JsonFormat.Shape.STRING, null, null, null);

        serializer.createContextual(null, new BeanPropertyStub(format));
    }

    @Test
    public void testIsEmpty_WhenValueIsNull_ThenReturnTrue() {
        assertTrue(serializer.isEmpty(null));
    }

    @Test
    public void testIsEmpty_WhenTimestampIsZero_ThenReturnTrue() {
        assertTrue(serializer.isEmpty("1970-01-01T00:00:00.000Z"));
    }

    @Test
    public void testGetSchema_WhenAsTimestampIsTrue_ThenReturnSchemaNodeWithNumberFormat() {
        JsonNode schemaNode = serializer.getSchema(serializers, null);

        assertEquals("number", schemaNode.get("type").asText());
    }

    @Test
    public void testGetSchema_WhenAsTimestampIsFalse_ThenReturnSchemaNodeWithStringFormat() {
        DateTimeSerializerBase<String> newSerializer = serializer.withFormat(Boolean.FALSE, dateFormat);

        JsonNode schemaNode = newSerializer.getSchema(serializers, null);

        assertEquals("string", schemaNode.get("type").asText());
    }

    @Test
    public void testAcceptJsonFormatVisitor_WhenAsNumberIsTrue_ThenVisitIntFormatWithLongNumberTypeAndUtcMillisecondFormat() throws JsonMappingException {
        DateTimeSerializerBase<String> newSerializer = serializer.withFormat(Boolean.TRUE, null);
        JsonFormatVisitorWrapperStub visitor = new JsonFormatVisitorWrapperStub(true);
        JavaType typeHint = new JavaTypeStub();

        newSerializer.acceptJsonFormatVisitor(visitor, typeHint);

        assertEquals(JsonGenerator.NumberType.LONG, visitor.getNumberType());
        assertEquals(JsonValueFormat.UTC_MILLISEC, visitor.getJsonValueFormat());
    }

    @Test
    public void testAcceptJsonFormatVisitor_WhenAsNumberIsFalse_ThenVisitStringFormatWithDateTimeFormat() throws JsonMappingException {
        JsonFormatVisitorWrapperStub visitor = new JsonFormatVisitorWrapperStub(false);
        JavaType typeHint = new JavaTypeStub();

        serializer.acceptJsonFormatVisitor(visitor, typeHint);

        assertNull(visitor.getNumberType());
        assertEquals(JsonValueFormat.DATE_TIME, visitor.getJsonValueFormat());
    }

    @Test
    public void testSerialize() {
        // todo: write test for serialize method
    }
}

class DateTimeSerializerStub extends DateTimeSerializerBase<String> {

    public DateTimeSerializerStub() {
        super(String.class, null, null);
    }

    @Override
    public DateTimeSerializerBase<String> withFormat(Boolean timestamp, DateFormat customFormat) {
        return null;
    }

    @Override
    protected long _timestamp(String value) {
        return 0;
    }

    @Override
    public void serialize(String value, JsonGenerator gen, SerializerProvider serializers) throws IOException {}

}

class CustomSerializerProviderStub extends SerializerProvider {

}

class BeanPropertyStub extends BeanProperty {

    private JsonFormat.Value format;

    public BeanPropertyStub() {
        super(null, null, null, null);
    }

    public BeanPropertyStub(JsonFormat.Value format) {
        super(null, null, null, null);
        this.format = format;
    }

    @Override
    public JsonFormat.Value findPropertyFormat(SerializerProvider provider) {
        return format;
    }
}

class JsonFormatVisitorWrapperStub extends JsonFormatVisitorWrapper.Base {

    private boolean asNumber;
    private JsonGenerator.NumberType numberType;
    private JsonValueFormat jsonValueFormat;

    public JsonFormatVisitorWrapperStub(boolean asNumber) {
        this.asNumber = asNumber;
    }

    @Override
    public JsonObjectFormatVisitor expectObjectFormat(JavaType type) throws JsonMappingException {
        return null;
    }

    @Override
    public JsonArrayFormatVisitor expectArrayFormat(JavaType type) throws JsonMappingException {
        return null;
    }

    @Override
    public JsonStringFormatVisitor expectStringFormat(JavaType type) throws JsonMappingException {
        return null;
    }

    @Override
    public JsonNumberFormatVisitor expectNumberFormat(JavaType type) throws JsonMappingException {
        this.numberType = asNumber ? JsonGenerator.NumberType.LONG : null;
        return null;
    }

    @Override
    public JsonIntegerFormatVisitor expectIntegerFormat(JavaType type) throws JsonMappingException {
        return null;
    }

    @Override
    public JsonBooleanFormatVisitor expectBooleanFormat(JavaType type) throws JsonMappingException {
        return null;
    }

    @Override
    public JsonNullFormatVisitor expectNullFormat(JavaType type) throws JsonMappingException {
        return null;
    }

    @Override
    public JsonAnyFormatVisitor expectAnyFormat(JavaType type) throws JsonMappingException {
        return null;
    }

    public JsonGenerator.NumberType getNumberType() {
        return numberType;
    }

    public JsonValueFormat getJsonValueFormat() {
        return jsonValueFormat;
    }
}

class JavaTypeStub extends JavaType {

    @Override
    public boolean isContainerType() {
        return false;
    }

    @Override
    protected JavaType _narrow(Class<?> subclass) {
        return null;
    }
}