import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.std.DateTimeSerializerBase;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.TimeZone;

public class DateTimeSerializerBaseTest {

    private DateTimeSerializerBase<Long> dateTimeSerializer;
    private ObjectMapper objectMapper;

    @Before
    public void setup() {
        SimpleModule module = new SimpleModule();
        module.addSerializer(Long.class, new DateTimeSerializerBase<Long>(Long.class, false, null));
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(module);
        objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        dateTimeSerializer = objectMapper.findValueSerializer(Long.class, null);
    }

    @Test
    public void testDefaultSerialization() throws JsonGenerationException, JsonMappingException {
        Long date = 1592819200000L; // 1st Jan 2020, 00:02:40
        String json = dateTimeSerializer.serialize(date, new JsonGenerator(), new SerializerProvider());
        assertEquals("\"2020-01-01T00:02:40.000Z\"", json);
    }

    @Test
    public void testCustomFormatSerialization() throws JsonGenerationException, JsonMappingException {
        DateFormat customFormat = new SimpleDateFormat("dd-MM-yyyy hh:mm a");
        DateTimeSerializerBase<Long> customSerializer = new DateTimeSerializerBase<>(Long.class, false, customFormat);
        String json = customSerializer.serialize(1592819200000L, new JsonGenerator(), new SerializerProvider());
        assertEquals("\"01-01-2020 00:02 AM\"", json);
    }

    @Test(expected = JsonMappingException.class)
    public void testSerializationWithNullValue() throws JsonGenerationException, JsonMappingException {
        String json = dateTimeSerializer.serialize(null, new JsonGenerator(), new SerializerProvider());
        fail("Expected JsonMappingException.class but got null");
    }

    @Test(expected = JsonGenerationException.class)
    public void testSerializationWithInvalidTimestamp() throws JsonGenerationException, JsonMappingException {
        DateTimeSerializerBase<Long> invalidSerializer = new DateTimeSerializerBase<>(Long.class, false, null);
        String json = invalidSerializer.serialize(-1L, new JsonGenerator(), new SerializerProvider());
        fail("Expected JsonGenerationException.class but got \"\"");
    }
}