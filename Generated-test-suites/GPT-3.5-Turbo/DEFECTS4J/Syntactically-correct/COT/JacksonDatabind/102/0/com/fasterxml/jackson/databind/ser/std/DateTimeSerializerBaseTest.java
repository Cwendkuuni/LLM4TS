import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;
import java.util.concurrent.atomic.AtomicReference;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitorWrapper;
import com.fasterxml.jackson.databind.ser.*;
import com.fasterxml.jackson.databind.ser.std.*;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

public class DateTimeSerializerBaseTest {

    private DateTimeSerializerBase<ClassA> dateTimeSerializerBase;

    @Before
    public void setUp() {
        dateTimeSerializerBase = new DateTimeSerializerBase<ClassA>(ClassA.class, false, null) {
            @Override
            public DateTimeSerializerBase<ClassA> withFormat(Boolean timestamp, DateFormat customFormat) {
                return null;
            }

            @Override
            public boolean isEmpty(SerializerProvider serializers, ClassA value) {
                return false;
            }

            @Override
            protected long _timestamp(ClassA value) {
                return 0;
            }

            @Override
            public void serialize(ClassA value, JsonGenerator gen, SerializerProvider serializers) {
            }

            @Override
            protected void _acceptJsonFormatVisitor(JsonFormatVisitorWrapper visitor, JavaType typeHint, boolean asNumber)
                    throws JsonMappingException {
            }

            @Override
            protected void _serializeAsString(Date value, JsonGenerator g, SerializerProvider provider) throws Exception {
            }
        };
    }

    @Test
    public void testConstructor() {
        assertEquals(ClassA.class, dateTimeSerializerBase.handledType());
        assertFalse(dateTimeSerializerBase._useTimestamp);
        assertNull(dateTimeSerializerBase._customFormat);
        assertNull(dateTimeSerializerBase._reusedCustomFormat);
    }

    @Test
    public void testWithFormat() {
        DateTimeSerializerBase<ClassA> result = dateTimeSerializerBase.withFormat(false, new SimpleDateFormat());
        assertNull(result._useTimestamp);
        assertNotNull(result._customFormat);
        assertNull(result._reusedCustomFormat);
    }

    @Test
    public void testCreateContextualWithoutProperty() throws JsonMappingException {
        SerializerProvider serializerProvider = mock(SerializerProvider.class);
        BeanProperty property = null;
        JsonSerializer<?> result = dateTimeSerializerBase.createContextual(serializerProvider, property);
        assertEquals(dateTimeSerializerBase, result);
    }

    @Test
    public void testCreateContextualWithNumericShape() throws JsonMappingException {
        JsonFormat.Value format = mock(JsonFormat.Value.class);
        when(format.getShape()).thenReturn(JsonFormat.Shape.NUMBER);
        SerializerProvider serializerProvider = mock(SerializerProvider.class);
        BeanProperty property = mock(BeanProperty.class);
        when(dateTimeSerializerBase.findFormatOverrides(serializerProvider, property, dateTimeSerializerBase.handledType())).thenReturn(format);
        DateTimeSerializerBase<ClassA> result = (DateTimeSerializerBase<ClassA>) dateTimeSerializerBase.createContextual(serializerProvider, property);
        assertTrue(result._useTimestamp);
        assertNull(result._customFormat);
        assertNull(result._reusedCustomFormat);
    }

    @Test
    public void testCreateContextualWithCustomPattern() throws JsonMappingException {
        JsonFormat.Value format = mock(JsonFormat.Value.class);
        when(format.getShape()).thenReturn(JsonFormat.Shape.STRING);
        when(format.hasPattern()).thenReturn(true);
        when(format.getPattern()).thenReturn("dd-MM-yyyy");
        when(format.hasTimeZone()).thenReturn(false);
        SerializerProvider serializerProvider = mock(SerializerProvider.class);
        BeanProperty property = mock(BeanProperty.class);
        when(dateTimeSerializerBase.findFormatOverrides(serializerProvider, property, dateTimeSerializerBase.handledType())).thenReturn(format);
        DateTimeSerializerBase<ClassA> result = (DateTimeSerializerBase<ClassA>) dateTimeSerializerBase.createContextual(serializerProvider, property);
        assertFalse(result._useTimestamp);
        assertNotNull(result._customFormat);
        assertNull(result._reusedCustomFormat);
    }

    @Test
    public void testCreateContextualWithLocalAndTimeZone() throws JsonMappingException {
        JsonFormat.Value format = mock(JsonFormat.Value.class);
        when(format.getShape()).thenReturn(JsonFormat.Shape.STRING);
        when(format.hasPattern()).thenReturn(false);
        when(format.hasLocale()).thenReturn(true);
        when(format.getLocale()).thenReturn(Locale.US);
        when(format.hasTimeZone()).thenReturn(true);
        when(format.getTimeZone()).thenReturn(TimeZone.getTimeZone("PST"));
        SerializerProvider serializerProvider = mock(SerializerProvider.class);
        BeanProperty property = mock(BeanProperty.class);
        when(dateTimeSerializerBase.findFormatOverrides(serializerProvider, property, dateTimeSerializerBase.handledType())).thenReturn(format);
        DateTimeSerializerBase<ClassA> result = (DateTimeSerializerBase<ClassA>) dateTimeSerializerBase.createContextual(serializerProvider, property);
        assertFalse(result._useTimestamp);
        assertNull(result._customFormat);
        assertNull(result._reusedCustomFormat);
    }

    @Test
    public void testCreateContextualWithLocale() throws JsonMappingException {
        JsonFormat.Value format = mock(JsonFormat.Value.class);
        when(format.getShape()).thenReturn(JsonFormat.Shape.STRING);
        when(format.hasPattern()).thenReturn(false);
        when(format.hasLocale()).thenReturn(true);
        when(format.getLocale()).thenReturn(Locale.US);
        when(format.hasTimeZone()).thenReturn(false);
        SerializerProvider serializerProvider = mock(SerializerProvider.class);
        BeanProperty property = mock(BeanProperty.class);
        when(dateTimeSerializerBase.findFormatOverrides(serializerProvider, property, dateTimeSerializerBase.handledType())).thenReturn(format);
        DateTimeSerializerBase<ClassA> result = (DateTimeSerializerBase<ClassA>) dateTimeSerializerBase.createContextual(serializerProvider, property);
        assertFalse(result._useTimestamp);
        assertNull(result._customFormat);
        assertNull(result._reusedCustomFormat);
    }

    @Test
    public void testGetSchemaAsString() {
        SerializerProvider serializers = mock(SerializerProvider.class);
        when(dateTimeSerializerBase._asTimestamp(serializers)).thenReturn(false);
        JsonNode jsonNode = dateTimeSerializerBase.getSchema(serializers, null);
        assertEquals("number", jsonNode.asText());
    }

    @Test
    public void testGetSchemaAsNumber() {
        SerializerProvider serializers = mock(SerializerProvider.class);
        when(dateTimeSerializerBase._asTimestamp(serializers)).thenReturn(true);
        JsonNode jsonNode = dateTimeSerializerBase.getSchema(serializers, null);
        assertEquals("string", jsonNode.asText());
    }

    @Test
    public void testAcceptJsonFormatVisitorAsNumber() throws JsonMappingException {
        JsonFormatVisitorWrapper visitor = mock(JsonFormatVisitorWrapper.class);
        JavaType typeHint = mock(JavaType.class);
        when(dateTimeSerializerBase._asTimestamp(visitor.getProvider())).thenReturn(true);
        dateTimeSerializerBase.acceptJsonFormatVisitor(visitor, typeHint);
        verify(dateTimeSerializerBase).visitIntFormat(visitor, typeHint, JsonParser.NumberType.LONG, JsonValueFormat.UTC_MILLISEC);
        verify(dateTimeSerializerBase, never()).visitStringFormat(Mockito.any(JsonFormatVisitorWrapper.class), Mockito.any(JavaType.class),
                Mockito.any(JsonValueFormat.class));
    }

    @Test
    public void testAcceptJsonFormatVisitorAsString() throws JsonMappingException {
        JsonFormatVisitorWrapper visitor = mock(JsonFormatVisitorWrapper.class);
        JavaType typeHint = mock(JavaType.class);
        when(dateTimeSerializerBase._asTimestamp(visitor.getProvider())).thenReturn(false);
        dateTimeSerializerBase.acceptJsonFormatVisitor(visitor, typeHint);
        verify(dateTimeSerializerBase, never()).visitIntFormat(Mockito.any(JsonFormatVisitorWrapper.class), Mockito.any(JavaType.class),
                Mockito.any(JsonParser.NumberType.class), Mockito.any(JsonValueFormat.class));
        verify(dateTimeSerializerBase).visitStringFormat(visitor, typeHint, JsonValueFormat.DATE_TIME);
    }
}