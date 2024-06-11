
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.junit.Before;
import org.junit.Test;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;
import static org.mockito.Mockito.*;

public class DateTimeSerializerBaseTest {

    private DateTimeSerializerBase<Date> serializer;

    @Before
    public void setUp() {
        serializer = new DateTimeSerializerBase<Date>(Date.class, false, null) {
            @Override
            public DateTimeSerializerBase<Date> withFormat(Boolean timestamp, DateFormat customFormat) {
                return null;
            }
            @Override
            protected long _timestamp(Date value) {
                return 0;
            }
            @Override
            public void serialize(Date value, JsonGenerator gen, SerializerProvider serializers) {}
        };
    }

    @Test
    public void testCreateContextual_ShapeIsNumeric_ShouldReturnWithUseTimestampTrue() throws Exception {
        // Arrange
        BeanProperty property = mock(BeanProperty.class);
        Annotated annotated = mock(Annotated.class);
        when(property.getMember()).thenReturn(annotated);
        JsonFormat.Value format = mock(JsonFormat.Value.class);
        when(format.getShape()).thenReturn(JsonFormat.Shape.NUMBER);
        SerializerProvider serializers = mock(SerializerProvider.class);
        when(serializers.getAnnotationIntrospector().findFormat(annotated)).thenReturn(format);

        // Act
        serializer.createContextual(serializers, property);

        // Assert
        DateTimeSerializerBase<Date> expectedSerializer = serializer.withFormat(true, null);
        verify(serializers, times(1)).getAnnotationIntrospector();
        verify(serializers, times(1)).getLocale();
        verify(serializers, times(1)).getTimeZone();
        verifyNoMoreInteractions(serializers);
        assertEquals(expectedSerializer.getClass(), serializer.getClass());
        assertEquals(expectedSerializer._useTimestamp, serializer._useTimestamp);
        assertEquals(expectedSerializer._customFormat, serializer._customFormat);
    }

    @Test
    public void testCreateContextual_ShapeIsString_ShouldReturnWithUseTimestampFalseAndCustomFormat() throws Exception {
        // Arrange
        BeanProperty property = mock(BeanProperty.class);
        Annotated annotated = mock(Annotated.class);
        when(property.getMember()).thenReturn(annotated);
        JsonFormat.Value format = mock(JsonFormat.Value.class);
        when(format.getShape()).thenReturn(JsonFormat.Shape.STRING);
        TimeZone timeZone = TimeZone.getTimeZone("UTC");
        Locale locale = Locale.US;
        String pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSZ";
        when(format.getTimeZone()).thenReturn(timeZone);
        when(format.hasPattern()).thenReturn(true);
        when(format.getPattern()).thenReturn(pattern);
        when(format.hasLocale()).thenReturn(true);
        when(format.getLocale()).thenReturn(locale);
        SerializerProvider serializers = mock(SerializerProvider.class);
        when(serializers.getAnnotationIntrospector().findFormat(annotated)).thenReturn(format);
        when(serializers.getTimeZone()).thenReturn(timeZone);

        // Act
        serializer.createContextual(serializers, property);

        // Assert
        DateTimeSerializerBase<Date> expectedSerializer = serializer.withFormat(false, new SimpleDateFormat(pattern, locale));
        expectedSerializer._customFormat.setTimeZone(timeZone);
        verify(serializers, times(1)).getAnnotationIntrospector();
        verify(serializers, times(1)).getTimeZone();
        verifyNoMoreInteractions(serializers);
        assertEquals(expectedSerializer.getClass(), serializer.getClass());
        assertEquals(expectedSerializer._useTimestamp, serializer._useTimestamp);
        assertEquals(expectedSerializer._customFormat, serializer._customFormat);
    }

    @Test
    public void testCreateContextual_ShapeIsUnknown_ShouldReturnOriginalSerializer() throws Exception {
        // Arrange
        BeanProperty property = mock(BeanProperty.class);
        Annotated annotated = mock(Annotated.class);
        when(property.getMember()).thenReturn(annotated);
        JsonFormat.Value format = mock(JsonFormat.Value.class);
        when(format.getShape()).thenReturn(JsonFormat.Shape.ANY);
        SerializerProvider serializers = mock(SerializerProvider.class);
        when(serializers.getAnnotationIntrospector().findFormat(annotated)).thenReturn(format);

        // Act
        serializer.createContextual(serializers, property);

        // Assert
        verify(serializers, times(1)).getAnnotationIntrospector();
        verifyNoMoreInteractions(serializers);
        assertEquals(serializer.getClass(), DateTimeSerializerBase.class);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateContextual_NullSerializerProvider_ShouldThrowIllegalArgumentException() throws Exception {
        // Arrange
        BeanProperty property = mock(BeanProperty.class);
        when(property.getMember()).thenReturn(null);
        serializer.createContextual(null, property);

        // Act & Assert
        // IllegalArgumentException expected
    }

    @Test
    public void testIsEmpty_NullValue_ShouldReturnTrue() throws Exception {
        // Arrange
        Date value = null;

        // Act
        boolean result = serializer.isEmpty(value);

        // Assert
        assertTrue(result);
    }

    @Test
    public void testIsEmpty_NotNullValue_ZeroTimestamp_ShouldReturnTrue() throws Exception {
        // Arrange
        Date value = new Date(0);

        // Act
        boolean result = serializer.isEmpty(value);

        // Assert
        assertTrue(result);
    }

    @Test
    public void testIsEmpty_NotNullValue_NonZeroTimestamp_ShouldReturnFalse() throws Exception {
        // Arrange
        Date value = new Date();

        // Act
        boolean result = serializer.isEmpty(value);

        // Assert
        assertFalse(result);
    }

    @Test
    public void testIsEmpty_NullSerializerProvider_NullValue_ShouldReturnTrue() throws Exception {
        // Arrange
        SerializerProvider serializers = null;
        Date value = null;

        // Act
        boolean result = serializer.isEmpty(serializers, value);

        // Assert
        assertTrue(result);
    }

    @Test
    public void testIsEmpty_NullSerializerProvider_NotNullValue_ZeroTimestamp_ShouldReturnTrue() throws Exception {
        // Arrange
        SerializerProvider serializers = null;
        Date value = new Date(0);

        // Act
        boolean result = serializer.isEmpty(serializers, value);

        // Assert
        assertTrue(result);
    }

    @Test
    public void testIsEmpty_NullSerializerProvider_NotNullValue_NonZeroTimestamp_ShouldReturnFalse() throws Exception {
        // Arrange
        SerializerProvider serializers = null;
        Date value = new Date();

        // Act
        boolean result = serializer.isEmpty(serializers, value);

        // Assert
        assertFalse(result);
    }

    @Test
    public void testGetSchema_UseTimestampTrue_ShouldReturnNumberSchemaNode() throws Exception {
        // Arrange
        SerializerProvider serializers = mock(SerializerProvider.class);
        when(serializers.isEnabled(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)).thenReturn(true);
        ObjectMapper mapper = new ObjectMapper();
        DateFormat dateFormat = mapper.getDateFormat();

        // Act
        JsonNode schema = serializer.getSchema(serializers, Date.class);

        // Assert
        assertTrue(schema.isObject());
        assertTrue(schema.get("type").isTextual());
        assertTrue(schema.get("type").textValue().equals("number"));
        assertTrue(schema.get("format").isTextual());
        assertTrue(schema.get("format").textValue().equals("utc-millisec"));
    }

    @Test
    public void testGetSchema_UseTimestampFalse_ShouldReturnStringSchemaNode() throws Exception {
        // Arrange
        SerializerProvider serializers = mock(SerializerProvider.class);
        when(serializers.isEnabled(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)).thenReturn(false);
        ObjectMapper mapper = new ObjectMapper();
        DateFormat dateFormat = mapper.getDateFormat();

        // Act
        JsonNode schema = serializer.getSchema(serializers, Date.class);

        // Assert
        assertTrue(schema.isObject());
        assertTrue(schema.get("type").isTextual());
        assertTrue(schema.get("type").textValue().equals("string"));
        assertTrue(schema.get("format").isTextual());
        assertTrue(schema.get("format").textValue().equals("date-time"));
    }

    @Test
    public void testAcceptJsonFormatVisitor_UseTimestampTrue_ShouldVisitIntFormat() throws Exception {
        // Arrange
        JsonFormatVisitorWrapper visitor = mock(JsonFormatVisitorWrapper.class);
        JavaType typeHint = mock(JavaType.class);
        SerializerProvider provider = mock(SerializerProvider.class);
        when(serializer._asTimestamp(provider)).thenReturn(true);

        // Act
        serializer.acceptJsonFormatVisitor(visitor, typeHint);

        // Assert
        verify(visitor, times(1)).expectIntFormat(typeHint, JsonParser.NumberType.LONG);
        verify(visitor, times(1)).getProvider();
        verifyNoMoreInteractions(visitor);
    }

    @Test
    public void testAcceptJsonFormatVisitor_UseTimestampFalse_ShouldVisitStringFormat() throws Exception {
        // Arrange
        JsonFormatVisitorWrapper visitor = mock(JsonFormatVisitorWrapper.class);
        JavaType typeHint = mock(JavaType.class);
        SerializerProvider provider = mock(SerializerProvider.class);
        when(serializer._asTimestamp(provider)).thenReturn(false);

        // Act
        serializer.acceptJsonFormatVisitor(visitor, typeHint);

        // Assert
        verify(visitor, times(1)).expectStringFormat(typeHint);
        verify(visitor, times(1)).getProvider();
        verifyNoMoreInteractions(visitor);
    }

    // Additional test cases can be added here for other methods in DateTimeSerializerBase class

}

