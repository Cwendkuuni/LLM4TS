import org.junit.Assert;
import org.junit.Test;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.TimeZone;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.jsonFormatVisitors.*;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import com.fasterxml.jackson.databind.util.StdDateFormat;
import com.fasterxml.jackson.databind.ser.std.DateTimeSerializerBase;

public class DateTimeSerializerBaseTest {

    @Test
    public void testWithFormat() {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
        DateTimeSerializerBase serializer = new DateTimeSerializerBase() {
            @Override
            public DateTimeSerializerBase withFormat(Boolean timestamp, DateFormat customFormat) {
                return null;
            }

            @Override
            public void serialize(T value, JsonGenerator gen, SerializerProvider serializers) throws IOException {

            }

            @Override
            protected long _timestamp(T value) {
                return 0;
            }

            @Override
            public void acceptJsonFormatVisitor(JsonFormatVisitorWrapper visitor, JavaType typeHint) throws JsonMappingException {

            }
        };

        DateTimeSerializerBase result = serializer.withFormat(false, dateFormat);

        Assert.assertFalse(result._useTimestamp);
        Assert.assertEquals(dateFormat, result._customFormat);
    }

    @Test
    public void testCreateContextual_NullProperty() throws JsonMappingException {
        SerializerProvider serializers = null;
        BeanProperty property = null;
        DateTimeSerializerBase serializer = new DateTimeSerializerBase() {
            @Override
            public DateTimeSerializerBase withFormat(Boolean timestamp, DateFormat customFormat) {
                return null;
            }

            @Override
            public void serialize(T value, JsonGenerator gen, SerializerProvider serializers) throws IOException {

            }

            @Override
            protected long _timestamp(T value) {
                return 0;
            }

            @Override
            public void acceptJsonFormatVisitor(JsonFormatVisitorWrapper visitor, JavaType typeHint) throws JsonMappingException {

            }
        };

        JsonSerializer result = serializer.createContextual(serializers, property);

        Assert.assertEquals(serializer, result);
    }

    @Test
    public void testCreateContextual_NumericShape() throws JsonMappingException {
        SerializerProvider serializers = null;
        BeanProperty property = null;
        DateTimeSerializerBase serializer = new DateTimeSerializerBase() {
            @Override
            public DateTimeSerializerBase withFormat(Boolean timestamp, DateFormat customFormat) {
                return null;
            }

            @Override
            public void serialize(T value, JsonGenerator gen, SerializerProvider serializers) throws IOException {

            }

            @Override
            protected long _timestamp(T value) {
                return 0;
            }

            @Override
            public void acceptJsonFormatVisitor(JsonFormatVisitorWrapper visitor, JavaType typeHint) throws JsonMappingException {

            }
        };

        JsonFormat.Value format = new JsonFormat.Value();
        format.setShape(JsonFormat.Shape.NUMBER);

        JsonSerializer result = serializer.createContextual(serializers, property);

        Assert.assertNotNull(result);
        Assert.assertEquals(serializer, result);
        Assert.assertTrue(result._useTimestamp);
        Assert.assertNull(result._customFormat);
    }

    @Test
    public void testCreateContextual_StringShape() throws JsonMappingException {
        SerializerProvider serializers = null;
        BeanProperty property = null;
        DateTimeSerializerBase serializer = new DateTimeSerializerBase() {
            @Override
            public DateTimeSerializerBase withFormat(Boolean timestamp, DateFormat customFormat) {
                return null;
            }

            @Override
            public void serialize(T value, JsonGenerator gen, SerializerProvider serializers) throws IOException {

            }

            @Override
            protected long _timestamp(T value) {
                return 0;
            }

            @Override
            public void acceptJsonFormatVisitor(JsonFormatVisitorWrapper visitor, JavaType typeHint) throws JsonMappingException {

            }
        };

        SimpleDateFormat expectedDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
        JsonFormat.Value format = new JsonFormat.Value();
        format.setShape(JsonFormat.Shape.STRING);
        format.setPattern("yyyy-MM-dd'T'HH:mm:ss.SSSZ");

        JsonSerializer result = serializer.createContextual(serializers, property);

        Assert.assertNotNull(result);
        Assert.assertEquals(serializer, result);
        Assert.assertFalse(result._useTimestamp);
        Assert.assertEquals(expectedDateFormat, result._customFormat);
    }

    @Test
    public void testIsEmpty_NullValue_ReturnsTrue() {
        DateTimeSerializerBase serializer = new DateTimeSerializerBase() {
            @Override
            public DateTimeSerializerBase withFormat(Boolean timestamp, DateFormat customFormat) {
                return null;
            }

            @Override
            public void serialize(T value, JsonGenerator gen, SerializerProvider serializers) throws IOException {

            }

            @Override
            protected long _timestamp(T value) {
                return 0;
            }

            @Override
            public void acceptJsonFormatVisitor(JsonFormatVisitorWrapper visitor, JavaType typeHint) throws JsonMappingException {

            }
        };

        boolean result = serializer.isEmpty(null);

        Assert.assertTrue(result);
    }

    @Test
    public void testGetSchema_AsNumber() {
        DateTimeSerializerBase serializer = new DateTimeSerializerBase() {
            @Override
            public DateTimeSerializerBase withFormat(Boolean timestamp, DateFormat customFormat) {
                return null;
            }

            @Override
            public void serialize(T value, JsonGenerator gen, SerializerProvider serializers) throws IOException {

            }

            @Override
            protected long _timestamp(T value) {
                return 0;
            }

            @Override
            public void acceptJsonFormatVisitor(JsonFormatVisitorWrapper visitor, JavaType typeHint) throws JsonMappingException {

            }
        };

        JsonNode result = serializer.getSchema(null, null);

        Assert.assertNotNull(result);
        Assert.assertEquals(JsonNodeType.STRING, result.getNodeType());
    }

    @Test
    public void testGetSchema_AsString() {
        DateTimeSerializerBase serializer = new DateTimeSerializerBase() {
            @Override
            public DateTimeSerializerBase withFormat(Boolean timestamp, DateFormat customFormat) {
                return null;
            }

            @Override
            public void serialize(T value, JsonGenerator gen, SerializerProvider serializers) throws IOException {

            }

            @Override
            protected long _timestamp(T value) {
                return 0;
            }

            @Override
            public void acceptJsonFormatVisitor(JsonFormatVisitorWrapper visitor, JavaType typeHint) throws JsonMappingException {

            }
        };

        JsonNode result = serializer.getSchema(null, null);

        Assert.assertNotNull(result);
        Assert.assertEquals(JsonNodeType.NUMBER, result.getNodeType());
    }

    @Test
    public void testAcceptJsonFormatVisitor_AsNumber() throws JsonMappingException {
        DateTimeSerializerBase serializer = new DateTimeSerializerBase() {
            @Override
            public DateTimeSerializerBase withFormat(Boolean timestamp, DateFormat customFormat) {
                return null;
            }

            @Override
            public void serialize(T value, JsonGenerator gen, SerializerProvider serializers) throws IOException {

            }

            @Override
            protected long _timestamp(T value) {
                return 0;
            }

            @Override
            public void acceptJsonFormatVisitor(JsonFormatVisitorWrapper visitor, JavaType typeHint) throws JsonMappingException {

            }
        };
        JsonFormatVisitorWrapper visitor = new JsonFormatVisitorWrapperBase();

        serializer.acceptJsonFormatVisitor(visitor, null);

        Assert.assertTrue(visitor.isDone());
    }

    @Test
    public void testAcceptJsonFormatVisitor_AsString() throws JsonMappingException {
        DateTimeSerializerBase serializer = new DateTimeSerializerBase() {
            @Override
            public DateTimeSerializerBase withFormat(Boolean timestamp, DateFormat customFormat) {
                return null;
            }

            @Override
            public void serialize(T value, JsonGenerator gen, SerializerProvider serializers) throws IOException {

            }

            @Override
            protected long _timestamp(T value) {
                return 0;
            }

            @Override
            public void acceptJsonFormatVisitor(JsonFormatVisitorWrapper visitor, JavaType typeHint) throws JsonMappingException {

            }
        };
        JsonFormatVisitorWrapper visitor = new JsonFormatVisitorWrapperBase();

        serializer.acceptJsonFormatVisitor(visitor, null);

        Assert.assertTrue(visitor.isDone());
    }

    @Test
    public void testSerialize() throws IOException {
        DateTimeSerializerBase serializer = new DateTimeSerializerBase() {
            @Override
            public DateTimeSerializerBase withFormat(Boolean timestamp, DateFormat customFormat) {
                return null;
            }

            @Override
            public void serialize(T value, JsonGenerator gen, SerializerProvider serializers) throws IOException {

            }

            @Override
            protected long _timestamp(T value) {
                return 0;
            }

            @Override
            public void acceptJsonFormatVisitor(JsonFormatVisitorWrapper visitor, JavaType typeHint) throws JsonMappingException {

            }
        };
        JsonGenerator gen = null;
        SerializerProvider serializers = null;

        serializer.serialize(null, gen, serializers);
    }

    @Test
    public void testIsTimestamp_NullSerializerProvider_ThrowsIllegalArgumentException() {
        DateTimeSerializerBase serializer = new DateTimeSerializerBase() {
            @Override
            public DateTimeSerializerBase withFormat(Boolean timestamp, DateFormat customFormat) {
                return null;
            }

            @Override
            public void serialize(T value, JsonGenerator gen, SerializerProvider serializers) throws IOException {

            }

            @Override
            protected long _timestamp(T value) {
                return 0;
            }

            @Override
            public void acceptJsonFormatVisitor(JsonFormatVisitorWrapper visitor, JavaType typeHint) throws JsonMappingException {

            }
        };
        SerializerProvider serializers = null;

        try {
            serializer.isTimestamp(serializers);
            Assert.fail();
        } catch (IllegalArgumentException e) {
            Assert.assertEquals("Null SerializerProvider passed for " + serializer.handledType().getName(), e.getMessage());
        }
    }

    @Test
    public void testIsTimestamp_UndefinedUseTimestamp_ReturnsFalse() {
        DateTimeSerializerBase serializer = new DateTimeSerializerBase() {
            @Override
            public DateTimeSerializerBase withFormat(Boolean timestamp, DateFormat customFormat) {
                return null;
            }

            @Override
            public void serialize(T value, JsonGenerator gen, SerializerProvider serializers) throws IOException {

            }

            @Override
            protected long _timestamp(T value) {
                return 0;
            }

            @Override
            public void acceptJsonFormatVisitor(JsonFormatVisitorWrapper visitor, JavaType typeHint) throws JsonMappingException {

            }
        };
        SerializerProvider serializers = new SerializerProvider();

        boolean result = serializer.isTimestamp(serializers);

        Assert.assertFalse(result);
    }

    @Test
    public void testIsTimestamp_CustomFormatExists_ReturnsFalse() {
        DateTimeSerializerBase serializer = new DateTimeSerializerBase() {
            @Override
            public DateTimeSerializerBase withFormat(Boolean timestamp, DateFormat customFormat) {
                return null;
            }

            @Override
            public void serialize(T value, JsonGenerator gen, SerializerProvider serializers) throws IOException {

            }

            @Override
            protected long _timestamp(T value) {
                return 0;
            }

            @Override
            public void acceptJsonFormatVisitor(JsonFormatVisitorWrapper visitor, JavaType typeHint) throws JsonMappingException {

            }
        };
        DateFormat customFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
        customFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        serializer._customFormat = customFormat;
        SerializerProvider serializers = new SerializerProvider();

        boolean result = serializer.isTimestamp(serializers);

        Assert.assertFalse(result);
    }

    @Test
    public void testIsTimestamp_UseTimestampTrue_ReturnsTrue() {
        DateTimeSerializerBase serializer = new DateTimeSerializerBase() {
            @Override
            public DateTimeSerializerBase withFormat(Boolean timestamp, DateFormat customFormat) {
                return null;
            }

            @Override
            public void serialize(T value, JsonGenerator gen, SerializerProvider serializers) throws IOException {

            }

            @Override
            protected long _timestamp(T value) {
                return 0;
            }

            @Override
            public void acceptJsonFormatVisitor(JsonFormatVisitorWrapper visitor, JavaType typeHint) throws JsonMappingException {

            }
        };
        serializer._useTimestamp = true;
        SerializerProvider serializers = new SerializerProvider();

        boolean result = serializer.isTimestamp(serializers);

        Assert.assertTrue(result);
    }

    protected void visitIntFormat(JsonFormatVisitorWrapper visitor, JavaType typeHint, JsonParser.NumberType numberType, JsonValueFormat format) throws JsonMappingException {
        visitor.expectNumberFormat(typeHint, numberType);
    }

    protected void visitStringFormat(JsonFormatVisitorWrapper visitor, JavaType typeHint, JsonValueFormat format) throws JsonMappingException {
        visitor.expectStringFormat(typeHint);
    }
}