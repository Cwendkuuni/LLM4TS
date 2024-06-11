import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.ser.std.DateTimeSerializerBase;
import com.fasterxml.jackson.databind.util.StdDateFormat;

import java.io.IOException;
import java.lang.reflect.Type;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.TimeZone;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class DateTimeSerializerBaseTest {

    @Test
    public void testConstructor() {
        DateFormat customFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        DateTimeSerializerBase<Object> serializer = new DateTimeSerializerBase<Object>(Object.class, false, customFormat);
        assertNotNull(serializer);
    }

    @Test
    public void testWithFormat() {
        DateTimeSerializerBase<Object> serializer = Mockito.spy(new DateTimeSerializerBase<Object>(Object.class, false, null));
        Boolean timestamp = true;
        DateFormat customFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        DateTimeSerializerBase<Object> result = serializer.withFormat(timestamp, customFormat);
        assertEquals(serializer, result);

        if (timestamp) {
            assertEquals(serializer.getClass().getName() + "_timestamp", result.getClass().getName());
            assertNotNull(result._useTimestamp);
            assertNotNull(result._customFormat);
        } else {
            assertEquals(serializer.getClass().getName(), result.getClass().getName());
            assertNull(result._useTimestamp);
            assertNotNull(result._customFormat);
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateContextualNullProperty() {
        DateTimeSerializerBase<Object> serializer = Mockito.spy(new DateTimeSerializerBase<Object>(Object.class, false, null));
        SerializerProvider serializers = mock(SerializerProvider.class);
        serializer.createContextual(serializers, null);
    }

    @Test
    public void testCreateContextualNullHandledType() {
        DateTimeSerializerBase<Object> serializer = Mockito.spy(new DateTimeSerializerBase<Object>(Object.class, false, null));
        SerializerProvider serializers = mock(SerializerProvider.class);
        Type handledType = null;
        serializer.createContextual(serializers, null).getClass();
    }

    @Test
    public void testCreateContextualWithFormat() throws Exception {
        DateTimeSerializerBase<Object> serializer = Mockito.spy(new DateTimeSerializerBase<Object>(Object.class, false, null));
        SerializerProvider serializers = mock(SerializerProvider.class);
        BeanProperty property = Mockito.mock(BeanProperty.class);
        JsonFormat.Value format = new JsonFormat.Value();
        JsonFormat.Shape shape = JsonFormat.Shape.STRING;
        when(property.getName()).thenReturn("testProperty");
        when(property.getType()).thenReturn(Integer.class);
        when(serializers.findFormatOverrides(Mockito.eq(serializers), Mockito.eq(property), Mockito.eq(Integer.class))).thenReturn(format);

        serializer.createContextual(serializers, property);

        assertNotNull(serializer._customFormat);
        assertEquals(Shape.STRING, serializer._customFormat.getShape());
        assertEquals("testProperty", serializer._customFormat.getSerializationConfig().getFormatName());
    }

    @Test
    public void testSerializeNullValue() throws IOException {
        DateTimeSerializerBase<Object> serializer = new DateTimeSerializerBase<Object>(Object.class, false, null);
        ObjectNode result = null;
        JsonGenerator gen = mock(JsonGenerator.class);
        SerializerProvider serializers = mock(SerializerProvider.class);
        Type typeHint = Object.class;

        when(serializers.isEnabled(JsonSerializer.Feature.WRITE_NULL_STRING_AS_NULL_OBJECT)).thenReturn(true);
        when(gen.writeNull()).thenReturn(result);

        serializer.serialize(null, gen, serializers);
        assertNotNull(result);
    }

    @Test
    public void testSerializeEmptyValue() throws IOException {
        DateTimeSerializerBase<Object> serializer = new DateTimeSerializerBase<Object>(Object.class, false, null);
        ObjectNode result = null;
        JsonGenerator gen = mock(JsonGenerator.class);
        SerializerProvider serializers = mock(SerializerProvider.class);
        Type typeHint = Object.class;

        Object value = new Object();
        long timestamp = 0;
        when(_timestamp(value)).thenReturn(timestamp);

        when(serializers.isEnabled(JsonSerializer.Feature.WRITE_NULL_STRING_AS_NULL_OBJECT)).thenReturn(true);
        when(gen.writeNull()).thenReturn(result);
        when(gen.writeNumber(Mockito.eq(timestamp))).thenReturn(result);

        serializer.serialize(value, gen, serializers);
        assertNotNull(result);
    }

    @Test
    public void testSerializeValueWithCustomFormat() throws Exception {
        DateTimeSerializerBase<Object> serializer = new DateTimeSerializerBase<Object>(Object.class, false, new SimpleDateFormat("dd-MM-yyyy HH:mm:ss"));
        ObjectNode result = null;
        JsonGenerator gen = mock(JsonGenerator.class);
        SerializerProvider serializers = mock(SerializerProvider.class);
        Type typeHint = Object.class;
        Object value = new Object();
        long timestamp = System.currentTimeMillis();

        when(_timestamp(value)).thenReturn(timestamp);
        when(gen.writeString(Mockito.eq(formatDate(new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").format(new java.util.Date(timestamp)))))).thenReturn(result);

        serializer.serialize(value, gen, serializers);
        assertNotNull(result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSerializeWithNullGenerator() throws IOException {
        DateTimeSerializerBase<Object> serializer = new DateTimeSerializerBase<Object>(Object.class, false, null);
        SerializerProvider serializers = mock(SerializerProvider.class);
        Type typeHint = Object.class;
        JsonGenerator gen = null;

        serializer.serialize(new Object(), gen, serializers);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSerializeWithNullSerializerProvider() throws IOException {
        DateTimeSerializerBase<Object> serializer = new DateTimeSerializerBase<Object>(Object.class, false, null);
        JsonGenerator gen = mock(JsonGenerator.class);
        Type typeHint = Object.class;
        SerializerProvider serializers = null;

        serializer.serialize(new Object(), gen, serializers);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSerializeWithNullTypeHint() throws IOException {
        DateTimeSerializerBase<Object> serializer = new DateTimeSerializerBase<Object>(Object.class, false, null);
        JsonGenerator gen = mock(JsonGenerator.class);
        SerializerProvider serializers = mock(SerializerProvider.class);
        Type typeHint = null;

        serializer.serialize(new Object(), gen, serializers, typeHint);
    }

    @Test
    public void testGetSchema() throws Exception {
        DateTimeSerializerBase<Object> serializer = new DateTimeSerializerBase<Object>(Object.class, false, null);
        JsonNode schema = null;
        SerializerProvider serializers = mock(SerializerProvider.class);

        schema = serializer.getSchema(serializers, Object.class);
        assertNotNull(schema);
    }

    private String formatDate(String dateString) {
        StdDateFormat dateFormat = new StdDateFormat();
        try {
            return dateFormat.format(new java.util.Date(System.currentTimeMillis()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}