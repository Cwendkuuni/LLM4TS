import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import com.fasterxml.jackson.databind.ser.SerializerProvider;
import com.fasterxml.jackson.databind.util.StdDateFormat;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.lang.reflect.Type;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;
import java.util.concurrent.atomic.AtomicReference;

import static org.mockito.Mockito.mock;

public class DateTimeSerializerBaseTest {

    private DateTimeSerializerBase<?> dateTimeSerializerBase;

    @Before
    public void setup() {
        dateTimeSerializerBase = new DateTimeSerializerBase<String>(String.class, true, createCustomFormat()) {
            @Override
            public DateTimeSerializerBase<String> withFormat(Boolean timestamp, DateFormat customFormat) {
                return null;
            }

            @Override
            protected long _timestamp(String value) {
                return 0;
            }

            @Override
            public void serialize(String value, JsonGenerator gen, SerializerProvider serializers) throws IOException {

            }
        };
    }

    private DateFormat createCustomFormat() {
        return new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ", Locale.US);
    }

    @Test
    public void testCreateContextualWithNullProperty() throws Exception {
        BeanProperty property = null;
        SerializerProvider provider = mock(SerializerProvider.class);

        ContextualSerializer serializer = dateTimeSerializerBase.createContextual(provider, property);

        Assert.assertEquals(dateTimeSerializerBase, serializer);
    }

    @Test
    public void testCreateContextualWithNumericShape() throws Exception {
        BeanProperty property = mock(BeanProperty.class);
        JsonSerializer<?> expectedSerializer = mock(JsonSerializer.class);
        SerializerProvider provider = mock(SerializerProvider.class);

        ContextualSerializer serializer = dateTimeSerializerBase.createContextual(provider, property);

        Assert.assertEquals(expectedSerializer, serializer);
    }

    @Test
    public void testCreateContextualWithPattern() throws Exception {
        BeanProperty property = mock(BeanProperty.class);
        SimpleDateFormat expectedFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ", Locale.US);

        ContextualSerializer serializer = dateTimeSerializerBase.createContextual(provider, property);

        Assert.assertNotNull(serializer);
        Assert.assertEquals(expectedFormat, serializer._customFormat);
    }

    @Test
    public void testCreateContextualNoChange() throws Exception {
        BeanProperty property = mock(BeanProperty.class);
        SimpleDateFormat currentFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ", Locale.US);
        DateFormat expectedFormat = new SimpleDateFormat(currentFormat.toPattern(), Locale.US);

        ContextualSerializer serializer = dateTimeSerializerBase.createContextual(provider, property);

        Assert.assertNotNull(serializer);
        Assert.assertEquals(expectedFormat, serializer._customFormat);
    }

    @Test
    public void testCreateContextualLocaleChange() throws Exception {
        BeanProperty property = mock(BeanProperty.class);
        SimpleDateFormat currentFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ", Locale.US);
        DateFormat expectedFormat = new SimpleDateFormat(currentFormat.toPattern(), Locale.FRANCE);
        expectedFormat.setTimeZone(currentFormat.getTimeZone());

        ContextualSerializer serializer = dateTimeSerializerBase.createContextual(provider, property);

        Assert.assertNotNull(serializer);
        Assert.assertEquals(expectedFormat, serializer._customFormat);
    }

    @Test
    public void testSerializeEmptyString() throws IOException {
        JsonGenerator gen = mock(JsonGenerator.class);
        SerializerProvider provider = mock(SerializerProvider.class);
        String value = "";

        dateTimeSerializerBase.serialize(value, gen, provider);

        //todo: add assertion
    }

    @Test
    public void testSerializeNull() throws IOException {
        JsonGenerator gen = mock(JsonGenerator.class);
        SerializerProvider provider = mock(SerializerProvider.class);
        String value = null;

        dateTimeSerializerBase.serialize(value, gen, provider);

        //todo: add assertion
    }

    @Test
    public void testSerializeWithCustomFormat() throws IOException {
        DateFormat expectedFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ", Locale.US);
        expectedFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
        JsonGenerator gen = mock(JsonGenerator.class);
        SerializerProvider provider = mock(SerializerProvider.class);
        String value = "2020-02-02T12:30:00+0000";

        dateTimeSerializerBase.serialize(value, gen, provider);

        //todo: add assertion
    }

    @Test
    public void testIsEmpty() {
        SerializerProvider serializers = mock(SerializerProvider.class);
        String value = "2020-02-02T12:30:00+0000";

        boolean result = dateTimeSerializerBase.isEmpty(serializers, value);

        Assert.assertFalse(result);
    }

    @Test
    public void testIsEmptyWithTimestampZero() {
        SerializerProvider serializers = mock(SerializerProvider.class);
        String value = "";

        boolean result = dateTimeSerializerBase.isEmpty(serializers, value);

        Assert.assertTrue(result);
    }

    @Test
    public void testGetSchemaAsNumber() {
        SerializerProvider serializers = mock(SerializerProvider.class);
        Type typeHint = mock(Type.class);

        JsonNode schema = dateTimeSerializerBase.getSchema(serializers, typeHint);

        //todo: add assertion
    }

    @Test
    public void testGetSchemaAsString() {
        SerializerProvider serializers = mock(SerializerProvider.class);
        Type typeHint = mock(Type.class);

        JsonNode schema = dateTimeSerializerBase.getSchema(serializers, typeHint);

        //todo: add assertion
    }

    @Test
    public void testAcceptJsonFormatVisitorWithAsNumber() throws JsonMappingException {
        JsonFormatVisitorWrapper visitor = mock(JsonFormatVisitorWrapper.class);
        JavaType typeHint = mock(JavaType.class);

        dateTimeSerializerBase.acceptJsonFormatVisitor(visitor, typeHint);

        //todo: add assertion
    }

    @Test
    public void testAcceptJsonFormatVisitorWithAsString() throws JsonMappingException {
        JsonFormatVisitorWrapper visitor = mock(JsonFormatVisitorWrapper.class);
        JavaType typeHint = mock(JavaType.class);

        dateTimeSerializerBase.acceptJsonFormatVisitor(visitor, typeHint);

        //todo: add assertion
    }

    @Test
    public void testTimestamp() {
        String value = "2020-02-02T12:30:00+0000";

        long timestamp = dateTimeSerializerBase._timestamp(value);

        Assert.assertEquals(0, timestamp);
    }

    @Test
    public void testSerializeAsString() throws IOException {
        Date value = parseDate("2020-02-02T12:30:00+0000");
        JsonGenerator g = mock(JsonGenerator.class);
        SerializerProvider provider = mock(SerializerProvider.class);

        dateTimeSerializerBase._serializeAsString(value, g, provider);

        //todo: add assertion
    }

    private Date parseDate(String value) {
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ", Locale.US);
        try {
            return format.parse(value);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

}