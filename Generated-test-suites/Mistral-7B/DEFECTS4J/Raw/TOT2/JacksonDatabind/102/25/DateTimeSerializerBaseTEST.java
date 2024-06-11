import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.json.JsonWriter;
import com.fasterxml.jackson.databind.ser.std.DateTimeSerializerBase;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class DateTimeSerializerBaseTest {

    private DateTimeSerializerBase<Date> dateTimeSerializer;
    private ObjectMapper objectMapper;

    @Before
    public void setup() {
        dateTimeSerializer = new DateTimeSerializerBase<Date>(Date.class, false, null);
        objectMapper = new ObjectMapper();
    }

    @Test
    public void testSerializeDate() throws JsonGenerationException, JsonMappingException, IOException {
        Date date = new Date();
        JsonWriter jsonWriter = objectMapper.getFactory().createJsonWriter(new java.io.FileOutputStream("output.json"));
        dateTimeSerializer.serialize(date, jsonWriter, new SerializerProvider());
        jsonWriter.close();

        ObjectMapper mapper = new ObjectMapper();
        Date deserializedDate = mapper.readValue(new java.io.File("output.json"), Date.class);
        Assert.assertEquals(date, deserializedDate);
    }

    @Test
    public void testSerializeDateWithCustomFormat() throws JsonGenerationException, JsonMappingException, IOException {
        DateFormat customFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.US);
        customFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        DateTimeSerializerBase<Date> customSerializer = new DateTimeSerializerBase<Date>(Date.class, false, customFormat);

        Date date = new Date();
        JsonWriter jsonWriter = objectMapper.getFactory().createJsonWriter(new java.io.FileOutputStream("output.json"));
        customSerializer.serialize(date, jsonWriter, new SerializerProvider());
        jsonWriter.close();

        ObjectMapper mapper = new ObjectMapper();
        Date deserializedDate = mapper.readValue(new java.io.File("output.json"), Date.class);
        Assert.assertEquals(date, deserializedDate);
    }

    @Test(expected = JsonMappingException.class)
    public void testSerializeNullDate() throws JsonGenerationException, JsonMappingException, IOException {
        DateTimeSerializerBase<Date> dateTimeSerializer = new DateTimeSerializerBase<Date>(Date.class, false, null);

        dateTimeSerializer.serialize(null, null, new SerializerProvider());
    }

    @Test
    public void testDeserializeValidDate() throws JsonGenerationException, JsonMappingException, IOException, ParseException {
        ObjectMapper objectMapper = new ObjectMapper();
        JsonParser jsonParser = objectMapper.getFactory().createParser(new java.io.File("input.json"));
        JsonNode tree = objectMapper.readTree(jsonParser);
        JsonNode node = tree.get("value");

        Date date = objectMapper.readValue(node.asText(), Date.class);
        Assert.assertNotNull(date);
    }

    @Test(expected = JsonMappingException.class)
    public void testDeserializeInvalidDateFormat() throws JsonGenerationException, JsonMappingException, IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        JsonParser jsonParser = objectMapper.getFactory().createParser(new java.io.File("input.json"));
        JsonNode tree = objectMapper.readTree(jsonParser);
        JsonNode node = tree.get("value");
        node.set("invalidFormat", "invalidFormat");

        ObjectMapper mapper = new ObjectMapper();
        mapper.readValue(node.asText(), Date.class);
    }
}