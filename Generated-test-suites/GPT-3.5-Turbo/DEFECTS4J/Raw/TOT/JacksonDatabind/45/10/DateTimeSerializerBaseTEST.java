
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

class DateTimeSerializerBaseTest {

    @Test
    void testSerializeWithTimestamp() throws IOException {
        DateTimeSerializerBase<Date> serializer = new DateTimeSerializerBase<>(Date.class, true, null) {
            @Override
            protected long _timestamp(Date value) {
                return value.getTime();
            }

            @Override
            public void serialize(Date value, JsonGenerator gen, SerializerProvider serializers) throws IOException {

            }
        };

        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, true);

        Date date = new Date();

        String expected = "{\"timestamp\":" + date.getTime() + "}";
        String actual = mapper.writeValueAsString(date);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void testSerializeWithStringFormat() throws IOException {
        DateTimeSerializerBase<Date> serializer = new DateTimeSerializerBase<>(Date.class, false, null) {
            @Override
            protected long _timestamp(Date value) {
                return value.getTime();
            }

            @Override
            public void serialize(Date value, JsonGenerator gen, SerializerProvider serializers) throws IOException {

            }
        };

        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);

        Date date = new Date();

        DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ", Locale.US);
        df.setTimeZone(TimeZone.getTimeZone("GMT"));

        String expected = "{\"date\":\"" + df.format(date) + "\"}";
        String actual = mapper.writeValueAsString(date);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void testSerializeWithCustomFormat() throws IOException {
        final String pattern = "yyyy/MM/dd HH:mm:ss";
        final DateFormat customFormat = new SimpleDateFormat(pattern, Locale.US);

        DateTimeSerializerBase<Date> serializer = new DateTimeSerializerBase<>(Date.class, false, customFormat) {
            @Override
            protected long _timestamp(Date value) {
                return value.getTime();
            }

            @Override
            public void serialize(Date value, JsonGenerator gen, SerializerProvider serializers) throws IOException {

            }
        };

        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);

        Date date = new Date();

        String expected = "{\"date\":\"" + customFormat.format(date) + "\"}";
        String actual = mapper.writeValueAsString(date);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void testCreateContextual() throws IOException {
        class MockedBeanProperty extends BeanProperty {
            @Override
            public AnnotatedMember getMember() {
                return null;
            }
        }

        SerializerProvider serializerProvider = null;

        DateTimeSerializerBase<Date> serializer = new DateTimeSerializerBase<>(Date.class, false, null) {
            @Override
            protected long _timestamp(Date value) {
                return value.getTime();
            }

            @Override
            public void serialize(Date value, JsonGenerator gen, SerializerProvider serializers) throws IOException {

            }
        };

        DateTimeSerializerBase<Date> contextualSerializer = serializer.createContextual(serializerProvider, new MockedBeanProperty());

        Assertions.assertEquals(serializer, contextualSerializer);
    }

    @Test
    void testIsEmpty() throws IOException {
        DateTimeSerializerBase<Date> serializer = new DateTimeSerializerBase<>(Date.class, false, null) {
            @Override
            protected long _timestamp(Date value) {
                return value.getTime();
            }

            @Override
            public void serialize(Date value, JsonGenerator gen, SerializerProvider serializers) throws IOException {

            }
        };

        Date date = new Date();

        Assertions.assertFalse(serializer.isEmpty(date));
    }

    @Test
    void testGetSchema() throws IOException {
        DateTimeSerializerBase<Date> serializer = new DateTimeSerializerBase<>(Date.class, false, null) {
            @Override
            protected long _timestamp(Date value) {
                return value.getTime();
            }

            @Override
            public void serialize(Date value, JsonGenerator gen, SerializerProvider serializers) throws IOException {

            }
        };

        ObjectMapper mapper = new ObjectMapper();

        String expected = "{\"type\":\"string\",\"format\":\"date-time\"}";
        String actual = mapper.writeValueAsString(serializer.getSchema(null, null));
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void testAcceptJsonFormatVisitor() throws IOException {
        DateTimeSerializerBase<Date> serializer = new DateTimeSerializerBase<>(Date.class, false, null) {
            @Override
            protected long _timestamp(Date value) {
                return value.getTime();
            }

            @Override
            public void serialize(Date value, JsonGenerator gen, SerializerProvider serializers) throws IOException {

            }
        };

        ObjectMapper mapper = new ObjectMapper();

        JsonFormatVisitorWrapper visitor = new JsonFormatVisitorWrapper.Base(StdDateFormat.DATE_FORMAT_STR_ISO8601);
        JavaType typeHint = null;

        String expected = "date-time";
        String actual = mapper.writeValueAsString(serializer.acceptJsonFormatVisitor(visitor, typeHint));
        Assertions.assertEquals(expected, actual);
    }

}

