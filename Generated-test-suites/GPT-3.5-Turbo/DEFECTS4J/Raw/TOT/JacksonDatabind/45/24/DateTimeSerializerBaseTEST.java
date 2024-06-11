
import static org.junit.Assert.*;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import org.junit.Test;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import com.fasterxml.jackson.databind.ser.std.DateTimeSerializerBase;
import com.fasterxml.jackson.databind.ser.std.StdScalarSerializer;
import com.fasterxml.jackson.databind.util.StdDateFormat;

@SuppressWarnings("serial")
public class DateTimeSerializerBaseTest {

    @Test
    public void testWithFormatDefaultValue() throws Exception {
        DateTimeSerializerBase<Date> serializer = new DateTimeSerializerBase<Date>(Date.class, false, null) {
            @Override
            public DateTimeSerializerBase<Date> withFormat(Boolean timestamp, DateFormat customFormat) {
                return null;
            }

            @Override
            protected long _timestamp(Date value) {
                return 0;
            }

            @Override
            public void serialize(Date value, JsonGenerator gen, SerializerProvider serializers) throws IOException {

            }

        };
        assertNull(serializer.withFormat(null, null));
    }

    @Test
    public void testWithFormatTimestamp() throws Exception {
        DateTimeSerializerBase<Date> serializer = new DateTimeSerializerBase<Date>(Date.class, false, null) {
            @Override
            public DateTimeSerializerBase<Date> withFormat(Boolean timestamp, DateFormat customFormat) {
                return null;
            }

            @Override
            protected long _timestamp(Date value) {
                return 0;
            }

            @Override
            public void serialize(Date value, JsonGenerator gen, SerializerProvider serializers) throws IOException {

            }

        };
        assertNotNull(serializer.withFormat(true, null));
    }

    @Test
    public void testWithFormatCustomFormat() throws Exception {
        DateTimeSerializerBase<Date> serializer = new DateTimeSerializerBase<Date>(Date.class, false, null) {
            @Override
            public DateTimeSerializerBase<Date> withFormat(Boolean timestamp, DateFormat customFormat) {
                return null;
            }

            @Override
            protected long _timestamp(Date value) {
                return 0;
            }

            @Override
            public void serialize(Date value, JsonGenerator gen, SerializerProvider serializers) throws IOException {

            }

        };
        assertNotNull(serializer.withFormat(false, new SimpleDateFormat("yyyy-MM-dd")));
    }

    @Test
    public void testCreateContextualPropertyNull() throws Exception {
        DateTimeSerializerBase<Date> serializer = new DateTimeSerializerBase<Date>(Date.class, false, null) {
            @Override
            public DateTimeSerializerBase<Date> withFormat(Boolean timestamp, DateFormat customFormat) {
                return null;
            }

            @Override
            protected long _timestamp(Date value) {
                return 0;
            }

            @Override
            public void serialize(Date value, JsonGenerator gen, SerializerProvider serializers) throws IOException {

            }

        };
        assertSame(serializer, serializer.createContextual(null, null));
    }

    @Test
    public void testCreateContextualPropertyNonNull() throws Exception {
        DateTimeSerializerBase<Date> serializer = new DateTimeSerializerBase<Date>(Date.class, false, null) {
            @Override
            public DateTimeSerializerBase<Date> withFormat(Boolean timestamp, DateFormat customFormat) {
                return null;
            }

            @Override
            protected long _timestamp(Date value) {
                return 0;
            }

            @Override
            public void serialize(Date value, JsonGenerator gen, SerializerProvider serializers) throws IOException {

            }

        };
        assertSame(serializer, serializer.createContextual(null, new BeanProperty() {

            @Override
            public com.fasterxml.jackson.databind.AnnotationIntrospector.Reference getPropertyType() {
                return null;
            }

            @Override
            public PropertyName getFullName() {
                return null;
            }

            @Override
            public PropertyName getWrapperName() {
                return null;
            }

            @Override
            public AnnotatedMember getMember() {
                Annotated annotated = new Annotated() {

                    @Override
                    public <A extends Annotation> A getAnnotation(Class<A> arg0) {
                        return null;
                    }

                    @Override
                    public boolean hasAnnotation(Class<?> arg0) {
                        return false;
                    }

                    @Override
                    public boolean hasOneOf(Class<? extends Annotation>[] arg0) {
                        return false;
                    }

                };
                return new AnnotatedMember() {

                    @Override
                    public Class<?> getDeclaringClass() {
                        return null;
                    }

                    @Override
                    public Type getGenericType() {
                        return null;
                    }

                    @Override
                    public PropertyName getName() {
                        return null;
                    }

                    @Override
                    public <A extends Annotation> A getAnnotation(Class<A> arg0) {
                        return null;
                    }

                    @Override
                    public boolean hasAnnotation(Class<?> arg0) {
                        return false;
                    }

                    @Override
                    public boolean hasOneOf(Class<? extends Annotation>[] arg0) {
                        return false;
                    }

                    @Override
                    public Annotated withAnnotations(AnnotationMap arg0) {
                        return null;
                    }

                    @Override
                    public AnnotationMap getAllAnnotations() {
                        return null;
                    }

                    @Override
                    public int getAnnotationsCount() {
                        return 0;
                    }

                    @Override
                    public Iterable<AnnotationMap> annotations() {
                        return null;
                    }

                };
            }

            @Override
            public PropertyMetadata getMetadata() {
                return null;
            }

            @Override
            public AnnotatedMember getPrimaryMember() {
                return null;
            }

            @Override
            public Annotated getSecondaryMember() {
                return null;
            }

            @Override
            public <A extends Annotation> A getAnnotation(Class<A> arg0) {
                return null;
            }

            @Override
            public boolean hasAnnotation(Class<?> arg0) {
                return false;
            }

            @Override
            public boolean hasOneOf(Class<? extends Annotation>[] arg0) {
                return false;
            }

        }));
    }

    @Test
    public void testIsEmptyValueNull() throws Exception {
        DateTimeSerializerBase<Date> serializer = new DateTimeSerializerBase<Date>(Date.class, false, null) {
            @Override
            public DateTimeSerializerBase<Date> withFormat(Boolean timestamp, DateFormat customFormat) {
                return null;
            }

            @Override
            protected long _timestamp(Date value) {
                return 0;
            }

            @Override
            public void serialize(Date value, JsonGenerator gen, SerializerProvider serializers) throws IOException {

            }

        };
        assertTrue(serializer.isEmpty(null, null));
    }

    @Test
    public void testIsEmptyTrue() throws Exception {
        DateTimeSerializerBase<Date> serializer = new DateTimeSerializerBase<Date>(Date.class, false, null) {
            @Override
            public DateTimeSerializerBase<Date> withFormat(Boolean timestamp, DateFormat customFormat) {
                return null;
            }

            @Override
            protected long _timestamp(Date value) {
                return 0;
            }

            @Override
            public void serialize(Date value, JsonGenerator gen, SerializerProvider serializers) throws IOException {

            }

        };
        assertTrue(serializer.isEmpty(new SerializerProvider(null, new SerializationConfig(null, null, new DateFormatSymbols(Locale.getDefault())), null), null));
    }

    @Test
    public void testIsEmptyFalse() throws Exception {
        DateTimeSerializerBase<Date> serializer = new DateTimeSerializerBase<Date>(Date.class, false, null) {
            @Override
            public DateTimeSerializerBase<Date> withFormat(Boolean timestamp, DateFormat customFormat) {
                return null;
            }

            @Override
            protected long _timestamp(Date value) {
                return new Date().getTime();
            }

            @Override
            public void serialize(Date value, JsonGenerator gen, SerializerProvider serializers) throws IOException {

            }

        };
        assertFalse(serializer.isEmpty(new SerializerProvider(null, new SerializationConfig(null, null, new DateFormatSymbols(Locale.getDefault())), null), new Date()));
    }

    @Test
    public void testGetSchemaAsTimestamp() throws Exception {
        DateTimeSerializerBase<Date> serializer = new DateTimeSerializerBase<Date>(Date.class, false, null) {
            @Override
            public DateTimeSerializerBase<Date> withFormat(Boolean timestamp, DateFormat customFormat) {
                return null;
            }

            @Override
            protected long _timestamp(Date value) {
                return 0;
            }

            @Override
            public void serialize(Date value, JsonGenerator gen, SerializerProvider serializers) throws IOException {

            }

        };
        assertEquals("{\"type\":\"number\",\"format\":\"utc-millisec\"}", serializer.getSchema(new SerializerProvider(null, new SerializationConfig(null, null, new DateFormatSymbols(Locale.getDefault())), null), null).toString());
    }

    @Test
    public void testGetSchemaAsString() throws Exception {
        DateTimeSerializerBase<Date> serializer = new DateTimeSerializerBase<Date>(Date.class, false, null) {
            @Override
            public DateTimeSerializerBase<Date> withFormat(Boolean timestamp, DateFormat customFormat) {
                return null;
            }

            @Override
            protected long _timestamp(Date value) {
                return 0;
            }

            @Override
            public void serialize(Date value, JsonGenerator gen, SerializerProvider serializers) throws IOException {

            }

        };
        assertEquals("{\"type\":\"string\",\"format\":\"date-time\"}", serializer.getSchema(new SerializerProvider(null, new SerializationConfig(null, null, new DateFormatSymbols(Locale.getDefault())), null), null).toString());
    }

    @Test
    public void testAcceptJsonFormatVisitorAsNumber() throws Exception {
        DateTimeSerializerBase<Date> serializer = new DateTimeSerializerBase<Date>(Date.class, false, null) {
            @Override
            public DateTimeSerializerBase<Date> withFormat(Boolean timestamp, DateFormat customFormat) {
                return null;
            }

            @Override
            protected long _timestamp(Date value) {
                return 0;
            }

            @Override
            public void serialize(Date value, JsonGenerator gen, SerializerProvider serializers) throws IOException {

            }

        };
        JsonFormatVisitorWrapper visitor = new JsonFormatVisitorWrapper.Base();
        serializer.acceptJsonFormatVisitor(visitor, null);
        assertEquals("number", visitor.getProvider().getShape().name().toLowerCase());
    }

    @Test
    public void testAcceptJsonFormatVisitorAsString() throws Exception {
        DateTimeSerializerBase<Date> serializer = new DateTimeSerializerBase<Date>(Date.class, false, null) {
            @Override
            public DateTimeSerializerBase<Date> withFormat(Boolean timestamp, DateFormat customFormat) {
                return null;
            }

            @Override
            protected long _timestamp(Date value) {
                return 0;
            }

            @Override
            public void serialize(Date value, JsonGenerator gen, SerializerProvider serializers) throws IOException {

            }

        };
        JsonFormatVisitorWrapper visitor = new JsonFormatVisitorWrapper.Base();
        serializer.acceptJsonFormatVisitor(visitor, null);
        assertEquals("string", visitor.getProvider().getShape().name().toLowerCase());
    }
}
