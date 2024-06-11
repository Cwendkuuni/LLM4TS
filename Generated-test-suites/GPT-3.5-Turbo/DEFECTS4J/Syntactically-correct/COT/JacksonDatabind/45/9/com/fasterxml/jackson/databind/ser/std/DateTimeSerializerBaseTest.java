
import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.Serializers;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class DateTimeSerializerBaseTest {
    private DateTimeSerializerBase<Object> datetimeSerializer;

    @Before
    public void setup() {
        datetimeSerializer = new DateTimeSerializerBase<Object>(Object.class, false, null) {
            @Override
            public DateTimeSerializerBase<Object> withFormat(Boolean timestamp,
                DateFormat customFormat) {
                return null;
            }
            @Override
            public void serialize(Object value, JsonGenerator gen,
                SerializerProvider serializers) throws IOException {
            }
            @Override
            protected long _timestamp(Object value) {
                return 0;
            }
        };
    }

    @Test
    public void testCreateContextualWithoutProperty() throws JsonMappingException {
        SerializerProvider serializers = null;
        BeanProperty property = null;
        DateTimeSerializerBase<Object> result = datetimeSerializer
            .createContextual(serializers, property);
        assertEquals(datetimeSerializer, result);
    }

    @Test
    public void testCreateContextualWithPropertyAndNumericShape() throws JsonMappingException {
        SerializerProvider serializers = null;
        BeanProperty property = new BeanProperty() {
            @Override
            public String getName() { return null; }
            @Override
            public PropertyName getFullName() { return null; }
            @Override
            public Type getType() { return null; }
            @Override
            public PropertyName getWrapperName() { return null; }
            @Override
            public AnnotatedMember getMember() { return null; }
            @Override
            public <A extends Annotation> A getAnnotation(Class<A> acls) { return null; }
            @Override
            public <A extends Annotation> A getContextAnnotation(Class<A> acls) { return null; }
            @Override
            public Class<?> getRawType() { return null; }
            @Override
            public boolean hasAllowedValues() { return false; }
            @Override
            public Object getTypeId() { return null; }
            @Override
            public boolean isRequired() { return false; }
        };
        DateTimeSerializerBase<Object> result = datetimeSerializer
            .createContextual(serializers, property);
        Boolean expectedResult = true;
        assertEquals(expectedResult, result._useTimestamp);
    }

    @Test
    public void testCreateContextualWithPropertyAndStringShapeAndNullTimeZone() throws JsonMappingException {
        SerializerProvider serializers = null;
        TimeZone timeZone = null;
        BeanProperty property = new BeanProperty() {
            @Override
            public String getName() { return null; }
            @Override
            public PropertyName getFullName() { return null; }
            @Override
            public Type getType() { return null; }
            @Override
            public PropertyName getWrapperName() { return null; }
            @Override
            public AnnotatedMember getMember() { return null; }
            @Override
            public <A extends Annotation> A getAnnotation(Class<A> acls) { return null; }
            @Override
            public <A extends Annotation> A getContextAnnotation(Class<A> acls) { return null; }
            @Override
            public Class<?> getRawType() { return null; }
            @Override
            public boolean hasAllowedValues() { return false; }
            @Override
            public Object getTypeId() { return null; }
            @Override
            public boolean isRequired() { return false; }
        };
        JsonFormat.Value format = new JsonFormat.Value(JsonFormat.Shape.STRING,
            null, timeZone, null, null);
        datetimeSerializer.createContextual(serializers, property);
        DateTimeSerializerBase<Object> result = datetimeSerializer
            .createContextual(serializers, property);
        assertNotNull(result);
    }

    @Test
    public void testCreateContextualWithPropertyAndStringShapeAndNonNullTimeZoneAndNonNullPatternAndNonNullLocale() throws JsonMappingException {
        SerializerProvider serializers = null;
        TimeZone timeZone = TimeZone.getTimeZone("GMT");
        String pattern = "yyyy-MM-dd HH:mm:ss";
        Locale locale = Locale.US;
        BeanProperty property = new BeanProperty() {
            @Override
            public String getName() { return null; }
            @Override
            public PropertyName getFullName() { return null; }
            @Override
            public Type getType() { return null; }
            @Override
            public PropertyName getWrapperName() { return null; }
            @Override
            public AnnotatedMember getMember() { return null; }
            @Override
            public <A extends Annotation> A getAnnotation(Class<A> acls) { return null; }
            @Override
            public <A extends Annotation> A getContextAnnotation(Class<A> acls) { return null; }
            @Override
            public Class<?> getRawType() { return null; }
            @Override
            public boolean hasAllowedValues() { return false; }
            @Override
            public Object getTypeId() { return null; }
            @Override
            public boolean isRequired() { return false; }
        };
        JsonFormat.Value format = new JsonFormat.Value(JsonFormat.Shape.STRING,
            null, timeZone, locale, pattern);
        datetimeSerializer.createContextual(serializers, property);
        DateTimeSerializerBase<Object> result = datetimeSerializer
            .createContextual(serializers, property);
        assertNotNull(result);
    }

    @Test
    @Deprecated
    public void testIsEmptyWithValue() {
        Object value = new Object();
        boolean result = datetimeSerializer.isEmpty(value);
        assertFalse(result);
    }

    @Test
    @Deprecated
    public void testIsEmptyWithoutValue() {
        Object value = null;
        boolean result = datetimeSerializer.isEmpty(value);
        assertTrue(result);
    }

    @Test
    public void testIsEmptyWithValueUsingSerializerProvider() {
        SerializerProvider serializers = null;
        Object value = new Object();
        boolean result = datetimeSerializer.isEmpty(serializers, value);
        assertFalse(result);
    }

    @Test
    public void testIsEmptyWithoutValueUsingSerializerProvider() {
        SerializerProvider serializers = null;
        Object value = null;
        boolean result = datetimeSerializer.isEmpty(serializers, value);
        assertTrue(result);
    }

    @Test
    public void testGetSchema() {
        SerializerProvider serializers = null;
        Type typeHint = null;
        //todo: (ryan) add a format for the date in the schema?
        JsonNode result = datetimeSerializer.getSchema(serializers, typeHint);
        assertNotNull(result);
    }

    @Test
    public void testAcceptJsonFormatVisitorAsNumber() throws JsonMappingException {
        JsonFormatVisitorWrapper visitor = null;
        JavaType typeHint = null;
        boolean asNumber = true;
        datetimeSerializer.acceptJsonFormatVisitor(visitor, typeHint, asNumber);
    }

    @Test
    public void testAcceptJsonFormatVisitorWithString() throws JsonMappingException {
        JsonFormatVisitorWrapper visitor = null;
        JavaType typeHint = null;
        boolean asNumber = false;
        datetimeSerializer.acceptJsonFormatVisitor(visitor, typeHint, asNumber);
    }
}
