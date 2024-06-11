package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitorWrapper;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import com.fasterxml.jackson.databind.ser.std.DateTimeSerializerBase;
import com.fasterxml.jackson.databind.util.StdDateFormat;

public class DateTimeSerializerBaseTest {
    @SuppressWarnings("unchecked")
    @Test
    public void testDateTimeSerializerBase() {
        // Test default constructor
        DateTimeSerializerBase<Date> dateSerializerBase = new DateTimeSerializerBase<Date>(Date.class, null, null);
        assertNull(dateSerializerBase._useTimestamp);
        assertNull(dateSerializerBase._customFormat);

        // Test constructor with useTimestamp and null customFormat
        DateTimeSerializerBase<Date> dateSerializerBaseWithUseTimestamp = new DateTimeSerializerBase<Date>(Date.class, true, null);
        assertTrue(dateSerializerBaseWithUseTimestamp._useTimestamp.booleanValue());
        assertNull(dateSerializerBaseWithUseTimestamp._customFormat);

        // Test constructor with null useTimestamp and customFormat
        DateTimeSerializerBase<Date> dateSerializerBaseWithCustomFormat = new DateTimeSerializerBase<Date>(Date.class, null, new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"));
        assertNull(dateSerializerBaseWithCustomFormat._useTimestamp);
        assertNotNull(dateSerializerBaseWithCustomFormat._customFormat);

        // Test withFormat method
        DateTimeSerializerBase<Date> dateSerializerBaseWithCustomFormatAndUseTimestamp = dateSerializerBaseWithCustomFormat.withFormat(true, null);
        assertNull(dateSerializerBaseWithCustomFormatAndUseTimestamp._customFormat);
        assertTrue(dateSerializerBaseWithCustomFormatAndUseTimestamp._useTimestamp.booleanValue());

        // Test createContextual method with null property
        JsonSerializer<Date> serializer = dateSerializerBase.createContextual(null, null);
        assertEquals(dateSerializerBase, serializer);

        // Test createContextual method with property and no format overrides
        ObjectPropertyMeta propertyMeta = new ObjectPropertyMeta(Date.class, "propertyName", null);
        JsonSerializer<Date> serializerWithNoFormatOverrides = dateSerializerBase.createContextual(null, propertyMeta);
        assertEquals(dateSerializerBase, serializerWithNoFormatOverrides);

        // Test createContextual method with property and format overrides
        JsonFormat.Value formatOverride = new JsonFormat.Value();
        formatOverride.setShape(JsonFormat.Shape.STRING);
        formatOverride.setLocale(Locale.US);
        JsonSerializer<Date> serializerWithFormatOverrides = dateSerializerBase.createContextual(null, propertyMeta);
        assertNotNull(serializerWithFormatOverrides);
        SimpleDateFormat customFormat = ((DateTimeSerializerBase) serializerWithFormatOverrides)._customFormat;
        assertEquals(Locale.US, customFormat.getLocale());

        // Test _asTimestamp method with null serializers
        assertFalse(dateSerializerBase._asTimestamp(null));

        // Test _asTimestamp method with no _useTimestamp and null _customFormat
        assertFalse(dateSerializerBase._asTimestamp(new Object()));

        // Test _asTimestamp method with _useTimestamp set to true
        assertTrue(dateSerializerBaseWithUseTimestamp._asTimestamp(new Object()));

        // Test _asTimestamp method with _customFormat set to non-null
        SimpleDateFormat customFormatForAsTimestamp = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        assertFalse(dateSerializerBaseWithCustomFormat._asTimestamp(new Object()));
        assertSame(customFormatForAsTimestamp, dateSerializerBaseWithCustomFormat._customFormat);
        assertTrue(dateSerializerBaseWithCustomFormat._asTimestamp(new Object(), new Object()));

        // Test _acceptJsonFormatVisitor method with null visitor, typeHint and asNumber
        JsonFormatVisitorWrapper visitor = null;
        JsonSerializer<Date> serializerWithNullVisitor = dateSerializerBase.createContextual(null, null);
        serializerWithNullVisitor._acceptJsonFormatVisitor(visitor, null, false);
        assertNull(visitor);

        // Test _acceptJsonFormatVisitor method with non-null visitor, typeHint and asNumber = false
        JsonFormat
            visitorWithStringFormat = new JsonDeserializer.None() {
                @Override
                public void acceptJsonFormatVisitor(JsonFormatVisitorWrapper visitor, JavaType typeHint) throws JsonMappingException {
                    visitor.visitStringFormat(typeHint, JsonValueFormat.DATE_TIME);
                }
            };
        JsonSerializer<Date> serializerWithStringFormat = dateSerializerBase.createContextual(null, null);
        serializerWithStringFormat._acceptJsonFormatVisitor(visitorWithStringFormat, null, false);
        assertEquals(JsonValueFormat.DATE_TIME, visitorWithStringFormat.getFormatShape());

        // Test _acceptJsonFormatVisitor method with non-null visitor, typeHint and asNumber = true
        JsonFormat
            visitorWithIntFormat = new JsonDeserializer.None() {
                @Override
                public void acceptJsonFormatVisitor(JsonFormatVisitorWrapper visitor, JavaType typeHint) throws JsonMappingException {
                    visitor.visitIntFormat(typeHint, JsonParser.NumberType.LONG, JsonValueFormat.UTC_MILLISEC);
                }
            };
        JsonSerializer<Date> serializerWithIntFormat = dateSerializerBase.createContextual(null, null);
        serializerWithIntFormat._acceptJsonFormatVisitor(visitorWithIntFormat, null, true);
        assertEquals(JsonParser.NumberType.LONG, visitorWithIntFormat.getNumberType());
        assertEquals(JsonValueFormat.UTC_MILLISEC, visitorWithIntFormat.getFormatShape());
    }

    @Test(expected = IllegalStateException.class)
    public void testCreateContextualWithNullSerializerProvider() throws Exception {
        DateTimeSerializerBase<Date> dateSerializerBase = new DateTimeSerializerBase<Date>(Date.class, null, null);
        dateSerializerBase.createContextual(null, null);
    }

    @Test
    public void testSerialize() throws Exception {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        DateTimeSerializerBase<Date> dateSerializerBase = new DateTimeSerializerBase<Date>(Date.class, null, dateFormat);
        JsonGenerator gen = new JsonFactory().createGenerator(new ByteArrayOutputStream(), JsonEncoding.UTF8);
        Object date = new Date(1463635200000L); // 2016-02-29 12:00:00 UTC
        dateSerializerBase.serialize(date, gen, null);
        assertEquals("2016-02-29T12:00:00.000Z", gen.getOutputContentAsString());
    }
}