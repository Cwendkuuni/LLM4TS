package com.fasterxml.jackson.databind.ser.std;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.json.JsonMapperFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.ser.std.DateTimeSerializerBase;
import com.fasterxml.jackson.databind.ser.std.StdDateSerializer;
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
import static org.junit.Assert.fail;

public class DateTimeSerializerBaseTest {

    private ObjectMapper objectMapper;

    @Before
    public void setUp() {
        objectMapper = JsonMapperFactory.builder()
                .withSerializerModifier(new StandardSerializerModifier())
                .build();
    }

    @Test
    public void testSimpleSerializableDateTime() throws JsonProcessingException, IOException {
        DateTimeSerializerBase<Date> serializer = new StdDateSerializer(Date.class);
        SerializerProvider provider = objectMapper.getSerializationConfig().getDefaultSerializerProvider();
        JsonGenerator gen = objectMapper.getFactory().createGenerator(new ByteArrayOutputStream(), provider.getRootValueSerializer(), 0);

        Date date = new Date();
        serializer.serialize(date, gen, provider);
        gen.flush();
        byte[] serializedData = gen.getOutputBuffer().toByteArray();
        ObjectNode deserializedData = objectMapper.readTree(new ByteArrayInputStream(serializedData));

        Date deserializedDate = objectMapper.readValue(deserializedData, Date.class);
        assertEquals(date, deserializedDate);
    }

    @Test
    public void testCustomFormatSerialization() throws JsonGenerationException, JsonProcessingException, IOException {
        DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss", Locale.US);
        DateTimeSerializerBase<Date> serializer = new DateTimeSerializerBase<Date>(Date.class, false, dateFormat) {
            @Override
            protected void _serializeAsString(Date value, JsonGenerator g, SerializerProvider provider) throws IOException {
                super._serializeAsString(value, g, provider);
                g.writeString("Custom format: ");
            }
        };
        SerializerProvider provider = objectMapper.getSerializationConfig().getDefaultSerializerProvider();

        Date date = new Date();
        JsonGenerator gen = objectMapper.getFactory().createGenerator(new ByteArrayOutputStream(), serializer, provider);

        serializer.serialize(date, gen, provider);
        gen.flush();
        byte[] serializedData = gen.getOutputBuffer().toByteArray();
        ObjectNode deserializedData = objectMapper.readTree(new ByteArrayInputStream(serializedData));
        assertEquals("Custom format: ", deserializedData.get(0).asText());

        Date deserializedDate = objectMapper.readValue(deserializedData, Date.class);
        assertEquals(date, deserializedDate);
    }

    @Test
    public void testCustomFormatDeserialization() throws JsonProcessingException, IOException {
        DateTimeSerializerBase<Date> serializer = new StdDateSerializer(Date.class);
        SerializerProvider provider = objectMapper.getSerializationConfig().getDefaultSerializerProvider();

        ObjectNode inputNode = ObjectMapper.FactoryBuilder.jsonMapperFactory(objectMapper)
                .getFactory()
                .createObjectParser(new ByteArrayInputStream(new byte[]{"{\"date\":\"01-01-2022 12:01:12\"}"}))
                .readValueAs(ObjectNode.class);

        Date date = serializer.deserialize(inputNode, provider.getInputFactory(), provider);

        SimpleDateFormat expectedDateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        Date expectedDate = expectedDateFormat.parse("01-01-2022 12:01:12");
        assertEquals(expectedDate, date);
    }

    @Test(expected = JsonMappingException.class)
    public void testCustomFormatDeserializationWithInvalidFormat() throws JsonProcessingException, IOException {
        DateTimeSerializerBase<Date> serializer = new StdDateSerializer(Date.class);
        SerializerProvider provider = objectMapper.getSerializationConfig().getDefaultSerializerProvider();

        ObjectNode inputNode = ObjectMapper.FactoryBuilder.jsonMapperFactory(objectMapper)
                .getFactory()
                .createObjectParser(new ByteArrayInputStream(new byte[]{"{\"date\":\"InvalidFormat\"}"}))
                .readValueAs(ObjectNode.class);

        Date date = serializer.deserialize(inputNode, provider.getInputFactory(), provider);
        fail("Expected JsonMappingException");
    }

    @Test(expected = JsonMappingException.class)
    public void testUseTimestampSerialization() throws JsonProcessingException, IOException {
        DateTimeSerializerBase<Date> serializer = new StdDateSerializer(Date.class, true, null);
        SerializerProvider provider = objectMapper.getSerializationConfig().getDefaultSerializerProvider();

        Date date = new Date();
        JsonGenerator gen = objectMapper.getFactory().createGenerator(new ByteArrayOutputStream(), serializer, provider);

        serializer.serialize(date, gen, provider);
        gen.flush();
        byte[] serializedData = gen.getOutputBuffer().toByteArray();
        ObjectNode deserializedData = objectMapper.readTree(new ByteArrayInputStream(serializedData));

        long timestamp = deserializedData.get(0).longValue();
        Date deserializedDate = new Date(timestamp);
        Date expectedDate = new Date();
        assertEquals(expectedDate, deserializedDate);

        fail("Expected JsonMappingException");
    }
}