import static org.junit.Assert.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.DateTimeSerializerBase;

public class DateTimeSerializerBaseTest {
    private DateTimeSerializerBase<Date> serializer;

    @Before
    public void setUp() {
        serializer = new TestDateSerializer();
    }

    @After
    public void tearDown() {
        serializer = null;
    }

    @Test
    public void testSerialize_NullValue_ShouldNotThrowException() {
        try {
            serializer.serialize(null, null, null);
        } catch (Exception e) {
            fail("Should not throw exception");
        }
    }

    @Test
    public void testSerialize_UseTimestampEnabled_ShouldSerializeAsTimestamp() throws Exception {
        SerializerProvider provider = new ObjectMapper().getSerializerProvider();
        provider.enable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        TestDateSerializer timestampSerializer = new TestDateSerializer();

        assertEquals(Boolean.TRUE, timestampSerializer._useTimestamp);

        Date date = new Date();
        DateFormat defaultFormat = provider.getConfig().getDateFormat();
        String expected = Long.toString(defaultFormat.parse(defaultFormat.format(date)).getTime());

        StringBuilder sb = new StringBuilder();
        timestampSerializer.serialize(date, new TestJsonGenerator(sb), provider);
        String actual = sb.toString();

        assertEquals(expected, actual);
    }

    @Test
    public void testSerialize_UseTimestampDisabled_ShouldSerializeAsStringWithDefaultFormat() throws Exception {
        SerializerProvider provider = new ObjectMapper().getSerializerProvider();
        provider.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        TestDateSerializer stringSerializer = new TestDateSerializer();

        assertEquals(Boolean.FALSE, stringSerializer._useTimestamp);

        Date date = new Date();
        DateFormat defaultFormat = provider.getConfig().getDateFormat();
        String expected = defaultFormat.format(date);

        StringBuilder sb = new StringBuilder();
        stringSerializer.serialize(date, new TestJsonGenerator(sb), provider);
        String actual = sb.toString();

        assertEquals(expected, actual);
    }

    @Test
    public void testSerialize_UseTimestampDisabled_CustomFormatNull_ShouldSerializeAsStringWithDefaultFormat() throws Exception {
        SerializerProvider provider = new ObjectMapper().getSerializerProvider();
        provider.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        TestDateSerializer stringSerializer = new TestDateSerializer();

        assertEquals(Boolean.FALSE, stringSerializer._useTimestamp);
        assertNull(stringSerializer._customFormat);

        Date date = new Date();
        DateFormat defaultFormat = provider.getConfig().getDateFormat();
        String expected = defaultFormat.format(date);

        StringBuilder sb = new StringBuilder();
        stringSerializer.serialize(date, new TestJsonGenerator(sb), provider);
        String actual = sb.toString();

        assertEquals(expected, actual);
    }

    @Test
    public void testSerialize_UseTimestampDisabled_CustomFormatNotNull_ShouldSerializeAsStringWithCustomFormat() throws Exception {
        SerializerProvider provider = new ObjectMapper().getSerializerProvider();
        provider.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        TestDateSerializer stringSerializer = new TestDateSerializer();
        stringSerializer._customFormat = new SimpleDateFormat("yyyy/MM/dd");

        assertEquals(Boolean.FALSE, stringSerializer._useTimestamp);
        assertNotNull(stringSerializer._customFormat);

        Date date = new Date();
        DateFormat customFormat = stringSerializer._customFormat;
        String expected = customFormat.format(date);

        StringBuilder sb = new StringBuilder();
        stringSerializer.serialize(date, new TestJsonGenerator(sb), provider);
        String actual = sb.toString();

        assertEquals(expected, actual);
    }

    @Test
    public void testCreateContextual_FormatOverridesNull_ShouldReturnSameSerializer() throws Exception {
        SerializerProvider provider = new ObjectMapper().getSerializerProvider();
        ObjectMapper mapper = new ObjectMapper();
        TestDateSerializer serializer = new TestDateSerializer();

        JsonSerializer<?> result = serializer.createContextual(provider, null);

        assertSame(serializer, result);
    }

    @Test
    public void testCreateContextual_FormatOverridesNumeric_ShouldReturnSerializerWithUseTimestampEnabled() throws Exception {
        SerializerProvider provider = new ObjectMapper().getSerializerProvider();
        ObjectMapper mapper = new ObjectMapper();
        TestDateSerializer serializer = new TestDateSerializer();

        JsonFormat.Value format = new JsonFormat.Value();
        format.setShape(JsonFormat.Shape.NUMBER);

        JsonSerializer<?> result = serializer.createContextual(provider, format);

        assertEquals(Boolean.TRUE, ((TestDateSerializer) result)._useTimestamp);
    }

    @Test
    public void testCreateContextual_FormatOverridesPattern_ShouldReturnSerializerWithCustomFormat() throws Exception {
        SerializerProvider provider = new ObjectMapper().getSerializerProvider();
        ObjectMapper mapper = new ObjectMapper();
        TestDateSerializer serializer = new TestDateSerializer();

        JsonFormat.Value format = new JsonFormat.Value();
        format.setPattern("yyyy/MM/dd");

        JsonSerializer<?> result = serializer.createContextual(provider, format);

        assertNotNull(((TestDateSerializer) result)._customFormat);
        assertEquals("yyyy/MM/dd", ((TestDateSerializer) result)._customFormat.toPattern());
    }

    @Test
    public void testCreateContextual_FormatOverridesLocale_ShouldReturnSerializerWithCustomFormatAndLocale() throws Exception {
        SerializerProvider provider = new ObjectMapper().getSerializerProvider();
        ObjectMapper mapper = new ObjectMapper();
        TestDateSerializer serializer = new TestDateSerializer();

        JsonFormat.Value format = new JsonFormat.Value();
        format.setLocale(Locale.US);

        JsonSerializer<?> result = serializer.createContextual(provider, format);

        assertNotNull(((TestDateSerializer) result)._customFormat);
        assertEquals(Locale.US, ((TestDateSerializer) result)._customFormat.getLocale());
    }

    @Test
    public void testCreateContextual_FormatOverridesTimeZone_ShouldReturnSerializerWithCustomFormatAndTimeZone() throws Exception {
        SerializerProvider provider = new ObjectMapper().getSerializerProvider();
        ObjectMapper mapper = new ObjectMapper();
        TestDateSerializer serializer = new TestDateSerializer();

        JsonFormat.Value format = new JsonFormat.Value();
        format.setTimeZone(TimeZone.getTimeZone("GMT+0:00"));

        JsonSerializer<?> result = serializer.createContextual(provider, format);

        assertNotNull(((TestDateSerializer) result)._customFormat);
        assertEquals(TimeZone.getTimeZone("GMT+0:00"), ((TestDateSerializer) result)._customFormat.getTimeZone());
    }

    // Other test methods for remaining methods in the class

    private class TestDateSerializer extends DateTimeSerializerBase<Date> {
        public TestDateSerializer() {
            super(Date.class, Boolean.FALSE, null);
        }

        @Override
        public DateTimeSerializerBase<Date> withFormat(Boolean timestamp, DateFormat customFormat) {
            return null;
        }

        @Override
        protected long _timestamp(Date value) {
            return 0;
        }

        @Override
        public void serialize(Date value, TestJsonGenerator gen, SerializerProvider serializers) {
            // No implementation needed for testing
        }
    }

    private class TestJsonGenerator {
        private StringBuilder sb;

        public TestJsonGenerator(StringBuilder sb) {
            this.sb = sb;
        }

        public void writeString(String value) {
            sb.append(value);
        }
    }
}