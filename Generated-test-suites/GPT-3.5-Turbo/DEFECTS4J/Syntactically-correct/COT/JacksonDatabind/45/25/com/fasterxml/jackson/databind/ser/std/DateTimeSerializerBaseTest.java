import static org.junit.Assert.*;
import java.io.IOException;
import java.lang.reflect.Type;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.TimeZone;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.introspect.Annotated;
import com.fasterxml.jackson.databind.jsonFormatVisitors.*;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import com.fasterxml.jackson.databind.util.StdDateFormat;
import org.junit.Test;

public class DateTimeSerializerBaseTest {

    @Test
    public void withFormat_Test() {
        DateTimeSerializerBase<?> serializer = new DateTimeSerializerBase<>(null, false, null) {
            @Override
            public DateTimeSerializerBase<?> withFormat(Boolean timestamp, DateFormat customFormat) {
                return null;
            }

            @Override
            protected long _timestamp(Object value) {
                return 0;
            }

            @Override
            public void serialize(Object value, JsonGenerator gen, SerializerProvider serializers) throws IOException {

            }
        };

        DateTimeSerializerBase<?> contextualSerializer = serializer.withFormat(false, new SimpleDateFormat("yyyy-MM-dd"));
        assertFalse(contextualSerializer._asTimestamp(null));
        assertEquals("yyyy-MM-dd", ((SimpleDateFormat)contextualSerializer._customFormat).toPattern());
    }

    @Test
    public void createContextual_Test() {
        DateTimeSerializerBase<?> serializer = new DateTimeSerializerBase<>(null, false, null) {
            @Override
            public DateTimeSerializerBase<?> withFormat(Boolean timestamp, DateFormat customFormat) {
                return null;
            }

            @Override
            protected long _timestamp(Object value) {
                return 0;
            }

            @Override
            public void serialize(Object value, JsonGenerator gen, SerializerProvider serializers) throws IOException {

            }
        };

        BeanProperty property = new BeanProperty() {
            @Override
            public Type getType() {
                return null;
            }

            @Override
            public <A extends Annotation> A getAnnotation(Class<A> annotationClass) {
                return null;
            }

            @Override
            public <A extends Annotation> A getContextAnnotation(Class<A> annotationClass) {
                return null;
            }

            @Override
            public PropertyName getFullName() {
                return null;
            }

            @Override
            public boolean isRequired() {
                return false;
            }

            @Override
            public <A extends Annotation> A getMemberAnnotation(Class<A> annotationType) {
                return null;
            }

            @Override
            public AnnotatedMember getMember() {
                return null;
            }
        };

        SerializerProvider provider = null;

        JsonSerializer<?> contextualSerializer = serializer.createContextual(provider, property);

        assertNotNull(contextualSerializer);
        assertEquals(serializer, contextualSerializer);
    }

    @Test
    public void isEmpty_Test() {
        DateTimeSerializerBase<?> serializer = new DateTimeSerializerBase<>(null, false, null) {
            @Override
            public DateTimeSerializerBase<?> withFormat(Boolean timestamp, DateFormat customFormat) {
                return null;
            }

            @Override
            protected long _timestamp(Object value) {
                return (value == null) ? 0L : 1L;
            }

            @Override
            public void serialize(Object value, JsonGenerator gen, SerializerProvider serializers) throws IOException {

            }
        };

        assertTrue(serializer.isEmpty(null, null));
        assertFalse(serializer.isEmpty(null, new Object()));
    }

    @Test
    public void getSchema_Test() {
        DateTimeSerializerBase<?> serializer = new DateTimeSerializerBase<>(null, false, null) {
            @Override
            public DateTimeSerializerBase<?> withFormat(Boolean timestamp, DateFormat customFormat) {
                return null;
            }

            @Override
            protected long _timestamp(Object value) {
                return 0;
            }

            @Override
            public void serialize(Object value, JsonGenerator gen, SerializerProvider serializers) throws IOException {

            }
        };

        SerializerProvider provider = null;

        JsonNode schema = serializer.getSchema(provider, null);

        assertEquals("string", schema.get("type").asText());
        assertTrue(schema.get("required").asBoolean());
    }

    @Test
    public void acceptJsonFormatVisitor_Test() {
        DateTimeSerializerBase<?> serializer = new DateTimeSerializerBase<>(null, false, null) {
            @Override
            public DateTimeSerializerBase<?> withFormat(Boolean timestamp, DateFormat customFormat) {
                return null;
            }

            @Override
            protected long _timestamp(Object value) {
                return 0;
            }

            @Override
            public void serialize(Object value, JsonGenerator gen, SerializerProvider serializers) throws IOException {

            }
        };

        JsonFormatVisitorWrapper visitor = null;
        JavaType typeHint = null;

        serializer.acceptJsonFormatVisitor(visitor, typeHint);
    }

}