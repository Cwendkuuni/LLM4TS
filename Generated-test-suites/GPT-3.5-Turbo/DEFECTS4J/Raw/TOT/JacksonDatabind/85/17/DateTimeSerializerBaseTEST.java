import org.junit.Test;
import java.text.SimpleDateFormat;
import java.util.Date;
import static org.junit.Assert.*;

public class DateTimeSerializerBaseTest {

    @Test
    public void testWithFormat() {
        Boolean useTimestamp = false;
        SimpleDateFormat customFormat = new SimpleDateFormat("dd-MM-yyyy");
        DateTimeSerializerBase<Date> serializer = new DateTimeSerializerBase<Date>(Date.class, useTimestamp, customFormat) {
            @Override
            public DateTimeSerializerBase<Date> withFormat(Boolean timestamp, DateFormat customFormat) {
                return null;
            }

            @Override
            public void serialize(Date value, JsonGenerator gen, SerializerProvider serializers) throws IOException {

            }

            @Override
            protected long _timestamp(Date value) {
                return 0;
            }
        };

        DateFormat customFormatUpdated = new SimpleDateFormat("yyyy-MM-dd");
        DateTimeSerializerBase<Date> updatedSerializer = serializer.withFormat(false, customFormatUpdated);
        assertEquals(updatedSerializer._customFormat, customFormatUpdated);
    }

    @Test
    public void testCreateContextual() {
        JsonSerializer<?> jsonSerializer;
        BeanProperty property;

        SerializerProvider serializerProvider = new SerializerProvider();

        DateTimeSerializerBase<Date> serializer = new DateTimeSerializerBase<Date>(Date.class, true, null) {
            @Override
            public DateTimeSerializerBase<Date> withFormat(Boolean timestamp, DateFormat customFormat) {
                return null;
            }

            @Override
            public void serialize(Date value, JsonGenerator gen, SerializerProvider serializers) throws IOException {

            }

            @Override
            protected long _timestamp(Date value) {
                return 0;
            }
        };

        assertNull(serializer.createContextual(serializerProvider, null));

        JsonFormat.Value format = new JsonFormat.Value();
        format = format.withShape(JsonFormat.Shape.STRING);

        property = new BeanProperty() {
            @Override
            public String getName() {
                return null;
            }

            @Override
            public PropertyName getFullName() {
                return null;
            }

            @Override
            public <A extends Annotation> A getAnnotation(Class<A> aClass) {
                return null;
            }

            @Override
            public <A extends Annotation> A getContextAnnotation(Class<A> aClass) {
                return null;
            }
        };

        jsonSerializer = serializer.createContextual(serializerProvider, property);
        assertNotNull(jsonSerializer);
    }

    @Test
    public void testIsEmpty() {
        DateTimeSerializerBase<Date> serializer = new DateTimeSerializerBase<Date>(Date.class, true, null) {
            @Override
            public DateTimeSerializerBase<Date> withFormat(Boolean timestamp, DateFormat customFormat) {
                return null;
            }

            @Override
            public void serialize(Date value, JsonGenerator gen, SerializerProvider serializers) throws IOException {

            }

            @Override
            protected long _timestamp(Date value) {
                return 0;
            }
        };

        assertTrue(serializer.isEmpty(null, null));

        Date date = new Date();
        assertFalse(serializer.isEmpty(null, date));
    }

    @Test
    public void testGetSchema() {
        DateTimeSerializerBase<Date> serializer = new DateTimeSerializerBase<Date>(Date.class, true, null) {
            @Override
            public DateTimeSerializerBase<Date> withFormat(Boolean timestamp, DateFormat customFormat) {
                return null;
            }

            @Override
            public void serialize(Date value, JsonGenerator gen, SerializerProvider serializers) throws IOException {

            }

            @Override
            protected long _timestamp(Date value) {
                return 0;
            }
        };

        assertNotNull(serializer.getSchema(null, Date.class));
    }

    @Test
    public void testAcceptJsonFormatVisitor() {
        JsonFormatVisitorWrapper visitorWrapper = new JsonFormatVisitorWrapper() {
            @Override
            public SerializerProvider getProvider() {
                return null;
            }

            @Override
            public void setProvider(SerializerProvider provider) {
                
            }

            @Override
            public JsonFormatVisitorWrapper expectHostname() {
                return null;
            }

            @Override
            public JsonFormatVisitorWrapper expectAnyFormat(JavaType javaType) {
                return null;
            }

            @Override
            public JsonFormatVisitorWrapper expectAnyFormat(Class<?> aClass) {
                return null;
            }

            @Override
            public JsonFormatVisitorWrapper expectBooleanFormat(JavaType javaType) {
                return null;
            }

            @Override
            public JsonFormatVisitorWrapper expectBooleanFormat(Class<?> aClass) {
                return null;
            }

            @Override
            public JsonFormatVisitorWrapper expectIntegerFormat(JavaType javaType) {
                return null;
            }

            @Override
            public JsonFormatVisitorWrapper expectIntegerFormat(Class<?> aClass) {
                return null;
            }

            @Override
            public JsonFormatVisitorWrapper expectIntFormat(JavaType javaType) {
                return null;
            }

            @Override
            public JsonFormatVisitorWrapper expectIntFormat(Class<?> aClass) {
                return null;
            }

            @Override
            public JsonFormatVisitorWrapper expectLongFormat(JavaType javaType) {
                return null;
            }

            @Override
            public JsonFormatVisitorWrapper expectLongFormat(Class<?> aClass) {
                return null;
            }

            @Override
            public JsonFormatVisitorWrapper expectNumberFormat(JavaType javaType) {
                return null;
            }

            @Override
            public JsonFormatVisitorWrapper expectNumberFormat(Class<?> aClass) {
                return null;
            }

            @Override
            public JsonFormatVisitorWrapper expectFloatFormat(JavaType javaType) {
                return null;
            }

            @Override
            public JsonFormatVisitorWrapper expectFloatFormat(Class<?> aClass) {
                return null;
            }

            @Override
            public JsonFormatVisitorWrapper expectDoubleFormat(JavaType javaType) {
                return null;
            }

            @Override
            public JsonFormatVisitorWrapper expectDoubleFormat(Class<?> aClass) {
                return null;
            }

            @Override
            public JsonFormatVisitorWrapper expectArrayFormat(JavaType javaType) {
                return null;
            }

            @Override
            public JsonFormatVisitorWrapper expectArrayFormat(Class<?> aClass) {
                return null;
            }

            @Override
            public JsonArrayFormatVisitor expectArrayFormat() {
                return null;
            }

            @Override
            public JsonStringFormatVisitor expectStringFormat(JavaType javaType) {
                return null;
            }

            @Override
            public JsonStringFormatVisitor expectStringFormat(Class<?> aClass) {
                return null;
            }

            @Override
            public JsonArrayFormatVisitor expectArrayFormat(JavaType javaType, Class<?> aClass) {
                return null;
            }

            @Override
            public JsonObjectFormatVisitor expectObjectFormat(JavaType javaType) {
                return null;
            }

            @Override
            public JsonObjectFormatVisitor expectObjectFormat(Class<?> aClass) {
                return null;
            }

            @Override
            public JsonObjectFormatVisitor expectObjectFormat() {
                return null;
            }
        };
        JavaType typeHint = null;

        DateTimeSerializerBase<Date> serializer = new DateTimeSerializerBase<Date>(Date.class, true, null) {
            @Override
            public DateTimeSerializerBase<Date> withFormat(Boolean timestamp, DateFormat customFormat) {
                return null;
            }

            @Override
            public void serialize(Date value, JsonGenerator gen, SerializerProvider serializers) throws IOException {

            }

            @Override
            protected long _timestamp(Date value) {
                return 0;
            }
        };

        try {
            serializer.acceptJsonFormatVisitor(visitorWrapper, typeHint);
        } catch (JsonMappingException e) {
            fail("Exception should not be thrown");
        }
    }

    @Test
    public void testSerialize() {
        Date value = new Date();
        JsonGenerator jsonGenerator = null;
        SerializerProvider serializers = null;

        DateTimeSerializerBase<Date> serializer = new DateTimeSerializerBase<Date>(Date.class, true, null) {
            @Override
            public DateTimeSerializerBase<Date> withFormat(Boolean timestamp, DateFormat customFormat) {
                return null;
            }

            @Override
            public void serialize(Date value, JsonGenerator gen, SerializerProvider serializers) throws IOException {

            }

            @Override
            protected long _timestamp(Date value) {
                return 0;
            }
        };

        try {
            serializer.serialize(value, jsonGenerator, serializers);
        } catch (IOException e) {
            fail("Exception should not be thrown");
        }
    }

    @Test
    public void testTimestamp() {
        Date value = new Date();

        DateTimeSerializerBase<Date> serializer = new DateTimeSerializerBase<Date>(Date.class, true, null) {
            @Override
            public DateTimeSerializerBase<Date> withFormat(Boolean timestamp, DateFormat customFormat) {
                return null;
            }

            @Override
            public void serialize(Date value, JsonGenerator gen, SerializerProvider serializers) throws IOException {

            }

            @Override
            protected long _timestamp(Date value) {
                return 0;
            }
        };

        assertEquals(serializer._timestamp(value), 0L);
    }
}