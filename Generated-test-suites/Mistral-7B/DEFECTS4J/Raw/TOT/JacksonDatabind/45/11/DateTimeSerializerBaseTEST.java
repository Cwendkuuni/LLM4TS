import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Locale;
import java.util.TimeZone;

import static org.junit.Assert.assertEquals;

public class DateTimeSerializerBaseTest {

    private ObjectMapper objectMapper;

    @Before
    public void setup() {
        objectMapper = new ObjectMapper();
        objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
    }

    @Test
    public void testSerializeDateWithDefaultFormat() throws IOException {
        DateTimeSerializerBase<ZonedDateTime> dateTimeSerializer = new DateTimeSerializerBase<ZonedDateTime>(
                ZonedDateTime.class, false, null);
        ZonedDateTime zonedDateTime = ZonedDateTime.now();

        String jsonString = objectMapper.writerWithLocalLocalization(Locale.US).writeValueAsString(zonedDateTime);

        JsonParser jsonParser = objectMapper.getFactory().createParser(jsonString);
        ZonedDateTime deserializedZonedDateTime = objectMapper.readValue(jsonParser, ZonedDateTime.class);

        assertEquals(zonedDateTime, deserializedZonedDateTime);
    }

    @Test
    public void testSerializeDateWithCustomFormat() throws IOException, JsonProcessingException {
        DateTimeSerializerBase<Instant> dateTimeSerializer = new DateTimeSerializerBase<Instant>(
                Instant.class, false, new SimpleDateFormat("yyyy-MM-dd HH:mm:ss z"));

        Instant instant = Instant.now();

        String jsonString = objectMapper.writerWithLocalLocalization(Locale.US).writeValueAsString(instant);

        JsonParser jsonParser = objectMapper.getFactory().createParser(jsonString);
        Instant deserializedInstant = objectMapper.readValue(jsonParser, Instant.class);

        assertEquals(instant, deserializedInstant);
    }

    @Test
    public void testSerializeDateWithUseTimestamp() throws IOException {
        DateTimeSerializerBase<ZonedDateTime> dateTimeSerializer = new DateTimeSerializerBase<ZonedDateTime>(
                ZonedDateTime.class, true, null);

        ZonedDateTime zonedDateTime = ZonedDateTime.now();

        String jsonString = objectMapper.writeValueAsString(zonedDateTime);

        JsonParser jsonParser = objectMapper.getFactory().createParser(jsonString);
        long deserializedLong = jsonParser.readValueAsLong();

        assertEquals(zonedDateTime.toInstant().toEpochMilli(), deserializedLong);
    }

    @Test(expected = JsonMappingException.class)
    public void testSerializeDateWithInvalidFormat() throws IOException {
        DateTimeSerializerBase<String> dateTimeSerializer = new DateTimeSerializerBase<String>(
                String.class, false, null);

        String jsonString = "invalid_json_string";

        objectMapper.writerWithLocalLocalization(Locale.US).writeValueAsString(jsonString);
    }
}