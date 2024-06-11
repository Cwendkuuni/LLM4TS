
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import org.junit.Test;
import org.mockito.Mockito;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitorWrapper;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonIntegerFormatVisitor;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonStringFormatVisitor;
import com.fasterxml.jackson.databind.jsonschema.JsonSchema;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import com.fasterxml.jackson.databind.ser.std.DateTimeSerializerBase;
import com.fasterxml.jackson.databind.util.StdDateFormat;

public class DateTimeSerializerBaseTest {

    @Test
    public void testSerialize() throws IOException {
        // Arrange
        DateTimeSerializerBase<Date> serializer = new DateTimeSerializerBase<Date>(Date.class, true, null) {
            @Override
            public DateTimeSerializerBase<Date> withFormat(Boolean timestamp, DateFormat customFormat) {
                return null;
            }

            @Override
            protected long _timestamp(Date value) {
                return value.getTime();
            }

            @Override
            public void serialize(Date value, JsonGenerator gen, SerializerProvider serializers)
                    throws IOException {
                gen.writeNumber(value.getTime());
            }
        };

        SerializerProvider provider = mock(SerializerProvider.class);
        JsonGenerator generator = mock(JsonGenerator.class);
        Date date = new Date();

        // Act
        serializer.serialize(date, generator, provider);

        // Assert
        Mockito.verify(generator, Mockito.times(1)).writeNumber(date.getTime());
    }

    @Test
    public void testCreateContextualWithNumericShape() throws JsonProcessingException {
        // Arrange
        DateTimeSerializerBase<Date> serializer = new DateTimeSerializerBase<Date>(Date.class, null, null) {
            @Override
            public DateTimeSerializerBase<Date> withFormat(Boolean timestamp, DateFormat customFormat) {
                return null;
            }

            @Override
            protected long _timestamp(Date value) {
                return value.getTime();
            }

            @Override
            public void serialize(Date value, JsonGenerator gen, SerializerProvider serializers)
                    throws IOException {
                
            }
        };

        SerializerProvider provider = mock(SerializerProvider.class);
        BeanProperty property = mock(BeanProperty.class);
        JsonSerializer<?> returnValue = serializer.createContextual(provider, property);

        // Act
        serializer.createContextual(provider, property);

        // Assert
        assertEquals(serializer, returnValue);
    }

    @Test
    public void testCreateContextualWithCustomFormat() throws ParseException, JsonProcessingException {
        // Arrange
        DateTimeSerializerBase<Date> serializer = new DateTimeSerializerBase<Date>(Date.class, null, null) {
            @Override
            public DateTimeSerializerBase<Date> withFormat(Boolean timestamp, DateFormat customFormat) {
                return null;
            }

            @Override
            protected long _timestamp(Date value) {
                return value.getTime();
            }

            @Override
            public void serialize(Date value, JsonGenerator gen, SerializerProvider serializers)
                    throws IOException {

            }
        };

        SerializerProvider provider = mock(SerializerProvider.class);
        BeanProperty property = mock(BeanProperty.class);
        JsonFormat.Value format = mock(JsonFormat.Value.class);
        Mockito.when(format.getShape()).thenReturn(JsonFormat.Shape.STRING);
        Mockito.when(format.hasPattern()).thenReturn(true);
        Mockito.when(format.getPattern()).thenReturn("yyyy-MM-dd");
        Mockito.when(format.hasLocale()).thenReturn(true);
        Mockito.when(format.getLocale()).thenReturn(Locale.US);

        // Act
        SerializerProvider result = (SerializerProvider) serializer.createContextual(provider, property);

        // Assert
        assertEquals(serializer, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateContextualWithNullFormatThrowsIllegalArgumentException()
            throws ParseException, JsonProcessingException {
        // Arrange
        DateTimeSerializerBase<Date> serializer = new DateTimeSerializerBase<Date>(Date.class, null, null) {
            @Override
            public DateTimeSerializerBase<Date> withFormat(Boolean timestamp, DateFormat customFormat) {
                return null;
            }

            @Override
            protected long _timestamp(Date value) {
                return value.getTime();
            }

            @Override
            public void serialize(Date value, JsonGenerator gen, SerializerProvider serializers)
                    throws IOException {

            }
        };

        SerializerProvider provider = mock(SerializerProvider.class);
        BeanProperty property = null;

        // Act
        serializer.createContextual(provider, property);
    }

    @Test
    public void testIsEmptyWithValueReturnsFalse() {
        // Arrange
        DateTimeSerializerBase<Date> serializer = new DateTimeSerializerBase<Date>(Date.class, null, null) {
            @Override
            public DateTimeSerializerBase<Date> withFormat(Boolean timestamp, DateFormat customFormat) {
                return null;
            }

            @Override
            protected long _timestamp(Date value) {
                return value.getTime();
            }

            @Override
            public void serialize(Date value, JsonGenerator gen, SerializerProvider serializers)
                    throws IOException {

            }
        };
        Date date = new Date();

        // Act
        boolean result = serializer.isEmpty(null, date);

        // Assert
        assertEquals(false, result);
    }

    @Test
    public void testIsEmptyWithNullValueReturnsTrue() {
        // Arrange
        DateTimeSerializerBase<Date> serializer = new DateTimeSerializerBase<Date>(Date.class, null, null) {
            @Override
            public DateTimeSerializerBase<Date> withFormat(Boolean timestamp, DateFormat customFormat) {
                return null;
            }

            @Override
            protected long _timestamp(Date value) {
                return value.getTime();
            }

            @Override
            public void serialize(Date value, JsonGenerator gen, SerializerProvider serializers)
                    throws IOException {

            }
        };
        Date date = null;

        // Act
        boolean result = serializer.isEmpty(null, date);

        // Assert
        assertEquals(true, result);
    }

    @Test
    public void testGetSchemaWithNumericShape() {
        // Arrange
        DateTimeSerializerBase<Date> serializer = new DateTimeSerializerBase<Date>(Date.class, null, null) {
            @Override
            public DateTimeSerializerBase<Date> withFormat(Boolean timestamp, DateFormat customFormat) {
                return null;
            }

            @Override
            protected long _timestamp(Date value) {
                return value.getTime();
            }

            @Override
            public void serialize(Date value, JsonGenerator gen, SerializerProvider serializers)
                    throws IOException {

            }
        };
        SerializerProvider provider = mock(SerializerProvider.class);
        String expectedFormat = "number";

        // Act
        JsonNode result = serializer.getSchema(provider, null);
        String actualFormat = result.get("type").asText();

        // Assert
        assertEquals(expectedFormat, actualFormat);
    }

    @Test
    public void testGetSchemaWithStringShape() {
        // Arrange
        DateTimeSerializerBase<Date> serializer = new DateTimeSerializerBase<Date>(Date.class, null, null) {
            @Override
            public DateTimeSerializerBase<Date> withFormat(Boolean timestamp, DateFormat customFormat) {
                return null;
            }

            @Override
            protected long _timestamp(Date value) {
                return value.getTime();
            }

            @Override
            public void serialize(Date value, JsonGenerator gen, SerializerProvider serializers)
                    throws IOException {

            }
        };
        SerializerProvider provider = mock(SerializerProvider.class);
        provider.configure(MapperFeature.SORT_PROPERTIES_ALPHABETICALLY, true);
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        df.setTimeZone(TimeZone.getTimeZone("UTC"));
        Date date = df.parse("2020-01-01 00:00:00");

        // Act
        JsonNode result = serializer.getSchema(provider, null);
        String actualFormat = result.get("format").asText();
        String actualPattern = result.get("pattern").asText();
        String actualExample = result.get("example").asText();

        // Assert
        assertEquals("string", actualFormat);
        assertEquals("yyyy-MM-dd HH:mm:ss", actualPattern);
        assertEquals("2020-01-01 00:00:00", actualExample);
    }

    @Test
    public void testAcceptJsonFormatVisitorWithNumericShape() throws JsonProcessingException {
        // Arrange
        DateTimeSerializerBase<Date> serializer = new DateTimeSerializerBase<Date>(Date.class, null, null) {
            @Override
            public DateTimeSerializerBase<Date> withFormat(Boolean timestamp, DateFormat customFormat) {
                return null;
            }

            @Override
            protected long _timestamp(Date value) {
                return value.getTime();
            }

            @Override
            public void serialize(Date value, JsonGenerator gen, SerializerProvider serializers)
                    throws IOException {

            }
        };
        JsonFormatVisitorWrapper visitor = mock(JsonFormatVisitorWrapper.class);
        JavaType typeHint = mock(JavaType.class);
        boolean asNumber = true;

        // Act
        serializer.acceptJsonFormatVisitor(visitor, typeHint);
        JsonIntegerFormatVisitor jsonVisitor = visitor.expectIntegerFormat(typeHint);
        jsonVisitor.numberType(JsonParser.NumberType.LONG);

        // Assert
        Mockito.verify(visitor, Mockito.times(1)).expectIntegerFormat(typeHint);
    }

    @Test
    public void testAcceptJsonFormatVisitorWithStringShape() throws JsonProcessingException {
        // Arrange
        DateTimeSerializerBase<Date> serializer = new DateTimeSerializerBase<Date>(Date.class, null, null) {
            @Override
            public DateTimeSerializerBase<Date> withFormat(Boolean timestamp, DateFormat customFormat) {
                return null;
            }

            @Override
            protected long _timestamp(Date value) {
                return value.getTime();
            }

            @Override
            public void serialize(Date value, JsonGenerator gen, SerializerProvider serializers)
                    throws IOException {

            }
        };
        JsonFormatVisitorWrapper visitor = mock(JsonFormatVisitorWrapper.class);
        JavaType typeHint = mock(JavaType.class);
        boolean asNumber = false;

        // Act
        serializer.acceptJsonFormatVisitor(visitor, typeHint);
        JsonStringFormatVisitor jsonVisitor = visitor.expectStringFormat(typeHint);
        jsonVisitor.format(JsonValueFormat.DATE_TIME);

        // Assert
        Mockito.verify(visitor, Mockito.times(1)).expectStringFormat(typeHint);
    }

}
