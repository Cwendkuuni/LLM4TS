import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class DateTimeSerializerBaseTest {

    private ObjectMapper objectMapper;

    @Before
    public void setUp() {
        objectMapper = new ObjectMapper();
    }

    @Test
    public void testDateTimeSerializerBaseWithDefaultSettings() throws JsonMappingException, JsonProcessingException {
        DateTimeSerializerBase<Date> dateTimeSerializerBase = objectMapper.findValueSerializer(Date.class);
        SerializerProvider serializers = objectMapper.getSerializerProvider();
        Date date = new Date();

        ObjectNode jsonNode = objectMapper.createObjectNode();
        jsonNode.put("date", date);

        JsonNode result = dateTimeSerializerBase.serializeValues(jsonNode, serializers, null);
        assertNotNull(result);
        assertEquals(objectMapper.valueToTree(date).toString(), result.toString());

        Date deserializedDate = objectMapper.treeToValue(result, Date.class);
        assertEquals(date, deserializedDate);
    }

    @Test
    public void testDateTimeSerializerBaseWithCustomFormat() throws JsonMappingException, JsonProcessingException {
        DateTimeSerializerBase<Date> dateTimeSerializerBase = objectMapper.findValueSerializer(Date.class)
                .withFormat(Boolean.FALSE, new SimpleDateFormat("dd-MM-yyyy HH:mm:ss", Locale.US));

        SerializerProvider serializers = objectMapper.getSerializerProvider();
        Date date = new Date();

        ObjectNode jsonNode = objectMapper.createObjectNode();
        jsonNode.put("date", date);

        JsonNode result = dateTimeSerializerBase.serializeValues(jsonNode, serializers, null);
        assertNotNull(result);
        assertEquals("14-06-2023 10:30:00", result.get("date").asText());

        Date deserializedDate = objectMapper.treeToValue(result, Date.class);
        assertEquals(date, deserializedDate);
    }

    @Test(expected = JsonMappingException.class)
    public void testDateTimeSerializerBaseWithInvalidCustomFormat() throws JsonMappingException, JsonProcessingException {
        DateTimeSerializerBase<Date> dateTimeSerializerBase = objectMapper.findValueSerializer(Date.class)
                .withFormat(Boolean.FALSE, new SimpleDateFormat("dd-MM-yyyy HH:mm:sszz"));

        SerializerProvider serializers = objectMapper.getSerializerProvider();
        Date date = new Date();

        ObjectNode jsonNode = objectMapper.createObjectNode();
        jsonNode.put("date", date);

        JsonNode result = dateTimeSerializerBase.serializeValues(jsonNode, serializers, null);
        result.toString(); // Force serialization to throw exception
    }

    @Test(expected = JsonMappingException.class)
    public void testDateTimeSerializerBaseWithNullCustomFormat() throws JsonMappingException, JsonProcessingException {
        DateTimeSerializerBase<Date> dateTimeSerializerBase = objectMapper.findValueSerializer(Date.class);

        SerializerProvider serializers = objectMapper.getSerializerProvider();
        Date date = new Date();

        ObjectNode jsonNode = objectMapper.createObjectNode();
        jsonNode.put("date", date);

        JsonNode result = dateTimeSerializerBase.serializeValues(jsonNode, serializers, null)
                .withFormat(Boolean.FALSE, null);

        result.toString(); // Force serialization to throw exception
    }

    @Test
    public void testDateTimeSerializerBaseWithTimestamp() throws JsonMappingException, JsonProcessingException, IOException {
        DateTimeSerializerBase<Date> dateTimeSerializerBase = objectMapper.findValueSerializer(Date.class)
                .withFormat(Boolean.TRUE, null);

        SerializerProvider serializers = objectMapper.getSerializerProvider();
        Date date = new Date();

        ObjectNode jsonNode = objectMapper.createObjectNode();
        jsonNode.put("date", date);

        JsonNode result = dateTimeSerializerBase.serializeValues(jsonNode, serializers, null);
        assertNotNull(result);
        assertEquals(date.getTime(), result.get("date").longValue());

        Date deserializedDate = objectMapper.treeToValue(result, Date.class);
        assertEquals(date, deserializedDate);
    }

    @After
    public void tearDown() {
        objectMapper = null;
    }
}