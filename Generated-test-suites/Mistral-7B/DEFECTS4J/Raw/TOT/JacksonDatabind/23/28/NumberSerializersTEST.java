import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import org.junit.Test;

import static org.junit.Assert.*;

public class NumberSerializersTest {

    @Test
    public void testIntegerSerializer() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        IntegerSerializer integerSerializer = mapper.findValue("1", IntegerSerializer.class);
        JsonNode node = mapper.treeParser().readValue(new StringReader("1"), Integer.class);

        // Test serialization
        JsonGenerator gen = new JsonFactory().createGenerator(new StringWriter());
        integerSerializer.serialize(1, gen, new SerializerProvider());
        String serialized = gen.getOutputState().getString();
        JsonNode expected = node;
        assertEquals("Expected serialized integer to match deserialized one", expected, new ObjectMapper().readTree(serialized));

        // Test deserialization
        int deserialized = mapper.readValue(serialized, Integer.class);
        assertEquals("Expected deserialized integer to match original one", 1, deserialized);

        // Test edge cases
        jsonRoundTripTest(mapper, "1234567890", IntegerSerializer.class);
        jsonRoundTripTest(mapper, "-1234567890", IntegerSerializer.class);
        jsonRoundTripTest(mapper, "0", IntegerSerializer.class);
        jsonRoundTripTest(mapper, "1234567890123", IntegerSerializer.class);

        // Test error scenarios
        try {
            mapper.readValue("hello", IntegerSerializer.class);
            fail("Expected JsonMappingException for non-number string input");
        } catch (JsonMappingException e) {
            // Expected
        }

        try {
            mapper.readValue("1.5", IntegerSerializer.class);
            fail("Expected JsonMappingException for non-integer float input");
        } catch (JsonMappingException e) {
            // Expected
        }
    }

    @Test
    public void testIntLikeSerializer() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        IntLikeSerializer intLikeSerializer = mapper.findValue("1", IntLikeSerializer.class);
        JsonNode node = mapper.treeParser().readValue(new StringReader("1"), Number.class);

        // Test serialization
        JsonGenerator gen = new JsonFactory().createGenerator(new StringWriter());
        intLikeSerializer.serialize(1, gen, new SerializerProvider());
        String serialized = gen.getOutputState().getString();
        JsonNode expected = node;
        assertEquals("Expected serialized number to match deserialized one", expected, new ObjectMapper().readTree(serialized));

        // Test deserialization
        Number deserialized = mapper.readValue(serialized, Number.class);
        assertEquals("Expected deserialized number to match original one", 1, deserialized.intValue());

        // Test edge cases
        jsonRoundTripTest(mapper, "1234567890", IntLikeSerializer.class);
        jsonRoundTripTest(mapper, "-1234567890", IntLikeSerializer.class);
        jsonRoundTripTest(mapper, "0", IntLikeSerializer.class);
        jsonRoundTripTest(mapper, "1234567890123", IntLikeSerializer.class);
    }

    @Test
    public void testLongSerializer() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        LongSerializer longSerializer = mapper.findValue("1", LongSerializer.class);
        JsonNode node = mapper.treeParser().readValue(new StringReader("1"), Long.class);

        // Test serialization
        JsonGenerator gen = new JsonFactory().createGenerator(new StringWriter());
        longSerializer.serialize(1L, gen, new SerializerProvider());
        String serialized = gen.getOutputState().getString();
        JsonNode expected = node;
        assertEquals("Expected serialized long to match deserialized one", expected, new ObjectMapper().readTree(serialized));

        // Test deserialization
        long deserialized = mapper.readValue(serialized, Long.class);
        assertEquals("Expected deserialized long to match original one", 1, deserialized);

        // Test edge cases
        jsonRoundTripTest(mapper, "12345678901234567890", LongSerializer.class);
        jsonRoundTripTest(mapper, "-12345678901234567890", LongSerializer.class);
        jsonRoundTripTest(mapper, "0", LongSerializer.class);
    }

    @Test
    public void testShortSerializer() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        ShortSerializer shortSerializer = mapper.findValue("1", ShortSerializer.class);
        JsonNode node = mapper.treeParser().readValue(new StringReader("1"), Short.class);

        // Test serialization
        JsonGenerator gen = new JsonFactory().createGenerator(new StringWriter());
        shortSerializer.serialize((short) 1, gen, new SerializerProvider());
        String serialized = gen.getOutputState().getString();
        JsonNode expected = node;
        assertEquals("Expected serialized short to match deserialized one", expected, new ObjectMapper().readTree(serialized));

        // Test deserialization
        short deserialized = mapper.readValue(serialized, Short.class);
        assertEquals("Expected deserialized short to match original one", (short) 1, deserialized);

        // Test edge cases
        jsonRoundTripTest(mapper, "12345", ShortSerializer.class);
        jsonRoundTripTest(mapper, "-12345", ShortSerializer.class);
        jsonRoundTripTest(mapper, "0", ShortSerializer.class);
    }

    @Test
    public void testFloatSerializer() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        FloatSerializer floatSerializer = mapper.findValue("1.0", FloatSerializer.class);
        JsonNode node = mapper.treeParser().readValue(new StringReader("1.0"), Float.class);

        // Test serialization
        JsonGenerator gen = new JsonFactory().createGenerator(new StringWriter());
        floatSerializer.serialize(1.0f, gen, new SerializerProvider());
        String serialized = gen.getOutputState().getString();
        JsonNode expected = node;
        assertEquals("Expected serialized float to match deserialized one", expected, new ObjectMapper().readTree(serialized));

        // Test deserialization
        float deserialized = mapper.readValue(serialized, Float.class);
        assertEquals("Expected deserialized float to match original one", 1.0f, deserialized);

        // Test edge cases
        jsonRoundTripTest(mapper, "1234567890.1234567890", FloatSerializer.class);
        jsonRoundTripTest(mapper, "-1234567890.1234567890", FloatSerializer.class);
        jsonRoundTripTest(mapper, "0.0", FloatSerializer.class);
    }

    @Test
    public void testDoubleSerializer() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        DoubleSerializer doubleSerializer = mapper.findValue("1.0", DoubleSerializer.class);
        JsonNode node = mapper.treeParser().readValue(new StringReader("1.0"), Double.class);

        // Test serialization
        JsonGenerator gen = new JsonFactory().createGenerator(new StringWriter());
        doubleSerializer.serialize(1.0, gen, new SerializerProvider());
        String serialized = gen.getOutputState().getString();
        JsonNode expected = node;
        assertEquals("Expected serialized double to match deserialized one", expected, new ObjectMapper().readTree(serialized));

        // Test deserialization
        double deserialized = mapper.readValue(serialized, Double.class);
        assertEquals("Expected deserialized double to match original one", 1.0, deserialized);

        // Test edge cases
        jsonRoundTripTest(mapper, "12345678901234567890.1234567890", DoubleSerializer.class);
        jsonRoundTripTest(mapper, "-12345678901234567890.1234567890", DoubleSerializer.class);
        jsonRoundTripTest(mapper, "0.0", DoubleSerializer.class);
    }

    private void jsonRoundTripTest(ObjectMapper mapper, String json, Class<? extends Base<?>> serializerClass) throws JsonProcessingException {
        JsonNode node = mapper.readTree(json);
        JsonSerializer<?> serializer = mapper.findValue(json, serializerClass);
        JsonNode deserializedNode = mapper.treeParser().readValue(new StringReader(serializer.getValueAsString()), serializer.handledType());
        assertEquals("Expected deserialized JSON to match original JSON", node, deserializedNode);
    }
}