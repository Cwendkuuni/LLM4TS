import static org.junit.Assert.*;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.introspect.Annotated;

public class DateTimeSerializerBaseTest {

    @Spy
    private DateTimeSerializerBase<Date> dateTimeSerializerBase;

    @Mock
    private JsonSerializer<?> jsonSerializer;

    @Mock
    private SerializerProvider serializerProvider;

    @Test
    public void testIsEmpty_NullDate_ReturnsTrue() {
        assertTrue(dateTimeSerializerBase.isEmpty((Date) null));
    }

    @Test
    public void testIsEmpty_NullValue_ReturnsTrue() {
        assertFalse(dateTimeSerializerBase.isEmpty(serializerProvider, (Date) null));
    }

    @Test
    public void testGetSchema_AsTimestamp_ReturnsNumberSchemaNode() {
        JsonNode schema = dateTimeSerializerBase.getSchema(serializerProvider, Date.class);

        assertEquals("number", schema.get("type").asText());
        assertTrue(schema.get("required").asBoolean());
    }

    @Test
    public void testGetSchema_AsString_ReturnsStringSchemaNode() {
        JsonFormat.Value format = new JsonFormat.Value();
        format = format.withShape(JsonFormat.Shape.STRING);

        when(serializerProvider.getAnnotationIntrospector().findFormat((Annotated) null)).thenReturn(format);

        JsonNode schema = dateTimeSerializerBase.getSchema(serializerProvider, Date.class);

        assertEquals("string", schema.get("type").asText());
        assertTrue(schema.get("required").asBoolean());
    }

    @Test
    public void testSerialize_AsTimestamp_WritesDateAsNumericTimestamp() throws IOException {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));

        Date date = dateFormat.parse("2020-01-01 00:00:00");

        dateTimeSerializerBase.withFormat(true, null);
        dateTimeSerializerBase.serialize(date, jsonGenerator, serializerProvider);

        verify(jsonGenerator).writeNumber(date.getTime());
    }

    @Test
    public void testSerialize_AsString_WithDefaultPattern_WritesDateAsStringWithIso8601Pattern() throws IOException {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));

        Date date = dateFormat.parse("2020-01-01 00:00:00");

        JsonFormat.Value format = new JsonFormat.Value();
        format = format.withShape(JsonFormat.Shape.STRING);

        when(serializerProvider.getAnnotationIntrospector().findFormat((Annotated) null)).thenReturn(format);
        when(serializerProvider.getTimeZone()).thenReturn(TimeZone.getTimeZone("UTC"));

        dateTimeSerializerBase.withFormat(null, null);
        dateTimeSerializerBase.serialize(date, jsonGenerator, serializerProvider);

        verify(jsonGenerator).writeString("2020-01-01T00:00:00Z");
    }

    @Test
    public void testSerialize_AsString_WithCustomPatternAndTimeZone_WritesDateAsStringWithCustomPatternAndTimeZone() throws IOException {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));

        Date date = dateFormat.parse("2020-01-01 00:00:00");

        JsonFormat.Value format = new JsonFormat.Value();
        format = format.withShape(JsonFormat.Shape.STRING);
        format = format.withPattern("yyyy/MM/dd HH:mm:ss");
        format = format.withLocale(Locale.US);
        format = format.withTimeZone(TimeZone.getTimeZone("EST"));

        when(serializerProvider.getAnnotationIntrospector().findFormat((Annotated) null)).thenReturn(format);
        when(serializerProvider.getTimeZone()).thenReturn(TimeZone.getTimeZone("UTC"));

        dateTimeSerializerBase.withFormat(null, null);
        dateTimeSerializerBase.serialize(date, jsonGenerator, serializerProvider);

        verify(jsonGenerator).writeString("2019/12/31 19:00:00");
    }

    @Test
    public void testAcceptJsonFormatVisitor_AsTimestamp_VisitsIntFormatWithNumberTypeAndUtcMillisecFormat() throws JsonMappingException {
        JsonFormatVisitorWrapper visitor = mock(JsonFormatVisitorWrapper.class);
        JavaType javaType = mock(JavaType.class);

        dateTimeSerializerBase.acceptJsonFormatVisitor(visitor, javaType);

        verify(visitor).visitIntFormat(eq(javaType), eq(JsonParser.NumberType.LONG), eq(JsonValueFormat.UTC_MILLISEC));
    }

    @Test
    public void testAcceptJsonFormatVisitor_AsString_VisitsStringFormatWithDateTimeFormat() throws JsonMappingException {
        JsonFormatVisitorWrapper visitor = mock(JsonFormatVisitorWrapper.class);
        JavaType javaType = mock(JavaType.class);

        JsonFormat.Value format = new JsonFormat.Value();
        format = format.withShape(JsonFormat.Shape.STRING);

        when(serializerProvider.getAnnotationIntrospector().findFormat((Annotated) null)).thenReturn(format);

        dateTimeSerializerBase.acceptJsonFormatVisitor(visitor, javaType);

        verify(visitor).visitStringFormat(eq(javaType), eq(JsonValueFormat.DATE_TIME));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAsTimestamp_NullSerializerProvider_ThrowsIllegalArgumentException() {
        dateTimeSerializerBase._asTimestamp(null);
    }

}