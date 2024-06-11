import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.*;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import static org.junit.Assert.*;

public class DateTimeSerializerBaseTest {

    private ObjectMapper objectMapper;

    @Before
    public void setup() {
        objectMapper = new ObjectMapper();
    }

    @Test
    public void testSerializeDateAsTimestamp() throws IOException {
        DateTimeSerializerBase<Date> dateSerializer = objectMapper.findValueSerializer(Date.class);
        Date date = new Date();

        JsonGenerator jsonGenerator = objectMapper.getPolymorphicSerializerProvider().findValueSerializerProvider(JsonGenerator.class)
                .getSerializer(JsonSerializer.Id.DATE);
        jsonGenerator.writeNumber(date.getTime());
        JsonSerializer<Date> serializedDate = dateSerializer.withFormat(Boolean.TRUE, null);

        byte[] serializedBytes = objectMapper.writeValueAsBytes(date);
        Date deserializedDate = objectMapper.readValue(serializedBytes, Date.class);

        assertEquals(deserializedDate, date);
        assertEquals(jsonGenerator.getValueAsString(), serializedBytes);
        assertEquals(date.getTime(), serializedDate.serializeValue(date, jsonGenerator, null));
    }

    @Test
    public void testSerializeDateAsString() throws IOException, ParseException {
        DateTimeSerializerBase<Date> dateSerializer = objectMapper.findValueSerializer(Date.class);
        Date date = new Date();

        SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        format.setTimeZone(TimeZone.getTimeZone("UTC"));

        JsonGenerator jsonGenerator = objectMapper.getPolymorphicSerializerProvider().findValueSerializerProvider(JsonGenerator.class)
                .getSerializer(JsonSerializer.Id.STRING);
        JsonSerializer<Date> serializedDate = dateSerializer.withFormat(Boolean.FALSE, format);

        byte[] serializedBytes = objectMapper.writeValueAsBytes(date);
        Date deserializedDate = objectMapper.readValue(serializedBytes, Date.class);

        assertEquals(deserializedDate, date);
        assertEquals(format.format(date), new String(serializedBytes));
        assertEquals(date.toString(), serializedDate.serializeValue(date, jsonGenerator, null));
    }

    @Test(expected = IOException.class)
    public void testSerializeDateWithInvalidFormat() throws IOException, ParseException {
        DateTimeSerializerBase<Date> dateSerializer = objectMapper.findValueSerializer(Date.class);

        SimpleDateFormat invalidFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ssa");
        JsonGenerator jsonGenerator = objectMapper.getPolymorphicSerializerProvider().findValueSerializerProvider(JsonGenerator.class)
                .getSerializer(JsonSerializer.Id.STRING);
        JsonSerializer<Date> serializedDate = dateSerializer.withFormat(Boolean.FALSE, invalidFormat);

        Date date = new Date();

        byte[] serializedBytes = objectMapper.writeValueAsBytes(date);
        assertFalse(serializedBytes.length > 0);

        serializedDate.serializeValue(date, jsonGenerator, null);
    }

    @Test(expected = IOException.class)
    public void testSerializeDateWithNullFormat() throws IOException, ParseException {
        DateTimeSerializerBase<Date> dateSerializer = objectMapper.findValueSerializer(Date.class);

        JsonGenerator jsonGenerator = objectMapper.getPolymorphicSerializerProvider().findValueSerializerProvider(JsonGenerator.class)
                .getSerializer(JsonSerializer.Id.STRING);
        JsonSerializer<Date> serializedDate = dateSerializer.withFormat(Boolean.FALSE, null);

        Date date = new Date();

        byte[] serializedBytes = objectMapper.writeValueAsBytes(date);
        assertFalse(serializedBytes.length > 0);

        serializedDate.serializeValue(date, jsonGenerator, null);
    }

    @Test
    public void testDeserializeDateAsTimestamp() throws IOException {
        DateTimeSerializerBase<Date> dateSerializer = objectMapper.findValueSerializer(Date.class);

        JsonParser jsonParser = objectMapper.getPolymorphicSerializerProvider().findValueDeserializerProvider(JsonParser.class)
                .getDeserializer(JsonDeserializer.Id.DATE_TIME);

        long timestamp = 1633026235342L;
        byte[] serializedBytes = java.util.Base64.getEncoder().encode(new byte[8]); // 8 bytes for long
        String jsonString = new String(serializedBytes) + "Z";

        Date deserializedDate = jsonParser.readValueAs(Date.class, jsonString);

        assertEquals(deserializedDate, new Date(timestamp));
        assertEquals(timestamp, dateSerializer.serializeValue(new Date(timestamp), jsonGenerator, null).longValue());
    }

    @Test
    public void testDeserializeDateAsString() throws IOException, ParseException {
        DateTimeSerializerBase<Date> dateSerializer = objectMapper.findValueSerializer(Date.class);

        SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        format.setTimeZone(TimeZone.getTimeZone("UTC"));

        JsonParser jsonParser = objectMapper.getPolymorphicSerializerProvider().findValueDeserializerProvider(JsonParser.class)
                .getDeserializer(JsonDeserializer.Id.STRING);

        String jsonString = "25-12-2021 12:00:00";

        Date deserializedDate = jsonParser.readValueAs(Date.class, jsonString);

        assertEquals(deserializedDate, new Date(format.parse(jsonString).getTime()));
        assertEquals(jsonString, dateSerializer.serializeValue(deserializedDate, jsonGenerator, null).toString());
    }

    @Test(expected = IOException.class)
    public void testDeserializeDateWithInvalidFormat() throws IOException, ParseException {
        DateTimeSerializerBase<Date> dateSerializer = objectMapper.findValueSerializer(Date.class);

        SimpleDateFormat invalidFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ssa");
        JsonParser jsonParser = objectMapper.getPolymorphicSerializerProvider().findValueDeserializerProvider(JsonParser.class)
                .getDeserializer(JsonDeserializer.Id.STRING);

        String jsonString = "25-12-2021 12:00:00a";

        dateSerializer.deserializeValue(jsonParser, null);
    }

    @Test(expected = IOException.class)
    public void testDeserializeDateWithNullFormat() throws IOException, ParseException {
        DateTimeSerializerBase<Date> dateSerializer = objectMapper.findValueSerializer(Date.class);

        JsonParser jsonParser = objectMapper.getPolymorphicSerializerProvider().findValueDeserializerProvider(JsonParser.class)
                .getDeserializer(JsonDeserializer.Id.STRING);

        String jsonString = "25-12-2021 12:00:00";

        dateSerializer.deserializeValue(jsonParser, null);
    }
}