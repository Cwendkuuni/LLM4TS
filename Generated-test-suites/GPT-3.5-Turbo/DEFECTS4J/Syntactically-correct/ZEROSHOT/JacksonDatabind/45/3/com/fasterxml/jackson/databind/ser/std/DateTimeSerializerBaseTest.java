import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.JsonSerializationContext;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.junit.Before;
import org.junit.Test;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.TimeZone;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

public class DateTimeSerializerBaseTest {

    // Mocks
    private SerializerProvider serializers;
    private BeanProperty property;
    private JsonFormat.Value format;
    private JsonFormat.Shape shape;
    private TimeZone timeZone;
    private SimpleDateFormat simpleDateFormat;
    private JsonFormatVisitorWrapper visitor;
    private JavaType typeHint;

    // Test objects
    private DateTimeSerializerBase<Object> dateTimeSerializerBase;

    @Before
    public void setup() {
        serializers = mock(SerializerProvider.class);
        property = mock(BeanProperty.class);
        format = mock(JsonFormat.Value.class);
        shape = mock(JsonFormat.Shape.class);
        timeZone = mock(TimeZone.class);
        simpleDateFormat = mock(SimpleDateFormat.class);
        visitor = mock(JsonFormatVisitorWrapper.class);
        typeHint = mock(JavaType.class);

        dateTimeSerializerBase = new DateTimeSerializerBase<Object>(Object.class, true, null) {
            @Override
            public DateTimeSerializerBase<Object> withFormat(Boolean timestamp, DateFormat customFormat) {
                return null;
            }

            @Override
            protected long _timestamp(Object value) {
                return 0;
            }

            @Override
            public void serialize(Object value, JsonGenerator gen, SerializerProvider serializers) {

            }
        };
    }

    @Test
    public void testCreateContextual() throws JsonMappingException {
        when(format.getShape()).thenReturn(shape);
        when(shape.isNumeric()).thenReturn(true);

        DateTimeSerializerBase<Object> result = dateTimeSerializerBase.createContextual(serializers, property);

        assertEquals(Boolean.TRUE, result._useTimestamp);
        assertEquals(null, result._customFormat);
    }

    @Test
    public void testCreateContextual_StringShape() throws JsonMappingException {
        when(format.getShape()).thenReturn(shape);
        when(shape.isNumeric()).thenReturn(false);
        when(format.getShape()).thenReturn(JsonFormat.Shape.STRING);
        when(format.getTimeZone()).thenReturn(timeZone);
        when(format.hasPattern()).thenReturn(true);
        when(format.getPattern()).thenReturn("yyyy-MM-dd");
        when(format.hasLocale()).thenReturn(true);
        when(format.getLocale()).thenReturn(Locale.US);
        when(serializers.getTimeZone()).thenReturn(timeZone);

        DateTimeSerializerBase<Object> result = dateTimeSerializerBase.createContextual(serializers, property);

        assertEquals(Boolean.FALSE, result._useTimestamp);
        assertEquals(simpleDateFormat, result._customFormat);
    }

    @Test
    public void testCreateContextual_NoFormat() throws JsonMappingException {
        DateTimeSerializerBase<Object> result = dateTimeSerializerBase.createContextual(serializers, property);

        assertEquals(null, result._useTimestamp);
        assertEquals(null, result._customFormat);
    }

    @Test
    public void testIsEmpty_WithValue() {
        Object value = new Object();

        boolean result = dateTimeSerializerBase.isEmpty(serializers, value);

        assertEquals(false, result);
    }

    @Test
    public void testIsEmpty_WithNullValue() {
        Object value = null;

        boolean result = dateTimeSerializerBase.isEmpty(serializers, value);

        assertEquals(true, result);
    }

    @Test
    public void testIsEmpty_WithZeroTimestamp() {
        Object value = new Object() {
            @Override
            protected long _timestamp(Object value) {
                return 0;
            }
        };

        boolean result = dateTimeSerializerBase.isEmpty(serializers, value);

        assertEquals(true, result);
    }

    @Test
    public void testGetSchema_AsTimestamp() {
        JsonNodeFactory nodeFactory = JsonNodeFactory.instance;
        JsonNode expectedSchemaNode = nodeFactory.objectNode();

        when(serializers.isEnabled(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)).thenReturn(true);
        ObjectNode result = (ObjectNode) dateTimeSerializerBase.getSchema(serializers, null);

        assertEquals(expectedSchemaNode, result);
    }

    @Test
    public void testGetSchema_AsString() {
        JsonNodeFactory nodeFactory = JsonNodeFactory.instance;
        JsonNode expectedSchemaNode = nodeFactory.objectNode();

        when(format.hasPattern()).thenReturn(true);
        when(format.getPattern()).thenReturn("yyyy-MM-dd");
        when(format.hasLocale()).thenReturn(true);
        when(format.getLocale()).thenReturn(Locale.US);
        when(serializers.getLocale()).thenReturn(Locale.US);
        ObjectNode result = (ObjectNode) dateTimeSerializerBase.getSchema(serializers, null);

        assertEquals(expectedSchemaNode, result);
    }

    @Test
    public void testAcceptJsonFormatVisitor_AsNumber() throws JsonMappingException {
        dateTimeSerializerBase.acceptJsonFormatVisitor(visitor, typeHint);

        verify(visitor).getProvider();
    }

    public void testAcceptJsonFormatVisitor_AsString() throws JsonMappingException {
        dateTimeSerializerBase.acceptJsonFormatVisitor(visitor, typeHint);

        verify(visitor).getProvider();
    }
}