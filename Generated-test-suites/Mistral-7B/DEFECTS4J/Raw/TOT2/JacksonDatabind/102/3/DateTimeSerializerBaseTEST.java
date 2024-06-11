import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class DateTimeSerializerBaseTest {

    private ObjectMapper mapper;

    @Before
    public void setup() {
        mapper = new ObjectMapper();
    }

    @Test
    public void testSerializingDateWithDefaultSettings() throws JsonGenerationException, IOException {
        DateTimeSerializerBase<Date> dateSerializer = new DateTimeSerializerBase<Date>(Date.class, false, null);
        Date date = new Date();

        JsonNode result = mapper.writeValueAsTree(date, dateSerializer);

        JsonNode expected = mapper.readTree("{\"@type\":\"Date\",\"@value\":\"" + date.toISO8601String() + "\"}");

        assertEquals(expected, result);
    }

    @Test
    public void testSerializingDateWithCustomFormat() throws JsonGenerationException, IOException {
        DateTimeSerializerBase<Date> dateSerializer = new DateTimeSerializerBase<Date>(Date.class, false, new SimpleDateFormat("yyyy-MM-dd"));
        Date date = new Date();

        JsonNode result = mapper.writeValueAsTree(date, dateSerializer);

        JsonNode expected = mapper.readTree("{\"@type\":\"String\",\"@value\":\"2023-03-29\"}"); // Replace with the actual date string

        assertEquals(expected, result);
    }

    @Test
    public void testSerializingDateAsTimestamp() throws JsonGenerationException, IOException {
        DateTimeSerializerBase<Date> dateSerializer = new DateTimeSerializerBase<Date>(Date.class, true, null);
        Date date = new Date();

        JsonNode result = mapper.writeValueAsTree(date, dateSerializer);

        JsonNode expected = mapper.readTree("{\"@type\":\"Number\",\"@value\":\"" + date.getTime() + "\"}");

        assertEquals(expected, result);
    }

    @Test(expected = JsonMappingException.class)
    public void testSerializingNullDate() throws JsonGenerationException, IOException {
        DateTimeSerializerBase<Date> dateSerializer = new DateTimeSerializerBase<Date>(Date.class, false, null);

        mapper.writeValueAsTree(null, dateSerializer);
    }

    @Test
    public void testDeserializingDateWithDefaultSettings() throws JsonGenerationException, IOException {
        DateTimeSerializerBase<Date> dateSerializer = new DateTimeSerializerBase<Date>(Date.class, false, null);
        String jsonString = "{\"@type\":\"Date\",\"@value\":\"2023-03-29T14:30:00.000Z\"}";

        Date date = mapper.readValue(jsonString, dateSerializer.handledType());

        assertEquals(new Date(), date);
    }

    @Test
    public void testDeserializingDateWithCustomFormat() throws JsonGenerationException, IOException {
        DateTimeSerializerBase<Date> dateSerializer = new DateTimeSerializerBase<Date>(Date.class, false, new SimpleDateFormat("yyyy-MM-dd"));
        String jsonString = "{\"@type\":\"String\",\"@value\":\"2023-03-29\"}";

        Date date = mapper.readValue(jsonString, dateSerializer.handledType());

        assertEquals(new Date(), date); // Assuming the test system's date is the same as March 29, 2023
    }

    @Test
    public void testDeserializingDateAsTimestamp() throws JsonGenerationException, IOException {
        DateTimeSerializerBase<Date> dateSerializer = new DateTimeSerializerBase<Date>(Date.class, true, null);
        String jsonString = "{\"@type\":\"Number\",\"@value\":\"1679699200000\"}"; // Replace with a timestamp corresponding to a specific date

        Date date = mapper.readValue(jsonString, dateSerializer.handledType());

        assertEquals(new Date(1679699200000L), date);
    }

    @Test(expected = JsonMappingException.class)
    public void testDeserializingInvalidJson() throws IOException {
        DateTimeSerializerBase<Date> dateSerializer = new DateTimeSerializerBase<Date>(Date.class, false, null);
        String jsonString = "invalid";

        mapper.readValue(jsonString, dateSerializer.handledType());
    }
}