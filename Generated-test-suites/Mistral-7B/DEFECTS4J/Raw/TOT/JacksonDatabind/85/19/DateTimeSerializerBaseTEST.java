import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.*;
import org.junit.Test;

import static org.junit.Assert.*;

public class DateTimeSerializerBaseTest {

    @Test
    public void testSerializerBaseWithTimestamp() throws JsonProcessingException {
        Date date = new Date(1616434221000L); // Thu Jan 21 15:37:01 GMT+01:00 2021
        ObjectMapper mapper = new ObjectMapper();
        DateTimeSerializerBase<Date> serializer = mapper.findValueSerializer(Date.class);
        JsonGenerator gen = mapper.getPolymorphicSerializer(Date.class).getSerializerProvider().getJsonGenerator();

        serializer.serialize(date, gen, null);
        String json = gen.getOutputFactory().writeValueAsString(gen.getBuffer());
        assertEquals("1616434221000", json);

        JsonParser parser = mapper.getFactory().createParser(json);
        Date deserializedDate = mapper.readValue(parser, Date.class);
        assertEquals(date, deserializedDate);
    }

    @Test
    public void testSerializerBaseWithCustomFormat() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        DateTimeSerializerBase<Date> serializer = mapper.findValueSerializer(Date.class);
        serializer = serializer.withFormat(Boolean.FALSE, new SimpleDateFormat("dd-MM-yyyy"));

        Date date = new Date();
        JsonGenerator gen = mapper.getPolymorphicSerializer(Date.class).getSerializerProvider().getJsonGenerator();

        serializer.serialize(date, gen, null);
        String json = gen.getOutputFactory().writeValueAsString(gen.getBuffer());
        assertEquals("22-12-2021", json);

        JsonParser parser = mapper.getFactory().createParser(json);
        Date deserializedDate = mapper.readValue(parser, Date.class);
        assertEquals(date, deserializedDate);
    }

    @Test(expected = JsonMappingException.class)
    public void testSerializerBaseWithInvalidFormat() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        DateTimeSerializerBase<Date> serializer = mapper.findValueSerializer(Date.class);
        serializer = serializer.withFormat(Boolean.FALSE, new SimpleDateFormat("dd-MM-2021"));

        Date date = new Date();
        JsonGenerator gen = mapper.getPolymorphicSerializer(Date.class).getSerializerProvider().getJsonGenerator();

        serializer.serialize(date, gen, null);
    }

    @Test
    public void testSerializerBaseWithNullValue() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        DateTimeSerializerBase<Date> serializer = mapper.findValueSerializer(Date.class);

        JsonGenerator gen = mapper.getPolymorphicSerializer(Date.class).getSerializerProvider().getJsonGenerator();

        serializer.serialize(null, gen, null);
        String json = gen.getOutputFactory().writeValueAsString(gen.getBuffer());
        assertEquals("null", json);

        JsonParser parser = mapper.getFactory().createParser(json);
        Date deserializedDate = mapper.readValue(parser, Date.class);
        assertNull(deserializedDate);
    }

    @Test
    public void testSerializerBaseWithEmptyValue() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        DateTimeSerializerBase<Date> serializer = mapper.findValueSerializer(Date.class);

        JsonGenerator gen = mapper.getPolymorphicSerializer(Date.class).getSerializerProvider().getJsonGenerator();

        serializer.serialize(new Date(), gen, null);
        gen.writeNull();
        String json = gen.getOutputFactory().writeValueAsString(gen.getBuffer());
        assertEquals("null", json);

        JsonParser parser = mapper.getFactory().createParser(json);
        Date deserializedDate = mapper.readValue(parser, Date.class);
        assertNull(deserializedDate);
    }

    @Test
    public void testSerializerBaseWithEmptyValueInCustomFormat() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        DateTimeSerializerBase<Date> serializer = mapper.findValueSerializer(Date.class);
        serializer = serializer.withFormat(Boolean.FALSE, new SimpleDateFormat("dd-MM-yyyy"));

        Date date = new Date();
        JsonGenerator gen = mapper.getPolymorphicSerializer(Date.class).getSerializerProvider().getJsonGenerator();

        serializer.serialize(date, gen, null);
        gen.writeNull();
        String json = gen.getOutputFactory().writeValueAsString(gen.getBuffer());
        assertEquals("", json);

        JsonParser parser = mapper.getFactory().createParser(json);
        Date deserializedDate = mapper.readValue(parser, Date.class);
        assertNull(deserializedDate);
    }
}