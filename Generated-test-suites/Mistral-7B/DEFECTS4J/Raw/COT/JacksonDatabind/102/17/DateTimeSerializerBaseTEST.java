import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.json.JsonMapperBuilder;
import com.fasterxml.jackson.databind.util.StdDateFormat;
import com.fasterxml.jackson.databind.util.TestBase;

public class DateTimeSerializerBaseTest extends TestBase {

    private ObjectMapper objectMapper;
    private DateTimeSerializerBase<Date> dateTimeSerializer;

    @Before
    public void setup() throws Exception {
        objectMapper = new JsonMapperBuilder()
                .build();
        dateTimeSerializer = new DateTimeSerializerBase<Date>(Date.class, false, null);
    }

    @Test
    public void testDefaultSerialization() throws JsonProcessingException {
        Date date = new Date();
        JsonSerializer<Date> jsonSerializer = dateTimeSerializer;

        String serializedDate = jsonSerializer.serializeValue(date, objectMapper.getSerializationConfig(), objectMapper);
        assertEquals("expected valid serialized date", new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ").format(date), serializedDate);
    }

    @Test
    public void testSerializationWithFormat() throws JsonProcessingException {
        DateFormat customFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss z", Locale.ENGLISH);
        DateTimeSerializerBase<Date> customSerializer = dateTimeSerializer.withFormat(false, customFormat);

        Date date = new Date();
        String serializedDate = customSerializer.serializeValue(date, objectMapper.getSerializationConfig(), objectMapper).getValueAsString();
        assertEquals("expected serialized date with custom format", customFormat.format(date), serializedDate);
    }

    @Test
    public void testSerializationAsTimestamp() throws JsonProcessingException {
        DateTimeSerializerBase<Date> timestampSerializer = dateTimeSerializer.withFormat(true, null);

        Date date = new Date();
        String serializedDate = timestampSerializer.serializeValue(date, objectMapper.getSerializationConfig(), objectMapper).getValueAsString();
        assertTrue("expected serialized date as timestamp", serializedDate.matches("-?[0-9]+"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSerializationWithNullProvider() throws JsonProcessingException {
        DateTimeSerializerBase<Date> serializer = dateTimeSerializer;
        serializer.serializeValue(new Date(), null, null);
    }

    @Test
    public void testSerializationWithCustomFormatAndLocale() throws JsonProcessingException, ParseException {
        DateTimeSerializerBase<Date> customSerializer = dateTimeSerializer.withFormat(false, new SimpleDateFormat("dd-MM-yyyy HH:mm:ss z", new Locale("es", "ES")));

        Date date = new Date();
        String serializedDate = customSerializer.serializeValue(date, objectMapper.getSerializationConfig(), objectMapper).getValueAsString();
        assertEquals("expected serialized date with custom format and locale", new SimpleDateFormat("dd-MM-yyyy HH:mm:ss z", new Locale("es", "ES")).format(date), serializedDate);
    }

    @Test
    public void testSerializationWithCustomFormatAndTimeZone() throws JsonProcessingException, ParseException {
        DateTimeSerializerBase<Date> customSerializer = dateTimeSerializer.withFormat(false, new SimpleDateFormat("dd-MM-yyyy HH:mm:ss z", Locale.ENGLISH));
        TimeZone tz = TimeZone.getTimeZone("UTC");
        customSerializer._reusedCustomFormat.set(new SimpleDateFormat("dd-MM-yyyy HH:mm:ss z", Locale.ENGLISH));
        customSerializer._reusedCustomFormat.get().setTimeZone(tz);

        Date date = new Date();
        String serializedDate = customSerializer.serializeValue(date, objectMapper.getSerializationConfig(), objectMapper).getValueAsString();
        assertEquals("expected serialized date with custom format and time zone", new SimpleDateFormat("dd-MM-yyyy HH:mm:ss z", Locale.ENGLISH).format(date), serializedDate);
    }

    @Test(expected = JsonMappingException.class)
    public void testSerializationWithInvalidFormat() throws JsonProcessingException {
        DateTimeSerializerBase<Date> customSerializer = dateTimeSerializer.withFormat(false, new StdDateFormat("%d %B %Y %H:%M:%S %p"));

        Date date = new Date();
        customSerializer.serializeValue(date, objectMapper.getSerializationConfig(), objectMapper);
    }

    @Test
    public void testSerializationWithNullDate() throws JsonProcessingException {
        DateTimeSerializerBase<Date> serializer = dateTimeSerializer;
        assertNull("expected null when serializing null date", serializer.serializeValue(null, objectMapper.getSerializationConfig(), objectMapper));
    }

    @Test
    public void testDeserialization() throws JsonProcessingException {
        String jsonDate = "\"2023-03-27T08:30:00.000Z\"";
        JsonParser jsonParser = objectMapper.getFactory().createParser(jsonDate);

        JsonDeserializer<Date> jsonDeserializer = dateTimeSerializer;
        Date deserializedDate = jsonDeserializer.deserialize(jsonParser, Date.class, objectMapper.getDeserializationConfig());

        assertEquals("expected deserialized date", new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ").parse(jsonDate), deserializedDate);
    }

    @Test
    public void testDeserializationWithFormat() throws JsonProcessingException {
        String jsonDate = "\"12-12-2023 12:12:12 EDT\"";
        JsonParser jsonParser = objectMapper.getFactory().createParser(jsonDate);

        DateTimeSerializerBase<Date> customDeserializer = dateTimeSerializer;
        customDeserializer._customFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss z", Locale.ENGLISH);
        JsonDeserializer<Date> jsonDeserializer = customDeserializer;

        Date deserializedDate = jsonDeserializer.deserialize(jsonParser, Date.class, objectMapper.getDeserializationConfig());

        assertEquals("expected deserialized date with custom format", new SimpleDateFormat("dd-MM-yyyy HH:mm:ss z", Locale.ENGLISH).parse(jsonDate), deserializedDate);
    }

    @Test(expected = JsonMappingException.class)
    public void testDeserializationWithInvalidFormat() throws JsonProcessingException {
        String jsonDate = "\"12 %B 2023 %H:%M:%S %p\"";
        JsonParser jsonParser = objectMapper.getFactory().createParser(jsonDate);

        DateTimeSerializerBase<Date> customDeserializer = dateTimeSerializer;
        JsonDeserializer<Date> jsonDeserializer = customDeserializer;

        jsonDeserializer.deserialize(jsonParser, Date.class, objectMapper.getDeserializationConfig());
    }
}