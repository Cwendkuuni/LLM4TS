import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class NumberSerializersTest {

    @Test
    public void testIntegerSerializer() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Integer value = 123;
        String json = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(value);
        JsonNode rootNode = mapper.readTree(json);
        JsonNode numberNode = rootNode.get(0);
        assertEquals("123", numberNode.asText());

        JsonParser parser = mapper.getFactory().createParser(json);
        assertEquals(value.intValue(), parser.readValueAs(Integer.class));
    }

    @Test
    public void testIntegerSerializerWithType() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Integer value = 123;
        String json = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(value);

        JsonNode rootNode = mapper.readTree(json);
        JsonSerializer<Integer> serializer = mapper.findValueSerializer(Integer.class, null);
        JsonNode serializedNode = serializer.serializeWithType(value, null, mapper.getSerializerProvider(), mapper.getTypeSerializer());
        assertEquals(rootNode.toString(), serializedNode.toString());
    }

    @Test
    public void testIntLikeSerializer() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Number value = 123;
        String json = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(value);
        JsonNode rootNode = mapper.readTree(json);
        JsonNode numberNode = rootNode.get(0);
        assertEquals("123", numberNode.asText());

        JsonParser parser = mapper.getFactory().createParser(json);
        assertEquals(value.intValue(), parser.readValueAs(Integer.class));
    }

    @Test
    public void testIntLikeSerializerWithType() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Number value = 123;
        String json = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(value);

        JsonNode rootNode = mapper.readTree(json);
        JsonSerializer<Number> serializer = mapper.findValueSerializer(Number.class, null);
        JsonNode serializedNode = serializer.serializeWithType(value, null, mapper.getSerializerProvider(), mapper.getTypeSerializer());
        assertEquals(rootNode.toString(), serializedNode.toString());
    }

    @Test
    public void testLongSerializer() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Long value = 1234567890123L;
        String json = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(value);
        JsonNode rootNode = mapper.readTree(json);
        JsonNode numberNode = rootNode.get(0);
        assertEquals("1234567890123", numberNode.asText());

        JsonParser parser = mapper.getFactory().createParser(json);
        assertEquals(value.longValue(), parser.readValueAs(Long.class));
    }

    @Test
    public void testLongSerializerWithType() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Long value = 1234567890123L;
        String json = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(value);

        JsonNode rootNode = mapper.readTree(json);
        JsonSerializer<Long> serializer = mapper.findValueSerializer(Long.class, null);
        JsonNode serializedNode = serializer.serializeWithType(value, null, mapper.getSerializerProvider(), mapper.getTypeSerializer());
        assertEquals(rootNode.toString(), serializedNode.toString());
    }

    @Test
    public void testFloatSerializer() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Float value = 1.23f;
        String json = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(value);
        JsonNode rootNode = mapper.readTree(json);
        JsonNode numberNode = rootNode.get(0);
        assertEquals("1.23", numberNode.asText());

        JsonParser parser = mapper.getFactory().createParser(json);
        assertEquals(value.floatValue(), parser.readValueAs(Float.class));
    }

    @Test
    public void testFloatSerializerWithType() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Float value = 1.23f;
        String json = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(value);

        JsonNode rootNode = mapper.readTree(json);
        JsonSerializer<Float> serializer = mapper.findValueSerializer(Float.class, null);
        JsonNode serializedNode = serializer.serializeWithType(value, null, mapper.getSerializerProvider(), mapper.getTypeSerializer());
        assertEquals(rootNode.toString(), serializedNode.toString());
    }

    @Test
    public void testDoubleSerializer() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Double value = 1.23456789;
        String json = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(value);
        JsonNode rootNode = mapper.readTree(json);
        JsonNode numberNode = rootNode.get(0);
        assertEquals("1.23456789", numberNode.asText());

        JsonParser parser = mapper.getFactory().createParser(json);
        assertEquals(value.doubleValue(), parser.readValueAs(Double.class));
    }

    @Test
    public void testDoubleSerializerWithType() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Double value = 1.23456789;
        String json = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(value);

        JsonNode rootNode = mapper.readTree(json);
        JsonSerializer<Double> serializer = mapper.findValueSerializer(Double.class, null);
        JsonNode serializedNode = serializer.serializeWithType(value, null, mapper.getSerializerProvider(), mapper.getTypeSerializer());
        assertEquals(rootNode.toString(), serializedNode.toString());
    }
}