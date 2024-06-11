package com.fasterxml.jackson.databind.ser.std;

import static com.fasterxml.jackson.core.JsonParser.NumberType.INT;
import static com.fasterxml.jackson.core.JsonParser.NumberType.LONG;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;

import org.junit.Test;

public class NumberSerializersTest {

    private static final ObjectMapper mapper = new ObjectMapper();

    @Test
    public void testAddAll() throws JsonProcessingException {
        Map<String, JsonSerializer<?>> allDeserializers = new HashMap<>();
        NumberSerializers.addAll(allDeserializers);

        assertEquals(6, allDeserializers.size());
        assertEquals(Integer.class.getName(), allDeserializers.get(Integer.class.getName()).getClass().getName());
        assertEquals(Integer.TYPE.getName(), allDeserializers.get(Integer.TYPE.getName()).getClass().getName());
        assertEquals(Long.class.getName(), allDeserializers.get(Long.class.getName()).getClass().getName());
        assertEquals(Long.TYPE.getName(), allDeserializers.get(Long.TYPE.getName()).getClass().getName());
        assertNotNull(allDeserializers.get(Byte.class.getName()));
        assertNotNull(allDeserializers.get(Byte.TYPE.getName()));
        assertNotNull(allDeserializers.get(Short.class.getName()));
        assertNotNull(allDeserializers.get(Short.TYPE.getName()));
        assertNotNull(allDeserializers.get(Double.class.getName()));
        assertNotNull(allDeserializers.get(Double.TYPE.getName()));
        assertNotNull(allDeserializers.get(Float.class.getName()));
        assertNotNull(allDeserializers.get(Float.TYPE.getName()));
    }

    @Test
    public void testIntegerSerializer() throws JsonProcessingException {
        Integer value = 123;
        String json = mapper.writeValueAsString(value);
        assertEquals("123", json);

        JsonParser parser = mapper.getFactory().createParser(json);
        JsonToken token = parser.nextToken();
        assertEquals(JsonToken.VALUE_NUMBER_INT, token);
        assertEquals(value, parser.getValueAsInt());
    }

    @Test
    public void testIntegerSerializer_withType() throws JsonProcessingException {
        Integer value = 123;
        String json = mapper.writeValueAsString(value);

        JsonParser parser = mapper.getFactory().createParser(json);
        JsonToken token = parser.nextToken();
        assertEquals(JsonToken.START_OBJECT, token);
        Object readValue = parser.readValueAs(Map.class);
        JsonNode typeNode = ((Map) readValue).get("@type");
        assertEquals("number", typeNode.asText());
        JsonNode valueNode = ((Map) readValue).get("value");
        assertEquals("123", valueNode.asText());

        JsonSerializer<Integer> serializer = mapper.findValueSerializer(Integer.class);
        JsonGenerator gen = mapper.getFactory().createGenerator(new ByteArrayOutputStream(), null);
        serializer.serialize(value, gen, mapper.getSerializerProvider());
        assertEquals("123", gen.getOutputContent().toString());
    }

    @Test
    public void testIntLikeSerializer() throws JsonProcessingException {
        Byte value = 127;
        String json = mapper.writeValueAsString(value);
        assertEquals("127", json);

        JsonParser parser = mapper.getFactory().createParser(json);
        JsonToken token = parser.nextToken();
        assertEquals(JsonToken.VALUE_NUMBER_INT, token);
        assertEquals(value, parser.getValueAsInt());
    }

    @Test
    public void testLongSerializer() throws JsonProcessingException {
        Long value = 123L;
        String json = mapper.writeValueAsString(value);
        assertEquals("123", json);

        JsonParser parser = mapper.getFactory().createParser(json);
        JsonToken token = parser.nextToken();
        assertEquals(JsonToken.VALUE_NUMBER_LONG, token);
        assertEquals(value, parser.getValueAsLong());
    }

    @Test
    public void testFloatSerializer() throws JsonProcessingException {
        Float value = 123.45f;
        String json = mapper.writeValueAsString(value);
        assertEquals("123.45", json);

        JsonParser parser = mapper.getFactory().createParser(json);
        JsonToken token = parser.nextToken();
        assertEquals(JsonToken.VALUE_NUMBER_FLOAT, token);
        assertEquals(value, parser.getValueAsFloat());
    }

    @Test
    public void testDoubleSerializer() throws JsonProcessingException {
        Double value = 123.45;
        String json = mapper.writeValueAsString(value);
        assertEquals("123.45", json);

        JsonParser parser = mapper.getFactory().createParser(json);
        JsonToken token = parser.nextToken();
        assertEquals(JsonToken.VALUE_NUMBER_FLOAT, token);
        assertEquals(value, parser.getValueAsDouble());
    }

    @Test
    public void testDoubleSerializer_withCustomJsonFormat() throws JsonProcessingException {
        @JsonFormat(shape = JsonFormat.Shape.STRING)
        static class MyData {
            private Double value;

            public Double getValue() {
                return value;
            }

            public void setValue(Double value) {
                this.value = value;
            }
        }

        MyData data = new MyData();
        data.setValue(123.45);
        String json = mapper.writeValueAsString(data);
        assertEquals("123.45", json);

        JsonParser parser = mapper.getFactory().createParser(json);
        JsonToken token = parser.nextToken();
        assertEquals(JsonToken.START_OBJECT, token);
        Object readValue = parser.readValueAs(MyData.class);
        assertEquals(data.getValue(), readValue.getValue());
    }
}