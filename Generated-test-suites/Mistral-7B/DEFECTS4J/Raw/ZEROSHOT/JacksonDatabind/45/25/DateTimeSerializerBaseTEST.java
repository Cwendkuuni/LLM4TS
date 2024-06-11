import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.introspect.Annotated;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitorWrapper;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import com.fasterxml.jackson.databind.util.StdDateFormat;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class DateTimeSerializerBaseTest {

    private DateTimeSerializerBase<Long> dateTimeSerializer;
    private SerializerProvider serializerProvider;
    private JsonGenerator jsonGenerator;
    private JsonParser jsonParser;

    @Before
    public void setup() {
        serializerProvider = mock(SerializerProvider.class);
        jsonGenerator = mock(JsonGenerator.class);
        jsonParser = mock(JsonParser.class);
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setSerializerProvider(serializerProvider);
        dateTimeSerializer = new StdDateSerializer(Long.TYPE, false, null);
    }

    @Test
    public void testCreateContextual_whenFormatIsNumeric_shouldReturnNumericSerializer() throws Exception {
        JsonFormat jsonFormat = Mockito.mock(JsonFormat.class);
        Mockito.when(serializerProvider.getAnnotationIntrospector()).thenReturn(Mockito.mock(AnnotationIntrospector.class));
        Mockito.when(serializerProvider.getAnnotationIntrospector().findFormat(any(Annotated.class))).thenReturn(jsonFormat);
        Mockito.when(jsonFormat.getShape()).thenReturn(JsonFormat.Shape.NUMBER);

        JsonSerializer<?> serializer = dateTimeSerializer.createContextual(serializerProvider, null);

        verify(serializerProvider).findValueSerializer(Long.TYPE, serializer.getClass());
        assertEquals(dateTimeSerializer.getClass(), serializer.getClass());
    }

    @Test
    public void testCreateContextual_whenFormatIsString_shouldReturnStringSerializerWithFormat() throws Exception {
        JsonFormat jsonFormat = Mockito.mock(JsonFormat.class);
        Mockito.when(serializerProvider.getAnnotationIntrospector()).thenReturn(Mockito.mock(AnnotationIntrospector.class));
        Mockito.when(serializerProvider.getAnnotationIntrospector().findFormat(any(Annotated.class))).thenReturn(jsonFormat);
        Mockito.when(jsonFormat.getShape()).thenReturn(JsonFormat.Shape.STRING);
        TimeZone timeZone = Mockito.mock(TimeZone.class);
        String pattern = "dd-MM-yyyy HH:mm:ss z";
        Locale locale = Locale.ENGLISH;
        SimpleDateFormat dateFormat = new SimpleDateFormat(pattern, locale);
        dateFormat.setTimeZone(timeZone);
        Mockito.when(jsonFormat.getPattern()).thenReturn(pattern);
        Mockito.when(jsonFormat.getLocale()).thenReturn(locale);
        Mockito.when(jsonFormat.getTimeZone()).thenReturn(timeZone);

        JsonSerializer<?> serializer = dateTimeSerializer.createContextual(serializerProvider, null);

        JsonFormatVisitorWrapper jsonFormatVisitorWrapper = Mockito.mock(JsonFormatVisitorWrapper.class);
        dateTimeSerializer._acceptJsonFormatVisitor(jsonFormatVisitorWrapper, JsonTypeFactory.valueType(Long.TYPE), false);

        verify(jsonFormatVisitorWrapper).visitStringFormat(any(JsonFormatVisitorWrapper.class), any(JsonTypeReference.class), eq(JsonValueFormat.DATE_TIME));
        assertEquals(dateTimeSerializer.getClass(), serializer.getClass());
    }

    @Test
    public void testSerialize_whenUseTimestampIsTrue_shouldSerializeAsNumeric() throws Exception {
        Long value = 1L;

        Mockito.when(serializerProvider.findValueSerializer(Long.TYPE, JsonSerializer.Type.NUMBER)).thenReturn(Mockito.mock(JsonSerializer.NumberSerializer.class));

        dateTimeSerializer.serialize(value, jsonGenerator, serializerProvider);

        verify(jsonGenerator).writeNumber(value.longValue());
    }

    @Test
    public void testSerialize_whenUseTimestampIsFalseAndCustomFormatIsProvided_shouldSerializeAsString() throws Exception {
        Long value = 1L;
        TimeZone timeZone = Mockito.mock(TimeZone.class);
        String pattern = "dd-MM-yyyy HH:mm:ss z";
        Locale locale = Locale.ENGLISH;
        SimpleDateFormat dateFormat = new SimpleDateFormat(pattern, locale);
        dateFormat.setTimeZone(timeZone);

        jsonGenerator.setDateFormat(dateFormat);

        dateTimeSerializer.serialize(value, jsonGenerator, serializerProvider);

        verify(jsonGenerator).writeStringValue(dateFormat.format(value));
    }

    @Test
    public void testIsEmpty_whenValueIsNotNull_shouldReturnFalse() throws Exception {
        Long value = 1L;

        boolean result = dateTimeSerializer.isEmpty(value);

        assertEquals(false, result);
    }

    @Test
    public void testIsEmpty_whenValueIsNull_shouldReturnTrue() throws Exception {
        boolean result = dateTimeSerializer.isEmpty(null);

        assertEquals(true, result);
    }

    @Test
    public void testIsEmpty_whenValueIsZero_shouldReturnTrue() throws Exception {
        boolean result = dateTimeSerializer.isEmpty(0L);

        assertEquals(true, result);
    }

    @Test
    public void testGetSchema_shouldReturnNumberSchemaIfUseTimestampIsTrue() throws Exception {
        JsonNode schema = dateTimeSerializer.getSchema(serializerProvider, Long.TYPE);

        assertEquals("number", schema.getClass().getSimpleName());
    }

    @Test
    public void testGetSchema_shouldReturnStringSchemaIfUseTimestampIsFalse() throws Exception {
        JsonFormat jsonFormat = Mockito.mock(JsonFormat.class);
        Mockito.when(serializerProvider.getAnnotationIntrospector()).thenReturn(Mockito.mock(AnnotationIntrospector.class));
        Mockito.when(serializerProvider.getAnnotationIntrospector().findFormat(any(Annotated.class))).thenReturn(jsonFormat);
        Mockito.when(jsonFormat.getShape()).thenReturn(JsonFormat.Shape.STRING);

        JsonNode schema = dateTimeSerializer.getSchema(serializerProvider, Long.TYPE);

        assertEquals("string", schema.getClass().getSimpleName());
    }
}