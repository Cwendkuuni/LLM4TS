import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.SerializationFeature;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonFormat.Shape;
import com.fasterxml.jackson.databind.JsonFormatVisitorWrapper;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.util.StdDateFormat;
import com.fasterxml.jackson.databind.util.TestBase;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.TimeZone;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

@RunWith(TestBase.Parametrized.class)
public class DateTimeSerializerBaseTest extends TestBase {

    private static final Class<Object> HANDLED_TYPE = Object.class;

    private Boolean useTimestamp;
    private DateFormat customFormat;

    public DateTimeSerializerBaseTest(Boolean useTimestamp, DateFormat customFormat) {
        this.useTimestamp = useTimestamp;
        this.customFormat = customFormat;
    }

    @Test
    public void testCreateContextual() throws Exception {
        SerializerProvider serializers = mock(SerializerProvider.class);
        BeanProperty property = mock(BeanProperty.class);

        DateTimeSerializerBase<Object> serializer = new DateTimeSerializerBase<Object>(HANDLED_TYPE, useTimestamp, customFormat);
        JsonFormat.Value format = Mockito.mock(JsonFormat.Value.class);
        when(format.getShape()).thenReturn(Shape.STRING);
        when(format.hasPattern()).thenReturn(false);
        when(format.hasLocale()).thenReturn(false);
        when(format.hasTimeZone()).thenReturn(null);
        when(serializers.getLocale()).thenReturn(Locale.getDefault());
        when(serializers.getTimeZone()).thenReturn(TimeZone.getDefault());

        JsonFormatVisitorWrapper visitor = mock(JsonFormatVisitorWrapper.class);
        when(visitor.getProvider()).thenReturn(serializers);

        JsonSerializer<Object> result = serializer.createContextual(serializers, property);

        if (useTimestamp != null) {
            assertTrue(result instanceof DateTimeSerializerBase);
            assertTrue(((DateTimeSerializerBase<Object>) result)._useTimestamp.booleanValue());
            assertNull(((DateTimeSerializerBase<Object>) result)._customFormat);
        } else {
            assertFalse(result instanceof DateTimeSerializerBase);
            assertSame(serializer, result);
        }
    }

    @Test
    public void testIsEmpty() throws Exception {
        Object value = null;
        assertTrue(isEmpty(value));

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(0);
        Object valueDate = calendar.getTime();
        assertTrue(isEmpty(valueDate));
    }

    @Test
    public void testGetSchema() throws Exception {
        String expected = useTimestamp ? "number" : "string";
        JsonNode result = new DateTimeSerializerBase<Object>(HANDLED_TYPE, useTimestamp, customFormat).getSchema(null, null);
        assertEquals(expected, result.getClass().getSimpleName());
    }

    @Test
    public void testAcceptJsonFormatVisitor() throws Exception {
        JsonFormatVisitorWrapper visitor = mock(JsonFormatVisitorWrapper.class);
        JavaType typeHint = mock(JavaType.class);

        new DateTimeSerializerBase<Object>(HANDLED_TYPE, useTimestamp, customFormat)
                .acceptJsonFormatVisitor(visitor, typeHint);

        if (useTimestamp) {
            verify(visitor).visitIntFormat(any(JsonFormatVisitorWrapper.class.getType()), any(JavaType.class), eq(JsonParser.NumberType.LONG), eq(JsonValueFormat.UTC_MILLISEC));
        } else {
            verify(visitor).visitStringFormat(any(JsonFormatVisitorWrapper.class.getType()), any(JavaType.class), eq(JsonValueFormat.DATE_TIME));
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSerializeWithNullProvider() throws Exception {
        DateTimeSerializerBase<Object> serializer = new DateTimeSerializerBase<Object>(HANDLED_TYPE, useTimestamp, customFormat);
        serializer.serialize(null, mock(JsonGenerator.class), null);
    }

    @Test
    public void testSerializeAsTimestamp() throws Exception {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss.SSS Z");
        long timestamp = dateFormat.parse("01-Jan-2022 12:34:56.789 +01:00").getTime();
        JsonGenerator gen = mock(JsonGenerator.class);
        SerializerProvider serializers = mock(SerializerProvider.class);
        when(serializers.isEnabled(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)).thenReturn(useTimestamp.booleanValue());

        DateTimeSerializerBase<Object> serializer = new DateTimeSerializerBase<Object>(HANDLED_TYPE, useTimestamp, customFormat);
        serializer.serialize(new Date(timestamp), gen, serializers);

        if (useTimestamp) {
            verify(gen).writeNumber(eq(timestamp));
        } else {
            verifyNoMoreInteractions(gen);
        }
    }

    @Test
    public void testSerializeAsString() throws Exception {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss.SSS Z");
        long timestamp = dateFormat.parse("01-Jan-2022 12:34:56.789 +01:00").getTime();
        JsonGenerator gen = mock(JsonGenerator.class);
        SerializerProvider serializers = mock(SerializerProvider.class);
        when(serializers.isEnabled(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)).thenReturn(false);

        DateTimeSerializerBase<Object> serializer = new DateTimeSerializerBase<Object>(HANDLED_TYPE, useTimestamp, customFormat);
        serializer.serialize(new Date(timestamp), gen, serializers);

        if (customFormat != null) {
            verify(gen).writeString(eq(customFormat.format(new Date(timestamp))));
        } else {
            verifyNoMoreInteractions(gen);
        }
    }

    @Test
    public void testWithFormat() throws Exception {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss.SSS Z");
        DateFormat customFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");

        DateTimeSerializerBase<Object> serializer = new DateTimeSerializerBase<Object>(HANDLED_TYPE, useTimestamp, null);
        DateTimeSerializerBase<Object> result = serializer.withFormat(useTimestamp, customFormat);

        if (useTimestamp != null) {
            assertTrue(result instanceof DateTimeSerializerBase);
            assertTrue(((DateTimeSerializerBase<Object>) result)._useTimestamp.booleanValue());
            assertSame(customFormat, ((DateTimeSerializerBase<Object>) result)._customFormat);
        } else {
            assertFalse(result instanceof DateTimeSerializerBase);
            assertSame(serializer, result);
        }
    }
}