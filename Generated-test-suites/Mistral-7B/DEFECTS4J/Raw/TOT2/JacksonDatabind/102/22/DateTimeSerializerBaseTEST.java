import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.json.JsonMapperFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class DateTimeSerializerBaseTest {

    private ObjectMapper objectMapper;

    @Before
    public void setup() {
        objectMapper = JsonMapperFactory.builder()
                .build(); // Creating ObjectMapper instance
    }

    @Test
    public void testSerializingDateWithDefaultSettings() throws JsonMappingException, JsonGenerationException, IOException {
        DateTimeSerializerBase<Date> dateSerializer = new DateTimeSerializerBase<Date>(Date.class, false, null);
        Date date = new Date();

        JsonGenerator jsonGenerator = objectMapper.getFactory().createGenerator(System.out);
        dateSerializer.serialize(date, jsonGenerator, new SerializerProvider());
        jsonGenerator.flush();
        jsonGenerator.close();

        // Verifying the output is as expected
        ObjectNode jsonNode = objectMapper.readValue(System.in, ObjectNode.class);
        Assert.assertEquals("\"2022-12-31T12:34:56.789Z\"", jsonNode.toString());
    }

    @Test
    public void testSerializingDateWithCustomFormat() throws JsonMappingException, JsonGenerationException, IOException, ParseException {
        DateTimeSerializerBase<Date> dateSerializer = new DateTimeSerializerBase<Date>(Date.class, false, new SimpleDateFormat("dd-MM-yyyy HH:mm:ss.SSS zzz"));
        Date date = new Date();

        JsonGenerator jsonGenerator = objectMapper.getFactory().createGenerator(System.out);
        dateSerializer.serialize(date, jsonGenerator, new SerializerProvider());
        jsonGenerator.flush();
        jsonGenerator.close();

        ObjectMapper jsonMapper = new ObjectMapper();
        ObjectNode jsonNode = jsonMapper.readValue(System.in, ObjectNode.class);
        Assert.assertEquals("\"dd-MM-yyyy HH:mm:ss.SSS zzz\"", jsonNode.path("$[0]").asText());
    }

    @Test
    public void testSerializingDateWithCustomLocaleAndTimeZone() throws JsonMappingException, JsonGenerationException, IOException {
        DateTimeSerializerBase<Date> dateSerializer = new DateTimeSerializerBase<Date>(Date.class, false, new SimpleDateFormat("dd-MM-yyyy HH:mm:ss.SSS"));
        Date date = new Date();
        TimeZone timeZone = TimeZone.getTimeZone("Asia/Kolkata");
        Locale locale = new Locale("en", "IN");
        dateSerializer._reusedCustomFormat.set(new SimpleDateFormat("dd-MM-yyyy HH:mm:ss.SSS", locale));
        dateSerializer._reusedCustomFormat.get().setTimeZone(timeZone);

        JsonGenerator jsonGenerator = objectMapper.getFactory().createGenerator(System.out);
        dateSerializer.serialize(date, jsonGenerator, new SerializerProvider());
        jsonGenerator.flush();
        jsonGenerator.close();

        ObjectMapper jsonMapper = new ObjectMapper();
        ObjectNode jsonNode = jsonMapper.readValue(System.in, ObjectNode.class);
        Assert.assertEquals("\"dd-MM-yyyy HH:mm:ss.SSS\"", jsonNode.path("$[0]").asText());
        Assert.assertEquals("Asia/Kolkata", jsonNode.path("$[0]/timezone").asText());
        Assert.assertEquals("IN", jsonNode.path("$[0]/locale").asText());
    }

    @Test
    public void testSerializingNullDate() throws JsonMappingException, JsonGenerationException, IOException {
        DateTimeSerializerBase<Date> dateSerializer = new DateTimeSerializerBase<Date>(Date.class, false, null);

        JsonGenerator jsonGenerator = objectMapper.getFactory().createGenerator(System.out);
        dateSerializer.serialize(null, jsonGenerator, new SerializerProvider());
        jsonGenerator.flush();
        jsonGenerator.close();

        ObjectNode jsonNode = objectMapper.readValue(System.in, ObjectNode.class);
        Assert.assertEquals("null", jsonNode.toString());
    }

    @Test(expected = JsonMappingException.class)
    public void testSerializingDateWithInvalidCustomFormat() throws JsonMappingException, JsonGenerationException, IOException, ParseException {
        DateTimeSerializerBase<Date> dateSerializer = new DateTimeSerializerBase<Date>(Date.class, false, new SimpleDateFormat("dd-M-yyyy HH:mm:ss.SSS"));
        Date date = new Date();

        JsonGenerator jsonGenerator = objectMapper.getFactory().createGenerator(System.out);
        dateSerializer.serialize(date, jsonGenerator, new SerializerProvider());
        jsonGenerator.flush();
        jsonGenerator.close();
    }

    @Test(expected = JsonMappingException.class)
    public void testSerializingDateWithNullCustomFormat() throws JsonMappingException, JsonGenerationException, IOException {
        DateTimeSerializerBase<Date> dateSerializer = new DateTimeSerializerBase<Date>(Date.class, false, null);

        JsonGenerator jsonGenerator = objectMapper.getFactory().createGenerator(System.out);
        dateSerializer.serialize(new Date(), jsonGenerator, new SerializerProvider());
        jsonGenerator.flush();
        jsonGenerator.close();
    }

    @Test(expected = JsonMappingException.class)
    public void testSerializingDateWithTimestamp() throws JsonMappingException, JsonGenerationException, IOException {
        DateTimeSerializerBase<Date> dateSerializer = new DateTimeSerializerBase<Date>(Date.class, true, null);
        Date date = new Date();

        JsonGenerator jsonGenerator = objectMapper.getFactory().createGenerator(System.out);
        dateSerializer.serialize(date, jsonGenerator, new SerializerProvider());
        jsonGenerator.flush();
        jsonGenerator.close();
    }
}