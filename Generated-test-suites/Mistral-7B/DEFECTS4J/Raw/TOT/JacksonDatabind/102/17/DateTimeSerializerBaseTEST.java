import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.ser.std.DateTimeSerializerBase;
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

    private static ObjectMapper mapper = new ObjectMapper();

    @Test
    public void testSerializationWithDefaultFormat() throws JsonProcessingException {
        DateTimeSerializerBase<Date> serializer = new DateTimeSerializerBase<Date>(Date.class, false, null);
        Date date = new Date();

        ObjectNode result = mapper.valueToTree(date);
        assertEquals("\"2022-12-31T23:59:59.000Z\"", result.toString());
    }

    @Test
    public void testSerializationWithCustomFormat() throws JsonProcessingException {
        DateTimeSerializerBase<Date> serializer = new DateTimeSerializerBase<Date>(Date.class, false, new SimpleDateFormat("dd-MM-yyyy HH:mm:ss zzz"));
        Date date = new Date();

        ObjectNode result = mapper.valueToTree(date);
        assertEquals("\"31-12-2022 23:59:59 +0000\"", result.toString());
    }

    @Test
    public void testSerializationWithTimestamp() throws JsonProcessingException {
        DateTimeSerializerBase<Date> serializer = new DateTimeSerializerBase<Date>(Date.class, true, null);
        Date date = new Date();

        ObjectNode result = mapper.valueToTree(date);
        assertEquals("2022-12-31T23:59:59.000Z", result.toString());
    }

    @Test
    public void testDeserializationWithDefaultFormat() throws JsonMappingException, IOException {
        String json = "\"2022-12-31T23:59:59.000Z\"";
        ObjectNode jsonNode = mapper.readValue(json, ObjectNode.class);

        Date date = mapper.treeToValue(jsonNode, Date.class);
        assertNotNull(date);
    }

    @Test
    public void testDeserializationWithCustomFormat() throws JsonMappingException, IOException {
        String json = "\"31-12-2022 23:59:59 +0000\"";
        ObjectNode jsonNode = mapper.readValue(json, ObjectNode.class);

        Date date = mapper.treeToValue(jsonNode, Date.class);
        assertNotNull(date);
    }

    @Test(expected = JsonMappingException.class)
    public void testDeserializationWithInvalidFormat() throws JsonMappingException, IOException {
        String json = "\"invalid format\"";
        ObjectNode jsonNode = mapper.readValue(json, ObjectNode.class);

        mapper.treeToValue(jsonNode, Date.class);
    }

    @Test(expected = JsonMappingException.class)
    public void testDeserializationWithNullFormat() throws JsonMappingException, IOException {
        String json = "null";
        ObjectNode jsonNode = mapper.readValue(json, ObjectNode.class);

        mapper.treeToValue(jsonNode, Date.class);
    }
}