package com.fasterxml.jackson.databind.ser.std;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.node.ObjectNode;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class JsonValueSerializerTest {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    static {
        SimpleModule module = new SimpleModule();
        module.addSerializer(String.class, new JsonValueSerializer(null, null, null, false));
        MAPPER.registerModule(module);
    }

    @Test
    public void testJsonValueSerializerBasic() throws JsonProcessingException {
        String input = "{\"value\": 1}";
        JsonNode node = MAPPER.readTree(input);
        Object value = node.path("value").asInt();

        JsonSerializer<Object> serializer = MAPPER.findValueSerializer(Integer.class, null);
        Assertions.assertNotNull(serializer);

        JsonGenerator gen = MAPPER.getFactory().createGenerator(System.out);
        ObjectMapper mapper = new ObjectMapper();

        JsonValueSerializer jsonValueSerializer = new JsonValueSerializer(null, null, serializer, false);
        jsonValueSerializer.serialize(new Integer(1), gen, MAPPER.getSerializerProvider());

        gen.flush();
        String output = gen.getOutputStream().toString();
        Assertions.assertEquals(input, output);
    }

    @Test
    public void testJsonValueSerializerNull() throws JsonProcessingException {
        String input = "{\"value\": null}";
        JsonNode node = MAPPER.readTree(input);

        JsonSerializer<Object> serializer = MAPPER.findValueSerializer(String.class, null);
        Assertions.assertNotNull(serializer);

        JsonValueSerializer jsonValueSerializer = new JsonValueSerializer(null, null, serializer, false);
        ObjectMapper mapper = new ObjectMapper();
        JsonGenerator gen = mapper.getFactory().createGenerator(System.out);

        jsonValueSerializer.serialize(null, gen, MAPPER.getSerializerProvider());

        gen.flush();
        String output = gen.getOutputStream().toString();
        Assertions.assertEquals(input, output);
    }

    @Test
    public void testJsonValueSerializerException() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        JsonValueSerializer jsonValueSerializer = new JsonValueSerializer(null, null, null, false);

        Assertions.assertThrows(JsonMappingException.class, () -> {
            JsonGenerator gen = mapper.getFactory().createGenerator(System.out);
            jsonValueSerializer.serialize(new Object(), gen, mapper.getSerializerProvider());
        });
    }

    @Test
    public void testJsonValueSerializerEnum() throws JsonProcessingException {
        String input = "{\"value\": \"RED\"}";
        JsonNode node = MAPPER.readTree(input);
        Enum value = Enum.valueOf("MyEnum", node.path("value").asText());

        JsonSerializer<Object> serializer = MAPPER.findValueSerializer(Enum.class, null);
        Assertions.assertNotNull(serializer);

        JsonValueSerializer jsonValueSerializer = new JsonValueSerializer(null, null, serializer, false);
        ObjectMapper mapper = new ObjectMapper();
        JsonGenerator gen = mapper.getFactory().createGenerator(System.out);

        jsonValueSerializer.serialize(value, gen, mapper.getSerializerProvider());

        gen.flush();
        String output = gen.getOutputStream().toString();
        Assertions.assertEquals(input, output);
    }
}